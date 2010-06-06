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

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.restfulie.config.XStreamConfigTest.Item;
import br.com.caelum.restfulie.http.XmlMediaType;

import com.thoughtworks.xstream.XStream;

public class DefaultTransitionTest {
	
	private String defaultPayment;
	private RestClient restfulie;

	@Before
	public void setup() {
		this.defaultPayment = "<payment>\n" +
			"  <cardNumber>1234123412341234</cardNumber>\n" +
			"  <cardholderName>guilherme silveira</cardholderName>\n" +
			"  <expiryMonth>11</expiryMonth>\n" +
			"  <expiryYear>12</expiryYear>\n" +
			"</payment>";
		this.restfulie = Restfulie.custom();
		this.restfulie.getMediaTypes().register(new MyXmlMediaType());
	}
	
	class MyXmlMediaType extends XmlMediaType {
		@SuppressWarnings("unchecked")
		protected List<Class> getTypesToEnhance() {
			List<Class> list = new ArrayList<Class>();
			list.add(Item.class);
			return list;
		}
	}
	
	@Test
	public void shouldExecuteASimpleHttpRequest() throws IOException, URISyntaxException {
		Response response = restfulie.at("http://localhost:3000/restfulie/items").accept("application/xml").get();
		System.out.println(response.getContent());
		List<Item> items = response.getResource();
		assertThat(response.getCode(), is(200));
	}
	
}
