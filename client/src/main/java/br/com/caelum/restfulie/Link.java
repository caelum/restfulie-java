package br.com.caelum.restfulie;

import br.com.caelum.restfulie.http.Request;

/**
 * A relation to any resource or resource state.
 *
 * @author guilherme silveira
 */
public interface Link {

	String getHref();

	String getRel();

	Request follow();
}
