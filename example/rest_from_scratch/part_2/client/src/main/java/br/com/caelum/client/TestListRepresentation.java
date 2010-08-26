package br.com.caelum.client;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import br.com.caelum.example.model.Item;
import br.com.caelum.restfulie.Resource;
import br.com.caelum.restfulie.Response;
import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.Restfulie;
import br.com.caelum.restfulie.mediatype.XmlMediaType;

public class TestListRepresentation {
	public static void main(String[] args) throws URISyntaxException, IOException {

		RestClient restfulie = Restfulie.custom();
		restfulie.getMediaTypes().register(new XmlMediaType() {
			@Override
			protected List<Class> getTypesToEnhance() {
				return Arrays.<Class>asList(Item.class);
			}

			@Override
			protected List<String> getCollectionNames() {
				return Arrays.asList("items");
			}
		});

		Response response = restfulie.at("http://localhost:8080/restfulie/items").accept("application/xml").get();
		List<Item> items = response.getResource();
		System.out.println(items);
		System.out.println(((Resource)items).getLink("basket").getHref());

	}
}
