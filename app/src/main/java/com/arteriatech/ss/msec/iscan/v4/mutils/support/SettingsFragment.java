package com.arteriatech.ss.msec.iscan.v4.mutils.support;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.interfaces.DialogCallBack;
import com.arteriatech.ss.msec.iscan.v4.mutils.interfaces.PasswordDialogCallbackInterface;
import com.arteriatech.ss.msec.iscan.v4.mutils.registration.RegistrationModel;
import com.arteriatech.ss.msec.iscan.v4.mutils.registration.UtilRegistrationActivity;
import com.arteriatech.ss.msec.iscan.v4.mutils.updatepassword.ChangePasswordFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {


    private int comingFrom = 0;
    //    private FragmentCallbackInterface fragmentCallBackInterface = null;
    private Bundle extraBundle = null;
    private LinearLayout llChangePassword, llExtendPassword;
    private RegistrationModel registrationModel = null;
    private ProgressDialog pdLoadDialog = null;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        extraBundle = getArguments();
        extraBundle = getArguments();
        if (extraBundle != null) {
            registrationModel = (RegistrationModel) extraBundle.getSerializable(UtilConstants.RegIntentKey);
        }
//        if (bundle != null) {
//            comingFrom = bundle.getInt(UtilConstants.EXTRA_COME_FROM, 0);
//            extraBundle = bundle.getBundle(UtilRegistrationActivity.EXTRA_BUNDLE_REGISTRATION);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        Fragment fragment = new PasscodeSettingsFragment();
        fragment.setArguments(extraBundle);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flPasscode, fragment, fragment.getClass().getName());
        fragmentTransaction.commit();
    }

    private void initView(View view) {
        llChangePassword = (LinearLayout) view.findViewById(R.id.llChangePassword);
        llChangePassword.setOnClickListener(this);

        llExtendPassword = (LinearLayout) view.findViewById(R.id.llExtendPassword);
        llExtendPassword.setOnClickListener(this);

        if (registrationModel != null) {
            if (registrationModel.isExtenndPwdReq()) {
                llExtendPassword.setVisibility(View.VISIBLE);
            } else {
                llExtendPassword.setVisibility(View.GONE);
            }
        } else {
            llExtendPassword.setVisibility(View.GONE);
        }

        if (registrationModel != null) {
            if (registrationModel.isUpdateAsPortalPwdReq()) {
                llChangePassword.setVisibility(View.VISIBLE);
            } else {
                llChangePassword.setVisibility(View.GONE);
            }
        } else {
            llChangePassword.setVisibility(View.GONE);
        }

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
       /* if (fragmentCallBackInterface != null) {
            fragmentCallBackInterface.fragmentCallBack(getString(R.string.settings), null);
        }*/
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.llChangePassword) {
            openFragment(new ChangePasswordFragment());
        } else if (v.getId() == R.id.llExtendPassword) {
            showConformationDialogExtendPassword();
        }
    }

    private void openFragment(Fragment mainMenuFragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mainMenuFragment.setArguments(extraBundle);
        fragmentTransaction.replace(R.id.flContainer, mainMenuFragment, mainMenuFragment.getClass().getName());
        fragmentTransaction.addToBackStack(mainMenuFragment.getClass().getName());
        fragmentTransaction.commit();
    }

    private void extendPassword(String password) {
        if (UtilConstants.isNetworkAvailable(getActivity())) {
            exportOfflineDB(getContext(), registrationModel, password);
        } else {
            UtilConstants.onNoNetwork(getActivity());
        }

    }

    private void showConformationDialogExtendPassword() {
        extraBundle = getArguments();
        if (extraBundle != null) {
            registrationModel = (RegistrationModel) extraBundle.getSerializable(UtilConstants.RegIntentKey);
        }
        if (registrationModel != null) {
            UtilConstants.showPasswordRemarksDialog(getActivity(), new PasswordDialogCallbackInterface() {
                @Override
                public void clickedStatus(boolean clickedStatus, String text) {
                    if (clickedStatus)
                        extendPassword(text);
                }
            }, getContext().getString(R.string.alert_plz_enter_password));
        }

    }

    private void exportOfflineDB(final Context mContext, final RegistrationModel regModel, String pUserPwd) {
        String pUserName = "";
       if (registrationModel!=null) {
           SharedPreferences sharedPreferences = mContext.getSharedPreferences(registrationModel.getShredPrefKey(), 0);
           pUserName = sharedPreferences.getString(UtilRegistrationActivity.KEY_username, "");
       }
        if (!TextUtils.isEmpty(pUserName) && !TextUtils.isEmpty(pUserPwd)) {
            extendPassword(mContext, regModel.getIDPURL(), regModel.getExternalTUserName(), regModel.getExternalTPWD(), pUserName, pUserPwd);
        } else {
            UtilConstants.displayShortToast(mContext, "Unable to get Username and Password");
        }

    }

    private void extendPassword(final Context mContext, final String domineUrl, final String tUserName, final String tPsw, final String pUserID, final String password) {
        pdLoadDialog = new ProgressDialog(mContext, R.style.UtilsDialogTheme);
        pdLoadDialog.setMessage(getString(R.string.extend_pwd_please_wait));
        pdLoadDialog.setCancelable(false);
        pdLoadDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = domineUrl + "/service/scim/Users?filter=userName%20eq%20'" + pUserID + "'";
                String puserID = pUserID;

                try {
                    String jsonValue = UtilConstants.getPuserIdUtilsReponse(new URL(url), tUserName, tPsw);
                    if (!TextUtils.isEmpty(jsonValue)) {
                        JSONObject jsonObject = new JSONObject(jsonValue);
                        JSONArray jsonArray = jsonObject.optJSONArray("Resources");
                        if (jsonArray != null && jsonArray.length() > 0) {
                            puserID = jsonArray.getJSONObject(0).getString("id");
                        }

                        if (!TextUtils.isEmpty(puserID)) {
                            String url1 = domineUrl + "/service/scim/Users/" + puserID;
                            String validatePuser = UtilConstants.getPuserIdUtilsReponse(new URL(url1), tUserName, tPsw);
                            if (!TextUtils.isEmpty(validatePuser)) {
                                JSONObject userObject = new JSONObject(validatePuser);
                                String userStatus = userObject.optString("passwordStatus");
                                JSONObject metaObject = userObject.getJSONObject("meta");
                                JSONArray schemasArray = userObject.optJSONArray("schemas");
                                JSONObject bodyObject = new JSONObject();
                                bodyObject.put("id", puserID);
                                bodyObject.put("password", password);
                                bodyObject.put("passwordStatus", "enabled");
                                bodyObject.put("meta", metaObject);
                                bodyObject.put("schemas", schemasArray);
                                String changePassword = UtilConstants.getPswResetUtilsReponse(new URL(url1), tUserName, tPsw, bodyObject.toString());
                                if (!TextUtils.isEmpty(changePassword)) {
                                    try {
                                        JSONObject userPObject = new JSONObject(changePassword);
                                        String userPStatus = userPObject.optString("passwordStatus");
                                        if (!TextUtils.isEmpty(userPStatus) && userPStatus.equalsIgnoreCase("enabled")) {
                                            setPwdInDataVault(mContext, password);
                                            displayErrorMessage(mContext.getString(R.string.extend_pwd_finish_success), mContext);
                                        } else {
                                            displayErrorMessage(mContext.getString(R.string.extend_pwd_error_occured) + " " + userPStatus, mContext);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        displayErrorMessage(changePassword + " Please use different password", mContext);
                                    }
                                } else {
                                    displayErrorMessage(mContext.getString(R.string.extend_pwd_error_occured), mContext);
                                }
                            } else {
                                displayErrorMessage(mContext.getString(R.string.extend_pwd_error_occured), mContext);
                            }
                        } else {
                            displayErrorMessage(mContext.getString(R.string.extend_pwd_error_occured), mContext);
                        }
                    } else {
                        displayErrorMessage(mContext.getString(R.string.no_network_conn), mContext);
                    }
                } catch (IOException var16) {
                    var16.printStackTrace();
                    displayErrorMessage(var16.getMessage(), mContext);
                } catch (JSONException var17) {
                    var17.printStackTrace();
                    displayErrorMessage(var17.getMessage(), mContext);
                } catch (Exception var17) {
                    var17.printStackTrace();
                    displayErrorMessage(var17.getMessage(), mContext);
                }
            }
        }).start();
    }

    private void displayErrorMessage(final String strMsg, final Context mContext) {
        try {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    try {
                        pdLoadDialog.cancel();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (strMsg.contains("successfully")) {
                        exitAPP();
                    }
                    UtilConstants.displayShortToast(mContext, strMsg);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exitAPP() {
        UtilConstants.dialogBoxWithCallBack(getContext(), "", getString(R.string.extend_pwd_updated_succefully), getString(R.string.ok), "", false, new DialogCallBack() {
            @Override
            public void clickedStatus(boolean clickedStatus) {
                getActivity().finishAffinity();
                System.exit(0);
            }
        });
    }

    private void setPwdInDataVault(Context mContext, String password) {
        try {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(registrationModel.getShredPrefKey(), 0);
            SharedPreferences.Editor spEditer = sharedPreferences.edit();
            spEditer.putString(UtilConstants.Password_Key, password);
            spEditer.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
