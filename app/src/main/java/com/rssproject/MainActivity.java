package com.rssproject;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
    private final String LOG_TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


}
