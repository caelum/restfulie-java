package br.com.caelum.restfulie.http;

import java.util.List;

public interface Headers {

	public abstract String getMain(String string);

	public abstract List<String> getRaw(String key);

}
