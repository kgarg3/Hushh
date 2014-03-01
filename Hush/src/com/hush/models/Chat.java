package com.hush.models;

import java.util.Date;
import java.util.List;

import com.hush.utils.AsyncHelper;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

@ParseClassName("Chat")
public class Chat extends ParseObject {
	
	private static List<Chat> chats;

	private List<Chatter> chatters;
	private List<Message> messages;
	
	// Default public constructor, needed by Parse
	public Chat() {
		super();
	}
	
	public Chat(String topic, String type) {
		putTopic(topic);
		putType(type);
		putCreator();
	}
	
	public void saveToParse() {
		saveEventually();
	}
	
	public String getTopic() {
		return getString("topic");
	}

	public void putTopic(String chatName) {
		put("topic", chatName);
	}

	public String getType() {
		return getString("type");
	}

	public void putType(String type) {
		put("type", type);
	}
	
	public boolean getIsDeleted() {
		return getBoolean("isDeleted");
	}

	public void putIsDeleted() {
		put("isDeleted", true);
	}
	
	public User getCreator() {
		return (User) getParseObject("owner");
	}

	public void putCreator() {
		put("creator", ParseUser.getCurrentUser());
	}
	
	public Date getCreatedAt() {
		return super.getCreatedAt(); 
	}

	// Chatters APIs
	public ParseRelation<Chatter> getChattersRelation() {
		ParseRelation<Chatter> relation = getRelation("chatters");
		return relation;
	}
	
	public void fetchChattersFromParse(final AsyncHelper ah) {

		getChattersRelation().getQuery().findInBackground(new FindCallback<Chatter>() {

			@Override
			public void done(List<Chatter> chatterResults, ParseException e) {
				if (e == null) {
					chatters = chatterResults;
				} else {
					chatters = null;
				}
				// Inform the caller that the operation was completed, so they can query the results back
				ah.chattersFetched();
			}
		});
	}

	public List<Chatter> getChatters() {
		return chatters;
	}
	
	public void addChatter(Chatter chatter) {
		getChattersRelation().add(chatter);
	}

	public void removeChatter(Chatter chatter) {
		getChattersRelation().remove(chatter);
	}
	
	// Messages APIs
	public ParseRelation<Message> getMessagesRelation() {
		ParseRelation<Message> relation = getRelation("messages");
		return relation;
	}
	
	public void fetchMessagesFromParse(int totalMessages, final AsyncHelper ah) {

		ParseQuery<Message> query = getMessagesRelation().getQuery();
		
		// Fetch only the last 50 messages with newest at the end
		query.setLimit(totalMessages);
		query.addAscendingOrder("createdAt");
		
		query.findInBackground(new FindCallback<Message>() {
			@Override
			public void done(List<Message> messageResults, ParseException e) {
				if (e == null) {
					messages = messageResults;
				} else {
					messages = null;
				}
				// Inform the caller that the operation was completed, so they can query the results back
				ah.messagesFetched();
			}
		});
	}

	public List<Message> getMessages() {
		return messages;
	}
	
	public void sendMessage(Message message) {
		getMessagesRelation().add(message);
	}

}

