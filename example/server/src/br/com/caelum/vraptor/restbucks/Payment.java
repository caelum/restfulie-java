package br.com.caelum.vraptor.restbucks;

import java.math.BigDecimal;

public class Payment {

	private String cardNumber;
	private String cardholderName;
	private int expiryMonth;
	private int expiryYear;
	private BigDecimal amount;

	public Payment(String cardNumber, String cardholderName, int expiryMonth,
			int expiryYear, BigDecimal amount) {
		super();
		this.cardNumber = cardNumber;
		this.cardholderName = cardholderName;
		this.expiryMonth = expiryMonth;
		this.expiryYear = expiryYear;
		this.amount = amount;
	}

}
