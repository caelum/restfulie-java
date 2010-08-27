package br.com.caelum.example.controller;

import java.util.List;

import br.com.caelum.example.model.Item;
import br.com.caelum.example.model.Payment;
import br.com.caelum.example.model.Payment.Status;
import br.com.caelum.vraptor.restfulie.hypermedia.HypermediaResource;
import br.com.caelum.vraptor.restfulie.relation.RelationBuilder;

public class Basket implements HypermediaResource {

	private final long id;
	private final List<Item> items;
	private Payment payment;

	public Basket(long id, List<Item> items) {
		this.id = id;
		this.items = items;
	}

	public long getId() {
		return id;
	}

	public List<Item> getItems() {
		return items;
	}

	public Payment getPayment() {
		return payment;
	}

	@Override
	public void configureRelations(RelationBuilder builder) {
		builder.relation("self").uses(BasketsController.class).show(id);
		builder.relation("payment").uses(PaymentsController.class).create(id, null);
	}

	public void pay(Payment payment) {
		this.payment = payment;
		payment.setStatus(Status.ACCEPTED);
	}

}
