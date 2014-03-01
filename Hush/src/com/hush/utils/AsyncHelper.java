package com.hush.utils;

import java.util.List;

import com.hush.models.Chat;

public interface AsyncHelper {
	public void userAttributesFetched(String inName, String inFacebookId);
	public void chatsFetched(List<Chat> chats);
	public void chattersFetched();
	public void messagesFetched();
}
