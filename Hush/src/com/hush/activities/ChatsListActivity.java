package com.hush.activities;


import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.hush.R;
import com.hush.fragments.FriendsChatsFragment;
import com.hush.fragments.MyChatsFragment;
import com.hush.listeners.FragmentTabListener;
import com.hush.utils.FriendListRequestListener;

/**
 * 
 * @author 
 *
 * Activity that shows all the chats of the logged in user and his friends. 
 */
public class ChatsListActivity extends FragmentActivity {
	
	private static final String TAB_MY_CHATS_TAG = "UserChatsFragment";
	private static final String TAB_FRIENDS_CHATS_TAG = "FriendsChatsFragment";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_chats);
		
        // Get the list of all friends
        FriendListRequestListener.getUserData();

		setupNavgationTabs();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.all_chats, menu);
		return true;
	}
	
	private void setupNavgationTabs() {
		ActionBar actionBar = getActionBar();
		//setup the navigation tabs
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		Tab tabMyChats = actionBar.newTab().setText(R.string.tab_my_chats)
				.setTag(TAB_MY_CHATS_TAG).setTabListener(
						new FragmentTabListener<MyChatsFragment>(R.id.flChatsActivityChatLists, this, 
								TAB_MY_CHATS_TAG, MyChatsFragment.class));

		Tab tabFrdsChats = actionBar.newTab().setText(R.string.tab_frds_chats)
				.setTag(TAB_FRIENDS_CHATS_TAG).setTabListener(
						new FragmentTabListener<FriendsChatsFragment>(R.id.flChatsActivityChatLists, this, 
								TAB_FRIENDS_CHATS_TAG, FriendsChatsFragment.class));

		actionBar.addTab(tabMyChats);
		actionBar.addTab(tabFrdsChats);
		actionBar.selectTab(tabMyChats);
	}
	
	// menu actions
	public void onNewChatClick(MenuItem mi) {
		Intent i = new Intent(ChatsListActivity.this, NewChatTopicActivity.class);
		startActivity(i);
	}

}
