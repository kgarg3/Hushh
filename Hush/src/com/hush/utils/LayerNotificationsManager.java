package com.hush.utils;

import java.util.List;

import layer.sdk.NotificationManager;
import layer.sdk.messages.Message;
import layer.sdk.messages.Recipient;
import android.content.Context;
import android.util.Log;

public class LayerNotificationsManager implements NotificationManager {
	  private static final String TAG = LayerNotificationsManager.class.getSimpleName();

	@Override
	public boolean onHeadersReceived(Context context, List<Message> messages) {
		String text = "onHeadersReceived, messages= " + messages;
		Log.d(TAG, text);
		//Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
		return true;
	}

	@Override
	public void onStateChanged(Context context, Recipient.State recipientState, Recipient recipient, Message message) {
		String text = "onStateChanged, recipientState= " + recipientState + "; recipient= " + recipient + "; message= " + message;
		Log.d(TAG, text);
		//Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onBodiesReceived(Context context, List<Message> messages) {
		String text = "onBodiesReceived, messages= " + messages;
		Log.d(TAG, text);
		//Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}
}
