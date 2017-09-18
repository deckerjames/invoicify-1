package com.theironyard.invoicify.controllers;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.theironyard.invoicify.models.Company;
import com.theironyard.invoicify.repositories.CompanyRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private CompanyRepository companyRepository;

	public AdminController(CompanyRepository companyRepository) {
		this.companyRepository = companyRepository;
	}

	@GetMapping("companies")
	public ModelAndView showAllCompanies() {
		ModelAndView mv = new ModelAndView("admin/companies");
		List<Company> companies = companyRepository.findAll(new Sort(Sort.Direction.ASC, "name"));
		mv.addObject("company", companies);

		return mv;
	}

	@PostMapping("companies")
	public ModelAndView create(Company company) {
		ModelAndView mv = new ModelAndView("redirect:/admin/companies");
		companyRepository.save(company);

		return mv;
	}

}
