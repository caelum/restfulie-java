package br.com.caelum.restbucks.model;

import static br.com.caelum.restfulie.Restfulie.resource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("order")
public class Order {

	private String id;
	private Location location;
	
	private List<Item> items;

	private String status;
	private Payment payment;

	public enum Location {
		takeAway, drinkIn
	};

	public Order(String status, List<Item> items, Location location) {
		this.status = status;
		this.items = items;
		this.location = location;
	}

	public Order() {
		this.items = new ArrayList<Item>();
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public Payment getPayment() {
		return payment;
	}

	public void add(Item item) {
		this.items.add(item);
	}

	public BigDecimal getCost() {
		BigDecimal total = BigDecimal.ZERO;
		for (Item item : items) {
			total= total.add(item.getPrice());
		}
		return total;
	}

	public String getLatestUri() {
		return resource(this).getTransition("latest").getHref();
	}
	
	public Receipt pay(Payment payment) {
		return resource(this).getTransition("pay").executeAndRetrieve(payment);
	}

	public void cancel() {
		resource(this).getTransition("cancel").execute();
	}
	
}
