package com.hush.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;

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
	
	public String getFormattedTime(Context context) {
		long createdAtDate;
		if(getCreatedAt() == null) {
			Calendar c = Calendar.getInstance(); 
			createdAtDate = c.getTime().getTime();
		} else {
			createdAtDate = getCreatedAt().getTime();
		}
		
		String formattedTime = new SimpleDateFormat("h:mm a").format(createdAtDate);
		return formattedTime;
	}
}

