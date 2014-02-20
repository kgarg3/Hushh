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

	public static void getUserData(final Session session){
	    Request request = Request.newMeRequest(session, 
	        new Request.GraphUserCallback() {
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
	    Session activeSession = Session.getActiveSession();
	    if(activeSession.getState().isOpened()){
	        Request friendRequest = Request.newMyFriendsRequest(activeSession, 
	            new GraphUserListCallback(){
	                @Override
	                public void onCompleted(List<GraphUser> users,
	                        Response response) {
	                    Log.i("FRIENDS LIST", response.toString());
	                }
	        });
	        Bundle params = new Bundle();
	        params.putString("fields", "id,name,friends");
	        friendRequest.setParameters(params);
	        friendRequest.executeAsync();
	    }
	}

}
