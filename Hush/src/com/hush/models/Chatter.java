package com.hush.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Chatter")
public class Chatter extends ParseObject {

	public String facebookId;
	public String name;

	// Default constructor needed by parse
	public Chatter() {
		super();
	}

	public Chatter(String facebookId, String name) {
		setFacebookId(facebookId);
		setName(name);
	}

	public void saveToParse() {
		saveEventually();
	}

	public String getFacebookId() {
		facebookId = getString("facebookId");
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		put("facebookId", facebookId);
	}

	public String getName() {
		name = getString("name");
		return name;
	}

	public void setName(String name) {
		put("name", name);
	}
}
