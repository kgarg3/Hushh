package com.hush.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.hush.R;
import com.hush.adapter.ChatAdapter;
import com.hush.data.HushData;
import com.hush.listeners.EndlessScrollListener;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;


/**
 * 
 * @author gargka
 *
 * Fragment to hold the list of chats
 */
public abstract class ChatListFragment extends Fragment {

	protected  PullToRefreshListView lvChats;
	protected ProgressBar progressBarLoadingTweets;
	protected ChatAdapter adapter;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Defines the xml file for the fragment
		View view = inflater.inflate(R.layout.fragment_chat_list, container, false);

		// Setup handles to view objects here
		progressBarLoadingTweets = (ProgressBar) view.findViewById(R.id.pgbarChatListFragment);
		lvChats = (PullToRefreshListView) view.findViewById(R.id.lvChatListFragmentChatsList);	
		adapter = new ChatAdapter(getActivity(), HushData.getChatList());
		lvChats.setAdapter(adapter);

		setupListeners();

		return view;
	}

	/**
	 * Add a scroll and pull to refresh listener to the chats list view
	 */
	private void setupListeners() {
		//scrolling should load more chats 
		lvChats.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {

			}
		});

		// Set a listener to be invoked when the list should be refreshed.
		lvChats.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Your code to refresh the list contents
				// Make sure you call listView.onRefreshComplete()
				// once the loading is done. This can be done from here or any
				// place such as when the network request has completed successfully.

				// Now we call onRefreshComplete to signify refresh has finished
				lvChats.onRefreshComplete();

			}
		});
	}

	public ChatAdapter getAdapter() {
		return adapter;
	}
	
	protected abstract void getChatListType();
}
