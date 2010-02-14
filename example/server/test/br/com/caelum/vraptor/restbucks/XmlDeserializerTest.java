package br.com.caelum.vraptor.restbucks;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Test;

import com.thoughtworks.xstream.XStream;

public class XmlDeserializerTest {
	
	private String item() {
		return	"    <item>"+
		"      <drink>LATTE</drink>"+
		"      <milk>SEMI</milk>"+
		"      <size>LARGE</size>"+
		"    </item>";
	}
	
	private InputStream orderXml(String items) {
		return streamFor("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"+
		"<order>"+
		"  <id>510</id>"+
		"  <location>TO_TAKE</location>"+
		"  <items>"+
			items +
		"  </items>"+
		" </order>");
	}

	@Test
	public void shouldBeCapableOfDeserializingBasicData() {
		Order order = deserialize(orderXml(""));
		assertThat(order.getId(), equalTo("510"));
		assertThat(order.getLocation(), equalTo(Order.Location.TO_TAKE));
		assertThat(order.getItems().size(), equalTo(0));
	}

	private Order deserialize(InputStream input) {
		XStream deserializer = createXStream();
		Order order = (Order) deserializer.fromXML(input);
		return order;
	}

	private XStream createXStream() {
		return new XmlDeserializer(null).getXStream();
	}
	
	private InputStream streamFor(String base) {
		return new ByteArrayInputStream(base.getBytes());
	}

	@Test
	public void shouldBeCapableOfReadingAnItem() {
		Order order = deserialize(orderXml(item()));
		assertThat(order.getItems().size(), equalTo(1));
		Item item = order.getItems().get(0);
		assertThat(item.getDrink(), equalTo(Item.Coffee.LATTE));
		assertThat(item.getMilk(), equalTo(Item.Milk.SEMI));
		assertThat(item.getSize(), equalTo(Item.Size.LARGE));
	}

	@Test
	public void shouldBeCapableOfReadingItems() {
		Order order = deserialize(orderXml(item() + item()));
		assertThat(order.getItems().size(), equalTo(2));
	}
}
