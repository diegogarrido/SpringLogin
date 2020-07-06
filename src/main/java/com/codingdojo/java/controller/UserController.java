package com.codingdojo.java.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.codingdojo.java.model.User;
import com.codingdojo.java.service.UserService;

@Controller
public class UserController {

	@Autowired
	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/")
	public String index() {
		return "redirect:/login";
	}

	@GetMapping("/registration")
	public String registerForm() {
		return "registration";
	}

	@PostMapping(value = "/registration")
	public String registerUser(@Validated User user, Model model, HttpSession session) {
		if (user.getPassword().equals(user.getPasswordConfirmation())) {
			session.setAttribute("userId", userService.registerUser(user).getId());
			return "redirect:/home";
		} else {
			model.addAttribute("error", "Password do not match");
			return "registration";
		}
	}

	@GetMapping("/login")
	public String login(HttpSession session) {
		System.out.println(session.getAttribute("userId"));
		if (session.getAttribute("userId") != null) {
			return "redirect:/home";
		} else {
			return "login";
		}
	}

	@PostMapping(value = "/login")
	public String loginUser(User user, Model model) {
		if (userService.authenticateUser(user.getEmail(), user.getPassword())) {
			return "home";
		} else {
			model.addAttribute("error", "Invalid password");
			return "login";
		}
	}

	@GetMapping("/home")
	public String home(HttpSession session, Model model) {
		Integer id = Integer.parseInt(session.getAttribute("userId").toString());
		User user = userService.findUserById(id);
		if (user != null) {
			model.addAttribute("user", user);
			return "home";
		} else {
			session.removeAttribute("userId");
			return "redirect:/login";
		}
	}

	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.removeAttribute("userId");
		return "login";
	}
}
