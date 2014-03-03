package com.hush.activities;

import java.util.ArrayList;
import java.util.Collection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.hush.HushApp;
import com.hush.R;
import com.hush.models.Chat;
import com.hush.models.Chatter;
import com.hush.utils.HushPushReceiver;

public class NewChatActivity extends Activity {

	private static final int PICK_FRIENDS_ACTIVITY = 1;
	private UiLifecycleHelper lifecycleHelper;
	boolean pickFriendsWhenSessionOpened;

	private EditText etChatTopic;
	private TextView tvFriendCount;
	private Switch swPublicPrivate;
	private MenuItem miDone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_chat);

		// Populate view variables
		etChatTopic = (EditText) findViewById(R.id.etChatTopic);
		tvFriendCount = (TextView) findViewById(R.id.tvFriendCount);
		swPublicPrivate = (Switch) findViewById(R.id.swPublicPrivate);

		lifecycleHelper = new UiLifecycleHelper(this, new Session.StatusCallback() {
			@Override
			public void call(Session session, SessionState state, Exception exception) {
				onSessionStateChanged(session, state, exception);
			}
		});
		lifecycleHelper.onCreate(savedInstanceState);

		ensureOpenSession();
		setupTextViewListener();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_chat_topic, menu);
		miDone = menu.findItem(R.id.action_invite_friends);
		return true;
	}

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

	// actions
	public void onInviteFriendsClick(View view) {
		startPickFriendsActivity();
	}

	public void onDoneClick(MenuItem mi) {

		// Create chat and chatters objects in parse
		String chatType = swPublicPrivate.isChecked() ? "private" : "public";
		Chat chat = new Chat(etChatTopic.getText().toString(), chatType);
		chat.saveToParse();

		Chatter chatter;
		final ArrayList<String> fbChatterIds = new ArrayList<String>();
		Collection<GraphUser> selection = HushApp.getSelectedUsers();
		for (GraphUser user : selection) {
			chatter = new Chatter(user.getId(), user.getName());
			fbChatterIds.add(chatter.getFacebookId());
			chatter.saveToParse();
			chat.addChatter(chatter);
		}

		// Add the original user to the chat
		chatter = new Chatter(HushApp.getCurrentUser().getFacebookId(), HushApp.getCurrentUser().getName());
		chatter.saveToParse();
		chat.addChatter(chatter);

		// Send a push notification
		chat.saveToParseWithPush(getString(R.string.app_name), HushPushReceiver.pushType.NEW_CHAT, getString(R.string.new_chat_push_notif_message), fbChatterIds);

		// Add the chat to user's chats 
		HushApp.getCurrentUser().addChat(chat);
		HushApp.getCurrentUser().saveToParse();


		// Set active chat and navigate to a chat window
		HushApp.getCurrentUser().setCurrentChat(chat);
		Intent i = new Intent(NewChatActivity.this, ChatWindowActivity.class);
		startActivity(i);
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
		} else {
			pickFriendsWhenSessionOpened = true;
		}
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

	private void displayFriendCount() {
		Collection<GraphUser> selectedFriends = HushApp.getSelectedUsers();

		if (selectedFriends == null || selectedFriends.size() == 0) { return; }

		tvFriendCount.setText("(" + selectedFriends.size() + ")");
		tvFriendCount.setTextColor(Color.parseColor("#669900"));
	}

	private void setupTextViewListener() {

		TextWatcher watcher = new TextWatcher() {		
			
			//enable the done menu only when there is a topic AND at least one friend is selected. 
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String topic = etChatTopic.getText().toString();
				String selectedFriendsCount = tvFriendCount.getText().toString();
				if( topic != null && !topic.equals("") && 
						selectedFriendsCount != null && Integer.valueOf(selectedFriendsCount.substring(1, 2)) > 0)				
					miDone.setEnabled(true);
				else
					miDone.setEnabled(false);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

			@Override
			public void afterTextChanged(Editable s) { }
		};
		
		etChatTopic.addTextChangedListener(watcher);
		tvFriendCount.addTextChangedListener(watcher);

	}
}
