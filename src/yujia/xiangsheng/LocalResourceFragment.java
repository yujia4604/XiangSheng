package yujia.xiangsheng;

import java.io.File;

import yujia.model.Baobei;
import yujia.util.Logger;
import yujia.util.MyUtil;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.openapi.v2.AppConnect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

public class LocalResourceFragment extends Fragment {

	protected String spinnerFilter;
	private View fragmentLayout;
	private String[] spinerFilterArray;
	private ListView listView;
	private String[] fileNames;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Logger.i("MybaobeilistActivity oncreate called");
		fragmentLayout = View.inflate(getActivity(),
				R.layout.local_res_fragment, null);
		LinearLayout adlayout = (LinearLayout) fragmentLayout
				.findViewById(R.id.AdLinearLayout);
		AppConnect.getInstance(getActivity()).showBannerAd(getActivity(),
				adlayout);
		listView = (ListView) fragmentLayout.findViewById(R.id.listView);
		return fragmentLayout;
	}

	@Override
	public void onStart() {
		super.onStart();
		Logger.i("mybaobeilist fragment  onstart called");
		updateLocalResList();
	}

	@Override
	public void onResume() {
		super.onResume();
		Logger.i("mybaobeilist fragment  onResume called");
	}

	public void updateLocalResList() {
		if (isLocalXiangdShengResEmpty()) {// 检查宝贝列表是否为空
			MyUtil.makeToast(getActivity(), "暂无本地资源", false);
			changeToAddBaobeiTab();
		} else {
			setLocalResList();
		}
	}

	@SuppressWarnings("deprecation")
	private void changeToAddBaobeiTab() {
		Logger.i("changeToAddBaobeiTab called");
		((MainActivity) getActivity()).mViewPager.setCurrentItem(1);
	}

	private boolean isLocalXiangdShengResEmpty() {
		File dir = new File(G.XiangShengDir);
		if (!dir.exists())
			return true;
		String[] fileNames = dir.list();
		if (fileNames == null || fileNames.length == 0)
			return true;

		return false;
	}

	private void setLocalResList() {
		fileNames = new File(G.XiangShengDir).list();
		// fileNames = removeSuffix(fileNames);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, fileNames);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Logger.i("local list item click" + position);
				Intent it = new Intent(getActivity(), PlayActivity.class);
				it.putExtra("name", fileNames[position]);
				getActivity().startActivity(it);

			}
		});

		listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {

				Builder builder = MyUtil.getDialogBuilder(getActivity(),
						"是否删除本文件?", fileNames[arg2]);
				builder.setPositiveButton("确定", new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						File file = new File(G.XiangShengDir + fileNames[arg2]);
						if (file.delete()) {
							MyUtil.makeToast(getActivity(), "删除成功", false);
						} else {
							MyUtil.makeToast(getActivity(), "删除失败", false);
						}
						setLocalResList();
					}

				});

				builder.setNegativeButton("取消",
						new MyUtil.DialogDismissBtnListener());
				builder.show();
				return true;
			}
		});
	}

	private String[] removeSuffix(String[] fileNames) {
		for (int i = 0; i < fileNames.length; i++) {
			if (fileNames[i] != null && fileNames[i].contains(".ogg")) {
				fileNames[i] = fileNames[i].substring(0,
						fileNames[i].length() - 4);
			}
		}
		return fileNames;
	}

	protected void showGoToBuyOrDetailDialog(final Baobei baobei) {

	}

	protected void showIfDownloadDetailDialog(final Baobei baobei) {

	}

	protected void showIfDeleteItemDialog(final Baobei baobei) {
		Builder builder = MyUtil.getDialogBuilder(getActivity(),
				"是否删除宝贝及宝贝详情?", null);
		builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.setNegativeButton("取消", new MyUtil.DialogDismissBtnListener());
		builder.show();

	}

	protected void deleteBaobeiInDBTable(Baobei baobei) {

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	public void onPriceChanged() {
		Logger.i(this, "onPriceChanged called");

	}

	private void showBaobeiDetail(Baobei baobei) {
		Intent intent = new Intent(G.context, PlayActivity.class);
		intent.putExtra("itemid", baobei.getItemId());
		G.context.startActivity(intent);
	}

}
