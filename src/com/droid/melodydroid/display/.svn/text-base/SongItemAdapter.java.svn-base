package com.droid.melodydroid.display;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class SongItemAdapter extends ArrayAdapter<SongItem> {

	private Context context;
	private List<SongItem> listOfSongItems;

	public SongItemAdapter(Context context, int textviewResourceId,
			List<SongItem> listOfSongItems) {
		super(context, textviewResourceId, listOfSongItems);
		this.context = context;
		this.listOfSongItems = listOfSongItems;
	}

	public View getView(int position, View view, ViewGroup viewGroup) {
		SongItem songItem = (SongItem) listOfSongItems.get(position);
		return new SongItemAdapterView(context, songItem);
	}
}
