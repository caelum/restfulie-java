package br.com.caelum.restfulie.opensearch.converter;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.restfulie.opensearch.SearchDescription;
import br.com.caelum.restfulie.opensearch.conveter.DefaultTagsConveter;

import com.thoughtworks.xstream.XStream;

public class TagsConverterTest {

	private XStream stream = new XStream();
	
	@Before
	public void setUp() {
		stream.registerConverter(new DefaultTagsConveter());
		stream.processAnnotations(SearchDescription.class);
	}
	
	@Test
	public void shouldConvertAnEmptyElement() {
		//Given
		String xml = "<OpenSearchDescription>" +
						"<Tags></Tags>" +
				     "</OpenSearchDescription>";
		
		//When
		SearchDescription desc = (SearchDescription) stream.fromXML(new StringReader(xml));
		
		//Then
		assertThat(desc.getTags().size(), is(equalTo(0)));
	}
	
	@Test
	public void shouldConvertAnElementWithTwoTagsSeparetedByOneSpace() {
		//Given
		String xml = "<OpenSearchDescription>" +
						"<Tags>restfulie rest</Tags>" +
					 "</OpenSearchDescription>";

		//When
		SearchDescription desc = (SearchDescription) stream.fromXML(new StringReader(xml));
		
		//Then
		assertThat(desc.getTags().size(), is(equalTo(2)));
		assertThat(desc.getTags().get(0), is(equalTo("restfulie")));
		assertThat(desc.getTags().get(1), is(equalTo("rest")));
	}
	
	@Test
	public void shouldConvertAnElementWithThreeTagsSeparetedByMoreThanOneSpace() {
		//Given
		String xml = "<OpenSearchDescription>" +
		"<Tags>restfulie      rest      http</Tags>" +
		"</OpenSearchDescription>";
		
		//When
		SearchDescription desc = (SearchDescription) stream.fromXML(new StringReader(xml));
		
		//Then
		assertThat(desc.getTags().size(), is(equalTo(3)));
		assertThat(desc.getTags().get(0), is(equalTo("restfulie")));
		assertThat(desc.getTags().get(1), is(equalTo("rest")));
		assertThat(desc.getTags().get(2), is(equalTo("http")));
	}
	
}
