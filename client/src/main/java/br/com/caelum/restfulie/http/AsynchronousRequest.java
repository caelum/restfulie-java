package br.com.caelum.restfulie.http;

import java.util.concurrent.Callable;
import br.com.caelum.restfulie.Response;

/**
 * An asynchronous HTTP request.
 * 
 * @author samuel portela
 */
public class AsynchronousRequest implements Callable<Response> {

    private final Request request;
    private final HttpMethod httpMethod;
    private final Object payload;
    private final RequestCallback requestCallback;
    
    public AsynchronousRequest(Request request, HttpMethod httpMethod, RequestCallback requestCallback) {
        this(request, httpMethod, null, requestCallback);
    }
    
    public AsynchronousRequest(Request request, HttpMethod httpMethod, Object payload, RequestCallback requestCallback) {
        this.request = request;
        this.httpMethod = httpMethod;
        this.payload = payload;
        this.requestCallback = requestCallback;
    }

    public Request getRequest() {
        return request;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public Object getPayload() {
        return payload;
    }

    public Response call() {
        Response response = null;
        try {
            response = getHttpMethod().execute(request, payload);
            requestCallback.callback(response);
        }
        catch (Exception e) {
            requestCallback.onException(request, httpMethod, e);
        }
        return response;
    }

}
