package com.rssproject.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.rssproject.objects.Item;
import com.rssproject.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class ItemGridAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Item> data;
    private LayoutInflater inflater=null;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

	private String sort;
    
    public ItemGridAdapter(Context context, ArrayList<Item> d) {

    	this.sort=sort;
        mContext = context;
        data=d;
        inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
        		.cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
				.showImageOnFail(android.R.color.transparent)
				.cacheOnDisk(true)
				.displayer(new FadeInBitmapDisplayer(1500))
				.resetViewBeforeLoading(true)
				.imageScaleType(ImageScaleType.EXACTLY)// default
				.build();
    }

	public int getCount() {
        return data.size();
    }
	

    public Object getItem(int position) {
        return position;
    }
    

    public long getItemId(int position) {
        return position;
    }



    public View getView( int position,  View convertView, ViewGroup parent) {
    	 final ViewHolderItem viewHolder;
        if(convertView==null)
        {
        	 convertView = inflater.inflate(R.layout.item_grid, null);
        	 viewHolder = new ViewHolderItem();
        	
        	 viewHolder.thumb_image=(ImageView)convertView.findViewById(R.id.list_image2); // thumb image
        	 viewHolder.progressBar1 = (ProgressBar)convertView.findViewById(R.id.progressBar1);
        	 viewHolder.title=(TextView)convertView.findViewById(R.id.title); // likes
        	 viewHolder.date=(TextView)convertView.findViewById(R.id.date); // likes
        	 viewHolder.position = position;
        	 convertView.setTag(viewHolder);
        
        }else{
        	viewHolder = (ViewHolderItem) convertView.getTag();
        }

        viewHolder.progressBar1.setVisibility(View.VISIBLE);

        Item item = data.get(position);
        String item_title = item.getTitle();
        String item_date = item.getPubdate();
        SimpleDateFormat curFormater = new SimpleDateFormat("EEE,ddMMMyyyyHH:mm:ssZ", Locale.ENGLISH);

        Date dateObj = null;
        String newDateStr = item_date;
        try {
            dateObj = curFormater.parse(item_date);
            SimpleDateFormat postFormater = new SimpleDateFormat("yyyy-MM-dd");
            newDateStr = postFormater.format(dateObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        int MAX = 60;
        int maxLength = (item_title.length() < MAX)?item_title.length():MAX;
        String dots = (item_title.length() < MAX)?"":"...";
        item_title = item_title.substring(0, maxLength)+dots;

        viewHolder.title.setText(item_title);
        viewHolder.date.setText(newDateStr);

        String image_url="http://m1.s98.rscdn.net/img/no_image.png";
        if (item.getEnclosure()!=null)
            if (item.getEnclosure().getUrl()!=null)
             image_url = item.getEnclosure().getUrl();

            imageLoader.displayImage(image_url, viewHolder.thumb_image, options, new ImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {

						}

						@Override
						public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
							            	viewHolder.progressBar1.setVisibility(View.GONE);
							            	viewHolder.thumb_image.invalidate();
							            	viewHolder.thumb_image.setVisibility(View.VISIBLE);
							            	viewHolder.thumb_image.setImageBitmap(BitmapFactory
                                                    .decodeResource(mContext
                                                            .getResources(), android.R.drawable.ic_dialog_alert));
						}

						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
							viewHolder.thumb_image.setVisibility(View.VISIBLE);
							viewHolder.progressBar1.setVisibility(View.GONE);
							viewHolder.thumb_image.invalidate();
						}

						@Override
						public void onLoadingCancelled(String imageUri, View view) {

                        }
					});

        return convertView;
    }
    

    static class ViewHolderItem {
		public ProgressBar progressBar1;
		public ImageView thumb_image;
    	  int position;
        public TextView title;
        public TextView date;
    }
    
}