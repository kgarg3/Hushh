package com.hush.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hush.HushApp;
import com.hush.R;
import com.hush.activities.ChatWindowActivity;
import com.hush.models.Chat;

/**
 * 
 * @author gargka
 * 
 *         Adapter for the chats list view
 */
public class ChatAdapter extends ArrayAdapter<Chat> {

	// View lookup cache
	private static class ViewHolder {
		TextView topic;
		// TextView createdAt;
	}

	public ChatAdapter(Context context, ArrayList<Chat> chats) {
		super(context, 0, chats);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder; // view lookup cache stored in tag
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.chat_item, null);
			viewHolder.topic = (TextView) convertView.findViewById(R.id.tvChatItemChatTopic);
			// viewHolder.createdAt = (TextView) convertView.findViewById(R.id.tvExpirationTime);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final Chat chat = getItem(position);

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				HushApp.getCurrentUser().setCurrentChat(chat);
				Intent intent = new Intent(getContext(), ChatWindowActivity.class);
				getContext().startActivity(intent);
			}
		});

		viewHolder.topic.setText(chat.getTopic());

		// TODO: if chat has notifications and is unread
		// if(chat is unread)
		// make the text bold, else normal
		// make the view background gray else white

		// TextView tvExpirationTime = (TextView) view.findViewById(R.id.tvExpirationTime);
		// tvExpirationTime.setText(getExpirationTime(chat.getCreatedAt()));

		return convertView;
	}

	/**
	 * Returns the expiration time in sec if #min is less than 1 else the
	 * minutes if the #hours is less than 1 else the hours
	 * 
	 * @param createdAt
	 * @return
	 */
	private String getExpirationTime(Date createdAt) {
		Date expirationTime = new Date(createdAt.getTime() + (long) 24 * 60
				* 60 * 1000 - (new Date()).getTime());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(expirationTime);
		int hours = calendar.get(Calendar.HOUR_OF_DAY);

		if (hours < 1) {
			int min = calendar.get(Calendar.MINUTE);
			if (min < 1) {
				int sec = calendar.get(Calendar.SECOND);
				return String.valueOf(sec) + "s";
			} else
				return String.valueOf(min) + "m";
		}
		return String.valueOf(hours) + "h";
	}

}
