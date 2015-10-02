package com.rssproject.activities;

import android.os.Bundle;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.rssproject.objects.Item;
import com.rssproject.R;

public class InfoActivity extends SherlockActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Item item = getIntent().getParcelableExtra("item");
        Toast.makeText(getApplicationContext(),item.getTitle(),Toast.LENGTH_LONG).show();

    }
}
