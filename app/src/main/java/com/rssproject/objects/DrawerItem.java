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
	public int getImgResID() {
		return imgResID;
	}

    @Override
    public boolean equals(Object o) {
        DrawerItem another = (DrawerItem)o;
        if (another.getItemName().equals(getItemName()))
            return true;
        else
            return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Bundle b = new Bundle();
        b.putString("ItemName", ItemName);
        b.putInt("imgResID", imgResID);
        dest.writeBundle(b);
    }

    public static final Creator<DrawerItem> CREATOR =
            new Creator<DrawerItem>() {
                public DrawerItem createFromParcel(Parcel in) {
                    Bundle b = in.readBundle();
                    String ItemName = b.getString("ItemName");
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
