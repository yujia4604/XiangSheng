package yujia.xiangsheng;

import yujia.model.StreamingMediaPlayer;
import yujia.model.Works;
import yujia.util.Logger;
import yujia.util.MyStringUtil;
import yujia.util.MyUtil;
import yujia.xiangsheng.PlayService.LocalBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.openapi.v2.AppConnect;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

public class PlayActivity extends Activity {

	private StreamingMediaPlayer player;
	private TextView nameView;
	private TextView totalPlayTime;
	private int clickCount_down;
	private int clickCount_up;
	private TextView msgView;
	private boolean mBound;
	protected PlayService mService;
	private WifiLock wifiLock;
	private boolean isRestart;

	public TextView getNameView() {
		return nameView;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.i(this, "oncreate called");
		setContentView(R.layout.play_layout);

		ImageButton playBtn = (ImageButton) findViewById(R.id.btn_online_play);
		ImageButton stopBtn = (ImageButton) findViewById(R.id.stopbutton);
		ImageButton previousBtn = (ImageButton) findViewById(R.id.btn_previous);
		ImageButton nextBtn = (ImageButton) findViewById(R.id.btn_next);

		TextView currentPlayTime = (TextView) findViewById(R.id.currentPlayTimeView);
		msgView = (TextView) findViewById(R.id.msgView);

		totalPlayTime = (TextView) findViewById(R.id.totalPlayTimeView);
		nameView = (TextView) findViewById(R.id.nameView);
		SeekBar progressBar = (SeekBar) findViewById(R.id.music_progress);
		String filename = getIntent().getStringExtra("name");
		if (G.isFirstBoot) {
			G.isFirstBoot = false;
			MyUtil.makeToast(this, "双击音量键可切换曲目~~", true);
		}

		if (filename == null) {// 播放在在线文件
			Works works = (Works) getIntent().getSerializableExtra("works");
			if (works == null) {
				isRestart = true;
				return;
			}

			Logger.i(" get serializable extra " + works);
			player = new StreamingMediaPlayer(this, currentPlayTime,
					progressBar, works, playBtn, previousBtn, nextBtn);
			int sec = works.getLength();

			String totalTime = MyStringUtil.getFormatTime(sec * 1000);
			Logger.i(" total sec = " + sec + " format string  = " + totalTime);
			totalPlayTime.setText(totalTime);
			nameView.setText(works.getName());
			filename = works.getName();
		} else {// 播放本地文件
			player = new StreamingMediaPlayer(this, currentPlayTime, playBtn,
					progressBar, filename);
			nameView.setText(filename);

		}

		previousBtn.setOnClickListener(player);
		nextBtn.setOnClickListener(player);
		stopBtn.setOnClickListener(player);

		Intent intent = new Intent(this, PlayService.class);
		intent.putExtra("filename", filename);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
		Logger.i(this, "onCreate bindService called");

		wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
				.createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");

		wifiLock.acquire();
		LinearLayout adlayout = (LinearLayout) findViewById(R.id.AdLinearLayout);
		AppConnect.getInstance(this).showBannerAd(this, adlayout);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(isRestart){
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Bind to LocalService

	}

	@Override
	protected void onStop() {
		super.onStop();
	
	}

	/** Defines callbacks for service binding, passed to bindService() */
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			// We've bound to LocalService, cast the IBinder and get
			// LocalService instance
			Logger.i(this, "onServiceConnected");
			LocalBinder binder = (LocalBinder) service;
			mService = (PlayService) binder.getService();
			mBound = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			Logger.i(this, "onServiceDisconnected");
			mBound = false;
		}
	};
	private long firstClickTime;

	public TextView getMsgView() {
		return msgView;
	}

	public TextView getTotalPlayTime() {
		return totalPlayTime;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mBound) {
			unbindService(mConnection);
			mBound = false;
			Logger.i(this, "onDestroy unbindService called");
		}
		if (player != null)
			player.onDestroy();
		if (wifiLock != null)
			wifiLock.release();
		

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		long down = event.getDownTime();
		long eventtime = event.getEventTime();
		if (down != eventtime)
			return super.onKeyDown(keyCode, event);
		if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
			Logger.i(" volume down called");
			if (!isDoubleClick()) {
				clickCount_down = 0;
				return super.onKeyDown(keyCode, event);
			}
			clickCount_down++;
			clickCount_up = 0;
			if (clickCount_down >= 2) {
				player.playNext();
				clickCount_down = 0;
				firstClickTime = 0;
				return true;
			} else {
				return super.onKeyDown(keyCode, event);
			}

		} else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
			Logger.i(" volume up called");
			if (!isDoubleClick()) {
				clickCount_up = 0;
				return super.onKeyDown(keyCode, event);
			}
			clickCount_down = 0;
			clickCount_up++;
			if (clickCount_up >= 2) {
				player.playPrevious();
				clickCount_up = 0;
				firstClickTime = 0;
				return true;
			} else
				return super.onKeyDown(keyCode, event);

		} else {

			return super.onKeyDown(keyCode, event);

		}

	}

	/**
	 * 判断是不是连续的双击，或第一次点击
	 * 
	 * @return
	 */
	private boolean isDoubleClick() {
		long now = System.currentTimeMillis();
		if (firstClickTime == 0) {
			firstClickTime = System.currentTimeMillis();
			return true;
		}
		if (now - firstClickTime > 1000) {
			firstClickTime = 0;
			return false;
		}
		return true;
	}

	public PlayService getService() {
		// TODO Auto-generated method stub
		return mService;
	}

}
