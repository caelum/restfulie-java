package br.com.caelum.vraptor.restbucks.web;

import static br.com.caelum.vraptor.view.Results.xml;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.restbucks.Item;
import br.com.caelum.vraptor.restbucks.Order;
import br.com.caelum.vraptor.restbucks.OrderDatabase;
import br.com.caelum.vraptor.view.Status;

@Resource
public class ItemController {

	private final Result result;
	private final Status status;
	private final OrderDatabase database;

	public ItemController(Result result, Status status,
			OrderDatabase database) {
		this.result = result;
		this.status = status;
		this.database = database;
	}

	@Path("/orders/{order.id}/items/{item.id}")
	public void get(Order order, Item item) {
		order = database.getOrder(order.getId());
		if (order != null) {
			result.use(xml()).from(order.findItem(item.getId())).serialize();
		} else {
			status.notFound();
		}
	}

	@Path("/orders/{order.id}/items")
	public void index(Order order) {
		order = database.getOrder(order.getId());
		if (order != null) {
			result.use(xml()).from(order.getItems(), "items").serialize();
		} else {
			status.notFound();
		}
	}
}
