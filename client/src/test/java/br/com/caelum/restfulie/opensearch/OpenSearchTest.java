package br.com.caelum.restfulie.opensearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.StringReader;

import org.junit.Test;

import com.thoughtworks.xstream.XStream;

/**
 * @author jose donizetti
 *
 */
public class OpenSearchTest {

	@Test
	public void shouldUnmarshallAnOpenSearchDocument() {
	      String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
	          "<OpenSearchDescription xmlns=\"http://a9.com/-/spec/opensearch/1.1/\">"+
	            "<ShortName>Restbuy</ShortName>"+
	            "<Description>Restbuy search engine.</Description>"+
	            "<Tags>restbuy</Tags>"+
	            "<Contact>admin@restbuy.com</Contact>"+
	            "<Url type=\"application/atom+xml\"  template=\"http://localhost:3000/products?q={searchTerms}&amp;pw={startPage?}&amp;format=atom\" />"+
	            "<Url type=\"application/json\"  template=\"http://localhost:3000/products?q={searchTerms}&amp;pw={startPage?}&amp;format=json\" />"+
	          "</OpenSearchDescription>";
	      
	      XStream xstream = new XStream();
	      xstream.processAnnotations(SearchDescription.class);
	      	
	      SearchDescription desc = (SearchDescription) xstream.fromXML(new StringReader(xml));
	      assertThat(desc.getShortName(), is(equalTo("Restbuy")));
	      assertThat(desc.getDescription(), is(equalTo("Restbuy search engine.")));
	      assertThat(desc.getTags().get(0), is(equalTo("restbuy")));
	      assertThat(desc.getContact(), is(equalTo("admin@restbuy.com")));
	      
	      assertThat(desc.getUrls().get(0).getType(), is(equalTo("application/atom+xml")));
	      assertThat(desc.getUrls().get(0).getTemplate(), is(equalTo("http://localhost:3000/products?q={searchTerms}&pw={startPage?}&format=atom")));
	      
	      assertThat(desc.getUrls().get(1).getType(), is(equalTo("application/json")));
	      assertThat(desc.getUrls().get(1).getTemplate(), is(equalTo("http://localhost:3000/products?q={searchTerms}&pw={startPage?}&format=json")));
	}
	
}
