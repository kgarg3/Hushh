package com.hush.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.hush.R;
import com.hush.clients.FacebookClient;
import com.hush.clients.LayerClient;
import com.hush.models.HushUser;

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

	
	public void createOrLoginUserIntoHush(View v) {
        FacebookClient.setUserPropertiesFromFacebookAndLoginIntoLayer();
	}
}
