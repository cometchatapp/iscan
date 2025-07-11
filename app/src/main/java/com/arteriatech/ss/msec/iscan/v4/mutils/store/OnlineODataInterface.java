package com.arteriatech.ss.msec.iscan.v4.mutils.store;

import android.os.Bundle;

import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.store.ODataRequestExecution;

import java.util.List;

/**
 * Created by e10769 on 04-09-2017.
 */

public interface OnlineODataInterface {
    void responseSuccess(ODataRequestExecution oDataRequestExecution, List<ODataEntity> entities, Bundle bundle);
    void responseFailed(ODataRequestExecution oDataRequestExecution, String errorMsg, Bundle bundle);
}
