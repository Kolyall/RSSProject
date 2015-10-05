package com.rssproject;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.rssproject.fragments.NewsGridFragment;
import com.rssproject.objects.DrawerItem;
import com.rssproject.service.InternetIntentService;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.Tracking;
import net.hockeyapp.android.UpdateManager;

import java.util.ArrayList;

public class MainActivity  extends SherlockFragmentActivity  {
    public static final boolean DEBUG = true;
    public static final String TAG = "com.zastavok.net";
    private static final String FRAGMENT_TAG = "FRAGMENT_TAG";

    ArrayList<DrawerItem> dataList;
    NewsGridFragment fragment;
    int item_position = 0;
    private LinearLayout mDrawer;
    private CustomDrawerAdapter adapter;
    ActionBar actionBar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public void reloadListFromRSS() {
        Intent intentService = new Intent(getApplicationContext(), InternetIntentService.class);
        intentService.putExtra(InternetIntentService.EXTRA_EVENT_NAME, InternetIntentService.EVENT_RELOAD_LIST);
        startService(intentService);
    }

    private ProgressDialog pDialog;
    private void showLoadingDialog() {
        if(pDialog==null)
            pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setCancelable(false);
        pDialog.setMessage(getString(R.string.loading));
        pDialog.show();
    }
    private void dismissProgressDialog() {
        if (pDialog!=null)
            if (pDialog.isShowing())
                pDialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Tracking.startUsage(this);
        checkForCrashes();
        checkForUpdates();
    }

    private void checkForCrashes() {
        CrashManager.register(this, getString(R.string.HOT_KEY_ID));
    }

    private void checkForUpdates() {
        // Remove this for store builds!
        UpdateManager.register(this, getString(R.string.HOT_KEY_ID));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {   case (android.R.id.home):{
                mDrawerToggle.setDrawerIndicatorEnabled(true);
                if (mDrawerLayout.isDrawerOpen(mDrawer))
                    mDrawerLayout.closeDrawer(mDrawer);
                else
                    mDrawerLayout.openDrawer(mDrawer);
                return true;
            }
            case (R.id.menu_sync):{
                if (!isNetworkConnected()){
                    Toast.makeText(getApplicationContext(),"Нет интернет соединения!",Toast.LENGTH_LONG).show();
                    return true;
                }
                showLoadingDialog();
                reloadListFromRSS();
                return true;
            }
        }
        return true;
    }

    BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().contains(InternetIntentService.ACTION_LIST_RELOADED)) {
                dismissProgressDialog();
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(InternetIntentService.ACTION_LIST_RELOADED);
        registerReceiver(receiver, intentFilter);

        actionBar = getSupportActionBar();
        actionBar.setIcon(android.R.color.transparent);

        dataList = getIntent().getParcelableArrayListExtra("dataList");

        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            fragment = NewsGridFragment.newInstance();//new SlidingTabsColorsFragment(dataList);
            transaction.replace(R.id.sample_content_fragment, fragment,FRAGMENT_TAG);
            transaction.commit();
        }else{
            fragment = (NewsGridFragment) getSupportFragmentManager()
                    .findFragmentByTag(FRAGMENT_TAG);
        }


        //--------left menu
        mTitle = mDrawerTitle = getTitle();
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawer = (LinearLayout) findViewById(R.id.left_RelativeLayout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        adapter = new CustomDrawerAdapter(this, R.layout.custom_drawer_item, dataList);

        mDrawerList.setAdapter(adapter);

        monItemClick = adapter.getonItemClick();

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_menu_left2,
                R.string.drawer_open,
                R.string.drawer_close) {
            public void onDrawerClosed(View view) {
                actionBar.setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to
            }

            public void onDrawerOpened(View drawerView) {
                actionBar.setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        //-------left menu

        if (savedInstanceState != null) {
            item_position = savedInstanceState.getInt("item_position", 0);
            setItemSelected(item_position);
        }
    }

    private void setItemSelected(int possition) {

        monItemClick.onClickPosition(possition);
        item_position = possition;

        mDrawerList.setItemChecked(possition, true);

        setTitle(dataList.get(possition).getItemName());
        //	mDrawerLayout.closeDrawer(mDrawerList);
        mDrawerLayout.closeDrawer(mDrawer);

    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("item_position", item_position);
        getSupportFragmentManager().putFragment(outState, FRAGMENT_TAG, fragment );
    }

    public void SelectItem(int possition) {
        monItemClick.onClickPosition(possition);
        item_position = possition;
        mDrawerList.setItemChecked(possition, true);
        setTitle(dataList.get(possition).getItemName());
        mDrawerLayout.closeDrawer(mDrawer);
        fragment.selectItem(dataList.get(item_position));
    }


    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        actionBar.setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);

        //        if (colorDialog != null)
        //            if (colorDialog.isAdded()) {
        //                Log.e("colorDialog","isAdded, reshow");
        //                colorDialog.dismiss();
        //                colorDialog.setColorYesNoListener(this);
        //                String baseUrl = dataList.get(item_position).getItemBasename();
        //                if (!colorDialog.isAdded())
        //                    colorDialog.show(baseUrl, getSupportFragmentManager(), "tag");
        //            }
    }

    private ActionBarDrawerToggle mDrawerToggle;




    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            if (!isNetworkConnected()) {
//                setItemSelected(item_position);
//                setTitle(dataList.get(item_position).getItemName());
//                Toast.makeText(getApplicationContext(),"No internet",Toast.LENGTH_LONG).show();
//            }
//            else {
                SelectItem(position);
//            }
        }
    }

    onItemClick monItemClick;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;


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




    @Override
    protected void onPause() {
        Tracking.stopUsage(this);
        super.onPause();
        if (back_toast != null && back_toast.getView().getWindowVisibility() == View.VISIBLE)
            back_toast.cancel();
    }

    static Toast back_toast;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (back_toast == null || back_toast.getView().getWindowVisibility() != View.VISIBLE) {
                back_toast = Toast.makeText(
                        getApplicationContext(),
                        getString(R.string.exit),
                        Toast.LENGTH_LONG);
                back_toast.show();
            }
            else if (back_toast != null && back_toast.getView()
                    .getWindowVisibility() == View.VISIBLE) {
                back_toast.cancel();
                finish();
            }
        }
        return false;//super.onKeyDown(keyCode, event);
    }

}