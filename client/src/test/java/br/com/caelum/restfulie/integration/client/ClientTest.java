package br.com.caelum.restfulie.integration.client;

import static br.com.caelum.restfulie.Restfulie.resource;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
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
		response = pay(response);

		order = response.getResource();
		assertThat(order.getState(), is(equalTo("processing_payment")));
	}

	private Response pay(Response response) {
		Order order;
		order = response.getResource();

		Payment payment = new Payment("Guilherme Silveira", 444, order.getPrice());
		response = resource(order).getLink("payment").follow().handling("application/xml").post(payment);
		return response;
	}

	@Ignore
	public void shouldTryAndPayForIt() {
		Response response = search("20", 1);
		List<Product> products = response.getResource();

		Product product = products.get(0);
		Order orderParam = newOrder(product.getId());

		response = resource(products).getLink("order").follow().handling("application/xml")
				.post(newOrder("Av. Princesa Isabel 350, Copacabana, Rio de Janeiro"));

		Order order = response.getResource();

		response = resource(order).getLink("self").follow().handling("application/xml").put(orderParam);
		response = pay(response);

		order = response.getResource();

		response = waitPaymentSucesse(1, response);
		order = response.getResource();

		assertThat(order.getState(), is(equalTo("paid")));

	}

	// def wait_payment_success(attempts, result)
	//
	// results = search("20")
	//
	// results.resource.products.each do |product|
	//
	// result.order.state = w"paid"
	// response =
	// results.resource.products.links.order.follow.post(result.order)
	//
	// puts "Checking order status at #{result.order.links.self.href}"
	// end
	//
	// if result.order.state == "unpaid" && attempts>0
	// puts
	// "Ugh! Payment rejected! Get some credits my boy... I am trying it again."
	// result = pay(result)
	// wait_payment_success(attempts-1, result)
	// else
	// result
	// end
	// end

	private Response waitPaymentSucesse(int attempts, Response otherResponse) {
		Response response = search("20", 1);

		List<Product> products = response.getResource();

		Order order = otherResponse.getResource();
		for (Product product : products) {
			order.setState("paid");

			Order anotherOrder = response.getResource();
			resource(anotherOrder.getProduct()).getLink("order").follow().post(order);

			System.out.println("Checking order status at " + resource(order).getLink("self").getHref());

		}

		if (order.getState().equals("unpaid") && attempts > 0) {
			System.out.println("Ugh! Payment reject! Get some credits my boy... I am trying it again.");
			response = pay(otherResponse);
			waitPaymentSucesse(attempts - 1, response);
		}

		return response;
	}

	private Response search(String term, int page) {
		Response response = restfulie.at("http://localhost:3000/products/opensearch.xml")
				.accept("application/opensearchdescription+xml").get();
		SearchDescription desc = response.getResource();
		response = restfulie.at(desc.use("application/xml").atPage(page).search("20").getUri()).get();
		return response;
	}

	private Order newOrder(String address) {
		Order order = new Order();
		order.setAddress(address);
		return order;
	}

}
