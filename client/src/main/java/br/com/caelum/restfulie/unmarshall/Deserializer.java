package br.com.caelum.restfulie.unmarshall;

/**
 * Basic deserializer for hypermedia aware resources.
 * 
 * @author guilherme silveira
 */
public interface Deserializer {

	Object fromXml(String xml);
}
