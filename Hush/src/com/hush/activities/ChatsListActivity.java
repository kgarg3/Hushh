package com.hush.activities;


import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.Session;
import com.hush.HushApp;
import com.hush.R;
import com.hush.fragments.PrivateChatsFragment;
import com.hush.fragments.PublicChatsFragment;
import com.hush.models.Chat;
import com.hush.utils.AsyncHelper;
import com.parse.ParseUser;

/**
 * 
 * @author 
 *
 * Activity that shows all the chats of the logged in user and his friends. 
 */
public class ChatsListActivity extends FragmentActivity implements AsyncHelper {
	public static final String CHAT = "chat";

	private FragmentPagerAdapter adapterViewPager;
	private ViewPager vpPager;
	private static List<Chat> chats;

	public static class ChatsListPagerAdapter extends FragmentPagerAdapter {
		private static int NUM_ITEMS = 2;
		private final String privateChats, publicChats;

		private PrivateChatsFragment privateChatsFragment;
		private PublicChatsFragment publicChatFragment;

		public ChatsListPagerAdapter(FragmentManager fragmentManager, String privateChats, String publicChats) {
			super(fragmentManager);
			this.privateChats = privateChats;
			this.publicChats = publicChats;
		}

		// Returns total number of pages
		@Override
		public int getCount() {
			return NUM_ITEMS;
		}

		// Returns the fragment to display for that page
		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 1: 
				if(publicChatFragment == null) {  
					return publicChatFragment = new PublicChatsFragment();
				}
				return publicChatFragment;
			case 0: //fallthrough
			default:
				if(privateChatsFragment == null) { 
					return privateChatsFragment = new PrivateChatsFragment();
				}
				return privateChatsFragment;
			}
		}

		// Returns the page title for the top indicator
		@Override
		public CharSequence getPageTitle(int position) {
			return position == 0 ? privateChats : publicChats;
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chats_list);

		//create the page viewer
		vpPager = (ViewPager) findViewById(R.id.vpPager);
		adapterViewPager = new ChatsListPagerAdapter(getSupportFragmentManager(), 
				getString(R.string.tab_private_chats), getString(R.string.tab_public_chats));
		vpPager.setAdapter(adapterViewPager);

		HushApp.getCurrentUser().fetchMessagesFromParse(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chats_list, menu);
		return true;
	}

	// menu actions
	public void onNewChatClick(MenuItem mi) {
		Intent i = new Intent(ChatsListActivity.this, NewChatActivity.class);
		startActivity(i);
	}

	public void onLogoutClick(MenuItem mi) {
		// Log the user out
		ParseUser.logOut();
		
		// Also close the session and clear theauth token
		Session facebookSession = Session.getActiveSession();
		if (facebookSession == null) {
			facebookSession = new com.facebook.Session(this);
			com.facebook.Session.setActiveSession(facebookSession);
		}
		facebookSession.closeAndClearTokenInformation();
		
		Toast.makeText(this, "Logged out!", Toast.LENGTH_SHORT).show();
		Intent i = new Intent(ChatsListActivity.this, HushLoginActivity.class);
		startActivity(i);
	}

	@Override
	public void chatsFetched() {
		chats = HushApp.getCurrentUser().getChats();
	}

	@Override
	public void chattersFetched() {
		// Ignore in this activity
	}

	@Override
	public void messagesFetched() {
		// Ignore in this activity
	}

}
