package com.rssproject.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class InternetIntentService extends IntentService {
	public static final String TAG = "InternetIntentService";
	public static final String EXTRA_EVENT_NAME = "EXTRA_EVENT_NAME";
	public static final String KEY_success = "success";
	public static final int EVENT_LOAD_LIST = 1;
	public static final String ACTION_LIST_LOADED = "ACTION_LIST_LOADED";
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
                boolean success = mXmlManager.loadList();
                sendBroadcast(ACTION_LIST_LOADED,
                        success
                        );
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
		Log.e("InternetIntentService", action);
		Intent intent = new Intent();
		intent.setAction(action);
		intent.putExtra(KEY_success,success);
	sendBroadcast(intent);
}

}
