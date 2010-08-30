package br.com.caelum.example.controller;

import static br.com.caelum.vraptor.view.Results.representation;
import static br.com.caelum.vraptor.view.Results.status;
import br.com.caelum.example.infra.Payments;
import br.com.caelum.example.model.Payment;
import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Get;
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
	@Consumes("application/xml")
	public void create(Long id, Payment payment) {
		Basket basket = baskets.get(id);
		basket.pay(payment);
		payments.save(payment);

		result.use(status()).created("/payment/" + payment.getId());
	}

	@Get @Path("/payment/{id}")
	public void show(Long id) {
		result.use(representation()).from(payments.get(id)).serialize();
	}

}
