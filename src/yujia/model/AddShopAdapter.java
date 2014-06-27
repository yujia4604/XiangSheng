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

public class AddShopAdapter extends BaseAdapter {
	private List<Shop> shopList;
	private Context context;

	public AddShopAdapter(Context context, List<Shop> shoplist) {
		shopList = shoplist;
		this.context = context;
	}

	@Override
	public int getCount() {
		return shopList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return shopList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = View.inflate(context, R.layout.shopitem, null);
		}
		final Shop shop = shopList.get(position);
	
		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.imageView);
		TextView nameView = (TextView) convertView.findViewById(R.id.txtViewShopName);
		TextView adressView = (TextView) convertView
				.findViewById(R.id.txtViewAdress);
		TextView beobeiNumView = (TextView) convertView
				.findViewById(R.id.txtViewBaobeiNum);
		TextView sellCountView = (TextView) convertView
				.findViewById(R.id.txtViewSellCount);
		imageView.setImageBitmap(shop.getLittle80Bitmap());
		nameView.setText(shop.getName());
		adressView.setText(shop.getAddress());
		beobeiNumView.setText(shop.getBaobeiNum());
		sellCountView.setText(shop.getSellCounut());
		return convertView;
	}

}
