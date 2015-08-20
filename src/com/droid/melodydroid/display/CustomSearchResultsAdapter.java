package com.droid.melodydroid.display;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class CustomSearchResultsAdapter extends ArrayAdapter<CustomSearchResult> {

	private Context context;
	private List<CustomSearchResult> searchResultsList;
	
	public CustomSearchResultsAdapter (Context context, int textviewResourceId,
			List<CustomSearchResult> searchResultsList) {
		super(context, textviewResourceId, searchResultsList);
		this.context = context;
		this.searchResultsList = searchResultsList;
	}

	public View getView(int position, View view, ViewGroup viewGroup) {
		CustomSearchResult searchResult = searchResultsList.get(position);
		return new CustomSearchResultsView(context, searchResult);
	}	
}
