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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.restfulie.http.HttpMethod;
import br.com.caelum.restfulie.unmarshall.Deserializer;

public class DefaultTransitionTest {
	
	private Deserializer deserializer;
	private String defaultPayment;

	@Before
	public void setup() {
		this.deserializer = mock(Deserializer.class);
		this.defaultPayment = "<payment>\n" +
			"  <cardNumber>1234123412341234</cardNumber>\n" +
			"  <cardholderName>guilherme silveira</cardholderName>\n" +
			"  <expiryMonth>11</expiryMonth>\n" +
			"  <expiryYear>12</expiryYear>\n" +
			"</payment>";
	}
	
	@Test
	public void shouldExecuteAnHttpRequest() throws IOException {
		DefaultRelation transition = new DefaultRelation("latest", "http://localhost:8080/chapter05-service/order/1", null, null);
		Response result = transition.access();
		assertThat(result.getCode(), is(200));
		assertThat(result.getContent(), is("<content/>"));
	}
	
	@Test
	public void shouldParseAnObjectIfDesired() throws IOException {
		when(deserializer.fromXml(defaultPayment)).thenReturn("my resulting resource");
		DefaultRelation transition = new DefaultRelation("latest", "http://localhost:8080/chapter05-service/order/2/checkPaymentInfo", deserializer, null);
		Response result = transition.access();
		assertThat((String) result.getResource(), is("my resulting resource"));
	}


	@Test
	public void shouldAllowMethodOverriding() throws IOException {
		DefaultRelation transition = new DefaultRelation("checkPayment", "http://localhost:8080/chapter05-service/order/2/checkPaymentInfo", null, null);
		Response result = transition.method(HttpMethod.GET).access();
		assertThat(result.getContent(), is(defaultPayment));
	}


	@Test
	public void shouldAllowDeleteInvocations() {
		DefaultRelation transition = new DefaultRelation("cancel", "http://localhost:8080/chapter05-service/order/1", null, null);
		Response result = transition.access();
		assertThat(result.getCode(), is(200));
	}

}
