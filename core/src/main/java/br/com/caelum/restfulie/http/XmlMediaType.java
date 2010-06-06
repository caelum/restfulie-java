package br.com.caelum.restfulie.http;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewMethod;
import javassist.NotFoundException;

import javax.xml.namespace.QName;

import br.com.caelum.restfulie.DefaultRelation;
import br.com.caelum.restfulie.Resource;
import br.com.caelum.restfulie.client.DefaultTransitionConverter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.ReflectionProviderWrapper;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;

/**
 * A default implemenation for xml media type based on XStream.<br/>
 * Extend it and override the getXStream method to configure the xstream instance with extra parameters.
 * 
 * @author guilherme silveira
 */
@SuppressWarnings("unchecked")
public class XmlMediaType implements MediaType {
	
	private final List<String> types = new ArrayList<String>();
	
	{
		types.add("application/xml");
		types.add("xml");
		types.add("text/xml");
	}
	
	@Override
	public boolean answersTo(String type) {
		return types.contains(type);
	}


	@Override
	public <T> void marshal(T payload, Writer writer) throws IOException {
		XStream xStream = getXStream();
		xStream.toXML(payload, writer);
		writer.flush();
	}


	@Override
	public <T> T unmarshal(String content, MediaTypes types) {
		XStream xstream = getXStream();
		xstream.registerConverter(new DefaultTransitionConverter(types));
		return (T) xstream.fromXML(content);
	}

	/**
	 * Extension point for configuring your xstream instance. 
	 * @return an xstream instance with support for link enhancement.
	 */
	protected XStream getXStream() {
		QNameMap qnameMap = new QNameMap();
		QName qname = new QName("http://www.w3.org/2005/Atom", "atom");
		qnameMap.registerMapping(qname, DefaultRelation.class);
		ReflectionProvider provider = getProvider();
		XStream xstream = new XStream(provider, new StaxDriver(qnameMap)) {
			@Override
			protected MapperWrapper wrapMapper(MapperWrapper next) {
				return new LinkSupportWrapper(next);
			}
		};
		xstream.useAttributeFor(DefaultRelation.class, "rel");
		xstream.useAttributeFor(DefaultRelation.class, "href");

		for(Class type : getTypesToEnhance()) {
			enhanceResource(type);
			xstream.processAnnotations(type);
		}
		for(Class customType : realTypes.values()) {
			xstream.addImplicitCollection(customType, "link","link", DefaultRelation.class);
		}
		return xstream;
	}
	
	private final Map<Class,Class> realTypes= new HashMap<Class,Class>();
	
	protected List<Class> getTypesToEnhance() {
		return new ArrayList<Class>();
	}

	/**
	 * Extension point to define your own provider.
	 * @return
	 */
	private ReflectionProvider getProvider() {
		return new ReflectionProviderWrapper(new Sun14ReflectionProvider()) {
			@Override
			public Object newInstance(Class originalType) {
				if(realTypes.containsKey(originalType)) {
					return super.newInstance(realTypes.get(originalType));
				} else if(!Modifier.isFinal(originalType.getModifiers())) {
					// enhance now!
					Class enhanced = enhanceResource(originalType);
					return super.newInstance(enhanced);
				}
				return super.newInstance(originalType);
			}
		};
	}


	private class LinkSupportWrapper extends MapperWrapper{

		public LinkSupportWrapper(Mapper wrapped) {
			super(wrapped);
		}
		
		@Override
		public String getFieldNameForItemTypeAndName(Class definedIn,
				Class itemType, String itemFieldName) {
			if(realTypes.containsKey(definedIn) && itemFieldName.equals("link")) {
				return "link";
			}
			return super.getFieldNameForItemTypeAndName(definedIn, itemType, itemFieldName);
		}
	
	}

	public <T> Class enhanceResource(Class<T> originalType) {
		ClassPool pool = ClassPool.getDefault();
		try {
			// TODO extract this enhancement to an interface and test it appart
			CtClass newType =   pool.makeClass("br.com.caelum.restfulie." + originalType.getSimpleName() + "_" + System.currentTimeMillis());
			newType.setSuperclass(pool.get(originalType.getName()));
			newType.addInterface(pool.get(Resource.class.getName()));
			CtField field = CtField.make("public java.util.List link = new java.util.ArrayList();", newType);
			newType.addField(field);
			newType.addMethod(CtNewMethod.make("public java.util.List getRelations() { return link; }", newType));
			newType.addMethod(CtNewMethod.make("public br.com.caelum.restfulie.Relation getRelation(String rel) { for(int i=0;i<link.size();i++) {br.com.caelum.restfulie.Relation t = link.get(i); if(t.getRel().equals(rel)) return t; } return null; }", newType));
			Class customType = newType.toClass();
			this.realTypes.put(originalType, customType);
			return customType;
		} catch (NotFoundException e) {
			throw new IllegalStateException("Unable to extend type " + originalType.getName(), e);
		} catch (CannotCompileException e) {
			throw new IllegalStateException("Unable to extend type " + originalType.getName(), e);
		}
	}

}
