package com.arteriatech.ss.msec.iscan.v4.asyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.arteriatech.ss.msec.iscan.v4.mutils.common.UIListener;
import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.interfaces.AsyncTaskCallBack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by e10769 on 15-05-2017.
 */

public class DirecySyncAsyncTask extends AsyncTask<Void, Boolean, Boolean> {
    private Context mContext;
    private UIListener uiListener = null;
    private Hashtable dbHeadTable = null;
    private ArrayList<HashMap<String, String>> arrtable = null;
    private int type = 0;

    public DirecySyncAsyncTask(Context mContext, AsyncTaskCallBack asyncTaskCallBack, UIListener uiListener, Hashtable dbHeadTable, ArrayList<HashMap<String, String>> arrtable, int type) {
        this.mContext = mContext;
        this.uiListener = uiListener;
        this.dbHeadTable = dbHeadTable;
        this.arrtable = arrtable;
        this.type = type;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        boolean storeOpened = false;// TODO: 19-12-2018 Hided for version upgrade
      /*  OnlineStoreListener openListener = OnlineStoreListener.getInstance();
        OnlineODataStore store = openListener.getStore();
        if (store != null) {
            storeOpened = true;
        } else {

            try {
                storeOpened = OnlineManager.openOnlineStore(mContext);
            } catch (OnlineODataStoreException e) {
                e.printStackTrace();
            }

        }
        try {
            if (storeOpened) {
                if (type == 2) {
                    OnlineManager.updateTasksEntity(dbHeadTable, uiListener);
                }
            }
        } catch (OnlineODataStoreException e) {
            e.printStackTrace();
            TraceLog.e(Constants.SyncOnRequestSuccess, e);
            storeOpened = false;
            Constants.ErrorNo = Constants.Network_Error_Code_Offline;
        }*/
        return storeOpened;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (!aBoolean) {
            if (uiListener != null) {
                uiListener.onRequestError(0, new Exception(mContext.getString(R.string.no_network_conn)));
            }
        }
    }
}
