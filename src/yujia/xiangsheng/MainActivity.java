package yujia.xiangsheng;

import java.io.InputStream;
import java.util.ArrayList;

import yujia.util.DatabaseHelper;
import yujia.util.HttpUtil;
import yujia.util.Logger;
import yujia.util.MyUtil;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.SearchRecentSuggestions;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends ActionBarActivity {

	public ViewPager mViewPager;
	private TabsAdapter mTabsAdapter;
	private ArrayList<Fragment> fragmentList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.i(" MainActivity oncreate called");
		G.onCreate(this);
		if (MyUtil.isFirstBoot(this)) {
		} else {// 当不是第一次启动时
		}
		setContentView(R.layout.fragment_pager);
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.pager);
		setContentView(mViewPager);

		final ActionBar bar = getSupportActionBar();
		bar.setDisplayShowHomeEnabled(false);// 隐藏ACTIONBAR的主界面即lcon和标题栏
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

		mTabsAdapter = new TabsAdapter(this, mViewPager);
		mTabsAdapter.addTab(bar.newTab().setText("本地资源"),
				LocalResourceFragment.class, null);
		mTabsAdapter.addTab(bar.newTab().setText("在线资源"),
				OnlineResouceFragment.class, null);
		/*
		 * mTabsAdapter.addTab(bar.newTab().setText("设置"), CursorFragment.class,
		 * null);
		 */

		if (savedInstanceState != null) {
			bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
		}

	}

	private void createDBAndExecuteSql() {

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt("tab", getSupportActionBar()
				.getSelectedNavigationIndex());
	}

	/**
	 * This is a helper class that implements the management of tabs and all
	 * details of connecting a ViewPager with associated TabHost. It relies on a
	 * trick. Normally a tab host has a simple API for supplying a View or
	 * Intent that each tab will show. This is not sufficient for switching
	 * between pages. So instead we make the content part of the tab host 0dp
	 * high (it is not shown) and the TabsAdapter supplies its own dummy view to
	 * show as the tab content. It listens to changes in tabs, and takes care of
	 * switch to the correct paged in the ViewPager whenever the selected tab
	 * changes.
	 */
	public static class TabsAdapter extends FragmentPagerAdapter implements
			ActionBar.TabListener, ViewPager.OnPageChangeListener {
		private final Context mContext;
		private final ActionBar mActionBar;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

		static final class TabInfo {
			private final Class<?> clss;
			private final Bundle args;

			TabInfo(Class<?> _class, Bundle _args) {
				clss = _class;
				args = _args;
			}
		}

		public TabsAdapter(ActionBarActivity activity, ViewPager pager) {
			super(((FragmentActivity) activity).getSupportFragmentManager());
			mContext = activity;
			mActionBar = activity.getSupportActionBar();
			mViewPager = pager;
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
			TabInfo info = new TabInfo(clss, args);
			tab.setTag(info);
			tab.setTabListener(this);
			mTabs.add(info);
			mActionBar.addTab(tab);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}

		@Override
		public Fragment getItem(int position) {
			TabInfo info = mTabs.get(position);
			Fragment f = Fragment.instantiate(mContext, info.clss.getName(),
					info.args);
			((MainActivity) mContext).getFragmentList().add(position, f);
			return f;
		}

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			mActionBar.setSelectedNavigationItem(position);
			Logger.i("onpageselected " + position);
			if (position == 0) {
				LocalResourceFragment fragment = (LocalResourceFragment) ((MainActivity) mContext)
						.getFragmentList().get(0);
				fragment.updateLocalResList();
			}

		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}

		@Override
		public void onTabReselected(Tab arg0,
				android.support.v4.app.FragmentTransaction arg1) {

		}

		@Override
		public void onTabSelected(Tab tab,
				android.support.v4.app.FragmentTransaction arg1) {
			Object tag = tab.getTag();
			for (int i = 0; i < mTabs.size(); i++) {
				if (mTabs.get(i) == tag) {
					mViewPager.setCurrentItem(i);
				}
			}
		}

		@Override
		public void onTabUnselected(Tab arg0,
				android.support.v4.app.FragmentTransaction arg1) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		Logger.i("onstart called");
	}

	public ArrayList<Fragment> getFragmentList() {

		if (fragmentList == null)
			fragmentList = new ArrayList<Fragment>(2);
		return fragmentList;
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		Logger.i("new Intent called");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the options menu from XML
		getMenuInflater().inflate(R.menu.options_menu, menu);
		return true;
	}

	/*
	 * @Override public boolean onMenuItemSelected(int featureId, MenuItem item)
	 * {
	 * 
	 * }
	 */

	@Override
	protected void onDestroy() {
		super.onDestroy();
		G.onDestroy();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.m_setting) {
			startActivity(new Intent(this, MyPreferenceActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * 点击排序按键时调用
	 * 
	 * @param v
	 */
	public void onSortButtonClicked(View v) {
		OnlineResouceFragment fragment = (OnlineResouceFragment) getSupportFragmentManager()
				.findFragmentById(R.id.pager);
	}

	public void onPriceChanged() {
		((LocalResourceFragment) getFragmentList().get(0)).onPriceChanged();
	}
}
