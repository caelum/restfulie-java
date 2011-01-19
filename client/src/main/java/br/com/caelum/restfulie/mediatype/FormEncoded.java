package br.com.caelum.restfulie.mediatype;

import java.io.IOException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.Map;

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
	public <T> void marshal(T payload, Writer writer) throws IOException {
		if(payload.getClass().equals(String.class)) {
			writer.append(String.class.cast(payload));
			return;
		}
		Map<String, String> params = (Map<String, String>) payload;
		int at = 0;
		for (String key : params.keySet()) {
			writer.append(URLEncoder.encode(key));
			writer.append("=");
			writer.append(URLEncoder.encode(params.get(key)));
			if (++at != params.size()) {
				writer.append("&");
			}
		}
	}

	public <T> T unmarshal(String content, RestClient client) {
		return null;
	}

}
