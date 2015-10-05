package com.rssproject.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.rssproject.R;
import com.rssproject.objects.Item;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InfoActivity extends SherlockActivity {

    private ImageView thumb_image;
    private ProgressBar progressBar1;
    private TextView title;
    private TextView newsText;
    private TextView date;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private Button btn_open_web;




    public boolean onMenuItemSelected(int featureId, MenuItem item)
    {
        switch (item.getItemId()) {
            case  android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Item item = getIntent().getParcelableExtra("item");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

//        ActionBarSherlock actionBarSherlock = getSherlock();
        actionBar.setTitle(item.getTitle());

        thumb_image=(ImageView)findViewById(R.id.list_image2); // thumb image
        progressBar1 = (ProgressBar)findViewById(R.id.progressBar1);
        title=(TextView)findViewById(R.id.title); // likes
        newsText=(TextView)findViewById(R.id.newsText); // likes
        date=(TextView)findViewById(R.id.date); // likes
        btn_open_web=(Button)findViewById(R.id.btn_open_web); // likes
        btn_open_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item item = getIntent().getParcelableExtra("item");
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
                            .parse(item.getLink().trim()));
                    startActivity(browserIntent);
            }
        });

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

        String item_title = item.getTitle();
        String item_date = item.getPubdate();
        String item_text = item.getDescription();
        SimpleDateFormat curFormater = new SimpleDateFormat("EEE,ddMMMyyyyHH:mm:ssZ", Locale.ENGLISH);

        Date dateObj = null;
        String newDateStr = item_date;
        try {
            dateObj = curFormater.parse(item_date);
            SimpleDateFormat postFormater = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            newDateStr = postFormater.format(dateObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        title.setText(item_title);
        date.setText(newDateStr);
        newsText.setText(item_text);

        String image_url="http://m1.s98.rscdn.net/img/no_image.png";
        if (item.getEnclosure()!=null)
            if (item.getEnclosure().getUrl()!=null)
                image_url = item.getEnclosure().getUrl();

        imageLoader.displayImage(image_url, thumb_image, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressBar1.setVisibility(View.GONE);
                thumb_image.invalidate();
                thumb_image.setVisibility(View.VISIBLE);
                //                            viewHolder.thumb_image.setImageBitmap(mContext.getResources().getDrawable(R.drawable.no_image));
                thumb_image.setImageResource(R.drawable.no_image);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                thumb_image.setVisibility(View.VISIBLE);
                progressBar1.setVisibility(View.GONE);
                thumb_image.invalidate();
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        });

    }
}
