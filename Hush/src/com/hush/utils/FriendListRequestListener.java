package com.hush.utils;

import java.util.List;

import android.os.Bundle;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;


public class FriendListRequestListener {

	public static void getUserData(){
	    final Session session = Session.getActiveSession();

	    if(!session.getState().isOpened()) { return; }
	    
	    Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
	    
	    	@Override
	        public void onCompleted(GraphUser user, Response response) {
	            if(user != null && session == Session.getActiveSession()){
	                //pictureView.setProfileId(user.getId());
	                //userName.setText(user.getName());
	                getFriends();
	            }
	            if(response.getError() !=null){
	            }
	        }
	    });
	    
	    request.executeAsync();
	}

	private static void getFriends(){
		
	    Session session = Session.getActiveSession();
	    if(!session.getState().isOpened()) { return; }
	    
        Request friendRequest = Request.newMyFriendsRequest(session, new GraphUserListCallback(){
            @Override
            public void onCompleted(List<GraphUser> users, Response response) {
                Log.i("FRIENDS LIST", response.toString());
            }
        });
        
        Bundle params = new Bundle();
        params.putString("fields", "id,name");
        friendRequest.setParameters(params);
        
        friendRequest.executeAsync();
    }

}
