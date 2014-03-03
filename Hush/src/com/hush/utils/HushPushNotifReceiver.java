package com.hush.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.hush.models.Chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class HushPushNotifReceiver extends BroadcastReceiver {
	
	public static enum pushType {NEW_CHAT, NEW_MESSAGE};
	
	private static final String TAG = HushPushNotifReceiver.class.getSimpleName();

	@Override
	public void onReceive(final Context context, Intent intent) {

		File hushNotifsFile = new File(context.getFilesDir(), "hush_notifs.txt");

		try {
			String action = intent.getAction();
			Log.d(TAG, "got action " + action);
			if (action.equals(Constants.pushNotifAction)) {
				JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
				
				Iterator<?> itr = json.keys();
				while (itr.hasNext()) {
					String key = (String) itr.next();
					if (key.equals("customData")) {
						
						String customData = json.getString(key);
						
						// Write to file
						ArrayList<String> notifs = new ArrayList<String>();
						notifs.add(customData);
						
						Log.d(TAG, "Key/value :  " + key + " => " + json.getString(key));
						
						try {
							FileUtils.writeLines(hushNotifsFile, notifs);
						} catch (IOException e) {
							e.printStackTrace();
						}
						
						
						// If the push notif is for a new chat, then add the chat to the user's chats in Parse
						if (customData.startsWith(HushPushNotifReceiver.pushType.NEW_CHAT.toString())) {
							String[] chatId = customData.split(":");
							Chat.addChatToCurrentUserInParse(chatId[1]);
						}
						
						// Send a local broadcast for running activities to load the results
						LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Constants.pushNotifActionInternal));
					}
				}
			}
		} catch (JSONException e) {
			Log.d(TAG, "JSONException: " + e.getMessage());
		}
	}
}
