package com.arteriatech.ss.msec.iscan.v4.retailercreate;

import android.content.Context;
import android.os.AsyncTask;

public class ImageAsyncTask extends AsyncTask<String, Void, byte[]> {
    ImageCallBack imageCallback;
    Context context;
    byte[] imageByteArray = null;

    public ImageAsyncTask(Context context) {
        this.context = context;
        if (context instanceof ImageCallBack) {
            imageCallback = (ImageCallBack) context;
        }

    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected byte[] doInBackground(String... mStrBundleCpGuid) {


        return imageByteArray;
    }


    @Override
    protected void onPostExecute(byte[] imageByteArray) {
        super.onPostExecute(imageByteArray);
        if (imageCallback != null) {
            if (imageByteArray != null)
                imageCallback.getImageByte(imageByteArray, 0, imageByteArray.length);
        }

    }


}