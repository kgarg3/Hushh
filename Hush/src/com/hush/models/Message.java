package com.hush.models;

import java.util.Date;

import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("Message")
public class Message extends ParseObject {

	public Message() {
		super();
	}
	
	public String getContent() {
		return getString("content");
	}

	public void setContent(String content) {
		put("content", content);
	}

	public Date getCreatedAt() {
		return super.getCreatedAt();
	}

	public Chatter getChatter() {
		return (Chatter) getParseObject("chatter");
	}
}

