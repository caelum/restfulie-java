package br.com.caelum.restfulie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

public class DefaultResponse implements Response {

	private int code;
	private String content = "";
	private Map<String, List<String>> headers;
	private HttpURLConnection connection;

	public DefaultResponse(HttpURLConnection connection) throws IOException {
		this(connection, true);
	}

	public DefaultResponse(HttpURLConnection connection, boolean shouldReadContent)
			throws IOException {
		this.code = connection.getResponseCode();
		this.connection = connection;
		if (shouldReadContent) {
			InputStream stream = (InputStream) connection.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					stream));
			while (true) {
				String partial = reader.readLine();
				if (partial == null) {
					break;
				}
				if (content.length() != 0) {
					content += "\n";
				}
				content += partial;
			}
		}
		this.headers = connection.getHeaderFields();
	}

	public int getCode() {
		return code;
	}

	public String getContent() {
		return content;
	}

	public List<String> getHeader(String key) {
		return headers.get(key);
	}
	
	public HttpURLConnection getConnection() {
		return connection;
	}

	public <T> T getResource() {
		return null;
	}


}
