package br.com.caelum.restfulie.mediatype;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.caelum.restfulie.RestClient;
import br.com.caelum.restfulie.mediatype.MediaType;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Media Type implementation for unmarshalling data received in JSON format using GSON library
 * 
 * @author Felipe Brandao
 *
 */
public class GsonMediaType implements MediaType{
	
	private final static Pattern rootTypeDetectionRegex = Pattern.compile( "^\\{[ ]*\"([^\"]*)\"[ ]*:(.*)}$" );
	
	private final List<String> types = Arrays.asList("application/json", "text/json", "json");
	
	private Map<String,Class<?>> classes = new HashMap<String, Class<?>>();
	private Map<String,Type> collections = new HashMap<String, Type>();

	private Gson gson;
	
	public GsonMediaType() {
		this.gson = new Gson();
	}
	
	/**
	 * Register a class which should be used for unmarshalling.
	 * <br>
	 * It uses the lower case class name as alias to knows what should be unmarshalled.
	 * <br>
	 * @see #withType(String, Class)
	 * @param javaType Java class to be registered
	 * @return This instance
	 */
	public GsonMediaType withType( Class<?> javaType ){
		String correctAlias = javaType.getSimpleName();
		correctAlias = correctAlias.substring( 0 , 1 ).toLowerCase() + correctAlias.substring( 1 );
		return this.withType( correctAlias , javaType );
	}

	/**
	 * Register a class which should be used for unmarshalling.
	 * <br>
	 * Uses the informed alias to knows what should be unmarshalled.
	 * <br>
	 * @param alias Alias to the class
	 * @param javaType Java class to be registered
	 * @return This instance
	 */
	public GsonMediaType withType( String alias , Class<?> javaType ){
		this.classes.put( alias , javaType );
		return this;
	}
	
	/**
	 * Register an alias to identify a JSON resource (usually top level arrays) which should be unmarshalled for a
	 * specific Collection.
	 * 
	 * @see TypeToken
	 * @param alias Collection alias
	 * @param collectionType Java Type which sould be used (use TypeToken)
	 * @return This instance
	 */
	public GsonMediaType withCollection( String alias , Type collectionType ) {
		this.collections.put( alias , collectionType );
		return this;		
	}
	
	@Override
	public boolean answersTo( String type ) {
		return types.contains( type );
	}

	@Override
	public <T> void marshal( T payload, Writer writer, RestClient client ) throws IOException {
		String json = this.gson.toJson( payload );
		System.out.println( "Marshalled object:" + json );
		writer.append( json );
	}

	@Override
	public <T> T unmarshal( String content, RestClient client ) {
		JsonData jsonData = new JsonData();
		jsonData.json = content;
		
		Class<T> rootClass = detectRootClass( jsonData );
		if( rootClass != null ){ //we know wich class should be used
			return  this.gson.fromJson( jsonData.json , rootClass );
		}else{//detection failed, will try to detect as top level array (alias for collection)
			Type rootType = detectRootType( jsonData );
			
			if( rootType == null ){//we don't know exactly what to do, so let's blow up
				throw new IllegalArgumentException( "There's no registered class/collection alias for '" + jsonData.alias + "'" );
			}
		
			
			return  this.gson.fromJson( jsonData.json , rootType );	
		}
	}
	
	/**
	 * Detects which class should be used for unmarshalling
	 * @param <T>
	 * @param jsonData
	 * @return A class object for unmarshalling JSON data
	 */
	@SuppressWarnings("unchecked")
	private <T> Class<T> detectRootClass( JsonData jsonData ){
		Matcher matcher = rootTypeDetectionRegex.matcher( jsonData.json );
		if( matcher.matches() ){
			String alias = matcher.group(1);
			//defines informations inside jsonData, if fails to detect a registered class for unmarshall (could be a collection)
			jsonData.alias = alias;
			jsonData.json = matcher.group(2);
			
			Class<T> javaClass = (Class<T>) this.classes.get( alias );
			return javaClass;
		}
		return null;
	}

	/**
	 * Detects which type should be used for unmarshalling a top level array
	 * @param jsonData
	 * @return
	 */
	private Type detectRootType( JsonData jsonData ){
		return this.collections.get( jsonData.alias );
	}
	
	
	/**
	 * Handler for value related with the (un)marshalling
	 * @author felipebn
	 */
	private final static class JsonData{
		String alias;
		String json;
	}
}
