package yujia.util;

import org.jsoup.nodes.Element;

import android.util.Log;

public class Logger {
	private static final String tag = "xiangsheng";

	public static void i(String str) {
		Log.i(tag, str);
	}

	public static void i(Object str) {
		Log.i(tag, str.toString());
	}

	public static void log(double i) {
		i("" + i);
	}

	public static void ptSilder() {
		i("--------------------------------------");
	}

	public static void e(String string) {
		Log.e(tag, string);
	}

	public static Logger getLogger(Class<?> class1) {

		return new Logger();
	}

	public void info(String string) {
		System.out.println(string);
	}

	public void error(String string) {
		System.out.println(string);
	}

	public static Logger getLogger(String name) {
		return null;
	}

	public void warn(String string) {

	}

	public static void i(Object object, Object string) {
		Logger.i(object.getClass().getName() + ": " + string);
	}
}
