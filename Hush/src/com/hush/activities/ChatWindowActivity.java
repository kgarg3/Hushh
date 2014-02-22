package com.hush.activities;

import java.util.List;

import layer.sdk.MessageManager;
import layer.sdk.MessageManager.MessageListener;
import layer.sdk.messages.Message;
import layer.sdk.messages.Recipient;
import layer.sdk.messages.Tag;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.hush.R;

public class ChatWindowActivity extends Activity implements MessageListener {
	
	private static final String TAG = ChatWindowActivity.class.getSimpleName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_window);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat, menu);
		return true;
	}

	// menu actions
	public void onInviteFriendsClick(MenuItem mi) {
		Intent i = new Intent(ChatWindowActivity.this, InviteFriendsActivity.class);
		startActivity(i);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MessageManager.addMessageListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MessageManager.removeMessageListener(this);
	}

	@Override
	public boolean onHeadersReceived(List<Message> messages) {
		String text = "we received only headers, to auto fetch message bodies return true";
		Log.d(TAG, text);
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
		return true;
	}

	@Override
	public void onBodiesReceived(List<Message> messages) {
		String text = "bodies received";
		Log.d(TAG, text);
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStateChanged(Recipient.State state, Recipient recipient, Message message) {
		String text = "state of the recipient changed [pending|sent|delivered|read]";
		Log.d(TAG, text);
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onMessageTagChanged(List<Tag> tags, Message message) {
		String text = "user tagged a message on another device";
		Log.d(TAG, text);
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onError(int i, String s) {
		String text = "oops";
		Log.d(TAG, text);
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
}
