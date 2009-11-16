package br.com.caelum.restfulie;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private final Map<Class, Configuration> configs;
	
	public EntryPointService(URI uri) {
		this(uri, new HashMap<Class, Configuration>());
	}
	
	public EntryPointService(URI uri, Map<Class, Configuration> configs) {
		this.uri = uri;
		this.configs = configs;
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
		this.excludes.addAll(Arrays.asList(names));
		return this;
	}

	public BasicResourceSerializer include(String... names) {
		this.includes.addAll(Arrays.asList(names));
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
			BasicSerializer serializer = new XStreamXmlSerializer(new XStream(), writer, new DefaultTypeNameExtractor()).from(customObject);
			serializer.include(includes.toArray(new String[0]));
			serializer.exclude(excludes.toArray(new String[0]));
			serializer.serialize();
			writer.flush();
	        DefaultResponse response = new DefaultResponse(connection, new XStreamDeserializer(), false);
	        if(response.getCode()==201) {
	        	return service(new URI(response.getHeader("Location").get(0))).get();
	        }
	        return null;
		} catch (MalformedURLException e) {
			throw new TransitionException("Unable to execute " + uri, e);
		} catch (ProtocolException e) {
			throw new TransitionException("Unable to execute " + uri, e);
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
	        DefaultResponse response = new DefaultResponse(connection, new XStreamDeserializer(), true);
	        return response.getResource();
		} catch (MalformedURLException e) {
			throw new TransitionException("Unable to execute " + uri, e);
		} catch (ProtocolException e) {
			throw new TransitionException("Unable to execute " + uri, e);
		} catch (IOException e) {
			throw new TransitionException("Unable to execute " + uri, e);
		}

	}

}
