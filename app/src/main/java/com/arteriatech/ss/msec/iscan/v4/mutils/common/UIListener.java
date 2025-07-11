package com.arteriatech.ss.msec.iscan.v4.mutils.common;


import com.sap.smp.client.odata.exception.ODataException;

public interface UIListener {
	void onRequestError(int operation, Exception e);
	void onRequestSuccess(int operation, String key) throws ODataException, OfflineODataStoreException;
//	void onRequestErrorBackground(int operation, Exception e);
//	void onRequestSuccessBackground(int operation, String key) throws ODataException, OfflineODataStoreException;
}
