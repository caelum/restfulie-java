package br.com.caelum.restfulie.opensearch;


import static br.com.caelum.restfulie.opensearch.Url.page;
import static br.com.caelum.restfulie.opensearch.Url.queryFor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.StringReader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import br.com.caelum.restfulie.RestClient;

import com.thoughtworks.xstream.XStream;

/**
 * Verifying the URL parse
 * 
 * @author jose donizetti
 * 
 */

@RunWith(MockitoJUnitRunner.class)
public class UrlTest {
	
	@Mock
	private RestClient restfulie;

	@Test
	public void shouldConvertWithoutAnyUrl() {
		String xml = "<OpenSearchDescription>" + "</OpenSearchDescription>";
		XStream stream = new XStream();
		stream.processAnnotations(SearchDescription.class);
		stream.processAnnotations(Url.class);
		stream.fromXML(new StringReader(xml));
	}

	@Test
	public void shouldConvertAnElementWithOneUrl() {
		String xml = "<OpenSearchDescription>"
				+ "<Url type=\"application/atom+xml\"  template=\"http://localhost:3000/products?q={searchTerms}&amp;pw={startPage?}&amp;format=atom\" />"
				+ "</OpenSearchDescription>";
		XStream stream = new XStream();
		stream.processAnnotations(SearchDescription.class);
		stream.processAnnotations(Url.class);
		SearchDescription desc = (SearchDescription) stream.fromXML(new StringReader(xml));
		assertThat(desc.getUrls().size(), is(equalTo(1)));
		assertThat(desc.getUrls().get(0).getTemplate(), is(equalTo("http://localhost:3000/products?q={searchTerms}&pw={startPage?}&format=atom")));
		assertThat(desc.getUrls().get(0).getType(), is(equalTo("application/atom+xml")));
	}

	@Test
	public void shouldConvertAnElementWithTwoUrl() {
		String xml = "<OpenSearchDescription>"
				+ "<Url type=\"application/atom+xml\"  template=\"http://localhost:3000/products?q={searchTerms}&amp;pw={startPage?}&amp;format=atom\" />"
				+ "<Url type=\"application/json\"  template=\"http://localhost:3000/products?q={searchTerms}&amp;pw={startPage?}&amp;format=json\" />"
				+ "</OpenSearchDescription>";
		XStream stream = new XStream();
		stream.processAnnotations(SearchDescription.class);
		stream.processAnnotations(Url.class);
		SearchDescription desc = (SearchDescription) stream.fromXML(new StringReader(xml));
		assertThat(desc.getUrls().size(), is(equalTo(2)));
		assertThat(desc.getUrls().get(0).getTemplate(), is(equalTo("http://localhost:3000/products?q={searchTerms}&pw={startPage?}&format=atom")));
		assertThat(desc.getUrls().get(0).getType(), is(equalTo("application/atom+xml")));
		assertThat(desc.getUrls().get(1).getTemplate(), is(equalTo("http://localhost:3000/products?q={searchTerms}&pw={startPage?}&format=json")));
		assertThat(desc.getUrls().get(1).getType(), is(equalTo("application/json")));
	}

	@Test
	public void shouldReplaceSearchTerms() {
		Url url = new Url("application/json","http://localhost:3000/products?q={searchTerms}",restfulie);
		url.with(queryFor("doni"));
		assertThat(url.toUri(), is(equalTo("http://localhost:3000/products?q=doni")));
	}
	
	@Test
	public void shouldReplaceStartPage() {
		Url url = new Url("application/json","http://localhost:3000/products?pw={startPage?}",restfulie);
		url.with(queryFor("")).and(page(1));
		assertThat(url.toUri(), is(equalTo("http://localhost:3000/products?pw=1")));
	}
}
