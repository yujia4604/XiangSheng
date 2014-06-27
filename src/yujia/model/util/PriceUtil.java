package yujia.model.util;

public class PriceUtil {

	public static Float getPriceValue(String oldPrice) {
			if(oldPrice.contains("元"))
				oldPrice=oldPrice.replace("元", "");
		return Float.valueOf(oldPrice);
	}

}
