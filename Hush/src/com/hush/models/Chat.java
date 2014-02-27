package com.hush.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
	
	private String topic;
	private String type;
	private boolean isDeleted;
	private Date createdAt;
	private User creator;
	private List<Chatter> chatters;
	private List<Message> messages;
	
	// Default public constructor, needed by Parse
	public Chat() {
		super();
	}
	
	public Chat(String topic, String type) {
		setTopic(topic);
		setType(type);
		setCreator();
	}
	
	public void saveToParse() {
		saveInBackground();
	}
	
	public String getTopic() {
		topic = getString("topic");
		return topic;
	}

	public void setTopic(String chatName) {
		put("topic", chatName);
	}

	public String getType() {
		type = getString("type");
		return type;
	}

	public void setType(String type) {
		put("type", type);
	}
	
	public boolean getIsDeleted() {
		isDeleted = getBoolean("isDeleted");
		return isDeleted;
	}

	public void setIsDeleted() {
		put("isDeleted", true);
	}
	
	public User getCreator() {
		creator = (User) getParseObject("owner");
		return creator;
	}

	public void setCreator() {
		put("creator", ParseUser.getCurrentUser());
	}
	
	public Date getCreatedAt() {
		//createdAt = super.getCreatedAt(); 
		return createdAt;
	}
	
	public void setCreatedAt(Date time) {
		createdAt = time;
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
	
	
	
	
	// Decodes business json into business model object
	public static Chat fromJson(JSONObject jsonObject) {
		Chat chat = new Chat();
		// Deserialize json into object fields
		try {
			chat.topic = jsonObject.getString("topic");
			//chat.createdAt = getCreatedAt(); 	
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return chat;
	}

	public static ArrayList<Chat> fromJson(JSONArray jsonArray) {
		ArrayList<Chat> chats = new ArrayList<Chat>(jsonArray.length());

		for (int i=0; i < jsonArray.length(); i++) {
			JSONObject chatJson = null;
			try {
				chatJson = jsonArray.getJSONObject(i);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

			Chat chat = Chat.fromJson(chatJson);
			if (chat != null) {
				chats.add(chat);
			}
		}

		return chats;
	}
}

