package br.com.caelum.vraptor.restbucks.web;

import static br.com.caelum.vraptor.view.Results.representation;
import static br.com.caelum.vraptor.view.Results.xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Put;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.Routes;
import br.com.caelum.vraptor.restbucks.Item;
import br.com.caelum.vraptor.restbucks.Order;
import br.com.caelum.vraptor.restbucks.OrderDatabase;
import br.com.caelum.vraptor.restbucks.Payment;
import br.com.caelum.vraptor.restfulie.hypermedia.Transition;
import br.com.caelum.vraptor.serialization.Serializer;
import br.com.caelum.vraptor.view.Status;

/**
 * Ordering system provides two services: order retrieval and insertion.
 * 
 * @author guilherme silveira
 */
@Resource
public class OrderingController {

	private final Result result;
	private final Status status;
	private final OrderDatabase database;
	private final Routes routes;

	public OrderingController(Result result, Status status, OrderDatabase database, Routes routes) {
		this.result = result;
		this.status = status;
		this.database = database;
		this.routes = routes;
	}

	@Get
	@Path("/orders/{order.id}")
	public void get(Order order) {
		order = database.getOrder(order.getId());
		if (order != null) {
			Serializer serializer = result.use(representation()).from(order);
			serializer.include("items").include("location");
			serializer.include("payment").serialize();
		} else {
			status.notFound();
		}
	}
	
	@Post
	@Path("/orders")
	@Consumes
	public void add(Order order) {
		int id = 0;
		for(Item i : order.getItems()) {
			i.use(order, ++id);
		}
		database.save(order);
		routes.uriFor(OrderingController.class).get(order);
		status.created(routes.getUri());
	}
	
	@Delete
	@Path("/orders/{order.id}")
	@Transition
	public void cancel(Order order) {
		order = database.getOrder(order.getId());
		if(order.getStatus().equals("ready")) {
			order.finish();
		} else {
			order.cancel();
			database.delete(order);
		}
		status.ok();
	}
	
	@Get
	@Path("/orders")
	public List<Order> index() throws IOException {
		return new ArrayList<Order>(database.all());
	}

	@Post
	@Path("/orders/{order.id}/pay")
	@Consumes
	@Transition
	public void pay(Order order, Payment payment) {
		order = database.getOrder(order.getId());
		if(order.pay(payment)) {
			result.use(xml()).from(order.getReceipt()).serialize();
		} else {
			status.badRequest("Invalid payment value, order costs " + order.getCost());
		}
	}

	@Get
	@Path("/orders/{order.id}/checkPaymentInfo")
	public void checkPayment(Order order) {
		order = database.getOrder(order.getId());
		if (order != null) {
			result.use(xml()).from(order.getPayment()).serialize();
		} else {
			status.notFound();
		}
	}

	@Put
	@Path("/orders/{order.id}")
	@Transition
	@Consumes
	public void update(Order order) {
		order.setStatus("unpaid");
		database.update(order);
		// we could status.ok() or return the representation
		get(order); 
	}
	
}
