package com.droid.melodydroid.display;

import com.droid.melodydroid.R;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlaylistItemAdapterView extends LinearLayout {

	public PlaylistItemAdapterView(Context context, PlaylistItem playlistItem, int position) {
		super(context);

		this.setOrientation(VERTICAL);
		this.setTag(playlistItem);

		View v = inflate(context, R.layout.playlist_row, null);

		TextView playlistName = (TextView) v.findViewById(R.id.playlistName);
		playlistName.setText(playlistItem.getPlaylistName());
		if(position % 2 == 1)
			playlistName.setTextColor(0xff444444);

		addView(v);
	}	
}
