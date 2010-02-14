package br.com.caelum.vraptor.restbucks;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

import org.junit.Test;

import com.thoughtworks.xstream.XStream;

public class XmlDeserializerTest {
	
	private String item() {
		return	"    <item>"+
		"      <created-at>2010-02-10T21:39:38Z</created-at>"+
		"      <drink>latte</drink>"+
		"      <id>1140</id>"+
		"      <milk>DOUBLE</milk>"+
		"      <size>LARGE</size>"+
		"      <updated-at>2010-02-10T21:39:38Z</updated-at>"+
		"      <atom:link href=\"http://localhost:3000/orders/510\" xmlns:atom=\"http://www.w3.org/2005/Atom\" rel=\"self\"/>"+
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
		InputStream input = orderXml("");
		XStream deserializer = createXStream();
		Order order = (Order) deserializer.fromXML(input);
		assertThat(order.getId(), equalTo("510"));
		assertThat(order.getLocation(), equalTo(Order.Location.TO_TAKE));
		assertThat(order.getItems().size(), equalTo(0));
	}

	private XStream createXStream() {
		return new XmlDeserializer(null).getXStream();
	}
	
	private InputStream streamFor(String base) {
		return new ByteArrayInputStream(base.getBytes());
	}

	@Test
	public void shouldBeCapableOfReadingAnItem() {
		InputStream input = orderXml(item());
	}
}
