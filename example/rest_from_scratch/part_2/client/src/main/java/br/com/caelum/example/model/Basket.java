package br.com.caelum.example.model;

import java.util.List;

public class Basket {

	private List<Item> items;

	private Double cost;

	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public Double getCost() {
		return cost;
	}

	public void setCost(Double cost) {
		this.cost = cost;
	}

}
