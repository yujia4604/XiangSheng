package yujia.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import yujia.util.Logger;
import yujia.util.MyStringUtil;
import yujia.util.MyUtil;
import yujia.xiangsheng.G;
import yujia.xiangsheng.PlayActivity;
import yujia.xiangsheng.R;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class StreamingMediaPlayer implements OnClickListener {

	private TextView currentPlayTime;

	private ImageButton playButton;

	private SeekBar seekbar;

	// Track for display by progressBar
	private long mediaLengthInKb, mediaLengthInSeconds;
	private int totalKbRead = 0;

	// Create Handler to call View updates on the main UI thread.
	private final Handler handler = new Handler();

	private MediaPlayer mediaPlayer;

	private File downloadingMediaFile;

	private PlayActivity context;

	private int counter = 0;

	private Works works;

	protected boolean isPrepared;

	protected boolean isNeedCache;

	protected int requstKbSize;

	protected int requstPosition;

	protected boolean isStopped;

	private File bufferedFile;

	private boolean isLocalFile;

	private String filename;

	private DownloadRunnable downloadRunnable;

	/**
	 * 播放在線文件
	 * 
	 * @param context
	 * @param cuurentPlayTime
	 * @param progressBar
	 * @param works
	 * @param playBtn
	 * @param previousBtn
	 * @param nextBtn
	 */
	public StreamingMediaPlayer(PlayActivity context, TextView cuurentPlayTime,
			SeekBar progressBar, final Works works, ImageButton playBtn,
			ImageButton previousBtn, ImageButton nextBtn) {
		this.context = context;
		this.currentPlayTime = cuurentPlayTime;
		this.playButton = playBtn;
		this.seekbar = progressBar;
		this.works = works;

		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int progress = seekBar.getProgress();
				Logger.i(this, "onStopTrackingTouch  progress = " + progress);
				// 当音频正在播放时
				if (mediaPlayer != null) {
					int currentMediaCacheDuration = mediaPlayer.getDuration() / 1000;
					requstPosition = (int) (mediaLengthInSeconds * progress / 100);
					Logger.i("currentMediaCacheLenth "
							+ currentMediaCacheDuration + ", requstLength = "
							+ requstPosition);
					if (currentMediaCacheDuration >= requstPosition) {// 当当前下载的音频长度大于滑动的目标点时,不需要下载
						mediaPlayer.seekTo(requstPosition * 1000);
						if (isPrepared && !mediaPlayer.isPlaying()) {
							mediaPlayer.start();
						}
					} else {
						requstKbSize = requstPosition * 64 / 8
								+ G.INTIAL_KB_BUFFER;
						isNeedCache = true;
						Logger.i(this, "total read kb " + totalKbRead
								+ " requset kb size= " + requstKbSize);
						mediaPlayer.pause();
						if (downloadRunnable == null
								|| downloadRunnable.isInterrupted) {
							startStreaming(works);
						}
					}

				}

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

			}
		});
		playBtn.setOnClickListener(this);
	}

	/**
	 * 播放本地文件的构造函数
	 * 
	 * @param context2
	 * @param textStreamed2
	 * @param playBtn
	 * @param progressBar
	 * @param filename
	 */
	public StreamingMediaPlayer(PlayActivity context, TextView textStreamed,
			ImageButton playBtn, SeekBar progressBar, final String filename) {
		this.isLocalFile = true;
		this.context = context;
		this.currentPlayTime = textStreamed;
		this.playButton = playBtn;
		this.seekbar = progressBar;
		this.filename = filename;
		playBtn.setOnClickListener(this);

		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				int progress = seekBar.getProgress();
				Logger.i(this, "onStopTrackingTouch  progress = " + progress);
				// 当音频正在播放时
				if (mediaPlayer != null && mediaPlayer.isPlaying()) {
					int currentMediaCacheDuration = mediaPlayer.getDuration() / 1000;
					requstPosition = (int) (mediaLengthInSeconds * progress / 100);
					Logger.i("currentMediaCacheLenth "
							+ currentMediaCacheDuration + ", requstLength = "
							+ requstPosition);
					mediaPlayer.seekTo(requstPosition * 1000);

				}

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

			}
		});

	}

	/**
	 * Progressivly download the media to a temporary location and update the
	 * MediaPlayer as new content becomes available.
	 */
	public void startStreaming(final String mediaUrl, long mediaLengthInKb,
			long mediaLengthInSeconds) throws IOException {

		this.mediaLengthInKb = mediaLengthInKb;
		this.mediaLengthInSeconds = mediaLengthInSeconds;

		downloadRunnable = new DownloadRunnable(mediaUrl);

		new Thread(downloadRunnable).start();
	}

	/**
	 * Test whether we need to transfer buffered data to the MediaPlayer.
	 * Interacting with MediaPlayer on non-main UI thread can causes crashes to
	 * so perform this using a Handler.
	 * 
	 * @param inscreamtBytesRead
	 */
	private void checkMediaBuffer(final int inscreamtBytesRead) {
		Runnable updater = new Runnable() {
			public void run() {
				if (mediaPlayer == null) {
					// Only create the MediaPlayer once we have the minimum
					// buffered data
					if (totalKbRead >= G.INTIAL_KB_BUFFER) {
						try {
							context.getMsgView().setVisibility(View.INVISIBLE);
							startMediaPlayer();
						} catch (Exception e) {
							Log.e(getClass().getName(),
									"Error copying buffered conent.", e);
						}
					}
				} else if (inscreamtBytesRead >= G.DOWNLOAD_STERAM_CACHE_SIZE) {
					transferBufferToMediaPlayer();
				}
			}
		};
		handler.post(updater);
	}

	private void startMediaPlayer() {
		Logger.i("startmediaplayer called");
		/*
		 * bufferedFile = new File(context.getCacheDir(), "playingMedia" +
		 * (counter++) + ".dat"); moveFile(downloadingMediaFile, bufferedFile);
		 * 
		 * Log.e("Player bufferedFile length in byte ", bufferedFile.length() +
		 * ""); Log.e("Player", bufferedFile.getAbsolutePath());
		 */

		context.getService().playOnLineFile(works.getName(),
				downloadingMediaFile.getAbsolutePath(),
				new MediaPlayer.OnPreparedListener() {
					@Override
					public void onPrepared(MediaPlayer arg0) {
						Logger.i("mediapalyer is prepared");
						isPrepared = true;
						mediaPlayer = arg0;
						fireDataPreloadComplete();

					}
				});

	}

	/**
	 * Transfer buffered data to the MediaPlayer. Interacting with MediaPlayer
	 * on non-main UI thread can causes crashes to so perform this using a
	 * Handler.
	 */
	private void transferBufferToMediaPlayer() {
		Logger.i(" transferBufferToMediaPlayer called");
		try {
			// First determine if we need to restart the player after
			// transferring data...e.g. perhaps the user pressed pause
			final boolean wasPlaying = mediaPlayer.isPlaying();
			final int curPosition = mediaPlayer.getCurrentPosition();
			isPrepared = false;
			mediaPlayer.pause();
			mediaPlayer.release();

			/*
			 * File bufferedFile = new File(context.getCacheDir(),
			 * "playingMedia" + (counter++) + ".dat");
			 * moveFile(downloadingMediaFile, bufferedFile);
			 */
			context.getService().getHandler().post(new Runnable() {

				@Override
				public void run() {
					mediaPlayer = new MediaPlayer();
					try {
						mediaPlayer.setDataSource(downloadingMediaFile
								.getAbsolutePath());
						mediaPlayer.prepareAsync();
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

					mediaPlayer.setOnPreparedListener(new OnPreparedListener() {

						@Override
						public void onPrepared(MediaPlayer arg0) {
							isPrepared = true;
							mediaPlayer.seekTo(curPosition);
							boolean atEndOfFile = mediaPlayer.getDuration()
									- mediaPlayer.getCurrentPosition() >= 1000;
							if (wasPlaying || atEndOfFile) {
								mediaPlayer.start();
							}
						}
					});

				}
			});

			// Restart if at end of prior beuffered content or mediaPlayer was
			// previously playing.
			// NOTE: We test for < 1second of data because the media player can
			// stop when there is still
			// a few milliseconds of data left to play

		} catch (Exception e) {
			Log.e(getClass().getName(),
					"Error updating to newly loaded content.", e);
		}
	}

	private void updateSndProgress() {
		Runnable updater = new Runnable() {
			public void run() {
				// textStreamed.setText((CharSequence) (totalKbRead +
				// " Kb read"));
				float loadProgress = ((float) totalKbRead / (float) mediaLengthInKb);
				seekbar.setSecondaryProgress((int) (loadProgress * 100));
			}
		};
		handler.post(updater);
	}

	/**
	 * We have preloaded enough content and started the MediaPlayer so update
	 * the buttons & progress meters.
	 */
	private void fireDataPreloadComplete() {
		Logger.i("fireDataPreloadComplete called");
		Runnable updater = new Runnable() {
			public void run() {
				mediaPlayer.start();
				isStopped = false;
				startPlayProgressUpdater();
				playButton.setEnabled(true);
				// streamButton.setEnabled(false);
			}
		};
		handler.post(updater);
	}

	/**
	 * 当整个文件加载加完时
	 */
	private void onDataFullyLoaded() {
		Logger.i(" 音乐文件下载完成");
		Runnable updater = new Runnable() {
			public void run() {
				transferBufferToSDPlay();
				context.getMsgView().setVisibility(View.VISIBLE);
				context.getMsgView().setText("下载完成");
			}
		};
		handler.post(updater);
	}

	protected void transferBufferToSDPlay() {
		try {
			// First determine if we need to restart the player after
			// transferring data...e.g. perhaps the user pressed pause
			boolean wasPlaying = mediaPlayer.isPlaying();
			int curPosition = mediaPlayer.getCurrentPosition();
			mediaPlayer.pause();
			new File(G.XiangShengDir).mkdirs();
			File completeFile = new File(G.XiangShengDir, works.getName()
					.replace(" ", "-"));
			moveFile(downloadingMediaFile, completeFile);

			mediaPlayer = new MediaPlayer();
			mediaPlayer.setDataSource(completeFile.getAbsolutePath());
			// mediaPlayer.setAudioStreamType(AudioSystem.STREAM_MUSIC);
			mediaPlayer.prepare();
			mediaPlayer.seekTo(curPosition);

			// Restart if at end of prior beuffered content or mediaPlayer was
			// previously playing.
			// NOTE: We test for < 1second of data because the media player can
			// stop when there is still
			// a few milliseconds of data left to play
			boolean atEndOfFile = mediaPlayer.getDuration()
					- mediaPlayer.getCurrentPosition() <= 1000;
			if (wasPlaying || atEndOfFile) {
				mediaPlayer.start();
			}
		} catch (Exception e) {
			Log.e(getClass().getName(),
					"Error updating to newly loaded content.", e);
		}

	}

	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	public void startPlayProgressUpdater() {
		// Logger.i("startPlayProgressUpdater called");
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			int cuurent = mediaPlayer.getCurrentPosition();
			float progress = (((float) cuurent / 1000) / (float) mediaLengthInSeconds);
			currentPlayTime.setText(MyStringUtil.getFormatTime(cuurent));
			seekbar.setProgress((int) (progress * 100));

			Runnable notification = new Runnable() {
				public void run() {
					startPlayProgressUpdater();
				}
			};
			handler.postDelayed(notification, 1000);
		}
	}

	public void moveFile(File oldLocation, File newLocation) throws IOException {
		Logger.i("movefile from " + oldLocation.getAbsolutePath() + " ,to "
				+ newLocation.getAbsolutePath());
		if (oldLocation.exists()) {
			BufferedInputStream reader = new BufferedInputStream(
					new FileInputStream(oldLocation));
			BufferedOutputStream writer = new BufferedOutputStream(
					new FileOutputStream(newLocation, false));
			try {
				byte[] buff = new byte[8192];
				int numChars;
				while ((numChars = reader.read(buff, 0, buff.length)) != -1) {
					writer.write(buff, 0, numChars);
				}
			} catch (IOException ex) {
				throw new IOException("IOException when transferring "
						+ oldLocation.getPath() + " to "
						+ newLocation.getPath());
			} finally {
				try {
					if (reader != null) {
						writer.close();
						reader.close();
					}
				} catch (IOException ex) {
					Log.e(getClass().getName(),
							"Error closing files when transferring "
									+ oldLocation.getPath() + " to "
									+ newLocation.getPath());
				}
			}
		} else {
			throw new IOException(
					"Old location does not exist when transferring "
							+ oldLocation.getPath() + " to "
							+ newLocation.getPath());
		}
	}

	public void startStreaming(Works works) {
		PlayActivity playactivity = ((PlayActivity) context);
		playactivity.getNameView().setText(works.getName());
		playactivity.getTotalPlayTime().setText(
				MyStringUtil.getFormatTime(works.getLength() * 1000));
		isPrepared = false;
		try {
			startStreaming(G.URL_DOWNLOAD_PREFIX + works.getPath(),
					works.getSize(), works.getLength());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.stopbutton:
			Logger.i("play is stopped");
			onPlayStop();
			break;

		case R.id.btn_online_play:
			Logger.i("play is clicked");
			if (isLocalFile) {
				onPlayLocalFile();
			} else
				onPlay();
			break;
		case R.id.btn_previous:
			Logger.i("previous clicked");
			playPrevious();
			break;
		case R.id.btn_next:
			Logger.i("next clicked");
			playNext();
			break;
		}

	}

	private void onPlayLocalFile() {

		if (isPrepared) {
			if (mediaPlayer.isPlaying()) {// 暂停
				mediaPlayer.pause();
				playButton.setImageDrawable(context.getResources().getDrawable(
						R.drawable.ic_action_play));
			} else {
				if (isStopped) {
					Logger.i("play from stopped");
					isStopped = false;
					try {
						mediaPlayer.prepare();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mediaPlayer.seekTo(0);
				}
				mediaPlayer.start();
				playButton.setImageDrawable(context.getResources().getDrawable(
						R.drawable.ic_action_pause));
				startPlayProgressUpdater();
			}

		} else {
			context.getNameView().setText(filename);
			context.getService().playLocalFile(filename,
					new OnPreparedListener() {

						@Override
						public void onPrepared(MediaPlayer arg0) {
							isPrepared = true;
							mediaPlayer = arg0;
							mediaPlayer.start();
							context.getTotalPlayTime().setText(
									MyStringUtil.getFormatTime(mediaPlayer
											.getDuration()));
							seekbar.setSecondaryProgress(100);
							mediaLengthInSeconds = mediaPlayer.getDuration() / 1000;
							startPlayProgressUpdater();
							playButton.setImageDrawable(context.getResources()
									.getDrawable(R.drawable.ic_action_pause));

						}
					});

		}
	}

	private void onPlay() {
		if (isPrepared) {
			if (mediaPlayer.isPlaying()) {// 暂停
				mediaPlayer.pause();
				playButton.setImageDrawable(context.getResources().getDrawable(
						R.drawable.ic_action_play));
			} else {
				if (isStopped) {
					Logger.i("play from stopped");
					isStopped = false;
					try {
						mediaPlayer.prepare();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mediaPlayer.seekTo(0);
				}
				mediaPlayer.start();
				playButton.setImageDrawable(context.getResources().getDrawable(
						R.drawable.ic_action_pause));
				startPlayProgressUpdater();
			}

		} else {// 未开始播放时
			playButton.setImageDrawable(context.getResources().getDrawable(
					R.drawable.ic_action_pause));
			startStreaming(StreamingMediaPlayer.this.works);
		}
	}

	private void resetMediaplayer() {
		isPrepared = false;
		context.getService().getHandler().post(new Runnable() {

			@Override
			public void run() {
				if (mediaPlayer != null)
					mediaPlayer.release();
				mediaPlayer = null;
				isStopped = false;
				if (downloadRunnable != null) {
					downloadRunnable.interrupt();
					totalKbRead = 0;
				}
			}
		});

	}

	private void onPlayStop() {
		Logger.i("onPlayStop called");
		if (isPrepared) {
			context.getService().getHandler().post(new Runnable() {

				@Override
				public void run() {
					if (mediaPlayer != null)
						mediaPlayer.stop();
					isStopped = true;
					playButton.setImageDrawable(context.getResources()
							.getDrawable(R.drawable.ic_action_play));
					currentPlayTime.setText("00:00");
					seekbar.setProgress(0);
				}
			});

		}
	}

	public void onDestroy() {
		context.getService().getHandler().post(new Runnable() {

			@Override
			public void run() {
				if (mediaPlayer != null) {
					if (mediaPlayer.isPlaying())
						mediaPlayer.stop();
					isPrepared = false;
					mediaPlayer.release();
					mediaPlayer = null;

				}
			}
		});

		if (downloadRunnable != null)
			downloadRunnable.interrupt();
	}

	public void playNext() {
		Logger.i("play next called");
		onPlayStop();
		resetMediaplayer();
		if (isLocalFile) {
			String[] names = new File(G.XiangShengDir).list();
			for (int i = 0; i < names.length; i++) {
				if (filename.equals(names[i])) {
					if (i == names.length - 1) {
						MyUtil.makeToast(context, "已是最后一首", true);
						return;
					} else {
						filename = names[++i];
						onPlayLocalFile();
					}
				}
			}
		} else {

			if (G.worklist != null) {
				int index = getWorkListIndexById(works.getId());
				if (index == G.worklist.size() - 1 || index == -1) {
					MyUtil.makeToast(context, "已是最后一首", true);
				} else {
					index++;
					works = G.worklist.get(index);
					onPlay();
				}
			}
		}
	}

	// 由于worklist中不一定是从id=1的项目开始的，所以不能简单的用id来获取
	private int getWorkListIndexById(int id) {
		for (int i = 0; i < G.worklist.size(); i++) {
			Works w = G.worklist.get(i);
			if (id == w.getId())
				return i;
		}
		return -1;

	}

	/**
	 * 播放上一首
	 */
	public void playPrevious() {
		Logger.i("playPrevious called");
		onPlayStop();
		resetMediaplayer();
		if (isLocalFile) {
			String[] names = new File(G.XiangShengDir).list();
			for (int i = 0; i < names.length; i++) {
				if (filename.equals(names[i])) {
					if (i == 0) {
						MyUtil.makeToast(context, "已是第一首", true);
						return;
					} else {
						filename = names[--i];
						onPlayLocalFile();
					}

				}
			}

		} else {

			if (G.worklist != null) {
				int index = getWorkListIndexById(works.getId());
				if (index == 0) {
					MyUtil.makeToast(context, "已是第一首", true);
				} else {
					index--;
					works = G.worklist.get(index);
					onPlay();
				}
			}
		}
	}

	public class DownloadRunnable implements Runnable {

		private String mediaUrl;
		private boolean isInterrupted;

		public boolean isInterrupted() {
			return isInterrupted;
		}

		public DownloadRunnable(String mediaUrl) {
			this.mediaUrl = mediaUrl;
		}

		public void interrupt() {
			isInterrupted = true;
		}

		@Override
		public void run() {
			try {
				downloadAudioIncrement(mediaUrl);
			} catch (IOException e) {
				Log.e(getClass().getName(),
						"Unable to initialize the MediaPlayer for fileUrl="
								+ mediaUrl, e);
				return;
			}
		}

		/**
		 * Download the url stream to a temporary location and then call the
		 * setDataSource for that local file
		 * 
		 * @throws IOException
		 * 
		 * @throws InterruptedException
		 */
		public void downloadAudioIncrement(String mediaUrl) throws IOException {
			Logger.i("downloadAudioIncrement called");
			URLConnection cn = null;
			BufferedInputStream inStream = null;
			FileOutputStream out = null;
			int totalBytesRead = 0, inscreamtBytesRead = 0;
			downloadingMediaFile = new File(context.getCacheDir(),
					works.getName() + ".dat");
			Log.i(getClass().getName(), "downloadingMediaFile path "
					+ downloadingMediaFile.getAbsolutePath());
			try {
				// 检查是否存在未下载完的缓存文件
				if (downloadingMediaFile.exists()
						&& downloadingMediaFile.length() > G.DOWNLOAD_STERAM_CACHE_SIZE) {
					Logger.i("存在缓存文件");
					// 如果存在缓存文件则获取文件大小，并将流移动到此前缓存的末尾，
					long cacheSize = downloadingMediaFile.length();
					Logger.i("已缓存大小 " + cacheSize / 1000 + " kb");
					totalBytesRead = (int) cacheSize;
					inscreamtBytesRead = totalBytesRead;
					totalKbRead = totalBytesRead / 1000;
					if (!isPrepared) {
						startMediaPlayer();
						updateSndProgress();
					}
					// 由于每次过滤掉的字节数并不一定，所以重新向服务器发送信息获取指定位置的数据流
					String seekCacheUrl = mediaUrl + "&cache=" + cacheSize;
					inStream = new BufferedInputStream(
							getFileInputStream(seekCacheUrl));
				} else {
					inStream = new BufferedInputStream(
							getFileInputStream(mediaUrl));
				}

				out = new FileOutputStream(downloadingMediaFile, true);

				byte buf[] = new byte[16384];

				do {
					int duration = 0, current = 0;
					if (isPrepared) {
						duration = mediaPlayer.getDuration();
						try {
							current = mediaPlayer.getCurrentPosition();
						} catch (IllegalStateException e) {
							Logger.i("do while excepiton " + e.toString());
							interrupt();
						}

						Logger.i(" duration  = " + duration / 1000
								+ " , current  =  " + current / 1000
								+ " , d -c = " + (duration - current) / 1000);

					}

					if ((!isPrepared || duration - current < 1000 * 60)// 当mediaplayer未初始化或当前时间距总时间不到60秒，且小于单次缓冲总量时
							&& inscreamtBytesRead < G.DOWNLOAD_STERAM_CACHE_SIZE
							&& !isStopped) {
						int numread = inStream.read(buf, 0, buf.length);
						Logger.i("read stream byte " + numread);
						if (numread <= 0)
							break;
						out.write(buf, 0, numread);
						inscreamtBytesRead += numread;
						totalBytesRead += numread;
						totalKbRead = totalBytesRead / 1000;

						Logger.i("inscreamtBytesRead " + inscreamtBytesRead
								+ " , cache constant "
								+ G.DOWNLOAD_STERAM_CACHE_SIZE
								+ " , totalbyte " + totalBytesRead
								+ ", totalkbRead " + totalKbRead);
						checkMediaBuffer(inscreamtBytesRead);
						updateSndProgress();
						showStartCacheState();
					} else if (isNeedCache) {// 用户滑动滑需要缓冲时
						Logger.i(this, "do seek cache download");
						int totalNeedCacheSize = requstKbSize - totalKbRead;
						do {
							int numread = inStream.read(buf);
							if (numread <= 0)
								break;
							out.write(buf, 0, numread);
							inscreamtBytesRead += numread;
							totalBytesRead += numread;
							totalKbRead = totalBytesRead / 1000;
							Logger.i("read stream byte " + numread
									+ " ,totalKbRead =  " + totalKbRead
									+ " ,requstkbSize = " + requstKbSize);
							updateSndProgress();
							final int pacent = (int) ((float) (totalNeedCacheSize - (requstKbSize - totalKbRead))
									/ totalNeedCacheSize * 100);
							Logger.i("cache " + pacent);
							handler.post(new Runnable() {

								@Override
								public void run() {
									context.getMsgView().setVisibility(
											View.VISIBLE);
									context.getMsgView().setText(
											"缓冲中... " + pacent + "%");

								}
							});
						} while (totalKbRead < requstKbSize);
						transferBufferToMediaPlayer();
						isPrepared = true;
						mediaPlayer.start();
						mediaPlayer.seekTo(requstPosition * 1000);
						isNeedCache = false;
						handler.post(new Runnable() {

							@Override
							public void run() {
								startPlayProgressUpdater();
								context.getMsgView().setVisibility(
										View.INVISIBLE);
							}
						});
					} else {
						inscreamtBytesRead = 0;
						Thread.sleep(1000);
					}
				} while (!isInterrupted);

				if (!isInterrupted) {
					onDataFullyLoaded();
				}
			} catch (EOFException e) {
				e.printStackTrace();
				Logger.i("eof exception thrown ");
				downloadAudioIncrement(mediaUrl);
				isInterrupted = true;
			} catch (Exception e) {
				e.printStackTrace();
				Logger.i(this, e.toString());
				isInterrupted = true;
				downloadAudioIncrement(mediaUrl);
			} finally {
				Logger.i("strem download final called");
				if (inStream != null)
					inStream.close();
				if (out != null)
					out.close();
			}
		}

		private InputStream getFileInputStream(String mediaUrl)
				throws IOException, MalformedURLException {
			URLConnection cn;
			cn = new URL(mediaUrl).openConnection();
			cn.connect();
			InputStream in = cn.getInputStream();
			if (in == null) {
				Log.e(getClass().getName(),
						"Unable to create InputStream for mediaUrl:" + mediaUrl);
			}
			return in;
		}

		private void showStartCacheState() {
			if (totalKbRead <= G.INTIAL_KB_BUFFER) {// 当还没有开始播放时，显示正在缓冲
				final int startP = (int) ((float) totalKbRead
						/ G.INTIAL_KB_BUFFER * 100);
				handler.post(new Runnable() {

					@Override
					public void run() {
						TextView v = context.getMsgView();
						v.setVisibility(View.VISIBLE);
						v.setText("缓冲中..." + startP + "%");
					}
				});
			}
		}

	}
}