package br.com.caelum.restfulie.mediatype;

import java.io.IOException;
import java.io.Writer;

import br.com.caelum.restfulie.RestClient;

/**
 * A media type handler, capable of marshalling into representations related to
 * this media type and unmarshalling objects from those representations.
 * 
 * @author guilherme silveira
 */
public interface MediaType {

	boolean answersTo(String type);

	<T> void marshal(T payload, Writer writer) throws IOException;

	/**
	 * Unmarshalling should always be to something to be analyzed.<br/>
	 * Remember *not* to expect too much from unmarshalling, your server might
	 * have provided you something you did not expect. This is REST's idea.
	 */
	<T> T unmarshal(String content, RestClient client);

}
