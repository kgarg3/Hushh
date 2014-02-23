package com.hush.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Chat implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;
	private int notification;
	private String chatTopic;
	private Long timeRemaining;
	 
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public int getNotification() {
		return notification;
	}

	public void setNotification(int notification) {
		this.notification = notification;
	}

	public String getChatTopic() {
		return chatTopic;
	}

	public void setChatTopic(String chatName) {
		this.chatTopic = chatName;
	}

	public Long getTimeRemaining() {
		return timeRemaining;
	}

	public void setTimeRemaining(Long timeRemaining) {
		this.timeRemaining = timeRemaining;
	}


	// Decodes business json into business model object
	public static Chat fromJson(JSONObject jsonObject) {
		Chat chat = new Chat();
		// Deserialize json into object fields
		try {
			chat.id = jsonObject.getString("id");
			chat.notification = Integer.valueOf(jsonObject.getString("notifications"));
			chat.chatTopic = jsonObject.getString("topic");
			chat.timeRemaining = Long.valueOf(jsonObject.getString("timeRemaining")); 	
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

