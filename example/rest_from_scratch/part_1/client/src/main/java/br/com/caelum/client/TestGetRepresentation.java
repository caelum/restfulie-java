package br.com.caelum.client;

import java.io.IOException;
import java.net.URISyntaxException;

import br.com.caelum.example.model.Item;
import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.Restfulie;
import br.com.caelum.restfulie.mediatype.XmlMediaType;

import com.thoughtworks.xstream.XStream;

public class TestGetRepresentation {
	public static void main(String[] args) throws URISyntaxException, IOException {
		RestClient restfulie = Restfulie.custom();
		restfulie.getMediaTypes().register(new XmlMediaType() {
			@Override
			protected void configure(XStream xstream) {
				xstream.processAnnotations(Item.class);
			}
		});

		Response response = restfulie.at("http://localhost:8080/restfulie/items/2").accept("application/xml").get();
		Item item = response.getResource();
		System.out.println(item.getName());

	}
}
