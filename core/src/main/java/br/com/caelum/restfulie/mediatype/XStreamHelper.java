package br.com.caelum.restfulie.mediatype;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import br.com.caelum.restfulie.Resource;
import br.com.caelum.restfulie.http.DefaultRelation;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.ReflectionProviderWrapper;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;

@SuppressWarnings("unchecked")
public class XStreamHelper {

	private final HierarchicalStreamDriver driver;

	public XStreamHelper(HierarchicalStreamDriver driver) {
		this.driver = driver;
	}

	/**
	 * Extension point for configuring your xstream instance. 
	 * @param typesToEnhance 
	 * @return an xstream instance with support for link enhancement.
	 */
	public XStream getXStream(List<Class> typesToEnhance) {
		ReflectionProvider provider = getProvider();

		XStream xstream = new XStream(provider, driver) {
			@Override
			protected MapperWrapper wrapMapper(MapperWrapper next) {
				return new LinkSupportWrapper(next);
			}
		};
		xstream.useAttributeFor(DefaultRelation.class, "rel");
		xstream.useAttributeFor(DefaultRelation.class, "href");

		for(Class type : typesToEnhance) {
			enhanceResource(type);
			xstream.processAnnotations(type);
		}
		for(Class customType : realTypes.values()) {
			xstream.addImplicitCollection(customType, "link","link", DefaultRelation.class);
		}
		return xstream;
	}

	/**
	 * Extension point to define your own provider.
	 * @return
	 */
	private ReflectionProvider getProvider() {
		return new ReflectionProviderWrapper(new Sun14ReflectionProvider()) {
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
		public Class realClass(String elementName) {
			return super.realClass(elementName);
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

	private final Map<Class,Class> realTypes= new HashMap<Class,Class>();
	
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
