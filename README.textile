h1. Website

Check out "Restfulie's website":http://restfulie.caelumobjects.com if you still did not.

h1. Restfulie: quit pretending

CRUD through HTTP is a good step forward to using resources and becoming RESTful, another step further into it is to make use of hypermedia based services and this gem allows you to do it really fast.

You can read the "article on using the web for real":http://guilhermesilveira.wordpress.com/2009/11/03/quit-pretending-use-the-web-for-real-restfulie/ which gives an introduction to hypermedia/aware resources.

h2. Why would I use restfulie?

1. Easy --> writing hypermedia aware resource based clients
2. Easy --> hypermedia aware resource based services
3. Small -> it's not a bloated solution with a huge list of APIs
4. HATEOAS --> clients you are unaware of will not bother if you change your URIs
5. HATEOAS --> services that you consume will not affect your software whenever they change part of their flow or URIs

h2. Could you compare it with other REST related APIs?

Restfulie was the first API trying to somehow implement "Jim Webber":http://jim.webber.name/ point of view on how RESTFul systems use hypermedia as the way to lead your client's path through a business process.

You can see a "3rd party comparison":http://code.google.com/p/implementing-rest/wiki/ByLanguage between all REST frameworks.

Therefore Restfulie is unique in its feature set when compared to other (JAX-RS) based implementations: looking for simple code and favoring conventions over manual configurations when creating hypermedia aware system. Restfulie also handle content negotiation and its client implements cache and other set of features unsupported so far in other frameworks.

According to Richardson Maturity Model , systems are only to be called RESTFul if they support this kind of state flow transition through hypermedia content contained within resources representations:

<pre>
<order>
	<product>basic rails course</product>
	<product>RESTful training</product>
	<atom:link rel="payment" href="http://www.caelum.com.br/orders/1/pay" xmlns:atom="http://www.w3.org/2005/Atom"/>
	<atom:link rel="cancel" href="http://www.caelum.com.br/orders/1" xmlns:atom="http://www.w3.org/2005/Atom"/>
</order>
</pre>

If you are to implement a 3rd level (restful) service, Restfulie is the way to go. 

h2. More examples

There is a "Restfulie guide being built":http://github.com/caelum/restfulie-guide but still in beta version.
You can also "download a sample application":http://code.google.com/p/restfulie , both client and server code.
FInally, do not forget to ask your questions at our "mailing list":http://groups.google.com/group/restfulie-java?lnk=srg .

h2. Java or Ruby

Restfulie comes many different flavors, "java":http://github.com/caelum/restfulie-java and "ruby":http://github.com/caelum/restfulie.

h1. One minute examples

h2. Client side

The client side code allows you to hide http-protocol specifics if required, while allowing you to re-configure it when needed.
Example on accessing a resource and its services through the restfulie API:

<pre>
Order order = new Order();

// place the order
order = service("http://www.caelum.com.br/order").post(order);

// cancels it
resource(order).getRelation("cancel").execute();
</pre>

h2. Server side

This is a simple example how to make your state changes available to your resource consumers:

<pre>
public class Order implements HypermediaResource {

	public List<Relation> getRelations(Restfulie control) {
		if (status.equals("unpaid")) {
			control.relation("latest").uses(OrderingController.class).get(this);
			control.relation("cancel").uses(OrderingController.class).cancel(this);
		}
		return control.getRelations();
	}

}
</pre>

h1. Installation

h2. Download everything

Start "downloading all data":http://code.google.com/p/restfulie : the client jars, "vraptor":http://www.vraptor.org jars and both server side and client side application.

You can download a sample client and server side application on the same link, those will be helpful for you too understand how to use Restfulie.

h2. Client side installation

In order to use Restfulie in your client side app, simply add "all required jars":http://code.google.com/p/restfulie/ to your classpath.

h2. Server side installation

Download "vraptor's blank project":http://www.vraptor.org and configure your web.xml file. You are ready to go.

h1. Client side usage

The entry point for *Restfulie's* api is the *Restfulie* class. It's basic usage is through the *resource* method which, given an URI, will allow
you to retrieve a resource or post to a resource controller:

<pre>
  Order order = Restfulie.resource("http://www.caelum.com.br/orders/1").get();
  
  Client client = new Client();
  Restfulie.resource("http://www.caelum.com.br/clients").post(client);
</pre>

Due to the nature of the entry point and the java bytecode, Restfulie is still unable to allow the user to make the http verb even more transparent.

As seen earlier, as soon as you have acquired an object through the use of the restfulie api, you can invoke its transitions:

<pre>
Order order = Restfulie.resource("http://www.caelum.com.br/orders/1").get();
resource(order).getRelation("cancel").access();
</pre>

The *resource* method can be statically imported from the *Restfulie* class.

h2. Serialization configuration

Restfulie uses XStream behind the scenes, therefore all XStream related annotations are supported by default when using it.
The following example shows how to alias a type:

<pre>
@XStreamAlias("order")
public class Order {
}
</pre>

More info on how to configure XStream through the use of annotations can be "found in its website":"http://xstream.codehaus.org".

