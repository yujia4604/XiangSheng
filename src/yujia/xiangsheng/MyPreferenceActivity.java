package yujia.xiangsheng;

import yujia.util.Logger;
import yujia.util.MyUtil;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

public class MyPreferenceActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	public static Boolean isBGmsgPermit;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		Logger.i("key = " + key);

		Boolean value = MyUtil.getValueFromDufaultpref(this, key, false);
		Logger.i(key + " value  = " + value);
		Preference pef = findPreference(key);
		if (key.equals(getString(R.string.bgmsg)))
			isBGmsgPermit = value;
	}

	public static void loadPrefs(Context context) {
		isBGmsgPermit = MyUtil.getBooleanFromDufaultpref(context,
				context.getString(R.string.bgmsg), true);
		Logger.i("loadprefs called bgmsg= " + isBGmsgPermit);
	}

}
