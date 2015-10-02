package com.rssproject;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rssproject.objects.DrawerItem;

import java.util.List;

public class CustomDrawerAdapter extends ArrayAdapter<DrawerItem> implements onItemClick {

	Context mContext;
	List<DrawerItem> drawerItemList;
	int layoutResID;
	private int mSelectedPostion;

	public CustomDrawerAdapter(Context context, int layoutResourceID,
			List<DrawerItem> listItems) {
		super(context, layoutResourceID, listItems);
		this.mContext = context;
		this.drawerItemList = listItems;
		this.layoutResID = layoutResourceID;

	}
	
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		DrawerItemHolder drawerHolder;
		View view = convertView;

		
		
		if (view == null) {
			LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
			drawerHolder = new DrawerItemHolder();

			view = inflater.inflate(layoutResID, parent, false);
			drawerHolder.ItemName = (TextView) view
					.findViewById(R.id.drawer_itemName);
			drawerHolder.icon = (ImageView) view.findViewById(R.id.drawer_icon);

            drawerHolder. devider_default=(View) view.findViewById(R.id.devider_default);
            drawerHolder. devider_about=(View) view.findViewById(R.id.devider_about);

			view.setTag(drawerHolder);

		} else {
			drawerHolder = (DrawerItemHolder) view.getTag();

		}

		DrawerItem dItem = (DrawerItem) this.drawerItemList.get(position);

		drawerHolder.icon.setImageDrawable(view.getResources().getDrawable(
				dItem.getImgResID()));
//		if (dItem.getItemCount().length()>0){
//			drawerHolder.ItemCount.setText(dItem.getItemCount());
//			drawerHolder.ItemCount.setVisibility(View.VISIBLE);
//			}
//		else
//			drawerHolder.ItemCount.setVisibility(View.GONE);
		drawerHolder.ItemName.setText(dItem.getItemName());
		
//        if (position == getCount()-1){
//            drawerHolder.ItemName.setGravity(Gravity.CENTER);
//        } else{
            drawerHolder.ItemName.setGravity(Gravity.CENTER|Gravity.LEFT);
//        }
//        if (position == getCount()-2){
//            drawerHolder. devider_default.setVisibility(View.GONE);
//            drawerHolder. devider_about.setVisibility(View.VISIBLE);
//        }else{
            drawerHolder. devider_default.setVisibility(View.GONE);
            drawerHolder. devider_about.setVisibility(View.GONE);
//        }

		return view;
	}

	private static class DrawerItemHolder {
		TextView ItemName;
		ImageView icon;
        View devider_default;
        View devider_about;
	}


	public onItemClick getonItemClick() {
		return this;
	}

	public void onClickPosition(int position) {
		mSelectedPostion = position;
	}
}