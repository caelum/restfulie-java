package br.com.caelum.restfulie.integration.client;


import static br.com.caelum.restfulie.Restfulie.resource;
import static br.com.caelum.restfulie.opensearch.Url.page;
import static br.com.caelum.restfulie.opensearch.Url.queryFor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.Restfulie;
import br.com.caelum.restfulie.mediatype.XmlMediaType;
import br.com.caelum.restfulie.opensearch.SearchDescription;
import br.com.caelum.restfulie.opensearch.Tags;
import br.com.caelum.restfulie.opensearch.Url;

public class ClientTest {

	private RestClient restfulie;

	@Before
	public void setUp() {
		try {
			new URL("http://localhost:3000").openStream();
		} catch (IOException e) {
			Assume.assumeNoException(e);
		}
		restfulie = Restfulie.custom();
		XmlMediaType mediaType = new XmlMediaType().withTypes(SearchDescription.class, Url.class, Tags.class,
				Product.class, Order.class, Item.class, Payment.class);
		mediaType.withCollectionName("products");
		restfulie.getMediaTypes().register(mediaType);
	}

	@Test
	public void shouldBeAbleToSearchItems() {
		Response response = search("20", 1);
		List<Product> products = response.getResource();
		assertThat(products.size(), is(equalTo(2)));
	}

	@Test
	public void shouldBeAbleToCreateAnEmptyOrder() {
		Response response = search("20", 1);
		List<Product> products = response.getResource();
		response = resource(products).getLink("order").follow().accept("application/xml").post(newOrder("Av. Princesa Isabel 350, Copacabana, Rio de Janeiro"));
		Order order = response.getResource();
		assertThat(order.getAddress(), is(equalTo("Av. Princesa Isabel 350, Copacabana, Rio de Janeiro")));
	}

	@Test
	public void shouldBeAbleToAddAnItemToAnOrder() {
		Response response = search("20", 1);

		List<Product> product = response.getResource();
		response = resource(product).getLink("order").follow().handling("application/xml")
				.post(newOrder("Av. Princesa Isabel 350, Copacabana, Rio de Janeiro"));

		Order orderParam = newOrder(product.get(0).getId());

		Order order = response.getResource();

		System.out.println(resource(order).getLink("self").getType());
		response = resource(order).getLink("self").follow().handling("application/xml").put(orderParam);

		order = response.getResource();

		assertThat(order.getPrice(), is(equalTo(800.0)));
	}

	private Order newOrder(Integer id) {
		Order order = new Order();
		order.setProduct(id);
		order.setQuantity(1);
		return order;
	}

	@Test
	public void shouldBeAbletToPay() {
		Response response = search("20", 1);
		List<Product> products = response.getResource();

		Product product = products.get(0);
		Order orderParam = newOrder(product.getId());

		response = resource(products).getLink("order").follow().handling("application/xml")
				.post(newOrder("Av. Princesa Isabel 350, Copacabana, Rio de Janeiro"));
		Order order = response.getResource();

		response = resource(order).getLink("self").follow().handling("application/xml").put(orderParam);
		order = response.getResource();
		response = pay(order);

		order = response.getResource();
		assertThat(order.getState(), is(equalTo("processing_payment")));
	}

	private Response pay(Order order) {
		Payment payment = new Payment("Guilherme Silveira", 444, order.getPrice());
		return resource(order).getLink("payment").follow().handling("application/xml").post(payment);
	}

	@Test
	public void shouldTryAndPayForIt() {
		Response response = search("20",1);
		List<Product> products = response.getResource();
		Product product = products.get(0);

		Order order = resource(products).getLink("order").follow().handling("application/xml").post(newOrder("Av. Princesa Isabel 350, Copacabana, Rio de Janeiro eh este")).getResource();
		order = resource(order).getLink("self").follow().handling("application/xml").put(newOrder(product.getId())).getResource();

		response = pay(order);

		order = response.getResource();

		order = waitPaymentSuccess(1,order);

		assertThat(order.getState(),is(equalTo("preparing")));
	}

	private Order waitPaymentSuccess(int attempts, Order order) {

		while(order.getState().equals("processing_payment")) {

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println("esperando");
			order = resource(order).getLink("self").follow().handling("application/xml").get().getResource();
			System.out.println(order.getState());
		}

		if(order.getState().equals("unpaid") && attempts > 0) {
			System.out.println("payment rejected");
			order = pay(order).getResource();
			return waitPaymentSuccess(attempts - 1, order);
		} else {
			return order;
		}
	}

	private Response search(String term, int page) {
		Response response = restfulie.at("http://localhost:3000/products/opensearch.xml").accept("application/opensearchdescription+xml").get();
		SearchDescription desc = response.getResource();
		response = desc.use("application/xml").with(queryFor(term)).and(page(page)).get();
		return response;
	}

	private Order newOrder(String address) {
		Order order = new Order();
		order.setAddress(address);
		return order;
	}

}
