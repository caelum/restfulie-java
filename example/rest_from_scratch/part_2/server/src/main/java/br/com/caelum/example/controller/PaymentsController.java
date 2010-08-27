package br.com.caelum.example.controller;

import static br.com.caelum.vraptor.view.Results.representation;
import br.com.caelum.example.infra.Payments;
import br.com.caelum.example.model.Payment;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class PaymentsController {

	private final Result result;
	private final Baskets baskets;
	private final Payments payments;

	public PaymentsController(Result result, Baskets baskets, Payments payments) {
		this.result = result;
		this.baskets = baskets;
		this.payments = payments;
	}

	@Post @Path("/basket/{id}/payment")
	public void create(Long id, Payment payment) {
		Basket basket = baskets.get(id);
		basket.pay(payment);
		payments.save(payment);

		result.use(representation()).from(payment).serialize();
	}

	@Post @Path("/payment/{id}")
	public void show(Long id) {
		result.use(representation()).from(payments.get(id)).serialize();
	}

}
