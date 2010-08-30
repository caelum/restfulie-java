package br.com.caelum.client;

import static br.com.caelum.restfulie.Restfulie.resource;
import static br.com.caelum.restfulie.mediatype.MediaTypes.XML;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.example.model.Basket;
import br.com.caelum.example.model.Item;
import br.com.caelum.example.model.Payment;
import br.com.caelum.example.model.Payment.Status;
import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.Restfulie;
import br.com.caelum.restfulie.mediatype.XmlMediaType;

public class ClientTests {

	private RestClient restfulie;

	@Before
	public void setUp() throws Exception {
		restfulie = Restfulie.custom();
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
	}

	@Test
	public void shouldBeAbleToGetAnItemWithHypermedia() throws Exception {

		Response response = restfulie.at("http://localhost:8080/restfulie/items/2").accept("application/xml").get();
		Item item = response.getResource();

		assertNotNull(item);
		assertNotNull(item.getName());

		System.out.println(item.getName());

		assertTrue(resource(item).hasLink("self"));

		System.out.println(resource(item).getLink("self").getHref());

	}

	@Test
	public void shouldBeAbleToPostAnItemWithHypermedia() throws Exception {
		Item item = new Item("pipa", 299.0);
		Response response = restfulie.at("http://localhost:8080/restfulie/items").as("application/xml").post(item);

		Item savedItem = response.getResource();
		assertNotSame(item, savedItem);

		assertEquals("pipa", savedItem.getName());
		assertEquals(Double.valueOf(299.0), savedItem.getPrice());
		assertNotNull(savedItem.getId());
		System.out.println(savedItem.getId());

		assertTrue(resource(savedItem).hasLink("self"));
		System.out.println(savedItem.getName());
	}

	@Test
	public void shouldBeAbleToNavigateThroughLinks() throws Exception {
		Response response = restfulie.at("http://localhost:8080/restfulie/items").accept(XML).get();
		List<Item> items = response.getResource();

		assertNotNull(items);
		assertFalse(items.isEmpty());

		List<Item> selectedItems = items.subList(0, 2);

		assertTrue(resource(items).hasLink("basket"));
		response = resource(items).getLink("basket").follow().as(XML).accept(XML).post(new Basket(selectedItems));
		Basket basket = response.getResource();

		assertNotNull(basket.getId());
		assertEquals(basket.getItems().size(), selectedItems.size());
		System.out.println(basket.getItems());

		assertTrue(resource(basket).hasLink("payment"));
		response = resource(basket).getLink("payment").follow().as(XML).accept(XML).post(new Payment(basket.getCost()));
		Payment payment = response.getResource();

		assertEquals(Status.ACCEPTED, payment.getStatus());

		System.out.println(payment.getStatus());

	}
}
