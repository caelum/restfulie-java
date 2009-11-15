package br.com.caelum.restfulie;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;

/**
 * A service's entry point.
 * 
 * @author guilherme silveira
 */
public class EntryPointService {

	private final URI uri;

	public EntryPointService(URI uri) {
		this.uri = uri;
	}

	public static EntryPointService service(URI uri) {
		return new EntryPointService(uri);
	}
	
	public void create() {
		try {
			URL url = new URL(href);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(false);
			String methodName = methodName();
			connection.setRequestMethod(methodName);
	        return new DefaultResponse(connection, deserializer, methodName.equals("GET"));
		} catch (MalformedURLException e) {
			throw new TransitionException("Unable to execute transition " + rel + " @ " + href, e);
		} catch (ProtocolException e) {
			throw new TransitionException("Unable to execute transition " + rel + " @ " + href, e);
		} catch (IOException e) {
			throw new TransitionException("Unable to execute transition " + rel + " @ " + href, e);
		}
	}

}
