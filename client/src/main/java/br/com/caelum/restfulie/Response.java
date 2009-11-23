package br.com.caelum.restfulie;

import java.io.IOException;
import java.util.List;

/**
 * A transition execution response.<br>
 * Through this interface, one can either access the original response
 * (depending on the http layer provider chosen) or, if any, the resulting
 * resource.
 * 
 * @author guilherme silveira
 */
public interface Response {

	public int getCode();

	public String getContent() throws IOException;

	public List<String> getHeader(String key);

	/**
	 * Returns the resource if any resource can be parsed from this response's content.
	 * @throws IOException 
	 */
	public <T> T getResource() throws IOException;
	
}
