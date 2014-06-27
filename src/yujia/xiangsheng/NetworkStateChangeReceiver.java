package yujia.xiangsheng;

import yujia.util.Logger;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStateChangeReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals("android.net.conn.CONNECTIVITY_CHANGE")) {
			ConnectivityManager manager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo gprs = manager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo wifi = manager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			Logger.i("network openned mobile = " + gprs.isConnected()
					+ " wifi =" + wifi.isConnected());
			if (!gprs.isConnected() && !wifi.isConnected()) {
				Logger.i("network closed");
			} else if (wifi.isConnected()){// 当打开手机网络时
				MyPreferenceActivity.loadPrefs(context);
				if(MyPreferenceActivity.isBGmsgPermit){
					Logger.i("接收推送通知 ");
					G.context= context;
				}else{
					Logger.i("不接收推送通知");
				}

			}

		}
	}

}
