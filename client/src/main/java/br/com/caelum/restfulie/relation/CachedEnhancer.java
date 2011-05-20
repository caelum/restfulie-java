package br.com.caelum.restfulie.relation;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Caires & Thiago Miranda
 * This cached implementation will solve 99% of the concurrency problems,
 * but it still may have some cases of concurrency.
 */

@SuppressWarnings("rawtypes")
public class CachedEnhancer implements Enhancer {

	private final Enhancer enhancer;
	private Map<Class, Class> cache = new HashMap<Class, Class>();

	public CachedEnhancer(Enhancer enhancer) {
		this.enhancer = enhancer;
	}

	public <T> Class enhanceResource(Class<T> originalType) {
		if(cache.containsKey(originalType)) {
			return originalType;
		}
		Class enhanced = enhancer.enhanceResource(originalType);
		cache.put(originalType, enhanced);
		return enhanced;
	}

}
