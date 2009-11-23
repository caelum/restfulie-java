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
		DefaultTransition transition = new DefaultTransition("latest", "http://localhost:8080/chapter05-service/order/1", null, null);
		Response result = transition.execute();
		assertThat(result.getCode(), is(200));
		assertThat(result.getContent(), is("<content/>"));
	}
	
	@Test
	public void shouldParseAnObjectIfDesired() throws IOException {
		when(deserializer.fromXml(defaultPayment)).thenReturn("my resulting resource");
		DefaultTransition transition = new DefaultTransition("latest", "http://localhost:8080/chapter05-service/order/2/checkPaymentInfo", deserializer, null);
		Response result = transition.execute();
		assertThat((String) result.getResource(), is("my resulting resource"));
	}


	@Test
	public void shouldAllowMethodOverriding() throws IOException {
		DefaultTransition transition = new DefaultTransition("checkPayment", "http://localhost:8080/chapter05-service/order/2/checkPaymentInfo", null, null);
		Response result = transition.method(HttpMethod.GET).execute();
		assertThat(result.getContent(), is(defaultPayment));
	}


	@Test
	public void shouldAllowDeleteInvocations() {
		DefaultTransition transition = new DefaultTransition("cancel", "http://localhost:8080/chapter05-service/order/1", null, null);
		Response result = transition.execute();
		assertThat(result.getCode(), is(200));
	}

}