By default, Restfulie serializes all primitive, String and enum types. In order to serialize child elements, one has pre-configure Restfulie. This is
the typical usage-pattern applications will face while using restfulie:

<pre>
Resources resources = Restfulie.resources();
resources.configure(Order.class).include("items");

// the configuration step is completed, so lets use it now:
resources.entryAt("http://www.caelum.com.br/clients").post(new Client());
</pre>

The entire serialization process can be configured either through the *Resources* interface's methods or using *XStream*'s explicit configuration.

h2. Caching

Most REST frameworks will not help the developer providing etags, last modified and other cache related headers.

Meanwhile, in the server side, Restfulie might add extra headers to handle last modified, etag and max age situations that will improve response time and avoid useless bandwidth consumption. In order to benefit from such cache characteristics, simply implement the RestfulEntity interface.

h2. Accessing all possible transitions

One can access all possible transitions for an object by invoking a resource's *getRelations* method:

<pre>
	List<Relation> relations = resource(order).getRelations();
</pre>

While typical level 2 frameworks will only provide a statically, compilation time checked, relation/transition invocation, Restfulie allows clients/bots to adapt to REST results, giving your clients even less coupling to your services protocol.

h2. HTTP verbs

By default, restfulie uses a well known table of defaults for http verb detection according to the rel element:

* destroy, cancel and delete send a DELETE request
* update sends a PUT request
* refresh, reload, show, latest sends a GET request
* other methods sends a POST request

If you want to use a custom http verb in order to send your request, you can do it:

<pre>
 payment = resource(order).getRelation("payment").method(HttpMethod.PUT).accessAndRetrieve(payment);
 </pre>

h2. Sending some parameters

If you need to send some information to the server, this can be done by passing an argument to the execute method, which will be serialized and sent as the request body's content:

<pre>
 payment = resource(order).getRelation("payment").method(HttpMethod.PUT).accessAndRetrieve(payment);
 </pre>

h2. More info

Once you have found the entry point you want to use (retrieving a resource or creating one), the javadoc api is a resourcefull place for more info.


h1. Server side usage

The default way to use Restfulie is to define the getRelations method in your resource. The method receives a *Restfulie* instance (server side version) which allows you to dsl-like create transitions. In order to do that, given a *Restfulie* object, invoke the transition method with your *rel* name and the relative *controller action*:

<pre>
	public List<Relation> getRelations(Restfulie control) {
		control.relation("delete").uses(OrderingController.class).cancel(this);
		return control.getRelations();
	}
</pre>

Note that both the *OrderingController* class with its *cancel* method are web methods made available through the use of vraptor:

<pre>
@Resource
public OrderingController {

	@Delete
	@Path("/order/{order.id}")
	@Transition
	public void cancel(Order order) {
		order = database.getOrder(order.getId());
		order.cancel();
		status.ok();
	}
}
</pre>

Now you need to set up your application package in web.xml. This is the only configuration required:

<pre>
	<context-param>
        <param-name>br.com.caelum.vraptor.packages</param-name>
        <param-value>br.com.caelum.vraptor.restfulie,com.your.app.package.without.leading.whitespace</param-value>
    </context-param>
</pre>

h2. Relation/Transition invocation

