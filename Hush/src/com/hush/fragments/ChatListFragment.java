package com.hush.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hush.HushApp;
import com.hush.R;
import com.hush.adapter.ChatAdapter;
import com.hush.models.Chat;
import com.hush.models.Chatter;
import com.hush.models.Message;
import com.hush.models.User;
import com.hush.utils.AsyncHelper;
import com.parse.ParseException;

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
	protected ParseException exception;

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
				loadChatsIntoAdapter();
				
				// Now we call onRefreshComplete to signify refresh has finished
				lvChats.onRefreshComplete();
			}
		});
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
		else if(exception != null) {
			//we have a parse exception, alert the user
			Toast.makeText(getActivity(), getString(R.string.chat_list_parse_exception), Toast.LENGTH_SHORT).show();
		}
	}
	
	// TODO: ChatsListActivity should call this method to make the fragment load chats from disk 
	private void updateChatsAdapterFromDisk() {
		// Read the unread items from disk
		/*
			File filesDir = getFilesDir();
			File todoFile = new File(filesDir, "todo.txt");

			try {
				todoItems = new ArrayList<String>(FileUtils.readLines(todoFile));
			} catch (IOException e) {
				todoItems = new ArrayList<String>();
			}
		*/
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
