package com.hush.models;

import com.hush.clients.FacebookClient;
import com.parse.ParseClassName;
import com.parse.ParseUser;

@ParseClassName("User")
public class User extends ParseUser {
	
	private ParseUser parseUser;
	private String name;
	private String facebookId;

	// Default constructor needed by parse
	public User() {
		super();
	}

	public User(ParseUser inParseUser) {
		parseUser = inParseUser;
		FacebookClient.getAndSetUserAttributesInParse(this);
	}
	
	public ParseUser getParseUser() {
		return parseUser;
	}

	public String getName() {
		return name;
	}
	
	public String getFacebookId() {
		return facebookId;
	}
	
	public boolean getIsNew() {
		return ParseUser.getCurrentUser().isNew();
	}
	
	public void setUserAttributesInParse(String inName, String inFacebookId) {
		facebookId = inFacebookId;
		
		name = inName;
		ParseUser user = ParseUser.getCurrentUser();
		user.put("name", name);
		user.saveInBackground();
	}
}
