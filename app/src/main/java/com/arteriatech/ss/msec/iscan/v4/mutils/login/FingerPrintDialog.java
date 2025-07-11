package com.arteriatech.ss.msec.iscan.v4.mutils.login;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;
import androidx.core.os.CancellationSignal;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.registration.RegistrationModel;
import com.arteriatech.ss.msec.iscan.v4.mutils.registration.UtilRegistrationActivity;
import com.arteriatech.ss.msec.iscan.v4.mutils.security.AppLockApplication;
import com.arteriatech.ss.msec.iscan.v4.mutils.security.AppLockManager;


/**
 * Created by e10769 on 11-10-2017.
 */

public class FingerPrintDialog extends Dialog implements View.OnClickListener {

    private boolean isFromAppLock = false;
    private TextView tvTouchSensor;
    private Button btUsePasscode;
    private Activity mActivity;
    private FingerprintManagerCompat fingerprintManager = null;
    private CancellationSignal cancellationSignal = null;
    private ImageView ivFingerPrint;
    private RegistrationModel registrationModel = null;
    private Bundle extraBundle = null;

    public FingerPrintDialog(@NonNull Activity context, boolean isFromAppLock, RegistrationModel registrationModel, Bundle extraBundle) {
        super(context);
        this.mActivity = context;
        this.isFromAppLock = isFromAppLock;
        this.registrationModel = registrationModel;
        this.extraBundle = extraBundle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.finger_print_dialog);
        tvTouchSensor = (TextView) findViewById(R.id.tvTouchSensor);
        btUsePasscode = (Button) findViewById(R.id.btUsePasscode);
        ivFingerPrint = (ImageView) findViewById(R.id.ivFingerPrint);
        btUsePasscode.setOnClickListener(this);
        fingerprintManager = FingerprintManagerCompat.from(mActivity);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btUsePasscode) {
            dismiss();
        }
//        switch (v.getId()) {
//            case R.id.btUsePasscode:
//                dismiss();
//                break;
//
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializeFingerPrint(mActivity);
    }


    private void initializeFingerPrint(Activity mActivity) {
        if (fingerprintManager != null) {
            cancellationSignal = new CancellationSignal();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
            }
            fingerprintManager.authenticate(null, 0, cancellationSignal, getFingerprintCallback(mActivity), null);
        }
    }

    protected FingerprintManagerCompat.AuthenticationCallback getFingerprintCallback(final Activity mActivity) {
        return new FingerprintManagerCompat.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                // without the call to verifyPassword the unlock screen will show multiple times
                super.onAuthenticationSucceeded(result);
                if (AppLockManager.getInstance().getAppLock().verifyPassword(AppLockApplication.FINGERPRINT_VERIFICATION_BYPASS)) {
                    redirectSuccessActivity();
                } else {
                    wrongPassword();
                }
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                wrongPassword();
            }

            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                super.onAuthenticationError(errMsgId, errString);
//                ConstantsUtils.authenticationFailed(PasscodeLoginActivity.this, rlView);
//                ConstantsUtils.displayShortToast(PasscodeLoginActivity.this, getString(R.string.you_have_wrong_finger));
//                redirectToLoginScreen();
            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                super.onAuthenticationHelp(helpMsgId, helpString);
//                Toast.makeText(context, "AuthenticationHelp", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void wrongPassword() {

//                ConstantsUtils.displayShortToast(mActivity, mActivity.getString(R.string.you_have_wrong_finger));
        ivFingerPrint.setImageResource(R.drawable.ic_finger_print_error);
        PasscodeLoginActivity.currentAttempt++;
        if (PasscodeLoginActivity.currentAttempt == 2) {
            tvTouchSensor.setText(mActivity.getString(R.string.wrong_finger_max_attempt));
        } else {
            tvTouchSensor.setText(mActivity.getString(R.string.you_have_wrong_finger));
        }
        if (PasscodeLoginActivity.totalAttempt == PasscodeLoginActivity.currentAttempt) {
            if (cancellationSignal != null) {
                cancellationSignal.cancel();
            }
            PasscodeLoginActivity.redirectToLoginScreen(mActivity, registrationModel, extraBundle);
        }
    }

    private void redirectSuccessActivity() {
//        UtilConstants.hideKeyboardFrom(mActivity);
        if (!isFromAppLock) {
//            Intent goToNextActivity = new Intent(mActivity, MainActivity.class);
            if (registrationModel != null) {
                Intent goToNextActivity = new Intent(mActivity, registrationModel.getMainActivity());
                goToNextActivity.putExtra(UtilConstants.RegIntentKey, registrationModel);
                if (extraBundle != null)
                    goToNextActivity.putExtra(UtilRegistrationActivity.EXTRA_BUNDLE_REGISTRATION, extraBundle);
                goToNextActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mActivity.startActivity(goToNextActivity);
            }
        }
        mActivity.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (cancellationSignal != null) {
            cancellationSignal.cancel();
        }
    }
}
