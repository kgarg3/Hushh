package com.hush.clients;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.model.GraphUser;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;


public class ParseClient {

	private final static String TAG = ParseClient.class.getSimpleName();

	/*
	private final static DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
	private final static String chatId = UUID.randomUUID().toString();
	private final static String userDoesNotExistErrorMessage = "No user for "; 
	private final static long userDoesNotExistErrorCode = 2; 

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
					createAndLoginUser(hu);
				}
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
	*/
	
	public void setParseObject() {
    	ParseObject testObject = new ParseObject("TestObject");
    	testObject.put("foo", "bar");
    	testObject.saveInBackground();
	}
	
	public void requestMyDetails() {
		Request request = Request.newMeRequest(ParseFacebookUtils.getSession(), new Request.GraphUserCallback() {
			@Override
			public void onCompleted(GraphUser user, Response response) {
				if (user != null) {
					// Create a JSON object to hold the profile info
					JSONObject userProfile = new JSONObject();
					try {
						// Populate the JSON object
						userProfile.put("facebookId", user.getId());
						userProfile.put("name", user.getName());

						if (user.getProperty("gender") != null) {
							userProfile.put("gender", (String) user.getProperty("gender"));
						}
						if (user.getBirthday() != null) {
							userProfile.put("birthday",user.getBirthday());
						}

						// Save the user profile info in a user property
						ParseUser currentUser = ParseUser.getCurrentUser();
						currentUser.put("profile", userProfile);
						currentUser.saveInBackground();

						// Show the user info
						getUserProfileAttributesFromFacebook();
						
					} catch (JSONException e) {
						Log.d(TAG,"Error parsing returned user data.");
					}

				} else if (response.getError() != null) {
					if ((response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_RETRY)
							|| (response.getError().getCategory() == FacebookRequestError.Category.AUTHENTICATION_REOPEN_SESSION)) {
						Log.d(TAG, "The facebook session was invalidated.");
					} else {
						Log.d(TAG, "Some other error: " + response.getError().getErrorMessage());
					}
				}
			}
		});
		request.executeAsync();

	}

	public void getUserProfileAttributesFromFacebook() {
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser.get("profile") != null) {
			JSONObject userProfile = currentUser.getJSONObject("profile");
			try {
				if (userProfile.getString("facebookId") != null) {
					String facebookId = userProfile.get("facebookId").toString();
				}
				if (userProfile.getString("name") != null) {
					// userNameView.setText("");
				}
				
				if (userProfile.getString("gender") != null) {
					// userGenderView.setText(userProfile.getString("gender"));
				}

				if (userProfile.getString("birthday") != null) {
					// userDateOfBirthView.setText(userProfile.getString("birthday"));
				}
			} catch (JSONException e) {
				Log.d(TAG,"Error parsing saved user data.");
			}

		}
	}

}
