package com.hush.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.MenuItem;

import com.hush.R;
import com.hush.fragments.FriendsChatsFragment;
import com.hush.fragments.MyChatsFragment;

/**
 * 
 * @author 
 *
 * Activity that shows all the chats of the logged in user and his friends. 
 */
public class ChatsListActivity extends FragmentActivity {
	public static final String CHAT = "chat";

	private FragmentPagerAdapter adapterViewPager;
	private ViewPager vpPager;

	public static class MyPagerAdapter extends FragmentPagerAdapter {
		private static int NUM_ITEMS = 2;
		private final String privateChats, publicChats;

		private MyChatsFragment myChatsFragment;
		private FriendsChatsFragment frdsChatFragment;

		public MyPagerAdapter(FragmentManager fragmentManager, String privateChats, String publicChats) {
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
				if(frdsChatFragment == null)  
					return frdsChatFragment = new FriendsChatsFragment();
				return frdsChatFragment;
			case 0: //fallthrough
			default:
				if(myChatsFragment == null)  
					return myChatsFragment = new MyChatsFragment();
				return myChatsFragment;
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
		adapterViewPager = new MyPagerAdapter(getSupportFragmentManager(), 
				getString(R.string.tab_private_chats), getString(R.string.tab_public_chats));
		vpPager.setAdapter(adapterViewPager);
		setPagerListeners();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.all_chats, menu);
		return true;
	}

	/**
	 * Listener for pageViewer
	 */
	private void setPagerListeners() {
		// Attach the page change listener inside the activity
		vpPager.setOnPageChangeListener(new OnPageChangeListener() {

			// This method will be invoked when a new page becomes selected.
			@Override
			public void onPageSelected(int position) {
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
				ft.replace(R.id.flChatsActivityChatLists, adapterViewPager.getItem(position));
				ft.commit();
			}

			// This method will be invoked when the current page is scrolled
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

			// Called when the scroll state changes: 
			// SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
			@Override
			public void onPageScrollStateChanged(int state) { }
		});

	}

	// menu actions
	public void onNewChatClick(MenuItem mi) {
		Intent i = new Intent(ChatsListActivity.this, NewChatActivity.class);
		startActivity(i);
	}

}
