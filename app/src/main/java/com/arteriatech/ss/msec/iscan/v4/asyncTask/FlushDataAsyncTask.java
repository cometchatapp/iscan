package com.arteriatech.ss.msec.iscan.v4.asyncTask;

import android.os.AsyncTask;

import com.arteriatech.ss.msec.iscan.v4.mutils.common.OfflineODataStoreException;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UIListener;
import com.arteriatech.ss.msec.iscan.v4.store.OfflineManager;
import com.sap.smp.client.odata.exception.ODataException;

import java.util.ArrayList;

public class FlushDataAsyncTask extends AsyncTask<Void, Void, Void> {
    private UIListener uiListener;
    private ArrayList<String> alFlushColl;
    private String concatFlushCollStr = "";

    public FlushDataAsyncTask(UIListener uiListener, ArrayList<String> flushColl) {
        this.uiListener = uiListener;
        this.alFlushColl = flushColl;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Thread.sleep(1000);

            for (int incVal = 0; incVal < alFlushColl.size(); incVal++) {
                if (incVal == 0 && incVal == alFlushColl.size() - 1) {
                    concatFlushCollStr = concatFlushCollStr + alFlushColl.get(incVal);
                } else if (incVal == 0) {
                    concatFlushCollStr = concatFlushCollStr + alFlushColl.get(incVal) + ", ";
                } else if (incVal == alFlushColl.size() - 1) {
                    concatFlushCollStr = concatFlushCollStr + alFlushColl.get(incVal);
                } else {
                    concatFlushCollStr = concatFlushCollStr + alFlushColl.get(incVal) + ", ";
                }
            }

            try {
                if (!OfflineManager.offlineStore.getRequestQueueIsEmpty()) {
                    try {
                        OfflineManager.flushQueuedRequests(uiListener, concatFlushCollStr);
                    } catch (OfflineODataStoreException e) {
                        e.printStackTrace();
                    }
                }

            } catch (ODataException e) {
                e.printStackTrace();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
