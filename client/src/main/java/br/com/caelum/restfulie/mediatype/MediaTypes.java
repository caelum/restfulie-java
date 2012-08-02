package br.com.caelum.restfulie.mediatype;

import java.util.LinkedList;

import br.com.caelum.restfulie.RestfulieException;

/**
 * A media type registry.<br/>
 * Invoke register to add a new media types. Whenever two media type handlers can
 * handle the same media type, the latest added will be used to resolve the
 * conflict.
 *
 * @author guilherme silveira
 */
public class MediaTypes {

	public static final String XML = "application/xml";

	private final LinkedList<MediaType> types = new LinkedList<MediaType>();

	public MediaType forContentType(String searching) {
		searching = searching.split(";")[0];
		for (MediaType type : types) {
			if (type.answersTo(searching)) {
				return type;
			}
		}
		throw new RestfulieException("Unsupported media type '" + searching + "'");
	}

	public void register(MediaType mediaType) {
		this.types.addFirst(mediaType);
	}

}
