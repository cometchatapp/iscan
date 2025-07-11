package com.arteriatech.ss.msec.iscan.v4.mutils.common;

public class OfflineODataStoreException extends Exception {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OfflineODataStoreException(String errorMessage) {
        super(errorMessage);
    }

	public OfflineODataStoreException(Throwable throwable) {
        super(throwable);
    }

}
