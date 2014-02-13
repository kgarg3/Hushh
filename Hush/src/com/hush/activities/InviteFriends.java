package com.hush.activities;

import com.hush.R;
import com.hush.R.layout;
import com.hush.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class InviteFriends extends Activity {

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

}
