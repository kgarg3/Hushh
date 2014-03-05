package com.hush.utils;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.hush.R;
import com.hush.activities.HushLoginActivity;
import com.hush.models.Chat;

public class HushPushNotifReceiver extends BroadcastReceiver {
	
	public static enum pushType {NEW_CHAT, NEW_MESSAGE};
	
	private static final String TAG = HushPushNotifReceiver.class.getSimpleName();
	
	@Override
	public void onReceive(final Context context, Intent intent) {

		try {
			String action = intent.getAction();
			Log.d(TAG, "got action " + action);
			if (action.equals(Constants.externalPushNotifAction)) {
				JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));
				
				Iterator<?> itr = json.keys();
				while (itr.hasNext()) {
					String key = (String) itr.next();
					if (key.equals("customData")) {
						
						String customData = json.getString(key);
						
						Log.d(TAG, "Key/value :  " + key + " => " + json.getString(key));
	
						HushUtils.writeToFile(context, customData);
						
						/*
						 * These are 2 types of push messages currently coming in:
								{"customData":"NEW_CHAT|chatId|6ZqteHLXtD|One of your friends wants to chat!","action":"com.hush.HUSH_MESSAGE"}
								{"customData":"NEW_MESSAGE|chatId|xPmosaQOuq|hi!!","action":"com.hush.HUSH_MESSAGE"}
						*/
						String[] customDataParts = customData.split("\\|");
						
						// If the push notif is for a new chat, then add the chat to the user's chats in Parse
						if (customData.startsWith(HushPushNotifReceiver.pushType.NEW_CHAT.toString())) {
							Chat.addChatToCurrentUserInParse(customDataParts[2]);
						}
						
						generateNotification(context, customDataParts[2], customDataParts[3]);
						
						// Send a local broadcast for running activities to load the results
						LocalBroadcastManager.getInstance(context).sendBroadcast(new Intent(Constants.broadcastLocalMessageAction));
					}
				}
			}
		} catch (JSONException e) {
			Log.d(TAG, "JSONException: " + e.getMessage());
		}
	}
	
	private void generateNotification(Context context, String chatId, String pushMessage) {
		Intent intent = new Intent(context, HushLoginActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);

		NotificationManager notifManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

		NotificationCompat.Builder systemTrayNotif = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(context.getString(R.string.app_name))
				.setContentText(pushMessage)
				.setContentIntent(contentIntent);

		// Hide the notification after its selected
		systemTrayNotif.setAutoCancel(true);
		
		// Since chatIds are unique object Ids, their integer hashcodes should not collide
		// So this will create one new notification per chat 
		notifManager.notify(chatId.hashCode(), systemTrayNotif.build());
	}
}
