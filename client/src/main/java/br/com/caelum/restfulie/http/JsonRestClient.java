package br.com.caelum.restfulie.http;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jvnet.inflector.Pluralizer;
import org.jvnet.inflector.lang.en.NounPluralizer;

import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.RestfulieException;
import br.com.caelum.restfulie.http.apache.ApacheDispatcher;
import br.com.caelum.restfulie.mediatype.GsonMediaType;
import br.com.caelum.restfulie.mediatype.MediaTypes;
import br.com.caelum.restfulie.request.RequestDispatcher;

/**
 * RestClient implementation based on DefaultRestClient
 * <br>
 * This client has been created to allow using restfulie without XML related
 * libs, which i consider a bit bloat if users really don't need any XML stuff.
 * 
 * @author Felipe Brandao
 *
 */
public class JsonRestClient implements RestClient{
	
	private final MediaTypes types = new MediaTypes();

	private RequestDispatcher dispatcher;

	private Pluralizer inflector;
	
	private URI lastURI = null;

	private final ExecutorService threads;
	
	public JsonRestClient(){
		this.dispatcher = new ApacheDispatcher(this);
		//TODO don't know if really need to use it
		this.inflector = new NounPluralizer();
		types.register(new GsonMediaType());
		this.threads = Executors.newCachedThreadPool();
	}
	
	public JsonRestClient use(RequestDispatcher executor) {
		this.dispatcher = executor;
		return this;
	}

	public RequestDispatcher getProvider() {
		return dispatcher;
	}

	public MediaTypes getMediaTypes() {
		return types;
	}

	/**
	 * Entry point to direct access an uri.
	 */
	public Request at(URI uri) {
		lastURI = uri;
		return createRequestFor(uri);
	}

	/**
	 * Override this method to use your own Request object
	 * 
	 * @param uri
	 * @return
	 */
	protected Request createRequestFor(URI uri) {
		return new DefaultHttpRequest(uri, this);
	}

	/**
	 * Entry point to direct access an uri.
	 * @throws URISyntaxException
	 */
	public Request at(String uri) {
		try {
			return at(new URI(uri));
		} catch (URISyntaxException e) {
			throw new RestfulieException("Unable to build an URI for this request.", e);
		}
	}

	public URI lastURI() {
		return lastURI;
	}
	
	public Pluralizer inflectionRules() {
		return inflector;
	}

	public RestClient withInflector(Pluralizer inflector){
		this.inflector = inflector;
		return this;
	}

	@Override
	public ExecutorService getThreads() {
		return threads;
	}

}
