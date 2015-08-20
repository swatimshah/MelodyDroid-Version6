package com.droid.melodydroid.helper;

import org.cmc.music.metadata.IMusicMetadata;
import org.cmc.music.metadata.ImageData;
import org.cmc.music.metadata.MusicMetadataSet;

import android.util.Log;

import com.droid.melodydroid.core.GlobalConstants;
import com.droid.melodydroid.core.Melody;
import com.droid.melodydroid.criteria.SearchCriteria;

import java.util.Calendar;
import java.util.Vector;

public class MelodyDroidHelper {



	public static String extractFilterCriteria(String selectedCriteria) {
		return (selectedCriteria
				.substring(0, selectedCriteria.lastIndexOf("("))).trim();
	}

	public static Melody extractMp3MetaData(MusicMetadataSet musicMetaDataSet) {
		IMusicMetadata metadata = musicMetaDataSet.getSimplified();
		Melody melody = new Melody();
		melody.setArtist(checkAndReturnNotNull(metadata.getArtist()));
		melody.setAlbum(checkAndReturnNotNull(metadata.getAlbum()));
		melody.setMelodyTitle(checkAndReturnNotNull(metadata.getSongTitle()));
		melody.setGenre(checkAndReturnNotNull(metadata.getGenreName()));
		melody.setYear(checkAndReturnNotNull(metadata.getYear()));
		if (metadata.getPictures() != null && metadata.getPictures().size() > 0) {
			Log.v("Got album art pictures: ", metadata.getPictures().size() + "");
			melody.setAlbumArt(checkAndReturnNotNull((ImageData)metadata.getPictures().get(0)));
		}	
		return melody;
	}

	public static String checkAndReturnNotNull(String stringValue) {
		if (stringValue == null)
			return GlobalConstants.TAG_UNDEFINED;
		return stringValue;
	}

	public static String checkAndReturnNotNull(Number numberValue) {
		if (numberValue == null)
			return GlobalConstants.TAG_UNDEFINED;
		return numberValue.toString();
	}

	public static byte[] checkAndReturnNotNull(ImageData pictures) {
		if (pictures == null)
			return new byte[0];
		return pictures.imageData;
	}
		
	public static long now() {
		Calendar cal = Calendar.getInstance();
		return cal.getTimeInMillis();
	}

	public static boolean isSdPresent() {
		return android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
	}

	public static boolean isSearchOptionSelected() {
		if (!"*".equals(SearchCriteria.getAlbum())
				|| !"*".equals(SearchCriteria.getArtist())
				|| !"*".equals(SearchCriteria.getGenre())
				|| !"*".equals(SearchCriteria.getTitle())
				|| !"*".equals(SearchCriteria.getYear()))
			return true;
		else
			return false;
	}
}
