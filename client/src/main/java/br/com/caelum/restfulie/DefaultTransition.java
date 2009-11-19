package br.com.caelum.restfulie;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import br.com.caelum.restfulie.http.DefaultResponse;
import br.com.caelum.restfulie.http.IdentityContentProcessor;
import br.com.caelum.restfulie.unmarshall.Deserializer;

/**
 * Default implementation of a transition.
 * 
 * @author guilherme silveira
 * @author lucas souza
 */
public class DefaultTransition implements Transition {

	private String rel;
	private String href;
	private String methodToUse;
	private Deserializer deserializer;
	
	private static final Map<String,String> defaultMethods = new HashMap<String,String>();
	static {
		defaultMethods.put("latest", "GET");
		defaultMethods.put("show", "GET");
		defaultMethods.put("update", "POST");
		defaultMethods.put("cancel", "DELETE");
		defaultMethods.put("destroy", "DELETE");
		defaultMethods.put("suspend", "DELETE");
	}

	public DefaultTransition(String rel, String href, Deserializer deserializer) {
		this.rel = rel;
		this.href = href;
		this.deserializer = deserializer;
	}

	public String getHref() {
		return href;
	}

	public String getRel() {
		return rel;
	}

	public <T> Response execute(T arg) {
		// TODO 5: receive parameters by default
		// TODO 6: support other methods appart from default url system
		try {
			URL url = new URL(href);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(false);
			String methodName = methodName();
			connection.setRequestMethod(methodName);
			if(methodName.equals("GET")) {
				return new DefaultResponse(connection, deserializer);
			} else {
				return new DefaultResponse(connection, deserializer, new IdentityContentProcessor());
			}
		} catch (IOException e) {
			throw new TransitionException("Unable to execute transition " + rel + " @ " + href, e);
		}
	}

	private String methodName() {
		if(methodToUse != null) {
			return methodToUse;
		}
		if(defaultMethods.containsKey(rel)) {
			return defaultMethods.get(rel);
		}
		return "POST";
	}
	
	public <T> Response execute() {
		return execute(null);
	}

	public DefaultTransition method(String methodToUse) {
		this.methodToUse = methodToUse;
		return this;
	}

}
