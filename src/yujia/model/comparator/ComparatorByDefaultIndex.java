package yujia.model.comparator;

import java.util.Comparator;

import yujia.model.Baobei;

public class ComparatorByDefaultIndex implements Comparator<Baobei> {

	@Override
	public int compare(Baobei lhs, Baobei rhs) {
		int s1 = lhs.getDefalutIndex();
		int s2 = rhs.getDefalutIndex();
		return   s1-s2;
	}
}
