/***
 * Copyright (c) 2009 Caelum - www.caelum.com.br/opensource - guilherme.silveira@caelum.com.br
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.caelum.restfulie.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import br.com.caelum.restfulie.Resource;
import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.TransitionException;
import br.com.caelum.restfulie.unmarshall.Deserializer;

/**
 * Default response implementation based on HttpURLConnection.
 * 
 * @author guilherme silveira
 */
public class DefaultResponse implements Response {

	private int code;
	private Map<String, List<String>> headers;
	private HttpURLConnection connection;
	private final Deserializer deserializer;
	private ContentProcessor processor;

	/**
	 * Will use this connection to retrieve the response data. The deserializer
	 * will be used if the user wants to retrieve the resource.
	 */
	public DefaultResponse(HttpURLConnection connection,
			Deserializer deserializer) throws IOException {
		this(connection, deserializer, new HttpURLConnectionContentProcessor(connection));
	}

	public DefaultResponse(HttpURLConnection connection,
			Deserializer deserializer, ContentProcessor processor)
			throws IOException {
		this.deserializer = deserializer;
		this.code = connection.getResponseCode();
		this.connection = connection;
		this.headers = connection.getHeaderFields();
		this.processor = processor;
	}

	public int getCode() {
		return code;
	}

	public String getContent() throws IOException {
		return processor.read();
	}

	public List<String> getHeader(String key) {
		return headers.get(key);
	}

	public HttpURLConnection getConnection() {
		return connection;
	}

	@SuppressWarnings("unchecked")
	public <T> T getResource() throws IOException {
		String content = getContent();
		Resource deserializedResource = (Resource) deserializer.fromXml(content);
		setResponse(deserializedResource);
		//deserializedResource.
		return (T) deserializedResource;
	}

	private void setResponse(Resource deserializedResource) {
		try {
			java.lang.reflect.Field fResponse = deserializedResource.getClass().getDeclaredField("response");
			fResponse.setAccessible(true);
			fResponse.set(deserializedResource, this);
		} catch (SecurityException e) {
			throw new TransitionException("Unable inject web response in resource", e);
		} catch (NoSuchFieldException e) {
			throw new TransitionException("Unable inject web response in resource", e);
		} catch (IllegalArgumentException e) {
			throw new TransitionException("Unable inject web response in resource", e);
		} catch (IllegalAccessException e) {
			throw new TransitionException("Unable inject web response in resource", e);
		}
	}

}
