package yujia.xiangsheng;

import java.net.URLEncoder;
import java.util.Collections;

import yujia.model.Shop;
import yujia.model.UpdateOnlineResAsyncTask;
import yujia.model.comparator.ComparatorByDefaultIndex;
import yujia.model.comparator.ComparatorByPriceHighToLow;
import yujia.model.comparator.ComparatorByPriceLowToHigh;
import yujia.model.comparator.ComparatorBySales;
import yujia.model.data.MyBaobeiList;
import yujia.util.Logger;
import yujia.util.MyUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;

public class OnlineResouceFragment extends Fragment {

	private static final int SEARCH_BAOBEI = 0;
	private static final int SEARCH_SHOP = 1;
	private Spinner spinner;
	private View fragmentLayout;
	private SearchView searchView;

	public View getFragmentLayout() {
		return fragmentLayout;
	}

	public void setFragmentLayout(View fragmentLayout) {
		this.fragmentLayout = fragmentLayout;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.i("online res oncreate called");
	}

	@Override
	public void onStart() {
		super.onStart();
		Logger.i("online res onStart called");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Logger.i("online res onDestroyview called");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Logger.i("online res oncreateView called");
		fragmentLayout = inflater.inflate(R.layout.online_res_fragment, null);
		ListView listview = (ListView) fragmentLayout
				.findViewById(R.id.listView);
		updateOnlineRes(listview);
		return fragmentLayout;
	}

	private void updateOnlineRes(ListView listview) {
		new UpdateOnlineResAsyncTask(getActivity()).execute(listview);
	}

	public void search(String searchTxt) {

		String key = (String) spinner.getSelectedItem();
		Logger.e(" key = " + key + " , keyword = " + searchTxt);
		if (searchTxt == null || searchTxt.equals("")) {
			MyUtil.makeToast(this, "请先输入要搜索的关键字", false);
			return;
		} else {
			String encodeTxt = URLEncoder.encode(searchTxt);
			Logger.i(" encodeTxt = " + encodeTxt);
			if (key.equals("宝贝")) {
				searchBaobei(encodeTxt);
			} else {
				searchShop(encodeTxt);
			}
		}
	}

	protected void searchShop(String searchTxt) {
		String url = "http://s.taobao.com/search?app=shopsearch&q=" + searchTxt;
	}

	protected void searchBaobei(String searchTxt) {

	}

	@Override
	public void onResume() {
		super.onResume();
		Logger.i("online res onResume called");
	}

}
