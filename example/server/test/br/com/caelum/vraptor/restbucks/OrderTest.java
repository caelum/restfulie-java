package br.com.caelum.vraptor.restbucks;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Calendar;

import org.junit.Test;

public class OrderTest {
	
	@Test
	public void whenOrderIsPaidMoreThan10SecondsAgoItIsReady() {
		
		Payment payment = mock(Payment.class);
		
		when(payment.getAmount()).thenReturn(BigDecimal.ZERO);
		when(payment.getCreatedAt()).thenReturn(someSecondsAgo(15));
		
		Order o = new Order();
		o.pay(payment);
		
		assertThat(o.isReady(), is(true));
		
	}

	@Test
	public void whenOrderIsPaidLessThan10SecondsAgoItIsNotReadyYet() {
		
		Payment payment = mock(Payment.class);
		
		when(payment.getAmount()).thenReturn(BigDecimal.ZERO);
		when(payment.getCreatedAt()).thenReturn(someSecondsAgo(3));
		
		Order o = new Order();
		o.pay(payment);
		
		assertThat(o.isReady(), is(false));
		
	}

	private Calendar someSecondsAgo(int secs) {
		Calendar fifteenSecondsAgo = Calendar.getInstance();
		fifteenSecondsAgo.add(Calendar.SECOND, -secs);
		return fifteenSecondsAgo;
	}

}
