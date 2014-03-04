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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.hush.HushApp;
import com.hush.R;
import com.hush.adapter.MessageAdapter;
import com.hush.fragments.SimpleAlertDialog;
import com.hush.fragments.SimpleAlertDialog.SimpleAlertListener;
import com.hush.models.Chat;
import com.hush.models.Chatter;
import com.hush.models.Message;
import com.hush.models.User;
import com.hush.utils.AsyncHelper;
import com.hush.utils.Constants;
import com.hush.utils.HushPushNotifReceiver;
import com.hush.utils.HushUtils;

public class ChatWindowActivity extends FragmentActivity implements AsyncHelper {
	
	private int maxMessages = 50;
	
	private TextView tvChatTopic;
	private ListView lvMessages;
	private EditText etChatWindowMessage;
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
        
    	// TODO: Move into fragment
    	pushNotifReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            	
            	updateMessagesAdapterFromDisk();
            }
        };
        
        configureChatWindowMessage();
	}
	
	@Override
	protected void onResume() {
		super.onResume();

		chat = HushApp.getCurrentUser().getCurrentChat();
		chat.fetchChattersFromParse(this);
		chat.fetchMessagesFromParse(maxMessages, this);
		
        tvChatTopic.setText(chat.getTopic());
        
        // Register as broadcast receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(pushNotifReceiver, new IntentFilter(Constants.broadcastLocalMessageAction));

        updateMessagesAdapterFromDisk();
	}
	
	@Override
	public void onPause() {
		super.onPause();

		// Unregister as broadcast receiver
		LocalBroadcastManager.getInstance(this).unregisterReceiver(pushNotifReceiver);
	}

	private void updateMessagesAdapterFromDisk() {
		// Read the unread items from disk
		ArrayList<String> notifs = HushUtils.readFromFile(ChatWindowActivity.this);
		
		// There is no file to process, everything has been processed already
		if (notifs.size() == 0) {
			return;
		}

		HushUtils.deleteFile(ChatWindowActivity.this);
		
		for(String notifMsg : notifs) {
			if(notifMsg.startsWith(HushPushNotifReceiver.pushType.NEW_MESSAGE.toString())) {
				String[] notifParts = notifMsg.split("\\|");
				if(notifParts[2].equals(chat.getObjectId())) {
					adapterMessages.add(new Message(notifParts[3], "-1"));
				} else {
					HushUtils.writeToFile(ChatWindowActivity.this, notifMsg);
				}
			} else {
				HushUtils.writeToFile(ChatWindowActivity.this, notifMsg);
			}
		}
	}
		
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat_window, menu);
		return true;
	}

	private void setChatterFacebookIds() {
        if(chatterFacebookIds == null) {
        	chatterFacebookIds = new ArrayList<String>();
        }
        
        for(Chatter chatter: chatters) {
        	// Send the notif to everyone except yourself
        	if (!chatter.getFacebookId().equals(HushApp.getCurrentUser().getFacebookId())) {
        		chatterFacebookIds.add(chatter.getFacebookId());
        	}
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
		showDialog();
	}

	public void onSendClicked(View v) {
		String content = etChatWindowMessage.getText().toString();
		
		Message message = new Message(content, HushApp.getCurrentUser().getFacebookId());
    	message.saveToParse();
    	
    	chat.addMessage(message);
    	
		// Save to parse and send a push notification
        chat.saveToParseWithPush(HushPushNotifReceiver.pushType.NEW_MESSAGE.toString(), message.getContent(), getChatterFacebookIds());
        
        adapterMessages.add(message);
        
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
	
	private void showDialog() {
        SimpleAlertDialog.build(this, 
            (String) getText(R.string.chat_window_leave_prompt), "YES", "NO", new SimpleAlertListener() {
                @Override
                public void onPositive() {
            		User user = HushApp.getCurrentUser();
            		user.removeChat(chat);
            		user.saveToParse();
            		
            		Intent i = new Intent(ChatWindowActivity.this, ChatsListActivity.class);
            		startActivity(i);
                }

                @Override
                public void onNegative() {
                    // handle cancel
                }
        }).show();
	}
	
	private void configureChatWindowMessage() {
		final Button btnChatWindowSend = (Button) findViewById(R.id.btnChatWindowSend);
        etChatWindowMessage = ((EditText) findViewById(R.id.etChatWindowMessage));
        etChatWindowMessage.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(etChatWindowMessage.length() > 0) {
					btnChatWindowSend.setEnabled(true);
				} else {
					btnChatWindowSend.setEnabled(false);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
			
			@Override
			public void afterTextChanged(Editable s) { }
		});
	}

}
