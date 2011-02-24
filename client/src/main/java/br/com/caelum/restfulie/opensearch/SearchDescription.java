package br.com.caelum.restfulie.opensearch;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

/**
 * Openserach document representation
 * @author jose donizetti
 *
 */
@XStreamAlias("OpenSearchDescription")
public class SearchDescription {

	@XStreamAlias("ShortName")
	private String shortName;
	
	@XStreamAlias("Description")
	private String description;
	
	@XStreamAlias("Tags")
	private Tags tags;
	
	@XStreamAlias("Contact")
	private String contact;
	
	@XStreamImplicit(itemFieldName="Url")
	private List<Url> urls;
	
	public SearchDescription() {
		tags = new Tags();
		urls = new ArrayList<Url>();
	}

	public List<Url> getUrls() {
		return urls;
	}
	
	public void setUrls(List<Url> urls) {
		this.urls = urls;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public Tags getTags() {
		return tags;
	}

	public void setTags(Tags tags) {
		this.tags = tags;
	}

	public Url use(String string) {
		for(Url url : urls) {
			if(url.getType().equals(string)) {
				return url;
			}
		}
		throw new RuntimeException("no such url");
	}


}
