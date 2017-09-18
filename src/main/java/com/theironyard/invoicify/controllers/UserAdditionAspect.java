package com.theironyard.invoicify.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class UserAdditionAspect {

	@ModelAttribute
	public void addUserInformation(Model model, Authentication auth, HttpServletRequest request) {
		
		if (auth == null) {
			model.addAttribute("notUser", true);
		} else if (request.isUserInRole("ADMIN")) {
			model.addAttribute("user", auth.getPrincipal());
			model.addAttribute("invoices-list", true);
			model.addAttribute("billing-records-list", true);
			model.addAttribute("company-list", true);
		} else if (request.isUserInRole("ACCOUNTANT")) {
			model.addAttribute("user", auth.getPrincipal());
			model.addAttribute("invoices-list", true);
		} else if (request.isUserInRole("CLERK")) {
			model.addAttribute("user", auth.getPrincipal());
			model.addAttribute("billing-records-list", true);
		}
		else {
			model.addAttribute("user", auth.getPrincipal()); 
		}
	}
}
