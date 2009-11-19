package br.com.caelum.restfulie.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Responsible for parsing http request's results.
 * 
 * @author guilherme silveira
 */
public interface ContentProcessor {

	public String read() throws IOException;

}
