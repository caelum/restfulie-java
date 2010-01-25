package br.com.caelum.restbucks.model;

import static br.com.caelum.restfulie.Restfulie.resource;

import java.util.Calendar;

import br.com.caelum.restfulie.http.HttpMethod;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("receipt")
public class Receipt {

	private Calendar paymentTime;

	public Calendar getPaymentTime() {
		return paymentTime;
	}

	public Order getOrder() {
		return resource(this).getTransition("order").method(HttpMethod.GET).executeAndRetrieve();
	}

}
