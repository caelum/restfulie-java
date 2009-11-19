package br.com.caelum.restfulie.http;

import java.io.IOException;

/**
 * Simple processor which does nothing.
 * 
 * @author guilherme silveira
 */
public class IdentityContentProcessor implements ContentProcessor {

	public String read() throws IOException {
		return null;
	}

}
