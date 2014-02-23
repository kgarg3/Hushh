package com.hush.activities;

import java.util.Collection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ToggleButton;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.hush.HushApp;
import com.hush.R;
import com.hush.models.Chat;
import com.hush.models.Chatter;

public class NewChatActivity extends Activity {

	private static final int PICK_FRIENDS_ACTIVITY = 1;
    private UiLifecycleHelper lifecycleHelper;
    boolean pickFriendsWhenSessionOpened;
    
	private EditText etChatTopic; 
	private ToggleButton tbPublicPrivate;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_chat);
		
		// Populate view variables
		etChatTopic = (EditText) findViewById(R.id.etChatTopic);
		tbPublicPrivate = (ToggleButton) findViewById(R.id.tbPublicPrivate);
		
		lifecycleHelper = new UiLifecycleHelper(this, new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                onSessionStateChanged(session, state, exception);
            }
        });
        lifecycleHelper.onCreate(savedInstanceState);

        ensureOpenSession();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_chat_topic, menu);
		return true;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Do nothing
    }
	
	// actions
	public void onInviteFriendsClick(View view) {
		startPickFriendsActivity();
	}
	
	public void onDoneClick(MenuItem mi) {
		
		// Create chat and chatters objects in parse
		String chatType = tbPublicPrivate.isChecked() ? "public" : "private";
		Chat chat = new Chat(etChatTopic.getText().toString(), chatType);
		
		// Upload chatters to parse
    	HushApp application = (HushApp) getApplication();
        Collection<GraphUser> selection = application.getSelectedUsers();
        for (GraphUser user : selection) {
        	Chatter chatter = new Chatter(user.getId(), user.getName());
        	chatter.saveInBackground();
        	chat.addChatter(chatter);
        }
        chat.saveInBackground();

        // Navigate to chat window
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
}