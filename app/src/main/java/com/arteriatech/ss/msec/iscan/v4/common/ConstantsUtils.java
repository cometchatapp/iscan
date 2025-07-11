package com.arteriatech.ss.msec.iscan.v4.common;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.autosync.AutoSyncDataAlarmReceiver;
import com.arteriatech.ss.msec.iscan.v4.autosync.schedule.ScheduleSyncDataAlarmReceiver;
import com.arteriatech.ss.msec.iscan.v4.interfaces.AsyncTaskCallBack;
import com.arteriatech.ss.msec.iscan.v4.interfaces.CustomDialogCallBackWithCode;
import com.arteriatech.ss.msec.iscan.v4.log.ErrorCode;
import com.arteriatech.ss.msec.iscan.v4.mbo.ConfigTypsetTypeValuesBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.OutstandingBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.RemarkReasonBean;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.OfflineODataStoreException;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilOfflineManager;
import com.arteriatech.ss.msec.iscan.v4.mutils.datavault.UtilDataVault;
import com.arteriatech.ss.msec.iscan.v4.mutils.interfaces.DialogCallBack;
import com.arteriatech.ss.msec.iscan.v4.mutils.registration.RegistrationModel;
import com.arteriatech.ss.msec.iscan.v4.mutils.store.OnlineODataInterface;
import com.arteriatech.ss.msec.iscan.v4.mutils.support.SecuritySettingActivity;
import com.arteriatech.ss.msec.iscan.v4.registration.Configuration;
import com.arteriatech.ss.msec.iscan.v4.store.OfflineManager;
import com.arteriatech.ss.msec.iscan.v4.store.OnlineManager;
import com.arteriatech.ss.msec.iscan.v4.sync.UpdatePendingRequest;
import com.arteriatech.ss.msec.iscan.v4.ui.FlowLayout;
import com.arteriatech.ss.msec.iscan.v4.ui.MaterialDesignSpinner;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.sap.smp.client.httpc.events.IReceiveEvent;
import com.sap.smp.client.odata.ODataDuration;
import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.impl.ODataDurationDefaultImpl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by e10769 on 12-Apr-18.
 */

public class ConstantsUtils {

    public static final int MULTI_CAMERA_REQUEST_CODE = 90909;
    public static final int SWIPE_REFRESH_DISTANCE = 300;
    public static final int SWIPE_REFRESH_DISABLE = 999999;
    public static final int DATE_SETTINGS_REQUEST_CODE = 998;
    public static final int CAMERA_REQUEST_CODE = 9981;
    public static final int GALLERY_REQUEST_CODE = 9982;
    public static final int ACTIVITY_RESULT_FILTER = 850;
    public static final int ACTIVITY_RESULT_MATERIAL = 750;
    public static final int ADD_MATERIAL = 30;
    public static final int SO_CREATE_SINGLE_MATERIAL = 31;
    public static final int SO_EDIT_SINGLE_MATERIAL = 32;
    public static final int SO_SINGLE_MATERIAL = 33;
    public static final int SO_VIEW_SELECTED_MATERIAL = 34;
    public static final int SO_MULTIPLE_MATERIAL = 4;
    public static final int SO_CREATE_ACTIVITY = 1;
    public static final int SO_CREATE_CC_ACTIVITY = 2;
    public static final int SO_EDIT_ACTIVITY = 3;
    public static final int SO_APPROVAL_EDIT_ACTIVITY = 36;

    /*session type*/
    public static final int NO_SESSION = 0;// session passing only  app header
    public static final int SESSION_HEADER = 1;// session passing only  app header
    public static final int SESSION_QRY = 2;// session passing only qry
    public static final int SESSION_QRY_HEADER = 3;// session passing both app header and qry

    public static final String Brand = "Brand";
    public static final String Banner = "Banner";
    public static final String MaterialNo = "MaterialNo";
    public static final String SKUGroup = "SKUGroup";
    public static final String RegistrationTypeID = "RegistrationTypeID";
    public static final String RegistrationTypeDesc = "RegistrationTypeDesc";
    public static final String OnSaleOfCatDesc = "OnSaleOfCatDesc";
    public static final String OnSaleOfCatID = "OnSaleOfCatID";
    public static final String FreeMatCritria = "FreeMatCritria";
    public static final String BannerDesc = "BannerDesc";
    public static final String ProductCatDesc = "ProductCatDesc";
    public static final String MaterialGroupDesc = "MaterialGroupDesc";
    public static final String ItemMin = "ItemMin";
    public static final String ClaimGUID = "ClaimGUID";
    public static final String ClaimDate = "ClaimDate";
    public static final String ClaimItemGUID = "ClaimItemGUID";
    public static final String RegistrationType = "RegistrationType";
    public static final String ClaimDocumentID = "ClaimDocumentID";
    public static final String RegistrationDate = "RegistrationDate";
    public static final String EnrollmentDate = "EnrollmentDate";
    public static final String SchemeNo = "SchemeNo";
    public static final String ProductCategoryID = "ProductCategoryID";
    public static final String ProductCatID = "ProductCatID";
    public static final String HierarchicalRefGUID = "HierarchicalRefGUID";
    public static final int ITEM_MAX_LENGTH = 6;
    public static final String DISC_AMOUNT = "Disc Amount";
    public static final String AlternativeUOM2 = "AlternativeUOM2";
    public static final String AlternativeUOM2Num = "AlternativeUOM2Num";
    public static final String AlternativeUOM2Den = "AlternativeUOM2Den";
    public static final String AlternativeUOM1 = "AlternativeUOM1";
    public static final String AlternativeUOM1Num = "AlternativeUOM1Num";
    public static final String AlternativeUOM1Den = "AlternativeUOM1Den";

    public static final String ROLLID_TSO_04 = "000004";
    public static final String ROLLID_TSI_05 = "000005";
    public static final String ROLLID_DSR_06 = "000006";
    public static final String SKUGroupDesc = "SKUGroupDesc";
    public static final String ProductCategoryDesc = "ProductCategoryDesc";
    public static String APPROVALERRORMSG = "";

    public static final String MAXCLMDOC = "MAXCLMDOC";
    public static final String MAXREGDOC = "MAXREGDOC";
    public static final String ZDMS_SCCLM = "ZDMS_SCCLM";
    public static final String EXTRA_FROM="comingFrom";
    public static final String DISC_PERCENTAGE="Disc %";
    public static final String FREE_QTY="Free Qty";
    public static final String TargetBasedID="TargetBasedID";
    public static final String Training="Training";
    public static final String Meeting="Meeting";
    private static final String MC="MC";
    public static final String B="B";
    public static final String C="C";
    private static final String DAYEND="DAYEND";
    public static String ApprovalStatusID="ApprovalStatusID";
    public static final String EXTRA_ARRAY_LIST = "arrayList";





    /*actionbar center image*/
    public static void initActionBarView(AppCompatActivity mActivity, Toolbar toolbar, boolean homeUpEnabled, String title, int appIcon) {
        com.arteriatech.ss.msec.iscan.v4.mutils.actionbar.ActionBarView.initActionBarView(mActivity, toolbar, homeUpEnabled, title, appIcon, 0);
    }

