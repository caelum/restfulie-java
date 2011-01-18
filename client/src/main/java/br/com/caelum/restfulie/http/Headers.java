package br.com.caelum.restfulie.http;

import java.util.List;

public interface Headers {

	public abstract String getMain(String string);

	/**
	 * returns a list with all values for this header
	 */
	public abstract List<String> getRaw(String key);

}
