package com.hush.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hush.R;
import com.hush.utils.FacebookClient;

public class NewChatTopicActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_chat_topic);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_chat_topic, menu);
		return true;
	}
	
	// menu actions
	public void onInviteFriendsClick(MenuItem mi) {
		Intent i = new Intent(NewChatTopicActivity.this, InviteFriendsActivity.class);
		startActivity(i);
	}

	
	public void printAllFBFriendsToLogCat(View v) {
        // Get the list of all friends
        FacebookClient.getUserData();
	}
}
