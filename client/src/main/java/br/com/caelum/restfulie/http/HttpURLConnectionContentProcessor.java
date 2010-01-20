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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * Default implementation based on an httpurlconnection. Will basically process
 * all input data and return it through the read method.
 * 
 * @author guilherme silveira
 */
public class HttpURLConnectionContentProcessor implements ContentProcessor {

	private final HttpURLConnection connection;

	public HttpURLConnectionContentProcessor(HttpURLConnection connection) {
		this.connection = connection;
	}

	public String read() throws IOException {
		InputStream stream = (InputStream) connection.getContent();
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(stream));
		StringBuilder content = new StringBuilder();
		while (true) {
			String partial = reader.readLine();
			if (partial == null) {
				break;
			}
			if (content.length() != 0) {
				content.append("\n");
			}
			content.append(partial);
		}
		return content.toString();
	}

}
