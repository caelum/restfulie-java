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
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.Restfulie;
import br.com.caelum.restfulie.config.XStreamConfigTest.Item;
import br.com.caelum.restfulie.http.JsonMediaType;
import br.com.caelum.restfulie.http.XmlMediaType;

@SuppressWarnings("unchecked")
public class EntryTest {
	
	private RestClient restfulie;

	@Before
	public void setup() {
		this.restfulie = Restfulie.custom();
		this.restfulie.getMediaTypes().register(new MyXmlMediaType());
		this.restfulie.getMediaTypes().register(new MyJsonMediaType());
	}
	
	class MyXmlMediaType extends XmlMediaType {
		protected List<Class> getTypesToEnhance() {
			List<Class> list = new ArrayList<Class>();
			list.add(Item.class);
			return list;
		}
	}
	
	class MyJsonMediaType extends JsonMediaType {
		protected List<Class> getTypesToEnhance() {
			List<Class> list = new ArrayList<Class>();
			list.add(Item.class);
			return list;
		}
	}
	
	@Test
	public void shouldBeAbleToGetAndUnmarshall() throws IOException, URISyntaxException {
		Response response = restfulie.at("http://localhost:3000/restfulie/items").accept("application/xml").get();
		List<Item> items = response.getResource();
		assertThat(items.get(0).getName(), is(equalTo("Chave")));
		assertThat(response.getCode(), is(200));
	}
	
	@Test
	public void shouldPostCreatingResource() throws IOException, URISyntaxException {
		Response response = restfulie.at("http://localhost:3000/restfulie/items").accept("application/xml").as("application/xml").post(new Item("rest training", 1500.00));
		Item item = response.getResource();
		assertThat(item.getName(), is(equalTo("rest training")));
		assertThat(response.getCode(), is(200));

		response = restfulie.at("http://localhost:3000/restfulie/items").get();
		List<Item> items = response.getResource();
		assertTrue(itemIsThere(items, "rest training"));
	}

	private boolean itemIsThere(List<Item> items, String name) {
		for(Item i : items) {
			if(i.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	@Test
	public void shouldSupportJson() throws IOException, URISyntaxException {
		Response response = restfulie.at("http://localhost:3000/restfulie/items").accept("application/json").as("application/json").post(new Item("rest training json", 1500.00));
		System.out.println(response.getContent());
		Item item = response.getResource();
		assertThat(item.getName(), is(equalTo("rest training json")));
		assertThat(response.getCode(), is(200));

		response = restfulie.at("http://localhost:3000/restfulie/items").accept("application/json").get();
		System.out.println(response.getContent());
		List<Item> items = response.getResource();
		assertTrue(itemIsThere(items, "rest training json"));
	}


}
