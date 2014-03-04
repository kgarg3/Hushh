package com.hush.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.util.Log;

import com.hush.HushApp;
import com.hush.utils.AsyncHelper;
import com.hush.utils.HushPushNotifSender;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;


@ParseClassName("Chat")
public class Chat extends ParseObject {
	
	private final static String TAG = Chat.class.getSimpleName();
	
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
	
	public void saveToParseWithPush(final String notifTitle, final String pushType, final String pushMessage, final ArrayList<String> fbChattersToNotify) 
	{
		//TODO: Copied over in message, refactor this
		saveEventually(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				if (e != null) {
					Log.d(TAG, e.getMessage());
					return;
				}
				
				// Fetch Users with same facebook ids as in the list fbChattersToNotify
				// those are the users to notify
				ParseQuery<ParseUser> chatterUserQuery = ParseUser.getQuery();
				chatterUserQuery.whereContainedIn("facebookId", fbChattersToNotify);	

				chatterUserQuery.findInBackground(new FindCallback<ParseUser>() {

					@Override
					public void done(List<ParseUser> parseUsers, ParseException e) {
						if (e != null) {
							Log.d(TAG, e.getMessage());
							return;
						}
						
						String customData = "chatId" + "|" + getObjectId() + "|" + pushMessage;
						HushPushNotifSender.sendPushNotifToUsers(parseUsers, notifTitle, pushMessage, pushType, customData);
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

	public static void addChatToCurrentUserInParse(String chatObjectId) {
		ParseQuery<Chat> query = ParseQuery.getQuery(Chat.class);
		query.getInBackground(chatObjectId, new GetCallback<Chat>() {
			public void done(Chat chat, ParseException e) {
				if (e != null) {
					Log.d(TAG, e.getMessage());
					return;
				}
		    	HushApp.getCurrentUser().addChat(chat);
		    	HushApp.getCurrentUser().saveToParse();
			}
		});
	}
	
	// Chatters APIs
	public ParseRelation<Chatter> getChattersRelation() {
		return getRelation("chatters");
	}
	
	public void fetchChattersFromParse(final AsyncHelper ah) {

		final List<Chatter> chatters = new ArrayList<Chatter>();
		
		getChattersRelation().getQuery().findInBackground(new FindCallback<Chatter>() {

			@Override
			public void done(List<Chatter> chatterResults, ParseException e) {
				if (e != null) {
					Log.d(TAG, e.getMessage());
					return;
				}

				for (Chatter chatter : chatterResults) {
	    			chatter.getString("name");
	    			chatter.getString("facebookId");
	    			chatters.add(chatter);
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
				if (e != null) {
					Log.d(TAG, e.getMessage());
					return;
				}

				for (Message message : messageResults) {
	    			message.getString("content");
	    			messages.add(message);
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

