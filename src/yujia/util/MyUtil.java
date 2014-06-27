package yujia.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import yujia.xiangsheng.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.StatFs;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class MyUtil {
	// public static MyUtilites myUtilit = new MyUtilites();

	public final static String tag = "myUtilites";

	private static String getRegularString(String name) {
		Logger.i("before regular repalce = " + name);
		if (name == null)
			return null;
		name = name.replace("(", "\\(");
		name = name.replace(")", "\\)");
		Logger.i(" regular repalce result = " + name);
		return name;
	}

	/*
	 * public static String[] getAudioTitleArray() { File audioDir = new
	 * File(References.gameAudioPath); String[] fliesNames = audioDir.list();
	 * Loger.i( Arrays.toString(fliesNames)); return fliesNames; } public static
	 * String[] getOpernTitleArray() { File audioDir = new
	 * File(References.gameOpernPath); String[] fliesNames = audioDir.list();
	 * Loger.i( Arrays.toString(fliesNames)); return fliesNames; }
	 */

	public static class AudioFileNameFilter implements FilenameFilter {

		@Override
		public boolean accept(File dir, String filename) {

			return filename.endsWith("mp3") || filename.endsWith("ogg")
					|| filename.endsWith("m4a") || filename.endsWith("wav")
					|| filename.endsWith("wma");
		}

	}

	public static int getIntFromSharedPreferences(Context context,
			String fileName, String key) {
		SharedPreferences sp = context.getSharedPreferences(fileName, 0);
		int value = sp.getInt(key, 0);
		return value;
	}

	public static void storeIntToSharedPreferences(Context context,
			String fileName, String key, int value) {
		SharedPreferences sp = context.getSharedPreferences(fileName, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putInt(key, value);
		editor.commit();

	}

	public static void storeStrToDefaultPreferences(Context context,
			String key, String value) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void storeToDefaultPreferences(Context context, String key,
			int value) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	/**
	 * ���ĳpref�ļ����Ƿ����ĳ������?
	 * 
	 * @param context
	 * @param fileName
	 *            sharePref �ļ���
	 * @param key
	 *            �������?
	 * @return
	 */
	public static boolean isContainsInSharedPreferences(Context context,
			String fileName, String key) {
		SharedPreferences sp = context.getSharedPreferences(fileName, 0);
		return sp.contains(key);
	}

	public static void storeBooleanToSharedPreferences(Context context,
			String fileName, String key, boolean value) {
		SharedPreferences sp = context.getSharedPreferences(fileName, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static boolean getBooleanFromSharedPreferences(Context context,
			String fileName, String key) {
		SharedPreferences sp = context.getSharedPreferences(fileName, 0);
		boolean b = sp.getBoolean(key, false);
		return b;

	}

	public static void makeToast(Context c, String str, boolean isLongShow) {
		if (isLongShow)
			Toast.makeText(c, str, 5).show();
		else
			Toast.makeText(c, str, 2).show();
	}
	public static void makeToast(Fragment fragment, String str, boolean isLongShow) {
		if (isLongShow)
			Toast.makeText(fragment.getActivity(), str, 5).show();
		else
			Toast.makeText(fragment.getActivity(), str, 2).show();
	}

	/*
	 * public static void sendAdNotification(Context context, AdInfo adInfo,
	 * String pointsName) { String text = adInfo.getAdName() + " " +
	 * adInfo.getAdText() + " �� " + adInfo.getAdPoints() + pointsName;
	 * NotificationManager nm = (NotificationManager) context
	 * .getSystemService("notification"); Notification notification = new
	 * Notification(R.drawable.ic_launcher, text, System.currentTimeMillis());
	 * notification.flags = Notification.FLAG_AUTO_CANCEL; Intent intent = new
	 * Intent(context, AdDialogActivity.class); intent.putExtra("id",
	 * adInfo.getAdId()); PendingIntent contentIntent =
	 * PendingIntent.getActivity(context, 0, intent, 0); // must set this for
	 * content view, or will throw a exception
	 * notification.setLatestEventInfo(context, adInfo.getAdName(), adInfo
	 * .getAdText() + " �� " + adInfo.getAdPoints() + pointsName,
	 * contentIntent); nm.notify(0, notification); }
	 */



	/**
	 * ��Ĭ�ϵ�preferenc�л�ȡ����
	 * 
	 * @param c
	 * @param key
	 * @param defaulVaule
	 * @return
	 */
	public static int getIntFromDufaultpref(Context c, String key,
			int defaulVaule) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(c);

		int i = pref.getInt(key, defaulVaule);
		return i;
	}

	public static String getValueFromDufaultpref(Context c, String key,
			String defaulVaule) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(c);
		String str = pref.getString(key, defaulVaule);
		return str;
	}

	public static int getValueFromDufaultpref(Context c, String key,
			int defaulVaule) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(c);
		int value = pref.getInt(key, defaulVaule);
		return value;
	}
	
	public static <T> T getValueFromDufaultpref(Context c, String key,
			T defaulVaule) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(c);
		if (defaulVaule instanceof Integer) {
			return (T) (Integer) pref.getInt(key, (Integer) defaulVaule);
		} else if (defaulVaule instanceof Float) {
			return (T) (Float) pref.getFloat(key, (Float) defaulVaule);
		} else if (defaulVaule instanceof String) {
			return (T) pref.getString(key, (String) defaulVaule);
		} else if (defaulVaule instanceof Long) {
			return (T) (Long) pref.getLong(key, (Long) defaulVaule);
		} else if (defaulVaule instanceof Boolean) {
			return (T) (Boolean) pref.getBoolean(key, (Boolean) defaulVaule);
		}
		return null;

	}
	
	public static <T> void storeValueFromDufaultpref(Context c, String key,
			T value) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(c);
		SharedPreferences.Editor editor = pref.edit();
		if (value instanceof Integer) {
			editor.putInt(key, (Integer)value);
		} else if (value instanceof Float) {
			editor.putFloat(key, (Float)value);
		} else if (value instanceof String) {
			editor.putString(key, (String) value);
		} else if (value instanceof Long) {
			editor.putLong(key, (Long) value);
		} else if (value instanceof Boolean) {
			editor.putBoolean(key, (Boolean) value);
		}
		editor.commit();
	}

	/**
	 * ��Ĭ�ϵ�preferenc�л�ȡ����ֵ
	 * 
	 * @param c
	 * @param key
	 * @param defaulVaule
	 * @return
	 */
	public static boolean getBooleanFromDufaultpref(Context c, String key,
			boolean defaulVaule) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(c);
		boolean b = pref.getBoolean(key, defaulVaule);
		return b;
	}

	public static boolean WriteRawFileToSD(Context c, int id, String outPath) {
		InputStream is = c.getResources().openRawResource(id);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outPath);
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = is.read(buffer)) > 0) {
				fos.write(buffer, 0, count);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fos.flush();
				fos.close();
				is.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static boolean copyFileInSd(Context c, String srcPath, String outPath) {
		Logger.i("srcPath = " + srcPath + "\n" + "outPath = " + outPath);
		FileOutputStream fos = null;
		InputStream in = null;
		try {
			in = new FileInputStream(new File(srcPath));
			fos = new FileOutputStream(outPath);
			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = in.read(buffer)) > 0) {
				fos.write(buffer, 0, count);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				fos.flush();
				fos.close();
				in.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 
	 * @param c
	 * @param title
	 *            �Ի������?
	 * @param body
	 *            �Ի�������
	 * @return
	 */
	public static Builder getDialogBuilder(Context c, String title, String body) {
		AlertDialog.Builder builder = new AlertDialog.Builder(c);
		if (title != null)
			builder.setTitle(title);
		if (body != null)
			builder.setMessage(body);
		return builder;
	}

	/**
	 * ȥ�������ļ��ĺ�׺��
	 * 
	 * @param totalString
	 * @return
	 */
	public static String getPreSubString(String title) {
		int index = title.lastIndexOf(".");
		if (index == -1)
			return title;
		else {
			return title.substring(0, index);
		}
	}

	public static String getPostSubString(String title) {
		int index = title.lastIndexOf(".");
		if (index == -1)
			return null;
		else {
			return title.substring(index + 1);
		}
	}

	

	/**
	 * ��handlereΪ����̷���MESSAGE
	 * 
	 * @param what
	 * @param arg1
	 * @param arg2
	 * @param obj
	 * @param handler
	 */
	public static void senderMsgByhandler(int what, int arg1, int arg2,
			Object obj, Handler handler) {
		if (handler == null) {
			Logger.i("handler is null");
			return;
		}
		Message msg = handler.obtainMessage();
		msg.what = what;
		msg.obj = obj;
		msg.arg1 = arg1;
		msg.arg2 = arg2;
		handler.sendMessage(msg);
	}

	/**
	 * ��ʾһ��ȷ�ϵĶԻ���
	 * 
	 * @param context
	 * @param titleId
	 * @param bodyId
	 */
	/*
	 * public static void showSureDialog(Context context, int titleId, int
	 * bodyId) { String title = null; String body = null; if (titleId != 0)
	 * title = context.getString(titleId); if (bodyId != 0) body =
	 * context.getString(bodyId); showSureDialog(context, title, body); } public
	 * static void showSureDialog(Context context, String title, String body) {
	 * AlertDialog.Builder builder = getDialogBuilder(context, title, body);
	 * builder.setPositiveButton(context.getString(R.string.sure), new
	 * DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) {
	 * dialog.dismiss(); } }); builder.show(); }
	 */
	/**
	 * ����ַ���Ĭ�ϵ�prefs�ļ�
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void appendStrToDefaultPreferences(Context context,
			String key, String value) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		String prefer = pref.getString(key, null);

		SharedPreferences.Editor editor = pref.edit();
		if (prefer != null)
			value = prefer + "\n\n" + value;
		editor.putString(key, value);
		editor.commit();
	}

	public static boolean isScreenOn(Context context) {
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		return pm.isScreenOn();

	}

	public static void storeBooleanToDefaultPreferences(Context context,
			String key, Boolean value) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static void removeStrInDufaltPreferences(Context context, String key) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(key);
		editor.commit();
	}

	public static void storeStrToSharedPreferences(Context context,
			String fileName, String key, String value) {
		SharedPreferences sp = context.getSharedPreferences(fileName, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void removeStrToSharedPreferences(Context context,
			String fileName, String key) {
		SharedPreferences sp = context.getSharedPreferences(fileName, 0);
		SharedPreferences.Editor editor = sp.edit();
		editor.remove(key);
		editor.commit();
	}

	@SuppressWarnings("unchecked")
	/**
	 * ���ظ������ļ��б�������м�ֵ��?
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static Map<String, Object> getAllPairsFromSharedPreferences(
			Context context, String fileName) {
		SharedPreferences sp = context.getSharedPreferences(fileName, 0);
		if (sp == null)
			return null;
		return (Map<String, Object>) sp.getAll();
	}

	public static void waitAWhile(String tag, long time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.i(e.toString());
		}
	}

	/**
	 * // �����ޱ�����
	 * 
	 * @param a
	 */
	public static void setNoTitleBar(Activity a) {

		a.requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * ����ȫ��Ļ
	 * 
	 * @param a
	 */
	public static void setFullScreen(Activity a) {
		// ����ȫ�������?
		a.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		;
	}

	public static String getStringFromAssets(Context c, String fileName) {
		AssetManager am = c.getAssets();
		String s = null;
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(
					new InputStreamReader(am.open(fileName)));

			while ((s = reader.readLine()) != null)
				builder.append(s).append("\n");
			reader.close();
			return builder.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void playNotificationSound(Context c) throws Exception {
		MediaPlayer mp = new MediaPlayer();
		mp.reset();
		mp.setDataSource(c, RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
		mp.prepare();
		mp.start();
	}

	/**
	 * ���ص�ǰ������
	 * 
	 * @return
	 */
	public static String getCurrentDate() {
		java.util.Calendar c = java.util.Calendar.getInstance();
		java.text.SimpleDateFormat f = new java.text.SimpleDateFormat(
				"yyyyMMdd");
		return f.format(c.getTime());
	}

	/**
	 * 将aseets文件夹中的文件复制到sd卡，包含子文件夹中的文件
	 * 
	 * @param assetDir
	 *            asset目录
	 * @param sddir
	 *            sd卡目�?
	 */
	public static void CopyAssetsFileToSd(Context context, String assetDir,
			String sddir) {
		String[] files;
		try {
			files = context.getResources().getAssets().list(assetDir);
		} catch (IOException e1) {
			return;
		}
		File mWorkingPath = new File(sddir);
		// if this directory does not exists, make one.
		if (!mWorkingPath.exists()) {
			if (!mWorkingPath.mkdirs()) {

			}
		}

		for (int i = 0; i < files.length; i++) {
			try {
				String fileName = files[i];
				// we make sure file name not contains '.' to be a folder.
				if (!fileName.contains(".")) {
					if (0 == assetDir.length()) {
						CopyAssetsFileToSd(context, fileName, sddir + fileName
								+ "/");
					} else {
						CopyAssetsFileToSd(context, assetDir + "/" + fileName,
								sddir + fileName + "/");
					}
					continue;
				}
				File outFile = new File(mWorkingPath, fileName);
				if (outFile.exists())
					outFile.delete();
				InputStream in = null;
				if (0 != assetDir.length())
					in = context.getAssets().open(assetDir + "/" + fileName);
				else
					in = context.getAssets().open(fileName);
				OutputStream out = new FileOutputStream(outFile);

				// Transfer bytes from in to out
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}

				in.close();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getFileModifiedTime(File file) {
		Calendar cal = Calendar.getInstance();
		long time = file.lastModified();
		cal.setTimeInMillis(time);
		return cal.getTime().toLocaleString();
	}

	/**
	 * ����raw�ļ���sd��
	 * 
	 * @param c
	 * @param android
	 * @throws IOException
	 */
	public static void copyRawFileToSd(Context c, int rawId, String outPath)
			throws IOException {
		BufferedInputStream in = new BufferedInputStream(c.getResources()
				.openRawResource(rawId));
		byte[] buffer = new byte[2048];
		FileOutputStream out = new FileOutputStream(new File(outPath));
		int len = 0;
		while ((len = in.read(buffer)) != -1) {
			out.write(buffer, 0, len);
		}
		out.flush();
		out.close(); // TODO Auto-generated method stub

	}

	/**
	 * ����һ��choose�������ļ�
	 * 
	 * @param context
	 * @param path
	 *            �ļ���·��
	 */
	public static void startShareFileChooser(Context context, String path) {
		Intent in = new Intent(Intent.ACTION_SEND);
		File file = new File(path);
		in.setType("application/*");
		in.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		Intent chooser = Intent.createChooser(in, "ѡ���ͷ�ʽ");
		context.startActivity(chooser);
	}

	public static void sendMailByIntent(Context context) {
		String[] reciver = new String[] { "3529490@163.com" };
		String[] mySbuject = new String[] { context
				.getString(R.string.app_name) };
		String myCc = "cc";
		Intent myIntent = new Intent(android.content.Intent.ACTION_SEND);
		myIntent.setType("plain/text");
		myIntent.putExtra(android.content.Intent.EXTRA_EMAIL, reciver);
		myIntent.putExtra(android.content.Intent.EXTRA_CC, myCc);
		myIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mySbuject);
		myIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
		// ����Activity
		context.startActivity(Intent.createChooser(myIntent, "�����ʼ�..."));

	}

	/**
	 * �滻�ļ��е��ַ�
	 * 
	 * @param strsFilePath
	 *            �ļ�·��
	 * @param holder
	 *            ���滻���ַ�
	 * @param replace
	 *            �滻�ַ�
	 */
	public static void repalceStrInFile(String strsFilePath, String holder,
			String replace) {
		File strsFile = new File(strsFilePath);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(strsFile));
			String line = null;
			StringBuffer buffer = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				if (line.contains(holder)) {// �����а�Ӧ����Ƶ�λ���?
					line = line.replace(holder, replace);
					Logger.i(holder + " is placed by " + replace);
				}
				buffer.append(line).append("\n");
			}
			FileWriter writer = new FileWriter(strsFile);
			writer.write(buffer.toString());
			reader.close();
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ��Ԓ��ȷ�����������?
	public static class DialogDismissBtnListener implements
			DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.cancel();
		}

	}

	public static String getCurrentTimeString() {
		String time = new SimpleDateFormat("yyMMddHHmm").format(new Date());
		return time;
	}

	public static void showSDCardCannotAccessDilog(Context context,
			OnClickListener onClickListener) {
		/*
		 * Builder builder = getDialogBuilder(context,
		 * context.getString(R.string.title_canotAccessSd),
		 * context.getString(R.string.body_canontAccessSD));
		 * builder.setIcon(android.R.drawable.ic_dialog_alert);
		 * builder.setNegativeButton(R.string.sure, onClickListener);
		 * builder.show();
		 */
	}

	public static void shareToWeiXin(Context context) {
		/*
		 * Intent intent = new Intent(); ComponentName comp = new
		 * ComponentName("com.tencent.mm",
		 * "com.tencent.mm.ui.tools.ShareToTimeLineUI");
		 * intent.setComponent(comp);
		 * intent.setAction("android.intent.action.SEND");
		 * intent.setType("image/*"); File file = new File(G.Ad_image_path +
		 * "ad.jpg"); Loger.i(" file " + file.getAbsolutePath());
		 * intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file)); try {
		 * context.startActivity(intent); } catch (Exception e) {
		 * e.printStackTrace(); makeToast(context, "分享失败，请查检是否安装了微", true); }
		 */
	}

	public static boolean isFirstBoot(Context c) {
		boolean isFirst = getBooleanFromDufaultpref(c, "isFirstBoot", true);
		if (isFirst) {
			storeBooleanToDefaultPreferences(c, "isFirstBoot", false);
		}
		return isFirst;

	}

	public static int getBootTimes(Context c) {
		int palyTime = MyUtil.getIntFromDufaultpref(c, "playTime", 0);
		palyTime++;
		Logger.i("第" + palyTime + "次启动");
		MyUtil.storeToDefaultPreferences(c, "playTime", palyTime);
		return palyTime;
	}

	public static void showRateDialog(final Activity context) {
		/*
		 * Builder buidler = MyUtil.getDialogBuilder(context,
		 * context.getString(R.string.tileRate),
		 * context.getString(R.string.contentRate));
		 * buidler.setPositiveButton(R.string.ok, new OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 * AppUtil.jumpToMarketDownloadInterface(context); } });
		 * buidler.setNegativeButton(R.string.no, new OnClickListener() {
		 * 
		 * @Override public void onClick(DialogInterface dialog, int which) {
		 * context.finish(); } }); buidler.show();
		 */

	}

	public static long getAvailaleSize() {

		File path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径

		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
		// (availableBlocks * blockSize)/1024 KIB 单位
		// (availableBlocks * blockSize)/1024 /1024 MIB单位
	}

	/**
	 * 隐藏软键盘
	 * 
	 * @param activity
	 */
	public static void hideSoftKeyboad(Activity activity) {
		((InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(activity.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public static void showSDAvaildSize(Context context) {
		long size = MyUtil.getAvailaleSize();
		Logger.i("SD 剩余空间 " + size / (1024 * 1024));
		MyUtil.makeToast(context, "SD 剩余空间 " + (size / (1024 * 1024) + "M"),
				false);
	}

}