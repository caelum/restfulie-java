package br.com.caelum.restfulie.feature;


import static br.com.caelum.restfulie.feature.Features.retryWhenUnavaiable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.Restfulie;
import br.com.caelum.restfulie.http.Request;
import br.com.caelum.restfulie.request.RequestChain;

@RunWith(MockitoJUnitRunner.class)
public class RetryWhenUnavailableTest {

	@Mock
	private Request request;
	
	@Mock
	private RequestChain chain;
	
	@Mock
	private Response response;
	
	@Test
	public void shouldRetryWhenCode503() throws URISyntaxException {
		String verb = "get";
		URI uri = new URI("http://someplace.com");
		
		when(chain.next(request, verb, uri, null)).thenReturn(response);
		when(response.getCode()).thenReturn(503);
		
		new  RetryWhenUnavailable().process(chain, request, verb, uri, null);
		
		verify(chain, times(2)).next(request, verb, uri, null);
	}
	
	@Test
	public void shouldNotRetryWhenUnkonwCode() throws URISyntaxException {
		String verb = "get";
		URI uri = new URI("http://someplace.com");
		
		when(chain.next(request, verb, uri, null)).thenReturn(response);
		when(response.getCode()).thenReturn(200);
		
		new  RetryWhenUnavailable().process(chain, request, verb, uri, null);
		
		verify(chain, times(1)).next(request, verb, uri, null);
	}
	
	@Ignore
	public void dslText() {
		Restfulie.at("here").with(retryWhenUnavaiable()).get();
	}
	
}
