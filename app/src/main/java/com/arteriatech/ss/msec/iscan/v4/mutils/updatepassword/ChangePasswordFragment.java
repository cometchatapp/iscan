package com.arteriatech.ss.msec.iscan.v4.mutils.updatepassword;


import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.arteriatech.ss.msec.iscan.v4.mutils.registration.UtilRegistrationActivity;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.interfaces.DialogCallBack;
import com.arteriatech.ss.msec.iscan.v4.mutils.registration.RegistrationModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePasswordFragment extends Fragment {


    private EditText etOldPsw;
    private EditText etNewPsw;
    private EditText etConformPsw;
    private Button btClear;
    private Button btChangePsw;
    private SharedPreferences sharedPreferences;
    private LinearLayout coordinator;
    private TextView tvTitle;
    private TextInputLayout tilOldPassword;
    private TextInputLayout tilNewPassword;
    private TextInputLayout tilConformPassword;
    Bundle bundleExtra=null;
    Bundle bundle =null;
    private RegistrationModel registrationModel = null;
    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bundleExtra = getArguments();
        if (bundleExtra != null) {
            registrationModel = (RegistrationModel) bundleExtra.getSerializable(UtilConstants.RegIntentKey);
            bundle = bundleExtra.getBundle(UtilRegistrationActivity.EXTRA_BUNDLE_REGISTRATION);
        }
        return inflater.inflate(R.layout.fragment_change_password, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getContext().getSharedPreferences(registrationModel.getShredPrefKey(), 0);
        etOldPsw = (EditText) view.findViewById(R.id.etOldPsw);
        coordinator = (LinearLayout) view.findViewById(R.id.coordinator);
        etNewPsw = (EditText) view.findViewById(R.id.etNewPsw);
        etConformPsw = (EditText) view.findViewById(R.id.etConformPsw);
        btClear = (Button) view.findViewById(R.id.btClear);
        btChangePsw = (Button) view.findViewById(R.id.btChangePsw);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);

        tilOldPassword = (TextInputLayout) view.findViewById(R.id.tilOldPassword);
        tilNewPassword = (TextInputLayout) view.findViewById(R.id.tilNewPassword);
        tilConformPassword = (TextInputLayout) view.findViewById(R.id.tilConformPassword);

        tvTitle.setText(getString(R.string.settings_change_password));
        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etOldPsw.setText("");
                etNewPsw.setText("");
                etConformPsw.setText("");
                tilOldPassword.setErrorEnabled(false);
                tilNewPassword.setErrorEnabled(false);
                tilConformPassword.setErrorEnabled(false);
            }
        });
        btChangePsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UtilConstants.hideKeyboardFrom(getContext());
                String stOldPsw = etOldPsw.getText().toString();
                String stNewPsw = etNewPsw.getText().toString();
                String stConformPsw = etConformPsw.getText().toString();
                if (validatePassword(stOldPsw, stNewPsw, stConformPsw)) {
                    setPwdInDataVault(stNewPsw);
                    UtilConstants.dialogBoxWithCallBack(getContext(), "", getString(R.string.alert_pwd_updated_succefully), getString(R.string.ok), "", false, new DialogCallBack() {
                        @Override
                        public void clickedStatus(boolean clickedStatus) {
                            getActivity().finishAffinity();
                            System.exit(0);
                        }
                    });
                }
            }
        });
    }

    private boolean validatePassword(String stOldPsw, String stNewPsw, String stConformPsw) {
        tilOldPassword.setErrorEnabled(false);
        tilNewPassword.setErrorEnabled(false);
        tilConformPassword.setErrorEnabled(false);
        String psw = sharedPreferences.getString(UtilConstants.Password_Key, "");
        if (TextUtils.isEmpty(stOldPsw) || TextUtils.isEmpty(stNewPsw) || TextUtils.isEmpty(stConformPsw)) {
            if (TextUtils.isEmpty(stOldPsw)) {
                tilOldPassword.setErrorEnabled(true);
                tilOldPassword.setError(getString(R.string.validation_old_psw_reqired));
            }
            if (TextUtils.isEmpty(stNewPsw)) {
                tilNewPassword.setErrorEnabled(true);
                tilNewPassword.setError(getString(R.string.validation_new_psw_reqired));
            }
            if (TextUtils.isEmpty(stConformPsw)) {
                tilConformPassword.setErrorEnabled(true);
                tilConformPassword.setError(getString(R.string.validation_confm_psw_reqired));
            }
        } else if (checkIfSpaces(stOldPsw)) {
            tilOldPassword.setErrorEnabled(true);
            tilOldPassword.setError(getString(R.string.validation_old_psw_space));
        } else if (checkIfSpaces(stNewPsw)) {
            tilNewPassword.setErrorEnabled(true);
            tilNewPassword.setError(getString(R.string.validation_new_psw_space));
        } else if (checkIfSpaces(stConformPsw)) {
            tilConformPassword.setErrorEnabled(true);
            tilConformPassword.setError(getString(R.string.validation_confm_psw_space));
        } else if (!stOldPsw.equals(psw)) {
            tilOldPassword.setErrorEnabled(true);
            tilOldPassword.setError(getString(R.string.validation_old_psw_not_match));
        } else if (stNewPsw.length() < 8) {
            tilNewPassword.setErrorEnabled(true);
            tilNewPassword.setError(getString(R.string.validation_pwd_length_should_6_characters));
        } else if (!stNewPsw.equals(stConformPsw)) {
            tilNewPassword.setErrorEnabled(true);
            tilNewPassword.setError(getString(R.string.validation_new_pwd_confirm_pwd_same));
            tilConformPassword.setErrorEnabled(true);
            tilConformPassword.setError(getString(R.string.validation_new_pwd_confirm_pwd_same));
        }  else if (stOldPsw.equals(stConformPsw)) {
            tilNewPassword.setErrorEnabled(true);
            tilNewPassword.setError(getString(R.string.validation_old_psw_new_pass_should_not_same));
        } else if(!UtilConstants.isValidPassword(stNewPsw)) {
            tilNewPassword.setErrorEnabled(true);
            tilNewPassword.setError(getString(R.string.validation_pass_word_validation));
        } else if(!UtilConstants.isValidPassword(stConformPsw)) {
            tilConformPassword.setErrorEnabled(true);
            tilConformPassword.setError(getString(R.string.validation_pass_word_validation));
        }else {
            return true;
        }
        return false;
    }

    public boolean checkIfSpaces(String str) {
        boolean result = false;
        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(str);
        result = matcher.find();
        return result;
    }

    private void setPwdInDataVault(String password) {
        try {
            SharedPreferences.Editor spEditer = sharedPreferences.edit();//getString(UtilConstants.Password_Key, "");
            spEditer.putString(UtilConstants.Password_Key,password);
            spEditer.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.settings), true, false);
        menu.clear();
    }
}
