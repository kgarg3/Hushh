package com.hush.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.hush.R;

public class InviteFriendsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invite_friends);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.invite_friends, menu);
		return true;
	}
	
	// menu actions
	public void onViewChatClick(MenuItem mi) {
		Intent i = new Intent(InviteFriendsActivity.this, ChatActivity.class);
		startActivity(i);
	}

}
