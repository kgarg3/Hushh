package com.hush.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.hush.R;

/**
 * 
 * @author 
 * 
 * Login acitivity to verify users' credentials before loggin them into Hush. 
 *
 */
public class LoginActivity extends Activity {

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
    
    /**
     * Called when the login with facebook button is clicked. Perform facebook credential authorization here. 
     * @param v
     */
    public void loginWithFacebook(View v) {
  
    	
    }
    
}
