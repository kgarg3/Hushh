package com.hush.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.hush.R;
import com.hush.adapter.MessageAdapter;
import com.hush.models.Chat;
import com.hush.models.Chatter;
import com.hush.models.Message;
import com.hush.utils.AsyncHelper;

public class ChatWindowActivity extends FragmentActivity implements AsyncHelper {
	
	private static final String TAG = ChatWindowActivity.class.getSimpleName();
	
	private int maxMessages = 50;
	
	private ListView lvMessages;
	private MessageAdapter adapterMessages;

	private static Chat chat;
	private static List<Chatter> chatters;
	private static List<Message> messages;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_window);
		
		//chat = HushApp.getCurrentUser().getCurrentChat();
		//chat.fetchChattersFromParse(this);
		//chat.fetchMessagesFromParse(maxMessages, this);
		
		ArrayList<String> messages = new ArrayList<String>();
		messages.add("Message 1");
		messages.add("Message 2");
		messages.add("Message 3");
		messages.add("Message 4 Message 4 Message 4 Message 4 Message 4 Message 4 Message 4 Message 4 Message 4 Message 4 Message 4 Message 4 Message 4 Message 4 Message 4 ");
		messages.add("Message 5");
		messages.add("Message 6");
		messages.add("Message 7");
		messages.add("Message 8");
		messages.add("Message 9");
		messages.add("Message 10");
		messages.add("Message 11");
		messages.add("Message 12");
		messages.add("Message 13");
		messages.add("Message 14");
		messages.add("Message 15");
		messages.add("Message 16");
		messages.add("Message 17");
		messages.add("Message 18");
		
		
        lvMessages = (ListView) findViewById(R.id.lvMessages);
        adapterMessages = new MessageAdapter(this, messages);
        lvMessages.setAdapter(adapterMessages);
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

	@Override
	public void chattersFetched() {
		chatters = chat.getChatters();
	}

	@Override
	public void messagesFetched() {
		messages = chat.getMessages();
	}

	@Override
	public void chatsFetched() { }

	@Override
	public void userAttributesFetched(String inName, String inFacebookId) {	}

}
