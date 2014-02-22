package com.hush.clients;

import java.util.List;

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
	
	private final static String TAG = LayerClient.class.getSimpleName();
	private final static String userDoesNotExistErrorMessage = "No user for "; 
	private final static long userDoesNotExistErrorCode = 2; 

	public static User getCurrentLayerUser() {
		return UserManager.getUser();
	}
	
	public static void registerAndLoginUser(HushUser u) {
		UserManager.create(u.fbIdLayerUserName, "", u.firstName, u.lastName, u.fbIdLayerUserName, u.fbIdLayerUserName, new UserManager.UserCreateCallback() {
			  @Override
			  public void onCreated(User user) {
			    Log.d(TAG, "user successfully registered");
			  }

			  @Override
			  public void onError(int code, String message) {
			    Log.e(TAG, "onError, code= " + code + "; message= " + message);
			  }
			});
	}
	
	public static void loginUser(final HushUser hu) {
		SessionManager.login(hu.fbIdLayerUserName, "", new SessionManager.SessionLogInCallback() {

			@Override
			public void onLoggedIn(User user) {
				Log.d(TAG, "user successfully logged in");
			}

			@Override
			public void onError(int code, String message) {
				Log.d(TAG, "onError; code=" + code + "; message=" + message);

				if (code == userDoesNotExistErrorCode && message.startsWith(userDoesNotExistErrorMessage)) {
					LayerClient.registerAndLoginUser(hu);
				}

				/*
				if (getCurrentLayerUser() != null) {
					FacebookClient.getFBFriendsAndUploadToLayer();
				}
				*/
			}
		});
	}

	public static void addContact(String firstName, String lastName, long fbIdLayerUserName) {
		final Contact layerContact = ContactManager.newContact(firstName, lastName, Long.toString(fbIdLayerUserName), Long.toString(fbIdLayerUserName));

		ContactManager.addContact(layerContact, new ContactManager.ContactAddCallback() {

			@Override
			public void onContactsAdded() {
				Log.d("TAG", "contact " + layerContact.getFirstName() + " " + layerContact.getLastName() + " successfully added");
			}

			@Override
			public void onError(int code, String message) {
				Log.d("TAG", "onError; code=" + code + "; message=" + message);
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
		    Log.d("TAG", "contact successfully updated");
		  }

		  @Override
		  public void onError(int code, String message) {
		    Log.d("TAG", "onError; code=" + code + "; message=" + message);
		  }
		});
	}
	
	public void deleteContact() {
		Contact john = ContactManager.getContactByUUID("blah");
		ContactManager.deleteContact(john, new ContactManager.ContactDeleteCallback() {
			  @Override
			  public void onContactsDeleted() {
			    Log.d("TAG", "contact deleted");
			  }

			  @Override
			  public void onError(int code, String message) {
			    Log.d("TAG", "onError; code=" + code + "; message=" + message);
			  }
			});
	}
	*/
	
	public void sendMessageToContacts(List<Contact> contacts) {
		MessageManager.sendMessageToContacts("message body", "subject", contacts);
	}
	
	public List<Conversation> getConversationsForMatchingContacts(List<Contact> contacts) {
		return MessageManager.getConversations(Conversation.Type.PARTICIPANTS, contacts, Conversation.ParticipantMatching.ONLY);
	}
	
	public void startNewChatWithUsers(List<Contact> threadContacts) {
		Message message = MessageManager.newMessage("Subject", "Hi, all!".getBytes(), "text/plain");
		message.setThreadId("new random thread id, use a guid string");
		MessageManager.sendMessageToContacts(message.getBody().toString(), "subject", threadContacts);
	}
	
	public void getAllUsersInChat(String threadId) {
		Conversation threadConversation = MessageManager.getConversationByThreadId(threadId);
		List<LayerAddress> addresses = threadConversation.getParticipants();
		Message message = MessageManager.newMessage("Subject", "Hi, all!".getBytes(), "text/plain");
		MessageManager.sendMessageToLayerAddresses(message, addresses);
	}

	
	/*
	// Useful for future, if a user deletes a friend, their chats should be deleted from the friend's chat
	// Friend deletion can be pushed to clients using this
	public void getUpdatedContacts() {
		ContactManager.addContactListener(new ContactManager.ContactListener() {
			  @Override
			  public void onContactsSynced(List<Contact> contacts) {
			    Log.d("TAG", "new contact updates received; contact updates=" + contacts.toString());
			  }

			  @Override
			  public void onContactsDeleted(List<String> contactIds) {
			    Log.d("TAG", "existing contacts deleted");
			  }

			  @Override
			  public void onError(int code, String message) {
			    Log.d("TAG", "onError; code=" + code + "; message=" + message);
			  }
			});
	}
	
	// Useful for future
	public void pushDeviceContactsToLayer() {
		ContactManager.importContactsFromDevice(new ContactImportCallback() {
			  @Override
			  public void onContactsImported() {
			    Log.d("TAG", "contacts successfully imported");
			  }

			  @Override
			  public void onProgress(double percentage) {
			    Log.d("TAG", "Import " + percentage + "% done");
			  }

			  @Override
			  public void onError(int code, String message) {
			    Log.d("TAG", "onError; code=" + code + "; message=" + message);
			  }
			});
	}
	*/
}
