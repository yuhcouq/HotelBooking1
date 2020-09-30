package hotelbooking.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import hotelbooking.model.Booking;
import hotelbooking.model.Check;
import hotelbooking.model.User;

@Repository
public class BookingDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	public List<Booking> getListBookingMoveData() {
		String sql = "SELECT b.id_booking, b.room_id, b.user_id FROM booking b WHERE status != 0 AND check_move = 0";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Booking>(Booking.class));
	}

	public int updateCheckMove() {
		String sql = "UPDATE booking SET check_move = ? WHERE check_move = ? ";
		return jdbcTemplate.update(sql, new Object[] { 1, 0 });
	}

	public int insertBooking(Booking booking, User user) {
		String sql = "INSERT INTO booking(code, hotel_id, room_id, user_id, firstname, lastname, phone, email, gender, birthday, city, address, checkin, checkout, day, total_price, paid, status, note, check_move, created_time) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,0,?,?,?)";
		return jdbcTemplate.update(sql,
				new Object[] { Booking.getCode_auto(), booking.getHotel_id(), booking.getRoom_id(), user.getId_user(),
						user.getFirstname(), user.getLastname(), user.getPhone(), user.getEmail(), user.getGender(),
						user.getBirthday(), user.getCity(), user.getAddress(), booking.getCheckin(),
						booking.getCheckout(), booking.getDay(), booking.getTotal_price(), booking.getPrepayment(),
						user.getNote(), booking.getCheck_move(), booking.getCreated_time() });
	}

	public List<Booking> getAllBooking(String date) {
		String sql = "SELECT b.code, COUNT(b.code), b.* FROM booking b  WHERE status = 0 AND created_time <= ? GROUP BY b.code HAVING COUNT(b.code) >= 1";
		return jdbcTemplate.query(sql, new Object[] { date }, new BeanPropertyRowMapper<Booking>(Booking.class));
	}

	public List<Booking> getAllBookingOfHotel(String dateDay, int hotel_id) {
		String sql = "SELECT b.code, COUNT(b.code), b.* FROM booking b WHERE status = 0 AND created_time <= ? AND hotel_id = ? GROUP BY b.code HAVING COUNT(b.code) >= 1";
		return jdbcTemplate.query(sql, new Object[] { dateDay, hotel_id },
				new BeanPropertyRowMapper<Booking>(Booking.class));
	}

	public int CheckCode(int code) {
		String sql = "SELECT COUNT(*) FROM booking WHERE code = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { code }, Integer.class);
	}

	public List<Booking> getAllBooking(int code) {
		String sql = "SELECT * FROM booking WHERE code = ?";
		return jdbcTemplate.query(sql, new Object[] { code }, new BeanPropertyRowMapper<Booking>(Booking.class));
	}

	public int Payment(int code) {
		String sql = "UPDATE booking SET status = ? WHERE code = ? ";
		return jdbcTemplate.update(sql, new Object[] { 2, code });
	}

	public int Delete(int code) {
		String sql = "DELETE FROM booking WHERE code = ? ";
		return jdbcTemplate.update(sql, new Object[] { code });
	}

	public int Cancel(int id_booking) {
		String sql = "DELETE FROM booking WHERE id_booking = ? ";
		return jdbcTemplate.update(sql, new Object[] { id_booking });
	}

	public Booking getCusromerBooking(int code) {
		String sql = "SELECt * FROM booking WHERE code = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { code },
				new BeanPropertyRowMapper<Booking>(Booking.class));
	}

	public int UpdateCustomer(Booking booking) {
		String sql = "UPDATE booking SET firstname = ?, lastname = ?, phone = ?, email = ?, gender = ?, birthday = ?, city = ?, address = ? WHERE code = ? ";
		return jdbcTemplate.update(sql,
				new Object[] { booking.getFirstname(), booking.getLastname(), booking.getPhone(), booking.getEmail(),
						booking.getGender(), booking.getBirthday(), booking.getCity(), booking.getAddress(),
						booking.getCode() });
	}

	public Booking getBooking(int id_booking) {
		String sql = "SELECt * FROM booking WHERE id_booking = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { id_booking },
				new BeanPropertyRowMapper<Booking>(Booking.class));
	}

	public int UpdateBoooking(Booking booking) {
		String sql = "UPDATE booking SET room_id = ?, checkin = ?, checkout = ?, day = ?, total_price = ?, paid = ?, note = ? WHERE id_booking = ? ";
		return jdbcTemplate.update(sql,
				new Object[] { booking.getRoom_id(), booking.getCheckin(), booking.getCheckout(), booking.getDay(),
						booking.getTotal_price(), booking.getPaid(), booking.getNote(), booking.getId_booking() });
	}

	public List<Booking> getAllBooking() {
		String sql = "SELECT b.* FROM booking b  WHERE status != 0";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<Booking>(Booking.class));
	}

	public List<Booking> getAllBookingOfHotel(int hotel_id) {
		String sql = "SELECT b.* FROM booking b WHERE status != 0 AND hotel_id = ?";
		return jdbcTemplate.query(sql, new Object[] { hotel_id }, new BeanPropertyRowMapper<Booking>(Booking.class));
	}

	public List<Booking> getAllBooking(Check check) {
		String sql = "SELECT b.* FROM booking b  WHERE status != 0 AND checkin >= ? AND checkout <= ?";
		return jdbcTemplate.query(sql, new Object[] { check.getCheckin(), check.getCheckout() },
				new BeanPropertyRowMapper<Booking>(Booking.class));
	}

	public List<Booking> getAllBookingOfHotel(Check check, int hotel_id) {
		String sql = "SELECT b.* FROM booking b  WHERE status != 0 AND checkin >= ? AND checkout <= ? AND hotel_id = ?";
		return jdbcTemplate.query(sql, new Object[] { check.getCheckin(), check.getCheckout(), hotel_id },
				new BeanPropertyRowMapper<Booking>(Booking.class));
	}

	public List<Booking> getAllBookingByRoomMax(Check check) {
		String sql = "SELECT b.room_id, COUNT(b.room_id), SUM(b.total_price) AS price, b.* FROM booking b  WHERE status != 0 AND checkin >= ? AND checkout <= ? GROUP BY b.room_id HAVING COUNT(b.room_id) >= 1 ORDER BY price DESC";
		return jdbcTemplate.query(sql, new Object[] { check.getCheckin(), check.getCheckout() },
				new BeanPropertyRowMapper<Booking>(Booking.class));
	}

	public List<Booking> getAllBookingByRoomOfHotelMax(Check check, int hotel_id) {
		String sql = "SELECT b.room_id, COUNT(b.room_id), SUM(b.total_price) AS price, b.* FROM booking b  WHERE status != 0 AND hotel_id = ? AND checkin >= ? AND checkout <= ? GROUP BY b.room_id HAVING COUNT(b.room_id) >= 1 ORDER BY price DESC";
		return jdbcTemplate.query(sql, new Object[] { hotel_id, check.getCheckin(), check.getCheckout() },
				new BeanPropertyRowMapper<Booking>(Booking.class));
	}

	public List<Booking> getAllBookingByRoomMin(Check check) {
		String sql = "SELECT b.room_id, COUNT(b.room_id), SUM(b.total_price) AS price, b.* FROM booking b  WHERE status != 0 AND checkin >= ? AND checkout <= ? GROUP BY b.room_id HAVING COUNT(b.room_id) >= 1 ORDER BY price";
		return jdbcTemplate.query(sql, new Object[] { check.getCheckin(), check.getCheckout() },
				new BeanPropertyRowMapper<Booking>(Booking.class));
	}

	public List<Booking> getAllBookingByRoomOfHotelMin(Check check, int hotel_id) {
		String sql = "SELECT b.room_id, COUNT(b.room_id), SUM(b.total_price) AS price, b.* FROM booking b  WHERE status != 0 AND hotel_id = ? AND checkin >= ? AND checkout <= ? GROUP BY b.room_id HAVING COUNT(b.room_id) >= 1 ORDER BY price";
		return jdbcTemplate.query(sql, new Object[] { hotel_id, check.getCheckin(), check.getCheckout() },
				new BeanPropertyRowMapper<Booking>(Booking.class));
	}

	public List<Booking> getAllBooking(int hotel_id, Check check) {
		String sql = "SELECT b.* FROM booking b  WHERE status != 0 AND hotel_id = ? AND checkin >= ? AND checkout <= ?";
		return jdbcTemplate.query(sql, new Object[] { hotel_id, check.getCheckin(), check.getCheckout() },
				new BeanPropertyRowMapper<Booking>(Booking.class));
	}

	public List<Booking> getAllBookingByRoomMax(int hotel_id, Check check) {
		String sql = "SELECT b.room_id, COUNT(b.room_id), SUM(b.total_price) AS price, b.* FROM booking b  WHERE status != 0 AND hotel_id = ? AND checkin >= ? AND checkout <= ? GROUP BY b.room_id HAVING COUNT(b.room_id) >= 1 ORDER BY price DESC";
		return jdbcTemplate.query(sql, new Object[] { hotel_id, check.getCheckin(), check.getCheckout() },
				new BeanPropertyRowMapper<Booking>(Booking.class));
	}

	public List<Booking> getAllBookingByRoomMin(int hotel_id, Check check) {
		String sql = "SELECT b.room_id, COUNT(b.room_id), SUM(b.total_price) AS price, b.* FROM booking b  WHERE status != 0 AND hotel_id = ? AND checkin >= ? AND checkout <= ? GROUP BY b.room_id HAVING COUNT(b.room_id) >= 1 ORDER BY price";
		return jdbcTemplate.query(sql, new Object[] { hotel_id, check.getCheckin(), check.getCheckout() },
				new BeanPropertyRowMapper<Booking>(Booking.class));
	}

	public List<Booking> getAllMyBoooking(int id_user) {
		String sql = "SELECT b.*, h.hotel_name,r.image FROM booking b INNER JOIN hotel h ON b.hotel_id = h.id_hotel INNER JOIN room r ON r.id_room = b.room_id  WHERE b.status != 1 AND b.user_id = ?";
		return jdbcTemplate.query(sql, new Object[] { id_user }, new BeanPropertyRowMapper<Booking>(Booking.class));
	}

	public int updateStatus(int id_booking) {
		String sql = "UPDATE booking SET status = 1 WHERE id_booking = ? ";
		return jdbcTemplate.update(sql, new Object[] { id_booking });
	}

}
