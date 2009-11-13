package br.com.caelum.restfulie;

import org.junit.Test;

public class DefaultTransitionTest {
	
	@Test
	public void shouldExecuteAnHttpRequest() {
		DefaultTransition transition = new DefaultTransition("cancel", "http://localhost:8080/chapter05-service/order/1");
		transition.execute();
	}

}
