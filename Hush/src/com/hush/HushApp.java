package com.hush;

import java.util.List;

import layer.sdk.Layer;
import android.app.Application;

import com.facebook.model.GraphUser;
import com.parse.Parse;
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
    	Layer.init(getApplicationContext(), getString(R.string.layer_app_id));
    	
    	Parse.initialize(this, "psNJCoT9RhlnjpthicNrxPVM9llJOrSFbRNgXHjO", "DEdGrNxRUjMX6BZqdonXdSkX7AapKqkGxzVQObGW");
    	
    	/*
    	ParseObject testObject = new ParseObject("TestObject");
    	testObject.put("foo", "bar");
    	testObject.saveInBackground();
    	*/
    	
    	PushService.setDefaultPushCallback(this, ParseNotificationsHandlerActivity.class);
    }
}
