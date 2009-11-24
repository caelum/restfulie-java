package br.com.caelum.restfulie.serializer;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class XStreamXmlSerializerTest {

	private BasicSerializer serializer;
	private ByteArrayOutputStream stream;

	@Before
    public void setup() {
        this.stream = new ByteArrayOutputStream();
        XStream xstream = new XStream();
        xstream.processAnnotations(Order.class);
        xstream.processAnnotations(Item.class);
		this.serializer = new XStreamXmlSerializer(xstream, new OutputStreamWriter(stream));
    }

	public static class Address {
		String street;
		public Address(String street) {
			this.street = street;
		}
	}
	
	public static class Client {
		String name;
		Address address;
		public Client(String name) {
			this.name = name;
		}
		public Client(String name, Address address) {
			this.name = name;
			this.address = address;
		}
	}
	
	@XStreamAlias("item")
	public static class Item {
		String name;
		double price;
		public Item(String name, double price) {
			this.name = name;
			this.price = price;
		}
	}
	
	@XStreamAlias("order")
	public static class Order {
		Client client;
		double price;
		String comments;
		List<Item> items;

		public Order(Client client, double price, String comments, Item... items) {
			this.client = client;
			this.price = price;
			this.comments = comments;
			this.items = new ArrayList<Item>(Arrays.asList(items));
		}
		public String nice() {
			return "nice output";
		}

	}
	public static class AdvancedOrder extends Order{

		@SuppressWarnings("unused")
		private final String notes;

		public AdvancedOrder(Client client, double price, String comments, String notes) {
			super(client, price, comments);
			this.notes = notes;
		}

	}

	@Test
	public void shouldSerializeEverything() {
		String expectedResult = "<order>\n  <client>\n    <name>guilherme silveira</name>\n  </client>\n  <price>15.0</price>\n  <comments>pack it nicely, please</comments>\n  <items/>\n</order>";
		Order order = new Order(new Client("guilherme silveira"), 15.0, "pack it nicely, please");
		serializer.from(order).serialize();
		assertThat(result(), is(equalTo(expectedResult)));
	}


	@Test
	public void shouldSerializeParentFields() {
		Order order = new AdvancedOrder(null, 15.0, "pack it nicely, please", "complex package");
		serializer.from(order).serialize();
		assertThat(result(), containsString("<notes>complex package</notes>"));
	}

	private String result() {
		return new String(stream.toByteArray());
	}


}
