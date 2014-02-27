package com.hush.activities;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.hush.HushApp;
import com.hush.R;
import com.hush.models.User;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;
import com.parse.ParseUser;


/**
 * 
 * Login activity to verify users' credentials before logging them into Hush. 
 *
 */
public class HushLoginActivity extends Activity {
	
	private final static String TAG = HushLoginActivity.class.getSimpleName();
	private Dialog progressDialog;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


	@Override
	public void onResume() {
		super.onResume();
		autoLoginUser();
	}
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }
    
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
	}
	
    private void autoLoginUser() {

		// If there is an auth token for the user and the user is linked to a Facebook account
    	// then log them in
		ParseUser currentUser = ParseUser.getCurrentUser();
		if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
			// Set the user globally in HushApp
			HushApp.setCurrentUser(new User(currentUser));
			showChatsListActivity();
			return;
		}
    }
    
    public void login(View v) {
		// Auth token not found, so log them in
		progressDialog = ProgressDialog.show(HushLoginActivity.this, "", "Logging in...", true);
		
		//List<String> permissions = Arrays.asList("basic_info", "user_about_me", "user_birthday", "user_location");
		//ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
		
		ParseFacebookUtils.logIn(null, this, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException err) {
				boolean loginSuccessful = false;
				
				progressDialog.dismiss();
				if (user == null) {
					Log.d(TAG, "Uh oh. The user cancelled the Facebook login.");
				} else if (user.isNew()) {
					Log.d(TAG, "User signed up and logged in through Facebook!");
					loginSuccessful = true;
				} else {
					Log.d(TAG, "User logged in through Facebook!");
					loginSuccessful = true;
				}
				
				if (loginSuccessful) {
					User loggedInUser = new User(user);
					HushApp.setCurrentUser(loggedInUser);

					// doing this on login, since an existing user can log in to multiple devices
					ParseInstallation installation = ParseInstallation.getCurrentInstallation();
			    	// Associate the device with a user
			    	installation.put("user", user);
			    	installation.saveInBackground();
			    	
					showChatsListActivity();
					return;
				}
			}
		});
    }
    

    private void showChatsListActivity() {
		// Navigate user to chat lists
		Intent i = new Intent(HushLoginActivity.this, ChatsListActivity.class);
		startActivity(i);
    }
}
