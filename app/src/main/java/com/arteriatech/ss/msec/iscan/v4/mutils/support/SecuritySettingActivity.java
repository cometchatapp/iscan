package com.arteriatech.ss.msec.iscan.v4.mutils.support;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.mutils.actionbar.ActionBarView;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.registration.RegistrationModel;

/**
 * Created by e10860 on 2/8/2018.
 */

public class SecuritySettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_setting);
        Bundle bundle = getIntent().getExtras();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //  UtilConstants.initCustomActionBarView(this, toolbar, true, getString(R.string.settings),0);
        ActionBarView.initActionBarView(this, toolbar, true, getString(R.string.settings), 0, 0);
        Fragment mainMenuFragment = new com.arteriatech.ss.msec.iscan.v4.mutils.support.SettingsFragment();
        RegistrationModel registrationModel = (RegistrationModel) bundle.getSerializable(UtilConstants.RegIntentKey);
        bundle.putSerializable(UtilConstants.RegIntentKey, registrationModel);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mainMenuFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.flContainer, mainMenuFragment, mainMenuFragment.getClass().getName());
        fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
