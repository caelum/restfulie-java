package br.com.caelum.vraptor.restbucks;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.restbucks.Item.Coffee;
import br.com.caelum.vraptor.restbucks.Item.Milk;
import br.com.caelum.vraptor.restbucks.Item.Size;
import br.com.caelum.vraptor.restbucks.Order.Location;


/**
 * Simple database simulation.
 */
@Component
@ApplicationScoped
public class OrderDatabase {

	private static int total = 1;
	private Map<String, Order> orders = new HashMap<String, Order>();

	public OrderDatabase() {
		Item item = new Item(Coffee.LATTE, 1, Milk.WHOLE, Size.SMALL);
		ArrayList<Item> items = new ArrayList<Item>();
		items.add(item);

		Order order = new Order("unpaid", items, Location.TO_TAKE);
		order.setId("1");
		save(order.getId(), order);

		order = new Order("paid", items, Location.TO_TAKE);
		order.pay(new Payment("1234123412341234", "guilherme silveira", 11, 12,
				new BigDecimal(1020.0)));
		order.setId("2");
		save(order.getId(), order);
	}

	public synchronized void save(Order order) {
		order.setStatus("unpaid");
		total++;
		String id = String.valueOf(total);
		order.setId(id);
		orders.put(id, order);
	}

	public void save(String id, Order order) {
		orders.put(id, order);
	}

	public boolean orderExists(String id) {
		return orders.containsKey(id);
	}

	private static final long serialVersionUID = 1L;

	public Order getOrder(String id) {
		return orders.get(id);
	}

	public Collection<Order> all() {
		return orders.values();
	}

	public void delete(Order order) {
		orders.remove(order.getId());
	}

	public void update(Order order) {
		orders.put(order.getId(), order);
	}

}
