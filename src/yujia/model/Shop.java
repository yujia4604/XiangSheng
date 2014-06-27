package yujia.model;

import android.graphics.Bitmap;

public class Shop {
	private String name;
	private String shopId;
	private String url;
	private String sellCounut;
	private String baobeiNum;
	private String address;
	private Bitmap little80Bitmap;

	public Bitmap getLittle80Bitmap() {
		return little80Bitmap;
	}

	public void setLittle80Bitmap(Bitmap little80Bitmap) {
		this.little80Bitmap = little80Bitmap;
	}

	public String getBaobeiNum() {
		return baobeiNum;
	}

	public void setBaobeiNum(String baobeiNum) {
		this.baobeiNum = baobeiNum;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSellCounut() {
		return sellCounut;
	}

	public void setSellCounut(String sellCounut) {
		this.sellCounut = sellCounut;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public static String getShopId(String href) {
		if (href == null)
			return null;

		return null;

	}

	public int getBaobeiNumInteger(){
		String str = baobeiNum.replace("共", "");
		 	str=str.replace("件宝贝", "");
		return Integer.valueOf(str);
	}
	
	@Override
	public String toString() {
		return "name :"+name+"\n" +
				"address :"+address+"\n"+
				"baobeiNum :"+baobeiNum+"\n"+ 
				"sellCount :"+sellCounut+"\n" +
				"url :"+url+"\n";
	}

}