    public static boolean isAutomaticTimeZone(Context mContext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return android.provider.Settings.Global.getInt(mContext.getContentResolver(), android.provider.Settings.Global.AUTO_TIME, 0) == 1;
//                return Settings.Global.getInt(mContext.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return android.provider.Settings.System.getInt(mContext.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
        }
//       return true;
    }

    public static void showAutoDateSetDialog(final Activity mContext) {
        UtilConstants.dialogBoxWithCallBack(mContext, "", mContext.getString(R.string.autodate_change_msg), mContext.getString(R.string.autodate_change_btn), "", false, new com.arteriatech.ss.msec.iscan.v4.mutils.interfaces.DialogCallBack() {
            @Override
            public void clickedStatus(boolean b) {
                mContext.startActivityForResult(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS), DATE_SETTINGS_REQUEST_CODE);
            }
        });
    }

    public static void printErrorLog(String message) {
        Log.d("mSecSales", "ErrorLog : " + message);
        LogManager.writeLogError(Constants.error_txt + message);
    }

    static String selectedReasonDesc = "";
    static String selectedReasonCode = "";

    public static void showVisitRemarksDialog(final Activity activity, final CustomDialogCallBackWithCode customDialogCallBack,
                                              String title, final ArrayList<RemarkReasonBean> alReason) {
        selectedReasonDesc = "";
        selectedReasonCode = "";

        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.visit_remarks_dialog);

        final MaterialDesignSpinner spVisitEndReason = dialog.findViewById(R.id.spReason);
        final EditText etRemarks = dialog.findViewById(R.id.etRemarks);
        final TextInputLayout tilRemarks = dialog.findViewById(R.id.tilRemarks);

        ArrayAdapter<RemarkReasonBean> reasonadapter = new ArrayAdapter<RemarkReasonBean>(activity, R.layout.custom_textview,
                R.id.tvItemValue, alReason) {
            @Override
            public View getDropDownView(final int position, final View convertView, final ViewGroup parent) {
                final View v = super.getDropDownView(position, convertView, parent);
                ConstantsUtils.selectedView(v, spVisitEndReason, position, getContext());
                return v;
            }
        };

        reasonadapter.setDropDownViewResource(R.layout.spinnerinside);
        spVisitEndReason.setAdapter(reasonadapter);
        spVisitEndReason.showFloatingLabel();
        spVisitEndReason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedReasonDesc = alReason.get(position).getReasonDesc();
                selectedReasonCode = alReason.get(position).getReasonCode();
                if (selectedReasonCode.equalsIgnoreCase(Constants.str_06)) {
                    tilRemarks.setHint(activity.getString(R.string.lbl_remarks));
                }else {
                    tilRemarks.setHint(activity.getString(R.string.lbl_ret_remarks));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        Button okButton = dialog.findViewById(R.id.btYes);
        Button cancleButton = dialog.findViewById(R.id.btNo);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mStrVisitEndRemarks = "";
                if (selectedReasonCode.equalsIgnoreCase("00")) {
                    if (spVisitEndReason.getVisibility() == View.VISIBLE)
                        spVisitEndReason.setError("Select Reason");
                } else if (selectedReasonCode.equalsIgnoreCase(Constants.str_06)) {
                    try {
                        mStrVisitEndRemarks = etRemarks.getText().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                        mStrVisitEndRemarks = "";
                    }
                    if (mStrVisitEndRemarks.equalsIgnoreCase("") || mStrVisitEndRemarks.trim().length()==0) {
                        tilRemarks.setErrorEnabled(true);
                        tilRemarks.setError(activity.getString(R.string.visit_error_remarks));
                    } else {
                        if (mStrVisitEndRemarks.equalsIgnoreCase("")) {
                            mStrVisitEndRemarks = selectedReasonDesc;
                        } else {
                            mStrVisitEndRemarks = selectedReasonDesc + " " + mStrVisitEndRemarks;
                        }

                        dialog.dismiss();
                        if (customDialogCallBack != null) {
                            customDialogCallBack.cancelDialogCallBack(true, "", mStrVisitEndRemarks, selectedReasonCode, selectedReasonDesc);
                        }
                    }
                } else {
                    try {
                        mStrVisitEndRemarks = etRemarks.getText().toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                        mStrVisitEndRemarks = "";
                    }
                    if (mStrVisitEndRemarks.equalsIgnoreCase("")) {
                        mStrVisitEndRemarks = selectedReasonDesc;
                    } else {
                        mStrVisitEndRemarks = selectedReasonDesc + " " + mStrVisitEndRemarks;
                    }

                    if (mStrVisitEndRemarks.equalsIgnoreCase("")) {
                        tilRemarks.setErrorEnabled(true);
                        tilRemarks.setError(activity.getString(R.string.visit_error_remarks));
                    } else {
                        dialog.dismiss();
                        if (customDialogCallBack != null) {
                            customDialogCallBack.cancelDialogCallBack(true, "", mStrVisitEndRemarks, selectedReasonCode, selectedReasonDesc);
                        }
                    }
                }
                /*if (etRemarks.getText().toString().trim().length() < 1) {
                    tilRemarks.setErrorEnabled(true);
                    tilRemarks.setError(activity.getString(R.string.visit_error_remarks));
                } else {
                    dialog.dismiss();
                    if (customDialogCallBack != null) {
                        customDialogCallBack.cancelDialogCallBack(true, "", etRemarks.getText().toString(),selectedReasonCode,selectedReasonDesc);
                    }
                }*/
            }
        });
        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (customDialogCallBack != null) {
                    customDialogCallBack.cancelDialogCallBack(false, "", "", "", "");
                }
            }
        });
        etRemarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tilRemarks.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dialog.show();

    }

    public static int dpToPx(int dp, Context mContext) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
