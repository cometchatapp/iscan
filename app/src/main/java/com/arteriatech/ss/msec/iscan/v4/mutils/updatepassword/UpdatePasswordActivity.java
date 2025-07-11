/*
package com.arteriatech.ss.msec.bil.v2.mutils.updatepassword;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.arteriatech.ss.msec.bil.R;
import com.arteriatech.ss.msec.bil.v2.mutils.actionbar.ActionBarView;
import com.arteriatech.ss.msec.bil.v2.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.bil.v2.mutils.interfaces.DialogCallBack;
import com.arteriatech.ss.msec.bil.v2.mutils.registration.RegistrationModel;

*/
/**
 * Created by e10526 on 5/30/2017.
 *
 *//*


public class UpdatePasswordActivity extends AppCompatActivity implements View.OnClickListener {
    EditText etNewPwd, etConfirmPwd;
    CheckBox showPass;
    private String mStrNewPwd = "";
    private Toolbar toolbar;
    private RegistrationModel registrationModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Bundle bundleExtras = getIntent().getExtras();
        if (bundleExtras != null) {
            registrationModel = (RegistrationModel) bundleExtras.getSerializable(UtilConstants.RegIntentKey);
        }
        int icon = 0;
        if (registrationModel.getAppActionBarIcon() != 0) {
            icon = registrationModel.getAppActionBarIcon();
        }
        ActionBarView.initActionBarView(this, toolbar, false, getString(R.string.app_name), icon, 0);
        initUI();

    }

    private void initUI() {
        showPass = (CheckBox) findViewById(R.id.cbShowPwd);
        showPass.setOnClickListener(this);

        etNewPwd = (EditText) findViewById(R.id.etNewPwd);
        etNewPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etNewPwd.setBackgroundResource(R.drawable.edittext);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etConfirmPwd = (EditText) findViewById(R.id.etConfirmPwd);
        etConfirmPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                etNewPwd.setBackgroundResource(R.drawable.edittext);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void setPwdInDataVault() {
       */
/* try {
            // get Application Connection ID
            LogonCoreContext lgCtx = LogonCore.getInstance().getLogonContext();
            lgCtx.setBackendPassword(mStrNewPwd);
        } catch (LogonCoreException e) {
            e.printStackTrace();
        }*//*

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_update_pwd, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       */
/* switch (item.getItemId()) {

            case R.id.menu_update_pwd:

                if (isVaildationSuccess()) {
                    mStrNewPwd = etNewPwd.getText().toString();
                    setPwdInDataVault();
                    UtilConstants.dialogBoxWithCallBack(this, "", getString(R.string.alert_pwd_updated_succefully), getString(R.string.ok), "", false, new DialogCallBack() {
                        @Override
                        public void clickedStatus(boolean clickedStatus) {
//                            android.os.Process.killProcess(android.os.Process.myPid());
                            finishAffinity();
                            System.exit(0);
                        }
                    });
                }
                break;

            case android.R.id.home:
                onBackPressed();
                break;

        }
        return true;*//*

        if (item.getItemId()==R.id.menu_update_pwd) {
                if (isVaildationSuccess()) {
                    mStrNewPwd = etNewPwd.getText().toString();
                    setPwdInDataVault();
                    UtilConstants.dialogBoxWithCallBack(this, "", getString(R.string.alert_pwd_updated_succefully), getString(R.string.ok), "", false, new DialogCallBack() {
                        @Override
                        public void clickedStatus(boolean clickedStatus) {
//                            android.os.Process.killProcess(android.os.Process.myPid());
                            finishAffinity();
                            System.exit(0);
                        }
                    });
                }


        }else if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return true;
    }

    public Boolean isVaildationSuccess() {

        if (etNewPwd.getText().toString().equals("") || etConfirmPwd.getText().toString().equals("")) {
            if (etNewPwd.getText().toString().equals("")) {
                etNewPwd.setBackgroundResource(R.drawable.edittext_border);
            }
            if (etConfirmPwd.getText().toString().equals("")) {
                etConfirmPwd.setBackgroundResource(R.drawable.edittext_border);
            }
            UtilConstants.showAlert(getString(R.string.validation_plz_enter_mandatory_flds), UpdatePasswordActivity.this);
            return false;
        } else if (etNewPwd.getText().toString().length() < 6 || etConfirmPwd.getText().toString().length() < 6) {
            UtilConstants.showAlert(getString(R.string.validation_pwd_length_should_6_characters), UpdatePasswordActivity.this);
            return false;
        } else if (!etNewPwd.getText().toString().equalsIgnoreCase(etConfirmPwd.getText().toString())) {
            UtilConstants.showAlert(getString(R.string.validation_new_pwd_confirm_pwd_same), UpdatePasswordActivity.this);
            return false;
        } else {
            return true;
        }


    }

    @Override
    public void onClick(View v) {
       */
/* switch (v.getId()) {
            case R.id.cbShowPwd:
                if (showPass.isChecked()) {
                    etNewPwd.setTransformationMethod(null);
                    etNewPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                    etConfirmPwd.setTransformationMethod(null);
                    etConfirmPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    etNewPwd
                            .setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());
                    etNewPwd
                            .setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    etConfirmPwd
                            .setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());
                    etConfirmPwd
                            .setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                break;
            default:
                break;
        }*//*

        if (v.getId()==R.id.cbShowPwd) {
                if (showPass.isChecked()) {
                    etNewPwd.setTransformationMethod(null);
                    etNewPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);

                    etConfirmPwd.setTransformationMethod(null);
                    etConfirmPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    etNewPwd
                            .setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());
                    etNewPwd
                            .setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    etConfirmPwd
                            .setTransformationMethod(PasswordTransformationMethod
                                    .getInstance());
                    etConfirmPwd
                            .setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
        }
    }
}
*/
