package com.arteriatech.ss.msec.iscan.v4.asyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.arteriatech.ss.msec.iscan.v4.mutils.common.OfflineODataStoreException;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UIListener;
import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.store.OfflineManager;

public class RefreshAsyncTask extends AsyncTask<String, Boolean, Boolean> {
    private Context mContext;
    private String refreshList;
    private UIListener uiListener;

    public RefreshAsyncTask(Context context, String refreshList, UIListener uiListener) {
        this.mContext = context;
        this.refreshList = refreshList;
        this.uiListener = uiListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        if (!OfflineManager.isOfflineStoreOpen()) {
            try {
//                OfflineManager.openOfflineStore(mContext, uiListener);
                OfflineManager.openOfflineStoreInternal(mContext, uiListener);
            } catch (Throwable e) {
                e.printStackTrace();
                LogManager.writeLogError(Constants.error_txt + e.getMessage());
            }
        } else {
            if (!TextUtils.isEmpty(refreshList)) {
                try {
                    OfflineManager.refreshStoreSync(mContext, uiListener, Constants.Fresh, refreshList);
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                    LogManager.writeLogError(Constants.error_txt + e.getMessage());
                }
            }else {
                try {
                    OfflineManager.refreshStoreSync(mContext, uiListener, Constants.ALL, refreshList);
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                    LogManager.writeLogError(Constants.error_txt + e.getMessage());
                }
            }
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

    }
}
