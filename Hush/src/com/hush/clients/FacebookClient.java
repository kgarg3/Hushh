package com.hush.clients;

import android.util.Log;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.hush.utils.AsyncHelper;


public class FacebookClient {

	public static void fetchAndSetUserAttributes(final AsyncHelper ah){
	    final Session session = Session.getActiveSession();
	    if(!session.getState().isOpened()) { return; }
	    
	    Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
	    
	    	@Override
	        public void onCompleted(GraphUser user, Response response) {
	            if(user != null && session == Session.getActiveSession()) {
	            	ah.userAttributesFetched(user.getName(), user.getId());
	            	return;
	            }
	            
	            if(response.getError() != null) {
	            	String text = "Type = " + response.getError().getErrorType() + " Message = " + response.getError().getErrorMessage();
	            	Log.d("ERROR GETTING FB USER", text);
			        return;
	            }
	        }
	    });
	    
	    request.executeAsync();
	}
}
