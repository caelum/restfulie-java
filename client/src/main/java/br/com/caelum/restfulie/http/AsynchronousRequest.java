package br.com.caelum.restfulie.http;

import java.util.concurrent.Callable;
import br.com.caelum.restfulie.Response;

public abstract class AsynchronousRequest implements Callable<Response> {

    private Request request;
    private HttpMethod httpMethod;

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
    
    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    /**
     * Method to be called when the response comes.
     */
    public abstract void callback(Response response);

    public Response call() {
        final Response response = getHttpMethod().execute(request);
        callback(response);
        return response;
    }

}
