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
public class User extends ParseUser implements AsyncHelper {
	
	private String name;
	private String facebookId;
	private Chat currentChat;
	private List<Chat> chats;

	// Construct a new user
	public User() {
		super();
		FacebookClient.fetchAndSetUserAttributes(this);
	}
	
	public void saveToParse() {
		getCurrentUser().saveEventually();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String inName) {
		name = inName;
	}
	
	public String getFacebookId() {
		return facebookId;
	}
	
	public void setFacebookId(String inFacebookId) {
		facebookId = inFacebookId;
	}
	
	public boolean getIsNew() {
		return getCurrentUser().isNew();
	}
	
	public void setUserAttributesInParse(String inName, String inFacebookId) {
		getCurrentUser().put("name", name);
		getCurrentUser().put("facebookId", inFacebookId);
		getCurrentUser().saveEventually();
	}
	
    public Chat getCurrentChat() {
        return currentChat;
    }
    
    public void setCurrentChat(Chat inCurrentChat) {
        currentChat = inCurrentChat;
    }
    
	
	
	// Chat APIs
	public ParseRelation<Chat> getChatsRelation() {
		ParseRelation<Chat> relation = getCurrentUser().getRelation("chats");
		return relation;
	}
	
	public void fetchChatsFromParse(final AsyncHelper ah) {
		ParseQuery<Chat> query = getChatsRelation().getQuery();
		
		// Show chats expiring sooner on top. Deleted chats will automatically be at the bottom
		query.addAscendingOrder("createdAt");
		query.findInBackground(new FindCallback<Chat>() {

			@Override
			public void done(List<Chat> chatResults, ParseException e) {
				if (e == null) {
					chats = chatResults;
				} else {
					chats = null;
				}
				// Inform the caller that the operation was completed, so they can query the results back
				ah.chatsFetched();
			}
		});
	}
	
	public void addChat(Chat chat) {
		getChatsRelation().add(chat);
	}

	public List<Chat> getChats() {
		return chats;
	}
	
	public void sendMessage(Chat chat) {
		getChatsRelation().add(chat);
	}

	
	@Override
	public void userAttributesFetched(String inName, String inFacebookId) {
		setName(inName);
		setFacebookId(inFacebookId);
		setUserAttributesInParse(name, facebookId);
	}
	

	@Override
	public void chatsFetched() { }

	@Override
	public void chattersFetched() { }

	@Override
	public void messagesFetched() { }

}
