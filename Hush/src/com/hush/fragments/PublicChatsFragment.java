package com.hush.fragments;


/**
 * 
 * @author gargka
 * 
 * Fragment to hold friends chats
 */
public class PublicChatsFragment extends ChatListFragment{
	
	@Override
	protected String getChatListType() {
		return "public";
	}

}
