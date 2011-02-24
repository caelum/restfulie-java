package br.com.caelum.restfulie;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import br.com.caelum.restfulie.http.DefaultRestClient;
import br.com.caelum.restfulie.http.Request;

public class LinkTest {

	@Test
	public void shouldUseTheMediaTypeProviedByTheUser() {
		DefaultRestClient restfulie = new DefaultRestClient();
		Request request = restfulie.at("uri").as("application/xml");
		
		assertThat(request.getHeaders().get("Content-type"),is(equalTo("application/xml")));
	}
	
	
}
