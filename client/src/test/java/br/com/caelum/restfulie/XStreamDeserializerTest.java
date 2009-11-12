package br.com.caelum.restfulie;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class XStreamDeserializerTest {
	
	@XStreamAlias("order")
	public static class Order {
		public boolean equals(Object obj) {
			Order result = (Order) obj;
			return true;
		}
	}

	private XStreamDeserializer deserializer;
	
	@Before
	public void setup() {
		deserializer = new XStreamDeserializer();
	}
	
	@Test
	public void shouldDeserializeWithoutLinks() {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><order xmlns=\"http://www.caelum.com.br/restfulie\"></order>";
		Order expected = new Order();
		Order order = deserializer.fromXml(xml);
		assertThat(order, is(equalTo(expected)));
	}

}
