package com.hush.fragments;


/**
 * 
 * @author gargka
 * 
 * Fragment to hold friends chats
 */
public class FriendsChatsFragment extends ChatListFragment{
	
	@Override
	protected String getChatListType() {
		return "public";
	}
}
