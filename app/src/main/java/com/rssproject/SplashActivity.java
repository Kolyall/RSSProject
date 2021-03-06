package com.rssproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.rssproject.database.DatabaseHelper;
import com.rssproject.objects.DrawerItem;
import com.rssproject.objects.Enclosure;
import com.rssproject.objects.Item;
import com.rssproject.service.InternetIntentService;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.Tracking;
import net.hockeyapp.android.UpdateManager;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends SherlockActivity {

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().contains(InternetIntentService.ACTION_LIST_LOADED)) {
                boolean success = intent.getBooleanExtra(InternetIntentService.KEY_success, false);
                Log.e("onReceive", "success=" + success);

                if (success) {
                    MyRun mr = new MyRun(getApplicationContext());
                    new Thread(mr).start();
//                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
//                    finish();
//                    Bundle b = intent.getBundleExtra("bundle");
//                    Channel channel = b.getParcelable("channel");
//                    Log.e(LOG_TAG,channel.getTitle());
//                    Log.i(LOG_TAG, "creating " + getClass() + " at " + System.currentTimeMillis());
//
//                    tv.setMovementMethod(new ScrollingMovementMethod());
//                    doSampleDatabaseStuff("onCreate", tv);
//                    setContentView(tv);
                }
                else{
                    tv.setText(tv.getText().toString()+"\nНет интернет соединения.\nЗагрузка из Базы данных...");
                    //Back timer
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            sendBroadcast(InternetIntentService.ACTION_LIST_LOADED, true);
                        }
                    }, 2000);
                }
            }
        }
    };
    TextView tv;
    private Handler handler;

    @Override
    protected void onResume() {
        super.onResume();
        checkForCrashes();
        checkForUpdates();
        Tracking.startUsage(this);
    }

    @Override
    protected void onPause() {
        Tracking.stopUsage(this);
        super.onPause();
    }

    private void checkForCrashes() {
        CrashManager.register(this, getString(R.string.HOT_KEY_ID));
    }

    private void checkForUpdates() {
        // Remove this for store builds!
        UpdateManager.register(this, getString(R.string.HOT_KEY_ID));
    }

    class MyRun implements Runnable {
        Context applicationContext;
        public MyRun(Context applicationContext) {
            this.applicationContext = applicationContext;
        }
        public void run() {
            List<Item>  list = getListFromDB(applicationContext);
            addToUIListFrom(list);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.putParcelableArrayListExtra("dataList",dataList);
                    Log.e(LOG_TAG, " dataList.size : " + dataList.size());
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    private List<Item> getListFromDB(Context applicationContext) {
        RuntimeExceptionDao<Item, Integer> simpleDao = (new DatabaseHelper(applicationContext))
                .getItemDataDao();
                return simpleDao.queryForAll();
    }

    ArrayList<DrawerItem> dataList = new ArrayList<DrawerItem>();
    private void addToUIListFrom(List<Item> items) {
        DrawerItem allNews = new DrawerItem("Все новости",R.drawable.icon_left_point);
        if (!dataList.contains(allNews))
        dataList.add(allNews);
        for (Item item:items){
            DrawerItem drawerItem = new DrawerItem(item.getCategory(),R.drawable.icon_left_point);
            if (!dataList.contains(drawerItem))
                 dataList.add(drawerItem);
        }
    }

    private void sendBroadcast(
            String action,
            boolean success
    ) {
        Log.e("InternetIntentService", action);
        Intent intent = new Intent();
        intent.setAction(action);
        //        intent.putExtra("bundle", b);
        intent.putExtra(InternetIntentService.KEY_success, success);
        sendBroadcast(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter filter = new IntentFilter();
        filter.addAction(InternetIntentService.ACTION_LIST_LOADED);
        registerReceiver(receiver, filter);

        setContentView(R.layout.activity_splash);
        tv = (TextView)findViewById(R.id.hello);

        if (isNetworkConnected()){
            loadListFromRSS();
        }else{
            tv.setText(tv.getText().toString()+"\nНет интернет соединения.\nЗагрузка из Базы данных...");
            //Back timer
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    sendBroadcast(InternetIntentService.ACTION_LIST_LOADED, true);
                }
            }, 2000);
        }

        handler = new Handler(getBaseContext().getMainLooper());
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



    boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            try {
                //For 3G check
                boolean is3g = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                        .isConnectedOrConnecting();
                //For WiFi Check
                boolean isWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                        .isConnectedOrConnecting();


                if (!is3g && !isWifi) {
                    return false;
                }
                else {
                    return true;
                }

            } catch (Exception er) {
                return false;
            }
        }
        else
            return true;
    }

}
