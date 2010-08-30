package br.com.caelum.restfulie.http;

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

}
