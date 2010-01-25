package com.restbucks;

import java.util.Calendar;
import java.util.List;

import br.com.caelum.vraptor.rest.Restfulie;
import br.com.caelum.vraptor.rest.StateResource;
import br.com.caelum.vraptor.rest.Transition;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("order")
public class Order implements StateResource {

	private String id;
	private Location location;
	@XStreamImplicit
	private List<Item> items;

	private String status;
	private Payment payment;
	private Receipt receipt;

	public enum Location {
		takeAway, drinkIn
	};

	public Order(String status, List<Item> items, Location location) {
		this.status = status;
		this.items = items;
		this.location = location;
	}

	public Order() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void cancel() {
		status = "cancelled";
	}

	public List<Transition> getFollowingTransitions(Restfulie control) {
		control.relation("self").uses(OrderingController.class).get(this);
		if (status.equals("unpaid")) {
			control.transition("cancel").uses(OrderingController.class).cancel(this);
			control.relation("payment").uses(OrderingController.class).pay(this,null);
//			 when(notFound()).then(404);
//			 when(invalidState()).then(customWhatever());
		}
		if(status.equals("paid") && receipt.getPaymentTime().before(oneMinuteAgo())) {
			control.transition("retrieve").uses(OrderingController.class).cancel(this);
		}
		control.relation("songs").at("http://otherserver");
		return control.getTransitions();
	}

	private Calendar oneMinuteAgo() {
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, -1);
		return c;
	}

	public void pay(Payment payment) {
		status = "paid";
		this.receipt = new Receipt(this);
		this.payment = payment;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Payment getPayment() {
		return payment;
	}

	public Receipt getReceipt() {
		return receipt;
	}

	public void finish() {
		status = "retrieved by the client";
	}

}
