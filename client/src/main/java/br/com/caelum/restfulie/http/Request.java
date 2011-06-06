package br.com.caelum.restfulie.http;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.Future;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.feature.RequestFeature;
import br.com.caelum.restfulie.feature.ResponseFeature;

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

    Future<Response> getAsync(AsynchronousRequest asynchronousRequest);

	<T> Response post(T object);

	<T> Response put(T object);

	<T> Response patch(T object);

	Response delete();

	Response options();

	Response head();

	Map<String, String> getHeaders();
	Request addHeaders(Map<String, String> headers);

	/**
	 * Shortcut for accepting and sending some media type. It has the same effect
	 * as invoking as(type).accepts(type)
	 */
	Request handling(String type);

	URI getURI();

	/**
	 * Add feature to  RequestStack
	 */
	Request with(RequestFeature feature);
	/**
	 * Add feature to the ResponseStack
	 */
	Request with(ResponseFeature feature);
	
}
