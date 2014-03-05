package com.hush.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.hush.HushApp;
import com.hush.R;
import com.hush.adapter.ChatAdapter;
import com.hush.models.Chat;
import com.hush.models.Chatter;
import com.hush.models.Message;
import com.hush.models.User;
import com.hush.utils.AsyncHelper;
import com.hush.utils.Constants;
import com.hush.utils.HushPushNotifReceiver;
import com.hush.utils.HushUtils;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;


/**
 * 
 * @author gargka
 *
 * Fragment to hold the list of chats
 */
public abstract class ChatListFragment extends Fragment implements AsyncHelper { 

	protected PullToRefreshListView lvChats;
	protected ProgressBar progressBarLoadingTweets;
	protected ChatAdapter adapter;
	
	protected String previousChatType;
	
	protected User user = HushApp.getCurrentUser();
	protected List<Chat> chats;

	private BroadcastReceiver pushNotifReceiver;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Create the broadcast receiver object
        pushNotifReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            	updateChatsAdapterFromDisk();
            }
        };
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Defines the xml file for the fragment
		View view = inflater.inflate(R.layout.fragment_chat_list, container, false);

		// Setup handles to view objects here
		progressBarLoadingTweets = (ProgressBar) view.findViewById(R.id.pgbarChatListFragment);
		lvChats = (PullToRefreshListView) view.findViewById(R.id.lvChatListFragmentChatsList);	
		adapter = new ChatAdapter(getActivity(), new ArrayList<Chat>());
		lvChats.setAdapter(adapter);

		setupListeners();

		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		HushApp.getCurrentUser().fetchChatsFromParse(this);
	}

	/**
	 * Add a scroll and pull to refresh listener to the chats list view
	 */
	private void setupListeners() {
		// Set a listener to be invoked when the list should be refreshed.
		lvChats.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Your code to refresh the list contents
				// Make sure you call listView.onRefreshComplete()
				// once the loading is done. This can be done from here or any
				// place such as when the network request has completed successfully.
				HushApp.getCurrentUser().fetchChatsFromParse(ChatListFragment.this);
				
				// Now we call onRefreshComplete to signify refresh has finished
				lvChats.onRefreshComplete();
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		
		// Register as broadcast receiver
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(pushNotifReceiver, new IntentFilter(Constants.broadcastLocalMessageAction));
        
        updateChatsAdapterFromDisk();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		 // Unregister as broadcast receiver
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(pushNotifReceiver);
	}
	
	public ChatAdapter getAdapter() {
		return adapter;
	}
	
	protected abstract String getChatListType();
	
	protected void loadChatsIntoAdapter() {
		String chatType = getChatListType();
		//if we have switched from public to private or vice versa, clear out the adapter before adding chats to it
		if(!chatType.equals(previousChatType)) {
			adapter.clear();
		}
		
		//make a call to get the current user's chats based on the type
		if(chats != null && chats.size() > 0) {
			//make the progress bar visible
			progressBarLoadingTweets.setVisibility(ProgressBar.VISIBLE);
			
			ArrayList<Chat> chatsToShow = new ArrayList<Chat>();
			for(Chat c : chats) {
				if(c.getType().equals(chatType))
					chatsToShow.add(c);
			}
			
			adapter.addAll(chatsToShow);
			
			//set the progress bar to invisible
			progressBarLoadingTweets.setVisibility(ProgressBar.INVISIBLE);
		}
		/*
		else if(exception != null) {
			//we have a parse exception, alert the user
			Toast.makeText(getActivity(), getString(R.string.chat_list_parse_exception), Toast.LENGTH_SHORT).show();
		}
		*/
	}
	
	private void updateChatsAdapterFromDisk() {
		// Read the unread items from disk
		ArrayList<String> notifs = HushUtils.readFromFile(getActivity());
		
		// There is no file to process, everything has been processed already
		if (notifs.size() == 0) {
			return;
		}
		
		HushUtils.deleteFile(getActivity());
		
		for(final String notifMsg : notifs) {
			
			//  "chatId" + "|" + getObjectId() + "|" + pushMessage;

			String[] notifParts = notifMsg.split("\\|");
			
			ParseQuery<Chat> query = ParseQuery.getQuery(Chat.class);
			query.getInBackground(notifParts[2], new GetCallback<Chat>() {
				public void done(Chat chat, ParseException e) {
					if (e != null) {
						Log.d("TAG", e.getMessage());
						return;
					}
					
					if(notifMsg.startsWith(HushPushNotifReceiver.pushType.NEW_CHAT.toString())) {
						chat.setRead(false);
						adapter.add(chat);
						// Mark that chat as unread
					}
					else if(notifMsg.startsWith(HushPushNotifReceiver.pushType.NEW_MESSAGE.toString())) {
						// Mark the chat as unread
						chat.setRead(false);
					} else {
						// This should not happen
						Log.d("TAG", "Found notifLine: " + notifMsg );
					}
				}
			});
		}
	}
	
	@Override
	public void chatsFetched(List<Chat> inChats) {
		chats = inChats;
		loadChatsIntoAdapter();
	}
	
	@Override
	public void chattersFetched(List<Chatter> chatters) { }

	@Override
	public void messagesFetched(List<Message> messages) { }
	
	@Override
	public void userAttributesFetched(String inName, String inFacebookId) {}
	
}
