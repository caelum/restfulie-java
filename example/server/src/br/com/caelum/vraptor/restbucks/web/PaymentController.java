package br.com.caelum.vraptor.restbucks.web;

import static br.com.caelum.vraptor.view.Results.xml;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.restbucks.Order;
import br.com.caelum.vraptor.restbucks.OrderDatabase;
import br.com.caelum.vraptor.view.Status;

@Resource
public class PaymentController {

	private final Result result;
	private final Status status;
	private final OrderDatabase database;

	public PaymentController(Result result, Status status,
			OrderDatabase database) {
		this.result = result;
		this.status = status;
		this.database = database;
	}

	@Get
	@Path("/orders/{order.id}/payment")
	public void get(Order order) {
		order = database.getOrder(order.getId());
		if (order != null) {
			result.use(xml()).from(order.getPayment()).serialize();
		} else {
			status.notFound();
		}
	}
}
