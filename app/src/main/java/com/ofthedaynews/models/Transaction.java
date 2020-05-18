package com.ofthedaynews.models;

public class Transaction {
	private String date;
	private String type;
	private String amount;

	public Transaction(String date, String type, String amount) {
		this.date = date;
		this.type = type;
		this.amount = amount;
	}
	
	public String getDate() {
		return date;
	}
	public String getType() {
		return type;
	}
	public String getAmount() {
		return amount;
	}
	
}
