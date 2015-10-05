package com.rssproject.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.rssproject.R;
import com.rssproject.activities.InfoActivity;
import com.rssproject.adapters.ItemGridAdapter;
import com.rssproject.database.DatabaseHelper;
import com.rssproject.objects.DrawerItem;
import com.rssproject.objects.Item;
import com.rssproject.service.InternetIntentService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Unuchek on 15.09.2015.
 */
public class NewsGridFragment extends Fragment{
    private String LOG_TAG=getClass().getSimpleName();
    public static NewsGridFragment newInstance() {
        Bundle bundle = new Bundle();
        NewsGridFragment fragment = new NewsGridFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_empty, null);//в этот лайаут добавляй свои вьюхи
        return v;
    }
    private GridView gridView;
    ArrayList<Item> arrayList = new ArrayList<Item>();
    private ItemGridAdapter adapter;

    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }
    BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().contains(InternetIntentService.ACTION_LIST_RELOADED)) {
                selectItem(startId);
            }
        }


    };

    public void onResume() {
        intentFilter.addAction(InternetIntentService.ACTION_LIST_RELOADED);
        getActivity().registerReceiver(receiver, intentFilter);
        super.onResume();
        adapter.notifyDataSetChanged();
    }



    IntentFilter intentFilter = new IntentFilter();
    private ProgressBar progressBar1;
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        progressBar1 = (ProgressBar) view.findViewById(R.id.progressBar1);
        gridView = (GridView) view.findViewById(R.id.gridView1);
        adapter=new ItemGridAdapter(getActivity(), arrayList);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity()
                            .getApplicationContext(), InfoActivity.class);
                    intent.putExtra("item", arrayList.get(position));
                    getActivity().startActivity(intent);
            }
        });


        handler = new Handler(getActivity().getBaseContext().getMainLooper());
        selectItem(null);

    }

    Handler handler ;

    class MyRun implements Runnable {
        DrawerItem startId;
        Context applicationContext;
        public MyRun(Context applicationContext, DrawerItem startId) {
            this.startId = startId;
            this.applicationContext = applicationContext;
        }
        public void run() {
            List<Item>  list = null;
            try {
                list = retrieveByDate(applicationContext, startId);
                if (startId!=null)
                    Log.e("list","list retrieved:"+startId.getItemName());
                else
                    Log.e("list","list retrieved:"+null);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            addToUIListFrom(list);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar1.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }


    DrawerItem startId;
    public void selectItem(DrawerItem item_position) {
        startId=item_position;
        MyRun mr = new MyRun(getActivity().getApplicationContext(),startId);
        new Thread(mr).start();
    }

    private List<Item> getListFromDB(Context applicationContext, DrawerItem startId) {
        RuntimeExceptionDao<Item, Integer> simpleDao = (new DatabaseHelper(applicationContext))
                .getItemDataDao();
        if (startId==null||startId.getItemName().equals("Все новости"))
            return simpleDao.queryForAll();
        else
            return simpleDao.queryForEq("category", startId.getItemName());

    }

    public List<Item> retrieveByDate(Context applicationContext,DrawerItem startId)throws SQLException {
        QueryBuilder<Item,Integer> queryBuilder =  (new DatabaseHelper(applicationContext)).getItemDao().queryBuilder();
        List<Item> list;
        if (startId==null||startId.getItemName().equals("Все новости"))
            queryBuilder.where().isNotNull("title");
        else{
            queryBuilder.where().eq("category", startId.getItemName());
        }
        queryBuilder.orderBy("d_date", false);

        list = queryBuilder.query();
        return list;
    }

    private void addToUIListFrom(List<Item> items) {
        arrayList.removeAll(arrayList);
        for (Item item:items){
            if (!arrayList.contains(item))
            arrayList.add(item);
        }
    }

    private void getItemsFromService() {
        Intent intentService = new Intent(getActivity().getApplicationContext(), InternetIntentService.class);
        intentService.putExtra(InternetIntentService.EXTRA_EVENT_NAME, InternetIntentService.EVENT_LOAD_LIST);
        getActivity().startService(intentService);

    }

    boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            try{
                //For 3G check
                boolean is3g = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                        .isConnectedOrConnecting();
                //For WiFi Check
                boolean isWifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                        .isConnectedOrConnecting();


                if (!is3g && !isWifi)
                {
                    return false;
                }
                else
                {
                    return true;
                }

            }catch (Exception er){
                return false;
            }
        } else
            return true;
    }






}
