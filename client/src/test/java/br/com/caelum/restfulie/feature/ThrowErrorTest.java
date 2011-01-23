package br.com.caelum.restfulie.feature;

import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.http.Request;
import br.com.caelum.restfulie.http.error.BadRequestException;
import br.com.caelum.restfulie.http.error.ForbiddenException;
import br.com.caelum.restfulie.http.error.RedicetionException;
import br.com.caelum.restfulie.http.error.UnauthorizedException;

@RunWith(MockitoJUnitRunner.class)
public class ThrowErrorTest {

	
	@Mock
	private Request request;
	
	@Mock
	private Response response;
	
	@Test(expected=RedicetionException.class)
	public void shouldThrowRedirectionExceptionWhenCodeBetween300() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(300);
		
		//When
		new ThrowError(null).process(null, response);
	}
	
	@Test(expected=RedicetionException.class)
	public void shouldThrowRedirectionExceptionWhenCode350() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(350);
		
		//When
		new ThrowError(null).process(null, response);
	}
	
	@Test(expected=RedicetionException.class)
	public void shouldThrowRedirectionExceptionWhenCode399() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(399);
		
		//When
		new ThrowError(null).process(null, response);
	}
	
	@Test(expected=BadRequestException.class)
	public void shouldThrowBadRequestWhenCode400() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(400);
		
		//When
		new ThrowError(null).process(null, response);
	}
	
	@Test(expected=UnauthorizedException.class)
	public void shouldThrowBadRequestWhenCode401() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(401);
		
		//When
		new ThrowError(null).process(null, response);
	}
	
	
	
}
