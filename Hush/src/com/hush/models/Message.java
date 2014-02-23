package com.hush.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Message")
public class Message extends ParseObject {

	private String id;
	private String content;
	private boolean isMine;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isMine() {
		return isMine;
	}

	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}

	// Decodes business json into business model object
	public static Message fromJson(JSONObject jsonObject) {
		Message message = new Message();
		// Deserialize json into object fields
		try {
			message.id = jsonObject.getString("id");
			message.content = jsonObject.getString("content");
			message.isMine = Boolean.valueOf(jsonObject.getString("isMine"));	
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

