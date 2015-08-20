package com.droid.melodydroid.display;

import com.droid.melodydroid.R;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SongItemAdapterView extends LinearLayout {

	public SongItemAdapterView(Context context, SongItem songItem) {
		super(context);

		this.setOrientation(VERTICAL);
		this.setTag(songItem);

		View v = inflate(context, R.layout.song_row, null);

		TextView songName = (TextView) v.findViewById(R.id.songName);
		songName.setText(songItem.getSongName());

		addView(v);
	}
}
