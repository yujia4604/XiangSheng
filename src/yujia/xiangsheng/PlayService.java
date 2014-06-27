package yujia.xiangsheng;

import java.io.IOException;
import java.util.Random;

import yujia.util.Logger;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;

public class PlayService extends Service {

	// Binder given to clients
	private final IBinder mBinder = new LocalBinder();
	// Random number generator
	private final Random mGenerator = new Random();

	private Handler handler = new Handler();
	protected MediaPlayer mediaPlayer;

	/**
	 * Class used for the client Binder. Because we know this service always
	 * runs in the same process as its clients, we don't need to deal with IPC.
	 */
	public class LocalBinder extends Binder {
		PlayService getService() {
			// Return this instance of LocalService so clients can call public
			// methods
			return PlayService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		Logger.i(this, "onBind called");

		return mBinder;
	}

	private void startNotification(String filename) {
		// assign the song name to songName
		PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),
				0, new Intent(getApplicationContext(), PlayActivity.class),
				PendingIntent.FLAG_UPDATE_CURRENT);
		Notification notification = new Notification();
		notification.tickerText = getText(R.string.app_name);
		notification.icon = R.drawable.ic_launcher;
		notification.flags |= Notification.FLAG_ONGOING_EVENT;
		notification.setLatestEventInfo(getApplicationContext(),
				getText(R.string.app_name), "正在播放: " + filename, pi);
		startForeground(R.string.app_name, notification);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Logger.i(this, "unbind called");
		stopForeground(true);
		/*if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying())
				mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;

		}*/
		return super.onUnbind(intent);
	}

	/** method for clients */
	public int getRandomNumber() {
		return mGenerator.nextInt(100);
	}

	public void playLocalFile(final String filename,
			final MediaPlayer.OnPreparedListener listener) {
		handler.post(new Runnable() {

			@Override
			public void run() {
				mediaPlayer = new MediaPlayer();
				mediaPlayer.setWakeMode(getApplicationContext(),
						PowerManager.PARTIAL_WAKE_LOCK);
				try {
					mediaPlayer.setDataSource(G.XiangShengDir + filename);
					mediaPlayer.prepareAsync();
					mediaPlayer.setOnPreparedListener(listener);
					startNotification(filename);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	public void playOnLineFile(String filename, String absolutePath,
			OnPreparedListener onPreparedListener) {
		mediaPlayer = new MediaPlayer();

		try {
			mediaPlayer.setDataSource(absolutePath);
			startNotification(filename);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mediaPlayer.prepareAsync();
		mediaPlayer.setOnPreparedListener(onPreparedListener);
	}

	public Handler getHandler() {
		return handler;
	}

}
