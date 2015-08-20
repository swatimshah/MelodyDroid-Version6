package com.droid.melodydroid.display;

import com.droid.melodydroid.R;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomSearchResultsView extends LinearLayout {

	public CustomSearchResultsView(Context context, CustomSearchResult searchResult) {
		super(context);

		this.setOrientation(VERTICAL);
		this.setTag(searchResult);

		View v = inflate(context, R.layout.custom_search_row, null);

		TextView album = (TextView) v.findViewById(R.id.album);
		album.setText("Album: " + searchResult.getAlbum());

		TextView artist = (TextView) v.findViewById(R.id.artist);
		artist.setText("Artist: " + searchResult.getSinger());
		
		TextView genre = (TextView) v.findViewById(R.id.genre);
		genre.setText("Genre: " + searchResult.getGenre());
		
		TextView title = (TextView) v.findViewById(R.id.title);
		title.setText("Title: " + searchResult.getTitle());

		TextView year = (TextView) v.findViewById(R.id.year);
		year.setText("Year: " + searchResult.getYear());

		addView(v);
	}	
}
