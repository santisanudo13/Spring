package app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;









@Controller
@RequestMapping("")
public class LoginController {

	

	/**
	 * Mapeo a direccion root
	 * @return
	 */
	@RequestMapping("/")
	public ModelAndView index() {
		return new ModelAndView("login/login");
	}

	/**
	 * Mapeo a login
	 * @return
	 */
	@RequestMapping("/login")
	public ModelAndView login() {
		return new ModelAndView("login/login");
	}
	

}
