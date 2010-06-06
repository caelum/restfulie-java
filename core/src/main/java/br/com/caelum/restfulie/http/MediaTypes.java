package br.com.caelum.restfulie.http;

import java.util.LinkedList;

/**
 * A media type registry.<br/>
 * Invoke register to add new media types. Whenever two media type handlers can
 * handle the same media type, the latest added will be used to resolve the
 * conflict.
 * 
 * @author guilherme silveira
 */
public class MediaTypes {

	private final LinkedList<MediaType> types = new LinkedList<MediaType>();

	public MediaType forContentType(String searching) {
		for (MediaType type : types) {
			if (type.answersTo(searching)) {
				return type;
			}
		}
		return null;
	}

	public void register(MediaType mediaType) {
		this.types.addFirst(mediaType);
	}

}
