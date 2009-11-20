package br.com.caelum.restfulie.config;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

public class XStreamConfigTest {
	
	private SerializationConfig config;

	@Before
    public void setup() {
		this.config = new SerializationConfig();
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

		private final String notes;

		public AdvancedOrder(Client client, double price, String comments, String notes) {
			super(client, price, comments);
			this.notes = notes;
		}

	}

	@Test
	public void shouldSerializeAllBasicFields() {
		config.type(Order.class);
		Order order = new Order(new Client("guilherme silveira"), 15.0, "pack it nicely, please");
		assertThat(create().toXML(order), containsString("<price>15.0</price>"));
		assertThat(create().toXML(order), containsString("<comments>pack it nicely, please</comments>"));
		assertThat(create().toXML(order), not(containsString("<client>")));
		assertThat(create().toXML(order), not(containsString("<item>")));
	}

	public static enum Type { basic, advanced }
	public static class BasicOrder extends Order {
		public BasicOrder(Client client, double price, String comments, Type type) {
			super(client, price, comments);
			this.type = type;
		}
		private final Type type;
	}

	@Test
	public void shouldSerializeEnumFields() {
		config.type(BasicOrder.class);
		Order order = new BasicOrder(new Client("guilherme silveira"), 15.0, "pack it nicely, please", Type.basic);
		assertThat(create().toXML(order), containsString("<type>basic</type>"));
	}

//	@Test
//	@Ignore("not supported yet")
//	public void shouldSerializeCollectionWithPrefixTag() {
//		String expectedResult = "<order>\n  <price>15.0</price>\n  <comments>pack it nicely, please</comments>\n</order>";
//		expectedResult += expectedResult;
//		expectedResult = "<orders>" + expectedResult + "</orders>";
//		Order order = new Order(new Client("guilherme silveira"), 15.0, "pack it nicely, please");
//		//serializer.from("orders", Arrays.asList(order, order)).serialize();
//		assertThat(result(), is(equalTo(expectedResult)));
//	}
//
//	@Test
//	@Ignore("not supported yet")
//	public void shouldSerializeCollectionWithPrefixTagAndNamespace() {
//		String expectedResult = "<o:order>\n  <o:price>15.0</o:price>\n  <o:comments>pack it nicely, please</o:comments>\n</o:order>";
//		expectedResult += expectedResult;
//		expectedResult = "<o:orders xmlns:o=\"http://www.caelum.com.br/order\">" + expectedResult + "</o:orders>";
//		Order order = new Order(new Client("guilherme silveira"), 15.0, "pack it nicely, please");
////		serializer.from("orders", Arrays.asList(order, order)).namespace("http://www.caelum.com.br/order","o").serialize();
//		assertThat(result(), is(equalTo(expectedResult)));
//	}

	@Test
	public void shouldSerializeParentFields() {
		config.type(AdvancedOrder.class);
		Order order = new AdvancedOrder(null, 15.0, "pack it nicely, please", "complex package");
		assertThat(create().toXML(order), containsString("<notes>complex package</notes>"));
	}

//	@Test
//	public void shouldOptionallyExcludeFields() {
//		String expectedResult = "<order>\n  <comments>pack it nicely, please</comments>\n</order>";
//		Order order = new Order(new Client("guilherme silveira"), 15.0, "pack it nicely, please");
//		serializer.from(order).exclude("price").serialize();
//		assertThat(result(), is(equalTo(expectedResult)));
//	}

	@Test
	public void shouldOptionallyIncludeFieldAndNotItsNonPrimitiveFields() {
		config.type(AdvancedOrder.class).include("client");
		config.type(Client.class);
		Order order = new Order(new Client("guilherme silveira", new Address("R. Vergueiro")), 15.0, "pack it nicely, please");
		assertThat(create().toXML(order), containsString("<name>guilherme silveira</name>"));
		assertThat(create().toXML(order), not(containsString("R. Vergueiro")));
	}
	
//	@Test
//	public void shouldOptionallyIncludeChildField() {
//		Order order = new Order(new Client("guilherme silveira", new Address("R. Vergueiro")), 15.0, "pack it nicely, please");
//		serializer.from(order).include("client", "client.address").serialize();
//		assertThat(result(), containsString("<street>R. Vergueiro</street>"));
//	}
//
//
//	@Test
//	public void shouldOptionallyExcludeChildField() {
//		Order order = new Order(new Client("guilherme silveira"), 15.0, "pack it nicely, please");
//		serializer.from(order).include("client").exclude("client.name").serialize();
//		assertThat(result(), containsString("<client/>"));
//		assertThat(result(), not(containsString("<name>guilherme silveira</name>")));
//	}
	@Test
	public void shouldOptionallyIncludeListChildFields() {
		config.type(Order.class).include("client", "items");
		config.type(Client.class);
		Order order = new Order(new Client("guilherme silveira"), 15.0, "pack it nicely, please",
				new Item("any item", 12.99));
		assertThat(create().toXML(order), containsString("<items>"));
		assertThat(create().toXML(order), containsString("<name>any item</name>"));
		assertThat(create().toXML(order), containsString("<price>12.99</price>"));
		assertThat(create().toXML(order), containsString("</items>"));
	}

	@Test
	public void shouldSupportImplicitCollections() {
		config.type(Order.class).include("client").implicit("items");
		config.type(Client.class);
		Order order = new Order(new Client("guilherme silveira"), 15.0, "pack it nicely, please",
				new Item("any item", 12.99));
		assertThat(create().toXML(order), not(containsString("<items>")));
		assertThat(create().toXML(order), containsString("<name>any item</name>"));
		assertThat(create().toXML(order), containsString("<price>12.99</price>"));
		assertThat(create().toXML(order), not(containsString("</items>")));
	}
//	@Test
//	public void shouldOptionallyExcludeFieldsFromIncludedListChildFields() {
//		Order order = new Order(new Client("guilherme silveira"), 15.0, "pack it nicely, please", new Item("bala", 10.5), new Item("chocolate", 3.3));
//		serializer.from(order).include("items").exclude("items.price").serialize();
//		assertThat(result(), containsString("<item>\n      <name>bala</name>\n    </item>"));
//		assertThat(result(), containsString("<item>\n      <name>chocolate</name>\n    </item>"));
//	}

	private XStream create() {
		return new XStreamConfig(config).create();
	}

}
