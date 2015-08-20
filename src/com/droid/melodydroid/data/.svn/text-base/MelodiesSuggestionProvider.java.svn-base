package com.droid.melodydroid.data;

import java.util.Set;
import java.util.TreeSet;

import com.droid.melodydroid.helper.CaseInsensitiveString;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

public class MelodiesSuggestionProvider extends ContentProvider {

	public static final String PROVIDER_NAME = "melodysuggestionsproviderauthority";
	private DataHelper dataHelper;
	private static final String TABLE_NAME = "MELODYDROID";
	public static final Uri CONTENT_URI = Uri.parse("content://"
			+ PROVIDER_NAME + "/" + TABLE_NAME);
	public static final String[] COLUMNS = { "_id",
			SearchManager.SUGGEST_COLUMN_TEXT_1,
			SearchManager.SUGGEST_COLUMN_QUERY };

	public SQLiteDatabase openDatabase() {
		return dataHelper.openDatabase();
	}

	public void closeDatabase() {
		dataHelper.closeDatabase();
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
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

		String userQuery = null;

		if (selectionArgs != null) {
			userQuery = selectionArgs[0].toLowerCase();
			userQuery = userQuery.replaceAll("'", "''");
			selection = " lower(album) like '%" + userQuery
					+ "%' OR lower(singer) like '%" + userQuery
					+ "%' OR lower(title) like '%" + userQuery
					+ "%' OR lower(year) like '%" + userQuery
					+ "%' OR lower(genre) like '%" + userQuery + "%'";
		} else {
			userQuery = null;
			selection = null;
		}

		projection = new String[] { "album", "singer", "genre", "title", "year" };
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		SQLiteDatabase db = dataHelper.openDatabase();
		qb.setTables(TABLE_NAME);

		Cursor cursor = qb.query(db, projection, selection, null, null, null,
				sortOrder);

		MatrixCursor matrixCursor = new MatrixCursor(COLUMNS);
		Set<CaseInsensitiveString> results = new TreeSet<CaseInsensitiveString>();
		if (cursor.moveToFirst()) {
			do {
				for (int i = 0; i < 5; i++) {
					String dbString = cursor.getString(i).toLowerCase();
					if (userQuery != null && userQuery.length() != 0
							&& findMatch(dbString, userQuery)) {
						results.add(new CaseInsensitiveString(cursor
								.getString(i)));
						if (results.size() >= 5)
							break;
					}
				}
				if (results.size() >= 5)
					break;

			} while (cursor.moveToNext());
		}

		CaseInsensitiveString[] resultsArray = (CaseInsensitiveString[]) results
				.toArray(new CaseInsensitiveString[results.size()]);

		for (int j = 0; j < resultsArray.length; j++) {
			matrixCursor.addRow(new Object[] { j, resultsArray[j],
					resultsArray[j] });
		}

		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		cursor.close();
		return matrixCursor;
	}

	private boolean findMatch(String dbString, String userQuery) {

		String[] dbStringTokens = dbString.split(" ");

		for (int i = 0; i < dbStringTokens.length; i++) {
			if (dbStringTokens[i].startsWith(userQuery))
				return true;
		}

		return false;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		return 0;
	}

}
