package br.com.caelum.restfulie.mediatype;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.caelum.restfulie.http.DefaultRelation;
import br.com.caelum.restfulie.relation.Enhancer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.ReflectionProviderWrapper;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;
import com.thoughtworks.xstream.io.HierarchicalStreamDriver;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;

@SuppressWarnings("unchecked")
public class XStreamHelper {

	private final class EnhancedLookupProvider extends
			ReflectionProviderWrapper {
		private EnhancedLookupProvider(ReflectionProvider wrapper) {
			super(wrapper);
		}

		@Override
		public Object newInstance(Class originalType) {
			if (realTypes.containsKey(originalType)) {
				return super.newInstance(realTypes.get(originalType));
			} else if (!Modifier.isFinal(originalType.getModifiers())) {
				// enhance now!
				Class enhanced = new Enhancer().enhanceResource(originalType);
				return super.newInstance(enhanced);
			}
			return super.newInstance(originalType);
		}
	}

	@SuppressWarnings("deprecation")
	private class LinkSupportWrapper extends MapperWrapper {

		public LinkSupportWrapper(Mapper wrapped) {
			super(wrapped);
		}

		@Override
		public String getFieldNameForItemTypeAndName(Class definedIn,
				Class itemType, String itemFieldName) {
			if (realTypes.containsKey(definedIn)
					&& itemFieldName.equals("link")) {
				return "link";
			}
			return super.getFieldNameForItemTypeAndName(definedIn, itemType,
					itemFieldName);
		}

	}

	private final HierarchicalStreamDriver driver;

	private final Map<Class, Class> realTypes = new HashMap<Class, Class>();

	public XStreamHelper(HierarchicalStreamDriver driver) {
		this.driver = driver;
	}

	/**
	 * Extension point to define your own provider.
	 *
	 * @return
	 */
	private ReflectionProvider getProvider() {
		return new EnhancedLookupProvider(new Sun14ReflectionProvider());
	}

	/**
	 * Extension point for configuring your xstream instance.
	 *
	 * @param typesToEnhance
	 * @param list
	 * @return an xstream instance with support for link enhancement.
	 */
	public XStream getXStream(List<Class> typesToEnhance, List<String> collectionNames) {
		ReflectionProvider provider = getProvider();

		XStream xstream = new XStream(provider, driver) {
			@Override
			protected MapperWrapper wrapMapper(MapperWrapper next) {
				return new LinkSupportWrapper(next);
			}

		};
		xstream.useAttributeFor(DefaultRelation.class, "rel");
		xstream.useAttributeFor(DefaultRelation.class, "href");

		for (Class type : typesToEnhance) {
			realTypes.put(type, new Enhancer().enhanceResource(type));
			xstream.processAnnotations(type);
		}

		Class enhancedType = new Enhancer().enhanceResource(EnhancedList.class);
		realTypes.put(EnhancedList.class, enhancedType);

		for (String name : collectionNames) {
			xstream.alias(name, enhancedType);
		}

		for (Class type : typesToEnhance) {
			xstream.addImplicitCollection(enhancedType, "elements", xstream.getMapper().serializedClass(type), type);
		}

		for (Class customType : realTypes.values()) {
			xstream.addImplicitCollection(customType, "link", "link", DefaultRelation.class);
		}
		return xstream;
	}

}
