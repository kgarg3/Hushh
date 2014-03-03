package com.hush.activities;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.hush.HushApp;
import com.hush.R;
import com.hush.adapter.MessageAdapter;
import com.hush.models.Chat;
import com.hush.models.Chatter;
import com.hush.models.Message;
import com.hush.models.User;
import com.hush.utils.AsyncHelper;
import com.hush.utils.Constants;
import com.hush.utils.HushPushReceiver;
import com.parse.ParseException;

public class ChatWindowActivity extends FragmentActivity implements AsyncHelper {
	
	private int maxMessages = 50;
	
	private TextView tvChatTopic;
	private ListView lvMessages;
	private MessageAdapter adapterMessages;

	private static Chat chat;
	private static List<Chatter> chatters;
	private static ArrayList<String> chatterFacebookIds;

	private BroadcastReceiver pushNotifReceiver;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_window);

		tvChatTopic = (TextView) findViewById(R.id.tvChatTopic);
		lvMessages = (ListView) findViewById(R.id.lvChatWindowMessages);
        adapterMessages = new MessageAdapter(this, new ArrayList<Message>());
        lvMessages.setAdapter(adapterMessages);
        
        
        // Create the broadcast receiver object
        pushNotifReceiver  = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            	// If it is a new chat notif, then add this chat to the user's chat.
            	// That will indicate user has joined the chat
            	
            	
            	updateMessagesAdapterFromDisk();
            }
        };
	}
	
    @Override
    public void onPause() {
        super.onPause();
        
        // Unregister as broadcast receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(pushNotifReceiver);
    }
    
	@Override
	protected void onResume() {
		super.onResume();

		chat = HushApp.getCurrentUser().getCurrentChat();
		chat.fetchChattersFromParse(this);
		chat.fetchMessagesFromParse(maxMessages, this);
		
        tvChatTopic.setText(chat.getTopic());

		// Register as broadcast receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(pushNotifReceiver, new IntentFilter(Constants.pushNotifActionInternal));
        
        updateMessagesAdapterFromDisk();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat_window, menu);
		return true;
	}

	private void updateMessagesAdapterFromDisk() {
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
		
		// Update adapter
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
	
	// menu actions
	public void onInviteFriendsClick(MenuItem mi) {
		Intent i = new Intent(ChatWindowActivity.this, InviteFriendsActivity.class);
		startActivity(i);
	}

	public void onLeaveChatClick(MenuItem mi) {
		
		User user = HushApp.getCurrentUser();
		user.removeChat(chat);
		user.saveToParse();

		Intent i = new Intent(ChatWindowActivity.this, ChatsListActivity.class);
		startActivity(i);
	}

	public void onSendClicked(View v) {
		EditText etChatWindowMessage = ((EditText) findViewById(R.id.etChatWindowMessage));
		String content = etChatWindowMessage.getText().toString();
		
		Message message = new Message(content, HushApp.getCurrentUser().getFacebookId());
    	message.saveToParse();
    	
    	chat.addMessage(message);
        chat.saveToParse();
        
        adapterMessages.add(message);
        
		// Send a push notification
    	chat.saveToParseWithPush(getString(R.string.app_name), HushPushReceiver.pushType.NEW_CHAT, message.getContent(), getChatterFacebookIds());

    	etChatWindowMessage.setText("");
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
