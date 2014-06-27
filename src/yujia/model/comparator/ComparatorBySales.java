package yujia.model.comparator;

import java.util.Comparator;

import yujia.model.Baobei;

public class ComparatorBySales implements Comparator<Baobei> {

	@Override
	public int compare(Baobei lhs, Baobei rhs) {
		int s1 = (lhs.getSellCount());
		int s2 = rhs.getSellCount();
		return s2 - s1;
	}
}
