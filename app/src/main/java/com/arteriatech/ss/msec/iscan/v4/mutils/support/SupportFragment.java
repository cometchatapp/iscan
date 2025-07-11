package com.arteriatech.ss.msec.iscan.v4.mutils.support;


import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arteriatech.ss.msec.iscan.v4.BuildConfig;
import com.arteriatech.ss.msec.iscan.v4.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SupportFragment extends Fragment implements View.OnClickListener/*, SyncView*/ {


//    private AllSyncPresenterImpl allSyncPresenter;
    private ProgressDialog progressDialog = null;
    private TextView tvAppVersion, tvURL, tvPort, tvSuffix, tvEmailId, tvPhoneNo;
    private ConstraintLayout llSuffix;
    private ConstraintLayout llLog;
    private ConstraintLayout llPhoneNo;
    private ConstraintLayout llEmailId;

    public SupportFragment() {
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
        return inflater.inflate(R.layout.fragment_support, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        llLog = (ConstraintLayout) view.findViewById(R.id.llLog);
        llLog.setOnClickListener(this);
//        allSyncPresenter = new AllSyncPresenterImpl(getActivity(), this, Constants.ALL);

        tvAppVersion = (TextView) view.findViewById(R.id.tvAppVersion);
        tvURL = (TextView) view.findViewById(R.id.tvURL);
        tvPort = (TextView) view.findViewById(R.id.tvPort);
        tvSuffix = (TextView) view.findViewById(R.id.tvSuffix);
        llSuffix = (ConstraintLayout) view.findViewById(R.id.llSuffix);
        tvEmailId = (TextView) view.findViewById(R.id.tvEmailId);
        tvPhoneNo = (TextView) view.findViewById(R.id.tvPhoneNo);
        llPhoneNo = (ConstraintLayout) view.findViewById(R.id.llPhoneNo);
        llEmailId = (ConstraintLayout) view.findViewById(R.id.llEmailId);

        tvAppVersion.setText("1.0");
//        tvURL.setText(Configuration.server_Text);
//        tvPort.setText(Configuration.port_Text);
//        tvSuffix.setText(BuildConfig.VERSION_NAME);
        tvEmailId.setText(Html.fromHtml("<u>" + getString(R.string.register_support_email) + "</u>"));
        tvPhoneNo.setText(Html.fromHtml("<u>" + getString(R.string.register_support_phone) + "</u>"));

        llEmailId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (!TextUtils.isEmpty(getString(R.string.register_support_email))) {
                    try {
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setData(Uri.parse("mailto:"));
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.register_support_email)});
//                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, registrationModel.getEmailSubject());
//                        emailIntent.putExtra(Intent.EXTRA_TEXT, "message body");
                        startActivity(Intent.createChooser(emailIntent, "Email via..."));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/
            }
        });

        llPhoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (!TextUtils.isEmpty(getString(R.string.register_support_phone))) {
                    try {
                        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getString(R.string.register_support_phone)));
                        startActivity(dialIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/
            }
        });

    }

   /* @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.support_menu), true);
        menu.clear();
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.llLog:
//                startActivity(new Intent(getContext(), LogActivity.class));
//                break;
        }
    }

//    @Override
//    public void showProgressDialog() {
//        progressDialog = ConstantsUtils.showProgressDialog(getContext(), getString(R.string.app_loading));
//    }
//
//    @Override
//    public void hideProgressDialog() {
//        if (progressDialog != null) {
//            progressDialog.dismiss();
//        }
//    }
//
//    @Override
//    public void showMessage(String message) {
//        ConstantsUtils.displayShortToast(getContext(),message);
//    }
//
//    @Override
//    public void showCancelableDialog(String message, final SyncPresenter syncPresenter) {
//        progressDialog = ConstantsUtils.showProgressDialogWithWarningMsg(getContext(), message, new DialogCallBack() {
//            @Override
//            public void clickedStatus(boolean b) {
//                syncPresenter.isCancelProgressDialog(b);
//            }
//        });
//    }
//
//    @Override
//    public void onCancel() {
//        getActivity().getSupportFragmentManager().popBackStack();
//    }
//
//    @Override
//    public void displayCustomErrorMessage(int errorCode) {
//        Constants.displayMsgReqError(errorCode, getContext());
//    }
//
//    @Override
//    public void showFinalMessage(String successMessage, boolean status) {
//        UtilConstants.dialogBoxWithCallBack(getContext(), "", successMessage, getString(R.string.ok), "", false, null);
//    }
}
