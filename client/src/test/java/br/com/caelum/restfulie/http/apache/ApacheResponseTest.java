package br.com.caelum.restfulie.http.apache;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.http.Request;

@RunWith(MockitoJUnitRunner.class)
public class ApacheResponseTest
{
	
	@Mock
	private Response response;
	
	@Mock
	private Request request;
	
	@Mock
	private HttpResponse mockHttpResponse;
	
	@Before
	public void setUp()
	{
		mockHttpResponse = Mockito.mock(HttpResponse.class);
		response = new ApacheResponse(mockHttpResponse ,null, request);
	}
	
	@Test
	public void shouldGetResponseType()
	{
		when(mockHttpResponse.getHeaders("Content-Type")).thenReturn(new Header[]{new Header() {
			
			public String getValue() { return "text/html"; }
			
			public String getName() { return "Content-Type"; }
			
			public HeaderElement[] getElements() throws ParseException { return null; }
			}
		});
		assertEquals("text/html", response.getType());
	}
	
	@Test
	public void shouldReturnToOriginalResponseWhenNoneLocationIsDefined() throws URISyntaxException
	{
		URI origin = new URI("http://default.com");
		when(request.getURI()).thenReturn(origin);
		assertEquals( origin, response.getLocation() );
	}
	
	@Test
	public void shouldGetResponseLocation() throws URISyntaxException
	{
		when(mockHttpResponse.getHeaders("Location")).thenReturn(new Header[]{new Header() {
			
			public String getValue() { return "http://example.com"; }
			
			public String getName() { return "Location"; }
			
			public HeaderElement[] getElements() throws ParseException { return null; }
			}
		});
		assertEquals(new URI("http://example.com"), response.getLocation());
	}
}