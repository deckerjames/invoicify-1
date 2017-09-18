package com.theironyard.invoicify.models;

import javax.persistence.Entity;

@Entity
public class RateBasedBillingRecord extends BillingRecord {

	private double rate;
	
	private double quantity;
	
	public RateBasedBillingRecord() {}
	
	public RateBasedBillingRecord(double rate, double quantity, String description, User createdBy, Company client) {
		super(description, createdBy, client);
		this.rate = rate;
		this.quantity = quantity;
	}
	
	@Override
	public double getTotal() {
		return rate * quantity;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

}
