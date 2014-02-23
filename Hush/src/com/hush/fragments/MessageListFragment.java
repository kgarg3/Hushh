package com.hush.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.hush.R;
import com.hush.adapter.MessageAdapter;
import com.hush.data.HushData;
import com.hush.listeners.EndlessScrollListener;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class MessageListFragment extends Fragment{
	protected  PullToRefreshListView lvMessages;
	protected ProgressBar progressBarLoadingMessages;
	protected MessageAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Defines the xml file for the fragment
		View view = inflater.inflate(R.layout.fragment_message_list, container, false);

		// Setup handles to view objects here
		progressBarLoadingMessages = (ProgressBar) view.findViewById(R.id.pgbarMessageListFragment);
		lvMessages = (PullToRefreshListView) view.findViewById(R.id.lvMessageListFragmentMessageList);	
		adapter = new MessageAdapter(getActivity(), HushData.getMessageList());
		lvMessages.setAdapter(adapter);

		setupListeners();

		return view;
	}

	/**
	 * Add a scroll and pull to refresh listener to the chats list view
	 */
	private void setupListeners() {
		//scrolling should load more chats 
		lvMessages.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {

			}
		});

		// Set a listener to be invoked when the list should be refreshed.
		lvMessages.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// Your code to refresh the list contents
				// Make sure you call listView.onRefreshComplete()
				// once the loading is done. This can be done from here or any
				// place such as when the network request has completed successfully.

				// Now we call onRefreshComplete to signify refresh has finished
				lvMessages.onRefreshComplete();

			}
		});
	}

	public MessageAdapter getAdapter() {
		return adapter;
	}
}
