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

/**
 * Default response implementation based on HttpURLConnection.
 * 
 * @author guilherme silveira
 */
public class DefaultResponse implements Response {

	private int code;
	private Map<String, List<String>> headers;
	private HttpURLConnection connection;
	private ContentProcessor processor;
	private MediaTypes types;

	/**
	 * Will use this connection to retrieve the response data. The deserializer
	 * will be used if the user wants to retrieve the resource.
	 */
	public DefaultResponse(HttpURLConnection connection,
			MediaTypes types) throws IOException {
		this(connection, types, new HttpURLConnectionContentProcessor(connection));
	}

	public DefaultResponse(HttpURLConnection connection,
			MediaTypes types, ContentProcessor processor)
			throws IOException {
		this.types = types;
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
		Resource deserializedResource = (Resource) types.forContentType(headers.get("Content-type").get(0)).unmarshal(content);
		return (T) deserializedResource;
	}

}
