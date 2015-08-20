package com.droid.melodydroid.display;

import java.util.ArrayList;

import com.droid.melodydroid.R;

import com.droid.melodydroid.core.DatabaseSynchronizationService;
import com.droid.melodydroid.criteria.SearchCriteria;
import com.droid.melodydroid.helper.MelodyDroidHelper;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

public class DisplaySearchCriteriaNames extends ListActivity {

	private static final String KEY_FIRST_RUN = "firstTime";
	private static final int ITEM_ID_1 = 1;
	private static final int ITEM_ID_2 = 2;
	private static final int ITEM_ID_3 = 3;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	View.OnTouchListener gestureListener;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

//		SharedPreferences prefs = getPreferences(MODE_PRIVATE);
//		if (!prefs.contains(KEY_FIRST_RUN)) {
//		    /* do some one-off stuff here */
//		    prefs.edit().putBoolean(KEY_FIRST_RUN, false).commit();
//		}		

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		if(!prefs.getBoolean(KEY_FIRST_RUN, false)) {
		    // run your one time code
			DatabaseSynchronizationService dbSyncService = DatabaseSynchronizationService
				.getInstance(this);
			dbSyncService.findMelodiesFromSourceAndSynchDatabase();			
			
		    SharedPreferences.Editor editor = prefs.edit();
		    editor.putBoolean(KEY_FIRST_RUN, true);
		    editor.commit();
		}
		
		
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
								DisplaySearchCriteriaNames.this,
								DisplayPlaybackControls.class);
						displayPlaybackControls.putExtra("swipeBack",
								"DisplaySearchCriteriaNames");
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
					.getInstance(DisplaySearchCriteriaNames.this);
			dbSyncService.findMelodiesFromSourceAndSynchDatabase();
			return true;
		}

		return false; // should never happen
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if (!MelodyDroidHelper.isSdPresent()) {
			Intent sdCardError = new Intent(this, SdCardError.class);
			startActivity(sdCardError);
		} else {
			String selectedOption = ((CriteriaItem) l
					.getItemAtPosition(position)).getCriteriaName();
			SearchCriteria.setLastSelectedFilterName(selectedOption);
			Intent displaySearchCriteriaValues = new Intent(this,
					DisplaySearchCriteriaValues.class);
			startActivity(displaySearchCriteriaValues);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		ArrayList<CriteriaItem> listOfCriteria = new ArrayList<CriteriaItem>();
		listOfCriteria
				.add(new CriteriaItem("Album", SearchCriteria.getAlbum()));
		listOfCriteria.add(new CriteriaItem("Artist", SearchCriteria
				.getArtist()));
		listOfCriteria
				.add(new CriteriaItem("Genre", SearchCriteria.getGenre()));
		listOfCriteria
				.add(new CriteriaItem("Title", SearchCriteria.getTitle()));
		listOfCriteria.add(new CriteriaItem("Year", SearchCriteria.getYear()));

		CriteriaItemAdapter adapter = new CriteriaItemAdapter(this,
				R.layout.criteria_row, listOfCriteria);

		setListAdapter(adapter);

		LinearLayout myLinearLayout = (LinearLayout) findViewById(R.id.myDynamicLayout);
		myLinearLayout.removeAllViews();

		if (MelodyDroidHelper.isSearchOptionSelected()) {
			Button clearSearch = new Button(this);
			clearSearch.setText("Clear Search");
//			clearSearch.setMaxWidth(90);
//			clearSearch.setMaxHeight(55);
			clearSearch.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			clearSearch.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);			
			myLinearLayout.addView(clearSearch);

			clearSearch.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					SearchCriteria.clearAll();
					Intent displaySearchCriteriaNames = new Intent(
							DisplaySearchCriteriaNames.this,
							DisplaySearchCriteriaNames.class);
					startActivity(displaySearchCriteriaNames);
				}
			});

			clearSearch.setOnTouchListener(gestureListener);
		}

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

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// ignore orientation change
		super.onConfigurationChanged(newConfig);
	}
}
