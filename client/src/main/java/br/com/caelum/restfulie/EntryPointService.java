package br.com.caelum.restfulie;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	private final List<String> excludes = new ArrayList<String>();
	private final List<String> includes = new ArrayList<String>();
	
	public EntryPointService(URI uri) {
		this.uri = uri;
	}

	public static EntryPointService service(URI uri) {
		return new EntryPointService(uri);
	}
	
	public <T> BasicResourceSerializer custom(T object) {
		this.customObject = object;
		return this;
	}
	
	public <T> void post(T object) {
		custom(object).post();
	}

	public BasicResourceSerializer exclude(String... names) {
		this.excludes.addAll(Arrays.asList(names));
		return this;
	}

	public BasicResourceSerializer include(String... names) {
		this.includes.addAll(Arrays.asList(names));
		return this;
	}

	public void post() {
		try {
			HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
			connection.addRequestProperty("Content-type", "application/xml"); // read from some previous configured place
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			OutputStream output = connection.getOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(output);
			BasicSerializer serializer = new XStreamXmlSerializer(new XStream(), writer, new DefaultTypeNameExtractor()).from(customObject);
			serializer.include(includes.toArray(new String[0]));
			serializer.exclude(excludes.toArray(new String[0]));
			serializer.serialize();
			writer.flush();
	        new DefaultResponse(connection, null, false);
		} catch (MalformedURLException e) {
			throw new TransitionException("Unable to execute " + uri, e);
		} catch (ProtocolException e) {
			throw new TransitionException("Unable to execute " + uri, e);
		} catch (IOException e) {
			throw new TransitionException("Unable to execute " + uri, e);
		}
	}

}
