package com.droid.melodydroid.core;

import android.content.Context;
import android.util.Log;

import com.droid.melodydroid.helper.MelodyDroidHelper;

public class DatabaseSynchronizationService {

	private Context context;
	private static DatabaseSynchronizationService instanceDatabaseSynchronizationService = null;

	private DatabaseSynchronizationService(Context context) {
		this.context = context;
	}

	public static DatabaseSynchronizationService getInstance(Context context) {
		if (instanceDatabaseSynchronizationService == null) {
			instanceDatabaseSynchronizationService = new DatabaseSynchronizationService(
					context);
		}
		return instanceDatabaseSynchronizationService;
	}

	public void findMelodiesFromSourceAndSynchDatabase() {
		if (!MelodyDroidHelper.isSdPresent()) {
			return;
		}
		Log.v("DatabaseSynchronizationService: Start synchronizing db: ", ""
				+ MelodyDroidHelper.now());

		DatabaseSynchronizationThread databaseSynchronizationThread = new DatabaseSynchronizationThread(
				context);
		Thread thread = new Thread(databaseSynchronizationThread);
		thread.start();
		Log.v("DatabaseSynchronizationService: End synchronizing db: ", ""
				+ MelodyDroidHelper.now());

	}

}
