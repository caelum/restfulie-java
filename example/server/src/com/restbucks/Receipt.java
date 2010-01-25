package com.restbucks;

import java.util.Calendar;
import java.util.List;

import br.com.caelum.vraptor.rest.Restfulie;
import br.com.caelum.vraptor.rest.StateResource;
import br.com.caelum.vraptor.rest.Transition;

public class Receipt implements StateResource{
	
	private final Calendar paymentTime= Calendar.getInstance();
	private final Order order;
	
	public Receipt(Order order) {
		this.order = order;
	}

	public Calendar getPaymentTime() {
		return paymentTime;
	}
	
	public List<Transition> getFollowingTransitions(Restfulie control) {
		control.transition("order").uses(OrderingController.class).get(order);
		return control.getTransitions();
	}

}
