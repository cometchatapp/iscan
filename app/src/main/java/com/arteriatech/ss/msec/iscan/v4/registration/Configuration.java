package com.arteriatech.ss.msec.iscan.v4.registration;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.store.OfflineManager;

/**
 * Created by e10769 on 06-07-2017.
 */

public class Configuration {
    public static final String quodecAdminUserName = "dataadmin";
    public static final String quodecAdminPassword= "admin123";
    public static String APP_ID_COMMOM = "com.arteriatech.bil.common";


    // Britannia-QA s4q
    public static String server_Text = "mobile-a37nhdlgke.hana.ondemand.com", port_Text = "443",
            server_Text_default = "mobile-a37nhdlgke.hana.ondemand.com",
            server_Text_DR = "mobile-yilnr1oyk9.hana.ondemand.com",
            secConfig_Text = "GW", farm_ID = "", suffix = "";
    public static String server_Text_AP1 = "mobile-mhxye54iok.ap1.hana.ondemand.com";
    public static String INVOICE_URL = "https://oiview-a37nhdlgke.dispatcher.hana.ondemand.com/index.html?t=";
    public static boolean IS_HTTPS = true;
    public static String path1 = "odata";
    public static String path2 = "applications";
    public static String path3 = "latest";
    public static String Landscape = "QA";
    public static String PUGW_APP_ID = "com.arteriatech.pcgw.hana";
    public static String APP_ID_Postjds = "com.arteriatech.bil.postjds";
    public static String APP_ID = "com.arteriatech.mSecSales";
    public static String APP_ID_ENTITY_DATA = "com.arteriatech.bil.entitydata";
    public static String APP_ID_data = "com.arteriatech.hana.spgw";
    public static String APP_ID_RES = "com.arteriatech.res";
    public static String PING_APP_ID = "com.arteriatech.pingserver";
    public static String IDPURL = "";
    public static String IDPTUSRNAME = "";
    public static String IDPTUSRPWD = "";
    public static String LANDSCAPE = "S4Q";
    public static int connectionTimeOut = 60000;
    public static String APP_ID_FR = "com.arteriatech.bil.fr";
    public static String USER_NAME_DEVICE_ID = "";
    public static String USER_PASSWORD_DEVICE_ID = "";
    public static String FACE_RECOGNITION = "https://face-recognition.infilect.com/api/v1/recognition/";
    public static String FACE_RECOGNITION_TOKEN = "235e8c66142597eb76391db4c07c6d6ee6794f32";
    public static final String STACK_BOX_APIURL = "https://api.staging.stackbox.xyz";
    public static final String STACK_BOX_API_KEY = "4ff95edf-62e4-4396-ab49-f62fd776c223";
    public static String UserName = "";
    public static String Password = "";
    public static String b4uUrl = "https://test.saluto.in/britannia/Login";
    public static String IMG_APP_ID = "com.arteriatech.bil.promo.img";
    public static String APP_ID_doc = "com.arteriatech.bil.doc";
    public static String APP_ID_docap1 = "com.arteriatech.bil.doc";
    public static String Recognition = "recognition";
    public static String APP_ID_invoice = "com.arteriatech.postinvoices";
    public static String CRS_APP_ID = "com.arteriatech.quotations";
    public static String mSFA_APP_ID = "com.arteriatech.mSFA";

