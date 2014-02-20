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
	    Log.d(TAG, "we received only headers, to auto fetch message bodies return true");
	    return true;
	  }

	  @Override
	  public void onBodiesReceived(List<Message> messages) {
	    Log.d(TAG, "bodies received");
	  }

	  @Override
	  public void onStateChanged(Recipient.State state, Recipient recipient, Message message) {
	    Log.d(TAG, "state of the recipient changed [pending|sent|delivered|read]");
	  }

	  @Override
	  public void onMessageTagChanged(List<Tag> tags, Message message) {
	    Log.d(TAG, "user tagged a message on another device");
	  }

	  @Override
	  public void onError(int i, String s) {
	    Log.d(TAG, "oops");
	  }
}
