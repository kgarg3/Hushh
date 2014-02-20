package com.hush;

import java.util.List;

import layer.sdk.Layer;
import layer.sdk.MessageManager;
import android.app.Application;

import com.facebook.model.GraphUser;
import com.hush.utils.LayerNotificationsManager;

public class HushApp extends Application {

    private List<GraphUser> selectedUsers;

    public List<GraphUser> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(List<GraphUser> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }
    
    
    @Override
    public void onCreate() {
    	super.onCreate();
    	Layer.init(getApplicationContext(), getString(R.string.layer_app_id));
    	
    	// Register Layer's notification manager
    	LayerNotificationsManager manager = new LayerNotificationsManager();
    	MessageManager.registerNotificationManager(manager);
    }
}
