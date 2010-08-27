package br.com.caelum.example.controller;

import static br.com.caelum.vraptor.view.Results.representation;
import static br.com.caelum.vraptor.view.Results.status;

import java.util.List;

import br.com.caelum.example.model.Item;
import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;

@Resource
public class BasketsController {

	private final Result result;
	private final Baskets baskets;

	public BasketsController(Result result, Baskets baskets) {
		this.result = result;
		this.baskets = baskets;
	}

	@Get @Path("/basket/{id}")
	public void show(Long id) {
		result.use(representation()).from(baskets.get(id));
	}

	@Post @Path("/basket")
	@Consumes("application/xml")
	public void create(List<Item> items) {
		result.use(status()).created("/basket/" + baskets.newBasket(items).getId());
	}

}
