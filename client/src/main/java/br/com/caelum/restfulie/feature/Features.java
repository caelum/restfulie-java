package br.com.caelum.restfulie.feature;

/**
 * Lookup methods for features
 * @author jose donizetti
 */
public class Features {
	
	public static ResponseFeature throwError() {
		return new ThrowError();
	}
	
	public static RequestFeature retryWhenUnavaiable() {
		return new RetryWhenUnavailable();
	}

}
