package com.droid.melodydroid.display;

public class PlaylistItem {

	private String playlistName;

	public PlaylistItem(String myPlaylistName) {
		this.playlistName = myPlaylistName;
	}
	
	public String getPlaylistName() {
		return playlistName;
	}

	public void setPlaylistName(String playlistName) {
		this.playlistName = playlistName;
	}
	
}
