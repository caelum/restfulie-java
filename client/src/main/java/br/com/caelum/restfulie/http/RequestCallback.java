package br.com.caelum.restfulie.http;

import br.com.caelum.restfulie.Response;

public abstract class RequestCallback {

    /**
     * Method to be called when the response comes.
     */
    public abstract void callback(Response response);

    /**
     * Method to be called when something goes wrong with the request.
     */
    public void onException(Exception e) {
        e.printStackTrace();
    }

}
