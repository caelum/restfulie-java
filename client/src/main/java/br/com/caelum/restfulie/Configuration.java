package br.com.caelum.restfulie;

public interface Configuration {

	void include(String ... fields);
	void exclude(String ... fields);
}
