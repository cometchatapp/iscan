package com.arteriatech.ss.msec.iscan.v4.mutils.common;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.mutils.actionbar.ActionBarView;
import com.arteriatech.ss.msec.iscan.v4.mutils.datavault.UtilDataVault;
import com.arteriatech.ss.msec.iscan.v4.mutils.gps.GpsTracker;
import com.arteriatech.ss.msec.iscan.v4.mutils.interfaces.DialogCallBack;
import com.arteriatech.ss.msec.iscan.v4.mutils.interfaces.PasswordDialogCallbackInterface;
import com.arteriatech.ss.msec.iscan.v4.mutils.interfaces.RegistrationCallBack;
import com.arteriatech.ss.msec.iscan.v4.mutils.location.LocationUtils;
import com.arteriatech.ss.msec.iscan.v4.mutils.registration.RegistrationModel;
import com.arteriatech.ss.msec.iscan.v4.mutils.registration.UtilRegistrationActivity;
import com.arteriatech.ss.msec.iscan.v4.mutils.sync.SyncHistoryDB;
import com.arteriatech.ss.msec.iscan.v4.mutils.sync.SyncHistoryModel;
import com.arteriatech.ss.msec.iscan.v4.ui.DialogFactory;
import com.arteriatech.ss.msec.iscan.v4.ui.OnDialogClick;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.sap.smp.client.odata.ODataDuration;
import com.sap.smp.client.odata.impl.ODataDurationDefaultImpl;
import com.sap.smp.client.odata.metadata.ODataMetadata;
import com.sap.smp.client.odata.offline.ODataOfflineStore;
import com.sap.smp.client.odata.offline.ODataOfflineStoreOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by e10742 on 12-10-2016.
 */
public class UtilConstants {
    /*common error code*/
    public static final String ERROR_CODE_NETWORK = "A3000";//no internet
    public static final String ERROR_CODE_UI_2000 = "A2000";//Please enter mandatory fields
    public static final String ERROR_CODE_UI_2001 = "A2001";//Please enter valid user name
    public static final String ERROR_CODE_UI_2002 = "A2002";//Required field cannot contain spaces.
    public static final String ERROR_CODE_UNKNOWN = "A5000";//Unknown error, Please contact channel team
    public static final String ERROR_CODE_REGISTRATION_USER_LOCKED = "A1000";//User locked due to wrong input
    public static final int Build_Database_Failed_Error_Code1 = -100036;
    public static final int Build_Database_Failed_Error_Code2 = -100097;
    public static final int Build_Database_Failed_Error_Code3 = -10214;
    public static final String Executing_SQL_Commnd_Error = "10001";
    public static final int Execu_SQL_Error_Code = -10001;
    public static final int Store_Def_Not_matched_Code = -10247;
    public static final String Store_Defining_Req_Not_Matched = "10247";
    public static final String Invalid_Store_Option_Value = "InvalidStoreOptionValue";
    /*bundle*/
    public static final String BUNDLE_READ_FROM_TECHNICAL_CACHE = "readFromTechnicalCacheBundle";
    public static final String MONTHS[] = {"Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    public static final String MONTHS_NUMBER[] = {"01", "02", "03", "04",
            "05", "06", "07", "08", "09", "10", "11", "12"};
    public static final String PREFS_NAME = "mSFAPreference";
    public static final int ERROR_URL_NOT_PERMITTED = 22;
    public static final int ERROR_NO_SERVER_RESPONCE = -1;
    public static final int ERROR_IMO_LIB_NOT_FOUND = -3;
    public static final int ERROR_UNKNOWN_ERROR = 70003;
    public static final int ERROR_NO_REG_LIS = 70004;
    public static final int ERROR_NO_CAPTCH = 70009;
    public static final int ERROR_HTTP = 80001;
    public static final int ERROR_PARSER = 80002;
    public static final int ERROR_HOST_UN_REACHABLE = 80003;
    public static final int ERROR_USER_AUTH = 80004;
    public static final int ERROR_BAD_REQUEST = 400;
    public static final int ERROR_UN_AUTH = 401;
    public static final int ERROR_FORBIDDEN = 403;
    public static final int ERROR_SERVER_NOT_FOUND = 404;
    public static final String error_during_offline_close = "Error during store close: ";
    public static final String offline_store_not_closed = "Offline store not closed: ";
    public static final String isReIntilizeDB = "isReIntilizeDB";
    /*permission Request*/
    public static final int CAMERA_PERMISSION_CONSTANT = 890;
    public static final int Location_PERMISSION_CONSTANT = 700;
    public static final String error_txt_location = "Location :";
    public static double latitude = 0, longitude = 0;
    public static String SALS_TAR_FROMPER = "";
    public static String SALS_TAR_TOPER = "";
    public static String COLS_TAR_FROMPER = "";
    public static String COLS_TAR_TOPER = "";
    public static String DLR_OFT_FROMPER = "";
    public static String DLR_OFT_TOPER = "";
    public static String Comm_error_name = "Communication error";
    public static String CURYEAR = "";
    public static String CURMONTH = "";
    public static String TOYEAR = "";
    public static String TOMONTH = "";
    public static String MONTH = "";
    public static String YEAR = "";
    public static String RegIntentKey = "RegKey";
    public static String Permission_Error = "Permission error";
    private static int HOUR_PM = 0;
    private static int ZERO_MINUTES = 0;

    /*file mine type*/
    public static final String XLS_MINE_TYPE = "application/vnd.ms-excel";
    public static final String XLS_MINE_TYPE_1 = "application/msexcel";
    public static final String DOC_MINE_TYPE = "application/msword";
    public static final String JPG_MINE_TYPE = "image/jpeg";
    public static final String TXT_MINE_TYPE = "text/plain";
    public static final String TXT_MINE_TYPE_1 = "plain/text";
    public static final String POWERPOINT_MINE_TYPE = "application/vnd.ms-powerpoint";
    public static final String PDF_MINE_TYPE = "application/pdf";

    public static final int STORAGE_PERMISSION_CONSTANT = 890;

    public static boolean getLocation(Context context) {
        GpsTracker gps;
        boolean validLocation = false;
        gps = new GpsTracker(context);
        // check if GPS enabled
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }

