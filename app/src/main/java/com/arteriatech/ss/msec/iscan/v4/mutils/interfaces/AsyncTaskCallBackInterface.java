package com.arteriatech.ss.msec.iscan.v4.mutils.interfaces;

/**
 * Created by e10769 on 08-09-2017.
 */

public interface AsyncTaskCallBackInterface<T> {
    void asyncResponse(boolean status, T response, String message);
}
