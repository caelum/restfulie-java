package br.com.caelum.restfulie.http;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import br.com.caelum.restfulie.Response;
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

	public <T> T getResource() throws IOException {
		String content = getContent();
		System.out.println("USing " + deserializer + " to deserialize") ;
		return (T) deserializer.fromXml(content);
	}

}
