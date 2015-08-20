package com.droid.melodydroid.core;

import com.droid.melodydroid.helper.MelodyDroidHelper;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class DBSyncService extends Service {

	private DBSyncServiceBinder dbSyncServiceBinder = new DBSyncServiceBinder();
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return dbSyncServiceBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private class DBSyncServiceBinder extends Binder implements DBSyncInterface {

		@Override
		public void findMelodiesFromSourceAndSynchDatabase() {
			if (!MelodyDroidHelper.isSdPresent()) {
				return;
			}
			Log.v("DatabaseSynchronizationService: Start synchronizing db: ", ""
					+ MelodyDroidHelper.now());

			DatabaseSynchronizationThread databaseSynchronizationThread = new DatabaseSynchronizationThread(
					DBSyncService.this);
			Thread thread = new Thread(databaseSynchronizationThread);
			thread.start();
			Log.v("DatabaseSynchronizationService: End synchronizing db: ", ""
					+ MelodyDroidHelper.now());			
		}
	
	}
}
