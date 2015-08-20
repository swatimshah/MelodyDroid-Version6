package com.droid.melodydroid.criteria;

import java.util.List;

import com.droid.melodydroid.data.DataHelper;
import com.droid.melodydroid.display.SongItem;
import com.droid.melodydroid.display.ValueItem;

import android.content.Context;
import android.util.Log;

public class SearchCriteria {

	protected static String album = "*";
	protected static String artist = "*";
	protected static String genre = "*";
	protected static String title = "*";
	protected static String year = "*";
	protected static String lastSelectedFilterName;
	protected static String lastSelectedFilterValue;

	public static String getAlbum() {
		return album;
	}

	public static void setAlbum(String album) {
		SearchCriteria.album = album;
	}

	public static String getArtist() {
		return artist;
	}

	public static void setArtist(String artist) {
		SearchCriteria.artist = artist;
	}

	public static String getGenre() {
		return genre;
	}

	public static void setGenre(String genre) {
		SearchCriteria.genre = genre;
	}

	public static String getTitle() {
		return title;
	}

	public static void setTitle(String title) {
		SearchCriteria.title = title;
	}

	public static String getYear() {
		return year;
	}

	public static void setYear(String year) {
		SearchCriteria.year = year;
	}

	public static String getLastSelectedFilterName() {
		return lastSelectedFilterName;
	}

	public static void setLastSelectedFilterName(String lastSelectedFilterName) {
		SearchCriteria.lastSelectedFilterName = lastSelectedFilterName;
	}

	public static String getLastSelectedFilterValue() {
		return lastSelectedFilterValue;
	}

	public static void setLastSelectedFilterValue(String lastSelectedFilterValue) {
		SearchCriteria.lastSelectedFilterValue = lastSelectedFilterValue;
	}

	public static void setupSearchCriteria() {
		if ("Album".equals(lastSelectedFilterName))
			album = lastSelectedFilterValue;
		if ("Artist".equals(lastSelectedFilterName))
			artist = lastSelectedFilterValue;
		if ("Genre".equals(lastSelectedFilterName))
			genre = lastSelectedFilterValue;
		if ("Title".equals(lastSelectedFilterName))
			title = lastSelectedFilterValue;
		if ("Year".equals(lastSelectedFilterName))
			year = lastSelectedFilterValue;
	}

	private static String buildOccurrenceCountQuery() {

		StringBuffer query = new StringBuffer();
		StringBuffer whereClause = new StringBuffer();

		if ("Album".equals(lastSelectedFilterName)) {
			Album.buildOccurrenceCountQuery(query, whereClause);
		} else if ("Artist".equals(lastSelectedFilterName)) {
			Artist.buildOccurrenceCountQuery(query, whereClause);
		} else if ("Genre".equals(lastSelectedFilterName)) {
			Genre.buildOccurrenceCountQuery(query, whereClause);
		} else if ("Title".equals(lastSelectedFilterName)) {
			Title.buildOccurrenceCountQuery(query, whereClause);
		} else if ("Year".equals(lastSelectedFilterName)) {
			Year.buildOccurrenceCountQuery(query, whereClause);
		}

		return query.toString();
	}

	public static String getFilterSongQuery() {
		StringBuffer query = new StringBuffer();
		StringBuffer whereClause = new StringBuffer();

		if ("Album".equals(lastSelectedFilterName)) {
			Album.getFilterSongQuery(query, whereClause);
		} else if ("Artist".equals(lastSelectedFilterName)) {
			Artist.getFilterSongQuery(query, whereClause);
		} else if ("Genre".equals(lastSelectedFilterName)) {
			Genre.getFilterSongQuery(query, whereClause);
		} else if ("Title".equals(lastSelectedFilterName)) {
			Title.getFilterSongQuery(query, whereClause);
		} else if ("Year".equals(lastSelectedFilterName)) {
			Year.getFilterSongQuery(query, whereClause);
		}

		return query.toString();
	}

	public static List<ValueItem> getResultsWithOccurrenceCount(Context context) {
		String query = buildOccurrenceCountQuery();
		DataHelper dataHelper = new DataHelper(context);
		return dataHelper.executeAndGetResultsWithOccurrenceCount(query);
	}

	public static List<SongItem> getResults(Context context) {
		String query = getFilterSongQuery();
		DataHelper dataHelper = new DataHelper(context);
		return dataHelper.execute(query);
	}

	public static String convertToString() {
		Log.v("SearchCriteria: ", "album: " + album + "\n artist: " + artist
				+ "\n genre: " + genre + "\n title: " + title + "\n year: "
				+ year + "\n lastSelectedFilterName: " + lastSelectedFilterName
				+ "\n lastSelectedFilterValue: " + lastSelectedFilterValue);

		return "album: " + album + "\n artist: " + artist + "\n genre: "
				+ genre + "\n title: " + title + "\n year: " + year
				+ "\n lastSelectedFilterName: " + lastSelectedFilterName
				+ "\n lastSelectedFilterValue: " + lastSelectedFilterValue;
	}

	public static void clearAll() {
		album = "*";
		artist = "*";
		genre = "*";
		title = "*";
		year = "*";
		lastSelectedFilterName = "";
		lastSelectedFilterValue = "";
	}

	public static List<Long> getResultsWithMelodyIdsForPlaylists(
			String filterName, String filterValue, Context context) {
		String query = getMelodyIdsForPlaylistsQuery(filterName, filterValue);
		DataHelper dataHelper = new DataHelper(context);
		return dataHelper.executeMelodyIdsForPlaylists(query);
	}

	private static String getMelodyIdsForPlaylistsQuery(String filterName,
			String filterValue) {
		StringBuffer query = new StringBuffer();
		StringBuffer whereClause = new StringBuffer();

		if ("Album".equals(filterName)) {
			Album
					.getMelodyIdsForPlaylistsQuery(query, whereClause,
							filterValue);
		} else if ("Artist".equals(filterName)) {
			Artist.getMelodyIdsForPlaylistsQuery(query, whereClause,
					filterValue);
		} else if ("Genre".equals(filterName)) {
			Genre
					.getMelodyIdsForPlaylistsQuery(query, whereClause,
							filterValue);
		} else if ("Title".equals(filterName)) {
			Title
					.getMelodyIdsForPlaylistsQuery(query, whereClause,
							filterValue);
		} else if ("Year".equals(filterName)) {
			Year.getMelodyIdsForPlaylistsQuery(query, whereClause, filterValue);
		}

		return query.toString();
	}
	
}
