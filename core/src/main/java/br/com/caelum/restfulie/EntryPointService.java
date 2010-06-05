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

package br.com.caelum.restfulie;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import br.com.caelum.restfulie.config.Configuration;
import br.com.caelum.restfulie.config.SerializationConfig;
import br.com.caelum.restfulie.config.XStreamConfig;
import br.com.caelum.restfulie.http.ContentProcessor;
import br.com.caelum.restfulie.http.DefaultResponse;
import br.com.caelum.restfulie.http.HttpURLConnectionContentProcessor;
import br.com.caelum.restfulie.http.IdentityContentProcessor;
import br.com.caelum.restfulie.marshall.ResourceSerializer;
import br.com.caelum.restfulie.serializer.BasicSerializer;
import br.com.caelum.restfulie.serializer.XStreamXmlSerializer;

/**
 * A service's entry point.
 * 
 * @author guilherme silveira
 */
@SuppressWarnings("unchecked")
public class EntryPointService implements ResourceSerializer {

	private final URI uri;
	private Object customObject;
	private final XStreamConfig config;

	private final Map<String, String> headers = new HashMap<String, String>();

	public EntryPointService(URI uri) {
		this(uri, new HashMap<Class, Configuration>());
	}

	public EntryPointService(URI uri, Map<Class, Configuration> configs) {
		this(uri, new SerializationConfig(configs));
	}

	public EntryPointService(URI uri, SerializationConfig config) {
		this(uri, new XStreamConfig(config));
	}

	public EntryPointService(URI uri, XStreamConfig config) {
		this.config = config;
		this.uri = uri;
		headers.put("Accept", "application/xml");
	}

	public EntryPointService accept(String type) {
		headers.put("Accept", type);
		return this;
	}

	public ResourceSerializer exclude(String... names) {
		if (customObject == null) {
			throw new IllegalStateException(
					"Unable to exclude fields if you do not define on which type you will exclude it.");
		}
		config.type(customObject.getClass()).exclude(names);
		return this;
	}

	public ResourceSerializer include(String... names) {
		if (customObject == null) {
			throw new IllegalStateException(
					"Unable to include fields if you do not define on which type you will include it.");
		}
		config.type(customObject.getClass()).include(names);
		return this;
	}

	public Response sendPayload(String verb) {
		try {
			HttpURLConnection connection = prepareConnectionWithHeaders();
			if (!headers.containsKey("Content-type")) {
				throw new RestfulieException(
						"You should set a content type prior to sending some payload.");
			}
			connection.setDoOutput(true);
			connection.setRequestMethod(verb);
			OutputStream output = connection.getOutputStream();
			Writer writer = new OutputStreamWriter(output);
			BasicSerializer serializer = getSerializer(writer);
			serializer.serialize();
			writer.flush();
			DefaultResponse response = responseFor(connection,
					new IdentityContentProcessor());
			if (response.getCode() == 201) {
				return new EntryPointService(new URI(response.getHeader(
						"Location").get(0)), this.config).get();
			}
			// TODO return dumb proxy with access to the response
			return response;
		} catch (IOException e) {
			throw new RestfulieException("Unable to execute " + uri, e);
		} catch (URISyntaxException e) {
			throw new RestfulieException("Unable to execute " + uri, e);
		}
	}

	private HttpURLConnection prepareConnectionWithHeaders()
			throws IOException, MalformedURLException {
		HttpURLConnection connection = (HttpURLConnection) uri.toURL()
				.openConnection();
		for (String header : headers.keySet()) {
			connection.addRequestProperty(header, headers.get(header));
		}
		return connection;
	}

	private BasicSerializer getSerializer(Writer writer) {
		return new XStreamXmlSerializer(config.create(), writer)
				.from(customObject);
	}

	private Response retrieve(String verb) {
		try {
			HttpURLConnection connection = prepareConnectionWithHeaders();
			connection.setDoOutput(false);
			connection.setRequestMethod(verb);
			DefaultResponse response = responseFor(connection,
					new HttpURLConnectionContentProcessor(connection));
			return response;
		} catch (IOException e) {
			throw new RestfulieException("Unable to execute " + uri, e);
		}
	}

	public Response get() {
		return retrieve("GET");
	}

	private DefaultResponse responseFor(HttpURLConnection connection,
			ContentProcessor processor) throws IOException {
		return new DefaultResponse(connection, getDeserializer(), processor);
	}

	private XStreamDeserializer getDeserializer() {
		return new XStreamDeserializer(this.config);
	}

	@Override
	public ResourceSerializer as(String contentType) {
		headers.put("Content-type", contentType);
		return this;
	}

	@Override
	public Response delete() {
		return retrieve("DELETE");
	}

	@Override
	public Response head() {
		return retrieve("HEAD");
	}

	@Override
	public Response options() {
		return retrieve("OPTIONS");
	}

	@Override
	public <T> Response patch(T object) {
		this.customObject = object;
		return sendPayload("PATCH");
	}

	@Override
	public <T> Response post(T object) {
		this.customObject = object;
		return sendPayload("POST");
	}

	@Override
	public <T> Response put(T object) {
		this.customObject = object;
		return sendPayload("PUT");
	}

}