        if (latitude == 0 || longitude == 0) {
            showAlert("Unable to get location. Try again", context);
            return false;
        } else return true;
    }

    public static boolean isGPSEnabled(Context context) {
        // Declaring a Location Manager
        LocationManager locationManager;
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        // getting GPS status
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isGPSEnabled;
    }

    public static void showAlert(String message, Context context) {
        try {
            new DialogFactory.Alert(context).setMessage(message).isAlert(true)
                    .setTheme(R.style.MyDialogTheme_new).setPositiveButton(context.getString(R.string.msg_ok))
                    .setOnDialogClick(isPositive -> {
                        if (isPositive) {
                        }
                    })
                    .show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showAlertDialog(String message, Context context) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.UtilsDialogTheme);
            builder.setMessage(message).setCancelable(false)
                    .setPositiveButton(context.getString(R.string.msg_ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            ((Activity)context).finish();
                        }
                    });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String convertGregorianDateFormat(String dateVal) {
        Date date = null;
        String ackwardRipOff = dateVal.replace("/Date(", "").replace(")/", "");
        Long timeInMillis = Long.valueOf(ackwardRipOff);
        date = new Date(timeInMillis);

        return new SimpleDateFormat("dd/MM/yyyy").format(date).toString();
    }

    public static void showAlertYesNo(String message, Context context) {
        try {
            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.UtilsDialogTheme);
                    builder.setMessage(message).setCancelable(false)
                            .setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    ((Activity)context).finish();
                                }
                            }).setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void displayAlertWithBackPressed(final Activity activity, String message, Context context) {
        try {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.UtilsDialogTheme);
            alertDialog.setMessage(message);
            alertDialog.setCancelable(false);
            alertDialog.setPositiveButton(activity.getString(R.string.msg_ok), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try {
                        dialog.cancel();
                        activity.onBackPressed();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//	String extract = input.replaceAll("[^a-zA-Z]+", "");
    /*if (word.length() == 3) {
        return word;
	} else if (word.length() > 3) {
		return word.substring(word.length() - 3);
	} else {
		// whatever is appropriate in this case
		throw new IllegalArgumentException("word has less than 3 characters!");
	}*/

    /**
     * This Method find mobile network available or not
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    public static String getCurrentDate() {
        String currentDateTimeString1 = (String) android.text.format.DateFormat
                .format("yyyy-MM-dd", new Date());
        String currentDateTimeString = currentDateTimeString1;
        return getCurrentDateInHyphenFormatDDMMYYYY(currentDateTimeString);
    }

    public static String getCurrentDateInHyphenFormatDDMMYYYY(String dateStr) {
        String datefrt = "";
        if (dateStr != null) {
            String date = dateStr.substring(8, 10);
            int mnt = Integer.parseInt(dateStr.substring(5, 7));
            String yr = dateStr.substring(0, 4);
            datefrt = date + "-" + MONTHS_NUMBER[mnt - 1] + "-" + yr;
        }
        return datefrt;
    }

    public static void onNoNetwork(Context ctx) {
        try {
//        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx, R.style.UtilsDialogTheme);
            builder.setMessage(
                    ctx.getString(R.string.msg_no_network))
                    .setCancelable(false)
                    .setPositiveButton(ctx.getString(R.string.msg_ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getNewDateFormat(String dateStr) {
        String datefrt = "";
        if (dateStr != null && !dateStr.equalsIgnoreCase("")) {
            String date = dateStr.substring(8, 10);
            int mnt = Integer.parseInt(dateStr.substring(5, 7));
            String yr = dateStr.substring(0, 4);
            datefrt = date + "/" + MONTHS_NUMBER[mnt - 1] + "/" + yr;
        }
        return datefrt;
    }
    public static String getLastDay() {
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        return simpleDateFormat.format(cal.getTime()) + "T00:00:00";
    }

    public static String getLastTwoDay() {
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -2);
        return simpleDateFormat.format(cal.getTime()) + "T00:00:00";
    }

    public static String getDate1() {
        String currentDateTimeString1 = (String) android.text.format.DateFormat
                .format("yyyy-MM-dd", new Date());
        String currentDateTimeString = currentDateTimeString1;
        return getNewDateFormat(currentDateTimeString);
    }

    public static boolean isValidEmailAddress(String emailAddress) {
        String emailRegEx;
        Pattern pattern;
        // Regex for a valid email address
        emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";
       // emailRegEx= "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Za-z]{2,6}$";
        // Compare the regex with the email address
        pattern = Pattern.compile(emailRegEx);
        Matcher matcher = pattern.matcher(emailAddress);
        if (!matcher.find()) {
            return false;
        }
        return true;
    }

    public static String getNextVisitDay(String dateStr) {
        String date = "";
        if (dateStr != null && !dateStr.equalsIgnoreCase("")) {
            date = dateStr.substring(8, 10);
        }
        return date;
    }

    public static int getNextVisitMonth(String dateStr) {
        int mnt = 0;
        if (dateStr != null && !dateStr.equalsIgnoreCase("")) {
            mnt = Integer.parseInt(dateStr.substring(5, 7));
            //datefrt = date + "-" + MONTHS[mnt - 1] + "-" + yr;
        }
        return mnt;
    }

    public static String getNextVisitear(String dateStr) {
        String yr = "";
        if (dateStr != null && !dateStr.equalsIgnoreCase("")) {
            yr = dateStr.substring(0, 4);
        }
        return yr;
    }

    public static String addZerosBeforeNumber(String strOriginalNumber, int intLength) {
        String strAfterAddZeros = "";
        if (!strOriginalNumber.equalsIgnoreCase("")) {
            strAfterAddZeros = String.format("%0" + (intLength - strOriginalNumber.length()) + "d%s", 0, strOriginalNumber);
        }


        return strAfterAddZeros;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static Date getDate(String strDate) {
        String d = strDate.substring(0, 2);
        String m = strDate.substring(3, 5);
        String y = strDate.substring(6, 10);
        String nStrDate = y + "/" + m + "/" + d;
        long dt1 = new Date(nStrDate).getTime();
        return new Date(dt1);
    }

    public static String getDateFormatDMY(String strDate) {
        String nStrDate = "";
        if (!strDate.equalsIgnoreCase("")) {
            strDate = strDate.substring(0, 10);
            String d = strDate.substring(8, 10);
            String m = strDate.substring(5, 7);
            String y = strDate.substring(0, 4);
            nStrDate = d + "-" + m + "-" + y;
        }
        return nStrDate;
    }

    public static boolean compareDate(String strDlrDate) {
        boolean isTodayDate = false;
        try {
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            int mnt = mMonth + 1;
            String mon = "", day = "";
            if (mnt < 10)
                mon = "0" + mnt;
            else
                mon = "" + mnt;
            day = "" + mDay;
            if (mDay < 10)
                day = "0" + mDay;
            String currDay = "" + mDay;
            if (mDay < 10)
                currDay = "0" + (mDay + 1);
            if (!strDlrDate.equalsIgnoreCase("")) {
                String[] splitDlrDOB = strDlrDate.split("T");

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date currDate = sdf.parse(mYear + "-" + mon + "-" + currDay);
                Date dealerDate = sdf.parse(splitDlrDOB[0]);
                Calendar cal1 = Calendar.getInstance();
                Calendar cal2 = Calendar.getInstance();
                cal1.setTime(currDate);
                cal2.setTime(dealerDate);

                if ((cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)) && (cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH))) {
                    isTodayDate = true;
                    System.out.println("Date1 is equal Date2");
                }
            } else {
                isTodayDate = false;
            }


        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return isTodayDate;
    }

    public static String[][] getMonthValues(int noOfMons) {
        String[][] arrayTypeMonthValues = new String[2][noOfMons];

        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat month_date = new SimpleDateFormat("MMM");


            for (int i = 0; i < noOfMons; i++) {

                String month_name = month_date.format(cal.getTime());
                String curr_month_No = "";

                int year = cal.get(Calendar.YEAR);
                int intFromMnt = cal.get(Calendar.MONTH) + 1;
                if (intFromMnt < 10)
                    curr_month_No = "0" + intFromMnt;
                else
                    curr_month_No = "" + intFromMnt;


                arrayTypeMonthValues[0][i] = year + "-" + curr_month_No;
                arrayTypeMonthValues[1][i] = month_name.toUpperCase() + "-" + year;

                cal.add(Calendar.MONTH, -1); // ----->Get Previous Month

            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return arrayTypeMonthValues;
    }

    public static String[][] getOneweekValues(int noOfMons) {
        String[][] oneWeekDay = new String[2][noOfMons];

        try {
            Calendar cal = Calendar.getInstance();
            int mYear, mMonth, mDay;
            String day = "", mon = "";
            for (int i = 0; i < noOfMons; i++) {
                mYear = cal.get(Calendar.YEAR);
                mMonth = cal.get(Calendar.MONTH);
                mDay = cal.get(Calendar.DAY_OF_MONTH);
                mMonth = mMonth + 1;
                if (mMonth < 10)
                    mon = "0" + mMonth;
                else
                    mon = "" + mMonth;
                day = "" + mDay;
                if (mDay < 10)
                    day = "0" + mDay;

                oneWeekDay[0][i] = mon + "-" + day;
                oneWeekDay[1][i] = day + "-" + mon + "-" + mYear;
                cal.add(Calendar.DAY_OF_YEAR, 1); // ----->Get next day
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return oneWeekDay;
    }

    public static String getTodayFormat() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String today = df.format(c.getTime());
        return today;
    }

    public static String getLogName(String usrName, String file) {
        String currentDate = (String) android.text.format.DateFormat.format(
                "yyyy-MM-dd", new Date());
        Date date1 = new Date();
        date1.setHours(date1.getHours());
        SimpleDateFormat simpDate = new SimpleDateFormat("HHmm");
        String time = simpDate.format(date1);
        String fileName = usrName + "_" + currentDate.replace("-", "") + time
                + "_" + file;
        return fileName;

    }

    public static String insertCommas(String number) {
        String value = "";
        if (number.contains("."))
            value = number.substring(0, number.indexOf("."));
        else
            value = number;
        return insertCommas1(new BigDecimal(value));
    }

    public static String insertCommas1(BigDecimal number) {
        // for your case use this pattern -> #,##0.00
        DecimalFormat df = new DecimalFormat("#,##,##0");
        return df.format(number);
    }

    public static String getTimeInSecondsFormat() {

        String currentDateTimeString1 = (String) android.text.format.DateFormat
                .format("yyyy-MM-dd", new Date());
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS",
                Locale.US);
        String currentDateTimeString2 = dateFormat.format(new Date());

        String currentDateTimeString = currentDateTimeString1 + "T"
                + currentDateTimeString2;

        return getDateFormat(currentDateTimeString) + ","
                + currentDateTimeString2;
    }

    public static String convertDouble(String val) {
        BigDecimal bdTest = new BigDecimal(val);
        bdTest = bdTest.setScale(2, BigDecimal.ROUND_HALF_UP);
        return String.valueOf(bdTest);
    }

    public static String getSyncHistoryTime() {
        String currentDateTimeString1 = (String) android.text.format.DateFormat
                .format("yyyy-MM-dd", new Date());

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss",
                Locale.US);
        String currentDateTimeString2 = dateFormat.format(new Date());

        String currentDateTimeString = currentDateTimeString1 + "T"
                + currentDateTimeString2;

        return getDateFormat(currentDateTimeString) + ", "
                + currentDateTimeString2;
    }

    public static String removeDecimalPoints(String number) {
        // for your case use this pattern -> #,##0.00
        String s = "";
        if (number != null && !number.equalsIgnoreCase("")) {
            s = number;
            s = s.replaceAll("^0*([0-9]+).*", "$1");
        } else {
            s = "0";
        }
        return s;

    }

    public static String dateFormat(String val) {
        String start_dt = val;
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-DD");
        Date date = null;
        try {
            date = (Date) formatter.parse(start_dt);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy");
        String finalString = newFormat.format(date);
        return finalString;
    }

    public static String addZerosBeforeValue(BigInteger num, int digits) {
        String output = num.toString();
        while (output.length() < digits) output = "0" + output;
        return output;
    }

    public static int getDigitCount(BigInteger number) {
        double factor = Math.log(2) / Math.log(10);
        int digitCount = (int) (factor * number.bitLength() + 1);
        if (BigInteger.TEN.pow(digitCount - 1).compareTo(number) > 0) {
            return digitCount - 1;
        }
        return digitCount;
    }

    public static String removeAlphanumeric(String mStrAfterRemoveText) {
        if (mStrAfterRemoveText != null && !mStrAfterRemoveText.equalsIgnoreCase("")) {
            mStrAfterRemoveText = mStrAfterRemoveText.replaceAll("[^\\d.]", "");
        }

        return removeLeadingZeros(mStrAfterRemoveText);
    }

    public static String removeAlphanumericText(String mStrAfterRemoveText, int prefixLen) {
        /*if (mStrAfterRemoveText != null && !mStrAfterRemoveText.equalsIgnoreCase("")) {
            mStrAfterRemoveText=mStrAfterRemoveText.replaceAll("[^\\d.]", "");
		}*/
        try {
            String[] splitString;
            String mStrPrefixStr = "";
            int numberLengthWithLeadingZeroes;
            if (prefixLen > 0) {
                //			mStrPrefixStr ?= mStrAfterRemoveText.substring(0, prefixLen);
                mStrAfterRemoveText = mStrAfterRemoveText.substring(prefixLen, mStrAfterRemoveText.length());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return removeLeadingZeros(mStrAfterRemoveText);
    }

    public static String rearrangeSerialNoText(String mStrAfterRemoveText, int prefixLen) {
        String reArrStr = "";
        try {
            if (mStrAfterRemoveText != null && !mStrAfterRemoveText.equalsIgnoreCase("")) {
                String numericText = "";

                String startSerialNoStr = mStrAfterRemoveText.substring(0, 2);

                if (mStrAfterRemoveText.length() == 5) {
                    numericText = removeAlphanumericText(mStrAfterRemoveText, prefixLen);
                } else if (mStrAfterRemoveText.length() > 5) {
                    numericText = mStrAfterRemoveText.substring(mStrAfterRemoveText.length() - 5);
                } else {
                    numericText = mStrAfterRemoveText;
                }


                reArrStr = startSerialNoStr + "xxxx" + numericText;


            }
        } catch (Exception e) {
            reArrStr = "";
            e.printStackTrace();
        }
        return reArrStr;
    }

    public static String removeLeadingZeros(String str) {
        if (str != null) {
            while (str.startsWith("0")) {
                str = str.substring(1);
            }
        }
        return str;
    }

    public static String getNewDateFormatAsdd_mmm_yyyy(String dateStr) {
        String datefrt = "";
        if (dateStr != null && !dateStr.equalsIgnoreCase("")) {
            String date = dateStr.substring(8, 10);
            int mnt = Integer.parseInt(dateStr.substring(5, 7));
            String yr = dateStr.substring(0, 4);
            datefrt = date + "-" + MONTHS[mnt - 1] + "-" + yr;
        }
        return datefrt;
    }

    public static String removeLeadingZero(String value) {
        String textReturn = "";
        try {
            if (!value.equalsIgnoreCase("") && value != null) {
                textReturn = removeLeadingZero1(new BigDecimal(value));

            } else {
                textReturn = "0.00";
            }
        } catch (Exception e) {
            textReturn = "0.00";
            e.printStackTrace();
        }
        return textReturn;
    }

    public static String removeLeadingZeroQuantity(String value) {
        try {
            if (!value.equalsIgnoreCase("") && value != null) {
                return removeLeadingZeroQuantityConvert(new BigDecimal(value));

            } else {
                return "0.000";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "0.000";
        }
    }

    public static String removeLeadingZeroQuantityConvert(BigDecimal number) {
        // for your case use this pattern -> #,##0.00
        DecimalFormat df = new DecimalFormat("#####0.000");
        return df.format(number);
    }

    public static String removeLeadingZeroVal(String value) {

        if (!value.equalsIgnoreCase("") && value != null) {
            return removeLeadingZero(new BigDecimal(value));

        } else {
            return "0";
        }
    }

    public static String removeLeadingZero(BigDecimal number) {
        // for your case use this pattern -> #,##0.00
        DecimalFormat df = new DecimalFormat("#####0");
        return df.format(number);
    }

    public static String removeLeadingZero(Double number) {
        // for your case use this pattern -> # ,Ex : 10.00 to 10
        String df = "";
        try {
            df = new DecimalFormat("#").format(number);
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            df = "0";
            e.printStackTrace();
        }
        return df;
    }


	/*public static String getDateTimeFromat(String date, String time) {
		String datefrt = "";
//		datefrt = "00:00:00";
		datefrt = time.substring(11, 13) + ":" + time.substring(14, 16)
				+ ":" + time.substring(17, 19) ;
		String currentDateTimeString = date + "T" + datefrt;
		return currentDateTimeString;
	}*/

    public static String removeLeadingZero1(BigDecimal number) {
        // for your case use this pattern -> #,##0.00
        DecimalFormat df = new DecimalFormat("#####0.00");
        return df.format(number);
    }

    public static String removeLeadingZerowithTwoDecimal(String value) {
        try {
            if (value != null && !value.equalsIgnoreCase("")) {
                return passingBigDecimalWithTwoDecimal(new BigDecimal(value));

            } else {
                return "0.00";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "0.00";
        }
    }

    public static String passingBigDecimalWithTwoDecimal(BigDecimal number) {
        // for your case use this pattern -> #,##0.00
        DecimalFormat df = new DecimalFormat("#,##,##0.00");
        return df.format(number);
    }

    public static String getDateFormat(String dateStr) {
        String datefrt = "";
        if (dateStr != null) {
            String date = dateStr.substring(8, 10);
            int mnt = Integer.parseInt(dateStr.substring(5, 7));
            String yr = dateStr.substring(0, 4);

            datefrt = MONTHS[mnt - 1] + " " + date + ", " + yr;
        }
        return datefrt;
    }

    /*public static String convertTimeOnly(String mStrTime) {
        String timeValue="";

        if(mStrTime!=null && !mStrTime.equalsIgnoreCase("")) {

            if(mStrTime.contains("H")){
                String[] hour = mStrTime.split("PT");

                String[] hour1 = hour[1].split("H");

                String[] min = hour1[1].split("M");

                String[] sec = min[1].split("S");

                timeValue = hour1[0] + ":" + min[0] + ":" + sec[0];
            }else {
                String[] hour = mStrTime.split("PT");

//				String[] hour1 = hour[1].split("H");

                String[] min = hour[1].split("M");

                String[] sec = min[1].split("S");

                timeValue = 00 + ":" + min[0] + ":" + sec[0];
            }


        }

        return  timeValue;
    }*/

    public static String getTime() {
        String currentDateTimeString1 = (String) android.text.format.DateFormat
                .format("yyyy-MM-dd", new Date());

        String currentDateTimeString2 = (String) android.text.format.DateFormat
                .format("hh:mm:ss", new Date());
        String currentDateTimeString = currentDateTimeString1 + "T"
                + currentDateTimeString2;

        return getDateFormat(currentDateTimeString) + ","
                + currentDateTimeString2;
    }

    public static String convertCalenderToStringFormat(GregorianCalendar calendar) {
        String dateFormatted = "";
        if (calendar != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            simpleDateFormat.setCalendar(calendar);
            dateFormatted = simpleDateFormat.format(calendar.getTime());
        }
        return dateFormatted;
    }

    public static String convertGregorianCalendarToYYYYMMDDFormat(GregorianCalendar calendar) {
        String dateFormatted = "";
        if (calendar != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            simpleDateFormat.setCalendar(calendar);
            dateFormatted = simpleDateFormat.format(calendar.getTime());
        }
        return dateFormatted;
    }

    public static String getConvetDDMM(String date) {
        String formatDate = "";
        if (!date.equalsIgnoreCase("")) {
            String day = date.substring(8, 10);
            String mon = date.substring(5, 7);
            formatDate = day + " " + MONTHS[Integer.parseInt(mon) - 1];
        }
        return formatDate;
    }

    public static String getconvertedDateFormat(String dateStr) {
        String datefrt = "";
        if (dateStr != null) {
            String date = dateStr.substring(8, 10);
            int mnt = Integer.parseInt(dateStr.substring(5, 7));
            String yr = dateStr.substring(0, 4);
            datefrt = MONTHS[mnt - 1] + " " + date + ", " + yr;
        }
        return datefrt;
    }

    public static String getDateform(String dateStr) {
        String datefrt = "";
        if (dateStr != null) {
            datefrt = dateStr.substring(0, 10);
        }
        return datefrt;
    }

    public static String getCurrentDayFormatYYMMYYYY(String dateStr) {
        String datefrt = "";
        if (dateStr != null) {
            String date = dateStr.substring(8, 10);
            int mnt = Integer.parseInt(dateStr.substring(5, 7));
//			String mnt = dateStr.substring(5, 7);
            String yr = dateStr.substring(0, 4);
            datefrt = date + "-" + MONTHS_NUMBER[mnt - 1] + "-" + yr;
        }
        return datefrt;
    }

    public static String getNewDateFormatYYMMYYYY(String dateStr) {
        String datefrt = "";
        if (dateStr != null) {
            String date = dateStr.substring(8, 10);
//			int mnt = Integer.parseInt(dateStr.substring(5, 7));
            String mnt = dateStr.substring(5, 7);
            String yr = dateStr.substring(0, 4);
            datefrt = date + "." + mnt + "." + yr;
        }
        return datefrt;
    }

    public static String getNewDateFormatwithoutyear(String dateStr) {
        String datefrt = "";
        if (dateStr != null) {
            String date = dateStr.substring(8, 10);
            int mnt = Integer.parseInt(dateStr.substring(5, 7));
            String yr = dateStr.substring(0, 4);
            datefrt = date + " " + MONTHS[mnt - 1];
        }
        return datefrt;
    }

    public static String getMonthFormat(String dateStr) {
        String datefrt = "";
        if (dateStr != null) {
            // String date = dateStr.substring(8, 10);
            int mnt = Integer.parseInt(dateStr.substring(5, 7));
            String yr = dateStr.substring(0, 4);
            datefrt = MONTHS[mnt - 1] + " " + yr;
        }
        return datefrt;
    }

    public static String getNewMonthFormat(String dateStr) {
        String datefrt = "";
        if (dateStr != null) {
            // String date = dateStr.substring(8, 10);
            int mnt = Integer.parseInt(dateStr.substring(5, 7));
            String yr = dateStr.substring(0, 4);
            datefrt = MONTHS[mnt - 1] + " " + yr;
        }
        return datefrt;
    }

    public static String getNewTimeFormat(String time) {
        String datefrt = "";
        if (time != null) {
            datefrt = "PT" + time.substring(0, 2) + "H" + time.substring(3, 5)
                    + "M" + time.substring(6, 8) + "S";
        }
        return datefrt;
    }

    public static String getNewTimeFormatRev(String time) {
        String datefrt = "";
        if (time != null) {
            datefrt = time.substring(2, 4) + ":" + time.substring(5, 7) + ":00";
        }
        return datefrt;
    }

    public static String getDateFormat2(String dateStr) {
        String datefrt = null;
        String date = dateStr.substring(8, 9);
        int mnt = Integer.parseInt(dateStr.substring(5, 7));
        String yr = dateStr.substring(0, 4);
        datefrt = MONTHS[mnt - 1] + " " + date + ", " + yr;
        return datefrt;
    }

    public static String getReArrangeDateFormat(String dateStr) {
        String datefrt = "";
        if (dateStr != null && !dateStr.equalsIgnoreCase("")) {
            String date = dateStr.substring(8, 10);
            String month = dateStr.substring(5, 7);
            int mnt = Integer.parseInt(dateStr.substring(5, 7));
            String yr = dateStr.substring(0, 4);

            datefrt = yr + "-" + month + "-" + date;
        }
        return datefrt;
    }

    public static String getNewTime() {
        String currentDateTimeString1 = (String) android.text.format.DateFormat
                .format("yyyy-MM-dd", new Date());
        String currentDateTimeString2 = (String) android.text.format.DateFormat
                .format("hh:mm:ss", new Date());
        String currentDateTimeString = currentDateTimeString1 + "T"
                + currentDateTimeString2;
        return currentDateTimeString;
    }

    public static String getCurrentTimeTest() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss",
                Locale.US);
        String currentDateTimeString2 = dateFormat.format(new Date());
        return currentDateTimeString2;
    }

    public static String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss",
                Locale.US);
        String currentDateTimeString2 = dateFormat.format(new Date());
        // return getNewTimeFormat1(currentDateTimeString2);
        return getTimeformatMain(currentDateTimeString2);
    }

    public static String getDateDDMMYYYY() {
        String currentDateTimeString1 = (String) android.text.format.DateFormat
                .format("yyyy-MM-dd", new Date());
        String currentDateTimeString = currentDateTimeString1;
        return getNewDateFormatYYMMYYYY(currentDateTimeString);
    }

    public static String getFormattedDate() {
        String currentDate = (String) android.text.format.DateFormat.format(
                "yyyy-MM-dd", new Date());
        String formattedDate = currentDate + "T00:00:00";
        return formattedDate;
    }

    public static String getMonthYear() {
        String currentDateTimeString1 = (String) android.text.format.DateFormat
                .format("yyyy-MM", new Date());
        String currentDateTimeString = currentDateTimeString1;
        return getNewMonthFormat(currentDateTimeString);
    }

    public static String getTime1() {
        String currentDateTimeString1 = (String) android.text.format.DateFormat
                .format("yyyy-MM-dd", new Date());
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss.SSS",
                Locale.US);
        String currentDateTimeString2 = dateFormat.format(new Date());
        String currentDateTimeString = currentDateTimeString1 + "T"
                + currentDateTimeString2;
        return getDateFormat(currentDateTimeString) + ","
                + currentDateTimeString2;
    }

    public static String getDateTime() {
        String currentDateTime = (String) android.text.format.DateFormat
                .format("MMM dd, yyyy hh:mm:ss", new Date());
        return currentDateTime;
    }

    public static String getTimeformat(String date) {
        String currentDateTimeString2 = (String) android.text.format.DateFormat
                .format("hh:mm:ss", new Date());
        String currentDateTimeString = date + "T" + currentDateTimeString2;
        return currentDateTimeString;
    }

    public static String getTimeformat1(String date, String time) {
        String datefrt = "";
        // if(time!= null){
        // datefrt=time.substring(2,4)+":"+time.substring(5,7)+":00";
        // }else{
        datefrt = (String) android.text.format.DateFormat.format("hh:mm:ss",
                new Date());
        // }
        String currentDateTimeString = date + "T" + datefrt;
        return currentDateTimeString;
    }

    public static String getNewTimeFormat1(String time) {
        String datefrt = "";
        if (time != null) {
            datefrt = "PT" + time.substring(0, 2) + "H" + time.substring(3, 5)
                    + "M00S";
        }
        return datefrt;
    }

    public static String getTimeformatMain(String time) {
        String datefrt = "";
        String currDate = new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss")
                .format(new Date());
        datefrt = currDate.substring(11, 13) + "H" + currDate.substring(14, 16)
                + "M" + currDate.substring(17, 19) + "S";
        String currentDateTimeString = "PT" + datefrt;
        return currentDateTimeString;
    }

    public static String getTimeformat2(String date, String time) {
        String datefrt = "";
        datefrt = "00:00:00";
        String currentDateTimeString = date + "T" + datefrt;
        return currentDateTimeString;
    }

    public static String[] removeDuplicateNames(String[] originalArray) {
        String[] result = null;

        if (originalArray.length > 0 && originalArray != null) {

            List<String> list = Arrays.asList(originalArray);
            Set<String> set = new HashSet<>(list);

            result = new String[set.size()];
            set.toArray(result);
        }

        return result;
    }

    public static Calendar convertDateFormat(String dateVal) {
        Date date = null;
//		String dtStart = "2016-04-12T05:30:00";


        Calendar curCal = new GregorianCalendar();

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format.parse(dateVal);
            curCal.setTime(date);
//            curCal.add(Calendar.HOUR, 5);
//            curCal.add(Calendar.MINUTE,30);
            System.out.println("Date" + curCal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return curCal;
    }

    public static Calendar convertCalenderTimeFormat(String dateVal) {
        Date date = null;
//		String dtStart = "2016-04-12T05:30:00";


        Calendar curCal = new GregorianCalendar();

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format.parse(dateVal);
            curCal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return curCal;
    }

    // logic is changed 13-10-2016 (Some times Minute(M) and Second(S) value coming empty)
    public static String convertTimeOnly(String mStrTime) {
        String timeValue = "";
        if (mStrTime != null && !mStrTime.equalsIgnoreCase("")) {
            if (mStrTime.contains("H")) {
                String[] hour = mStrTime.split("PT");
                String[] hour1 = hour[1].split("H");
                if (mStrTime.contains("M")) {
                    String[] min = hour1[1].split("M");
                    String[] sec = null;
                    try {
                        sec = min[1].split("S");
                    } catch (Exception e) {
                        sec = new String[1];
                        sec[0] = "0";
//      e.printStackTrace();
                    }
                    timeValue = hour1[0] + ":" + min[0] + ":" + sec[0];
                } else {
//     String[] min = hour1[1].split("M");
                    String[] sec = null;
                    try {
                        sec = hour1[1].split("S");
                    } catch (Exception e) {
                        sec = new String[1];
                        sec[0] = "0";
//      e.printStackTrace();
                    }
                    timeValue = hour1[0] + ":" + 00 + ":" + sec[0];
                }
            } else {
                String[] hour = mStrTime.split("PT");
//    String[] hour1 = hour[1].split("H");
                if (mStrTime.contains("M")) {
                    String[] min = hour[1].split("M");
                    String[] sec = null;
                    try {
                        sec = min[1].split("S");
                    } catch (Exception e) {
                        sec = new String[1];
                        sec[0] = "0";
                    }
                    timeValue = 00 + ":" + min[0] + ":" + sec[0];
                } else {
//     String[] min = hour[1].split("M");
                    String[] sec = null;
                    try {
                        sec = hour[1].split("S");
                    } catch (Exception e) {
                        sec = new String[1];
                        sec[0] = "0";
                    }
                    timeValue = 00 + ":" + 00 + ":" + sec[0];
                }
            }
        }
        return timeValue;
    }

    public static Date convertTimeFormat(String timeVal) {
        Date date = null;
//		String dtStart = "PT12H11M13S";

        String[] hour = timeVal.split("PT");

        String[] hour1 = hour[1].split("H");


        String[] min = hour1[1].split("M");

        String[] sec = min[1].split("S");

        String timeValue = hour1[0] + ":" + min[0] + ":" + sec[0];

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        try {
            date = format.parse(timeValue);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date;
    }

    public static String convertTimeFormat1(String timeVal) {
        String timeValue="",hStr="00",mStr="00",secStr="00";
        try {
            if(timeVal.contains("PT")) {
                String[] hour = timeVal.split("PT");
                if (hour[1].contains("H")) {
                    hStr = hour[1].split("H")[0];
                    hour[1] = hour[1].split("H")[1];
                } else {
                    hStr = "00";
                }
                if (hour[1].contains("M")) {
                    mStr = hour[1].split("M")[0];
                    hour[1] = hour[1].split("M")[1];
                } else {
                    mStr = "00";
                }
                if (hour[1].contains("S")) {
                    secStr = hour[1].split("S")[0];
                } else {
                    secStr = "00";
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        timeValue = hStr + ":" + mStr + ":" + secStr;

        return timeValue;
    }

    public static String getConvertCalToStirngFormat(Calendar calVal) {
        String inActiveDate = "";
        if (calVal != null) {
            Date date = calVal.getTime();

            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");

            try {
                inActiveDate = format1.format(date);
                inActiveDate = inActiveDate + "T00:00:00";
                System.out.println(inActiveDate);
            } catch (android.net.ParseException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
        return inActiveDate;
    }

    public static String getTimeFormatOnly() {
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String currentDateTimeString = (String) android.text.format.DateFormat
                .format("HH:mm:ss", new Date());
//		String currentDateTimeString = currentDateTimeString1;
        return currentDateTimeString;
    }

    public static String getNewDateTimeFormat() {
        return (String) android.text.format.DateFormat.format("yyyy-MM-dd'T'HH:mm:ss", new Date());
    }

    public static String getNewDate() {
        String currentDateTimeString1 = (String) android.text.format.DateFormat
                .format("yyyy-MM-dd", new Date());
        String currentDateTimeString2 = (String) android.text.format.DateFormat
                .format("hh:mm:ss", new Date());
        String currentDateTimeString = currentDateTimeString1;
        return getTimeformat2(currentDateTimeString, null);
    }

    public static String getNewDate(String selDate) {
        String currentDateTimeString1 = (String) android.text.format.DateFormat
                .format("yyyy-MM-dd", Long.parseLong(selDate));
        String currentDateTimeString2 = (String) android.text.format.DateFormat
                .format("hh:mm:ss", new Date());
        String currentDateTimeString = currentDateTimeString1;
        return currentDateTimeString;
    }

    public static String getDateFormate(String Date) {
        Date = "20/10/2014";
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String today = formatter.format(Date);
        System.out.println("Today : " + today);
        return Date;
    }

    public static String removeLagingZeros(String str) {
        if (str != null) {
            while (str.endsWith("0")) {
                str = str.substring(1);
            }
        }
        return str;
    }

    public static String getConvetMMMDDYYYY() {
        String currentDate = (String) android.text.format.DateFormat.format(
                "MMM dd, yyyy", new Date());
        return currentDate;
    }

    public static long getDays(String dateStr) {
        long days = 0;
        if (dateStr != null && !dateStr.equals("")) {
            int day = Integer.parseInt(dateStr.substring(8, 10));
            int mnt = Integer.parseInt(dateStr.substring(5, 7));
            int yr = Integer.parseInt(dateStr.substring(0, 4));
            int month = mnt - 1;// 0-11 so 1 less

            Calendar thatDay = Calendar.getInstance();
            thatDay.set(Calendar.DAY_OF_MONTH, day);
            thatDay.set(Calendar.MONTH, month);
            thatDay.set(Calendar.YEAR, yr);
            Calendar today = Calendar.getInstance();
            long diff = today.getTimeInMillis() - thatDay.getTimeInMillis();
            days = diff / (24 * 60 * 60 * 1000);
        }
        return days;
    }

    public static void setAlphanumeric(EditText editTxt, int length) {
        try {

            editTxt.setFilters(new InputFilter[]{
                    new InputFilter.LengthFilter(length),
                    new AlphanumericInputFilter()});

        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void setAlphbates(EditText editTxt, int length) {
        try {

            editTxt.setFilters(new InputFilter[]{
                    new InputFilter.LengthFilter(length),
                    new AlphabateInputFilter()});

        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void setLength(EditText editTxt, String fieldLen) {

        try {
            if (fieldLen != null) {
                if (!fieldLen.equalsIgnoreCase("0.0")) {
//					String [] splitVal=fieldLen.split(Pattern.quote("."));

                    editTxt.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(fieldLen)});

                }
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String insertCommas1(String number) {
        return insertCommas1(new BigDecimal(number));
    }

    public static String getEditResourcePath(String collection, String key) {
        return new String(collection + "('" + key + "')");
    }

    public static boolean  isValidPanNumber(String panNumber) {
        String panNoRegEx;
        Pattern pattern;
        // Regex for a valid email address
        panNoRegEx = "[A-Z]{5}[0-9]{4}[A-Z]{1}";
        // Compare the regex with the email address
        pattern = Pattern.compile(panNoRegEx);
        Matcher matcher = pattern.matcher(panNumber);
        if (!matcher.find()) {
            return false;
        }
        return true;
    }

    public static ArrayList<HashMap<String, String>> convertToArrayListMap(String jsonString) {
        HashMap<String, String> myHashMap;
        ArrayList<HashMap<String, String>> arrtable = new ArrayList<HashMap<String, String>>();
        try {
            JSONArray jArray = new JSONArray(jsonString);
            JSONObject jObject = null;
            String keyString = null;
            for (int i = 0; i < jArray.length(); i++) {
                jObject = jArray.getJSONObject(i);
                myHashMap = new HashMap<String, String>();
                for (int k = 0; k < jObject.length(); k++) {
                    keyString = (String) jObject.names().get(k);
                    myHashMap.put(keyString, jObject.getString(keyString));
                }
                if (!myHashMap.isEmpty()) {
                    arrtable.add(myHashMap);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrtable;
    }

    public static boolean isInRange(Number number, BigInteger min,
                                    BigInteger max) {
        try {
            BigInteger bigInteger = null;
            if (number instanceof Byte || number instanceof Short
                    || number instanceof Integer || number instanceof Long) {
                bigInteger = BigInteger.valueOf(number.longValue());
            } else if (number instanceof Float || number instanceof Double) {
                bigInteger = new BigDecimal(number.doubleValue())
                        .toBigInteger();
            } else if (number instanceof BigInteger) {
                bigInteger = (BigInteger) number;
            } else if (number instanceof BigDecimal) {
                bigInteger = ((BigDecimal) number).toBigInteger();
            } else {
                // not a standard number
                bigInteger = new BigDecimal(number.doubleValue())
                        .toBigInteger();
            }
            return max.compareTo(bigInteger) >= 0
                    && min.compareTo(bigInteger) <= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isInRangeBetweenNoAval(String inputNo, BigInteger min,
                                                 BigInteger max) {

        boolean isNoAvl = false;
        try {
            ArrayList<String> list = new ArrayList<>();

            for (BigInteger bi = min;
                 bi.compareTo(max) <= 0;
                 bi = bi.add(BigInteger.ONE)) {
                list.add(bi.toString());
            }
//			String inputNo = number+"";
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).contains(inputNo)) {
                        isNoAvl = true;
                        break;
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isNoAvl;
    }

    public static String[] sortMultiDimArray(String[] multidimArray) {
        String twodimArray[] = multidimArray;

//		Arrays.sort(twodimArray, new ColumnComparator(0, SortingOrder.ASCENDING));

        Arrays.sort(twodimArray, new Comparator<String>() {
            @Override
            public int compare(final String entry1, final String entry2) {

                BigInteger i1 = null;
                BigInteger i2 = null;
                try {
                    i1 = new BigInteger(entry1);
                } catch (NumberFormatException e) {
                }

                try {
                    i2 = new BigInteger(entry2);
                } catch (NumberFormatException e) {
                }

                if (i1 != null && i2 != null) {
                    return i1.compareTo(i2);
                } else {
                    final String time1 = entry1;
                    final String time2 = entry2;
                    return time1.compareTo(time2);
                }
            }
        });

        return twodimArray;
    }
   /* public static void editTextDecimalFormat(final EditText editText, final int beforeDecimal, final int afterDecimal) {
        editText.setFilters(new InputFilter[] { new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                StringBuilder sbText = new StringBuilder(source);
                String text = sbText.toString();

                int mIntTextLength = text.length();

                if (dstart == 0) {
                    if(mIntTextLength>1){
                        return source;
                    }else {

                        if (text.contains("0") ) {
                            return "";
                        } else if (text.contains(".") ) {
                            return "0.";
                        } else if (text.contains("0..") ) {
                            return "0.";
                        } else {
                            return source;
                        }
                    }
                }
                String etText = editText.getText().toString();
                if (etText.isEmpty()) {
                    return null;
                }
                String temp = editText.getText() + source.toString();

                if (temp.equals(".")) {
                    return "0.";
                }if(temp.contains("0..")){
                    return "";
                } else if (temp.toString().indexOf(".") == -1) {
                    // no decimal point placed yet
                    if (temp.length() > beforeDecimal) {
                        return "";
                    }
                } else {
                    int dotPosition;
                    int cursorPositon = editText.getSelectionStart();
                    if (etText.indexOf(".") == -1) {
                        Log.i("First time Dot", etText.toString().indexOf(".") + " " + etText);
                        dotPosition = temp.indexOf(".");
                    } else {
                        dotPosition = etText.indexOf(".");
                    }
                    if (cursorPositon <= dotPosition) {
                        String beforeDot = etText.substring(0, dotPosition);
                        if (beforeDot.length() < beforeDecimal) {
                            return source;
                        } else {
                            if (source.toString().equalsIgnoreCase(".")) {
                                return source;
                            } else {
                                return "";
                            }

                        }
                    } else {
                        temp = temp.substring(temp.indexOf(".") + 1);
                        if (temp.length() > afterDecimal) {
                            return "";
                        }else if(etText.contains(source) && source.equals(".")){
                            return "";
                        }
                    }
                }
                return null;


            }
        } });


    }*/

    public static String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }

    @SuppressLint("NewApi")
    public static void showPopup(Context context, View v, final Activity activityName, int menuId) {
        PopupMenu popup = new PopupMenu(context, v);

        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(menuId, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                activityName.onOptionsItemSelected(item);
                return true;
            }
        });

        popup.show();
    }

    public static String insertCommas(BigDecimal number) {
        // for your case use this pattern -> #,##0.00
        DecimalFormat df = new DecimalFormat("#,##0");
        return df.format(number);
    }

    public static String getConvetDDMMYYYYY(String date) {
        String formatDate = "";
        if (date != null) {
            String day = date.substring(8, 10);
            String mon = date.substring(5, 7);
            String yr = date.substring(0, 4);

            formatDate = day + "/" + mon + "/" + yr;
        }
        return formatDate;
    }

    // To-Do Move to utils
    public static void getYearandMonth() {
        String date = (String) android.text.format.DateFormat.format(
                "yyyy-MM-dd", new Date());
        YEAR = date.substring(0, 4);
        CURYEAR = date.substring(0, 4);
        TOYEAR = date.substring(0, 4);
        MONTH = date.substring(5, 7);
        CURMONTH = date.substring(5, 7);
        TOMONTH = date.substring(5, 7);

        // To-Do Variable names should be renamed
        SALS_TAR_FROMPER = CURYEAR + MONTH;
        SALS_TAR_TOPER = CURYEAR + MONTH;
        COLS_TAR_FROMPER = CURYEAR + MONTH;
        COLS_TAR_TOPER = CURYEAR + MONTH;
        DLR_OFT_FROMPER = CURYEAR + MONTH;
        DLR_OFT_TOPER = CURYEAR + MONTH;
    }

    public static void setAmountPattern(EditText editTxt, final int beforeDecimal, final int afterDecimal) {

        try {
            editTxt.setFilters(new InputFilter[]{new DecimalFilter(editTxt, beforeDecimal, afterDecimal)});

        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String convert24hrFormatTo12hrFormat(String time) {

        DateFormat f1 = new SimpleDateFormat("HH:mm"); //HH for hour of the day (0 - 23)
        Date d;
        try {
            d = f1.parse(time);
            DateFormat f2 = new SimpleDateFormat("h:mm a");
            return f2.format(d).toLowerCase();

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getCurrentMonth() {
        String mStrCurrMonth = "";

        Calendar cal = Calendar.getInstance();
        int intFromMnt = cal.get(Calendar.MONTH) + 1;
        if (intFromMnt < 10)
            mStrCurrMonth = "0" + intFromMnt;
        else
            mStrCurrMonth = "" + intFromMnt;

        return mStrCurrMonth;
    }

    public static String getCurrentYear() {
        String mStrCurrentYear = "";

        Calendar cal = Calendar.getInstance();
        mStrCurrentYear = cal.get(Calendar.YEAR) + "";

        return mStrCurrentYear;
    }

    public static boolean isSpecificCollTodaySyncOrNot(String sleDate) {


        boolean mBoolDBSynced = false;
        if (sleDate != null && !sleDate.equalsIgnoreCase("")) {

            Date date = null;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                date = null;
                try {
                    date = sdf.parse(sleDate);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int mYear = cal.get(Calendar.YEAR);
            int mMonth = cal.get(Calendar.MONTH);
            int mDay = cal.get(Calendar.DAY_OF_MONTH);

            Calendar calCurrent = Calendar.getInstance();

            int mYearCurrent = calCurrent.get(Calendar.YEAR);
            int mMonthCurrent = calCurrent.get(Calendar.MONTH);
            int mDayCurrent = calCurrent.get(Calendar.DAY_OF_MONTH);

            if (mYear == mYearCurrent && mMonth == mMonthCurrent && mDay == mDayCurrent) {
                mBoolDBSynced = true;
            } else {
                mBoolDBSynced = false;
            }

        } else {
            mBoolDBSynced = false;
        }
        return mBoolDBSynced;
    }

    public static String getConcatinatinFlushCollectios(ArrayList<String> alFlushColl) {
        String concatFlushCollStr = "";
        for (int incVal = 0; incVal < alFlushColl.size(); incVal++) {
            if (incVal == 0 && incVal == alFlushColl.size() - 1) {
                concatFlushCollStr = concatFlushCollStr + alFlushColl.get(incVal);
            } else if (incVal == 0) {
                concatFlushCollStr = concatFlushCollStr + alFlushColl.get(incVal) + ", ";
            } else if (incVal == alFlushColl.size() - 1) {
                concatFlushCollStr = concatFlushCollStr + alFlushColl.get(incVal);
            } else {
                concatFlushCollStr = concatFlushCollStr + alFlushColl.get(incVal) + ", ";
            }
        }

        return concatFlushCollStr;
    }

    public static String getLastMonthDate() {
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        return simpleDateFormat.format(cal.getTime()) + "T00:00:00";
    }

    public static String convertArrListToGsonString(ArrayList<HashMap<String, String>> arrtable) {
        String convertGsonString = "";
        Gson gson = new Gson();
        try {
            convertGsonString = gson.toJson(arrtable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertGsonString;
    }

    public static ODataDuration getOdataDuration() {
        final Calendar calCurrentTime = Calendar.getInstance();
        int hourOfDay = calCurrentTime.get(Calendar.HOUR_OF_DAY); // 24 hour clock
        int minute = calCurrentTime.get(Calendar.MINUTE);
        int second = calCurrentTime.get(Calendar.SECOND);
        ODataDuration oDataDuration = null;
        try {
            oDataDuration = new ODataDurationDefaultImpl();
            oDataDuration.setHours(hourOfDay);
            oDataDuration.setMinutes(minute);
            oDataDuration.setSeconds(BigDecimal.valueOf(second));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return oDataDuration;
    }

    public static String getLastThreeMonthDate() {
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -3);
        return simpleDateFormat.format(cal.getTime()) + "T00:00:00";
    }

    public static File SaveImageInDevice(String filename, Bitmap bitmap) {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;

        File file = new File(extStorageDirectory, filename + ".jpg");
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, filename + ".jpg");
        }
        try {
            // make a new bitmap from your file

            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("file", "" + file);
        return file;

    }

    public static ODataDuration getTimeAsODataDuration(String timeString) {

        List<String> timeDuration = Arrays.asList(timeString.split("-"));
        int hour = Integer.parseInt(timeDuration.get(0));
        int minute = Integer.parseInt(timeDuration.get(1));
        int seconds = 00;
        ODataDuration oDataDuration = null;
        try {
            oDataDuration = new ODataDurationDefaultImpl();
            oDataDuration.setHours(hour);
            oDataDuration.setMinutes(minute);
            oDataDuration.setSeconds(BigDecimal.valueOf(seconds));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return oDataDuration;
    }

    public static void editTextDecimalFormat(final EditText editText, final int beforeDecimal, final int afterDecimal) {
        editText.setFilters(new InputFilter[]{new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                StringBuilder sbText = new StringBuilder(source);
                String text = sbText.toString();
                int mIntTextLength = text.length();
                if (dstart == 0) {
                    if (editText.getText().toString().length() > 0) {
                        String etText = editText.getText().toString();
                        int cursorPositon = editText.getSelectionStart();
                        String temp = editText.getText() + source.toString();
                        int dotPosition;
                        if (etText.indexOf(".") == -1) {
                            dotPosition = temp.indexOf(".");
                        } else {
                            dotPosition = etText.indexOf(".");
                        }

                        if (cursorPositon <= dotPosition) {
                            String beforeDot = etText.substring(0, dotPosition);
                            if (beforeDot.length() < beforeDecimal) {
                                String afterDot = etText.substring(cursorPositon, dotPosition);
                                if (afterDot.length() > afterDecimal) {
                                    if (!text.contains(".")) {
                                        return source;
                                    } else {
                                        return "";
                                    }

                                } else {
                                    return source;
                                }
                            }

                            if (source.toString().equalsIgnoreCase(".")) {
                                return source;
                            }

                            return "";
                        }
                        return null;
                    } else {
                        return (CharSequence) (mIntTextLength > 1 ? source : (text.contains("0") ? "" : (text.contains(".") ? "0." : (text.contains("0..") ? "0." : source))));
                    }

                } else {
                    String etText = editText.getText().toString();
                    if (etText.isEmpty()) {
                        return null;
                    } else {
                        String temp = editText.getText() + source.toString();
                        if (temp.equals(".")) {
                            return "0.";
                        } else if (temp.contains("0..")) {
                            return "";
                        } else {
                            if (temp.toString().indexOf(".") == -1) {
                                if (temp.length() > beforeDecimal) {
                                    return "";
                                }
                            } else {
                                int cursorPositon = editText.getSelectionStart();
                                int dotPosition;
                                if (etText.indexOf(".") == -1) {
                                    dotPosition = temp.indexOf(".");
                                } else {
                                    dotPosition = etText.indexOf(".");
                                }

                                if (cursorPositon <= dotPosition) {
                                    String beforeDot = etText.substring(0, dotPosition);
                                    if (beforeDot.length() < beforeDecimal) {
                                        String afterDot = etText.substring(cursorPositon, dotPosition);
                                        if (afterDot.length() > afterDecimal) {
                                            if (!text.contains(".")) {
                                                return source;
                                            } else {
                                                return "";
                                            }

                                        } else {
                                            return source;
                                        }
                                    }

                                    if (source.toString().equalsIgnoreCase(".")) {
                                        return source;
                                    }

                                    return "";
                                }

                                temp = temp.substring(temp.indexOf(".") + 1);
                                if (temp.length() > afterDecimal) {
                                    return "";
                                }

                                if (etText.contains(source) && source.equals(".")) {
                                    return "";
                                }
                            }

                            return null;
                        }
                    }
                }
            }
        }});
    }

    public static void editTextDecimalFormatZeroAllow(final EditText editText, final int beforeDecimal, final int afterDecimal) {
        editText.setFilters(new InputFilter[]{new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                StringBuilder sbText = new StringBuilder(source);
                String text = sbText.toString();
                int mIntTextLength = text.length();
                String etText;
                int dotPosition;
                String beforeDot;
                String afterDot;
                if (dstart == 0) {
                    if (editText.getText().toString().length() > 0) {
                        etText = editText.getText().toString();
                        int temp1 = editText.getSelectionStart();
                        String cursorPositon1 = editText.getText() + source.toString();
                        if (etText.indexOf(".") == -1) {
                            dotPosition = cursorPositon1.indexOf(".");
                        } else {
                            dotPosition = etText.indexOf(".");
                        }

                        if (temp1 <= dotPosition) {
                            beforeDot = etText.substring(0, dotPosition);
                            if (beforeDot.length() < beforeDecimal) {
                                afterDot = etText.substring(temp1, dotPosition);
                                return (CharSequence) (afterDot.length() > afterDecimal ? (!text.contains(".") ? source : "") : source);
                            } else {
                                return (CharSequence) (source.toString().equalsIgnoreCase(".") ? source : "");
                            }
                        } else {
                            return null;
                        }
                    } else {
                        return (CharSequence) (mIntTextLength > 1 ? source : (text.contains("0") ? "0" : (text.contains(".") ? "0." : (text.contains("0..") ? "0." : source))));
                    }
                } else {
                    etText = editText.getText().toString();
                    if (etText.isEmpty()) {
                        return null;
                    } else {
                        String temp = editText.getText() + source.toString();
                        if (temp.equals(".")) {
                            return "0.";
                        } else if (temp.contains("0..")) {
                            return "";
                        } else {
                            if (temp.toString().indexOf(".") == -1) {
                                if (temp.length() > beforeDecimal) {
                                    return "";
                                }
                            } else {
                                int cursorPositon = editText.getSelectionStart();
                                if (etText.indexOf(".") == -1) {
                                    dotPosition = temp.indexOf(".");
                                } else {
                                    dotPosition = etText.indexOf(".");
                                }

                                if (cursorPositon <= dotPosition) {
                                    beforeDot = etText.substring(0, dotPosition);
                                    if (beforeDot.length() < beforeDecimal) {
                                        afterDot = etText.substring(cursorPositon, dotPosition);
                                        if (afterDot.length() > afterDecimal) {
                                            if (!text.contains(".")) {
                                                return source;
                                            }

                                            return "";
                                        }

                                        return source;
                                    }

                                    if (source.toString().equalsIgnoreCase(".")) {
                                        return source;
                                    }

                                    return "";
                                }

                                temp = temp.substring(temp.indexOf(".") + 1);
                                if (temp.length() > afterDecimal) {
                                    return "";
                                }

                                if (etText.contains(source) && source.equals(".")) {
                                    return "";
                                }
                            }

                            return null;
                        }
                    }
                }
            }
        }});
    }

    public static String getNameSpace(ODataOfflineStore oDataOfflineStore) {
        String mStrNameSpace = "";

        ODataMetadata oDataMetadata = null;
        try {
            oDataMetadata = oDataOfflineStore.getMetadata();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Set<String> set = oDataMetadata.getMetaNamespaces();

        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                String tempNameSpace = itr.next().toString();
                if (!tempNameSpace.equalsIgnoreCase("OfflineOData")) {
                    mStrNameSpace = tempNameSpace;
                }
            }
        }

        return mStrNameSpace;
    }

    public static String getTimeAgo(String mStrDate, Context ctx) {
        try {
            StringBuffer sb = new StringBuffer();
            Date current = Calendar.getInstance().getTime();

            Date date = null;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                date = null;
                try {
                    date = sdf.parse(mStrDate);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (date != null) {
                long diffInSeconds = (current.getTime() - date.getTime()) / 1000;

                long sec = (diffInSeconds >= 60 ? diffInSeconds % 60 : diffInSeconds);
                long min = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds;
                long hrs = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds;
                long days = (diffInSeconds = (diffInSeconds / 24)) >= 30 ? diffInSeconds % 30 : diffInSeconds;
                long months = (diffInSeconds = (diffInSeconds / 30)) >= 12 ? diffInSeconds % 12 : diffInSeconds;
                long years = (diffInSeconds = (diffInSeconds / 12));

                if (years > 0) {
                    if (years == 1) {
                        sb.append(years + " " + ctx.getString(R.string.date_util_unit_year));
                    } else {
                        sb.append(years + " " + ctx.getString(R.string.date_util_unit_years));
                    }
           /* if (years <= 6 && months > 0) {
                if (months == 1) {
                    sb.append(" and a month");
                } else {
                    sb.append(" and " + months + " months");
                }
            }*/
                } else if (months > 0) {
                    if (months == 1) {
                        sb.append(months + " " + ctx.getString(R.string.date_util_unit_month));
                    } else {
                        sb.append(months + " " + ctx.getString(R.string.date_util_unit_months));
                    }
           /* if (months <= 6 && days > 0) {
                if (days == 1) {
                    sb.append(" and a day");
                } else {
                    sb.append(" and " + days + " days");
                }
            }*/
                } else if (days > 0) {
                    if (days == 1) {
                        sb.append(days + " " + ctx.getString(R.string.date_util_unit_day));
                    } else {
                        sb.append(days + " " + ctx.getString(R.string.date_util_unit_days));
                    }
            /*if (days <= 3 && hrs > 0) {
                if (hrs == 1) {
                    sb.append(" and an hour");
                } else {
                    sb.append(" and " + hrs + " hours");
                }
            }*/
                } else if (hrs > 0) {
                    if (hrs == 1) {
                        sb.append(hrs + " " + ctx.getString(R.string.date_util_unit_hour));
                    } else {
                        sb.append(hrs + " " + ctx.getString(R.string.date_util_unit_hours));
                    }
           /* if (min > 1) {
                sb.append(" and " + min + " minutes");
            }*/
                } else if (min > 0) {
                    if (min == 1) {
                        sb.append(min + " " + ctx.getString(R.string.date_util_unit_minute));
                    } else {
                        sb.append(min + " " + ctx.getString(R.string.date_util_unit_minutes));
                    }
           /* if (sec > 1) {
                sb.append(" and " + sec + " seconds");
            }*/
                } else {
            /*if (sec <= 1) {
                sb.append("about a second");
            } else {
                sb.append("about " + sec + " seconds");
            }*/

                    if (min == 1) {
                        sb.append(min + " " + ctx.getString(R.string.date_util_unit_minute));
                    } else {
                        sb.append(min + " " + ctx.getString(R.string.date_util_unit_minute));
                    }
                }

                sb.append(" " + ctx.getString(R.string.date_util_sufix_ago));

                return sb.toString();
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static int getTimeDistanceInMinutes(long time) {
        long timeDistance = currentDate().getTime() - time;
        return Math.round((Math.abs(timeDistance) / 1000) / 60);
    }

    public static String getSyncHistoryddmmyyyyTime() {
        String currentDateTimeString1 = (String) android.text.format.DateFormat.format("dd/MM/yyyy", new Date());
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
        String currentDateTimeString2 = dateFormat.format(new Date());
        String currentDateTimeString = currentDateTimeString1 + "T" + currentDateTimeString2;
        return currentDateTimeString1 + " " + currentDateTimeString2;
    }

    public static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public static String trimQtyDecimalPlace(String qty) {
        if (qty.contains("."))
            return qty.substring(0, qty.indexOf("."));
        else
            return qty;
    }

    public static String getFirstDateOfCurrentMonth() {
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return simpleDateFormat.format(cal.getTime()) + "T00:00:00";
    }

    public static long getFirstDateOfCurrentMonthInMiliseconds() {
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTimeInMillis();
    }

    /*returns item number in six characters*/
    public static String getItemNoInSixCharsWithPrefixZeros(String itemNo) {
        String updatedString = new String(itemNo);
        if (itemNo != null) {
            int prefixLength = 6 - itemNo.length();
            if (itemNo.length() < 6) {
                for (int i = 0; i < prefixLength; i++) {
                    updatedString = "0" + updatedString;
                }
            }
        } else
            updatedString = "000000";
        return updatedString;
    }

    public static void hideKeyboardFrom(Context context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    /**
     * @param context
     * @param dateString pass date string in  dd/MM/yyyy formate
     * @return
     */
    @Deprecated
    public static String convertDateIntoDeviceFormat(Context context, String dateString) {
        String pattern = getDeviceDateFormat(context);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Date convertedDate = new Date();

        try {
            convertedDate = dateFormat.parse(dateString);
        } catch (ParseException var7) {
            var7.printStackTrace();
        }

        String dateInDeviceFormat = (String) android.text.format.DateFormat.format(pattern, convertedDate);
        return dateInDeviceFormat;
    }
    /**
     * @param context
     * @param dateString pass date string in  dd/MM/yyyy formate
     * @return
     */
    public static String convertDateIntoDeviceFormat(Context context, String dateString, String configDateFormat) {
        String pattern = getDeviceDateFormat(context);
        if (!TextUtils.isEmpty(configDateFormat)){
            pattern = configDateFormat;
        }
        SimpleDateFormat dateFormats = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Date convertedDate = new Date();

        try {
            convertedDate = dateFormats.parse(dateString);
        } catch (ParseException var7) {
            var7.printStackTrace();
        }

        String dateInDeviceFormat = (String) android.text.format.DateFormat.format(pattern, convertedDate);
        return dateInDeviceFormat;
    }

    /** Deprecated
     * this is deprecated use this method convertDateIntoDeviceFormat(context, calender, dateformat)
     * @param context
     * @param calendar
     * @return
     */
    @Deprecated
    public static String convertDateIntoDeviceFormat(Context context,GregorianCalendar calendar) {
        if (calendar!=null) {
            String pattern = getDeviceDateFormat(context);
            try {
                return (String) android.text.format.DateFormat.format(pattern, calendar);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * you can you your own dateformat
     * @param context
     * @param calendar
     * @param configDateFormat
     * @return
     */
    public static String convertDateIntoDeviceFormat(Context context,GregorianCalendar calendar, String configDateFormat) {
        if (calendar!=null) {
            String pattern = getDeviceDateFormat(context);
            if (!TextUtils.isEmpty(configDateFormat)){
                pattern = configDateFormat;
            }
            try {
                return (String) android.text.format.DateFormat.format(pattern, calendar);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return "";
    }
    public static String getDeviceDateFormat(Context context) {
        java.text.DateFormat dateFormat1 = android.text.format.DateFormat.getDateFormat(context);
       return ((SimpleDateFormat) dateFormat1).toLocalizedPattern();
    }

    public static String getNoOfDaysBefore(int days) {
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -days);
        return simpleDateFormat.format(cal.getTime()) + "T00:00:00";
    }

    public static TextView setFontSizeByMaxText(TextView textView) {
        try {
            int lineCount = textView.getText().length();

            if (lineCount < 20) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            } else if (lineCount < 41) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            } else if (lineCount < 60) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            } else if (lineCount < 80) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            } else if (lineCount < 100) {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
            } else {
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 6);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        }
        return textView;
    }

    public static int dpToPx(int valueInDP, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int px = valueInDP * ((int) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static void dialogBoxWithCallBack(Context context, String title, String message, String positiveButton, String negativeButton, boolean isCancel, final DialogCallBack dialogCallBack) {
        try {
            new DialogFactory.Alert(context).setPositiveButton(positiveButton).setNegativeButton(negativeButton).setTheme(R.style.MaterialAlertDialog).setMessage(message).setOnDialogClick(new OnDialogClick() {
                @Override
                public void onDialogClick(boolean isPositive) {
                    if(isPositive){
                        if (dialogCallBack != null)
                            dialogCallBack.clickedStatus(true);
                    }else {
                        if (dialogCallBack != null)
                            dialogCallBack.clickedStatus(false);
                    }
                }
            }).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void oldDialogBoxWithCallBack(Context context, String title, String message, String positiveButton, String negativeButton, boolean isCancel, final DialogCallBack dialogCallBack) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyTheme);
            builder.setCancelable(isCancel);
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
    public static void dialogBoxWithCallBackNavigate(Context context, String title, String message, String positiveButton, String negativeButton, boolean isCancel, final DialogCallBack dialogCallBack) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.UtilsDialogTheme);
            builder.setCancelable(isCancel);
            if (!title.equalsIgnoreCase("")) {
                builder.setTitle(title);
            }
            builder.setMessage(message).setCancelable(false).setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    if (dialogCallBack != null)
                        dialogCallBack.clickedStatus(true);
                    ((Activity)context).finish();
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

    public static boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void initReg(Context context, Activity activity, RegistrationCallBack registrationCallBack) {
//        try {
//            packageManager.getPackageInfo(packagename, 0);
//            return true;
//        } catch (PackageManager.NameNotFoundException e) {
//            return false;
//        }
    }

    public static String getErrorMsg(int error_code, Context context) {
        String mStrErrMsg = "";
        if (error_code == ERROR_URL_NOT_PERMITTED) {
            mStrErrMsg = "[" + error_code + "] " + context.getString(R.string.can_not_be_register);
        } else if (error_code == ERROR_NO_SERVER_RESPONCE) {
            mStrErrMsg = "[" + error_code + "] " + context.getString(R.string.reg_failed_no_server_res);
        } else if (error_code == ERROR_IMO_LIB_NOT_FOUND) {
            mStrErrMsg = "[" + error_code + "] " + context.getString(R.string.imo_lib_not_found);
        } else if (error_code == ERROR_UNKNOWN_ERROR) {
            mStrErrMsg = "[" + error_code + "] " + context.getString(R.string.reg_failed_unknown_error);
        } else if (error_code == ERROR_NO_REG_LIS) {
            mStrErrMsg = "[" + error_code + "] " + context.getString(R.string.no_reg_listner);
        } else if (error_code == ERROR_NO_CAPTCH) {
            mStrErrMsg = "[" + error_code + "] " + context.getString(R.string.no_captch_listner);
        } else if (error_code == ERROR_HTTP) {
            mStrErrMsg = "[" + error_code + "] " + context.getString(R.string.http_error);
        } else if (error_code == ERROR_PARSER) {
            mStrErrMsg = "[" + error_code + "] " + context.getString(R.string.parser_error);
        } else if (error_code == ERROR_HOST_UN_REACHABLE) {
            mStrErrMsg = "[" + error_code + "] " + context.getString(R.string.host_is_unreachable);
        } else if (error_code == ERROR_USER_AUTH) {
            mStrErrMsg = "[" + error_code + "] " + context.getString(R.string.user_auth_error);
        } else if (error_code == ERROR_BAD_REQUEST) {
            mStrErrMsg = "[" + error_code + "] " + context.getString(R.string.error_bad_req);
        } else if (error_code == ERROR_UN_AUTH) {
            mStrErrMsg = "[" + error_code + "] " + context.getString(R.string.error_un_autorized);
        } else if (error_code == ERROR_FORBIDDEN) {
            mStrErrMsg = "[" + error_code + "] " + context.getString(R.string.error_forbidden_error);
        } else if (error_code == ERROR_SERVER_NOT_FOUND) {
            mStrErrMsg = "[" + error_code + "] " + context.getString(R.string.error_not_found);
        } else {
            mStrErrMsg = "[" + ERROR_CODE_UNKNOWN + "] " + context.getString(R.string.reg_failed_unknown_error);
        }

        return mStrErrMsg;
    }

    public static void showAlertWithHeading(String message, Context ctxt, String mStrAttemptText) {
        try {
            String mstr_msg = "";
            AlertDialog.Builder builder = new AlertDialog.Builder(ctxt, R.style.UtilsDialogTheme);
            if (!mStrAttemptText.equalsIgnoreCase("")) {
                mstr_msg = "\n" + mStrAttemptText + "\n\n" + message;
            } else {
                mstr_msg = message;
            }
            TextView tvAttemtTxt = new TextView(ctxt);
            tvAttemtTxt.setText(mstr_msg);
            tvAttemtTxt.setGravity(Gravity.CENTER_HORIZONTAL);
            tvAttemtTxt.setTextSize(20);
            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            tvAttemtTxt.setTypeface(boldTypeface);

            //set custom title
            builder.setCustomTitle(tvAttemtTxt);
            builder.setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static String getApplicationUrl(String serverText, String port, String appID, boolean isHttps) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(isHttps ? "https" : "http")
                .encodedAuthority(serverText + ":" + port)
                .appendPath(appID);
//                .fragment(applicationName);
        return builder.build().toString();
    }
    public static String getApplicationUrl(String serverText, String port, String appID, boolean isHttps, String suffix, String farmId) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(isHttps ? "https" : "http")
                .encodedAuthority(serverText + ":" + port)
                .appendPath(suffix)
                .appendPath(farmId)
                .appendPath(appID);
//                .fragment(applicationName);
        return builder.build().toString();
    }
    /*get url from the smp*/
    public static URL getURLFromConnection(Context mContext, String resoursePath,SharedPreferences sharedPreferences) throws MalformedURLException {
        URL url = null;
       /* LogonCoreContext lgCtx = LogonCore.getInstance().getLogonContext();
        String protocol = lgCtx.isHttps() ? "https" : "http";
        String relayUrlSuffix = lgCtx.getResourcePath();*/
        if (TextUtils.isEmpty(sharedPreferences.getString(UtilRegistrationActivity.KEY_SUFFIX,"")))
            url = new URL(getApplicationUrl(sharedPreferences.getString(UtilRegistrationActivity.KEY_serverHost,""),sharedPreferences.getString(UtilRegistrationActivity.KEY_serverPort,""),resoursePath,sharedPreferences.getBoolean(UtilRegistrationActivity.KEY_ISHTTPS,false)));
        else {
            url = new URL(getApplicationUrl(sharedPreferences.getString(UtilRegistrationActivity.KEY_serverHost,""),sharedPreferences.getString(UtilRegistrationActivity.KEY_serverPort,""),resoursePath,sharedPreferences.getBoolean(UtilRegistrationActivity.KEY_ISHTTPS,false),sharedPreferences.getString(UtilRegistrationActivity.KEY_SUFFIX,""),sharedPreferences.getString(UtilRegistrationActivity.KEY_FARMID,"")));
        }
        return url;
    }

    /*http url connection*/
    public static String getPCUtilsReponse(URL url, String userName, String psw, String connectionId) throws IOException {
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
            String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes("UTF-8"), Base64.NO_WRAP);
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
                result = readResponse(stream);
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

    /**
     * Converts the contents of an InputStream to a String.
     */
    private static String readResponse(InputStream stream)
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

    /**
     * add comma indian format
     *
     * @param number
     * @return
     */
    public static String commaFormat(BigDecimal number) {
        DecimalFormat df = new DecimalFormat("#,##,##0.00");
        return df.format(number);
    }

    public static String commaSeparator(String value) {
        try {
            return value != null && !value.equalsIgnoreCase("") ? UtilConstants.commaFormat(new BigDecimal(value)) : "0.00";
        } catch (Exception var2) {
            var2.printStackTrace();
            return "0.00";
        }
    }

    public static File getPCUtilsFile(String downloadURL, File storagePath, String userName, String psw, String connectionId, String sessionId, boolean isSessionRequired) throws IOException {
        URL url = new URL(downloadURL);
        HttpURLConnection urlConnection = (HttpURLConnection) url
                .openConnection();
        urlConnection.setReadTimeout(1000 * 60);
        // Timeout for connection.connect() arbitrarily set to 3000ms.
        urlConnection.setConnectTimeout(1000 * 60);
        String userCredentials = userName + ":" + psw;
        String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes("UTF-8"), Base64.NO_WRAP);
        urlConnection.setRequestProperty("Authorization", basicAuth);
        urlConnection.setRequestProperty("X-SMP-APPCID", connectionId);
        if (isSessionRequired) {
            urlConnection.setRequestProperty("x-arteria-loginid", sessionId);
        }
        // For this use case, set HTTP method to GET.
        urlConnection.setRequestMethod("GET");
        // Already true by default but setting just in case; needs to be true since this request
        // is carrying an input (response) body.
        urlConnection.setDoInput(true);

        // connect
        urlConnection.connect();

        int responseCode = urlConnection.getResponseCode();
        Log.e("Msg", urlConnection.getResponseMessage());
        if (responseCode != HttpsURLConnection.HTTP_OK) {
            throw new IOException("HTTP error code: " + responseCode);
        }

        InputStream inputStream = null;
        inputStream = urlConnection.getInputStream();
        FileOutputStream fileOutputStream = new FileOutputStream(storagePath);

        // create a buffer...
        byte[] buffer = new byte[1000 * 1024];
        int bufferLength = 0;

        while ((bufferLength = inputStream.read(buffer)) > 0) {
            fileOutputStream.write(buffer, 0, bufferLength);
        }
        // close the output stream when complete //
        fileOutputStream.close();

        return storagePath;
    }

    public static void navigateToAppSettingsScreen(Context context) {
        try {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
            intent.setData(uri);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File downloadSalesOrderFiles(Context mContext, String soNo, String upendURL, String sharedPrefKey, String resoursePath, String sessionId, boolean isSessionRequired, String fileExtension) throws IOException {
        File downloadedDirectory = null;
        File myDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        myDirectory = new File(myDirectory + "/" + soNo + "." + fileExtension);
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(sharedPrefKey, 0);
        String userId = sharedPreferences.getString(UtilRegistrationActivity.KEY_username, "");
        String psw = sharedPreferences.getString(UtilRegistrationActivity.KEY_password, "");
        String connectionId = sharedPreferences.getString(UtilRegistrationActivity.KEY_appConnID, "");
        URL getURL = UtilConstants.getURLFromConnection(mContext, resoursePath, sharedPreferences);
        if (getURL != null && !TextUtils.isEmpty(userId)) {
            String downLoadURL = String.valueOf(getURL) + upendURL;
            downloadedDirectory = UtilConstants.getPCUtilsFile(downLoadURL, myDirectory, userId, psw, connectionId, sessionId, isSessionRequired);
        }
        return downloadedDirectory;
    }

    public static void openViewer(Activity mContext, String urlSt, String type) {
        try {
            if (!TextUtils.isEmpty(urlSt)) {
                File e = new File(urlSt);
                if (e.exists()) {
                    Uri path;
                    Intent objIntent = new Intent("android.intent.action.VIEW");
                    if (Build.VERSION_CODES.N <= Build.VERSION.SDK_INT) {
                        path = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".provider", e);

                    } else {
                        path = Uri.fromFile(e);
                    }
                    switch (type) {
                        case XLS_MINE_TYPE:
                            objIntent.setDataAndType(path, "application/vnd.ms-excel");
                            break;
                        case XLS_MINE_TYPE_1:
                            objIntent.setDataAndType(path, "application/vnd.ms-excel");
                            break;
                        case DOC_MINE_TYPE:
                            objIntent.setDataAndType(path, "application/msword");
                            break;
                        case JPG_MINE_TYPE:
                            objIntent.setDataAndType(path, "image/*");
                            break;
                        case POWERPOINT_MINE_TYPE:
                            objIntent.setDataAndType(path, "application/vnd.ms-powerpoint");
                            break;
                        case TXT_MINE_TYPE:
                            objIntent.setDataAndType(path, "text/plain");
                            break;
                        case TXT_MINE_TYPE_1:
                            objIntent.setDataAndType(path, "plain/text");
                            break;
                        case PDF_MINE_TYPE:
                            objIntent.setDataAndType(path, "application/pdf");
                            break;
                    }
                    objIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    objIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    mContext.startActivity(objIntent);
                } else {
                    try {
                        Toast.makeText(mContext, mContext.getString(R.string.error_file_not_exist), Toast.LENGTH_SHORT).show();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } catch (Exception var5) {
            var5.printStackTrace();
            switch (type) {
                case XLS_MINE_TYPE:
                    Toast.makeText(mContext, mContext.getString(R.string.error_open_xls), Toast.LENGTH_SHORT).show();
                    break;
                case XLS_MINE_TYPE_1:
                    Toast.makeText(mContext, mContext.getString(R.string.error_open_xls), Toast.LENGTH_SHORT).show();
                    break;
                case DOC_MINE_TYPE:
                    Toast.makeText(mContext, mContext.getString(R.string.error_open_word), Toast.LENGTH_SHORT).show();
                    break;
                case JPG_MINE_TYPE:
                    Toast.makeText(mContext, mContext.getString(R.string.error_open_doc), Toast.LENGTH_SHORT).show();
                    break;
                case POWERPOINT_MINE_TYPE:
                    Toast.makeText(mContext, mContext.getString(R.string.error_open_ppt), Toast.LENGTH_SHORT).show();
                    break;
                case TXT_MINE_TYPE:
                    Toast.makeText(mContext, mContext.getString(R.string.error_open_text), Toast.LENGTH_SHORT).show();
                    break;
                case TXT_MINE_TYPE_1:
                    Toast.makeText(mContext, mContext.getString(R.string.error_open_text), Toast.LENGTH_SHORT).show();
                    break;
                case PDF_MINE_TYPE:
                    Toast.makeText(mContext, mContext.getString(R.string.error_open_pdf), Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    }

    public static boolean closeStore(Context mContext, ODataOfflineStoreOptions options,
                                     String errMsg, ODataOfflineStore offlineStore,
                                     String sharedPrefName, String errorCode) {
        boolean isReInitStore = false;
        try {
            if (errMsg.contains("" + UtilConstants.Build_Database_Failed_Error_Code1)
                    || errMsg.contains("" + UtilConstants.Build_Database_Failed_Error_Code2)
                    || errMsg.contains("" + UtilConstants.Build_Database_Failed_Error_Code3)
                    || errorCode.contains("" + UtilConstants.Execu_SQL_Error_Code)
                    || errorCode.contains("" + UtilConstants.Store_Def_Not_matched_Code)) {
                isReInitStore = UtilOfflineManager.closeOfflineStore(mContext, options, offlineStore, sharedPrefName);
            } else {
                isReInitStore = false;
            }
        } catch (Exception e) {
            LogManager.writeLogError(UtilConstants.error_during_offline_close + e.getMessage());
        }
        return isReInitStore;
    }

    public static void setPermissionStatus(Context mContext, String key, boolean value) {
        SharedPreferences permissionStatus = mContext.getSharedPreferences("permissionStatus", MODE_PRIVATE);
        SharedPreferences.Editor editor = permissionStatus.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getPermissionStatus(Context mContext, String key) {
        SharedPreferences permissionStatus = mContext.getSharedPreferences("permissionStatus", MODE_PRIVATE);
        return permissionStatus.getBoolean(key, false);
    }

    public static void dialogBoxWithButton(Context context, String title, String message, String positiveButton, String negativeButton, final DialogCallBack dialogCallBack) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.UtilsDialogTheme);
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

    public static Location getLocationNoDialog(Context context) {
        boolean validLocation = false;
        GpsTracker gps = new GpsTracker(context);
        double latitude = 0.0D;
        double longitude = 0.0D;
        if (gps.canGetLocation()) {
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
        }

        if (latitude != 0.0D && longitude != 0.0D) {
            return gps.getLocations();
        } else {
            return null;
        }
    }

    public static void alertLocationPopup(int errorCode, String errorMsg, final Context context, final Activity activity) {
        if (errorMsg.contains(UtilConstants.Permission_Error)) {
            if (Build.VERSION_CODES.M <= android.os.Build.VERSION.SDK_INT) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, UtilConstants.Location_PERMISSION_CONSTANT);
                } else if (UtilConstants.getPermissionStatus(activity, Manifest.permission.ACCESS_COARSE_LOCATION)) {

                    UtilConstants.dialogBoxWithButton(context, "",
                            context.getString(R.string.this_app_needs_location_permission), context.getString(R.string.msg_go_to_settings),
                            context.getString(R.string.cancel), new DialogCallBack() {
                                @Override
                                public void clickedStatus(boolean clickedStatus) {
                                    if (clickedStatus) {
                                        UtilConstants.navigateToAppSettingsScreen(context);
                                    }
                                }
                            });
                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            UtilConstants.Location_PERMISSION_CONSTANT);
                }
                UtilConstants.setPermissionStatus(context, Manifest.permission.ACCESS_COARSE_LOCATION, true);
            }
        } else if (errorCode == LocationUtils.ERROR_GPS_NOT_ENABLE) {
            AlertDialog.Builder gpsEnableDlg = new AlertDialog.Builder(activity, R.style.UtilsDialogTheme);
            gpsEnableDlg
                    .setMessage(context.getString(R.string.alert_gps_not_enabled));
            gpsEnableDlg.setPositiveButton(context.getString(R.string.settings),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            activity.startActivity(intent);
                        }
                    });
            // on pressing cancel button
            gpsEnableDlg.setNegativeButton(context.getString(R.string.cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            // Showing Alert Message
            gpsEnableDlg.show();
        } else {
            UtilConstants.showAlert(errorMsg, context);
        }
    }

    public static AlertDialog showProgressDialogs(Context context, String messages, boolean isCancel) {
        try {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            final View dialogView = LayoutInflater.from(context).inflate(R.layout.linear_indeterminate_progress, null);
            TextView tvLoadingMsg = (TextView) dialogView.findViewById(R.id.tvLoadingMsg);
            tvLoadingMsg.setText(messages);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setCancelable(isCancel);
            AlertDialog b = dialogBuilder.create();
            b.show();
            return b;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void hideProgressDialog(AlertDialog alertDialog) {
        try {
            if (alertDialog != null)
                alertDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    enum SortingOrder {
        ASCENDING, DESCENDING;
    }

    public static class DecimalDigitsInputFilter implements InputFilter {

        Pattern mPattern;

//		Pattern mPattern;
//		String len = "1.0";


        public DecimalDigitsInputFilter(String len) {
//			mPattern = Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1)
//					+ "}+((\\.[0-9]{0," + (digitsAfterZero - 1)
//					+ "})?)||(\\.)?");

            int value = (new BigDecimal(len).compareTo(new BigDecimal("0.0")));
            if (!len.equalsIgnoreCase("0.0") && value > 0) {
                String[] splitVal = len.split("\\.");

                String digBeforeDec = "", digAfterDec = "";

                digBeforeDec = splitVal[0].equalsIgnoreCase("0") ? "{0}" : "{0," + (Integer.parseInt(splitVal[0]) - 1) + "}";

                digAfterDec = splitVal[1].equalsIgnoreCase("0") ? "{0}" : "{0," + (Integer.parseInt(splitVal[1]) - 1) + "}";

                mPattern = Pattern.compile("[0-9]" + digBeforeDec + "((\\.[0-9]" + digAfterDec + ")?)||(\\.)?");
            }
        }


        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {

            StringBuilder sbText = new StringBuilder(source);
            String text = sbText.toString();
            if (dstart == 0) {
                if (text.contains("0")) {
                    return "";
                } else {
                    return source;
                }
            }

            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }

    }

    public static class AlphanumericInputFilter implements InputFilter {

        Pattern mPattern;

        public AlphanumericInputFilter() {
            mPattern = Pattern.compile("^[a-zA-Z0-9]*$");
        }


        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {


            StringBuilder sbText = new StringBuilder(source);
            String text = sbText.toString();
            if (dstart == 0) {
                if (text.contains("0")) {
                    return "";
                } else {
                    return source;
                }
            }

            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }

    }

    public static class AlphabateInputFilter implements InputFilter {

        Pattern mPattern;

        public AlphabateInputFilter() {
            mPattern = Pattern.compile("^[a-zA-Z]*$");
        }


        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {


            StringBuilder sbText = new StringBuilder(source);
            String text = sbText.toString();
            if (dstart == 0) {
                if (text.contains("0")) {
                    return "";
                } else {
                    return source;
                }
            }

            Matcher matcher = mPattern.matcher(dest);
            if (!matcher.matches())
                return "";
            return null;
        }
    }

    static class ColumnComparator implements Comparator<Comparable[]> {
        private final int iColumn;
        private final SortingOrder order;

        public ColumnComparator(int column, SortingOrder order) {
            this.iColumn = column;
            this.order = order;
        }

        @Override
        public int compare(Comparable[] c1, Comparable[] c2) {
            int result = c1[iColumn].compareTo(c2[iColumn]);
            return order == SortingOrder.ASCENDING ? result : -result;
        }
    }

    public static class DecimalFilter implements InputFilter {
        EditText editText;
        int beforeDecimal, afterDecimal;

        public DecimalFilter(EditText editText, int beforeDecimal, int afterDecimal) {
            this.editText = editText;
            this.afterDecimal = afterDecimal;
            this.beforeDecimal = beforeDecimal;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {

            StringBuilder sbText = new StringBuilder(source);
            String text = sbText.toString();
            if (dstart == 0) {
                if (text.contains("0")) {
                    return "";
                } else {
                    return source;
                }
            }
            String etText = editText.getText().toString();
            if (etText.isEmpty()) {
                return null;
            }
            String temp = editText.getText() + source.toString();

            if (temp.equals(".")) {
                return "0.";
            } else if (temp.toString().indexOf(".") == -1) {
                // no decimal point placed yet
                if (temp.length() > beforeDecimal) {
                    return "";
                }
            } else {
                int dotPosition;
                int cursorPositon = editText.getSelectionStart();
                if (etText.indexOf(".") == -1) {
                    Log.i("First time Dot", etText.toString().indexOf(".") + " " + etText);
                    dotPosition = temp.indexOf(".");
//
                } else {
                    dotPosition = etText.indexOf(".");
//
                }
                if (cursorPositon <= dotPosition) {
                    String beforeDot = etText.substring(0, dotPosition);
                    if (beforeDot.length() < beforeDecimal) {
                        return source;
                    } else {
                        if (source.toString().equalsIgnoreCase(".")) {
                            return source;
                        } else {
                            return "";
                        }

                    }
                } else {
                    temp = temp.substring(temp.indexOf(".") + 1);
                    if (temp.length() > afterDecimal) {
                        return "";
                    }
                }
            }
            return null;
        }


    }

    /* public static void showProgressBar(View yourView, View progressView, String message){
         try {
             if (progressView != null && yourView != null) {
 //                yourView.setVisibility(View.GONE);
                 progressView.setVisibility(View.VISIBLE);
                 TextView progressText = (TextView) progressView.findViewById(R.id.tvLoadingMsg);
                 if (progressText != null)
                     progressText.setText(message);
             }
         }catch (Exception e){
             e.printStackTrace();
         }
     }
     public static void hideProgressBar(View yourView, View progressView){
         try {
             if (progressView != null && yourView != null) {
                 progressView.setVisibility(View.GONE);
 //                yourView.setVisibility(View.VISIBLE);
             }
         }catch (Exception e){
             e.printStackTrace();
         }
     }*/
    public static final String EXTRA_COME_FROM = "comeFrom";
    /* security  */
    public static String QUICK_PIN = "quickPIN";
    public static String ENABLE_ACCESS = "enablePasscodeAccess";
    public static String SET_TIME_OUT_LIMIT = "passcodeTimeOutLimit";
    public static String SET_TIME_OUT_POS = "passcodeTimeOutPos";
    public static String ENABLE_FINGERPRINT_ACCESS = "enableFingerPrintaccess";
    public static String QUICK_PIN_ACCESS = "PINaccess";
    public static String SECURITY_ON = "Yes";
    public static String SECURITY_OFF = "No";

    public static boolean restartApp(Activity activity, String sharedPrefKey, Class navAct) {
        /*LogonCoreContext lgCtx1 = null;
        try {
            lgCtx1 = LogonCore.getInstance().getLogonContext();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (lgCtx1 == null) {

            SharedPreferences sharedPreferences = activity.getSharedPreferences(sharedPrefKey, 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isAppRestart", true);
            editor.commit();
            Log.e("Restart", "Called");
            activity.finishAffinity();
            Intent dialogIntent = new Intent(activity, navAct);
            activity.startActivity(dialogIntent);
        } else {
            return false;

        }*/
        return false;
    }

    public static void authenticationFailed(Context mContext, View view) {
        Animation shake = AnimationUtils.loadAnimation(mContext, R.anim.shake);
        view.startAnimation(shake);
    }

    public static void displayShortToast(Context mContext, String message) {
        try {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final int ITEM_MAX_LENGTH = 6;
    public static final int ITEM_MAX_LENGTH_3 = 3;

    public static final int PERMISSION_INITIAL_REQUEST = 919;


    public static final int ACTIVITY_RESULT_MATERIAL = 750;
    public static final int ACTIVITY_RESULT_FILTER = 850;
    public static final int ACTIVITY_RESULT_PASSCODE = 1050;
    public static final int ACTIVITY_REQUEST_PASSCODE_ON = 1051;
    public static final int ACTIVITY_REQUEST_PASSCODE_OFF = 1052;
    public static final String ErrorInParser = "Error in initializing the parser!";
    public static final String device_reg_failed_txt = "Device registration failed";
    public static String Password_Key = UtilRegistrationActivity.KEY_password;
    public static String serverHost_key = UtilRegistrationActivity.KEY_serverHost;
    public static String serverPort_key = UtilRegistrationActivity.KEY_serverPort;
    public static String serverClient_key = UtilRegistrationActivity.KEY_serverClient;
    public static String companyid_key = UtilRegistrationActivity.KEY_companyid;
    public static String securityConfig_key = UtilRegistrationActivity.KEY_securityConfig;
    public static String appConnID_key = UtilRegistrationActivity.KEY_appConnID;
    public static String appID_key = UtilRegistrationActivity.KEY_appID;
    public static String isForgotPwdActivated = UtilRegistrationActivity.KEY_isForgotPwdActivated;
    public static String ForgotPwdOTP = UtilRegistrationActivity.KEY_ForgotPwdOTP;
    public static String ForgotPwdGUID = UtilRegistrationActivity.KEY_ForgotPwdGUID;
    public static String isUserIsLocked = UtilRegistrationActivity.KEY_isUserIsLocked;
    public static String SalesPersonMobileNo = "MobileNo";
    public static String BirthDayAlertsDate = UtilRegistrationActivity.KEY_BirthDayAlertsDate;
    public static String BalanceConfirmationHistories = "BalanceConfirmationHistories";
    public static String SpecialGLTypeID = "SpecialGLTypeID";
    public static String SpecialGLTypeIDDesc = "SpecialGLTypeIDDesc";
    public static String ConfirmedOn = "ConfirmedOn";
    public static final String savePass = "savePass";
    public static final String isPasswordSaved = "isPasswordSaved";
    public static final String SHOWNOTIFICATION = "SHOWNOTIFICATION";

    public static void initCustomActionBarView(AppCompatActivity mActivity, Toolbar toolbar, boolean homeUpEnabled, String title, int appIcon) {
        ActionBarView.initActionBarView(mActivity, toolbar, homeUpEnabled, title, appIcon, R.drawable.ic_close);
    }

    public static String getLastSyncTime(Context context, String whereCol, String whereColVal) {
        String lastSyncTime = "";
        String lastSyncDuration = "";
        List<SyncHistoryModel> syncHistoryModelList = new SyncHistoryDB(context).getSyncTimeBySpecificColl(whereCol, whereColVal);
        if (!syncHistoryModelList.isEmpty()) {
            lastSyncTime = syncHistoryModelList.get(0).getTimeStamp();
            try {
                lastSyncDuration = UtilConstants.getTimeAgo(lastSyncTime, context);
            } catch (Exception e) {
                lastSyncDuration = "";
            }
        }
        return lastSyncDuration;
    }

    public static String getLastSyncTimeInMilliSecond(Context context, String whereCol, String whereColVal) {
        String lastSyncTime = "";
        List<SyncHistoryModel> syncHistoryModelList = new SyncHistoryDB(context).getSyncTimeBySpecificColl(whereCol, whereColVal);
        if (!syncHistoryModelList.isEmpty()) {
            lastSyncTime = syncHistoryModelList.get(0).getTimeStamp();
        }
        return lastSyncTime;
    }


    public static long getMilliSeconds(String givenDateString) {
        long timeInMilliseconds = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            Date mDate = sdf.parse(givenDateString);
            timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeInMilliseconds;
    }

    public static String getLastRefreshedTime(Context context, String lastSyncTime) {
        if (!TextUtils.isEmpty(lastSyncTime)) {
            Calendar smsTime = Calendar.getInstance();
            long timeMill = getMilliSeconds(lastSyncTime);
            if (timeMill>0) {
                smsTime.setTimeInMillis(timeMill);
                Calendar now = Calendar.getInstance();
                final String timeFormatString = "h:mm aa";
                final String dateTimeFormatString = "dd-MMM-yyyy, h:mm aa";
                final long HOURS = 60 * 60 * 60;
                if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
                    return "today at " + android.text.format.DateFormat.format(timeFormatString, smsTime);
                } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
                    return "yesterday at " + android.text.format.DateFormat.format(timeFormatString, smsTime);
                } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
                    return "at " + android.text.format.DateFormat.format(dateTimeFormatString, smsTime).toString();
                } else {
                    return "at " + android.text.format.DateFormat.format("dd-MMM-yyyy, h:mm aa", smsTime).toString();
                }
            }else {
                return "";
            }
        }else {
            return "";
        }
    }

    /*update sync time in sqlite db*/
    public static void updatingSyncTime(Context mContext, ArrayList<String> alAssignColl) {
        try {
            String syncTime = UtilConstants.getSyncHistoryddmmyyyyTime();
            for (String colName : alAssignColl) {
                if (colName.contains("?$")) {
                    String splitCollName[] = colName.split("\\?");
                    colName = splitCollName[0];
                }
                new SyncHistoryDB(mContext).updateRecord(colName, syncTime);
            }

        } catch (Exception exce) {
            LogManager.writeLogError(UtilConstants.sync_table_history_txt + exce.getMessage());
        }
    }

    public static void updateAllSyncHistory(Context context, String[] definingReqArray) {
        String syncTime = UtilConstants.getSyncHistoryddmmyyyyTime();
        SyncHistoryDB syncHistoryDB = new SyncHistoryDB(context);
        for (String aDefiningReqArray : definingReqArray) {
            String collection = aDefiningReqArray;
            if (collection.contains("?$")) {
                String splitCollName[] = collection.split("\\?");
                collection = splitCollName[0];
            }

            syncHistoryDB.updateRecord(collection, syncTime);
        }
    }

    public static final String sync_table_history_txt = "Sync table(History)";

    public static String getLastSeenDateFormat(Context context, long smsTimeInMilis) {
        if (smsTimeInMilis>0) {
            Calendar smsTime = Calendar.getInstance();
            smsTime.setTimeInMillis(smsTimeInMilis);
            Calendar now = Calendar.getInstance();
            final String timeFormatString = "h:mm aa";
            final String dateTimeFormatString = "dd-MMM-yyyy, h:mm aa";
            final long HOURS = 60 * 60 * 60;
            if (now.get(Calendar.DATE) == smsTime.get(Calendar.DATE)) {
                return "today at " + android.text.format.DateFormat.format(timeFormatString, smsTime);
            } else if (now.get(Calendar.DATE) - smsTime.get(Calendar.DATE) == 1) {
                return "yesterday at " + android.text.format.DateFormat.format(timeFormatString, smsTime);
            } else if (now.get(Calendar.YEAR) == smsTime.get(Calendar.YEAR)) {
                return "at " + android.text.format.DateFormat.format(dateTimeFormatString, smsTime).toString();
            } else {
                return "at " + android.text.format.DateFormat.format("dd-MMM-yyyy, h:mm aa", smsTime).toString();
            }
        }else {
            return "";
        }
    }

    public static String getDateFormat(Context context, long smsTimeInMilis) {
        try {
            if (smsTimeInMilis>0) {
                Calendar smsTime = Calendar.getInstance();
                smsTime.setTimeInMillis(smsTimeInMilis);
                Calendar now = Calendar.getInstance();
                final String timeFormatString = "h:mm aa";//2018-12-26
                final String dateTimeFormatString = "yyyy-MM-dd";
                return "" + android.text.format.DateFormat.format("MM/dd/yyyy HH:mm:ss", smsTime).toString();
            }else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean isReadWritePermissionEnabled(final Context mContext, Activity mActivity) {
        boolean isPermissionGranted = false;

        if (Build.VERSION_CODES.M <= android.os.Build.VERSION.SDK_INT) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                        ) {
                    ActivityCompat.requestPermissions(mActivity, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CONSTANT);
                } else if (getPermissionStatus(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) || getPermissionStatus(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    dialogBoxWithCallBack(mContext, "", mContext.getString(R.string.this_app_needs_storage_permission), mContext.getString(R.string.enable), mContext.getString(R.string.later), false, new DialogCallBack() {
                        @Override
                        public void clickedStatus(boolean clickedStatus) {
                            if (clickedStatus){
                                navigateToAppSettingsScreen(mContext);
                            }
                        }
                    });
                } else {
                    ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            STORAGE_PERMISSION_CONSTANT);
                }
                setPermissionStatus(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
            } else {
                //You already have the permission, just go ahead.
                isPermissionGranted = true;
            }
        } else {
            isPermissionGranted = true;
        }

        return isPermissionGranted;
    }
   /* public static String getNameSpaceOnline(OnlineODataStore oDataOfflineStore) {
        String mStrNameSpace = "";
        ODataMetadata oDataMetadata = null;

        try {
            oDataMetadata = oDataOfflineStore.getMetadata();
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        Set set = oDataMetadata.getMetaNamespaces();
        if(set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();

            while(itr.hasNext()) {
                String tempNameSpace = itr.next().toString();
                if(!tempNameSpace.equalsIgnoreCase("OfflineOData")) {
                    mStrNameSpace = tempNameSpace;
                }
            }
        }

        return mStrNameSpace;
    }*/
    public static String getCurrencyFormat(String currencyCode, String mStrAmount) {

        String mStrConAmount = mStrAmount;

        try {
            NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
            if (!TextUtils.isEmpty(currencyCode)) {
                format.setCurrency(Currency.getInstance(currencyCode));
                if (!TextUtils.isEmpty(mStrAmount))
                    mStrConAmount = format.format(new BigDecimal(mStrAmount));
                else
                    mStrConAmount = format.format(new BigDecimal(0));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mStrConAmount;
    }

    @Deprecated
    public static GregorianCalendar convertDateDeviceFormatToCalender(Context context,String strDate) {
        String pattern = getDeviceDateFormat(context);
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        GregorianCalendar cal = new GregorianCalendar();
        if(date!=null)
        cal.setTime(date);
        return cal;
    }
    public static GregorianCalendar convertDateDeviceFormatToCalender(Context context,String strDate, String configDateFormat) {
        String pattern = getDeviceDateFormat(context);
        if (!TextUtils.isEmpty(configDateFormat)){
            pattern = configDateFormat;
        }
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        GregorianCalendar cal = new GregorianCalendar();
        if(date!=null)
            cal.setTime(date);
        return cal;
    }

    public static ArrayList<Object> getPendingCollList(Context mContext, ArrayList<String> alEntityList,String sharedPrefKey) {
        ArrayList<Object> objectsArrayList = new ArrayList<>();
        int mIntPendingCollVal = 0;
        String[][] invKeyValues = null;
        Set<String> set = new HashSet<>();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(sharedPrefKey, 0);
        if(alEntityList!=null && alEntityList.size()>0){
            invKeyValues = new String[getPendingListSize(mContext,alEntityList,sharedPrefKey)][2];
            for(String entityName : alEntityList){
                set = sharedPreferences.getStringSet(entityName, null);
                if (set != null && !set.isEmpty()) {
                    Iterator itr = set.iterator();
                    while (itr.hasNext()) {
                        invKeyValues[mIntPendingCollVal][0] = itr.next().toString();
                        invKeyValues[mIntPendingCollVal][1] = entityName;
                        mIntPendingCollVal++;
                    }
                }
            }
        }
        if (mIntPendingCollVal > 0) {
            Arrays.sort(invKeyValues, new ArrayComarator());
            objectsArrayList.add(mIntPendingCollVal);
            objectsArrayList.add(invKeyValues);
        }

        return objectsArrayList;

    }

    private static int getPendingListSize(Context mContext,ArrayList<String> alEntityList,String sharedPrefKey) {
        int size = 0;
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(sharedPrefKey, 0);
        if(alEntityList!=null && alEntityList.size()>0){
            for(String entityName : alEntityList){
                Set<String> set = new HashSet<>();
                set = sharedPreferences.getStringSet(entityName, null);
                if (set != null && !set.isEmpty()) {
                    size = size + set.size();
                }
            }
        }
        return size;
    }
    public static String KeyNo = "KeyNo";
    public static String KeyValue = "KeyValue";
    public static String KeyType = "KeyType";
    public static String DataVaultData = "DataVaultData";
    public static String makePendingDataToJsonString(Context context,RegistrationModel regModel) {
        String mStrJson = "";
        ArrayList<Object> objectArrayList = getPendingCollList(context,regModel.getAlEntityNames(),regModel.getShredPrefKey());
        if (!objectArrayList.isEmpty()) {
            String[][] invKeyValues = (String[][]) objectArrayList.get(1);
            JSONArray jsonArray = new JSONArray();
            for (int k = 0; k < invKeyValues.length; k++) {
                JSONObject jsonObject = new JSONObject();
                String store = UtilDataVault.getValueFromDataVault(invKeyValues[k][0].toString(),context,regModel.getDataVaultPassword());
               try {
                    // Add the values to the jsonObject
                    jsonObject.put(KeyNo, invKeyValues[k][0]);
                    jsonObject.put(KeyType, invKeyValues[k][1]);
                    jsonObject.put(KeyValue, store);
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            JSONObject jsonObj = new JSONObject();
            try {
                jsonObj.put(DataVaultData, jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mStrJson = jsonObj.toString();
        }
        return mStrJson;
    }
    public static class ArrayComarator implements Comparator<String[]> {

        @Override
        public int compare(String s1[], String s2[]) {
            BigInteger i1 = null;
            BigInteger i2 = null;
            try {
                i1 = new BigInteger(s1[0]);
            } catch (NumberFormatException e) {
            }

            try {
                i2 = new BigInteger(s2[0]);
            } catch (NumberFormatException e) {
            }

            if (i1 != null && i2 != null) {
                return i1.compareTo(i2);
            } else {
                return s1[0].compareTo(s2[0]);
            }
        }

    }

    public static void removeDataValtFromSharedPref(Context context, String createType, String refDocNo,
                                                                 boolean isRemoveAllData,ArrayList<String> alEntityList,String mSharedPrefKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(mSharedPrefKey, 0);
        if(alEntityList!=null && alEntityList.size()>0){
            for(String strEntityName:alEntityList){
                removeDeviceDocNoFromSharedPref(context, strEntityName, refDocNo, sharedPreferences, isRemoveAllData);
            }
        }
    }

    public static void removeDeviceDocNoFromSharedPref(Context context, String createType, String refDocNo, SharedPreferences sharedPreferences, boolean isRemoveAllData) {
        if (!isRemoveAllData) {
            Set<String> set = new HashSet<>();
            set = sharedPreferences.getStringSet(createType, null);

            HashSet<String> setTemp = new HashSet<>();
            if (set != null && !set.isEmpty()) {
                Iterator itr = set.iterator();
                while (itr.hasNext()) {
                    setTemp.add(itr.next().toString());
                }
            }
            setTemp.remove(refDocNo);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putStringSet(createType, setTemp);
            editor.commit();
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (sharedPreferences.contains(createType)) {
                editor.remove(createType);
                editor.commit();
            }
        }
    }
    public static final int STORAGE_PERMISSION = 891;
    public static boolean checkStoragePermission(final Activity activity) {
        boolean isPermissionEnable = false;
        if (Build.VERSION_CODES.M <= android.os.Build.VERSION.SDK_INT) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale((activity), Manifest.permission.READ_EXTERNAL_STORAGE)
                        && ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION);
                } else if (getPermissionStatus(activity, Manifest.permission.READ_EXTERNAL_STORAGE) && getPermissionStatus(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {

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
                            STORAGE_PERMISSION);
                }
                setPermissionStatus(activity, Manifest.permission.READ_EXTERNAL_STORAGE, true);
                setPermissionStatus(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE, true);
            } else {
                //You already have the permission
                isPermissionEnable = true;
            }
        } else {
            isPermissionEnable = true;
        }
        return isPermissionEnable;
    }
    public static boolean isFileExits(String fileName) {
        boolean isFileExits = false;
        try {
            File sdCardDir = Environment.getExternalStorageDirectory();
            // Get The Text file
            File txtFile = new File(sdCardDir, fileName);
            // Read the file Contents in a StringBuilder Object
            if (txtFile.exists()) {
                isFileExits = true;
            } else {
                isFileExits = false;
            }
        } catch (Exception e) {
            isFileExits = false;
            e.printStackTrace();
            LogManager.writeLogError("isFileExits() : " + e.getMessage());
        }
        return isFileExits;
    }

    public static String getTextFileData(String fileName) {
        // Get the dir of SD Card
        File sdCardDir = Environment.getExternalStorageDirectory();
        // Get The Text file
        File txtFile = new File(sdCardDir, fileName);
        // Read the file Contents in a StringBuilder Object
        StringBuilder text = new StringBuilder();
        if(txtFile.exists()){
            try {
                BufferedReader reader = new BufferedReader(new FileReader(txtFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    text.append(line);
                }
                reader.close();
            } catch (IOException e) {
                Log.e("C2c", "Error occured while reading text file!!");
                LogManager.writeLogError("getTextFileData() : (IOException)" + e.getMessage());
            }
        }else{
            text.append("");
        }
        return text.toString();
    }
    public static void setJsonStringDataToDataVault(String mJsonString,Context context,String mSharedPrefKey, String dataVaultPsw){
        try {
            JSONObject jsonObj = new JSONObject(mJsonString);
            // Getting data JSON Array nodes
            JSONArray jsonArray  = jsonObj.getJSONArray(DataVaultData);
            for (int incVal = 0; incVal < jsonArray.length(); incVal++) {
                JSONObject jsonObject = jsonArray.getJSONObject(incVal);
                String mStrKeyNo = jsonObject.getString(KeyNo);
                String mStrKeyKeyType = jsonObject.getString(KeyType);
                String mStrKeyValue = jsonObject.getString(KeyValue);
                saveDeviceDocNoToSharedPref(context, mStrKeyKeyType, mStrKeyNo,mSharedPrefKey);
                UtilDataVault.storeInDataVault(mStrKeyNo, mStrKeyValue, context, dataVaultPsw);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void saveDeviceDocNoToSharedPref(Context context, String createType, String refDocNo,String mSharedPrefKey) {
        Set<String> set = new HashSet<>();
        SharedPreferences sharedPreferences = context.getSharedPreferences(mSharedPrefKey, 0);
        set = sharedPreferences.getStringSet(createType, null);

        HashSet<String> setTemp = new HashSet<>();
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                setTemp.add(itr.next().toString());
            }
        }
        setTemp.add(refDocNo);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(createType, setTemp);
        editor.commit();
    }

    public static void deleteFileFromDeviceStorage(String mStrFileName){
        String filePath = Environment.getExternalStorageDirectory()+"/"+mStrFileName;
        try {
            File f = new File(filePath);
            Boolean deleted = f.delete();
        } catch (Exception e) {
            e.printStackTrace();
            LogManager.writeLogError("deleteFileFromDeviceStorage() : " + e.getMessage());
        }
    }

    public static String getPuserIdUtilsReponse(URL url, String userName, String psw) throws IOException {
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
            String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes("UTF-8"), Base64.NO_WRAP);
            connection.setRequestProperty("Authorization", basicAuth);
            connection.setRequestProperty("Content-Type", "application/scim+json");
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
                result = readResponse(stream);
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public static String getPswResetUtilsReponse(URL url, String userName, String psw, String body) throws IOException {
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
            String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes("UTF-8"), Base64.NO_WRAP);
            connection.setRequestProperty("Authorization", basicAuth);
            connection.setRequestProperty("Content-Type", "application/scim+json");
            // For this use case, set HTTP method to GET.
            connection.setRequestMethod("PUT");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            os.write(body.getBytes("UTF-8"));
            os.close();
            // Already true by default but setting just in case; needs to be true since this request
            // is carrying an input (response) body.

            // Open communications link (network traffic occurs here).
//            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpsURLConnection.HTTP_OK && responseCode != HttpsURLConnection.HTTP_BAD_REQUEST) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            // Retrieve the response body as an InputStream.
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                stream = connection.getInputStream();
                if (stream != null) {
                    // Converts Stream to String with max length of 500.
                    result = readResponse(stream);
                }
            }else {
                stream = connection.getErrorStream();
                if (stream != null) {
                    // Converts Stream to String with max length of 500.
                    result = readResponse(stream);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
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
    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN =   "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20})";;

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }
    public static void showPasswordRemarksDialog(final Activity activity, final PasswordDialogCallbackInterface customDialogCallBack, String title) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.ll_pwd_edit);

        final EditText newPassword = (EditText) dialog.findViewById(R.id.etNewPsw);
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }

        };
        newPassword.setFilters(new InputFilter[] { filter });
        final TextInputLayout tilRemarks = (TextInputLayout) dialog.findViewById(R.id.tilRemarks);
        TextView tvTitle = (TextView) dialog.findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        Button okButton = (Button) dialog.findViewById(R.id.btYes);
        Button cancleButton = (Button) dialog.findViewById(R.id.btNo);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(newPassword.getText().toString())){
//                    System.out.println("Not Valid");
                    tilRemarks.setErrorEnabled(true);
                    tilRemarks.setError("Please enter password");
                }else if(!isValidPassword(newPassword.getText().toString())){
//                    System.out.println("Not Valid");
                    tilRemarks.setErrorEnabled(true);
                    tilRemarks.setError("Password must contain mix of upper and lower case letters as well as digits and one special character(8-20)");
                }else{
                    System.out.println("Valid");
                    dialog.dismiss();
                    if (customDialogCallBack != null) {
                        customDialogCallBack.clickedStatus(true,newPassword.getText().toString());
                    }
                }
            }
        });
        cancleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (customDialogCallBack != null) {
                    customDialogCallBack.clickedStatus(false, "");
                }
            }
        });
        newPassword.addTextChangedListener(new TextWatcher() {
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
    public static boolean validMobileNo(String mobileNo){
        boolean isValidMobile=false;
        String regex = "(0/91)?[6-9][0-9]{9}";
        if(mobileNo.matches(regex)){
            isValidMobile=true;
        }else{
            isValidMobile=false;
        }
        return isValidMobile;
    }

    public static boolean validateGSTIN(String gstNo) {
        boolean isValidGst=false;

        String strGstPattren="([0]{1}[1-9]{1}|[1-2]{1}[0-9]{1}|[3]{1}[0-7]{1})([a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9a-zA-Z]{1}[zZ]{1}[1-9a-zA-Z]{1})";
        Pattern pattern = Pattern.compile(strGstPattren);

        Matcher matcher = pattern.matcher(gstNo);

        if (!matcher.matches()) {
            isValidGst=false;
        }else{
            isValidGst=true;
        }
        return isValidGst;


    }
    public static String convertCalenderToStringFormat1(GregorianCalendar calendar) {
        String dateFormatted = "";
        if (calendar != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            simpleDateFormat.setCalendar(calendar);
            dateFormatted = simpleDateFormat.format(calendar.getTime());
        }
        return dateFormatted+"T00:00:00";
    }

    public static String getNoOfDays(int days) {
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -days);
        return simpleDateFormat.format(cal.getTime()) + "T00:00:00";
    }

    public static String getNewDate1() {
        String currentDateTimeString1 = (String) android.text.format.DateFormat
                .format("dd-MM-yyy", new Date());
        String currentDateTimeString2 = (String) android.text.format.DateFormat
                .format("hh:mm:ss", new Date());
        String currentDateTimeString = currentDateTimeString1;
        return getTimeformat2(currentDateTimeString, null);
    }

    public static String returnTimeFormat(String strTime) {
        String time = strTime.replaceAll("PT","");
        String finalTime = time.replaceAll("H",":");
        String finalTime1 = finalTime.replaceAll("M",":");
        return finalTime1.replaceAll("S","");
    }
}
