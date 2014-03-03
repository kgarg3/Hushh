package com.hush.activities;

import java.util.Collection;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.AppEventsLogger;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.hush.HushApp;
import com.hush.R;

public class InviteFriendsActivity extends Activity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.invite_friends, menu);
		return true;
	}
	
	// menu actions
	public void onViewChatClick(MenuItem mi) {
		Intent i = new Intent(InviteFriendsActivity.this, ChatWindowActivity.class);
		startActivity(i);
	}

    private static final int PICK_FRIENDS_ACTIVITY = 1;
    private UiLifecycleHelper lifecycleHelper;
    boolean pickFriendsWhenSessionOpened;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);

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
    protected void onStart() {
        super.onStart();

        // Update the display every time we are started.
        //displaySelectedFriends();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Call the 'activateApp' method to log an app event for use in analytics and advertising reporting.  Do so in
        // the onResume methods of the primary Activities that an app may be launched into.
        AppEventsLogger.activateApp(this);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_FRIENDS_ACTIVITY:
                //displaySelectedFriends();
                uploadFriendsAsLayerContacts();
                break;
            default:
                Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
                break;
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

    /*
    private void displaySelectedFriends() {
        String results = "";
        HushApp application = (HushApp) getApplication();

        Collection<GraphUser> selection = application.getSelectedUsers();
        if (selection != null && selection.size() > 0) {
            ArrayList<String> names = new ArrayList<String>();
            for (GraphUser user : selection) {
                names.add(user.getName());
            }
            results = TextUtils.join(", ", names);
        } else {
            results = "<No friends selected>";
        }

        resultsTextView.setText(results);
    }
    */

    private void uploadFriendsAsLayerContacts() {
        Collection<GraphUser> selection = HushApp.getSelectedUsers();
        
        if (selection == null || selection.size() == 0) { return; }

        // Add the selected users as user's contacts into Layer 
        for (GraphUser user : selection) {
        	String userName = user.getName();
        	//Contact contact = LayerClient.getContactObject(firstAndLastNames[0], firstAndLastNames[1], user.getId());
        	//layerContacts.add(contact);
        	//LayerClient.addContact(contact);
        }
    }
    
    public void onClickPickFriends(View v) {
        startPickFriendsActivity();
    }
    
    public void startNewGroupChatWithSelectedUsers(View v) {
    	//LayerClient.startNewGroupChatWithSelectedUsers(layerContacts);
    }
    
    public void sendNewMessageOnChat(View v) {
    	//LayerClient.sendNewMessageOnChat();
    }

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
}
