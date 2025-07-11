package com.arteriatech.ss.msec.iscan.v4.mutils.support;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.mutils.actionbar.ActionBarView;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.interfaces.FragmentCallbackInterface;
import com.arteriatech.ss.msec.iscan.v4.mutils.registration.RegistrationModel;
import com.arteriatech.ss.msec.iscan.v4.mutils.registration.UtilRegistrationActivity;

public class PasscodeActivity extends AppCompatActivity implements FragmentCallbackInterface {

    private boolean isFromRegistration = false;
    private Bundle extraBundle = null;
    private SharedPreferences sharedPreferences;
    private Toolbar toolbar;
    private CardView llOnBoarding = null;
    private String TAG = PasscodeActivity.class.getSimpleName();
    private RegistrationModel registrationModel=null;
    private int comingFrom = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);
//        ConstantsUtils.initActionBar(this, false, getString(R.string.settings));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(PasscodeActivity.this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            comingFrom = bundle.getInt(UtilConstants.EXTRA_COME_FROM, 0);
            isFromRegistration = bundle.getBoolean(UtilRegistrationActivity.EXTRA_IS_FROM_REGISTRATION, false);
            extraBundle = bundle.getBundle(UtilRegistrationActivity.EXTRA_BUNDLE_REGISTRATION);
            registrationModel = (RegistrationModel) bundle.getSerializable(UtilConstants.RegIntentKey);
        }
        if(registrationModel!=null) {
            int icon = 0;
            if (registrationModel.getAppActionBarIcon() != 0) {
                icon = registrationModel.getAppActionBarIcon();
            }
            ActionBarView.initActionBarView(this, toolbar, false, getString(R.string.settings), icon,0);
//            if (isFromRegistration) {
//                registrationModel.getmApplication().startService(PasscodeActivity.this, isFromRegistration);
//            }
        }
        llOnBoarding = (CardView) findViewById(R.id.llOnBoarding);
        llOnBoarding.setVisibility(View.VISIBLE);
        Fragment fragment = new PasscodeSettingsFragment();
//        bundle = new Bundle();
        bundle.putInt(UtilConstants.EXTRA_COME_FROM, 1);
        bundle.putBoolean(UtilRegistrationActivity.EXTRA_IS_FROM_REGISTRATION, isFromRegistration);
        if (extraBundle != null)
            bundle.putBundle(UtilRegistrationActivity.EXTRA_BUNDLE_REGISTRATION, extraBundle);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flContainer, fragment, fragment.getClass().getName());
        fragmentTransaction.commit();
    }

    @Override
    public void fragmentCallBack(String title, Bundle bundle) {
        Log.d(TAG, "fragmentCallBack: " + title);
        /*if (title.equalsIgnoreCase(getString(R.string.settings)))
            ConstantsUtils.initActionBar(this, false, getString(R.string.settings));
        else
            ConstantsUtils.initActionBar(this, true, title);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_skip, menu);
        MenuItem item = menu.findItem(R.id.menu_skip);
//        if (TextUtils.isEmpty(sharedPreferences.getString(Constants.QUICK_PIN, ""))) {
            item.setTitle(getString(R.string.menu_done));
//        } else {
//            item.setTitle(getString(R.string.next_menu));
//        }
        if (llOnBoarding != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            int numberOfFragment = fragmentManager.getBackStackEntryCount();
            if (numberOfFragment > 0) {
                llOnBoarding.setVisibility(View.GONE);
            } else {
                llOnBoarding.setVisibility(View.VISIBLE);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.menu_skip) {
                Intent intent = new Intent(PasscodeActivity.this, registrationModel.getMainActivity());
                intent.putExtra(UtilRegistrationActivity.EXTRA_IS_FROM_REGISTRATION, isFromRegistration);
                if (registrationModel != null) {
                    intent.putExtra(UtilConstants.RegIntentKey, registrationModel);
                }
                startActivity(intent);
                finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
