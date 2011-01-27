package br.com.caelum.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import org.junit.Before;
import org.junit.Test;
import org.jvnet.inflector.Noun;

import br.com.caelum.example.model.Item;
import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.Restfulie;
import br.com.caelum.restfulie.mediatype.XmlMediaType;

public class ClientTests {

	private RestClient restfulie;

	@Before
	public void setUp() throws Exception 
	{
		restfulie = Restfulie.custom().withInflector( new MyInflector() );
		restfulie.getMediaTypes().register(new XmlMediaType().withTypes(Item.class));
	}
	
	@Test
	public void shouldBeAbleToInflectItem()
	{
		assertEquals( "itens", Noun.pluralOf("item", restfulie.inflectionRules()) );
	}

	@Test
	public void shouldBeAbleToGetAnItem() throws Exception 
	{
		Response response = restfulie.at("http://localhost:8080/restfulie/items/2").accept("application/xml").get();
		Item item = response.getResource();

		assertNotNull(item);

		assertNotNull(item.getName());

		System.out.println(item.getName());
	}

	@Test
	public void shouldBeAbleToPostAnItem() throws Exception 
	{
		Item item = new Item("pipa", 299.0);
		Response response = restfulie.at("http://localhost:8080/restfulie/items").as("application/xml").post(item);

		Item savedItem = response.getResource();
		assertNotSame(item, savedItem);

		assertEquals("pipa", savedItem.getName());
		assertEquals(Double.valueOf(299.0), savedItem.getPrice());
		assertNotNull(savedItem.getId());
		System.out.println(savedItem.getId());

	}
}
