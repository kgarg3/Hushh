package com.hush.activities;

import com.hush.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ChatsList extends Activity {

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
