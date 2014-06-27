package yujia.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ImageUtil {

	public static Bitmap getBitmapByByteArray(byte[] bytes) {
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

	}

}
