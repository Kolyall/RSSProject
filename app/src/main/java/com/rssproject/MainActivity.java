package com.rssproject;

import android.content.Context;
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
import com.actionbarsherlock.view.MenuItem;
import com.rssproject.fragments.EmptyFragment;
import com.rssproject.objects.DrawerItem;

import java.util.ArrayList;

public class MainActivity  extends SherlockFragmentActivity  {
    public static final boolean DEBUG = true;
    public static final String TAG = "com.zastavok.net";
    private static final String FRAGMENT_TAG = "FRAGMENT_TAG";

    ArrayList<DrawerItem> dataList;
    EmptyFragment fragment;
    int item_position = 0;
    private LinearLayout mDrawer;
    private CustomDrawerAdapter adapter;
    ActionBar actionBar;



    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
//        if (mDrawerToggle.onOptionsItemSelected((android.view.MenuItem) item)) {
//            return true;
//        }
//        else
        if (item.getItemId() == android.R.id.home) {
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
                mDrawerLayout.closeDrawer(GravityCompat.START);
            else
                mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return true;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getSupportActionBar();
        actionBar.setIcon(android.R.color.transparent);

        dataList = new ArrayList<DrawerItem>();
        dataList.add(
                new DrawerItem(
                        "Все новости",
                         R.drawable.icon_left_point));
        dataList.add(
                new DrawerItem(
                        "Политика",
                        R.drawable.icon_left_point));
        dataList.add(
                new DrawerItem(
                        "Экономика",
                        R.drawable.icon_left_point));
        dataList.add(
                new DrawerItem(
                        "В мире",
                        R.drawable.icon_left_point));
        dataList.add(
                new DrawerItem(
                        "Иркутская область",
                        R.drawable.icon_left_point));


        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            fragment = EmptyFragment.newInstance();//new SlidingTabsColorsFragment(dataList);
            transaction.replace(R.id.sample_content_fragment, fragment,FRAGMENT_TAG);
            transaction.commit();
        }else{
            fragment = (EmptyFragment) getSupportFragmentManager()
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

        if (savedInstanceState == null) {
            if (!isNetworkConnected()) {
                Toast.makeText(getApplicationContext(),"No internet",Toast.LENGTH_LONG).show();
            }
            else {
//                SelectItem(0);
            }
        }
        else{
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


    private void SelectItem(String search) {
//        actionBar.show();
        //        getActionBar().show();
        //        invalidateOptionsMenu();
        if (!isNetworkConnected()) {
            Toast.makeText(getApplicationContext(),"No internet",Toast.LENGTH_LONG).show();
        }
        else {
            //                item_position = 1;
            monItemClick.onClickPosition(item_position);


            mDrawerList.setItemChecked(item_position, true);

            setTitle("РџРѕРёСЃРє: " + search);
            //	mDrawerLayout.closeDrawer(mDrawerList);
            mDrawerLayout.closeDrawer(mDrawer);
            fragment.selectItem(item_position);
            //	mSFragmentPagerAdapter.notifyDataSetChanged();


        }
    }



    public void SelectItem(int possition) {

        monItemClick.onClickPosition(possition);
        item_position = possition;

        mDrawerList.setItemChecked(possition, true);

        setTitle(dataList.get(possition).getItemName());
//        	mDrawerLayout.closeDrawer(mDrawerList);
        mDrawerLayout.closeDrawer(mDrawer);

        fragment.selectItem(item_position);

        //	mSFragmentPagerAdapter.notifyDataSetChanged();


        //        }
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
            //            if (position == dataList.size()-1){
            //                setItemSelected(item_position);
            //                setTitle(dataList.get(item_position).getItemName());
            //                startActivity(new Intent(getApplicationContext(),AboutActivity.class));
            //                return;
            //            }

            if (!isNetworkConnected()) {
                setItemSelected(item_position);
                setTitle(dataList.get(item_position).getItemName());
                Toast.makeText(getApplicationContext(),"No internet",Toast.LENGTH_LONG).show();
            }
            else {
                SelectItem(position);
            }

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