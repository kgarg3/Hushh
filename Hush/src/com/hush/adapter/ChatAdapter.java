package com.hush.adapter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hush.R;
import com.hush.activities.ChatWindowActivity;
import com.hush.models.Chat;
import com.hush.timer.view.PieChartView;

/**
 * 
 * @author gargka
 *
 * Adapter for the chats list view
 */
public class ChatAdapter extends ArrayAdapter<Chat> {

	private static final BigDecimal THOUSAND = new BigDecimal(1000);
	private static final int COUNTDOWN_TICK_INTERVALL = 300;
	public static final int GUI_UPDATE_INTERVALL = COUNTDOWN_TICK_INTERVALL / 4;

	private PieChartView pieChart;

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
				//intent.putExtra(ChatsListActivity.CHAT, chat);
				getContext().startActivity(intent);
			}
		});

		TextView tvChatTopic = (TextView) view.findViewById(R.id.tvChatItemChatTopic);
		tvChatTopic.setText(chat.getTopic());

		//TODO: if chat has notifications and is unread
		//if(chat is unread)
		//make the text bold, else normal 
		//make the view background gray else white


		//set the timer
		pieChart = (PieChartView) view.findViewById(R.id.pieChart);

		//long currentTime = (new Date()).getTime();
		//Date currentDate = new Date(currentTime);
		long expirationTime = chat.getCreatedAt().getTime() + (long)24*60*60*1000;
		//Date expirationDate = new Date(expirationTime);
		long millisInFuture = expirationTime/* - currentTime*/;
		//Date millisInFutureDate = new Date(millisInFuture);
		//this.initialSecondsRoundedUp = BigDecimal.valueOf(millisInFuture).divide(THOUSAND, 0, RoundingMode.UP);
		startRefreshTimer(millisInFuture, chat);

		return view;
	}

	private void startRefreshTimer(long millisInFuture, Chat c) {
		final Chat chat = c;
		new CountDownTimer(millisInFuture, 1000) {
			public void onTick(long millisUntilFinished) {
				long secUntilFinished = millisUntilFinished/1000;
				long previousSecUntilFinished = secUntilFinished - 1;
				BigDecimal previousSecInBD = new BigDecimal(previousSecUntilFinished);
				
				float fraction = getRemainingFractionRoundedUpToFullSeconds(previousSecInBD, millisUntilFinished);
				pieChart.setFraction(fraction);

//						Date fraction = new Date(millisUntilFinished - chat.getCreatedAt().getTime());
//				long minsExpired = (millisUntilFinished - chat.getCreatedAt().getTime())/60000;

				//TODO: we need to convert everything to bigdecimal otherwise the division will default to 0 for very small
				//decimal values. 
			//	BigDecimal msExpiredInBD = new BigDecimal(millisUntilFinished - chat.getCreatedAt().getTime());
			//	BigDecimal totalms = new Big
						//	pieChart.setFraction(minsExpired / 1440);
			}

			public void onFinish() {
				//do cleanup on finish
			}
		}.start();
	}

	/**
	 * Returns the fraction of initial milliseconds (the value used as a
	 * parameter for {@link #startCountdown(long)} divided by remaining
	 * milliseconds (the value returned by {@link #getRemainingMilliseconds()}.
	 * The remaining milliseconds are rounded up to the next full second so that
	 * the value returned by this method only changes approximately once a
	 * second.
	 * 
	 * @return fraction of initial milliseconds divided by remaining
	 *         milliseconds
	 */
	public float getRemainingFractionRoundedUpToFullSeconds(BigDecimal initialSecondsRoundedUp, long remainingMillis) {
		if (initialSecondsRoundedUp != null
				&& BigDecimal.ZERO.compareTo(initialSecondsRoundedUp) < 0) {
			BigDecimal remainingMinsRoundedUp = new BigDecimal(remainingMillis).divide(THOUSAND, 0, RoundingMode.UP);
			BigDecimal fraction = remainingMinsRoundedUp.divide(initialSecondsRoundedUp, 3, RoundingMode.DOWN);
			return fraction.floatValue();
		} else {
			return 0f;
		}
	}


//	public float getRemainingFractionRoundedUpToFullMins(BigDecimal initialSecondsRoundedUp, long remainingMillis) {
//		if (initialSecondsRoundedUp != null
//				&& BigDecimal.ZERO.compareTo(initialSecondsRoundedUp) < 0) {
//			BigDecimal remainingMinsRoundedUp = new BigDecimal(remainingMillis).divide(THOUSAND, 0, RoundingMode.UP);
//			BigDecimal fraction = remainingMinsRoundedUp.divide(initialSecondsRoundedUp, 3, RoundingMode.DOWN);
//			return fraction.floatValue();
//		} else {
//			return 0f;
//		}
//	}
}
