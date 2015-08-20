package com.droid.melodydroid.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.droid.melodydroid.display.SongInfo;
import com.droid.melodydroid.display.SongItem;
import com.droid.melodydroid.display.ValueItem;
import com.droid.melodydroid.helper.CaseInsensitiveString;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DataHelper {

	private static final String TAG_UNDEFINED = "Undefined";
	private static final String DATABASE_NAME = "melodydroid.db";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "MELODYDROID";
	protected static final String TABLE_NAME_1 = "DROIDPLAYLISTS";

	private static final String INSERT = "insert into "
			+ TABLE_NAME
			+ "(singer, album, genre, year, title, fileName, albumArt) values (?, ?, ?, ?, ?, ?, ?)";

	private static final String DELETE = "delete from " + TABLE_NAME
			+ " where fileName = ?";

	private Context context;
	private static SQLiteDatabase db;
	private OpenHelper openHelper;

	public DataHelper(Context context) {
		this.context = context;
		this.openHelper = new OpenHelper(this.context);
		if (db == null)
			db = openHelper.getWritableDatabase();
	}

	public void createTable() {
		openHelper.onCreate(db);
	}

	public SQLiteDatabase openDatabase() {
		return db;
	}

	public void closeDatabase() {
		db.close();
	}

	public long insert(String singer, String album, String genre, String year,
			String title, String fileName, byte[] albumArt) {
		SQLiteStatement insertStmt = openDatabase().compileStatement(INSERT);
		;
		insertStmt.bindString(1, checkAndReturnNotNull(singer));
		insertStmt.bindString(2, checkAndReturnNotNull(album));
		insertStmt.bindString(3, checkAndReturnNotNull(genre));
		insertStmt.bindString(4, checkAndReturnNotNull(year));
		insertStmt.bindString(5, checkAndReturnNotNull(title));
		insertStmt.bindString(6, checkAndReturnNotNull(fileName));
		insertStmt.bindBlob(7, checkAndReturnNotNull(albumArt));
		long result = insertStmt.executeInsert();
		insertStmt.close();
		return result;
	}

	private String checkAndReturnNotNull(String stringValue) {
		if (stringValue == null)
			return "";
		return stringValue;
	}

	private byte[] checkAndReturnNotNull(byte[] byteArrayValue) {
		if (byteArrayValue == null)
			return new byte[0];
		return byteArrayValue;
	}

	public void delete(String fileName) {

		SQLiteStatement deleteStmt = db.compileStatement(DELETE);
		;
		deleteStmt.bindString(1, fileName);
		deleteStmt.execute();

	}

	public List<SongItem> execute(String query) {

		List<SongItem> results = new ArrayList<SongItem>();
		TreeSet<CaseInsensitiveString> resultsKeySet = new TreeSet<CaseInsensitiveString>();
		Map<CaseInsensitiveString, SongItem> resultsMap = new HashMap<CaseInsensitiveString, SongItem>();
		Cursor cursor = db.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			do {
				if (!cursor.getString(0).equalsIgnoreCase(TAG_UNDEFINED))
					resultsMap.put(
							new CaseInsensitiveString(cursor.getString(0)),
							new SongItem(cursor.getString(0), cursor
									.getString(1), cursor.getLong(2)));
				else
					resultsMap.put(
							new CaseInsensitiveString(cursor.getString(1)
									.substring(
											cursor.getString(1)
													.lastIndexOf("/") + 1)),
							new SongItem(cursor.getString(1).substring(
									cursor.getString(1).lastIndexOf("/") + 1),
									cursor.getString(1), cursor.getLong(2)));

			} while (cursor.moveToNext());
		}
		cursor.close();

		resultsKeySet.addAll(resultsMap.keySet());
		Iterator<CaseInsensitiveString> resultsKeyIterator = resultsKeySet
				.iterator();
		CaseInsensitiveString resultsKey;
		while (resultsKeyIterator.hasNext()) {
			resultsKey = resultsKeyIterator.next();
			results.add(resultsMap.get(resultsKey));
		}

		return results;
	}

	public List<ValueItem> executeAndGetResultsWithOccurrenceCount(String query) {

		Map<CaseInsensitiveString, Integer> resultsMap = new HashMap<CaseInsensitiveString, Integer>();
		Set<CaseInsensitiveString> resultsKeySet = new TreeSet<CaseInsensitiveString>();

		Cursor cursor = db.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			CaseInsensitiveString key;
			int value;
			do {
				key = new CaseInsensitiveString(cursor.getString(0));
				if (resultsMap.containsKey(key)) {
					value = ((Integer) resultsMap.get(key)).intValue()
							+ cursor.getInt(1);
					resultsMap.put(key, new Integer(value));
				} else {
					resultsMap.put(key, new Integer(cursor.getInt(1)));
				}
			} while (cursor.moveToNext());
		}

		resultsKeySet = new TreeSet<CaseInsensitiveString>(
				(Set<CaseInsensitiveString>) resultsMap.keySet());
		List<ValueItem> resultsList = new ArrayList<ValueItem>();

		Iterator<CaseInsensitiveString> resultsKeyIterator = resultsKeySet
				.iterator();
		while (resultsKeyIterator.hasNext()) {
			CaseInsensitiveString key = (CaseInsensitiveString) resultsKeyIterator
					.next();
			int value = ((Integer) resultsMap.get(key)).intValue();
			ValueItem valueItem = new ValueItem(key + " (" + value + ")");
			resultsList.add(valueItem);
		}

		cursor.close();

		return resultsList;
	}

	public Set<String> getCurrentMelodies() {

		Set<String> fileSet = new HashSet<String>();
		Cursor cursor = db.rawQuery("select fileName from " + TABLE_NAME, null);
		if (cursor.moveToFirst()) {
			do {
				fileSet.add(cursor.getString(0));

			} while (cursor.moveToNext());
		}
		cursor.close();

		return fileSet;
	}

	public int getNumberOfMelodies() {

		int numberOfMelodies = 0;
		Cursor cursor = db.rawQuery("select count(*) as rowcount from "
				+ TABLE_NAME, null);
		if (cursor.moveToFirst()) {
			do {
				numberOfMelodies = cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();

		return numberOfMelodies;
	}

	public static class OpenHelper extends SQLiteOpenHelper {

		public OpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE "
					+ TABLE_NAME
					+ " (_id INTEGER PRIMARY KEY, singer TEXT, album TEXT, genre TEXT, year TEXT, title TEXT, fileName TEXT, albumArt BLOB)");
			db.execSQL("CREATE TABLE " + TABLE_NAME_1
					+ " (playlistName TEXT, melodyId INTEGER)");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			onCreate(db);
		}
	}

	public List<Long> executeMelodyIdsForPlaylists(String query) {

		List<Long> results = new ArrayList<Long>();
		TreeSet<CaseInsensitiveString> resultsKeySet = new TreeSet<CaseInsensitiveString>();
		Map<CaseInsensitiveString, SongItem> resultsMap = new HashMap<CaseInsensitiveString, SongItem>();
		Cursor cursor = db.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			do {
				if (!cursor.getString(0).equalsIgnoreCase(TAG_UNDEFINED))
					resultsMap.put(
							new CaseInsensitiveString(cursor.getString(0)),
							new SongItem(cursor.getString(0), cursor
									.getString(1), cursor.getLong(2)));
				else
					resultsMap.put(
							new CaseInsensitiveString(cursor.getString(1)
									.substring(
											cursor.getString(1)
													.lastIndexOf("/") + 1)),
							new SongItem(cursor.getString(1).substring(
									cursor.getString(1).lastIndexOf("/") + 1),
									cursor.getString(1), cursor.getLong(2)));

			} while (cursor.moveToNext());
		}
		cursor.close();

		resultsKeySet.addAll(resultsMap.keySet());
		Iterator<CaseInsensitiveString> resultsKeyIterator = resultsKeySet
				.iterator();
		CaseInsensitiveString resultsKey;
		while (resultsKeyIterator.hasNext()) {
			resultsKey = resultsKeyIterator.next();
			results.add(resultsMap.get(resultsKey).getSongId());
		}

		return results;
	}

	public Integer getMelodyId(String fileAbsolutePath) {

		Log.e("DataHelper",
				"Getting Melody ID: " + fileAbsolutePath.replace("'", "''"));

		Integer melodyId = null;
		Cursor cursor = db.rawQuery("select _id from " + TABLE_NAME
				+ " where fileName='" + fileAbsolutePath.replace("'", "''")
				+ "'", null);
		if (cursor.moveToFirst()) {
			do {
				melodyId = cursor.getInt(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return melodyId;
	}

	public SongInfo getSongInfo(String fileName) {
		Log.v("DataHelper: ", "getSongInfo");
		SongInfo songInfo = new SongInfo();
		fileName = fileName.replaceAll("'", "''");
		Cursor cursor = db.rawQuery(
				"select album, singer, genre, title, year, albumArt from "
						+ TABLE_NAME + " where fileName='" + fileName + "'",
				null);
		if (cursor.moveToFirst()) {
			do {
				songInfo.setAlbum(cursor.getString(0));
				songInfo.setSinger(cursor.getString(1));
				songInfo.setGenre(cursor.getString(2));
				songInfo.setTitle(cursor.getString(3));
				songInfo.setYear(cursor.getString(4));
				songInfo.setAlbumArt(cursor.getBlob(5));
			} while (cursor.moveToNext());
		}
		cursor.close();

		return songInfo;
	}

}
