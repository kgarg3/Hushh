package com.hush.fragments;

import android.os.Bundle;

/**
 * 
 * @author gargka
 *
 * Fragment to hold the users chats
 */
public class MyChatsFragment extends ChatListFragment{
	
	private String title;
	private int page;

	// newInstance constructor for creating fragment with arguments
	public static MyChatsFragment newInstance(int page, String title) {
		MyChatsFragment fragmentFirst = new MyChatsFragment();
		Bundle args = new Bundle();
		args.putInt("someInt", page);
		args.putString("someTitle", title);
		fragmentFirst.setArguments(args);
		return fragmentFirst;
	}

	// Store instance variables based on arguments passed
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		page = getArguments().getInt("someInt", 0);
		title = getArguments().getString("someTitle");
	}


	@Override
	protected void getChatListType() {
		// TODO return mychats qualifier
		
	}

}
