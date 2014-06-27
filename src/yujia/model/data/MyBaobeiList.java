package yujia.model.data;

import java.util.ArrayList;

import yujia.model.Baobei;

public class MyBaobeiList extends ArrayList<Baobei> {

	private static final long serialVersionUID = 1L;

	private MyBaobeiList prePageList;
	private MyBaobeiList NextPageList;

	public MyBaobeiList getPrePageList() {
		return prePageList;
	}

	public void setPrePageList(MyBaobeiList prePageList) {
		this.prePageList = prePageList;
	}

	public MyBaobeiList getNextPageList() {
		return NextPageList;
	}

	public void setNextPageList(MyBaobeiList nextPageList) {
		NextPageList = nextPageList;
	}


}
