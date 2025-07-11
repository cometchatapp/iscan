package com.arteriatech.ss.msec.iscan.v4.mutils.login;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.core.os.CancellationSignal;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.mutils.actionbar.ActionBarView;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.registration.RegistrationModel;
import com.arteriatech.ss.msec.iscan.v4.mutils.registration.UtilRegistrationActivity;
import com.arteriatech.ss.msec.iscan.v4.mutils.security.AppLockManager;
import com.arteriatech.ss.msec.iscan.v4.mutils.security.DefaultAppLock;
import com.arteriatech.ss.msec.iscan.v4.mutils.security.SecurityUtils;

@SuppressLint("NewApi")
public class PasscodeLoginActivity extends AppCompatActivity {

    public static final String EXTRA_PASSCODE_STATUS = "passcodeStatus";
    public static int totalAttempt = 3;
    public static int currentAttempt = 0;
    Button btn_Okay, btn_pinclear;
    EditText ed_Loginone, ed_Logintwo, ed_Loginthree, ed_Loginfour;
    ImageView user_profile_photo;
    String enteredKey;
    private RelativeLayout rlView;
    private CancellationSignal cancellationSignal = null;
    private FingerprintManagerCompat fingerprintManager;
    private boolean onFingerPrintAvailable = false;
    private boolean isFromAppLock = false;
    private Toolbar toolbar;
    private boolean isVerifyPasscode = false;
    private RegistrationModel registrationModel = null;
    private Bundle extraBundle = null;

    public static void redirectToLoginScreen(Activity mActivity, RegistrationModel regModel, Bundle extraBundle) {
        removePasscode(mActivity);
        UtilConstants.hideKeyboardFrom(mActivity);
        if (regModel != null) {
            Intent goToNextActivity = new Intent(mActivity, regModel.getLogInActivity());
            goToNextActivity.putExtra(UtilConstants.RegIntentKey, regModel);
            goToNextActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            if (extraBundle != null)
                goToNextActivity.putExtra(UtilRegistrationActivity.EXTRA_BUNDLE_REGISTRATION, extraBundle);
            mActivity.startActivity(goToNextActivity);
            mActivity.finish();
        }
    }

