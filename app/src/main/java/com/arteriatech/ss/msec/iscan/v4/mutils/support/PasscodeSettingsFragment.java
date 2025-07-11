package com.arteriatech.ss.msec.iscan.v4.mutils.support;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.PopupMenu;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.interfaces.FragmentCallbackInterface;
import com.arteriatech.ss.msec.iscan.v4.mutils.login.PasscodeLoginActivity;
import com.arteriatech.ss.msec.iscan.v4.mutils.registration.RegistrationModel;
import com.arteriatech.ss.msec.iscan.v4.mutils.registration.UtilRegistrationActivity;
import com.arteriatech.ss.msec.iscan.v4.mutils.security.AppLockApplication;
import com.arteriatech.ss.msec.iscan.v4.mutils.security.AppLockManager;
import com.arteriatech.ss.msec.iscan.v4.mutils.security.DefaultAppLock;
import com.arteriatech.ss.msec.iscan.v4.mutils.security.SecurityUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PasscodeSettingsFragment extends Fragment implements View.OnClickListener {
    SharedPreferences sharedPreferencess;
    private Switch enableSwitch;
    private LinearLayout llAccessPin;
    private int comingFrom = 0;
    private FragmentCallbackInterface fragmentCallBackInterface = null;
    private Bundle extraBundle = null;
    private boolean isFromRegistration = false;
    private LinearLayout llChangePassword;
    private LinearLayout llFingerPrint;
    private Switch fingerPrintSwitch;
    private LinearLayout llTimeLimit;
    private TextView tvTimeOut;
    private String Tag = PasscodeSettingsFragment.class.getSimpleName();
    private TextView tvTimeLimit;
    private boolean dontCallActivityResult = false;
    private RegistrationModel registrationModel=null;
    Bundle bundle=null;
    public PasscodeSettingsFragment() {
        // Required empty public constructor
    }

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragmentCallBackInterface = (FragmentCallbackInterface) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        bundle = getArguments();
        if (bundle != null) {
            comingFrom = bundle.getInt(UtilConstants.EXTRA_COME_FROM, 0);
            isFromRegistration = bundle.getBoolean(UtilRegistrationActivity.EXTRA_IS_FROM_REGISTRATION, false);
            extraBundle = bundle.getBundle(UtilRegistrationActivity.EXTRA_BUNDLE_REGISTRATION);
            registrationModel = (RegistrationModel) bundle.getSerializable(UtilConstants.RegIntentKey);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_passcode_settings, container, false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onDisplayView();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        enableSwitch = (Switch) view.findViewById(R.id.pinSwitch);
        llAccessPin = (LinearLayout) view.findViewById(R.id.ll_changepin);
        llFingerPrint = (LinearLayout) view.findViewById(R.id.llFingerPrint);
        tvTimeLimit = (TextView) view.findViewById(R.id.tvChangePassword);
        fingerPrintSwitch = (Switch) view.findViewById(R.id.fingerPrint);
        llTimeLimit = (LinearLayout) view.findViewById(R.id.llTimeLimit);
        tvTimeOut = (TextView) view.findViewById(R.id.tvTimeOut);
        llTimeLimit.setOnClickListener(this);
        sharedPreferencess = PreferenceManager.getDefaultSharedPreferences(getContext());
        onDisplayView();

    }

    private void onDisplayView() {
        if (!UtilConstants.restartApp(getActivity(),registrationModel.getShredPrefKey(),registrationModel.getRegisterActivity())) {
            checkEnableAccess();
            implementListener();
        }
    }

    private void checkFingerPrintAccess() {
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (UtilConstants.SECURITY_ON.equalsIgnoreCase(shared.getString(UtilConstants.ENABLE_FINGERPRINT_ACCESS, ""))) {
            fingerPrintSwitch.setChecked(true);
        } else {
            fingerPrintSwitch.setChecked(false);
        }
    }

    private void implementFingerPrint() {
        fingerPrintSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    SharedPreferences.Editor editor = sharedPreferencess.edit();
                    editor.putString(UtilConstants.ENABLE_FINGERPRINT_ACCESS, UtilConstants.SECURITY_ON);
                    editor.apply();
                } else {
                    SharedPreferences.Editor editors = sharedPreferencess.edit();
                    editors.putString(UtilConstants.ENABLE_FINGERPRINT_ACCESS, UtilConstants.SECURITY_OFF);
                    editors.apply();
                }
            }
        });
    }

    private void implementListener() {
        enableSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!dontCallActivityResult) {
                    if (isChecked) {
                        if (TextUtils.isEmpty(sharedPreferencess.getString(UtilConstants.QUICK_PIN, ""))) {
//                            llAccessPin.setVisibility(View.GONE);
                            redirectToChangePsw();
                            checkFingerPrint(false);
                        } else {
                            conformPasscodeActivity(UtilConstants.ACTIVITY_REQUEST_PASSCODE_ON);
                        }

                    } else {
                        if (!TextUtils.isEmpty(sharedPreferencess.getString(UtilConstants.QUICK_PIN, ""))) {
                            conformPasscodeActivity(UtilConstants.ACTIVITY_REQUEST_PASSCODE_OFF);
                        }
                    }
                } else {
                    dontCallActivityResult = false;
                }
            }
        });

        llAccessPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToChangePsw();
            }
        });
        getSeconds(getContext(), sharedPreferencess.getInt(UtilConstants.SET_TIME_OUT_POS, -1));
    }

    private void conformPasscodeActivity(int requestCode) {
        Intent intent = new Intent(getContext(), PasscodeLoginActivity.class);
        intent.putExtra(UtilConstants.RegIntentKey,registrationModel);
        intent.putExtra(DefaultAppLock.EXTRA_FROM_VERIFY_PASSCODE, true);
        startActivityForResult(intent, requestCode);
    }
    @Override
    public void onStart() {
        super.onStart();

        // Mark this fragment as the selected Fragment.
//        llAccessPin.setVisibility(View.VISIBLE);
    }

    private void redirectToChangePsw() {
       /* if (fragmentCallBackInterface != null) {
            fragmentCallBackInterface.fragmentCallBack(getString(R.string.passcode_title), null);
        }*/
        Fragment fragment = new ChangePassCodeFragment();
//        Bundle bundle = new Bundle();
        bundle.putInt(UtilConstants.EXTRA_COME_FROM, comingFrom);
        bundle.putBoolean(UtilRegistrationActivity.EXTRA_IS_FROM_REGISTRATION, isFromRegistration);
        if (extraBundle != null)
            bundle.putBundle(UtilRegistrationActivity.EXTRA_BUNDLE_REGISTRATION, extraBundle);
        fragment.setArguments(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.flContainer, fragment, fragment.getClass().getName());
//        fragmentTransaction.add(R.id.flPasscode, fragment, fragment.getClass().getName());
        PasscodeSettingsFragment myFrag = (PasscodeSettingsFragment) fragmentManager.findFragmentByTag(PasscodeSettingsFragment.class.getName());
        fragmentTransaction.hide(myFrag);
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();
    }

    private void checkEnableAccess() {
        SharedPreferences shared = PreferenceManager.getDefaultSharedPreferences(getContext());
        if ("yes".equalsIgnoreCase(shared.getString(UtilConstants.ENABLE_ACCESS, ""))) {
            enableSwitch.setChecked(true);
            setVisibility(true);
            checkFingerPrint(true);
        } else {
            enableSwitch.setChecked(false);
            checkFingerPrint(false);
            setVisibility(false);
        }
    }

    private void checkFingerPrint(boolean status) {
        if (status) {
            if (SecurityUtils.checkPermission(getActivity())) {
                llFingerPrint.setVisibility(View.VISIBLE);
                implementFingerPrint();
                checkFingerPrintAccess();
            } else {
                llFingerPrint.setVisibility(View.GONE);
            }
        } else {
            llFingerPrint.setVisibility(View.GONE);
        }
    }

    private void setVisibility(boolean isEnable) {
        if (isEnable) {
            llAccessPin.setVisibility(View.VISIBLE);
            llTimeLimit.setVisibility(View.VISIBLE);
            llAccessPin.animate().alpha(0.8f);
            llAccessPin.setEnabled(true);
        } else {
            llTimeLimit.setVisibility(View.GONE);
            llAccessPin.animate().alpha(0.2f);
            llAccessPin.setEnabled(false);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        /*if (fragmentCallBackInterface != null) {
            fragmentCallBackInterface.fragmentCallBack(getString(R.string.settings), null);
        }*/
    }

    @Override
    public void onClick(View v) {
        /*switch (v.getId()) {
            case R.id.llTimeLimit:
                showMenu(getContext(), llTimeLimit);
                break;
        }*/
        if (v.getId() == R.id.llTimeLimit) {
                showMenu(getContext(), llTimeLimit);
        }
    }

    private void showMenu(final Context context, View view) {
        PopupMenu menu =new PopupMenu(context, view, 0, 0, R.style.SettingsPopupMenu);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                int seconds = getSeconds(context, id);
                SharedPreferences.Editor editors = sharedPreferencess.edit();
                editors.putInt(UtilConstants.SET_TIME_OUT_LIMIT, seconds);
                editors.putInt(UtilConstants.SET_TIME_OUT_POS, id);
                AppLockManager.getInstance().getAppLock().setOneTimeTimeout(seconds);
                editors.apply();
                return true;
            }
        });
        menu.inflate(R.menu.menu_passcode_time_out);
        Menu menu1 = menu.getMenu();
        int id = sharedPreferencess.getInt(UtilConstants.SET_TIME_OUT_POS, R.id.item_immediately);
        if (id != -1) {
            MenuItem timeMenuItem = menu1.findItem(id);
            SpannableStringBuilder text = new SpannableStringBuilder();
            text.append(timeMenuItem.getTitle());
            text.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.colorAccent)),
                    0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            timeMenuItem.setTitle(text);
        }
        menu.show();
    }

    private int getSeconds(Context mContext, int id) {
        int seconds = AppLockApplication.DEFAULT_TIMEOUT_S;
        String title = mContext.getResources().getString(R.string.passcode_immediately);
       /* switch (id) {
            case R.id.item_immediately:
                seconds = 1;
                title = mContext.getResources().getString(R.string.passcode_immediately);
                break;
            case R.id.item_one_min:
                seconds = 1 * 60;
                title = mContext.getResources().getString(R.string.passcode_one_min);
                break;
            case R.id.item_two_min:
                seconds = 2 * 60;
                title = mContext.getResources().getString(R.string.passcode_two_min);
                break;
            case R.id.item_five_min:
                seconds = 5 * 60;
                title = mContext.getResources().getString(R.string.passcode_five_min);
                break;
            case R.id.item_fifteen_min:
                seconds = 15 * 60;
                title = mContext.getResources().getString(R.string.passcode_fifteen_min);
                break;
            case R.id.item_one_hr:
                seconds = 60 * 60;
                title = mContext.getResources().getString(R.string.passcode_one_hr);
                break;
        }*/
        if (id == R.id.item_immediately) {
            seconds = 2;
            title = mContext.getResources().getString(R.string.passcode_immediately);
        } else if (id == R.id.item_one_min) {
            seconds = 1 * 60;
            title = mContext.getResources().getString(R.string.passcode_one_min);
        } else if (id == R.id.item_two_min) {
            seconds = 2 * 60;
            title = mContext.getResources().getString(R.string.passcode_two_min);
        } else if (id == R.id.item_five_min) {
            seconds = 5 * 60;
            title = mContext.getResources().getString(R.string.passcode_five_min);
        } else if (id == R.id.item_fifteen_min) {
            seconds = 15 * 60;
            title = mContext.getResources().getString(R.string.passcode_fifteen_min);
        } else if (id == R.id.item_one_hr) {
            seconds = 60 * 60;
            title = mContext.getResources().getString(R.string.passcode_one_hr);
        }

        try {
            tvTimeOut.setText(title);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return seconds;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UtilConstants.ACTIVITY_REQUEST_PASSCODE_ON) {

            if (resultCode == UtilConstants.ACTIVITY_RESULT_PASSCODE) {
                boolean isCorrectPasscode = data.getBooleanExtra(PasscodeLoginActivity.EXTRA_PASSCODE_STATUS, false);
                if (isCorrectPasscode) {
                    SharedPreferences.Editor editor = sharedPreferencess.edit();
                    editor.putString(UtilConstants.ENABLE_ACCESS, "yes");
                    editor.apply();
                    checkFingerPrint(true);
                    String securePassword = sharedPreferencess.getString(UtilConstants.QUICK_PIN, "");
                    AppLockManager.getInstance().getAppLock().setPassword(securePassword);
//                    enableSwitch.setChecked(true);
                    setVisibility(true);
                } else {
                    dontCallActivityResult = true;
                    enableSwitch.setChecked(false);
                }
            } else {
                dontCallActivityResult = true;
                enableSwitch.setChecked(false);
            }
        } else if (requestCode == UtilConstants.ACTIVITY_REQUEST_PASSCODE_OFF) {
            if (resultCode == UtilConstants.ACTIVITY_RESULT_PASSCODE) {
                boolean isCorrectPasscode = data.getBooleanExtra(PasscodeLoginActivity.EXTRA_PASSCODE_STATUS, false);
                if (isCorrectPasscode) {
                    /*SharedPreferences.Editor editors = sharedPreferencess.edit();
                    editors.putString(Constants.ENABLE_ACCESS, "no");
                    editors.apply();*/
                    PasscodeLoginActivity.removePasscode(getContext());
                    AppLockManager.getInstance().getAppLock().setPassword(null);

                    checkFingerPrint(false);
                    setVisibility(false);
//                    enableSwitch.setChecked(false);
                } else {
                    dontCallActivityResult = true;
                    enableSwitch.setChecked(true);
                }
            } else {
                dontCallActivityResult = true;
                enableSwitch.setChecked(true);
            }
        }
    }
}
