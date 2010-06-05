package br.com.caelum.restfulie.http;

import java.util.ArrayList;
import java.util.List;

public class MediaTypes {
	
	private final List<MediaType > types = new ArrayList<MediaType>();

	public MediaType forContentType(String searching) {
		for(MediaType type : types) {
			if(type.answersTo(searching)) {
				return type;
			}
		}
		return null;
	}

	public void register(MediaType mediaType) {
		this.types .add(mediaType);
	}

}
