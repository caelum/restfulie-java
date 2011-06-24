package br.com.caelum.restfulie;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;
import org.jvnet.inflector.lang.en.NounPluralizer;

import br.com.caelum.restfulie.mediatype.GsonMediaType;

import com.google.gson.reflect.TypeToken;

public class GsonMediaTypeTest {

	public static class Order{
		private int id;
		private String client;
		public Order() {}
		Order(int id, String client) {
			this.id = id;
			this.client = client;
		}
		@Override
		public boolean equals( Object obj ) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Order other = (Order) obj;
			if (client == null) {
				if (other.client != null)
					return false;
			} else if (!client.equals( other.client ))
				return false;
			if (id != other.id)
				return false;
			return true;
		}
	}
	
	private GsonMediaType mediaType;
	private RestClient client = mock(RestClient.class);
	
	@Before
	public void setup() {
		Locale.setDefault(Locale.ENGLISH);
		mediaType = new GsonMediaType()
			.withType(Order.class)
			.withCollection( "orders", new TypeToken<Collection<Order>>(){}.getType() );
		when(client.inflectionRules()).thenReturn(new NounPluralizer());
	}
	
	@Test
	public void shouldDeserializeWithoutLinks() {
		String json = "{\"order\":{\"id\":10,\"client\":\"Jonh Doe\"}}";
		Order expected = new Order(10,"Jonh Doe");
		Order order = mediaType.unmarshal(json, client);
		assertThat(order, is(equalTo(expected)));
	}
	
	@Test
	public void shouldDeserializeCollections() {
		String json = "{\"orders\":[{\"id\":10,\"client\":\"Jonh Doe\"},{\"id\":20,\"client\":\"Jane Doe\"}]}";
		Order expected = new Order(10,"Jonh Doe");
		Order expected2 = new Order(20,"Jane Doe");
		List<Order> orders = mediaType.unmarshal(json, client);
		assertThat(orders, hasItem(equalTo(expected)));
		assertThat(orders, hasItem(equalTo(expected2)));
	}
	
}
