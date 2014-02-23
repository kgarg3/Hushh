package com.hush;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class ParseNotificationsHandlerActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_parse_notifications_handler);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.parse_notifications_handler, menu);
		return true;
	}

}
