package com.hush.models;

import com.parse.ParseClassName;
import com.parse.ParseUser;


@ParseClassName("User")
public class User extends ParseUser {
	
	 public String name;

	 // Default constructor needed by parse
	 public User() {
		 super();
	 }

	 public User(String inName) {
		 name = inName;
	 }

	public static void setNameInParse(String name) {
		ParseUser user = ParseUser.getCurrentUser();
		user.put("name", name);
		user.saveInBackground();
	}
}
