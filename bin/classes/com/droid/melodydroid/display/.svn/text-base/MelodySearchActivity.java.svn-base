package com.droid.melodydroid.display;

import java.util.ArrayList;
import java.util.List;

import com.droid.melodydroid.R;
import com.droid.melodydroid.core.DatabaseSynchronizationService;
import com.droid.melodydroid.core.MDSInterface;
import com.droid.melodydroid.core.MDService;

import android.app.ListActivity;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.ListView;

public class MelodySearchActivity extends ListActivity {

	public static final String PROVIDER_NAME = "melodyproviderauthority";
	private static final String TABLE_NAME = "MELODYDROID";
	private static final int ITEM_ID_1 = 1;
	private static final int ITEM_ID_2 = 2;
	private static final int ITEM_ID_3 = 3;
	private List<CustomSearchResult> customSearchResultList = null;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;
	private MDSInterface mpInterface;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_search_main);

		final Intent queryIntent = getIntent();
		onNewIntent(queryIntent);
	}

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			Log.v("MelodySearchActivity: ", "onServiceConnected");
			mpInterface = (MDSInterface) service;
		}

		public void onServiceDisconnected(ComponentName className) {
			mpInterface = null;
		}
	};

	public void updateSongList(List<CustomSearchResult> customSearchResultList) {

		mpInterface.clearPlaylist();
		for (CustomSearchResult file : customSearchResultList) {
			mpInterface.addSongPlaylist(file.getFileName());
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {

		customSearchResultList = new ArrayList<CustomSearchResult>();
		final String queryAction = intent.getAction();

		super.onNewIntent(intent);
		if (Intent.ACTION_SEARCH.equals(queryAction)) {
			Log.v("MelodySearchActivity: ", "Received Search intent");
			String searchKeywords = intent.getStringExtra(SearchManager.QUERY);
			searchKeywords = searchKeywords.replaceAll("'", "''");
			String whereClause = " lower(album) like lower('%" + searchKeywords
					+ "%') OR singer like lower('%" + searchKeywords
					+ "%') OR title like lower('%" + searchKeywords
					+ "%') OR year like lower('%" + searchKeywords
					+ "%') OR genre like lower('%" + searchKeywords + "%')";

			Cursor cursor = getContentResolver().query(
					Uri.parse("content://" + PROVIDER_NAME + "/" + TABLE_NAME),
					new String[] { "album", "singer", "genre", "title", "year",
							"fileName" }, whereClause, null, null);

			if (cursor.moveToFirst()) {
				do {
					CustomSearchResult customResult = new CustomSearchResult();
					customResult.setAlbum(cursor.getString(0));
					customResult.setSinger(cursor.getString(1));
					customResult.setGenre(cursor.getString(2));
					customResult.setTitle(cursor.getString(3));
					customResult.setYear(cursor.getString(4));
					customResult.setFileName(cursor.getString(5));
					customSearchResultList.add(customResult);
				} while (cursor.moveToNext());
			}

			cursor.close();

			CustomSearchResultsAdapter adapter = new CustomSearchResultsAdapter(
					this, R.layout.custom_search_row, customSearchResultList);
			setListAdapter(adapter);
			ListView listView = getListView();
			ColorDrawable sage = new ColorDrawable(this.getResources().getColor(R.drawable.sage));		
			listView.setDivider(sage);
			listView.setDividerHeight(1);
						
			
			// Gesture detection
			gestureDetector = new GestureDetector(new MyGestureDetector());
			gestureListener = new View.OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					if (gestureDetector.onTouchEvent(event)) {
						return true;
					}
					return false;
				}
			};
			getListView().setOnTouchListener(gestureListener);				
		}
		
		Intent serviceIntent = new Intent();
		serviceIntent.setAction("com.droid.melodydroid.core.MDService");
		startService(serviceIntent);

		this.bindService(new Intent(this, MDService.class), mConnection,
				Context.BIND_AUTO_CREATE);		
	}

	class MyGestureDetector extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			try {
				if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
					return false;
				// right to left swipe
				if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
						&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				}
			} catch (Exception e) {
				// nothing
			}
			return false;
		}
	}
		
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, ITEM_ID_1, 1, R.string.search).setIcon(
				R.drawable.ic_menu_search);
		menu.add(0, ITEM_ID_2, 2, R.string.play_list).setIcon(
				R.drawable.playlist);
		menu.add(0, ITEM_ID_3, 3, R.string.dbsync).setIcon(R.drawable.syncicon);		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ITEM_ID_1:
			onSearchRequested();
			return true;
		case ITEM_ID_2:
			Intent displayPlaylists = new Intent(this, DisplayPlaylists.class);
			startActivity(displayPlaylists);
			return true;
		case ITEM_ID_3:
			DatabaseSynchronizationService dbSyncService = DatabaseSynchronizationService
					.getInstance(MelodySearchActivity.this);
			dbSyncService.findMelodiesFromSourceAndSynchDatabase();
			return true;									
		}
		return false; // should never happen
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		Renderer.playlistName = "Custom Search Results..";
		updateSongList(customSearchResultList);
		
		Intent displayPlaybackControls = new Intent(this,
				DisplayPlaybackControls.class);
		displayPlaybackControls.putExtra("position", position);
		displayPlaybackControls.putExtra("screen", "MelodySearchActivity");
		displayPlaybackControls.putExtra("swipeBack", "MelodySearchActivity");
//		displayPlaybackControls.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(displayPlaybackControls);
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unbindService(mConnection);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		this.bindService(new Intent(this, MDService.class), mConnection,
				Context.BIND_AUTO_CREATE);		
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// ignore orientation change
		super.onConfigurationChanged(newConfig);
	}
}
