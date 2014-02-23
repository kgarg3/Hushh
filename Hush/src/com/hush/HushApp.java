package com.hush;

import java.util.List;

import android.app.Application;

import com.facebook.model.GraphUser;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.PushService;

public class HushApp extends Application {

    private List<GraphUser> selectedUsers;

    public List<GraphUser> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<GraphUser> inSelectedUsers) {
        selectedUsers = inSelectedUsers;
    }
    
    @Override
    public void onCreate() {
    	super.onCreate();
    	
    	Parse.initialize(this, getString(R.string.parse_app_id), getString(R.string.parse_client_key));
    	
    	/*
    	ParseObject testObject = new ParseObject("TestObject");
    	testObject.put("foo", "bar");
    	testObject.saveInBackground();
    	*/
    	
    	ParseFacebookUtils.initialize(getString(R.string.facebook_app_id));
    	PushService.setDefaultPushCallback(this, ParseNotificationsHandlerActivity.class);
    }
}
