package com.arteriatech.ss.msec.iscan.v4.log;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.arteriatech.mutils.log.LogListFragment;
import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.common.ConstantsUtils;
import com.arteriatech.ss.msec.iscan.v4.common.MSFAApplication;


public class LogActivity extends AppCompatActivity{
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initialize action bar without back button(false)

        setContentView(R.layout.activity_log);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ConstantsUtils.initActionBarView(this, toolbar, true, getString(R.string.log_menu),0);

        //Calling LogList fragment
        getFragmentManager().beginTransaction().replace(R.id.fl_log_view, new LogListFragment()).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            MSFAApplication.setGAFields(this);
        } catch (Throwable e) {
            e.printStackTrace();
        }

       // MSFAApplication.setAnalytics(this,spNo,this.getClass().getSimpleName(),"View Log");
    }
}

