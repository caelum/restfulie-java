package br.com.caelum.example.model;

public class Payment {

	private Status status = Status.INVALID;
	private final Double amount;

	public Payment(Double amount) {
		this.amount = amount;
	}

	public Double getAmount() {
		return amount;
	}

	public Status getStatus() {
		return status;
	}

	public static enum Status {
		ACCEPTED, INVALID
	}
}
