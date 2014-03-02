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
		LayerDrawable bubble_wrapper;
		GradientDrawable outerRect;
	}
	
	public MessageAdapter(Context context, ArrayList<Message> messages) {
		super(context, 0, messages);
		activityContext = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Message message = getItem(position);

		ViewHolder viewHolder; // view lookup cache stored in tag
		if (convertView == null) {
			viewHolder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.message_item, null);
			viewHolder.content = (TextView) convertView.findViewById(R.id.tvContent);
			// viewHolder.createdAt = (TextView) convertView.findViewById(R.id.tvExpirationTime);
			
			viewHolder.bubble_wrapper = (LayerDrawable) activityContext.getResources().getDrawable(R.drawable.message_bubble);
			viewHolder.outerRect = (GradientDrawable) viewHolder.bubble_wrapper.findDrawableByLayerId(R.id.outerRectangle);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		viewHolder.content.setText(message.getContent());
		if (message.getChatterFacebookId().equals(HushApp.getCurrentUser().getFacebookId())) {
			viewHolder.outerRect.setColor(Color.parseColor(getContext().getString(R.color.purple)));
		} else {
			viewHolder.outerRect.setColor(Color.parseColor(getContext().getString(R.color.light_purple)));
		}
		
		return convertView;
	}
}
