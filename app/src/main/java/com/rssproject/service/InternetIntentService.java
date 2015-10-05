package com.rssproject.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class InternetIntentService extends IntentService {
	public static final String TAG = "InternetIntentService";
	public static final String EXTRA_EVENT_NAME = "EXTRA_EVENT_NAME";
	public static final String KEY_success = "success";
	public static final int EVENT_LOAD_LIST = 1;
	public static final int EVENT_RELOAD_LIST = 2;
	public static final String ACTION_LIST_LOADED = "ACTION_LIST_LOADED";
	public static final String ACTION_LIST_RELOADED = "ACTION_LIST_RELOADED";
	public XMLManager mXmlManager;
	public InternetIntentService() {
		super("InternetIntentService");
		mXmlManager = new XMLManager(this);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
		    int event = intent.getIntExtra(EXTRA_EVENT_NAME, -1);

            switch (event) {
            case EVENT_LOAD_LIST: {
                boolean success = false;
                try {
                    success = mXmlManager.loadList();
                }catch (Exception er){
                    er.printStackTrace();
                }
                sendBroadcast(ACTION_LIST_LOADED, success);
                break;
            }

            case EVENT_RELOAD_LIST: {
                boolean success = false;
                try {
                    success = mXmlManager.loadList();
                }catch (Exception er){
                    er.printStackTrace();
                }
                sendBroadcast(ACTION_LIST_RELOADED, success);
                break;
            }

            default:
                Log.d(TAG, "Incorrect action");
                break;
            }
		
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	private void sendBroadcast(
			String action,
            boolean success
			) {
//        Bundle b = new Bundle();
//        b.putParcelable("channel", channel);


		Log.e("InternetIntentService", action);
		Intent intent = new Intent();
		intent.setAction(action);
//        intent.putExtra("bundle", b);
		intent.putExtra(KEY_success, success);
	sendBroadcast(intent);
}

}
