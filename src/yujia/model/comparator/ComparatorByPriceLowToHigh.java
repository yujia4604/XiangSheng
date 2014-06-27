package yujia.model.comparator;

import java.util.Comparator;

import yujia.model.Baobei;

public class ComparatorByPriceLowToHigh implements Comparator<Baobei> {

	@Override
	public int compare(Baobei lhs, Baobei rhs) {
		String p1Str = removeUnsuppotString(lhs);
		String p2Str = removeUnsuppotString(rhs);
		Double s1 = Double.valueOf(p1Str);
		Double s2 = Double.valueOf(p2Str);
		return s1 - s2 < 0 ? -1 : (s1 - s2 == 0 ? 0 : 1);
	}

	public static String removeUnsuppotString(Baobei lhs) {
		String p1Str = lhs.getPrice();
		if (p1Str.contains("元"))
			p1Str = p1Str.split("元")[0];
		return p1Str;
	}
}
