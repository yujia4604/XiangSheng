package yujia.ad;

import yujia.util.Logger;
import yujia.xiangsheng.R;
import android.app.Activity;
import android.content.Context;
import android.openapi.v2.AdInfo;
import android.openapi.v2.AppConnect;
import android.openapi.v2.UpdatePointsNotifier;
import android.os.Handler;
import android.os.Message;
import android.widget.LinearLayout;

/**
 * 1.添加所需求许可 uses-permission android:name="android.permission.INTERNET" />
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
 * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
 * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
 * <uses-permission android:name="android.permission.GET_TASKS" />
 * ⑵在<application>段内添加以下内容： <activity android:name="com.waps.OffersWebView"
 * android:configChanges="keyboardHidden|orientation"/> <!--以下属性为应用ID,从万普后台获取-->
 * <meta-data android:name="WAPS_ID" android:value="应用标识" />
 * <!--以下属性为分发渠道ID,编码表参见本文档末附表--> <meta-data android:name="WAPS_PID"
 * android:value="分发渠道标识" />
 * 
 * 3.调用lloadWapsAds方法初始化万普广告 /> 4.在需要调用积分的activity中创建wapsAdUtilites实例
 */
public abstract class WapsAdUtil extends Activity implements
		UpdatePointsNotifier {
	private static final String tag = "wapAdUtilis";
	public Context context;
	private int points;
	private static long lastGetTime;
	public static boolean isWhiteUser;

	/**
	 * 初始化万普积分墙
	 * 
	 * @param c
	 */
	public static void loadWapsAds(final Context c) {
		AppConnect.getInstance(c); // 万普广告初始化
		//AppConnect.getInstance("abe7a980ce53870ba07cc7b456bcce6a", "hiapk", c);

	//	AppConnect.getInstance(c).initAdInfo();// 初始化（预先加载）自定义广告数据
		/*
		 * //默认为true,开发者可通过代码开启错误报告功能，并通过管理后台的“错误报告”功能随时查看收集到的错误报告，以便及时进行修正。
		 */
		AppConnect.getInstance(c).setCrashReport(false);
		Logger.i("初始化万普广告");

		// 初始化统计器(必须调用)
		//PayConnect.getInstance("abe7a980ce53870ba07cc7b456bcce6a", "goapk", c);
		// 初始化功能广告
		AppConnect.getInstance(c).initFunAd(c);

		/*
		 * new Thread(new Runnable() {
		 * 
		 * @Override public void run() { try { Thread.sleep(10000); } catch
		 * (Exception exceptio) { exceptio.printStackTrace(); }
		 */
		String isWhiteUser = AppConnect.getInstance(c).getConfig(
				"IS_WHITE_USER");
		Logger.i("isWhileUser = " + isWhiteUser);
		if (isWhiteUser != null
				&& (isWhiteUser.equals("true") || isWhiteUser.equals("")))
			WapsAdUtil.isWhiteUser = true;
		/*
		 * } }).start();
		 */
	}

	public WapsAdUtil(Context c) {
		context = c;
		loadWapsAds(c);
	}

	protected Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) {// 当接收积分成功时的处理
				Logger.i("get points " + msg.arg1);
				onGetPointsFromWapsServer(msg.arg1);
			} else if (msg.what == 1) {
				Logger.i("getupdataFailed");

			}
		}

	};

	public static AdInfo getWapsCustomAdInfos(Context c) {
		return AppConnect.getInstance(c).getAdInfo();// 每次调用将自动轮换广告

	}

	/**
	 * 当接收积分成功时的处理
	 * 
	 * @param points
	 */
	public abstract void onGetPointsFromWapsServer(int points);

	/**
	 * 显示留言对话框
	 * 
	 * @param context
	 */
	public static void showFeedbackActivity(Context context) {
		AppConnect.getInstance(context).showFeedback(context);
	}

	/**
	 * 释放万普广告资源
	 * 
	 * @param c
	 */
	public static void releaseAdResource(Context c) {
		AppConnect.getInstance(c).close();
	}

	/**
	 * 手动推送广告
	 * 
	 * @param context
	 * @param pointsName
	 *            积分在程序中的实际名称
	 */
	public static void pushAd(Context context, String pointsName) {
		/*
		 * GregorianCalendar c = new GregorianCalendar(); int day =
		 * c.get(Calendar.DAY_OF_MONTH); int oday =
		 * getIntFromSharedPreferences(context, "data", "day"); if (day == oday)
		 * { return; } AdInfo adInfo = getWapsCustomAdInfos(context); if (adInfo
		 * == null) return; // sendAdNotification(context, adInfo, pointsName);
		 * storeIntToSharedPreferences(context, "data", "day", day);
		 */
	}

	/**
	 * 显示更多自家应用
	 * 
	 * @param context
	 */
	public static void showMoreAppsByMe(Context context) {
		AppConnect.getInstance(context).showMore(context);
	}

	// public static void sendAdNotification(Context context, AdInfo adInfo,
	// String pointsName) {
	// String text = adInfo.getAdName() + " " + adInfo.getAdText() +
	// context.getString(R.string.get)
	// + adInfo.getAdPoints() + pointsName;
	// NotificationManager nm = (NotificationManager) context
	// .getSystemService("notification");
	// Notification notification = new Notification(R.drawable.ic_launcher,
	// text, System.currentTimeMillis());
	// notification.flags = Notification.FLAG_AUTO_CANCEL;
	// Intent intent = new Intent(context, AdDialogActivity.class);
	// intent.putExtra("id", adInfo.getAdId());
	// PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
	// intent, 0);
	// // must set this for content view, or will throw a exception
	// notification.setLatestEventInfo(context, adInfo.getAdName(), adInfo
	// .getAdText()
	// + context.getString(R.string.get) + adInfo.getAdPoints() + pointsName,
	// contentIntent);
	// nm.notify(0, notification);
	// }

	/**
	 * 当接收到服务器返回的积分数时
	 */
	@Override
	public void getUpdatePoints(String arg0, int arg1) {
		Logger.i("wap get poins suuceded " + arg1);
		if (arg1 > 0) {
			Message m = new Message();
			m.what = 0;// 接收成功
			m.arg1 = arg1;
			AppConnect.getInstance(context).spendPoints(arg1, this);// 消耗掉服务器上的金币数
			mHandler.sendMessage(m);
		}
	}

	@Override
	public void getUpdatePointsFailed(String arg0) {
		Logger.i("wap get poins failed " + arg0);
		Message m = mHandler.obtainMessage(1, arg0);
		mHandler.sendMessage(m);
	}

	/**
	 * 异步获取服务器上的积分
	 */
	public void updatePointsOnSeaver() {
		AppConnect.getInstance(context).getPoints(this);
	}

	/**
	 * 显示积分墙
	 * 
	 * @param c
	 */
	public static void showAppOffer(Context c) {
		Logger.i("显示积分墙");
		AppConnect.getInstance(c).showOffers(c);
	}
	
	/**
	 * 显示互动广告条
	 * @param c 显示广告的activity
	 */
	public static void showHuDongAD(Activity c){
		LinearLayout adlayout =(LinearLayout)c.findViewById(R.id.AdLinearLayout); 
		AppConnect.getInstance(c).showBannerAd(c, adlayout); 
	}

	public static void checkUpdate(Context c) {
		AppConnect.getInstance(c).checkUpdate(c);
	}

	

	/**
	 * 自定义Listener实现PaySuccessListener，用于监听支付成功
	 * 
	 * @author Administrator
	 * 
	 */

	public static void gotoUrl(Context c, String url) {
		Logger.i("waps goto url = "+url);
		AppConnect.getInstance(c).showBrowser(c, url);
	}
}
