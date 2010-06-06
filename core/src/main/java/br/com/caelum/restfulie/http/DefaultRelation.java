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

import br.com.caelum.restfulie.Link;
import br.com.caelum.restfulie.RelationToAccess;
import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.mediatype.MediaTypes;

/**
 * Default implementation of a transition.
 * 
 * @author guilherme silveira
 * @author lucas souza
 */
public class DefaultRelation implements Link {

	private String rel;
	private String href;
	private HttpMethod methodToUse;
	private final RestClient client;
	
	public DefaultRelation(String rel, String href, RestClient client) {
		this.rel = rel;
		this.href = href;
		this.client = client;
	}

	public String getHref() {
		return href;
	}

	public String getRel() {
		return rel;
	}

	public <T, Z> Z access(T arg) {
		return (Z) execute(arg, false);
	}

	public RelationToAccess method(HttpMethod methodToUse) {
		this.methodToUse = methodToUse;
		return this;
	}

	public <T> Response access(T arg) {
		return (Response) execute(null, false);
	}

	private <T> Object execute(T parameter, boolean shouldFollowAndDeserialize) {
		return null;
//        try {
//			URL url = new URL(href);
//			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//			connection.addRequestProperty("Content-type", "application/xml");
//			connection.addRequestProperty("Accept", "application/xml");
//			connection.setRequestMethod(methodName());
//			boolean hasParameter = parameter != null;
//			connection.setDoOutput(hasParameter);
//			if(hasParameter) {
//				OutputStream output = connection.getOutputStream();
//				Writer writer = new OutputStreamWriter(output);
//				BasicSerializer serializer = new XStreamXmlSerializer(config.create(), writer).from(parameter);
//				serializer.serialize();
//				writer.flush();
//			}
//			if(shouldFollowAndDeserialize) {
//		        DefaultResponse response = new DefaultResponse(connection, deserializer);
//		        if(response.getCode()==201) {
//		        	return new EntryPointService(new URI(response.getHeader("Location").get(0)), this.config).get();
//		        }
//				return response.getResource();
//			}
//			return new DefaultResponse(connection, deserializer, new IdentityContentProcessor());
//		} catch (IOException e) {
//			throw new RestfulieException("Unable to execute transition " + rel + " @ " + href, e);
//		} catch (URISyntaxException e) {
//			throw new RestfulieException("Unable to execute transition " + rel + " @ " + href, e);
//		}
	}

	public <T, R> R accessAndRetrieve(T arg) {
		return (R) execute(arg, true);
	}

	public Response access() {
		return (Response) execute(null, false);
	}

	public <R> R accessAndRetrieve() {
		return (R) accessAndRetrieve(null);
	}

}
