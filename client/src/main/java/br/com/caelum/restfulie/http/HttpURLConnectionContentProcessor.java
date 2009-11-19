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
