package br.com.caelum.restfulie.mediatype;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.caelum.restfulie.Resource;
import br.com.caelum.restfulie.http.DefaultRelation;
import br.com.caelum.restfulie.relation.Enhancer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;
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

		@SuppressWarnings("rawtypes")
		@Override
		public Object newInstance(Class originalType) {
			if (realTypes.containsKey(originalType)) {
				return super.newInstance(realTypes.get(originalType));
			} else if (!Modifier.isFinal(originalType.getModifiers())) {
				// enhance now!
				Class enhanced = enhancer.enhanceResource(originalType);
				return super.newInstance(enhanced);
			}
			return super.newInstance(originalType);
		}
	}

	private class LinkSupportWrapper extends MapperWrapper {

		public LinkSupportWrapper(Mapper wrapped) {
			super(wrapped);
		}

		@SuppressWarnings("rawtypes")
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

		@SuppressWarnings("rawtypes")
		@Override
		public String serializedClass(Class type) {
			if (Resource.class.isAssignableFrom(type)) {
				return super.serializedClass(type.getSuperclass());
			}
			return super.serializedClass(type);
		}

		@SuppressWarnings("rawtypes")
		@Override
		public boolean shouldSerializeMember(Class definedIn, String fieldName) {
			if (Resource.class.isAssignableFrom(definedIn) && fieldName.equals("link")) {
				return false;
			}
			return super.shouldSerializeMember(definedIn, fieldName);
		}

	}

	private final HierarchicalStreamDriver driver;
	private final Enhancer enhancer;

	@SuppressWarnings("rawtypes")
	private final Map<Class, Class> realTypes = new HashMap<Class, Class>();

	public XStreamHelper(HierarchicalStreamDriver driver, Enhancer enhancer) {
		this.driver = driver;
		this.enhancer = enhancer;
	}

	/**
	 * Extension point to define your own provider.
	 *
	 * @return
	 */
	protected ReflectionProvider getProvider() {
		return new EnhancedLookupProvider(new Sun14ReflectionProvider());
	}

	/**
	 * Extension point for configuring your xstream instance.
	 *
	 * @param typesToEnhance
	 * @param list
	 * @return an xstream instance with support for link enhancement.
	 */
	@SuppressWarnings("rawtypes")
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
		xstream.useAttributeFor(DefaultRelation.class, "type");

		for (Class type : typesToEnhance) {
			realTypes.put(type, enhancer.enhanceResource(type));
			xstream.processAnnotations(type);
		}

		Class enhancedType = enhancer.enhanceResource(EnhancedList.class);
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
