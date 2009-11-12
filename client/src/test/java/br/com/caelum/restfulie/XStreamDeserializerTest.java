package br.com.caelum.restfulie;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.thoughtworks.xstream.XStream;
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
		deserializer = new XStreamDeserializer() {
			@Override
			protected XStream getXStream() {
				XStream stream = super.getXStream();
				stream.processAnnotations(Order.class);
				return stream;
			}
		};
		deserializer.enhanceResource(Order.class);
	}
	
	@Test
	public void shouldDeserializeWithoutLinks() {

		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><order xmlns=\"http://www.caelum.com.br/restfulie\"></order>";
		Order expected = new Order();
		Order order = (Order) deserializer.fromXml(xml);
		assertThat(order, is(equalTo(expected)));
	}

	@Test
	public void shouldDeserializeWithASimpleLink() {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><order xmlns=\"http://www.caelum.com.br/restfulie\">" + linkFor("payment", "http://localhost/pay") + "</order>";
		Resource resource = resource(deserializer.fromXml(xml));
		assertThat(resource.getTransitions().size(), is(equalTo(1)));
		Transition first = resource.getTransitions().get(0);
		assertThat(first.getRel(), is(equalTo("payment")));
		assertThat(first.getHref(), is(equalTo("http://localhost/pay")));
	}

	@Test
	public void shouldDeserializeWithTwoLinks() {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><order xmlns=\"http://www.caelum.com.br/restfulie\">" 
			+ linkFor("payment", "http://localhost/pay") 
			+ linkFor("cancel", "http://localhost/cancel") 
			+ "</order>";
		Resource resource = resource(deserializer.fromXml(xml));
		assertThat(resource.getTransitions().size(), is(equalTo(2)));
		Transition first = resource.getTransitions().get(0);
		assertThat(first.getRel(), is(equalTo("payment")));
		assertThat(first.getHref(), is(equalTo("http://localhost/pay")));
		Transition second = resource.getTransitions().get(1);
		assertThat(second.getRel(), is(equalTo("cancel")));
		assertThat(second.getHref(), is(equalTo("http://localhost/cancel")));
	}

	private static <T> Resource resource(T object) {
		return (Resource) object;
	}

	private String linkFor(String rel, String uri) {
		return "<atom:link xmlns:atom=\"http://www.w3.org/2005/Atom\" rel=\"" + rel + "\" href=\"" + uri + "\"/>";
	}

}
