package br.com.caelum.restfulie;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Default implementation of a transition.
 * 
 * @author guilherme silveira
 * @author lucas souza
 */
public class DefaultTransition implements Transition {

	private String rel;
	private String href;

	public DefaultTransition(String rel, String href) {
		this.rel = rel;
		this.href = href;
	}

	public String getHref() {
		return href;
	}

	public String getRel() {
		return rel;
	}

	public <T> Response execute(T arg) {
		// TODO 1: use httpclient new version to execute GET
		// TODO 1.5: return result
		// TODO 2: support POST and others by default
		// TODO 3: allow method override
		// TODO 4: GET should automatically de-serialize result
		// TODO 5: receive parameters by default
		// TODO 5.5: allow httpclient customization
		// TODO 6: support other methods appart from httpclient
		try {
			URL url = new URL(href);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(false);
			connection.setRequestMethod("GET");
	        return new DefaultResponse(connection);
		} catch (MalformedURLException e) {
			throw new TransitionException("Unable to execute transition " + rel + " @ " + href, e);
		} catch (ProtocolException e) {
			throw new TransitionException("Unable to execute transition " + rel + " @ " + href, e);
		} catch (IOException e) {
			throw new TransitionException("Unable to execute transition " + rel + " @ " + href, e);
		}


	}
	public <T> Response execute() {
		return execute(null);
	}

}
