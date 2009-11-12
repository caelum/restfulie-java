package br.com.caelum.restfulie;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;

import javax.xml.namespace.QName;

import br.com.caelum.restfulie.XStreamDeserializerTest.Order;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.ReflectionProviderWrapper;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;

/**
 * Deserialization support through xstream.
 * 
 * @author guilherme silveira
 * @author lucas souza
 * 
 */
public class XStreamDeserializer implements Deserializer {

	public Object fromXml(String xml) {
		XStream xstream = getXStream();
		return xstream.fromXML(xml);
	}
	
	private class MyWrapper extends MapperWrapper{

		public MyWrapper(Mapper wrapped) {
			super(wrapped);
		}
		
		@Override
		public String getFieldNameForItemTypeAndName(Class definedIn,
				Class itemType, String itemFieldName) {
			return super.getFieldNameForItemTypeAndName(definedIn, itemType, itemFieldName);
		}
	
	}

	/**
	 * Extension point to configure your xstream instance.
	 * 
	 * @return the xstream instance to use for deserialization
	 */
	protected XStream getXStream() {
		QNameMap qnameMap = new QNameMap();
		QName qname = new QName("http://www.w3.org/2005/Atom", "atom");
		qnameMap.registerMapping(qname, DefaultTransition.class);
		ReflectionProvider provider = getProvider();
		XStream xstream = new XStream(provider, new StaxDriver(qnameMap)) {
			@Override
			protected MapperWrapper wrapMapper(MapperWrapper next) {
				return new MyWrapper(next);
			}
		};
		xstream.alias("link", DefaultTransition.class);
		return xstream;
	}

	/**
	 * Extension point to define your own provider.
	 * @return
	 */
	private ReflectionProvider getProvider() {
		return new ReflectionProviderWrapper(new Sun14ReflectionProvider()) {
			@Override
			public Object newInstance(Class originalType) {
				if(originalType.isPrimitive() || originalType.equals(String.class) || originalType.equals(Enum.class) || Modifier.isFinal(originalType.getModifiers())) {
					Object instance = super.newInstance(originalType);
					return instance;
				}
				ClassPool pool = ClassPool.getDefault();
				Class myCustomClass;
				try {
					CtClass custom =   pool.makeClass("br.com.caelum.restfulie." + originalType.getSimpleName() + "_" + System.currentTimeMillis());
					custom.setSuperclass(pool.get(originalType.getName()));
					custom.addInterface(pool.get(Resource.class.getName()));
					CtField field = CtField.make("public java.util.List link = new java.util.ArrayList();", custom);
					custom.addField(field);
					CtMethod method = CtNewMethod.make("public java.util.Collection getTransitions() { return new java.util.ArrayList(); }", custom);
					custom.addMethod(method);
					myCustomClass = custom.toClass();
				} catch (NotFoundException e) {
					throw new IllegalStateException("Unable to extend type " + originalType.getName(), e);
				} catch (CannotCompileException e) {
					throw new IllegalStateException("Unable to extend type " + originalType.getName(), e);
				}
				Object finalInstance = super.newInstance(myCustomClass);
			    return finalInstance;
			}
			
			@Override
			public boolean fieldDefinedInClass(String arg0, Class arg1) {
				return super.fieldDefinedInClass(arg0, arg1);
			}
			
			@Override
			public Field getField(Class definedIn, String fieldName) {
				return super.getField(definedIn, fieldName);
			}
			@Override
			public Class getFieldType(Object object, String fieldName,
					Class definedIn) {
				return super.getFieldType(object, fieldName, definedIn);
			}
			
			@Override
			public void writeField(Object object, String fieldName,
					Object value, Class definedIn) {
				super.writeField(object, fieldName, value, definedIn);
			}
		};
	}

}
