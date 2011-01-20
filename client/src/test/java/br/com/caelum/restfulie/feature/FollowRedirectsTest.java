package br.com.caelum.restfulie.feature;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.http.Request;

@RunWith(MockitoJUnitRunner.class)
public class FollowRedirectsTest {

	@Mock
	private Response redirectResponse, response;
	@Mock
	private RestClient client;
	@Mock
	private Request request, oldRequest;

	@Test
	public void shouldFollowAbsoluteLocation() {
		String uri = "http://mylocation.com";
		when(response.getCode()).thenReturn(302);
		when(response.getHeader("Location")).thenReturn(Arrays.asList(uri));
		when(client.at(uri)).thenReturn(request);
		when(request.get()).thenReturn(redirectResponse);
		
		assertThat(new FollowRedirects(client).process(null, response), is(equalTo(redirectResponse)));
		
	}

	@Test
	public void shouldNotFollowOtherCodes() {
		when(response.getCode()).thenReturn(202);
		assertThat(new FollowRedirects(client).process(null, response), is(equalTo(response)));
	}

	@Test
	public void shouldFollowRelativeLocations() throws URISyntaxException {
		
		String uri = "/client";
		when(response.getCode()).thenReturn(302);
		when(response.getHeader("Location")).thenReturn(Arrays.asList(uri));
		when(response.getRequest()).thenReturn(oldRequest);
		when(oldRequest.getURI()).thenReturn(new URI("http://caelum.com.br/everything_else"));
		when(client.at(new URI("http://caelum.com.br/client"))).thenReturn(request);
		when(request.get()).thenReturn(redirectResponse);
		
		assertThat(new FollowRedirects(client).process(null, response), is(equalTo(redirectResponse)));
		
	}
}
