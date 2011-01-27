package br.com.caelum.restfulie.mediatype;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Test;

import br.com.caelum.restfulie.http.DefaultRestClient;

public class FormEncodedTest {
	
	@Test
	public void shouldConcatenateParams() throws IOException {
		FormEncoded encoded = new FormEncoded();
		StringWriter writer = new StringWriter();
		Map<String, String > params = new HashMap<String, String>();
		params.put("name", "Guilherme");
		params.put("age", "29");
		encoded.marshal(params, writer, new DefaultRestClient());
		
		assertThat(writer.toString(), Matchers.anyOf(is(CoreMatchers.equalTo("name=Guilherme&age=29")),
				is(CoreMatchers.equalTo("age=29&name=Guilherme"))));
	}

}
