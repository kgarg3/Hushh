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

import com.hush.HushApp;
import com.hush.R;
import com.hush.models.Message;

public class MessageAdapter extends ArrayAdapter<Message> {
	
	Context activityContext;
	
	// View lookup cache
	private static class ViewHolder {
		TextView content;
	}
	
	public MessageAdapter(Context context, ArrayList<Message> messages) {
		super(context, 0, messages);
		activityContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder; // view lookup cache stored in tag
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.message_item, null);
			viewHolder.content = (TextView) convertView.findViewById(R.id.tvContent);
			// viewHolder.createdAt = (TextView) convertView.findViewById(R.id.tvExpirationTime);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		Message message = getItem(position);
		
		viewHolder.content.setText(message.getContent());

        final LayerDrawable bubble = (LayerDrawable) activityContext.getResources().getDrawable(R.drawable.message_bubble);
		GradientDrawable outerRect = (GradientDrawable) bubble.findDrawableByLayerId(R.id.outerRectangle);
		
		if (message.getChatterFacebookId().equals(HushApp.getCurrentUser().getFacebookId())) {
			outerRect.setColor(Color.parseColor(getContext().getString(R.color.light_purple)));
		} else {
			outerRect.setColor(Color.parseColor(getContext().getString(R.color.purple)));
		}

		return convertView;
	}
}
