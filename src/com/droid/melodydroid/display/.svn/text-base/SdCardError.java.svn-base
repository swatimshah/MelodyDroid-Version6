package com.droid.melodydroid.display;

import com.droid.melodydroid.R;
import com.droid.melodydroid.core.DatabaseSynchronizationService;
import com.droid.melodydroid.criteria.SearchCriteria;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class SdCardError extends Activity {

	private static final int ITEM_ID_0 = 0;
	private static final int ITEM_ID_1 = 1;
	private static final int ITEM_ID_2 = 2;
	private static final int ITEM_ID_3 = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.error_pg);

		setTitle(R.string.error_page_title);

		TextView error_message = (TextView) findViewById(R.id.error_message);
		error_message
				.setText("SD card not found. Please insert the SD card.");
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, ITEM_ID_0, 0, R.string.home).setIcon(R.drawable.melodica);
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

		case ITEM_ID_0:
			SearchCriteria.clearAll();
			Intent displaySearchCriteriaNames = new Intent(this,
					DisplaySearchCriteriaNames.class);
			startActivity(displaySearchCriteriaNames);
			return true;
		case ITEM_ID_1:
			onSearchRequested();
			return true;
		case ITEM_ID_2:
			Intent displayPlaylists = new Intent(this, DisplayPlaylists.class);
			startActivity(displayPlaylists);
			return true;
		case ITEM_ID_3:
			DatabaseSynchronizationService dbSyncService = DatabaseSynchronizationService
					.getInstance(SdCardError.this);
			dbSyncService.findMelodiesFromSourceAndSynchDatabase();
			return true;
		}

		return false; // should never happen
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// ignore orientation change
		super.onConfigurationChanged(newConfig);
	}
}
