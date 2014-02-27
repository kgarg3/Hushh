package com.hush.models;

import java.util.List;

import com.hush.clients.FacebookClient;
import com.hush.utils.AsyncHelper;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

@ParseClassName("User")
public class User extends ParseUser {

	private ParseUser parseUser;
	private String name;
	private String facebookId;
	private Chat currentChat;
	private List<Chat> chats;

	// Default constructor needed by parse
	public User() {
		super();
	}

	public User(ParseUser inParseUser) {
		parseUser = inParseUser;
		FacebookClient.fetchAndSetUserAttributesInParse(this);
	}

	public ParseUser getParseUser() {
		return parseUser;
	}

	public void saveToParse() {
		saveInBackground();
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

	public Chat getCurrentChat() {
		return currentChat;
	}

	public void setCurrentChat(Chat inCurrentChat) {
		currentChat = inCurrentChat;
	}




	// Chat APIs
	public ParseRelation<Chat> getChatsRelation() {
		ParseRelation<Chat> relation = getRelation("chats");
		return relation;
	}

	public void fetchChatsFromParse(final AsyncHelper ah) throws ParseException{
		ParseQuery<Chat> query = getChatsRelation().getQuery();

		// Show chats expiring sooner on top. Deleted chats will automatically be at the bottom
		query.addAscendingOrder("createdAt");
		query.whereEqualTo("userId", parseUser.getObjectId());
		query.findInBackground(new FindCallback<Chat>() {

			@Override
			public void done(List<Chat> chatResults, ParseException e) {
				if (e == null) {
					chats = chatResults;
				} else {					
					try {
						throw e;
					} catch (ParseException e1) { 
						chats = null;
					}
				}
				// Inform the caller that the operation was completed, so they can query the results back
				ah.chatsFetched();
			}
		});
	}

	public List<Chat> getChats() {
		return chats;
	}

	public void sendMessage(Chat chat) {
		getChatsRelation().add(chat);
	}

}
