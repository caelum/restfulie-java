package br.com.caelum.restfulie.http;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.caelum.restfulie.client.DefaultTransitionConverter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

/**
 * A xstream + jettison based media type implementation.
 * 
 * @author guilherme silveira
 */
@SuppressWarnings("unchecked")
public class JsonMediaType implements MediaType {

	private final List<String> types = Arrays.asList("application/json", "text/json", "json");

	private final XStreamHelper helper = new XStreamHelper(
			new JettisonMappedXmlDriver());

	@Override
	public boolean answersTo(String type) {
		return types.contains(type);
	}

	@Override
	public <T> void marshal(T payload, Writer writer) throws IOException {
		XStream xstream = helper.getXStream(getTypesToEnhance());
		xstream.toXML(payload, writer);
		writer.flush();
	}

	@Override
	public <T> T unmarshal(String content, MediaTypes types) {
		XStream xstream = helper.getXStream(getTypesToEnhance());
		xstream.registerConverter(new DefaultTransitionConverter(types));
		return (T) xstream.fromXML(content);
	}

	protected List<Class> getTypesToEnhance() {
		return new ArrayList<Class>();
	}

}
