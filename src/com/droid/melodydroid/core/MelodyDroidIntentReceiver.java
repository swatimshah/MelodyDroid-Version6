package com.droid.melodydroid.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MelodyDroidIntentReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		DatabaseSynchronizationService dbSyncService = DatabaseSynchronizationService
				.getInstance(context);
		dbSyncService.findMelodiesFromSourceAndSynchDatabase();
	}

}
