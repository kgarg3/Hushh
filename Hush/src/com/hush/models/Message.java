package com.hush.models;

import java.util.Date;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Message")
public class Message extends ParseObject {

	public Message() {
		super();
	}
	
	public Message(String content, String chatterFacebookId) {
		putContent(content);
		putChatterFacebookId(chatterFacebookId);
	}
	
	public void saveToParse() {
		saveEventually();
	}

	public String getContent() {
		return getString("content");
	}

	public void putContent(String content) {
		put("content", content);
	}

	public String getChatterFacebookId() {
		return getString("chatterFacebookId");
	}

	public void putChatterFacebookId(String chatterFacebookId) {
		put("chatterFacebookId", chatterFacebookId);
	}

	public Date getCreatedAt() {
		return super.getCreatedAt();
	}
}

