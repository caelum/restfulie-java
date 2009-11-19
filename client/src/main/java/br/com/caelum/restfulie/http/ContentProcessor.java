package br.com.caelum.restfulie.http;

import java.io.IOException;

/**
 * Responsible for parsing http request's results.
 * 
 * @author guilherme silveira
 */
public interface ContentProcessor {

	public String read() throws IOException;

}
