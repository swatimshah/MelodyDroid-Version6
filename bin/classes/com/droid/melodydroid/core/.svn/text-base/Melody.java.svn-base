package com.droid.melodydroid.core;

import com.droid.melodydroid.data.DataHelper;

public class Melody {

	private String year;
	private String artist;
	private String album;
	private String songTitle;
	private String genre;
	private String fileName;
	private byte[] albumArt;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getSongTitle() {
		return songTitle;
	}

	public void setMelodyTitle(String songTitle) {
		this.songTitle = songTitle;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public byte[] getAlbumArt() {
		return albumArt;
	}

	public void setAlbumArt(byte[] albumArt) {
		this.albumArt = albumArt;
	}
	
	public void insert(DataHelper dataHelper) {
		dataHelper.insert(artist, album, genre, year, songTitle, fileName, albumArt);
	}

	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Melody: \n" + "artist: " + artist + " \n"
				+ "album: " + album + " \n" + "songTitle: " + songTitle + " \n"
				+ "Genre Name: " + genre + " \n" + "Year: " + year + "\n"
				+ "Filename: " + fileName + "\n");
		return stringBuilder.toString();
	}

}
