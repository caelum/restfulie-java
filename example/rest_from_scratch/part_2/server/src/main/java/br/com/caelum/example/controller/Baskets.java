package br.com.caelum.example.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.caelum.example.model.Item;
import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class Baskets {

	private Map<Long, Basket> baskets = new HashMap<Long, Basket>();

	private long current = 1;

	public Basket newBasket(List<Item> items) {
		long id = current++;
		Basket basket = new Basket(id, items);
		baskets.put(id, basket);
		return basket;
	}

	public Basket get(Long id) {
		return baskets.get(id);
	}
}
