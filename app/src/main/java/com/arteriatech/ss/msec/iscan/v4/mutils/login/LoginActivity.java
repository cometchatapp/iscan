package com.arteriatech.ss.msec.iscan.v4.mutils.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.arteriatech.ss.msec.iscan.v4.mutils.registration.UtilRegistrationActivity;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.arteriatech.ss.msec.iscan.v4.BuildConfig;
import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.mutils.actionbar.ActionBarView;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.registration.RegistrationModel;
import com.arteriatech.ss.msec.iscan.v4.mutils.registration.SupportActivity;
import com.sybase.persistence.DataVaultException;
import com.sybase.persistence.PrivateDataVault;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText userEdit = null;
    EditText passEdit = null;
    //    CheckBox showPass = null;
    CheckBox savePass = null;
    String appConnID = "", userName = "", pwd = "";
    Button bt_login = null, bt_login_clear = null;
    private boolean isNotification = false;
    private TextView bt_login_forget_pass;

    private int mIntMaxLimit = 0;
    private TextView appVersion;
    private Toolbar toolbar;
    private TextInputLayout il_Password;
    private TextInputLayout il_UserName;
    private RegistrationModel registrationModel = null;
    private int comingFrom = 0;
    private boolean isFromRegistration = false;
    private ImageView user_profile_photo;
    private Bundle extraBundle = null;
    private Button btSupport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ConstantsUtils.initActionBar(this, false, getString(R.string.app_name));
        setContentView(R.layout.activity_login);
        Bundle bundle = getIntent().getExtras();
        SharedPreferences settings = null;
        if (bundle != null) {
            comingFrom = bundle.getInt(UtilConstants.EXTRA_COME_FROM, 0);
            isFromRegistration = bundle.getBoolean(UtilRegistrationActivity.EXTRA_IS_FROM_REGISTRATION, false);
            extraBundle = bundle.getBundle(UtilRegistrationActivity.EXTRA_BUNDLE_REGISTRATION);
            registrationModel = (RegistrationModel) bundle.getSerializable(UtilConstants.RegIntentKey);
        }

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        user_profile_photo = (ImageView) findViewById(R.id.user_profile_photo);
        if (registrationModel != null) {
            int icon = 0;
            if (registrationModel.getAppActionBarIcon() != 0) {
                icon = registrationModel.getAppActionBarIcon();
            }
            ActionBarView.initActionBarView(this, toolbar, false, getString(R.string.app_name), icon, 0);
            settings = getSharedPreferences(registrationModel.getShredPrefKey(),
                    0);
            user_profile_photo.setImageResource(registrationModel.getAppLogo());
        }


        //getting username and password
        getUserNamePwd();
        //initialize UI
        initUI();

        //check for saved password
        checkPwdSaved(settings);

        boolean privateDataVault = false;
        try {
            privateDataVault = PrivateDataVault.vaultExists(getPackageName());
        } catch (DataVaultException e) {
            e.printStackTrace();
        }
        if(!privateDataVault){
            PrivateDataVault.createVault("com.arteriatech.ss.msec.bil.v4",Constants.EncryptKey.toCharArray());
        }

    }

    /*Checks password saved or not*/
    void checkPwdSaved(SharedPreferences settings) {
        if (settings.contains(UtilConstants.isPasswordSaved)) {
            settings.getBoolean(UtilConstants.isPasswordSaved, false);
        } else {
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(UtilConstants.isPasswordSaved, false);
            editor.commit();
        }

       /* boolean mBooleanPwdSaved =settings.getBoolean(Constants.isPasswordSaved, false);
        savePass.setChecked(mBooleanPwdSaved);
        if (mBooleanPwdSaved) {
            passEdit.setText(pwd);
        }*/
    }

    /*Initializes User interfaces of screen*/
    public void initUI() {
        userEdit = (EditText) findViewById(R.id.et_login_username);
        userEdit.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        passEdit = (EditText) findViewById(R.id.et_login_password);
        appVersion = (TextView) findViewById(R.id.tv_version);
        if (registrationModel != null) {
            appVersion.setText(registrationModel.getAppVersionName());
        }
        il_UserName = (TextInputLayout) findViewById(R.id.ilUserName);
        il_Password = (TextInputLayout) findViewById(R.id.ilPassword);
        userEdit.setText(userName);
        userEdit.setFocusable(true);
        passEdit.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//        showPass = (CheckBox) findViewById(R.id.ch_login_show_pass);
        savePass = (CheckBox) findViewById(R.id.ch_login_save_pass);
//        showPass.setOnClickListener(this);
        savePass.setOnClickListener(this);

        bt_login = (Button) findViewById(R.id.bt_login);
        btSupport = (Button) findViewById(R.id.btSupport);

        bt_login.setOnClickListener(this);
        btSupport.setOnClickListener(this);
        bt_login_clear = (Button) findViewById(R.id.bt_login_clear);

        bt_login_clear.setOnClickListener(this);

//        bt_login_exit = (Button) findViewById(R.id.bt_login_exit);

//        bt_login_exit.setOnClickListener(this);

        bt_login_forget_pass = (TextView) findViewById(R.id.bt_login_forget_pass);

        bt_login_forget_pass.setOnClickListener(this);
        if (registrationModel != null) {
            if (registrationModel.isDisplayForgetPsw()){
                bt_login_forget_pass.setVisibility(View.VISIBLE);
            }else {
                bt_login_forget_pass.setVisibility(View.GONE);
            }
            if (registrationModel.isLgnUsrNameNonEdit()) {
                userEdit.setFocusable(false);
                userEdit.setCursorVisible(false);
                userEdit.setInputType(InputType.TYPE_NULL);
            }
        }

    }

    @Override
    public void onClick(View v) {

        /*switch (v.getId()) {
            case R.id.bt_login:
                onValidation();
                break;
            case R.id.bt_login_clear:
                onClear();
                break;

            case R.id.bt_login_forget_pass:
                onForgetPwd();
                break;


            case R.id.ch_login_save_pass:
                SharedPreferences settings = getSharedPreferences(
                        registrationModel.getShredPrefKey(), 0);
                if (savePass.isChecked()) {
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean(UtilConstants.savePass, true);
                    editor.commit();
                    savePass.setChecked(true);

                } else {
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean(UtilConstants.savePass, false);
                    editor.commit();
                    savePass.setChecked(false);
                }
                break;
        }*/
        if (v.getId() == R.id.bt_login) {
            onValidation();
        } else if (v.getId() == R.id.bt_login_clear) {
            onClear();

        } else if (v.getId() == R.id.bt_login_forget_pass) {
            onForgetPwd();
        } else if (v.getId() == R.id.ch_login_save_pass) {
            SharedPreferences settings = getSharedPreferences(
                    registrationModel.getShredPrefKey(), 0);
            if (savePass.isChecked()) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(UtilConstants.savePass, true);
                editor.commit();
                savePass.setChecked(true);

            } else {
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(UtilConstants.savePass, false);
                editor.commit();
                savePass.setChecked(false);
            }
        } else if (v.getId() == R.id.btSupport) {
            Intent intent = new Intent(LoginActivity.this, SupportActivity.class);
            intent.putExtra(UtilConstants.RegIntentKey, registrationModel);
            if (extraBundle != null)
                intent.putExtra(UtilRegistrationActivity.EXTRA_BUNDLE_REGISTRATION, extraBundle);
            startActivity(intent);
        }
    }

    /*Reads username and password from Logon Context*/
    private void getUserNamePwd() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.PREFS_NAME, 0);
        userName = sharedPreferences.getString(UtilRegistrationActivity.KEY_username,"");
        pwd = sharedPreferences.getString(UtilRegistrationActivity.KEY_password,"");
       /* try {
            // get Application Connection ID
            LogonCoreContext lgCtx = LogonCore.getInstance().getLogonContext();
            userName = lgCtx.getBackendUser();
            pwd = lgCtx.getBackendPassword();
            appConnID = LogonCore.getInstance().getLogonContext()
                    .getConnId();
        } catch (LogonCoreException e) {
            LogManager.writeLogError(UtilConstants.device_reg_failed_txt, e);
        }*/
    }

    /*Alert for forgot password*/
    private void alertForgetPwdMsg() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.UtilsDialogTheme);
        builder.setMessage(R.string.alert_user_is_locked)
                .setCancelable(false)
                .setPositiveButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                onForgetPwd();

                            }
                        })
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        builder.show();
    }

    /*Validates username and password*/
    private void onValidation() {

        if (!validateFields()) {
            if (userEdit.getText().toString().toUpperCase().trim()
                    .equalsIgnoreCase(userName)
                    && passEdit.getText().toString().trim()
                    .equals(pwd)) {

                SharedPreferences settings = getSharedPreferences(registrationModel.getShredPrefKey(), 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(UtilConstants.isPasswordSaved, savePass.isChecked());
                editor.commit();
                mIntMaxLimit = 0;
                if (UtilConstants.isNetworkAvailable(getApplicationContext())) {
                    onMainMenuLogin();
                } else {
                    UtilConstants.displayShortToast(LoginActivity.this, getString(R.string.validation_offline_login_no_internet));
                    //Navigating to main menu
                    onMainMenuLogin();
                }

            } else {


                if (mIntMaxLimit == 3) {
                    alertForgetPwdMsg();
                } else {
                    mIntMaxLimit++;
                    UtilConstants.displayShortToast(LoginActivity.this, getString(R.string.validation_plz_enter_username_pwd));
                }

            }
        } /*else {
            ConstantsUtils.displayShortToast(LoginActivity.this,getString(R.string.validation_plz_fill_all_mandatory_flds));
        }*/

    }

    private boolean validateFields() {
        int isValidMandotry = 0;
        il_UserName.setErrorEnabled(false);
        il_Password.setErrorEnabled(false);
        String userName = userEdit.getText().toString();
        String password = passEdit.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            isValidMandotry = 1;
            il_UserName.setErrorEnabled(true);
            il_UserName.setError(getString(R.string.validation_plz_enter_user_name));
        } else {
            boolean areSpaces = checkIfSpaces(userName);
            if (areSpaces) {
                isValidMandotry = 3;
                il_UserName.setErrorEnabled(true);
                il_UserName.setError(getString(R.string.validation_user_name_space));
            }
        }
        if (TextUtils.isEmpty(password)) {
            isValidMandotry = 1;
            il_Password.setErrorEnabled(true);
            il_Password.setError(getString(R.string.validation_plz_enter_psw));
        } else {
            boolean areSpaces = checkIfSpaces(password);
            if (areSpaces) {
                il_Password.setErrorEnabled(true);
                il_Password.setError(getString(R.string.validation_psw_space));
                isValidMandotry = 3;
            }
        }
        return isValidMandotry != 0;
    }

    /**
     * This method checks spaces available or not.
     */
    public boolean checkIfSpaces(String str) {
        boolean result = false;

        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(str);
        result = matcher.find();

        return result;

    }

    /*Navigates to main menu*/
    private void onMainMenuLogin() {
        Intent intent = new Intent(this, registrationModel.getMainActivity());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(UtilConstants.SHOWNOTIFICATION, isNotification);
        intent.putExtra(UtilConstants.RegIntentKey, registrationModel);
        if (extraBundle != null)
            intent.putExtra(UtilRegistrationActivity.EXTRA_BUNDLE_REGISTRATION, extraBundle);
        startActivity(intent);
        finish();
    }

    /*This method calls when back button pressed*/
    @Override
    public void onBackPressed() {
        onExit();
        /*AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyTheme);
        builder.setMessage(R.string.do_u_want_exit_app)
                .setCancelable(false)
                .setPositiveButton(R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                onExit();

                            }
                        })
                .setNegativeButton(R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        builder.show();*/

    }

    /*This method executes exit current activity to home activity
     */
    public void onExit() {
        if (registrationModel!=null && registrationModel.isBackToStartActivity()){
            super.onBackPressed();
        }else {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            startActivity(homeIntent);
            finish();
        }
    }

    /*clears password field*/
    private void onClear() {
        passEdit.setText("");
        passEdit.setFocusable(true);
        passEdit.setFocusableInTouchMode(true);
        il_UserName.setErrorEnabled(false);
        il_Password.setErrorEnabled(false);
    }

    /*Navigates to forget password screen*/
    private void onForgetPwd() {
//        Intent intent = new Intent(this, ForgotPasswordActivity.class);
//        startActivity(intent);
    }

    /*displays popup*/
    /*public void showPopup(View v) {
        UtilConstants.showPopup(getApplicationContext(), v, LoginActivity.this,
                R.menu.menu_back);
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_back:
                onBackPressed();
                break;

            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }*/

}
