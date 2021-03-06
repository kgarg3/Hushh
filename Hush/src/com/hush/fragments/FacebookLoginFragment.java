package com.hush.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.hush.R;
import com.hush.activities.ChatsListActivity;

// Class created from: https://developers.facebook.com/docs/android/login-with-facebook#step3
public class FacebookLoginFragment extends Fragment {

	private static final String TAG = "FacebookLoginFragment";
	private UiLifecycleHelper uiHelper;
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	    	String text = "Logged in...";
	        Log.d(TAG, text);
	        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
	    	
	        Intent i = new Intent(getActivity(), ChatsListActivity.class);
	    	startActivity(i);
	    	
	    } else if (state.isClosed()) {
	    	String text = "Logged out...";
	        Log.d(TAG, text);
	        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
	    }
	}
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.fragment_facebook_login, container, false);

	    // Call the setFragment() method on the LoginButton instance to allow the fragment
	    // to handle the onActivityResult() call rather than the calling activity handling it
	    LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
	    authButton.setFragment(this);
	    // You can request additional permissions here
	    // authButton.setReadPermissions(Arrays.asList("read_friendlists"));

	    return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiHelper.onCreate(savedInstanceState);
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    
	    // For scenarios where the main activity is launched and user session is not null
	    // (like when your app is launched through a bookmark link from the Facebook app),
	    // the session state change notification may not be triggered. So check
	    // it and trigger it if it's open/closed.
	    Session session = Session.getActiveSession();
	    if (session != null && (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }

	    uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	
}