//        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics));
    }

    public static String getCurrentDateFormat(Context context) {
        String dateFormatted = "";
        try {
            Calendar calendar = Calendar.getInstance();
            if (calendar != null) {
                String patten = getConfigTypeDateFormat(context);
                if (TextUtils.isEmpty(patten)) {
                    patten = UtilConstants.getDeviceDateFormat(context);
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(patten);
                TimeZone timeZone = TimeZone.getTimeZone("Asia/Calcutta");
                simpleDateFormat.setTimeZone(timeZone);
                dateFormatted = simpleDateFormat.format(calendar.getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateFormatted;
    }

    public static void displayShortToast(Context mContext, String message) {
        try {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayLongToast(Context mContext, String message) {
        try {
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayErrorDialog(Context mContext, String message) {
        if (!TextUtils.isEmpty(message)) {
            UtilConstants.dialogBoxWithCallBack(mContext, "", message, mContext.getString(R.string.ok), "", false, null);
        } else {
            UtilConstants.dialogBoxWithCallBack(mContext, "", mContext.getString(R.string.msg_no_network), mContext.getString(R.string.ok), "", false, null);
        }
    }

    public static void displayFilter(String[] strArray, ViewGroup llFlowLayout, Context mContext) {
        try {
            llFlowLayout.removeAllViews();
            if (strArray != null) {
                FlowLayout.LayoutParams filterParams = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                filterParams.setMargins(4, 4, 4, 4);
                for (String typeText : strArray) {
                    if (!TextUtils.isEmpty(typeText)) {
                        TextView textView = new TextView(mContext);
                        textView.setLayoutParams(filterParams);
                        textView.setPadding(4, 4, 4, 4);
                        textView.setTextColor(ContextCompat.getColor(mContext, R.color.secondaryTextColor));
                        textView.setText(typeText);
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mContext.getResources().getDimension(R.dimen.util_small_text_sp));
                        textView.setBackgroundResource(R.drawable.chip_shape);
                        llFlowLayout.addView(textView);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ProgressDialog showProgressDialog(AppCompatActivity mContext) {
        ProgressDialog pdLoadDialog = null;
        try {
            pdLoadDialog = new ProgressDialog(mContext);
            pdLoadDialog.setMessage(mContext.getString(R.string.app_loading));
            pdLoadDialog.setTitle(mContext.getString(R.string.app_loading));
            pdLoadDialog.setCancelable(false);
            pdLoadDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pdLoadDialog;
    }
    public static ProgressDialog progressDialog(Context mContext) {
        ProgressDialog pdLoadDialog = null;
        try {
            pdLoadDialog = new ProgressDialog(mContext, R.style.ProgressDialogTheme);
            pdLoadDialog.setMessage(mContext.getString(R.string.posting_sales));
            pdLoadDialog.setCancelable(false);
            pdLoadDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pdLoadDialog;
    }

    public static ProgressDialog showProgressDialog(Context mContext, String message) {
        ProgressDialog pdLoadDialog = null;
        try {
            pdLoadDialog = new ProgressDialog(mContext, R.style.ProgressDialogTheme);
            pdLoadDialog.setMessage(message);
            pdLoadDialog.setCancelable(false);
            pdLoadDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pdLoadDialog;
    }


    public static String getLastSeenDateFormat(Context context, long smsTimeInMilis) {
        return UtilConstants.getLastSeenDateFormat(context, smsTimeInMilis);
    }

    public static String getDateFormat(Context context, long smsTimeInMilis) {
        return UtilConstants.getDateFormat(context, smsTimeInMilis);
    }


    public static void setProgressColor(Context mContext, SwipeRefreshLayout swipeRefresh) {
        swipeRefresh.setDistanceToTriggerSync(ConstantsUtils.SWIPE_REFRESH_DISTANCE);
        swipeRefresh.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
    }

    public static long getMilliSeconds(String givenDateString) {
        long timeInMilliseconds = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            Date mDate = sdf.parse(givenDateString);
            timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }

    public static void onlineRequest(final Context mContext, String query, boolean isSessionRequired, int requestId, int sessionType, final OnlineODataInterface onlineODataInterface, final boolean isReqOnline) {
        final Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_RESOURCE_PATH, query);
        bundle.putBoolean(Constants.BUNDLE_SESSION_REQUIRED, isSessionRequired);
        bundle.putInt(Constants.BUNDLE_REQUEST_CODE, requestId);
        bundle.putInt(Constants.BUNDLE_SESSION_TYPE, sessionType);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (isReqOnline) {
                    OnlineManager.requestQuery(onlineODataInterface, bundle, mContext);// TODO: 19-12-2018 Hided for version upgrade
                } else {
                    OfflineManager.requestQueryOffline(onlineODataInterface, bundle, mContext);
                }
            }
        }).start();
    }

    public static String getSessionId(Context mContext) throws IOException, JSONException {
        String sessionId = "1";
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);
        String loginId = sharedPreferences.getString("username", "");
        String psw = sharedPreferences.getString(UtilConstants.Password_Key, "");
        String connectionId = sharedPreferences.getString(UtilConstants.appConnID_key, "");
        URL getURL = getSessionURL(mContext);
        if (getURL != null) {
            //https://ppcutilsc7c0ed3a1-ce507821b.ap1.hana.ondemand.com/ppcutils/GetLoginID/?destname=pugw_utils_op&Application=PD&Object=mDC&Method=read&FmName=&IsTestRun=
            String strJson = downloadUrl(getURL, loginId, psw, connectionId);
            JSONObject jsonObject = new JSONObject(strJson);
            sessionId = jsonObject.optString("UserSession");
        }
        Log.e("Main", "Session ID Loaded");
        LogManager.writeLogInfo("Session ID Loaded");
        return sessionId;
    }

    public static URL getSessionURL(Context mContext) throws MalformedURLException {
        URL url = null;
//        OnlineStoreListener openListener = OnlineStoreListener.getInstance();
       /* LogonCoreContext lgCtx = LogonCore.getInstance().getLogonContext();// TODO: 19-12-2018 Hided for version upgrade
        String protocol = lgCtx.isHttps() ? "https" : "http";
        String relayUrlSuffix = lgCtx.getResourcePath();
        if (relayUrlSuffix.equalsIgnoreCase(""))
            url = new URL("" + protocol + "://" + lgCtx.getHost() + ":" + lgCtx.getPort() + "/UserSession");
        else {
            String farmId = lgCtx.getFarmId();
            url = new URL("" + protocol + "://" + lgCtx.getHost() + ":" + lgCtx.getPort() + "/" + relayUrlSuffix + "/" + farmId + "/" + "UserSession");
        }*/
        return url;
    }

    /*http url connection*/
    public static String downloadUrl(URL url, String userName, String psw, String connectionId) throws IOException {
        InputStream stream = null;
        HttpsURLConnection connection = null;
        String result = null;
        try {
            connection = (HttpsURLConnection) url.openConnection();
            // Timeout for reading InputStream arbitrarily set to 3000ms.
            connection.setReadTimeout(1000 * 30);
            // Timeout for connection.connect() arbitrarily set to 3000ms.
            connection.setConnectTimeout(1000 * 30);
            String userCredentials = userName + ":" + psw;
            String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);
            connection.setRequestProperty("Authorization", basicAuth);
            connection.setRequestProperty("X-SMP-APPCID", connectionId);
            // For this use case, set HTTP method to GET.
            connection.setRequestMethod("GET");
            // Already true by default but setting just in case; needs to be true since this request
            // is carrying an input (response) body.
            connection.setDoInput(true);
            // Open communications link (network traffic occurs here).
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            // Retrieve the response body as an InputStream.
            stream = connection.getInputStream();
            if (stream != null) {
                // Converts Stream to String with max length of 500.
                result = readStream(stream);
            }
        } finally {
            // Close Stream and disconnect HTTPS connection.
            if (stream != null) {
                stream.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return result;
    }

    public static String readStream(InputStream stream)
            throws IOException {
        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        StringBuilder buffer = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            buffer.append(line).append('\n');
        }
        return buffer.toString();
    }

    public static void selectedView(final View v, final Spinner spinner, final int position, final Context mContext) {
        v.post(new Runnable() {
            @Override
            public void run() {
                try {
                    ((TextView) v.findViewById(R.id.tvItemValue)).setSingleLine(false);
                    if (position == spinner.getSelectedItemPosition())
                        ((TextView) v.findViewById(R.id.tvItemValue)).setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public static String convertDatetoDisplayFormat(Context mContext, String dateString) {
        String stringDateReturns = "";
        Date date = null;
        try {
            date = (new SimpleDateFormat("dd/MM/yyyy")).parse(dateString);
            stringDateReturns = (new SimpleDateFormat("yyyy-MM-dd")).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        stringDateReturns = UtilConstants.convertDateIntoDeviceFormat(mContext, dateString, ConstantsUtils.getConfigTypeDateFormat(mContext));
        return stringDateReturns;
    }

    public static String convertDateIntoDisplayFormat(Context mContext, String dateString) {
        String stringDateReturns = "";
        /*Date date = null;
        try {
            date = (new SimpleDateFormat("dd/MM/yyyy")).parse(dateString);
            stringDateReturns = (new SimpleDateFormat("dd-MMM-yyyy")).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        stringDateReturns = UtilConstants.convertDateIntoDeviceFormat(mContext, dateString, ConstantsUtils.getConfigTypeDateFormat(mContext));
        return stringDateReturns;
    }

    public static String commaSeparator(String value, String uom) {
        String returnValue = "0.00";
        try {
            if (uom.equalsIgnoreCase("INR"))
                returnValue = value != null && !value.equalsIgnoreCase("") ? commaFormat(new BigDecimal(value), "#,##,##0.00") : "0.00";
            else if (uom.equalsIgnoreCase("EUR"))
                returnValue = value != null && !value.equalsIgnoreCase("") ? commaFormat(new BigDecimal(value), "###,###,##0.00") : "0.00";
            else
                returnValue = value != null && !value.equalsIgnoreCase("") ? commaFormat(new BigDecimal(value), "#,##,##0.00") : "0.00";
        } catch (Exception var2) {
            var2.printStackTrace();
            returnValue = "0.00";
        }
        return returnValue;
    }

    public static String commaFormat(BigDecimal number, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(number);
    }

    public static String removeDecimalValueIfDecimalIsZero(String decimalValue) {
        try {
            if (!TextUtils.isEmpty(decimalValue)) {
                String decimalNumber = decimalValue.substring(decimalValue.indexOf(".")).substring(1);
                Double doubleValue = Double.parseDouble(decimalNumber);
                if (doubleValue > 0) {
                    return decimalValue;
                } else {
                    return commaFormat(new BigDecimal(decimalValue), "##0");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decimalValue;
    }

    public static String checkNoUOMZero(String UOM, String qty) throws OfflineODataStoreException {
        boolean isNoUOMZero = false;
        String qry = Constants.ConfigTypesetTypes + "?$filter=" + Constants.Typeset + " eq '" + Constants.UOMNO0 + "' and " +
                Constants.Types + " eq '" + UOM + "'";

        if (UOM != null && !UOM.equalsIgnoreCase("")) {
            if (OfflineManager.offlineStore != null) {
                List<ODataEntity> entities = UtilOfflineManager.getEntities(OfflineManager.offlineStore, qry);
                if (entities != null && entities.size() > 0) {
                    isNoUOMZero = true;
                }
            }
        }
        if (isNoUOMZero)
            return qty != null && !qty.equalsIgnoreCase("") ? commaFormat(new BigDecimal(qty), "###,###,##0") : "000";
        else if (UOM != null && UOM.equals("")) {
            return qty != null && !qty.equalsIgnoreCase("") ? commaFormat(new BigDecimal(qty), "###,###,##0") : "000";
        } else
            return qty != null && !qty.equalsIgnoreCase("") ? commaFormat(new BigDecimal(qty), "###,###,##0.00") : "0.00";
    }

    public static String convertDateFromString(String date) {
        String dateFinal = "";
        try {
            String[] splited = date.split("/");
            String d = splited[0];
            String m = splited[1];
            String y = splited[2];
            return y + "-" + m + "-" + d + "T00:00:00";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateFinal;
    }

    public static String getCurrentTimestamp() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(new Date());
    }

    public static ViewGroup.MarginLayoutParams getLayoutParams(CardView cardView) {
        return (ViewGroup.MarginLayoutParams) cardView.getLayoutParams();
    }

    public static String convertCalenderToDisplayDateFormat(GregorianCalendar calendar) {
        String dateFormatted = "";
        try {
            if (calendar != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                simpleDateFormat.setCalendar(calendar);
                dateFormatted = simpleDateFormat.format(calendar.getTime());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateFormatted;
    }

    public static void displayAlertMessage(CoordinatorLayout coordinatorLayout, String errorMsg) {
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, errorMsg, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    public static int getDueDays(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        Date date = new Date();
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long timeDifferenceInMIliSecond = (new Date().getTime()) - date.getTime();
        return (int) (timeDifferenceInMIliSecond / (1000 * 60 * 60 * 24));
    }

    public static int getBillAge(OutstandingBean item) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        try {
            date = sdf.parse(item.getInvoiceDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long timeDifferenceInMIliSecond = (new Date().getTime()) - date.getTime();
        int billOutDays = (int) (timeDifferenceInMIliSecond / (1000 * 60 * 60 * 24));
        return billOutDays;
    }

    public static void getRollId(Context mContext, AsyncTaskCallBack asyncTaskCallBack) {
        String rollId = "";
        SharedPreferences sharedPerf = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);
        if (!sharedPerf.getBoolean(Constants.isRollResponseGot, false)) {

//                String loginId = sharedPerf.getString(UtilRegistrationActivity.KEY_username, "");
                String authOrgValue = OnlineManager.doOnlineGetRequest(Constants.UserProfiles + "('MSEC')", mContext, event -> {
                    if (event.getResponseStatusCode()==200) {
                        String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                        Log.d("OnlineManager", "getUserRollInfo: " + responseBody + " " + event.getResponseStatusCode());
                        JSONObject jsonObj = null;
                        try {
                            jsonObj = new JSONObject(responseBody);
                            JSONObject objectD = jsonObj.optJSONObject("d");

                            SharedPreferences.Editor editor = sharedPerf.edit();
                            editor.putString(Constants.USERROLE,  objectD.optString("RoleID"));
                            editor.putBoolean(Constants.isRollResponseGot, true);
                            editor.apply();

                            asyncTaskCallBack.onStatus(true, "");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            asyncTaskCallBack.onStatus(false, e.getMessage());
                        }
                    }else if(event.getResponseStatusCode()==401){
                        try {
                            String  resultJson = readResponse(event.getStream());
                            if (resultJson.contains("401 Nicht autorisiert")){
                                asyncTaskCallBack.onStatus(false, "User is locked or unavailable in backend system");
                            }else{
                                asyncTaskCallBack.onStatus(false, "User is locked or unavailable in backend system");
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                            asyncTaskCallBack.onStatus(false, "User is locked or unavailable in backend system");
                        }
                    }else {
                        String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                        asyncTaskCallBack.onStatus(false, responseBody);
                    }
                }, iError->{
                    iError.printStackTrace();
                    String errorMessage = "";
                    if(iError.getMessage().contains("No address associated with hostname") || iError.getMessage().contains("Software caused connection")|| iError.getMessage().contains("Failed to connect")|| iError.getMessage().contains("during system call")){
                        errorMessage = "Unable to fetch user profiles data due to poor internet connectivity[UserProfiles]. \n\nPlease check internet and retry";
                    }
                    Log.d("OnlineManager error", "getUserRollInfo: ");
                    LogManager.writeLogError("OnlineManager error userprofiles : "+  iError.getMessage());
                    if(!TextUtils.isEmpty(errorMessage)){
                        asyncTaskCallBack.onStatus(false, errorMessage);
                    }else {
                        asyncTaskCallBack.onStatus(false, iError.getMessage());
                    }
                });

        } else {
            asyncTaskCallBack.onStatus(true,"");
        }
    }
    public static String readResponse(InputStream stream) throws IOException {
        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder buffer = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line).append('\n');
        }

        return buffer.toString();
    }
    public static void getUserAuthInfo(Context mContext, AsyncTaskCallBack asyncTaskCallBack) {
        String rollId = "";
        SharedPreferences sharedPerf = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);
        if (!sharedPerf.getBoolean(Constants.isUserAuthResponseGot, false)) {

//                String loginId = sharedPerf.getString(UtilRegistrationActivity.KEY_username, "");
                String authOrgValue = OnlineManager.doOnlineGetRequest("UserProfileAuthSet?$filter=Application%20eq%20%27MSEC%27", mContext, event -> {
                    if (event.getResponseStatusCode()==200) {
                        String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                        Log.d("OnlineManager", "getUserRollInfo: " + responseBody + " " + event.getResponseStatusCode());
                        JSONObject jsonObj = null;
                        try {
                            jsonObj = new JSONObject(responseBody);
                            JSONObject objectD = jsonObj.optJSONObject("d");
                            String value = "";
                            JSONArray jsonArray = objectD.getJSONArray("results");
                            boolean result = false;
                            for (int i=0; i<jsonArray.length();i++){
                                try {
                                    value = "";
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    value = jsonObject.optString(Constants.AuthOrgValue);
                                    if(value.equalsIgnoreCase("RPD")){
                                        result = true;
                                        break;
                                    }else if(value.equalsIgnoreCase("AWSM")){
                                        result = true;
                                        break;
                                    }else if(value.equalsIgnoreCase("SDA")){
                                        result = true;
                                        break;
                                    }else if(value.equalsIgnoreCase("PSM")){
                                        result = true;
                                        break;
                                    }else if(value.equalsIgnoreCase("VAN")){
                                        result = true;
                                        break;
                                    }else if(value.equalsIgnoreCase("CALLCNTR")){
                                        result = true;
                                        break;
                                    }else if(value.equalsIgnoreCase("BCRVAN")){
                                        result = true;
                                        break;
                                    }else if (value.equalsIgnoreCase("BVAN")) {
                                        result = true;
                                        break;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            if(result) {
                                SharedPreferences.Editor editor = sharedPerf.edit();
                                editor.putString(Constants.USERAuthID,value);
                                editor.putBoolean(Constants.isUserAuthResponseGot, true);
                                editor.apply();
                                asyncTaskCallBack.onStatus(true, "");
                            }else {
                                asyncTaskCallBack.onStatus(false, "Role authorization not maintained in user authorization");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            asyncTaskCallBack.onStatus(false, e.getMessage());
                        }
                    }else {
                        String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                        asyncTaskCallBack.onStatus(false, responseBody);
                    }
                }, iError->{
                    iError.printStackTrace();
                    Log.d("OnlineManager", "getUserRollInfo: ");
                    String errorMessage = "";
                    if(iError.getMessage().contains("No address associated with hostname") || iError.getMessage().contains("Software caused connection")|| iError.getMessage().contains("Failed to connect")|| iError.getMessage().contains("during system call")){
                        errorMessage = "Unable to fetch user authorization data due to poor internet connectivity[UserProfileAuthSet]. \n\n Please check internet and retry";
                    }
                    LogManager.writeLogError("OnlineManager error UserProfileAuthSet : "+  iError.getMessage());
                    if(!TextUtils.isEmpty(errorMessage)){
                        asyncTaskCallBack.onStatus(false, errorMessage);
                    }else {
                        asyncTaskCallBack.onStatus(false, iError.getMessage());
                    }
                });

        } else {
            asyncTaskCallBack.onStatus(true,"");
        }
    }

    public static String getRollInformation(Context mContext) {
        SharedPreferences sharedPerf = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);
        return sharedPerf.getString(Constants.USERROLE, "");
    }

    public static String getAuthInformation(Context mContext) {
        SharedPreferences sharedPerf = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);
        return sharedPerf.getString(Constants.USERAuthID, "");
    }

    public static BigDecimal decimalRoundOff(BigDecimal bigDecimalValue, int numberOfDigitsAfterDecimalPoint) {
        bigDecimalValue = bigDecimalValue.setScale(numberOfDigitsAfterDecimalPoint,
                BigDecimal.ROUND_HALF_UP);
        return bigDecimalValue;
    }

    public static String addZeroBeforeValue(int values, int minLenght) {
        String finalValues = "";
        try {
            if (values == 0) {
                values = values + 1;
            }
            String stringValues = values + "";
            int currentLength = stringValues.length();
            String typeValues = OfflineManager.getValueByColumnName(Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Typeset + " eq '" + Constants.SS + "' and " + Constants.Types + " eq '" + Constants.SMINVITMNO + "' &$top=1", Constants.TypeValue);
            boolean noItemZero = getNoItemZero();
            if (typeValues.equalsIgnoreCase("10") && noItemZero) {
                finalValues = values + "0";
            } else if ((typeValues.equalsIgnoreCase("1")) && !noItemZero) {
                if (minLenght == 6) {
                    if (currentLength == 1) {
                        finalValues = "00000" + values;
                    } else if (currentLength == 2) {
                        finalValues = "0000" + values;
                    } else if (currentLength == 3) {
                        finalValues = "000" + values;
                    } else if (currentLength == 4) {
                        finalValues = "00" + values;
                    } else if (currentLength == 5) {
                        finalValues = "0" + values;
                    } else if (currentLength == 6) {
                        finalValues = "" + values;
                    } else {
                        finalValues = values + "";
                    }
                }else if (minLenght == 3) {
                    if (currentLength == 1) {
                        finalValues = "000" + values;
                    } else if (currentLength == 2) {
                        finalValues = "00" + values;
                    } else if (currentLength == 3) {
                        finalValues = "0" + values;
                    } else if (currentLength == 4) {
                        finalValues = "" + values;
                    } else {
                        finalValues = values + "";
                    }
                } else {
                    finalValues = values + "";
                }
            } else if ((typeValues.equalsIgnoreCase("10")) && !noItemZero) {
                if (minLenght == 6) {
                    if (currentLength == 1) {
                        finalValues = "0000" + values+"0";
                    } else if (currentLength == 2) {
                        finalValues = "000" + values+"0";
                    } else if (currentLength == 3) {
                        finalValues = "00" + values+"0";
                    } else if (currentLength == 4) {
                        finalValues = "0" + values+"0";
                    } else if (currentLength == 5) {
                        finalValues =  values+"0";
                    } else {
                        finalValues = values + "";
                    }
                }else if (minLenght == 3) {
                    if (currentLength == 1) {
                        finalValues = "00" + values+"0";
                    } else if (currentLength == 2) {
                        finalValues = "0" + values+"0";
                    } else if (currentLength == 3) {
                        finalValues =  values+"0";
                    } else {
                        finalValues = values + "";
                    }
                } else {
                    finalValues = values + "";
                }
            } else {
                finalValues = values + "";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalValues;
    }
    public static boolean getNoItemZero() {
         String query = Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Typeset + " eq '" +
               Constants.SS + "' and " + Constants.Types + " eq '" + Constants.NOITMZEROS + "' &$top=1";
        try {
            String typeValues = OfflineManager.getValueByColumnName(query, Constants.TypeValue);
            if (typeValues.equalsIgnoreCase(Constants.X)) {
                return true;
            }
        } catch (OfflineODataStoreException e) {
            e.printStackTrace();
        }
        return false;
    }
    public static String convertDateIntoDisplayFormat(String dateString) {
        String stringDateReturns = "";
        Date date = null;
        try {
            date = (new SimpleDateFormat("dd/MM/yyyy")).parse(dateString);
            stringDateReturns = (new SimpleDateFormat("dd-MMM-yyyy")).format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return stringDateReturns;
    }
    public static String getAutoSyncTimeInMin(){
        try {
            //time in minutes
            return OfflineManager.getValueByColumnName(Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Typeset + " eq '" + Constants.MSEC + "' and " + Constants.Types + " eq '"+Constants.AUTOSYNC+"' &$top=1", Constants.TypeValue);
        } catch (OfflineODataStoreException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String getSOAutoSyncTimeInMin(){
        try {
            //time in minutes
            return OfflineManager.getValueByColumnName(Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Typeset + " eq '" + Constants.MSEC + "' and " + Constants.Types + " eq '"+Constants.SOAUTOSYNC  +"' &$top=1", Constants.TypeValue);
        } catch (OfflineODataStoreException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static void startAutoSync(Context mContext, boolean isForceReset) {
        try {
            SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);
            Constants.isSync = false;
            String autoSyncTime = ConstantsUtils.getAutoSyncTimeInMin();
            if (isForceReset || !sharedPreferences.getString("AUTOSYNCTIME","").equalsIgnoreCase(autoSyncTime)) {
                if (!TextUtils.isEmpty(autoSyncTime)) {
                UpdatePendingRequest.getInstance(null).callSchedule(autoSyncTime);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("AUTOSYNCTIME", autoSyncTime);
                editor.apply();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogManager.writeLogError(Constants.error_txt + e.getMessage());
        }
    }

    public static boolean checkStoragePermission(final Activity activity) {
        boolean isPermissionEnable = false;
        if (Build.VERSION_CODES.M <= android.os.Build.VERSION.SDK_INT) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale((activity), Manifest.permission.READ_EXTERNAL_STORAGE)
                        && ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.STORAGE_PERMISSION);
                } else if (Constants.getPermissionStatus(activity, Manifest.permission.READ_EXTERNAL_STORAGE) && Constants.getPermissionStatus(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                    UtilConstants.dialogBoxWithCallBack(activity, "", activity.getString(R.string.this_app_needs_storage_permission), activity.getString(R.string.msg_go_to_settings),
                            activity.getString(R.string.cancel), false, new DialogCallBack() {
                                @Override
                                public void clickedStatus(boolean clickedStatus) {
                                    if (clickedStatus) {
                                        UtilConstants.navigateToAppSettingsScreen(activity);
                                    }
                                }
                            });
                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            Constants.STORAGE_PERMISSION);
                }
                Constants.setPermissionStatus(activity, Manifest.permission.READ_EXTERNAL_STORAGE, true);
                Constants.setPermissionStatus(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
            } else {
                //You already have the permission
                isPermissionEnable = true;
            }
        } else {
            isPermissionEnable = true;
        }
        return isPermissionEnable;
    }
    public static String getConfigTypeDateFormat(Context mContext){
        try {
            return OfflineManager.getValueByColumnName(Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Typeset + " eq '" +Constants.SS + "' and " + Constants.Types + " eq '"+Constants.DATEFORMAT+"' &$top=1", Constants.TypeValue);
        } catch (OfflineODataStoreException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String getDiffTime(String startTime, String endTime) {
        String mStrDiffTime = "";

        long difference = 0;
        try {
            if(!startTime.equalsIgnoreCase("")) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                try {
                    Date startDate = simpleDateFormat.parse(startTime);
                    Date endDate = simpleDateFormat.parse(endTime);

                    difference = endDate.getTime() - startDate.getTime();
                    if (difference < 0) {
                        Date dateMax = null;

                        dateMax = simpleDateFormat.parse(startTime);

                        Date dateMin = simpleDateFormat.parse(endTime);
                        difference = (dateMax.getTime() - startDate.getTime()) + (endDate.getTime() - dateMin.getTime());
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            int days = (int) (difference / (1000 * 60 * 60 * 24));
            int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);

            int seconds = (int)(difference / 1000) % 60;


            return (hours<10?"0"+hours:hours) + ":" + (min<10?"0"+min:min) +":"+(seconds<10?"0"+seconds:seconds);
        } catch (Exception e) {
            return 00 + ":" + 00 +":"+00;
        }
    }
    public static String checkBeatApprove(Context mContext){
//        try {
            return "";//OfflineManager.getValueByColumnName(Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Typeset + " eq '" +Constants.SS + "' and " + Constants.Types + " eq '"+Constants.DATEFORMAT+"' &$top=1", Constants.TypeValue);
//        } catch (OfflineODataStoreException e) {
//            e.printStackTrace();
//        }
//        return "";
    }
    public static void showKeyboard(Context mContext, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static void dialogBoxWithButton(Context context, String title, String message, String positiveButton, String negativeButton, final DialogCallBack dialogCallBack) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyTheme);
            if (!title.equalsIgnoreCase("")) {
                builder.setTitle(title);
            }
            builder.setMessage(message).setCancelable(false).setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    if (dialogCallBack != null)
                        dialogCallBack.clickedStatus(true);
                }
            });
            if (!negativeButton.equalsIgnoreCase("")) {
                builder.setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if (dialogCallBack != null)
                            dialogCallBack.clickedStatus(false);
                    }
                });
            }
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    public static void insertSyncTimes(String colName) {
//        if (colName.contains("?$")) {
//            String splitCollName[] = colName.split("\\?");
//            colName = splitCollName[0];
//            if (colName.contains("/")) {
//                String splitColl[] = colName.split("/");
//                colName = splitColl[0];
//            }
//        }
//        String syncTime = UtilConstants.getSyncHistoryddmmyyyyTime();
//        UtilConstants.insertLogHistoryTable(Constants.SYNC_TABLE, colName, syncTime);
//    }


    public static void insertSyncTimes(String colName) {
        if (colName.contains("?$")) {
            String[] splitCollName = colName.split("\\?");
            colName = splitCollName[0];
            if (colName.contains("/")) {
                String[] splitColl = colName.split("/");
                colName = splitColl[0];
            }
        }
        String syncTime = UtilConstants.getSyncHistoryddmmyyyyTime();
        try {
//            Constants.events.insertLogHistoryTable(Constants.SYNC_TABLE, colName, syncTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean showImage(String pastDate){
        try{
            if (!TextUtils.isEmpty(pastDate)) {
                String[] pastDateSplit = pastDate.split("/");
                String oldD = pastDateSplit[0];
                String oldM = pastDateSplit[1];

                Calendar oldCalender = Calendar.getInstance();
                Calendar currentCalender = Calendar.getInstance();
                oldCalender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(oldD));
                oldCalender.set(Calendar.MONTH, Integer.parseInt(oldM) - 1);
                Date oldDate = oldCalender.getTime();
                Date currentDate = currentCalender.getTime();
                long numberOfDays = getUnitBetweenDates(oldDate, currentDate, TimeUnit.DAYS);
                if (numberOfDays >= 0) {
                    return true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    private static long getUnitBetweenDates(Date startDate, Date endDate, TimeUnit unit) {
        long timeDiff = endDate.getTime() - startDate.getTime();
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS);
    }

    public static int getMaxImagesforWindowDis(String types) {
        int maxImage = 0;
        try {
            String stMaxValue = OfflineManager.getValueByColumnName(Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Typeset + " eq '" +
                    Constants.SC + "' and " + Constants.Types + " eq '" + types + "' &$top=1", Constants.TypeValue);

            if (!TextUtils.isEmpty(stMaxValue))
                maxImage = Integer.parseInt(stMaxValue);

        } catch (OfflineODataStoreException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return maxImage;
    }
    public static void openImageInDialogBox(Context context, byte[] imageByteArray){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.img_expand);
        // set the custom dialog components - text, image and
        ImageView image = dialog.findViewById(R.id.imageView1);

        image.setImageBitmap(BitmapFactory.decodeByteArray(imageByteArray, 0,
                imageByteArray.length));
        dialog.show();
    }

    /**
     * @param mContext
     * @return
    0: First time Install
    1: APP runs in same version
    2: APP is Updated
     */
/*
    public static int getFirstTimeRun(Context mContext) {
        SharedPreferences sp = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);
        int result, currentVersionCode = BuildConfig.VERSION_CODE;
        int lastVersionCode = sp.getInt(Constants.KEY_FIRST_TIME_RUN, -1);
        if (lastVersionCode == -1) result = 0; else
            result = (lastVersionCode == currentVersionCode) ? 1 : 2;
        sp.edit().putInt(Constants.KEY_FIRST_TIME_RUN, currentVersionCode).apply();
        return result;
    }
*/
    public static void storeInDataVault(String docNo, String jsonHeaderObjectAsString, Context mContext) {
        UtilDataVault.storeInDataVault(docNo,jsonHeaderObjectAsString,mContext,Constants.EncryptKey);
    }
    public static String getFromDataVault(String docNo, Context mContext) {
        return UtilDataVault.getValueFromDataVault(docNo,mContext,Constants.EncryptKey);
    }

    /**
     * This will conver string 32 char to GUID 36 char
     * @param guid32
     * @return
     */
    public static String string32To36(String guid32) {
        String guidValue = "";
        try {
            for (int i = 0; i < guid32.length(); i++) {
                if (i == 8 || i == 12 || i == 16 || i == 20) {
                    guidValue = guidValue.concat("-");
                    guidValue = guidValue + (guid32.charAt(i));
                } else {
                    guidValue = guidValue.concat(String.valueOf(guid32.charAt(i)));
                }
            }
            System.out.println(guidValue);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return guidValue;
    }

    public static String returnCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        return getDateFormat("dd-MM-yyyy").format(calendar.getTime());
    }
    public static String returnCurrentDate(String format){
        Calendar calendar = Calendar.getInstance();
        return getDateFormat(format).format(calendar.getTime());
    }

    public static SimpleDateFormat getDateFormat(String customFormat) {
        return new SimpleDateFormat(customFormat);
    }

    public static String formatDate(String existingFormat, String inputDate, String inputFormat) {
        SimpleDateFormat currentFormat = null;
        Date date = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(existingFormat, Locale.US);
            date = simpleDateFormat.parse(inputDate);
            currentFormat = new SimpleDateFormat(inputFormat,Locale.US);
            assert date != null;
            currentFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert currentFormat != null;
        return currentFormat.format(date);
    }
    public static String oDataTimeFormat() {
        Calendar calendar = Calendar.getInstance();
        return Constants.getDateFormat("HH-mm-ss").format(calendar.getTime());
    }

    public static String oDataTimeFormat1() {
        Calendar calendar = Calendar.getInstance();
        String time = Constants.getDateFormat("HH-mm-ss").format(calendar.getTime());
        if (!TextUtils.isEmpty(time)) {
            String itme1[] = time.split("-");
            return "PT"+itme1[0]+"H"+itme1[1]+"M"+itme1[2]+"S";
        }
        return "";
    }

    public static ODataDuration oDataTimeFormatConvert() {
        final Calendar calCurrentTime = Calendar.getInstance();
        int hourOfDay = calCurrentTime.get(Calendar.HOUR_OF_DAY); // 24 hour clock
        int minute = calCurrentTime.get(Calendar.MINUTE);
        ODataDuration oDataDuration = null;
        try {
            oDataDuration = new ODataDurationDefaultImpl();
            oDataDuration.setHours(hourOfDay);
            oDataDuration.setMinutes(minute);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return oDataDuration;
    }
    private static final long LIMIT = 10000000000L;
    private static long last = 0;

    public static long getID() {
        // 10 digits.
        long id = System.currentTimeMillis() % LIMIT;
        if ( id <= last ) {
            id = (last + 1) % LIMIT;
        }
        return last = id;
    }
    public static void focusOnView(final NestedScrollView nestedScroll) {
        nestedScroll.post(new Runnable() {
            @Override
            public void run() {
                nestedScroll.scrollTo(0, 0);
            }
        });
    }

    public static androidx.appcompat.app.AlertDialog.Builder showAlert(String message, Context context , DialogInterface.OnClickListener listener) {
        androidx.appcompat.app.AlertDialog.Builder builder = null;
        try {
            builder = new androidx.appcompat.app.AlertDialog.Builder(context, R.style.DialogTheme);
            builder.setMessage(message).setCancelable(false).setPositiveButton("Ok", listener);
            builder.show();
        } catch (Exception var3) {
            var3.printStackTrace();
        }
        return builder;
    }


    private static boolean isActiveNetwork(Context context){
        return isConnected(context);
    }
    private static boolean isConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isWiFi = false;
        boolean isMobile = false;
        boolean isConnected = false;
        if (activeNetwork != null) {
            isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
            isMobile = activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
            isConnected = activeNetwork.isConnectedOrConnecting();
        }
        if (isConnected) {
            if (isWiFi) {
                return isConnectedToThisServer();
            }
            if (isMobile) {
                return isConnectedToThisServer();
            }
        } else {
            errorCounnt = 5;
            return false;
        }
        return false;
    }
    private static boolean isConnectedToThisServer() {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);
        Future<Boolean>isConnected = executor.submit(() -> {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);
            try {
                sock.connect(sockaddr, timeoutMs);
                sock.close();
                System.out.println("Google DNS : Connected to server");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Google DNS : failed to Connect");
                return false;
            }
        });
        try {
            return isConnected.get();
        } catch (Throwable e) {
            return false;
        }
    }

    public static void deletePostedSOData(Context context) {
        try {
            Set<String> set = new HashSet<>();
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);
            set = sharedPreferences.getStringSet(Constants.SecondarySOCreate, null);
            if (set != null && !set.isEmpty()) {
                Iterator itr = set.iterator();
                while (itr.hasNext()) {
                    String store = null, deviceNo = "";
                    try {
                        deviceNo = itr.next().toString();
                        store = ConstantsUtils.getFromDataVault(deviceNo.toString(),context);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject fetchJsonHeaderObject = new JSONObject(store);
                        String ssoGuid =  fetchJsonHeaderObject.getString(Constants.SSSOGuid);
                        String OrderNo =  "";
                        if(fetchJsonHeaderObject.has(Constants.OrderNo)){
                            OrderNo =  fetchJsonHeaderObject.getString(Constants.OrderNo);
                        }
                        String query = Constants.SSSOs + "?$filter=" + Constants.SSSOGuid + " eq guid'" +ssoGuid + "'";

                    } catch (JSONException e) {
                        LogManager.writeLogError("Refresh Sync SO Delete : "+e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        } catch (NumberFormatException e) {
            LogManager.writeLogError("Refresh Sync SO Delete : "+e.getMessage());
            e.printStackTrace();
        }

    }


    public static void deletePostedROData(Context context) {
        try {
            Set<String> set = new HashSet<>();
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);
            set = sharedPreferences.getStringSet(Constants.ROList, null);
            if (set != null && !set.isEmpty()) {
                Iterator itr = set.iterator();
                while (itr.hasNext()) {
                    String store = null, deviceNo = "";
                    try {
                        deviceNo = itr.next().toString();
                        store = ConstantsUtils.getFromDataVault(deviceNo.toString(),context);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject fetchJsonHeaderObject = new JSONObject(store);
                        String ssoGuid =  fetchJsonHeaderObject.getString(Constants.SSROGUID);
                        String OrderNo =  "";
                        String query = Constants.SSROs + "?$filter=" + Constants.SSROGUID + " eq guid'" +ssoGuid + "'";

                    } catch (JSONException e) {
                        LogManager.writeLogError("Refresh Sync RO Delete : "+e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        } catch (NumberFormatException e) {
            LogManager.writeLogError("Refresh Sync SO Delete : "+e.getMessage());
            e.printStackTrace();
        }

    }

    private static boolean isNetworkStopped;
    public static void checkNetwork(Context context,OnNetworkInfoListener networkInfoListener,boolean isInterupted){
        errorCounnt = 0;

        if (!isInterupted) {
            isNetworkStopped=false;
            try {
                Thread networkThread = new Thread(() -> {
                    check(context,networkInfoListener);
                });
                networkThread.start();
            } catch (Throwable e) {
                isNetworkStopped=true;
            }
        }else{
            isNetworkStopped =true;
        }
    }
    public static void checkNetwork(Context context,OnNetworkInfoListener networkInfoListener,boolean isInterupted,int failureCount){
        errorCounnt = 0;
        ConstantsUtils.failureCount = failureCount;
        if (!isInterupted) {
            isNetworkStopped=false;
            Thread networkThread = new Thread(() -> {
                check(context,networkInfoListener);
            });
            networkThread.start();
        }else{
            isNetworkStopped =true;
        }
    }
    private static  int errorCounnt = 0;
    private static  int failureCount = 5;
    private static void check(Context context,OnNetworkInfoListener networkInfoListener){
        try {
            if (!isNetworkStopped) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                boolean isError = ConstantsUtils.isActiveNetwork(context);
                if (!isError) {
                    errorCounnt++;
                    if(errorCounnt>failureCount) {
                        LogManager.writeLogError(ErrorCode.NETWORK_ERROR);
                        Log.e("CHECKING NETWORK", "NETWORK ERROR");
                        if (networkInfoListener != null) {
                            networkInfoListener.onNetworkFailureListener(true);
                        }
                    }else{
                        Log.e("CHECKING NETWORK", "FAILED ATTEMPT");
                        check(context, networkInfoListener);
                    }
                } else {
                    Log.e("CHECKING NETWORK", "NETWORK ACTIVE");
                    check(context, networkInfoListener);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    public interface OnNetworkInfoListener{
        void onNetworkFailureListener(boolean isFailed);
    }

    public static String getErrorCode(String value){
        try {
            String answer = value.substring(value.indexOf("([-")+1,value.indexOf("]")+1);
            return answer.replaceAll("[^0-9]", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static void inValidPasswordDialog(Context context,String mErrTxt) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custom_scroll_dialog, null);

        TextView textview = view.findViewById(R.id.tv_err_msg);
        final TextView tvdetailmsg = view.findViewById(R.id.tv_detail_msg);

        String temp_errMsg = mErrTxt;
        temp_errMsg = makecustomHttpErrormessage(context,temp_errMsg);
        if (!TextUtils.isEmpty(temp_errMsg) && temp_errMsg.equalsIgnoreCase(mErrTxt)) {
            if (mErrTxt.contains("invalid authentication")) {
                textview.setText(Constants.PasswordExpiredMsg);
                tvdetailmsg.setText(mErrTxt);
            } else if (mErrTxt.contains("HTTP Status 401 ? Unauthorized")) {
                textview.setText(Constants.PasswordExpiredMsg);
                tvdetailmsg.setText(mErrTxt);
            }
        }

        else {
            textview.setText("\n" + temp_errMsg);
        }

        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .setPositiveButton(android.R.string.ok, null) //Set to null. We override the onclick
                .setNeutralButton("Details", null)
                .setNegativeButton("Settings", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        dialogInterface.dismiss();
                    }
                });

                Button mesg = dialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                mesg.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something

                        tvdetailmsg.setVisibility(View.VISIBLE);
                        // dialog.dismiss();
                    }
                });

                Button change = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                change.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        // TODO Do something
                        RegistrationModel<Serializable> registrationModel = new RegistrationModel<>();
                        Intent intent = new Intent(context, SecuritySettingActivity.class);
                        registrationModel.setExtenndPwdReq(true);
                        registrationModel.setUpdateAsPortalPwdReq(true);
                        registrationModel.setIDPURL(Configuration.IDPURL);
                        registrationModel.setExternalTUserName(Configuration.IDPTUSRNAME);
                        registrationModel.setExternalTPWD(Configuration.IDPTUSRPWD);
                        intent.putExtra(UtilConstants.RegIntentKey, registrationModel);
                        //context.startActivityForResult(intent, 350);
                        context.startActivity(intent);
                        // dialog.dismiss();
                    }
                });

            }
        });
        dialog.show();

    }

    public static String makecustomHttpErrormessage(Context context,String error_msg) {
        String[] array = Constants.getDefiningReq(context);
        httphashmaperrorcodes();
        if (!TextUtils.isEmpty(error_msg) && error_msg.contains("HTTP code")) {
            String make_message = "";
            List<String> errorList = Arrays.asList(error_msg.split(","));
            if (errorList.size() > 0) {
                for (int i = 0; i < array.length; i++) {
                    if (error_msg.contains(array[i])) {
                        make_message = array[i] + ":";
                        break;
                    }
                }
                String httperrormsg = "";
                for (String data : errorList) {
                    Iterator<String> keySetIterator = httpErrorCodes.keySet().iterator();
                    while (keySetIterator.hasNext()) {
                        String key = keySetIterator.next();
                        if (data.contains(key)) {
                            httperrormsg = httpErrorCodes.get(key) + "-" + key;
                            break;
                        }

                    }
                }
                if (!TextUtils.isEmpty(httperrormsg))
                    make_message += httperrormsg + " Please contact channel team";
                else make_message = error_msg;
            } else {
                make_message = error_msg;
            }
            return make_message;

        }
        return error_msg;

    }
    public static HashMap<String, String> httpErrorCodes = new HashMap<String, String>();
    public static void httphashmaperrorcodes() {
        //500 series
        httpErrorCodes.put("500", "Connection Timeout");
        httpErrorCodes.put("501", "Not Implemented");
        httpErrorCodes.put("502", "Bad Gateway");
        httpErrorCodes.put("503", "Service Unavailable");
        httpErrorCodes.put("504", "Gateway Timeout");
        httpErrorCodes.put("505", "HTTP Version Not Supported");
        httpErrorCodes.put("506", "Variant Also Negotiates");
        httpErrorCodes.put("507", "Insufficient Storage");
        httpErrorCodes.put("508", "Loop Detected");
        httpErrorCodes.put("509", "Unassigned");
        httpErrorCodes.put("510", "Not Extended");
        httpErrorCodes.put("511", "Network Authentication Required");
        //400 series
        httpErrorCodes.put("400", "Bad Request");
        httpErrorCodes.put("401", "Unauthorized");
        httpErrorCodes.put("402", "Payment Required");
        httpErrorCodes.put("403", "Forbidden");
        httpErrorCodes.put("404", "Not Found");
        httpErrorCodes.put("405", "Method Not Allowed");
        httpErrorCodes.put("406", "Not Acceptable");
        httpErrorCodes.put("407", "Proxy Authentication Required");
        httpErrorCodes.put("408", "Request Timeout");
        httpErrorCodes.put("409", "Conflict");
        httpErrorCodes.put("410", "Gone");
        httpErrorCodes.put("411", "Length Required");
        httpErrorCodes.put("412", "Precondition Failed");
        httpErrorCodes.put("413", "Payload Too Large");
        httpErrorCodes.put("414", "URI Too Long");
        httpErrorCodes.put("415", "Unsupported Media Type");
        httpErrorCodes.put("416", "Range Not Satisfiable");
        httpErrorCodes.put("417", "Expectation Failed");
        httpErrorCodes.put("421", "Misdirected Request");
        httpErrorCodes.put("422", "Unprocessable Entity");
        httpErrorCodes.put("423", "Locked");
        httpErrorCodes.put("424", "Failed Dependency");
        httpErrorCodes.put("425", "Too Early");
        httpErrorCodes.put("426", "Upgrade Required");
        httpErrorCodes.put("427", "Unassigned");
        httpErrorCodes.put("428", "Precondition Required");
        httpErrorCodes.put("429", "Too Many Requests");
        httpErrorCodes.put("430", "Unassigned");
        httpErrorCodes.put("431", "Request Header Fields Too Large");
        httpErrorCodes.put("451", "Unavailable For Legal Reasons");
    }

    public static void validateIMEI(Context mContext, AsyncTaskCallBack asyncTaskCallBack){
        OnlineManager.validateIMEI("UserProfileAuthSet?$filter=Application eq 'MSEC' and AuthOrgTypeID eq '000038' and AuthOrgValue eq '"+MSFAApplication.APP_DEVICEID+"'", mContext, event -> {
            if (event.getResponseStatusCode()==200) {
                String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                Log.d("OnlineManager", "getUserRollInfo: " + responseBody + " " + event.getResponseStatusCode());
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(responseBody);
                    JSONObject objectD = jsonObj.optJSONObject("d");
                    String value = "";
                    JSONArray jsonArray = objectD.getJSONArray("results");
                    if(jsonArray.length()>0) {
                        asyncTaskCallBack.onStatus(true, "");
                    }else {
                        asyncTaskCallBack.onStatus(false, "Device-ID not mapped. Please retry after mapping..!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    asyncTaskCallBack.onStatus(false, e.getMessage());
                }
            }else {
                String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                asyncTaskCallBack.onStatus(false, responseBody);
            }
        }, iError->{
            iError.printStackTrace();
            Log.d("OnlineManager", "getUserRollInfo: ");
            asyncTaskCallBack.onStatus(false, iError.getMessage());
        });
    }

    public static PendingIntent alarmPendingIntent,scheduleSyncAlarmPendingIntent;
    public static void startAutoSyncBG(Context mContext, boolean isForceReset) {
        try {
            if (ConstantsUtils.isAutomaticTimeZone(mContext)) {
                SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);
                try {
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("FIRSTAUTOSYNCTIME", sdf.format(cal.getTime()));
                    editor.putString("LASTSYNCDATE", com.arteriatech.mutils.common.UtilConstants.getNewDate());
                    editor.apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Constants.isSync = false;
                String autoSyncTime = ConstantsUtils.getAutoSyncTimeInMin();
                if (isForceReset || !sharedPreferences.getString("AUTOSYNCTIME", "").equalsIgnoreCase(autoSyncTime)) {
//                if (!TextUtils.isEmpty(autoSyncTime)) {
                    //  UpdatePendingRequest.getInstance(null).callSchedule(autoSyncTime);
                    Intent intent = new Intent(mContext.getApplicationContext(), AutoSyncDataAlarmReceiver.class);
                    // Create a PendingIntent to be triggered when the alarm goes off
                    alarmPendingIntent = PendingIntent.getBroadcast(mContext, AutoSyncDataAlarmReceiver.REQUEST_CODE,
                            intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    // Setup periodic alarm every 5 seconds
                    long firstMillis = System.currentTimeMillis(); // first run of alarm is immediate
                    int intervalMillis = 0; // as of API 19, alarm manager will be forced up to 60000 to save battery
                    try {
                        LogManager.writeLogError("AutoSync config time : "+autoSyncTime);
                        intervalMillis = 1000 * 60 * Integer.parseInt(autoSyncTime);
                        LogManager.writeLogInfo("AutoSync Scheduled with time : "+autoSyncTime);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        LogManager.writeLogError("AutoSync default time: "+60);
                        intervalMillis = 1000 * 60 *60;
                    }
                    AlarmManager alarm = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                    // See https://developer.android.com/training/scheduling/alarms.html
                    alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis+intervalMillis, intervalMillis, alarmPendingIntent);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("AUTOSYNCTIME", autoSyncTime);
                    editor.apply();
//                }
                }
            }else{
                LogManager.writeLogError("Auto Sync not started because date is not valid ");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogManager.writeLogError(Constants.error_txt + e.getMessage());
        }
    }

    public static void startScheduleSyncBG(Context mContext, boolean isForceReset) {
        try {
            if (ConstantsUtils.isAutomaticTimeZone(mContext)) {
                SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);
                try {
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("FIRSTSOAUTOSYNCTIME", sdf.format(cal.getTime()));
                    editor.putString("LASTSOSYNCDATE", com.arteriatech.mutils.common.UtilConstants.getNewDate());
                    editor.apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Constants.isSync = false;
                String autoSyncTime = ConstantsUtils.getSOAutoSyncTimeInMin();
                if (isForceReset || !sharedPreferences.getString("SOAUTOSYNC", "").equalsIgnoreCase(autoSyncTime)) {
//                if (!TextUtils.isEmpty(autoSyncTime)) {
                    //  UpdatePendingRequest.getInstance(null).callSchedule(autoSyncTime);
                    Intent intent = new Intent(mContext.getApplicationContext(), ScheduleSyncDataAlarmReceiver.class);
                    // Create a PendingIntent to be triggered when the alarm goes off
                    scheduleSyncAlarmPendingIntent = PendingIntent.getBroadcast(mContext, ScheduleSyncDataAlarmReceiver.REQUEST_CODE,
                            intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    // Setup periodic alarm every 5 seconds
                    long firstMillis = System.currentTimeMillis(); // first run of alarm is immediate
                    int intervalMillis = 0; // as of API 19, alarm manager will be forced up to 60000 to save battery
                    try {
                        LogManager.writeLogError("AutoSync config time : "+autoSyncTime);
                        intervalMillis = 1000 * 60 * Integer.parseInt(autoSyncTime);
                        LogManager.writeLogInfo("AutoSync Scheduled with time : "+autoSyncTime);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        LogManager.writeLogError("Schedule Sync default time: "+15);
                        intervalMillis = 1000 * 60 *30;
                    }
                    AlarmManager alarm = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                    // See https://developer.android.com/training/scheduling/alarms.html
                    alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis+intervalMillis, intervalMillis, scheduleSyncAlarmPendingIntent);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("SOAUTOSYNC", autoSyncTime);
                    editor.apply();
//                }
                }
            }else{
                LogManager.writeLogError("Auto Sync not started because date is not valid ");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogManager.writeLogError(Constants.error_txt + e.getMessage());
        }
    }

    public synchronized static boolean isPinging(){
        try {
            return isReachable();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    private static boolean isReachable() throws IOException {
        final ReentrantLock[] reentrantLock = {null};
        final Socket[] sock = {null};
        final boolean[] isError = new boolean[1];
        try {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (reentrantLock[0] == null){
                            reentrantLock[0] = new ReentrantLock();
                        }
                        reentrantLock[0].lock();
                        int timeoutMs = 1500;
                        sock[0] = new Socket();
                        SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);
                        sock[0].connect(sockaddr, timeoutMs);
                        sock[0].close();
                        isError[0] =true;
                    } catch (IOException e) {
                        e.printStackTrace();
                        isError[0] =false;
                    }finally {
                        assert reentrantLock[0] != null;
                        reentrantLock[0].unlock();
                    }
                }
            });
            thread.start();
            thread.join();
        } catch (Throwable e) {
            isError[0]=false;
        }finally {
            if (sock[0]!=null) {
                if (!sock[0].isClosed()){
                    sock[0].close();
                }
            }
        }
        return isError[0];
    }

    public static CountDownTimer countDownTimer = null;
    public static boolean isTickerCalled=false;
    public static OnTimeTickerListener timeTickerListener;
    public static void checkInternetWithDate(int min,boolean isStart,OnTimeTickerListener listener){
            timeTickerListener = listener;
            if (isStart) {
                if (MSFAApplication.AW_TIME!=null&&!TextUtils.isEmpty(MSFAApplication.AW_TIME)&&!TextUtils.equals(MSFAApplication.AW_TIME,"0")) {
                    int maxCountdown = 10 * 60000;
                    int interval = Integer.parseInt(MSFAApplication.AW_TIME)*60000;
                    countDownTimer = new CountDownTimer(maxCountdown, interval) {
                        public void onTick(long millisUntilFinished) {
                            timeTickerListener.onTimeTick(true);
                        }
                        @Override
                        public void onFinish() {
                            timeTickerListener.onTimeTick(false);
                        }
                    };
                    countDownTimer.start();
                }
            }
    }
    public static void showInternetDateAndTimeDialog(Activity context){
        if (!ConstantsUtils.isAutomaticTimeZone(context)) {
            ConstantsUtils.showAutoDateSetDialog(context);
        }else if (!UtilConstants.isNetworkAvailable(context)){
            showAlert("Internet not enabled",context);
        }else if(UtilConstants.isNetworkAvailable(context)&&!isConnectedToThisServer()){
            showAlert("Mobile data enabled and no active internet connectivity",context);
        }
    }
    private static AlertDialog dialog;
    public static void showAlert(String message, Context context) {
        try {
            if (dialog!=null&&dialog.isShowing()){
                dialog.dismiss();
            }
            AlertDialog.Builder test = new AlertDialog.Builder(context,R.style.DialogTheme);
            test.setCancelable(false);
            test.setMessage(message);
            test.setPositiveButton(context.getString(R.string.msg_ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
            dialog = test.create();
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public interface OnTimeTickerListener{
        void onTimeTick(boolean isTick);
    }
    public static void stopNetworkAndDateCheck(){
        if (countDownTimer != null) {
            LogManager.writeLogInfo("update sync Timer count Down is cancel");
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }
    public static String getCPConfigDate(int numberofDays){
        String currentDate="";
        if (numberofDays!=0) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");// HH:mm:ss");
            currentDate = df.format(c.getTime());

            c.add(Calendar.DAY_OF_MONTH, numberofDays);  // number of days to add
            return df.format(c.getTime());
        }else{
            return currentDate;
        }
    }

    public static void getIdpDetails(Context mContext, AsyncTaskCallBack asyncTaskCallBack) {
        SharedPreferences sharedPerf = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);
        if (!sharedPerf.getBoolean(Constants.isIDPResponseGot, false)) {
            OnlineManager.doOnlineGetRequest(Constants.ConfigTypsetTypeValues+"?$filter=Typeset eq 'SS' and (Types eq 'IDPISHTTPS' or Types eq 'IDPTNT' or Types eq 'IDPURL1'or Types eq 'IDPID'or Types eq 'IDPPWD')", mContext, event -> {
                if (event.getResponseStatusCode()==200) {
                    String isHttp="";
                    String idpTNT="";
                    String idpUrl1="";
                    String idpUser="";
                    String idpPass="";
                    String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                    Log.d("OnlineManager", "configData: " + responseBody + " " + event.getResponseStatusCode());
                    try {
                        JSONObject jsonObject = new JSONObject(responseBody);
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("d"));
                        JSONArray jsonArray = jsonObject1.optJSONArray("results");
                        if (jsonArray.length()>0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object1 = jsonArray.getJSONObject(i);
                                ConfigTypsetTypeValuesBean configTypes = new ConfigTypsetTypeValuesBean();
                                if (object1.optString("Types").equalsIgnoreCase("IDPISHTTPS")) {
                                    if (object1.optString("TypeValue").equalsIgnoreCase("X")) {
                                        isHttp = "https://";
                                    } else {
                                        isHttp = "http://";
                                    }
                                }
                                if (object1.optString("Types").equalsIgnoreCase("IDPTNT")) {
                                    idpTNT = object1.optString("TypeValue");
                                }
                                if (object1.optString("Types").equalsIgnoreCase("IDPURL1")) {
                                    idpUrl1 = object1.optString("TypeValue");
                                }
                                if (object1.optString("Types").equalsIgnoreCase("IDPID")) {
                                    idpUser = object1.optString("TypeValue");
                                }
                                if (object1.optString("Types").equalsIgnoreCase("IDPPWD")) {
                                    idpPass = object1.optString("TypeValue");
                                }
                            }
                            String idpUrl = isHttp + idpTNT + "." + idpUrl1 + "/";
                            SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("IDPURL", idpUrl);
                            editor.putString("IDPUserId", idpUser);
                            editor.putString("IDPPass", idpPass);
                            editor.putBoolean(Constants.isIDPResponseGot, true);
                            editor.commit();
                            asyncTaskCallBack.onStatus(true, "");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        LogManager.writeLogError(Constants.error_txt + " : " + e.getMessage());
                        asyncTaskCallBack.onStatus(false, e.getMessage());
                    }
                }else if(event.getResponseStatusCode()==401){
                    try {
                        String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                        asyncTaskCallBack.onStatus(false, responseBody);
                    } catch (Throwable e) {
                        e.printStackTrace();
                        String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                        asyncTaskCallBack.onStatus(false, responseBody);
                    }
                }else {
                    String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                    asyncTaskCallBack.onStatus(false, responseBody);
                }
            }, iError->{
                iError.printStackTrace();
                String errorMessage = "";
                if(iError.getMessage().contains("No address associated with hostname") || iError.getMessage().contains("Software caused connection")|| iError.getMessage().contains("Failed to connect")|| iError.getMessage().contains("during system call")){
                    errorMessage = "Unable to fetch user profiles data due to poor internet connectivity[UserProfiles]. \n\nPlease check internet and retry";
                }
                Log.d("OnlineManager error", "getUserRollInfo: ");
                LogManager.writeLogError("OnlineManager error userprofiles : "+  iError.getMessage());
                if(!TextUtils.isEmpty(errorMessage)){
                    asyncTaskCallBack.onStatus(false, errorMessage);
                }else {
                    asyncTaskCallBack.onStatus(false, iError.getMessage());
                }
            });

        } else {
            asyncTaskCallBack.onStatus(true,"");
        }
    }
    public static long getMilliFromDate(String dateFormat) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00");
        try {
            date = formatter.parse(dateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("Today is " + date);
        return date.getTime();
    }
    public static String geterrormessageForInternetlost(String error, Context context){
        String errormessage="";
        if(error.contains("No address associated with hostname"))
        {
            errormessage = context.getString(R.string.data_conn_lost_during_sync);

        }else if(error.contains("Network is unreachable")) {
            errormessage = context.getString(R.string.data_conn_lost_during_sync);
        }else if(error.contains("Software caused connection")) {
            errormessage = context.getString(R.string.data_conn_lost_during_sync);
        }

        return errormessage;
    }

    public static String convertToFormat(String dateVal,String format) {
        Date date = null;
        String ackwardRipOff = dateVal.replace("/Date(", "").replace(")/", "");
        Long timeInMillis = Long.valueOf(ackwardRipOff);
        date = new Date(timeInMillis);
        return new SimpleDateFormat(format).format(date).toString();
    }

    public static String getCurrentDateTime(){
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyy");
        String currentDate = dateFormatter.format(c.getTime());
        SimpleDateFormat time_format = new SimpleDateFormat("HH:mm");
        String timeComp = time_format.format(Calendar.getInstance().getTime());
        return currentDate+" "+timeComp;
    }
    public static String convertGregorianDateFormat1(String dateVal) {
        Date date = null;
        String ackwardRipOff = dateVal.replace("/Date(", "").replace(")/", "");
        Long timeInMillis = Long.valueOf(ackwardRipOff);
        date = new Date(timeInMillis);
        return new SimpleDateFormat("dd/MM/yyyy").format(date).toString();
    }

    public static String convertGregorianDateFormat(String dateVal) {
        Date date = null;
        String ackwardRipOff = dateVal.replace("/Date(", "").replace(")/", "");
        Long timeInMillis = Long.valueOf(ackwardRipOff);
        date = new Date(timeInMillis);

        return new SimpleDateFormat("dd/MMM/yyyy",Locale.ENGLISH).format(date).toString();
    }
    public static String getNoOfDays(int days) {
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -days);
        return simpleDateFormat.format(cal.getTime()) + "T00:00:00";
    }
}
