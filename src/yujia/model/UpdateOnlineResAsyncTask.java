package yujia.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import yujia.util.HttpUtil;
import yujia.util.Logger;
import yujia.util.MyUtil;
import yujia.xiangsheng.G;
import yujia.xiangsheng.MainActivity;
import yujia.xiangsheng.PlayActivity;
import yujia.xiangsheng.R;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class UpdateOnlineResAsyncTask extends BaseAsyncTask {

	private ListView listview;
	private MainActivity mainActivity;
	private ArrayList<Works> worksList;

	public UpdateOnlineResAsyncTask(Context context) {
		super(context);
		this.mainActivity = (MainActivity) context;
	}

	@Override
	protected Integer doInBackground(Object... params) {
		listview = (ListView) params[0];
		try {
			String jsonString = HttpUtil.getStringByGet(G.URL_SEARCH);
			Logger.i("search result = " + jsonString);
			JSONObject json = new JSONObject(jsonString);
			boolean isSuccess = json.getBoolean(G.LABEL_REQUEST_SUCCESS);
			if (!isSuccess)
				return -1;
			JSONArray worksArray = json.getJSONArray("works");
			int num = worksArray.length();
			Logger.i("works num " + num);
			worksList = new ArrayList<Works>();
			for (int i = 0; i < num; i++) {
				JSONObject jo = (JSONObject) worksArray.get(i);
				Works works = new Works();
				works.setId(jo.getInt("id"));
				works.setName(jo.getString("name"));
				works.setLength(jo.getInt("length"));
				works.setSize(jo.getInt("size"));
				works.setPath(jo.getString("path"));
				worksList.add(works);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		return 1;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		if (result == -1) {
			MyUtil.makeToast(mainActivity, "获取在线资源信息失败", true);
			return;
		}
		OnlineWorksAdapter adapter = new OnlineWorksAdapter(mainActivity,
				worksList);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Works works = worksList.get(position);
				Logger.i(" clicked works name " + works.getName());

				Intent it=new Intent(mainActivity, PlayActivity.class);
				it.putExtra("works", works);
				mainActivity.startActivity(it);

			}
		});

		G.worklist=worksList;
		
	}

}
