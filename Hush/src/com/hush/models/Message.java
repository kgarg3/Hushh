package com.hush.models;

import java.util.Date;

import android.content.Context;
import android.text.format.DateUtils;

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
		String timeString = DateUtils.getRelativeDateTimeString(context, getCreatedAt().getTime(), 
				DateUtils.SECOND_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0).toString();
		String formattedTime = timeString.split(",")[0];
		return formattedTime;
	}
}

