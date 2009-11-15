package br.com.caelum.restfulie;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;

import br.com.caelum.restfulie.serializer.DefaultTypeNameExtractor;
import br.com.caelum.restfulie.serializer.XStreamXmlSerializer;

import com.thoughtworks.xstream.XStream;

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
	
	public <T> void post(T object) {
		try {
			HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
			connection.addRequestProperty("Content-type", "application/xml"); // read from some previous configured place
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			OutputStream output = connection.getOutputStream();
			OutputStreamWriter writer = new OutputStreamWriter(output);
			new XStreamXmlSerializer(new XStream(), writer, new DefaultTypeNameExtractor()).from(object).serialize();
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
