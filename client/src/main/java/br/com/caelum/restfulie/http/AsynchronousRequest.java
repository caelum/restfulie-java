package br.com.caelum.restfulie.http;

import java.util.concurrent.Callable;
import br.com.caelum.restfulie.Response;

public abstract class AsynchronousRequest implements Callable<Response> {

    private Request request;
    private HttpMethod httpMethod;
    private Object payload;
    
    public AsynchronousRequest setUp(Request newRequest, HttpMethod newHttpMethod) {
        this.request = newRequest;
        this.httpMethod = newHttpMethod;
        return this;
    }
    
    public AsynchronousRequest setUp(Request newRequest, HttpMethod newHttpMethod, Object newPayload) {
        this.request = newRequest;
        this.httpMethod = newHttpMethod;
        this.payload = newPayload;
        return this;
    }

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

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    /**
     * Method to be called when the response comes.
     */
    public abstract void callback(Response response);

    /**
     * Method to be called when something goes wrong with the request.
     */
    public void onError(Exception e) {
        e.printStackTrace();
    }

    public Response call() {
        Response response = null;
        try {
            response = getHttpMethod().execute(request, payload);
            callback(response);
        }
        catch (Exception e) {
            onError(e);
        }
        return response;
    }

}