    // BRIT S4R
   /* public static String server_Text = "mobile-yilnr1oyk9.hana.ondemand.com", port_Text = "443",
            server_Text_default = "mobile-yilnr1oyk9.hana.ondemand.com",
            server_Text_DR = "mobile-a37nhdlgke.hana.ondemand.com",
            secConfig_Text = "GW", farm_ID = "", suffix = "";
    public static String Landscape = "S4R";
    public static String path1 = "odata";
    public static String path2 = "applications";
    public static String path3 = "latest";
    public static String server_Text_AP1 = "mobile-mhxye54iok.ap1.hana.ondemand.com";
    public static boolean IS_HTTPS = true;
    public static String APP_ID = "com.arteriatech.mSecSales";
    public static String INVOICE_URL = "https://oiview-yilnr1oyk9.dispatcher.hana.ondemand.com/index.html?t=";
    public static String PUGW_APP_ID = "com.arteriatech.pcgw.hana";
    public static String APP_ID_data = "com.arteriatech.hana.spgw";
    public static String APP_ID_ENTITY_DATA = "com.arteriatech.bil.entitydata";
    public static String APP_ID_RES = "com.arteriatech.res";
    public static String IDPURL = "https://arteria7.accounts.ondemand.com/";
    public static String IDPTUSRNAME = "a7c5a0b6-704c-4328-aa88-2c4d28d304e3";
    public static String IDPTUSRPWD = "BILap1@2020";
    public static String LANDSCAPE = "S4R";
    public static String USER_NAME_DEVICE_ID = "";
    public static String USER_PASSWORD_DEVICE_ID = "";
    public static String FACE_RECOGNITION ="https://face-recognition.infilect.com/api/v1/recognition/";
    public static String FACE_RECOGNITION_TOKEN ="d3eecb0f26bd5ad30bdec266bd5dea201c583a1f";
    public static final String STACK_BOX_APIURL = "https://api.staging.stackbox.xyz";
    public static final String STACK_BOX_API_KEY = "4ff95edf-62e4-4396-ab49-f62fd776c223";
    public static int connectionTimeOut =60000;
    public static String APP_ID_doc = "com.arteriatech.bil.doc";
    public static String APP_ID_docap1 = "com.arteriatech.bil.doc";
    public static String APP_ID_FR = "com.arteriatech.bil.fr";
    public static String APP_ID_Postjds = "com.arteriatech.bil.postjds";
    public static String UserName="";
    public static String Password="";
    public static String b4uUrl="https://test.saluto.in/britannia/Login";
    public static String PING_APP_ID = "com.arteriatech.pingserver";
    public static String IMG_APP_ID = "com.arteriatech.bil.promo.img";
    public static String Recognition = "recognition";
    public static String APP_ID_invoice = "com.arteriatech.postinvoices";
    public static String CRS_APP_ID = "com.arteriatech.quotations";
    public static String mSFA_APP_ID = "com.arteriatech.mSFA";*/

    // Britannia-PRD
   /* public static String server_Text = "mobile-njgf2wk7xy.eu2.hana.ondemand.com", port_Text = "443",
            server_Text_default = "mobile-njgf2wk7xy.eu2.hana.ondemand.com",
            server_Text_DR = "mobile-mj2hxhh5mc.ap1.hana.ondemand.com",
            secConfig_Text = "GW", farm_ID = "", suffix = "";
    public static boolean IS_HTTPS = true;
    public static String Landscape = "";
    public static String path1 = "odata";
    public static String path2 = "applications";
    public static String path3 = "latest";
    public static String INVOICE_URL = "https://oiview-xcmu84siv3.dispatcher.hana.ondemand.com/index.html?t=";
    public static String APP_ID = "com.arteriatech.mSecSales";
    public static String APP_ID_Postjds = "com.arteriatech.bil.postjds";
    public static String APP_ID_data = "com.arteriatech.hana.spgw";
    public static String PUGW_APP_ID = "com.arteriatech.pcgw.hana";
    public static String APP_ID_doc = "com.arteriatech.bil.doc";
    public static String APP_ID_docap1 = "com.arteriatech.bil.doc.ap1";
    public static String APP_ID_FR = "com.arteriatech.bil.fr";
    public static String APP_ID_RES = "com.arteriatech.res";
    public static String APP_ID_ENTITY_DATA = "com.arteriatech.bil.entitydata";
    public static String IDPURL = "https://arteria7.accounts.ondemand.com/";
    public static String IDPTUSRNAME = "a7c5a0b6-704c-4328-aa88-2c4d28d304e3";
    public static String IDPTUSRPWD = "BILap1@2020";
    public static String LANDSCAPE = "";
    public static String USER_NAME_DEVICE_ID = "";
    public static String USER_PASSWORD_DEVICE_ID = "";
    public static String FACE_RECOGNITION ="https://face-recognition.infilect.in/api/v1/recognition/";
    public static String FACE_RECOGNITION_TOKEN ="235e8c66142597eb76391db4c07c6d6ee6794f32";
    public static final String STACK_BOX_APIURL = "https://api.stackbox.xyz";
    public static final String STACK_BOX_API_KEY = "4ff95edf-62e4-4396-ab49-f62fd776c223";
    public static int connectionTimeOut =60000;
    public static String UserName="";
    public static String Password="";
    public static String b4uUrl="https://www.britannia4u.com";
    public static String PING_APP_ID = "com.arteriatech.pingserver";
    public static String IMG_APP_ID = "com.arteriatech.bil.promo.img";
    public static String APP_ID_invoice = "com.arteriatech.postinvoices";
    public static String CRS_APP_ID = "com.arteriatech.quotations";
    public static String mSFA_APP_ID = "com.arteriatech.mSFA";
    public static String Recognition = "recognition";*/
    public static String IscanSOCreate="IscanSOCreate";

//     Britannia-PRD-DR
//    public static String server_Text = "mobile-mhxye54iok.ap1.hana.ondemand.com", port_Text = "443",
//            secConfig_Text = "GW", farm_ID = "", suffix = "";
//    public static boolean IS_HTTPS = true;
//    public static String APP_ID = "com.arteriatech.mSecSales";
//    public static String IDPURL = "https://arteria7.accounts.ondemand.com/";
//    public static String IDPTUSRNAME = "a7c5a0b6-704c-4328-aa88-2c4d28d304e3";
//    public static String IDPTUSRPWD = "BILap1@2020";
//    public static String LANDSCAPE = "";
//    public static String USER_NAME_DEVICE_ID = "";
//    public static String USER_PASSWORD_DEVICE_ID = "";
//    public static String FACE_RECOGNITION ="https://face-recognition.infilect.in/api/v1/recognition/";
//    public static String FACE_RECOGNITION_TOKEN ="235e8c66142597eb76391db4c07c6d6ee6794f32";
//    public static final String STACK_BOX_APIURL = "https://api.stackbox.xyz";
//    public static final String STACK_BOX_API_KEY = "4ff95edf-62e4-4396-ab49-f62fd776c223";
//    public static int connectionTimeOut =60000;
//    public static String UserName="";
//    public static String Password="";
//    public static String b4uUrl="https://www.britannia4u.com";

