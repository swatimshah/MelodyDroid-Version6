package com.droid.melodydroid.data;

import com.droid.melodydroid.helper.MelodyDroidHelper;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class MelodiesProvider extends ContentProvider {

	private static final String TABLE_NAME = "MELODYDROID";
	private DataHelper dataHelper;

	public SQLiteDatabase openDatabase() {
		return dataHelper.openDatabase();
	}

	public void closeDatabase() {
		dataHelper.closeDatabase();
	}

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri arg0, ContentValues arg1) {
		return null;
	}

	@Override
	public boolean onCreate() {
		dataHelper = new DataHelper(getContext());
		return (dataHelper == null) ? false : true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		Log.v("MelodiesProvider: Beginning to query ..", "" + MelodyDroidHelper
				.now());
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = openDatabase();
		qb.setTables(TABLE_NAME);
		Cursor cursor = qb.query(db, projection, selection, selectionArgs,
				null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		Log.v("MelodiesProvider: Ended query execution ..", "" + MelodyDroidHelper
				.now());
		return cursor;
	}

	@Override
	public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return 0;
	}

}
