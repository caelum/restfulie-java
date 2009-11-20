package br.com.caelum.restfulie;

public class Restfulie {

	public static <T> Resource resource(T object) {
		return (Resource) object;
	}

	public static Resources server() {
		return new DefaultResources();
	}

}
