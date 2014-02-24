package com.hush.fragments;


/**
 * 
 * @author gargka
 *
 * Fragment to hold the users chats
 */
public class MyChatsFragment extends ChatListFragment {

	@Override
	protected String getChatListType() {
		return "private";
	}


}
