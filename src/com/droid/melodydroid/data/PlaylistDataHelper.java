package com.droid.melodydroid.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.droid.melodydroid.display.SongItem;
import com.droid.melodydroid.helper.CaseInsensitiveString;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class PlaylistDataHelper extends DataHelper {

	private static final String TAG_UNDEFINED = "Undefined";
	private static final String INSERT = "insert into " + TABLE_NAME_1
			+ " (playlistname, melodyid) values (?, ?)";

	private static final String DELETE_PLAYLIST = "delete from " + TABLE_NAME_1
	+ " where playlistName = ? ";

	private static final String DELETE_ASSOCIATIONS = "delete from " + TABLE_NAME_1 + " where melodyId = ?";
	
	public PlaylistDataHelper(Context context) {
		super(context);
	}

	public long insert(String playlistName, List<Long> melodyIds) {
		SQLiteDatabase db = openDatabase();
		SQLiteStatement insertStmt = db.compileStatement(INSERT);
		long result = 0L;
		for (int i = 0; i < melodyIds.size(); i++) {
			insertStmt.bindString(1, playlistName);
			insertStmt.bindLong(2, melodyIds.get(i));
			result = insertStmt.executeInsert();			
		}
		return result;
	}

	public Set<CaseInsensitiveString> retrieveAllPlayLists() {
		SQLiteDatabase db = openDatabase();
		Set<CaseInsensitiveString> playLists = new TreeSet<CaseInsensitiveString>();
		Cursor cursor = db.rawQuery("select playlistName from "
				+ TABLE_NAME_1, null);
		if (cursor.moveToFirst()) {
			do {
				playLists.add(new CaseInsensitiveString(cursor.getString(0)));
			} while (cursor.moveToNext());
		}
		cursor.close();

		return playLists;
	}

	public List<Integer> retrieveAllMelodies(String playListName) {
		SQLiteDatabase db = openDatabase();
		List<Integer> melodyIds = new ArrayList<Integer>();
		Cursor cursor = db.rawQuery("select melodyId from " + TABLE_NAME_1
				+ " where playlistName = '" + playListName + "'", null);

		if (cursor.moveToFirst()) {
			do {
				melodyIds.add(new Integer(cursor.getInt(0)));
			} while (cursor.moveToNext());
		}

		cursor.close();
		return melodyIds;
	}

	public List<SongItem> retrieveAllMelodyInfo(List<Integer> melodyIds) {
		SQLiteDatabase db = openDatabase();
		List<SongItem> melodiesInfo = new ArrayList<SongItem>();
		for (int i = 0; i < melodyIds.size(); i++) {
			Cursor cursor = db.rawQuery(
					"select title, fileName, _id from MELODYDROID where _id='"
							+ melodyIds.get(i) + "'", null);

			if (cursor.moveToFirst()) {

				if (!cursor.getString(0).equalsIgnoreCase(TAG_UNDEFINED))
					melodiesInfo.add(new SongItem(cursor.getString(0), cursor
							.getString(1), cursor.getLong(2)));
				else
					melodiesInfo.add(new SongItem(
							cursor.getString(1).substring(
									cursor.getString(1).lastIndexOf("/") + 1),
							cursor.getString(1), cursor.getLong(2)));

			}
			cursor.close();
		}
		return melodiesInfo;
	}

	public void removePlaylist(String selectedPlaylistName) {
		SQLiteDatabase db = openDatabase();
		SQLiteStatement deleteStmt = db.compileStatement(DELETE_PLAYLIST);		
		deleteStmt.bindString(1, selectedPlaylistName);
		deleteStmt.execute();
	}

	public void deleteMelodyIdFromPlaylists(Integer melodyId) {
		SQLiteDatabase db = openDatabase();
		SQLiteStatement deleteStmt = db.compileStatement(DELETE_ASSOCIATIONS);		
		deleteStmt.bindLong(1, melodyId);
		deleteStmt.execute();		
	}
	
}
