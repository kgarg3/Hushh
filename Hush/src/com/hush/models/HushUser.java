package com.hush.models;

public class HushUser {

	/*
		List<Chats>
		List<FriendsChats>
	 */
	
	 public String fbIdLayerUserName;	// Also used as layerUserName
	 // String layerPassword;
	 public String firstName;
	 public String lastName;

	 private static HushUser hushUser;		// This is a singleton 

	 protected HushUser() {
	 }

	 public static HushUser getInstance(String fbId, String firstName, String lastName) {
		 if(hushUser != null) {
			 return hushUser;
		 }
		 
		 HushUser u = new HushUser();
		 u.fbIdLayerUserName = fbId;
		 u.firstName = firstName;
		 u.lastName = lastName;
		 
		 return u;
	 }
}
