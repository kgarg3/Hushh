package com.hush.clients;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Request.GraphUserListCallback;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.model.GraphUser;
import com.hush.models.HushUser;


public class FacebookClient {

	public static void setUserPropertiesFromFacebookAndLoginIntoLayer(){
	    final Session session = Session.getActiveSession();

	    if(!session.getState().isOpened()) { return; }
	    
	    Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
	    
	    	@Override
	        public void onCompleted(GraphUser user, Response response) {
	            if(user != null && session == Session.getActiveSession()) {
	            	HushUser hu = HushUser.getNewInstance(user.getId(), user.getFirstName(), user.getLastName());
	            	LayerClient.loginUser(hu);
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


	/*
	public static void getFBFriendsAndUploadToLayer(){
		
	    Session session = Session.getActiveSession();
	    
	    if(!session.getState().isOpened()) { return; }
	    
        Request friendRequest = Request.newMyFriendsRequest(session, new GraphUserListCallback() {
        	
            @Override
            public void onCompleted(List<GraphUser> users, Response response) {
                
            	//Log.i("FRIENDS LIST", response.toString());
            	String text = response.getGraphObject().getInnerJSONObject().toString();
            	Log.i("FRIENDS LIST", text);
            	//Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                
            	JSONArray jsonArray;
            	try {
			        jsonArray = response.getGraphObject().getInnerJSONObject().getJSONArray("data");
			        for (int i = 0; i < jsonArray.length(); i++) {
		           	     JSONObject jsonObject = jsonArray.getJSONObject(i);
		           	     
		           	     String fbId = jsonObject.getString("id");
		           	     String fullName = jsonObject.getString("name");
		           	     String[] firstAndLastNames = fullName.split(" ");
		           	     
		           	     LayerClient.addContact(firstAndLastNames[0], firstAndLastNames[1], fbId);
		           	}
            	} catch (JSONException e) {
			        e.printStackTrace();
			        return;
			    }
            }
        });
        
        Bundle params = new Bundle();
        params.putString("fields", "id,name");
        friendRequest.setParameters(params);
        
        friendRequest.executeAsync();
    }
	*/
}
