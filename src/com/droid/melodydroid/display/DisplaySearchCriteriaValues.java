package com.droid.melodydroid.display;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.droid.melodydroid.R;

import com.droid.melodydroid.core.DatabaseSynchronizationService;
import com.droid.melodydroid.criteria.SearchCriteria;
import com.droid.melodydroid.data.PlaylistDataHelper;
import com.droid.melodydroid.helper.CaseInsensitiveString;
import com.droid.melodydroid.helper.MelodyDroidHelper;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.EditText;
import android.widget.ListView;

public class DisplaySearchCriteriaValues extends ListActivity {

	List<ValueItem> listOfValueItems = new ArrayList<ValueItem>();
	private static final int ITEM_ID_1 = 1;
	private static final int ITEM_ID_2 = 2;
	private static final int ITEM_ID_3 = 3;
	private static final int CONTEXT_MENU_ITEM_ID_0 = 0;
	private static final int CONTEXT_MENU_ITEM_ID_1 = 1;
	private static List<Long> onTheGoMelodyList = new ArrayList<Long>();
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_filter_values);

		setTitle(getCustomTitle());

		listOfValueItems = SearchCriteria.getResultsWithOccurrenceCount(this);

		ValueItemAdapter adapter = new ValueItemAdapter(this,
				R.layout.value_row, listOfValueItems);
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
								DisplaySearchCriteriaValues.this,
								DisplayPlaybackControls.class);
						displayPlaybackControls.putExtra("swipeBack",
								"DisplaySearchCriteriaValues");
						startActivity(displayPlaybackControls);
					}
				}
			} catch (Exception e) {
				// nothing
			}
			return false;
		}
	}

	private CharSequence getCustomTitle() {
		if ("Album".equals(SearchCriteria.getLastSelectedFilterName()))
			return "Album List";
		if ("Artist".equals(SearchCriteria.getLastSelectedFilterName()))
			return "Artist List";
		if ("Genre".equals(SearchCriteria.getLastSelectedFilterName()))
			return "Genre List";
		if ("Title".equals(SearchCriteria.getLastSelectedFilterName()))
			return "Title List";
		if ("Year".equals(SearchCriteria.getLastSelectedFilterName()))
			return "Year List";

		return "Values For Search Option";
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == android.R.id.list) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			String selectedCriteriaValue = MelodyDroidHelper
					.extractFilterCriteria(listOfValueItems.get(info.position)
							.getValue());
			menu.setHeaderTitle("Add To Playlist: " + selectedCriteriaValue);
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

		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		String filterValue = MelodyDroidHelper
				.extractFilterCriteria(listOfValueItems.get(info.position)
						.getValue());
		String filterName = SearchCriteria.getLastSelectedFilterName();
		List<Long> melodyIds = SearchCriteria
				.getResultsWithMelodyIdsForPlaylists(filterName, filterValue,
						this);

		switch (item.getItemId()) {
		case CONTEXT_MENU_ITEM_ID_0:
			getOnTheGoMelodyList().addAll(melodyIds);
			return true;
		case CONTEXT_MENU_ITEM_ID_1:
			displayInputDialog(melodyIds);
			return true;
		default:
			PlaylistDataHelper playlistDataHelper = new PlaylistDataHelper(this);
			playlistDataHelper.insert((String) item.getTitle(), melodyIds);
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
					.getInstance(DisplaySearchCriteriaValues.this);
			dbSyncService.findMelodiesFromSourceAndSynchDatabase();
			return true;
		}
		return false; // should never happen
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		ValueItem lastSelectedFilterValue = (ValueItem) l
				.getItemAtPosition(position);
		SearchCriteria.setLastSelectedFilterValue(MelodyDroidHelper
				.extractFilterCriteria(lastSelectedFilterValue.getValue()));
		Intent displayMelodies = new Intent(this, DisplayMelodies.class);
		startActivity(displayMelodies);
	}

	private void displayInputDialog(final List<Long> melodyIds) {

		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Playlist Name");

		final EditText input = new EditText(this);
		alert.setView(input);

		alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String playlistName = input.getText().toString();
				PlaylistDataHelper playlistDataHelper = new PlaylistDataHelper(
						DisplaySearchCriteriaValues.this);
				playlistDataHelper.insert(playlistName, melodyIds);
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

	public static List<Long> getOnTheGoMelodyList() {
		return onTheGoMelodyList;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		setTitle(getCustomTitle());

		listOfValueItems = SearchCriteria.getResultsWithOccurrenceCount(this);

		ValueItemAdapter adapter = new ValueItemAdapter(this,
				R.layout.value_row, listOfValueItems);
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
		registerForContextMenu(getListView());

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// ignore orientation change
		super.onConfigurationChanged(newConfig);
	}
}
