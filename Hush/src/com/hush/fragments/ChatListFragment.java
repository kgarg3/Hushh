package com.hush.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.hush.R;
import com.hush.adapter.ChatAdapter;
import com.hush.models.Chat;

import eu.erikw.PullToRefreshListView;

/**
 * 
 * @author gargka
 *
 * Fragment to hold the list of chats
 */
public class ChatListFragment extends Fragment {

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
		adapter = new ChatAdapter(getActivity(), new ArrayList<Chat>());
		lvChats.setAdapter(adapter);
		
		return view;
	}

	public ChatAdapter getAdapter() {
		return adapter;
	}
}
