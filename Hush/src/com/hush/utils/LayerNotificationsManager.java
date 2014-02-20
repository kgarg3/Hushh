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
		Log.d(TAG, "onHeadersReceived, messages= " + messages);
		return true;
	}

	@Override
	public void onStateChanged(Context context, Recipient.State recipientState,
			Recipient recipient, Message message) {
		Log.d(TAG, "onStateChanged, recipientState= " + recipientState
				+ "; recipient= " + recipient + "; message= " + message);
	}

	@Override
	public void onBodiesReceived(Context context, List<Message> messages) {
		Log.d(TAG, "onBodiesReceived, messages= " + messages);
	}
}
