package com.hush.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hush.R;
import com.hush.models.Message;

public class MessageAdapter extends ArrayAdapter<Message> {
	
	public MessageAdapter(Context context, ArrayList<Message> messages) {
		super(context, 0, messages);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.message_item, null);
		}

		final Message message = getItem(position);

		//set up views here
		TextView tvMessage = (TextView) view.findViewById(R.id.tvMessage);
		tvMessage.setText(String.valueOf(message.getContent()));

		return view;
	}
}
