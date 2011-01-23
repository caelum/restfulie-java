package br.com.caelum.restfulie.http;

import java.net.URI;
import java.util.Map;

import br.com.caelum.restfulie.Response;

/**
 * An http request. Defaults verb to GET.
 *
 * @author guilherme silveira
 */
public interface Request {

	/**
	 * Access the resource, returning the request result.
	 * @return
	 */
	Response access();

	/**
	 * Adds a request header.
	 */
	Request with(String key, String value);

	/**
	 * Sets the verb to use (default GET).
	 */
	Request using(String verb);


	Request as(String contentType);

	/**
	 * Accepts one specific content type.
	 */
	Request accept(String type);

	Response get();

	<T> Response post(T object);

	<T> Response put(T object);

	<T> Response patch(T object);

	Response delete();

	Response options();

	Response head();

	public Map<String, String> getHeaders();

	/**
	 * Shortcut for accepting and sending some media type. It has the same effect
	 * as invoking as(type).accepts(type)
	 */
	Request handling(String type);

	URI getURI();

	/**
	 * Add feature ThrowError on the ResponseStack
	 */
	Request throwError();

	
	/**
	 * Add feature RetryWhenUnavailable on the RequestStack
	 */
	Request retryWhenUnavailable();

}
