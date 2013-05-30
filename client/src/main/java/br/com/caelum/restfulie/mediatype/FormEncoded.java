package br.com.caelum.restfulie.mediatype;

import java.io.IOException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.Map;

import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;

import br.com.caelum.restfulie.RestClient;

/**
 * A media type that supports x-www-form-urlencoded.
 * 
 * @author guilherme silveira
 */
public class FormEncoded implements MediaType {

	public boolean answersTo(String type) {
		return "application/x-www-form-urlencoded".equals(type);
	}

	@SuppressWarnings("unchecked")
	public <T> void marshal(T payload, Writer writer, RestClient client) throws IOException {
		if(payload.getClass().equals(String.class)) {
			writer.append(String.class.cast(payload));
			return;
		}
		Map<String, String> params = (Map<String, String>) payload;
		int at = 0;
		for (String key : params.keySet()) {
			writer.append(URLEncoder.encode(key,client.charset()));
			writer.append("=");
			writer.append(URLEncoder.encode(params.get(key),client.charset()));
			if (++at != params.size()) {
				writer.append("&");
			}
		}
	}

	public <T> T unmarshal(String content, RestClient client) {
		return null;
	}

	public MediaType withReflectionProvider(ReflectionProvider refletionProvider) {
		return this;
	}

}
