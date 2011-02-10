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

package br.com.caelum.restfulie.http.javanet;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.RestfulieException;
import br.com.caelum.restfulie.http.ContentProcessor;
import br.com.caelum.restfulie.http.Headers;
import br.com.caelum.restfulie.http.HttpURLConnectionContentProcessor;
import br.com.caelum.restfulie.http.Request;

/**
 * Default response implementation based on HttpURLConnection.
 * 
 * @author guilherme silveira
 */
public class JavaNetResponse implements Response {

	private int code;
	private Headers headers;
	private HttpURLConnection connection;
	private ContentProcessor processor;
	private final RestClient client;
	private final Request request;

	/**
	 * Will use this connection to retrieve the response data. The deserializer
	 * will be used if the user wants to retrieve the resource.
	 */
	public JavaNetResponse(HttpURLConnection connection,
			RestClient client, Request request) throws IOException {
		this(connection, client, new HttpURLConnectionContentProcessor(connection), request);
	}

	public JavaNetResponse(HttpURLConnection connection,
			RestClient client, ContentProcessor processor, Request request)
			throws IOException {
		this.client = client;
		this.request = request;
		this.code = connection.getResponseCode();
		this.connection = connection;
		this.headers = new MapHeaders(connection.getHeaderFields());
		this.processor = processor;
	}

	public int getCode() {
		return code;
	}

	public String getContent() {
		try {
			return processor.read();
		} catch (IOException e) {
			throw new RestfulieException("Unable to parse response content", e);
		}
	}

	public List<String> getHeader(String key) {
		return headers.get(key);
	}

	public HttpURLConnection getConnection() {
		return connection;
	}

	@SuppressWarnings("unchecked")
	public <T> T getResource() throws IOException {
		String contentType = getContentType();
		String content = getContent();
		return (T) client.getMediaTypes().forContentType(contentType).unmarshal(content, client);
	}

	private String getContentType() {
		return headers.getMain("Content-Type");
	}

	public Headers getHeaders() {
		return headers;
	}

	public URI getLocation() {
		try {
			return new URI(headers.getMain("Location"));
		} catch (URISyntaxException e) {
			throw new RestfulieException("Invalid URI received as a response", e);
		}
	}

	public String getType() {
		return getContentType();
	}

	public Request getRequest() {
		return this.request;
	}

	public String getStatusLine() {
		try {
			return connection.getResponseMessage();
		} catch (IOException e) {
			throw new RestfulieException("Invalid Status line", e);
		}
	}

}
