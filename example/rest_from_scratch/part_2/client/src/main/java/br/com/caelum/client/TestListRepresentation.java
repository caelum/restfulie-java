package br.com.caelum.client;

import static br.com.caelum.restfulie.Restfulie.resource;
import static br.com.caelum.restfulie.mediatype.MediaTypes.XML;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import br.com.caelum.example.model.Basket;
import br.com.caelum.example.model.Item;
import br.com.caelum.example.model.Payment;
import br.com.caelum.restfulie.Resource;
import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.Restfulie;
import br.com.caelum.restfulie.mediatype.XmlMediaType;

public class TestListRepresentation {
	public static void main(String[] args) throws URISyntaxException, IOException {

		RestClient restfulie = Restfulie.custom();
		restfulie.getMediaTypes().register(new XmlMediaType() {
			@Override
			protected List<Class> getTypesToEnhance() {
				return Arrays.<Class>asList(Item.class, Basket.class, Payment.class);
			}

			@Override
			protected List<String> getCollectionNames() {
				return Arrays.asList("items");
			}
		});

		Response response = restfulie.at("http://localhost:8080/restfulie/items").accept(XML).get();
		List<Item> items = response.getResource();

		response = resource(items).getLink("basket").follow().as(XML).accept(XML).post(items.subList(0, 2));

		Basket basket = response.getResource();
		response = resource(basket).getLink("payment").follow().as(XML).accept(XML).post(new Payment(basket.getCost()));
		Payment payment = response.getResource();

		System.out.println(payment.getStatus());
		System.out.println(items);
		System.out.println(((Resource)items).getLink("basket").getHref());

	}
}
