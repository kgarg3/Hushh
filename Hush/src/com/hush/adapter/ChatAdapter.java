package com.hush.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.hush.R;
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
	    
        return view;
	}
}
