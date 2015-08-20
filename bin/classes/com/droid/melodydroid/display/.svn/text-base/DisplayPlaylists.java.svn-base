package com.droid.melodydroid.display;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.droid.melodydroid.R;
import com.droid.melodydroid.core.DatabaseSynchronizationService;
import com.droid.melodydroid.data.PlaylistDataHelper;
import com.droid.melodydroid.helper.CaseInsensitiveString;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.AdapterView;
import android.widget.ListView;

public class DisplayPlaylists extends ListActivity {

	private static final int ITEM_ID_1 = 1;
	private static final int ITEM_ID_3 = 3;
	private static final int CONTEXT_MENU_ITEM_ID_0 = 0;

	private List<PlaylistItem> listOfPlaylists = null;

	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playlists);

		final Intent queryIntent = getIntent();
		onNewIntent(queryIntent);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String playlist = ((PlaylistItem) l.getItemAtPosition(position))
				.getPlaylistName();
		Intent displayPlaylistMelodies = new Intent(this,
				DisplayPlaylistMelodies.class);
		displayPlaylistMelodies.putExtra("playlistName", playlist);
		startActivity(displayPlaylistMelodies);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == android.R.id.list) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			String selectedPlaylistName = listOfPlaylists.get(info.position)
					.getPlaylistName();
			if ("On The Go".equals(selectedPlaylistName))
				menu.add(0, CONTEXT_MENU_ITEM_ID_0, CONTEXT_MENU_ITEM_ID_0,
						"Empty: " + selectedPlaylistName);
			else
				menu.add(0, CONTEXT_MENU_ITEM_ID_0, CONTEXT_MENU_ITEM_ID_0,
						"Delete: " + selectedPlaylistName);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		String selectedPlaylistName = listOfPlaylists.get(info.position)
				.getPlaylistName();
		switch (item.getItemId()) {
		case CONTEXT_MENU_ITEM_ID_0:
			if ("On The Go".equals(selectedPlaylistName)) {
				DisplaySearchCriteriaValues.getOnTheGoMelodyList().clear();
			} else {
				PlaylistDataHelper playlistDataHelper = new PlaylistDataHelper(
						this);
				playlistDataHelper.removePlaylist(selectedPlaylistName);
				Intent displayPlaylists = new Intent(this,
						DisplayPlaylists.class);
				startActivity(displayPlaylists);
			}
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, ITEM_ID_1, 1, R.string.search).setIcon(
				R.drawable.ic_menu_search);
		menu.add(0, ITEM_ID_3, 3, R.string.dbsync).setIcon(R.drawable.syncicon);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case ITEM_ID_1:
			onSearchRequested();
			return true;
		case ITEM_ID_3:
			DatabaseSynchronizationService dbSyncService = DatabaseSynchronizationService
					.getInstance(DisplayPlaylists.this);
			dbSyncService.findMelodiesFromSourceAndSynchDatabase();
			return true;
		}

		return false; // should never happen
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		listOfPlaylists = new ArrayList<PlaylistItem>();
		listOfPlaylists.add(new PlaylistItem("On The Go"));

		PlaylistDataHelper playlistDataHelper = new PlaylistDataHelper(this);
		Set<CaseInsensitiveString> setOfPlaylists = playlistDataHelper
				.retrieveAllPlayLists();

		Iterator<CaseInsensitiveString> playlistIterator = setOfPlaylists
				.iterator();
		while (playlistIterator.hasNext()) {
			listOfPlaylists.add(new PlaylistItem(playlistIterator.next()
					.toString()));
		}

		PlaylistItemAdapter adapter = new PlaylistItemAdapter(this,
				R.layout.playlist_row, listOfPlaylists);
		setListAdapter(adapter);
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
		registerForContextMenu(getListView());

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
								DisplayPlaylists.this,
								DisplayPlaybackControls.class);
						displayPlaybackControls.putExtra("swipeBack",
								"DisplayPlaylists");
						startActivity(displayPlaybackControls);
					}
				}
			} catch (Exception e) {
				// nothing
			}
			return false;
		}
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// ignore orientation change
		super.onConfigurationChanged(newConfig);
	}
}
