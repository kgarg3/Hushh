package com.hush.activities;

import java.util.ArrayList;
import java.util.Collection;
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

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
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
	
	private static final int maxMessages = 50;
	private static final int PICK_FRIENDS_ACTIVITY = 1;
	
	private boolean pickFriendsWhenSessionOpened;
	private int numFriendsSelected;
	private boolean enableSend = false;
	
	private TextView tvChatTopic;
	private ListView lvMessages;
	private EditText etChatWindowMessage;
	private MessageAdapter adapterMessages;
	private Button btnChatWindowSend;
	private Menu menu;

	private Chat chat;
	private List<Chatter> chatters;
	private ArrayList<String> chatterFacebookIds;
	
	private BroadcastReceiver pushNotifReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_window);

		tvChatTopic = (TextView) findViewById(R.id.tvChatTopic);
		etChatWindowMessage = ((EditText) findViewById(R.id.etChatWindowMessage));
		lvMessages = (ListView) findViewById(R.id.lvChatWindowMessages);
		btnChatWindowSend = (Button) findViewById(R.id.btnChatWindowSend);
        adapterMessages = new MessageAdapter(this, new ArrayList<Message>());
        lvMessages.setAdapter(adapterMessages);
        
        numFriendsSelected = 0;
        
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
	
	@Override
    public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.right_out, R.anim.left_in);
    }

	 @Override
     public boolean onOptionsItemSelected(MenuItem item) {
             switch (item.getItemId()) {
             case android.R.id.home:
                    onBackPressed(); 
             }
             return true;
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
		
		this.menu = menu;
		
        if(chat.getType().equals("public")) {
        	MenuItem inviteFriendsItem = menu.getItem(1);
        	inviteFriendsItem.setVisible(true);
        }
		
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
		startPickFriendsActivity();
	}

	public void onLeaveChatClick(MenuItem mi) {
		showDialog();
	}

	public void onSendClicked(View v) {
		String content = etChatWindowMessage.getText().toString();
		
		Message message = new Message(content, HushApp.getCurrentUser().getFacebookId());
    	message.saveToParse();
    	
    	chat.addMessage(message);
    	chat.saveToParse();
    	
    	// Save to parse and send a push notification
        chat.saveToParseWithPush(HushPushNotifReceiver.pushType.NEW_MESSAGE.toString(), message.getContent(), getChatterFacebookIds());
        
        adapterMessages.add(message);
        
    	etChatWindowMessage.setText("");
	}
	
	@Override
	public void chattersFetched(List<Chatter> inChatters) {
		chatters = inChatters;
		
		int numParticipants = chatters.size();
		configureNumParticipantsMenuItem(numParticipants + numFriendsSelected);
	    
		setChatterFacebookIds();
		
		enableSend = true;
		if(etChatWindowMessage.getText().toString() != null &&
				etChatWindowMessage.getText().toString().length() > 0)
		{
			btnChatWindowSend.setEnabled(enableSend);
		}
	}

	@Override
	public void messagesFetched(List<Message> inMessages) {
		adapterMessages.addAll(inMessages);
	}

	@Override
	public void chatsFetched(List<Chat> chats) { }

	@Override
	public void userAttributesFetched(String inName, String inFacebookId) {	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PICK_FRIENDS_ACTIVITY:
			displayFriendCount();       
			break;
		default:
			Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
			break;
		}
	}
	
	private void displayFriendCount() {
		Collection<GraphUser> selectedFriends = HushApp.getSelectedUsers();
		
		if (selectedFriends == null || selectedFriends.size() == 0) { return; }
		numFriendsSelected = selectedFriends.size();
		
		Chatter chatter;
		final ArrayList<String> fbChatterIds = new ArrayList<String>();
		Collection<GraphUser> selection = HushApp.getSelectedUsers();
		for (GraphUser user : selection) {
			chatter = new Chatter(user.getId(), user.getName());
			fbChatterIds.add(user.getId());
			chatter.saveToParse();
			chat.addChatter(chatter);
		}
		
		// Send a push notification
    	//chat.saveToParseWithPush(HushPushNotifReceiver.pushType.NEW_CHAT.toString(), getString(R.string.new_chat_push_notif_message), fbChatterIds);
	}
	
	// private methods
	private void startPickFriendsActivity() {
		if (ensureOpenSession()) {
			Intent intent = new Intent(this, PickFriendsActivity.class);

				// Note: The following line is optional, as multi-select behavior is the default for
				// FriendPickerFragment. It is here to demonstrate how parameters could be passed to the
				// friend picker if single-select functionality was desired, or if a different user ID was
				// desired (for instance, to see friends of a friend).
			PickFriendsActivity.populateParameters(intent, null, true, true);
			startActivityForResult(intent, PICK_FRIENDS_ACTIVITY);
			overridePendingTransition(R.anim.slide_in_bottom, android.R.anim.fade_out);
		} else {
			pickFriendsWhenSessionOpened = true;
		}
	}
	
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
            		overridePendingTransition(R.anim.right_out, R.anim.left_in);
                }

                @Override
                public void onNegative() {
                    // handle cancel
                }
        }).show();
	}
	
	private void configureChatWindowMessage() {
        etChatWindowMessage.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(etChatWindowMessage.length() > 0 && enableSend) {
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
	
	private void configureNumParticipantsMenuItem(int chattersCount) {
		// # of participants
		MenuItem numParticipantsItem = this.menu.getItem(0);
		String numParticipants = "(" + chattersCount + ")";				
		numParticipantsItem.setActionView(R.layout.num_participants);
		View numParticipantsActionView = numParticipantsItem.getActionView();
		TextView tvNumParticipants = (TextView) numParticipantsActionView.findViewById(R.id.tvNumParticipants);
		tvNumParticipants.setText(numParticipants);
		numParticipantsItem.setVisible(true);
	}
	
	private boolean ensureOpenSession() {
		if (Session.getActiveSession() == null || !Session.getActiveSession().isOpened()) {
			Session.openActiveSession(this, true, new Session.StatusCallback() {
				@Override
				public void call(Session session, SessionState state, Exception exception) {
					onSessionStateChanged(session, state, exception);
				}
			});
			return false;
		}
		return true;
	}

	private void onSessionStateChanged(Session session, SessionState state, Exception exception) {
		if (pickFriendsWhenSessionOpened && state.isOpened()) {
			pickFriendsWhenSessionOpened = false;

			startPickFriendsActivity();
		}
	}

}
