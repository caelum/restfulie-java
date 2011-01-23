package br.com.caelum.restfulie.feature;

import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.http.Request;
import br.com.caelum.restfulie.http.error.RedicetionException;

@RunWith(MockitoJUnitRunner.class)
public class ThrowErrorTest {

	
	@Mock
	private Request request;
	
	@Mock
	private Response response;
	
	@Test(expected=RedicetionException.class)
	public void shouldThrowRedirectionExceptionWhenCodeBetween300And399() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(300);
		
		//When
		new ThrowError(null).process(null, response);
	}
	
}
