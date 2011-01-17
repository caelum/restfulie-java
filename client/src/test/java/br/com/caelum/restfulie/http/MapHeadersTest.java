package br.com.caelum.restfulie.http;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class MapHeadersTest {
	
	private Headers headers;
	private HashMap<String, List<String>> fields;
	
	@Before
	public void setUp() {
		this.fields = new HashMap<String, List<String>>();
		headers = new MapHeaders(fields);
	}

	@Test
	public void shouldReturnRaw() {
		fields.put("Content-Type", Arrays.asList("application/xml;charset=ISO-8859-1"));
		assertThat(headers.getRaw("Content-Type").get(0), is(equalTo("application/xml;charset=ISO-8859-1")));
	}
	
	@Test
	public void shouldReturnMain() {
		fields.put("Content-Type", Arrays.asList("application/xml"));
		assertThat(headers.getMain("Content-Type"), is(equalTo("application/xml")));
	}
	
	@Test
	public void shouldReturnMainIgnoringCharset() {
		fields.put("Content-Type", Arrays.asList("application/xml;charset=ISO-8859-1"));
		assertThat(headers.getMain("Content-Type"), is(equalTo("application/xml")));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldNotExecutedWhenNoHeaders() {
		assertThat(headers.getMain("Content-Type"), is(equalTo("application/xml")));
	}
	
}
