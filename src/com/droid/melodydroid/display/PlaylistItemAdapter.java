package com.droid.melodydroid.display;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class PlaylistItemAdapter extends ArrayAdapter<PlaylistItem> {

	private Context context;
	private List<PlaylistItem> listOfPlaylists;

	private int[] colors = new int[] { 0xff444444, 0xff888888};	
	
	public PlaylistItemAdapter(Context context, int textviewResourceId,
			List<PlaylistItem> listOfPlaylists) {
		super(context, textviewResourceId, listOfPlaylists);
		this.context = context;
		this.listOfPlaylists = listOfPlaylists;
	}

	public View getView(int position, View view, ViewGroup viewGroup) {
		PlaylistItem playlistItem = (PlaylistItem) listOfPlaylists.get(position);
		View myView = new PlaylistItemAdapterView(context, playlistItem, position);
		int colorPos = position % colors.length;
		myView.setBackgroundColor(colors[colorPos]);
		return myView;
	}
}
