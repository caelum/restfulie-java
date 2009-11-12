package br.com.caelum.restfulie;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.xml.namespace.QName;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.ReflectionProvider;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;
import com.thoughtworks.xstream.io.xml.QNameMap;
import com.thoughtworks.xstream.io.xml.StaxDriver;

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
		XStream xstream = new XStream(provider, new StaxDriver(qnameMap));
		xstream.alias("link", DefaultTransition.class);
		return xstream;
	}

	/**
	 * Extension point to define your own provider.
	 * @return
	 */
	private ReflectionProvider getProvider() {
		return new Sun14ReflectionProvider() {
			@Override
			public Object newInstance(Class type) {
				final Object instance = super.newInstance(type);
				if(type.isPrimitive() || type.equals(String.class) || type.equals(Enum.class)) {
					return instance;
				}
			    Enhancer enhancer = new Enhancer();
			    enhancer.setSuperclass(type);
				MethodInterceptor interceptor = new MethodInterceptor() {
			        public Object intercept(Object proxy, Method method, Object[] args, final MethodProxy methodProxy)throws Throwable {
						return method.invoke(instance, args);
			    	}
			    };
			    enhancer.setCallback(interceptor);
			    Object myInstance = enhancer.create();
			    return myInstance;
			}
		};
	}

}
