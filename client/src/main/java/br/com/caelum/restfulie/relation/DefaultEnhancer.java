package br.com.caelum.restfulie.relation;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewMethod;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import br.com.caelum.restfulie.Resource;

@SuppressWarnings("rawtypes")
public class DefaultEnhancer implements Enhancer {

	public <T> Class enhanceResource(Class<T> originalType) {
		ClassPool pool = ClassPool.getDefault();
		if (pool.find(DefaultEnhancer.class.getName()) == null) {
			pool.appendClassPath(new LoaderClassPath(getClass().getClassLoader()));
		}
		try {
			// TODO extract this enhancement to an interface and test it appart
			CtClass newType =   pool.makeClass("br.com.caelum.restfulie." + originalType.getSimpleName() + "_" + System.currentTimeMillis());
			newType.setSuperclass(pool.get(originalType.getName()));
			newType.addInterface(pool.get(Resource.class.getName()));
			enhanceLinks(newType);
			return newType.toClass();
		} catch (NotFoundException e) {
			throw new IllegalStateException("Unable to extend type " + originalType.getName(), e);
		} catch (CannotCompileException e) {
			throw new IllegalStateException("Unable to extend type " + originalType.getName(), e);
		}
	}

	private void enhanceLinks(CtClass newType) throws CannotCompileException {
		CtField field = CtField.make("public java.util.List link = new java.util.ArrayList();", newType);
		newType.addField(field);
		newType.addMethod(CtNewMethod.make("public java.util.List getLinks() { return link; }", newType));
		newType.addMethod(CtNewMethod.make("public java.util.List getLinks(String rel) { java.util.List links = new java.util.ArrayList(); for(int i=0;i<link.size();i++) {br.com.caelum.restfulie.Link t = link.get(i); if(t.getRel().equals(rel)) links.add(t); } return links; }", newType));
		newType.addMethod(CtNewMethod.make("public br.com.caelum.restfulie.Link getLink(String rel) { for(int i=0;i<link.size();i++) {br.com.caelum.restfulie.Link t = link.get(i); if(t.getRel().equals(rel)) return t; } return null; }", newType));
		newType.addMethod(CtNewMethod.make("public boolean hasLink(String link) { return getLink(link)!=null; }", newType));
	}

}
