package br.com.caelum.client;

import java.net.URISyntaxException;

import br.com.caelum.example.model.Item;
import br.com.caelum.restfulie.Resources;
import br.com.caelum.restfulie.Restfulie;

public class TestPostRepresentation {
	public static void main(String[] args) throws URISyntaxException {
		Resources server = Restfulie.resources();
		server.configure(Item.class);

		Item item = new Item("pipa", 299.0);
		item = server.entryAt("http://localhost:8080/restfulie/items").post(item);

		System.out.println(item.getNome());
	}
}
