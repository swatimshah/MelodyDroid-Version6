package com.droid.melodydroid.core;

import java.util.ArrayList;
import java.util.List;

import com.droid.melodydroid.R;
import com.droid.melodydroid.data.DataHelper;
import com.droid.melodydroid.display.DisplayPlaybackControls;
import com.droid.melodydroid.display.SongInfo;
import com.droid.melodydroid.helper.MelodyDroidHelper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class MDService extends Service {

	private MediaPlayer mp = new MediaPlayer();
	private List<String> songs = new ArrayList<String>();
	private int currentPosition;
	private NotificationManager nm;
	private static final int NOTIFY_ID = 100;
	private MDServiceBinder mdsServiceBinder = new MDServiceBinder();
	public Handler pbHandler;
	MelodyDroidApplication mda;
	Message pbMaxMsg;
	Message songInfo;
	Message playPauseMsg;
	Message progressMsg;
	String currentlyPlayedFile = "";
	int currentPositionOfProgressBar;

	@Override
	public void onCreate() {
		super.onCreate();
		mda = (MelodyDroidApplication) getApplication();
		mda.setMDService(this);
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		TelephonyManager mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		if (mgr != null) {
			mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mp.stop();
		mp.release();
		nm.cancel(NOTIFY_ID);

		TelephonyManager mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		if (mgr != null) {
			mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
		}

	}

	@Override
	public IBinder onBind(Intent arg0) {
		return mdsServiceBinder;
	}

	private class MDServiceBinder extends Binder implements MDSInterface {

		public void playFile(int position) {
			try {
				Log.v("MDService: The size of songs list: ", "" + songs.size());
				if (MelodyDroidHelper.isSdPresent()) {
					currentPosition = position;
					playSong(songs.get(position), false);
				} else {
					Toast toast = Toast.makeText(MDService.this,
							"SD Card is not available", Toast.LENGTH_SHORT);
					toast.show();
				}
			} catch (IndexOutOfBoundsException e) {
				Log.v("MDService: ", e.getMessage());
			}

		}

		public void addSongPlaylist(String song) {
			songs.add(song);
		}

		public void clearPlaylist() {
			songs.clear();
		}

		public void skipBack() {
			prevSong();
		}

		public void skipForward() {
			nextSong();
		}

		public void pause() {
			nm.cancel(NOTIFY_ID);
			mp.pause();
		}

		public void resume() {
			try {
				playSong(songs.get(currentPosition), true);
			} catch (IndexOutOfBoundsException e) {
				Log.v("MDService: ", e.getMessage());
			}

		}

		public void stop() {
			nm.cancel(NOTIFY_ID);
			mp.stop();
		}

		@Override
		public void getMessages() {
			/* Creating a message */
			pbMaxMsg = new Message();
			pbMaxMsg.arg1 = mp.getDuration() / 1000;
			/* Sending the message */
			mda.getDisplayPlaybackControls().getPbMaxHandler()
					.sendMessage(pbMaxMsg);
			/* Creating a message */
			songInfo = new Message();
			try {
				songInfo.obj = getSongInfo(currentlyPlayedFile);
			} catch (SQLiteException sqle) {
				SongInfo myDummySongInfo = new SongInfo();
				myDummySongInfo.setAlbum("");
				myDummySongInfo.setFileName("");
				myDummySongInfo.setGenre("");
				myDummySongInfo.setSinger("");
				myDummySongInfo.setTitle("");
				myDummySongInfo.setYear("");
				songInfo.obj = myDummySongInfo;
				Log.v("MDService: ", sqle.getMessage());
				Context context = getApplicationContext();
				CharSequence text = "Error playing the song "
						+ currentlyPlayedFile;
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
			/* Sending the message */
			mda.getDisplayPlaybackControls().getSongInfoHandler()
					.sendMessage(songInfo);
			/* Creating a message */
			if (mp.isPlaying()) {
				playPauseMsg = new Message();
				playPauseMsg.obj = "pause";
			} else {
				playPauseMsg = new Message();
				playPauseMsg.obj = "play";
			}
			/* Sending the message */
			mda.getDisplayPlaybackControls().getPlayPauseHandler()
					.sendMessage(playPauseMsg);
			/* Creating a message */
			progressMsg = new Message();
			progressMsg.arg1 = currentPositionOfProgressBar;
			/* Sending the message */
			mda.getDisplayPlaybackControls().getGuiHandler()
					.sendMessage(progressMsg);

		}
	};

	private void playSong(String file, boolean resume) {

		currentlyPlayedFile = file;

		try {
			displayNotificationPlayback(nm, file);
			if (!resume) {
				mp.reset();
				Log.v("MDService: Currently playing ", "" + file);
				mp.setDataSource(file);
				mp.prepare();
			}
			mp.start();

			/* Creating a message */
			pbMaxMsg = new Message();
			pbMaxMsg.arg1 = mp.getDuration() / 1000;
			/* Sending the message */
			mda.getDisplayPlaybackControls().getPbMaxHandler()
					.sendMessage(pbMaxMsg);

			/* Creating a message */
			songInfo = new Message();
			try {
				songInfo.obj = getSongInfo(file);
			} catch (SQLiteException sqle) {
				SongInfo myDummySongInfo = new SongInfo();
				myDummySongInfo.setAlbum("");
				myDummySongInfo.setFileName("");
				myDummySongInfo.setGenre("");
				myDummySongInfo.setSinger("");
				myDummySongInfo.setTitle("");
				myDummySongInfo.setYear("");
				songInfo.obj = myDummySongInfo;
				Log.v("MDService: ", sqle.getMessage());
				Context context = getApplicationContext();
				CharSequence text = "Error playing the song " + file;
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			}
			/* Sending the message */
			mda.getDisplayPlaybackControls().getSongInfoHandler()
					.sendMessage(songInfo);

			/* Creating a message */
			playPauseMsg = new Message();
			playPauseMsg.obj = "pause";
			/* Sending the message */
			mda.getDisplayPlaybackControls().getPlayPauseHandler()
					.sendMessage(playPauseMsg);

			new Thread(new Runnable() {
				@Override
				public void run() {
					int previousPosition = -1;
					int nextPosition = mp.getCurrentPosition();
					while (previousPosition != nextPosition) {

						/* Creating a message */
						progressMsg = new Message();
						progressMsg.arg1 = nextPosition / 1000;
						/* Sending the message */
						mda.getDisplayPlaybackControls().getGuiHandler()
								.sendMessage(progressMsg);
						;
						currentPositionOfProgressBar = nextPosition / 1000;
						previousPosition = nextPosition;

						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						nextPosition = mp.getCurrentPosition();
					}
				}
			}).start();

			mp.setOnCompletionListener(new OnCompletionListener() {
				public void onCompletion(MediaPlayer arg0) {
					onCompletionTasks();
				}
			});
		} catch (Exception e) {
			Log.v("MDService: ", e.getMessage());
			Context context = getApplicationContext();
			CharSequence text = "Error playing the song " + file;
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
		}
	}

	private void onCompletionTasks() {
		// Check if last song or not
		if (++currentPosition >= songs.size()) {
			--currentPosition;
			nm.cancel(NOTIFY_ID);

			/* Creating a message */
			Message playPauseMsg = new Message();
			playPauseMsg.obj = "play";
			/* Sending the message */
			mda.getDisplayPlaybackControls().getPlayPauseHandler()
					.sendMessage(playPauseMsg);

		} else {
			playSong(songs.get(currentPosition), false);
		}
	}

	private Object getSongInfo(String file) {
		DataHelper dataHelper = new DataHelper(this);
		SongInfo songInfo = dataHelper.getSongInfo(file);
		return songInfo;
	}

	private NotificationManager displayNotificationPlayback(
			NotificationManager nm, String fileName) {
		Notification notification = new Notification(R.drawable.playbackstart,
				"Playing Melody", System.currentTimeMillis());
		Context context = getApplicationContext();
		Intent intent = new Intent(this, DisplayPlaybackControls.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		notification.setLatestEventInfo(context, "Currently Playing", fileName,
				contentIntent);
		nm.notify(NOTIFY_ID, notification);
		return nm;
	}

	private void nextSong() {
		// Check if last song or not
		if (++currentPosition >= songs.size()) {
			currentPosition = 0;
			nm.cancel(NOTIFY_ID);
			playSong(songs.get(currentPosition), false);
		} else {
			playSong(songs.get(currentPosition), false);
		}
	}

	private void prevSong() {
		if (currentPosition >= 1) {
			playSong(songs.get(--currentPosition), false);
		} else {
			playSong(songs.get(currentPosition), false);
		}
	}

	PhoneStateListener phoneStateListener = new PhoneStateListener() {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			if (state == TelephonyManager.CALL_STATE_RINGING) {
				mp.pause();
			} else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
				mp.pause();
			}
			super.onCallStateChanged(state, incomingNumber);
		}
	};

	Handler seekHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			mp.seekTo(msg.arg1 * 1000);
		}
	};

	public Handler getSeekHandler() {
		return seekHandler;
	}

	public void setSeekHandler(Handler seekHandler) {
		this.seekHandler = seekHandler;
	}

}
