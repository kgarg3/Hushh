package com.hush.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hush.R;
import com.hush.models.Message;

public class MessageAdapter extends ArrayAdapter<Message> {
	
	Context activityContext;
	private int chatterIndex = 0;
	private String[] colorCodes;
	
	public MessageAdapter(Context context, ArrayList<Message> messages) {
		super(context, 0, messages);
		activityContext = context;
		
		colorCodes = activityContext.getResources().getStringArray(R.array.message_bubble_colors);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.message_item, null);
		}

		//Set up views
		((TextView) view.findViewById(R.id.message_text)).setText(String.valueOf(getItem(position).getContent()));
		
        final LayerDrawable bubble = (LayerDrawable) activityContext.getResources().getDrawable(R.drawable.message_bubble);
		GradientDrawable outerRect = (GradientDrawable) bubble.findDrawableByLayerId(R.id.outerRectangle);
		outerRect.setColor(Color.parseColor(colorCodes[chatterIndex++ % colorCodes.length]));

		return view;
	}
}
