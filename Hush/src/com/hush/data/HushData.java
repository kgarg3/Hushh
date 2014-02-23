package com.hush.data;

import java.util.ArrayList;

import com.hush.models.Chat;
import com.hush.models.Message;

public class HushData {

	public static ArrayList<Chat> getChatList() {
		ArrayList<Chat> chatlist = new ArrayList<Chat>();		
		for(int i=0; i<100; i++){
			Chat chat = new Chat();
			
			chat.setChatTopic("Chat" + i);
			chat.setNotification(i);
			chat.setId(String.valueOf(i));
			chat.setTimeRemaining((long)i*50);
			
			chatlist.add(chat);
		}	
		return chatlist;
	}
	
	public static ArrayList<Message> getMessageList() {
		ArrayList<Message> chatlist = new ArrayList<Message>();		
		for(int i=0; i<100; i++){
			Message message = new Message();
			
			message.setId(String.valueOf(i));
			message.setContent("Message" + i);
			message.setChatId(String.valueOf(i));
			
			chatlist.add(message);
		}	
		return chatlist;
	}
	
	
}

