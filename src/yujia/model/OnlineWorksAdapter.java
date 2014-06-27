package yujia.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import yujia.util.Logger;
import yujia.xiangsheng.R;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class OnlineWorksAdapter extends BaseAdapter {
	private List<Works> worksList;
	private Context context;

	public OnlineWorksAdapter(Context context, List<Works> baobeiList) {
		worksList = baobeiList;
		this.context = context;
	}

	@Override
	public int getCount() {
		return worksList.size();
	}

	@Override
	public Object getItem(int position) {
		return worksList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = View.inflate(context, R.layout.works_item, null);
			// convertView.findViewById(R.id.checkbox).setVisibility(View.VISIBLE);
		}

		final Works works = worksList.get(position);
		/*
		 * Logger.i("position =  " + position + " ," + baobei.getName() +
		 * " ,selected = " + baobei.isSelected());
		 */
		TextView nameView = (TextView) convertView
				.findViewById(R.id.txtWorksName);
		TextView lengthView = (TextView) convertView
				.findViewById(R.id.txtWorksLength);
		TextView sizeView = (TextView) convertView
				.findViewById(R.id.txtWorksSize);
		nameView.setText(works.getName());
		lengthView.setText(works.getLength() / 60 + "分钟");
		sizeView.setText(works.getSize() / 1000 + "M");

		return convertView;
	}

}
