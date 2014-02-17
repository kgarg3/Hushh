package com.hush.activities;

import com.hush.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

/**
 * 
 * @author 
 *
 * Activity that shows all the chats of the logged in user and his friends. 
 */
public class ChatsListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_all_chats);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.all_chats, menu);
		return true;
	}

}
