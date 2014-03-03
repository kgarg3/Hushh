package com.hush.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.hush.HushApp;
import com.hush.HushPushReceiver;
import com.hush.R;
import com.hush.adapter.MessageAdapter;
import com.hush.models.Chat;
import com.hush.models.Chatter;
import com.hush.models.Message;
import com.hush.utils.AsyncHelper;

public class ChatWindowActivity extends FragmentActivity implements AsyncHelper {
	
	private int maxMessages = 50;
	
	private ListView lvMessages;
	private MessageAdapter adapterMessages;

	private static Chat chat;
	private static List<Chatter> chatters;
	private static ArrayList<String> chatterFacebookIds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_window);
		
		chat = HushApp.getCurrentUser().getCurrentChat();
		chat.fetchChattersFromParse(this);
		chat.fetchMessagesFromParse(maxMessages, this);
		
        lvMessages = (ListView) findViewById(R.id.lvChatWindowMessages);
        adapterMessages = new MessageAdapter(this, new ArrayList<Message>());
        lvMessages.setAdapter(adapterMessages);
	}

	private void setChatterFacebookIds() {
        if(chatterFacebookIds == null) {
        	chatterFacebookIds = new ArrayList<String>();
        }
        
        for(Chatter chatter: chatters) {
        	chatterFacebookIds.add(chatter.getFacebookId());
        }
	}

	private ArrayList<String> getChatterFacebookIds() {
        return chatterFacebookIds;
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		// Read the unread items from disk
		/*
			File filesDir = getFilesDir();
			File todoFile = new File(filesDir, "todo.txt");

			try {
				todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
			} catch (IOException e) {
				todoItems = new ArrayList<String>();
			}
		*/
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat_window, menu);
		return true;
	}

	// menu actions
	public void onInviteFriendsClick(MenuItem mi) {
		Intent i = new Intent(ChatWindowActivity.this, InviteFriendsActivity.class);
		startActivity(i);
	}

	public void onLeaveChatClick(MenuItem mi) {
		// TODO: Remove the user from parse
		Intent i = new Intent(ChatWindowActivity.this, ChatsListActivity.class);
		startActivity(i);
	}

	public void onSendClicked(View v) {
		String content = ((EditText) findViewById(R.id.etChatWindowMessage)).getText().toString();
		
		Message message = new Message(content, HushApp.getCurrentUser().getFacebookId());
    	message.saveToParse();
    	
    	chat.addMessage(message);
        chat.saveToParse();
        
        adapterMessages.add(message);
        
		// TODO: Call the push notif function for the function
        
    	chat.saveToParseWithPush(HushPushReceiver.pushType.NEW_CHAT, message.getContent(), getChatterFacebookIds());
	}
	
	@Override
	public void chattersFetched(List<Chatter> inChatters) {
		chatters = inChatters;
		setChatterFacebookIds();
	}

	@Override
	public void messagesFetched(List<Message> inMessages) {
		adapterMessages.addAll(inMessages);
	}

	@Override
	public void chatsFetched(List<Chat> chats) { }

	@Override
	public void userAttributesFetched(String inName, String inFacebookId) {	}

}
