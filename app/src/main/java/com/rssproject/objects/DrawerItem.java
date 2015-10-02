package com.rssproject.objects;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class DrawerItem implements Parcelable {

	String ItemName;
	int imgResID;
	
	public DrawerItem(String itemName, int imgResID) {
		super();
		ItemName = itemName;
		this.imgResID = imgResID;
	}
	
	public String getItemName() {
		return ItemName;
	}
	public void setItemName(String itemName) {
		ItemName = itemName;
	}
	public int getImgResID() {
		return imgResID;
	}
	public void setImgResID(int imgResID) {
		this.imgResID = imgResID;
	}
	

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(ItemName);
//        dest.writeString(ItemCount);
//        dest.writeString(ItemBasename);
//        dest.writeInt(imgResID);
        Bundle b = new Bundle();
        b.putString("ItemName", ItemName);
        b.putInt("imgResID", imgResID);
        dest.writeBundle(b);
    }

    public static final Creator<DrawerItem> CREATOR =
            new Creator<DrawerItem>() {
                public DrawerItem createFromParcel(Parcel in) {

//                    category.ItemName = in.readString();
                    Bundle b = in.readBundle();
                    String ItemName = b.getString("ItemName");
                    String ItemCount = b.getString("ItemCount");
                    String ItemBasename = b.getString("ItemBasename");
                    int imgResID = b.getInt("imgResID");
                    DrawerItem item = new DrawerItem(ItemName,imgResID);
                    return item;
                }

                @Override
                public DrawerItem[] newArray(int size) {
                    return new DrawerItem[size];
                }
            };
}
