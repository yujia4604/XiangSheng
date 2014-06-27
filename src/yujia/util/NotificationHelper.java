package yujia.util;

import yujia.xiangsheng.G;
import yujia.xiangsheng.MainActivity;
import yujia.xiangsheng.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class NotificationHelper {

	

	@SuppressWarnings({ "unchecked", "deprecation" })
	public static Notification sendNotify(Context context, Class<?> targetClass) {
		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification n = new Notification(R.drawable.ic_launcher, "Wifi Fly",
				System.currentTimeMillis());
		n.flags = Notification.FLAG_ONGOING_EVENT;
		Intent i = new Intent(context, targetClass);
		// i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
		// | Intent.FLAG_ACTIVITY_NEW_TASK);
		// PendingIntent
		PendingIntent contentIntent = PendingIntent.getActivity(context, R.string.app_name, i,
				PendingIntent.FLAG_UPDATE_CURRENT);

		n.setLatestEventInfo(context, " Wifi Fly", "程序正在运行，点击进入主界面.", contentIntent);
		nm.notify(R.string.app_name, n);
		return n;
	}

	public static void cancelNotify(Context context) {
		NotificationManager nm = (NotificationManager) context.getApplicationContext()
				.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(R.string.app_name);
		nm.cancelAll();
	}

	/**
	 * 发送一个可取消的消息
	 * 
	 * @param context
	 * @param targetClass
	 * @param title
	 * @param msg
	 */
	public static void sendNotify(Context context, Class<?> targetClass, String title, String msg) {
		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification n = new Notification(R.drawable.ic_launcher, "Wifi Fly",
				System.currentTimeMillis());
		Intent i = new Intent(context, targetClass);
		// PendingIntent
		PendingIntent contentIntent = PendingIntent.getActivity(context, R.string.app_name, i,
				PendingIntent.FLAG_UPDATE_CURRENT);

		n.setLatestEventInfo(context, title, msg, contentIntent);
		n.flags = Notification.FLAG_AUTO_CANCEL;
		nm.notify(R.string.app_name, n);

	}

	@SuppressWarnings("deprecation")
	public static void sendNotify(Context context, int priceUpBaobei, int priceDownBaobei) {
		Logger.i("sendNotify called up "+priceUpBaobei+" down "+priceDownBaobei);
		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher,
				context.getString(R.string.app_name), System.currentTimeMillis());
		// 为通知加入默认提示音
		notification.defaults = Notification.DEFAULT_SOUND;
		notification.flags = Notification.FLAG_AUTO_CANCEL | Notification.FLAG_SHOW_LIGHTS;
		Intent i = new Intent(context, MainActivity.class);
		// PendingIntent
		PendingIntent contentIntent = PendingIntent.getActivity(context, R.string.app_name, i,
				PendingIntent.FLAG_UPDATE_CURRENT);

		notification.setLatestEventInfo(context, context.getString(R.string.app_name), getContent(priceUpBaobei, priceDownBaobei), contentIntent);
		nm.notify(R.string.app_name, notification);
	}

	

	public static void sendNotifyByBuilder(Context context, int priceUpBaobei, int priceDownBaobei) {
		Logger.i("sendNotifyByBuilder called up "+priceUpBaobei+" down "+priceDownBaobei);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(context.getString(R.string.app_name))
				.setContentText(getContent(priceUpBaobei, priceDownBaobei));
		mBuilder.setTicker(getContent(priceUpBaobei, priceDownBaobei));
		mBuilder.setAutoCancel(true);
	/*	if (MainActivity.isPlayMsgSound)
			mBuilder.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND);
		else*/
			mBuilder.setDefaults(Notification.DEFAULT_LIGHTS);
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(context, MainActivity.class);
		
		

		// The stack builder object will contain an artificial back stack for
		// the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(MainActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(R.string.app_name, mBuilder.build());
	}

	private static String getContent(int priceUpBaobei,
			int priceDownBaobei) {
			StringBuffer sb=new StringBuffer();
			//sb.append("主人，您的聚宝盆中");
			if(priceDownBaobei!=0&&priceUpBaobei!=0){
				sb.append(priceDownBaobei+"个宝贝降价");
				sb.append(",");
				sb.append(priceUpBaobei+"个宝贝涨价");
			}else if(priceDownBaobei==0){
				sb.append(priceUpBaobei+"个宝贝涨价");
			}else{
				sb.append(priceDownBaobei+"个宝贝降价");
			}
			sb.append(",快去看看吧！");
		return sb.toString()
				;
	}
}
