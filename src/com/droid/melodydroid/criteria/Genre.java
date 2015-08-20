package com.droid.melodydroid.criteria;

public class Genre extends SearchCriteria {
	protected static void buildOccurrenceCountQuery(StringBuffer query,
			StringBuffer whereClause) {

		// Get genres with occurrence count satisfying the search criteria.
		String whereClauseString = null;
		String finalWhereClause = null;

		query.append("select genre, count(*) as rowcount from MELODYDROID ");

		if (!artist.equals("*"))
			whereClause.append(" lower(singer) = lower('" + artist + "') AND ");
		if (!album.equals("*"))
			whereClause.append(" lower(album) = lower('" + album + "') AND ");
		if (!title.equals("*"))
			whereClause.append(" lower(title) = lower('" + title + "') AND ");
		if (!year.equals("*"))
			whereClause.append(" lower(year) = lower('" + year + "') ");

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

		query.append(" group by genre order by genre;");

	}

	public static void getFilterSongQuery(StringBuffer query,
			StringBuffer whereClause) {
		String whereClauseString = null;
		String finalWhereClause = null;

		query.append("select title, fileName, _id from MELODYDROID ");

		if (!album.equals("*"))
			whereClause.append(" lower(album) = lower('" + album + "') AND");
		if (!artist.equals("*"))
			whereClause.append(" lower(singer) = lower('" + artist + "') AND");

		whereClause.append(" lower(genre) = lower('" + lastSelectedFilterValue
				+ "') AND");

		if (!title.equals("*"))
			whereClause.append(" lower(title) = lower('" + title + "') AND");
		if (!year.equals("*")) {
			whereClause.append(" lower(year) = lower('" + year + "') ");
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

		if (!album.equals("*"))
			whereClause.append(" lower(album) = lower('" + album + "') AND");
		if (!artist.equals("*"))
			whereClause.append(" lower(singer) = lower('" + artist + "') AND");

		whereClause.append(" lower(genre) = lower('" + filterValue
				+ "') AND");

		if (!title.equals("*"))
			whereClause.append(" lower(title) = lower('" + title + "') AND");
		if (!year.equals("*")) {
			whereClause.append(" lower(year) = lower('" + year + "') ");
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
