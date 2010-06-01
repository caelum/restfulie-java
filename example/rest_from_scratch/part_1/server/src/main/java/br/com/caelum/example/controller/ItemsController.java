package br.com.caelum.example.controller;

import java.util.List;

import br.com.caelum.example.infra.Database;
import br.com.caelum.example.model.Item;
import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import static br.com.caelum.vraptor.view.Results.representation;
import static br.com.caelum.vraptor.view.Results.status;

@Resource
public class ItemsController {

	private final Database database;
	private final Result result;

	public ItemsController(Database database, Result result) {
		this.database = database;
		this.result = result;
	}

	@Get
	@Path("/items")
	public void lista() {
		List<Item> lista = database.lista();
		result.use(representation()).from(lista).serialize();
	}

	@Get
	@Path("/items/{id}")
	public void get(int id) {
		Item item = database.get(id);
		if(item == null) {
			result.use(status()).notFound();
		} else {
			result.use(representation()).from(item).serialize();
		}
	}

	@Post
	@Consumes
	@Path("/items")
	public void adiciona(Item item) {
		database.adiciona(item);
		result.use(status()).created("http://localhost:8080/restfulie/items/" + item.getId());
	}
}
