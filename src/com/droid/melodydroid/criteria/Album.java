package com.droid.melodydroid.criteria;

public class Album extends SearchCriteria {

	protected static void buildOccurrenceCountQuery(StringBuffer query,
			StringBuffer whereClause) {
		// Get albums with occurrence count satisfying the search criteria.
		String whereClauseString = null;
		String finalWhereClause = null;

		query.append("select album, count(*) as rowcount from MELODYDROID ");

		if (!artist.equals("*"))
			whereClause.append(" lower(singer) = lower('" + artist + "') AND ");
		if (!genre.equals("*"))
			whereClause.append(" lower(genre) = lower('" + genre + "') AND ");
		if (!title.equals("*"))
			whereClause.append(" lower(title) = lower('" + title + "') AND ");
		if (!year.equals("*"))
			whereClause.append(" lower(year) = lower('" + year + "')");

		whereClauseString = whereClause.toString().trim();

		if (whereClauseString.endsWith("AND")) {
			int start = 0;
			int end = whereClauseString.lastIndexOf("AND");
			finalWhereClause = whereClauseString.substring(start, end);
		} else {
			finalWhereClause = whereClauseString;
		}

		if (finalWhereClause != null && finalWhereClause.length() != 0)
			query.append(" where " + finalWhereClause);

		query.append(" group by album order by album;");
	}

	public static void getFilterSongQuery(StringBuffer query,
			StringBuffer whereClause) {
		String whereClauseString = null;
		String finalWhereClause = null;

		query.append("select title, fileName, _id from MELODYDROID ");

		whereClause.append(" lower(album) = lower('" + lastSelectedFilterValue
				+ "') AND");

		if (!artist.equals("*"))
			whereClause.append(" lower(singer) = lower('" + artist + "') AND");
		if (!genre.equals("*"))
			whereClause.append(" lower(genre) = lower('" + genre + "') AND");
		if (!title.equals("*"))
			whereClause.append(" lower(title) = lower('" + title + "') AND");
		if (!year.equals("*")) {
			whereClause.append(" lower(year) = lower('" + year + "')");
		}
		whereClauseString = whereClause.toString().trim();

		if (whereClauseString.endsWith("AND")) {
			int start = 0;
			int end = whereClauseString.lastIndexOf("AND");
			finalWhereClause = whereClauseString.substring(start, end);
		} else {
			finalWhereClause = whereClauseString;
		}

		if (finalWhereClause != null && finalWhereClause.length() != 0) {
			query.append(" where " + finalWhereClause);
		}
		query.append(" order by title;");

	}

	public static void getMelodyIdsForPlaylistsQuery(StringBuffer query,
			StringBuffer whereClause, String filterValue) {
		String whereClauseString = null;
		String finalWhereClause = null;

		query.append("select title, fileName, _id from MELODYDROID ");

		whereClause.append(" lower(album) = lower('" + filterValue
				+ "') AND");

		if (!artist.equals("*"))
			whereClause.append(" lower(singer) = lower('" + artist + "') AND");
		if (!genre.equals("*"))
			whereClause.append(" lower(genre) = lower('" + genre + "') AND");
		if (!title.equals("*"))
			whereClause.append(" lower(title) = lower('" + title + "') AND");
		if (!year.equals("*")) {
			whereClause.append(" lower(year) = lower('" + year + "')");
		}
		whereClauseString = whereClause.toString().trim();

		if (whereClauseString.endsWith("AND")) {
			int start = 0;
			int end = whereClauseString.lastIndexOf("AND");
			finalWhereClause = whereClauseString.substring(start, end);
		} else {
			finalWhereClause = whereClauseString;
		}

		if (finalWhereClause != null && finalWhereClause.length() != 0) {
			query.append(" where " + finalWhereClause);
		}
		query.append(" order by title;");

	}

}
