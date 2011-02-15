package br.com.caelum.restfulie.opensearch.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.StringReader;

import org.junit.Test;

import br.com.caelum.restfulie.opensearch.SearchDescription;

import com.thoughtworks.xstream.XStream;

public class TagsConverterTest {

	@Test
	public void shouldConvertAnEmptyElement() {
		String xml = "<OpenSearchDescription>" +
						"<Tags></Tags>" +
				     "</OpenSearchDescription>";
		XStream stream = new XStream();
		stream.processAnnotations(SearchDescription.class);
		SearchDescription desc = (SearchDescription) stream.fromXML(new StringReader(xml));
		assertThat(desc.getTags().size(), is(equalTo(0)));
	}
	
	@Test
	public void shouldConvertAnElementWithTwoTagsSeparetedByOneSpace() {
	String xml = "<OpenSearchDescription>" +
		"<Tags>restfulie rest</Tags>" +
		"</OpenSearchDescription>";
		XStream stream = new XStream();
		stream.processAnnotations(SearchDescription.class);
		SearchDescription desc = (SearchDescription) stream.fromXML(new StringReader(xml));
		assertThat(desc.getTags().size(), is(equalTo(2)));
		assertThat(desc.getTags().get(0), is(equalTo("restfulie")));
		assertThat(desc.getTags().get(1), is(equalTo("rest")));
	}
	
	@Test
	public void shouldConvertAnElementWithThreeTagsSeparetedByMoreThanOneSpace() {
		String xml = "<OpenSearchDescription>" +
		"<Tags>restfulie      rest      http</Tags>" +
		"</OpenSearchDescription>";
		XStream stream = new XStream();
		stream.processAnnotations(SearchDescription.class);
		SearchDescription desc = (SearchDescription) stream.fromXML(new StringReader(xml));
		assertThat(desc.getTags().size(), is(equalTo(3)));
		assertThat(desc.getTags().get(0), is(equalTo("restfulie")));
		assertThat(desc.getTags().get(1), is(equalTo("rest")));
		assertThat(desc.getTags().get(2), is(equalTo("http")));
	}
	
}
