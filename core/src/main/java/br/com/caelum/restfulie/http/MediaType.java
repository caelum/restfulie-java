package br.com.caelum.restfulie.http;

import java.io.IOException;
import java.io.Writer;

import br.com.caelum.restfulie.Resource;

public interface MediaType {

	boolean answersTo(String type);

	<T> void marshal(T payload, Writer writer) throws IOException;

	Resource unmarshal(String content, MediaTypes types);

}
