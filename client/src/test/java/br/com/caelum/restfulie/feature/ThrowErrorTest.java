package br.com.caelum.restfulie.feature;

import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.http.Request;
import br.com.caelum.restfulie.http.error.BadRequestException;
import br.com.caelum.restfulie.http.error.ForbiddenException;
import br.com.caelum.restfulie.http.error.MethodNotAllowedException;
import br.com.caelum.restfulie.http.error.NotFoundException;
import br.com.caelum.restfulie.http.error.RedicetionException;
import br.com.caelum.restfulie.http.error.UnauthorizedException;
import br.com.caelum.restfulie.request.ResponseChain;

@RunWith(MockitoJUnitRunner.class)
public class ThrowErrorTest {

	
	@Mock
	private RestClient client;
	
	@Mock
	private Request request;
	
	@Mock
	private ResponseChain chain;
	
	@Mock
	private Response response;
	
	@Test(expected=RedicetionException.class)
	public void shouldThrowRedirectionExceptionWhenCodeBetween300() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(300);
		
		//When
		new ThrowError(client).process(chain, response);
	}
	
	@Test(expected=RedicetionException.class)
	public void shouldThrowRedirectionExceptionWhenCode350() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(350);
		
		//When
		new ThrowError(client).process(chain, response);
	}
	
	@Test(expected=RedicetionException.class)
	public void shouldThrowRedirectionExceptionWhenCode399() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(399);
		
		//When
		new ThrowError(client).process(chain, response);
	}
	
	@Test(expected=BadRequestException.class)
	public void shouldThrowBadRequestWhenCode400() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(400);
		
		//When
		new ThrowError(client).process(chain, response);
	}
	
	@Test(expected=UnauthorizedException.class)
	public void shouldThrowBadRequestWhenCode401() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(401);
		
		//When
		new ThrowError(client).process(chain, response);
	}
	
	@Test(expected=ForbiddenException.class)
	public void shouldThrowBadRequestWhenCode403() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(403);
		
		//When
		new ThrowError(client).process(chain, response);
	}
	
	@Test(expected=NotFoundException.class)
	public void shouldThrowBadRequestWhenCode404() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(404);
		
		//When
		new ThrowError(client).process(chain, response);
	}
	
	@Test(expected=MethodNotAllowedException.class)
	public void shouldThrowBadRequestWhenCode405() {
		//Given
		when(request.get()).thenReturn(response);
		when(response.getCode()).thenReturn(405);
		
		//When
		new ThrowError(client).process(chain, response);
	}
	
	
	
}
