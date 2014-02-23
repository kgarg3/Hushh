package com.hush.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;


@ParseClassName("User")
public class User extends ParseObject {

	/*
		List<Chats>
		List<FriendsChats>
	 */
	
	 public String facebookId;	// Also used as layerUserName
	 public String name;

	 // Default constructor needed by parse
	 public User() {
	 }

	 public User(String inFacebookId, String inName) {
		 facebookId = inFacebookId;
		 name = inName;
	 }

	public static void setNameInParse(String name) {
		ParseUser currentUser = ParseUser.getCurrentUser();
		currentUser.put("name", name);
		currentUser.saveInBackground();
	}
}
