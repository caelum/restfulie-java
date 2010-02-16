package br.com.caelum.vraptor.restbucks;

import java.math.BigDecimal;
import java.util.Calendar;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("payment")
public class Payment {

	@XStreamAlias("card-number")
	private String cardNumber;
	@XStreamAlias("cardholder-name")
	private String cardholderName;
	@XStreamAlias("expiry-month")
	private int expiryMonth;
	@XStreamAlias("expiry-year")
	private int expiryYear;
	private BigDecimal amount;
	private Calendar createdAt;

	public Payment(String cardNumber, String cardholderName, int expiryMonth,
			int expiryYear, BigDecimal amount) {
		super();
		this.cardNumber = cardNumber;
		this.cardholderName = cardholderName;
		this.expiryMonth = expiryMonth;
		this.expiryYear = expiryYear;
		this.amount = amount;
	}
	
	public BigDecimal getAmount() {
		return amount;
	}

	public Calendar getCreatedAt() {
		return this.createdAt;
	}
	public void setCreatedAt(Calendar createdAt) {
		this.createdAt = createdAt;
	}
	
}
