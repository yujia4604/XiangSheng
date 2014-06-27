package yujia.model;

import yujia.util.Logger;
import android.database.Cursor;
import android.graphics.Bitmap;

public class Baobei {
	private int id;
	private String itemId;
	private String itemUrl;
	private String name;
	private String price;
	private String shopName;
	private int sellCount;
	private boolean isTmall;
	private int defalutIndex;
	private float  currentPrice;
	private float  oldPrice;
	private boolean isPriceChanged;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public boolean isPriceChanged() {
		return isPriceChanged;
	}

	public void setPriceChanged(boolean isPriceChanged) {
		this.isPriceChanged = isPriceChanged;
	}

	public float getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(float oldPrice) {
		this.oldPrice = oldPrice;
	}
	

	public float getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(float currentPrice) {
		this.currentPrice = currentPrice;
	}

	public Baobei(Cursor cursor) {
		id=cursor.getInt(cursor.getColumnIndex("_id"));
		itemId = cursor.getString(cursor.getColumnIndex("ITEMID"));
		shopName = cursor.getString(cursor.getColumnIndex("SHOP"));
		price = cursor.getString(cursor.getColumnIndex("PRICE"));
		name = cursor.getString(cursor.getColumnIndex("NAME"));
		sellCount = cursor.getInt(cursor.getColumnIndex("SELLCONUT"));
		itemUrl = cursor.getString(cursor.getColumnIndex("ITEMURL"));
	}

	public Baobei() {
	}

	public int getDefalutIndex() {
		return defalutIndex;
	}

	public void setDefalutIndex(int defalutIndex) {
		this.defalutIndex = defalutIndex;
	}

	public boolean isTmall() {
		return isTmall;
	}

	public void setTmall(boolean isTmall) {
		this.isTmall = isTmall;
	}

	public int getSellCount() {
		return sellCount;
	}

	public void setSellCount(int sellCount) {
		this.sellCount = sellCount;
	}

	public Bitmap getLittle80bitmap() {
		return little80bitmap;
	}

	private Bitmap little80bitmap;

	private boolean isSelected;

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public Bitmap getLittleSumbitmap() {
		return little80bitmap;
	}

	public void setLittleSumBitmap(Bitmap little100bitmap) {
		this.little80bitmap = little100bitmap;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String sellerName) {
		this.shopName = sellerName;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getItemUrl() {
		return itemUrl;
	}

	public void setItemUrl(String itemUrl) {
		this.itemUrl = itemUrl;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	@Override
	public String toString() {
		return "name = " + name + " ,\n" + "shop " + shopName + " ,\n"
				+ "price " + price + " ,\n" + "itmeid " + itemId + " ,\n"
				+ "itmeurl = " + itemUrl + " ,\n " + "sumBitmap : "
				+ little80bitmap;
	}

	public void setItemIdByUrl(String url) {
		Logger.i("baobei url = " + url);
		int start = url.indexOf("id=") + "id=".length();
		int end = url.indexOf("&");
		String id = null;
		Logger.i("start " + start + "  ,end = " + end);
		if (end == -1)// 当没有“&”时
			id = url.substring(start);
		else if (end < start) {// 当“&”在id=前面时
			int end2 = url.lastIndexOf("&");// 获取最后一个”&“
			if (end == end2)// 当只有一个“&”时
				id = url.substring(start);
			else {
				String[] strs = url.split("&");
				if (strs.length == 3)// 当有两个”&“时
					url.subSequence(start, end2);
				else {// 当”&“的数目超过两个时
					for (String str : strs) {
						if (str.contains("id=")) {
							id = str.substring(3);
							break;
						}
					}
				}
			}
		} else
			id = url.substring(start, end);
		Logger.i("baobei id= " + id);
		setItemId(id);
	}


}
