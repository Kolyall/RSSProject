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
import android.widget.Toast;

import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.rssproject.R;
import com.rssproject.activities.InfoActivity;
import com.rssproject.adapters.ItemGridAdapter;
import com.rssproject.database.DatabaseHelper;
import com.rssproject.objects.Item;
import com.rssproject.service.InternetIntentService;

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
            if (intent.getAction().contains(InternetIntentService.ACTION_LIST_LOADED)) {

            }
        }


    };

    public void onResume() {
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
                if (isNetworkConnected()) {
                    Intent intent = new Intent(getActivity()
                            .getApplicationContext(), InfoActivity.class);
                    intent.putExtra("item", arrayList.get(position));
                    getActivity().startActivity(intent);
                }
                else
                    Toast.makeText(getActivity()
                            .getApplicationContext(), "No internet", Toast.LENGTH_LONG).show();
            }
        });


        handler = new Handler(getActivity().getBaseContext().getMainLooper());
        selectItem(0);

    }

    Handler handler ;

    class MyRun implements Runnable {
        int startId;
        Context applicationContext;
        public MyRun(Context applicationContext, int startId) {
            this.startId = startId;
            this.applicationContext = applicationContext;

        }
        public void run() {

            List<Item>  list = getListFromDB(applicationContext,startId);


            addToUIListFrom(list);


//            Bundle bundle = new Bundle();
//            Message message=new Message();
//            message.setData(bundle);
//            handler.handleMessage(message);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    progressBar1.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                }
            });

        }


    }



    public void selectItem(int item_position) {
        int startId=item_position;
        MyRun mr = new MyRun(getActivity().getApplicationContext(),startId);
        new Thread(mr).start();
    }

    private List<Item> getListFromDB(Context applicationContext, int startId) {
        RuntimeExceptionDao<Item, Integer> simpleDao = (new DatabaseHelper(applicationContext))
                .getItemDataDao();
        switch (startId){
            case 0:{
                return simpleDao.queryForAll();
            }
            case 1:{
                return simpleDao.queryForEq("category", "Политика");
            }
            case 2:{
                return simpleDao.queryForEq("category", "Экономика");
            }
            case 3:{
                return simpleDao.queryForEq("category", "В мире");
            }
            case 4:{
                return simpleDao.queryForEq("category", "Иркутская область");
            }
            default:{
                return simpleDao.queryForAll();
            }
        }

//        return simpleDao.queryForAll();

    }

    private void addToUIListFrom(List<Item> items) {
        arrayList.removeAll(arrayList);
        for (Item item:items){
            if (!arrayList.contains(item))
            arrayList.add(item);
            Log.e("item_title", item.getCategory());
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
