package com.hush.activities;

import java.util.Arrays;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.hush.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

/**
 * 
 * Login acitivity to verify users' credentials before logging them into Hush. 
 *
 */
public class LoginActivity extends FragmentActivity {
	
	private final static String TAG = LoginActivity.class.getSimpleName();
	private static ProgressDialog progressDialog;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }
    
    public void loginWithParseUsingFacebook(View v) {
		Intent i = new Intent(LoginActivity.this, ChatsListActivity.class);
		startActivity(i);

		/*
    	progressDialog = ProgressDialog.show(
            LoginActivity.this, "", "Logging in...", true);
	        List<String> permissions = Arrays.asList("basic_info"); // , "user_about_me", "user_relationships", "user_birthday", "user_location");
	        ParseFacebookUtils.logIn(permissions, this, new LogInCallback() {
	        	
	            @Override
	            public void done(ParseUser user, ParseException err) {
	                progressDialog.dismiss();
	                if (user == null) {
	                    Log.d(TAG, "Uh oh. The user cancelled the Facebook login.");
	                } else if (user.isNew()) {
	                    Log.d(TAG, "User signed up and logged in through Facebook!");
	                } else {
	                    Log.d(TAG, "User logged in through Facebook!");
	                }
	            }
        });
        */
    }
}
