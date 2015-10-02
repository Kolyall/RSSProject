package com.rssproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.rssproject.database.DatabaseHelper;
import com.rssproject.service.InternetIntentService;

import java.util.List;

public class SplashActivity extends SherlockActivity {
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().contains(InternetIntentService.ACTION_LIST_LOADED)) {
                boolean success = intent.getBooleanExtra(InternetIntentService.KEY_success, false);
                Log.e("onReceive", "success=" + success);

                if (success) {
//                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                    finish();
                    Log.i(LOG_TAG, "creating " + getClass() + " at " + System.currentTimeMillis());

                    tv.setMovementMethod(new ScrollingMovementMethod());
                    doSampleDatabaseStuff("onCreate", tv);
                    setContentView(tv);
                }
            }
        }
    };
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction(InternetIntentService.ACTION_LIST_LOADED);
        registerReceiver(receiver, filter);

        setContentView(R.layout.activity_splash);

        loadListFromRSS();
         tv = new TextView(this);

    }

    private final String LOG_TAG = getClass().getSimpleName();
    private void doSampleDatabaseStuff(String action, TextView tv) {
        // get our dao
        RuntimeExceptionDao<Item, Integer> simpleDao = (new DatabaseHelper(getApplicationContext())).getItemDataDao();
        // query for all of the data objects in the database
        List<Item> list = simpleDao.queryForAll();
        // our string builder for building the content-view
        StringBuilder sb = new StringBuilder();
        sb.append("Found ").append(list.size()).append(" entries in DB in ").append(action).append("()\n");

        // if we already have items in the database
        int simpleC = 1;
        for (Item simple : list) {
            sb.append('#').append(simpleC).append(": ").append(simple).append('\n');
            simpleC++;
        }
        sb.append("------------------------------------------\n");
        tv.setText(sb.toString());
        Log.i(LOG_TAG, "Done with page at " + System.currentTimeMillis());


        RuntimeExceptionDao<Enclosure, Integer> enclosureDao = (new DatabaseHelper(getApplicationContext())).getEnclosureDataDao();
        // query for all of the data objects in the database
        List<Enclosure> enclosureList = enclosureDao.queryForAll();
        Log.e(LOG_TAG, "enclosure size " + enclosureList.size());
        for (Enclosure enclosure:enclosureList){
            Log.e(LOG_TAG, "enclosure " + enclosure.getUrl());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public void loadListFromRSS() {
        Intent intentService = new Intent(getApplicationContext(), InternetIntentService.class);
        intentService.putExtra(InternetIntentService.EXTRA_EVENT_NAME, InternetIntentService.EVENT_LOAD_LIST);
        startService(intentService);
    }
}
