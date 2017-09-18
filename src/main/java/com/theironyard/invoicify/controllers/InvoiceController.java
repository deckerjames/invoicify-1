package com.theironyard.invoicify.controllers;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.theironyard.invoicify.models.BillingRecord;
import com.theironyard.invoicify.models.Company;
import com.theironyard.invoicify.models.Invoice;
import com.theironyard.invoicify.models.InvoiceLineItem;
import com.theironyard.invoicify.models.User;
import com.theironyard.invoicify.repositories.BillingRecordRepository;
import com.theironyard.invoicify.repositories.CompanyRepository;
import com.theironyard.invoicify.repositories.InvoiceRepository;

@Controller
@RequestMapping("/invoices")
public class InvoiceController {

	CompanyRepository companyRepository;
	BillingRecordRepository billingRecordRepository;

	@Autowired
	private InvoiceRepository invoiceRepository;

	public InvoiceController(CompanyRepository companyRepository, BillingRecordRepository billingRecordRepository) {
		this.companyRepository = companyRepository;
		this.billingRecordRepository = billingRecordRepository;
	}

	@GetMapping("")
	public ModelAndView list(Authentication auth) {
		User user = (User) auth.getPrincipal();
		ModelAndView mv = new ModelAndView("invoices/list");
		mv.addObject("user", user);
		mv.addObject("invoices", invoiceRepository.findAll());
		return mv;
	}

	@GetMapping("new")
	public ModelAndView selectClient() {
		ModelAndView mv = new ModelAndView("invoices/step-1");
		mv.addObject("companies", companyRepository.findAll());
		return mv;
	}

	@PostMapping("new")
	public ModelAndView selectInvoices(long clientId) {
		ModelAndView mv = new ModelAndView("invoices/step-2");
		mv.addObject("clientId", clientId);
		mv.addObject("billingRecords", billingRecordRepository.findByClientIdAndLineItemIsNull(clientId));
		return mv;
	}

	@PostMapping("create")
	public ModelAndView createInvoice(Invoice invoice, long clientId, long[] recordIds, Authentication auth) {
		ModelAndView mv = new ModelAndView();

		try {
			User creator = (User)auth.getPrincipal();
			List<BillingRecord> billingRecords =  billingRecordRepository.findByIdIn(recordIds);
			long nowish = Calendar.getInstance().getTimeInMillis();
			Date now = new Date(nowish);


			List<InvoiceLineItem> items = new ArrayList<InvoiceLineItem>();
			for(BillingRecord record : billingRecords) {
				InvoiceLineItem lineItem = new InvoiceLineItem();
				lineItem.setBillingRecord(record);;
				lineItem.setCreatedBy(creator);
				lineItem.setCreatedOn(now);
				lineItem.setInvoice(invoice);
				items.add(lineItem);
			}

			invoice.setLineItems(items);
			invoice.setCompany(companyRepository.findOne(clientId));
			invoice.setCreatedBy(creator);
			invoice.setCreatedOn(now);

			invoiceRepository.save(invoice);
			mv.setViewName("redirect:/invoices");

		}catch (InvalidDataAccessApiUsageException idaaue) {
			mv.setViewName("/invoices/step-2");
			mv.addObject("clientId", clientId);
			mv.addObject("billingRecords", billingRecordRepository.findByClientIdAndLineItemIsNull(clientId));
			mv.addObject("errorMessage", "Please select at least one billing record");	
		}

		return mv;
	}

}