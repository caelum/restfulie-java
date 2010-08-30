package br.com.caelum.example.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("payment")
public class Payment {

	private Status status = Status.INVALID;
	private final Double amount;
	private Long id;

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

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
}
