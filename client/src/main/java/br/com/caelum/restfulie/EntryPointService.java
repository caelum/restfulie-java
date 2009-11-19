package br.com.caelum.restfulie;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import br.com.caelum.restfulie.http.DefaultResponse;
import br.com.caelum.restfulie.http.HttpURLConnectionContentProcessor;
import br.com.caelum.restfulie.http.IdentityContentProcessor;
import br.com.caelum.restfulie.marshall.BasicResourceSerializer;
import br.com.caelum.restfulie.serializer.BasicSerializer;
import br.com.caelum.restfulie.serializer.DefaultTypeNameExtractor;
import br.com.caelum.restfulie.serializer.XStreamXmlSerializer;

import com.thoughtworks.xstream.XStream;

/**
 * A service's entry point.
 * 
 * @author guilherme silveira
 */
public class EntryPointService implements BasicResourceSerializer{

	private final URI uri;
	private Object customObject;
	private final SerializationConfig config;
	
	public EntryPointService(URI uri) {
		this(uri, new HashMap<Class, Configuration>());
	}
	
	public EntryPointService(URI uri, Map<Class, Configuration> configs) {
		this.uri = uri;
		this.config = new SerializationConfig(configs);
	}

	public static EntryPointService service(URI uri) {
		return new EntryPointService(uri);
	}
	
	public <T> BasicResourceSerializer custom(T object) {
		this.customObject = object;
		return this;
	}
	
	public <T, R> R post(T object) {
		return custom(object).post();
	}

	public BasicResourceSerializer exclude(String... names) {
		if(customObject==null) {
			throw new IllegalStateException("Unable to exclude fields if you do not define on which type you will exclude it.");
		}
		config.type(customObject.getClass()).exclude(names);
		return this;
	}

	public BasicResourceSerializer include(String... names) {
		if(customObject==null) {
			throw new IllegalStateException("Unable to include fields if you do not define on which type you will include it.");
		}
		config.type(customObject.getClass()).include(names);
		return this;
	}

	public <R> R post() {
		try {
			HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
			connection.addRequestProperty("Content-type", "application/xml"); // read from some previous configured place
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			OutputStream output = connection.getOutputStream();
			Writer writer = new OutputStreamWriter(output);
			BasicSerializer serializer = new XStreamXmlSerializer(new XStream(), writer, new DefaultTypeNameExtractor()).from(customObject, config.type(customObject.getClass()));
			serializer.serialize();
			writer.flush();
	        DefaultResponse response = new DefaultResponse(connection, new XStreamDeserializer(), new IdentityContentProcessor());
	        if(response.getCode()==201) {
	        	return service(new URI(response.getHeader("Location").get(0))).get();
	        }
	        return null;
		} catch (IOException e) {
			throw new TransitionException("Unable to execute " + uri, e);
		} catch (URISyntaxException e) {
			throw new TransitionException("Unable to execute " + uri, e);
		}
	}

	public <R> R get() {
		try {
			HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
			connection.addRequestProperty("Accepts", "application/xml"); // read from some previous configured place
			connection.setDoOutput(false);
			connection.setRequestMethod("GET");
	        DefaultResponse response = new DefaultResponse(connection, new XStreamDeserializer(), new HttpURLConnectionContentProcessor(connection));
	        return response.getResource();
		} catch (IOException e) {
			throw new TransitionException("Unable to execute " + uri, e);
		}

	}

}
