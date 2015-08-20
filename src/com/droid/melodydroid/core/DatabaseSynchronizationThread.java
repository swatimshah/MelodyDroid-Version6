package com.droid.melodydroid.core;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.cmc.music.common.ID3ReadException;
import org.cmc.music.metadata.MusicMetadataSet;
import org.cmc.music.myid3.MyID3;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Environment;
import android.util.Log;

import com.droid.melodydroid.R;
import com.droid.melodydroid.data.DataHelper;
import com.droid.melodydroid.data.PlaylistDataHelper;
import com.droid.melodydroid.display.DisplaySearchCriteriaNames;
import com.droid.melodydroid.helper.MelodyDroidHelper;

public class DatabaseSynchronizationThread implements Runnable {

	private static final int START_DB_UPDATE = 0;
	private static final int END_DB_UPDATE = 1;
	private static final int DB_STATUS = 2;
	private Context context;
	private static Object syncObject = new Object();

	public DatabaseSynchronizationThread(Context context) {
		this.context = context;
	}

	public void run() {
		Log.v("DatabaseSynchronizationThread: Entering Run ...............", ""
				+ MelodyDroidHelper.now());
		synchBlock();
		Log.v("DatabaseSynchronizationThread: Ending Run ...............", ""
				+ MelodyDroidHelper.now());
	}

	private void synchBlock() {
		synchronized (syncObject) {
			Log.v("DatabaseSynchronizationThread: Entering Block...............",
					"" + MelodyDroidHelper.now());
			NotificationManager nm = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
			displayNotificationStartDBUpdate(nm);

			Map<String, File> deviceAllMelodiesMap = new HashMap<String, File>();
			Set<String> deletedMelodies = new HashSet<String>();
			Set<String> newMelodies = new HashSet<String>();
			Set<String> existingMelodies = new HashSet<String>();
			DataHelper dataHelper = new DataHelper(context);

			try {
				existingMelodies = dataHelper.getCurrentMelodies();
			} catch (SQLiteException sqle) {
				Log.v("DatabaseSynchronizationService: SQLiteException: ",
						sqle.getMessage());
				if (sqle.getMessage().indexOf("no such table") != -1)
					dataHelper.createTable();
			}

			deviceAllMelodiesMap = synchMelodies(deletedMelodies, newMelodies,
					existingMelodies);

			if (deletedMelodies != null && deletedMelodies.size() > 0) {
				removeDeletedMelodies(deletedMelodies);
			}
			if (newMelodies != null && newMelodies.size() > 0) {
				addNewMelodies(deviceAllMelodiesMap, newMelodies);
			}

			cancelNotification(nm);
			int totalNumberOfMelodies = dataHelper.getNumberOfMelodies();

			displayNotificationEndDBUpdate(nm, newMelodies.size(),
					deletedMelodies.size(), totalNumberOfMelodies);

			// displayNotificationDatabaseStatus(nm, totalNumberOfMelodies);

			Log.v("DatabaseSynchronizationThread: ",
					"Exiting Block..............." + MelodyDroidHelper.now());
		}
	}

	private void cancelNotification(NotificationManager nm) {
		nm.cancel(START_DB_UPDATE);
	}

	private void displayNotificationStartDBUpdate(NotificationManager nm) {
		Notification notification = new Notification(R.drawable.syncicon,
				"Refresh " + GlobalConstants.APPLICATION_NAME,
				System.currentTimeMillis());
		Intent intent = new Intent(context, DisplaySearchCriteriaNames.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				intent, 0);
		notification.setLatestEventInfo(context, "Refresh "
				+ GlobalConstants.APPLICATION_NAME,
				"The database refresh is in process.", contentIntent);
		nm.notify(START_DB_UPDATE, notification);
	}

	private NotificationManager displayNotificationEndDBUpdate(
			NotificationManager nm, int newMelodiesCount,
			int deletedMelodiesCount, int totalMelodiesCount) {
		Notification notification = new Notification(R.drawable.syncicon,
				GlobalConstants.APPLICATION_NAME + " Refresh Completed",
				System.currentTimeMillis());
		Intent intent = new Intent(context, DisplaySearchCriteriaNames.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				intent, 0);

		String notificationMessage = "";

		if (newMelodiesCount != 0 || deletedMelodiesCount != 0) {
			notificationMessage = "Found " + newMelodiesCount + " new, "
					+ deletedMelodiesCount + " erased melodies. "
					+ "Found total " + totalMelodiesCount + " melodies.";
		} else {
			notificationMessage = "Found total " + totalMelodiesCount
					+ " melodies.";
		}

		notification.setLatestEventInfo(context,
				GlobalConstants.APPLICATION_NAME + " Refresh Completed",
				notificationMessage, contentIntent);
		nm.notify(END_DB_UPDATE, notification);
		return nm;
	}

