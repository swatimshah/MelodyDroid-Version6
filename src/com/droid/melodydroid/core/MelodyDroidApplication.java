package com.droid.melodydroid.core;

import com.droid.melodydroid.display.DisplayPlaybackControls;

import android.app.Application;

public class MelodyDroidApplication extends Application {

	private DisplayPlaybackControls displayPlaybackControls;
	private MDService mDService;

	public DisplayPlaybackControls getDisplayPlaybackControls() {
		return displayPlaybackControls;
	}

	public void setDisplayPlaybackControls(
			DisplayPlaybackControls displayPlaybackControls) {
		this.displayPlaybackControls = displayPlaybackControls;
	}

	public MDService getMDService() {
		return mDService;
	}

	public void setMDService(MDService service) {
		mDService = service;
	}

}
