package br.com.caelum.restfulie.mediatype;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.mapper.Mapper;
import com.thoughtworks.xstream.mapper.MapperWrapper;

/**
 * Implements a must ignore rule that sets an internal hashmap with unknown
 * fields.
 * 
 * @author guilherme silveira
 */
@SuppressWarnings("unchecked")
public class MustIgnoreWrapper extends MapperWrapper {

	private final XStreamHelper helper;

	public MustIgnoreWrapper(Mapper wrapper, XStreamHelper helper) {
		super(wrapper);
		this.helper = helper;
	}

	@Override
	public Class getItemTypeForItemFieldName(Class definedIn, String fieldName) {
		Class type = super.getItemTypeForItemFieldName(definedIn, fieldName);
		System.out.println(fieldName);
		if (type == null) {
			return MustIgnoreProperty.class;
		}
		return type;
	}

	@Override
	public SingleValueConverter getConverterFromItemType(String fieldName,
			Class type, Class definedIn) {
		System.out.println(fieldName);
		return super.getConverterFromItemType(fieldName, type, definedIn);
	}

	//	
	@Override
	public SingleValueConverter getConverterFromItemType(String fieldName,
			Class type) {
		System.out.println(fieldName);
		return super.getConverterFromItemType(fieldName, type);
	}

	//	
	@Override
	public Converter getLocalConverter(Class definedIn, String fieldName) {
		System.out.println(fieldName);
		return super.getLocalConverter(definedIn, fieldName);
	}

	//	
	// @Override
	public SingleValueConverter getConverterFromAttribute(Class definedIn,
			String attribute, Class type) {
		System.out.println(attribute);
		return super.getConverterFromAttribute(definedIn, attribute, type);
	}

	//	
	// @Override
	 public String realMember(Class type, String serialized) {
			System.out.println(serialized);
		 return super.realMember(type, serialized);
	 }

	@Override
	public String getFieldNameForItemTypeAndName(Class definedIn,
			Class itemType, String fieldName) {
		String field = super.getFieldNameForItemTypeAndName(definedIn,
				itemType, fieldName);
		System.out.println(fieldName + "222");
		if (field == null) {
			return "_mustIgnoreProperties";
		}
		return field;
	}

}
