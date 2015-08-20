package com.droid.melodydroid.display;

import java.util.ArrayList;
import java.util.List;

import com.droid.melodydroid.R;
import com.droid.melodydroid.core.DatabaseSynchronizationService;
import com.droid.melodydroid.core.MDSInterface;
import com.droid.melodydroid.core.MDService;
import com.droid.melodydroid.data.PlaylistDataHelper;

import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
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

public class DisplayPlaylistMelodies extends ListActivity {

	private static final int ITEM_ID_1 = 1;
	private static final int ITEM_ID_2 = 2;
	private static final int ITEM_ID_3 = 3;

	private List<SongItem> listOfSongs = new ArrayList<SongItem>();
	private String playlist;

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;
	private MDSInterface mpInterface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playlist_melodies);

		final Intent queryIntent = getIntent();
		onNewIntent(queryIntent);
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
					if (Renderer.renderDisplayControl) {
						Intent displayPlaybackControls = new Intent(
								DisplayPlaylistMelodies.this,
								DisplayPlaybackControls.class);
						displayPlaybackControls.putExtra("swipeBack",
								"DisplayPlaylistMelodies");
						displayPlaybackControls.putExtra("playlistName",
								playlist);
						startActivity(displayPlaybackControls);
					}
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
					.getInstance(DisplayPlaylistMelodies.this);
			dbSyncService.findMelodiesFromSourceAndSynchDatabase();
			return true;
		}

		return false; // should never happen
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Renderer.playlistName = playlist;
		updateSongList(listOfSongs);
		
		Intent displayPlaybackControls = new Intent(this,
				DisplayPlaybackControls.class);
		displayPlaybackControls.putExtra("position", position);
//		displayPlaybackControls.putExtra("playlistName", playlist);
		displayPlaybackControls.putExtra("screen", "DisplayPlaylistMelodies");
		displayPlaybackControls
				.putExtra("swipeBack", "DisplayPlaylistMelodies");
//		displayPlaybackControls.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(displayPlaybackControls);
	}

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			Log.v("DisplayPlaylistMelodies: ", "onServiceConnected");
			mpInterface = (MDSInterface) service;
		}

		public void onServiceDisconnected(ComponentName className) {
			mpInterface = null;
		}
	};

	public void updateSongList(List<SongItem> listOfSongs) {

		mpInterface.clearPlaylist();
		for (SongItem file : listOfSongs) {
			mpInterface.addSongPlaylist(file.getSongAbsolutePath());
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		Log.v("DisplayPlaylistMelodies: ", "Entering onNewIntent");

		playlist = intent.getStringExtra("playlistName");

		Log.v("DisplayPlaylistMelodies: ", "Playlist: " + playlist);

		PlaylistDataHelper playlistDataHelper = new PlaylistDataHelper(this);
		List<Integer> melodyIds = new ArrayList<Integer>();

		if ("On The Go".equals(playlist)) {
			List<Long> onTheGoPlayList = DisplaySearchCriteriaValues
					.getOnTheGoMelodyList();
			for (int i = 0; i < onTheGoPlayList.size(); i++) {
				melodyIds.add(onTheGoPlayList.get(i).intValue());
			}
		} else {
			melodyIds = playlistDataHelper.retrieveAllMelodies(playlist);
		}

		listOfSongs = playlistDataHelper.retrieveAllMelodyInfo(melodyIds);
		// Collections.sort(listOfSongs);

		SongItemAdapter adapter = new SongItemAdapter(this, R.layout.song_row,
				listOfSongs);
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

		Intent serviceIntent = new Intent();
		serviceIntent.setAction("com.droid.melodydroid.core.MDService");
		startService(serviceIntent);

		this.bindService(new Intent(this, MDService.class), mConnection,
				Context.BIND_AUTO_CREATE);
		
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
