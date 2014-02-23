package com.hush.clients;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import layer.sdk.ContactManager;
import layer.sdk.LayerAddress;
import layer.sdk.MessageManager;
import layer.sdk.SessionManager;
import layer.sdk.UserManager;
import layer.sdk.contacts.Contact;
import layer.sdk.messages.Conversation;
import layer.sdk.messages.Message;
import layer.sdk.user.User;
import android.util.Log;

import com.hush.models.HushUser;

public class LayerClient {
	
	private final static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
	private final static String chatId = UUID.randomUUID().toString();
	private final static String TAG = LayerClient.class.getSimpleName();
	private final static String userDoesNotExistErrorMessage = "No user for "; 
	private final static long userDoesNotExistErrorCode = 2; 

	public static User getCurrentLayerUser() {
		return UserManager.getUser();
	}
	
	public static void createAndLoginUser(HushUser u) {
		UserManager.create(u.fbIdLayerUserName, "", u.firstName, u.lastName, u.fbIdLayerUserName, u.fbIdLayerUserName, new UserManager.UserCreateCallback() {
			
			@Override
			public void onCreated(User user) {
				String text = "user successfully registered";
				Log.d(TAG, text);
				//Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onError(int code, String message) {
				String text = "onError, code= " + code + "; message= " + message;
				Log.e(TAG, text);
				//Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	public static void loginUser(final HushUser hu) {
		SessionManager.login(hu.fbIdLayerUserName, "", new SessionManager.SessionLogInCallback() {

			@Override
			public void onLoggedIn(User user) {
				String text = "user successfully logged in"; 
				Log.d(TAG, text);
				//Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onError(int code, String message) {
				String text = "onError; code=" + code + "; message=" + message; 
				Log.d(TAG, text);
				//Toast.makeText(this, text, Toast.LENGTH_SHORT).show();

				if (code == userDoesNotExistErrorCode && message.startsWith(userDoesNotExistErrorMessage)) {
					LayerClient.createAndLoginUser(hu);
				}

				/*
				if (getCurrentLayerUser() != null) {
					FacebookClient.getFBFriendsAndUploadToLayer();
				}
				*/
			}
		});
	}

	public static Contact getContactObject(String firstName, String lastName, String fbIdLayerUserName) {
		return ContactManager.newContact(firstName, lastName, fbIdLayerUserName, fbIdLayerUserName);
	}
	
	public static void addContact(final Contact layerContact) {

		ContactManager.addContact(layerContact, new ContactManager.ContactAddCallback() {

			@Override
			public void onContactsAdded() {
				String text = "contact " + layerContact.getFirstName() + " " + layerContact.getLastName() + " successfully added";
				Log.d(TAG, text);
				//Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onError(int code, String message) {
				String text = "onError; code=" + code + "; message=" + message;
				Log.d(TAG, text);
				//Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	/*
	public void updateContact() {
		Contact john = ContactManager.getContactByUUID("blah");
		john.addPhone("+1 415 123 4567");
		ContactManager.updateContact(john, new ContactManager.ContactUpdateCallback() {
		  @Override
		  public void onContactsUpdated() {
		    String text = "contact successfully updated";
		    Log.d(TAG, text);
		    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
		  }

		  @Override
		  public void onError(int code, String message) {
		    String text = "onError; code=" + code + "; message=" + message;
		    Log.d(TAG, text);
		    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
		  }
		});
	}
	
	public void deleteContact() {
		Contact john = ContactManager.getContactByUUID("blah");
		ContactManager.deleteContact(john, new ContactManager.ContactDeleteCallback() {
			  @Override
			  public void onContactsDeleted() {
			  	String text = "contact deleted";
			    Log.d(TAG, text);
		    	Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
			  }

			  @Override
			  public void onError(int code, String message) {
			    String text = "onError; code=" + code + "; message=" + message;
			    Log.d(TAG, text);
    		    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
			  }
			});
	}
	*/
	
	public static void startNewGroupChatWithSelectedUsers(List<Contact> contacts) {
		String date = dateFormat.format(new Date());
		String messageBody = "Chat initiated, Test message sent at " + dateFormat.format(new Date());
		Message message = MessageManager.newMessage("Chat initiated, Test Msg Subject sent at " + date, messageBody.getBytes(), "text/plain");
		message.setThreadId(chatId);
		MessageManager.sendMessageToContact(message, contacts);
	}
	
	public static void sendNewMessageOnChat() {
		
		Conversation chat = MessageManager.getConversationByThreadId(chatId);
		List<LayerAddress> addresses = chat.getParticipants();
		
		String date = dateFormat.format(new Date());
		Message message = MessageManager.newMessage("Test Msg Subject sent at " + date, ("Test message sent at " + dateFormat.format(new Date())).getBytes(), "text/plain");
		message.setThreadId(chatId);

		MessageManager.sendMessageToLayerAddresses(message, addresses);
	}
	
	public static void getAllUsersInChat() {
		Conversation chat = MessageManager.getConversationByThreadId(chatId);
		List<LayerAddress> addresses = chat.getParticipants();
		
		String text = "Total participants = " + addresses.size();
		Log.d(TAG, text);
	}

	public static void addContactsToChat(List<Contact> contacts) {
		Conversation chat = MessageManager.getConversationByThreadId(chatId);
		
		List<LayerAddress> addresses = chat.getParticipants();
		
		// Add all the layer addresses of each contact in the input list into the existing list
		for (Contact contact : contacts ) {
			addresses.addAll(contact.getLayerAddresses());
		}
		
		// Now send a msg to all the addresses
		String date = dateFormat.format(new Date());
		Message message = MessageManager.newMessage("Test Msg Subject sent at " + date, ("Test message sent at " + dateFormat.format(new Date())).getBytes(), "text/plain");
		message.setThreadId(chatId);

		MessageManager.sendMessageToLayerAddresses(message, addresses);
	}
	
	
	/*
	public static List<Conversation> getConversationsForMatchingContacts(List<Contact> contacts) {
		return MessageManager.getConversations(Conversation.Type.PARTICIPANTS, contacts, Conversation.ParticipantMatching.ONLY);
	}

	// Useful for future, if a user deletes a friend, their chats should be deleted from the friend's chat
	// Friend deletion can be pushed to clients using this
	public void getUpdatedContacts() {
		ContactManager.addContactListener(new ContactManager.ContactListener() {
			  @Override
			  public void onContactsSynced(List<Contact> contacts) {
			    String text = "new contact updates received; contact updates=" + contacts.toString();
			    Log.d(TAG, text);
			   	Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
			  }

			  @Override
			  public void onContactsDeleted(List<String> contactIds) {
			    Log.d(TAG, "existing contacts deleted");
			    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
			  }

			  @Override
			  public void onError(int code, String message) {
			    Log.d(TAG, "onError; code=" + code + "; message=" + message);
			    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
			  }
			});
	}
	
	// Useful for future
	public void pushDeviceContactsToLayer() {
		ContactManager.importContactsFromDevice(new ContactImportCallback() {
			  @Override
			  public void onContactsImported() {
			    Log.d(TAG, "contacts successfully imported");
			    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
			  }

			  @Override
			  public void onProgress(double percentage) {
			    Log.d(TAG, "Import " + percentage + "% done");
			    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
			  }

			  @Override
			  public void onError(int code, String message) {
			    Log.d(TAG, "onError; code=" + code + "; message=" + message);
			    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
			  }
			});
	}
	*/
}
