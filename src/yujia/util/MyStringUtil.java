package yujia.util;

public class MyStringUtil {

	public static String getFormatTime(long millis) {

//		String output = "00:00:000";
		String output = "00:00";
		long mills = millis % 1000;
		long seconds = millis / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;

		seconds = seconds % 60;
		minutes = minutes % 60;
		hours = hours % 60;

		String secondsD = String.valueOf(seconds);
		String minutesD = String.valueOf(minutes);
		String hoursD = String.valueOf(hours);
		String millsD = String.valueOf(mills);

		if (seconds < 10)
			secondsD = "0" + seconds;
		if (minutes < 10)
			minutesD = "0" + minutes;
		if (mills < 100) {
			if (mills < 10) {
				millsD = "00" + millsD;
			} else
				millsD = "0" + millsD;
		}
		if (hours == 0)
			return minutesD + ":" + secondsD ;
		output = hoursD + ":" + minutesD + ":" + secondsD;
		return output;
	}

}
