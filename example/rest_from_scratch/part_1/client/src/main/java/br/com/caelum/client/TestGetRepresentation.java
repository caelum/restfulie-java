package br.com.caelum.client;

import java.net.URISyntaxException;

import br.com.caelum.example.model.Item;
import br.com.caelum.restfulie.Resources;
import br.com.caelum.restfulie.Restfulie;

public class TestGetRepresentation {
	public static void main(String[] args) throws URISyntaxException {
		Resources server = Restfulie.resources();
		server.configure(Item.class);

		Item item = server.entryAt("http://localhost:8080/restfulie/items/3").accept("application/xml").get();

		System.out.println(item.getNome());
	}
}
