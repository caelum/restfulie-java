package br.com.caelum.restfulie.http.apache;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.restfulie.Link;
import br.com.caelum.restfulie.RestClient;

@RunWith(MockitoJUnitRunner.class)
public class ApacheHeadersTest {

	@Mock
	private HttpResponse response;
	
	@Mock
	private Header header;
	
	@Mock
	private RestClient client;

	@Test
	public void shouldReturnsAllTheLinksOfTheHeader() {
		//Given
		when(header.getName()).thenReturn("link");
		when(header.getValue()).thenReturn("<http://amundsen.com/examples/mazes/2d/five-by-five/5:east>; rel=\"current\",<http://amundsen.com/examples/mazes/2d/five-by-five/0:west>; rel=\"west\",<http://amundsen.com/examples/mazes/2d/five-by-five/10:east>; rel=\"east\"");
		when(response.getHeaders("link")).thenReturn(new Header[] {header});
		
		//When
		List<Link> links = new ApacheHeaders(response,client).links();
		
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
		when(response.getHeaders("link")).thenReturn(new Header[] {header});
		
		//When
		Link link = new ApacheHeaders(response,client).link("west");
		
		//Then
		assertThat(link.getHref(), is(equalTo("http://amundsen.com/examples/mazes/2d/five-by-five/0:west")));
		assertThat(link.getRel(), is(equalTo("west")));
	}
}
