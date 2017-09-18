package com.theironyard.invoicify.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.theironyard.invoicify.models.Company;
import com.theironyard.invoicify.models.FlatFeeBillingRecord;
import com.theironyard.invoicify.models.RateBasedBillingRecord;
import com.theironyard.invoicify.models.User;
import com.theironyard.invoicify.repositories.BillingRecordRepository;
import com.theironyard.invoicify.repositories.CompanyRepository;
import com.theironyard.invoicify.repositories.UserRepository;

@Configuration
@Profile("development")
public class SeedData {
	
	public SeedData(BillingRecordRepository recordRepository, CompanyRepository companyRepository, UserRepository usersRepository, PasswordEncoder encoder) {
		usersRepository.save(new User("curtis", encoder.encode("password"), "TEACHER"));
		User admin = usersRepository.save(new User("admin", encoder.encode("admin"), "ADMIN"));
		User clerk = usersRepository.save(new User("clerk", encoder.encode("clerk"), "CLERK"));
		usersRepository.save(new User("accountant", encoder.encode("accountant"), "ACCOUNTANT"));
		
		Company ajax = companyRepository.save(new Company("AJAX Ltd."));
		Company lomax = companyRepository.save(new Company("Lomax Brothers, LLC"));
		
		recordRepository.save(new FlatFeeBillingRecord(450, "Guitar Strings", clerk, lomax));
		recordRepository.save(new FlatFeeBillingRecord(22, "Picks", clerk, ajax));
		recordRepository.save(new FlatFeeBillingRecord(6981, "Wizard Amp", clerk, lomax));
		recordRepository.save(new FlatFeeBillingRecord(999, "Monster Cables", clerk, ajax));
		recordRepository.save(new FlatFeeBillingRecord(1.05, "Snickers Bar", admin, lomax));
		
		recordRepository.save(new RateBasedBillingRecord(29, 666, "Beauty Beast", admin, ajax));
		recordRepository.save(new RateBasedBillingRecord(99, 48.59, "80s Metal", clerk, ajax));
		recordRepository.save(new RateBasedBillingRecord(28.95, 3, "Speakers", admin, ajax));
		recordRepository.save(new RateBasedBillingRecord(9008, 8847, "Crapinsteiner", admin, lomax));
		recordRepository.save(new RateBasedBillingRecord(47, 43.47, "Sausage", clerk, ajax));
	}

}


















