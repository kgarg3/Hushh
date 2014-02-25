package com.hush.models;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Message")
public class Message extends ParseObject {

	private String content;
	private Date createdAt;
	private Chatter chatter;

	public Message() {
		super();
	}
	
	public String getContent() {
		content = getString("content");
		return content;
	}

	public void setContent(String content) {
		put("content", content);
	}

	public Date getCreatedAt() {
		createdAt = super.getCreatedAt();
		return createdAt;
	}

	public Chatter getChatter() {
		chatter = (Chatter) getParseObject("chatter");
		return chatter;
	}
	

	// Decodes business json into business model object
	public static Message fromJson(JSONObject jsonObject) {
		Message message = new Message();
		// Deserialize json into object fields
		try {
			message.content = jsonObject.getString("content");
			//message.chatter = Boolean.valueOf(jsonObject.getString("isMine"));	
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

		return message;
	}

	public static ArrayList<Message> fromJson(JSONArray jsonArray) {
		ArrayList<Message> messages = new ArrayList<Message>(jsonArray.length());

		for (int i=0; i < jsonArray.length(); i++) {
			JSONObject chatJson = null;
			try {
				chatJson = jsonArray.getJSONObject(i);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

			Message message = Message.fromJson(chatJson);
			if (message != null) {
				messages.add(message);
			}
		}

		return messages;
	}
}

