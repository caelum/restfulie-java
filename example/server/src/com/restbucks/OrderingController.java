package com.restbucks;

import static br.com.caelum.vraptor.view.Results.xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.caelum.restfulie.vraptor.Transition;
import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Delete;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.Routes;
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
	@Path("/order/{order.id}")
	public void get(Order order) {
		order = database.getOrder(order.getId());
		if (order != null) {
			Serializer serializer = result.use(xml()).from(order); //.namespace("http://restbucks.com/order", "o");
			serializer.include("items");
			serializer.include("payment").serialize();
		} else {
			status.notFound();
		}
	}
	
	@Post
	@Path("/order")
	@Consumes("application/xml")
	public void add(Order order) {
		database.save(order);
		routes.uriFor(OrderingController.class).get(order);
		status.created(routes.getUri());
	}
	
	@Delete
	@Path("/order/{order.id}")
	@Transition
	public void cancel(Order order) {
		order = database.getOrder(order.getId());
		if(order.getStatus().equals("paid")) {
			order.finish();
		} else {
			order.cancel();
		}
		status.ok();
	}
	
	@Get
	@Path("/order")
	public List<Order> index() throws IOException {
		return new ArrayList<Order>(database.all());
	}

	@Post
	@Path("/order/{order.id}/pay")
	@Consumes("application/xml")
	@Transition
	public void pay(Order order, Payment payment) {
		order = database.getOrder(order.getId());
		order.pay(payment);
		result.use(xml()).from(order.getReceipt()).serialize();
	}

	@Get
	@Path("/order/{order.id}/checkPaymentInfo")
	public void checkPayment(Order order) {
		order = database.getOrder(order.getId());
		if (order != null) {
			result.use(xml()).from(order.getPayment()).serialize();
		} else {
			status.notFound();
		}
	}

}
