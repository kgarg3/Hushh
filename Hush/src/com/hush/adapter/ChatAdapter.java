package com.hush.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hush.R;
import com.hush.activities.ChatWindowActivity;
import com.hush.activities.ChatsListActivity;
import com.hush.models.Chat;

/**
 * 
 * @author gargka
 *
 * Adapter for the chats list view
 */
public class ChatAdapter extends ArrayAdapter<Chat> {

	public ChatAdapter(Context context, ArrayList<Chat> chats) {
		super(context, 0, chats);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.chat_item, null);
		}

		final Chat chat = getItem(position);

		view.setOnClickListener( new OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getContext(), ChatWindowActivity.class);
				intent.putExtra(ChatsListActivity.CHAT, chat);
				getContext().startActivity(intent);
			}
		});

		//set up views here
		TextView tvNotifications = (TextView) view.findViewById(R.id.tvChatItemNotificationCount);
		tvNotifications.setText(String.valueOf(chat.getNotification()));

		TextView tvChatTopic = (TextView) view.findViewById(R.id.tvChatItemChatTopic);
		tvChatTopic.setText(chat.getChatTopic());

		return view;
	}
}
