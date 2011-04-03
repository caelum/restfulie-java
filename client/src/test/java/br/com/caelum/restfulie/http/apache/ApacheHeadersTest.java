package br.com.caelum.restfulie.http.apache;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.restfulie.Link;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.http.Header;
import br.com.caelum.restfulie.http.Headers;

@RunWith(MockitoJUnitRunner.class)
public class ApacheHeadersTest {

	@Mock
	private HttpResponse response;
	
	@Mock
	private org.apache.http.Header header;
	
	@Mock
	private RestClient client;
	
	@Test
	public void shouldGetJustTheFirstInformation()
	{
		Headers headers = new ApacheHeaders(response, client);
		
		when(response.getHeaders("Content-Type")).thenReturn(headers());
		assertEquals("text/html", headers.getMain("Content-Type"));
	}
	
	@Test
	public void shouldReturnEmptyWhenNoneContentTypeIsDeclared()
	{
		Headers headers = new ApacheHeaders(response, client);
		assertEquals("", headers.getFirst("Content-Type"));
	}
	
	private org.apache.http.Header[] headers()
	{
		return new org.apache.http.Header[]{
			new ContentTypeHeader("text/html"), new ContentTypeHeader("text/xml")
		};
	}
	
	@Test
	public void shouldReturnsAllTheLinksOfTheHeader() {
		//Given
		when(header.getName()).thenReturn("link");
		when(header.getValue()).thenReturn("<http://amundsen.com/examples/mazes/2d/five-by-five/5:east>;   rel=\"current\",<http://amundsen.com/examples/mazes/2d/five-by-five/0:west>;  rel=\"west\",<http://amundsen.com/examples/mazes/2d/five-by-five/10:east>; rel=\"east\"");
		when(response.getHeaders("link")).thenReturn(new org.apache.http.Header[] {header});
		
		//When
		List<Link> links = new ApacheHeaders(response,client).getLinks();
		
		//Then
		assertThat(links.size(), is(equalTo(3)));
		
		assertThat(links.get(0).getHref(), is(equalTo("http://amundsen.com/examples/mazes/2d/five-by-five/5:east")));
		assertThat(links.get(0).getRel(), is(equalTo("current")));
		
		assertThat(links.get(1).getHref(), is(equalTo("http://amundsen.com/examples/mazes/2d/five-by-five/0:west")));
		assertThat(links.get(1).getRel(), is(equalTo("west")));
		
		assertThat(links.get(2).getHref(), is(equalTo("http://amundsen.com/examples/mazes/2d/five-by-five/10:east")));
		assertThat(links.get(2).getRel(), is(equalTo("east")));
	}
	
	@Test
	public void shouldReturnALinkGivenTheRelValue() {
		//Given
		when(header.getName()).thenReturn("link");
		when(header.getValue()).thenReturn("<http://amundsen.com/examples/mazes/2d/five-by-five/5:east>; rel=\"current\",<http://amundsen.com/examples/mazes/2d/five-by-five/0:west>; rel=\"west\",<http://amundsen.com/examples/mazes/2d/five-by-five/10:east>; rel=\"east\"");
		when(response.getHeaders("link")).thenReturn(new org.apache.http.Header[] {header});
		
		//When
		Link link = new ApacheHeaders(response,client).getLink("west");
		
		//Then
		assertThat(link.getHref(), is(equalTo("http://amundsen.com/examples/mazes/2d/five-by-five/0:west")));
		assertThat(link.getRel(), is(equalTo("west")));
	};
	
	
	@Test
	public void shouldBeIterable() {
		
		when(header.getName()).thenReturn("Accept");
		when(header.getValue()).thenReturn("application/xml");
		
		org.apache.http.Header apacheHeader2 = mock(org.apache.http.Header.class);
		
		when(apacheHeader2.getName()).thenReturn("Content-type");
		when(apacheHeader2.getValue()).thenReturn("application/xml");
		when(response.getAllHeaders()).thenReturn(new org.apache.http.Header[] {header,apacheHeader2});
		
		Headers headers = new ApacheHeaders(response,client);

		for(Header h : headers) {
			System.out.println(h.getName());
			System.out.println(h.getValue());
		}
		
		Iterator<Header> iterator = headers.iterator();
		
		Header header = iterator.next();
		
		assertThat("Accept", is(equalTo(header.getName())));
		assertThat("application/xml", is(equalTo(header.getValue())));
		
		Header header2 = iterator.next();
		
		assertThat("Content-type", is(equalTo(header2.getName())));
		assertThat("application/xml", is(equalTo(header2.getValue())));

	}
}
