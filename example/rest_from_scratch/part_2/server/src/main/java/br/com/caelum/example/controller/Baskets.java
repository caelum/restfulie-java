package br.com.caelum.example.controller;

import java.util.HashMap;
import java.util.Map;

import br.com.caelum.vraptor.ioc.ApplicationScoped;
import br.com.caelum.vraptor.ioc.Component;

@Component
@ApplicationScoped
public class Baskets {

	private Map<Long, Basket> baskets = new HashMap<Long, Basket>();

	private long current = 1;


	public Basket get(Long id) {
		return baskets.get(id);
	}

	public void save(Basket basket) {
		long id = current++;
		basket.setId(id);
		baskets.put(id, basket);
	}
}
