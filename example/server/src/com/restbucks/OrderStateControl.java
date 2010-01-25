package com.restbucks;

import br.com.caelum.restfulie.vraptor.StateControl;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class OrderStateControl implements StateControl<Order> {
	
	private final OrderDatabase database;

	public OrderStateControl(OrderDatabase database) {
		this.database = database;
	}
	
	@SuppressWarnings("unchecked")
	public Class[] getControllers() {
		return new Class[]{OrderingController.class};
	}
	
	public Order retrieve(String id) {
		return database.getOrder(id);
	}
	
}