    //BRIT new
    /*public static String server_Text = "mobile-mhxye54iok.ap1.hana.ondemand.com", port_Text = "443",
            secConfig_Text = "GW", farm_ID = "", suffix = "";
    public static boolean IS_HTTPS = true;
    public static String APP_ID = "com.arteriatech.mSecSales";
    public static String IDPURL = "https://arteria7.accounts.ondemand.com/";
    public static String IDPTUSRNAME = "a7c5a0b6-704c-4328-aa88-2c4d28d304e3";
    public static String IDPTUSRPWD = "BILap1@2020";
    public static String LANDSCAPE = "S4R";
    public static String USER_NAME_DEVICE_ID = "";
    public static String USER_PASSWORD_DEVICE_ID = "";
    public static final String STACK_BOX_APIURL = "https://api.staging.stackbox.xyz";
    public static final String STACK_BOX_API_KEY = "4ff95edf-62e4-4396-ab49-f62fd776c223";*/


    public static String getStackBoxAPIURL(){
        String value="";
        try {
            if (OfflineManager.isOfflineStoreOpen()) {
                value = OfflineManager.getValueByColumnName(Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Typeset + " eq '" + Constants.MSEC + "' and " + Constants.Types + " eq '"+Constants.STACKBOX+"' &$top=1", Constants.TypeValue);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (value!=null&&!TextUtils.isEmpty(value)){
            return value;
        }else{
            return STACK_BOX_APIURL;
        }
    }
    public static String getStackBoxAPIKey(){
        String value="";
        try {
            if (OfflineManager.isOfflineStoreOpen()) {
                value = OfflineManager.getValueByColumnName(Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Typeset + " eq '" + Constants.MSEC + "' and " + Constants.Types + " eq '"+Constants.STBXKEY+"' &$top=1", Constants.TypeValue);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if (value!=null&&!TextUtils.isEmpty(value)){
            return value;
        }else{
            return STACK_BOX_API_KEY;
        }
    }

    public static void getAutoLogOffValue(Context context){
        String value="";
        try {
            if (OfflineManager.isOfflineStoreOpen()) {
                value = OfflineManager.getValueByColumnName(Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Typeset + " eq '" + Constants.MSEC + "' and " + Constants.Types + " eq '"+Constants.AUTOLOGOFF+"' &$top=1", Constants.TypeValue);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if(TextUtils.isEmpty(value)){
            value= ""+45;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.AUTOLOGOFF,value);
        editor.apply();
    }

    public static void getAutoPOPOffValue(Context context){
        String value="";
        try {
            if (OfflineManager.isOfflineStoreOpen()) {
                value = OfflineManager.getValueByColumnName(Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Typeset + " eq '" + Constants.MSEC + "' and " + Constants.Types + " eq '"+Constants.AUTOPOPOFF+"' &$top=1", Constants.TypeValue);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        if(TextUtils.isEmpty(value)){
            value= ""+10000;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.AUTOPOPOFF,value);
        editor.apply();
    }

}
