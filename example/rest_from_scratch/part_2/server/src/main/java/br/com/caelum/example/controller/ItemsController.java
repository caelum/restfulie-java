package br.com.caelum.example.controller;

import static br.com.caelum.vraptor.view.Results.representation;
import static br.com.caelum.vraptor.view.Results.status;
import br.com.caelum.example.infra.Database;
import br.com.caelum.example.model.Item;
import br.com.caelum.vraptor.Consumes;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Resource;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.restfulie.Restfulie;
import br.com.caelum.vraptor.restfulie.hypermedia.ConfigurableHypermediaResource;

@Resource
public class ItemsController {

	private final Database database;
	private final Result result;
	private final Restfulie restfulie;

	public ItemsController(Database database, Result result, Restfulie restfulie) {
		this.database = database;
		this.result = result;
		this.restfulie = restfulie;
	}

	@Get
	@Path("/items")
	public void list() {
		ConfigurableHypermediaResource resource = restfulie.enhance(database.lista());
		resource.relation("basket").uses(BasketsController.class).create();

		result.use(representation()).from(resource, "items").serialize();
	}

	@Get
	@Path("/items/{id}")
	public void show(int id) {
		Item item = database.get(id);
		result.use(representation()).from(item).serialize();
	}

	@Post
	@Consumes
	@Path("/items")
	public void create(Item item) {
		database.adiciona(item);
		result.use(status()).created("/items/" + item.getId());
	}
}
