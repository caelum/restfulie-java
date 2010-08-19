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

package br.com.caelum.restfulie.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.Restfulie;
import br.com.caelum.restfulie.mediatype.JsonMediaType;
import br.com.caelum.restfulie.mediatype.XmlMediaType;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@SuppressWarnings("unchecked")
public class EntryTest {

	private RestClient restfulie;

	@Before
	public void setup() {
		this.restfulie = Restfulie.custom();
		this.restfulie.getMediaTypes().register(new MyXmlMediaType());
		this.restfulie.getMediaTypes().register(new MyJsonMediaType());
	}

	@XStreamAlias("item")
	public static class Item {

		private Long id;
		private String name;
		private double price;

		public Item(String name, double price) {
			this.name = name;
			this.price = price;
		}

		public String getName() {
			return name;
		}

	}
	class MyXmlMediaType extends XmlMediaType {
		@Override
		protected List<Class> getTypesToEnhance() {
			return Arrays.<Class>asList(Item.class);
		}

		@Override
		protected void configure(XStream xstream) {
			xstream.alias("items", ArrayList.class);
		}
	}

	class MyJsonMediaType extends JsonMediaType {
		@Override
		protected List<Class> getTypesToEnhance() {
			return Arrays.<Class>asList(Item.class);
		}

		@Override
		protected void configure(XStream xstream) {
			xstream.alias("items", ArrayList.class);
//			xstream.processAnnotations(Item.class);
//			xstream.registerConverter(new CollectionConverter(xstream
//					.getMapper()) {
//				@Override
//				protected void populateCollection(
//						HierarchicalStreamReader reader,
//						UnmarshallingContext context, Collection collection) {
//					String name = reader.getNodeName();
//					System.out.println(reader.getAttributeCount());
//					System.out.println();
//					while (reader.hasMoreChildren()) {
//						reader.moveDown();
////						System.out.println(reader.getNodeName());
////						System.out.println(reader.getAttributeCount());
//						Object item = readItem(reader, context, collection, name);
////						ISSO AQUI NAO TA FUNCIONANDO, ELE NAO TA PEGANDO
//						collection.add(item);
//						reader.moveUp();
//					}
//				}
//
//				protected Object readItem(HierarchicalStreamReader reader,
//						UnmarshallingContext context, Object current, String name) {
//					String classAttribute = reader.getAttribute(mapper()
//							.aliasForAttribute("class"));
//					Class type;
//					if(name.equals("items")) {
//						type = Item.class;
//					}else if (classAttribute == null) {
//						type = mapper().realClass(reader.getNodeName());
//					} else {
//						type = mapper().realClass(classAttribute);
//					}
//					Object result = context.convertAnother(current, type);
//					return result;
//				}
//			});
		}
	}

	@Test
	public void shouldBeAbleToGetAndUnmarshall() throws IOException,
			URISyntaxException {
		Response response = restfulie.at(
				"http://localhost:8080/restfulie/items").accept(
				"application/xml").get();
		List<Item> items = response.getResource();
		assertThat(items.get(0).getName(), is(equalTo("Chave")));
		assertThat(response.getCode(), is(200));
	}

	//
	// @Test
	// public void shouldBeAbleToIgnoreFields() throws IOException,
	// URISyntaxException {
	// Response response =
	// restfulie.at("http://localhost:3000/restfulie/items").accept("application/xml").get();
	// List<Item> items = response.getResource();
	// assertThat(Restfulie.resource(items.get(0)).getUnknownProperty("price"),
	// is(equalTo("20.0")));
	// assertThat(response.getCode(), is(200));
	// }
	//
	@Test
	public void shouldPostCreatingResource() throws IOException,
			URISyntaxException {
		Response response = restfulie.at(
				"http://localhost:8080/restfulie/items").accept(
				"application/xml").as("application/xml").post(
				new Item("rest training", 1500.00));
		Item item = response.getResource();

		assertThat(item.getName(), is(equalTo("rest training")));
		assertThat(response.getCode(), is(200));

		response = restfulie.at("http://localhost:8080/restfulie/items").get();
		List<Item> items = response.getResource();
		assertTrue(itemIsThere(items, "rest training"));
	}

	private boolean itemIsThere(List<Item> items, String name) {
		for (Item i : items) {
			if (i.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	@Test
	@Ignore("not yet")
	public void shouldSupportJson() throws IOException, URISyntaxException {
		Response response = restfulie
			.at("http://localhost:8080/restfulie/items")
			.accept("application/json")
			.as("application/json")
			.post(new Item("rest training json", 1500.00));

		Item item = response.getResource();
		assertThat(item.getName(), is(equalTo("rest training json")));
		assertThat(response.getCode(), is(200));

		response = restfulie.at("http://localhost:8080/restfulie/items")
				.accept("application/json").get();
		System.out.println(response.getContent());
		List<Item> items = response.getResource();
		assertTrue(itemIsThere(items, "rest training json"));
	}

}
