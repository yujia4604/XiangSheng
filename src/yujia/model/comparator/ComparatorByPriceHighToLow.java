package yujia.model.comparator;

import java.util.Comparator;

import yujia.model.Baobei;

public class ComparatorByPriceHighToLow implements Comparator<Baobei> {

	@Override
	public int compare(Baobei lhs, Baobei rhs) {
		
		String p1Str = ComparatorByPriceLowToHigh.removeUnsuppotString(lhs);
		String p2Str = ComparatorByPriceLowToHigh.removeUnsuppotString(rhs);
		Double s1 = Double.valueOf(p1Str);
		Double s2 = Double.valueOf(p2Str);
		return s1 - s2 < 0 ? 1 : (s1 - s2 == 0 ? 0 : -1);
	}
}
