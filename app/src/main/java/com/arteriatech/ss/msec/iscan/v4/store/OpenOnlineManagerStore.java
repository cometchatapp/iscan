package com.arteriatech.ss.msec.iscan.v4.store;

import android.content.Context;
import android.os.AsyncTask;

import com.arteriatech.ss.msec.iscan.v4.mutils.common.OnlineODataStoreException;
import com.arteriatech.ss.msec.iscan.v4.interfaces.AsyncTaskCallBack;


/**
 * Created by e10860 on 11/13/2017.
 */

public class OpenOnlineManagerStore extends AsyncTask<String , Boolean,Boolean> {
    Context mContext;
    boolean isOnlineStoreOpened= false;
   private AsyncTaskCallBack asyncTaskCallBack;
    private String errorMessage ="";

    public OpenOnlineManagerStore(Context mContext, AsyncTaskCallBack asyncTaskCallBack){
        this.mContext=mContext;
        this.asyncTaskCallBack=asyncTaskCallBack;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {

        try {
            isOnlineStoreOpened = OnlineManager.openOnlineStore(mContext);
        } catch (OnlineODataStoreException e) {
            e.printStackTrace();
            errorMessage = e.getMessage();
        }
        return isOnlineStoreOpened ;
    }

    @Override
    protected void onPostExecute(Boolean s) {
        super.onPostExecute(s);
        if(asyncTaskCallBack!=null){
            asyncTaskCallBack.onStatus(isOnlineStoreOpened,errorMessage);
        }
    }
}
