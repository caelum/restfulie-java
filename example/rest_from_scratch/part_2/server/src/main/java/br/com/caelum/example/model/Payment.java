package br.com.caelum.example.model;

import br.com.caelum.example.controller.PaymentsController;
import br.com.caelum.vraptor.restfulie.hypermedia.HypermediaResource;
import br.com.caelum.vraptor.restfulie.relation.RelationBuilder;

public class Payment implements HypermediaResource {

	private Long id;
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

	public void setStatus(Status status) {
		this.status = status;
	}

	public static enum Status {
		ACCEPTED, INVALID
	}

	@Override
	public void configureRelations(RelationBuilder builder) {
		builder.relation("self").uses(PaymentsController.class).show(id);
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}
}
