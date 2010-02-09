package br.com.caelum.restfulie;

/**
 * A relation to any resource or resource state.
 * 
 * @author guilherme silveira
 */
public interface Relation extends RelationToAccess {

	String getHref();
	
	String getRel();

}
