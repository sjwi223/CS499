package com.sqs.training.controller;

import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sqs.training.domain.User;

@Controller
public class RegisterController {
	
	@Autowired
	private LdapTemplate ldapTemplate;

	@RequestMapping("/register")
	public String displayRegisterPage(Map<String, Object> model) {
		User registrationForm = new User();
		model.put("registrationForm",registrationForm);
		return "register";
	}
	
	@RequestMapping("/registerUser")
	public String registerUser(@ModelAttribute("registrationForm") User user) {
		DirContextAdapter context = new DirContextAdapter("uid=" + user.getUserId());
		String[] objectClass = new String[] {"top", "person"};
		context.setAttributeValues("objectclass", objectClass);
		context.setAttributeValue("sn", user.getLastName());
		context.setAttributeValue("cn", user.getFirstName() + " " + user.getLastName());
		context.setAttributeValue("userPassword", user.getPassword());
		ldapTemplate.bind(context);
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
