package com.hush.clients;

import android.util.Log;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.hush.models.User;


public class FacebookClient {

	public static void fetchAndSetUserAttributesInParse(final User inUser){
	    final Session session = Session.getActiveSession();
	    if(!session.getState().isOpened()) { return; }
	    
	    Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
	    
	    	@Override
	        public void onCompleted(GraphUser user, Response response) {
	            if(user != null && session == Session.getActiveSession()) {
	            	inUser.setUserAttributesInParse(user.getName(), user.getId());
	            }
	            
	            if(response.getError() != null) {
	            	String text = "Type = " + response.getError().getErrorType() + " Message = " + response.getError().getErrorMessage();
	            	Log.d("ERROR GETTING FB USER", text);
	            	//Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
			        return;
	            }
	        }
	    });
	    
	    request.executeAsync();
	}
}
