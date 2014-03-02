package com.hush.activities;


import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.Session;
import com.hush.R;
import com.hush.fragments.PrivateChatsFragment;
import com.hush.fragments.PublicChatsFragment;
import com.hush.listeners.FragmentTabListener;
import com.parse.ParseUser;

public class ChatsListActivity extends FragmentActivity {
	public static final String CHAT = "chat";
	
	private static final String TAB_PRIVATE_CHATS_TAG = "PrivateChatsFragment";
	private static final String TAB_PUBLIC_CHATS_TAG = "PublicChatsFragment";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chats_list);

		setupNavgationTabs();
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

	private void setupNavgationTabs() {
	 	ActionBar actionBar = getActionBar();
	 	//setup the navigation tabs
	 	actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	 	
	 	Tab tabMyChats = actionBar.newTab().setText(R.string.tab_private_chats)
 			.setTag(TAB_PRIVATE_CHATS_TAG).setTabListener(
				new FragmentTabListener<PrivateChatsFragment>(R.id.flChatsActivityChatLists, this, 
					TAB_PRIVATE_CHATS_TAG, PrivateChatsFragment.class));

	 	Tab tabFrdsChats = actionBar.newTab().setText(R.string.tab_public_chats)
 			.setTag(TAB_PUBLIC_CHATS_TAG).setTabListener(
				new FragmentTabListener<PublicChatsFragment>(R.id.flChatsActivityChatLists, this, 
					TAB_PUBLIC_CHATS_TAG, PublicChatsFragment.class));
	 	
	 	actionBar.addTab(tabMyChats);
	 	actionBar.addTab(tabFrdsChats);
	 	actionBar.selectTab(tabMyChats);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chats_list, menu);
		return true;
	}

	// menu actions
	public void onNewChatClick(MenuItem mi) {
		Intent i = new Intent(ChatsListActivity.this, NewChatActivity.class);
		startActivity(i);
	}

	public void onLogoutClick(MenuItem mi) {
		// Log the user out
		ParseUser.logOut();
		
		// Also close the session and clear theauth token
		Session facebookSession = Session.getActiveSession();
		if (facebookSession == null) {
			facebookSession = new com.facebook.Session(this);
			com.facebook.Session.setActiveSession(facebookSession);
		}
		facebookSession.closeAndClearTokenInformation();
		
		Toast.makeText(this, "Logged out!", Toast.LENGTH_SHORT).show();
		Intent i = new Intent(ChatsListActivity.this, HushLoginActivity.class);
		startActivity(i);
	}
}
