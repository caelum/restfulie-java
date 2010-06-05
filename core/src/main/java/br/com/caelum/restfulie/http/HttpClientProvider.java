package br.com.caelum.restfulie.http;

import java.net.URI;

public interface HttpClientProvider {

	Request request(URI uri, MediaTypes types);

}
