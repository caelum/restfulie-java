package br.com.caelum.restfulie.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import br.com.caelum.restfulie.Response;

/**
 * The RequestCallback is used by AsynchronousRequest to provide asynchronous HTTP requests using
 * Threads. As the only thing above a Thread is the JVM, it would not be possible to catch the
 * exception without using the future.get() method. Because of this, currently the only way to catch
 * exceptions thrown during the asynchronous request execution is overwriting the onException
 * method. Maybe in the future, if a better solution is found, this code can be improved (keeping
 * the compatibility).
 * 
 * @author samuel portela
 */
public abstract class RequestCallback {

    private static final Logger logger = LoggerFactory.getLogger(RequestCallback.class);

    /**
     * Method to be called when the response comes.
     */
    public abstract void callback(Response response);

    /**
     * Method to be called when something goes wrong with the request.
     */
    public void onException(Request request, HttpMethod httpMethod, Exception e) {
        e.printStackTrace();
        logger.error("An asynchronous request could not be made to " + request.getURI() + " using the HTTP " + httpMethod + " method", e);
    }

}
