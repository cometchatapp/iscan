package com.arteriatech.ss.msec.iscan.v4.mutils.support;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.registration.RegistrationModel;
import com.arteriatech.ss.msec.iscan.v4.mutils.registration.UtilRegistrationActivity;
import com.arteriatech.ss.msec.iscan.v4.mutils.security.AppLockManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePassCodeFragment extends Fragment {
    EditText ed_Pinone, ed_Pintwo, ed_Pinthree, ed_Pinfour, ed_Confrimone, ed_Confrimtwo, ed_Confrimthree, ed_Confrimfour, ed_Oldone, ed_Oldtwo, ed_Oldthree, ed_Oldfour;
    TextView tv_Oldpin, tv_enterPin, tv_confirmPin;
    LinearLayout ll_Oldlay, ll_Pinlay, ll_Confirmlay, ll_Pinconfirm;
    Button btn_Confrim, bt_Clear;
    private int comingFrom = 0;
    private boolean isFromRegistration = false;
    private Bundle extraBundle = null;
    private TextView tvTitle;
    private RelativeLayout rlView;
    private Bundle bundleExtra = null;
    private RegistrationModel registrationModel=null;
    public ChangePassCodeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        bundleExtra = getArguments();
        if (bundleExtra != null) {
            comingFrom = bundleExtra.getInt(UtilConstants.EXTRA_COME_FROM, 0);
            isFromRegistration = bundleExtra.getBoolean(UtilRegistrationActivity.EXTRA_IS_FROM_REGISTRATION, false);
            extraBundle = bundleExtra.getBundle(UtilRegistrationActivity.EXTRA_BUNDLE_REGISTRATION);
            registrationModel = (RegistrationModel) bundleExtra.getSerializable(UtilConstants.RegIntentKey);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_access_pin, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_Oldpin = (TextView) view.findViewById(R.id.tv_oldpin);
        tv_enterPin = (TextView) view.findViewById(R.id.tv_enterpin);
        tv_confirmPin = (TextView) view.findViewById(R.id.tv_confirm);
        ed_Oldone = (EditText) view.findViewById(R.id.ed_oldone);
        ed_Oldtwo = (EditText) view.findViewById(R.id.ed_oldtwo);
        ed_Oldthree = (EditText) view.findViewById(R.id.ed_oldthree);
        ed_Oldfour = (EditText) view.findViewById(R.id.ed_oldfour);
        ll_Oldlay = (LinearLayout) view.findViewById(R.id.ll_oldpin);
        ll_Pinlay = (LinearLayout) view.findViewById(R.id.ll_pinlay);
        ll_Confirmlay = (LinearLayout) view.findViewById(R.id.ll_confirmlay);
        ed_Pinone = (EditText) view.findViewById(R.id.ed_pinone);
        ed_Pintwo = (EditText) view.findViewById(R.id.ed_pintwo);
        ed_Pinthree = (EditText) view.findViewById(R.id.ed_pinthree);
        ed_Pinfour = (EditText) view.findViewById(R.id.ed_pinfour);
        ed_Confrimone = (EditText) view.findViewById(R.id.ed_confrim_one);
        ed_Confrimtwo = (EditText) view.findViewById(R.id.ed_confrim_two);
        ed_Confrimthree = (EditText) view.findViewById(R.id.ed_confrim_three);
        ed_Confrimfour = (EditText) view.findViewById(R.id.ed_confrim_four);
        ll_Pinconfirm = (LinearLayout) view.findViewById(R.id.ll_pinconfirm);
        btn_Confrim = (Button) view.findViewById(R.id.btn_confirm);
        bt_Clear = (Button) view.findViewById(R.id.bt_clear);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        rlView = (RelativeLayout) view.findViewById(R.id.rlView);

        tvTitle.setText(getString(R.string.passcode_title));
        if (!UtilConstants.restartApp(getActivity(),registrationModel.getShredPrefKey(),registrationModel.getRegisterActivity())) {
            checkAccessPin(getContext());
            textChangeListener(getContext());

        }
    }

    private void textChangeListener(final Context mContext) {
        ed_Oldone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                oldPinValidation(mContext);

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ed_Oldone.getText().toString().length() == 1) {
                    ed_Oldtwo.requestFocus();

                }
            }
        });
        ed_Oldtwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                oldPinValidation(mContext);

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ed_Oldtwo.getText().toString().length() == 1) {
                    ed_Oldthree.requestFocus();

                }
            }
        });
        ed_Oldthree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                oldPinValidation(mContext);

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ed_Oldthree.getText().toString().length() == 1) {
                    ed_Oldfour.requestFocus();

                }
            }
        });

        ed_Oldfour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                oldPinValidation(mContext);

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ed_Oldfour.getText().toString().length() == 1) {
                    ed_Oldfour.clearFocus();

                }

            }
        });

        ed_Pinone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ed_Pinone.getText().toString().length() == 1) {
                    ed_Pintwo.requestFocus();

                }
            }
        });


        ed_Pintwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ed_Pintwo.getText().toString().length() == 1) {

                    ed_Pinthree.requestFocus();
                }
            }
        });

        ed_Pinthree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ed_Pinthree.getText().toString().length() == 1) {

                    ed_Pinfour.requestFocus();
                }
            }
        });

        ed_Pinfour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ed_Pinfour.getText().toString().length() == 1) {

                    ed_Confrimone.requestFocus();
                }
            }
        });

        ed_Confrimone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ed_Confrimone.getText().toString().length() == 1) {

                    ed_Confrimtwo.requestFocus();
                }
            }
        });

        ed_Confrimtwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ed_Confrimtwo.getText().toString().length() == 1) {

                    ed_Confrimthree.requestFocus();
                }
            }
        });

        ed_Confrimthree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ed_Confrimthree.getText().toString().length() == 1) {

                    ed_Confrimfour.requestFocus();
                }
            }
        });

        ed_Confrimfour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (ed_Confrimfour.getText().toString().length() == 1) {
                    ed_Confrimfour.clearFocus();
                }
            }
        });
        bt_Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ed_Pinone.setText("");
                ed_Pintwo.setText("");
                ed_Pinthree.setText("");
                ed_Pinfour.setText("");

                ed_Confrimone.setText("");
                ed_Confrimtwo.setText("");
                ed_Confrimthree.setText("");
                ed_Confrimfour.setText("");

            }
        });

        btn_Confrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pinOne = ed_Pinone.getText().toString();
                String pinTwo = ed_Pintwo.getText().toString();
                String pinThree = ed_Pinthree.getText().toString();
                String pinFour = ed_Pinfour.getText().toString();

                String passwordOne = pinOne + pinTwo + pinThree + pinFour;

                String confrimOne = ed_Confrimone.getText().toString();
                String confirmTwo = ed_Confrimtwo.getText().toString();
                String confrimThree = ed_Confrimthree.getText().toString();
                String confirmFour = ed_Confrimfour.getText().toString();
                String confirmPassword = confrimOne + confirmTwo + confrimThree + confirmFour;


                if (TextUtils.isEmpty(pinOne) || TextUtils.isEmpty(pinTwo) || TextUtils.isEmpty(pinThree) || TextUtils.isEmpty(pinFour)) {
                    UtilConstants.authenticationFailed(getActivity(), rlView);
                    UtilConstants.displayShortToast(mContext, getString(R.string.plz_enter_passcode));
                } else if (TextUtils.isEmpty(confrimOne) || TextUtils.isEmpty(confirmTwo) || TextUtils.isEmpty(confrimThree) || TextUtils.isEmpty(confirmFour)) {
                    UtilConstants.authenticationFailed(getActivity(), rlView);
                    UtilConstants.displayShortToast(mContext, getString(R.string.plz_enter_confirm_passcode));
                } else {
                    SharedPreferences sharedPreferencess = PreferenceManager.getDefaultSharedPreferences(mContext);
                    String mStrOldPin = sharedPreferencess.getString(UtilConstants.QUICK_PIN, "");
                    if ("yes".equalsIgnoreCase(sharedPreferencess.getString(UtilConstants.QUICK_PIN_ACCESS, ""))) {
                        if (!mStrOldPin.equalsIgnoreCase("")) {
                            if (mStrOldPin.equalsIgnoreCase(confirmPassword)) {
                                ed_Pinone.setText("");
                                ed_Pintwo.setText("");
                                ed_Pinthree.setText("");
                                ed_Pinfour.setText("");

                                ed_Confrimone.setText("");
                                ed_Confrimtwo.setText("");
                                ed_Confrimthree.setText("");
                                ed_Confrimfour.setText("");
                                UtilConstants.displayShortToast(mContext, getString(R.string.enter_diffrent_pwd));
                                UtilConstants.authenticationFailed(getActivity(), rlView);
                            } else {
                                savePin(mContext, passwordOne, confirmPassword);
                            }
                        } else {
                            savePin(mContext, passwordOne, confirmPassword);
                        }

                    } else {
                        savePin(mContext, passwordOne, confirmPassword);
                    }
                }


            }
        });

    }

    private void oldPinValidation(Context mContext) {
        String oldPinOne = ed_Oldone.getText().toString();
        String oldPinTwo = ed_Oldtwo.getText().toString();
        String oldPinThree = ed_Oldthree.getText().toString();
        String oldPinFour = ed_Oldfour.getText().toString();
        if (!TextUtils.isEmpty(oldPinOne) && !TextUtils.isEmpty(oldPinTwo) && !TextUtils.isEmpty(oldPinThree) && !TextUtils.isEmpty(oldPinFour)) {
            SharedPreferences pinPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            String oldPin = pinPreferences.getString(UtilConstants.QUICK_PIN, "");
            String enteredPin = oldPinOne + oldPinTwo + oldPinThree + oldPinFour;
            if (oldPin.equalsIgnoreCase(enteredPin)) {

                tv_Oldpin.setVisibility(View.GONE);
                ll_Oldlay.setVisibility(View.GONE);
                ll_Pinconfirm.setVisibility(View.VISIBLE);
                tv_enterPin.setVisibility(View.VISIBLE);
                tv_confirmPin.setVisibility(View.VISIBLE);
                ll_Pinlay.setVisibility(View.VISIBLE);
                ll_Confirmlay.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(mContext, getString(R.string.you_have_enterd_worng_pin), Toast.LENGTH_LONG).show();
                tv_Oldpin.setVisibility(View.VISIBLE);
                ll_Oldlay.setVisibility(View.VISIBLE);
                tv_enterPin.setVisibility(View.GONE);
                tv_confirmPin.setVisibility(View.GONE);
                ll_Pinlay.setVisibility(View.GONE);
                ll_Confirmlay.setVisibility(View.GONE);
                ll_Pinconfirm.setVisibility(View.GONE);
                UtilConstants.authenticationFailed(getActivity(), rlView);
                clearOldPin();

            }
        }
    }

    private void clearOldPin() {
        ed_Oldone.setText("");
        ed_Oldtwo.setText("");
        ed_Oldthree.setText("");
        ed_Oldfour.setText("");
        ed_Oldone.requestFocus();
    }

    private void savePin(Context mContext, String pswOne, String pswTwo) {

        if (pswOne.equalsIgnoreCase(pswTwo)) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(UtilConstants.QUICK_PIN, pswOne);
            editor.putString(UtilConstants.QUICK_PIN_ACCESS, "yes");
            editor.putString(UtilConstants.ENABLE_ACCESS, "yes");
            editor.apply();
           /* if (comingFrom == 1) {
                Intent intent = new Intent(mContext, MainActivity.class);
                intent.putExtra(UtilRegistrationActivity.EXTRA_IS_FROM_REGISTRATION, isFromRegistration);
                if (extraBundle != null) {
                    intent.putExtra(UtilRegistrationActivity.EXTRA_BUNDLE_REGISTRATION, extraBundle);
                }
                startActivity(intent);
                getActivity().finish();
            }else {*/
            AppLockManager.getInstance().getAppLock().setPassword(pswOne);
            UtilConstants.displayShortToast(mContext,getString(R.string.passcode_created_successfully));
            getActivity().getSupportFragmentManager().popBackStack();
//            }
//              Toast.makeText(mContext, "", Toast.LENGTH_LONG).show();
//            finish();
        } else {
            ed_Pinone.setText("");
            ed_Pintwo.setText("");
            ed_Pinthree.setText("");
            ed_Pinfour.setText("");

            ed_Confrimone.setText("");
            ed_Confrimtwo.setText("");
            ed_Confrimthree.setText("");
            ed_Confrimfour.setText("");

            UtilConstants.authenticationFailed(getActivity(), rlView);
            UtilConstants.displayShortToast(getContext(), getString(R.string.pscode_confirm_passcode_not_match));
        }
    }

    private void checkAccessPin(Context mContext) {

        SharedPreferences pinPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String oldPin = pinPreferences.getString(UtilConstants.QUICK_PIN_ACCESS, "");

        if (!TextUtils.isEmpty(oldPin)) {
            tv_Oldpin.setVisibility(View.VISIBLE);
            ll_Oldlay.setVisibility(View.VISIBLE);
            ll_Pinconfirm.setVisibility(View.GONE);
            tv_enterPin.setVisibility(View.GONE);
            tv_confirmPin.setVisibility(View.GONE);
            ll_Pinlay.setVisibility(View.GONE);
            ll_Confirmlay.setVisibility(View.GONE);


        } else {
            tv_Oldpin.setVisibility(View.GONE);
            ll_Oldlay.setVisibility(View.GONE);
            ll_Pinconfirm.setVisibility(View.VISIBLE);
            tv_enterPin.setVisibility(View.VISIBLE);
            tv_confirmPin.setVisibility(View.VISIBLE);
            ll_Pinlay.setVisibility(View.VISIBLE);
            ll_Confirmlay.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
}