	// private NotificationManager displayNotificationDatabaseStatus(
	// NotificationManager nm, int totalMelodiesCount) {
	// Notification notification = new Notification(R.drawable.syncicon,
	// GlobalConstants.APPLICATION_NAME + " Database Status", System
	// .currentTimeMillis());
	// Intent intent = new Intent(context, DisplaySearchCriteriaNames.class);
	// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	// PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
	// intent, 0);
	// notification.setLatestEventInfo(context, GlobalConstants.APPLICATION_NAME
	// + " Database Status", "Found total " + totalMelodiesCount
	// + " melodies.", contentIntent);
	// nm.notify(DB_STATUS, notification);
	// return nm;
	// }

	private Map<String, File> synchMelodies(Set<String> deletedMelodies,
			Set<String> newMelodies, Set<String> existingMelodies) {

		Map<String, File> deviceAllMelodiesMap;
		File home = null;

		Log.v("SDK Version: ", "" + android.os.Build.VERSION.SDK_INT);

		// if(android.os.Build.VERSION.SDK_INT < 8) {
		// home = new File(GlobalConstants.MEDIA_PATH);
		// }
		// else {
		// home = new File(GlobalConstants.MEDIA_PATH_2_2);
		// }

		String externalUrl = Environment.getExternalStorageDirectory()
				.getPath();

		Log.v("The external storage directory is: ", externalUrl);

		if (android.os.Build.DEVICE.contains("Samsung")
				|| android.os.Build.MANUFACTURER.contains("Samsung")
				|| android.os.Build.DEVICE.contains("samsung")
				|| android.os.Build.MANUFACTURER.contains("samsung")
				|| android.os.Build.MANUFACTURER.contains("YU")
				|| android.os.Build.MANUFACTURER.contains("Motorola")
				|| android.os.Build.MANUFACTURER.contains("motorola")) {
			externalUrl = "/storage/";
		}

		home = new File(externalUrl);

		Log.v("The extended external storage directory is: ", externalUrl);

		Mp3Filter mp3Filter = new Mp3Filter();
		Set<String> deviceAllMelodiesSet = new HashSet<String>();
		Map<String, File> filesMap = listFiles(home, mp3Filter);

		deviceAllMelodiesMap = filesMap;
		deviceAllMelodiesSet.addAll(deviceAllMelodiesMap.keySet());

		deletedMelodies.addAll(existingMelodies);
		deletedMelodies.removeAll(deviceAllMelodiesSet);

		newMelodies.addAll(deviceAllMelodiesSet);
		newMelodies.removeAll(existingMelodies);
		return deviceAllMelodiesMap;
	}

	private Map<String, File> listFiles(File home, Mp3Filter mp3Filter) {
		Map<String, File> filesMap = listFilesRecurse(home, mp3Filter, true);
		return filesMap;
	}

	private void addNewMelodies(Map<String, File> deviceAllMelodiesMap,
			Set<String> newMelodies) {
		Iterator<String> newMelodiesIterator = newMelodies.iterator();
		while (newMelodiesIterator.hasNext()) {
			populateMelody(deviceAllMelodiesMap, newMelodiesIterator);
		}
		// melodies.insert(dataHelper);
	}

	private void populateMelody(Map<String, File> deviceAllMelodiesMap,
			Iterator<String> newMelodiesIterator) {
		Melody melody = null;
		DataHelper dataHelper = new DataHelper(context);
		File melodyFile = (File) deviceAllMelodiesMap
				.get(((String) newMelodiesIterator.next()));

		MusicMetadataSet musicMetaDataSet = null;
		try {
			musicMetaDataSet = new MyID3().read(melodyFile); // read
			// metadata
		} catch (IOException ioe) {
			Log.v("DatabaseSynchronizationThread: IOException: ",
					ioe.getMessage());
		} catch (ID3ReadException e) {
			e.printStackTrace();
		}
		if (musicMetaDataSet == null) // perhaps no metadata
			Log.v("DatabaseSynchronizationThread: ",
					"File does not have any metadata");
		else {
			melody = MelodyDroidHelper.extractMp3MetaData(musicMetaDataSet);
			melody.setFileName(melodyFile.getAbsolutePath());
			melody.insert(dataHelper);
			// melodies.setMelodies(melody);
		}
	}

	private void removeDeletedMelodies(Set<String> deletedMelodies) {
		DataHelper dataHelper = new DataHelper(context);
		PlaylistDataHelper playlistDataHelper = new PlaylistDataHelper(context);
		Iterator<String> deletedMelodiesIterator = deletedMelodies.iterator();
		while (deletedMelodiesIterator.hasNext()) {
			String fileAbsolutePath = (String) deletedMelodiesIterator.next();
			playlistDataHelper.deleteMelodyIdFromPlaylists(dataHelper
					.getMelodyId(fileAbsolutePath));
			dataHelper.delete(fileAbsolutePath);
		}
	}

	private Map<String, File> listFilesRecurse(File dir, FilenameFilter filter,
			boolean recurse) {
		HashMap<String, File> files = new HashMap<String, File>();
		File[] entries = dir.listFiles();

		if (entries != null) {
			for (File entry : entries) {
				if (filter == null || filter.accept(dir, entry.getName()))
					files.put(entry.getAbsolutePath(), entry);
				if (recurse && entry.isDirectory())
					files.putAll(listFilesRecurse(entry, filter, recurse));
			}
		}
		return files;

	}
}
