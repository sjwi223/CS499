package com.sqs.training.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.mobile.device.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sqs.training.domain.User;

@Controller
public class AuthenticationController {
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@RequestMapping("/login")
	public String displayLoginPage(Map<String, Object> model) {
		User loginForm = new User();
		model.put("loginForm",loginForm);
		return "login";
	}
	
	@RequestMapping("/loginUser")
	public String registerUser(@ModelAttribute("loginForm") User user, Map<String, Object> model,
			HttpSession session) {
		Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUserId(), user.getPassword());
		Authentication checkAuth = authenticationManager.authenticate(authentication);
		if (checkAuth.isAuthenticated()) {
			session.setAttribute("user", user);
		} else {
			model.put("errorMessage", "Incorrect credentials");
			return "login";
		}
		return "home";
	}
	
	@RequestMapping("/logout")
	public String logoutUser(Map<String, Object> model,
			HttpSession session) {
		session.removeAttribute("user");
		return "home";
	}
	
	@RequestMapping("/device")
	public @ResponseBody String detectDevice(String id, Device device) {
		String deviceType = "unknown";
		if (device.isNormal()) {
			deviceType = "normal";
		} else if (device.isMobile()) {
			deviceType = "mobile";
		} else if (device.isTablet()) {
			deviceType = "tablet";
		}
		return deviceType; 
	}
}
