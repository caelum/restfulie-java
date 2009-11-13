package br.com.caelum.restfulie;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class DefaultTransitionTest {
	
	@Test
	public void shouldExecuteAnHttpRequest() {
		DefaultTransition transition = new DefaultTransition("latest", "http://localhost:8080/chapter05-service/order/1");
		Response result = transition.execute();
		assertThat(result.getCode(), is(200));
		assertThat(result.getContent(), is("<content/>"));
	}
	
	public static class Payment {
		private double value;
	}

	@Test
	public void shouldParseAnObjectIfDesired() {
		DefaultTransition transition = new DefaultTransition("latest", "http://localhost:8080/chapter05-service/order/1/checkPayment");
		Response result = transition.execute();
		Payment payment = result.getResource();
		assertThat(payment.value, is(200.50));
	}


	@Test
	public void shouldAllowMethodOverriding() {
		DefaultTransition transition = new DefaultTransition("checkPayment", "http://localhost:8080/chapter05-service/order/1/checkPayment");
		Response result = transition.method("get").execute();
		Payment payment = result.getResource();
		assertThat(payment.value, is(200.50));
	}


	@Test
	public void shouldAllowDeleteInvocations() {
		DefaultTransition transition = new DefaultTransition("cancel", "http://localhost:8080/chapter05-service/order/1");
		Response result = transition.execute();
		assertThat(result.getCode(), is(200));
	}

}
