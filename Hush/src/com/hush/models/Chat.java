package com.hush.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.hush.utils.AsyncHelper;
import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;


@ParseClassName("Chat")
public class Chat extends ParseObject {
	
	// Default public constructor, needed by Parse
	public Chat() {
		super();
	}
	
	public Chat(String topic, String type) {
		putTopic(topic);
		putType(type);
	}
	
	public void saveToParse() {
		saveEventually();
	}
	
	public void saveToParseWithPush(final String message, final ArrayList<String> fbChattersToNotify) 
	{
		saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException arg0) {
				ParseQuery<ParseUser> chatterUserQuery = ParseUser.getQuery();
				chatterUserQuery.whereContainedIn("facebookId", fbChattersToNotify);	
				
				chatterUserQuery.findInBackground(new FindCallback<ParseUser>() {

					@Override
					public void done(List<ParseUser> arg0, ParseException arg1) {
						ParseQuery<ParseInstallation> userQuery = ParseInstallation.getQuery();
						userQuery.whereContainedIn("user", arg0);
						
						JSONObject data= null;
						try {
							data = new JSONObject("{\"title\" : \"Hush!\"," +
													"\"intent\" : \"ChatWindowActivity\"," +
													"\"action\" : \"com.hush.UPDATE_STATUS\"," +
													"\"chatId\" :" + getObjectId() + "}");
						} catch (JSONException e) {
							e.printStackTrace();
						}
						
						ParsePush push = new ParsePush();
						push.setQuery(userQuery);
						push.setData(data);
						push.setMessage(message);
						push.sendInBackground();
						
					}
				
				});
			}
		});
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
	
	public Date getCreatedAt() {
		return super.getCreatedAt(); 
	}

	// Chatters APIs
	public ParseRelation<Chatter> getChattersRelation() {
		ParseRelation<Chatter> relation = getRelation("chatters");
		return relation;
	}
	
	public void fetchChattersFromParse(final AsyncHelper ah) {

		final List<Chatter> chatters = new ArrayList<Chatter>();
		
		getChattersRelation().getQuery().findInBackground(new FindCallback<Chatter>() {

			@Override
			public void done(List<Chatter> chatterResults, ParseException e) {
				if (e == null) {
					for (Chatter chatter : chatterResults) {
		    			chatter.getString("name");
		    			chatter.getString("facebookId");
		    			chatters.add(chatter);
		    		}
				} else {
					Log.d("HEY", "you're screwed");
				}
				// Inform the caller that the operation was completed, so they can query the results back
				ah.chattersFetched(chatters);
			}
		});
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

		final List<Message> messages = new ArrayList<Message>();
		
		ParseQuery<Message> query = getMessagesRelation().getQuery();
		
		// Show chats expiring sooner on top. Deleted chats will automatically be at the bottom
		query.addAscendingOrder("createdAt");
		
		query.findInBackground(new FindCallback<Message>() {

			@Override
			public void done(List<Message> messageResults, ParseException e) {
				if (e == null) {
					for (Message message : messageResults) {
		    			message.getString("content");
		    			messages.add(message);
		    		}
				} else {
					Log.d("HEY", "you're screwed");
				}
				
				// Inform the caller that the operation was completed, so they can query the results back
				ah.messagesFetched(messages);
			}
		});
	}

	public void addMessage(Message message) {
		getMessagesRelation().add(message);
	}

}

