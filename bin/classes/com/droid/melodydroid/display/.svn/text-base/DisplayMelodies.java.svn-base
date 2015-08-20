package com.droid.melodydroid.display;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.droid.melodydroid.R;
import com.droid.melodydroid.core.DatabaseSynchronizationService;
import com.droid.melodydroid.core.MDSInterface;
import com.droid.melodydroid.core.MDService;
import com.droid.melodydroid.criteria.SearchCriteria;
import com.droid.melodydroid.data.PlaylistDataHelper;
import com.droid.melodydroid.helper.CaseInsensitiveString;
import com.droid.melodydroid.helper.MelodyDroidHelper;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

public class DisplayMelodies extends ListActivity {

	private List<SongItem> listOfSongs = new ArrayList<SongItem>();
	private static final int ITEM_ID_1 = 1;
	private static final int ITEM_ID_2 = 2;
	private static final int CONTEXT_MENU_ITEM_ID_0 = 0;
	private static final int ITEM_ID_3 = 3;
	private static final int CONTEXT_MENU_ITEM_ID_1 = 1;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;
	private MDSInterface mpInterface;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_melodies);

		final Intent queryIntent = getIntent();
		onNewIntent(queryIntent);
	}

	private ServiceConnection mConnection = new ServiceConnection() {
		public void onServiceConnected(ComponentName className, IBinder service) {
			Log.v("DisplayMelodies: ", "onServiceConnected");
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
								DisplayMelodies.this,
								DisplayPlaybackControls.class);
						displayPlaybackControls.putExtra("swipeBack",
								"DisplayMelodies");
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
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == android.R.id.list) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			String selectedSong = listOfSongs.get(info.position).getSongName();
			menu.setHeaderTitle("Add To Playlist: " + selectedSong);
			menu.add(0, CONTEXT_MENU_ITEM_ID_0, CONTEXT_MENU_ITEM_ID_0,
					R.string.onthegoplaylist);
			menu.add(0, CONTEXT_MENU_ITEM_ID_1, CONTEXT_MENU_ITEM_ID_1,
					R.string.newplaylist);
			PlaylistDataHelper playlistDataHelper = new PlaylistDataHelper(this);
			Set<CaseInsensitiveString> playLists = playlistDataHelper
					.retrieveAllPlayLists();
			Iterator<CaseInsensitiveString> playlistIterator = playLists
					.iterator();
			int i = 0;
			while (playlistIterator.hasNext()) {
				menu.add(0, CONTEXT_MENU_ITEM_ID_1 + i + 1,
						CONTEXT_MENU_ITEM_ID_1 + i + 1, playlistIterator.next()
								.toString());
				++i;
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		List<Long> melodyId = new ArrayList<Long>();
		AdapterView.AdapterContextMenuInfo info = null;
		info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		melodyId.add(listOfSongs.get(info.position).getSongId());

		switch (item.getItemId()) {
		case CONTEXT_MENU_ITEM_ID_0:
			DisplaySearchCriteriaValues.getOnTheGoMelodyList().addAll(melodyId);
			return true;
		case CONTEXT_MENU_ITEM_ID_1:
			displayInputDialog(melodyId);
			return true;
		default:
			PlaylistDataHelper playlistDataHelper = new PlaylistDataHelper(
					DisplayMelodies.this);
			playlistDataHelper.insert((String) item.getTitle(), melodyId);
			return super.onContextItemSelected(item);
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
					.getInstance(DisplayMelodies.this);
			dbSyncService.findMelodiesFromSourceAndSynchDatabase();
			return true;
		}
		return false; // should never happen
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		List<Long> melodyId = new ArrayList<Long>();
		melodyId.add(listOfSongs.get(position).getSongId());
		DisplaySearchCriteriaValues.getOnTheGoMelodyList().addAll(melodyId);

		Renderer.playlistName = "Search Results...";
		updateSongList(listOfSongs);
		
		Intent displayPlaybackControls = new Intent(this,
				DisplayPlaybackControls.class);
		displayPlaybackControls.putExtra("position", position);
		displayPlaybackControls.putExtra("screen", "DisplayMelodies");
		displayPlaybackControls.putExtra("swipeBack", "DisplayMelodies");
//		displayPlaybackControls.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(displayPlaybackControls);
	}

	private void displayInputDialog(final List<Long> melodyId) {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Playlist Name");

		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String playlistName = input.getText().toString();
				PlaylistDataHelper playlistDataHelper = new PlaylistDataHelper(
						DisplayMelodies.this);
				playlistDataHelper.insert(playlistName, melodyId);
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});

		alert.show();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		listOfSongs = SearchCriteria.getResults(this);

		SongItemAdapter adapter = new SongItemAdapter(this, R.layout.song_row,
				listOfSongs);
		setListAdapter(adapter);
		ListView listView = getListView();
		ColorDrawable sage = new ColorDrawable(this.getResources().getColor(R.drawable.sage));		
		listView.setDivider(sage);
		listView.setDividerHeight(1);				

		Intent serviceIntent = new Intent();
		serviceIntent.setAction("com.droid.melodydroid.core.MDService");
		startService(serviceIntent);

		this.bindService(new Intent(this, MDService.class), mConnection,
				Context.BIND_AUTO_CREATE);

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

		LinearLayout myLinearLayout = (LinearLayout) findViewById(R.id.myDynamicLayout);

		myLinearLayout.removeAllViews();

		Button modifySearch = new Button(this);
		modifySearch.setText("Modify Search");
//		modifySearch.setMaxWidth(90);
//		modifySearch.setMaxHeight(55);
		modifySearch.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		modifySearch.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
		myLinearLayout.addView(modifySearch);

		modifySearch.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SearchCriteria.setupSearchCriteria();
				Intent displaySearchCriteriaNames = new Intent(
						DisplayMelodies.this, DisplaySearchCriteriaNames.class);
				startActivity(displaySearchCriteriaNames);
			}
		});
		modifySearch.setOnTouchListener(gestureListener);

		if (MelodyDroidHelper.isSearchOptionSelected()) {
			Button newSearch = new Button(this);
			newSearch.setText("New Search");
//			newSearch.setMaxWidth(90);
//			newSearch.setMaxHeight(55);
			newSearch.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));			
			newSearch.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
			myLinearLayout.addView(newSearch);

			newSearch.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					SearchCriteria.clearAll();
					Intent displaySearchCriteriaNames = new Intent(
							DisplayMelodies.this,
							DisplaySearchCriteriaNames.class);
					startActivity(displaySearchCriteriaNames);
				}
			});

			newSearch.setOnTouchListener(gestureListener);
		}
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
