package yujia.xiangsheng;

import java.util.ArrayList;

import yujia.ad.WapsAdUtil;
import yujia.model.Works;
import android.content.Context;
import android.os.Environment;

public class G {
	public static Context context;
	public static ArrayList<Works> worklist;
	public static boolean isFirstBoot=true;
	public static final String HOST = "http://192.168.1.10:8888/";
	//public static final String HOST ="http://drawwords.mhobaike.com/";
	public static final String URL_SEARCH = HOST + "xiangsheng/search";
	public static final String URL_DOWNLOAD_PREFIX = HOST
			+ "xiangsheng/download?path=";
	public static final String LABEL_REQUEST_SUCCESS = "requestSuccess";
	public static final String XiangShengDir = Environment
			.getExternalStorageDirectory() + "/xiangsheng/";
	/**
	 * 边播边下载时，不一次性下载完成，而是每次下载固定常量的资源
	 */
	public static final int DOWNLOAD_STERAM_CACHE_SIZE = 64 * 1024 * 60*2 / 8;
	public static final int INTIAL_KB_BUFFER = 64 * 60 / 8;// 初始缓冲（60秒）

	// 96kbps*10secs/8bits
	// per byte

	public static void onCreate(MainActivity mainActivity) {
		G.context = mainActivity;
		WapsAdUtil.loadWapsAds(mainActivity);

		// 隐藏android移动设备的user Agent属性，使淘宝不会跳转至手机版

		/**
		 * 由于使用urlconntection 访问淘宝网页时报“java.net.ProtocolException: redirected
		 * too many times错误 网上说明如下，需要使用cookie保存用户session It's apparently
		 * redirecting in an infinite loop because you don't maintain the user
		 * session. The session is usually backed by a cookie. You need to
		 * create a CookieManager before you use URLConnection.
		 * 
		 * 
		 */

		MyPreferenceActivity.loadPrefs(context);
		/*
		 * MyUtil.copyFileInSd(context, "/data/data/" + context.getPackageName()
		 * + "/databases/dianpu", Environment.getExternalStorageDirectory() +
		 * "/dianpu.db");
		 */
	}

	public static void onDestroy() {
		WapsAdUtil.releaseAdResource(context);
		context = null;
		worklist = null;
	}

}
