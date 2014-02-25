package com.hush.fragments;


/**
 * 
 * @author gargka
 *
 * Fragment to hold the users chats
 */
public class PrivateChatsFragment extends ChatListFragment {

	@Override
	protected String getChatListType() {
		return "private";
	}


}
