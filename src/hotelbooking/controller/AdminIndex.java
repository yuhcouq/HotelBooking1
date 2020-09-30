package hotelbooking.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import hotelbooking.constant.Defines;
import hotelbooking.dao.RoomDao;
import hotelbooking.model.User;

@Controller
@RequestMapping("/admin")
public class AdminIndex {
	@Autowired
	private Defines defines;

	@Autowired
	private RoomDao roomDao;

	@ModelAttribute
	public void addCommonsObject(ModelMap modelMap, HttpSession session, Principal principal) {
		modelMap.addAttribute("defines", defines);
		// User user = userDao.getUser(principal.getName());
		// session.setAttribute("user", user);
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index(HttpSession session, ModelMap model) {
		if (session.getAttribute("userAdmin") == null) {
			return "redirect:/admin/error403";
		} else {
			if (session.getAttribute("userAdmin") != null) {
				User userAdmin = (User) session.getAttribute("userAdmin");
				if (userAdmin.getRole_id() == 1) {
					model.addAttribute("listRooms", roomDao.getListRooms());
					return "admin.room.index";
				} else {
					model.addAttribute("listRooms", roomDao.getAllRoomsOfHotel(userAdmin.getHotel_id()));
					return "admin.room.index";
				}
			} else {
				return "redirect:/admin/error403";
			}
		}
	}

	@RequestMapping(value = "/logout")
	public String logout(HttpSession session) {
		session.removeValue("userAdmin");
		session.removeValue("role");
		return "redirect:/public/index";
	}

	@RequestMapping("/error403")
	public String error403() {
		return "err.403";
	}
}
