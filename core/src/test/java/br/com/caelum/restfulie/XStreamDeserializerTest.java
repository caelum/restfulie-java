/***
 * Copyright (c) 2009 Caelum - www.caelum.com.br/opensource - guilherme.silveira@caelum.com.br
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.caelum.restfulie;

import static br.com.caelum.restfulie.Restfulie.resource;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.restfulie.config.SerializationConfig;
import br.com.caelum.restfulie.config.XStreamConfig;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;


public class XStreamDeserializerTest {
	
	@XStreamAlias("order")
	public static class Order {
		public boolean equals(Object obj) {
			return Order.class.isAssignableFrom(obj.getClass());
		}
	}

	private XStreamDeserializer deserializer;
	
	@Before
	public void setup() {
		XStreamConfig config = new XStreamConfig(new SerializationConfig()) {
			protected XStream getXStream() {
				XStream stream = super.getXStream();
				stream.processAnnotations(Order.class);
				return stream;
			}
		};
		config.enhanceResource(Order.class);
		this.deserializer = new XStreamDeserializer(config);
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
		Resource resource = Restfulie.resource(deserializer.fromXml(xml));
		assertThat(resource.getRelations().size(), is(equalTo(1)));
		Relation first = resource.getRelations().get(0);
		assertThat(first.getRel(), is(equalTo("payment")));
		assertThat(first.getHref(), is(equalTo("http://localhost/pay")));
	}

	@Test
	public void shouldSupportTheLinkWithoutTheXmlns() {
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><order xmlns=\"http://www.caelum.com.br/restfulie\" xmlns:atom=\"http://www.w3.org/2005/Atom\">" + simpleLinkFor("payment", "http://localhost/pay") + "</order>";
		Resource resource = resource(deserializer.fromXml(xml));
		assertThat(resource.getRelations().size(), is(equalTo(1)));
		Relation first = resource.getRelations().get(0);
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
		assertThat(resource.getRelations().size(), is(equalTo(2)));
		Relation first = resource.getRelations().get(0);
		assertThat(first.getRel(), is(equalTo("payment")));
		assertThat(first.getHref(), is(equalTo("http://localhost/pay")));
		Relation second = resource.getRelations().get(1);
		assertThat(second.getRel(), is(equalTo("cancel")));
		assertThat(second.getHref(), is(equalTo("http://localhost/cancel")));
	}

	private String linkFor(String rel, String uri) {
		return "<atom:link xmlns:atom=\"http://www.w3.org/2005/Atom\" rel=\"" + rel + "\" href=\"" + uri + "\"/>";
	}

	private String simpleLinkFor(String rel, String uri) {
		return "<atom:link rel=\"" + rel + "\" href=\"" + uri + "\"/>";
	}

}
