package com.hush.models;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

@ParseClassName("Chat")
public class Chat extends ParseObject {
	
	private String topic;
	private String type;
	private boolean isDeleted;
	private Date createdAt;
	private User creator;
	private ParseRelation<Chatter> chatters;
	private ParseRelation<Message> messages;
	
	// Default public constructor, needed by Parse
	public Chat() {
		super();
	}
	
	public Chat(String topic, String type) {
		setTopic(topic);
		setType(type);
		setCreator();
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
		createdAt = getCreatedAt();
		return createdAt;
	}

	public ParseRelation<Chatter> getChatters() {
		chatters = getRelation("chatters");
		return chatters;
	}

	public void addChatter(Chatter chatter) {
		getChatters().add(chatter);
	}

	public void removeChatter(Chatter chatter) {
		getChatters().remove(chatter);
	}
	
	public ParseRelation<Message> getMessages() {
		messages = getRelation("messages");
		return messages;
	}

	public void addMessage(Message message) {
		getMessages().add(message);
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