By using the *@Transition* to annotate your method, Restfulie will automatically load the order from the database and check for either 404 (object not found), 405 (method not allowed), 409 (conflict: transition is not allowed for this resource's state) and 406 (content negotiation failed).

This is one of the advantages of using Restfulie over other level 2 Rest frameworks. By supporting hypermedia content and handling transitions out of the box, Restfulie creates a new layer capable
of helping the server to deal with unexpected states. 

h2. Typical example

1. Create your model (i.e. Order)

<pre>
@XStreamAlias("order")
public class Order {

	private String id;
	private Location location;
	private List<Item> items;

	private transient String status;
	private Payment payment;

	public enum Location {
		takeAway, drinkIn
	};
	
	// ...

}

@XStreamAlias("item")
public class Item {
	enum Coffee {LATTE, CAPPUCINO, ESPRESSO};
	enum Milk {SKIM, SEMI, WHOLE};
	enum Size {SMALL, MEDIUM, LARGE};

	private Coffee drink;
	private int quantity;
	private  Milk milk;
	private Size size;

	// ...

}
</pre>

2. Usually the *getRelations* method would check the resource state in order to coordinate which transitions can be executed:
So add the *getRelations* method returning an array of possible transitions/relations:

<pre>
public class Order implements HypermediaResource {


	public List<Relation> getRelations(Restfulie control) {
		if (status.equals("unpaid")) {
			control.relation("latest").uses(OrderingController.class).get(this);
			control.relation("cancel").uses(OrderingController.class).cancel(this);
			control.relation("payment").uses(OrderingController.class).pay(this,null);
		}
		return control.getRelations();
	}

}</pre>


3. Create your *retrieval* method:

<pre>
	@Get
	@Path("/order/{order.id}")
	public void get(Order order) {
		order = database.getOrder(order.getId());
		result.use(xml()).from(order).include("items").serialize();
	}
</pre>

You are ready to go. Create a new order and access it through your /order/id path.
The best way to start is to download the sample application and go through the *OrderingController* and *Order* classes.

h3. Content negotiation

While most REST frameworks only support rendering xml out of the box, Restfulie already provides (through VRaptor and Spring) xml, xhtml and json representations of your resource. You can add new serializers as required. In order to take
content negotiation into play, simply use VRaptor's representation() renderer:

<pre>
	@Get
	@Path("/order/{order.id}")
	public void get(Order order) {
		order = database.getOrder(order.getId());
		result.use(representation()).from(order).include("items").serialize();
	}
</pre>

h3. Creating relations to other servers

<pre>
public class Order implements HypermediaResource {


	public List<Relation> getRelations(Restfulie control) {
		control.relation("up").at('http://caelumobjects.com');
		return control.getRelations();
	}

}</pre>

Note that you can either create relations or transitions. We suggest clients to only use relations, but for clear semantics in some servers, you might want to invoke control.transition.

h2. Accepting more than one argument

While typical JAX-RS services will deserialize your request body into your method argument and require you to retrieve extra URI information through the requested URI, Restfulie accepts one core parameter (based on its alias) and extra parameters to be extracted through the URI itself:

<pre>
	@Post
	@Path("/order/{order.id}/pay")
	@Consumes
	@Transition
	public void pay(Order order, Payment payment) {
		order = database.getOrder(order.getId());
		order.pay(payment);
		status.ok();
	}
</pre>

Parameter support is provided through VRaptor, so Iogi and Paranamer support is already built-in.


h1. Asynchronous Request

To make an asynchronous request, you can use getAsync, postAsync, putAsync or deleteAsync methods. For that, you must provide a RequestCallback instance with a callback method implementation (the code to be executed when the response comes), like this:

<pre>
RequestCallback requestCallback = new RequestCallback() {
    @Override
    public void callback(Response response) {
        // code to be executed when the response comes
    }
};

Future<Response> future1 = Restfulie.at("http://www.caelum.com.br/clients").getAsync(requestCallback);
</pre>

If you prefer, you can abbreviate it this way:

<pre>
Future<Response> future1 = Restfulie.at("http://www.caelum.com.br/clients").getAsync(new RequestCallback() {
    @Override
    public void callback(Response response) {
        // code to be executed when the response comes
    }
});
</pre>

h2. Using Log4j to log exceptions from Asynchronous Request

If you want Log4j to log the exceptions occurred when something goes wrong with the asynchronous request, you can create a log4j.xml configuration file on your classpath like this:

<pre>
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE log4j:configuration SYSTEM "log4j.dtd">
<log4j:configuration xmlns:log4j="http://jakarta.apache.org/log4j/">
        
        <appender name="stdout" class="org.apache.log4j.ConsoleAppender">
                <layout class="org.apache.log4j.PatternLayout">
                        <param name="ConversionPattern" 
                                value="%d{HH:mm:ss,SSS} %5p [%-20c{1}] %m%n"/>
                </layout>
        </appender>

        <category name="br.com.caelum.restfulie">
                <priority value="ERROR" />
                <appender-ref ref="stdout" />
        </category>

</log4j:configuration>
</pre>

h2. Getting help and mailing lists

If you are looking for or want to help, let us know at the mailing list:

"http://groups.google.com/group/restfulie-java":http://groups.google.com/group/restfulie-java

"VRaptor's website":http://www.vraptor.org also contain its own mailing list which can be used to get help on implementing controller's.

h2. Team

Restfulie was created and is maintained within Caelum by

Projetct Founder
* "Guilherme Silveira":http://guilhermesilveira.wordpress.com ( "email":mailto:guilherme.silveira@caelum.com.br ) - twitter:http://www.twitter.com/guilhermecaelum

Contributors
* Lucas Cavalcanti ("email":mailto:lucas.cavalcanti@caelum.com.br) - twitter:http://www.twitter.com/lucascs
* "Adriano Almeida":http://ahalmeida.com/ ("email":mailto:adriano.almeida@caelum.com.br) - twitter:http://www.twitter.com/adrianoalmeida7
* Samuel Portela ("email":mailto:negociosnainternet@gmail.com) - twitter:http://www.twitter.com/samuel_portela

h3. Example

You can see an "application's source code":http://github.com/caelum/restfulie-java/tree/master/example/, both client and server side were implemented using *restfulie*:

h2. Contributing

Users are encouraged to contribute with extra implementations for each layer (i.e. spring mvc implementation for the controller layer).

h2. Inner libraries

In its Java version, Restfulie uses by default:

* VRaptor:http://www.vraptor.org as the server-side controller  
* XStream:"http://xstream.codehaus.org" as its serialization library
* java.net api for http requests
* Spring IoC for dependency injection

XStream is the most famous java serialization tool around with support both to json and xml while VRaptor (as Rails) supplies a reverse URI lookup system upon its controller which provides a way to identify URI's from well defined transitions.

h2. License

Check the "license file":LICENSE

<script type="text/javascript">
var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
try {
var pageTracker = _gat._getTracker("UA-11770776-1");
pageTracker._trackPageview();
} catch(err) {}</script>