package br.com.caelum.restfulie.integration.client;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("payment")
public class Payment {

	@XStreamAlias("card_holder")
	private final String holder;
	@XStreamAlias("card_number")
	private final int number;
	private final Double value;
	private String state;

	public Payment(String holder, int number, Double value) {
		this.holder = holder;
		this.number = number;
		this.value = value;
	}

	public String getHolder() {
		return holder;
	}

	public int getNumber() {
		return number;
	}

	public Double getValue() {
		return value;
	}

	public String getState() {
		return state;
	}
	
}
