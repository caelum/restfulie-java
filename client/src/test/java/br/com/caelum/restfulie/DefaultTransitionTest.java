package br.com.caelum.restfulie;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class DefaultTransitionTest {
	
	private Deserializer deserializer;

	@Before
	public void setup() {
		this.deserializer = mock(Deserializer.class);
	}
	
	@Test
	public void shouldExecuteAnHttpRequest() {
		DefaultTransition transition = new DefaultTransition("latest", "http://localhost:8080/chapter05-service/order/1", null);
		Response result = transition.execute();
		assertThat(result.getCode(), is(200));
		assertThat(result.getContent(), is("<content/>"));
	}
	
	public static class Payment {
		private double value;
	}

	@Test
	public void shouldParseAnObjectIfDesired() {
		when(deserializer.fromXml("<payment>\n" +
			"  <cardNumber>1234123412341234</cardNumber>\n" +
			"  <cardholderName>guilherme silveira</cardholderName>\n" +
			"  <expiryMonth>11</expiryMonth>\n" +
			"  <expiryYear>12</expiryYear>\n" +
			"</payment>")).thenReturn("my resulting resource");
		DefaultTransition transition = new DefaultTransition("latest", "http://localhost:8080/chapter05-service/order/2/checkPaymentInfo", deserializer);
		Response result = transition.execute();
		assertThat((String) result.getResource(), is("my resulting resource"));
	}


	@Test
	public void shouldAllowMethodOverriding() {
		DefaultTransition transition = new DefaultTransition("checkPayment", "http://localhost:8080/chapter05-service/order/2/checkPayment", null);
		Response result = transition.method("get").execute();
		Payment payment = result.getResource();
		assertThat(payment.value, is(200.50));
	}


	@Test
	public void shouldAllowDeleteInvocations() {
		DefaultTransition transition = new DefaultTransition("cancel", "http://localhost:8080/chapter05-service/order/1", null);
		Response result = transition.execute();
		assertThat(result.getCode(), is(200));
	}

}