    public static void removePasscode(Context mContext) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(UtilConstants.QUICK_PIN, "");
        editor.putString(UtilConstants.QUICK_PIN_ACCESS, "");
        editor.putString(UtilConstants.ENABLE_ACCESS, "");
        editor.putString(UtilConstants.ENABLE_FINGERPRINT_ACCESS, "");
        editor.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_login);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            registrationModel = (RegistrationModel) bundle.getSerializable(UtilConstants.RegIntentKey);
            isFromAppLock = bundle.getBoolean(DefaultAppLock.EXTRA_FROM_APPLOCK);
            isVerifyPasscode = bundle.getBoolean(DefaultAppLock.EXTRA_FROM_VERIFY_PASSCODE);
            extraBundle = bundle.getBundle(UtilRegistrationActivity.EXTRA_BUNDLE_REGISTRATION);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        user_profile_photo = (ImageView) findViewById(R.id.user_profile_photo);
        if (registrationModel != null) {
            int icon = 0;
            if (registrationModel.getAppActionBarIcon() != 0) {
                icon = registrationModel.getAppActionBarIcon();
            }
            AppLockManager.getInstance().getAppLock().setUnlockClassName(registrationModel.getNoPasscodeActivity());
            ActionBarView.initActionBarView(this, toolbar, false, getString(R.string.app_name), icon, 0);
            user_profile_photo.setImageResource(registrationModel.getAppLogo());
        }
        currentAttempt = 0;

        ed_Loginone = (EditText) findViewById(R.id.ed_loginone);
        ed_Logintwo = (EditText) findViewById(R.id.ed_logintwo);
        ed_Loginthree = (EditText) findViewById(R.id.ed_loginthree);
        ed_Loginfour = (EditText) findViewById(R.id.ed_loginfour);
        rlView = (RelativeLayout) findViewById(R.id.rlView);
        btn_Okay = (Button) findViewById(R.id.btn_ok);
        btn_pinclear = (Button) findViewById(R.id.btn_pinclear);
        btn_pinclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ed_Loginfour.setText("");
                ed_Loginthree.setText("");
                ed_Logintwo.setText("");
                ed_Loginone.setText("");
                ed_Loginone.requestFocus();
            }
        });

        btn_Okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinValidate();
            }
        });

        ed_Loginone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pinValidate();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ed_Loginone.getText().toString().length() == 1) {
                    ed_Logintwo.requestFocus();
                }
            }
        });
        ed_Logintwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pinValidate();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ed_Logintwo.getText().toString().length() == 1) {
                    ed_Loginthree.requestFocus();
                } else if (ed_Logintwo.getText().toString().length() == 0) {
                    ed_Loginone.requestFocus();
                }
            }
        });
        ed_Loginthree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pinValidate();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ed_Loginthree.getText().toString().length() == 1) {
                    ed_Loginfour.requestFocus();
                } else if (ed_Loginthree.getText().toString().length() == 0) {
                    ed_Logintwo.requestFocus();
                }
                /*if (ed_Loginthree.getText().toString().length() == 1) {
                    ed_Loginfour.requestFocus();

                }*/
            }
        });

        ed_Loginfour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                pinValidate();
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ed_Loginfour.getText().toString().length() == 1) {
                    ed_Loginfour.clearFocus();
                }
            }
        });
        onFingerPrintAvailable = checkFingerPrintAvalilable(PasscodeLoginActivity.this);
        if (onFingerPrintAvailable) {
            if (registrationModel != null) {
                FingerPrintDialog fingerPrintDialog = new FingerPrintDialog(PasscodeLoginActivity.this, isFromAppLock, registrationModel, extraBundle);
                fingerPrintDialog.show();
            }
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
        }
    }

    private void pinValidate() {
        String pinOne = ed_Loginone.getText().toString();
        String pinTwo = ed_Logintwo.getText().toString();
        String pinThree = ed_Loginthree.getText().toString();
        String pinFour = ed_Loginfour.getText().toString();
        if (!TextUtils.isEmpty(pinOne) && !TextUtils.isEmpty(pinTwo) && !TextUtils.isEmpty(pinThree) && !TextUtils.isEmpty(pinFour)) {
            enteredKey = pinOne + pinTwo + pinThree + pinFour;
            if (AppLockManager.getInstance().getAppLock().verifyPassword(enteredKey)) {
                redirectSuccessActivity();
            } else {
                clearAllText();
                UtilConstants.authenticationFailed(PasscodeLoginActivity.this, rlView);

                currentAttempt++;
                if (PasscodeLoginActivity.currentAttempt == 2) {
                    UtilConstants.displayShortToast(PasscodeLoginActivity.this, getString(R.string.wrong_finger_max_attempt));
                } else {
                    UtilConstants.displayShortToast(PasscodeLoginActivity.this, getString(R.string.you_have_enterd_worng_pin));
                }
                if (totalAttempt == currentAttempt) {
                    redirectToLoginScreen(PasscodeLoginActivity.this, registrationModel, extraBundle);
                }
            }
        }
    }

    private void redirectSuccessActivity() {
        UtilConstants.hideKeyboardFrom(PasscodeLoginActivity.this);
        if (isVerifyPasscode) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_PASSCODE_STATUS, true);
            setResult(UtilConstants.ACTIVITY_RESULT_PASSCODE, intent);
        } else if (!isFromAppLock) {
            if (registrationModel != null) {
                Intent goToNextActivity = new Intent(this, registrationModel.getMainActivity());
                goToNextActivity.putExtra(UtilConstants.RegIntentKey, registrationModel);
                if (extraBundle != null)
                    goToNextActivity.putExtra(UtilRegistrationActivity.EXTRA_BUNDLE_REGISTRATION, extraBundle);
                goToNextActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(goToNextActivity);
            }

        }
        finish();
    }

    private void clearAllText() {
        ed_Loginfour.setText("");
        ed_Loginthree.setText("");
        ed_Logintwo.setText("");
        ed_Loginone.setText("");
        ed_Loginone.requestFocus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_passcode_login, menu);
        MenuItem menuItem = menu.findItem(R.id.menu_finger);
        if (onFingerPrintAvailable) {
            menuItem.setVisible(true);

//                fingerprintManager = (FingerprintManager) getSystemService(FINGERPRINT_SERVICE);
//                fpHelper = new FingerprintHandler(PasscodeLoginActivity.this,this);
//                fpHelper.startAuth(fingerprintManager);


        } else {
            menuItem.setVisible(false);
        }
        return true;
    }

    private boolean checkFingerPrintAvalilable(Activity mContext) {
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(mContext);
        if (UtilConstants.SECURITY_ON.equalsIgnoreCase(shared.getString(UtilConstants.ENABLE_FINGERPRINT_ACCESS, ""))) {
            if (SecurityUtils.checkPermission(mContext)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.menu_finger) {
            if (registrationModel != null) {
                FingerPrintDialog fingerPrintDialog = new FingerPrintDialog(PasscodeLoginActivity.this, isFromAppLock, registrationModel,extraBundle);
                fingerPrintDialog.show();
            }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (registrationModel != null && registrationModel.isBackToStartActivity() && !isFromAppLock && !isVerifyPasscode) {
            super.onBackPressed();
        } else if (!isVerifyPasscode) {
            AppLockManager.getInstance().getAppLock().forcePasswordLock();
            Intent i = new Intent();
            i.setAction(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            startActivity(i);
            finish();
        } else {
            finish();
        }

    }

}
