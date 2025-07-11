package com.arteriatech.ss.msec.iscan.v4.common;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.inputmethodservice.KeyboardView;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.system.ErrnoException;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.annimon.stream.Stream;
import com.arteriatech.mutils.changepassword.ChangePasswordActivity;
import com.arteriatech.mutils.initialPassword.InitialPasswordActivity;
import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.mutils.registration.RegistrationModel;
import com.arteriatech.mutils.registration.UtilRegistrationActivity;
import com.arteriatech.ss.msec.iscan.v4.DeviceIDUtils;
import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.mbo.BirthdaysBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.Config;
import com.arteriatech.ss.msec.iscan.v4.mbo.ConfigTypsetTypeValuesBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.DmsDivQryBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.ErrorBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.MTPRoutePlanBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.RemarkReasonBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.RetailerBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.ReturnOrderBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.SKUGroupBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.SOCreateBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.SchemeBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.SchemeCalcuBean;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.OfflineODataStoreException;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.Operation;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UIListener;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilOfflineManager;
import com.arteriatech.ss.msec.iscan.v4.mutils.interfaces.DialogCallBack;
import com.arteriatech.ss.msec.iscan.v4.mutils.location.LocationInterface;
import com.arteriatech.ss.msec.iscan.v4.mutils.location.LocationModel;
import com.arteriatech.ss.msec.iscan.v4.mutils.location.LocationUtils;

import com.arteriatech.ss.msec.iscan.v4.registration.Configuration;
import com.arteriatech.ss.msec.iscan.v4.reports.invoicelist.InvoiceListBean;

import com.arteriatech.ss.msec.iscan.v4.store.OfflineManager;
import com.arteriatech.ss.msec.iscan.v4.sync.SyncInfo.RefreshListInterface;
import com.arteriatech.ss.msec.iscan.v4.sync.SyncSelectionActivity;
import com.arteriatech.ss.msec.iscan.v4.sync.SyncUtils;
import com.arteriatech.ss.msec.iscan.v4.ui.DialogFactory;
import com.arteriatech.ss.msec.iscan.v4.ui.OnDialogClick;
import com.arteriatech.ss.msec.iscan.v4.utils.OfflineUtils;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.sap.client.odata.v4.core.CharBuffer;
import com.sap.client.odata.v4.core.GUID;
import com.sap.client.odata.v4.core.StringFunction;
import com.sap.smp.client.httpc.events.IReceiveEvent;
import com.sap.smp.client.odata.ODataDuration;
import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.ODataGuid;
import com.sap.smp.client.odata.ODataPropMap;
import com.sap.smp.client.odata.ODataProperty;
import com.sap.smp.client.odata.impl.ODataDurationDefaultImpl;
import com.sap.smp.client.odata.offline.ODataOfflineException;
import com.sap.smp.client.odata.offline.ODataOfflineStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import static android.content.Context.MODE_PRIVATE;
import static com.arteriatech.ss.msec.iscan.v4.store.OfflineManager.offlineStore;

/**
 * Created by e10769 on 12-Apr-18.
 */

public class Constants {
    public static final String AUTOLOGOFF = "AUTOLOGOFF";
    public static final String AUTOPOPOFF = "AUTOPOPOFF";
    public static final String uom = "uom";
    public static final String minQty = "minQty";
    public static final String maxQty = "maxQty";
    public static final String KATS = "KATS";
    public static String PRIBILLDAY = "PRIBILLDAY";
    public static String CP_NAME = "CP_NAME";
    public static String CP_NO = "CP_NO";
    public static String PI_POst = "0000017";
    public static String RefDocItemUOM = "RefDocItemUOM";
    public static String RefDocItemQty = "RefDocItemQty";
    public static String refDocNo = "RefDocNo";
    public static String ProposedDlvQty = "ProposedDlvQty";
    public static String Tray = "Tray";
    public static String Loaf = "Loaf";
    public static String ViewIndents = "ViewIndents";
    public static String DashBoard = "DashBoard";
    public static String NetWeight = "NetWeight";
    public static String AlterQty = "AlterQty";
    public static String AlterUOM = "AlterUOM";
    public static String intentFrom = "intentFrom";
    public static String DealerInvoices = "DealerInvoices";
    public static final String CheckIn = "CheckIn";
    public static final String CheckOut = "CheckOut";
    public static final String Start_Factory = "Start Factory";
    public static String stop_ringtone = "stop_sound";
    public static String alert_message = "alert_message";
    public static final String VanDriver_NAME = "VanDriver_NAME";
    public static String isReg = "isReg";
    public static final String B1 = "B1";
    public static final String B2 = "B2";
    public static final String B3 = "B3";
    public static final String B4 = "B4";
    public static final String B5 = "B5";
    public static final String B6 = "B6";
    public static final String B7 = "B7";
    public static final String B8 = "B8";
    public static String DeviationRemarks = "DeviationRemarks";
    public static String VisitPayload = "VisitPayload";
    public static String beatName = "BEAT_NAME";
    public static String ActualSeq = "ActualSeq";
    public static final String Accept = "Accept";
    public static final String Reject = "Reject";
    public static final String AutoMaterialDocs = "AutoMaterialDocs";
    public static final String ZZIMGHTTPS = "ZZIMGHTTPS";
    public static final String ZZIMGHOST = "ZZIMGHOST";
    public static final String ZNI = "ZNI";
    public static final String ZZIMGSFX1 = "ZZIMGSFX1";
    public static final String ZZIMGSFX2 = "ZZIMGSFX2";
    public static final String ZZIMGPARM1 = "ZZIMGPARM1";
    public static final String ZZIMGPARM2 = "ZZIMGPARM2";
    public static final String introScreen = "introScreen";
    public static final String QRCODEVISIBLE="QRCODEVISIBLE";
    public static final String DocumentTypes = "DocumentTypes";
    public static final String SurveyResultItemGUID="SurveyResultItemGUID";
    public static final String SurveyResultGUID="SurveyResultGUID";
    public static final String QuinSrvGUID="QuinSrvGUID";
    public static final String QuinSrvOptGUID="QuinSrvOptGUID";
    public static final String OptionID="OptionID";
    public static final String Value="Value";
    public static final String ImageURL="ImageURL";
    public static final String SurveyRsltHdr="SurveyRsltHdr";
    public static final String DocObjectDetails="DocObjectDetails";
    public static final String SurveyGUID="SurveyGUID";
    public static final String SurveyDate="SurveyDate";
    public static final String SurveyByTypeID="SurveyByTypeID";
    public static final String SurveyByID="SurveyByID";
    public static final String SurveyByName="SurveyByName";
    public static final String SurveyByNo="SurveyByNo";
    public static final String SurveyRsltItem="SurveyRsltItem";
    public static final String SurveyRsltHdrType="SurveyRsltHdrType";
    public static final String SurveyRsltItemType="SurveyRsltItemType";
    public static final String Surveys="Surveys";
    public static final String SurveyGeo="SurveyGeo";
    public static final String SurveyQuestions="SurveyQuestions";
    public static final String SurveyQuestionOptions="SurveyQuestionOptions";
    public static final String OSAISHTTPS="OSAISHTTPS";
    public static final String ZZMINVLG1="ZZMINVLG1";
    public static final String ZZMINVLG2="ZZMINVLG2";
    public static final String ZZMXBILCUT="ZZMXBILCUT";
    public static final String MATSEQ="MATSEQ";
    public static final String OSAHOSTNM="OSAHOSTNM";
    public static final String OSAURLSFX1="OSAURLSFX1";
    public static final String OSAURLSFX2="OSAURLSFX2";
    public static final String OSAURLSFX3="OSAURLSFX3";
    public static final String OSAKEY1="OSAKEY1";
    public static final String OSAKEY2="OSAKEY2";
    public static final String OSAKEY3="OSAKEY3";
    public static final String OSAKEY4="OSAKEY4";
    public static final String OSACID="OSACID";
    public static final String ZODRSN = "ZODRSN";
    public static String DISCPGUID = "DISCPGUID";
    public static String DISCPNO = "DISCPNO";
    public static String DISCPNAME = "DISCPNAME";
    public static String ATT_POst = "000011";
    public static final String FACE_REINIT = "FACE_REINIT";
    public static int IntializeDBVersion = 1;
    public static final String EXTRA_CRASHED_FLAG = "EXTRA_CRASHED_FLAG";
    public static final String CURRENT_VERSION_CODE = "currentVersionCode";
    public static final String INTIALIZEDB = "intializedb";
    public static final String INTENT_EXTRA_BILL_SKU_BEAN="INTENT_EXTRA_BILL_SKU_BEAN";
    public static final String INTENT_EXTRA_SO_BIL_BEAN="INTENT_EXTRA_SO_BIL_BEAN";
    public static final String ONBACKPRESS="ONBACKPRESS";
    public static final String INTENT_EXTRA_RETAILER_BEAN="INTENT_EXTRA_RETAILER_BEAN";
    public static final String INTENT_EXTRA_ORDER_BROWSER = "INTENT_EXTRA_ORDER_BROWSER";
    public static final String INTENT_EXTRA_ORDER_ENTRY = "INTENT_EXTRA_ORDER_ENTRY";
    public static final String INTENT_EXTRA_COLLECTION_NEW= "INTENT_EXTRA_COLLECTION_NEW";
    public static final String INTENT_EXTRA_RETURN_ORDER= "INTENT_EXTRA_RETURN_ORDER";
    public static final String INTENT_EXTRA_COLLECTION= "INTENT_EXTRA_COLLECTION";
    public static final String INTENT_EXTRA_EOD= "INTENT_EXTRA_EOD";
    public static final String INTENT_EXTRA_BILLING= "INTENT_EXTRA_BILLING";
    public static final String INTENT_EXTRA_BILLING_BACK= "INTENT_EXTRA_BILLING_BACK";
    public static final String INTENT_EXTRA_BILL_BROWSER= "INTENT_EXTRA_BILL_BROWSER";
    public static final String INTENT_EXTRA_UPDATE_RETAILER= "INTENT_EXTRA_UPDATE_RETAILER";
    public static final String INTENT_EXTRA_BEATS= "INTENT_EXTRA_BEATS";
    public static final String INTENT_EXTRA_MODIFY_RETAILER= "INTENT_EXTRA_MODIFY_RETAILER";
    public static final String INTENT_EXTRA_GRN_BROWSER= "INTENT_EXTRA_GRN_BROWSER";
    public static final String INTENT_EXTRA_GRN_ENTRY= "INTENT_EXTRA_GRN_ENTRY";
    public static final String INTENT_EXTRA_SALES_ORDER= "INTENT_EXTRA_SALES_ORDER";
    public static final String RETAILER_DETAILS= "RETAILER_DETAILS";
    public static final String BEAT_DETAILS= "BEAT_DETAILS";
    public static final String INVOICE_DETAILS= "INVOICE_DETAILS";
    public static final String ORDER_BROWSER_DETAILS= "ORDER_BROWSER_DETAILS";
    public static final String INTENT_EXTRA_SKU_ITEMS = "INTENT_EXTRA_SKU_ITEMS";
    public static final String SELECTED_UOM_NON_META_PROPERTY = "SELECTED_UOM_NON_META_PROPERTY";
    public static final String OL_LOCKED = "OL_LOCKED";
    public static final String RUPEE_SYMBOL = "\u20B9 ";
    public static final String ENVIRONMENT = "ENVIRONMENT";
    public static final String DEVICE_ID_CHECK = "DEVICE_ID_CHECK";
    public static final String ACCOUNT_ID = "ACCOUNT_ID";
    public static final String PASSWORD_SAVE = "PASSWORD_SAVE";
    public static String SchemeCPsEntity = ".SchemeCP";
    public static String ClaimsEntity = ".Claim";
    public static String ComingFromCreateSenarios = "";
    public static String OtherRouteNameVal = "";
    public static String OtherRouteGUIDVal = "";
    public static String Conv_Mode_Type_Other = "0000000001";
    public static String REPEATABLE_REQUEST_ID = "";
    public static String GeoAccuracy = "GeoAccuracy";
    public static String URL1 = "URL1";
    public static String URL2 = "URL2";
    public static String URL3 = "URL3";
    public static String URL4 = "URL4";
    public static String URL5 = "URL5";
    public static String URL6 = "URL6";
    public static String SPDistGUID = "SPDistGUID";
    public static String AggregatorID = "AggregatorID";
    public static String AddDetails = "AddDetails";
    public static String TimeTaken = "TimeTaken";

    public static String WindowDisplayID = "11";
    public static String WindowDisplayClaimID = "13";
    public static String WindowDisplayValueHelp = "WindowDisplay";
    public static final int TAKE_PICTURE = 190;
    public static String EXTRA_ARRAY_LIST = "arrayList";
    public static String A = "A";
    public static String SchemeCPDocumentID = "SchemeCPDocumentID";
    public static String WindowHeight = "WindowHeight";
    public static String WindowBreadth = "WindowBreadth";
    public static String WindowLength = "WindowLength";
    public static String SchemeCPGUID = "SchemeCPGUID";
    public static String RPD_RETAILER_NAME = "RPD_RETAILER_NAME";
    public static String SchemeCPDocType = "SchemeCPDocType";
    public static String WindowSizeUOM = "WindowUOM";
    public static final String SOS_SO_TASK_ENTITY = ".Task";
    public static String DecisionKey = "DecisionKey";
    public static String Comments = "Comments";
    public static String RejectStatus = "02";
    public static String ApprovalStatus01 = "01";
    public static final String[][] billAges = {{"00", "01", "02", "03", "04"}, {"All", "0 - 30 Days", "31 - 60 Days", "61 - 90 Days", "> 90 Days"}};
    public static final String InvList = "InvList";
    public static final String RTGS = "RTGS";
    public static final String NEFT = "NEFT";
    public static final String DD = "DD";
    public static final String Cheque = "Cheque";
    public static int LAST_SEL_VAL = 0;
    public static final String RetailerChange = "RetailerChange";
    public static final String offlineDBPath = "/data/com.arteriatech.ss.msec.bil/files/mSecSales_Offline.udb";
    public static final String offlineReqDBPath = "/data/com.arteriatech.ss.msec.bil/files/mSecSales_Offline.rq.udb";
    public static boolean Exportdbflag = false;
    public static final String icurrentUDBPath = "/data/com.arteriatech.ss.msec.bil/files/mSecSales_Offline.udb";
    public static final String ibackupUDBPath = "mSecSales_Offline.udb";
    public static final String icurrentRqDBPath = "/data/com.arteriatech.ss.msec.bil/files/mSecSales_Offline.rq.udb";
    public static final String ibackupRqDBPath = "mSecSales_Offline.rq.udb";
    public static String OrderBrowerID = "";
    public static String TransporterModel = "TransporterModel";
    public static String CHKOUTSKIP = "CHKOUTSKIP";
    public static final String RouteUploads = "RouteUploads";
    public static final String ZVisitPlans = "ZVisitPlans";
    public static final String ZCustomerDocLinks = "ZCustomerDocLinks";
    public static final String isDlvryConfDisableTCode = "/ARTEC/DLVRY_CONF_TRSPR_MODEL";
    public static final String isDlvryConfDisableKey = "isDlvryConfDisabled";
    /*datavalt export*/
    public static String KeyNo = "KeyNo";
    public static String KeyValue = "KeyValue";
    public static String KeyType = "KeyType";
    public static String DataVaultData = "DataVaultData";
    public static String DataVaultFileName = "mSecSalesDataVault.txt";
    public static String ULPOTarget = "ULPOTarget";
    public static String ZCPRGT = "ZCPRGT";
    public static String ZLPRGT = "ZLPRGT";
    public static String ZCPRMT = "ZCPRMT";
    public static String ZLPRMT = "ZLPRMT";
    public static String ZCPROK = "ZCPROK";
    public static String ZCPRPR = "ZCPRPR";
    public static String CurrentMonth = "CurrentMonth";
    public static String LastMonth = "LastMonth";
    public static String ZLPRPR = "ZLPRPR";
    public static String ZLPRET = "ZLPRET";

    /*property name*/
    public static final String MobileNoSales = "MobileNoSales";
    public static final String AuthOrgValue = "AuthOrgValue";
    public static final String RoleID = "RoleID";
    public static final String AuthOrgTypeID = "AuthOrgTypeID";
    public static final String KPIGUID = "KPIGUID";
    public static final String HTTP_HEADER_SUP_APPCID = "X-SUP-APPCID";
    public static final String HTTP_HEADER_SMP_APPCID = "X-SMP-APPCID";
    public static final String EXTRA_SO_DETAIL = "openSODetails";
    public static final String EXTRA_PENDING_VIEWCOUNT = "viewCount";
    public static final String DELIVERY_STATUS = "DeliveryStatus";
    public static final String RefdocItmGUID = "RefdocItmGUID";
    public static String MRP = "MRP";
    public static String CPSnoGUID = "CPSnoGUID";
    public static String SPSNoGUID = "SPSNoGUID";
    public static String EXTRA_SSRO_GUID = "extraSSROguid";
    public static String PERCENTAGE = "%";
    public static String EXTRA_ORDER_DATE = "extraDate";
    public static String EXTRA_ORDER_IDS = "extraIDS";
    public static String EXTRA_ORDER_AMOUNT = "extraAmount";
    public static String EXTRA_ORDER_SATUS = "extraStatus";
    public static String EXTRA_ORDER_CURRENCY = "extraCurrency";
    public static String EXTRA_CPGUID = "extraCPGUID";
    public static String EXTRA_SOBEAN = "EXTRA_SOBEAN";
    public static String EXTRA_STOCK_BEAN = "extraStockBean";
    public static String EXTRA_STOCK_OWNER = "extraStockOwner";
    public static String EXTRA_SEARCH_HINT = "extraSearchHint";
    public static String EXTRA_SCHEME_GUID = "extraSchemeGUID";
    public static String AttendanceGUID = "AttendanceGUID";
    public static String StartDate = "StartDate";
    public static String StartTime = "StartTime";
    public static String EndTime = "EndTime";
    public static String StartLat = "StartLat";
    public static String StartLong = "StartLong";
    public static String EndDate = "EndDate";
    public static String EndLat = "EndLat";
    public static String EndLong = "EndLong";
    public static String Etag = "Etag";
    public static String MediaLink = "mediaLink";
    public static String TextCategoryID = "TextCategoryID";
    public static String TextCategoryTypeID = "TextCategoryTypeID";
    public static String TextCategoryDesc = "TextCategoryDesc";
    public static String TextCategoryTypeDesc = "TextCategoryTypeDesc";
    public static String Text = "Text";
    public static String InvoiceHisMatNo = "MaterialNo";
    public static String InvoiceHisMatDesc = "MaterialDesc";
    public static String InvoiceHisAmount = "GrossAmount";
    public static String InvoiceHisQty = "Quantity";
    public static String CompName = "CompName";
    public static String CompGUID = "CompGUID";
    public static String CompInfoGUID = "CompInfoGUID";
    public static String Earnings = "Earnings";
    public static String SchemeAmount = "SchemeAmount";
    public static String SchemeName = "SchemeName";
    public static String SchemeGUID = "SchemeGUID";
    public static String ValidFromDate = "ValidFromDate";
    public static String ValidToDate = "ValidToDate";
    public static String MatGrp1Amount = "MatGrp1Amount";
    public static String MatGrp2Amount = "MatGrp2Amount";
    public static String MatGrp3Amount = "MatGrp3Amount";
    public static String MatGrp4Amount = "MatGrp4Amount";
    public static String UpdatedOn = "UpdatedOn";
    public static String PurchaseQty = "PurchaseQty";
    public static String PurchaseAmount = "PurchaseAmount";
    public static String DealerName = "DealerName";
    public static String DealerCode = "DealerCode";
    public static String DealerType = "DealerType";
    public static String MTDValue = "MTDValue";
    public static String OrderToRecivive = "OrderToRecivive";
    public static String DateofDispatch = "DateofDispatch";
    public static String TradeDate = "TradeDate";
    public static String PriceDate = "PriceDate";
    public static String BrandName = "BrandName";
    public static String HDPE = "HDPE";
    public static String ExpiryDate = "ExpiryDate";
    public static String PaperBag = "PaperBag";
    public static String PriceType = "PriceType";
    public static String diaryCheck = "diaryCheck";
    public static String chitPadCheck = "chitPadCheck";
    public static String bannerCheck = "bannerCheck";
    public static String AmountOne = "AmountOne";
    public static String DateOne = "DateOne";
    public static String SearchText = "SearchText";
    public static String SalesBean = "SalesBean";
    public static String GrnBean = "GrnBean";
    public static String AmountTwo = "AmountTwo";
    public static String DateTwo = "DateTwo";
    public static String VillageBean = "VillageBean";
    public static String AmountThree = "AmountThree";
    public static String DateThree = "DateThree";
    public static String AmountFour = "AmountFour";
    public static String DateFour = "DateFour";
    public static String CPNo = "CPNo";
    public static String CPStock = "CPStock";
    public static String RetailerName = "Name";
    public static String Address2 = "Address2";
    public static String Address3 = "Address3";
    public static String Address4 = "Address4";
    public static String ApprovedAt = "ApprovedAt";
    public static String ApprovedOn = "ApprovedOn";
    public static String ApprvlStatusDesc = "ApprvlStatusDesc";
    public static String BlockID = "BlockID";
    public static String BlockName = "BlockName";
    public static String BuiltupArea = "BuiltupArea";
    public static String BuiltupAreaUom = "BuiltupAreaUom";
    public static String BusinessID1 = "BusinessID1";
    public static String BusinessID2 = "BusinessID2";
    public static String Landline = "Landline";
    public static String MobileVerifed = "MobileVerifed";
    public static String FaxNo = "FaxNo";
    public static String Geo1Desc = "Geo1Desc";
    public static String Geo1ID = "Geo1ID";
    public static String Geo2Desc = "Geo2Desc";
    public static String Geo2ID = "Geo2ID";
    public static String Geo3ID = "Geo3ID";
    public static String Geo4Desc = "Geo4Desc";
    public static String Geo4ID = "Geo4ID";
    public static String Geo3Desc = "Geo3Desc";
    public static String ID1 = "ID1";
    public static String ID2 = "ID2";
    public static String IsCompBillAvl = "IsCompBillAvl";
    public static String IsEduInstNrby = "IsEduInstNrby";
    public static String IsHomedlvryAvl = "IsHomedlvryAvl";
    public static String IsHsptlNearBy = "IsHsptlNearBy";
    public static String IsKeyCP = "IsKeyCP";
    public static String IsPhOrderAvl = "IsPhOrderAvl";
    public static String IsSmartPhAvl = "IsSmartPhAvl";
    public static String ConstructionStageDs = "ConstructionStageDs";
    public static String ConstructionStageID = "ConstructionStageID";
    public static String ConstructionType = "ConstructionType";
    public static String ConstructionTypeDs = "ConstructionTypeDs";
    public static String ContPerson = "ContPerson";
    public static String ContPersonEmail = "ContPersonEmail";
    public static String ContPersonMobile = "ContPersonMobile";
    public static String ApprovedBy = "ApprovedBy";
    public static String TownDesc = "TownDesc";
    public static String TownGuid = "TownGuid";
    public static String ParentID = "ParentID";
    public static String ParentName = "ParentName";
    public static String ParentTypDesc = "ParentTypDesc";
    public static String ParentTypeID = "ParentTypeID";
    public static String ParentTypeDesc = "ParentTypeDesc";
    public static String PartnerMgrName = "PartnerMgrName";
    public static String PartnerMgrNo = "PartnerMgrNo";
    public static String CPTypeDesc = "CPTypeDesc";
    public static String StatusID = "StatusID";
    public static String SourceID = "SourceID";
    public static String Param3 = "Param3";
    public static String Param2 = "Param2";
    public static String Param1 = "Param1";
    public static String FileContent = "FileContent";
    public static final String ProcessDocument= "ProcessDocument";
    public static String BeatDesc = "BeatDesc";
    public static String BeatCode = "BeatCode";
    public static String GRStatus = "GRStatus";
    public static String StatusIdRetailer = "01";
    public static String VisitSeq = "VisitSeq";
    public static String Description = "Description";
    public static String EXTRA_COMPLAINT_BEAN = "ExtraComplaintBean";
    public static String CategoryId = "CategoryId";
    public static String VoiceBalance = "VoiceBalance";
    public static String DataBalance = "DataBalance";
    public static String Last111Date = "Last111Date";
    public static String OutstandingAmt = "OutstandingAmt";
    public static String LastInvAmt = "LastInvAmt";
    public static String NewLaunchedProduct = "New Launched Product";
    public static String MustSellProduct = "Must Sell Product";
    public static String FocusedProduct = "Focused Product";
    public static String SalesOrderCreate = "Sales Order Create";
    public static String StockGuid = "StockGuid";
    public static String MerchReviewGUID = "MerchReviewGUID";
    public static String SPNo = "SPNo";
    public static String SPName = "SPName";
    public static String DiscountPer = "DiscountPer";
    public static String SPGUID = "SPGUID";
    public static String BillTo = "BillTo";
    public static String BillToDes = "BillToDes";
    public static String BEATDESC = "BEATDESC";
    public static String MerchReviewType = "MerchReviewType";
    public static String MerchReviewTypeDesc = "MerchReviewTypeDesc";
    public static String MerchReviewTime = "MerchReviewTime";
    public static String CreatedBy = "CreatedBy";
    public static String AccountGrp = "AccountGrp";
    public static String History = "History";
    public static String Pending = "Pending Sync";
    public static String ActivationDate = "ActivationDate";
    public static String GSTActiveDate = "GSTActiveDate";
    public static String CreatedOn = "CreatedOn";
    public static String CatalogInfo = "CatalogInfo";
    public static String CreatedAt = "CreatedAt";
    public static final String entityType = "EntityType";
    public static String ChangedBy = "ChangedBy";
    public static String TestRun = "TestRun";
    public static String SCHApplyReg = "SCHApplyReg";
    public static String CndnAdjustment = "CndnAdjustment";
    public static String CashDiscAmount = "CashDiscAmount";
    public static String CashDiscPerc = "CashDiscPerc";
    public static String SPCategoryDesc = "SPCategoryDesc";
    public static String FeedbackNo = "FeedbackNo";
    public static String FeedbackDesc = "FeedbackDesc";
    public static String DeviceStatus = "DeviceStatus";
    public static String FeedBackGuid = "FeedBackGuid";
    public static String FeebackGUID = "FeebackGUID";
    public static String FeedbackType = "FeedbackType";
    public static String FeedbackTypeDesc = "FeedbackTypeDesc";
    public static String FeedbackSubTypeID = "FeedbackSubTypeID";
    public static String FeedbackSubTypeDesc = "FeedbackSubTypeDesc";
    public static String FeedbackDate = "FeedbackDate";
    public static String FeedbackSubType = "FeedbackSubType";
    public static String SPCategoryID = "SPCategoryID";
    public static String Location = "Location";
    public static String Location1 = "Location1";
    public static String BTSID = "BTSID";
    public static String Testrun = "Testrun";
    public static String isComingFrom = "isComingFrom";
    public static String FeebackItemGUID = "FeebackItemGUID";
    public static String MerchReviewDate = "MerchReviewDate";
    public static String MerchReviewLat = "MerchReviewLat";
    public static String MerchReviewLong = "MerchReviewLong";
    public static String MerchImageGUID = "MerchImageGUID";
    public static String ImageMimeType = "ImageMimeType";
    public static String ImageSize = "ImageSize";
    public static String Image = "Image";
    public static String ImagePath = "ImagePath";
    public static String ImageByteArray = "ImageByteArray";
    public static String DocumentStore = "DocumentStore";
    public static String FileName = "FileName";
    public static String MimeType = "MimeType";
    public static String PlannedDate = "PlannedDate";
    public static String PlannedStartTime = "PlannedStartTime";
    public static String PlannedEndTime = "PlannedEndTime";
    public static String VisitTypeID = "VisitTypeID";
    public static String VisitTypeDesc = "VisitTypeDesc";
    public static String VisitDate = "VisitDate";
    public static String ProposedRoute = "ProposedRoute";
    public static String ApprovedRoute = "ApprovedRoute";
    public static String RouteID = "RouteID";
    public static String RouteGUID = "RouteGUID";
    public static String RouteDesc = "RouteDesc";
    public static String RouteClassification = "RouteClassification";
    public static String RoutePlanKey = "RoutePlanKey";
    public static String PaymentStatusID = "PaymentStatusID";
    public static String PaymentModeID = "PaymentModeID";
    public static String PaymentMode = "PaymentMode";
    public static String PaymentModeDesc = "PaymentModeDesc";
    public static String PaymetModeDesc = "PaymetModeDesc";
    public static String BranchName = "BranchName";
    public static String InstrumentNo = "InstrumentNo";
    public static String GATE2 = "GATE2";
    public static String BISCUITS = "BISCUITS";
    public static String GATE1 = "GATE1";
    public static String CRA = "CRA";
    public static String DAIRY = "DAIRY";
    public static String InstrumentDate = "InstrumentDate";
    public static String ReferenceTypeID = "ReferenceTypeID";
    public static String ReferenceTypeDesc = "ReferenceTypeDesc";
    public static String Source = "Source";
    public static String SourceReferenceID = "SourceReferenceID";
    public static String ObjectKey = "ObjectKey";
    public static String ApplicationID = "ApplicationID";
    public static String ObjectTypeID = "ObjectTypeID";
    public static String Source_SFA = "SFA";
    public static String BankID = "BankID";
    public static final String BeatGUID = "BeatGUID";
    public static final String H = "H";
    public static String BankName = "BankName";
    public static String Remarks = "Remarks";
    public static String Remarks1 = "Remarks1";
    public static String Currency = "Currency";
    public static String CurrentPotentialAmt = "CurrentPotentialAmt";
    public static String CurrentPotentialQty = "CurrentPotentialQty";
    public static String RefDocItmGUID = "RefDocItmGUID";
    public static String Status = "Status";
    public static String HTTPCODE = "HTTPCODE";
    public static String ResponseCode = "ResponseCode";
    public static String ResponseBody = "ResponseBody";
    public static String PayLoad = "PayLoad";
    public static String Message = "Message";
    public static String RejectionReasonDesc = "RejectionReasonDesc";
    public static String ProposedDlvUom = "ProposedDlvUom";
    public static String RejectionReasonID = "RejectionReasonID";
    public static String RefDocNo = "RefDocNo";
    public static String RefdocNo = "RefdocNo";
    public static String OrderNo = "OrderNo";
    public static String SSSOGuid = "SSSOGuid";
    public static String SuggestedQuantity = "SuggestedQuantity";
    public static String SSROGUID = "SSROGUID";
    public static String SSROItemGUID = "SSROItemGUID";
    public static String RejRsns = "RejRsns";
    public static String StockType = "StockType";
    public static String NetPrice = "NetPrice";
    public static String NetAmount = "NetAmount";
    public static String isSchemeActive = "isSchemeActive";
    public static String ROStatusID = "ROStatusID";
    public static String OrderDate = "OrderDate";
    public static String Amount = "Amount";
    public static String FIPGUID = "FIPGUID";
    public static String ExtRefID = "ExtRefID";
    public static String FIPDocType = "FIPDocType";
    public static String FIPDocType1 = "FIPDocType1";
    public static String FIPDate = "FIPDate";
    public static String FIPDocNo = "FIPDocNo";
    public static String FIPAmount = "FIPAmount";
    public static String DebitCredit = "DebitCredit";
    public static String ParentNo = "ParentNo";
    public static String SPFirstName = "SPFirstName";
    public static String SPLastName = "SPLastName";
    public static String SalesDist = "SalesDist";
    public static final String SalesDistDesc = "SalesDistDesc";
    public static String Route = "Route";
    public static String QusnrSrvGUID = "QusnrSrvGUID";
    public static String ApplGUID = "ApplGUID";
    public static String QusnrGUID = "QusnrGUID";
    public static String QusnrID = "QusnrID";
    public static String QusnrIDDesc = "QusnrIDDesc";
    public static String SrvText = "SrvText";
    public static String QusnrAnswID = "QusnrAnswID";
    public static String QusnrAnswIDDesc = "QusnrAnswIDDesc";
    public static String Rating = "Rating";
    public static final String QuestionnaireEntity = ".Questionnaire";
    public static final String VisitQuestionnaireEntity = ".VisitQuestionnaire";
    public static String ApplCatg = "ApplCatg";
    public static String SplProcessing = "SplProcessing";
    public static String SplProcessingDesc = "SplProcessingDs";
    public static String MatFrgtGrp = "MatFrgtGrp";
    public static String MatFrgtGrpDesc = "MatFrgtGrpDs";
    public static String FIPItemGUID = "FIPItemGUID";
    public static String ReferenceID = "ReferenceID";
    public static String ReferenceDate = "ReferenceDate";
    public static String BalanceAmount = "BalanceAmount";
    public static String ClearedAmount = "ClearedAmount";
    public static String FIPItemNo = "FIPItemNo";
    public static String FirstName = "FirstName";
    public static String SalesOffice = "SalesOffice";
    public static String LastName = "LastName";
    public static String AttendanceTypeH1 = "AttendanceTypeH1";
    public static String AttendanceTypeH2 = "AttendanceTypeH2";
    public static String AutoClosed = "AutoClosed";
    public static String PerformanceOnIDDesc = "PerformanceOnIDDesc";
    public static String MaterialGroupID = "MaterialGroupID";
    public static String MaterialGroupDesc = "MaterialGroupDesc";
    public static String Material_Catgeory = "MaterialCategory";
    public static String DbBatch = "Batch";
    public static String ManufacturingDate = "ManufacturingDate";
    public static String Material_No = "MaterialNo";
    public static String Material_Desc = "MaterialDesc";
    public static String BaseUom = "BaseUom";
    public static String BasePrice = "BasePrice";
    public static String VisitActivityGUID = "VisitActivityGUID";
    public static String VisitGUID = "VisitGUID";
    public static String ActivityType = "ActivityType";
    public static String ActivityTypeDesc = "ActivityTypeDesc";
    public static String ActivityRefID = "ActivityRefID";
    public static boolean flagforexportDB;
    public static String Validity = "Validity";
    public static String Benefits = "Benefits";
    public static String Price = "Price";
    public static String ItemNo = "ItemNo";
    public static final String MatGroupDesc = "MatGroupDesc";
    public static String SchemeDesc = "SchemeDesc";
    public static String ReviewDate = "ReviewDate";
    public static String CPTypeID = "CPTypeID";
    public static String DistanceDate = "DistanceDate";
    public static String DistanceTime = "DistanceTime";
    public static String LastOrderTime = "LastOrderTime";
    public static String SourceCoordinates = "SourceCoordinates";
    public static String CPTypeIDValue = "20";
    public static String SPGuid = "SPGuid";
    public static String SoldToCPGUID = "SoldToCPGUID";
    public static String ShipToCPGUID = "ShipToCPGUID";
    public static String ShipToID = "ShipToID";
    public static String ShipToType = "ShipToType";
    public static String ShipToDesc = "ShipToDesc";
    public static String ShipToIDCPGUID = "ShipToIDCPGUID";
    public static String SoldToTypeID = "SoldToTypeID";
    public static String SoldToTypDesc = "SoldToTypDesc";
    public static String ShipToTypeID = "ShipToTypeID";
    public static final String ONETIMESHIPTO = "OneTimeShipTo";
    public static String CPName = "CPName";
    public static String FromCPTypID = "FromCPTypID";
    public static String Address1 = "Address1";
    public static String CountryID = "CountryID";
    //	public static String Country = "Country";
    public static String BTSCircle = "BTSCircle";
    public static String DesignationID = "DesignationID";
    public static String DesignationDesc = "DesignationDesc";
    //	public static String ParentTypeID = "ParentTypeID";
    public static String DistrictDesc = "DistrictDesc";
    public static String CityDesc = "CityDesc";
    public static String CityID = "CityID";
    public static String SubDistrictGUID = "SubDistrictGUID";
    public static String ClosingTime = "ClosingTime";
    public static final String RegionID = "RegionID";
    public static String DistrictID = "DistrictID";
    public static String SubDistrictID = "SubDistrictID";
    public static String SubDistrictDesc = "SubDistrictDesc";
    public static String VisitNavigationFrom = "";
    public static String DBStockKey = "DBStockKey";
    public static String DBStockKeyDate = "DBStockKeyDate";
    public static String District = "District";
    public static String StateID = "StateID";
    public static String Country = "Country";
    public static String CountryName = "CountryName";
    public static String Landmark = "Landmark";
    public static String PostalCode = "PostalCode";
    public static String SalesPersonMobileNo = "MobileNo";
    public static String MobileNo = "Mobile1";
    public static String VerificationDat = "VerificationDat";
    public static String MobileNo1 = "MobileNo";
    public static String CPMobileNo = "CPMobileNo";
    public static String EmailID = "EmailID";
    public static String EmailID2 = "EmailID2";
    public static String DOB = "DOB";
    public static String DeactivatedOn = "DeactivatedOn";
    public static String NoOfSalesMan = "NoOfSalesMan";
    public static String WsCovered = "WsCovered";
    public static String OutletsCovered = "OutletsCovered";
    public static String DeactivationDate = "DeactivationDate";
    public static String DestinationDesc = "DestinationDesc";
    public static String DestinationID = "DestinationID";
    public static String DistCpGuid = "DistCpGuid";
    public static String DistCpUid = "DistCpUid";
    public static String Anniversary = "Anniversary";
    public static String PAN = "PAN";
    public static String VATNo = "VATNo";
    public static String TIN = "TIN";
    public static String OwnerName = "OwnerName";
    public static String NoOfLines = "NoOfLines";
    public static String MinimumBillAmt = "MinimumBillAmt";
    public static String OutletName = "Name";
    public static String RetailerProfile = "Group1";
    public static String RetailerProfileText = "RetailerProfile";
    public static String CPGRP5 = "CPGRP5";
    public static String CPGRP4 = "CPGRP4";
    public static String CPGRP2 = "CPGRP2";
    public static String CPGRP3 = "CPGRP3";
    public static String CPDFUM = "CPDFUM";
    public static String Group2 = "Group2";
    public static String Group11 = "Group11";
    public static String Group7 = "Group7";
    public static String Group8 = "Group8";
    public static String Group6 = "Group6";
    public static String Group9 = "Group9";
    public static String DistanceFromParent = "DistanceFromParent";
    public static String DstnceFromPUOM = "DstnceFromPUOM";
    public static String Day1 = "Day1";
    public static String Day2 = "Day2";
    public static String Day3 = "Day3";
    public static String Day4 = "Day4";
    public static String Day5 = "Day5";
    public static String Day6 = "Day6";
    public static String Day7 = "Day7";
    public static String BillSeries = "BillSeries";
    public static String Group1 = "Group1";
    public static String Group3 = "Group3";
    public static String Group3Desc = "Group3Desc";
    public static String Group2Desc = "Group2Desc";
    public static String Group1Desc = "Group1Desc";
    public static String Group5Desc = "Group5Desc";
    public static String Group4 = "Group4";
    public static String VerifiedBy = "VerifiedBy";
    public static String Group5 = "Group5";
    public static String Group4Desc = "Group4Desc";
    public static String Latitude = "Latitude";
    public static String Longitude = "Longitude";
    public static String OrderTime = "OrderTime";
    public static String InvTime = "InvTime";
    public static String SetResourcePath = "SetResourcePath";
    public static String PartnerMgrGUID = "PartnerMgrGUID";
    public static String OtherCustGuid = "OtherCustGuid";
    public static String CPGUID32 = "CPGUID32";
    public static String CPGUID = "CPGUID";
    public static String SPDistance = "SPDistance";
    public static String PFGUID = "PFGUID";
    public static String PartnerTypeID = "PartnerTypeID";
    public static String PartnarCPGUID = "PartnarCPGUID";
    public static String PartnerCPNo = "PartnerCPNo";
    public static String PartnarName = "PartnarName";
    public static String PartnerMobileNo = "PartnerMobileNo";
    public static String PartnerFunction = "PartnerFunction";
    public static String  PartnerFunctionShipTo= "01";
    public static String  PartnerFunctionBillTo= "02";
    public static String CP1GUID = "CP1GUID";
    public static String InfraCode = "InfraCode";
    public static String InfraCodeDesc = "InfraCodeDesc";
    public static String OwnedBy = "OwnedBy";
    public static String OwnedByDesc = "OwnedByDesc";
    public static String InfraQty = "InfraQty";
    public static String InfraUOM = "InfraUOM";
    public static String CP2GUID = "CP2GUID";
    public static String PerformanceTypeID = "PerformanceTypeID";
    public static String AsOnDate = "AsOnDate";
    public static String CPGuid = "CPGuid";
    public static String OINVAG = "OINVAG";
    public static final String CUSTOMERS = "Customers";
    public static final String CustomerNo = "CustomerNo";
    public static final String CustomerPO = "CustomerPO";
    public static final String CustomerName = "CustomerName";
    public static final String CustomerPODate = "CustomerPODate";
    public static final String SalesArea = "SalesArea";
    public static final String AmtPastDue = "AmtPastDue";
    public static final String AmtCurrentDue = "AmtCurrentDue";
    public static final String Amt31To60 = "Amt31To60";
    public static final String Amt61To90 = "Amt61To90";
    public static final String Amt91To120 = "Amt91To120";
    public static final String AmtOver120 = "AmtOver120";
    public static String COLLECTIONHDRS = "CollectionHdrs";
    public static String COLLECTIONITEMS = "CollectionItems";
    public static String OPEN_INVOICE_LIST = "OpenInvList";
    public static String INVOICES = "Invoices";
    public static String VISITACTIVITIES = "VisitActivities";
    public static String INVOICESSERIALNUMS = "InvoiceItmSerNumList";
    public static String ENDLONGITUDE = "EndLongitude";
    public static String REMARKS = "Remarks";
    public static String VISITKEY = "VisitGUID";
    public static String ROUTEPLANKEY = "RoutePlanGUID";
    public static String LOGINID = "LoginID";
    public static String DATE = "Date";
    public static String VISITTYPE = "VisitType";
    public static String CUSTOMERNO = "CustomerNo";
    public static String REASON = "Reason";
    public static String STARTDATE = "StartDate";
    public static String STARTTIME = "StartTime";
    public static String STARTLATITUDE = "StartLatitude";
    public static String STARTLONGITUDE = "StartLongitude";
    public static String ENDTIME = "EndTime";
    public static String ENDDATE = "EndDate";
    public static String ENDLATITUDE = "EndLatitude";
    public static String ETAG = "ETAG";
    public static final String VisitCatID = "VisitCatID";
    public static final String PROP_MER_TYPE = "RVWTYP";
    public static String RschGuid = "RschGuid";
    public static String RouteSchGUID = "RouteSchGUID";
    public static String VisitCPGUID = "VisitCPGUID";
    public static String ViisitCPNo = "ViisitCPNo";
    public static String VisitCPName = "VisitCPName";
    public static String SalesPersonID = "SalesPersonID";
    public static String ShortName = "ShortName";
    public static String RoutId = "RoutId";
    public static String SequenceNo = "SequenceNo";
    public static String DayOfWeek = "DayOfWeek";
    public static String DayOfMonth = "DayOfMonth";
    public static String DOW = "DOW";
    public static String DOM = "DOM";
    public static final String DESCRIPTION = "Description";
    public static final String EntityType = "EntityType";
    public static final String BILMaterials = "BilMaterials";
    public static final String IsDefault = "IsDefault";
    public static final String PropName = "PropName";
    public static final String ID = "ID";
    public static String IN = "IN";
    public static String ComplaintCategory = "ComplaintCategory";
    public static final String CustomerCreditLimits = "CustomerCreditLimits";
    public static final String DSPPRCNO0 = "DSPPRCNO0";
    public static final String DSPMATNO = "DSPMATNO";
    public static final String EVLTYP = "EVLTYP";
    public static final String TypeValue = "TypeValue";
    public static final String Typeset = "Typeset";
    public static final String Types = "Types";
    public static final String SCRSIM = "SCRSIM";
    public static final String SS = "SS";
    public static final String MAXEXPALWM = "MAXEXPALWM";
    public static final String MAXEXPALWD = "MAXEXPALWD";
    public static final String AUTOSYNC = "AUTOSYNC";
    public static final String SOAUTOSYNC   = "SOAUTOSYNC";
    public static final String DATEFORMAT = "DATEFORMAT";
    public static final String SMINVITMNO = "SMINVITMNO";
    public static final String NOITMZEROS = "NOITMZEROS";
    public static final String TypesName = "TypesName";
    public static String TypesValue = "TypeValue";
    public static final String Typesname = "Typesname";
    public static final String PROP_ATTTYP = "ATTTYP";
    public static final String PROP_ACTTYP = "ACTTYP";
    public static final String VisitSeqId = "VisitSeqId";
    public static String Name = "Name";
    public static final String City = "City";
    public static String Mobile1 = "Mobile1";
    public static String Mobile = "Mobile";
    public static String MOBILE = "MOBILE";
    public static final String SalesDistrictCode = "SalesDistrict";
    public static final String SalesDistrictDesc = "SalesDistrictDesc";
    public static final String CPUID = "CPUID";
    public static final String Address = "Address";
    public static String WeeklyOffDesc = "WeeklyOffDesc";
    public static String TaxRegStatusDesc = "TaxRegStatusDesc";
    public static String TaxRegStatus = "TaxRegStatus";
    public static String OutletSizeId = "OutletSizeId";
    public static String NoOfEmployee = "NoOfEmployee";
    public static String OutletShapeId = "OutletShapeId";
    public static String TaxRegStatusDone = "0000000001";
    public static String TaxRegStatusNotDone = "0000000002";
    public static String Tax1 = "Tax1";
    public static String TotalPotentialAmt = "TotalPotentialAmt";
    public static String PotentialType = "PotentialType";
    public static String OrderType = "OrderType";
    public static String VisitID = "VisitID";
    public static String Region = "Region";
    public final static String OrderEntry = "OrderEntry";
    public final static String OrderOrigin = "OrderOrigin";
    public static String ApprvlStatusID = "ApprvlStatusID";
    public static final String WeeklyOff = "WeeklyOff";
    public static String ApprovalStatusID = "ApprovalStatus";
    public static String ChangedAt = "ChangedAt";
    public static String ChangedOn = "ChangedOn";
    public static String RoutSchScope = "RoutSchScope";
    public static String DMSDivisionID = "DMSDivisionID";
    public static String DmsDivsionDesc = "DmsDivsionDesc";
    public static String DMSDivision = "DMSDivision";
    public static String DMSDivisionDesc = "DMSDivisionDesc";
    public static final String CvgValue = "CvgValue";
    public static String SoldToId = "SoldToId";
    public static String SoldToName = "SoldToName";
    public static String SoldToID = "SoldToID";
    public static String ApprovalStatusDs = "ApprovalStatusDs";
    public static String ApprovalStatus = "ApprovalStatus";
    public static String ApprovalStatusDesc = "ApprovalStatusDesc";
    public static String FromCPGUID = "FromCPGUID";
    public static String FromCPNo = "FromCPNo";
    public static String FromCPTypeID = "FromCPTypeID";
    public static final String UOMNO0 = "UOMNO0";
    public static String ConditionPricingDate = "ConditionPricingDate";
    public static String SONo = "SONo";
    public static String DelvStatusID = "DelvStatusID";
    public static final String Material = "Material";
    public static String Quantity = "Quantity";
    public static String SOUpdate = "SOUpdate";
    public static String SOCancel = "SOCancel";
    public static String TotalAmount = "TotalAmount";
    public static String InvoiceNo = "InvoiceNo";
    public static String InvoiceHisNo = "InvoiceNo";
    public static String InvoiceDate = "InvoiceDate";
    public static String InvoiceAmount = "InvoiceAmount";
    public static String InvoiceAmount1 = "GrossAmount";
    public static String InvoiceStatus = "StatusID";
    public static String InvoiceGUID = "InvoiceGUID";
    public static String MaterialDocGUID = "MaterialDocGUID";
    public static String FromGUID = "FromGUID";
    public static String FromTypeID = "FromTypeID";
    public static String ToGUID = "ToGUID";
    public static String ToTypeID = "ToTypeID";
    public static String MaterialDocDate = "MaterialDocDate";
    public static String MatDocItemGUID = "MatDocItemGUID";
    public static String RefDocItemNo = "RefDocItemNo";
    public static String MaterialDocQty = "MaterialDocQty";
    public static String MaterialCatID = "MaterialCatID";
    public static String MaterialCatDesc = "MaterialCatDesc";
    public static String MvmtTypeID = "MvmtTypeID";
    public static String RefType = "RefType";
    public static String VanGUID = "VanGUID";
    public static String AutoMaterialDocItems = "AutoMaterialDocItems";
    public static String RefDocTypeID = "RefDocTypeID";
    public static String VehicleNo = "VehicleNo";
    public static String VehicleRepDate = "VehicleRepDate";
    public static String UnloadDate = "UnloadDate";
    public static String MaterialDocNo = "MaterialDocNo";
    public static String StockOwner = "StockOwner";
    public static String ParentType = "ParentType";
    public static String StockTypeID = "StockTypeID";
    public static String UnrestrictedQty = "UnrestrictedQty";
    public static String SlabTypeID = "SlabTypeID";
    public static String SlabTypeDesc = "SlabTypeDesc";
    public static String IsIncludingPrimary = "IsIncludingPrimary";
    public static String SchemeCatID = "SchemeCatID";
    public static String SchemeCatDesc = "SchemeCatDesc";
    public static String OnSaleOfCatID = "OnSaleOfCatID";
    public static String OnSaleOfItemUOMDesc = "OnSaleOfItemUOMDesc";
    public static String OnSaleOfItemUOMID = "OnSaleOfItemUOMID";
    public static String OnSaleOfCatIDOrderMatGrp = "000005";
    public static String OnSaleOfCatIDMaterial = "000005";
    public static Boolean BoolMatWiseSchemeAvalible = false;
    public static Boolean BoolMatWiseQPSSchemeAvalible = false;
    public static String SchemeTypeID = "SchemeTypeID";
    public static String SchemeTypeDesc = "SchemeTypeDesc";
    public static String SchemeID = "SchemeID";
    public static String ValidFrom = "ValidFrom";
    public static String SKUGroupID = "SKUGroupID";
    public static String SKUGroupDesc = "SKUGroupDesc";
    public static String PriDiscountPer = "PriDiscountPer";
    public static String InvoiceTypeID = "InvoiceTypeID";
    public static String InvoiceTypeDesc = "InvoiceTypeDesc";
    public static String IsLatLongUpdate = "IsLatLongUpdate";
    public static String CreditDays = "CreditDays";
    public static String CreditBills = "CreditBills";
    public static String CreditLimit = "CreditLimit";
    public static String CreditPeriod = "CreditPeriod";
    public static final String Device = "DeviceData";
    public static final String NonDevice = "NonDeviceData";
    public static final String ExpenseFreq = "ExpenseFreq";
    public static final String ExpenseDaily = "000010";
    public static final String ExpenseMonthly = "000030";
    public static final String ExpenseType = "ExpenseType";
    public static final String LocationDesc = "LocationDesc";
    public static final String ExpenseTypeDesc = "ExpenseTypeDesc";
    public static final String ExpenseItemType = "ExpenseItemType";
    public static final String ExpenseItemTypeDesc = "ExpenseItemTypeDesc";
    public static final String ExpenseFreqDesc = "ExpenseFreqDesc";
    public static final String ExpenseItemCat = "ExpenseItemCat";
    public static final String ExpenseItemCatDesc = "ExpenseItemCatDesc";
    public static final String DefaultItemCat = "DefaultItemCat";
    public static final String DefaultItemCatDesc = "DefaultItemCatDesc";
    public static final String AmountCategory = "AmountCategory";
    public static final String AmountCategoryDesc = "AmountCategoryDesc";
    public static final String MaxAllowancePer = "MaxAllowancePer";
    public static final String ExpenseQuantityUom = "ExpenseQuantityUom";
    public static final String ItemFieldSet = "ItemFieldSet";
    public static final String ItemFieldSetDesc = "ItemFieldSetDesc";
    public static final String Allowance = "Allowance";
    public static final String IsSupportDocReq = "IsSupportDocReq";
    public static final String IsRemarksReq = "IsRemarksReq";
    public static final String ExpenseGUID = "ExpenseGUID";
    public static final String FiscalYear = "FiscalYear";
    public static final String ExpenseNo = "ExpenseNo";
    public static final String ExpenseDate = "ExpenseDate";
    public static final String ExpenseItemGUID = "ExpenseItemGUID";
    public static final String ExpeseItemNo = "ExpeseItemNo";
    public static final String ConvenyanceMode = "ConvenyanceMode";
    public static final String ConvenyanceModeDs = "ConvenyanceModeDs";
    public static final String Distance = "Distance";
    public static final String BeatDistance = "BeatDistance";
    public static final String ConveyanceAmt = "ConveyanceAmt";
    public static final String ExpenseDocumentID = "ExpenseDocumentID";
    public static final String VisitQuestionnaires = "VisitQuestionnaires";

    // SO Create Properties
    public static final String TestRun_Text = "M";
    public static String GrossAmt = "GrossAmt";
    public static String TAX = "TAX";
    public static String CPType = "CPType";
    public static String SecDiscount = "SecDiscount";
    public static String PriDiscount = "PriDiscount";
    public static String CashDiscount = "CashDiscount";
    public static String CashDiscountAmount = "CashDiscountAmount";
    public static String CashDiscountPercentage = "CashDiscountPercentage";
    public static String SecondaryDiscountPerc = "SecondaryDiscountPerc";
    public static String PrimaryDiscountPerc = "PrimaryDiscountPerc";
    public static String CashDiscountPerc = "CashDiscountPerc";
    public static String CashDiscPercAmt = "CashDiscPercAmt";
    public static String SecondaryTradeDiscount = "SecondaryTradeDiscount";
    public static String SecondaryTradeDiscAmt = "SecondaryTradeDiscAmt";
    public static String MFD = "MFD";
    public static String PONo = "PONo";
    public static String PODate = "PODate";
    public static String FromCPName = "FromCPName";
    public static String FromCPTypId = "FromCPTypId";
    public static String FromCPTypDs = "FromCPTypDs";
    public static String SoldToUID = "SoldToUID";
    public static String SoldToDesc = "SoldToDesc";
    public static String SoldToType = "SoldToType";
    public static String SoldToTypDs = "SoldToTypDs";
    public static String SSSOItemGUID = "SSSOItemGUID";
    public static String OrderMatGrp = "OrderMatGrp";
    public static String OrderMatGrpDesc = "OrderMatGrpDesc";
    public static String OrdMatGrpDesc = "OrdMatGrpDesc";
    public static String ShipToName = "ShipToName";
    public static String OpenAdvanceAmt = "OpenAdvanceAmt";
    // Schemes Properties
    public static String GeographyScopeID = "GeographyScopeID";
    public static String GeographyScopeDesc = "GeographyScopeDesc";
    public static String GeographyLevelID = "GeographyLevelID";
    public static String GeographyLevelDesc = "GeographyLevelDesc";
    public static String GeographyTypeID = "GeographyTypeID";
    public static String GeographyTypeDesc = "GeographyTypeDesc";
    public static String GeographyValueID = "GeographyValueID";
    public static String GeographyValueDesc = "GeographyValueDesc";
    public static String SchemeItemGUID = "SchemeItemGUID";
    public static String FromQty = "FromQty";
    public static String ToQty = "ToQty";
    public static String FromValue = "FromValue";
    public static String ToValue = "ToValue";
    public static String PayoutPerc = "PayoutPerc";
    public static String PayoutAmount = "PayoutAmount";
    public static String SlabRuleID = "SlabRuleID";
    public static String SlabRuleDesc = "SlabRuleDesc";
    public static String SaleUnitID = "SaleUnitID";
    public static String FreeQty = "FreeQty";
    public static String FreeArticle = "FreeArticle";
    public static String FreeArticleDesc = "FreeArticleDesc";
    public static String NoOfCards = "NoOfCards";
    public static String CardTitle = "CardTitle";
    public static String ScratchCardDesc = "ScratchCardDesc";
    public static String SalesAreaID = "SalesAreaID";
    public static String SlabGUID = "SlabGUID";
    public static String SaleUnitIDAmountWise = "000002";
    public static String TargetBasedID = "TargetBasedID";
    public static String BrandsCategories = "BrandsCategories";
    public static String OrderMaterialGroups = "OrderMaterialGroups";
    public static String Brands = "Brands";
    public static String BrandEntity = ".Brand";
    public static String OrderMaterialGroupsEntity = ".OrderMaterialGroup";
    public static String MaterialCategories = "MaterialCategories";
    public static String BrandID = "BrandID";
    public static String BrandDesc = "BrandDesc";
    public static String MaterialCategoryID = "MaterialCategoryID";
    public static String MaterialCategoryDesc = "MaterialCategoryDesc";
    public static String FreeQuantity = "FreeQuantity";
    public static String Category = "Category";
    public static String CRS_SKU_GROUP = "CRS SKU Group";
    public static String BannerID = "BannerID";
    public static String ProductCatID = "ProductCatID";
    public static String SchemeSaleUnitIDCBB = "000004";
    public static String SchemeSaleUnitIDBAG = "000006";
    public static String SchemeFreeProdSeq = "000001";
    public static String SchemeFreeProdLowMRP = "000002";
    public static String DmsDivision = "DmsDivision";
    public static String OrderReason = "OrderReason";
    public static String DmsDivisionDesc = "DmsDivisionDesc";
    public static HashMap<String, String> Map_Must_Sell_Mat = new HashMap<>();
    public static String IS_TRUE = "true";
    public static String IsHeaderBasedSlab = "IsHeaderBasedSlab";
    public static String SchFreeMatGrpGUID = "SchFreeMatGrpGUID";
    public static String FreeQtyUOM = "FreeQtyUOM";
    public static String TransRefTypeID = "TransRefTypeID";
    public static String TransRefNo = "TransRefNo";
    public static String TransRefItemNo = "TransRefItemNo";
    public static String AlternativeUOM1Num = "AlternativeUOM1Num";
    public static String TaxBaseVal = "TaxBaseVal";
    public static String Banner = "Banner";
    public static String BannerDesc = "BannerDesc";
    public static String AltUomNum = "AltUomNum";
    public static String AlternateUom = "AlternateUom";
    public static String AltUomDen = "AltUomDen";
    public static String AlternativeUOM1Den = "AlternativeUOM1Den";
    public static String IntermUnitPrice = "IntermUnitPrice";
    public static String MaterialGrp = "MaterialGrp";
    public static String OrgScopeID = "OrgScopeID";
    public static String OrgScopeDesc = "OrgScopeDesc";
    public static String IsExcluded = "IsExcluded";

    public static String SchemeItemMinSaleUnitIDPPacket = "000001";
    public static String SchemeItemMinSaleUnitIDAmount = "000002";
    public static String SchemeItemMinSaleUnitIDKG = "000003";
    public static String SchemeItemMinSaleUnitIDPercentage = "000004";
    public static String SchemeItemMinSaleUnitIDCBB = "000005";
    public static String SchemeItemMinSaleUnitIDTon = "000006";
    public static String SchemeItemMinSaleUnitIDBag = "000007";

    public static String Tax2 = "Tax2";
    public static String Tax3 = "Tax3";
    public static String Tax4 = "Tax4";
    public static String ProspectStatusID = "ProspectStatusID";
    public static String Tax5 = "Tax5";
    public static String Tax6 = "Tax6";
    public static String Tax7 = "Tax7";
    public static String Tax8 = "Tax8";
    public static String Tax9 = "Tax9";
    public static String Tax10 = "Tax10";
    public static String ConditionTypeID = "ConditionTypeID";
    public static String ConditionTypeDesc = "ConditionTypeDesc";
    public static String ReferenceTaxFieldID = "ReferenceTaxFieldID";
    public static String ReferenceTaxFieldDesc = "ReferenceTaxFieldDesc";
    public static String FormulaID = "FormulaID";
    public static String FormulaDesc = "FormulaDesc";
    public static String CalcOnID = "CalcOnID";
    public static String CalcOnDesc = "CalcOnDesc";
    public static String ApplicableOnID = "ApplicableOnID";
    public static String ApplicableOnDesc = "ApplicableOnDesc";
    public static String CalcOnConditionTypeID = "CalcOnConditionTypeID";
    public static String CalcOnConditionTypeDesc = "CalcOnConditionTypeDesc";
    public static final String ItemCatID = "ItemCatID";
    public static final String str_0 = "0";
    public static String BatchOrSerial = "BatchOrSerial";
    public static String EXTRA_OPEN_NOTIFICATION = "openNotification";
    public static String RatioSchNum = "RatioSchNum";
    public static String RatioSchDen = "RatioSchDen";
    public static String FreeMaterialNo = "FreeMaterialNo";
    public static String FreeTypeID = "FreeTypeID";
    public static String MSEC = "MSEC";
    public static String SKUGRP = "SKUGRP";
    public static String CPSTKUOM = "CPSTKUOM";
    public static String SKUGROUP = "SKU GROUP";
    public static String CRSSKUGROUP = "CRS SKU GROUP";

    public static String SchemeFreeProduct = "000001";
    public static String SchemeFreeSKUGroup = "000007";
    public static String SchemeFreeCRSSKUGroup = "000002";
    public static String SchemeFreeDiscountPercentage = "000003";
    public static String SchemeFreeDiscountAmount = "000004";
    public static String SchemeFreeScratchCard = "000005";
    public static String SchemeFreeFreeArticle = "000006";

    public static String OnSaleOfBanner = "000001";
    public static String OnSaleOfBrand = "000002";
    public static String OnSaleOfProdCat = "000003";
    public static String OnSaleOfOrderMatGrp = "000004";
    public static String OnSaleOfMat = "000005";
    public static String OnSaleOfSchemeMatGrp = "000006";
    public static String OnSaleofSubBrand = "000006";

    // Invoices Properties
    public static String isInvoiceItemsEnabled = "isInvoiceItemsEnabled";
    public static final String INVOICE_ITEM = "InvoiceItems";
    public static String CollectionAmount = "CollectionAmount";
    public static final String SSInvoice = "SSInvoice";
    public static final String SSSO = "SSSO";
    public static final String SSSOEntity = ".SSSO";
    public static final String SSSoItemDetailEntity = ".SSSOItemDetail";

    /*  alerts */
    public static String PartnerType = "PartnerType";
    public static String AlertGUID = "AlertGUID";
    public static String AlertText = "AlertText";
    public static String AlertHistory = "AlertHistory";
    public static String Alerts = "Alerts";
    public static String ALERT_ENTITY = ".Alert";
    public static String ExpenseEntity = ".Expense";
    public static String ExpenseItemEntity = ".ExpenseItemDetail";
    public static String PartnerID = "PartnerID";
    public static String PartnerName = "PartnerName";
    public static String AlertTypeDesc = "AlertTypeDesc";
    public static String AlertType = "AlertType";
    public static String ObjectType = "ObjectType";
    public static String DocumentType = "DocumentType";
    public static String DocumentRepositoryGUID = "DocumentRepositoryGUID";
    public static String ObjectID = "ObjectID";
    public static String ObjectSubID = "ObjectSubID";
    public static String ConfirmedBy = "ConfirmedBy";
    public static String Application = "Application";
    public static String DocumentID = "DocumentID";
    public static String DocumentSt = "DocumentStore";
    public static final String DocumentTypeID = "DocumentTypeID";
    public static final String DocumentTypeDesc = "DocumentTypeDesc";
    public static final String DocumentStatusID = "DocumentStatusID";
    public static final String DocumentStatusDesc = "DocumentStatusDesc";
    public static final String DocumentMimeType = "DocumentMimeType";
    public static final String DocumentSize = "DocumentSize";
    public static String DocumentLink = "DocumentLink";
    public static String DocumentName = "FileName";
    public static String FolderName = "VisualVid";
    public static String ConfirmedOn = "ConfirmedOn";
    public static final String red_hex_color_code = "#2cb037";
    public static String BirthDayAlertsTempKey = "BirthDayAlertsTempKey";
    public static String AlertsTempKey = "AlertsTempKey";
    public static String BirthDayAlertsKey = "BirthDayAlertsKey";
    public static String BirthDayAlertsDate = "BirthDayAlertsDate";
    public static String QUODECK_DATE = "QUODECK_DATE";
    public static String AlertsDataKey = "AlertsDataKey";
    public static final String BirthdayAlertsCount = "BirthdayAlertsCount";
    public static final String TextAlertsCount = "TextAlertsCount";
    public static final String AppointmentAlertsCount = "AppointmentAlertsCount";

    /*mime type*/
    public static final String MimeTypePDF = "application/pdf";
    public static final String MimeTypeMP4 = "application/octet-stream";
    public static final String MimeTypePPT = "application/ppt";
    public static final String MimeTypeDocx = "application/docx";
    public static final String MimeTypevndmspowerpoint = "application/vnd.ms-powerpoint";
    public static final String MimeTypeMsword = "application/msword";
    public static final String MimeTypePng = "image/png";
    public static final String MimeTypeJpg = "image/jpg";
    public static final String MimeTypeJpeg = "image/jpeg";

    /*app preference*/
    public static final String PREFS_NAME = "mSFAPreference";
    public static final String START_MY_ATT = "STARTMYATT";
    public static final String FR_STATUS = "FR_STATUS";
    public static final String isReIntilizeDB = "isReIntilizeDB";
    public static final String ActiveHost = "ActiveHost";
    public static final String HostName = "HostName";
    public static final String USERROLE = "UserRole";
    public static final String USERAuthID = "USERAuthID";
    public static final String isRollResponseGot = "isRollResponseGot";
    public static final String isUserAuthResponseGot = "isUserAuthResponseGot";

    /*key extra*/
    public static final String EXTRA_COME_FROM = "comeFrom";
    public static final String EXTRA_BEAN = "onBean";
    public static final String EXTRA_POS = "extraPOS";
    public static final String EXTRA_BEAN_LIST = "onBeanList";
    public static final String isMaterialEnabled = "isMaterialEnabled";
    public static final String EXTRA_SO_HEADER = "Header";
    public static final String QR_CODE_DATA = "Header";

    /*store*/
    public static final String EncryptKey = "welcome1";
    public static final String STORE_NAME = "mSecSales_Offline";
    public static final String backupDBPath = "mSecSales_Offline.udb";
    public static final String backuprqDBPath = "mSecSales_Offline.rq.udb";
    public static final String arteria_dayfilter = "x-arteria-daysfilter";
    public static final String NO_OF_DAYS = "0";
    public static Boolean IsOnlineStoreFailed = false;
    public static Boolean IsOnlineStoreStarted = false;
    public static final String arteria_session_header = "x-arteria-loginid";
//    public static OnlineODataStore onlineStore = null;

    // AWSM login name
    public static String dsrLoginName = "";
    /*entity name */
    public static String FeedbackEntity = ".Feedback";
    public static String VISITACTIVITYENTITY = ".VisitActivity";
    public static String FeedbackItemDetailEntity = ".FeedbackItemDetail";
    public static String VISITENTITY = ".Visit";
    public static String CUSTOMERENTITY = ".Customer";
    public static String CPSPRelationEntity = ".CPSPRelation";
    public static String ATTENDANCEENTITY = ".Attendance";
    public static String MERCHINDISINGENTITY = ".MerchReview";
    public static String MERCHINDISINGITEMENTITY = ".MerchReviewImage";
    public static String ChannelPartnerEntity = ".ChannelPartner";
    public static String InvoiceEntity = ".SSInvoice";
    public static String InvoiceItemEntity = ".SSInvoiceItemDetail";
    public static String InvoiceSerialNoEntity = ".SSInvoiceItemSerialNo";
    public static String FinancialPostingsEntity = ".FinancialPosting";
    public static String FinancialPostingsItemEntity = ".FinancialPostingItemDetail";
    public static String CompetitorInfoEntity = ".CompetitorInfo";
    public static String SPStockSNosEntity = ".SPStockItemSNo";
    public static String CPStockItemEntity = ".CPStockItem";
    public static String ComplaintEntity = ".Complaint";
    public static String UserCustomerEntity = ".UserCustomer";
    public static String UserSalesPersonEntity = ".UserSalesPerson";
    public static String UserChannelPartnerEntity = ".UserChannelPartner";
    public static String STOCK_ENTITY = ".Stock";
    public static String RouteScheduleEntity = ".RouteSchedule";
    public static String RouteSchedulePlanEntity = ".RouteSchedulePlan";
    public static String RouteScheduleSPEntity = ".RouteScheduleSP";
    public static String SalesOrderEntity = ".SSSO";
    public static String ReturnOrderEntity = ".SSRO";
    public static String ReturnOrderItemEntity = ".SSROItemDetail";
    public static String SalesOrderItemEntity = ".SSSOItemDetail";
    public static String AlertEntity = ".Alert";
    public static String CPDMSDivisionEntity = ".CPDMSDivision";
    public static String CPPartnerFunctionEntity = ".CPPartnerFunction";
    public static String CPInfraEntity = ".CPInfra";
    public static String TodayOrderVal = "0.0";
    public static String TotalCustomers = "0";
    public static String TotalProCalls = "0";
    public static String TotalLineCuts = "0";
    public static String CPPerformances = "CPPerformances";
    public static String CPPerformanceEntity = ".CPPerformance";

    /*collection list*/
    public static String SoldToTypeDesc = "SoldToTypeDesc";
    public static String DeletionInd = "DeletionInd";
    public static String Division = "Division";
    public static String InvoiceItemGUID = "InvoiceItemGUID";
    public static final String Materials = "Materials";
    public static final String MaterialEntitty = ".Material";
    public static final String SampleDisbursementDesc = "Sample Disbursement";
    public static final String SampleDisbursement = "SampleDisbursement";
    public static final String FinancialPostings = "FinancialPostings";
    public static final String FinancialPostingItemDetails = "FinancialPostingItemDetails";
    public static final String ReturnOrderCreate = "Return Order Create";
    public static String SOs = "SOs";
    public static final String SOItemDetails = "SOItemDetails";
    public static final String SOItems = "SOItems";
    public static final String SOTexts = "SOTexts";
    public static final String UserSalesPersons = "UserSalesPersons";
    public static final String CPConfigs = "CPConfigs";
    public static final String CPConfigEntity = ".CPConfig";
    public static final String UserChannelPartners = "UserChannelPartners";
    public static final String KPISet = "KPISet";
    public static final String Customers = "Customers";
    public static final String Targets = "Targets";
    public static final String TargetItems = "TargetItems";
    public static final String KPIItems = "KPIItems";
    public static final String CompetitorMasters = "CompetitorMasters";
    public static final String CompetitorInfos = "CompetitorInfos";
    public static final String SSOutstandingInvoices = "SSOutstandingInvoices";
    public static final String SSOutstandingInvoiceItemDetails = "SSOutstandingInvoiceItemDetails";
    public static final String PricingConditions = "PricingConditions";
    public static final String Claims = "Claims";
    public static final String ANSWID = "ANSWID";
    public static final String MATCAT = "MATCAT";
    public static final String Questions = "Questions";
    public static final String UserCustomers = "UserCustomers";
    public static final String ClaimItemDetails = "ClaimItemDetails";
    public static final String ClaimDocuments = "ClaimDocuments";
    public static final String CPDocuments = "CPDocuments";
    public static final String AttendanceDocuments = "AttendanceDocuments";
    public static final String ComplaintDocuments = "ComplaintDocuments";
    public static final String TextCategorySet = "TextCategorySet";
    public static final String MerchReviewImages = "MerchReviewImages";
    public static final String ExpenseDocuments = "ExpenseDocuments";
    public static final String ExpenseAllowances = "ExpenseAllowances";
    public static final String Expenses = "Expenses";
    public static final String ExpenseItemDetails = "ExpenseItemDetails";
    public static final String SchemeCPDocuments = "SchemeCPDocuments";
    public static final String SalesPersons = "SalesPersons";
    public static final String SPPayouts = "SPPayouts";
    public static final String ConfigTypsetTypeValues = "ConfigTypsetTypeValues";
    public static final String ConfigTypsetTypeValueEntity = ".ConfigTypsetTypeValue";
    public static final String SalesPersonEntity = ".SalesPerson";
    public static final String SPPayoutEntity = ".SPPayout";
    public static final String UserProfileAuthSet = "UserProfileAuthSet";
    public static final String UserProfileAuth = ".UserProfileAuth";
    public static final String UserLogins = "UserLogins";
    public static final String UserProfiles = "UserProfiles";
    public static final String Attendances = "Attendances";
    public static final String Visits = "Visits";
    public static final String VisitActivities = "VisitActivities";
    public static final String RoutePlans = "RoutePlans";
    public static final String RouteSchedules = "RouteSchedules";
    public static final String RouteSchedulePlans = "RouteSchedulePlans";
    public static final String ChannelPartners = "ChannelPartners";
    public static final String ValueHelps = "ValueHelps";
    public static final String SubDistricts = "SubDistricts";
    public static final String CPSummary1Set = "CPSummary1Set";
    public static final String CPSummary1 = ".CPSummary1";
    public static final String Wards = "Wards";
    public static final String Towns = "Towns";
    public static final String TownEntity = ".Town";
    public static final String SubDistrictEntity = ".SubDistrict";
    public static final String DistrctID = "DistrctID";
    public static final String ValueHelpEntity = ".ValueHelp";
    public static final String ExpenseConfigs = "ExpenseConfigs";
    public static final String MerchReviews = "MerchReviews";
//    public static String Collections = "Collections";
    public static String CustomerSalesAreas = "CustomerSalesAreas";
    public static final String ConfigTypesetTypes = "ConfigTypesetTypes";
    public static final String CPDMSDivisions = "CPDMSDivisions";
    public static final String CPInfras = "CPInfras";
    public static final String CPRoutes = "CPRoutes";
    public static final String SyncHistorys = "SyncHistorys";
    public static final String UserPartners = "UserPartners";
    public static final String CPPartnerFunctions = "CPPartnerFunctions";
    public static final String SSSOs = "SSSOs";
    public static final String Documents = "Documents";
    public static final String SSSoItemDetails = "SSSOItemDetails";
    public static String CPSPRelations = "CPSPRelations";
    public static String Feedbacks = "Feedbacks";
    public static final String Performances = "Performances";
    public static String FeedbackItemDetails = "FeedbackItemDetails";
    public static final String SSINVOICES = "SSInvoices";
    public static final String SSINVOICESEntity = ".SSInvoice";
    public static final String SSInvoiceItemDetails = "SSInvoiceItemDetails";
    public static final String SSPurchaseInvoices = "SSPurchaseInvoices";
    public static final String SSPurchaseInvoiceItemDetails = "SSPurchaseInvoiceItemDetails";
    public static final String SSPurchaseInvoiceItems = "SSPurchaseInvoiceItems";
    public static final String SSPurchaseInvoiceItemEntity = ".SSPurchaseInvoiceItem";
    public static final String SSPurchaseInvoiceEntity = ".SSPurchaseInvoice";
    public static final String SSInvoiceItemDetailEntity = ".SSInvoiceItemDetail";
    public static final String SSPurchaseInvoiceItemDetailEntity = ".SSPurchaseInvoiceItemDetail";
    public static final String CPStockItemDetails = "CPStockItemDetails";
    public static final String CPStockItemSnos = "CPStockItemSnos";
    public static final String CPStockItemSnosEntity = ".CPStockItemSno";
    public static final String CPStockItems = "CPStockItems";
    public static final String SPStockItems = "SPStockItems";
    public static final String SPStockItemSNos = "SPStockItemSNos";
    public static final String SPStockItemsEntity = ".SPStockItem";
    public static final String SPStockItemsSNoEntity = ".SPStockItemSNo";
    public static final String SegmentedMaterials = "SegmentedMaterials";
    public static final String SegmentedMaterialEntity = ".SegmentedMaterial";
    public static String SchemeFreeMatGrpMaterials = "SchemeFreeMatGrpMaterials";
    public static String SchemeItems = "SchemeItems";
    public static String SchemeItemDetails = "SchemeItemDetails";
    public static String SchemeSlabs = "SchemeSlabs";
    public static String SchemeGeographies = "SchemeGeographies";
    public static String SchemeCPs = "SchemeCPs";
    public static String SchemeSalesAreas = "SchemeSalesAreas";
    public static String SchemeFreeMatGrps = "SchemeFreeMatGrps";
    public static String SchemeFreeMatGrpEntity = ".SchemeFreeMatGrp";
    public static String SchemeFreeMatGrpMaterialEntity = ".SchemeFreeMatGrpMaterial";
    public static String SchemeCostObjects = "SchemeCostObjects";
    public static final String SSInvoiceTypes = "SSInvoiceTypes";
    public static final String SSInvoiceTypeEntity = ".SSInvoiceType";
    public static String GoodsIssueFromID = "GoodsIssueFromID";
    public static String ItemMin = "ItemMin";
    public static final String Schemes = "Schemes";
    public static String CPGroup2Desc = "CPGroup2Desc";
    public static String SalesAreaGUID = "SalesAreaGUID";
    public static String CPGroup1ID = "CPGroup1ID";
    public static String CPGroup1Desc = "CPGroup1Desc";
    public static String CPGroup3Desc = "CPGroup3Desc";
    public static String DivisionID = "DivisionID";
    public static String CPGroup4Desc = "CPGroup4Desc";
    public static String DistributionChannelID = "DistributionChannelID";
    public static String CPGroup3ID = "CPGroup3ID";
    public static String SalesOrgDesc = "SalesOrgDesc";
    public static String DistributionChannelDesc = "DistributionChannelDesc";
    public static String DivisionDesc = "DivisionDesc";
    public static String CPGroup4ID = "CPGroup4ID";
    public static String SalesOrgID = "SalesOrgID";
    public static String CPGroup2ID = "CPGroup2ID";
    public static String syncStartTime = "";
    public static String ChannelPartner = "ChannelPartner";

    //sync history
    public static String SyncHisGuid = "SyncHisGuid";
    public static String Collection = "Collection";
    public static String SyncDate = "SyncDate";
    public static String SyncTime = "SyncTime";
    public static String SyncApplication = "Application";
    public static final String SyncHistorysENTITY = ".SyncHistory";
    public static String SyncTypeDesc = "SyncTypeDesc";
    public static String SyncType = "SyncType";
    public static String SyncHistroy = "SyncHistorys";
    public static final String isIDPResponseGot = "isIDPResponseGot";
    public static String PartnerId = "PartnerId";
    public static String Sync_All = "000001";
    public static String DownLoad = "000002";
    public static String UpLoad = "000003";
    public static String Auto_Sync = "000004";
    public static String Sync_initial = "000000";
    public static String Sync_FR_START = "000999";
    public static String Sync_FR_CAMERA = "000998";
    public static String Sync_FR_CAMERA_CAPUTURED = "000997";
    public static String Sync_FR_POST = "000996";
    public static String Sync_FR_POST_END = "000995";
    public static String Sync_FR_STATUS_601 = "000994";
    public static String Sync_FR_STATUS_602 = "000993";
    public static String Sync_FR_STATUS_603 = "000992";
    public static String Sync_FR_STATUS_604 = "000991";
    public static String Sync_FR_STATUS_606 = "000990";
    public static String Sync_FR_STATUS_607 = "000989";
    public static String Sync_FR_RETRY = "000988";
    public static String Sync_FR_ATT_MARK = "000987";
    public static String Sync_FR_ATT_POST = "000986";
    public static String Sync_FR_ATT_POST_END = "000985";
    public static String Sync_FR_END = "000984";
    public static String Sync_FR_LOCATION_PERM = "000979";
    public static String Sync_FR_LOCATION_NOT_PERM = "000978";
    public static String Sync_FR_GPS_PERM = "000977";
    public static String Sync_FR_GPS_NOT_PERM = "000976";
    public static String Sync_FR_AUTODATE_PERM = "000975";
    public static String Sync_FR_AUTODATE_NOT_PERM = "000974";
    public static String Sync_FR_ATT_SAVING = "000973";
    public static String Sync_FR_ATT_SAVING_LOC = "000972";
    public static String Sync_Day_login = "000006";
    public static String add_outlet_start = "000007";
    public static String add_outlet_mob_start = "000008";
    public static String add_outlet_post_start = "000009";
    public static String add_outlet_refresh_start = "000010";
    public static String SO_SYNC_BG = "000012";
    public static String EndSync = "End";
    public static String StartSync = "Start";
    public static String LoginId = "LoginId";
    public static String APKVersion = "APKVersion";
    public static String APKVersionCode = "APKVersionCode";
    public static String MobileModel = "MobileModel";
    public static String DeviceID = "DeviceID";
    public static String RefGUID = "RefGUID";

    /*log msg*/
    public static final String OfflineStoreOpenFailed = "offlineStoreOpenFailed";
    public static final String OfflineStoreOpenedFailed = "Offline store opened failed";
    public static final String OfflineStoreStateChanged = "offlineStoreStateChanged";
    public static final String OfflineStoreOpenFinished = "offlineStoreOpenFinished";
    public static final String OfflineStoreNotification = "OfflineStoreNotification";
    public static final String RequestFlushResponse = "requestFlushResponse - status code ";
    public static final String OfflineStoreRequestFailed = "offlineStoreRequestFailed";
    public static final String PostedSuccessfully = "posted successfully";
    public static final String SynchronizationCompletedSuccessfully = "Synchronization completed successfully";
    public static final String OfflineStoreFlushStarted = "offlineStoreFlushStarted";
    public static final String OfflineStoreFlushFinished = "offlineStoreFlushFinished";
    public static final String OfflineStoreFlushSucceeded = "offlineStoreFlushSucceeded";
    public static final String OfflineStoreFlushFailed = "offlineStoreFlushFailed";
    public static final String FlushListenerNotifyError = "FlushListener::notifyError";
    public static final String offlineStoreRequestFailed = "offlineStoreRequestFailed";
    public static final String offline_store_not_closed = "Offline store not closed: ";
    public static final String invalid_payload_entityset_expected = "Invalid payload:EntitySet expected but got ";
    public static final String RequestCacheResponse = "requestCacheResponse";
    public static final String RequestFailed = "requestFailed";
    public static final String Status_message = "status message";
    public static final String Status_code = "status code";
    public static final String RequestFinished = "requestFinished";
    public static final String RequestServerResponse = "requestServerResponse";
    public static final String BeforeReadRequestServerResponse = "Before Read requestServerResponse";
    public static final String BeforeReadentity = "Before Read entity";
    public static final String AfterReadentity = "After Read entity";
    public static final String RequestStarted = "requestStarted";
    public static final String OfflineRequestListenerNotifyError = "OfflineRequestListener::notifyError";
    public static final String ErrorWhileRequest = "Error while request";
    public static final String error_txt = "Error :";
    public static final String OfflineStoreRefreshStarted = "OfflineStoreRefreshStarted";
    public static final String OfflineStoreRefreshSucceeded = "OfflineStoreRefreshSucceeded";
    public static final String OfflineStoreRefreshFailed = "OfflineStoreRefreshFailed";
    public static final String SyncOnRequestSuccess = "Sync::onRequestSuccess";
    public static final String ERROR_ARCHIVE_COLLECTION = "ErrorArchive";
    public static final String ERROR_ARCHIVE_ENTRY_REQUEST_METHOD = "RequestMethod";
    public static final String ERROR_ARCHIVE_ENTRY_REQUEST_BODY = "RequestBody";
    public static final String ERROR_ARCHIVE_ENTRY_HTTP_CODE = "HTTPStatusCode";
    public static final String ERROR_ARCHIVE_ENTRY_MESSAGE = "Message";
    public static final String ERROR_ARCHIVE_ENTRY_CUSTOM_TAG = "CustomTag";
    public static final String ERROR_ARCHIVE_ENTRY_REQUEST_URL = "RequestURL";
    public static final String error = "error";
    public static final String message = "message";
    public static final String value = "value";
    public static final String error_txt1 = "Error";
    public static final String error_archive_called_txt = "Error Arcive is called";


    public static final String Periodicity = "Periodicity";

    /*tcode*/
    public static final String isAdhocVisitEnabled = "isAdhocVisitEnabled";
    public static final String isAdhocVistTcode = "/ARTEC/ADHOC_VST";
    public static final String isMyTargetsEnabled = "isMyTargetsEnabled";
    public static final String isMyTargetsTcode = "/ARTEC/SS_MYTRGTS";
    public static final String isBehaviourEnabled = "isBehaviourEnabled";
    public static final String isBehaviourTcode = "/ARTEC/SS_SPCP_EVAL";
    public static final String isSchemeEnabled = "isSchemeEnabled";
    public static final String isSchemeTcode = "/ARTEC/SS_SCHEMES";
    public static final String isVisualAidEnabled = "isVisualAidEnabled";
    public static final String isVisualAidTcode = "/ARTEC/SS_VSLADS";
    public static final String isDigitalProductEntryEnabled = "isDigitalProductEntryEnabled";
    public static final String isDigitalProductEntryTcode = "/ARTEC/SS_DGTPRD";
    public static final String isDBStockEnabled = "isDBStockEnabled";
    public static final String isDBStockTcode = "/ARTEC/SS_DBSTK";
    public static final String isSOCreateKey = "isSOCreate";
    public static final String isSOCreateTcode = "/ARTEC/SS_SOCRT";
    public static final String isCollCreateEnabledKey = "isCollCreateEnabled";
    public static final String isCollCreateTcode = "/ARTEC/SS_FICRT";
    public static final String isSampleDisbursmentEnabledKey = "isSampleDisbursmentCreateEnabled";
    public static final String isSampleDisbursmentCreateTcode = "/ARTEC/SS_SAMPCRT";
    public static final String isCustomerComplaintEnabledKey = "isCustomerComplaintCreateEnabled";
    public static final String isCustomerComplaintCreateTcode = "/ARTEC/SF_CUSTCOMPCRT";
    public static String isWindowDisplayKey = "isWindowDisplayEnabled";
    public static String isWindowDisplayTcode = "/ARTEC/SS_WIN_DISPLAY";
    public static final String isMerchReviewKey = "isMerCreateEnabled";
    public static final String isMerchReviewTcode = "/ARTEC/SS_MERRVW";
    public static final String isMerchReviewListKey = "isMerCreateListEnabled";
    public static final String isMerchReviewListTcode = "/ARTEC/SS_MERRVWLST";
    public static final String isStockListKey = "isStocksListEnabled";
    public static final String isStockListTcode = "/ARTEC/SS_CPSTKLIST";
    public static final String isFeedBackListKey = "isFeedBackListEnabled";
    public static final String isFeedBackListTcode = "/ARTEC/SF_FDBKLIST";
    public static final String isSecondarySalesListKey = "isSecondarySalesListEnabled";
    public static final String isSecondarySalesListTcode = "/ARTEC/SS_SOLIST";
    public static final String isSecondaryInvoiceListTcode = "/ARTEC/SS_INVHIS";
    public static final String isSecondaryInvoiceListKey = "isSecondaryInvoiceListEnabled";
    public static final String isCollListTcode = "/ARTEC/SS_COLLHIS";
    public static final String isCollListKey = "isCollListEnabled";
    public static final String isOutstandingListTcode = "/ARTEC/SS_OUTSTND";
    public static final String isOutstandingListKey = "isOutstandingListEnabled";
    public static final String isMustSellKey = "isMustSellEnabled";
    public static final String isMustSellTcode = "/ARTEC/MC_MSTSELL";
    public static final String isFocusedProductKey = "isFocusedProductEnabled";
    public static final String isFocusedProductTcode = "/ARTEC/SS_FOCPROD";
    public static final String isNewProductKey = "isNewProductEnabled";
    public static final String isNewProductTcode = "/ARTEC/SS_NEWPROD";
    public static final String isCompInfoEnabled = "isCompInfoEnabled";
    public static final String isCompInfoTcode = "/ARTEC/SS_COMPINFO";
    public static final String isCompetitorListKey = "isCompetitorListEnabled";
    public static final String isCompetitorListTcode = "/ARTEC/SF_COMPINFOLST";
    public static final String isFeedbackCreateKey = "isFeedbackCreateEnabled";
    public static final String isFeedbackTcode = "/ARTEC/SS_FDBKCRT";
    public static final String isInvoiceCreateKey = "isCreateInvoiceEnabled";
    public static final String isInvoiceTcode = "/ARTEC/SS_INVCRT";
    public static final String isReturnOrderCreateEnabled = "isReturnOrderCreateEnabled";
    public static final String isReturnOrderTcode = "/ARTEC/SS_RETURNORDER";
    public static final String isDaySummaryKey = "isDaySummaryEnabled";
    public static final String isDaySummaryTcode = "/ARTEC/SS_DAYSMRY";
    public static final String isDlrBehaviourKey = "isBehaviourEnabled";
    public static final String isDlrBehaviourTcode = "/ARTEC/SS_SPCP_EVAL";
    public static final String isRetailerStockKey = "isRetailerStock";
    public static final String isRetailerStockTcode = "/ARTEC/SS_CPSTK";
    public static final String isVisitSummaryKey = "isVisitSummaryEnabled";
    public static final String isVisitSummaryTcode = "/ARTEC/SS_VISTSMRY";
    public static final String isExpenseEntryKey = "isExpenseEntryEnabled";
    public static final String isExpenseEntryTcode = "/ARTEC/SS_EXP_ENTRY";
    public static final String isExpenseListKey = "isExpenseListEnabled";
    public static final String isExpenseListTcode = "/ARTEC/SS_EXPLIST";
    public static final String isReturnOrderListKey = "isReturnOrderListEnabled";
    public static final String isReturnOrderListTcode = "/ARTEC/SS_ROLIST";
    public static final String isSampleDisbursmentListKey = "isSampleDisbursmentListEnabled";
    public static final String isSampleDisbursmentListTcode = "/ARTEC/SS_SMPLST";
    public static final String isCustomerComplaintListKey = "isCustomerComplaintListEnabled";
    public static final String isCustomerComplaintListTcode = "/ARTEC/SS_CUSTCOMPLST";
    public static final String isRetailerListEnabled = "isRetailerListEnabled";
    public static final String isRetailerListTcode = "/ARTEC/SS_CP_GETLST";
    public static String isStartCloseEnabled = "isStartCloseEnabled";
    public static String isStartCloseTcode = "/ARTEC/MC_ATTND";
    public static final String isRouteEnabled = "isRouteEnabled";
    public static final String isRoutePlaneTcode = "/ARTEC/SS_ROUTPLAN";
    public static String isRetailerApprovalKey = "isRetailerApprovalKey";
    public static String isRetailerApprovalTcode = "/ARTEC/SS_RET_APRL";
    public static final String isCreateRetailerKey = "isCreateRetailerEnabled";
    public static final String isCreateRetailerTcode = "/ARTEC/SS_CP_CRT";
    public static final String isUpdateRetailerKey = "isRetailerUpdate";
    public static final String isUpdateRetailerTcode = "/ARTEC/SS_CP_CHG";
    public static final String isRetailerTrendKey = "isRetailerTendEnabled";
    public static final String isRetailerTrendTcode = "/ARTEC/SS_TRENDS";
    public static final String isSyncHistroyKey = "isSyncHistroyEnabled";
    public static final String isSyncHistroyTcode = "/ARTEC/SS_SYNC_HSTY_CRT";
    public static final String isSyncHistroyListKey = "isSyncHistroyListKey";
    public static final String isSyncHistroyListTcode = "/ARTEC/SS_SYNC_HSTY_LST";
    /*store related error*/
    public static final String Error = "Error";
    public static final String Error_code_missing = "";
    /*others*/
    public static String x_csrf_token = "";
    public static final String Fresh = "Fresh";
    public static int ErrorCode = 0;
    public static int ErrorNo = 0;
    public static int ErrorNoSyncLog = 0;
    public static int ErrorNoTechincalCache = 0;
    public static int ErrorNo_Get_Token = 0;
    public static String ErrorName = "";
    public static String NetworkError_Name = "NetworkError";
    public static String Comm_error_name = "Communication error";
    public static String Network_Name = "Network";
    public static String Unothorized_Error_Name = "401";
    public static String Max_restart_reached = "Maximum restarts reached";
    public static int Network_Error_Code = 101;
    public static int Comm_Error_Code = 110;
    public static int UnAuthorized_Error_Code = 401;
    public static int UnAuthorized_Error_Code_Offline = -10207;
    public static int Network_Error_Code_Offline = -10205;
    public static int Unable_to_reach_server_offline = -10208;
    public static int Resource_not_found = -10210;
    public static int DATABASE_ERROR = -10005;
    public static int Unable_to_reach_server_failed_offline = -10204;
    public static int Build_Database_Failed_Error_Code1 = -100036;
    public static int Build_Database_Failed_Error_Code2 = -100097;
    public static int Build_Database_Failed_Error_Code3 = -10214;
    public static int Database_Transction_Failed_Error_Code = -10104;
    public static String Invalid_input_Error_Code = "-10133";
    public static String Executing_SQL_Commnd_Error = "10001";
    public static int Execu_SQL_Error_Code = -10001;
    public static int Store_Def_Not_matched_Code = -10247;
    public static String Store_Defining_Req_Not_Matched = "10247";
    public static String RFC_ERROR_CODE_100027 = "100027";
    public static String RFC_ERROR_CODE_100029 = "100029";
    public static String Invalid_Store_Option_Value = "InvalidStoreOptionValue";
    public static HashMap<String, Object> MapEntityVal = new HashMap<String, Object>();
    public static HashSet<String> kpiNames = new HashSet<>();
    public static ArrayList<String> AL_ERROR_MSG = new ArrayList<>();
    public static Set<String> Entity_Set = new HashSet<>();
    public static String CreateOperation = "Create";
    public static String ReadOperation = "Read";
    public static String UpdateOperation = "Update";
    public static String DeleteOperation = "Delete";
    public static String QueryOperation = "Query";
    public static final String MerchList = "MerchList";
    public static final String Merchendising_Snap = "Merchendising Snapshot";

    /*default value*/
    public static boolean mBoolIsReqResAval = false;
    public static boolean mBoolIsNetWorkNotAval = false;
    public static String Route_Plan_No = "";
    public static String Route_Plan_Desc = "";
    public static String Route_Plan_Key = "";
    public static String Route_Schudle_GUID = "";
    public static final String BeatPlan = "BeatPlan";
    public static boolean isSync = false;
    public static boolean isBGSync = false;
    public static boolean isOrderSync = false;
    public static String ALL = "ALL";
    public static String All = "All";
    public static String None = "None";
    public static String yes = "Yes";
    public static String No = "No";
    public static String SOITST = "SOITST";
    public static String DELVST = "DELVST";
    public static String ClosingeDay = "ClosingeDay";
    public static String Today = "Today";
    public static String PreviousDay = "PreviousDay";
    public static String ClosingeDayType = "ClosingeDayType";
    public static final String AdhocVisitCatID = "02";
    public static final String BeatVisitCatID = "01";
    public static final String OtherBeatVisitCatID = "02";
    public static final String X = "X";
    public static final String isLocalFilterQry = "?$filter= sap.islocal() ";
    public static final String isNonLocalFilterQry = "?$filter= not sap.islocal() ";
    public static final String device_reg_failed_txt = "Device registration failed";
    public static final String SHOWNOTIFICATION = "SHOWNOTIFICATION";
    public static final String timeStamp = "TimeStamp";
    public static final String sync_table_history_txt = "Sync table(History)";
    public static boolean isStoreClosed = false;
    public static final String str_00 = "00";
    public static final String str_01 = "01";
    public static final String str_2 = "2";
    public static String str_02 = "02";
    public static final String str_04 = "04";
    public static final String str_03 = "03";
    public static final String str_3 = "3";
    public static final String str_1 = "1";
    public static final String str_06 = "06";
    public static final String str_05 = "05";
    public static final String str_20 = "20";
    public static final String SecondarySOCreate = "Secondary SO Create";
    public static final String SecondarySOCreateTemp = "Secondary SO Create Temp";
    public static final String OUTLET_LOCKED = "OUTLET_LOCKED";
    public static final String RetailerStock = "RetailerStock";
    public static String TLSD = "TLSD";
    public static final String username = "username";
    public static String IsfreeGoodsItem = "IsfreeGoodsItem";
    public static String SOCreateID = "06";
    public static String CompetitorId = "08";
    public static String CustomerCompCreateID = "10";
    public static String RetailerStockID = "07";
    public static String CollCreateID = "02";
    public static String MerchReviewCreateID = "01";
    public static String CompInfoCreateID = "04";
    public static String FeedbackID = "03";
    public static String SampleDisbursementID = "09";
    public static String ROCreateID = "08";
    public static final String ITEM_TXT = "ITEMS";
    public static final String ITEM_TXT_INFRA = "ITEMS_INFRA";
    public static final String ITEM_TXT_DOC_SPLIT = "ITEM_TXT_DOC_SPLIT";
    public static final String ITEM_TXT_CPROUTE = "ITEM_TXT_CPROUTE";
    public static String DSTSTKVIEW = "DSTSTKVIEW";
    public static String SchemeDetails = "SchemeDetails";
    public static String CPSTKAUOM1 = "CPSTKAUOM1";
    public static String PRICALBTC = "PRICALBTC";
    public static String DBSTKTYPE = "DBSTKType";
    public static String MSLInd = "MSLInd";
    public static String FocussedInd = "FocussedInd";
    public static String CrossSell = "CrossSell";
    public static String UPSell = "UPSell";
    public static String SOQ = "SOQ";
    public static String SellIndicator = "SellIndicator";
    public static String ROList = "ROList";
    public static String DR = "DR";
    public static String CS = "CS";
    public static String US = "US";
    public static final String Requestsuccess_status_message_key = "requestsuccess - status message key";
    public static final String RequestFailed_status_message = "requestFailed - status message ";
    public static final String RequestServerResponseStatusCode = "requestServerResponse - status code";
    public static final String OnlineRequestListenerNotifyError = "OnlineRequestListener::notifyError";
    public static final String FeedbackCreated = "Feedback created";
    public static final String ExpenseCreated = "Expense created";
    public static final String RequestsuccessStatusMessageBeforeSuccess = "requestsuccess - status message before success";
    public static final String[] ORG_MONTHS = {"Jan", "Feb", "Mar", "Apr",
            "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    public static final String[] NEW_MONTHSCODE = {"11", "12", "01", "02",
            "03", "04", "05", "06", "07", "08", "09", "10"};
    public static final String RequestID = "RequestID";
    public static final String RepeatabilityCreation = "RepeatabilityCreation";
    public static final String T = "T";
    public static ODataDuration mStartTimeDuration = null;
    public static double DoubGetRunningSlabPer = 0.0;
    public static String SchemeCatIDInstantScheme = "000002";
    public static String BasketCatID = "000002";
    public static String SchemeCatIDQPSScheme = "000001";
    public static String SchemeTypeIDBasketScheme = "000008";
    public static final String APP_UPGRADE_TYPESET_VALUE = "MSEC";

    // Default Names
    public static final String RetailerApprovalList = "RetailerApprovalList";
    public static final String RetailerList = "RetailerList";
    public static final String RetailerDefList = "RetailerDefList";
    public static final String ItemList = "ItemList";
    public static final String Retailer = "Retailer";
    public static final String NAVFROM = "NAVFROM";
    public static final String AdhocList = "AdhocList";
    public static final String comingFrom = "ComingFrom";
    public static final String REGISTRATION = "REGISTRATION";
    public static final String Position = "Position";
    public static final String RPD = "RPD";
    public static final String VisitType = "VisitType";
    public static final String OtherRouteGUID = "OtherRouteGUID";
    public static final String OtherRouteName = "OtherRouteName";
    public static final String BeatType = "BeatType";
    public static String SalesDistrictID = "SalesDistrictID";
    public static final String EXTRA_TITLE = "extraTitle";
    public static final String MTPList = "MTPList";
    public static final String tel_txt = "tel:";
    //    public static String ComingFromCreateSenarios = "";
    public static final String Visit = "Visit";
    public static final String Reports = "Reports";
    public static final String Summary = "Summary";
    public static final String RetName = "RetName";
    public static boolean isAllSync = false;
    public static MSFAApplication mApplication = null;

    // Data base
    public static String DATABASE_NAME = "mSecSales.db";
    public static boolean devicelogflag = false;
    public static boolean importdbflag = false;
    public static boolean FlagForUpdate = false;
    public static boolean FlagForSecurConnection = false;
    public static final String TimeStamp = "TimeStamp";
    public static final String SyncTableHistory = "Sync table(History)";
    public static final String delete_from = "DELETE FROM ";
    public static final String default_value_double = "0.0";
    public static final String default_value_int = "0";
    public static final String create_table = "create table IF NOT EXISTS ";
    public static final String EventsData = "EventsData";
    public static final String on_Create = "onCreate:";
    public static String DATABASE_REGISTRATION_TABLE = "registrationtable";
    public static final String[] reportsArray = {"SOs", "ROs", "Invoices", "Collections", "Merchandising", "Feedbacks", "Outstandings", "Competitor Info","Complaints","Retailer Trends","Sample Disbursement"};
    public static int[] IconVisibiltyReportFragment = {0, 0, 0, 0, 0, 0, 0, 0,0,0,0};
    public static int[] IconPositionReportFragment = {0, 0, 0, 0, 0, 0, 0, 0,0,0,0};
    public static final String whatsapp_packagename = "com.whatsapp";
    public static final String whatsapp_conv_packagename = "com.whatsapp.Conversation";
    public static final String whatsapp_domainname = "@s.whatsapp.net";
    public static final String jid = "jid";
    public static final String sms_txt = "sms:";
    public static final String plain_text = "plain/text";
    public static final String send_email = "Send your email in:";
    public static boolean iSAutoSync = false;
    public static String ValidTo = "ValidTo";
    public static String strErrorWithColon = "Error : ";
    public static final String PartnerGUID = "PartnerGUID";
    public static final String PartnerNo = "PartnerNo";
    public static final String TargetQty = "TargetQty";
    public static final String ActualQty = "ActualQty";
    public static final String TargetValue = "TargetValue";
    public static final String OtherRefDesc = "OtherRefDesc";
    public static final String ActualValue = "ActualValue";
    public static final String TargetGUID = "TargetGUID";
    public static final String MaterialNo = "MaterialNo";
    public static final String Earning = "Earning";
    public static final String IsearningAdj = "IsearningAdj";
    public static final String MaxEarning = "MaxEarning";
    public static String MaterialDesc = "MaterialDesc";
    public static final String OrdMatGrp = "OrdMatGrp";
    public static final String MaterialGroup = "MaterialGroup";
    public static final String MaterialGrpDesc = "MaterialGrpDesc";
    public static String OrderMaterialGroupDesc = "OrderMaterialGroupDesc";
    public static String OrderMaterialGroupID = "OrderMaterialGroupID";
    public static final String KPICode = "KPICode";
    public static final String KPIName = "Name";
    public static final String DisplayName = "DisplayName";
    public static final String KPIFor = "KPIFor";
    public static final String CalculationSource = "CalculationSource";
    public static final String CalculationBase = "CalculationBase";
    public static final String KPICategory = "KPICategory";
    public static final String SPChannelEvaluationList = "SPChannelEvaluationList";
    public static final String Month = "Month";
    public static final String Year = "Year";
    public static final String Period = "Period";
    public static final String RollUpTo = "RollUpTo";
    public static final String RollupStatus = "RollupStatus";
    public static final String RollupStatusDesc = "RollupStatusDesc";
    public static String SegmentId = "SegmentId";
    public static String OrderMaterialGroup = "OrderMaterialGroup";
    public static final String QtyMonth1PrevPerf = "QtyMonth1PrevPerf";
    public static final String QtyMonth2PrevPerf = "QtyMonth2PrevPerf";
    public static final String QtyMonth3PrevPerf = "QtyMonth3PrevPerf";
    public static final String ReportOnID = "ReportOnID";

    /*auto sync*/
    public static boolean isDayStartSyncEnbled = false;
    public static int mErrorCount = 0;

    public static String EvaluationTypeID = "EvaluationTypeID";
    public static String UOM = "UOM";
    public static String ExpiryMonth = "ExpiryMonth";
    public static String ExpiryYear = "ExpiryYear";
    public static String NetValue = "NetValue";
    public static final String AlternativeUOM1 = "AlternativeUOM1";
    public static final String AlternativeUOM2 = "AlternativeUOM2";
    public static String isSOApprovalKey = "isSOApprovalEnabled";
    public static String isSOApprovalTcode = "/ARTEC/SF_SOAPRL";

    public static String isMtpApprovalkey = "isMtpApprovalEnabled";
    public static String isMtpApprovalTcode = "/ARTEC/SF_MTP_CRT";


    public static String isContractApprovalkey = "isContractApprovalEnabled";
    public static String isContractApprovalTcode = "/ARTEC/SF_CNTR_APPRL";

    public static String isCreditLimitApprkey = "isCCreditLimitApprEnabled";
    public static String isCreditLimitApprTcode = "/ARTEC/SF_CRD_LMT_APPRL";

    public static String isPendingDispatchkey = "isPendingDispatchEnabled";
    public static String isPendingDispatchTcode = "/ARTEC/SF_PND_DISP";
    public static ArrayList<MTPRoutePlanBean> alTodayBeatCustomers = new ArrayList<>();
    public static String SO_Cust_QRY = "";
    public static ArrayList<String> alCustomers = new ArrayList<>();


    /*bundle*/
    public static final String BUNDLE_RESOURCE_PATH = "resourcePath";
    public static final String BUNDLE_OPERATION = "operationBundle";
    public static final String BUNDLE_REQUEST_CODE = "requestCodeBundle";
    public static final String BUNDLE_SESSION_ID = "sessionIdBundle";
    public static final String BUNDLE_SESSION_REQUIRED = "isSessionRequired";
    public static final String BUNDLE_SESSION_URL_REQUIRED = "isSessionTOUrlRequired";
    public static final String BUNDLE_SESSION_TYPE = "sessionTypeBundle";
    public static final String BUNDLE_IS_BATCH_REQUEST = "isBatchRequestBundle";
    public static final String EXTRA_SESSION_REQUIRED = "isSessionRequired";
    public static final String Tasks = "Tasks";
    public static final String CustomerCreditLimitDocs = "CustomerCreditLimitDocs";
    public static final String TaskHistorys = "TaskHistorys";
    public static String InstanceID = "InstanceID";
    public static String SSROs = "SSROs";
    public static String SSROItemDetails = "SSROItemDetails";
    public static String ReturnOrders = "ReturnOrders";
    public static String ReturnOrderItems = "ReturnOrderItems";
    public static String ReturnOrderItemDetails = "ReturnOrderItemDetails";
    public static String RetOrdNo = "RetOrdNo";
    public static String InvoiceQty = "InvoiceQty";
    public static final String GRStatusID = "GRStatusID";
    public static String UnitPrice = "UnitPrice";
    public static String ScreenID = "ScreenID";
    public static String IncentiveAmount = "IncentiveAmount";
    public static final String StorLoc = "StorLoc";
    public static final String Plant = "Plant";
    public static final String PlantDesc = "PlantDesc";
    public static final String DelvQty = "DelvQty";
    public static String OpenQty = "OpenQty";
    public static final String SalesAreaDesc = "SalesAreaDesc";
    public static String OrderTypeDesc = "OrderTypeDesc";
    public static String ShipToPartyName = "ShipToPartyName";
    public static String ShipToParty = "ShipToParty";
    public static String TransporterID = "TransporterID";
    public static String TransporterName = "TransporterName";
    public static final String UnloadingPoint = "UnloadingPoint";
    public static final String ReceivingPoint = "ReceivingPoint";
    public static final String PaytermDesc = "PaytermDesc";
    public static final String Payterm = "Payterm";
    public static final String Incoterm1 = "Incoterm1";
    public static final String Incoterm2 = "Incoterm2";
    public static final String Incoterm1Desc = "Incoterm1Desc";
    public static final String ShippingTypeID = "ShippingTypeID";
    public static String ShippingTypeDesc = "ShippingTypeDesc";
    public static String SaleOffDesc = "SaleOffDesc";
    public static final String SalesGroup = "SalesGroup";
    public static String SaleGrpDesc = "SaleGrpDesc";
    public static String DelvStatus = "DelvStatus";
    public static final String MeansOfTranstyp = "MeansOfTranstyp";
    public static final String MeansOfTranstypDesc = "MeansOfTranstypDesc";
    public static final String SOPartnerFunctions = "SOPartnerFunctions";
    public static final String PartnerFunctionID = "PartnerFunctionID";
    public static String SOConditions = "SOConditions";
    public static String CondCurrency = "CondCurrency";
    public static String ConditionAmount = "ConditionAmount";
    public static String ConditionAmtPer = "ConditionAmtPer";
    public static String ConditionValue = "ConditionValue";
    public static final String RegionDesc = "RegionDesc";
    public static final String CountryDesc = "CountryDesc";
    public static final String PartnerFunctionDesc = "PartnerFunctionDesc";
    public static final String VendorNo = "VendorNo";
    public static final String VendorName = "VendorName";
    public static final String PersonnelName = "PersonnelName";
    public static final String PersonnelNo = "PersonnelNo";
    public static String Mobile2 = "Mobile2";
    public static String RegionIDRegionID = "RegionIDRegionID";
    public static String PaymentMethod = "";
    public static String IssuingBank = "";
    public static final String SALESORDERITEMS = "SalesOrderItems";
    public static String GrossAmount = "GrossAmount";
    public static String Rate = "Rate";
    public static String CashDiscountPer = "CashDiscountPer";
    public static String Freight = "Freight";
    public static String Tax = "Tax";
    public static String Discount = "Discount";
    public static String TaxAmount = "TaxAmount";
    public static String StatusDesc = "StatusDesc";
    public static String DelvStatusDesc = "DelvStatusDesc";
    public static final String ItemCategory = "ItemCategory";
    public static String ItemCatDesc = "ItemCatDesc";
    public static final String DepotStock = "DepotStock";
    public static String OwnStock = "OwnStock";
    public static String RejReason = "RejReason";
    public static String RejReasonDesc = "RejReasonDesc";
    public static String ClosedQuantity = "ClosedQuantity";
    public static String OpenQuantity = "OpenQuantity";
    public static final String DeviceNo = "DeviceNo";
    public static String QAQty = "QAQty";
    public static String EXTRA_TAB_POS = "extraTabPos";
    public static String comingFromChange = "comingFromChange";
    public static final String ORDER_TYPE = "ORDERTYPE";
    public static final String ORDER_TYPE_DESC = "ORDERTYPE_DESC";
    public static final String SALESAREA = "SALESAREA";
    public static final String SALESAREA_DESC = "SALESAREADESC";
    public static final String SOLDTO = "SOLDTO";
    public static final String SOLDTONAME = "SOLDTONAME";
    public static final String SHIPPINTPOINT = "SHIPPINTPOINT";
    public static final String SHIPPINTPOINTDESC = "SHIPPINTPOINTDESC";
    public static final String SHIPTO = "SHIPTO";
    public static final String SHIPTONAME = "SHIPTONAME";
    public static final String FORWARDINGAGENT = "FORWARDINGAGENT";
    public static final String FORWARDINGAGENTNAME = "FORWARDINGAGENTNAME";
    public static final String PLANT = "PLANT";
    public static final String PLANTDESC = "PLANTDSEC";
    public static final String INCOTERM1 = "INCOTERM1";
    public static final String INCOTERM1DESC = "INCOTERM1DESC";
    public static final String INCOTERM2 = "INCOTERM2";
    public static final String SALESDISTRICT = "SALESDISTRICT";
    public static final String SALESDISTRICTDESC = "SALESDISTRICTDESC";
    public static final String ROUTE = "ROUTE";
    public static final String ROUTEDESC = "ROUTEDESC";
    public static final String MEANSOFTRANSPORT = "MEANSOFTRANSPORT";
    public static final String MEANSOFTRANSPORTDESC = "MEANSOFTRANSPORTDESC";
    public static final String STORAGELOC = "STORAGELOC";
    public static final String CUSTOMERPO = "CUSTOMERPO";
    public static final String CUSTOMERPODATE = "CUSTOMERPODATE";
    public static String SalesGrpDesc = "SalesGrpDesc";
    public static String SalesOffDesc = "SalesOffDesc";
    public static String SalesOfficeID = "SalesOfficeID";
    public static String SalesGroupID = "SalesGroupID";
    public static final String SalesOfficeDesc = "SalesOfficeDesc";
    public static Bundle SOBundleExtras = null;
    public static final String SalesOrderItems = "SalesOrderItems";
    public static final String StorLocDesc = "StorLocDesc";
    public static final String StatusUpdate = "StatusUpdate";
    public static String isSOCancelEnabled = "isSOCancelEnabled";
    public static String isSOChangeEnabled = "isSOChangeEnabled";
    public static String LoginID = "LoginID";
    public static final String OrderTypeText = "OrderTypeText";
    public static String REJRSN = "REJRSN";
    public static String Uom = "Uom";
    public static String CPStockItemGUID = "CPStockItemGUID";
    public static String SPStockItemGUID = "SPStockItemGUID";
    public static String StockValue = "StockValue";
    public static String LandingPrice = "LandingPrice";
    public static String WholeSalesLandingPrice = "WholeSalesLandingPrice";
    public static String Margin = "Margin";
    public static String ConsumerOffer = "ConsumerOffer";
    public static String ShelfLife = "ShelfLife";
    public static String TradeOffer = "TradeOffer";
    public static String Batch = "Batch";
    public static String IntrmUnitPrice = "IntrmUnitPrice";
    public static String ISFreeGoodsItem = "ISFreeGoodsItem";
    public static String ItemCat = "ItemCat";
    public static String RoutDesc = "RoutDesc";
    public static String Scheme = "RoutDesc";
    public static String SchemeSize = "SchemeSize";

    public static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };
    public static final int PERMISSION_REQUEST_CODE = 110;
    public static final int STORAGE_PERMISSION = 891;
    public static String Brand="Brand";
    public static String BrandInd="BrandInd";
    public static String InvoiceList="InvoiceList";
        public static String RegSchemeCat = "RegSchemeCat";
    public static String WDSPINVDTR = "WDSPINVDTR";
    public static String SC = "SC";

    public static String EXTRA_SCHEME_NAME = "extraSchemeName";
    public static String EXTRA_SCHEME_IS_SECONDTIME = "isSecondTime";
    public static String EXTRA_SCHEME_TYPE_ID = "extraSchemeTypeIds";
    public static String EXTRA_INVOICE_DATE = "invoiceDate";
    public static String EXTRA_SCHEME_ID = "schemeIds";
    public static String EXTRA_IS_OFFLINE = "isOffline";
    public final static String ExpenseListBean = "ExpenseListBean";
    public static final String KEY_FIRST_TIME_RUN = "firstTimeRun";
    public static final String MaterialDocs = "MaterialDocs";
    public static final String MaterialDocEntity = ".MaterialDoc";
    public static final String MaterialDocItemDetailsEntity = ".MaterialDocItemDetail";
    public static final String MaterialDocItems = "MaterialDocItems";
    public static final String MaterialDocItemDetails = "MaterialDocItemDetails";
    public static final String MaterialDocItemCatSplits = "MaterialDocItemCatSplits";
    public static final String MaterialDocItemsEntity = ".MaterialDocItem";
    public static final String DISTRIBUTOR_NAME = "DISTRIBUTOR_NAME";
    public static final String DISTRIBUTOR_DETAILS = "DISTRIBUTOR_DETAILS";
    public static final String RPD_DASHBOARD_NAME = "RPD_DASHBOARD_NAME";
    public static final String RPD_COMMUNICATION_NAME = "RPD_COMMUNICATION_NAME";
    public static final String SALES_PERSON_NAME = "SALES_PERSON_NAME";
    public static final String SP_NAME = "SP_NAME";
    public static final String RouteScheduleDetails = "RouteScheduleDetails";
    public static final String RouteProductDetTypes = "RouteProductDetTypes";
    public static final String CPProductDetTypes = "CPProductDetTypes";
    public static final String RouteScheduleDetailEntity = ".RouteScheduleDetail";
    public static final String RouteProductDetTypeEntity = ".RouteProductDetType";
    public static final String CPProductDetTypeEntity = ".CPProductDetType";
    public static final String RouteScheduleSPs = "RouteScheduleSPs";
    public static final String RouteScheduleSPsEntity = ".RouteScheduleSP";

    /*defining request for open store*/
//    public static final String DSR = "000006",MD = "000007",MSP ="000005";
    public static final String DSR = "AWSM", CALL_CNTR = "CALLCNTR", MD = "RPD", MSP = "SDA", PSM = "PSM", VAN = "VAN"
            , BCRVAN = "BCRVAN", BVAN = "BVAN";
    public static final String EXTRA_SCHEME_GUID_LIST = "EXTRA_SCHEME_GUID_LIST";
    public static String StockRefGUID = "StockRefGUID";
    public static String MatGrpID = "MatGrpID";
    public static String MatGrpDesc = "MatGrpDesc";
    public static String BeatGuid = "BeatGuid";
    public static String BeatName = "BeatName";
    public static String CPGROUP1 = "CPGRP1";
    public static String ConfigTypsetEntity = ".ConfigTypsetTypeValue";
    public static String FreqOfDispatch = "FreqOfDispatch";
    public static String CPOLSZ = "CPOLSZ";
    public static String CPOLSP = "CPOLSP";
    public static String CPGRP7 = "CPGRP7";
    public static String CPGRP6 = "CPGRP6";
    public static String CPGRP8 = "CPGRP8";
    public static String CPINFRA = "CPINFR";
    public static String ConfigTypesetTypeEntity = ".ConfigTypesetType";
    public static String CPOWNB = "CPOWNB";
    public static String CPINUM = "CPINUM";
    public static String RETAILER_BEAN = "RETAILER_BEAN";
    public static String SearchTerm = "SearchTerm";
    public static String ZoneID = "ZoneID";
    public static String ZoneDesc = "ZoneDesc";
    public static String WardGUID = "WardGUID";
    public static String TownID = "TownID";
    public static String DMSDiv = "DMSDiv";
    public static String CPGRP9 = "CPGRP9";
    public static String IS_GREEN_OUTLET = "IS_GREEN_OUTLET";
    public static String TGT_ULPO = "TGT_ULPO";
    public static String TGT_VALUE = "TGT_VALUE";
    public static String MTD_ULPO = "MTD_ULPO";
    public static String MTD_VALUE = "MTD_VALUE";
    public static String PasswordExpiredMsg = "User is locked or password expired. Click on Change to Password change in Settings or Please Contact support team";
    public static final String IS_IMEI_VERIFIED = "IS_IMEI_VERIFIED";
    public static final String RouteGuid = "RouteGuid";
    public static final String RouteId = "RouteId";
    public static final String SeqInd = "SeqInd";
    public static final String SubBrand = "SubBrand";
    public static final String SubBrandDesc = "SubBrandDesc";
    public static String Mobile3 = "Mobile3";
    public static final String STACKBOX = "STACKBOX";
    public static final String ZWKKPI = "ZWKKPI";
    public static final String STBXKEY = "STBXKEY";
    public static final String INCAMT = "INCAMT";

    public static int NewDefingRequestVersion = 5;
    public static final String SkuGroup = "SkuGroup";
    public static final String SkuGroupDesc = "SkuGroupDesc";
    public static final String AltUom = "AltUom";
    public static String QUODECK_ENROLL = "QUODECK_ENROLL";
    public static String QUODECK_ENROLL_ID = "QUODECK_ENROLL_ID";
    public static String QUODECK_ENROLL_COURSES = "QUODECK_ENROLL_ID_COURSES";
    public static String QUODECK_COURSES = "QUODECK_COURSES";
    public static String QUODECK_COURSES_SKIP_COUNT = "QUODECK_COURSES_SKIP_COUNT";
    public static String QUODECK_COURSES_MESSAGE = "QUODECK_COURSES_MESSAGE";


    public static final String BillGenerate = "BillGenerate";
    public static final String position = "position";
    public static String SELECTED_UOM = "SELECTED_UOM";
    public static String TXNLnkGuid = "TXNLnkGuid";
    public static String PreTXNType = "PreTXNType";
    public static String PreTXNRef = "PreTXNRef";
    public static String InvoiceItemGuid = "InvoiceItemGuid";
    public static String SubsTXNType = "SubsTXNType";
    public static String SubsTXNRef = "SubsTXNRef";
    public static String PreTXNQty = "PreTXNQty";
    public static String PreTXNUom = "PreTXNUom";
    public static String PreTXNAmount = "PreTXNAmount";
    public static String PreTXNCurrency = "PreTXNCurrency";
    public static final String SSInvoiceItemTXNLinks = "SSInvoiceItemTXNLinks";
    public static final String BillToDesc = "BillToDesc";
    public static final String BillToID = "BillToID";
    public static final String CPGroup1 = "CPGroup1";
    public static final String CPGroup2 = "CPGroup2";
    public static final String CPGroup3 = "CPGroup3";
    public static final String CPGroup4 = "CPGroup4";
    public static final String DeliveryDate = "DeliveryDate";
    public static final String InvoiceValue = "InvoiceValue";
    public static final String RoundOff = "RoundOff";
    public static final String SchemeReqd = "SchemeReqd";
    public static final String TaxableAmount = "TaxableAmount";
    public static final String HsnCode = "HsnCode";
    public static final String DiscChngMnul = "DiscChngMnul";
    public static final String SPNumberRangeSet = "SPNumberRangeSet";
    public static final String SPNumberRangeEntity = ".SPNumberRange";
    public static final String BomMatIndicator = "BomMatIndicator";
    public static final String isSurveyCreateKey = "isSurveyCreate";
    public static final String isSurveyCreateTcode = "/ARTEC/SS_SRVY_CRT";
    public static final String StockConversion = "StockConversion";
    public static final String StockQty = "StockQty";
    public static final String AutoPasswordForReset = "AutoPasswordForReset";
    public static String SPCDV03072025 = "SPCDV03072025";

    public static String[] getDefiningReq(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);
        String loginId = sharedPreferences.getString("username", "");
        String rollID = ConstantsUtils.getAuthInformation(context);
        if (sharedPreferences.getInt(CURRENT_VERSION_CODE, 0) <= 2) {
            if (rollID != null && !TextUtils.isEmpty(rollID)) {
                switch (rollID){
                    case DSR: // AWSM
                        MSFAApplication.isAWSM =true;
                        return new String[]{
                                Customers,
                                "CPConfigs",SPPayouts,
                                "ChannelPartners",Attendances,
                                "SalesPersons",
                                SSROs+"?$filter=(OrderType eq '000021' or OrderType eq '000020') and StatusID eq '000005'",
                                SSROItemDetails,
                                "UserSalesPersons",
                                "RoutePlans",
                                "CPSummary1Set",
                                "RouteSchedulePlans",
                                "RouteScheduleSPs",
                                "RouteScheduleDetails",
                                "RouteProductDetTypes",
                                SyncHistroy,UserPartners,
                                "CPDMSDivisions",
//                            CPInfras,
                                CPPartnerFunctions,
                                CPPerformances,
                                "RouteSchedules",
                                "SSSOs",
                                "SSSOItemDetails",
                                "CPSPRelations",
                                "Visits",
                                "VisitActivities",
//                            "UserCustomers",
                                "Questions",
                                "VisitQuestionnaires",
                                SubDistricts,
//                            Wards,
                                Towns,
                                Schemes, SchemeItemDetails, SchemeSlabs, SchemeGeographies, SchemeCPs, SchemeSalesAreas,
                                //MaterialDocs,
                                //MaterialDocItems,
                                KPISet,
                                Targets,
                                TargetItems,
                                KPIItems,
//                            Customers,SSInvoiceTypes,SSINVOICES,
                                SSInvoiceItemDetails,
                                SSINVOICES,
                                "CPStockItems?$filter=(StockOwner eq '01')",
                                "CPStockItemSnos?$filter=(StockOwner eq '01')",
                                "SegmentedMaterials",
                                "Brands",
//                              "MaterialCategories",
                                "OrderMaterialGroups",
                                "BrandsCategories",
                                "UserProfileAuthSet?$filter=Application%20eq%20%27MSEC%27",
//                              "UserProfileAuthSet?$filter=Application%20eq%20%27PD%27",
                                "ConfigTypsetTypeValues?$filter=Typeset eq 'OBKDYS' or Typeset eq 'MSEC' or Typeset eq 'STPNCD' or Typeset eq 'ORDENT' or Typeset eq 'ZKPICD' or Typeset eq 'DSTSTKVIEW' or Typeset eq 'ZPRGKP' or Typeset eq 'KPICDE' or Typeset eq 'ATTTYP' or Typeset eq 'RVWTYP' or Typeset eq 'FIPRTY' or Typeset eq 'ACTTYP' or Typeset eq 'SF' or Typeset eq 'SC' or Typeset eq 'SS' or Typeset eq 'MC' or Typeset eq 'SSCP' or Typeset eq 'SSROUT' or Typeset eq 'SSSO' or Typeset eq 'ZCPRGT' or Typeset eq 'ZCPRMT' or Typeset eq 'ZCPROK' or Typeset eq 'ZCPRPR' or Typeset eq 'ZLPRPR' or Typeset eq 'ZLPRMT' or Typeset eq 'ZLPRET' or Typeset eq 'ZLPRGT' or Typeset eq 'ZCPRET' or Typeset eq 'CPGRP2'",
                                "ConfigTypesetTypes?$filter=Typeset eq 'SSSTTY' or Typeset eq 'ZDMGRT' or Typeset eq 'APNRMD' or Typeset eq 'ZCPOVN' or Typeset eq 'SCGOTY' or Typeset eq 'UOMNO0' or Typeset eq 'ANSWID' or Typeset eq 'CPOLSZ' or Typeset eq 'CPOLSP' or Typeset eq 'CPGRP7' or Typeset eq 'CPGRP8' or Typeset eq 'CPINFR' or Typeset eq 'CPOWNB' or Typeset eq 'CPINUM' or Typeset eq 'CPGRP9' or Typeset eq 'CPGRP6' or Typeset eq 'CPDFUM'",
                                "ValueHelps?$filter= ModelID eq 'SSGW_ALL' and (EntityType eq 'Visit' or EntityType eq 'ChannelPartner' or EntityType eq 'SSROItemDetail')"};
                    case CALL_CNTR: // CALL CENTER
                        MSFAApplication.isCallCenter =true;
                        return new String[]{
                                Customers,
                                "CPConfigs",SPPayouts,
                                "ChannelPartners",Attendances,
                                "SalesPersons",
                                SSROs+"?$filter=(OrderType eq '000021' or OrderType eq '000020') and StatusID eq '000005'",
                                SSROItemDetails,
                                "UserSalesPersons",
                                "RoutePlans",
                                "RouteSchedulePlans",
                                "RouteScheduleSPs",
                                "RouteScheduleDetails",
                                "RouteProductDetTypes",
                                SyncHistroy,UserPartners,
                                "CPDMSDivisions",
//                            CPInfras,
                                CPPartnerFunctions,
                                CPPerformances,
                                "RouteSchedules",
                                "SSSOs",
                                "SSSOItemDetails",
                                "CPSPRelations",
                                "Visits",
                                "VisitActivities",
//                            "UserCustomers",
                                "Questions",
                                "VisitQuestionnaires",
                                SubDistricts,
//                            Wards,
                                Towns,
                                Schemes, SchemeItemDetails, SchemeSlabs, SchemeGeographies, SchemeCPs, SchemeSalesAreas,
                                //MaterialDocs,
                                //MaterialDocItems,
//                                KPISet,
//                                Targets,
//                                TargetItems,
//                                KPIItems,
//                            Customers,SSInvoiceTypes,SSINVOICES,SSInvoiceItemDetails,
                                SSINVOICES,
                                "CPStockItems?$filter=(StockOwner eq '01')",
                                "CPStockItemSnos?$filter=(StockOwner eq '01')",
                                "SegmentedMaterials",
                                "Brands",
//                              "MaterialCategories",
                                "OrderMaterialGroups",
                                "BrandsCategories",
                                "UserProfileAuthSet?$filter=Application%20eq%20%27MSEC%27",
//                              "UserProfileAuthSet?$filter=Application%20eq%20%27PD%27",
                                "ConfigTypsetTypeValues?$filter=Typeset eq 'OBKDYS' or Typeset eq 'MSEC' or Typeset eq 'STPNCD' or Typeset eq 'ORDENT' or Typeset eq 'ZKPICD' or Typeset eq 'DSTSTKVIEW' or Typeset eq 'ZPRGKP' or Typeset eq 'KPICDE' or Typeset eq 'ATTTYP' or Typeset eq 'RVWTYP' or Typeset eq 'FIPRTY' or Typeset eq 'ACTTYP' or Typeset eq 'SF' or Typeset eq 'SC' or Typeset eq 'SS' or Typeset eq 'MC' or Typeset eq 'SSCP' or Typeset eq 'SSROUT' or Typeset eq 'SSSO' or Typeset eq 'ZCPRGT' or Typeset eq 'ZCPRMT' or Typeset eq 'ZCPROK' or Typeset eq 'ZCPRPR' or Typeset eq 'ZLPRPR' or Typeset eq 'ZLPRMT' or Typeset eq 'ZLPRET' or Typeset eq 'ZLPRGT' or Typeset eq 'ZCPRET' or Typeset eq 'CPGRP2'",
                                "ConfigTypesetTypes?$filter=Typeset eq 'SSSTTY' or Typeset eq 'ZDMGRT' or Typeset eq 'APNRMD' or Typeset eq 'ZCPOVN' or Typeset eq 'SCGOTY' or Typeset eq 'UOMNO0' or Typeset eq 'ANSWID' or Typeset eq 'CPOLSZ' or Typeset eq 'CPOLSP' or Typeset eq 'CPGRP7' or Typeset eq 'CPGRP8' or Typeset eq 'CPINFR' or Typeset eq 'CPOWNB' or Typeset eq 'CPINUM' or Typeset eq 'CPGRP9' or Typeset eq 'CPGRP6' or Typeset eq 'CPDFUM'",
                                "ValueHelps?$filter= ModelID eq 'SSGW_ALL' and (EntityType eq 'Visit' or EntityType eq 'ChannelPartner' or EntityType eq 'SSROItemDetail')"};
                    case MSP: //SDA
                        MSFAApplication.isSDA =true;
                        return new String[]{
                                "ChannelPartners",Attendances,
                                "SalesPersons",
                                "UserSalesPersons",
                                "RoutePlans",
                                "RouteSchedulePlans",
                                "RouteSchedules",
                                RouteScheduleSPs,
                                SyncHistroy,
                                UserPartners,
                                "SSSOs",
                                "SSSOItemDetails",
                                "CPDMSDivisions",
                                CPInfras,
                                CPPartnerFunctions,
                                SubDistricts,
                                Wards,
                                "CPSPRelations",
                                "UserChannelPartners",
                                Schemes, SchemeItemDetails, SchemeSlabs, SchemeGeographies, SchemeCPs, SchemeSalesAreas,
                                KPISet,
                                Targets,
                                TargetItems,
                                KPIItems,
                                CPPerformances,
                                Customers,
                                SSInvoiceTypes,
                                SSINVOICES,
                                SSInvoiceItemDetails,
//                          SSPurchaseInvoices,
                                "Visits",
                                "VisitActivities",
                                "CPStockItems?$filter=(StockOwner eq '02')",
                                "CPStockItemSnos?$filter=(StockOwner eq '02')",
                                "Brands",
                                "OrderMaterialGroups",
                                "UserProfileAuthSet?$filter=Application%20eq%20%27MSEC%27",
                                "UserProfileAuthSet?$filter=Application%20eq%20%27PD%27",
                                "ConfigTypsetTypeValues?$filter=Typeset eq 'ORDENT' or Typeset eq 'ZRPDVS' or Typeset eq 'ZCSDPM' or Typeset eq 'ZCSDMT' or Typeset eq 'ZCSDGT' or Typeset eq 'MSEC' or Typeset eq 'ATTTYP' or Typeset eq 'RVWTYP' or Typeset eq 'FIPRTY' or Typeset eq 'ACTTYP' or Typeset eq 'SF' or Typeset eq 'SC' or Typeset eq 'SS' or Typeset eq 'MC' or Typeset eq 'SSCP' or Typeset eq 'SSROUT' or Typeset eq 'SSSO' or Typeset eq 'ZCPRGT' or Typeset eq 'ZCPRMT' or Typeset eq 'ZCPROK' or Typeset eq 'ZCPRPR' or Typeset eq 'ZLPRPR' or Typeset eq 'ZLPRMT' or Typeset eq 'ZLPRET' or Typeset eq 'ZLPRGT' or Typeset eq 'ZCPRET' or Typeset eq 'CPGRP2' or Typeset eq 'ZCSDGT' or Typeset eq 'ZCSDPM' or Typeset eq 'ZCSDMT' or Typeset eq 'ZPSDAS' or Typeset eq 'ZPSDRP' or Typeset eq 'ZPSDCV' or Typeset eq 'ZPSDBL'",
                                "ConfigTypesetTypes?$filter=Typeset eq 'APNRMD' or Typeset eq 'SCGOTY' or Typeset eq 'UOMNO0' or Typeset eq 'CPOLSZ' or Typeset eq 'CPOLSP' or Typeset eq 'CPGRP7' or Typeset eq 'CPGRP8' or Typeset eq 'CPINFR' or Typeset eq 'CPOWNB' or Typeset eq 'CPINUM' or Typeset eq 'CPGRP9'",
                                "ValueHelps?$filter= ModelID eq 'SSGW_ALL' and (EntityType eq 'Visit' or EntityType eq 'ChannelPartner' or EntityType eq 'Target' or EntityType eq 'SSSOItemDetail' or EntityType eq  'SSSO')"};

                    case PSM: //PSM
                        MSFAApplication.isPSM =true;
                        return new String[]{
                                "CPConfigs",
                                "ChannelPartners",Attendances,
                                "SalesPersons",
                                "UserSalesPersons",
                                "RouteSchedulePlans",
                                "RouteScheduleSPs",
                                "RouteScheduleDetails",
                                "RouteProductDetTypes",
                                SyncHistroy,UserPartners,
                                "CPDMSDivisions",
                                CPPartnerFunctions,
                                "RouteSchedules",
                                "CPSPRelations",
                                SubDistricts,
                                Towns,
                                "UserProfileAuthSet?$filter=Application%20eq%20%27MSEC%27",
                                "ConfigTypsetTypeValues?$filter=Typeset eq 'MSEC' or Typeset eq 'STPNCD' or Typeset eq 'ORDENT' or Typeset eq 'ZKPICD' or Typeset eq 'DSTSTKVIEW' or Typeset eq 'ZPRGKP' or Typeset eq 'KPICDE' or Typeset eq 'ATTTYP' or Typeset eq 'RVWTYP' or Typeset eq 'FIPRTY' or Typeset eq 'ACTTYP' or Typeset eq 'SF' or Typeset eq 'SC' or Typeset eq 'SS' or Typeset eq 'MC' or Typeset eq 'SSCP' or Typeset eq 'SSROUT' or Typeset eq 'SSSO' or Typeset eq 'ZCPRGT' or Typeset eq 'ZCPRMT' or Typeset eq 'ZCPROK' or Typeset eq 'ZCPRPR' or Typeset eq 'ZLPRPR' or Typeset eq 'ZLPRMT' or Typeset eq 'ZLPRET' or Typeset eq 'ZLPRGT' or Typeset eq 'ZCPRET' or Typeset eq 'CPGRP2'",
                                "ConfigTypesetTypes?$filter=Typeset eq 'APNRMD' or Typeset eq 'SCGOTY' or Typeset eq 'UOMNO0' or Typeset eq 'ANSWID' or Typeset eq 'CPOLSZ' or Typeset eq 'CPOLSP' or Typeset eq 'CPGRP7' or Typeset eq 'CPGRP8' or Typeset eq 'CPINFR' or Typeset eq 'CPOWNB' or Typeset eq 'CPINUM' or Typeset eq 'CPGRP9' or Typeset eq 'CPGRP6' or Typeset eq 'CPDFUM'",
                                "ValueHelps?$filter= ModelID eq 'SSGW_ALL' and (EntityType eq 'ChannelPartner')"};
                    case VAN: //VAN
                        MSFAApplication.isVAN = true;
                        return new String[]{
                                Customers,
                                "CPConfigs",Attendances,
                                "ChannelPartners",SSROs,SSROItemDetails,
                                "SalesPersons",
                                "UserSalesPersons",
                                "RouteSchedulePlans",
                                "RouteScheduleSPs",
                                "RouteScheduleDetails",
                                "RouteProductDetTypes",
                                SyncHistroy, UserPartners,
                                "CPDMSDivisions",
                                CPPartnerFunctions,
                                "SPStockItems?$filter=(StockOwner eq '05')",
                                "SPStockItemSNos",
                                "RouteSchedules",
                                "CPSPRelations",
                                "UserChannelPartners",
                                SubDistricts,
                                Towns,
                                Schemes, SchemeItemDetails, SchemeSlabs, SchemeGeographies, SchemeCPs, SchemeSalesAreas,SchemeFreeMatGrpMaterials,SchemeFreeMatGrps,
                                SSInvoiceTypes,
                                SSINVOICES,
                                SSInvoiceItemDetails,
                                "SPNumberRangeSet",
                                "Visits",
                                "VisitActivities",
                                OrderMaterialGroups,Brands,
                                "UserProfileAuthSet?$filter=Application%20eq%20%27MSEC%27",
                                "ConfigTypsetTypeValues?$filter=Typeset eq 'MSEC' or Typeset eq 'ZVNSCR' or Typeset eq 'STPNCD' or Typeset eq 'ORDENT' or Typeset eq 'ZKPICD' or Typeset eq 'DSTSTKVIEW' or Typeset eq 'ZPRGKP' or Typeset eq 'KPICDE' or Typeset eq 'ATTTYP' or Typeset eq 'RVWTYP' or Typeset eq 'FIPRTY' or Typeset eq 'ACTTYP' or Typeset eq 'SF' or Typeset eq 'SC' or Typeset eq 'SS' or Typeset eq 'MC' or Typeset eq 'SSCP' or Typeset eq 'SSROUT' or Typeset eq 'SSSO' or Typeset eq 'ZCPRGT' or Typeset eq 'ZCPRMT' or Typeset eq 'ZCPROK' or Typeset eq 'ZCPRPR' or Typeset eq 'ZLPRPR' or Typeset eq 'ZLPRMT' or Typeset eq 'ZLPRET' or Typeset eq 'ZLPRGT' or Typeset eq 'ZCPRET' or Typeset eq 'CPGRP2'",
                                "ConfigTypesetTypes?$filter=Typeset eq 'APNRMD' or Typeset eq 'SCGOTY' or Typeset eq 'UOMNO0' or Typeset eq 'ANSWID' or Typeset eq 'CPOLSZ' or Typeset eq 'CPOLSP' or Typeset eq 'CPGRP7' or Typeset eq 'CPGRP8' or Typeset eq 'CPINFR' or Typeset eq 'CPOWNB' or Typeset eq 'CPINUM' or Typeset eq 'CPGRP9' or Typeset eq 'CPGRP6' or Typeset eq 'CPDFUM'",
                                "ValueHelps?$filter= ModelID eq 'SSGW_ALL' and (EntityType eq 'ChannelPartner' or EntityType eq 'SSROItemDetail' or EntityType eq 'SSInvoice')"};

                }
            }
        }else if(sharedPreferences.getInt(CURRENT_VERSION_CODE, 0) <= 4){
            if (rollID != null && !TextUtils.isEmpty(rollID)) {
                switch (rollID){
                    case DSR: // AWSM
                        MSFAApplication.isAWSM =true;
                        return new String[]{
                                Customers,
                                "CPConfigs",SPPayouts,
                                "ChannelPartners",Attendances,
                                "SalesPersons",
                                SSROs+"?$filter=(OrderType eq '000021' or OrderType eq '000020') and StatusID eq '000005'",
                                SSROItemDetails,
                                "UserSalesPersons",
                                "RoutePlans",
                                "CPSummary1Set",
                                "RouteSchedulePlans",
                                "RouteScheduleSPs",
                                "RouteScheduleDetails",
                                "RouteProductDetTypes",
                                "CPProductDetTypes",
                                SyncHistroy,UserPartners,
                                "CPDMSDivisions",
//                            CPInfras,
                                CPPartnerFunctions,
                                CPPerformances,
                                "RouteSchedules",
                                "SSSOs",
                                "SSSOItemDetails",
                                "CPSPRelations",
                                "Visits",
                                "VisitActivities",
//                            "UserCustomers",
                                "Questions",
                                "VisitQuestionnaires",
                                SubDistricts,
//                            Wards,
                                Towns,
                                Schemes, SchemeItemDetails, SchemeSlabs, SchemeGeographies, SchemeCPs, SchemeSalesAreas,
                                //MaterialDocs,
                                //MaterialDocItems,
                                KPISet,
                                Targets,
                                TargetItems,
                                KPIItems,
//                            Customers,SSInvoiceTypes,SSINVOICES,
                                SSInvoiceItemDetails,
                                SSINVOICES,
                                "CPStockItems?$filter=(StockOwner eq '01')",
                                "CPStockItemSnos?$filter=(StockOwner eq '01')",
                                "SegmentedMaterials",
                                "Brands",
//                              "MaterialCategories",
                                "OrderMaterialGroups",
                                "BrandsCategories",
                                "UserProfileAuthSet?$filter=Application%20eq%20%27MSEC%27",
//                              "UserProfileAuthSet?$filter=Application%20eq%20%27PD%27",
                                "ConfigTypsetTypeValues?$filter=Typeset eq 'OBKDYS' or Typeset eq 'MSEC' or Typeset eq 'STPNCD' or Typeset eq 'ORDENT' or Typeset eq 'ZKPICD' or Typeset eq 'DSTSTKVIEW' or Typeset eq 'ZPRGKP' or Typeset eq 'KPICDE' or Typeset eq 'ATTTYP' or Typeset eq 'RVWTYP' or Typeset eq 'FIPRTY' or Typeset eq 'ACTTYP' or Typeset eq 'SF' or Typeset eq 'SC' or Typeset eq 'SS' or Typeset eq 'MC' or Typeset eq 'SSCP' or Typeset eq 'SSROUT' or Typeset eq 'SSSO' or Typeset eq 'ZCPRGT' or Typeset eq 'ZCPRMT' or Typeset eq 'ZCPROK' or Typeset eq 'ZCPRPR' or Typeset eq 'ZLPRPR' or Typeset eq 'ZLPRMT' or Typeset eq 'ZLPRET' or Typeset eq 'ZLPRGT' or Typeset eq 'ZCPRET' or Typeset eq 'CPGRP2' or Typeset eq 'CPGP10'",
                                "ConfigTypesetTypes?$filter=Typeset eq 'SSSTTY' or Typeset eq 'ZDMGRT' or Typeset eq 'APNRMD' or Typeset eq 'ZCPOVN' or Typeset eq 'SCGOTY' or Typeset eq 'UOMNO0' or Typeset eq 'ANSWID' or Typeset eq 'CPOLSZ' or Typeset eq 'CPOLSP' or Typeset eq 'CPGRP7' or Typeset eq 'CPGRP8' or Typeset eq 'CPINFR' or Typeset eq 'CPOWNB' or Typeset eq 'CPINUM' or Typeset eq 'CPGRP9' or Typeset eq 'CPGRP6' or Typeset eq 'CPDFUM' or Typeset eq 'ZADACT' or Typeset eq 'ZADTOT'",
                                "ValueHelps?$filter= ModelID eq 'SSGW_ALL' and (EntityType eq 'Visit' or EntityType eq 'ChannelPartner' or EntityType eq 'SSROItemDetail')"};
                    case BCRVAN: // BCRVAN
                        MSFAApplication.isBCRVAN =true;
                        return new String[]{
                                "SPStockItems?$filter=(StockOwner eq '05')",
                                "SPStockItemSNos",
                                "SPNumberRangeSet",
                                Customers,
                                "CPConfigs",SPPayouts,
                                "ChannelPartners",Attendances,
                                "SalesPersons",
                                SSROs+"?$filter=(OrderType eq '000021' or OrderType eq '000020') and StatusID eq '000005'",
                                SSROItemDetails,
                                "UserSalesPersons",
                                "RoutePlans",
                                "CPSummary1Set",
                                "RouteSchedulePlans",
                                "RouteScheduleSPs",
                                "RouteScheduleDetails",
                                "RouteProductDetTypes",
                                "CPProductDetTypes",
                                SyncHistroy,UserPartners,
                                "CPDMSDivisions",
//                            CPInfras,
                                CPPartnerFunctions,
                                CPPerformances,
                                "RouteSchedules",
                                "SSSOs",
                                "SSSOItemDetails",
                                "CPSPRelations",
                                "Visits",
                                "VisitActivities",
//                            "UserCustomers",
                                "Questions",
                                "VisitQuestionnaires",
                                SubDistricts,
//                            Wards,
                                Towns,
                                Schemes, SchemeItemDetails, SchemeSlabs, SchemeGeographies, SchemeCPs, SchemeSalesAreas,
                                //MaterialDocs,
                                //MaterialDocItems,
                                KPISet,
                                Targets,
                                TargetItems,
                                KPIItems,
//                            Customers,SSInvoiceTypes,SSINVOICES,
                                SSInvoiceItemDetails,
                                SSINVOICES,
                                "CPStockItems?$filter=(StockOwner eq '01')",
                                "CPStockItemSnos?$filter=(StockOwner eq '01')",
                                "SegmentedMaterials",
                                "Brands",
//                              "MaterialCategories",
                                "OrderMaterialGroups",
                                "BrandsCategories",
                                FinancialPostings,
                                FinancialPostingItemDetails,
                                "UserProfileAuthSet?$filter=Application%20eq%20%27MSEC%27",
//                              "UserProfileAuthSet?$filter=Application%20eq%20%27PD%27",
                                "ConfigTypsetTypeValues?$filter=Typeset eq 'OBKDYS' or Typeset eq 'MSEC' or Typeset eq 'STPNCD' or Typeset eq 'ORDENT' or Typeset eq 'ZKPICD' or Typeset eq 'DSTSTKVIEW' or Typeset eq 'ZPRGKP' or Typeset eq 'KPICDE' or Typeset eq 'ATTTYP' or Typeset eq 'RVWTYP' or Typeset eq 'FIPRTY' or Typeset eq 'ACTTYP' or Typeset eq 'SF' or Typeset eq 'SC' or Typeset eq 'SS' or Typeset eq 'MC' or Typeset eq 'SSCP' or Typeset eq 'SSROUT' or Typeset eq 'SSSO' or Typeset eq 'ZCPRGT' or Typeset eq 'ZCPRMT' or Typeset eq 'ZCPROK' or Typeset eq 'ZCPRPR' or Typeset eq 'ZLPRPR' or Typeset eq 'ZLPRMT' or Typeset eq 'ZLPRET' or Typeset eq 'ZLPRGT' or Typeset eq 'ZCPRET' or Typeset eq 'CPGRP2' or Typeset eq 'CPGP10'",
                                "ConfigTypesetTypes?$filter=Typeset eq 'SSSTTY' or Typeset eq 'ZDMGRT' or Typeset eq 'APNRMD' or Typeset eq 'ZCPOVN' or Typeset eq 'SCGOTY' or Typeset eq 'UOMNO0' or Typeset eq 'ANSWID' or Typeset eq 'CPOLSZ' or Typeset eq 'CPOLSP' or Typeset eq 'CPGRP7' or Typeset eq 'CPGRP8' or Typeset eq 'CPINFR' or Typeset eq 'CPOWNB' or Typeset eq 'CPINUM' or Typeset eq 'CPGRP9' or Typeset eq 'CPGRP6' or Typeset eq 'CPDFUM' or Typeset eq 'ZADACT' or Typeset eq 'ZADTOT'",
                                "ValueHelps?$filter= ModelID eq 'SSGW_ALL' and (EntityType eq 'Visit' or EntityType eq 'FinancialPostingItemDetail' or EntityType eq 'FinancialPosting' or EntityType eq 'ChannelPartner' or EntityType eq 'SSROItemDetail')"};
                    case CALL_CNTR: // CALL CENTER
                        MSFAApplication.isCallCenter =true;
                        return new String[]{
                                Customers,
                                "CPConfigs",SPPayouts,
                                "ChannelPartners",Attendances,
                                "SalesPersons",
                                SSROs+"?$filter=(OrderType eq '000021' or OrderType eq '000020') and StatusID eq '000005'",
                                SSROItemDetails,
                                "UserSalesPersons",
                                "RoutePlans",
                                "RouteSchedulePlans",
                                "RouteScheduleSPs",
                                "RouteScheduleDetails",
                                "RouteProductDetTypes",
                                "CPProductDetTypes",
                                SyncHistroy,UserPartners,
                                "CPDMSDivisions",
//                            CPInfras,
                                CPPartnerFunctions,
                                CPPerformances,
                                "RouteSchedules",
                                "SSSOs",
                                "SSSOItemDetails",
                                "CPSPRelations",
                                "Visits",
                                "VisitActivities",
//                            "UserCustomers",
                                "Questions",
                                "VisitQuestionnaires",
                                SubDistricts,
//                            Wards,
                                Towns,
                                Schemes, SchemeItemDetails, SchemeSlabs, SchemeGeographies, SchemeCPs, SchemeSalesAreas,
                                //MaterialDocs,
                                //MaterialDocItems,
//                                KPISet,
//                                Targets,
//                                TargetItems,
//                                KPIItems,
//                            Customers,SSInvoiceTypes,SSINVOICES,SSInvoiceItemDetails,
                                SSINVOICES,
                                "CPStockItems?$filter=(StockOwner eq '01')",
                                "CPStockItemSnos?$filter=(StockOwner eq '01')",
                                "SegmentedMaterials",
                                "Brands",
//                              "MaterialCategories",
                                "OrderMaterialGroups",
                                "BrandsCategories",
                                "UserProfileAuthSet?$filter=Application%20eq%20%27MSEC%27",
//                              "UserProfileAuthSet?$filter=Application%20eq%20%27PD%27",
                                "ConfigTypsetTypeValues?$filter=Typeset eq 'OBKDYS' or Typeset eq 'MSEC' or Typeset eq 'STPNCD' or Typeset eq 'ORDENT' or Typeset eq 'ZKPICD' or Typeset eq 'DSTSTKVIEW' or Typeset eq 'ZPRGKP' or Typeset eq 'KPICDE' or Typeset eq 'ATTTYP' or Typeset eq 'RVWTYP' or Typeset eq 'FIPRTY' or Typeset eq 'ACTTYP' or Typeset eq 'SF' or Typeset eq 'SC' or Typeset eq 'SS' or Typeset eq 'MC' or Typeset eq 'SSCP' or Typeset eq 'SSROUT' or Typeset eq 'SSSO' or Typeset eq 'ZCPRGT' or Typeset eq 'ZCPRMT' or Typeset eq 'ZCPROK' or Typeset eq 'ZCPRPR' or Typeset eq 'ZLPRPR' or Typeset eq 'ZLPRMT' or Typeset eq 'ZLPRET' or Typeset eq 'ZLPRGT' or Typeset eq 'ZCPRET' or Typeset eq 'CPGRP2' or Typeset eq 'CPGP10'",
                                "ConfigTypesetTypes?$filter=Typeset eq 'SSSTTY' or Typeset eq 'ZDMGRT' or Typeset eq 'APNRMD' or Typeset eq 'ZCPOVN' or Typeset eq 'SCGOTY' or Typeset eq 'UOMNO0' or Typeset eq 'ANSWID' or Typeset eq 'CPOLSZ' or Typeset eq 'CPOLSP' or Typeset eq 'CPGRP7' or Typeset eq 'CPGRP8' or Typeset eq 'CPINFR' or Typeset eq 'CPOWNB' or Typeset eq 'CPINUM' or Typeset eq 'CPGRP9' or Typeset eq 'CPGRP6' or Typeset eq 'CPDFUM' or Typeset eq 'ZADACT' or Typeset eq 'ZADTOT'",
                                "ValueHelps?$filter= ModelID eq 'SSGW_ALL' and (EntityType eq 'Visit' or EntityType eq 'ChannelPartner' or EntityType eq 'SSROItemDetail')"};
                    case MSP: //SDA
                        MSFAApplication.isSDA =true;
                        return new String[]{
                                "ChannelPartners",Attendances,
                                "SalesPersons",
                                "UserSalesPersons",
                                "RoutePlans",
                                "RouteSchedulePlans",
                                "RouteSchedules",
                                RouteScheduleSPs,
                                SyncHistroy,
                                UserPartners,
                                "SSSOs",
                                "SSSOItemDetails",
                                "CPDMSDivisions",
                                CPInfras,
                                CPPartnerFunctions,
                                SubDistricts,
                                Wards,
                                "CPSPRelations",
                                "UserChannelPartners",
                                Schemes, SchemeItemDetails, SchemeSlabs, SchemeGeographies, SchemeCPs, SchemeSalesAreas,
                                KPISet,
                                Targets,
                                TargetItems,
                                KPIItems,
                                CPPerformances,
                                Customers,
                                SSInvoiceTypes,
                                SSINVOICES,
                                SSInvoiceItemDetails,
//                          SSPurchaseInvoices,
                                "Visits",
                                "VisitActivities",
                                "CPStockItems?$filter=(StockOwner eq '02')",
                                "CPStockItemSnos?$filter=(StockOwner eq '02')",
                                "Brands",
                                "OrderMaterialGroups",
                                "UserProfileAuthSet?$filter=Application%20eq%20%27MSEC%27",
                                "UserProfileAuthSet?$filter=Application%20eq%20%27PD%27",
                                "ConfigTypsetTypeValues?$filter=Typeset eq 'ORDENT' or Typeset eq 'ZRPDVS' or Typeset eq 'ZCSDPM' or Typeset eq 'ZCSDMT' or Typeset eq 'ZCSDGT' or Typeset eq 'MSEC' or Typeset eq 'ATTTYP' or Typeset eq 'RVWTYP' or Typeset eq 'FIPRTY' or Typeset eq 'ACTTYP' or Typeset eq 'SF' or Typeset eq 'SC' or Typeset eq 'SS' or Typeset eq 'MC' or Typeset eq 'SSCP' or Typeset eq 'SSROUT' or Typeset eq 'SSSO' or Typeset eq 'ZCPRGT' or Typeset eq 'ZCPRMT' or Typeset eq 'ZCPROK' or Typeset eq 'ZCPRPR' or Typeset eq 'ZLPRPR' or Typeset eq 'ZLPRMT' or Typeset eq 'ZLPRET' or Typeset eq 'ZLPRGT' or Typeset eq 'ZCPRET' or Typeset eq 'CPGRP2' or Typeset eq 'ZCSDGT' or Typeset eq 'ZCSDPM' or Typeset eq 'ZCSDMT' or Typeset eq 'ZPSDAS' or Typeset eq 'ZPSDRP' or Typeset eq 'ZPSDCV' or Typeset eq 'ZPSDBL' or Typeset eq 'CPGP10'",
                                "ConfigTypesetTypes?$filter=Typeset eq 'APNRMD' or Typeset eq 'SCGOTY' or Typeset eq 'UOMNO0' or Typeset eq 'CPOLSZ' or Typeset eq 'CPOLSP' or Typeset eq 'CPGRP7' or Typeset eq 'CPGRP8' or Typeset eq 'CPINFR' or Typeset eq 'CPOWNB' or Typeset eq 'CPINUM' or Typeset eq 'CPGRP9' or Typeset eq 'ZADACT' or Typeset eq 'ZADTOT'",
                                "ValueHelps?$filter= ModelID eq 'SSGW_ALL' and (EntityType eq 'Visit' or EntityType eq 'ChannelPartner' or EntityType eq 'Target' or EntityType eq 'SSSOItemDetail' or EntityType eq  'SSSO')"};

                    case PSM: //PSM
                        MSFAApplication.isPSM =true;
                        return new String[]{
                                "CPConfigs",
                                "ChannelPartners",Attendances,
                                "SalesPersons",
                                "UserSalesPersons",
                                "RouteSchedulePlans",
                                "RouteScheduleSPs",
                                "RouteScheduleDetails",
                                "RouteProductDetTypes",
                                "CPProductDetTypes",
                                SyncHistroy,UserPartners,
                                "CPDMSDivisions",
                                CPPartnerFunctions,
                                "RouteSchedules",
                                "CPSPRelations",
                                SubDistricts,
                                Towns,
                                "UserProfileAuthSet?$filter=Application%20eq%20%27MSEC%27",
                                "ConfigTypsetTypeValues?$filter=Typeset eq 'MSEC' or Typeset eq 'STPNCD' or Typeset eq 'ORDENT' or Typeset eq 'ZKPICD' or Typeset eq 'DSTSTKVIEW' or Typeset eq 'ZPRGKP' or Typeset eq 'KPICDE' or Typeset eq 'ATTTYP' or Typeset eq 'RVWTYP' or Typeset eq 'FIPRTY' or Typeset eq 'ACTTYP' or Typeset eq 'SF' or Typeset eq 'SC' or Typeset eq 'SS' or Typeset eq 'MC' or Typeset eq 'SSCP' or Typeset eq 'SSROUT' or Typeset eq 'SSSO' or Typeset eq 'ZCPRGT' or Typeset eq 'ZCPRMT' or Typeset eq 'ZCPROK' or Typeset eq 'ZCPRPR' or Typeset eq 'ZLPRPR' or Typeset eq 'ZLPRMT' or Typeset eq 'ZLPRET' or Typeset eq 'ZLPRGT' or Typeset eq 'ZCPRET' or Typeset eq 'CPGRP2' or Typeset eq 'CPGP10'",
                                "ConfigTypesetTypes?$filter=Typeset eq 'APNRMD' or Typeset eq 'SCGOTY' or Typeset eq 'UOMNO0' or Typeset eq 'ANSWID' or Typeset eq 'CPOLSZ' or Typeset eq 'CPOLSP' or Typeset eq 'CPGRP7' or Typeset eq 'CPGRP8' or Typeset eq 'CPINFR' or Typeset eq 'CPOWNB' or Typeset eq 'CPINUM' or Typeset eq 'CPGRP9' or Typeset eq 'CPGRP6' or Typeset eq 'CPDFUM' or Typeset eq 'ZADACT' or Typeset eq 'ZADTOT'",
                                "ValueHelps?$filter= ModelID eq 'SSGW_ALL' and (EntityType eq 'ChannelPartner')"};
                    case VAN: //VAN
                        MSFAApplication.isVAN = true;
                        return new String[]{
                                Customers,
                                "CPConfigs",Attendances,
                                "ChannelPartners",SSROs,SSROItemDetails,
                                "SalesPersons",
                                "UserSalesPersons",
                                "RouteSchedulePlans",
                                "RouteScheduleSPs",
                                "RouteScheduleDetails",
                                "RouteProductDetTypes",
                                "CPProductDetTypes",
                                SyncHistroy, UserPartners,
                                "CPDMSDivisions",
                                CPPartnerFunctions,
                                CPPerformances,
                                "SPStockItems?$filter=(StockOwner eq '05')",
                                "SPStockItemSNos",
                                "RouteSchedules",
                                "CPSPRelations",
                                "UserChannelPartners",
                                SubDistricts,
                                Towns,
                                Schemes, SchemeItemDetails, SchemeSlabs, SchemeGeographies, SchemeCPs, SchemeSalesAreas,SchemeFreeMatGrpMaterials,SchemeFreeMatGrps,
                                SSInvoiceTypes,
                                SSINVOICES,
                                SSInvoiceItemDetails,
                                "SPNumberRangeSet",
                                "Visits",
                                "VisitActivities",
                                OrderMaterialGroups,Brands,
                                "UserProfileAuthSet?$filter=Application%20eq%20%27MSEC%27",
                                "ConfigTypsetTypeValues?$filter=Typeset eq 'MSEC' or Typeset eq 'ZVNSCR' or Typeset eq 'STPNCD' or Typeset eq 'ORDENT' or Typeset eq 'ZKPICD' or Typeset eq 'DSTSTKVIEW' or Typeset eq 'ZPRGKP' or Typeset eq 'KPICDE' or Typeset eq 'ATTTYP' or Typeset eq 'RVWTYP' or Typeset eq 'FIPRTY' or Typeset eq 'ACTTYP' or Typeset eq 'SF' or Typeset eq 'SC' or Typeset eq 'SS' or Typeset eq 'MC' or Typeset eq 'SSCP' or Typeset eq 'SSROUT' or Typeset eq 'SSSO' or Typeset eq 'ZCPRGT' or Typeset eq 'ZCPRMT' or Typeset eq 'ZCPROK' or Typeset eq 'ZCPRPR' or Typeset eq 'ZLPRPR' or Typeset eq 'ZLPRMT' or Typeset eq 'ZLPRET' or Typeset eq 'ZLPRGT' or Typeset eq 'ZCPRET' or Typeset eq 'CPGRP2' or Typeset eq 'CPGP10'",
                                "ConfigTypesetTypes?$filter=Typeset eq 'APNRMD' or Typeset eq 'SCGOTY' or Typeset eq 'UOMNO0' or Typeset eq 'ANSWID' or Typeset eq 'CPOLSZ' or Typeset eq 'CPOLSP' or Typeset eq 'CPGRP7' or Typeset eq 'CPGRP8' or Typeset eq 'CPINFR' or Typeset eq 'CPOWNB' or Typeset eq 'CPINUM' or Typeset eq 'CPGRP9' or Typeset eq 'CPGRP6' or Typeset eq 'CPDFUM' or Typeset eq 'ZADACT' or Typeset eq 'ZADTOT'",
                                "ValueHelps?$filter= ModelID eq 'SSGW_ALL' and (EntityType eq 'ChannelPartner' or EntityType eq 'SSROItemDetail' or EntityType eq 'SSInvoice')"};

                }
            }
        }else{
            if (rollID != null && !TextUtils.isEmpty(rollID)) {
                switch (rollID){
                    case DSR: // AWSM
                        MSFAApplication.isAWSM =true;
                        return new String[]{
                                Customers,
                                "CPConfigs",SPPayouts,
                                "ChannelPartners",Attendances,
                                "SalesPersons",
                                SSROs+"?$filter=(OrderType eq '000021' or OrderType eq '000020') and StatusID eq '000005'",
                                SSROItemDetails,
                                "UserSalesPersons",
                                "RoutePlans",
                                "CPSummary1Set",
                                "RouteSchedulePlans",
                                "RouteScheduleSPs",
                                "RouteScheduleDetails",
                                "RouteProductDetTypes",
                                "CPProductDetTypes",
                                SyncHistroy,UserPartners,
                                "CPDMSDivisions",
//                            CPInfras,
                                CPPartnerFunctions,
                                CPPerformances,
                                "RouteSchedules",
                                "SSSOs",
                                "SSSOItemDetails",
                                "CPSPRelations",
                                "Visits",
                                "VisitActivities",
//                            "UserCustomers",
                                "Questions",
                                "VisitQuestionnaires",
                                SubDistricts,
//                            Wards,
                                Towns,
                                Schemes, SchemeItemDetails, SchemeSlabs, SchemeGeographies, SchemeCPs, SchemeSalesAreas,
                                //MaterialDocs,
                                //MaterialDocItems,
                                KPISet,
                                Targets,
                                TargetItems,
                                KPIItems,
//                            Customers,SSInvoiceTypes,SSINVOICES,
                            SSInvoiceItemDetails,
                                SSINVOICES,
                                "CPStockItems?$filter=(StockOwner eq '01')",
                                "CPStockItemSnos?$filter=(StockOwner eq '01')",
                                "SegmentedMaterials",
                                "Brands",
//                              "MaterialCategories",
                                "OrderMaterialGroups",
                                "BrandsCategories",
                                "UserProfileAuthSet?$filter=Application%20eq%20%27MSEC%27",
//                              "UserProfileAuthSet?$filter=Application%20eq%20%27PD%27",
                                "ConfigTypsetTypeValues?$filter=Typeset eq 'OBKDYS' or Typeset eq 'MSEC' or Typeset eq 'STPNCD' or Typeset eq 'ORDENT' or Typeset eq 'ZKPICD' or Typeset eq 'DSTSTKVIEW' or Typeset eq 'ZPRGKP' or Typeset eq 'KPICDE' or Typeset eq 'ATTTYP' or Typeset eq 'RVWTYP' or Typeset eq 'FIPRTY' or Typeset eq 'ACTTYP' or Typeset eq 'SF' or Typeset eq 'SC' or Typeset eq 'SS' or Typeset eq 'MC' or Typeset eq 'SSCP' or Typeset eq 'SSROUT' or Typeset eq 'SSSO' or Typeset eq 'ZCPRGT' or Typeset eq 'ZCPRMT' or Typeset eq 'ZCPROK' or Typeset eq 'ZCPRPR' or Typeset eq 'ZLPRPR' or Typeset eq 'ZLPRMT' or Typeset eq 'ZLPRET' or Typeset eq 'ZLPRGT' or Typeset eq 'ZCPRET' or Typeset eq 'CPGRP2' or Typeset eq 'CPGP10' or Typeset eq 'ZCRDA'",
                                "ConfigTypesetTypes?$filter=Typeset eq 'SSSTTY' or Typeset eq 'ZDMGRT' or Typeset eq 'APNRMD' or Typeset eq 'ZCPOVN' or Typeset eq 'SCGOTY' or Typeset eq 'UOMNO0' or Typeset eq 'ANSWID' or Typeset eq 'CPOLSZ' or Typeset eq 'CPOLSP' or Typeset eq 'CPGRP7' or Typeset eq 'CPGRP8' or Typeset eq 'CPINFR' or Typeset eq 'CPOWNB' or Typeset eq 'CPINUM' or Typeset eq 'CPGRP9' or Typeset eq 'CPGRP6' or Typeset eq 'CPDFUM' or Typeset eq 'ZADACT' or Typeset eq 'ZADTOT' or Typeset eq 'ZODRSN'",
                                "ValueHelps?$filter= ModelID eq 'SSGW_ALL' and (EntityType eq 'Visit' or EntityType eq 'ChannelPartner' or EntityType eq 'SSROItemDetail')"};
                    case BCRVAN: // BCRVAN
                        MSFAApplication.isBCRVAN =true;
                        return new String[]{
                                "SPStockItems?$filter=(StockOwner eq '05')",
                                "SPStockItemSNos",
                                "SPNumberRangeSet",
                                Customers,
                                "CPConfigs",SPPayouts,
                                "ChannelPartners",Attendances,
                                "SalesPersons",
                                SSROs+"?$filter=(OrderType eq '000021' or OrderType eq '000020') and StatusID eq '000005'",
                                SSROItemDetails,
                                "UserSalesPersons",
                                "RoutePlans",
                                "CPSummary1Set",
                                "RouteSchedulePlans",
                                "RouteScheduleSPs",
                                "RouteScheduleDetails",
                                "RouteProductDetTypes",
                                "CPProductDetTypes",
                                SyncHistroy,UserPartners,
                                "CPDMSDivisions",
//                            CPInfras,
                                CPPartnerFunctions,
                                CPPerformances,
                                "RouteSchedules",
                                "SSSOs",
                                "SSSOItemDetails",
                                "CPSPRelations",
                                "Visits",
                                "VisitActivities",
//                            "UserCustomers",
                                "Questions",
                                "VisitQuestionnaires",
                                SubDistricts,
//                            Wards,
                                Towns,
                                Schemes, SchemeItemDetails, SchemeSlabs, SchemeGeographies, SchemeCPs, SchemeSalesAreas,
                                //MaterialDocs,
                                //MaterialDocItems,
                                KPISet,
                                Targets,
                                TargetItems,
                                KPIItems,
//                            Customers,SSInvoiceTypes,SSINVOICES,
                                SSInvoiceItemDetails,
                                SSINVOICES,
                                "CPStockItems?$filter=(StockOwner eq '01')",
                                "CPStockItemSnos?$filter=(StockOwner eq '01')",
                                "SegmentedMaterials",
                                "Brands",
//                              "MaterialCategories",
                                "OrderMaterialGroups",
                                "BrandsCategories",
                                FinancialPostings,
                                FinancialPostingItemDetails,
                                "UserProfileAuthSet?$filter=Application%20eq%20%27MSEC%27",
//                              "UserProfileAuthSet?$filter=Application%20eq%20%27PD%27",
                                "ConfigTypsetTypeValues?$filter=Typeset eq 'OBKDYS' or Typeset eq 'MSEC' or Typeset eq 'STPNCD' or Typeset eq 'ORDENT' or Typeset eq 'ZKPICD' or Typeset eq 'DSTSTKVIEW' or Typeset eq 'ZPRGKP' or Typeset eq 'KPICDE' or Typeset eq 'ATTTYP' or Typeset eq 'RVWTYP' or Typeset eq 'FIPRTY' or Typeset eq 'ACTTYP' or Typeset eq 'SF' or Typeset eq 'SC' or Typeset eq 'SS' or Typeset eq 'MC' or Typeset eq 'SSCP' or Typeset eq 'SSROUT' or Typeset eq 'SSSO' or Typeset eq 'ZCPRGT' or Typeset eq 'ZCPRMT' or Typeset eq 'ZCPROK' or Typeset eq 'ZCPRPR' or Typeset eq 'ZLPRPR' or Typeset eq 'ZLPRMT' or Typeset eq 'ZLPRET' or Typeset eq 'ZLPRGT' or Typeset eq 'ZCPRET' or Typeset eq 'CPGRP2' or Typeset eq 'CPGP10' or Typeset eq 'ZCRDA'",
                                "ConfigTypesetTypes?$filter=Typeset eq 'SSSTTY' or Typeset eq 'ZDMGRT' or Typeset eq 'APNRMD' or Typeset eq 'ZCPOVN' or Typeset eq 'SCGOTY' or Typeset eq 'UOMNO0' or Typeset eq 'ANSWID' or Typeset eq 'CPOLSZ' or Typeset eq 'CPOLSP' or Typeset eq 'CPGRP7' or Typeset eq 'CPGRP8' or Typeset eq 'CPINFR' or Typeset eq 'CPOWNB' or Typeset eq 'CPINUM' or Typeset eq 'CPGRP9' or Typeset eq 'CPGRP6' or Typeset eq 'CPDFUM' or Typeset eq 'ZADACT' or Typeset eq 'ZADTOT'",
                                "ValueHelps?$filter= ModelID eq 'SSGW_ALL' and (EntityType eq 'Visit' or EntityType eq 'FinancialPostingItemDetail' or EntityType eq 'FinancialPosting' or EntityType eq 'ChannelPartner' or EntityType eq 'SSROItemDetail')"};
                    case CALL_CNTR: // CALL CENTER
                        MSFAApplication.isCallCenter =true;
                        return new String[]{
                                Customers,
                                "CPConfigs",SPPayouts,
                                "ChannelPartners",Attendances,
                                "SalesPersons",
                                SSROs+"?$filter=(OrderType eq '000021' or OrderType eq '000020') and StatusID eq '000005'",
                                SSROItemDetails,
                                "UserSalesPersons",
                                "RoutePlans",
                                "RouteSchedulePlans",
                                "RouteScheduleSPs",
                                "RouteScheduleDetails",
                                "RouteProductDetTypes",
                                "CPProductDetTypes",
                                SyncHistroy,UserPartners,
                                "CPDMSDivisions",
//                            CPInfras,
                                CPPartnerFunctions,
                                CPPerformances,
                                "RouteSchedules",
                                "SSSOs",
                                "SSSOItemDetails",
                                "CPSPRelations",
                                "Visits",
                                "VisitActivities",
//                            "UserCustomers",
                                "Questions",
                                "VisitQuestionnaires",
                                SubDistricts,
//                            Wards,
                                Towns,
                                Schemes, SchemeItemDetails, SchemeSlabs, SchemeGeographies, SchemeCPs, SchemeSalesAreas,
                                //MaterialDocs,
                                //MaterialDocItems,
//                                KPISet,
//                                Targets,
//                                TargetItems,
//                                KPIItems,
//                            Customers,SSInvoiceTypes,SSINVOICES,SSInvoiceItemDetails,
                                SSINVOICES,
                                "CPStockItems?$filter=(StockOwner eq '01')",
                                "CPStockItemSnos?$filter=(StockOwner eq '01')",
                                "SegmentedMaterials",
                                "Brands",
//                              "MaterialCategories",
                                "OrderMaterialGroups",
                                "BrandsCategories",
                                "UserProfileAuthSet?$filter=Application%20eq%20%27MSEC%27",
//                              "UserProfileAuthSet?$filter=Application%20eq%20%27PD%27",
                                "ConfigTypsetTypeValues?$filter=Typeset eq 'OBKDYS' or Typeset eq 'MSEC' or Typeset eq 'STPNCD' or Typeset eq 'ORDENT' or Typeset eq 'ZKPICD' or Typeset eq 'DSTSTKVIEW' or Typeset eq 'ZPRGKP' or Typeset eq 'KPICDE' or Typeset eq 'ATTTYP' or Typeset eq 'RVWTYP' or Typeset eq 'FIPRTY' or Typeset eq 'ACTTYP' or Typeset eq 'SF' or Typeset eq 'SC' or Typeset eq 'SS' or Typeset eq 'MC' or Typeset eq 'SSCP' or Typeset eq 'SSROUT' or Typeset eq 'SSSO' or Typeset eq 'ZCPRGT' or Typeset eq 'ZCPRMT' or Typeset eq 'ZCPROK' or Typeset eq 'ZCPRPR' or Typeset eq 'ZLPRPR' or Typeset eq 'ZLPRMT' or Typeset eq 'ZLPRET' or Typeset eq 'ZLPRGT' or Typeset eq 'ZCPRET' or Typeset eq 'CPGRP2' or Typeset eq 'CPGP10' or Typeset eq 'ZCRDA'",
                                "ConfigTypesetTypes?$filter=Typeset eq 'SSSTTY' or Typeset eq 'ZDMGRT' or Typeset eq 'APNRMD' or Typeset eq 'ZCPOVN' or Typeset eq 'SCGOTY' or Typeset eq 'UOMNO0' or Typeset eq 'ANSWID' or Typeset eq 'CPOLSZ' or Typeset eq 'CPOLSP' or Typeset eq 'CPGRP7' or Typeset eq 'CPGRP8' or Typeset eq 'CPINFR' or Typeset eq 'CPOWNB' or Typeset eq 'CPINUM' or Typeset eq 'CPGRP9' or Typeset eq 'CPGRP6' or Typeset eq 'CPDFUM' or Typeset eq 'ZADACT' or Typeset eq 'ZADTOT'",
                                "ValueHelps?$filter= ModelID eq 'SSGW_ALL' and (EntityType eq 'Visit' or EntityType eq 'ChannelPartner' or EntityType eq 'SSROItemDetail')"};
                    case MSP: //SDA
                        MSFAApplication.isSDA =true;
                        return new String[]{
                                "ChannelPartners",Attendances,
                                "SalesPersons",
                                "UserSalesPersons",
                                "RoutePlans",
                                "RouteSchedulePlans",
                                "RouteSchedules",
                                RouteScheduleSPs,
                                SyncHistroy,
                                UserPartners,
                                "SSSOs",
                                "SSSOItemDetails",
                                "CPDMSDivisions",
                                CPInfras,
                                CPPartnerFunctions,
                                SubDistricts,
                                Wards,
                                "CPSPRelations",
                                "UserChannelPartners",
                                Schemes, SchemeItemDetails, SchemeSlabs, SchemeGeographies, SchemeCPs, SchemeSalesAreas,
                                KPISet,
                                Targets,
                                TargetItems,
                                KPIItems,
                                CPPerformances,
                                Customers,
                                SSInvoiceTypes,
                                SSINVOICES,
                                SSInvoiceItemDetails,
//                          SSPurchaseInvoices,
                                "Visits",
                                "VisitActivities",
                                "CPStockItems?$filter=(StockOwner eq '02')",
                                "CPStockItemSnos?$filter=(StockOwner eq '02')",
                                "Brands",
                                "OrderMaterialGroups",
                                "UserProfileAuthSet?$filter=Application%20eq%20%27MSEC%27",
                                "UserProfileAuthSet?$filter=Application%20eq%20%27PD%27",
                                "ConfigTypsetTypeValues?$filter=Typeset eq 'ORDENT' or Typeset eq 'ZRPDVS' or Typeset eq 'ZCSDPM' or Typeset eq 'ZCSDMT' or Typeset eq 'ZCSDGT' or Typeset eq 'MSEC' or Typeset eq 'ATTTYP' or Typeset eq 'RVWTYP' or Typeset eq 'FIPRTY' or Typeset eq 'ACTTYP' or Typeset eq 'SF' or Typeset eq 'SC' or Typeset eq 'SS' or Typeset eq 'MC' or Typeset eq 'SSCP' or Typeset eq 'SSROUT' or Typeset eq 'SSSO' or Typeset eq 'ZCPRGT' or Typeset eq 'ZCPRMT' or Typeset eq 'ZCPROK' or Typeset eq 'ZCPRPR' or Typeset eq 'ZLPRPR' or Typeset eq 'ZLPRMT' or Typeset eq 'ZLPRET' or Typeset eq 'ZLPRGT' or Typeset eq 'ZCPRET' or Typeset eq 'CPGRP2' or Typeset eq 'ZCSDGT' or Typeset eq 'ZCSDPM' or Typeset eq 'ZCSDMT' or Typeset eq 'ZPSDAS' or Typeset eq 'ZPSDRP' or Typeset eq 'ZPSDCV' or Typeset eq 'ZPSDBL' or Typeset eq 'CPGP10'",
                                "ConfigTypesetTypes?$filter=Typeset eq 'APNRMD' or Typeset eq 'SCGOTY' or Typeset eq 'UOMNO0' or Typeset eq 'CPOLSZ' or Typeset eq 'CPOLSP' or Typeset eq 'CPGRP7' or Typeset eq 'CPGRP8' or Typeset eq 'CPINFR' or Typeset eq 'CPOWNB' or Typeset eq 'CPINUM' or Typeset eq 'CPGRP9' or Typeset eq 'ZADACT' or Typeset eq 'ZADTOT'",
                                "ValueHelps?$filter= ModelID eq 'SSGW_ALL' and (EntityType eq 'Visit' or EntityType eq 'ChannelPartner' or EntityType eq 'Target' or EntityType eq 'SSSOItemDetail' or EntityType eq  'SSSO')"};

                    case PSM: //PSM
                        MSFAApplication.isPSM =true;
                        return new String[]{
                                "CPConfigs",
                                "ChannelPartners",Attendances,
                                "SalesPersons",
                                "UserSalesPersons",
                                "RouteSchedulePlans",
                                "RouteScheduleSPs",
                                "RouteScheduleDetails",
                                "RouteProductDetTypes",
                                "CPProductDetTypes",
                                SyncHistroy,UserPartners,
                                "CPDMSDivisions",
                                CPPartnerFunctions,
                                "RouteSchedules",
                                "CPSPRelations",
                                SubDistricts,
                                Towns,
                                "UserProfileAuthSet?$filter=Application%20eq%20%27MSEC%27",
                                "ConfigTypsetTypeValues?$filter=Typeset eq 'MSEC' or Typeset eq 'STPNCD' or Typeset eq 'ORDENT' or Typeset eq 'ZKPICD' or Typeset eq 'DSTSTKVIEW' or Typeset eq 'ZPRGKP' or Typeset eq 'KPICDE' or Typeset eq 'ATTTYP' or Typeset eq 'RVWTYP' or Typeset eq 'FIPRTY' or Typeset eq 'ACTTYP' or Typeset eq 'SF' or Typeset eq 'SC' or Typeset eq 'SS' or Typeset eq 'MC' or Typeset eq 'SSCP' or Typeset eq 'SSROUT' or Typeset eq 'SSSO' or Typeset eq 'ZCPRGT' or Typeset eq 'ZCPRMT' or Typeset eq 'ZCPROK' or Typeset eq 'ZCPRPR' or Typeset eq 'ZLPRPR' or Typeset eq 'ZLPRMT' or Typeset eq 'ZLPRET' or Typeset eq 'ZLPRGT' or Typeset eq 'ZCPRET' or Typeset eq 'CPGRP2' or Typeset eq 'CPGP10'",
                                "ConfigTypesetTypes?$filter=Typeset eq 'APNRMD' or Typeset eq 'SCGOTY' or Typeset eq 'UOMNO0' or Typeset eq 'ANSWID' or Typeset eq 'CPOLSZ' or Typeset eq 'CPOLSP' or Typeset eq 'CPGRP7' or Typeset eq 'CPGRP8' or Typeset eq 'CPINFR' or Typeset eq 'CPOWNB' or Typeset eq 'CPINUM' or Typeset eq 'CPGRP9' or Typeset eq 'CPGRP6' or Typeset eq 'CPDFUM' or Typeset eq 'ZADACT' or Typeset eq 'ZADTOT'",
                                "ValueHelps?$filter= ModelID eq 'SSGW_ALL' and (EntityType eq 'ChannelPartner')"};
                    case VAN: //VAN
                        MSFAApplication.isVAN = true;
                        return new String[]{
                                Customers,
                                "CPConfigs",Attendances,
                                "ChannelPartners",SSROs,SSROItemDetails,
                                "SalesPersons",
                                "UserSalesPersons",
                                "RouteSchedulePlans",
                                "RouteScheduleSPs",
                                "RouteScheduleDetails",
                                "RouteProductDetTypes",
                                "CPProductDetTypes",
                                SyncHistroy, UserPartners,
                                "CPDMSDivisions",
                                CPPartnerFunctions,
                                CPPerformances,
                                "SPStockItems?$filter=(StockOwner eq '05')",
                                "SPStockItemSNos",
                                "RouteSchedules",
                                "CPSPRelations",
                                "UserChannelPartners",
                                SubDistricts,
                                Towns,
                                Schemes, SchemeItemDetails, SchemeSlabs, SchemeGeographies, SchemeCPs, SchemeSalesAreas,SchemeFreeMatGrpMaterials,SchemeFreeMatGrps,
                                SSInvoiceTypes,
                                SSINVOICES,
                                SSInvoiceItemDetails,
                                "SPNumberRangeSet",
                                "Visits",
                                "VisitActivities",
                                OrderMaterialGroups,Brands,
                                "UserProfileAuthSet?$filter=Application%20eq%20%27MSEC%27",
                                "ConfigTypsetTypeValues?$filter=Typeset eq 'MSEC' or Typeset eq 'ZVNSCR' or Typeset eq 'STPNCD' or Typeset eq 'ORDENT' or Typeset eq 'ZKPICD' or Typeset eq 'DSTSTKVIEW' or Typeset eq 'ZPRGKP' or Typeset eq 'KPICDE' or Typeset eq 'ATTTYP' or Typeset eq 'RVWTYP' or Typeset eq 'FIPRTY' or Typeset eq 'ACTTYP' or Typeset eq 'SF' or Typeset eq 'SC' or Typeset eq 'SS' or Typeset eq 'MC' or Typeset eq 'SSCP' or Typeset eq 'SSROUT' or Typeset eq 'SSSO' or Typeset eq 'ZCPRGT' or Typeset eq 'ZCPRMT' or Typeset eq 'ZCPROK' or Typeset eq 'ZCPRPR' or Typeset eq 'ZLPRPR' or Typeset eq 'ZLPRMT' or Typeset eq 'ZLPRET' or Typeset eq 'ZLPRGT' or Typeset eq 'ZCPRET' or Typeset eq 'CPGRP2' or Typeset eq 'CPGP10'",
                                "ConfigTypesetTypes?$filter=Typeset eq 'APNRMD' or Typeset eq 'SCGOTY' or Typeset eq 'UOMNO0' or Typeset eq 'ANSWID' or Typeset eq 'CPOLSZ' or Typeset eq 'CPOLSP' or Typeset eq 'CPGRP7' or Typeset eq 'CPGRP8' or Typeset eq 'CPINFR' or Typeset eq 'CPOWNB' or Typeset eq 'CPINUM' or Typeset eq 'CPGRP9' or Typeset eq 'CPGRP6' or Typeset eq 'CPDFUM' or Typeset eq 'ZADACT' or Typeset eq 'ZADTOT'",
                                "ValueHelps?$filter= ModelID eq 'SSGW_ALL' and (EntityType eq 'ChannelPartner' or EntityType eq 'SSROItemDetail' or EntityType eq 'SSInvoice')"};

                    case BVAN: //VAN
                        MSFAApplication.isBVAN = true;
                        return new String[]{
                                Customers,
                                "CPConfigs", Attendances,
                                "ChannelPartners",
                                SSROs, SSROItemDetails,
                                "SSSOs",
                                "SSSOItemDetails",
                                "SalesPersons",
                                "UserSalesPersons",
                                "RouteSchedulePlans",
                                "RouteScheduleSPs",
                                "RouteScheduleDetails",
                                "RouteProductDetTypes",
                                SyncHistroy, UserPartners,
                                "CPDMSDivisions",
                                CPPartnerFunctions,
                                "SPStockItems?$filter=(StockOwner eq '05')",
                                "CPStockItems?$filter=(StockOwner eq '01')",
                                "SPStockItemSNos",
                                "RouteSchedules",
                                "CPSPRelations",
                                "UserChannelPartners",
                                SubDistricts,
                                Towns,
                                Schemes, SchemeItemDetails, SchemeSlabs, SchemeGeographies, SchemeCPs, SchemeSalesAreas, SchemeFreeMatGrpMaterials, SchemeFreeMatGrps,
                                SSInvoiceTypes,
//                                SSINVOICES,
                                SSInvoiceItemDetails,
                                "SPNumberRangeSet",
                                "Visits",
                                "VisitActivities",
                                OrderMaterialGroups, Brands,
                                "UserProfileAuthSet?$filter=Application%20eq%20%27MSEC%27",
                                "ConfigTypsetTypeValues?$filter=Typeset eq 'MSEC' or Typeset eq 'ZVNSCR' or Typeset eq 'STPNCD' or Typeset eq 'ORDENT' or Typeset eq 'ZKPICD' or Typeset eq 'DSTSTKVIEW' or Typeset eq 'ZPRGKP' or Typeset eq 'KPICDE' or Typeset eq 'ATTTYP' or Typeset eq 'RVWTYP' or Typeset eq 'FIPRTY' or Typeset eq 'ACTTYP' or Typeset eq 'SF' or Typeset eq 'SC' or Typeset eq 'SS' or Typeset eq 'MC' or Typeset eq 'SSCP' or Typeset eq 'SSROUT' or Typeset eq 'SSSO' or Typeset eq 'ZCPRGT' or Typeset eq 'ZCPRMT' or Typeset eq 'ZCPROK' or Typeset eq 'ZCPRPR' or Typeset eq 'ZLPRPR' or Typeset eq 'ZLPRMT' or Typeset eq 'ZLPRET' or Typeset eq 'ZLPRGT' or Typeset eq 'ZCPRET' or Typeset eq 'CPGRP2' or Typeset eq 'CPGP10' or Typeset eq 'ZBDCRS'",
                                "ConfigTypesetTypes?$filter=Typeset eq 'APNRMD' or Typeset eq 'SCGOTY' or Typeset eq 'UOMNO0' or Typeset eq 'ANSWID' or Typeset eq 'CPOLSZ' or Typeset eq 'CPOLSP' or Typeset eq 'CPGRP7' or Typeset eq 'CPGRP8' or Typeset eq 'CPINFR' or Typeset eq 'CPOWNB' or Typeset eq 'CPINUM' or Typeset eq 'CPGRP9' or Typeset eq 'CPGRP6' or Typeset eq 'CPDFUM' or Typeset eq 'CPGRP3' or Typeset eq 'ZADACT' or Typeset eq 'ZADTOT'",
                                "ValueHelps?$filter= ModelID eq 'SSGW_ALL' and (EntityType eq 'ChannelPartner' or EntityType eq 'SSROItemDetail' or EntityType eq 'SSInvoice')"};

                }
            }
        }
            return null;
    }

    public static final void updateTCodeToSharedPreference(SharedPreferences sharedPreferences, SharedPreferences.Editor editor, ArrayList<Config> authList) {
        if (authList != null && authList.size() > 0) {
            if (sharedPreferences.contains(isStartCloseEnabled)) {
                editor.remove(isStartCloseEnabled);
            }
            if (sharedPreferences.contains(isRetailerListEnabled)) {
                editor.remove(isRetailerListEnabled);
            }
            if (sharedPreferences.contains("isRetailerUpdate")) {
                editor.remove("isRetailerUpdate");
            }
            if (sharedPreferences.contains(isCreateRetailerKey)) {
                editor.remove(isCreateRetailerKey);
            }
            if (sharedPreferences.contains(isSurveyCreateKey)) {
                editor.remove(isSurveyCreateKey);
            }
            if (sharedPreferences.contains("isHelpLine")) {
                editor.remove("isHelpLine");
            }
            if (sharedPreferences.contains("isMyStock")) {
                editor.remove("isMyStock");
            }
            if (sharedPreferences.contains("isVisitCreate")) {
                editor.remove("isVisitCreate");
            }
            if (sharedPreferences.contains(isRetailerTrendKey)) {
                editor.remove(isRetailerTrendKey);
            }
            if (sharedPreferences.contains("isRetailerStock")) {
                editor.remove("isRetailerStock");
            }
            if (sharedPreferences.contains(isCollListKey)) {
                editor.remove(isCollListKey);
            }
            if (sharedPreferences.contains(isSecondaryInvoiceListKey)) {
                editor.remove(isSecondaryInvoiceListKey);
            }
            if (sharedPreferences.contains(isRouteEnabled)) {
                editor.remove(isRouteEnabled);
            }
            if (sharedPreferences.contains(isAdhocVisitEnabled)) {
                editor.remove(isAdhocVisitEnabled);
            }
            if (sharedPreferences.contains("isTariffEnabled")) {
                editor.remove("isTariffEnabled");
            }
            if (sharedPreferences.contains(isSchemeEnabled)) {
                editor.remove(isSchemeEnabled);
            }
            if (sharedPreferences.contains(isBehaviourEnabled)) {
                editor.remove(isBehaviourEnabled);
            }
            if (sharedPreferences.contains(isMyTargetsEnabled)) {
                editor.remove(isMyTargetsEnabled);
            }
            if (sharedPreferences.contains("isMyPerformanceEnabled")) {
                editor.remove("isMyPerformanceEnabled");
            }
            if (sharedPreferences.contains(isFocusedProductKey)) {
                editor.remove(isFocusedProductKey);
            }
            if (sharedPreferences.contains(isDlvryConfDisableKey)) {
                editor.remove(isDlvryConfDisableKey);
            }
            if (sharedPreferences.contains(isNewProductKey)) {
                editor.remove(isNewProductKey);
            }
            if (sharedPreferences.contains(isFocusedProductKey)) {
                editor.remove(isFocusedProductKey);
            }
            if (sharedPreferences.contains(isOutstandingListKey)) {
                editor.remove(isOutstandingListKey);
            }
            if (sharedPreferences.contains(isDBStockEnabled)) {
                editor.remove(isDBStockEnabled);
            }
            if (sharedPreferences.contains(isVisualAidEnabled)) {
                editor.remove(isVisualAidEnabled);
            }
            if (sharedPreferences.contains(isRetailerStockKey)) {
                editor.remove(isRetailerStockKey);
            }
            if (sharedPreferences.contains(isDaySummaryKey)) {
                editor.remove(isDaySummaryKey);
            }
            if (sharedPreferences.contains(isDlrBehaviourKey)) {
                editor.remove(isDlrBehaviourKey);
            }
            if (sharedPreferences.contains("isActStatusEnabled")) {
                editor.remove("isActStatusEnabled");
            }
            if (sharedPreferences.contains(isMerchReviewKey)) {
                editor.remove(isMerchReviewKey);
            }
            if (sharedPreferences.contains(isMerchReviewListKey)) {
                editor.remove(isMerchReviewListKey);
            }
            if (sharedPreferences.contains(isSOCreateKey)) {
                editor.remove(isSOCreateKey);
            }
            if (sharedPreferences.contains(isVisitSummaryKey)) {
                editor.remove(isVisitSummaryKey);
            }
            if (sharedPreferences.contains(isInvoiceCreateKey)) {
                editor.remove(isInvoiceCreateKey);
            }
            if (sharedPreferences.contains(isCollCreateEnabledKey)) {
                editor.remove(isCollCreateEnabledKey);
            }
            if (sharedPreferences.contains(isFeedbackCreateKey)) {
                editor.remove(isFeedbackCreateKey);
            }
            if (sharedPreferences.contains("isCompInfoEnabled")) {
                editor.remove("isCompInfoEnabled");
            }
            if (sharedPreferences.contains(isStockListKey)) {
                editor.remove(isStockListKey);
            }
            if (sharedPreferences.contains(isFeedBackListKey)) {
                editor.remove(isFeedBackListKey);
            }
            if (sharedPreferences.contains(isCompetitorListKey)) {
                editor.remove(isCompetitorListKey);
            }
            if (sharedPreferences.contains(isReturnOrderListKey)) {
                editor.remove(isReturnOrderListKey);
            }
            if (sharedPreferences.contains(isSecondarySalesListKey)) {
                editor.remove(isSecondarySalesListKey);
            }
            if (sharedPreferences.contains(isSampleDisbursmentEnabledKey)) {
                editor.remove(isSampleDisbursmentEnabledKey);
            }
            if (sharedPreferences.contains(isCustomerComplaintEnabledKey)) {
                editor.remove(isCustomerComplaintEnabledKey);
            }
            if (sharedPreferences.contains(isReturnOrderCreateEnabled)) {
                editor.remove(isReturnOrderCreateEnabled);
            }
            if (sharedPreferences.contains(isWindowDisplayKey)) {
                editor.remove(isWindowDisplayKey);
            }
            if (sharedPreferences.contains(isSampleDisbursmentListKey)) {
                editor.remove(isSampleDisbursmentListKey);
            }
            if (sharedPreferences.contains(isCustomerComplaintListKey)) {
                editor.remove(isCustomerComplaintListKey);
            }
            if (sharedPreferences.contains(isExpenseEntryKey)) {
                editor.remove(isExpenseEntryKey);
            }
            if (sharedPreferences.contains(isDigitalProductEntryEnabled)) {
                editor.remove(isDigitalProductEntryEnabled);
            }
            if (sharedPreferences.contains(isCompInfoEnabled)) {
                editor.remove(isCompInfoEnabled);
            }
            if (sharedPreferences.contains(isCompetitorListKey)) {
                editor.remove(isCompetitorListKey);
            }
            if (sharedPreferences.contains(isRetailerApprovalKey)) {
                editor.remove(isRetailerApprovalKey);
            }
            if (sharedPreferences.contains(isExpenseListKey)) {
                editor.remove(isExpenseListKey);
            }
            if (sharedPreferences.contains(isUpdateRetailerKey)) {
                editor.remove(isUpdateRetailerKey);
            }

            if (sharedPreferences.contains(isSyncHistroyKey)) {
                editor.remove(isSyncHistroyKey);
            }

            if (sharedPreferences.contains(isSyncHistroyListKey)) {
                editor.remove(isSyncHistroyListKey);
            }
            editor.commit();
            for (int incVal = 0; incVal < authList.size(); incVal++) {
                if (authList.get(incVal).getFeature().equalsIgnoreCase(isStartCloseTcode)) {
                    editor.putString(isStartCloseEnabled, isStartCloseTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isRetailerListTcode)) {
                    editor.putString(isRetailerListEnabled, isRetailerListTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase("/ARTEC/SS_CP_CHG")) {
                    editor.putString("isRetailerUpdate", "/ARTEC/SS_CP_CHG");
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isCreateRetailerTcode)) {
                    editor.putString(isCreateRetailerKey, isCreateRetailerTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isUpdateRetailerTcode)) {
                    editor.putString(isUpdateRetailerKey, isUpdateRetailerTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase("/ARTEC/SF_HELPLINE")) {
                    editor.putString("isHelpLine", "/ARTEC/SF_HELPLINE");
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase("/ARTEC/SF_MYSTK")) {
                    editor.putString("isMyStock", "/ARTEC/SF_MYSTK");
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase("/ARTEC/SF_VST")) {
                    editor.putString("isVisitCreate", "/ARTEC/SF_VST");
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isRetailerTrendTcode)) {
                    editor.putString(isRetailerTrendKey, isRetailerTrendTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase("/ARTEC/SS_CPSTK")) {
                    editor.putString(isRetailerStockKey, "/ARTEC/SS_CPSTK");
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isCollListTcode)) {
                    editor.putString(isCollListKey, isCollListTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isSecondaryInvoiceListTcode)) {
                    editor.putString(isSecondaryInvoiceListKey, isSecondaryInvoiceListTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isRoutePlaneTcode)) {
                    editor.putString(isRouteEnabled, isRoutePlaneTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isAdhocVistTcode)) {
                    editor.putString(isAdhocVisitEnabled, isAdhocVistTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase("/ARTEC/SS_TARIFF")) {
                    editor.putString("isTariffEnabled", "/ARTEC/SS_TARIFF");
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isSchemeTcode)) {
                    editor.putString(isSchemeEnabled, isSchemeTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isBehaviourTcode)) {
                    editor.putString(isBehaviourEnabled, isBehaviourTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isMyTargetsTcode)) {
                    editor.putString(isMyTargetsEnabled, isMyTargetsTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase("/ARTEC/SS_MYPERF")) {
                    editor.putString("isMyPerformanceEnabled", "/ARTEC/SS_MYPERF");
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isFocusedProductTcode)) {
                    editor.putString(isFocusedProductKey, isFocusedProductTcode);
                }else if (authList.get(incVal).getFeature().equalsIgnoreCase(isDlvryConfDisableTCode)) {
                    editor.putString(isDlvryConfDisableKey, isDlvryConfDisableTCode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isNewProductTcode)) {
                    editor.putString(isNewProductKey, isNewProductTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isMustSellTcode)) {
                    editor.putString(isMustSellKey, isMustSellTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase("/ARTEC/SS_ACTSTS")) {
                    editor.putString("isActStatusEnabled", "/ARTEC/SS_ACTSTS");
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isMerchReviewTcode)) {
                    editor.putString(isMerchReviewKey, isMerchReviewTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isInvoiceTcode)) {
                    editor.putString(isInvoiceCreateKey, isInvoiceTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isCollCreateTcode)) {
                    editor.putString(isCollCreateEnabledKey, isCollCreateTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isFeedbackTcode)) {
                    editor.putString(isFeedbackCreateKey, isFeedbackTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase("/ARTEC/SS_COMPINFO")) {
                    editor.putString("isCompInfoEnabled", "/ARTEC/SS_COMPINFO");
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isOutstandingListTcode)) {
                    editor.putString(isOutstandingListKey, isOutstandingListTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isMerchReviewListTcode)) {
                    editor.putString(isMerchReviewListKey, isMerchReviewListTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isSOCreateTcode)) {
                    editor.putString(isSOCreateKey, isSOCreateTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isDBStockTcode)) {
                    editor.putString(isDBStockEnabled, isDBStockTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isDaySummaryTcode)) {
                    editor.putString(isDaySummaryKey, isDaySummaryTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isDlrBehaviourTcode)) {
                    editor.putString(isDlrBehaviourKey, isDlrBehaviourTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isRetailerStockTcode)) {
                    editor.putString(isRetailerStockKey, isRetailerStockTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isVisitSummaryTcode)) {
                    editor.putString(isVisitSummaryKey, isVisitSummaryTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isVisualAidTcode)) {
                    editor.putString(isVisualAidEnabled, isVisualAidTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isStockListTcode)) {
                    editor.putString(isStockListKey, isStockListTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isFeedBackListTcode)) {
                    editor.putString(isFeedBackListKey, isFeedBackListTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isReturnOrderListTcode)) {
                    editor.putString(isReturnOrderListKey, isReturnOrderListTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isSecondarySalesListTcode)) {
                    editor.putString(isSecondarySalesListKey, isSecondarySalesListTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isSampleDisbursmentCreateTcode)) {
                    editor.putString(isSampleDisbursmentEnabledKey, isSampleDisbursmentCreateTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isCustomerComplaintCreateTcode)) {
                    editor.putString(isCustomerComplaintEnabledKey, isCustomerComplaintCreateTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isReturnOrderTcode)) {
                    editor.putString(isReturnOrderCreateEnabled, isReturnOrderTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isWindowDisplayTcode)) {
                    editor.putString(isWindowDisplayKey, isWindowDisplayTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isSampleDisbursmentListTcode)) {
                    editor.putString(isSampleDisbursmentListKey, isSampleDisbursmentListTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isCustomerComplaintListTcode)) {
                    editor.putString(isCustomerComplaintListKey, isCustomerComplaintListTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isExpenseEntryTcode)) {
                    editor.putString(isExpenseEntryKey, isExpenseEntryTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isDigitalProductEntryTcode)) {
                    editor.putString(isDigitalProductEntryEnabled, isDigitalProductEntryTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isSchemeTcode)) {
                    editor.putString(isSchemeEnabled, isSchemeTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isCompInfoTcode)) {
                    editor.putString(isCompInfoEnabled, isCompInfoTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isCompetitorListTcode)) {
                    editor.putString(isCompetitorListKey, isCompetitorListTcode);
                } else if (authList.get(incVal).getFeature().equalsIgnoreCase(isRetailerApprovalTcode)) {
                    editor.putString(isRetailerApprovalKey, isRetailerApprovalTcode);
                }else if (authList.get(incVal).getFeature().equalsIgnoreCase(isExpenseListTcode)) {
                    editor.putString(isExpenseListKey, isExpenseListTcode);
                }else if (authList.get(incVal).getFeature().equalsIgnoreCase(isSyncHistroyTcode)) {
                    editor.putString(isSyncHistroyKey, isSyncHistroyTcode);
                }else if (authList.get(incVal).getFeature().equalsIgnoreCase(isSyncHistroyListTcode)) {
                    editor.putString(isSyncHistroyListKey, isSyncHistroyListTcode);
                }else if (authList.get(incVal).getFeature().equalsIgnoreCase(isSurveyCreateTcode)) {
                    editor.putString(isSurveyCreateKey, isSurveyCreateTcode);
                }

                editor.commit();
            }
        }
    }
    public static String getSPGUID() {
        String spGuid = "";
       /* try {
            spGuid = OfflineManager.getGuidValueByColumnName(Constants.UserSalesPersons + "?$select=" + Constants.SPGUID, Constants.SPGUID);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        return "";
//       return "00505635-001e-1ed7-a1af-00ae1c55ee43";
    }


        public static ErrorBean getErrorCode ( int operation, Exception exception, Context context){
            ErrorBean errorBean = new ErrorBean();
            try {
                int errorCode = 0;
                boolean hasNoError = true;
                if ((operation == Operation.Create.getValue())) {

                    try {
                        // below error code getting from online manger (While posting data vault data)
//                    errorCode = ((ErrnoException) ((ODataNetworkException) exception).getCause().getCause()).errno;
                        Throwable throwables = (exception.getCause()).getCause().getCause();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            if (throwables instanceof ErrnoException) {
                                errorCode = ((ErrnoException) throwables).errno;
                            } else {
                                if (exception.getMessage().contains(Constants.Unothorized_Error_Name)) {
                                    errorCode = Constants.UnAuthorized_Error_Code;
                                    hasNoError = false;
                                } else if (exception.getMessage().contains(Constants.Comm_error_name)) {
                                    hasNoError = false;
                                    errorCode = Constants.Comm_Error_Code;
                                } else if (exception.getMessage().contains(Constants.Network_Name)) {
                                    hasNoError = false;
                                    errorCode = Constants.Network_Error_Code;
                                } else {
                                    Constants.ErrorNo = 0;
                                }
                            }
                        } else {
                            try {
                                if (exception.getMessage() != null) {
                                    if (exception.getMessage().contains(Constants.Unothorized_Error_Name)) {
                                        errorCode = Constants.UnAuthorized_Error_Code;
                                        hasNoError = false;
                                    } else if (exception.getMessage().contains(Constants.Comm_error_name)) {
                                        hasNoError = false;
                                        errorCode = Constants.Comm_Error_Code;
                                    } else if (exception.getMessage().contains(Constants.Network_Name)) {
                                        hasNoError = false;
                                        errorCode = Constants.Network_Error_Code;
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                        if (errorCode != Constants.UnAuthorized_Error_Code) {
                            if (errorCode == Constants.Network_Error_Code || errorCode == Constants.Comm_Error_Code) {
                                hasNoError = false;
                            } else {
                                hasNoError = true;
                            }
                        }
                    } catch (Exception e1) {
                        if (exception.getMessage().contains(Constants.Unothorized_Error_Name)) {
                            errorCode = Constants.UnAuthorized_Error_Code;
                            hasNoError = false;
                        } else {
                            Constants.ErrorNo = 0;
                        }
                    }
                    LogManager.writeLogError("Error : [" + errorCode + "]" + exception.getMessage());

                } else if (operation == Operation.OfflineFlush.getValue() || operation == Operation.OfflineRefresh.getValue()) {
                    try {
                        // below error code getting from offline manger (While posting flush and refresh collection)
                        try {
                            errorCode = ((ODataOfflineException) exception.getCause()).getCode();
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }

                        // Display popup for Communication and Unauthorized errors
                        if (errorCode == Constants.Network_Error_Code_Offline
                                || errorCode == Constants.UnAuthorized_Error_Code_Offline
                                || errorCode == Constants.Unable_to_reach_server_offline
                                || errorCode == Constants.Resource_not_found
//                            || errorCode==Constants.DATABASE_ERROR
                                || errorCode == Constants.Unable_to_reach_server_failed_offline) {

                            hasNoError = false;
                        } else {
                            hasNoError = true;
                        }

                    } catch (Exception e) {
                        try {
                            String mStrErrMsg = exception.getCause().getLocalizedMessage();
                            if (mStrErrMsg.contains(Executing_SQL_Commnd_Error)) {
                                hasNoError = false;
                                errorCode = -10001;
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        e.printStackTrace();
                    }
                    if (errorCode != 0) {
                        LogManager.writeLogError("Error : [" + errorCode + "]" + exception.getMessage());
                    }
                } else if (operation == Operation.GetStoreOpen.getValue()) {
                    // below error code getting from offline manger (While posting flush and refresh collection)
                    try {
                        errorCode = ((ODataOfflineException) exception.getCause()).getCode();

                        // Display popup for Communication and Unauthorized errors
                        if (errorCode == Constants.Network_Error_Code_Offline
                                || errorCode == Constants.UnAuthorized_Error_Code_Offline
                                || errorCode == Constants.Unable_to_reach_server_offline
                                || errorCode == Constants.Resource_not_found
//                            || errorCode==Constants.DATABASE_ERROR
                                || errorCode == Constants.Unable_to_reach_server_failed_offline) {

                            hasNoError = false;
                        } else {
                            hasNoError = true;
                        }
                    } catch (Exception e) {
                        try {
                            String mStrErrMsg = exception.getCause().getLocalizedMessage();
                            if (mStrErrMsg.contains(Store_Defining_Req_Not_Matched)) {
                                hasNoError = false;
                                errorCode = -10247;
                            }
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        e.printStackTrace();
                    }
                }

                errorBean.setErrorCode(errorCode);
                if (exception.getMessage() != null && !exception.getMessage().equalsIgnoreCase("")) {
                    errorBean.setErrorMsg(exception.getMessage());
                } else {
                    errorBean.setErrorMsg(context.getString(R.string.unknown_error));
                }

                errorBean.setHasNoError(hasNoError);
            } catch (Exception e) {
                e.printStackTrace();
            }

            boolean isStoreFaied = false;
            if (errorBean.getErrorCode() == Constants.Resource_not_found
                    || errorBean.getErrorCode() == UtilConstants.Execu_SQL_Error_Code
//                || errorBean.getErrorCode() == Constants.DATABASE_ERROR
                    || errorBean.getErrorCode() == UtilConstants.Store_Def_Not_matched_Code
                    || errorBean.getErrorMsg().contains(Database_Transction_Failed_Error_Code + "")) {
                isStoreFaied = UtilConstants.closeStore(context,
                        OfflineManager.options, errorBean.getErrorMsg() + "",
                        offlineStore, Constants.PREFS_NAME, errorBean.getErrorCode() + "");

            }
            if (errorBean.getErrorMsg().contains(UtilConstants.Build_Database_Failed_Error_Code1 + "")
                    || errorBean.getErrorMsg().contains(UtilConstants.Build_Database_Failed_Error_Code2 + "")
                    || errorBean.getErrorMsg().contains(UtilConstants.Build_Database_Failed_Error_Code3 + "")
                    || errorBean.getErrorCode() == UtilConstants.Execu_SQL_Error_Code
                    || errorBean.getErrorCode() == UtilConstants.Store_Def_Not_matched_Code
                    || errorBean.getErrorMsg().contains(Database_Transction_Failed_Error_Code + "")) {
                if (errorBean.getErrorMsg().contains("500")
                        || errorBean.getErrorMsg().contains(Constants.RFC_ERROR_CODE_100029)
                        || errorBean.getErrorMsg().contains(Constants.RFC_ERROR_CODE_100027)) {
                    errorBean.setStoreFailed(false);
                } else {
                    offlineStore = null;
                    OfflineManager.options = null;
                    errorBean.setStoreFailed(true);
                }

            } else {
                errorBean.setStoreFailed(false);
            }
            String value = checkUnknownNetworkerror(errorBean.getErrorMsg(), context);
//        String value1= makecustomHttpErrormessage(errorBean.getErrorMsg(),getDefinigReq(context));
            errorBean.setErrorMsg(value);
            int httperrorCode = makecustomHttpErrorCode(value);
            if (httperrorCode != 0)
                errorBean.setErrorCode(httperrorCode);
            return errorBean;
        }
        public static HashMap<String, String> httpErrorCodes = new HashMap<String, String>();
        public static void httphashmaperrorcodes () {
            //500 series
            httpErrorCodes = new HashMap<String, String>();
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
        public static int makecustomHttpErrorCode (String error_msg){
            httphashmaperrorcodes();
            String httperrorcode = "";
            int code = 0;
            if (!TextUtils.isEmpty(error_msg) && error_msg.contains("HTTP code")) {
                List<String> errorList = Arrays.asList(error_msg.split(","));
                if (errorList.size() > 0) {

                    for (String data : errorList) {
                        Iterator<String> keySetIterator = httpErrorCodes.keySet().iterator();
                        while (keySetIterator.hasNext()) {
                            String key = keySetIterator.next();
                            if (data.contains(key)) {
                                httperrorcode = key;
                                break;
                            }

                        }
                    }

                }


            }
            if (!TextUtils.isEmpty(httperrorcode)) {
                try {
                    code = Integer.parseInt(httperrorcode);
                } catch (Exception e) {
                    e.printStackTrace();
                    code = 0;
                }
            }
            return code;

        }
        public static String makecustomHttpErrormessage (String error_msg, String[]defQuery){
            httphashmaperrorcodes();
            if (!TextUtils.isEmpty(error_msg) && error_msg.contains("HTTP code")) {
                String make_message = "";
                List<String> errorList = Arrays.asList(error_msg.split(","));
                if (errorList.size() > 0) {
                    for (int i = 0; i < defQuery.length; i++) {
                        if (error_msg.contains(defQuery[i])) {
                            make_message = defQuery[i] + ":";
                            break;
                        } else if (error_msg.contains("?")) {
                            try {
                                if (defQuery[i].contains("?")) {
                                    String value = defQuery[i];
                                    int position = value.indexOf("?");
                                    value = value.substring(0, position);
                                    if (error_msg.contains(value)) {
                                        make_message = " '" + value + "' ";
                                        break;
                                    }
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    String httperrormsg = "";
                    for (String data : errorList) {
                        Iterator<String> keySetIterator = httpErrorCodes.keySet().iterator();
                        while (keySetIterator.hasNext()) {
                            String key = keySetIterator.next();
                            if (data.contains(key)) {
                                httperrormsg = "[" + key + "]";
                                break;
                            }

                        }
                    }
                    if (!TextUtils.isEmpty(httperrormsg))
                        make_message += httperrormsg;
                    else make_message = error_msg;
                } else {
                    make_message = error_msg;
                }
                return make_message;

            }
            return error_msg;

        }

        public static String checkUnknownNetworkerror (String errorMsg, Context mcontext){
            String customErrorMsg = errorMsg;
            if (!TextUtils.isEmpty(errorMsg)) {
                if ((errorMsg.contains("10340"))) {
                    String value1 = makecustomHttpErrormessage(errorMsg, getDefiningReq(mcontext));
                    int httperrorCode = makecustomHttpErrorCode(errorMsg);
                    if (httperrorCode != 0) {
                        switch (httperrorCode) {
                            case 500:
                                customErrorMsg = "Synchronization not completed due to connection timeout error " + value1 + ". Please contact support team [10340]";
                                break;
                            case 504:
                                customErrorMsg = "Synchronization not completed due to connection timeout error " + value1 + ". Please contact support team [10340]";
                                break;
                            case 400:
                                customErrorMsg = "Synchronization not completed due to error in " + value1 + ". Please contact support team [10340]";
                                break;
                            default:
                                customErrorMsg = "Synchronization not completed due to an error on the server. Please contact support team [10340]";
                        }
                    } else {
                        customErrorMsg = "Synchronization not completed due to an error on the server. Please contact support team [10340]";
                    }
//                customErrorMsg = "Synchronization not completed due to connection timeout error [10340].Please contact support team";

                } else if (errorMsg.contains("10341"))
                    customErrorMsg = "Synchronization not completed due to network interuption. Please check the data connection and try again [10341]";
                else if (errorMsg.contains("10342"))
                    customErrorMsg = "Synchronization not completed due to network interuption. Please check the data connection and try again [10342]";
                else if (errorMsg.contains("10343")) {
                    customErrorMsg = getWebViewEMessage(mcontext);
                } else if (errorMsg.contains("10345"))
                    customErrorMsg = "Synchronization not completed due to server unavailability. Please contact support team [10345]";
                else if (errorMsg.contains("10346"))
                    customErrorMsg = "Synchronization not completed due to network interuption. Please check the data connection and try again [10346]";
                else if (errorMsg.contains("10349"))
                    customErrorMsg = "Synchronization not completed due to network interuption. Please check the data connection and try again [10349]";
                else if (errorMsg.contains("10348"))
                    customErrorMsg = "Please try synchronization again after some time [10348]";
                else if (errorMsg.contains("10345"))
                    customErrorMsg = "Synchronization not completed due to server unavailability. Please contact support team [10345]";
                else if (errorMsg.contains("10065"))
                    customErrorMsg = "Not able to download data from server at this moment. Please check the data connection and try again. Contact support team if issue exist after reattempt [10065]";
                else if (errorMsg.contains("10058"))
                    customErrorMsg = "Please restart your internet connection and try again [10058]";
            }
            return customErrorMsg;
        }

        private static String getErrorMessageWithHTTPCode () {
            return "";
        }


        public static String getWebviewVersion (Context context){
            PackageInfo pi = null;
            String value = "";
            PackageManager pm = context.getPackageManager();
            try {
                pi = pm.getPackageInfo("com.google.android.webview", 0);
                value = pi.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                value = "0";
            }
            return value;
        }
        public static String getChromeVersion (Context context){
            PackageInfo pi = null;
            String value = "";
            PackageManager pm = context.getPackageManager();
            try {
                pi = pm.getPackageInfo("com.android.chrome", 0);
                value = pi.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                value = "0";
            }
            return value;
        }

        public static String getWebViewVersion (Context context){
            PackageInfo pi = null;
            String value = "";
            PackageManager pm = context.getPackageManager();
            try {
                pi = pm.getPackageInfo("com.google.android.webview", 0);
                value = pi.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                value = "0";
            }
            return value;
        }

        private static String getWebViewEMessage (Context context){
            if (Build.VERSION.SDK_INT <= 23) {
                String value = getWebviewVersion(context);
                if (value != null && value.length() > 2) {
                    value = value.substring(0, 2);
                    if (Integer.parseInt(value) < 79) {
                        return "Please update web view and chrome versions to 79 or above and try again";
                    } else {
                        return "User ID locked or password expired.Please contact support team";
                    }
                }
            } else {
                return "User ID locked or password expired.Please contact support team";
            }
            return "User ID locked or password expired.Please contact support team";
        }

        public static void displayErrorDialog (Context context, String error_msg){
            String mErrorMsg = "";
            if (Constants.AL_ERROR_MSG.size() > 0) {
                mErrorMsg = Constants.convertALBussinessMsgToString(Constants.AL_ERROR_MSG);
            }
            if (mErrorMsg.equalsIgnoreCase("")) {
                UtilConstants.showAlert(error_msg, context);
            } else {
                Constants.customAlertDialogWithScroll(context, mErrorMsg);
            }
        }

        public static void displayMsgReqError ( int errorCode, Context context){
            if (errorCode == Constants.UnAuthorized_Error_Code || errorCode == Constants.UnAuthorized_Error_Code_Offline) {
                UtilConstants.showAlert(context.getString(R.string.auth_fail_plz_contact_admin, errorCode + ""), context);
            } else if (errorCode == Constants.Unable_to_reach_server_offline || errorCode == Constants.Network_Error_Code_Offline) {
                UtilConstants.showAlert(context.getString(R.string.data_conn_lost_during_sync_error_code, errorCode + ""), context);
            } else if (errorCode == Constants.Resource_not_found) {
                UtilConstants.showAlert(context.getString(R.string.techincal_error_plz_contact, errorCode + ""), context);
            } else if (errorCode == Constants.Unable_to_reach_server_failed_offline) {
                UtilConstants.showAlert(context.getString(R.string.comm_error_server_failed_plz_contact, errorCode + ""), context);
            } else {
                UtilConstants.showAlert(context.getString(R.string.data_conn_lost_during_sync_error_code, errorCode + ""), context);
            }
        }

        public static String makeMsgReqError ( int errorCode, Context context,boolean isInvError){
            String mStrErrorMsg = "";

            if (!isInvError) {
                if (errorCode == Constants.UnAuthorized_Error_Code || errorCode == Constants.UnAuthorized_Error_Code_Offline) {
                    mStrErrorMsg = context.getString(R.string.auth_fail_plz_contact_admin, errorCode + "");
                } else if (errorCode == Constants.Unable_to_reach_server_offline || errorCode == Constants.Network_Error_Code_Offline) {
                    mStrErrorMsg = context.getString(R.string.data_conn_lost_during_sync_error_code, errorCode + "");
                } else if (errorCode == Constants.Resource_not_found) {
                    mStrErrorMsg = context.getString(R.string.techincal_error_plz_contact, errorCode + "");
                } else if (errorCode == Constants.Unable_to_reach_server_failed_offline) {
                    mStrErrorMsg = context.getString(R.string.comm_error_server_failed_plz_contact, errorCode + "");
                } else {
                    mStrErrorMsg = context.getString(R.string.data_conn_lost_during_sync_error_code, errorCode + "");
                }
            } else {
                if (errorCode == 4) {
                    mStrErrorMsg = context.getString(R.string.auth_fail_plz_contact_admin, Constants.UnAuthorized_Error_Code + "");
                } else if (errorCode == 3) {
                    mStrErrorMsg = context.getString(R.string.data_conn_lost_during_sync_error_code, Constants.Network_Error_Code + "");
                } else {
                    mStrErrorMsg = context.getString(R.string.data_conn_lost_during_sync_error_code, Constants.Network_Error_Code + "");
                }
            }

            return mStrErrorMsg;
        }

        public static void setSyncTime (Context context){
            SharedPreferences settings = context.getSharedPreferences(Constants.PREFS_NAME,
                    0);
            if (settings.getBoolean(Constants.isReIntilizeDB, false)) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean(Constants.isReIntilizeDB, false);
                editor.apply();
                try {
                    try {
                        SyncUtils.initialInsert(context);  // create sync history table
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SyncUtils.updateAllSyncHistory(context);
                } catch (Exception exce) {
                    LogManager.writeLogError(Constants.sync_table_history_txt + exce.getMessage());
                }
            }
        }

        /**
         * SHOW PROGRESS DIALOG
         *
         * @param context
         * @param title
         * @param message
         * @return
         */
        public static ProgressDialog showProgressDialog (Context context, String title, String
        message){
            ProgressDialog progressDialog = null;
            try {
                progressDialog = new ProgressDialog(context, R.style.MaterialAlertDialog);
                progressDialog.setMessage(message);
                progressDialog.setTitle(title);
                progressDialog.setCancelable(false);
                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return progressDialog;
        }

        public static void getLocation (Activity mActivity,final LocationInterface locationInterface)
        {
            UtilConstants.latitude = 0.0;
            UtilConstants.longitude = 0.0;
            LocationUtils.getCustomLocation(mActivity, new LocationInterface() {
                @Override
                public void location(boolean status, LocationModel locationModel, String errorMsg, int errorCode) {
                    if (status) {
                        android.location.Location location = locationModel.getLocation();
                        UtilConstants.latitude = location.getLatitude();
                        UtilConstants.longitude = location.getLongitude();
                        Log.d("LocationUtils", "location: " + locationModel.getLocationFrom());
                    }
                    if (locationInterface != null) {
                        locationInterface.location(status, locationModel, errorMsg, errorCode);
                    }
                }
            });
        }

        public static String convertStrGUID32to36 (String strGUID32){
            return CharBuffer.join9(StringFunction.substring(strGUID32, 0, 8), "-", StringFunction.substring(strGUID32, 8, 12), "-", StringFunction.substring(strGUID32, 12, 16), "-", StringFunction.substring(strGUID32, 16, 20), "-", StringFunction.substring(strGUID32, 20, 32));
        }

        public static ArrayList<String> getPendingList () {
            ArrayList<String> alFlushColl = new ArrayList<>();
            try {
                if (OfflineManager.getVisitStatusForCustomer(Constants.Attendances + Constants.isLocalFilterQry)) {
                    alFlushColl.add(Constants.Attendances);
                }
                if (OfflineManager.getVisitStatusForCustomer(Constants.Visits + Constants.isLocalFilterQry)) {
                    alFlushColl.add(Constants.Visits);
                }

                if (OfflineManager.getVisitStatusForCustomer(Constants.VisitActivities + Constants.isLocalFilterQry)) {
                    alFlushColl.add(Constants.VisitActivities);
                }

                if (OfflineManager.getVisitStatusForCustomer(Constants.MerchReviews + Constants.isLocalFilterQry)) {
                    alFlushColl.add(Constants.MerchReviews);
                }
                if (OfflineManager.getVisitStatusForCustomer(Constants.SyncHistorys + Constants.isLocalFilterQry)) {
                    alFlushColl.add(Constants.SyncHistorys);
                }

            } catch (OfflineODataStoreException e) {
                LogManager.writeLogError(Constants.error_txt + e.getMessage());
            }

            return alFlushColl;
        }

        public static ArrayList<String> getRefreshList () {
            ArrayList<String> alAssignColl = new ArrayList<>();
            try {
                alAssignColl.add(Constants.ConfigTypsetTypeValues);
                if (OfflineManager.getVisitStatusForCustomer(Constants.Attendances + Constants.isLocalFilterQry)) {
                    alAssignColl.add(Constants.Attendances);
                }
                if (OfflineManager.getVisitStatusForCustomer(Constants.Visits + Constants.isLocalFilterQry)) {
                    alAssignColl.add(Constants.Visits);
                }

                if (OfflineManager.getVisitStatusForCustomer(Constants.VisitActivities + Constants.isLocalFilterQry)) {
                    alAssignColl.add(Constants.VisitActivities);
                }
                if (OfflineManager.getVisitStatusForCustomer(Constants.SyncHistorys + Constants.isLocalFilterQry)) {
                    alAssignColl.add(Constants.SyncHistorys);
                }

                if (OfflineManager.getVisitStatusForCustomer(Constants.MerchReviews + Constants.isLocalFilterQry)) {
                    alAssignColl.add(Constants.MerchReviews);
                    alAssignColl.add(Constants.MerchReviewImages);
                }

            } catch (OfflineODataStoreException e) {
                LogManager.writeLogError(Constants.error_txt + e.getMessage());
            }

            return alAssignColl;
        }

        public static String getConcatinatinFlushCollectios (ArrayList < String > alFlushColl) {
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

        @SuppressLint("NewApi")
        public static String getSyncType (Context context, String collectionName, String operation){
            String mStrSyncType = "4";
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);
            String sharedVal = sharedPreferences.getString(collectionName, "");
            if (!sharedVal.equalsIgnoreCase("")) {
                if (operation.equalsIgnoreCase(CreateOperation)) {
                    if (sharedVal.substring(0, 1).equalsIgnoreCase("0")) {
                        mStrSyncType = "4";
                    } else if (sharedVal.substring(0, 1).equalsIgnoreCase("1")) {
                        mStrSyncType = "1";
                    } else if (sharedVal.substring(0, 1).equalsIgnoreCase("2")) {
                        mStrSyncType = "2";
                    } else if (sharedVal.substring(0, 1).equalsIgnoreCase("3")) {
                        mStrSyncType = "3";
                    } else if (sharedVal.substring(0, 1).equalsIgnoreCase("4")) {
                        mStrSyncType = "4";
                    } else if (sharedVal.substring(0, 1).equalsIgnoreCase("5")) {
                        mStrSyncType = "5";
                    }
                } else if (operation.equalsIgnoreCase(ReadOperation)) {
                    if (sharedVal.substring(1, 2).equalsIgnoreCase("0")) {
                        mStrSyncType = "4";
                    } else if (sharedVal.substring(1, 2).equalsIgnoreCase("1")) {
                        mStrSyncType = "1";
                    } else if (sharedVal.substring(1, 2).equalsIgnoreCase("2")) {
                        mStrSyncType = "2";
                    } else if (sharedVal.substring(1, 2).equalsIgnoreCase("3")) {
                        mStrSyncType = "3";
                    } else if (sharedVal.substring(1, 2).equalsIgnoreCase("4")) {
                        mStrSyncType = "4";
                    } else if (sharedVal.substring(1, 2).equalsIgnoreCase("5")) {
                        mStrSyncType = "5";
                    }

                } else if (operation.equalsIgnoreCase(UpdateOperation)) {
                    if (sharedVal.substring(2, 3).equalsIgnoreCase("0")) {
                        mStrSyncType = "4";
                    } else if (sharedVal.substring(2, 3).equalsIgnoreCase("1")) {
                        mStrSyncType = "1";
                    } else if (sharedVal.substring(2, 3).equalsIgnoreCase("2")) {
                        mStrSyncType = "2";
                    } else if (sharedVal.substring(2, 3).equalsIgnoreCase("3")) {
                        mStrSyncType = "3";
                    } else if (sharedVal.substring(2, 3).equalsIgnoreCase("4")) {
                        mStrSyncType = "4";
                    } else if (sharedVal.substring(2, 3).equalsIgnoreCase("5")) {
                        mStrSyncType = "5";
                    }
                } else if (operation.equalsIgnoreCase(DeleteOperation)) {
                    if (sharedVal.substring(3, 4).equalsIgnoreCase("0")) {
                        mStrSyncType = "4";
                    } else if (sharedVal.substring(3, 4).equalsIgnoreCase("1")) {
                        mStrSyncType = "1";
                    } else if (sharedVal.substring(3, 4).equalsIgnoreCase("2")) {
                        mStrSyncType = "2";
                    } else if (sharedVal.substring(3, 4).equalsIgnoreCase("3")) {
                        mStrSyncType = "3";
                    } else if (sharedVal.substring(3, 4).equalsIgnoreCase("4")) {
                        mStrSyncType = "4";
                    } else if (sharedVal.substring(3, 4).equalsIgnoreCase("5")) {
                        mStrSyncType = "5";
                    }
                } else if (operation.equalsIgnoreCase(QueryOperation)) {
                    if (sharedVal.substring(4, 5).equalsIgnoreCase("0")) {
                        mStrSyncType = "4";
                    } else if (sharedVal.substring(4, 5).equalsIgnoreCase("1")) {
                        mStrSyncType = "1";
                    } else if (sharedVal.substring(4, 5).equalsIgnoreCase("2")) {
                        mStrSyncType = "2";
                    } else if (sharedVal.substring(4, 5).equalsIgnoreCase("3")) {
                        mStrSyncType = "3";
                    } else if (sharedVal.substring(4, 5).equalsIgnoreCase("4")) {
                        mStrSyncType = "4";
                    } else if (sharedVal.substring(4, 5).equalsIgnoreCase("5")) {
                        mStrSyncType = "5";
                    }
                }
            } else {
                mStrSyncType = "4";
            }
            return mStrSyncType;
        }

        public static String convertALBussinessMsgToString (ArrayList < String > arrayList) {
            String mErrorMsg = "";
            if (arrayList != null && arrayList.size() > 0) {
                for (String errMsg : arrayList) {
                    if (mErrorMsg.length() == 0) {
                        mErrorMsg = mErrorMsg + errMsg;
                    } else {
                        mErrorMsg = mErrorMsg + "\n" + errMsg;
                    }
                }
            }
            return mErrorMsg;
        }

        public static void customAlertDialogWithScroll ( final Context context, final String mErrTxt)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.custom_dialog_scroll, null);

            String mStrErrorEntity = getErrorEntityName();

            TextView textview = view.findViewById(R.id.tv_err_msg);

            textview.setText(context.getString(R.string.msg_error_occured_during_sync_except) + " " + mStrErrorEntity + " \n" + mErrTxt);

            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.DialogTheme);
            alertDialog.setCancelable(false)
                    .setPositiveButton(context.getString(R.string.msg_ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            copyMessageToClipBoard(context, mErrTxt);
                        }
                    });
            alertDialog.setView(view);
            AlertDialog alert = alertDialog.create();
            alert.show();

        }

        public static void copyMessageToClipBoard (Context context, String message){
            ClipboardManager clipboard = (ClipboardManager) context.getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Error Message", message);
            clipboard.setPrimaryClip(clip);
            showAlert(context.getString(R.string.issue_copied_to_clipboard_send_to_chnnel_team), context);
        }

        public static String getErrorEntityName () {
            String mEntityName = "";

            try {
                if (Constants.Entity_Set != null && Constants.Entity_Set.size() > 0) {

                    if (Constants.Entity_Set != null && !Constants.Entity_Set.isEmpty()) {
                        Iterator itr = Constants.Entity_Set.iterator();
                        while (itr.hasNext()) {
                            if (mEntityName.length() == 0) {
                                mEntityName = mEntityName + itr.next().toString();
                            } else {
                                mEntityName = mEntityName + "," + itr.next().toString();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                mEntityName = "";
            }

            return mEntityName;
        }

        public static void removeDeviceDocNoFromSharedPref (Context context, String
        createType, String refDocNo, SharedPreferences sharedPreferences,boolean isRemoveAllData){
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

        public static ArrayList<String> removeDataValtFromSharedPref (Context context, String
        createType, String refDocNo,boolean isRemoveAllData){
            ArrayList<String> dataValtCollectionName = new ArrayList<>();
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);
            if (createType.equalsIgnoreCase(Constants.SecondarySOCreate) || isRemoveAllData) {
                Constants.removeDeviceDocNoFromSharedPref(context, Constants.SecondarySOCreate, refDocNo, sharedPreferences, isRemoveAllData);
                if (!isRemoveAllData) {
                    dataValtCollectionName.addAll(SyncUtils.getSOsCollection());
                    return dataValtCollectionName;
                }
            }

            if (createType.equalsIgnoreCase(Constants.OUTLET_LOCKED) && isRemoveAllData) {
                Constants.removeDeviceDocNoFromSharedPref(context, Constants.OUTLET_LOCKED, refDocNo, sharedPreferences, isRemoveAllData);

            }
            if (createType.equalsIgnoreCase(Constants.Feedbacks) || isRemoveAllData) {
                Constants.removeDeviceDocNoFromSharedPref(context, Constants.Feedbacks, refDocNo, sharedPreferences, isRemoveAllData);
                if (!isRemoveAllData) {
                    dataValtCollectionName.addAll(SyncUtils.getFeedBack());
                    return dataValtCollectionName;
                }
            }
            if (createType.equalsIgnoreCase(Constants.FinancialPostings) || isRemoveAllData) {
                Constants.removeDeviceDocNoFromSharedPref(context, Constants.FinancialPostings, refDocNo, sharedPreferences, isRemoveAllData);
                if (!isRemoveAllData) {
                    dataValtCollectionName.addAll(SyncUtils.getFIPCollection());
                    dataValtCollectionName.addAll(SyncUtils.getInvoice());
                    return dataValtCollectionName;
                }
            }
            if (createType.equalsIgnoreCase(Constants.SampleDisbursement) || isRemoveAllData) {
                Constants.removeDeviceDocNoFromSharedPref(context, Constants.SampleDisbursement, refDocNo, sharedPreferences, isRemoveAllData);
                if (!isRemoveAllData) {
                    dataValtCollectionName.addAll(SyncUtils.getInvoice());
                    return dataValtCollectionName;
                }
            }
            if (createType.equalsIgnoreCase(Constants.ROList) || isRemoveAllData) {
                Constants.removeDeviceDocNoFromSharedPref(context, Constants.ROList, refDocNo, sharedPreferences, isRemoveAllData);
                if (!isRemoveAllData) {
                    dataValtCollectionName.addAll(SyncUtils.getROsCollection());
                    return dataValtCollectionName;
                }
            }
            if (createType.equalsIgnoreCase(Constants.CPList) || isRemoveAllData) {
                Constants.removeDeviceDocNoFromSharedPref(context, Constants.CPList, refDocNo, sharedPreferences, isRemoveAllData);
                if (!isRemoveAllData) {
                    dataValtCollectionName.addAll(SyncUtils.getFOS());
                    return dataValtCollectionName;
                }
            }
            if (createType.equalsIgnoreCase(Constants.ReturnOrderCreate) || isRemoveAllData) {
                Constants.removeDeviceDocNoFromSharedPref(context, Constants.ReturnOrderCreate, refDocNo, sharedPreferences, isRemoveAllData);
                if (!isRemoveAllData) {
                    dataValtCollectionName.addAll(SyncUtils.getROsCollection());
                    return dataValtCollectionName;
                }
            }
            if (createType.equalsIgnoreCase(Constants.Expenses) || isRemoveAllData) {
                Constants.removeDeviceDocNoFromSharedPref(context, Constants.Expenses, refDocNo, sharedPreferences, isRemoveAllData);
                if (!isRemoveAllData) {
                    dataValtCollectionName.addAll(SyncUtils.getExpenseListCollection());
                    return dataValtCollectionName;
                }
            }
            if (createType.equalsIgnoreCase(Constants.MaterialDocs) || isRemoveAllData) {
                Constants.removeDeviceDocNoFromSharedPref(context, Constants.MaterialDocs, refDocNo, sharedPreferences, isRemoveAllData);
                if (!isRemoveAllData) {
                    dataValtCollectionName.addAll(SyncUtils.getGRNListCollection());
                    return dataValtCollectionName;
                }
            }
            return dataValtCollectionName;
        }

        public static void updateLastSyncTimeToTable (Context
        mContext, ArrayList < String > alAssignColl){
            SyncUtils.updatingSyncTime(mContext, alAssignColl);
        }

        public static final void createTable (SQLiteDatabase db, String tableName, String clumsname)
        {
            try {
                String sql = Constants.create_table + tableName
                        + " ( " + clumsname + ", Status text );";
                Log.d(Constants.EventsData, Constants.on_Create + sql);
                db.execSQL(sql);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static final void deleteTable (SQLiteDatabase db, String tableName){
            try {
                String delSql = Constants.delete_from + tableName;
                db.execSQL(delSql);

            } catch (Exception e) {
                System.out.println("createTableKey(EventDataSqlHelper): " + e.getMessage());
            }
        }

        public static final void insertHistoryDB (SQLiteDatabase db, String tblName, String
        clmname, String value){
            String sql = "INSERT INTO " + tblName + "( " + clmname + ") VALUES('"
                    + value + "') ;";
            db.execSQL(sql);
        }

        public static final void updateStatus (SQLiteDatabase db, String tblName, String
        clmname, String value, String inspectionLot){
            String sql = "UPDATE " + tblName + " SET  " + clmname + "='" + value
                    + "' Where Collections = '" + inspectionLot + "';";
            db.execSQL(sql);
        }

        public static final void createDB (SQLiteDatabase db){
            String sql = "create table if not exists "
                    + Constants.DATABASE_REGISTRATION_TABLE
                    + "( username  text, password   text,repassword text,themeId text,mainView text);";
            Log.d("EventsData", "onCreate: " + sql);
            db.execSQL(sql);
        }

        public static final void setIconVisibiltyReports (SharedPreferences sharedPreferences,
        int[] mArrIntReportsOriginalStatus){
            String sharedVal = sharedPreferences.getString(isSecondarySalesListKey, "");
            if (sharedVal.equalsIgnoreCase(isSecondarySalesListTcode)) {
                mArrIntReportsOriginalStatus[0] = 1;
            } else {
                mArrIntReportsOriginalStatus[0] = 1;
            }
            sharedVal = sharedPreferences.getString(isReturnOrderListKey, "");
            if (sharedVal.equalsIgnoreCase(isReturnOrderListTcode)) {
                mArrIntReportsOriginalStatus[1] = 1;
            } else {
                mArrIntReportsOriginalStatus[1] = 0;
            }

            sharedVal = sharedPreferences.getString(isSecondaryInvoiceListKey, "");
            if (sharedVal.equalsIgnoreCase(isSecondaryInvoiceListTcode)) {
                mArrIntReportsOriginalStatus[2] = 1;
            } else {
                mArrIntReportsOriginalStatus[2] = 0;
            }

            sharedVal = sharedPreferences.getString(isCollListKey, "");
            if (sharedVal.equalsIgnoreCase(isCollListTcode)) {
                mArrIntReportsOriginalStatus[3] = 1;
            } else {
                mArrIntReportsOriginalStatus[3] = 0;
            }
            sharedVal = sharedPreferences.getString(isMerchReviewListKey, "");
            if (sharedVal.equalsIgnoreCase(isMerchReviewListTcode)) {
                mArrIntReportsOriginalStatus[4] = 1;
            } else {
                mArrIntReportsOriginalStatus[4] = 0;
            }

            sharedVal = sharedPreferences.getString(isFeedBackListKey, "");
            if (sharedVal.equalsIgnoreCase(isFeedBackListTcode)) {
                mArrIntReportsOriginalStatus[5] = 1;
            } else {
                mArrIntReportsOriginalStatus[5] = 0;
            }

            sharedVal = sharedPreferences.getString(isOutstandingListKey, "");
            if (sharedVal.equalsIgnoreCase(isOutstandingListTcode)) {
                mArrIntReportsOriginalStatus[6] = 1;
            } else {
                mArrIntReportsOriginalStatus[6] = 0;
            }

            sharedVal = sharedPreferences.getString(isCompetitorListKey, "");
            if (sharedVal.equalsIgnoreCase(isCompetitorListTcode)) {
                mArrIntReportsOriginalStatus[7] = 1;
            } else {
                mArrIntReportsOriginalStatus[7] = 0;
            }

            sharedVal = sharedPreferences.getString(isCustomerComplaintListKey, "");
            if (sharedVal.equalsIgnoreCase(isCustomerComplaintListTcode)) {
                mArrIntReportsOriginalStatus[8] = 1;
            } else {
                mArrIntReportsOriginalStatus[8] = 0;
            }

            sharedVal = sharedPreferences.getString(isRetailerTrendKey, "");
            if (sharedVal.equalsIgnoreCase(isRetailerTrendTcode)) {
                mArrIntReportsOriginalStatus[9] = 1;
            } else {
                mArrIntReportsOriginalStatus[9] = 0;
            }
            sharedVal = sharedPreferences.getString(isSampleDisbursmentListKey, "");

            if (sharedVal.equalsIgnoreCase(isSampleDisbursmentListTcode)) {
                mArrIntReportsOriginalStatus[10] = 1;
            } else {
                mArrIntReportsOriginalStatus[10] = 0;
            }

        }

        public static String getDistNameFromCPDMSDIV (String mStrCPGUID, String mStrSPGUID){
            String spGuid = "";
            try {
                spGuid = OfflineManager.getGuidValueByColumnName(Constants.SalesPersons + "?$select=" + Constants.SPGUID + " ", Constants.SPGUID);
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }

            String selParentName = "";
            try {
                selParentName = OfflineManager.getValueByColumnName(Constants.CPDMSDivisions + "?$select=" + Constants.ParentName + " &$filter="
                        + Constants.CPGUID + " eq guid'" + mStrCPGUID.toUpperCase() + "' and " + Constants.PartnerMgrGUID + " eq guid'" + spGuid.toUpperCase() + "' ", Constants.ParentName);

            } catch (OfflineODataStoreException e) {
                selParentName = "";
                e.printStackTrace();
            }
            return selParentName;
        }

        public static void displayPieChart (String targetPer, PieChart pieChart, Context context,
        float textSize, String mStrValue){
            pieChart.setUsePercentValues(false);
            pieChart.getDescription().setEnabled(false);
            //spacing between graph and margin
            //mChart.setExtraOffsets(5, 10, 5, 5);
            pieChart.setExtraOffsets(-5, -5, -5, -5);

            pieChart.setDragDecelerationFrictionCoef(0.95f);
//        pieChart.setCenterText(Constants.generateCenterSpannableText(ConstantsUtils.decimalZeroBasedOnValue(targetPer) + "%"));
            pieChart.setCenterTextSize(textSize);
            pieChart.setCenterText(Constants.generateCenterSpannableText(mStrValue));

            pieChart.setDrawHoleEnabled(true);
            pieChart.setHoleColor(Color.WHITE);

            pieChart.setTransparentCircleColor(Color.WHITE);
            pieChart.setTransparentCircleAlpha(110);

            pieChart.setHoleRadius(75f);
            pieChart.setTransparentCircleRadius(61f);

            pieChart.setDrawCenterText(true);

            // enable rotation of the chart by touch
            pieChart.setRotationEnabled(false);
            pieChart.setHighlightPerTapEnabled(false);
            Constants.setPieChartData(targetPer, pieChart, context);
            pieChart.animateXY(1500, 1500);
//        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
            // entry label styling
            pieChart.getLegend().setEnabled(false);
            pieChart.setDrawEntryLabels(false);

//        pieChart.getTransformer().prepareMatrixValuePx(chart);
//        pieChart.mat().prepareMatrixOffset(chart);

//        pieChart.getContentRect().set(0, 0, pieChart.getWidth(), pieChart.getHeight());
            pieChart.invalidate();
        }

        public static void setPieChartData (String totalPercent, PieChart pieChart, Context context)
        {

            String remainingPercent = "0";
            try {
                remainingPercent = String.valueOf(100 - Integer.parseInt(totalPercent.split("\\.")[0]));
            } catch (Exception e) {
                e.printStackTrace();
            }
            float flTotalPercent = Float.parseFloat(totalPercent);
            float flRemainingPer = Float.parseFloat(remainingPercent);

            List<PieEntry> entries = new ArrayList<>();
            if (flTotalPercent != 0f)
                entries.add(new PieEntry(flTotalPercent, ""));
            if (flRemainingPer != 0f)
                entries.add(new PieEntry(Float.parseFloat(remainingPercent), ""));

            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setSliceSpace(0f);

            //pie chart background color start
            ArrayList<Integer> colors = new ArrayList<Integer>();
            if (flTotalPercent != 0f)
                colors.add(ColorTemplate.rgb(String.format("#%06X", (0xFFFFFF & ContextCompat.getColor(context, R.color.primaryColor)))));
            if (flRemainingPer != 0f)
                colors.add(Color.rgb(238, 238, 238));
            dataSet.setColors(colors);
            //pie chart background color end
            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setDrawValues(false);
            pieChart.setData(data);
//        pieChart.highlightValue(0, 0, false);
            pieChart.highlightValue(null);
        }

        public static SpannableString generateCenterSpannableText (String totalPercent){

            SpannableString s = new SpannableString(totalPercent);
            s.setSpan(new RelativeSizeSpan(2.5f), 0, s.length(), 0);
            return s;
        }

        public static String getTotalOrderValueByCurrentMonth (String mStrFirstDateMonth, String
        cpQry, String mStrCPDMSDIVQry){

            double totalOrderVal = 0.0;

       /* String mStrOrderVal = "0.0";
        try {
            if(cpQry.equalsIgnoreCase("")){
                mStrOrderVal = OfflineManager.getTotalSumByCondition("" + Constants.SSInvoices +
                        "?$select=" + Constants.NetAmount + " &$filter=" + Constants.InvoiceDate + " ge datetime'" + mStrFirstDateMonth + "' and "+Constants.InvoiceDate+" lt datetime'"+ UtilConstants.getNewDate() +"' and "+mStrCPDMSDIVQry+" ", Constants.NetAmount);
            }else{
                mStrOrderVal = OfflineManager.getTotalSumByCondition("" + Constants.SSInvoices +
                        "?$select=" + Constants.NetAmount + " &$filter=" + Constants.InvoiceDate + " ge datetime'" + mStrFirstDateMonth + "' and "+Constants.InvoiceDate+" lt datetime'"+ UtilConstants.getNewDate() +"' and ("+cpQry+") and "+mStrCPDMSDIVQry+" ", Constants.NetAmount);
            }


        } catch (OfflineODataStoreException e) {
            e.printStackTrace();
        }
        double mdouDevOrderVal = 0.0;

        totalOrderVal = Double.parseDouble(mStrOrderVal) + mdouDevOrderVal;*/

            return totalOrderVal + "";
        }

        public static String getLastDateOfCurrentMonth () {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
            Date lastDayOfMonth = cal.getTime();
            String currentDateTimeString1 = (String) DateFormat.format("yyyy-MM-dd", lastDayOfMonth);
            return getTimeformat2(currentDateTimeString1, null);
        }

        public static String getTimeformat2 (String date, String time){
            String datefrt = "";
            datefrt = "00:00:00";
            String currentDateTimeString = date + "T" + datefrt;
            return currentDateTimeString;
        }
        public static String getDatebynumberofDays (String nodays){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar dateCal = Calendar.getInstance();
            dateCal.setTime(new Date());
            dateCal.add(Calendar.DATE,-Integer.parseInt(nodays));
            String datefrt = "";
            final String formattedDate = dateFormat.format(dateCal.getTime());
            datefrt = "00:00:00";
//            final LocalDate date = LocalDate.now();
//            final LocalDate dateMinusDays = date.minusDays(Integer.parseInt(nodays));
//            final String formattedDate = dateMinusDays.format(DateTimeFormatter.ISO_LOCAL_DATE);

            String currentDateTimeString = formattedDate + "T" + datefrt;
            return currentDateTimeString;
        }

        public static String getFirstDateOfCurrentMonth () {
            String dateFormat = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
            return simpleDateFormat.format(cal.getTime()) + "T00:00:00";
        }

        public static String getSPGUID (String columnName){
            String spGuid = "";
            try {
                spGuid = OfflineManager.getGuidValueByColumnName(Constants.UserSalesPersons + "?$select=" + columnName, columnName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return spGuid;
            //  return "00505635-001e-1ed7-a1af-00ae1c55ee43";
        }

        public static String getSOOrderType () {
            String ordettype = "";
            try {
                ordettype = OfflineManager.getValueByColumnName(Constants.ValueHelps + "?$top=1 &$select=" + Constants.ID + " &$filter=" + Constants.EntityType + " eq 'SSSO' and  " +
                        "" + Constants.PropName + " eq 'OrderType' and  " + Constants.ParentID + " eq '000010' ", Constants.ID);
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }
            return ordettype;
        }

        public static String getReturnOrderType () {
            String ordettype = "";
            try {
                ordettype = OfflineManager.getValueByColumnName(Constants.ValueHelps + "?$top=1 &$select=" + Constants.ID + " &$filter=" + Constants.EntityType + " eq 'SSRO' and  " +
                        "" + Constants.PropName + " eq 'OrderType' and  " + Constants.ParentID + " eq '000020' ", Constants.ID);
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }
            return ordettype;
        }

        public static ArrayList<RetailerBean> alTodayBeatRet = new ArrayList<>();
        public static HashMap<String, String> mMapCPSeqNo = new HashMap<>();
        public static HashSet<String> mSetTodayRouteSch = new HashSet<>();
        public static String SS_INV_RET_QRY = "";
        public static ArrayList<String> alRetailers = new ArrayList<>();
        public static ArrayList<String> alRetailersGuid = new ArrayList<>();
        public static ArrayList<String> alRetailersGuid36 = new ArrayList<>();
        public static ArrayList<String> alRetailersCount = new ArrayList<>();

        public static String getVisitTargetForToday () {
            alTodayBeatRet.clear();
            String count = "0";
            ArrayList<RetailerBean> alRetailerList = new ArrayList<>();
            alRetailerList = getTodaysBeatRetailers();
            alTodayBeatRet.addAll(alRetailerList);
            count = (alRetailerList.size() > 0) ? String.valueOf(alRetailerList.size()) : "0";
            return count;
        }

        public static ArrayList<RetailerBean> getTodaysBeatRetailers () {
            ArrayList<RetailerBean> alRetailerList = new ArrayList<>();
            ArrayList<RetailerBean> alRSCHList = getTodayRoutePlan();
            if (alRSCHList != null && alRSCHList.size() > 0) {
                String mCPGuidQry = getCPFromRouteSchPlan(alRSCHList);
                try {
                    if (!mCPGuidQry.equalsIgnoreCase("")) {
                        List<RetailerBean> listRetailers = OfflineManager.getTodayBeatRetailer(mCPGuidQry, Constants.mMapCPSeqNo);
                        alRetailerList = (ArrayList<RetailerBean>) listRetailers;
                    }
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }
            }
            return alRetailerList;

        }

        public static ArrayList<RetailerBean> getTodayRoutePlan () {
            String routeQry = Constants.RoutePlans + "?$filter=" + Constants.VisitDate + " eq datetime'" + UtilConstants.getNewDate() + "'";
            ArrayList<RetailerBean> alRSCHList = null;
            try {
                alRSCHList = OfflineManager.getTodayRoutes1(routeQry, "");
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }

            return alRSCHList;
        }

        public static String getCPFromRouteSchPlan (ArrayList < RetailerBean > alRouteList) {
            String mCPGuidQry = "", qryForTodaysBeat = "";
            if (alRouteList != null && alRouteList.size() > 0) {
                String mRSCHQry = "";
                // Routescope ID will be same for all the routes planned for the day hence first record scope is used to decide
                String routeSchopeVal = alRouteList.get(0).getRoutSchScope();
                if (alRouteList.size() > 1) {

                    if (routeSchopeVal.equalsIgnoreCase("000001")) {
                        for (RetailerBean routeList : alRouteList) {
                            if (mRSCHQry.length() == 0)
                                mRSCHQry += " guid'" + routeList.getRschGuid().toUpperCase() + "'";
                            else
                                mRSCHQry += " or " + Constants.RouteSchGUID + " eq guid'" + routeList.getRschGuid().toUpperCase() + "'";

                        }

                    } else if (routeSchopeVal.equalsIgnoreCase("000002")) {
                        // Get the list of retailers from RoutePlans

                    }

                } else {


                    if (routeSchopeVal.equalsIgnoreCase("000001")) {

                        mRSCHQry = "guid'" + alRouteList.get(0).getRschGuid().toUpperCase() + "'";


                    } else if (routeSchopeVal.equalsIgnoreCase("000002")) {
                        // Get the list of retailers from RoutePlans
                    }

                }
                qryForTodaysBeat = Constants.RouteSchedulePlans + "?$filter=(" +
                        Constants.RouteSchGUID + " eq " + mRSCHQry + ") &$orderby=" + Constants.SequenceNo + "";

                try {
                    // Prepare Today's beat Retailer Query
                    mCPGuidQry = OfflineManager.getBeatList(qryForTodaysBeat);

                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }
            }

            return mCPGuidQry;
        }

        /*returns total number of retailers visited(Route plan)*/
        public static String getVisitedRetailerCount () {
            String mTodayBeatVisitCount = "0";
            try {
                if (alTodayBeatRet != null && alTodayBeatRet.size() > 0) {
                    String mVisitQry = Constants.Visits + "?$filter= " + Constants.StartDate + " eq datetime'" + UtilConstants.getNewDate() + "' and " + Constants.ENDDATE + " eq datetime'" + UtilConstants.getNewDate() + "' " + "and (" + Constants.VisitCatID + " eq '" + Constants.str_01 + "' ) and StatusID ne '02'";
                    Set<String> retList = new HashSet<>();
                    try {
                        retList = OfflineManager.getUniqueOutVisitFromVisit(mVisitQry);
                        mTodayBeatVisitCount = retList.size() + "";
                    } catch (Exception e) {
                        mTodayBeatVisitCount = "0";
                    }

                } else {
                    mTodayBeatVisitCount = "0";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mTodayBeatVisitCount;
        }

        public static DmsDivQryBean getDMSDIV (String mStrParentID){
            DmsDivQryBean dmsDivQryBean = new DmsDivQryBean();
            try {
                if (mStrParentID.equalsIgnoreCase("")) {
                    dmsDivQryBean = OfflineManager.getDMSDIVQry(Constants.CPSPRelations + "?$select=DMSDivisionID");
                } else {
                    dmsDivQryBean = OfflineManager.getDMSDIVQry(Constants.CPSPRelations + "?$select=DMSDivisionID &$filter=" + Constants.CPGUID + " eq '" + mStrParentID + "'");
                }
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }
            return dmsDivQryBean;
        }

        public static int getBalanceVisit (ArrayList < RetailerBean > alTodaysRetailers) {
            int mIntBalVisitRet = 0;
            String mStrRetQry = "";
            if (alTodaysRetailers != null && alTodaysRetailers.size() > 0) {
                for (int i = 0; i < alTodaysRetailers.size(); i++) {
                    if (i == 0 && i == alTodaysRetailers.size() - 1) {
                        mStrRetQry = mStrRetQry
                                + "(" + Constants.VisitCPGUID + "%20eq%20'"
                                + alTodaysRetailers.get(i).getCpGuidStringFormat().toUpperCase() + "')";

                    } else if (i == 0) {
                        mStrRetQry = mStrRetQry
                                + "(" + Constants.VisitCPGUID + "%20eq%20'"
                                + alTodaysRetailers.get(i).getCpGuidStringFormat().toUpperCase() + "'";

                    } else if (i == alTodaysRetailers.size() - 1) {
                        mStrRetQry = mStrRetQry
                                + "%20or%20" + Constants.VisitCPGUID + "%20eq%20'"
                                + alTodaysRetailers.get(i).getCpGuidStringFormat().toUpperCase() + "')";
                    } else {
                        mStrRetQry = mStrRetQry
                                + "%20or%20" + Constants.VisitCPGUID + "%20eq%20'"
                                + alTodaysRetailers.get(i).getCpGuidStringFormat().toUpperCase() + "'";
                    }
                }
            }

            if (!mStrRetQry.equalsIgnoreCase("")) {
                String mStrBalVisitQry = Constants.RouteSchedulePlans + "?$filter = " + mStrRetQry + " ";
                try {
                    mIntBalVisitRet = OfflineManager.getBalanceRetVisitRoute(mStrBalVisitQry);
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }
            } else {
                mIntBalVisitRet = 0;
            }

            return mIntBalVisitRet;
        }

        public static String getTotalOrderValue (Context context, String mStrCurrentDate,
                ArrayList < RetailerBean > alTodaysRetailers){

            String mSOOrderType = getSOOrderType();
            double totalOrderVal = 0.0;

            String mStrRetQry = "", ssINVRetQry = "";
            boolean isReadAllRetail = true;
            if (isReadAllRetail) {
                List<ODataEntity> entities = null;
                try {
                    entities = UtilOfflineManager.getEntities(offlineStore, "CPDMSDivisions?$filter=StatusID eq '01' and ApprvlStatusID eq '03' and DMSDivision ne ''");
                    if (entities != null) {
                        int i = 0;
                        int totalSize = entities.size();
                        for (ODataEntity entity : entities) {
                            ODataPropMap properties = entity.getProperties();
                            ODataProperty property = properties.get(Constants.CPNo);
                            String cpNo = (String) property.getValue();
                            property = properties.get(Constants.CPGUID);
                            ODataGuid mCpGuid = null;
                            String cpguid = "";
                            try {
                                mCpGuid = (ODataGuid) property.getValue();
//                            retBean.setCPGUID(mCpGuid.guidAsString36().toUpperCase());
                                cpguid = mCpGuid.guidAsString32().toUpperCase() + "";
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (i == 0 && i == totalSize - 1) {
                                mStrRetQry = mStrRetQry
                                        + "(" + Constants.SoldToId + "%20eq%20'"
                                        + cpNo + "')";

                                ssINVRetQry = ssINVRetQry
                                        + "(" + Constants.SoldToID + "%20eq%20'"
                                        + cpNo + "')";

                            } else if (i == 0) {
                                mStrRetQry = mStrRetQry
                                        + "(" + Constants.SoldToId + "%20eq%20'"
                                        + cpNo + "'";

                                ssINVRetQry = ssINVRetQry
                                        + "(" + Constants.SoldToID + "%20eq%20'"
                                        + cpNo + "'";

                            } else if (i == totalSize - 1) {
                                mStrRetQry = mStrRetQry
                                        + "%20or%20" + Constants.SoldToId + "%20eq%20'"
                                        + cpNo + "')";

                                ssINVRetQry = ssINVRetQry
                                        + "%20or%20" + Constants.SoldToID + "%20eq%20'"
                                        + cpNo + "')";
                            } else {
                                mStrRetQry = mStrRetQry
                                        + "%20or%20" + Constants.SoldToId + "%20eq%20'"
                                        + cpNo + "'";

                                ssINVRetQry = ssINVRetQry
                                        + "%20or%20" + Constants.SoldToID + "%20eq%20'"
                                        + cpNo + "'";
                            }
                            if (!alRetailers.contains(cpNo)) {
                                alRetailers.add(cpNo);
                                alRetailersGuid.add(cpguid);
                            }
                            i++;
                        }
                    }
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }
            } else {
                if (alTodaysRetailers != null && alTodaysRetailers.size() > 0) {
                    for (int i = 0; i < alTodaysRetailers.size(); i++) {
                        if (i == 0 && i == alTodaysRetailers.size() - 1) {
                            mStrRetQry = mStrRetQry
                                    + "(" + Constants.SoldToId + "%20eq%20'"
                                    + alTodaysRetailers.get(i).getCPNo() + "')";

                            ssINVRetQry = ssINVRetQry
                                    + "(" + Constants.SoldToID + "%20eq%20'"
                                    + alTodaysRetailers.get(i).getCPNo() + "')";

                        } else if (i == 0) {
                            mStrRetQry = mStrRetQry
                                    + "(" + Constants.SoldToId + "%20eq%20'"
                                    + alTodaysRetailers.get(i).getCPNo() + "'";

                            ssINVRetQry = ssINVRetQry
                                    + "(" + Constants.SoldToID + "%20eq%20'"
                                    + alTodaysRetailers.get(i).getCPNo() + "'";

                        } else if (i == alTodaysRetailers.size() - 1) {
                            mStrRetQry = mStrRetQry
                                    + "%20or%20" + Constants.SoldToId + "%20eq%20'"
                                    + alTodaysRetailers.get(i).getCPNo() + "')";

                            ssINVRetQry = ssINVRetQry
                                    + "%20or%20" + Constants.SoldToID + "%20eq%20'"
                                    + alTodaysRetailers.get(i).getCPNo() + "')";
                        } else {
                            mStrRetQry = mStrRetQry
                                    + "%20or%20" + Constants.SoldToId + "%20eq%20'"
                                    + alTodaysRetailers.get(i).getCPNo() + "'";

                            ssINVRetQry = ssINVRetQry
                                    + "%20or%20" + Constants.SoldToID + "%20eq%20'"
                                    + alTodaysRetailers.get(i).getCPNo() + "'";
                        }

                        if (!alRetailers.contains(alTodaysRetailers.get(i).getCPNo())) {
                            alRetailers.add(alTodaysRetailers.get(i).getCPNo());
                            alRetailersGuid.add(alTodaysRetailers.get(i).getCpGuidStringFormat());
                            alRetailersGuid36.add(alTodaysRetailers.get(i).getCPGUID().toUpperCase());
                        }
                    }
                }
            }
            Constants.SS_INV_RET_QRY = mStrRetQry;
            String mStrOrderVal = "0.0";
            if (alRetailers.size() > 0) {
                try {
                    if (!mStrRetQry.equalsIgnoreCase("")) {
                        mStrOrderVal = OfflineManager.getTotalSumByCondition("" + Constants.SSSOs +
                                "?$select=" + Constants.NetPrice + " &$filter=" + Constants.OrderDate + " eq datetime'" + mStrCurrentDate + "' and " + Constants.OrderType + " eq '" + mSOOrderType + "' and Status ne '000004' and " + mStrRetQry + " ", Constants.NetPrice);
                    } else {
                        mStrOrderVal = OfflineManager.getTotalSumByCondition("" + Constants.SSSOs +
                                "?$select=" + Constants.NetPrice + " &$filter=" + Constants.OrderDate + " eq datetime'" + mStrCurrentDate + "' and " + Constants.OrderType + " eq '" + mSOOrderType + "' and Status ne '000004'", Constants.NetPrice);
                    }
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }
            }
            double mdouDevOrderVal = 0.0;

            if (alRetailers.size() > 0) {
                try {
                    mdouDevOrderVal = OfflineManager.getDeviceTotalOrderAmt(Constants.SecondarySOCreate, context, mStrCurrentDate, alRetailers);
                } catch (Exception e) {
                    mdouDevOrderVal = 0.0;
                }
            }

            totalOrderVal = Double.parseDouble(mStrOrderVal) + mdouDevOrderVal;

            return totalOrderVal + "";
        }

        public static String getDeviceTLSD (ArrayList < String > alTodaysRetailers, String
        entityType, Context mContext){

            double mDoubleDevTLSD = 0.0;
            if (alTodaysRetailers.size() > 0) {
                try {
                    mDoubleDevTLSD = OfflineManager.getTLSD(entityType, mContext,
                            UtilConstants.getNewDate(), alTodaysRetailers);
                } catch (Exception e) {
                    mDoubleDevTLSD = 0.0;
                    e.printStackTrace();
                }
            }
            return mDoubleDevTLSD + "";

        }

        public static String getDeviceTLSDOffline (String mStrSoldToID){
            String mStrQry = "", mStrOfflineTLSD = "0";
            if (!Constants.SS_INV_RET_QRY.equalsIgnoreCase("")) {
                try {
                    mStrQry = OfflineManager.makeSSSOQry(Constants.SSSOs + "?$filter= " + Constants.OrderDate +
                            " eq datetime'" + UtilConstants.getNewDate() + "' and " + Constants.OrderType + " eq '" + Constants.getSOOrderType() + "' and " + Constants.SS_INV_RET_QRY + " ", Constants.SSSOGuid);
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }
            }

            if (!mStrQry.equalsIgnoreCase("")) {
                try {
                    mStrOfflineTLSD = OfflineManager.getCountTLSDFromDatabase(Constants.SSSoItemDetails + "?$filter=" + Constants.IsfreeGoodsItem + " ne '" + Constants.X + "' and " + mStrQry);
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }
            }


            Double mDouTotalTLSD = Double.parseDouble(mStrOfflineTLSD);

            return mDouTotalTLSD + "";
        }

        public static String getDeviceBillCut (ArrayList < String > alTodaysRetailers, String
        entityType, Context mContext){

            int mIntDevBillCut = 0;
            if (alTodaysRetailers.size() > 0) {
                try {
                    String mQry = Constants.SSSOs + "?$select=" + Constants.SoldToId + " &$filter= " + Constants.OrderDate + " eq datetime'"
                            + UtilConstants.getNewDate() + "' and " + Constants.OrderType + " eq '" + Constants.getSOOrderType() + "' and " + Constants.SS_INV_RET_QRY + " ";
                    mIntDevBillCut = OfflineManager.getUniqueBillCut(entityType, mContext,
                            UtilConstants.getNewDate(), alTodaysRetailers, mQry, Constants.SoldToId);
                } catch (Exception e) {
                    mIntDevBillCut = 0;
                    e.printStackTrace();
                }
            }
            return mIntDevBillCut + "";
        }

        public static void createVisit (Map < String, String > parameterMap, String cpGuid, Context
        context, UIListener listener){

            try {
                Thread.sleep(100);

                GUID guid = GUID.newRandom();

                Hashtable table = new Hashtable();
                //noinspection unchecked
                table.put(Constants.CPNo, UtilConstants.removeLeadingZeros(parameterMap.get(Constants.CPNo)));

                table.put(Constants.CPName, parameterMap.get(Constants.CPName));
                //noinspection unchecked
                table.put(Constants.STARTDATE, UtilConstants.getNewDate());

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

                table.put(Constants.STARTTIME, oDataDuration);
                try {
                    //noinspection unchecked
                    table.put(Constants.StartLat, BigDecimal.valueOf(UtilConstants.round(UtilConstants.latitude, 12)));
                    //noinspection unchecked
                    table.put(Constants.StartLong, BigDecimal.valueOf(UtilConstants.round(UtilConstants.longitude, 12)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //noinspection unchecked
                table.put(Constants.EndLat, "");
                //noinspection unchecked
                table.put(Constants.EndLong, "");
                //noinspection unchecked
                table.put(Constants.ENDDATE, "");
                //noinspection unchecked
                table.put(Constants.ENDTIME, "");
                //noinspection unchecked
                table.put(Constants.VISITKEY, guid.toString().toUpperCase());

                table.put(Constants.StatusID, parameterMap.get(Constants.StatusID));

                table.put(Constants.VisitCatID, parameterMap.get(Constants.VisitCatID));

                table.put(Constants.CPTypeID, parameterMap.get(Constants.CPTypeID));

                if (parameterMap.get(Constants.PlannedDate) != null) {
                    table.put(Constants.PlannedDate, parameterMap.get(Constants.PlannedDate));
                } else {
                    table.put(Constants.PlannedDate, "");
                }

                if (parameterMap.get(Constants.PlannedStartTime) != null) {
                    ODataDuration startDuration = UtilConstants.getTimeAsODataDuration(parameterMap.get(Constants.PlannedStartTime));
                    table.put(Constants.PlannedStartTime, startDuration);
                } else {
                    table.put(Constants.PlannedStartTime, "");
                }

                if (parameterMap.get(Constants.PlannedEndTime) != null) {
                    ODataDuration endDuration = UtilConstants.getTimeAsODataDuration(parameterMap.get(Constants.PlannedEndTime));
                    table.put(Constants.PlannedEndTime, endDuration);
                } else {
                    table.put(Constants.PlannedEndTime, "");
                }

                //noinspection unchecked
                if (parameterMap.get(Constants.Remarks) != null) {
                    table.put(Constants.Remarks, parameterMap.get(Constants.Remarks));
                }

                table.put(Constants.VisitTypeID, parameterMap.get(Constants.VisitTypeID));
                table.put(Constants.VisitTypeDesc, parameterMap.get(Constants.VisitTypeDesc));


                if (parameterMap.get(Constants.VisitDate) != null) {
                    table.put(Constants.VisitDate, UtilConstants.getNewDateTimeFormat());
                } else {
                    table.put(Constants.VisitDate, UtilConstants.getNewDateTimeFormat());
                }

                table.put(Constants.CPGUID, cpGuid.toUpperCase());

//            String[][] mArraySPValues = getSPValesFromCPDMSDivisionByCPGUIDAndDMSDivision(cpGuid.guidAsString36().toUpperCase());
//
//            try {
//                table.put(Constants.SPGUID, mArraySPValues[4][0].toUpperCase());
//            } catch (Exception e) {
                table.put(Constants.SPGUID, Constants.getSPGUID());
//            }

                SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);

//            int sharedVal = sharedPreferences.getInt("VisitSeqId", 0);

                String loginIdVal = sharedPreferences.getString(Constants.username, "");
                //noinspection unchecked
                table.put(Constants.LOGINID, loginIdVal);

                try {
                    table.put(Constants.VisitSeq, parameterMap.get(Constants.VisitSeq));
                } catch (Exception e) {
                    table.put(Constants.VisitSeq, "");
                    e.printStackTrace();
                }

           /* sharedVal++;

            SharedPreferences sharedPreferencesVal = context.getSharedPreferences(Constants.PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPreferencesVal.edit();
            editor.putInt(Constants.VisitSeqId, sharedVal);
            editor.commit();*/


                String mStrRoutePlanKey = Constants.Route_Plan_Key;
                if (!mStrRoutePlanKey.equalsIgnoreCase("")) {
                    String mStrRouteGuidFormat = CharBuffer.join9(StringFunction.substring(mStrRoutePlanKey, 0, 8), "-", StringFunction.substring(mStrRoutePlanKey, 8, 12), "-", StringFunction.substring(mStrRoutePlanKey, 12, 16), "-", StringFunction.substring(mStrRoutePlanKey, 16, 20), "-", StringFunction.substring(mStrRoutePlanKey, 20, 32));
                    //noinspection unchecked
                    table.put(Constants.ROUTEPLANKEY, mStrRouteGuidFormat.toUpperCase());
                } else {
                    String mStrRouteKey = getRouteNo(cpGuid);
                    if (mStrRouteKey.equalsIgnoreCase("")) {
                        table.put(Constants.ROUTEPLANKEY, "");
                    } else {
                        String mStrRouteGuidFormat = CharBuffer.join9(StringFunction.substring(mStrRouteKey, 0, 8), "-", StringFunction.substring(mStrRouteKey, 8, 12), "-", StringFunction.substring(mStrRouteKey, 12, 16), "-", StringFunction.substring(mStrRouteKey, 16, 20), "-", StringFunction.substring(mStrRouteKey, 20, 32));
                        table.put(Constants.ROUTEPLANKEY, mStrRouteGuidFormat.toUpperCase());
                    }

                }

                try {
                    //noinspection unchecked
                    OfflineManager.createVisit(table, listener, context);
                } catch (OfflineODataStoreException e) {
                    LogManager.writeLogError(Constants.error_txt + e.getMessage());
                }
            } catch (InterruptedException e) {
                LogManager.writeLogError(Constants.error_txt + e.getMessage());
            }


        }

        public static String getRouteNo (String mCpGuid){

            String mStrRouteKey = "";
            String qryStr = Constants.RouteSchedulePlans + "?$filter=" + Constants.VisitCPGUID + " eq '" + mCpGuid.toUpperCase() + "' ";
            try {
                mStrRouteKey = OfflineManager.getRoutePlanKeyNew(qryStr);

            } catch (OfflineODataStoreException e) {
                mStrRouteKey = "";
                e.printStackTrace();
            }
            return mStrRouteKey;
        }

        public static void printLog (String message){
            Log.d("OnlineStore", "error : " + message);
            LogManager.writeLogError("error : " + message);
        }

        public static void printLogInfo (String message){
            Log.d("OnlineStore", "info : " + message);
            LogManager.writeLogInfo("info : " + message);
        }

        public static InputFilter getNumberAlphabetOnly () {
            InputFilter filter = new InputFilter() {

                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                    if (source.equals("")) { // for backspace
                        return source;
                    }
                    if (source.toString().matches("[a-zA-Z0-9 ]*")) //put your constraints here
                    {
                        return source;
                    }
                    return "";
                }
            };
            return filter;
        }

        public static InputFilter getNumberAlphabet () {
            InputFilter filter = new InputFilter() {

                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                    if (source.equals("")) { // for backspace
                        return source;
                    }
                    if (source.toString().matches("[a-zA-Z0-9]*")) //put your constraints here
                    {
                        return source;
                    }
                    return "";
                }
            };
            return filter;
        }
        public static InputFilter getNumberOnly () {
            InputFilter filter = new InputFilter() {

                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                    if (source.equals("")) { // for backspace
                        return source;
                    }
                    if (source.toString().matches("[0-9]*")) //put your constraints here
                    {
                        return source;
                    }
                    return "";
                }
            };
            return filter;
        }
        public static InputFilter getNumberAlphabetCaps () {
            InputFilter filter = new InputFilter() {

                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

                    if (source.equals("")) { // for backspace
                        return source;
                    }
                    if (source.toString().matches("[A-Z0-9]*")) //put your constraints here
                    {
                        return source;
                    }
                    return "";
                }
            };
            return filter;
        }
        public static InputFilter[] getAlphabetOnly ( int maxLength){
            InputFilter[] FilterArray = new InputFilter[2];
            FilterArray[0] = new InputFilter.LengthFilter(maxLength);
            FilterArray[1] = Constants.getNumberAlphabetOnly();
            return FilterArray;
        }
        public static void saveDeviceDocNoToSharedPref (Context context, String createType, String
        refDocNo){
            Set<String> set = new HashSet<>();
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);
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

        public static void onVisitActivityUpdate (Context mContext, String mStrBundleCPGUID32,
                String visitActRefID, String vistActType,
                String visitActTypeDesc, ODataDuration mStartTimeDuration){
            try {
                //========>Start VisitActivity
                Hashtable visitActivityTable = new Hashtable();
                String mStrSPGUID = Constants.getSPGUID();
                String getVisitGuidQry = Constants.Visits + "?$filter=EndDate eq datetime'" + UtilConstants.getNewDate() + "' and CPGUID eq '" + mStrBundleCPGUID32.toUpperCase() + "' " +
                        "and StartDate eq datetime'" + UtilConstants.getNewDate() + "' and " + Constants.SPGUID + " eq guid'" + mStrSPGUID + "'";
                ODataGuid mGuidVisitId = null;
                try {
                    mGuidVisitId = OfflineManager.getVisitDetails(getVisitGuidQry);
                } catch (OfflineODataStoreException e) {
                    LogManager.writeLogError(Constants.error_txt + e.getMessage());
                }
                SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);

                String loginIdVal = sharedPreferences.getString(Constants.username, "");
                if (mGuidVisitId != null) {
                    GUID mStrGuide = GUID.newRandom();
                    visitActivityTable.put(Constants.VisitActivityGUID, mStrGuide.toString());
                    visitActivityTable.put(Constants.LOGINID, loginIdVal);
                    visitActivityTable.put(Constants.VisitGUID, mGuidVisitId.guidAsString36());
                    visitActivityTable.put(Constants.ActivityType, vistActType);
                    visitActivityTable.put(Constants.ActivityTypeDesc, visitActTypeDesc);
                    visitActivityTable.put(Constants.ActivityRefID, visitActRefID);
                    try {
                        visitActivityTable.put(Constants.Latitude, BigDecimal.valueOf(UtilConstants.round(UtilConstants.latitude, 12)));
                        visitActivityTable.put(Constants.Longitude, BigDecimal.valueOf(UtilConstants.round(UtilConstants.longitude, 12)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (mStartTimeDuration != null) {
                        visitActivityTable.put(Constants.StartTime, mStartTimeDuration);
                    }
                    visitActivityTable.put(Constants.EndTime, UtilConstants.getOdataDuration());

                    try {
                        OfflineManager.createVisitActivity(visitActivityTable);
                    } catch (OfflineODataStoreException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //========>End VisitActivity
        }

        public static String[][] getDistributorsByCPGUID (Context mContext, String mStrCPGUID){
            String mDBStkType = Constants.getName(Constants.ConfigTypsetTypeValues, Constants.TypeValue, Constants.Types, Constants.DSTSTKVIEW);
            if (TextUtils.isEmpty(mDBStkType)) {
                mDBStkType = "01";
            }
            String spGuid = "";
            String qryStr = "";
            String[][] mArrayDistributors = null;

            try {
                String mStrConfigTypeQry = Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Types + " eq '" + Constants.DSTSTKVIEW + "'";
            /*if (OfflineManager.getVisitStatusForCustomer(mStrConfigTypeQry)) {
                if(mDBStkType.equalsIgnoreCase(Constants.str_01)){
                    try {
                        spGuid = OfflineManager.getGuidValueByColumnName(Constants.SalesPersons + "?$select=" + Constants.SPGUID+" ", Constants.SPGUID);
                    } catch (OfflineODataStoreException e) {
                        e.printStackTrace();
                    }
                    if (ConstantsUtils.getRollInformation(mContext).equalsIgnoreCase(ConstantsUtils.ROLLID_DSR_06)) {
                        qryStr = Constants.CPDMSDivisions + "?$filter=" + Constants.CPGUID + " eq guid'" + mStrCPGUID.toUpperCase() + "' and " + Constants.PartnerMgrGUID + " eq guid'" + spGuid.toUpperCase() + "'";
                    }else {
                        qryStr = Constants.CPDMSDivisions + "?$filter=" + Constants.CPGUID + " eq guid'" + mStrCPGUID.toUpperCase() + "'";
                    }
                }else{
                    qryStr = Constants.ChannelPartners + "?$filter=" + Constants.CPGUID + " eq guid'" + mStrCPGUID.toUpperCase() + "' ";
                }
            }else{
                mDBStkType = "";
                qryStr = Constants.ChannelPartners + "?$filter=" + Constants.CPGUID + " eq guid'" + mStrCPGUID.toUpperCase() + "' ";
            }*/
                try {
                    spGuid = OfflineManager.getGuidValueByColumnName(Constants.SalesPersons + "?$select=" + Constants.SPGUID + " ", Constants.SPGUID);
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }
                qryStr = Constants.CPDMSDivisions + "?$filter=" + Constants.CPGUID + " eq guid'" + mStrCPGUID.toUpperCase() + "'";
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                mArrayDistributors = OfflineManager.getDistributorListByCPGUID(qryStr);

            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }

            if (mArrayDistributors == null) {
                mArrayDistributors = new String[11][1];
                mArrayDistributors[0][0] = "";
                mArrayDistributors[1][0] = "";
                mArrayDistributors[2][0] = "";
                mArrayDistributors[3][0] = "";
                mArrayDistributors[4][0] = "";
                mArrayDistributors[5][0] = "";
                mArrayDistributors[6][0] = "";
                mArrayDistributors[7][0] = "";
                mArrayDistributors[8][0] = "";
                mArrayDistributors[9][0] = "";
                mArrayDistributors[10][0] = "";
            } else {
                try {
                    if (mArrayDistributors[4][0] != null) {

                    }
                } catch (Exception e) {
                    mArrayDistributors = new String[11][1];
                    mArrayDistributors[0][0] = "";
                    mArrayDistributors[1][0] = "";
                    mArrayDistributors[2][0] = "";
                    mArrayDistributors[3][0] = "";
                    mArrayDistributors[4][0] = "";
                    mArrayDistributors[5][0] = "";
                    mArrayDistributors[6][0] = "";
                    mArrayDistributors[7][0] = "";
                    mArrayDistributors[8][0] = "";
                    mArrayDistributors[9][0] = "";
                    mArrayDistributors[10][0] = "";
                }
            }

            return mArrayDistributors;
        }
        public static String[][] getSPValesFromCPDMSDivisionByCPGUIDAndDMSDivision (String
        mStrCPGUID, Context mContext){
            String spGuid = Constants.getSPGUID(Constants.SPGUID);
            String selCPDMSDIV = "";
            try {
                if (ConstantsUtils.getRollInformation(mContext).equalsIgnoreCase(ConstantsUtils.ROLLID_DSR_06)) {
                    selCPDMSDIV = OfflineManager.getValueByColumnName(Constants.CPDMSDivisions + "?$select=" + Constants.DMSDivision + " &$filter="
                            + Constants.CPGUID + " eq guid'" + mStrCPGUID.toUpperCase() + "' and " + Constants.PartnerMgrGUID + " eq guid'" + spGuid.toUpperCase() + "' ", Constants.DMSDivision);
                } else {
                    selCPDMSDIV = OfflineManager.getValueByColumnName(Constants.CPDMSDivisions + "?$select=" + Constants.DMSDivision + " &$filter="
                            + Constants.CPGUID + " eq guid'" + mStrCPGUID.toUpperCase() + "' ", Constants.DMSDivision);
                }
//            selCPDMSDIV = OfflineManager.getValueByColumnName(Constants.CPDMSDivisions + "?$select=" + Constants.DMSDivision + " &$filter="
//                    + Constants.CPGUID + " eq guid'" + mStrCPGUID.toUpperCase() + "' and "+Constants.PartnerMgrGUID+" eq guid'"+spGuid.toUpperCase()+"' ", Constants.DMSDivision);


            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }

            String[][] mArraySPValues = null;
            String qryStr = "";
            if (ConstantsUtils.getRollInformation(mContext).equalsIgnoreCase(ConstantsUtils.ROLLID_DSR_06)) {
                qryStr = Constants.CPDMSDivisions + "?$filter=" + Constants.CPGUID + " eq guid'" + mStrCPGUID.toUpperCase() + "' and "
                        + Constants.DMSDivision + " eq '" + selCPDMSDIV + "' and " + Constants.PartnerMgrGUID + " eq guid'" + spGuid.toUpperCase() + "'";
            } else {
                qryStr = Constants.CPDMSDivisions + "?$filter=" + Constants.CPGUID + " eq guid'" + mStrCPGUID.toUpperCase() + "' and "
                        + Constants.DMSDivision + " eq '" + selCPDMSDIV + "' ";
            }
//        qryStr = Constants.CPDMSDivisions + "?$filter=" + Constants.CPGUID + " eq guid'" + mStrCPGUID.toUpperCase() + "' and "
//                + Constants.DMSDivision + " eq '" + selCPDMSDIV + "' and "+Constants.PartnerMgrGUID+" eq guid'"+spGuid.toUpperCase()+"'";
            try {
                mArraySPValues = OfflineManager.getSPValuesByCPGUIDAndDMSDivision(qryStr);

            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }


            if (mArraySPValues == null) {
                mArraySPValues = new String[12][1];
                mArraySPValues[0][0] = "";
                mArraySPValues[1][0] = "";
                mArraySPValues[2][0] = "";
                mArraySPValues[3][0] = "";
                mArraySPValues[4][0] = "";
                mArraySPValues[5][0] = "";
                mArraySPValues[6][0] = "";
                mArraySPValues[7][0] = "";
                mArraySPValues[8][0] = "";
                mArraySPValues[9][0] = "";
                mArraySPValues[10][0] = "";
                mArraySPValues[11][0] = "";
            } else {
                try {
                    if (mArraySPValues[4][0] != null) {

                    }
                } catch (Exception e) {
                    mArraySPValues = new String[12][1];
                    mArraySPValues[0][0] = "";
                    mArraySPValues[1][0] = "";
                    mArraySPValues[2][0] = "";
                    mArraySPValues[3][0] = "";
                    mArraySPValues[4][0] = "";
                    mArraySPValues[5][0] = "";
                    mArraySPValues[6][0] = "";
                    mArraySPValues[7][0] = "";
                    mArraySPValues[8][0] = "";
                    mArraySPValues[9][0] = "";
                    mArraySPValues[10][0] = "";
                    mArraySPValues[11][0] = "";
                }
            }

            return mArraySPValues;
        }
        public static String getName (String collName, String columnName, String
        whereColumnn, String whereColval){
            String colmnVal = "";
            try {
                colmnVal = OfflineManager.getValueByColumnName(collName + "?$select=" + columnName + " &$filter = " + whereColumnn + " eq '" + whereColval + "'", columnName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return colmnVal;
        }

        public static void displayMsgINet ( int errCode, Context context){
            if (errCode == 4) {
                UtilConstants.showAlert(context.getString(R.string.auth_fail_plz_contact_admin, Constants.UnAuthorized_Error_Code + ""), context);
            } else if (errCode == 3) {
                UtilConstants.showAlert(context.getString(R.string.data_conn_lost_during_sync_error_code, Constants.Network_Error_Code + ""), context);
            } else {
                UtilConstants.showAlert(context.getString(R.string.data_conn_lost_during_sync_error_code, Constants.Network_Error_Code + ""), context);
            }
        }

        public static JSONObject getFeedbackHeaderValuesFromJsonObject (JSONObject
        fetchJsonHeaderObject){
            JSONObject dbHeadTable = new JSONObject();
            try {
                //noinspection unchecked
                dbHeadTable.put(Constants.FeebackGUID, fetchJsonHeaderObject.getString(Constants.FeebackGUID));
                //noinspection unchecked
                dbHeadTable.put(Constants.Remarks, fetchJsonHeaderObject.getString(Constants.Remarks));
                //noinspection unchecked
                dbHeadTable.put(Constants.CPNo, fetchJsonHeaderObject.getString(Constants.CPNo));
                //noinspection unchecked
                dbHeadTable.put(Constants.CPGUID, fetchJsonHeaderObject.getString(Constants.CPGUID));

                //noinspection unchecked
                dbHeadTable.put(Constants.FeedbackType, fetchJsonHeaderObject.getString(Constants.FeedbackType));
                dbHeadTable.put(Constants.FeedbackTypeDesc, fetchJsonHeaderObject.getString(Constants.FeedbackTypeDesc));

//            dbHeadTable.put(Constants.LOGINID, fetchJsonHeaderObject.getString(Constants.LOGINID));


                dbHeadTable.put(Constants.CPTypeID, fetchJsonHeaderObject.getString(Constants.CPTypeID));

                dbHeadTable.put(Constants.SPGUID, fetchJsonHeaderObject.getString(Constants.SPGUID));

                dbHeadTable.put(Constants.SPNo, fetchJsonHeaderObject.getString(Constants.SPNo));

                dbHeadTable.put(Constants.CreatedOn, fetchJsonHeaderObject.getString(Constants.CreatedOn));

                dbHeadTable.put(Constants.CreatedAt, fetchJsonHeaderObject.getString(Constants.CreatedAt));
                dbHeadTable.put(Constants.ParentID, fetchJsonHeaderObject.getString(Constants.ParentID));
                dbHeadTable.put(Constants.ParentName, fetchJsonHeaderObject.getString(Constants.ParentName));
                dbHeadTable.put(Constants.ParentTypeID, fetchJsonHeaderObject.getString(Constants.ParentTypeID));
                dbHeadTable.put(Constants.ParentTypDesc, fetchJsonHeaderObject.getString(Constants.ParentTypDesc));
                dbHeadTable.put(Constants.FeedbackDate, fetchJsonHeaderObject.optString(Constants.FeedbackDate));

                JSONArray itemsArray = new JSONArray(fetchJsonHeaderObject.getString(Constants.ITEM_TXT));
                JSONArray jsonArray = new JSONArray();
                for (int incrementVal = 0; incrementVal < itemsArray.length(); incrementVal++) {

                    JSONObject singleRow = itemsArray.getJSONObject(incrementVal);
                    JSONObject itemObject = new JSONObject();
                    itemObject.put(Constants.Remarks, singleRow.optString(Constants.Remarks));
                    itemObject.put(Constants.FeebackGUID, singleRow.optString(Constants.FeebackGUID));
                    itemObject.put(Constants.FeebackItemGUID, singleRow.optString(Constants.FeebackItemGUID));
                    itemObject.put(Constants.FeedbackType, singleRow.optString(Constants.FeedbackType));
                    itemObject.put(Constants.FeedbackTypeDesc, singleRow.optString(Constants.FeedbackTypeDesc));
                    itemObject.put(Constants.FeedbackSubTypeID, singleRow.optString(Constants.FeedbackSubTypeID));
                    itemObject.put(Constants.FeedbackSubTypeDesc, singleRow.optString(Constants.FeedbackSubTypeDesc));
                    itemObject.put(Constants.ParentID, fetchJsonHeaderObject.optString(Constants.ParentID));
                    itemObject.put(Constants.ParentName, fetchJsonHeaderObject.optString(Constants.ParentName));
                    itemObject.put(Constants.ParentTypDesc, fetchJsonHeaderObject.optString(Constants.ParentTypDesc));
                    itemObject.put(Constants.ParentTypeID, fetchJsonHeaderObject.optString(Constants.ParentTypeID));
                    jsonArray.put(itemObject);
                }
                dbHeadTable.put(Constants.FeedbackItemDetails, jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return dbHeadTable;
        }
        public static JSONObject getSOHeaderValuesFromJsonObject (JSONObject fetchJsonHeaderObject){
            JSONObject dbHeadTable = new JSONObject();
            try {

                try {
                    try {
                        if (fetchJsonHeaderObject.has(Constants.SSSOGuid)) {
                            dbHeadTable.put(Constants.SSSOGuid, fetchJsonHeaderObject.getString(Constants.SSSOGuid));
                            REPEATABLE_REQUEST_ID = fetchJsonHeaderObject.getString(Constants.SSSOGuid).replace("-", "");
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.SPGUID)) {
                            if (!TextUtils.isEmpty(fetchJsonHeaderObject.getString(Constants.SPGUID))) {
                                dbHeadTable.put(Constants.SPGUID, fetchJsonHeaderObject.getString(Constants.SPGUID));
                            } else {
                                String SPGUID = Constants.getSPGUID();
                                dbHeadTable.put(Constants.SPGUID, SPGUID);
                            }
                        } else {
                            String SPGUID = Constants.getSPGUID();
                            dbHeadTable.put(Constants.SPGUID, SPGUID);
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.OrderType)) {
                            dbHeadTable.put(Constants.OrderType, fetchJsonHeaderObject.getString(Constants.OrderType));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.OrderEntry)) {
                            if (fetchJsonHeaderObject.getString(Constants.OrderEntry) != null && !TextUtils.isEmpty(fetchJsonHeaderObject.getString(Constants.OrderEntry))) {
                                dbHeadTable.put(Constants.OrderEntry, fetchJsonHeaderObject.getString(Constants.OrderEntry));
                            } else {
                                dbHeadTable.put(Constants.OrderEntry, "BEATVISIT");
                            }
                        } else {
                            dbHeadTable.put(Constants.OrderEntry, "BEATVISIT");
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.OrderOrigin)) {
                            if (fetchJsonHeaderObject.getString(Constants.OrderOrigin) != null && !TextUtils.isEmpty(fetchJsonHeaderObject.getString(Constants.OrderOrigin))) {
                                dbHeadTable.put(Constants.OrderOrigin, fetchJsonHeaderObject.getString(Constants.OrderOrigin));
                            } else {
                                dbHeadTable.put(Constants.OrderOrigin, "MOBILE");
                            }
                        } else {
                            dbHeadTable.put(Constants.OrderOrigin, "MOBILE");
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.OrderTypeDesc)) {
                            dbHeadTable.put(Constants.OrderTypeDesc, fetchJsonHeaderObject.getString(Constants.OrderTypeDesc));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.OrderDate)) {
                            dbHeadTable.put(Constants.OrderDate, fetchJsonHeaderObject.getString(Constants.OrderDate));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.Source)) {
                            if (fetchJsonHeaderObject.getString(Constants.Source) != null && !TextUtils.isEmpty(fetchJsonHeaderObject.getString(Constants.Source))) {
                                dbHeadTable.put(Constants.Source, fetchJsonHeaderObject.getString(Constants.Source));
                            } else {
                                dbHeadTable.put(Constants.Source, "MOBILE");
                            }
                        } else {
                            dbHeadTable.put(Constants.Source, "MOBILE");
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.BeatGuid)) {
                            dbHeadTable.put(Constants.BeatGuid, fetchJsonHeaderObject.getString(Constants.BeatGuid));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.BeatName)) {
                            dbHeadTable.put(Constants.BeatName, fetchJsonHeaderObject.getString(Constants.BeatName));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
//            dbHeadTable.put(Constants.DmsDivision, fetchJsonHeaderObject.getString(Constants.DmsDivision));
//            dbHeadTable.put(Constants.DmsDivisionDesc, fetchJsonHeaderObject.getString(Constants.DmsDivisionDesc));
                    try {
                        if (fetchJsonHeaderObject.has(Constants.PONo)) {
                            dbHeadTable.put(Constants.PONo, fetchJsonHeaderObject.getString(Constants.PONo));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.PODate)) {
                            dbHeadTable.put(Constants.PODate, fetchJsonHeaderObject.getString(Constants.PODate));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.FromCPGUID)) {
                            dbHeadTable.put(Constants.FromCPGUID, fetchJsonHeaderObject.getString(Constants.FromCPGUID));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.FromCPNo)) {
                            dbHeadTable.put(Constants.FromCPNo, fetchJsonHeaderObject.getString(Constants.FromCPNo));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.FromCPName)) {
                            dbHeadTable.put(Constants.FromCPName, fetchJsonHeaderObject.getString(Constants.FromCPName));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

                    try {
                        if (fetchJsonHeaderObject.has(Constants.FromCPTypId)) {
                            dbHeadTable.put(Constants.FromCPTypId, fetchJsonHeaderObject.getString(Constants.FromCPTypId));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.FromCPTypDs)) {
                            dbHeadTable.put(Constants.FromCPTypDs, fetchJsonHeaderObject.getString(Constants.FromCPTypDs));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.CPGUID)) {
                            dbHeadTable.put(Constants.CPGUID, fetchJsonHeaderObject.getString(Constants.CPGUID));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.CPNo)) {
                            dbHeadTable.put(Constants.CPNo, fetchJsonHeaderObject.getString(Constants.CPNo));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.CPName)) {
                            dbHeadTable.put(Constants.CPName, fetchJsonHeaderObject.getString(Constants.CPName));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.CPType)) {
                            dbHeadTable.put(Constants.CPType, fetchJsonHeaderObject.getString(Constants.CPType));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.CPTypeDesc)) {
                            dbHeadTable.put(Constants.CPTypeDesc, fetchJsonHeaderObject.getString(Constants.CPTypeDesc));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.SoldToCPGUID)) {
                            dbHeadTable.put(Constants.SoldToCPGUID, fetchJsonHeaderObject.getString(Constants.SoldToCPGUID));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.SoldToId)) {
                            dbHeadTable.put(Constants.SoldToId, fetchJsonHeaderObject.getString(Constants.SoldToId));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
//            dbHeadTable.put(Constants.SoldToUID, fetchJsonHeaderObject.getString(Constants.SoldToUID));
                    try {
                        if (fetchJsonHeaderObject.has(Constants.SoldToDesc)) {
                            dbHeadTable.put(Constants.SoldToDesc, fetchJsonHeaderObject.getString(Constants.SoldToDesc));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.SoldToType)) {
                            dbHeadTable.put(Constants.SoldToType, fetchJsonHeaderObject.getString(Constants.SoldToType));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

                    try {
                        if (fetchJsonHeaderObject.has(Constants.Status)) {
                            if (fetchJsonHeaderObject.getString(Constants.Status).equalsIgnoreCase("X")) {
                                dbHeadTable.put(Constants.Status, "000001");
                            } else if (fetchJsonHeaderObject.getString(Constants.Status).equalsIgnoreCase("000000")) {
                                dbHeadTable.put(Constants.Status, "000001");
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.SPNo)) {
                            dbHeadTable.put(Constants.SPNo, fetchJsonHeaderObject.getString(Constants.SPNo));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.FirstName)) {
                            dbHeadTable.put(Constants.FirstName, fetchJsonHeaderObject.getString(Constants.FirstName));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.TestRun)) {
                            dbHeadTable.put(Constants.TestRun, fetchJsonHeaderObject.getString(Constants.TestRun));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.GrossAmt)) {
                            dbHeadTable.put(Constants.GrossAmt, fetchJsonHeaderObject.getString(Constants.GrossAmt));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.Tax)) {
                            dbHeadTable.put(Constants.Tax, fetchJsonHeaderObject.getString(Constants.Tax));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.NetPrice)) {
                            if (fetchJsonHeaderObject.getString(Constants.NetPrice) != null && !fetchJsonHeaderObject.getString(Constants.NetPrice).equals("null")) {
                                dbHeadTable.put(Constants.NetPrice, fetchJsonHeaderObject.getString(Constants.NetPrice));
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.Currency)) {
                            dbHeadTable.put(Constants.Currency, fetchJsonHeaderObject.getString(Constants.Currency));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.CreatedOn)) {
                            dbHeadTable.put(Constants.CreatedOn, fetchJsonHeaderObject.getString(Constants.CreatedOn));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.CreatedAt)) {
                            dbHeadTable.put(Constants.CreatedAt, fetchJsonHeaderObject.getString(Constants.CreatedAt));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fetchJsonHeaderObject.has(Constants.Latitude)) {
                            dbHeadTable.put(Constants.Latitude, fetchJsonHeaderObject.getString(Constants.Latitude));
                        }
                        if (fetchJsonHeaderObject.has(Constants.Longitude)) {
                            dbHeadTable.put(Constants.Longitude, fetchJsonHeaderObject.getString(Constants.Longitude));
                        }
                        if (fetchJsonHeaderObject.has(Constants.OrderTime)) {
                            dbHeadTable.put(Constants.OrderTime, fetchJsonHeaderObject.getString(Constants.OrderTime));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }

                JSONArray itemsArray = new JSONArray(fetchJsonHeaderObject.getString(Constants.ITEM_TXT));
                JSONArray jsonArray = new JSONArray();
                try {
                    if (itemsArray != null && itemsArray.length() > 0) {
                        for (int incrementVal = 0; incrementVal < itemsArray.length(); incrementVal++) {

                            JSONObject singleRow = itemsArray.getJSONObject(incrementVal);
                            JSONObject itemObject = new JSONObject();
                            try {
                                if (singleRow.has(Constants.SSSOItemGUID)) {
                                    itemObject.put(Constants.SSSOItemGUID, singleRow.get(Constants.SSSOItemGUID));
                                    if (singleRow.get(Constants.SSSOItemGUID) != null && !TextUtils.isEmpty(singleRow.get(Constants.SSSOItemGUID).toString())) {
                                        itemObject.put(Constants.SSSOItemGUID, singleRow.get(Constants.SSSOItemGUID));
                                    } else {
                                        String guid = GUID.newRandom().toString36();
                                        itemObject.put(Constants.SSSOItemGUID, guid);
                                    }
                                } else {
                                    String guid = GUID.newRandom().toString36();
                                    itemObject.put(Constants.SSSOItemGUID, guid);
                                }
                            } catch (Throwable e) {
                                try {
                                    String guid = GUID.newRandom().toString36();
                                    itemObject.put(Constants.SSSOItemGUID, guid);
                                } catch (Throwable ex) {
                                    LogManager.writeLogError("SSSOItemGUID in Item population throwing error " + ex.toString());
                                }
                            }
                            try {
                                if (singleRow.has(Constants.SSSOGuid)) {
                                    itemObject.put(Constants.SSSOGuid, singleRow.get(Constants.SSSOGuid));
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            try {
                                if (singleRow.has(Constants.Quantity)) {
                                    singleRow.get(Constants.Quantity);
                                    if (!TextUtils.isEmpty(singleRow.get(Constants.Quantity).toString())) {
                                        itemObject.put(Constants.Quantity, singleRow.get(Constants.Quantity));
                                    } else {
                                        itemObject.put(Constants.Quantity, "1");
                                    }
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            try {
                                if (singleRow.has(Constants.ItemNo)) {
                                    itemObject.put(Constants.ItemNo, singleRow.get(Constants.ItemNo));
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            try {
                                if (singleRow.has(Constants.MaterialNo)) {
                                    itemObject.put(Constants.MaterialNo, singleRow.get(Constants.MaterialNo));
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            try {
                                if (singleRow.has(Constants.SkuGroup)) {
                                    itemObject.put(Constants.SkuGroup, singleRow.get(Constants.SkuGroup));
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            try {
                                if (singleRow.has(Constants.BomMatIndicator)) {
                                    itemObject.put(Constants.BomMatIndicator, singleRow.get(Constants.BomMatIndicator));
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }

                            try {
                                if (singleRow.has(Constants.Currency)) {
                                    itemObject.put(Constants.Currency, singleRow.get(Constants.Currency));
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            try {
                                if (singleRow.has(Constants.Uom)) {
                                    if (singleRow.has(Constants.SELECTED_UOM_NON_META_PROPERTY) && !TextUtils.isEmpty(singleRow.getString(Constants.SELECTED_UOM_NON_META_PROPERTY))) {
                                        itemObject.put(Constants.Uom, singleRow.get(Constants.SELECTED_UOM_NON_META_PROPERTY));
                                    } else {
                                        itemObject.put(Constants.Uom, singleRow.get(Constants.Uom));
                                    }
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            try {
                                if (singleRow.has(Constants.NetPrice)) {
                                    if (singleRow.get(Constants.NetPrice) != null && !singleRow.get(Constants.NetPrice).equals("")) {
                                        itemObject.put(Constants.NetPrice, singleRow.get(Constants.NetPrice));
                                    }
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            try {
                                if (singleRow.has(Constants.MRP)) {
                                    itemObject.put(Constants.MRP, singleRow.get(Constants.MRP));
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }

                            try {
                                if (singleRow.has(Constants.GrossAmt)) {
                                    itemObject.put(Constants.GrossAmt, singleRow.get(Constants.GrossAmt));
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            try {
                                if (singleRow.has(Constants.UnitPrice)) {
                                    itemObject.put(Constants.UnitPrice, singleRow.get(Constants.UnitPrice));
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }

                            try {
                                if (singleRow.has(Constants.CashDiscountPerc)) {
                                    itemObject.put(Constants.CashDiscountPerc, singleRow.get(Constants.CashDiscountPerc));
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            try {
                                if (singleRow.has(Constants.CashDiscPercAmt)) {
                                    itemObject.put(Constants.CashDiscPercAmt, singleRow.get(Constants.CashDiscPercAmt));
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            try {
                                if (singleRow.has(Constants.IsfreeGoodsItem)) {
                                    itemObject.put(Constants.IsfreeGoodsItem, singleRow.get(Constants.IsfreeGoodsItem));
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            try {
                                if (singleRow.has(Constants.RejReason)) {
                                    itemObject.put(Constants.RejReason, singleRow.get(Constants.RejReason));
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            try {
                                if (singleRow.has(Constants.RejReasonDesc)) {
                                    itemObject.put(Constants.RejReasonDesc, singleRow.get(Constants.RejReasonDesc));
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            try {
                                if (singleRow.has(Constants.Status)) {
                                    itemObject.put(Constants.Status, singleRow.get(Constants.Status));
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }try {
                                if (singleRow.has(Constants.ProposedDlvQty)) {
                                    itemObject.put(Constants.ProposedDlvQty, singleRow.get(Constants.ProposedDlvQty));
                                }
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            jsonArray.put(itemObject);
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.SSSoItemDetails, jsonArray);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return dbHeadTable;
        }

        public static JSONObject getCollHeaderValuesFromJsonObject (JSONObject fetchJsonHeaderObject)
        {

            JSONObject dbHeadTable = new JSONObject();

            try {
                if (!TextUtils.isEmpty(fetchJsonHeaderObject.optString(Constants.BeatGUID))) {
                    dbHeadTable.put(Constants.BeatGUID, fetchJsonHeaderObject.optString(Constants.BeatGUID));
                }
                dbHeadTable.put(Constants.CPNo, fetchJsonHeaderObject.getString(Constants.CPNo));
                dbHeadTable.put(Constants.BankID, fetchJsonHeaderObject.getString(Constants.BankID));
                try {
                    dbHeadTable.put(Constants.ParentTypeID, fetchJsonHeaderObject.getString(Constants.ParentTypeID));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                dbHeadTable.put(Constants.BankName, fetchJsonHeaderObject.getString(Constants.BankName));
                dbHeadTable.put(Constants.BranchName, fetchJsonHeaderObject.getString(Constants.BranchName));
                dbHeadTable.put(Constants.InstrumentNo, fetchJsonHeaderObject.getString(Constants.InstrumentNo));
                dbHeadTable.put(Constants.Amount, fetchJsonHeaderObject.getString(Constants.Amount));
                dbHeadTable.put(Constants.Remarks, fetchJsonHeaderObject.getString(Constants.Remarks));
                dbHeadTable.put(Constants.FIPDocType, fetchJsonHeaderObject.getString(Constants.FIPDocType));
                dbHeadTable.put(Constants.PaymentModeID, fetchJsonHeaderObject.getString(Constants.PaymentModeID));
                dbHeadTable.put(Constants.FIPDate, fetchJsonHeaderObject.getString(Constants.FIPDate) + "T00:00:00");
                if (!TextUtils.isEmpty(fetchJsonHeaderObject.optString(Constants.InstrumentDate))) {
                    dbHeadTable.put(Constants.InstrumentDate, fetchJsonHeaderObject.optString(Constants.InstrumentDate) + "T00:00:00");
                }
                dbHeadTable.put(Constants.CPGUID, fetchJsonHeaderObject.getString(Constants.CPGUID));
                dbHeadTable.put(Constants.LOGINID, fetchJsonHeaderObject.getString(Constants.LOGINID));
                dbHeadTable.put(Constants.FIPGUID, fetchJsonHeaderObject.getString(Constants.FIPGUID));
                dbHeadTable.put(Constants.SPGUID, fetchJsonHeaderObject.optString(Constants.SPGuid).toUpperCase());
                dbHeadTable.put(Constants.CPName, fetchJsonHeaderObject.getString(Constants.CPName));
                dbHeadTable.put(Constants.ParentNo, fetchJsonHeaderObject.getString(Constants.ParentNo));
                dbHeadTable.put(Constants.SPNo, fetchJsonHeaderObject.getString(Constants.SPNo));
                dbHeadTable.put(Constants.SPFirstName, fetchJsonHeaderObject.getString(Constants.SPFirstName));
//            dbHeadTable.put(Constants.CreatedOn, fetchJsonHeaderObject.getString(Constants.CreatedOn));
//            dbHeadTable.put(Constants.CreatedAt, fetchJsonHeaderObject.getString(Constants.CreatedAt));
                dbHeadTable.put(Constants.Currency, fetchJsonHeaderObject.getString(Constants.Currency));
                dbHeadTable.put(Constants.CPTypeID, fetchJsonHeaderObject.getString(Constants.CPTypeID));
                dbHeadTable.put(Constants.Source, fetchJsonHeaderObject.getString(Constants.Source));

                JSONArray itemsArray = new JSONArray(fetchJsonHeaderObject.getString(Constants.ITEM_TXT));
                JSONArray jsonArray = new JSONArray();
                for (int incrementVal = 0; incrementVal < itemsArray.length(); incrementVal++) {

                    JSONObject singleRow = itemsArray.getJSONObject(incrementVal);
                    JSONObject itemObject = new JSONObject();
                    itemObject.put(Constants.FIPItemGUID, singleRow.optString(Constants.FIPItemGUID));
                    itemObject.put(Constants.FIPGUID, singleRow.optString(Constants.FIPGUID));
                    itemObject.put(Constants.ReferenceTypeID, singleRow.optString(Constants.ReferenceTypeID));
                    itemObject.put(Constants.FIPItemNo, String.valueOf(incrementVal + 1));
                    if (!singleRow.optString(Constants.ReferenceID).equalsIgnoreCase("")) {
                        itemObject.put(Constants.FIPAmount, singleRow.optString(Constants.FIPAmount));
                        itemObject.put(Constants.ReferenceID, singleRow.optString(Constants.ReferenceID));
//                        itemObject.put(Constants.Amount, singleRow.optString(Constants.Amount));
//                        itemObject.put(Constants.CashDiscountPercentage, singleRow.optString(Constants.CashDiscountPercentage));
//                        itemObject.put(Constants.CashDiscount, singleRow.optString(Constants.CashDiscount));
                    } else {
                        itemObject.put(Constants.FIPAmount, singleRow.optString(Constants.FIPAmount));
                    }
                    if (!TextUtils.isEmpty(singleRow.optString(Constants.InstrumentDate))) {
                        itemObject.put(Constants.InstrumentDate, singleRow.optString(Constants.InstrumentDate));
                    }
                    itemObject.put(Constants.Currency, singleRow.optString(Constants.Currency));
                    itemObject.put(Constants.InstrumentNo, singleRow.optString(Constants.InstrumentNo));
                    itemObject.put(Constants.PaymentMode, singleRow.optString(Constants.PaymentModeID));
                    itemObject.put(Constants.PaymetModeDesc, singleRow.optString(Constants.PaymetModeDesc));
                    if (!TextUtils.isEmpty(singleRow.optString(Constants.BeatGUID))) {
                        itemObject.put(Constants.BeatGUID, singleRow.optString(Constants.BeatGUID));
                    }
                    jsonArray.put(itemObject);
                }
                dbHeadTable.put(Constants.FinancialPostingItemDetails, jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return dbHeadTable;
        }

        public static String formatValue (String key, String value){

            String formattedValue = "";
            if (!key.equalsIgnoreCase("") && !value.equalsIgnoreCase("")) {

                formattedValue = value + " - " + key;

            } else {
                if (!key.equalsIgnoreCase("") || !value.equalsIgnoreCase("")) {
                    formattedValue = value + " - " + key;

                }

            }

            return formattedValue;

        }
        public static boolean restartApp (Activity activity){
        /*LogonCoreContext lgCtx1 = null;
        try {// TODO: 19-12-2018 Hided for version upgrade
            lgCtx1 = LogonCore.getInstance().getLogonContext();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (lgCtx1 == null) {

            SharedPreferences sharedPreferences = activity.getSharedPreferences(Constants.PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isAppRestart", true);
            editor.commit();
            Log.e("Restart", "Called");
            activity.finishAffinity();
            Intent dialogIntent = new Intent(activity, RegistrationActivity.class);
            activity.startActivity(dialogIntent);
        } else {*/
            return false;

//        }
//        return true;
        }
        public static String getConfigTypeIndicator (String collName, String columnName, String
        whereColumnn, String whereColval, String propertyColumn, String propVal){
            String colmnVal = "";
            try {
                colmnVal = OfflineManager.getValueByColumnName(collName + "?$select=" + columnName + " &$filter = " + whereColumnn + " eq '" + whereColval + "' and " + propertyColumn + " eq '" + propVal + "' ", columnName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return colmnVal;
        }

        /*permission Request*/
        public static final int CAMERA_PERMISSION_CONSTANT = 890;

        public static void setPermissionStatus (Context mContext, String key,boolean value){
            SharedPreferences permissionStatus = mContext.getSharedPreferences("permissionStatus", MODE_PRIVATE);
            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(key, value);
            editor.apply();
        }
        public static boolean getPermissionStatus (Context mContext, String key){
            SharedPreferences permissionStatus = mContext.getSharedPreferences("permissionStatus", MODE_PRIVATE);
            return permissionStatus.getBoolean(key, false);
        }
        public static void navigateToAppSettingsScreen (Context context){
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", context.getPackageName(), null);
            intent.setData(uri);
            context.startActivity(intent);
        }

        public static TextView setFontSizeByMaxText (TextView textView){
            try {
                int lineCount = textView.getText().length();

                if (lineCount < 20) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                } else if (lineCount < 35) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
                } else if (lineCount < 50) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                } else if (lineCount < 70) {
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                } else if (lineCount < 85) {
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

        public static int dpToPx ( int i, Context context){
            Resources resources = context.getResources();
            DisplayMetrics metrics = resources.getDisplayMetrics();
            int px = i * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
            return px;
        }

        public static HashMap<String, String> MAPQPSSCHGuidByMaterial = new HashMap<>();
        public static HashMap<String, ArrayList<String>> MAPSCHGuidByMaterial = new HashMap<>();//TODO need to change
        public static HashMap<String, String> MAPORDQtyByCrsSkuGrp = new HashMap<>();
        public static HashMap<String, ArrayList<String>> MAPSCHGuidByCrsSkuGrp = new HashMap<>();// TODO need to change
        public static HashMap<String, ArrayList<SKUGroupBean>> HashMapSubMaterials = new HashMap<>();
        public static ArrayList<SKUGroupBean> selectedSOItems = new ArrayList<>();

        public static String convertHashMapToString (HashMap < String, String > hashMap, String
        propertyName){
            String mStrQry = "";
            if (hashMap != null && !hashMap.isEmpty()) {
                Iterator iterator = hashMap.keySet().iterator();
                int incVal = 0;
                while (iterator.hasNext()) {
                    if (incVal == 0 && incVal == hashMap.size() - 1) {
                        mStrQry = mStrQry
                                + "(" + propertyName + "%20eq%20'"
                                + iterator.next().toString() + "')";

                    } else if (incVal == 0) {
                        mStrQry = mStrQry
                                + "((" + propertyName + "%20eq%20'"
                                + iterator.next().toString() + "')";

                    } else if (incVal == hashMap.size() - 1) {
                        mStrQry = mStrQry
                                + "%20or%20(" + propertyName + "%20eq%20'"
                                + iterator.next().toString() + "'))";
                    } else {
                        mStrQry = mStrQry
                                + "%20or%20(" + propertyName + "%20eq%20'"
                                + iterator.next().toString() + "') ";
                    }
                    incVal++;
                }
            }

            return mStrQry;
        }

        public static String getOrderQtyByRetiler (String mStrRetNo, String mStrCurrentDate, Context
        context, String ssoQry){
            String mStrOrderQty = "0.0";
            double orderQty = 0.0;
            try {
                if (!ssoQry.equalsIgnoreCase("")) {
                    mStrOrderQty = OfflineManager.getTotalSumByCondition("" + Constants.SSSoItemDetails +
                            "?$select=" + Constants.Quantity + " &$filter= " + ssoQry + " ", Constants.Quantity);
                }
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }

            double mdouDevOrderQty = 0.0;
            try {
                mdouDevOrderQty = OfflineManager.getDeviceTotalOrderQtyByRetailer(Constants.SSSOs, context, mStrCurrentDate, mStrRetNo, Constants.Quantity);
            } catch (Exception e) {
                mdouDevOrderQty = 0.0;
            }

            orderQty = Double.parseDouble(mStrOrderQty) + mdouDevOrderQty;

            return orderQty + "";
        }
        public static String getLast30Days () {
            String dateFormat = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -30);
            return simpleDateFormat.format(cal.getTime()) + "T00:00:00";
        }

        public static String getInvQtyByInvQry (String invQry){
            String mStInvQty = "0.0";
            try {
                if (!invQry.equalsIgnoreCase("")) {
                    mStInvQty = OfflineManager.getTotalSumByCondition("" + Constants.SSInvoiceItemDetails +
                            "?$select=" + Constants.Quantity + " &$filter= " + invQry + " ", Constants.Quantity);
                }
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }
            return mStInvQty + "";
        }

        public static String getNoOfDaysBefore ( int days){
            String dateFormat = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -days);
            return simpleDateFormat.format(cal.getTime()) + "T00:00:00";
        }

        public static boolean isSchemeBasketOrNot (String mStrSchemeGuid){
            boolean mBoolHeadWiseScheme = false;
            String mStrSchemeQry = Constants.Schemes + "?$filter= " + Constants.SchemeGUID +
                    " eq guid'" + mStrSchemeGuid + "' and  " + Constants.SchemeTypeID + " eq '" + Constants.SchemeTypeIDBasketScheme + "' ";
            try {
                mBoolHeadWiseScheme = OfflineManager.getVisitStatusForCustomer(mStrSchemeQry);
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }
            return mBoolHeadWiseScheme;
        }

        public static HashMap<String, SKUGroupBean> hashMapCpStockDataByMaterial = new HashMap<>();
        public static HashMap<String, SchemeBean> hashMapCpStockItemGuidQtyValByMaterial = new HashMap<>();
        public static HashMap<String, SchemeBean> hashMapCpStockItemGuidQtyValByOrderMatGrp = new HashMap<>();
        public static HashMap<String, Set> hashMapMaterialValByOrdMatGrp = new HashMap<>();

        public static String SchemeTypeNormal = "NormalScheme";
        public static String SchemeTypeBasket = "BasketScheme";
        public static String EmptyGUIDVal = "00000000-0000-0000-0000-000000000000";
        public static String BasketHeadingName = "Basket(Min)";
        public static HashMap<String, SchemeBean> HashMapSchemeValuesBySchemeGuid = new HashMap<>();
        public static HashMap<String, String> HashMapSchemeIsInstantOrQPS = new HashMap<>();
        public static HashMap<String, ArrayList<String>> HashMapSchemeListByMaterial = new HashMap<>();
        public static String SchemeQRY = "";
        public static String CPGUIDVAL = "";
        public static String SCHEMECPGUID = "";
        public static String SCHEMEDISTRIBUTORGUID = "";
        public static String YES = "YES";

        public static String trimQtyDecimalUpvalue (String qty){
            String roundQry = "";
            try {
                if (qty.contains(".")) {
                    String[] arrQty = qty.split("\\.");
                    double roundValue = Double.parseDouble(arrQty[0]);
                    double roundDecimal = Double.parseDouble(arrQty[1]);
                    if (roundDecimal > 0) {
                        roundValue = roundValue + 1;
                    }
                    roundQry = roundValue + "";
                    return roundQry.substring(0, roundQry.indexOf(".")) + "";
                } else
                    return qty;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return roundQry;
        }

        public static void showCustomKeyboard (View v, KeyboardView keyboardView, Context context){
            if (v != null) {
                ((InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
            keyboardView.setVisibility(View.VISIBLE);
            keyboardView.setEnabled(true);

        }

        public static void hideCustomKeyboard (KeyboardView keyboardView){
            try {
                keyboardView.setVisibility(View.GONE);
                keyboardView.setEnabled(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void decrementEditTextVal (EditText editText, String mStrDotAval){
            BigDecimal mDouAmountVal = new BigDecimal("0");
            String et_text = editText.getText().toString();
            String total = "0.0";
            if (!et_text.isEmpty()) {
                total = et_text;

            }

            if (total.contains(".")) {
//            double number = Double.parseDouble(total);
                BigDecimal number = new BigDecimal(total);
//            int integer = (int)number;
                BigInteger integer = new BigDecimal(number.doubleValue()).toBigInteger();
                String[] splitNumber = total.split("\\.");
                BigDecimal decimal = new BigDecimal("0.0");
                BigInteger subtactVal = new BigInteger("1");
                if (splitNumber.length > 1) {
                    if (!splitNumber[1].equalsIgnoreCase("")) {
                        decimal = BigDecimal.valueOf(Double.parseDouble("." + splitNumber[1]));
                        mDouAmountVal = BigDecimal.valueOf(integer.subtract(subtactVal).doubleValue() + decimal.doubleValue());
                    } else {
                        mDouAmountVal = BigDecimal.valueOf(integer.subtract(subtactVal).doubleValue());
                    }
                } else {
                    mDouAmountVal = BigDecimal.valueOf(integer.subtract(subtactVal).doubleValue());
                }

            } else {
                mDouAmountVal = BigDecimal.valueOf(Double.parseDouble(total) - 1);
            }
            int res = mDouAmountVal.compareTo(new BigDecimal("0"));

            if (res <= 0) {
                if (mStrDotAval.equalsIgnoreCase("Y")) {
                    setCursorPos(editText);
                    if (et_text.contains(".")) {
                        editText.setText("0.0");
                    } else {
                        editText.setText(UtilConstants.removeLeadingZeroVal("0"));
                    }
                } else {
                    editText.setText("0");
                }
                setCursorPos(editText);
            } else {
                if (mStrDotAval.equalsIgnoreCase("Y")) {
                    setCursorPos(editText);
                    if (et_text.contains(".")) {
                        editText.setText(mDouAmountVal + "");
                    } else {
                        editText.setText(UtilConstants.removeLeadingZeroVal(mDouAmountVal + ""));
                    }
                } else {
                    editText.setText(UtilConstants.removeLeadingZeroVal(mDouAmountVal + ""));
                }
                setCursorPos(editText);
            }

        }

        public static void incrementTextValues (EditText editText, String mStrDotAval){
//        double sPrice = 0;
            BigDecimal sPrice = new BigDecimal("0");
            String et_text = editText.getText().toString();

            String total = "0.0";
            if (!et_text.isEmpty()) {
                total = et_text;
            }
//        sPrice = Double.parseDouble(total);
//        sPrice++;

            if (total.contains(".")) {
//            double number = Double.parseDouble(total);
                BigDecimal number = new BigDecimal(total);
//            int integer = (int)number;
                BigInteger integer = new BigDecimal(number.doubleValue()).toBigInteger();
                String[] splitNumber = total.split("\\.");
                BigDecimal decimal = new BigDecimal("0.0");
                BigInteger incrementVal = new BigInteger("1");
                if (splitNumber.length > 1) {
                    if (!splitNumber[1].equalsIgnoreCase("")) {
//                    decimal = Double.parseDouble("."+splitNumber[1]);
                        decimal = BigDecimal.valueOf(Double.parseDouble("." + splitNumber[1]));
                        sPrice = BigDecimal.valueOf(integer.add(incrementVal).doubleValue() + decimal.doubleValue());
                    } else {
                        sPrice = BigDecimal.valueOf(integer.add(incrementVal).doubleValue());
                    }
                } else {
                    sPrice = BigDecimal.valueOf(integer.add(incrementVal).doubleValue());
                }

            } else {
                sPrice = BigDecimal.valueOf(Double.parseDouble(total) + 1);
            }
            if (mStrDotAval.equalsIgnoreCase("Y")) {
                setCursorPos(editText);
                if (et_text.contains(".")) {
                    editText.setText(sPrice + "");
                } else {
                    editText.setText(UtilConstants.removeLeadingZeroVal(sPrice + ""));
                }
            } else {
                editText.setText(UtilConstants.removeLeadingZeroVal(sPrice + ""));
            }
            setCursorPos(editText);

        }
        private static void setCursorPos (EditText editText){
            int position = 0;
            try {
                position = editText.getText().toString().length();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                editText.setSelection(position);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public static String calculatePrimaryDiscount (String mStrPriDis, String mStrNetAmt){
            String mDouPriSchDis = "0.0";

            try {
                if (Double.parseDouble(mStrPriDis) > 0 && Double.parseDouble(mStrNetAmt) > 0) {
                    mDouPriSchDis = Constants.formulaOneCalculation(mStrPriDis, mStrNetAmt);
                } else {
                    mDouPriSchDis = "0.0";
                }
            } catch (NumberFormatException e) {
                mDouPriSchDis = "0.0";
            }
            return mDouPriSchDis;
        }
        public static String formulaOneCalculation (String taxPer, String applyColumn){
            Double mCaluclateVal = 0.0;
            try {
                mCaluclateVal = (Double.parseDouble(applyColumn) * Double.parseDouble(taxPer)) / 100;
            } catch (NumberFormatException e) {
                mCaluclateVal = 0.0;
            }

            if (mCaluclateVal.isInfinite() || mCaluclateVal.isNaN()) {
                mCaluclateVal = 0.0;
            }
            return mCaluclateVal + "";

        }
        public static int round ( double d){
            double dAbs = Math.abs(d);
            int i = (int) dAbs;
            double result = dAbs - (double) i;
            if (result < 0.5) {
                return d < 0 ? -i : i;
            } else {
                return d < 0 ? -(i + 1) : i + 1;
            }
        }
        public static String makeCPQry (ArrayList < String > alRetailers, String columnName){
            String mCPQry = "";
            for (String cpNo : alRetailers) {
                if (mCPQry.length() == 0)
                    mCPQry += " " + columnName + " eq '" + cpNo + "'";
                else
                    mCPQry += " or " + columnName + " eq '" + cpNo + "'";

            }

            return mCPQry;
        }
        public static String getTaxAmount (String mStrAfterPriDisAmount, String
        mStrSecDisAmt, ODataEntity oDataEntity, String mStrOrderQty){
            String mStrAfterSecAmt = (Double.parseDouble(mStrAfterPriDisAmount) - Double.parseDouble(mStrSecDisAmt)) + "";
            Double mStrNetAmtPerQty = Double.parseDouble(mStrAfterSecAmt) / Double.parseDouble(mStrOrderQty);
            String mStrTaxAmt = "0";
            try {
                mStrTaxAmt = OfflineManager.getPriceOnFieldByMatBatchAfterPrimarySecDiscount(oDataEntity, mStrNetAmtPerQty + "", mStrOrderQty);
            } catch (OfflineODataStoreException e) {
                mStrTaxAmt = "0";
            }

            return mStrTaxAmt;
        }
        public static String getTaxAmount (String mStrNetAmtPerQty, ODataEntity oDataEntity, String
        mStrOrderQty){
       /* String mStrAfterSecAmt = (Double.parseDouble(mStrAfterPriDisAmount) - Double.parseDouble(mStrSecDisAmt)) + "";
        Double mStrNetAmtPerQty = Double.parseDouble(mStrAfterSecAmt) / Double.parseDouble(mStrOrderQty);*/
            String mStrTaxAmt = "0";
            try {
                mStrTaxAmt = OfflineManager.getPriceOnFieldByMatBatchAfterPrimarySecDiscount(oDataEntity, mStrNetAmtPerQty + "", mStrOrderQty);
            } catch (OfflineODataStoreException e) {
                mStrTaxAmt = "0";
            }

            return mStrTaxAmt;
        }

        public static String getCalculateColumn (String mStrApplicableOnID){
            String mStrCalColumn = "";

            if (mStrApplicableOnID.equalsIgnoreCase("01")) {
//            mStrCalColumn = Constants.UnitPrice;
                mStrCalColumn = Constants.IntermUnitPrice;
            } else if (mStrApplicableOnID.equalsIgnoreCase("02")) {
                mStrCalColumn = Constants.LandingPrice;
            } else if (mStrApplicableOnID.equalsIgnoreCase("03")) {
                mStrCalColumn = Constants.MRP;
            } else if (mStrApplicableOnID.equalsIgnoreCase("04")) {
//            mStrCalColumn = Constants.UnitPrice;
                mStrCalColumn = Constants.IntermUnitPrice;
            }

            return mStrCalColumn;
        }
        public static ArrayList<SchemeBean> removeDuplicateScheme
        (ArrayList < SchemeBean > schPerCalBeanList) {
            ArrayList<SchemeBean> schPerCalBeanListFinal = new ArrayList<>();
            ArrayList<String> schemeIdList = new ArrayList<>();
            if (schPerCalBeanList != null) {
                for (SchemeBean schemeBean : schPerCalBeanList) {
                    if (!schemeIdList.contains(schemeBean.getSchemeGuid())) {
                        schPerCalBeanListFinal.add(schemeBean);
                        schemeIdList.add(schemeBean.getSchemeGuid());
                    }
                }
            }
            return schPerCalBeanListFinal;
        }
        public static double getSecDiscAmtPer ( double calSecPer, ArrayList<
        SchemeCalcuBean > schemeCalcuBeanArrayList){
            double values = 0.0;
            for (SchemeCalcuBean schemeCalcuBean : schemeCalcuBeanArrayList) {
                values = values + schemeCalcuBean.getmDouSecDiscount();
            }
            values = values + calSecPer;
            return values;
        }
        public static double getSecSchemeAmt ( double mDouSecAmt, ArrayList<
        SchemeCalcuBean > schemeCalcuBeanArrayList){
            double values = 0.0;
            for (SchemeCalcuBean schemeCalcuBean : schemeCalcuBeanArrayList) {
                values = values + schemeCalcuBean.getmDouSecAmt();
            }
            values = mDouSecAmt + values;
            return values;
        }
        public static SchemeBean getPrimaryTaxValByMaterial (String cPStockItemGUID, String
        mStrMatNo,
                String mStrOrderQty,boolean ratioSchemeCal, SKUGroupBean skuGroupBean, SOCreateBean
        soCreateBean){

            SchemeBean mStrNetAmount = null;
            try {

                String mStockSnoQry = "";
                if (soCreateBean.getPriceBatchCalType().equalsIgnoreCase(Constants.X)) {
                    mStockSnoQry = Constants.CPStockItemSnos + "?$filter=" + Constants.MaterialNo + " eq '" + mStrMatNo + "' and "
                            + Constants.CPStockItemGUID + " eq guid'" + cPStockItemGUID + "' and "
                            + Constants.StockTypeID + " eq '" + Constants.str_1 +
                            "'  &$orderby=" + Constants.ManufacturingDate + "%20desc ";
                } else {
                    mStockSnoQry = Constants.CPStockItemSnos + "?$filter=" + Constants.MaterialNo + " eq '" + mStrMatNo + "' and "
                            + Constants.CPStockItemGUID + " eq guid'" + cPStockItemGUID + "' and "
                            + Constants.StockTypeID + " eq '" + Constants.str_1 +
                            "'  &$orderby=" + Constants.ManufacturingDate + "%20asc ";
                }
                mStrNetAmount = OfflineManager.getNetAmount(mStockSnoQry, mStrOrderQty, mStrMatNo, ratioSchemeCal, skuGroupBean, soCreateBean.getPriceBatchCalType());
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }
            return mStrNetAmount;
        }

        public static String trimQtyDecimalPlace (String qty){
            if (qty.contains("."))
                return qty.substring(0, qty.indexOf("."));
            else
                return qty;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public static <T, E > Set < T > getKeysByValue(Map < T, E > map, E value) {
            Set<T> keys = new HashSet<T>();
            for (Map.Entry<T, E> entry : map.entrySet()) {
                if (Objects.equals(value, entry.getValue())) {
                    keys.add(entry.getKey());
                }
            }
            return keys;
        }

        public static String getTypesetValueForSkugrp (Context ctx){
            String typeValues = "";
            try {
                typeValues = OfflineManager.getValueByColumnName(Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Typeset + " eq '" +
                        Constants.MSEC + "' and " + Constants.Types + " eq '" + Constants.SKUGRP + "'", Constants.TypeValue);
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }
            if (typeValues.equalsIgnoreCase("X")) {
                return ctx.getString(R.string.lbl_sku_group);
            } else {
                return ctx.getString(R.string.lbl_crs_sku_group);
            }
        }

        public static String getPRICALBATCHType () {
            String mStrPRICALBTC = "";
            try {
                String mStrConfigTypeQry = Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Types + " eq '" + Constants.PRICALBTC + "'";
                if (OfflineManager.getVisitStatusForCustomer(mStrConfigTypeQry)) {
                    mStrPRICALBTC = Constants.getName(Constants.ConfigTypsetTypeValues, Constants.TypeValue, Constants.Types, Constants.PRICALBTC);
                }
            } catch (OfflineODataStoreException e) {
                mStrPRICALBTC = "";
                e.printStackTrace();
            }
            return mStrPRICALBTC;
        }

        public static String[][] getDMSDivisionByCPGUID (String mStrCPGUID, Context mContext){
            String qryStr = "";
            String[][] mArrayCPDMSDivisions = null;
            if (ConstantsUtils.getRollInformation(mContext).equalsIgnoreCase(ConstantsUtils.ROLLID_DSR_06)) {
                String spGuid = "";
                try {
                    spGuid = OfflineManager.getGuidValueByColumnName(Constants.SalesPersons + "?$select=" + Constants.SPGUID + " ", Constants.SPGUID);
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }
                qryStr = Constants.CPDMSDivisions + "?$filter=" + Constants.CPGUID + " eq guid'" + mStrCPGUID.toUpperCase() + "' and " + Constants.PartnerMgrGUID + " eq guid'" + spGuid.toUpperCase() + "' ";
            } else {
                qryStr = Constants.CPDMSDivisions + "?$filter=" + Constants.CPGUID + " eq guid'" + mStrCPGUID.toUpperCase() + "'";
            }

            try {
                mArrayCPDMSDivisions = OfflineManager.getDMSDivisionByCPGUID(qryStr);

            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }

            if (mArrayCPDMSDivisions == null) {
                mArrayCPDMSDivisions = new String[2][1];
                mArrayCPDMSDivisions[0][0] = "";
                mArrayCPDMSDivisions[1][0] = "";
            }

            return mArrayCPDMSDivisions;
        }

        public static final String getSOSuccessMsg (String fipDocNo){
            return "SO # " + fipDocNo + " created";
        }

        public static final String getCollectionSuccessMsg (String fipDocNo){
            return "Collection # " + fipDocNo + " created";
        }

        public static final String getROSuccessMsg (String fipDocNo){
            return "RO # " + fipDocNo + " created";
        }

        public static String Y = "Y";
        public static String N = "N";

        public static String getDueDateStatus (Calendar todayCalenderDate, String strInvoiceDate){
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                Date invoiceDate = null;
                invoiceDate = formatter.parse(strInvoiceDate);
                Calendar oldDay = Calendar.getInstance();
                oldDay.setTime(invoiceDate);
                long diff = todayCalenderDate.getTimeInMillis() - oldDay.getTimeInMillis(); //result in millis
                long days = diff / (24 * 60 * 60 * 1000);
                if (days > 0) {
                    return "C";//over due
                } else if (days < -6) {
                    return "B";//not due
                } else {
                    return "A";//near due
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        public static void updateBirthdayAlertsStatus (String keyVal, Context context){
            String store = ConstantsUtils.getFromDataVault(keyVal, context);
            if (store != null && !store.equalsIgnoreCase("")) {
                HashMap<String, String> hashMap = Constants.getBirthdayListFromDataValt(store);
                if (!hashMap.isEmpty()) {
                    Iterator mapSelctedValues = hashMap.keySet()
                            .iterator();
                    while (mapSelctedValues.hasNext()) {
                        String Key = (String) mapSelctedValues.next();
                        hashMap.put(Key, Constants.Y);
                    }
                }
                setBirthdayToDataVault(hashMap, keyVal, context);

            } else {
                HashMap<String, String> hashMap = new HashMap<>();
                setBirthdayToDataVault(hashMap, keyVal, context);
            }
        }
        public static HashMap<String, String> getBirthdayListFromDataValt (String mStrKeyVal){
            HashMap hashMap = new HashMap();
            //Fetch object from data vault
            try {
                JSONObject fetchJsonHeaderObject = new JSONObject(mStrKeyVal);

                String itemsString = fetchJsonHeaderObject.getString(Constants.ITEM_TXT);

                hashMap = convertStringToMap(itemsString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return hashMap;
        }
        public static HashMap<String, String> convertStringToMap (String jsonString){
            HashMap myHashMap = new HashMap();
            try {
                JSONArray e = new JSONArray("[" + jsonString + "]");
                JSONObject jObject = null;
                String keyString = null;

                for (int i = 0; i < e.length(); ++i) {
                    jObject = e.getJSONObject(i);
                    for (int k = 0; k < jObject.length(); ++k) {
                        keyString = (String) jObject.names().get(k);
                        myHashMap.put(keyString, jObject.getString(keyString));
                    }
                }
            } catch (Exception var8) {
                var8.printStackTrace();
            }

            return myHashMap;
        }
        public static void setBirthdayToDataVault (HashMap < String, String > hashMap, String
        alertsKey, Context context){

            Hashtable dbHeaderTable = new Hashtable();
            Gson gson = new Gson();
            try {
                String jsonFromMap = gson.toJson(hashMap);
                //noinspection unchecked
                dbHeaderTable.put(Constants.ITEM_TXT, jsonFromMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject jsonHeaderObject = new JSONObject(dbHeaderTable);
            //noinspection deprecation
            ConstantsUtils.storeInDataVault(alertsKey, jsonHeaderObject.toString(), context);
        }
        public static ArrayList<BirthdaysBean> getAlertsValuesFromDataVault (Context context){
            ArrayList<BirthdaysBean> alDataValutAlertList = new ArrayList<>();
            ArrayList<BirthdaysBean> alDataValutAlertHistList = new ArrayList<>();
            JSONObject fetchJsonHeaderObject = null;
            String store = ConstantsUtils.getFromDataVault(AlertsDataKey, context);
            if (store != null && !store.equalsIgnoreCase("")) {
                try {
                    fetchJsonHeaderObject = new JSONObject(store);
                    String itemsString = fetchJsonHeaderObject.getString(Constants.ITEM_TXT);
                    alDataValutAlertList = Constants.convertToBirthDayArryList(itemsString);
                    if (alDataValutAlertList != null && alDataValutAlertList.size() > 0) {
                        for (BirthdaysBean birthdaysBean : alDataValutAlertList) {
                            if (birthdaysBean.getStatus().equalsIgnoreCase(Constants.Y)) {
                                alDataValutAlertHistList.add(birthdaysBean);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return alDataValutAlertHistList;
        }

        public static ArrayList<BirthdaysBean> convertToBirthDayArryList (String jsonString){
            ArrayList<BirthdaysBean> alBirthDayList = null;
            try {
                Gson gson = new Gson();
                Type stringStringMap = new TypeToken<ArrayList<BirthdaysBean>>() {
                }.getType();
                alBirthDayList = gson.fromJson(jsonString, stringStringMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return alBirthDayList;
        }

        public static void setAlertsValToDataVault (ArrayList < BirthdaysBean > alAlerts, String
        alertsKey, Context context){

            Hashtable dbHeaderTable = new Hashtable();
            Gson gson = new Gson();
            try {
                String jsonFromMap = gson.toJson(alAlerts);
                //noinspection unchecked
                dbHeaderTable.put(Constants.ITEM_TXT, jsonFromMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject jsonHeaderObject = new JSONObject(dbHeaderTable);
            ConstantsUtils.storeInDataVault(alertsKey, jsonHeaderObject.toString(), context);
        }

        public static void setBirthdayListToDataValut (Context context){
            String[][] oneWeekDay;
            oneWeekDay = UtilConstants.getOneweekValues(1);
            String[] splitDayMonth = oneWeekDay[0][0].split("-");

            ArrayList<BirthdaysBean> alRetBirthDayTempList = new ArrayList<>();
            ArrayList<BirthdaysBean> alDataVaultList = new ArrayList<>();
            ArrayList<BirthdaysBean> alRetBirthDayList = getTodayBirthDayList();

            try {
                SharedPreferences settings = context.getSharedPreferences(Constants.PREFS_NAME,
                        0);
                String mStrBirthdayDate = settings.getString(Constants.BirthDayAlertsDate, "");

                if (mStrBirthdayDate.equalsIgnoreCase(UtilConstants.getDate1())) {

                    String store = ConstantsUtils.getFromDataVault(Constants.BirthDayAlertsKey, context);
                    if (store != null && !store.equalsIgnoreCase("")) {
                        alDataVaultList = Constants.getBirthdayListFromDataVault(store);


                        if (alRetBirthDayList != null && alRetBirthDayList.size() > 0) {
                            for (BirthdaysBean firstBeanAL : alRetBirthDayList) {
                                boolean mBoolIsRecordExists = false;

                                if (alDataVaultList != null && alDataVaultList.size() > 0) {

                                    // Loop arrayList1 items
                                    for (BirthdaysBean secondBeanAL : alDataVaultList) {
                                        if (firstBeanAL.getCPUID().toUpperCase().equalsIgnoreCase(secondBeanAL.getCPUID()) && (!firstBeanAL.getAppointmentAlert()
                                                && !secondBeanAL.getAppointmentAlert())) {

                                            if ((secondBeanAL.getDOB().equalsIgnoreCase(firstBeanAL.getDOB())
                                                    || (secondBeanAL.getAnniversary().equalsIgnoreCase(firstBeanAL.getAnniversary())))) {

                                                BirthdaysBean birthdaysBean = new BirthdaysBean();
                                                birthdaysBean.setCPUID(firstBeanAL.getCPUID());
                                                if (firstBeanAL.getDOB().contains(splitDayMonth[1] + "/" + splitDayMonth[0]) && secondBeanAL.getDOBStatus().equalsIgnoreCase(""))
                                                    birthdaysBean.setDOBStatus("");
                                                else
                                                    birthdaysBean.setDOBStatus(Constants.X);

                                                if (firstBeanAL.getAnniversary().contains(splitDayMonth[1] + "/" + splitDayMonth[0]) && secondBeanAL.getAnniversaryStatus().equalsIgnoreCase(""))
                                                    birthdaysBean.setAnniversaryStatus("");
                                                else
                                                    birthdaysBean.setAnniversaryStatus(Constants.X);

                                                birthdaysBean.setMobileNo(firstBeanAL.getMobileNo());
                                                birthdaysBean.setDOB(firstBeanAL.getDOB());
                                                birthdaysBean.setAnniversary(firstBeanAL.getAnniversary());
                                                birthdaysBean.setOwnerName(firstBeanAL.getOwnerName());
                                                birthdaysBean.setRetailerName(firstBeanAL.getRetailerName());
                                                birthdaysBean.setMobileNo(firstBeanAL.getMobileNo());
                                                alRetBirthDayTempList.add(birthdaysBean);
                                                mBoolIsRecordExists = true;
                                                break;
                                            }

                                        } else {
                                            if (firstBeanAL.getCPUID().toUpperCase().equalsIgnoreCase(secondBeanAL.getCPUID())
                                                    && (firstBeanAL.getAppointmentAlert()
                                                    && secondBeanAL.getAppointmentAlert())) {
                                                BirthdaysBean birthdaysBean = new BirthdaysBean();
                                                birthdaysBean.setCPUID(firstBeanAL.getCPUID());
                                                if (secondBeanAL.getAppointmentStatus().equalsIgnoreCase(""))
                                                    birthdaysBean.setAppointmentStatus("");
                                                else
                                                    birthdaysBean.setAppointmentStatus(Constants.X);

                                                birthdaysBean.setMobileNo(firstBeanAL.getMobileNo());
                                                birthdaysBean.setOwnerName(firstBeanAL.getOwnerName());
                                                birthdaysBean.setRetailerName(firstBeanAL.getRetailerName());
                                                birthdaysBean.setMobileNo(firstBeanAL.getMobileNo());
                                                birthdaysBean.setAppointmentTime(firstBeanAL.getAppointmentTime());
                                                birthdaysBean.setAppointmentEndTime(firstBeanAL.getAppointmentEndTime());
                                                birthdaysBean.setAppointmentType(firstBeanAL.getAppointmentType());
                                                birthdaysBean.setAppointmentAlert(true);
                                                alRetBirthDayTempList.add(birthdaysBean);
                                                mBoolIsRecordExists = true;
                                                break;
                                            }
                                        }
                                    }

                                    if (!mBoolIsRecordExists) {
                                        BirthdaysBean birthdaysBean = new BirthdaysBean();
                                        if (!firstBeanAL.getAppointmentAlert()) {
                                            birthdaysBean.setCPUID(firstBeanAL.getCPUID());
                                            birthdaysBean.setDOBStatus(firstBeanAL.getDOBStatus());
                                            birthdaysBean.setAnniversaryStatus(firstBeanAL.getAnniversaryStatus());
                                            birthdaysBean.setMobileNo(firstBeanAL.getMobileNo());
                                            birthdaysBean.setDOB(firstBeanAL.getDOB());
                                            birthdaysBean.setAnniversary(firstBeanAL.getAnniversary());
                                            birthdaysBean.setOwnerName(firstBeanAL.getOwnerName());
                                            birthdaysBean.setRetailerName(firstBeanAL.getRetailerName());
                                            alRetBirthDayTempList.add(birthdaysBean);
                                        } else {
                                            birthdaysBean.setCPUID(firstBeanAL.getCPUID());
                                            birthdaysBean.setAppointmentStatus("");
                                            birthdaysBean.setMobileNo(firstBeanAL.getMobileNo());
                                            birthdaysBean.setOwnerName(firstBeanAL.getOwnerName());
                                            birthdaysBean.setRetailerName(firstBeanAL.getRetailerName());
                                            birthdaysBean.setMobileNo(firstBeanAL.getMobileNo());
                                            birthdaysBean.setAppointmentTime(firstBeanAL.getAppointmentTime());
                                            birthdaysBean.setAppointmentEndTime(firstBeanAL.getAppointmentEndTime());
                                            birthdaysBean.setAppointmentType(firstBeanAL.getAppointmentType());
                                            birthdaysBean.setAppointmentAlert(true);
                                            alRetBirthDayTempList.add(birthdaysBean);
                                        }
                                    }

                                }


                            }

                        }


                        setCurrentDateTOSharedPerf(context);
                        // TODO add values into data vault
                        if (alRetBirthDayTempList != null && alRetBirthDayTempList.size() > 0) {
                            assignValuesIntoDataVault(alRetBirthDayTempList, context);
                        } else {
                            if (alRetBirthDayList != null && alRetBirthDayList.size() > 0) {
                                assignValuesIntoDataVault(alRetBirthDayList, context);
                            } else {
                                assignEmptyValuesIntoDataVault(context);
                            }
                        }

                    } else {
                        setCurrentDateTOSharedPerf(context);
                        // TODO add values into data vault
                        if (alRetBirthDayList != null && alRetBirthDayList.size() > 0) {
                            assignValuesIntoDataVault(alRetBirthDayList, context);
                        } else {
                            assignEmptyValuesIntoDataVault(context);
                        }
                    }


                } else {
                    assignEmptyValuesIntoDataVault(context);
                    setCurrentDateTOSharedPerf(context);
                    // TODO add values into data vault
                    if (alRetBirthDayList != null && alRetBirthDayList.size() > 0) {
                        assignValuesIntoDataVault(alRetBirthDayList, context);
                    } else {
                        assignEmptyValuesIntoDataVault(context);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


        public static int setBirthDayRecordsToDataValut (Context context){
            int mIntBirthdaycount = 0;
            SharedPreferences settings = context.getSharedPreferences(Constants.PREFS_NAME,
                    0);
            ArrayList<BirthdaysBean> alRetBirthDayList = getTodayBirthDayListOnly();
            try {

                String mStrBirthdayDate = settings.getString(Constants.BirthDayAlertsDate, "");

                if (mStrBirthdayDate.equalsIgnoreCase(UtilConstants.getDate1())) {
                    String store = ConstantsUtils.getFromDataVault(Constants.BirthDayAlertsTempKey, context);
                    if (store != null && !store.equalsIgnoreCase("")) {
                        HashMap<String, String> hashMap = Constants.getBirthdayListFromDataValt(store);
                        if (!hashMap.isEmpty()) {
                            if (alRetBirthDayList != null && alRetBirthDayList.size() > 0) {
                                for (BirthdaysBean firstBeanAL : alRetBirthDayList) {
                                    if (hashMap.containsKey(firstBeanAL.getCPUID())) {
                                        String mStrVal = hashMap.get(firstBeanAL.getCPUID());
                                        if (mStrVal.equalsIgnoreCase(Constants.Y)) {
                                            hashMap.put(firstBeanAL.getCPUID(), Constants.Y);
                                        } else {
                                            hashMap.put(firstBeanAL.getCPUID(), Constants.N);
                                            mIntBirthdaycount++;
                                        }
                                    } else {
                                        hashMap.put(firstBeanAL.getCPUID(), Constants.N);
                                        mIntBirthdaycount++;
                                    }
                                }
                            } else {
                                mIntBirthdaycount = 0;
                            }
                        } else {
                            hashMap = new HashMap<>();
                            if (alRetBirthDayList != null && alRetBirthDayList.size() > 0) {
                                for (BirthdaysBean firstBeanAL : alRetBirthDayList) {
                                    if (hashMap.containsKey(firstBeanAL.getCPUID())) {
                                        String mStrVal = hashMap.get(firstBeanAL.getCPUID());
                                        if (mStrVal.equalsIgnoreCase(Constants.Y)) {
                                            hashMap.put(firstBeanAL.getCPUID(), Constants.Y);
                                        } else {
                                            hashMap.put(firstBeanAL.getCPUID(), Constants.N);
                                            mIntBirthdaycount++;
                                        }
                                    } else {
                                        hashMap.put(firstBeanAL.getCPUID(), Constants.N);
                                        mIntBirthdaycount++;
                                    }
                                }
                            } else {
                                mIntBirthdaycount = 0;
                            }
                        }
                        setCurrentDateTOSharedPerf(context);
                        if (hashMap == null)
                            hashMap = new HashMap<>();
                        setBirthdayToDataVault(hashMap, Constants.BirthDayAlertsTempKey, context);

                    } else {
                        HashMap<String, String> hashMap = new HashMap<>();
                        if (alRetBirthDayList != null && alRetBirthDayList.size() > 0) {
                            for (BirthdaysBean firstBeanAL : alRetBirthDayList) {
                                if (hashMap.containsKey(firstBeanAL.getCPUID())) {
                                    String mStrVal = hashMap.get(firstBeanAL.getCPUID());
                                    if (mStrVal.equalsIgnoreCase(Constants.Y)) {
                                        hashMap.put(firstBeanAL.getCPUID(), Constants.Y);
                                    } else {
                                        hashMap.put(firstBeanAL.getCPUID(), Constants.N);
                                        mIntBirthdaycount++;
                                    }
                                } else {
                                    hashMap.put(firstBeanAL.getCPUID(), Constants.N);
                                    mIntBirthdaycount++;
                                }
                            }
                        } else {
                            mIntBirthdaycount = 0;
                        }

                        setCurrentDateTOSharedPerf(context);
                        setBirthdayToDataVault(hashMap, Constants.BirthDayAlertsTempKey, context);
                    }
                } else {
                    HashMap<String, String> hashMap = new HashMap<>();
                    if (alRetBirthDayList != null && alRetBirthDayList.size() > 0) {
                        for (BirthdaysBean firstBeanAL : alRetBirthDayList) {
                            if (hashMap.containsKey(firstBeanAL.getCPUID())) {
                                String mStrVal = hashMap.get(firstBeanAL.getCPUID());
                                if (mStrVal.equalsIgnoreCase(Constants.Y)) {
                                    hashMap.put(firstBeanAL.getCPUID(), Constants.Y);
                                } else {
                                    hashMap.put(firstBeanAL.getCPUID(), Constants.N);
                                    mIntBirthdaycount++;
                                }
                            } else {
                                hashMap.put(firstBeanAL.getCPUID(), Constants.N);
                                mIntBirthdaycount++;
                            }
                        }
                    } else {
                        mIntBirthdaycount = 0;
                    }

                    setCurrentDateTOSharedPerf(context);
                    setBirthdayToDataVault(hashMap, Constants.BirthDayAlertsTempKey, context);

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(Constants.BirthdayAlertsCount, mIntBirthdaycount);
            editor.commit();

            return mIntBirthdaycount;

        }

        public static int setAlertsRecordsToDataValut (Context context){
            int mIntTextAlertcount = 0;
            SharedPreferences settings = context.getSharedPreferences(Constants.PREFS_NAME,
                    0);

            ArrayList<BirthdaysBean> arrayList = new ArrayList<>();
            ArrayList<BirthdaysBean> alRetBirthDayList = OfflineManager.getAlertsFromLocalDB(arrayList);
            try {

                String store = ConstantsUtils.getFromDataVault(Constants.AlertsTempKey, context);
                if (store != null && !store.equalsIgnoreCase("")) {
                    HashMap<String, String> hashMap = Constants.getBirthdayListFromDataValt(store);
                    if (!hashMap.isEmpty()) {
                        if (alRetBirthDayList != null && alRetBirthDayList.size() > 0) {
                            for (BirthdaysBean firstBeanAL : alRetBirthDayList) {
                                if (hashMap.containsKey(firstBeanAL.getAlertGUID())) {
                                    String mStrVal = hashMap.get(firstBeanAL.getAlertGUID());
                                    if (mStrVal.equalsIgnoreCase(Constants.Y)) {
                                        hashMap.put(firstBeanAL.getAlertGUID(), Constants.Y);
                                    } else {
                                        hashMap.put(firstBeanAL.getAlertGUID(), Constants.N);
                                        mIntTextAlertcount++;
                                    }
                                } else {
                                    hashMap.put(firstBeanAL.getAlertGUID(), Constants.N);
                                    mIntTextAlertcount++;
                                }
                            }
                        } else {
                            mIntTextAlertcount = 0;
                        }
                    } else {
                        hashMap = new HashMap<>();
                        if (alRetBirthDayList != null && alRetBirthDayList.size() > 0) {
                            for (BirthdaysBean firstBeanAL : alRetBirthDayList) {
                                if (hashMap.containsKey(firstBeanAL.getAlertGUID())) {
                                    String mStrVal = hashMap.get(firstBeanAL.getAlertGUID());
                                    if (mStrVal.equalsIgnoreCase(Constants.Y)) {
                                        hashMap.put(firstBeanAL.getAlertGUID(), Constants.Y);
                                    } else {
                                        hashMap.put(firstBeanAL.getAlertGUID(), Constants.N);
                                        mIntTextAlertcount++;
                                    }
                                } else {
                                    hashMap.put(firstBeanAL.getAlertGUID(), Constants.N);
                                    mIntTextAlertcount++;
                                }
                            }
                        } else {
                            mIntTextAlertcount = 0;
                        }
                    }

                    setCurrentDateTOSharedPerf(context);
                    setBirthdayToDataVault(hashMap, Constants.AlertsTempKey, context);

                } else {

                    HashMap<String, String> hashMap = new HashMap<>();
                    if (alRetBirthDayList != null && alRetBirthDayList.size() > 0) {
                        for (BirthdaysBean firstBeanAL : alRetBirthDayList) {
                            if (hashMap.containsKey(firstBeanAL.getAlertGUID())) {
                                String mStrVal = hashMap.get(firstBeanAL.getAlertGUID());
                                if (mStrVal.equalsIgnoreCase(Constants.Y)) {
                                    hashMap.put(firstBeanAL.getAlertGUID(), Constants.Y);
                                } else {
                                    hashMap.put(firstBeanAL.getAlertGUID(), Constants.N);
                                    mIntTextAlertcount++;
                                }
                            } else {
                                hashMap.put(firstBeanAL.getAlertGUID(), Constants.N);
                                mIntTextAlertcount++;
                            }
                        }
                    } else {
                        mIntTextAlertcount = 0;
                    }

                    setCurrentDateTOSharedPerf(context);
                    setBirthdayToDataVault(hashMap, Constants.AlertsTempKey, context);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(Constants.TextAlertsCount, mIntTextAlertcount);
            editor.commit();

            return mIntTextAlertcount;

        }

        public static ArrayList<BirthdaysBean> getTodayBirthDayListOnly () {
            ArrayList<BirthdaysBean> alRetBirthDayList = null;
            String[][] oneWeekDay = UtilConstants.getOneweekValues(1);
            if (oneWeekDay != null && oneWeekDay.length > 0) {
                for (int i = 0; i < oneWeekDay[0].length; i++) {
                    String[] splitDayMonth = oneWeekDay[0][i].split("-");
                    String mStrBirthdayAvlQry = Constants.ChannelPartners + "?$filter=(month%28" + Constants.DOB + "%29%20eq " + splitDayMonth[0] + " " +
                            "and day%28" + Constants.DOB + "%29%20eq " + UtilConstants.removeLeadingZeros(splitDayMonth[1]) + ") or (month%28" + Constants.Anniversary + "%29%20eq " + splitDayMonth[0] + " " +
                            "and day%28" + Constants.Anniversary + "%29%20eq " + UtilConstants.removeLeadingZeros(splitDayMonth[1]) + ") ";
                    try {
                        if (OfflineManager.getVisitStatusForCustomer(mStrBirthdayAvlQry)) {

                            try {
                                alRetBirthDayList = OfflineManager.getTodayBirthDayList(mStrBirthdayAvlQry);
                            } catch (OfflineODataStoreException e) {
                                LogManager.writeLogError(Constants.error_txt + e.getMessage());
                            }
                        }
                    } catch (OfflineODataStoreException e) {
                        e.printStackTrace();
                    }
                }
            }

            return alRetBirthDayList;
        }

        public static ArrayList<BirthdaysBean> getTodayBirthDayList () {
            ArrayList<BirthdaysBean> alRetBirthDayList = null;
            ArrayList<BirthdaysBean> alAppointmentList = null;
            String[][] oneWeekDay = UtilConstants.getOneweekValues(1);
            if (oneWeekDay != null && oneWeekDay.length > 0) {
                for (int i = 0; i < oneWeekDay[0].length; i++) {

                    String[] splitDayMonth = oneWeekDay[0][i].split("-");

                    String mStrBirthdayAvlQry = Constants.ChannelPartners + "?$filter=(month%28" + Constants.DOB + "%29%20eq " + splitDayMonth[0] + " " +
                            "and day%28" + Constants.DOB + "%29%20eq " + UtilConstants.removeLeadingZeros(splitDayMonth[1]) + ") or (month%28" + Constants.Anniversary + "%29%20eq " + splitDayMonth[0] + " " +
                            "and day%28" + Constants.Anniversary + "%29%20eq " + UtilConstants.removeLeadingZeros(splitDayMonth[1]) + ") ";
                    try {
                        if (OfflineManager.getVisitStatusForCustomer(mStrBirthdayAvlQry)) {

                            try {
                                alRetBirthDayList = OfflineManager.getTodayBirthDayList(mStrBirthdayAvlQry);
                            } catch (OfflineODataStoreException e) {
                                LogManager.writeLogError(Constants.error_txt + e.getMessage());
                            }
                        }
                    } catch (OfflineODataStoreException e) {
                        e.printStackTrace();
                    }

                    String mStrAppointmentListQuery = Constants.Visits + "?$filter=" + Constants.StatusID + " eq '00' and (month%28" + Constants.PlannedDate + "%29%20eq " + splitDayMonth[0] + " " +
                            "and day%28" + Constants.PlannedDate + "%29%20eq " + UtilConstants.removeLeadingZeros(splitDayMonth[1]) + ")";
                    try {
                        alAppointmentList = OfflineManager.getAppointmentListForAlert(mStrAppointmentListQuery);
                    } catch (OfflineODataStoreException e) {
                        e.printStackTrace();
                    }


                    if (alRetBirthDayList != null && alRetBirthDayList.size() > 0) {
                        if (alAppointmentList != null && alAppointmentList.size() > 0) {
                            alRetBirthDayList.addAll(alRetBirthDayList.size(), alAppointmentList);
                        }
                    } else {
                        alRetBirthDayList = new ArrayList<>();
                        if (alAppointmentList != null && alAppointmentList.size() > 0) {
                            alRetBirthDayList.addAll(alAppointmentList);
                        }
                    }

                }
            }

            return alRetBirthDayList;
        }
        public static ArrayList<BirthdaysBean> getBirthdayListFromDataVault (String mStrKeyVal){

            ArrayList<BirthdaysBean> beanArrayList = null;
            //Fetch object from data vault
            try {

                JSONObject fetchJsonHeaderObject = new JSONObject(mStrKeyVal);

                String itemsString = fetchJsonHeaderObject.getString(Constants.ITEM_TXT);

                beanArrayList = convertToBirthDayArryList(itemsString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return beanArrayList;
        }
        public static void setCurrentDateTOSharedPerf (Context context){
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME,
                    0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.BirthDayAlertsDate, UtilConstants.getDate1());
            editor.commit();

        }
        public static void setQuodeckDateTOSharedPerf (Context context){
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME,
                    0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(Constants.QUODECK_DATE, UtilConstants.getDate1());
            editor.commit();

        }
        public static void assignValuesIntoDataVault
        (ArrayList < BirthdaysBean > alRetBirthDayList, Context context){

            Gson gson = new Gson();
            Hashtable dbHeaderTable = new Hashtable();
            try {
                String jsonFromMap = gson.toJson(alRetBirthDayList);
                //noinspection unchecked
                dbHeaderTable.put(Constants.ITEM_TXT, jsonFromMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONObject jsonHeaderObject = new JSONObject(dbHeaderTable);
            ConstantsUtils.storeInDataVault(Constants.BirthDayAlertsKey, jsonHeaderObject.toString(), context);
        }
        public static void assignEmptyValuesIntoDataVault (Context context){
            ConstantsUtils.storeInDataVault(Constants.BirthDayAlertsKey, "", context);
        }
        public static void setAppointmentNotification (Context context){


        }
        private static SharedPreferences getAppointmentSharedPrefer (Context context){
            return context.getSharedPreferences("appointmentPref", Context.MODE_PRIVATE);
        }

        public static void saveSharedPref (Context context, String key,int value){
            SharedPreferences sharedPreferences = getAppointmentSharedPrefer(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(key, value);
            editor.apply();
        }
        public static void removeAlarmIdFromSharedPref (Context context, String key){
            SharedPreferences sharedPreferences = getAppointmentSharedPrefer(context);
            if (sharedPreferences.contains(key)) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(key);
                editor.apply();
            }
        }
        public static int getSharedPref (Context context, String key,int defaultValue){
            SharedPreferences sharedPreferences = getAppointmentSharedPrefer(context);
            return sharedPreferences.getInt(key, defaultValue);
        }
        public static String getNameByCPGUID (String collName, String columnName, String
        whereColumnn, String whereColval){
            String colmnVal = "";
            try {
                colmnVal = OfflineManager.getValueByColumnName(collName + "?$select=" + columnName + " &$filter = " + whereColumnn + " eq guid'" + whereColval + "'", columnName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return colmnVal;
        }

        public static Set<String> getOrderMatGrpByBrandAndCategory (String mStrCatID, String
        mStrBrandID, String mStrDMSDivisionQry){
            Set<String> mSetOrderMatGrp = new HashSet<>();
            try {

                if (!mStrCatID.equalsIgnoreCase("") && mStrBrandID.equalsIgnoreCase("")) {
                    mSetOrderMatGrp = OfflineManager.getValueByColumnNameCRSSKU(Constants.OrderMaterialGroups + "?$select=" + Constants.OrderMaterialGroupID +
                            " &$filter = " + Constants.MaterialCategoryID + " eq '" + mStrCatID + "' and " + mStrDMSDivisionQry + "  ", Constants.OrderMaterialGroupID);
                } else if (mStrCatID.equalsIgnoreCase("") && !mStrBrandID.equalsIgnoreCase("")) {
                    mSetOrderMatGrp = OfflineManager.getValueByColumnNameCRSSKU(Constants.OrderMaterialGroups + "?$select=" + Constants.OrderMaterialGroupID +
                            " &$filter = " + Constants.BrandID + " eq '" + mStrBrandID + "' and " + mStrDMSDivisionQry + " ", Constants.OrderMaterialGroupID);
                } else {
                    mSetOrderMatGrp = OfflineManager.getValueByColumnNameCRSSKU(Constants.OrderMaterialGroups + "?$select=" + Constants.OrderMaterialGroupID +
                            " &$filter = " + Constants.BrandID + " eq '" + mStrBrandID + "' and " + Constants.MaterialCategoryID + " eq '" + mStrCatID + "' and " + mStrDMSDivisionQry + " ", Constants.OrderMaterialGroupID);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return mSetOrderMatGrp;
        }
        public static boolean isDBStockCategoryDisplay (Context mContext){
            return false;
        }

        public static Locale getLocalFromISO (String iso4217code){
            Locale toReturn = null;
            for (Locale locale : NumberFormat.getAvailableLocales()) {
                String code = NumberFormat.getCurrencyInstance(locale).
                        getCurrency().getCurrencyCode();
                if (iso4217code.equals(code)) {
                    toReturn = locale;
                    break;
                }
            }
            return toReturn;
        }

        public static String getCurrencyPattren (String currencyCode, String mStrAmount){

            String mStrConAmount = "";

            try {
                NumberFormat format = NumberFormat.getCurrencyInstance(Locale.getDefault());
                if (!TextUtils.isEmpty(currencyCode))
                    format.setCurrency(java.util.Currency.getInstance(currencyCode));
                if (!TextUtils.isEmpty(mStrAmount))
                    mStrConAmount = format.format(new BigDecimal(mStrAmount));
                else
                    mStrConAmount = format.format(new BigDecimal(0));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mStrConAmount;
        }

        public static String appendPrecedingZeros (String mStrInputNo,int stringLength){


            String mfinalString = Constants.trimLeadingZeros(mStrInputNo);
       /* try {
            if(mStrInputNo!=null && !mStrInputNo.equalsIgnoreCase("")) {
                try {
                    int numberOfDigits = mStrInputNo.length();
                    int numberOfLeadingZeroes = stringLength - numberOfDigits;
                    StringBuilder sb = new StringBuilder();
                    if (numberOfLeadingZeroes > 0) {
                        for (int i = 0; i < numberOfLeadingZeroes; i++) {
                            sb.append("0");
                        }
                    }
                    sb.append(mStrInputNo);
                    mfinalString = sb.toString();
                } catch (Exception e) {
                    mfinalString = mStrInputNo;
                    e.printStackTrace();
                }
            }else{
                mfinalString = "";
            }
        } catch (Exception e) {
            mfinalString = "";
            e.printStackTrace();
        }*/

            return mfinalString;
        }

        public static String trimLeadingZeros (String source){
            for (int i = 0; i < source.length(); ++i) {
                char c = source.charAt(i);
                if (c != '0') {
                    return source.substring(i);
                }
            }
            return ""; // or return "0";
        }

    public static File SaveImageInDevice1  (Context context,String filename, Bitmap bitmap){
        String extStorageDirectory = context.getExternalFilesDir(null).toString();
        OutputStream outStream = null;

        File file = new File(extStorageDirectory, filename );
        if (file.exists()) {
            file.delete();
//            filename = filename.replace(".jpg",".png");
            file = new File(extStorageDirectory, filename);
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
    public static File SaveImageInDevice2  (Context context,String filename, Bitmap bitmap){
        String extStorageDirectory = context.getExternalFilesDir(null).toString();
        OutputStream outStream = null;

        File file = new File(extStorageDirectory, filename );
        if (file.exists()) {
            file.delete();
//            filename = filename.replace(".jpg",".png");
            file = new File(extStorageDirectory, filename);
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
        public static File SaveImageInDevice (String filename, Bitmap bitmap){
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            OutputStream outStream = null;
            File file = new File(extStorageDirectory, filename + ".jpg");
            if (filename.contains(".")) {
                file = new File(extStorageDirectory, filename);
            }
            if (file.exists()) {
                file.delete();
            }
            try {
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
        public static boolean checkPermission (Context context){
            if (Build.VERSION_CODES.M <= Build.VERSION.SDK_INT) {
                int result = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
                int resultCore = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
                return result == PackageManager.PERMISSION_GRANTED && resultCore == PackageManager.PERMISSION_GRANTED;
            } else {
                return true;
            }
        }

        public static void dialogBoxWithButton (Context context, String title, String
        message, String positiveButton, String negativeButton,final DialogCallBack dialogCallBack){
            UtilConstants.dialogBoxWithCallBack(context, title, message, positiveButton, negativeButton, false, dialogCallBack);
        }

        public static String getRouteSchGUID (String collName, String columnName, String
        whereColumnn, String whereColval, String cpTypeID){
            String mStrRouteSchGUID = "";
            if (cpTypeID.equalsIgnoreCase(Constants.str_01)) {
                try {
                    mStrRouteSchGUID = OfflineManager.getGuidValueByColumnName(collName + "?$top=1 &$select=" + columnName + " &$filter = " + whereColumnn + " eq '" + whereColval + "'", columnName);
                } catch (Exception e) {
                    mStrRouteSchGUID = "";
                }
            } else {
                // future will use ful
            }
            return mStrRouteSchGUID;
        }

        public static String getValueFromDataVault (String key, Context context){

            return ConstantsUtils.getFromDataVault(key, context);
        }


        public static void setCursorPostion (EditText editText, View view, MotionEvent motionEvent){
            EditText edText = (EditText) view;
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            int touchPosition = editText.getOffsetForPosition(x, y);
            if (touchPosition >= 0) {
                editText.setSelection(touchPosition);
            }
        }

        public static String getCurrencySymbol (String currency, String amount){
            return UtilConstants.getCurrencyFormat(currency, amount);
        }

        public static ArrayList<String> getDefinigReqList (Context mContext){
            ArrayList<String> alAssignColl = new ArrayList<>();
            String[] DEFINGREQARRAY = getDefiningReq(mContext);
            for (String collectionName : DEFINGREQARRAY) {
                if (collectionName.contains("?")) {
                    String[] splitCollName = collectionName.split("\\?");
                    collectionName = splitCollName[0];
                }
                alAssignColl.add(collectionName);
            }
            return alAssignColl;
        }

        /**
         * Delete visual vid product
         */
        public static void deleteFolder (Activity activity){
            if (Build.VERSION_CODES.M <= android.os.Build.VERSION.SDK_INT) {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    File myDirectory = new File(Environment.getExternalStorageDirectory(), Constants.FolderName);
                    if (myDirectory.exists()) {
                        myDirectory.delete();
                    }
                }
            } else {
                File myDirectory = new File(Environment.getExternalStorageDirectory(), Constants.FolderName);
                if (myDirectory.exists()) {
                    myDirectory.delete();
                }
            }
        }

        public static void openImageInGallery (Context mContext, String file){
//        String videoResource = file.getPath();
            Uri intentUri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", new File(file));
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(intentUri, "image/jpeg");
            try {
                mContext.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                // Instruct the user to install a PDF reader here, or something
                Toast.makeText(mContext, mContext.getString(R.string.visual_aidl_app_not_found), Toast.LENGTH_LONG).show();
            }
        }

        public static String makePendingDataToJsonString (Context context){
            String mStrJson = "";
            ArrayList<Object> objectArrayList = SyncSelectionActivity.getPendingCollList(context, false);
            if (!objectArrayList.isEmpty()) {
                String[][] invKeyValues = (String[][]) objectArrayList.get(1);
                JSONArray jsonArray = new JSONArray();
                for (int k = 0; k < invKeyValues.length; k++) {
                    JSONObject jsonObject = new JSONObject();
                    String store = ConstantsUtils.getFromDataVault(invKeyValues[k][0], context);
                    try {
                        // Add the values to the jsonObject
                        jsonObject.put(Constants.KeyNo, invKeyValues[k][0]);
                        jsonObject.put(Constants.KeyType, invKeyValues[k][1]);
                        jsonObject.put(Constants.KeyValue, store);
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

        public static boolean isFileExits (String fileName){
            boolean isFileExits = false;
            try {
                File sdCardDir = Environment.getExternalStorageDirectory();
                // Get The Text file
                File txtFile = new File(sdCardDir, fileName);
                // Read the file Contents in a StringBuilder Object
                isFileExits = txtFile.exists();
            } catch (Exception e) {
                isFileExits = false;
                e.printStackTrace();
                LogManager.writeLogError("isFileExits() : " + e.getMessage());
            }
            return isFileExits;
        }

        public static String getTextFileData (String fileName){
            // Get the dir of SD Card
            File sdCardDir = Environment.getExternalStorageDirectory();
            // Get The Text file
            File txtFile = new File(sdCardDir, fileName);
            // Read the file Contents in a StringBuilder Object
            StringBuilder text = new StringBuilder();
            if (txtFile.exists()) {
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
            } else {
                text.append("");
            }
            return text.toString();
        }
        public static void setJsonStringDataToDataVault (String mJsonString, Context context){
            try {
                JSONObject jsonObj = new JSONObject(mJsonString);
                // Getting data JSON Array nodes
                JSONArray jsonArray = jsonObj.getJSONArray(DataVaultData);
                for (int incVal = 0; incVal < jsonArray.length(); incVal++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(incVal);
                    String mStrKeyNo = jsonObject.getString(KeyNo);
                    String mStrKeyKeyType = jsonObject.getString(KeyType);
                    String mStrKeyValue = jsonObject.getString(KeyValue);
                    Constants.saveDeviceDocNoToSharedPref(context, mStrKeyKeyType, mStrKeyNo);
                    ConstantsUtils.storeInDataVault(mStrKeyNo, mStrKeyValue, context);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public static String[][] getDealer (String closingDate){
            String[][] retList = null;

            try {
                String routeQry = Constants.RoutePlans + "?$filter=" + Constants.VisitDate + " eq datetime'" + closingDate + "'";

                ArrayList<RetailerBean> alRSCHList = null;
                try {
                    alRSCHList = OfflineManager.getTodayRoutes1(routeQry, "");
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }
                if (alRSCHList != null && alRSCHList.size() > 0) {
                    String routeSchopeVal = alRSCHList.get(0).getRoutSchScope();
                    if (alRSCHList.size() > 1) {
                        String mRSCHQry = "";
                        if (routeSchopeVal.equalsIgnoreCase("000001")) {
                            for (RetailerBean routeList : alRSCHList) {
                                if (mRSCHQry.length() == 0)
                                    mRSCHQry += " guid'" + routeList.getRschGuid().toUpperCase() + "'";
                                else
                                    mRSCHQry += " or " + Constants.RouteSchGUID + " eq guid'" + routeList.getRschGuid().toUpperCase() + "'";

                            }

                            String qryForTodaysBeat = Constants.RouteSchedulePlans + "?$filter=(" +
                                    Constants.RouteSchGUID + " eq " + mRSCHQry + ") &$orderby=" + Constants.SequenceNo + "";

                            retList = OfflineManager.getNotVisitedRetailerList(qryForTodaysBeat, closingDate);


                        } else if (routeSchopeVal.equalsIgnoreCase("000002")) {
                            // Get the list of retailers from RoutePlans
                        }


                    } else {


                        if (routeSchopeVal.equalsIgnoreCase("000001")) {
                            String qryForTodaysBeat = Constants.RouteSchedulePlans + "?$filter=" + Constants.RouteSchGUID + " eq guid'"
                                    + alRSCHList.get(0).getRschGuid().toUpperCase() + "' &$orderby=" + Constants.SequenceNo + "";

                            retList = OfflineManager.getNotVisitedRetailerList(qryForTodaysBeat, closingDate);

                        } else if (routeSchopeVal.equalsIgnoreCase("000002")) {
                            // Get the list of retailers from RoutePlans
                        }

                    }

                } else {
                    retList = null;
                }

            } catch (OfflineODataStoreException e) {
                LogManager.writeLogError(Constants.error_txt + e.getMessage());
            }

            return retList;

        }
        public static boolean isEndateAndEndTimeValid (String mStrStartDate, String mStrStartTime){

            boolean isValidEndDateAndTime = false;
            try {
                String mStrTime = UtilConstants.convertTimeOnly(mStrStartTime);
                String mStrCurrentTime = UtilConstants.convertTimeOnly(UtilConstants.getOdataDuration().toString());

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                Date startDate = null;
                Date endDate = null;

                try {
                    startDate = format.parse(mStrStartDate);
                    endDate = format.parse(UtilConstants.getNewDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date startTimeDate = null;
                Date endTimeDate = null;

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                try {
                    startTimeDate = simpleDateFormat.parse(mStrTime);
                    endTimeDate = simpleDateFormat.parse(mStrCurrentTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                isValidEndDateAndTime = false;

                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                int mYear = cal.get(Calendar.YEAR);
                int mMonth = cal.get(Calendar.MONTH);
                int mDay = cal.get(Calendar.DAY_OF_MONTH);

                Calendar calCurrent = Calendar.getInstance();
                calCurrent.setTime(endDate);
                int mYearCurrent = calCurrent.get(Calendar.YEAR);
                int mMonthCurrent = calCurrent.get(Calendar.MONTH);
                int mDayCurrent = calCurrent.get(Calendar.DAY_OF_MONTH);

                if (mYear == mYearCurrent && mMonth == mMonthCurrent && mDay == mDayCurrent) {
                    isValidEndDateAndTime = !endTimeDate.before(startTimeDate);
                } else {
                    isValidEndDateAndTime = startDate.compareTo(endDate) <= 0;

                }

            } catch (Exception e) {
                isValidEndDateAndTime = false;
            }
            return isValidEndDateAndTime;
        }

        public static ArrayList<RemarkReasonBean> getReasonValues
        (ArrayList < RemarkReasonBean > alReason) {
            alReason.clear();
            String query = Constants.ValueHelps + "?$filter= PropName eq '" + "Reason" + "' and EntityType eq 'Visit' &$orderby=" + Constants.ID + "";
            LogManager.writeLogInfo("Reason Selection list get value Qry : " + query);
            try {
                alReason.addAll(OfflineManager.getRemarksReason(query));
                LogManager.writeLogInfo("Reason Selection list get value count : " + alReason.size());
            } catch (OfflineODataStoreException e) {
                LogManager.writeLogError("Reason Selection list get value error : " + e.getMessage());
                e.printStackTrace();
            }

            return alReason;
        }

        public static ArrayList<RetailerBean> getVisitedRetFromVisit () {
            String mVisitQry = Constants.Visits + "?$filter= " + Constants.StartDate + " eq datetime'" + UtilConstants.getNewDate() + "' and " + Constants.ENDDATE + " eq datetime'" + UtilConstants.getNewDate() + "' and " + Constants.VisitDate + " eq datetime'" + UtilConstants.getNewDate() + "' " + "and StatusID ne '02' and (" + Constants.VisitCatID + " eq '" + Constants.str_01 + "' or " + Constants.VisitCatID + " eq '" + Constants.str_02 + "') &$orderby=" + Constants.EndTime + "%20desc ";

            ArrayList<RetailerBean> retList = OfflineManager.getCPListFromVisit(mVisitQry);
            return retList;
        }
        public static String getOrderValByRetiler (String mStrRetNo, String mStrCurrentDate, String
        mSOOrderType, Context context){
            String mStrOrderVal = "0.0";
            double orderVal = 0.0;
            try {
                mStrOrderVal = OfflineManager.getTotalSumByCondition("" + Constants.SSSOs +
                        "?$select=" + Constants.NetPrice + " &$filter=" + Constants.OrderDate + " eq datetime'" + mStrCurrentDate +
                        "' and " + Constants.SoldToId + " eq '" + mStrRetNo + "' and " + Constants.OrderType + " eq '" + mSOOrderType + "' ", Constants.NetPrice);

            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }

            double mdouDevOrderVal = 0.0;
            try {
                mdouDevOrderVal = OfflineManager.getDeviceTotalOrderAmtByRetailer(Constants.SecondarySOCreate, context, mStrCurrentDate, mStrRetNo);
            } catch (Exception e) {
                mdouDevOrderVal = 0.0;
            }

            orderVal = Double.parseDouble(mStrOrderVal) + mdouDevOrderVal;

            return orderVal + "";
        }

        public static String getDeviceAndDataVaultTLSD (String mStrSoldToID, String mStrCPDMSDIVQry)
        {
            String mStrQry = "", mStrOfflineTLSD = "0";
            try {
                if (mStrSoldToID.equalsIgnoreCase("")) {
                    mStrQry = OfflineManager.makeSSSOQry(Constants.SSINVOICES + "?$filter= " + Constants.InvoiceDate +
                            " ge datetime'" + Constants.getFirstDateOfCurrentMonth() + "' and " + Constants.InvoiceDate + " lt datetime'" + UtilConstants.getNewDate() + "' and " + mStrCPDMSDIVQry + " and " + Constants.InvoiceTypeID + " ne '" + Constants.getSampleInvoiceTypeID() + "' ", Constants.InvoiceGUID);
                } else {
                    mStrQry = OfflineManager.makeSSSOQry(Constants.SSINVOICES + "?$filter= " + Constants.InvoiceDate +
                            " ge datetime'" + Constants.getFirstDateOfCurrentMonth() + "' and " + Constants.InvoiceDate + " lt datetime'" + UtilConstants.getNewDate() + "' and " + Constants.SoldToID + " eq '" + mStrSoldToID + "' and " + mStrCPDMSDIVQry + " and " + Constants.InvoiceTypeID + " ne '" + Constants.getSampleInvoiceTypeID() + "' ", Constants.InvoiceGUID);
                }
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }

            if (!mStrQry.equalsIgnoreCase("")) {
                try {
                    mStrOfflineTLSD = OfflineManager.getCountTLSDFromDatabase(Constants.SSInvoiceItemDetails + "?$filter= " + mStrQry);
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }
            }

            Double mDouDeviceTLSD = 0.0;

            Double mDouTotalTLSD = mDouDeviceTLSD + Double.parseDouble(mStrOfflineTLSD);

            return mDouTotalTLSD + "";
        }

        public static String getSampleInvoiceTypeID () {
            String InvoiceTypeID = "";
            try {
                InvoiceTypeID = OfflineManager.getValueByColumnName(Constants.SSInvoiceTypes + "?$top=1 &$select=" + Constants.InvoiceTypeID + " " +
                        "&$filter=" + Constants.GoodsIssueFromID + " eq '000002' and GoodsIssueCatID eq '000002'", Constants.InvoiceTypeID);
            } catch (OfflineODataStoreException e) {
                InvoiceTypeID = "";
            }
            return InvoiceTypeID;
        }

        public static String getGUIDEditResourcePath (String collection, String key){
            return collection + "(guid'" + key + "')";
        }

        public static String EntityKeyID = "EntityKeyID";
        public static String EntityKeyDesc = "EntityKeyDesc";
        public static String EntityAttribute1 = "EntityAttribute1";
        public static String EntityKey = "EntityKey";

        // constants after merge
        public static String StatusId = "StatusID";
        public static final String SSRO = "SSSO";

        public static String[][] CheckForOtherInConfigValue (String[][]configValues){
            for (int i = 0; i < configValues[0].length; i++) {
                if (configValues[1][i].equalsIgnoreCase("Others")) {
                    String[] temp = new String[configValues.length];
                    for (int k = 0; k < configValues.length; k++) {
                        temp[k] = configValues[k][i];
                    }
                    for (int j = i; j < configValues[0].length - 1; j++) {
                        for (int k = 0; k < configValues.length; k++) {
                            configValues[k][j] = configValues[k][j + 1];
                        }
                    }
                    for (int k = 0; k < configValues.length; k++) {
                        configValues[k][configValues[0].length - 1] = temp[k];
                    }
                    break;
                }
            }
            return configValues;
        }
        public static String ComplaintType = "ComplaintType";
        public static <K extends Comparable, V extends
        Comparable > Map < K, V > sortByValues(Map < K, V > map) {
            List<Map.Entry<K, V>> entries = new LinkedList<>(map.entrySet());

            java.util.Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {

                @Override
                public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                    return o1.getValue().compareTo(o2.getValue());
                }
            });

            //LinkedHashMap will keep the keys in the order they are inserted
            //which is currently sorted on natural ordering
            Map<K, V> sortedMap = new LinkedHashMap<>();

            for (Map.Entry<K, V> entry : entries) {
                sortedMap.put(entry.getKey(), entry.getValue());
            }

            return sortedMap;
        }
        public static String[][] getDistributorsByCPGUID (String mStrCPGUID, Context mContext){
            String mDBStkType = Constants.getName(Constants.ConfigTypsetTypeValues, Constants.TypeValue, Constants.Types, Constants.DSTSTKVIEW);
            String spGuid = "";
            String qryStr = "";
            String[][] mArrayDistributors = null;

            try {
                String mStrConfigTypeQry = Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Types + " eq '" + Constants.DSTSTKVIEW + "'";
                if (OfflineManager.getVisitStatusForCustomer(mStrConfigTypeQry)) {
                    if (mDBStkType.equalsIgnoreCase(Constants.str_01)) {
                        if (ConstantsUtils.getRollInformation(mContext).equalsIgnoreCase(ConstantsUtils.ROLLID_DSR_06)) {
                            try {
                                spGuid = OfflineManager.getGuidValueByColumnName(Constants.SalesPersons + "?$select=" + Constants.SPGUID + " ", Constants.SPGUID);
                            } catch (OfflineODataStoreException e) {
                                e.printStackTrace();
                            }
                            qryStr = Constants.CPDMSDivisions + "?$filter=" + Constants.CPGUID + " eq guid'" + mStrCPGUID.toUpperCase() + "' and " + Constants.PartnerMgrGUID + " eq guid'" + spGuid.toUpperCase() + "'";
                        } else {
                            qryStr = Constants.CPDMSDivisions + "?$filter=" + Constants.CPGUID + " eq guid'" + mStrCPGUID.toUpperCase() + "'";
                        }

                    } else {
                        qryStr = Constants.ChannelPartners + "?$filter=" + Constants.CPGUID + " eq guid'" + mStrCPGUID.toUpperCase() + "' ";
                    }
                } else {
                    mDBStkType = "";
                    qryStr = Constants.ChannelPartners + "?$filter=" + Constants.CPGUID + " eq guid'" + mStrCPGUID.toUpperCase() + "' ";
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                mArrayDistributors = OfflineManager.getDistributorListByCPGUID(qryStr);

            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }

            if (mArrayDistributors == null) {
                mArrayDistributors = new String[11][1];
                mArrayDistributors[0][0] = "";
                mArrayDistributors[1][0] = "";
                mArrayDistributors[2][0] = "";
                mArrayDistributors[3][0] = "";
                mArrayDistributors[4][0] = "";
                mArrayDistributors[5][0] = "";
                mArrayDistributors[6][0] = "";
                mArrayDistributors[7][0] = "";
                mArrayDistributors[8][0] = "";
                mArrayDistributors[9][0] = "";
                mArrayDistributors[10][0] = "";
            } else {
                try {
                    if (mArrayDistributors[4][0] != null) {

                    }
                } catch (Exception e) {
                    mArrayDistributors = new String[11][1];
                    mArrayDistributors[0][0] = "";
                    mArrayDistributors[1][0] = "";
                    mArrayDistributors[2][0] = "";
                    mArrayDistributors[3][0] = "";
                    mArrayDistributors[4][0] = "";
                    mArrayDistributors[5][0] = "";
                    mArrayDistributors[6][0] = "";
                    mArrayDistributors[7][0] = "";
                    mArrayDistributors[8][0] = "";
                    mArrayDistributors[9][0] = "";
                    mArrayDistributors[10][0] = "";
                }
            }

            return mArrayDistributors;
        }

        public static final String CustomerComplaintsCreate = "Consumer Complaints Create";

        public static void onVisitActivityUpdate (String mStrBundleCPGUID32, String loginIdVal,
                String visitActRefID, String vistActType, String visitActTypeDesc){
            //========>Start VisitActivity
            try {
                Hashtable visitActivityTable = new Hashtable();
                String getVisitGuidQry = Constants.Visits + "?$filter=EndDate eq null and CPGUID eq '" + mStrBundleCPGUID32.toUpperCase() + "' " +
                        "and StartDate eq datetime'" + UtilConstants.getNewDate() + "' ";
                ODataGuid mGuidVisitId = null;
                try {
                    mGuidVisitId = OfflineManager.getVisitDetails(getVisitGuidQry);
                } catch (OfflineODataStoreException e) {
                    LogManager.writeLogError(Constants.error_txt + e.getMessage());
                }
                GUID mStrGuide = GUID.newRandom();
                visitActivityTable.put(Constants.VisitActivityGUID, mStrGuide.toString());
                visitActivityTable.put(Constants.LOGINID, loginIdVal);
                visitActivityTable.put(Constants.VisitGUID, mGuidVisitId.guidAsString36());
                visitActivityTable.put(Constants.ActivityType, vistActType);
                visitActivityTable.put(Constants.ActivityTypeDesc, visitActTypeDesc);
                visitActivityTable.put(Constants.ActivityRefID, visitActRefID);
//            visitActivityTable.put(Constants.Latitude, BigDecimal.valueOf(UtilConstants.latitude));
//            visitActivityTable.put(Constants.Longitude, BigDecimal.valueOf(UtilConstants.longitude));

                try {
                    visitActivityTable.put(Constants.Latitude, BigDecimal.valueOf(UtilConstants.round(UtilConstants.latitude, 12)));
                    visitActivityTable.put(Constants.Longitude, BigDecimal.valueOf(UtilConstants.round(UtilConstants.longitude, 12)));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                visitActivityTable.put(Constants.StartTime, mStartTimeDuration);
                visitActivityTable.put(Constants.EndTime, UtilConstants.getOdataDuration());


                try {
                    OfflineManager.createVisitActivity(visitActivityTable);
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            //========>End VisitActivity
        }

        public static JSONObject getSSInvoiceHeaderValuesFromJsonObject (JSONObject
        fetchJsonHeaderObject){
            JSONObject dbHeadTable = new JSONObject();
            try {
                try {
                    dbHeadTable.put(Constants.BeatGUID, fetchJsonHeaderObject.getString(Constants.BeatGUID));
                } catch (Throwable e) {
                    dbHeadTable.put(Constants.BeatGUID, "");
                }
                try {
                    dbHeadTable.put(Constants.BeatCode, fetchJsonHeaderObject.getString(Constants.BeatCode));
                } catch (Throwable e) {
                    dbHeadTable.put(Constants.BeatCode, "");
                }try {
                    dbHeadTable.put(Constants.Remarks, fetchJsonHeaderObject.getString(Constants.Remarks));
                } catch (Throwable e) {
                    dbHeadTable.put(Constants.Remarks, "");
                }
                try {
                    dbHeadTable.put(Constants.TestRun, fetchJsonHeaderObject.getString(Constants.TestRun));
                } catch (Throwable e) {
                    dbHeadTable.put(Constants.TestRun, "");
                }
                try {
                    dbHeadTable.put(Constants.SCHApplyReg, fetchJsonHeaderObject.getString(Constants.SCHApplyReg));
                } catch (Throwable e) {
                    dbHeadTable.put(Constants.SCHApplyReg, "");
                }
                try {
                    dbHeadTable.put(Constants.BeatDesc, fetchJsonHeaderObject.getString(Constants.BeatDesc));
                } catch (Throwable e) {
                    dbHeadTable.put(Constants.BeatDesc, "");
                }
                try {
                    dbHeadTable.put(Constants.InvoiceNo, fetchJsonHeaderObject.getString(Constants.InvoiceNo));
                } catch (Throwable e) {
                }
                try {
                    dbHeadTable.put(Constants.BillToDesc, fetchJsonHeaderObject.getString(Constants.BillToDesc));
                } catch (Throwable e) {
                    dbHeadTable.put(Constants.BillToDesc, "");
                }
                try {
                    dbHeadTable.put(Constants.BillToID, fetchJsonHeaderObject.getString(Constants.BillToID));
                } catch (Throwable e) {
                    dbHeadTable.put(Constants.BillToID, "");
                }
                try {
                    dbHeadTable.put(Constants.CPGroup1, fetchJsonHeaderObject.getString(Constants.CPGroup1));
                } catch (Throwable e) {
                    dbHeadTable.put(Constants.CPGroup1, "");
                }
                try {
                    dbHeadTable.put(Constants.CPGroup3, fetchJsonHeaderObject.getString(Constants.CPGroup3));
                } catch (Throwable e) {
                    dbHeadTable.put(Constants.CPGroup3, "");
                }
                try {
                    dbHeadTable.put(Constants.CPGroup4, fetchJsonHeaderObject.getString(Constants.CPGroup4));
                } catch (Throwable e) {
                    dbHeadTable.put(Constants.CPGroup4, "");
                }
                try {
                    dbHeadTable.put(Constants.Currency, fetchJsonHeaderObject.getString(Constants.Currency));
                } catch (Throwable e) {
                    dbHeadTable.put(Constants.Currency, "");
                }
                try {
                    dbHeadTable.put(Constants.InvoiceGUID, fetchJsonHeaderObject.getString(Constants.InvoiceGUID));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    REPEATABLE_REQUEST_ID = fetchJsonHeaderObject.getString(Constants.InvoiceGUID).replace("-", "");
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.Currency, fetchJsonHeaderObject.getString(Constants.Currency));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.DeliveryDate, fetchJsonHeaderObject.getString(Constants.DeliveryDate));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.GrossAmount, fetchJsonHeaderObject.getString(Constants.GrossAmount));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.InvoiceTypeDesc, fetchJsonHeaderObject.getString(Constants.InvoiceTypeDesc));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.InvoiceValue, fetchJsonHeaderObject.getString(Constants.InvoiceValue));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.PaymentModeDesc, fetchJsonHeaderObject.getString(Constants.PaymentModeDesc));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.PaymentModeID, fetchJsonHeaderObject.getString(Constants.PaymentModeID));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.PaymentStatusID, fetchJsonHeaderObject.getString(Constants.PaymentStatusID));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.RoundOff, fetchJsonHeaderObject.getString(Constants.RoundOff));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.SchemeReqd, fetchJsonHeaderObject.getString(Constants.SchemeReqd));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.SPGUID, fetchJsonHeaderObject.getString(Constants.SPGUID));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.SPName, fetchJsonHeaderObject.getString(Constants.SPName));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.SPNo, fetchJsonHeaderObject.getString(Constants.SPNo));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.Tax, fetchJsonHeaderObject.getString(Constants.Tax));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.Tax1, fetchJsonHeaderObject.getString(Constants.Tax1));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.Tax2, fetchJsonHeaderObject.getString(Constants.Tax2));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.TaxableAmount, fetchJsonHeaderObject.getString(Constants.TaxableAmount));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.CPGUID, fetchJsonHeaderObject.getString(Constants.CPGUID));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.CPNo, fetchJsonHeaderObject.getString(Constants.CPNo));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.CPName, fetchJsonHeaderObject.getString(Constants.CPName));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.CPTypeDesc, fetchJsonHeaderObject.getString(Constants.CPTypeDesc));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.CPTypeID, fetchJsonHeaderObject.getString(Constants.CPTypeID));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
//                dbHeadTable.put(Constants.InvoiceNo, fetchJsonHeaderObject.getString(Constants.InvoiceNo));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.InvoiceTypeID, fetchJsonHeaderObject.getString(Constants.InvoiceTypeID));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.InvoiceDate, fetchJsonHeaderObject.getString(Constants.InvoiceDate));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.PODate, fetchJsonHeaderObject.getString(Constants.PODate));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.SoldToCPGUID, fetchJsonHeaderObject.getString(Constants.SoldToCPGUID));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.SoldToID, fetchJsonHeaderObject.getString(Constants.SoldToID));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.SoldToName, fetchJsonHeaderObject.getString(Constants.SoldToName));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.SoldToTypeID, fetchJsonHeaderObject.getString(Constants.SoldToTypeID));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.ShipToCPGUID, fetchJsonHeaderObject.getString(Constants.ShipToCPGUID));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.ShipToID, fetchJsonHeaderObject.getString(Constants.ShipToID));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.ShipToName, fetchJsonHeaderObject.getString(Constants.ShipToName));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.ShipToTypeID, fetchJsonHeaderObject.getString(Constants.ShipToTypeID));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.TestRun, fetchJsonHeaderObject.getString(Constants.TestRun));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.BillTo, fetchJsonHeaderObject.getString(Constants.BillTo));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.Source, fetchJsonHeaderObject.getString(Constants.Source));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.getString(Constants.StatusID).equalsIgnoreCase("X")) {
                        dbHeadTable.put(Constants.StatusID, "01");
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.NetAmount, fetchJsonHeaderObject.getString(Constants.NetAmount));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.CashDiscAmount, fetchJsonHeaderObject.getString(Constants.CashDiscAmount));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.CashDiscPerc, fetchJsonHeaderObject.getString(Constants.CashDiscPerc));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.Latitude)) {
                        dbHeadTable.put(Constants.Latitude, fetchJsonHeaderObject.getString(Constants.Latitude));
                    }
                    if (fetchJsonHeaderObject.has(Constants.Longitude)) {
                        dbHeadTable.put(Constants.Longitude, fetchJsonHeaderObject.getString(Constants.Longitude));
                    }
                    if (fetchJsonHeaderObject.has(Constants.InvTime)) {
                        dbHeadTable.put(Constants.InvTime, fetchJsonHeaderObject.getString(Constants.InvTime));
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                JSONArray itemsArray = new JSONArray(fetchJsonHeaderObject.getString(Constants.ITEM_TXT));
                JSONArray jsonArray = new JSONArray();
                for (int incrementVal = 0; incrementVal < itemsArray.length(); incrementVal++) {
                    JSONObject singleRow = itemsArray.getJSONObject(incrementVal);
                    JSONObject itemObject = new JSONObject();
                    try {
                        itemObject.put(Constants.InvoiceItemGUID, singleRow.get(Constants.InvoiceItemGUID));
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        itemObject.put(Constants.InvoiceGUID, singleRow.get(Constants.InvoiceGUID));
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
//                    itemObject.put(Constants.StockGuid, singleRow.get(Constants.StockGuid));
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (singleRow.has(Constants.ISFreeGoodsItem)) {
                            itemObject.put(Constants.ISFreeGoodsItem, singleRow.get(Constants.ISFreeGoodsItem));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (singleRow.has(Constants.ItemCat)) {
                            itemObject.put(Constants.ItemCat, singleRow.get(Constants.ItemCat));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (singleRow.has(Constants.CashDiscountPer)) {
                            itemObject.put(Constants.CashDiscountPer, singleRow.get(Constants.CashDiscountPer));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        itemObject.put(Constants.ItemNo, singleRow.get(Constants.ItemNo));
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
//                itemObject.put(Constants.InvoiceNo, singleRow.get(Constants.InvoiceNo));
                    try {
                        itemObject.put(Constants.Remarks, singleRow.get(Constants.Remarks));
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        itemObject.put(Constants.Quantity, singleRow.get(Constants.Quantity));
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        itemObject.put(Constants.MaterialNo, singleRow.get(Constants.MaterialNo));
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        itemObject.put(Constants.MaterialDesc, singleRow.get(Constants.MaterialDesc));
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (singleRow.has(Constants.SELECTED_UOM) && !TextUtils.isEmpty(singleRow.get(Constants.SELECTED_UOM).toString())) {
                            itemObject.put(Constants.UOM, singleRow.get(Constants.SELECTED_UOM));
                        } else if (singleRow.has(Constants.UOM)) {
                            itemObject.put(Constants.UOM, singleRow.get(Constants.UOM));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        itemObject.put(Constants.MRP, singleRow.get(Constants.MRP));
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        itemObject.put(Constants.NetAmount, singleRow.get(Constants.NetAmount));
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        itemObject.put(Constants.SecondaryTradeDiscount, singleRow.get(Constants.SecondaryTradeDiscount));
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        itemObject.put(Constants.SecondaryTradeDiscAmt, singleRow.get(Constants.SecondaryTradeDiscAmt));
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
//                itemObject.put(Constants.Tax, singleRow.get(Constants.Tax));
//                itemObject.put(Constants.GrossAmount, singleRow.get(Constants.GrossAmount));
                    try {
                        itemObject.put(Constants.Currency, singleRow.get(Constants.Currency));
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        itemObject.put(Constants.MFD, singleRow.get(Constants.MFD));
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        itemObject.put(Constants.ExpiryDate, singleRow.get(Constants.ExpiryDate));
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        itemObject.put(Constants.Batch, singleRow.get(Constants.Batch));
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        itemObject.put(Constants.InvoiceDate, singleRow.get(Constants.InvoiceDate));
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
               /* try {
                    itemObject.put(Constants.StockRefGUID, singleRow.get(Constants.StockRefGUID));
                } catch (Throwable e) {
                    e.printStackTrace();
                }*/
                    if (!TextUtils.isEmpty(singleRow.optString(Constants.BeatGUID))) {
                        itemObject.put(Constants.BeatGUID, singleRow.get(Constants.BeatGUID));
                    }
                    try {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.AlternativeUOM1))) {
                            itemObject.put(Constants.AlternativeUOM1, singleRow.get(Constants.AlternativeUOM1));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.Batch))) {
                            itemObject.put(Constants.Batch, singleRow.get(Constants.Batch));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.AlternativeUOM1Den))) {
                            itemObject.put(Constants.AlternativeUOM1Den, singleRow.get(Constants.AlternativeUOM1Den));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.AlternativeUOM1Den))) {
                            itemObject.put(Constants.AlternativeUOM1Den, singleRow.get(Constants.AlternativeUOM1Den));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.DiscChngMnul))) {
                            itemObject.put(Constants.DiscChngMnul, "false");
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.GrossAmount))) {
                            itemObject.put(Constants.GrossAmount, singleRow.get(Constants.GrossAmount));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.HsnCode))) {
                            itemObject.put(Constants.HsnCode, singleRow.get(Constants.HsnCode));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.SPName))) {
                            itemObject.put(Constants.SPName, singleRow.get(Constants.SPName));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!TextUtils.isEmpty(singleRow.optString("SPGuid"))) {
                            itemObject.put("SPGuid", singleRow.get("SPGuid"));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.Tax1))) {
                            itemObject.put(Constants.Tax1, singleRow.get(Constants.Tax1));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.Tax2))) {
                            itemObject.put(Constants.Tax2, singleRow.get(Constants.Tax2));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    try {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.TaxableAmount))) {
                            itemObject.put(Constants.TaxableAmount, singleRow.get(Constants.TaxableAmount));
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

                    if (singleRow.optString(Constants.RefDocItmGUID) != null && !TextUtils.isEmpty(singleRow.optString(Constants.RefDocItmGUID))) {
                        itemObject.put(Constants.RefDocItmGUID, singleRow.get(Constants.RefDocItmGUID));
                    }
                    try {
                        JSONArray subjsonArray = new JSONArray();
                        String docsSplitItems = singleRow.getString(Constants.INVOICE_ITEM);
                        JSONArray docsSplitArray = new JSONArray(docsSplitItems);
                        for (int incrVal = 0; incrVal < docsSplitArray.length(); incrVal++) {
                            JSONObject subSingleRow = docsSplitArray.getJSONObject(incrVal);
                            JSONObject subItemObject = new JSONObject();
                            subItemObject.put(Constants.TXNLnkGuid, subSingleRow.get(Constants.TXNLnkGuid));
                            subItemObject.put(Constants.PreTXNType, subSingleRow.get(Constants.PreTXNType));
                            subItemObject.put(Constants.PreTXNRef, subSingleRow.get(Constants.PreTXNRef));
                            subItemObject.put(Constants.SubsTXNType, subSingleRow.get(Constants.SubsTXNType));
                            subItemObject.put(Constants.SubsTXNRef, subSingleRow.get(Constants.SubsTXNRef));
                            subItemObject.put(Constants.PreTXNQty, subSingleRow.get(Constants.PreTXNQty));
                            subItemObject.put(Constants.PreTXNUom, subSingleRow.get(Constants.PreTXNUom));
                            subItemObject.put(Constants.PreTXNCurrency, subSingleRow.get(Constants.PreTXNCurrency));
                            subjsonArray.put(subItemObject);
                        }
                        itemObject.put(Constants.SSInvoiceItemTXNLinks, subjsonArray);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(itemObject);
                }
                dbHeadTable.put(Constants.SSInvoiceItemDetails, jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return dbHeadTable;
        }

    public static JSONObject getSSInvoiceHeaderValues(JSONObject
                                                              fetchJsonHeaderObject) {
        JSONObject dbHeadTable = new JSONObject();
        try {
            try {
                dbHeadTable.put(Constants.BeatGUID, fetchJsonHeaderObject.getString(Constants.BeatGUID));
            } catch (Throwable e) {
                dbHeadTable.put(Constants.BeatGUID, "");
            }
            try {
                dbHeadTable.put(Constants.BeatCode, fetchJsonHeaderObject.getString(Constants.BeatCode));
            } catch (Throwable e) {
                dbHeadTable.put(Constants.BeatCode, "");
            }
            try {
                dbHeadTable.put(Constants.Remarks, fetchJsonHeaderObject.getString(Constants.Remarks));
            } catch (Throwable e) {
                dbHeadTable.put(Constants.Remarks, "");
            }
            try {
                dbHeadTable.put(Constants.TestRun, fetchJsonHeaderObject.getString(Constants.TestRun));
            } catch (Throwable e) {
                dbHeadTable.put(Constants.TestRun, "");
            }
            try {
                dbHeadTable.put(Constants.SCHApplyReg, fetchJsonHeaderObject.getString(Constants.SCHApplyReg));
            } catch (Throwable e) {
                dbHeadTable.put(Constants.SCHApplyReg, "");
            }
            try {
                dbHeadTable.put(Constants.BeatDesc, fetchJsonHeaderObject.getString(Constants.BeatDesc));
            } catch (Throwable e) {
                dbHeadTable.put(Constants.BeatDesc, "");
            }
            try {
                dbHeadTable.put(Constants.InvoiceNo, fetchJsonHeaderObject.getString(Constants.InvoiceNo));
            } catch (Throwable e) {
            }
            try {
                dbHeadTable.put(Constants.BillToDesc, fetchJsonHeaderObject.getString(Constants.BillToDesc));
            } catch (Throwable e) {
                dbHeadTable.put(Constants.BillToDesc, "");
            }
            try {
                dbHeadTable.put(Constants.BillToID, fetchJsonHeaderObject.getString(Constants.BillToID));
            } catch (Throwable e) {
                dbHeadTable.put(Constants.BillToID, "");
            }
            try {
                dbHeadTable.put(Constants.CPGroup1, fetchJsonHeaderObject.getString(Constants.CPGroup1));
            } catch (Throwable e) {
                dbHeadTable.put(Constants.CPGroup1, "");
            }
            try {
                dbHeadTable.put(Constants.CPGroup3, fetchJsonHeaderObject.getString(Constants.CPGroup3));
            } catch (Throwable e) {
                dbHeadTable.put(Constants.CPGroup3, "");
            }
            try {
                dbHeadTable.put(Constants.CPGroup4, fetchJsonHeaderObject.getString(Constants.CPGroup4));
            } catch (Throwable e) {
                dbHeadTable.put(Constants.CPGroup4, "");
            }
            try {
                dbHeadTable.put(Constants.Currency, fetchJsonHeaderObject.getString(Constants.Currency));
            } catch (Throwable e) {
                dbHeadTable.put(Constants.Currency, "");
            }
            try {
                dbHeadTable.put(Constants.InvoiceGUID, fetchJsonHeaderObject.getString(Constants.InvoiceGUID));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                REPEATABLE_REQUEST_ID = fetchJsonHeaderObject.getString(Constants.InvoiceGUID).replace("-", "");
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.Currency, fetchJsonHeaderObject.getString(Constants.Currency));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.DeliveryDate, fetchJsonHeaderObject.getString(Constants.DeliveryDate));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.GrossAmount, fetchJsonHeaderObject.getString(Constants.GrossAmount));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.InvoiceTypeDesc, fetchJsonHeaderObject.getString(Constants.InvoiceTypeDesc));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.InvoiceValue, fetchJsonHeaderObject.getString(Constants.InvoiceValue));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.PaymentModeDesc, fetchJsonHeaderObject.getString(Constants.PaymentModeDesc));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.PaymentModeID, fetchJsonHeaderObject.getString(Constants.PaymentModeID));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.PaymentStatusID, fetchJsonHeaderObject.getString(Constants.PaymentStatusID));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.RoundOff, fetchJsonHeaderObject.getString(Constants.RoundOff));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.SchemeReqd, fetchJsonHeaderObject.getString(Constants.SchemeReqd));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.SPGUID, fetchJsonHeaderObject.getString(Constants.SPGUID));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.SPName, fetchJsonHeaderObject.getString(Constants.SPName));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.SPNo, fetchJsonHeaderObject.getString(Constants.SPNo));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.Tax, fetchJsonHeaderObject.getString(Constants.Tax));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.Tax1, fetchJsonHeaderObject.getString(Constants.Tax1));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.Tax2, fetchJsonHeaderObject.getString(Constants.Tax2));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.TaxableAmount, fetchJsonHeaderObject.getString(Constants.TaxableAmount));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.CPGUID, fetchJsonHeaderObject.getString(Constants.CPGUID));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.CPNo, fetchJsonHeaderObject.getString(Constants.CPNo));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.CPName, fetchJsonHeaderObject.getString(Constants.CPName));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.CPTypeDesc, fetchJsonHeaderObject.getString(Constants.CPTypeDesc));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.CPTypeID, fetchJsonHeaderObject.getString(Constants.CPTypeID));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
//                dbHeadTable.put(Constants.InvoiceNo, fetchJsonHeaderObject.getString(Constants.InvoiceNo));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.InvoiceTypeID, fetchJsonHeaderObject.getString(Constants.InvoiceTypeID));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.InvoiceDate, fetchJsonHeaderObject.getString(Constants.InvoiceDate));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.PODate, fetchJsonHeaderObject.getString(Constants.PODate));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.SoldToCPGUID, fetchJsonHeaderObject.getString(Constants.SoldToCPGUID));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.SoldToID, fetchJsonHeaderObject.getString(Constants.SoldToID));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.SoldToName, fetchJsonHeaderObject.getString(Constants.SoldToName));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.SoldToTypeID, fetchJsonHeaderObject.getString(Constants.SoldToTypeID));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.ShipToCPGUID, fetchJsonHeaderObject.getString(Constants.ShipToCPGUID));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.ShipToID, fetchJsonHeaderObject.getString(Constants.ShipToID));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.ShipToName, fetchJsonHeaderObject.getString(Constants.ShipToName));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.ShipToTypeID, fetchJsonHeaderObject.getString(Constants.ShipToTypeID));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.TestRun, fetchJsonHeaderObject.getString(Constants.TestRun));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.BillTo, fetchJsonHeaderObject.getString(Constants.BillTo));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.Source, fetchJsonHeaderObject.getString(Constants.Source));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.getString(Constants.StatusID).equalsIgnoreCase("X")) {
                    dbHeadTable.put(Constants.StatusID, "01");
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.NetAmount, fetchJsonHeaderObject.getString(Constants.NetAmount));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.CashDiscAmount, fetchJsonHeaderObject.getString(Constants.CashDiscAmount));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.CashDiscPerc, fetchJsonHeaderObject.getString(Constants.CashDiscPerc));
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.Latitude)) {
                    dbHeadTable.put(Constants.Latitude, fetchJsonHeaderObject.getString(Constants.Latitude));
                }
                if (fetchJsonHeaderObject.has(Constants.Longitude)) {
                    dbHeadTable.put(Constants.Longitude, fetchJsonHeaderObject.getString(Constants.Longitude));
                }
                if (fetchJsonHeaderObject.has(Constants.InvTime)) {
                    dbHeadTable.put(Constants.InvTime, fetchJsonHeaderObject.getString(Constants.InvTime));
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            JSONArray itemsArray = new JSONArray(fetchJsonHeaderObject.getString(Constants.ITEM_TXT));
            JSONArray jsonArray = new JSONArray();
            for (int incrementVal = 0; incrementVal < itemsArray.length(); incrementVal++) {
                JSONObject singleRow = itemsArray.getJSONObject(incrementVal);
                JSONObject itemObject = new JSONObject();
                try {
                    itemObject.put(Constants.InvoiceItemGUID, singleRow.get(Constants.InvoiceItemGUID));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    itemObject.put(Constants.InvoiceGUID, singleRow.get(Constants.InvoiceGUID));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
//                    itemObject.put(Constants.StockGuid, singleRow.get(Constants.StockGuid));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    if (singleRow.has(Constants.ISFreeGoodsItem)) {
                        itemObject.put(Constants.ISFreeGoodsItem, singleRow.get(Constants.ISFreeGoodsItem));
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    if (singleRow.has(Constants.ItemCat)) {
                        itemObject.put(Constants.ItemCat, singleRow.get(Constants.ItemCat));
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    if (singleRow.has(Constants.CashDiscountPer)) {
                        itemObject.put(Constants.CashDiscountPer, singleRow.get(Constants.CashDiscountPer));
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    itemObject.put(Constants.ItemNo, singleRow.get(Constants.ItemNo));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
//                itemObject.put(Constants.InvoiceNo, singleRow.get(Constants.InvoiceNo));
                try {
                    itemObject.put(Constants.Remarks, singleRow.get(Constants.Remarks));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    itemObject.put(Constants.Quantity, singleRow.get(Constants.Quantity));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    itemObject.put(Constants.MaterialNo, singleRow.get(Constants.MaterialNo));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    itemObject.put(Constants.MaterialDesc, singleRow.get(Constants.MaterialDesc));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    /*if (singleRow.has(Constants.SELECTED_UOM) && !TextUtils.isEmpty(singleRow.get(Constants.SELECTED_UOM).toString())) {
                        itemObject.put(Constants.UOM, singleRow.get(Constants.SELECTED_UOM));
                    } else*/
                    if (singleRow.has(Constants.UOM)) {
                        itemObject.put(Constants.UOM, singleRow.get(Constants.UOM));
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    itemObject.put(Constants.MRP, singleRow.get(Constants.MRP));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    itemObject.put(Constants.NetAmount, singleRow.get(Constants.NetAmount));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    itemObject.put(Constants.SecondaryTradeDiscount, singleRow.get(Constants.SecondaryTradeDiscount));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    itemObject.put(Constants.SecondaryTradeDiscAmt, singleRow.get(Constants.SecondaryTradeDiscAmt));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
//                itemObject.put(Constants.Tax, singleRow.get(Constants.Tax));
//                itemObject.put(Constants.GrossAmount, singleRow.get(Constants.GrossAmount));
                try {
                    itemObject.put(Constants.Currency, singleRow.get(Constants.Currency));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    itemObject.put(Constants.MFD, singleRow.get(Constants.MFD));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    itemObject.put(Constants.ExpiryDate, singleRow.get(Constants.ExpiryDate));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    itemObject.put(Constants.Batch, singleRow.get(Constants.Batch));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    itemObject.put(Constants.InvoiceDate, singleRow.get(Constants.InvoiceDate));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
               /* try {
                    itemObject.put(Constants.StockRefGUID, singleRow.get(Constants.StockRefGUID));
                } catch (Throwable e) {
                    e.printStackTrace();
                }*/
                if (!TextUtils.isEmpty(singleRow.optString(Constants.BeatGUID))) {
                    itemObject.put(Constants.BeatGUID, singleRow.get(Constants.BeatGUID));
                }
                try {
                    if (!TextUtils.isEmpty(singleRow.optString(Constants.AlternativeUOM1))) {
                        itemObject.put(Constants.AlternativeUOM1, singleRow.get(Constants.AlternativeUOM1));
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    if (!TextUtils.isEmpty(singleRow.optString(Constants.Batch))) {
                        itemObject.put(Constants.Batch, singleRow.get(Constants.Batch));
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    if (!TextUtils.isEmpty(singleRow.optString(Constants.AlternativeUOM1Den))) {
                        itemObject.put(Constants.AlternativeUOM1Den, singleRow.get(Constants.AlternativeUOM1Den));
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    if (!TextUtils.isEmpty(singleRow.optString(Constants.AlternativeUOM1Den))) {
                        itemObject.put(Constants.AlternativeUOM1Den, singleRow.get(Constants.AlternativeUOM1Den));
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    if (!TextUtils.isEmpty(singleRow.optString(Constants.DiscChngMnul))) {
                        itemObject.put(Constants.DiscChngMnul, "false");
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    if (!TextUtils.isEmpty(singleRow.optString(Constants.GrossAmount))) {
                        itemObject.put(Constants.GrossAmount, singleRow.get(Constants.GrossAmount));
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    if (!TextUtils.isEmpty(singleRow.optString(Constants.HsnCode))) {
                        itemObject.put(Constants.HsnCode, singleRow.get(Constants.HsnCode));
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    if (!TextUtils.isEmpty(singleRow.optString(Constants.SPName))) {
                        itemObject.put(Constants.SPName, singleRow.get(Constants.SPName));
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    if (!TextUtils.isEmpty(singleRow.optString("SPGuid"))) {
                        itemObject.put("SPGuid", singleRow.get("SPGuid"));
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    if (!TextUtils.isEmpty(singleRow.optString(Constants.Tax1))) {
                        itemObject.put(Constants.Tax1, singleRow.get(Constants.Tax1));
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    if (!TextUtils.isEmpty(singleRow.optString(Constants.Tax2))) {
                        itemObject.put(Constants.Tax2, singleRow.get(Constants.Tax2));
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    if (!TextUtils.isEmpty(singleRow.optString(Constants.TaxableAmount))) {
                        itemObject.put(Constants.TaxableAmount, singleRow.get(Constants.TaxableAmount));
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }

                if (singleRow.optString(Constants.RefDocItmGUID) != null && !TextUtils.isEmpty(singleRow.optString(Constants.RefDocItmGUID))) {
                    itemObject.put(Constants.RefDocItmGUID, singleRow.get(Constants.RefDocItmGUID));
                }
                try {
                    JSONArray subjsonArray = new JSONArray();
                    String docsSplitItems = singleRow.getString(Constants.INVOICE_ITEM);
                    JSONArray docsSplitArray = new JSONArray(docsSplitItems);
                    for (int incrVal = 0; incrVal < docsSplitArray.length(); incrVal++) {
                        JSONObject subSingleRow = docsSplitArray.getJSONObject(incrVal);
                        JSONObject subItemObject = new JSONObject();
                        subItemObject.put(Constants.TXNLnkGuid, subSingleRow.get(Constants.TXNLnkGuid));
                        subItemObject.put(Constants.PreTXNType, subSingleRow.get(Constants.PreTXNType));
                        subItemObject.put(Constants.PreTXNRef, subSingleRow.get(Constants.PreTXNRef));
                        subItemObject.put(Constants.SubsTXNType, subSingleRow.get(Constants.SubsTXNType));
                        subItemObject.put(Constants.SubsTXNRef, subSingleRow.get(Constants.SubsTXNRef));
                        subItemObject.put(Constants.PreTXNQty, subSingleRow.get(Constants.PreTXNQty));
                        subItemObject.put(Constants.PreTXNUom, subSingleRow.get(Constants.PreTXNUom));
                        subItemObject.put(Constants.PreTXNCurrency, subSingleRow.get(Constants.PreTXNCurrency));
                        subjsonArray.put(subItemObject);
                    }
                    itemObject.put(Constants.SSInvoiceItemTXNLinks, subjsonArray);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                jsonArray.put(itemObject);
            }
            dbHeadTable.put(Constants.SSInvoiceItemDetails, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dbHeadTable;
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

        public static String ComplaintDate = "ComplaintDate";
        public static String ComplaintStatusID = "ComplaintStatusID";
        public static String ComplaintStatusDesc = "ComplaintStatusDesc";

        public static String ComplaintNo = "ComplaintNo";
        public static String ComplaintGUID = "ComplaintGUID";
        public static String ComplaintPriorityID = "ComplaintPriorityID";
        public static String ComplaintPriorityDesc = "ComplaintPriorityDesc";
        public static String ComplaintTypeDesc = "ComplaintTypeDesc";
        public static String ComplaintTypeID = "ComplaintTypeID";
        public static String ComplainCategoryDesc = "ComplainCategoryDesc";
        public static String ComplaintCategoryID = "ComplaintCategoryID";
        public static final String str_false = "false";
        public static final String Complaints = "Complaints";
        public static final String COMPLAINTLISTMODEL = "complaintlistmodel";

        public static String PerformanceOnID = "PerformanceOnID";
        public static String PerformanceGUID = "PerformanceGUID";
        public static String AmtTarget = "AmtTarget";
        public static String AmtLMTD = "AmtLMTD";
        public static String AmtMTD = "AmtMTD";
        public static String AmtMonth1PrevPerf = "AmtMonth1PrevPerf";
        public static String AmtMonthlyGrowth = "AmtMonthlyGrowth";
        public static String AmtMonth2PrevPerf = "AmtMonth2PrevPerf";
        public static String AmtMonth3PrevPerf = "AmtMonth3PrevPerf";
        public static String AmtLastYearMTD = "AmtLastYearMTD";
        public static String QtyLastYearMTD = "QtyLastYearMTD";
        public static String QtyTarget = "QtyTarget";
        public static String QtyLMTD = "QtyLMTD";
        public static String QtyMTD = "QtyMTD";
        public static String QtyMonthlyGrowth = "QtyMonthlyGrowth";
        public static final String OtherRouteList = "OtherRouteList";
        public static final String OUTLET_LIST = "OUTLET_LIST";
        public static SQLiteDatabase EventUserHandler;
        public static final String SYNC_TABLE = "SyncTable";
        public static boolean checkSearch = false;
        public static boolean addedSeaerch = false;

        public static final String getLastSyncTimeStamp (String tableName, String columnName, String
        columnValue){
            return "select *  from  " + tableName + " Where " + columnName + "='" + columnValue + "'  ;";

        }


        public static final String CPList = "CPList";
        public static final String StateDesc = "StateDesc";



        public static ArrayList<String> getPendingMerchList (Context context, String createType){
            ArrayList<String> devMerList = new ArrayList<>();
            Set<String> set = new HashSet<>();
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);
            set = sharedPreferences.getStringSet(createType, null);
            if (set != null && !set.isEmpty()) {
                Iterator itr = set.iterator();
                while (itr.hasNext()) {
                    devMerList.add(itr.next().toString());
                }
            }

            return devMerList;
        }
        public static void deleteDeviceMerchansisingFromDataVault (Context context){
            ArrayList<String> alDeviceMerList = Constants.getPendingMerchList(context, Constants.MerchList);
            if (alDeviceMerList != null && alDeviceMerList.size() > 0) {
                for (int incVal = 0; incVal < alDeviceMerList.size(); incVal++) {
                    try {
                        if (!OfflineManager.getVisitStatusForCustomer(Constants.MerchReviews +
                                "?$filter=sap.islocal() and " + Constants.MerchReviewGUID + " eq guid'" + alDeviceMerList.get(incVal) + "'")) {
                            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);
                            Constants.removeDeviceDocNoFromSharedPref(context, Constants.MerchList, alDeviceMerList.get(incVal), sharedPreferences, false);
                            ConstantsUtils.storeInDataVault(alDeviceMerList.get(incVal), "", context);
                        }
                    } catch (OfflineODataStoreException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

    public static JSONObject getCPHeaderValuesFromJsonObject (JSONObject fetchJsonHeaderObject, Context context){
        JSONObject dbHeadTable = new JSONObject();
        try {
            dbHeadTable.put(Constants.CPGUID, fetchJsonHeaderObject.getString(Constants.CPGUID));
            REPEATABLE_REQUEST_ID = fetchJsonHeaderObject.getString(Constants.CPGUID).replace("-", "");
            try {
                if (fetchJsonHeaderObject.has(Constants.CPNo)) {
                    dbHeadTable.put(Constants.CPNo, fetchJsonHeaderObject.getString(Constants.CPNo));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.PartnerMgrName)) {
                    dbHeadTable.put(Constants.PartnerMgrName, fetchJsonHeaderObject.getString(Constants.PartnerMgrName));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.PartnerMgrNo)) {
                    dbHeadTable.put(Constants.PartnerMgrNo, fetchJsonHeaderObject.getString(Constants.PartnerMgrNo));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.Name)) {
                    try {
                        if (fetchJsonHeaderObject.getString(Constants.Name).length() <= 35) {
                            dbHeadTable.put(Constants.Name, fetchJsonHeaderObject.getString(Constants.Name));
                        } else {
                            dbHeadTable.put(Constants.Name, fetchJsonHeaderObject.getString(Constants.Name).substring(0, 34));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.CPUID)) {
                    dbHeadTable.put(Constants.CPUID, fetchJsonHeaderObject.getString(Constants.CPUID));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.AccountGrp)) {
                    dbHeadTable.put(Constants.AccountGrp, fetchJsonHeaderObject.getString(Constants.AccountGrp));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.Mobile3)) {
                    dbHeadTable.put(Constants.Mobile3, fetchJsonHeaderObject.getString(Constants.Mobile3));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.Latitude)) {
                    if (!TextUtils.isEmpty(fetchJsonHeaderObject.getString(Constants.Latitude))) {
                        dbHeadTable.put(Constants.Latitude, fetchJsonHeaderObject.getString(Constants.Latitude));
                    } else {
                        dbHeadTable.put(Constants.Latitude, "0.0");
                    }
                }
                if (fetchJsonHeaderObject.has(Constants.Longitude)) {
                    if (!TextUtils.isEmpty(fetchJsonHeaderObject.getString(Constants.Longitude))) {
                        dbHeadTable.put(Constants.Longitude, fetchJsonHeaderObject.getString(Constants.Longitude));
                    } else {
                        dbHeadTable.put(Constants.Latitude, "0.0");
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
//            dbHeadTable.put(Constants.ParentID, fetchJsonHeaderObject.getString(Constants.ParentID));
//            dbHeadTable.put(Constants.ParentName, fetchJsonHeaderObject.getString(Constants.ParentName));
            try {
                if (fetchJsonHeaderObject.has(Constants.SearchTerm)) {
                    dbHeadTable.put(Constants.SearchTerm, fetchJsonHeaderObject.getString(Constants.SearchTerm));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            dbHeadTable.put(Constants.ParentTypeID, fetchJsonHeaderObject.getString(Constants.ParentTypeID));
//            dbHeadTable.put(Constants.ParentTypDesc, fetchJsonHeaderObject.getString(Constants.ParentTypDesc));
            try {
                if (fetchJsonHeaderObject.has(Constants.CPTypeID)) {
                    dbHeadTable.put(Constants.CPTypeID, fetchJsonHeaderObject.getString(Constants.CPTypeID));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.CPTypeDesc)) {
                    dbHeadTable.put(Constants.CPTypeDesc, fetchJsonHeaderObject.getString(Constants.CPTypeDesc));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.Group1)) {
                    dbHeadTable.put(Constants.Group1, fetchJsonHeaderObject.getString(Constants.Group1));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.Group2)) {
                    dbHeadTable.put(Constants.Group2, fetchJsonHeaderObject.getString(Constants.Group2));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.Group3)) {
                    dbHeadTable.put(Constants.Group3, fetchJsonHeaderObject.getString(Constants.Group3));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.Group4)) {
                    dbHeadTable.put(Constants.Group4, fetchJsonHeaderObject.getString(Constants.Group4));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.PAN)) {
                    dbHeadTable.put(Constants.PAN, fetchJsonHeaderObject.getString(Constants.PAN));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            dbHeadTable.put(Constants.CPStock, fetchJsonHeaderObject.getString(Constants.CPStock));
            try {
                if (fetchJsonHeaderObject.has(Constants.UOM)) {
                    dbHeadTable.put(Constants.UOM, fetchJsonHeaderObject.getString(Constants.UOM));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.DOB)) {
                    dbHeadTable.put(Constants.DOB, fetchJsonHeaderObject.getString(Constants.DOB));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            /*try {
                dbHeadTable.put(Constants.Anniversary, fetchJsonHeaderObject.getString(Constants.Anniversary));
            } catch (JSONException e) {
                e.printStackTrace();
            }*/

            try {
                if (fetchJsonHeaderObject.has(Constants.ActivationDate)) {
                    dbHeadTable.put(Constants.ActivationDate, fetchJsonHeaderObject.getString(Constants.ActivationDate));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.GSTActiveDate)) {
                    dbHeadTable.put(Constants.GSTActiveDate, fetchJsonHeaderObject.getString(Constants.GSTActiveDate));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.Address1)) {
                    try {
                        if (fetchJsonHeaderObject.getString(Constants.Address1).length() <= 35) {
                            dbHeadTable.put(Constants.Address1, fetchJsonHeaderObject.getString(Constants.Address1));
                        } else {
                            dbHeadTable.put(Constants.Address1, fetchJsonHeaderObject.getString(Constants.Address1).substring(0, 34));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.Address2)) {
                    try {
                        if (fetchJsonHeaderObject.getString(Constants.Address2).length() <= 35) {
                            dbHeadTable.put(Constants.Address2, fetchJsonHeaderObject.getString(Constants.Address2));
                        } else {
                            dbHeadTable.put(Constants.Address2, fetchJsonHeaderObject.getString(Constants.Address2).substring(0, 34));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.Landmark)) {
                    dbHeadTable.put(Constants.Landmark, fetchJsonHeaderObject.getString(Constants.Landmark));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.ZoneID)) {
                    dbHeadTable.put(Constants.ZoneID, fetchJsonHeaderObject.getString(Constants.ZoneID));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.ZoneDesc)) {
                    dbHeadTable.put(Constants.ZoneDesc, fetchJsonHeaderObject.getString(Constants.ZoneDesc));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.TownID)) {
                    dbHeadTable.put(Constants.TownID, fetchJsonHeaderObject.getString(Constants.TownID));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.TownGuid)) {
                    dbHeadTable.put(Constants.TownGuid, fetchJsonHeaderObject.getString(Constants.TownGuid));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
           /* if (fetchJsonHeaderObject.has(Constants.WardGUID)&&!TextUtils.isEmpty(fetchJsonHeaderObject.getString(Constants.WardGUID))) {
                dbHeadTable.put(Constants.WardGUID, fetchJsonHeaderObject.getString(Constants.WardGUID));
            }*/
            try {
                if (fetchJsonHeaderObject.has(Constants.SubDistrictGUID) && !TextUtils.isEmpty(fetchJsonHeaderObject.getString(Constants.SubDistrictGUID))) {
                    dbHeadTable.put(Constants.SubDistrictGUID, fetchJsonHeaderObject.getString(Constants.SubDistrictGUID));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.TownDesc)) {
                    dbHeadTable.put(Constants.TownDesc, fetchJsonHeaderObject.getString(Constants.TownDesc));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.CityID)) {
                    dbHeadTable.put(Constants.CityID, fetchJsonHeaderObject.getString(Constants.CityID));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.CityDesc)) {
                    dbHeadTable.put(Constants.CityDesc, fetchJsonHeaderObject.getString(Constants.CityDesc));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.StateID)) {
                    dbHeadTable.put(Constants.StateID, fetchJsonHeaderObject.getString(Constants.StateID));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.StateDesc)) {
                    dbHeadTable.put(Constants.StateDesc, fetchJsonHeaderObject.getString(Constants.StateDesc));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.DistrictID)) {
                    dbHeadTable.put(Constants.DistrictID, fetchJsonHeaderObject.getString(Constants.DistrictID));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.DistrictDesc)) {
                    dbHeadTable.put(Constants.DistrictDesc, fetchJsonHeaderObject.getString(Constants.DistrictDesc));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.Country)) {
                    dbHeadTable.put(Constants.Country, fetchJsonHeaderObject.getString(Constants.Country));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.CountryName)) {
                    dbHeadTable.put(Constants.CountryName, fetchJsonHeaderObject.getString(Constants.CountryName));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.PostalCode)) {
                    dbHeadTable.put(Constants.PostalCode, fetchJsonHeaderObject.getString(Constants.PostalCode));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.Mobile1)) {
                    dbHeadTable.put(Constants.Mobile1, fetchJsonHeaderObject.getString(Constants.Mobile1));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.Mobile2)) {
                    dbHeadTable.put(Constants.Mobile2, fetchJsonHeaderObject.getString(Constants.Mobile2));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.Landline)) {
                    dbHeadTable.put(Constants.Landline, fetchJsonHeaderObject.getString(Constants.Landline));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.EmailID)) {
                    dbHeadTable.put(Constants.EmailID, fetchJsonHeaderObject.getString(Constants.EmailID));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.Currency)) {
                    dbHeadTable.put(Constants.Currency, fetchJsonHeaderObject.getString(Constants.Currency));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            dbHeadTable.put(Constants.CreditLimit, fetchJsonHeaderObject.getString(Constants.CreditLimit));
//            dbHeadTable.put(Constants.CreditDays, fetchJsonHeaderObject.getString(Constants.CreditDays));
            try {
                if (fetchJsonHeaderObject.has(Constants.StatusID)) {
                    dbHeadTable.put(Constants.StatusID, fetchJsonHeaderObject.getString(Constants.StatusID));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.StatusDesc)) {
                    dbHeadTable.put(Constants.StatusDesc, fetchJsonHeaderObject.getString(Constants.StatusDesc));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.ApprvlStatusID)) {
                    dbHeadTable.put(Constants.ApprvlStatusID, fetchJsonHeaderObject.getString(Constants.ApprvlStatusID));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.ApprvlStatusDesc)) {
                    dbHeadTable.put(Constants.ApprvlStatusDesc, fetchJsonHeaderObject.getString(Constants.ApprvlStatusDesc));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.OwnerName)) {
                    try {
                        if (fetchJsonHeaderObject.getString(Constants.OwnerName).length() <= 35) {
                            dbHeadTable.put(Constants.OwnerName, fetchJsonHeaderObject.getString(Constants.OwnerName));
                        } else {
                            dbHeadTable.put(Constants.OwnerName, fetchJsonHeaderObject.getString(Constants.OwnerName).substring(0, 34));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.SalesOfficeID)) {
                    dbHeadTable.put(Constants.SalesOfficeID, fetchJsonHeaderObject.getString(Constants.SalesOfficeID));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.SalesGrpDesc)) {
                    dbHeadTable.put(Constants.SalesGrpDesc, fetchJsonHeaderObject.getString(Constants.SalesGrpDesc));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.SalesGroupID)) {
                    dbHeadTable.put(Constants.SalesGroupID, fetchJsonHeaderObject.getString(Constants.SalesGroupID));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.SalesOffDesc)) {
                    dbHeadTable.put(Constants.SalesOffDesc, fetchJsonHeaderObject.getString(Constants.SalesOffDesc));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.IsKeyCP)) {
                    dbHeadTable.put(Constants.IsKeyCP, fetchJsonHeaderObject.getString(Constants.IsKeyCP));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.WeeklyOff)) {
                    dbHeadTable.put(Constants.WeeklyOff, fetchJsonHeaderObject.getString(Constants.WeeklyOff));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.WeeklyOffDesc)) {
                    dbHeadTable.put(Constants.WeeklyOffDesc, fetchJsonHeaderObject.getString(Constants.WeeklyOffDesc));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.FaxNo)) {
                    dbHeadTable.put(Constants.FaxNo, fetchJsonHeaderObject.getString(Constants.FaxNo));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.BusinessID1)) {
                    dbHeadTable.put(Constants.BusinessID1, fetchJsonHeaderObject.getString(Constants.BusinessID1));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                String rollID = ConstantsUtils.getAuthInformation(context);
                if (rollID.equalsIgnoreCase(Constants.PSM)) {
                    dbHeadTable.put(Constants.Source, "PSM");
                } else {
                    dbHeadTable.put(Constants.Source, "MOBILE");
                }
            } catch (JSONException e) {
                dbHeadTable.put(Constants.Source, "MOBILE");
                LogManager.writeLogError("Retailer mobile no error : " + e.getMessage());
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.Tax1)) {
                    dbHeadTable.put(Constants.Tax1, fetchJsonHeaderObject.getString(Constants.Tax1));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.OutletSizeId)) {
                    dbHeadTable.put(Constants.OutletSizeId, fetchJsonHeaderObject.getString(Constants.OutletSizeId));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.OutletShapeId)) {
                    dbHeadTable.put(Constants.OutletShapeId, fetchJsonHeaderObject.getString(Constants.OutletShapeId));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.NoOfEmployee)) {
                    dbHeadTable.put(Constants.NoOfEmployee, fetchJsonHeaderObject.getString(Constants.NoOfEmployee));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if (fetchJsonHeaderObject.has(Constants.OutletsCovered)) {
                    dbHeadTable.put(Constants.OutletsCovered, fetchJsonHeaderObject.getString(Constants.OutletsCovered));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if (fetchJsonHeaderObject.has(Constants.WsCovered)) {
                    dbHeadTable.put(Constants.WsCovered, fetchJsonHeaderObject.getString(Constants.WsCovered));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if (fetchJsonHeaderObject.has(Constants.NoOfSalesMan)) {
                    dbHeadTable.put(Constants.NoOfSalesMan, fetchJsonHeaderObject.getString(Constants.NoOfSalesMan));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.URL1)) {
                    dbHeadTable.put(Constants.URL1, fetchJsonHeaderObject.getString(Constants.URL1));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.URL2)) {
                    dbHeadTable.put(Constants.URL2, fetchJsonHeaderObject.getString(Constants.URL2));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.URL3)) {
                    dbHeadTable.put(Constants.URL3, fetchJsonHeaderObject.getString(Constants.URL3));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.URL4)) {
                    dbHeadTable.put(Constants.URL4, fetchJsonHeaderObject.getString(Constants.URL4));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.URL5)) {
                    dbHeadTable.put(Constants.URL5, fetchJsonHeaderObject.getString(Constants.URL5));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.URL6)) {
                    dbHeadTable.put(Constants.URL6, fetchJsonHeaderObject.getString(Constants.URL6));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.GeoAccuracy)) {
                    dbHeadTable.put(Constants.GeoAccuracy, fetchJsonHeaderObject.getString(Constants.GeoAccuracy));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.AddDetails)) {
                    dbHeadTable.put(Constants.AddDetails, fetchJsonHeaderObject.getString(Constants.AddDetails));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.TimeTaken)) {
                    dbHeadTable.put(Constants.TimeTaken, fetchJsonHeaderObject.getString(Constants.TimeTaken));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.TotalPotentialAmt)) {
                    dbHeadTable.put(Constants.TotalPotentialAmt, fetchJsonHeaderObject.getString(Constants.TotalPotentialAmt));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.ProspectStatusID)) {
                    dbHeadTable.put(Constants.ProspectStatusID, fetchJsonHeaderObject.getString(Constants.ProspectStatusID));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.PotentialType)) {
                    dbHeadTable.put(Constants.PotentialType, fetchJsonHeaderObject.getString(Constants.PotentialType));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.MobileVerifed)) {
                    if (fetchJsonHeaderObject.getString(Constants.MobileVerifed).equalsIgnoreCase("true")) {
                        dbHeadTable.put(Constants.MobileVerifed, "Y");
                    } else if (fetchJsonHeaderObject.getString(Constants.MobileVerifed).equalsIgnoreCase("false")) {
                        dbHeadTable.put(Constants.MobileVerifed, "N");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//            dbHeadTable.put(Constants.DeactivatedOn, fetchJsonHeaderObject.getString(Constants.DeactivatedOn));
            try {
                if (fetchJsonHeaderObject.has(Constants.TaxRegStatus)) {
                    dbHeadTable.put(Constants.TaxRegStatus, fetchJsonHeaderObject.getString(Constants.TaxRegStatus));
                }
                if (fetchJsonHeaderObject.has(Constants.TaxRegStatusDesc)) {
                    dbHeadTable.put(Constants.TaxRegStatusDesc, fetchJsonHeaderObject.getString(Constants.TaxRegStatusDesc));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.VerifiedBy, Constants.getSPGUID().replaceAll("-",""));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.VerificationDat, UtilConstants.getNewDateTimeFormat());
            } catch (Exception e) {
                e.printStackTrace();
            }

            JSONArray jsonArray = null;
            try {
                JSONArray itemsArray = new JSONArray(fetchJsonHeaderObject.getString(Constants.ITEM_TXT));
                jsonArray = new JSONArray();
                for (int incrementVal = 0; incrementVal < itemsArray.length(); incrementVal++) {
                    JSONObject singleRow = itemsArray.getJSONObject(incrementVal);
                    JSONObject itemObject = new JSONObject();
                    if (singleRow.has(Constants.CPGUID)) {
                        itemObject.put(Constants.CPGUID, singleRow.optString(Constants.CPGUID));
                    }
                    if (singleRow.has(Constants.CP1GUID)) {
                        itemObject.put(Constants.CP1GUID, singleRow.optString(Constants.CP1GUID));
                    }
                    if (singleRow.has(Constants.ParentID)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.ParentID))) {
                            itemObject.put(Constants.ParentID, singleRow.optString(Constants.ParentID));
                            if (singleRow.has(Constants.ParentTypeID)) {
                                if (!TextUtils.isEmpty(singleRow.optString(Constants.ParentTypeID))) {
                                    itemObject.put(Constants.ParentTypeID, singleRow.optString(Constants.ParentTypeID));
                                }
                            }
                            if (singleRow.has(Constants.ParentName)) {
                                if (!TextUtils.isEmpty(singleRow.optString(Constants.ParentName))) {
                                    itemObject.put(Constants.ParentName, singleRow.optString(Constants.ParentName));
                                }
                            }
                        }
                    }


                    if (singleRow.has(Constants.DMSDivision)) {
                        itemObject.put(Constants.DMSDivision, singleRow.optString(Constants.DMSDivision));
                    }
                    if (singleRow.has(Constants.DMSDivisionDesc)) {
                        itemObject.put(Constants.DMSDivisionDesc, singleRow.optString(Constants.DMSDivisionDesc));
                    }
                    if (fetchJsonHeaderObject.has(Constants.PartnerMgrGUID)) {
                        if (!TextUtils.isEmpty(fetchJsonHeaderObject.optString(Constants.PartnerMgrGUID))) {
                            itemObject.put(Constants.PartnerMgrGUID, fetchJsonHeaderObject.getString(Constants.PartnerMgrGUID));
                        }
                    }
                    if (singleRow.has(Constants.Currency)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.Currency))) {
                            itemObject.put(Constants.Currency, singleRow.optString(Constants.Currency));
                        }
                    }
                    if (singleRow.has(Constants.StatusID)) {
                        if (singleRow.optString(Constants.StatusID) != null) {
                            itemObject.put(Constants.StatusID, singleRow.optString(Constants.StatusID));
                        }
                    }
                    if (singleRow.has(Constants.SourceID)) {
                        if (singleRow.optString(Constants.SourceID) != null) {
                            itemObject.put(Constants.SourceID, singleRow.optString(Constants.SourceID));
                        }
                    }
                    if (singleRow.has(Constants.RouteGUID)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.RouteGUID))) {
                            itemObject.put(Constants.RouteGUID, singleRow.optString(Constants.RouteGUID));
                        }
                    }
                    if (singleRow.has(Constants.RouteID)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.RouteID))) {
                            itemObject.put(Constants.RouteID, singleRow.get(Constants.RouteID));
                        }
                    }
                    if (singleRow.has(Constants.Group1)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.Group1))) {
                            itemObject.put(Constants.Group1, singleRow.get(Constants.Group1));
                        }
                    }
                    if (singleRow.has(Constants.Group2)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.Group2))) {
                            itemObject.put(Constants.Group2, singleRow.get(Constants.Group2));
                        }
                    }

                    if (singleRow.has(Constants.Group3)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.Group3))) {
                            itemObject.put(Constants.Group3, singleRow.get(Constants.Group3));
                        }
                    }

                    if (singleRow.has(Constants.Group3Desc)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.Group3Desc))) {
                            itemObject.put(Constants.Group3Desc, singleRow.get(Constants.Group3Desc));
                        }
                    }

                    if (singleRow.has(Constants.FreqOfDispatch)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.FreqOfDispatch))) {
                            itemObject.put(Constants.FreqOfDispatch, singleRow.get(Constants.FreqOfDispatch));
                        }
                    }
                    if (singleRow.has(Constants.Group7)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.Group7))) {
                            itemObject.put(Constants.Group7, singleRow.get(Constants.Group7));
                        }
                    }
                    if (singleRow.has(Constants.Group8)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.Group8))) {
                            itemObject.put(Constants.Group8, singleRow.get(Constants.Group8));
                        }
                    }

                    if (singleRow.has(Constants.Group6)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.Group6))) {
                            itemObject.put(Constants.Group6, singleRow.get(Constants.Group6));
                        }
                    }
                    try {
                        if (singleRow.has(Constants.Group11)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.Group11))) {
                                itemObject.put(Constants.Group11, singleRow.get(Constants.Group11));
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

                    if (singleRow.has(Constants.Group9)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.Group9))) {
                            itemObject.put(Constants.Group9, singleRow.get(Constants.Group9));
                        }
                    }

                    if (singleRow.has(Constants.DistanceFromParent)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.DistanceFromParent))) {
                            itemObject.put(Constants.DistanceFromParent, singleRow.get(Constants.DistanceFromParent));
                        }
                    }

                    if (singleRow.has(Constants.DstnceFromPUOM)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.DstnceFromPUOM))) {
                            itemObject.put(Constants.DstnceFromPUOM, singleRow.get(Constants.DstnceFromPUOM));
                        }
                    }

                    if (singleRow.has(Constants.Day1)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.Day1))) {
                            itemObject.put(Constants.Day1, singleRow.get(Constants.Day1));
                        }
                    }

                    if (singleRow.has(Constants.Day2)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.Day2))) {
                            itemObject.put(Constants.Day2, singleRow.get(Constants.Day2));
                        }
                    }

                    if (singleRow.has(Constants.Day3)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.Day3))) {
                            itemObject.put(Constants.Day3, singleRow.get(Constants.Day3));
                        }
                    }

                    if (singleRow.has(Constants.Day4)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.Day4))) {
                            itemObject.put(Constants.Day4, singleRow.get(Constants.Day4));
                        }
                    }

                    if (singleRow.has(Constants.Day5)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.Day5))) {
                            itemObject.put(Constants.Day5, singleRow.get(Constants.Day5));
                        }
                    }

                    if (singleRow.has(Constants.Day6)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.Day6))) {
                            itemObject.put(Constants.Day6, singleRow.get(Constants.Day6));
                        }
                    }

                    if (singleRow.has(Constants.Day7)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.Day7))) {
                            itemObject.put(Constants.Day7, singleRow.get(Constants.Day7));
                        }
                    }

                    try {
                        itemObject.put(Constants.BillSeries, "03");
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(itemObject);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            dbHeadTable.put(Constants.CPDMSDivisions, jsonArray);

            //CPInfras
            JSONArray itemsInfraArray = new JSONArray(fetchJsonHeaderObject.getString(Constants.ITEM_TXT_INFRA));
            JSONArray jsonIntraArray = new JSONArray();
            for (int incrementVal = 0; incrementVal < itemsInfraArray.length(); incrementVal++) {
                JSONObject singleRow = itemsInfraArray.getJSONObject(incrementVal);
                JSONObject itemObject = new JSONObject();
                try {
                    if (singleRow.has(Constants.CPGUID)) {
                        itemObject.put(Constants.CPGUID, singleRow.optString(Constants.CPGUID));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (singleRow.has(Constants.CP2GUID)) {
                        itemObject.put(Constants.CP2GUID, singleRow.optString(Constants.CP2GUID));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (singleRow.has(Constants.InfraCode)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.InfraCode))) {
                            itemObject.put(Constants.InfraCode, singleRow.optString(Constants.InfraCode));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (singleRow.has(Constants.InfraCodeDesc)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.InfraCodeDesc))) {
                            itemObject.put(Constants.InfraCodeDesc, singleRow.optString(Constants.InfraCodeDesc));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (singleRow.has(Constants.OwnedBy)) {
                    if (!TextUtils.isEmpty(singleRow.optString(Constants.OwnedBy))) {
                        itemObject.put(Constants.OwnedBy, singleRow.optString(Constants.OwnedBy));
                    }
                }
                try {
                    if (singleRow.has(Constants.OwnedByDesc)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.OwnedByDesc))) {
                            itemObject.put(Constants.OwnedByDesc, singleRow.getString(Constants.OwnedByDesc));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (singleRow.has(Constants.InfraUOM)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.InfraUOM))) {
                            itemObject.put(Constants.InfraUOM, singleRow.optString(Constants.InfraUOM));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (singleRow.has(Constants.InfraQty)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.InfraQty))) {
                            itemObject.put(Constants.InfraQty, singleRow.get(Constants.InfraQty));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonIntraArray.put(itemObject);
            }
            dbHeadTable.put(Constants.CPInfras, jsonIntraArray);

            // CPROutes
            //CPInfras
            JSONArray itemsCPRouteArray = new JSONArray(fetchJsonHeaderObject.getString(Constants.ITEM_TXT_CPROUTE));
            JSONArray jsonCPROuteArray = new JSONArray();
            for (int incrementVal = 0; incrementVal < itemsCPRouteArray.length(); incrementVal++) {
                JSONObject singleRow = itemsCPRouteArray.getJSONObject(incrementVal);
                JSONObject itemObject = new JSONObject();
                try {
                    if (singleRow.has(Constants.CPGuid)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.CPGuid))) {
                            itemObject.put(Constants.CPGuid, singleRow.optString(Constants.CPGuid));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (singleRow.has(Constants.RouteGuid)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.RouteGuid))) {
                            itemObject.put(Constants.RouteGuid, singleRow.optString(Constants.RouteGuid));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (singleRow.has(Constants.SeqInd)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.SeqInd))) {
                            itemObject.put(Constants.SeqInd, singleRow.optString(Constants.SeqInd));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (singleRow.has(Constants.RouteId)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.RouteId))) {
                            itemObject.put(Constants.RouteId, singleRow.optString(Constants.RouteId));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (singleRow.has(Constants.RouteDesc)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.RouteDesc))) {
                            itemObject.put(Constants.RouteDesc, singleRow.optString(Constants.RouteDesc));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                itemObject.put(Constants.RouteClassification, "01");
                jsonCPROuteArray.put(itemObject);
            }
            dbHeadTable.put(Constants.CPRoutes, jsonCPROuteArray);
            //hardcoded partner function
            jsonArray = new JSONArray();
            JSONObject itemObject = new JSONObject();
            try {
                if (fetchJsonHeaderObject.has(Constants.CPGUID)) {
                    itemObject.put(Constants.CPGUID, fetchJsonHeaderObject.optString(Constants.CPGUID));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            itemObject.put(Constants.PFGUID, GUID.newRandom().toString36().toUpperCase());
            itemObject.put(Constants.PartnerFunction, "01");
            try {
                if (fetchJsonHeaderObject.has(Constants.PartnarName)) {
                    try {
                        if (fetchJsonHeaderObject.getString(Constants.OutletName).length() <= 35) {
                            itemObject.put(Constants.PartnarName, fetchJsonHeaderObject.optString(Constants.OutletName));
                        } else {
                            itemObject.put(Constants.PartnarName, fetchJsonHeaderObject.optString(Constants.OutletName).substring(0, 34));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.PartnerMobileNo)) {
                    itemObject.put(Constants.PartnerMobileNo, fetchJsonHeaderObject.optString(Constants.MobileNo));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                itemObject.put(Constants.PartnarCPGUID, fetchJsonHeaderObject.optString(Constants.CPGUID).replace("-", "").toUpperCase());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            itemObject.put(Constants.StatusID, Constants.str_01);
            jsonArray.put(itemObject);
            itemObject = new JSONObject();
            try {
                if (fetchJsonHeaderObject.has(Constants.CPGUID)) {
                    itemObject.put(Constants.CPGUID, fetchJsonHeaderObject.optString(Constants.CPGUID));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            itemObject.put(Constants.PFGUID, GUID.newRandom().toString36().toUpperCase());
            itemObject.put(Constants.PartnerFunction, "02");
            try {
                if (fetchJsonHeaderObject.has(Constants.PartnarName)) {
                    try {
                        if (fetchJsonHeaderObject.getString(Constants.OutletName).length() <= 35) {
                            itemObject.put(Constants.PartnarName, fetchJsonHeaderObject.optString(Constants.OutletName));
                        } else {
                            itemObject.put(Constants.PartnarName, fetchJsonHeaderObject.optString(Constants.OutletName).substring(0, 34));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            try {
                if (fetchJsonHeaderObject.has(Constants.PartnerMobileNo)) {
                    itemObject.put(Constants.PartnerMobileNo, fetchJsonHeaderObject.optString(Constants.MobileNo));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                itemObject.put(Constants.PartnarCPGUID, fetchJsonHeaderObject.optString(Constants.CPGUID).replace("-", "").toUpperCase());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            itemObject.put(Constants.StatusID, Constants.str_01);
            jsonArray.put(itemObject);
            dbHeadTable.put(Constants.CPPartnerFunctions, jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dbHeadTable;
    }

        public static JSONObject getCPHeaderValuesReview (JSONObject fetchJsonHeaderObject){
            JSONObject dbHeadTable = new JSONObject();
            try {
                dbHeadTable.put(Constants.CPGUID, fetchJsonHeaderObject.getString(Constants.CPGUID));
                REPEATABLE_REQUEST_ID = fetchJsonHeaderObject.getString(Constants.CPGUID).replace("-", "");
                try {
                    if (fetchJsonHeaderObject.has(Constants.CPNo)) {
                        dbHeadTable.put(Constants.CPNo, fetchJsonHeaderObject.getString(Constants.CPNo));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.PartnerMgrName)) {
                        dbHeadTable.put(Constants.PartnerMgrName, fetchJsonHeaderObject.getString(Constants.PartnerMgrName));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.PartnerMgrNo)) {
                        dbHeadTable.put(Constants.PartnerMgrNo, fetchJsonHeaderObject.getString(Constants.PartnerMgrNo));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.Name)) {
                        try {
                            if (fetchJsonHeaderObject.getString(Constants.Name).length() <= 35) {
                                dbHeadTable.put(Constants.Name, fetchJsonHeaderObject.getString(Constants.Name));
                            } else {
                                dbHeadTable.put(Constants.Name, fetchJsonHeaderObject.getString(Constants.Name).substring(0, 34));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.CPUID)) {
                        dbHeadTable.put(Constants.CPUID, fetchJsonHeaderObject.getString(Constants.CPUID));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.AccountGrp)) {
                        dbHeadTable.put(Constants.AccountGrp, fetchJsonHeaderObject.getString(Constants.AccountGrp));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.Latitude)) {
                        if (!TextUtils.isEmpty(fetchJsonHeaderObject.getString(Constants.Latitude))) {
                            dbHeadTable.put(Constants.Latitude, fetchJsonHeaderObject.getString(Constants.Latitude));
                        } else {
                            dbHeadTable.put(Constants.Latitude, "0.0");
                        }
                    }
                    if (fetchJsonHeaderObject.has(Constants.Longitude)) {
                        if (!TextUtils.isEmpty(fetchJsonHeaderObject.getString(Constants.Longitude))) {
                            dbHeadTable.put(Constants.Longitude, fetchJsonHeaderObject.getString(Constants.Longitude));
                        } else {
                            dbHeadTable.put(Constants.Latitude, "0.0");
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
//            dbHeadTable.put(Constants.ParentID, fetchJsonHeaderObject.getString(Constants.ParentID));
//            dbHeadTable.put(Constants.ParentName, fetchJsonHeaderObject.getString(Constants.ParentName));
                try {
                    if (fetchJsonHeaderObject.has(Constants.SearchTerm)) {
                        dbHeadTable.put(Constants.SearchTerm, fetchJsonHeaderObject.getString(Constants.SearchTerm));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//            dbHeadTable.put(Constants.ParentTypeID, fetchJsonHeaderObject.getString(Constants.ParentTypeID));
//            dbHeadTable.put(Constants.ParentTypDesc, fetchJsonHeaderObject.getString(Constants.ParentTypDesc));
                try {
                    if (fetchJsonHeaderObject.has(Constants.CPTypeID)) {
                        dbHeadTable.put(Constants.CPTypeID, fetchJsonHeaderObject.getString(Constants.CPTypeID));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.CPTypeDesc)) {
                        dbHeadTable.put(Constants.CPTypeDesc, fetchJsonHeaderObject.getString(Constants.CPTypeDesc));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.Group1)) {
                        dbHeadTable.put(Constants.Group1, fetchJsonHeaderObject.getString(Constants.Group1));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.Group2)) {
                        dbHeadTable.put(Constants.Group2, fetchJsonHeaderObject.getString(Constants.Group2));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.Group3)) {
                        dbHeadTable.put(Constants.Group3, fetchJsonHeaderObject.getString(Constants.Group3));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.Group4)) {
                        dbHeadTable.put(Constants.Group4, fetchJsonHeaderObject.getString(Constants.Group4));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.PAN)) {
                        dbHeadTable.put(Constants.PAN, fetchJsonHeaderObject.getString(Constants.PAN));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//            dbHeadTable.put(Constants.CPStock, fetchJsonHeaderObject.getString(Constants.CPStock));
                try {
                    if (fetchJsonHeaderObject.has(Constants.UOM)) {
                        dbHeadTable.put(Constants.UOM, fetchJsonHeaderObject.getString(Constants.UOM));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.DOB)) {
                        dbHeadTable.put(Constants.DOB, fetchJsonHeaderObject.getString(Constants.DOB));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            /*try {
                dbHeadTable.put(Constants.Anniversary, fetchJsonHeaderObject.getString(Constants.Anniversary));
            } catch (JSONException e) {
                e.printStackTrace();
            }*/

                try {
                    if (fetchJsonHeaderObject.has(Constants.ActivationDate)) {
                        dbHeadTable.put(Constants.ActivationDate, fetchJsonHeaderObject.getString(Constants.ActivationDate));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.GSTActiveDate)) {
                        dbHeadTable.put(Constants.GSTActiveDate, fetchJsonHeaderObject.getString(Constants.GSTActiveDate));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.Address1)) {
                        try {
                            if (fetchJsonHeaderObject.getString(Constants.Address1).length() <= 35) {
                                dbHeadTable.put(Constants.Address1, fetchJsonHeaderObject.getString(Constants.Address1));
                            } else {
                                dbHeadTable.put(Constants.Address1, fetchJsonHeaderObject.getString(Constants.Address1).substring(0, 34));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.Address2)) {
                        try {
                            if (fetchJsonHeaderObject.getString(Constants.Address2).length() <= 35) {
                                dbHeadTable.put(Constants.Address2, fetchJsonHeaderObject.getString(Constants.Address2));
                            } else {
                                dbHeadTable.put(Constants.Address2, fetchJsonHeaderObject.getString(Constants.Address2).substring(0, 34));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.Landmark)) {
                        dbHeadTable.put(Constants.Landmark, fetchJsonHeaderObject.getString(Constants.Landmark));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.ZoneID)) {
                        dbHeadTable.put(Constants.ZoneID, fetchJsonHeaderObject.getString(Constants.ZoneID));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.ZoneDesc)) {
                        dbHeadTable.put(Constants.ZoneDesc, fetchJsonHeaderObject.getString(Constants.ZoneDesc));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.TownID)) {
                        dbHeadTable.put(Constants.TownID, fetchJsonHeaderObject.getString(Constants.TownID));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.TownGuid)) {
                        dbHeadTable.put(Constants.TownGuid, fetchJsonHeaderObject.getString(Constants.TownGuid));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
           /* if (fetchJsonHeaderObject.has(Constants.WardGUID)&&!TextUtils.isEmpty(fetchJsonHeaderObject.getString(Constants.WardGUID))) {
                dbHeadTable.put(Constants.WardGUID, fetchJsonHeaderObject.getString(Constants.WardGUID));
            }*/
                try {
                    if (fetchJsonHeaderObject.has(Constants.SubDistrictGUID) && !TextUtils.isEmpty(fetchJsonHeaderObject.getString(Constants.SubDistrictGUID))) {
                        dbHeadTable.put(Constants.SubDistrictGUID, fetchJsonHeaderObject.getString(Constants.SubDistrictGUID));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.TownDesc)) {
                        dbHeadTable.put(Constants.TownDesc, fetchJsonHeaderObject.getString(Constants.TownDesc));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.CityID)) {
                        dbHeadTable.put(Constants.CityID, fetchJsonHeaderObject.getString(Constants.CityID));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.CityDesc)) {
                        dbHeadTable.put(Constants.CityDesc, fetchJsonHeaderObject.getString(Constants.CityDesc));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.StateID)) {
                        dbHeadTable.put(Constants.StateID, fetchJsonHeaderObject.getString(Constants.StateID));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.StateDesc)) {
                        dbHeadTable.put(Constants.StateDesc, fetchJsonHeaderObject.getString(Constants.StateDesc));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.DistrictID)) {
                        dbHeadTable.put(Constants.DistrictID, fetchJsonHeaderObject.getString(Constants.DistrictID));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.DistrictDesc)) {
                        dbHeadTable.put(Constants.DistrictDesc, fetchJsonHeaderObject.getString(Constants.DistrictDesc));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.Country)) {
                        dbHeadTable.put(Constants.Country, fetchJsonHeaderObject.getString(Constants.Country));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.CountryName)) {
                        dbHeadTable.put(Constants.CountryName, fetchJsonHeaderObject.getString(Constants.CountryName));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.PostalCode)) {
                        dbHeadTable.put(Constants.PostalCode, fetchJsonHeaderObject.getString(Constants.PostalCode));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.Mobile1)) {
                        dbHeadTable.put(Constants.Mobile1, fetchJsonHeaderObject.getString(Constants.Mobile1));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.Mobile2)) {
                        dbHeadTable.put(Constants.Mobile2, fetchJsonHeaderObject.getString(Constants.Mobile2));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.Landline)) {
                        dbHeadTable.put(Constants.Landline, fetchJsonHeaderObject.getString(Constants.Landline));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.EmailID)) {
                        dbHeadTable.put(Constants.EmailID, fetchJsonHeaderObject.getString(Constants.EmailID));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.Currency)) {
                        dbHeadTable.put(Constants.Currency, fetchJsonHeaderObject.getString(Constants.Currency));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//            dbHeadTable.put(Constants.CreditLimit, fetchJsonHeaderObject.getString(Constants.CreditLimit));
//            dbHeadTable.put(Constants.CreditDays, fetchJsonHeaderObject.getString(Constants.CreditDays));
                try {
                    if (fetchJsonHeaderObject.has(Constants.StatusID)) {
                        dbHeadTable.put(Constants.StatusID, fetchJsonHeaderObject.getString(Constants.StatusID));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.StatusDesc)) {
                        dbHeadTable.put(Constants.StatusDesc, fetchJsonHeaderObject.getString(Constants.StatusDesc));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.ApprvlStatusID)) {
                        dbHeadTable.put(Constants.ApprvlStatusID, fetchJsonHeaderObject.getString(Constants.ApprvlStatusID));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.ApprvlStatusDesc)) {
                        dbHeadTable.put(Constants.ApprvlStatusDesc, fetchJsonHeaderObject.getString(Constants.ApprvlStatusDesc));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.OwnerName)) {
                        try {
                            if (fetchJsonHeaderObject.getString(Constants.OwnerName).length() <= 35) {
                                dbHeadTable.put(Constants.OwnerName, fetchJsonHeaderObject.getString(Constants.OwnerName));
                            } else {
                                dbHeadTable.put(Constants.OwnerName, fetchJsonHeaderObject.getString(Constants.OwnerName).substring(0, 34));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.SalesOfficeID)) {
                        dbHeadTable.put(Constants.SalesOfficeID, fetchJsonHeaderObject.getString(Constants.SalesOfficeID));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.SalesGrpDesc)) {
                        dbHeadTable.put(Constants.SalesGrpDesc, fetchJsonHeaderObject.getString(Constants.SalesGrpDesc));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.SalesGroupID)) {
                        dbHeadTable.put(Constants.SalesGroupID, fetchJsonHeaderObject.getString(Constants.SalesGroupID));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.SalesOffDesc)) {
                        dbHeadTable.put(Constants.SalesOffDesc, fetchJsonHeaderObject.getString(Constants.SalesOffDesc));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.IsKeyCP)) {
                        dbHeadTable.put(Constants.IsKeyCP, fetchJsonHeaderObject.getString(Constants.IsKeyCP));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.WeeklyOff)) {
                        dbHeadTable.put(Constants.WeeklyOff, fetchJsonHeaderObject.getString(Constants.WeeklyOff));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.WeeklyOffDesc)) {
                        dbHeadTable.put(Constants.WeeklyOffDesc, fetchJsonHeaderObject.getString(Constants.WeeklyOffDesc));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.FaxNo)) {
                        dbHeadTable.put(Constants.FaxNo, fetchJsonHeaderObject.getString(Constants.FaxNo));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.BusinessID1)) {
                        dbHeadTable.put(Constants.BusinessID1, fetchJsonHeaderObject.getString(Constants.BusinessID1));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dbHeadTable.put(Constants.Source, "MOBILE");
                try {
                    if (fetchJsonHeaderObject.has(Constants.Tax1)) {
                        dbHeadTable.put(Constants.Tax1, fetchJsonHeaderObject.getString(Constants.Tax1));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.OutletSizeId)) {
                        dbHeadTable.put(Constants.OutletSizeId, fetchJsonHeaderObject.getString(Constants.OutletSizeId));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.OutletShapeId)) {
                        dbHeadTable.put(Constants.OutletShapeId, fetchJsonHeaderObject.getString(Constants.OutletShapeId));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.NoOfEmployee)) {
                        dbHeadTable.put(Constants.NoOfEmployee, fetchJsonHeaderObject.getString(Constants.NoOfEmployee));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    if (fetchJsonHeaderObject.has(Constants.OutletsCovered)) {
                        dbHeadTable.put(Constants.OutletsCovered, fetchJsonHeaderObject.getString(Constants.OutletsCovered));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    if (fetchJsonHeaderObject.has(Constants.WsCovered)) {
                        dbHeadTable.put(Constants.WsCovered, fetchJsonHeaderObject.getString(Constants.WsCovered));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    if (fetchJsonHeaderObject.has(Constants.NoOfSalesMan)) {
                        dbHeadTable.put(Constants.NoOfSalesMan, fetchJsonHeaderObject.getString(Constants.NoOfSalesMan));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.URL1)) {
                        dbHeadTable.put(Constants.URL1, fetchJsonHeaderObject.getString(Constants.URL1));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.URL2)) {
                        dbHeadTable.put(Constants.URL2, fetchJsonHeaderObject.getString(Constants.URL2));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.URL3)) {
                        dbHeadTable.put(Constants.URL3, fetchJsonHeaderObject.getString(Constants.URL3));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.URL4)) {
                        dbHeadTable.put(Constants.URL4, fetchJsonHeaderObject.getString(Constants.URL4));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.URL5)) {
                        dbHeadTable.put(Constants.URL5, fetchJsonHeaderObject.getString(Constants.URL5));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.URL6)) {
                        dbHeadTable.put(Constants.URL6, fetchJsonHeaderObject.getString(Constants.URL6));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.GeoAccuracy)) {
                        dbHeadTable.put(Constants.GeoAccuracy, fetchJsonHeaderObject.getString(Constants.GeoAccuracy));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.AddDetails)) {
                        dbHeadTable.put(Constants.AddDetails, fetchJsonHeaderObject.getString(Constants.AddDetails));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.TimeTaken)) {
                        dbHeadTable.put(Constants.TimeTaken, fetchJsonHeaderObject.getString(Constants.TimeTaken));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.MobileVerifed)) {
                        if (fetchJsonHeaderObject.getString(Constants.MobileVerifed).equalsIgnoreCase("true")) {
                            dbHeadTable.put(Constants.MobileVerifed, "Y");
                        } else if (fetchJsonHeaderObject.getString(Constants.MobileVerifed).equalsIgnoreCase("false")) {
                            dbHeadTable.put(Constants.MobileVerifed, "N");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//            dbHeadTable.put(Constants.DeactivatedOn, fetchJsonHeaderObject.getString(Constants.DeactivatedOn));
                try {
                    if (fetchJsonHeaderObject.has(Constants.TaxRegStatus)) {
                        dbHeadTable.put(Constants.TaxRegStatus, fetchJsonHeaderObject.getString(Constants.TaxRegStatus));
                    }
                    if (fetchJsonHeaderObject.has(Constants.TaxRegStatusDesc)) {
                        dbHeadTable.put(Constants.TaxRegStatusDesc, fetchJsonHeaderObject.getString(Constants.TaxRegStatusDesc));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONArray jsonArray = null;
                try {
                    JSONArray itemsArray = new JSONArray(fetchJsonHeaderObject.getString(Constants.ITEM_TXT));
                    jsonArray = new JSONArray();
                    for (int incrementVal = 0; incrementVal < itemsArray.length(); incrementVal++) {
                        JSONObject singleRow = itemsArray.getJSONObject(incrementVal);
                        JSONObject itemObject = new JSONObject();
                        if (singleRow.has(Constants.CPGUID)) {
                            itemObject.put(Constants.CPGUID, singleRow.optString(Constants.CPGUID));
                        }
                        if (singleRow.has(Constants.CP1GUID)) {
                            itemObject.put(Constants.CP1GUID, singleRow.optString(Constants.CP1GUID));
                        }
                        if (singleRow.has(Constants.ParentID)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.ParentID))) {
                                itemObject.put(Constants.ParentID, singleRow.optString(Constants.ParentID));
                                if (singleRow.has(Constants.ParentTypeID)) {
                                    if (!TextUtils.isEmpty(singleRow.optString(Constants.ParentTypeID))) {
                                        itemObject.put(Constants.ParentTypeID, singleRow.optString(Constants.ParentTypeID));
                                    }
                                }
                                if (singleRow.has(Constants.ParentName)) {
                                    if (!TextUtils.isEmpty(singleRow.optString(Constants.ParentName))) {
                                        itemObject.put(Constants.ParentName, singleRow.optString(Constants.ParentName));
                                    }
                                }
                            }
                        }


                        if (singleRow.has(Constants.DMSDivision)) {
                            itemObject.put(Constants.DMSDivision, singleRow.optString(Constants.DMSDivision));
                        }
                        if (singleRow.has(Constants.DMSDivisionDesc)) {
                            itemObject.put(Constants.DMSDivisionDesc, singleRow.optString(Constants.DMSDivisionDesc));
                        }
                        if (fetchJsonHeaderObject.has(Constants.PartnerMgrGUID)) {
                            if (!TextUtils.isEmpty(fetchJsonHeaderObject.optString(Constants.PartnerMgrGUID))) {
                                itemObject.put(Constants.PartnerMgrGUID, fetchJsonHeaderObject.getString(Constants.PartnerMgrGUID));
                            }
                        }
                        if (singleRow.has(Constants.Currency)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.Currency))) {
                                itemObject.put(Constants.Currency, singleRow.optString(Constants.Currency));
                            }
                        }
                        if (singleRow.has(Constants.StatusID)) {
                            if (singleRow.optString(Constants.StatusID) != null) {
                                itemObject.put(Constants.StatusID, singleRow.optString(Constants.StatusID));
                            }
                        }
                        if (singleRow.has(Constants.RouteGUID)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.RouteGUID))) {
                                itemObject.put(Constants.RouteGUID, singleRow.optString(Constants.RouteGUID));
                            }
                        }
                        if (singleRow.has(Constants.RouteID)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.RouteID))) {
                                itemObject.put(Constants.RouteID, singleRow.get(Constants.RouteID));
                            }
                        }
                        if (singleRow.has(Constants.Group1)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.Group1))) {
                                itemObject.put(Constants.Group1, singleRow.get(Constants.Group1));
                            }
                        }
                        if (singleRow.has(Constants.Group2)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.Group2))) {
                                itemObject.put(Constants.Group2, singleRow.get(Constants.Group2));
                            }
                        }

                        if (singleRow.has(Constants.Group3)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.Group3))) {
                                itemObject.put(Constants.Group3, singleRow.get(Constants.Group3));
                            }
                        }

                        if (singleRow.has(Constants.Group3Desc)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.Group3Desc))) {
                                itemObject.put(Constants.Group3Desc, singleRow.get(Constants.Group3Desc));
                            }
                        }

                        if (singleRow.has(Constants.FreqOfDispatch)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.FreqOfDispatch))) {
                                itemObject.put(Constants.FreqOfDispatch, singleRow.get(Constants.FreqOfDispatch));
                            }
                        }
                        if (singleRow.has(Constants.Group7)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.Group7))) {
                                itemObject.put(Constants.Group7, singleRow.get(Constants.Group7));
                            }
                        }
                        if (singleRow.has(Constants.Group8)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.Group8))) {
                                itemObject.put(Constants.Group8, singleRow.get(Constants.Group8));
                            }
                        }

                        if (singleRow.has(Constants.Group6)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.Group6))) {
                                itemObject.put(Constants.Group6, singleRow.get(Constants.Group6));
                            }
                        }

                        if (singleRow.has(Constants.Group9)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.Group9))) {
                                itemObject.put(Constants.Group9, singleRow.get(Constants.Group9));
                            }
                        }

                        if (singleRow.has(Constants.DistanceFromParent)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.DistanceFromParent))) {
                                itemObject.put(Constants.DistanceFromParent, singleRow.get(Constants.DistanceFromParent));
                            }
                        }

                        if (singleRow.has(Constants.DstnceFromPUOM)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.DstnceFromPUOM))) {
                                itemObject.put(Constants.DstnceFromPUOM, singleRow.get(Constants.DstnceFromPUOM));
                            }
                        }

                        if (singleRow.has(Constants.Day1)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.Day1))) {
                                itemObject.put(Constants.Day1, singleRow.get(Constants.Day1));
                            }
                        }

                        if (singleRow.has(Constants.Day2)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.Day2))) {
                                itemObject.put(Constants.Day2, singleRow.get(Constants.Day2));
                            }
                        }

                        if (singleRow.has(Constants.Day3)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.Day3))) {
                                itemObject.put(Constants.Day3, singleRow.get(Constants.Day3));
                            }
                        }

                        if (singleRow.has(Constants.Day4)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.Day4))) {
                                itemObject.put(Constants.Day4, singleRow.get(Constants.Day4));
                            }
                        }

                        if (singleRow.has(Constants.Day5)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.Day5))) {
                                itemObject.put(Constants.Day5, singleRow.get(Constants.Day5));
                            }
                        }

                        if (singleRow.has(Constants.Day6)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.Day6))) {
                                itemObject.put(Constants.Day6, singleRow.get(Constants.Day6));
                            }
                        }

                        if (singleRow.has(Constants.Day7)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.Day7))) {
                                itemObject.put(Constants.Day7, singleRow.get(Constants.Day7));
                            }
                        }

                        try {
                            itemObject.put(Constants.BillSeries, "03");
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        jsonArray.put(itemObject);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                dbHeadTable.put(Constants.CPDMSDivisions, jsonArray);

                //CPInfras
                JSONArray itemsInfraArray = new JSONArray(fetchJsonHeaderObject.getString(Constants.ITEM_TXT_INFRA));
                JSONArray jsonIntraArray = new JSONArray();
                for (int incrementVal = 0; incrementVal < itemsInfraArray.length(); incrementVal++) {
                    JSONObject singleRow = itemsInfraArray.getJSONObject(incrementVal);
                    JSONObject itemObject = new JSONObject();
                    try {
                        if (singleRow.has(Constants.CPGUID)) {
                            itemObject.put(Constants.CPGUID, singleRow.optString(Constants.CPGUID));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (singleRow.has(Constants.CP2GUID)) {
                            itemObject.put(Constants.CP2GUID, singleRow.optString(Constants.CP2GUID));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (singleRow.has(Constants.InfraCode)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.InfraCode))) {
                                itemObject.put(Constants.InfraCode, singleRow.optString(Constants.InfraCode));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (singleRow.has(Constants.InfraCodeDesc)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.InfraCodeDesc))) {
                                itemObject.put(Constants.InfraCodeDesc, singleRow.optString(Constants.InfraCodeDesc));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (singleRow.has(Constants.OwnedBy)) {
                        if (!TextUtils.isEmpty(singleRow.optString(Constants.OwnedBy))) {
                            itemObject.put(Constants.OwnedBy, singleRow.optString(Constants.OwnedBy));
                        }
                    }
                    try {
                        if (singleRow.has(Constants.OwnedByDesc)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.OwnedByDesc))) {
                                itemObject.put(Constants.OwnedByDesc, singleRow.getString(Constants.OwnedByDesc));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (singleRow.has(Constants.InfraUOM)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.InfraUOM))) {
                                itemObject.put(Constants.InfraUOM, singleRow.optString(Constants.InfraUOM));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (singleRow.has(Constants.InfraQty)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.InfraQty))) {
                                itemObject.put(Constants.InfraQty, singleRow.get(Constants.InfraQty));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonIntraArray.put(itemObject);
                }
                dbHeadTable.put(Constants.CPInfras, jsonIntraArray);

                // CPROutes
                //CPInfras
                JSONArray itemsCPRouteArray = new JSONArray(fetchJsonHeaderObject.getString(Constants.ITEM_TXT_CPROUTE));
                JSONArray jsonCPROuteArray = new JSONArray();
                for (int incrementVal = 0; incrementVal < itemsCPRouteArray.length(); incrementVal++) {
                    JSONObject singleRow = itemsCPRouteArray.getJSONObject(incrementVal);
                    JSONObject itemObject = new JSONObject();
                    try {
                        if (singleRow.has(Constants.CPGuid)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.CPGuid))) {
                                itemObject.put(Constants.CPGuid, singleRow.optString(Constants.CPGuid));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (singleRow.has(Constants.RouteGuid)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.RouteGuid))) {
                                itemObject.put(Constants.RouteGuid, singleRow.optString(Constants.RouteGuid));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (singleRow.has(Constants.RouteId)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.RouteId))) {
                                itemObject.put(Constants.RouteId, singleRow.optString(Constants.RouteId));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if (singleRow.has(Constants.RouteDesc)) {
                            if (!TextUtils.isEmpty(singleRow.optString(Constants.RouteDesc))) {
                                itemObject.put(Constants.RouteDesc, singleRow.optString(Constants.RouteDesc));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    itemObject.put(Constants.RouteClassification, "01");
                    jsonCPROuteArray.put(itemObject);
                }
                dbHeadTable.put(Constants.CPRoutes, jsonCPROuteArray);
                //hardcoded partner function
                jsonArray = new JSONArray();
                JSONObject itemObject = new JSONObject();
                try {
                    if (fetchJsonHeaderObject.has(Constants.CPGUID)) {
                        itemObject.put(Constants.CPGUID, fetchJsonHeaderObject.optString(Constants.CPGUID));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                itemObject.put(Constants.PFGUID, GUID.newRandom().toString36().toUpperCase());
                itemObject.put(Constants.PartnerFunction, "01");
                try {
                    if (fetchJsonHeaderObject.has(Constants.PartnarName)) {
                        try {
                            if (fetchJsonHeaderObject.getString(Constants.OutletName).length() <= 35) {
                                itemObject.put(Constants.PartnarName, fetchJsonHeaderObject.optString(Constants.OutletName));
                            } else {
                                itemObject.put(Constants.PartnarName, fetchJsonHeaderObject.optString(Constants.OutletName).substring(0, 34));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.PartnerMobileNo)) {
                        itemObject.put(Constants.PartnerMobileNo, fetchJsonHeaderObject.optString(Constants.MobileNo));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    itemObject.put(Constants.PartnarCPGUID, fetchJsonHeaderObject.optString(Constants.CPGUID).replace("-", "").toUpperCase());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                itemObject.put(Constants.StatusID, Constants.str_01);
                jsonArray.put(itemObject);
                itemObject = new JSONObject();
                try {
                    if (fetchJsonHeaderObject.has(Constants.CPGUID)) {
                        itemObject.put(Constants.CPGUID, fetchJsonHeaderObject.optString(Constants.CPGUID));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                itemObject.put(Constants.PFGUID, GUID.newRandom().toString36().toUpperCase());
                itemObject.put(Constants.PartnerFunction, "02");
                try {
                    if (fetchJsonHeaderObject.has(Constants.PartnarName)) {
                        try {
                            if (fetchJsonHeaderObject.getString(Constants.OutletName).length() <= 35) {
                                itemObject.put(Constants.PartnarName, fetchJsonHeaderObject.optString(Constants.OutletName));
                            } else {
                                itemObject.put(Constants.PartnarName, fetchJsonHeaderObject.optString(Constants.OutletName).substring(0, 34));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                try {
                    if (fetchJsonHeaderObject.has(Constants.PartnerMobileNo)) {
                        itemObject.put(Constants.PartnerMobileNo, fetchJsonHeaderObject.optString(Constants.MobileNo));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    itemObject.put(Constants.PartnarCPGUID, fetchJsonHeaderObject.optString(Constants.CPGUID).replace("-", "").toUpperCase());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                itemObject.put(Constants.StatusID, Constants.str_01);
                jsonArray.put(itemObject);
                dbHeadTable.put(Constants.CPPartnerFunctions, jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return dbHeadTable;
        }

        public static String getTypesetValueForRetUOM (Context ctx){
            String typeValues = "";
            try {
                typeValues = OfflineManager.getValueByColumnName(Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Typeset + " eq '" +
                        Constants.SS + "' and " + Constants.Types + " eq '" + Constants.CPSTKUOM + "'", Constants.TypeValue);
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }
            return typeValues;
        }
        public static ODataDuration getOdataDuration () {
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
        public static String convertArrListToGsonString
        (ArrayList < HashMap < String, String >> arrtable){
            String convertGsonString = "";
            Gson gson = new Gson();
            try {
                convertGsonString = gson.toJson(arrtable);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return convertGsonString;
        }
        public static JSONObject getROHeaderValuesFromJsonObject (JSONObject fetchJsonHeaderObject){
            JSONObject dbHeadTable = new JSONObject();
            try {

                dbHeadTable.put(Constants.SSROGUID, fetchJsonHeaderObject.getString(Constants.SSROGUID));
                try {
                    REPEATABLE_REQUEST_ID = fetchJsonHeaderObject.getString(Constants.SSROGUID).replace("-", "");
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                dbHeadTable.put(Constants.OrderDate, fetchJsonHeaderObject.optString(Constants.OrderDate));
                dbHeadTable.put(Constants.Currency, fetchJsonHeaderObject.optString(Constants.Currency));
                dbHeadTable.put(Constants.OrderType, fetchJsonHeaderObject.optString(Constants.OrderType));
                dbHeadTable.put(Constants.OrderTypeDesc, fetchJsonHeaderObject.optString(Constants.OrderTypeDesc));
                dbHeadTable.put(Constants.BeatGuid, fetchJsonHeaderObject.optString(Constants.BeatGuid));
                dbHeadTable.put(Constants.DmsDivision, fetchJsonHeaderObject.optString(Constants.DmsDivision));
                dbHeadTable.put(Constants.OrderReason, fetchJsonHeaderObject.optString(Constants.OrderReason));
                dbHeadTable.put(Constants.Incoterm1, fetchJsonHeaderObject.optString(Constants.Incoterm1));
                dbHeadTable.put(Constants.Incoterm1Desc, fetchJsonHeaderObject.optString(Constants.Incoterm1Desc));
                dbHeadTable.put(Constants.CPNo, fetchJsonHeaderObject.optString(Constants.CPNo));
                dbHeadTable.put(Constants.FromCPGUID, fetchJsonHeaderObject.optString(Constants.FromCPGUID));
                dbHeadTable.put(Constants.FromCPNo, fetchJsonHeaderObject.optString(Constants.FromCPNo));
                dbHeadTable.put(Constants.CPName, fetchJsonHeaderObject.optString(Constants.CPName));
                dbHeadTable.put(Constants.FromCPTypID, fetchJsonHeaderObject.optString(Constants.FromCPTypID));
                dbHeadTable.put(Constants.DmsDivisionDesc, fetchJsonHeaderObject.optString(Constants.DmsDivisionDesc));
                dbHeadTable.put(Constants.SPNo, fetchJsonHeaderObject.optString(Constants.SPNo));
                dbHeadTable.put(Constants.SPGUID, fetchJsonHeaderObject.optString(Constants.SPGUID));
                dbHeadTable.put(Constants.FirstName, fetchJsonHeaderObject.optString(Constants.FirstName));
                dbHeadTable.put(Constants.SoldToCPGUID, fetchJsonHeaderObject.optString(Constants.SoldToCPGUID));
                dbHeadTable.put(Constants.SoldToDesc, fetchJsonHeaderObject.optString(Constants.SoldToDesc));
                dbHeadTable.put(Constants.SoldToID, fetchJsonHeaderObject.optString(Constants.SoldToID));
                dbHeadTable.put(Constants.SoldToDesc, fetchJsonHeaderObject.optString(Constants.SoldToDesc));
                dbHeadTable.put(Constants.SoldToTypeID, fetchJsonHeaderObject.optString(Constants.SoldToTypeID));
                dbHeadTable.put(Constants.ShipToID, fetchJsonHeaderObject.optString(Constants.ShipToID));
                dbHeadTable.put(Constants.ShipToType, fetchJsonHeaderObject.optString(Constants.ShipToType));
                dbHeadTable.put(Constants.ShipToDesc, fetchJsonHeaderObject.optString(Constants.ShipToDesc));
                dbHeadTable.put(Constants.TestRun, fetchJsonHeaderObject.optString(Constants.TestRun));
                dbHeadTable.put(Constants.StatusID, fetchJsonHeaderObject.optString(Constants.StatusID));
                dbHeadTable.put(Constants.Source, fetchJsonHeaderObject.optString(Constants.Source));
                dbHeadTable.put(Constants.OrderOrigin, fetchJsonHeaderObject.optString(Constants.OrderOrigin));
                dbHeadTable.put(Constants.OrderEntry, fetchJsonHeaderObject.optString(Constants.OrderEntry));
                dbHeadTable.put(Constants.ShipToIDCPGUID, fetchJsonHeaderObject.optString(Constants.ShipToIDCPGUID));
                dbHeadTable.put(Constants.NetAmount, fetchJsonHeaderObject.optString(Constants.NetAmount));
                dbHeadTable.put(Constants.GrossAmount, fetchJsonHeaderObject.optString(Constants.GrossAmount));
                try {
                    dbHeadTable.put(Constants.Tax, fetchJsonHeaderObject.optString(Constants.Tax));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    dbHeadTable.put(Constants.CndnAdjustment, fetchJsonHeaderObject.getBoolean(Constants.CndnAdjustment));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONArray itemsArray = new JSONArray(fetchJsonHeaderObject.optString(Constants.ITEM_TXT));
                JSONArray jsonArray = new JSONArray();
                for (int incrementVal = 0; incrementVal < itemsArray.length(); incrementVal++) {
                    JSONObject singleRow = itemsArray.getJSONObject(incrementVal);
                    JSONObject itemObject = new JSONObject();
                    try {
                        itemObject.put(Constants.SSROItemGUID, singleRow.get(Constants.SSROItemGUID));
                        itemObject.put(Constants.RejRsns, singleRow.get(Constants.RejRsns));
                        itemObject.put(Constants.StockType, singleRow.get(Constants.StockType));
//                        itemObject.put(Constants.Batch, singleRow.get(Constants.Batch));
//                        itemObject.put(Constants.StockRefGUID, singleRow.get(Constants.StockRefGUID));
                        itemObject.put(Constants.Currency, singleRow.get(Constants.Currency));
                        itemObject.put(Constants.GrossAmount, singleRow.get(Constants.GrossAmount));
                        itemObject.put(Constants.SSROGUID, singleRow.get(Constants.SSROGUID));
                        itemObject.put(Constants.ItemNo, singleRow.get(Constants.ItemNo));
                        itemObject.put(Constants.MaterialNo, singleRow.get(Constants.MaterialNo));
                        itemObject.put(Constants.MaterialDesc, singleRow.get(Constants.MaterialDesc));
                        itemObject.put(Constants.MRP, singleRow.get(Constants.MRP));
                        itemObject.put(Constants.NetAmount, singleRow.get(Constants.NetAmount));
                        itemObject.put(Constants.OrdMatGrp, singleRow.get(Constants.OrderMatGrp));
                        itemObject.put(Constants.Quantity, singleRow.get(Constants.Quantity));
//                itemObject.put(Constants.OrderMatGrpDesc, singleRow.get(Constants.OrderMatGrpDesc));
                        itemObject.put(Constants.RejectionReasonID, singleRow.get(Constants.RejectionReasonID));
                        itemObject.put(Constants.Remarks, singleRow.get(Constants.Remarks));
                        itemObject.put(Constants.StatusID, singleRow.get(Constants.StatusID));
                        try {
                            if (!singleRow.optString(Constants.Tax).equalsIgnoreCase("")) {
                                itemObject.put(Constants.Tax, singleRow.optString(Constants.Tax));
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        try {
                            if (!singleRow.optString(Constants.Tax1).equalsIgnoreCase("")) {
                                itemObject.put(Constants.Tax1, singleRow.optString(Constants.Tax1));
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        try {
                            if (!singleRow.optString(Constants.Tax2).equalsIgnoreCase("")) {
                                itemObject.put(Constants.Tax2, singleRow.optString(Constants.Tax2));
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        try {
                            if (singleRow.has(Constants.UnitPrice)&&!singleRow.optString(Constants.UnitPrice).equalsIgnoreCase("")) {
                                itemObject.put(Constants.UnitPrice, singleRow.get(Constants.UnitPrice));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (singleRow.has(Constants.Uom)&&!singleRow.optString(Constants.Uom).equalsIgnoreCase("")) {
                                itemObject.put(Constants.UOM, singleRow.get(Constants.Uom));
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        try {
                            if (singleRow.has(Constants.UOM)&&!singleRow.optString(Constants.UOM).equalsIgnoreCase("")) {
                                itemObject.put(Constants.UOM, singleRow.get(Constants.Uom));
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        try {
                            if (singleRow.has(Constants.AlternativeUOM1)&&!singleRow.optString(Constants.AlternativeUOM1).equalsIgnoreCase("")) {
                                itemObject.put(Constants.AlternativeUOM1, singleRow.get(Constants.AlternativeUOM1));
                            }
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                        try {
                            if (singleRow.has(Constants.AlternativeUOM1Den)&&!singleRow.optString(Constants.AlternativeUOM1Den).equalsIgnoreCase("")) {
                                itemObject.put(Constants.AlternativeUOM1Den, singleRow.get(Constants.AlternativeUOM1Den));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (singleRow.has(Constants.AlternativeUOM1Num)&&!singleRow.optString(Constants.AlternativeUOM1Num).equalsIgnoreCase("")) {
                                itemObject.put(Constants.AlternativeUOM1Num, singleRow.get(Constants.AlternativeUOM1Num));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (singleRow.has(Constants.Brand)&&!singleRow.optString(Constants.Brand).equalsIgnoreCase("")) {
                                itemObject.put(Constants.Brand, singleRow.get(Constants.Brand));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (singleRow.has(Constants.DmsDivision)&&!singleRow.optString(Constants.DmsDivision).equalsIgnoreCase("")) {
                                itemObject.put(Constants.DmsDivision, singleRow.get(Constants.DmsDivision));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    jsonArray.put(itemObject);
                }
                dbHeadTable.put(Constants.SSROItemDetails, jsonArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return dbHeadTable;
        }


        public static String getCBBQty (SKUGroupBean skuGroupBean){
            double doublAltUom1Num = 0.0;
            double doublAltUom1Den = 0.0;
            Double doublCBBQty = 0.0;
            Double mDoubOrderQty = 0.0;
            Double doublCalQty = 0.0;
            String alternativeUOM = "";
            String selectedUOM = "";

            try {
                alternativeUOM = skuGroupBean.getAlternativeUOM1();
                selectedUOM = skuGroupBean.getSelectedUOM();
                try {
                    mDoubOrderQty = Double.parseDouble(skuGroupBean.getEtQty());
                } catch (NumberFormatException e) {
                    mDoubOrderQty = 0.0;
                    e.printStackTrace();
                }

                try {
                    doublAltUom1Num = Double.parseDouble(skuGroupBean.getAlternativeUOM1Num());
                } catch (NumberFormatException e) {
                    doublAltUom1Num = 0.0;
                    e.printStackTrace();
                }

                try {
                    doublAltUom1Den = Double.parseDouble(skuGroupBean.getAlternativeUOM1Den());
                } catch (NumberFormatException e) {
                    doublAltUom1Den = 0.0;
                    e.printStackTrace();
                }


                if (doublAltUom1Num > 0) {
                    if (doublAltUom1Num <= doublAltUom1Den) { // Emami and Pal Case
                        if (alternativeUOM.equalsIgnoreCase(selectedUOM)) {
                            try {
                                doublCalQty = mDoubOrderQty / doublAltUom1Num;
                            } catch (Exception e) {
                                doublCalQty = 0.0;
                            }
                            if (doublCalQty.isNaN() || doublCalQty.isInfinite()) {
                                doublCalQty = 0.0;
                            }

                            doublCBBQty = ConstantsUtils.decimalRoundOff(new BigDecimal(doublCalQty), 3).doubleValue();
                        } else {
                            try {
                                doublCalQty = mDoubOrderQty / doublAltUom1Den;
                            } catch (Exception e) {
                                doublCalQty = 0.0;
                            }
                            if (doublCalQty.isNaN() || doublCalQty.isInfinite()) {
                                doublCalQty = 0.0;
                            }

                            try {
                                doublCBBQty = doublCalQty * doublAltUom1Num;
                            } catch (Exception e) {
                                doublCBBQty = 0.0;
                            }

                            if (doublCBBQty.isNaN() || doublCBBQty.isInfinite()) {
                                doublCBBQty = 0.0;
                            }

                            doublCBBQty = ConstantsUtils.decimalRoundOff(new BigDecimal(doublCBBQty), 3).doubleValue();
                        }


                    } else { // RSPL

                        if (!selectedUOM.equalsIgnoreCase("")) {
                            if (alternativeUOM.equalsIgnoreCase(selectedUOM)) {

                                try {
                                    doublCalQty = mDoubOrderQty / doublAltUom1Num;
                                } catch (Exception e) {
                                    doublCalQty = 0.0;
                                }
                                if (doublCalQty.isNaN() || doublCalQty.isInfinite()) {
                                    doublCalQty = 0.0;
                                }

                                doublCBBQty = ConstantsUtils.decimalRoundOff(new BigDecimal(doublCalQty), 3).doubleValue();
                            } else {
                                try {
                                    doublCalQty = mDoubOrderQty / doublAltUom1Den;
                                } catch (Exception e) {
                                    doublCalQty = 0.0;
                                }
                                if (doublCalQty.isNaN() || doublCalQty.isInfinite()) {
                                    doublCalQty = 0.0;
                                }

                                try {
                                    doublCBBQty = doublCalQty * doublAltUom1Den;
                                } catch (Exception e) {
                                    doublCBBQty = 0.0;
                                }

                                if (doublCBBQty.isNaN() || doublCBBQty.isInfinite()) {
                                    doublCBBQty = 0.0;
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                doublCBBQty = 0.0;
                e.printStackTrace();
            }
            return doublCBBQty + "";

        }

        public static void hideProgressDialog (ProgressDialog progressDialog){
            try {
                if (progressDialog != null)
                    progressDialog.hide();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static List<ODataEntity> getListEntities (String mStrQry, ODataOfflineStore
        oDataOfflineStore){
            List<ODataEntity> entities = null;
            try {
                entities = UtilOfflineManager.getEntities(oDataOfflineStore, mStrQry);
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }
            return entities;
        }

        public static boolean isCBBOrBag (String onSaleUnitID){
            boolean isCBB = false;
            if (onSaleUnitID != null && !onSaleUnitID.equalsIgnoreCase("")) {
                if (onSaleUnitID.equalsIgnoreCase(Constants.SchemeSaleUnitIDCBB) || onSaleUnitID.equalsIgnoreCase(Constants.SchemeSaleUnitIDBAG)) {
                    isCBB = true;
                }
            } else {
                isCBB = false;
            }
            return isCBB;
        }


        public static String getTotalPieceFromCBB (SKUGroupBean skuGroupBean){
            double doublAltUom1Num = 0.0;
            double doublAltUom1Den = 0.0;
            Double doublPCQty = 0.0;
            Double doublCalQty = 0.0;
            Double mDoubleCBBQty = 0.0;
            try {
                try {
                    doublAltUom1Num = Double.parseDouble(skuGroupBean.getAlternativeUOM1Num());
                } catch (NumberFormatException e) {
                    doublAltUom1Num = 0.0;
                    e.printStackTrace();
                }

                try {
                    doublAltUom1Den = Double.parseDouble(skuGroupBean.getAlternativeUOM1Den());
                } catch (NumberFormatException e) {
                    doublAltUom1Den = 0.0;
                    e.printStackTrace();
                }
                try {
                    mDoubleCBBQty = Double.parseDouble(skuGroupBean.getCBBQty());
                } catch (NumberFormatException e) {
                    mDoubleCBBQty = 0.0;
                    e.printStackTrace();
                }


                if (doublAltUom1Num > 0) {
                    if (doublAltUom1Num <= doublAltUom1Den) { // Emami and Pal Case
                        try {
                            doublCalQty = mDoubleCBBQty * doublAltUom1Den;
                        } catch (Exception e) {
                            doublCalQty = 0.0;
                        }
                        if (doublCalQty.isNaN() || doublCalQty.isInfinite()) {
                            doublCalQty = 0.0;
                        }

                        try {
                            doublPCQty = doublCalQty * doublAltUom1Num;
                        } catch (Exception e) {
                            doublPCQty = 0.0;
                        }

                        if (doublPCQty.isNaN() || doublPCQty.isInfinite()) {
                            doublPCQty = 0.0;
                        }

                        doublPCQty = ConstantsUtils.decimalRoundOff(new BigDecimal(doublPCQty), 3).doubleValue();
                    } else { // RSPL
                        if (!skuGroupBean.getSelectedUOM().equalsIgnoreCase(skuGroupBean.getUOM()) && !skuGroupBean.getSelectedUOM().equalsIgnoreCase("")) {
                            try {
                                doublCalQty = mDoubleCBBQty * doublAltUom1Num;
                            } catch (Exception e) {
                                doublCalQty = 0.0;
                            }
                            if (doublCalQty.isNaN() || doublCalQty.isInfinite()) {
                                doublCalQty = 0.0;
                            }

                            try {
                                doublPCQty = doublCalQty;
                            } catch (Exception e) {
                                doublPCQty = 0.0;
                            }

                            if (doublPCQty.isNaN() || doublPCQty.isInfinite()) {
                                doublPCQty = 0.0;
                            }
                        } else {
                            try {
                                doublCalQty = mDoubleCBBQty * doublAltUom1Num;
                            } catch (Exception e) {
                                doublCalQty = 0.0;
                            }
                            if (doublCalQty.isNaN() || doublCalQty.isInfinite()) {
                                doublCalQty = 0.0;
                            }

                            try {
                                doublPCQty = doublCalQty;
                            } catch (Exception e) {
                                doublPCQty = 0.0;
                            }

                            if (doublPCQty.isNaN() || doublPCQty.isInfinite()) {
                                doublPCQty = 0.0;
                            }
                        }


                        doublPCQty = ConstantsUtils.decimalRoundOff(new BigDecimal(doublPCQty), 3).doubleValue();
                    }
                }
            } catch (Exception e) {
                doublPCQty = 0.0;
                e.printStackTrace();
            }
            return doublPCQty + "";

        }

        public static boolean compareTwoDates (String mStrDate, String mStrDateTwo){
            boolean mBoolValid = false;
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-mm-dd");
            Date convertedDate = new Date();
            Date convertedDate2 = new Date();
            try {
                convertedDate = dateFormat.parse(mStrDate);
                convertedDate2 = dateFormat.parse(mStrDateTwo);

                Calendar cal2 = Calendar.getInstance();
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(convertedDate);
                cal2.setTime(convertedDate2);

                if (cal1.after(cal2)) {
                    mBoolValid = false;
                    System.out.println("Date1 is after Date2");
                } else if (cal1.before(cal2)) {
                    mBoolValid = true;
                    System.out.println("Date1 is before Date2");
                } else {
                    mBoolValid = false;
                    System.out.println("Date1 is equal to Date2");
                }


           /* if (convertedDate.compareTo(convertedDate2) > 0) {
                mBoolValid = true;
                System.out.println("Date1 is after Date2");
            } else if (convertedDate.compareTo(convertedDate2) < 0) {
                mBoolValid = false;
                System.out.println("Date1 is before Date2");
            } else if (convertedDate.compareTo(convertedDate2) == 0) {
                mBoolValid = false;
                System.out.println("Date1 is equal to Date2");
            }*/


            } catch (ParseException e) {
                mBoolValid = false;
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return mBoolValid;
        }
        @NonNull
        public static Date convertStringToDate (String dates) throws ParseException {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return dateFormat.parse(dates);
        }
        public static JSONObject getExpenseHeaderValuesFromJsonObject (JSONObject
        fetchJsonHeaderObject){
            JSONObject dbHeadTable = new JSONObject();
            try {

                dbHeadTable.put(Constants.ExpenseGUID, fetchJsonHeaderObject.getString(Constants.ExpenseGUID));
//            dbHeadTable.put(Constants.LoginID, fetchJsonHeaderObject.getString(Constants.LoginID));
                dbHeadTable.put(Constants.ExpenseNo, fetchJsonHeaderObject.getString(Constants.ExpenseNo));
                dbHeadTable.put(Constants.FiscalYear, fetchJsonHeaderObject.getString(Constants.FiscalYear));
                dbHeadTable.put(Constants.CPName, fetchJsonHeaderObject.getString(Constants.CPName));
                dbHeadTable.put(Constants.CPGUID, fetchJsonHeaderObject.getString(Constants.CPGUID));
                dbHeadTable.put(Constants.CPNo, fetchJsonHeaderObject.getString(Constants.CPNo));
                dbHeadTable.put(Constants.CPType, fetchJsonHeaderObject.getString(Constants.CPType));
                dbHeadTable.put(Constants.CPTypeDesc, fetchJsonHeaderObject.getString(Constants.CPTypeDesc));
                dbHeadTable.put(Constants.SPGUID, fetchJsonHeaderObject.optString(Constants.SPGUID).toUpperCase());
                dbHeadTable.put(Constants.SPNo, fetchJsonHeaderObject.getString(Constants.SPNo));
                dbHeadTable.put(Constants.SPName, fetchJsonHeaderObject.getString(Constants.SPName));
                dbHeadTable.put(Constants.ExpenseType, fetchJsonHeaderObject.getString(Constants.ExpenseType));
                dbHeadTable.put(Constants.ExpenseTypeDesc, fetchJsonHeaderObject.getString(Constants.ExpenseTypeDesc));
                dbHeadTable.put(Constants.ExpenseDate, fetchJsonHeaderObject.getString(Constants.ExpenseDate) + "T00:00:00");
                dbHeadTable.put(Constants.Status, fetchJsonHeaderObject.getString(Constants.Status));
                dbHeadTable.put(Constants.StatusDesc, fetchJsonHeaderObject.getString(Constants.StatusDesc));
                dbHeadTable.put(Constants.Amount, fetchJsonHeaderObject.getString(Constants.Amount));
                dbHeadTable.put(Constants.Currency, fetchJsonHeaderObject.getString(Constants.Currency));

                JSONArray itemsArray = new JSONArray(fetchJsonHeaderObject.getString(Constants.ITEM_TXT));
                JSONArray jsonArray = new JSONArray();
                for (int incrementVal = 0; incrementVal < itemsArray.length(); incrementVal++) {
                    JSONObject singleRow = itemsArray.getJSONObject(incrementVal);
                    JSONObject itemObject = new JSONObject();
                    itemObject.put(Constants.ExpenseItemGUID, singleRow.get(Constants.ExpenseItemGUID));
                    itemObject.put(Constants.ExpenseGUID, singleRow.get(Constants.ExpenseGUID));
                    itemObject.put(Constants.ExpeseItemNo, singleRow.get(Constants.ExpeseItemNo));
                    itemObject.put(Constants.ExpenseItemType, singleRow.get(Constants.ExpenseItemType));
                    itemObject.put(Constants.ExpenseItemTypeDesc, singleRow.get(Constants.ExpenseItemTypeDesc));
                    if (!TextUtils.isEmpty(singleRow.optString(Constants.BeatGUID))) {
                        itemObject.put(Constants.BeatGUID, singleRow.get(Constants.BeatGUID));
                    }
                    itemObject.put(Constants.Location, singleRow.get(Constants.Location));
                    itemObject.put(Constants.ConvenyanceMode, singleRow.get(Constants.ConvenyanceMode));
                    itemObject.put(Constants.ConvenyanceModeDs, singleRow.get(Constants.ConvenyanceModeDs));
                    if (!TextUtils.isEmpty(singleRow.optString(Constants.BeatDistance))) {
                        itemObject.put(Constants.BeatDistance, singleRow.optString(Constants.BeatDistance));
                    }
                    itemObject.put(Constants.Amount, singleRow.get(Constants.Amount));
                    itemObject.put(Constants.UOM, singleRow.get(Constants.UOM));
                    itemObject.put(Constants.Currency, singleRow.get(Constants.Currency));
                    itemObject.put(Constants.Remarks, singleRow.get(Constants.Remarks));
                    jsonArray.put(itemObject);
                }
                dbHeadTable.put(Constants.ExpenseItemDetails, jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return dbHeadTable;
        }

        public static String makeInvoiceListQry
        (ArrayList < InvoiceListBean > ssInvArrayList, String guidColumn){
            String ssINVListQry = "";
            if (ssInvArrayList != null && ssInvArrayList.size() > 0) {
                for (int incVal = 0; incVal < ssInvArrayList.size(); incVal++) {
                    if (incVal == 0 && incVal == ssInvArrayList.size() - 1) {
                        ssINVListQry = ssINVListQry
                                + "(" + guidColumn + "%20eq%20guid'"
                                + ssInvArrayList.get(incVal).getInvoiceGuid().toUpperCase() + "')";

                    } else if (incVal == 0) {
                        ssINVListQry = ssINVListQry
                                + "(" + guidColumn + "%20eq%20guid'"
                                + ssInvArrayList.get(incVal).getInvoiceGuid().toUpperCase() + "'";

                    } else if (incVal == ssInvArrayList.size() - 1) {
                        ssINVListQry = ssINVListQry
                                + "%20or%20" + guidColumn + "%20eq%20guid'"
                                + ssInvArrayList.get(incVal).getInvoiceGuid().toUpperCase() + "')";
                    } else {
                        ssINVListQry = ssINVListQry
                                + "%20or%20" + guidColumn + "%20eq%20guid'"
                                + ssInvArrayList.get(incVal).getInvoiceGuid().toUpperCase() + "'";
                    }
                }
            } else {
                ssINVListQry = "";
            }
            return ssINVListQry;
        }

        public static boolean isCBBOrBagItemMinmum (String onSaleUnitID){
            boolean isCBB = false;
            if (onSaleUnitID != null && !onSaleUnitID.equalsIgnoreCase("")) {
                if (onSaleUnitID.equalsIgnoreCase(Constants.SchemeItemMinSaleUnitIDCBB) || onSaleUnitID.equalsIgnoreCase(Constants.SchemeItemMinSaleUnitIDBag)) {
                    isCBB = true;
                }
            } else {
                isCBB = false;
            }
            return isCBB;
        }

        public static String getCalInvoiceAlterQty (ReturnOrderBean skuGroupBean){
            double doublAltUom1Num = 0.0;
            double doublAltUom1Den = 0.0;
            Double doublCBBQty = 0.0;
            Double doublCalQty = 0.0;

            try {

                try {
                    doublAltUom1Num = Double.parseDouble(skuGroupBean.getAlternativeUOM1Num());
                } catch (NumberFormatException e) {
                    doublAltUom1Num = 0.0;
                    e.printStackTrace();
                }

                try {
                    doublAltUom1Den = Double.parseDouble(skuGroupBean.getAlternativeUOM1Den());
                } catch (NumberFormatException e) {
                    doublAltUom1Den = 0.0;
                    e.printStackTrace();
                }


                if (doublAltUom1Num > 0) {
                    if (doublAltUom1Num <= doublAltUom1Den) { // Emami and Pal Case
                        try {
                            doublCalQty = doublAltUom1Num / doublAltUom1Den;
                        } catch (Exception e) {
                            doublCalQty = 0.0;
                        }
                        if (doublCalQty.isNaN() || doublCalQty.isInfinite()) {
                            doublCalQty = 0.0;
                        }


                        doublCBBQty = ConstantsUtils.decimalRoundOff(new BigDecimal(doublCalQty), 3).doubleValue();


                    } else { // RSPL

                        try {
                            doublCalQty = doublAltUom1Den / doublAltUom1Num;
                        } catch (Exception e) {
                            doublCalQty = 0.0;
                        }
                        if (doublCalQty.isNaN() || doublCalQty.isInfinite()) {
                            doublCalQty = 0.0;
                        }
                        doublCBBQty = ConstantsUtils.decimalRoundOff(new BigDecimal(doublCalQty), 3).doubleValue();

                    }
                }
            } catch (Exception e) {
                doublCBBQty = 0.0;
                e.printStackTrace();
            }
            return doublCBBQty + "";

        }
        public static ArrayList<String> getEntityNames () {
            ArrayList<String> alEntityList = new ArrayList<>();
            alEntityList.add(Constants.SecondarySOCreate);
            alEntityList.add(Constants.ChannelPartners);

            return alEntityList;
        }

        public static String get12HoursFromat (String mStrTime){
            String mStrTimeFormat = "";
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                Date dateObj = sdf.parse(mStrTime);
                mStrTimeFormat = new SimpleDateFormat("h:mm aa").format(dateObj);
            } catch (final ParseException e) {
                mStrTimeFormat = "00:00";
            }
            return mStrTimeFormat;
        }

        public static String getOrderCountFromSSSOs (Context context, String mStrCurrentDate){

            String mSOOrderType = getSOOrderType();
            String mStrRetQry = "";
            int totalOrderCount = 0;
            alRetailersCount.clear();
            String mOfflineOrderCount = "0", mDataVaultOrderCount = "0";
            try {
                if (Constants.alRetailersGuid36 != null && Constants.alRetailersGuid36.size() > 0) {
                    for (int i = 0; i < Constants.alRetailersGuid36.size(); i++) {
                        if (i == 0 && i == Constants.alRetailersGuid36.size() - 1) {
                            mStrRetQry = mStrRetQry
                                    + "(" + Constants.SoldToCPGUID + "%20eq%20 guid'"
                                    + Constants.alRetailersGuid36.get(i).toUpperCase() + "')";


                        } else if (i == 0) {
                            mStrRetQry = mStrRetQry
                                    + "(" + Constants.SoldToCPGUID + "%20eq%20guid'"
                                    + Constants.alRetailersGuid36.get(i).toUpperCase() + "'";


                        } else if (i == Constants.alRetailersGuid36.size() - 1) {
                            mStrRetQry = mStrRetQry
                                    + "%20or%20" + Constants.SoldToCPGUID + "%20eq%20guid'"
                                    + Constants.alRetailersGuid36.get(i).toUpperCase() + "')";

                        } else {
                            mStrRetQry = mStrRetQry
                                    + "%20or%20" + Constants.SoldToCPGUID + "%20eq%20guid'"
                                    + Constants.alRetailersGuid36.get(i).toUpperCase() + "'";

                        }


                    }


                    String mStrRetTotalQry = Constants.SSSOs + " ?$filter=" + Constants.OrderDate + " eq datetime'" + mStrCurrentDate + "' " +
                            "and " + Constants.OrderType + " eq '" + mSOOrderType + "' and Status ne '000004' and " + mStrRetQry + " ";

                    List<ODataEntity> entities = null;
                    try {
                        entities = UtilOfflineManager.getEntities(offlineStore, mStrRetTotalQry);
                    } catch (OfflineODataStoreException e) {
                        e.printStackTrace();
                    }
                    try {
                        ODataProperty property;
                        ODataPropMap properties;
                        if (entities != null && entities.size() > 0) {
                            for (ODataEntity oDataEntity : entities) {
                                properties = oDataEntity.getProperties();
                                property = properties.get(Constants.SoldToCPGUID);
                                ODataGuid mGUIDVal = null;
                                String cpGUID = "";
                                try {
                                    mGUIDVal = (ODataGuid) property.getValue();
                                    cpGUID = mGUIDVal.guidAsString36().toUpperCase();
                                } catch (Exception e) {
                                    cpGUID = "";
                                    e.printStackTrace();
                                }
                                if (!alRetailersCount.contains(cpGUID.toUpperCase())) {
                                    alRetailersCount.add(cpGUID.toUpperCase());
                                }
                            }
                            mOfflineOrderCount = entities.size() + "";
                        } else {
                            mOfflineOrderCount = "0";
                        }
                    } catch (Exception e) {
                        mOfflineOrderCount = "0";
                        e.printStackTrace();
                    }

                    try {
                        mDataVaultOrderCount = OfflineManager.getDeviceOrderCount(Constants.SecondarySOCreate, context, mStrCurrentDate, Constants.alRetailersGuid36);
                    } catch (Exception e) {
                        mDataVaultOrderCount = "0";
                    }
                    try {
                        totalOrderCount = Constants.alRetailersCount.size();
                    } catch (NumberFormatException e) {
                        totalOrderCount = 0;
                        e.printStackTrace();
                    }
                } else {
                    totalOrderCount = 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return totalOrderCount + "";
        }

        public static String getCountryCode (Context mContext){
            String mConCode = "91";
            try {
                TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
                String countryIso = telephonyManager.getSimCountryIso().toUpperCase();
                mConCode = PhoneNumberUtil.getInstance().getCountryCodeForRegion(countryIso) + "";
            } catch (Exception e) {
                mConCode = "91";
                e.printStackTrace();
            }
            return mConCode;
        }

        public static String getCurrencyFromSP () {
            String mStrCurrency = "";
            try {
                mStrCurrency = OfflineManager.getValueByColumnName(Constants.SalesPersons + "?$top=1 &$select=" + Constants.Currency, Constants.Currency);
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }
            return mStrCurrency;
        }
        public static SimpleDateFormat getDateFormat (String customFormat){
            return new SimpleDateFormat(customFormat);
        }

        public static JSONObject getMaterialDocsHeaderValuesFromJsonObject (JSONObject
        fetchJsonHeaderObject){
            JSONObject dbHeadTable = new JSONObject();
            try {
                dbHeadTable.put(Constants.MaterialDocGUID, fetchJsonHeaderObject.getString(Constants.MaterialDocGUID));
                dbHeadTable.put(Constants.FromGUID, fetchJsonHeaderObject.getString(Constants.FromGUID));
                dbHeadTable.put(Constants.FromTypeID, fetchJsonHeaderObject.getString(Constants.FromTypeID));
                dbHeadTable.put(Constants.ToGUID, fetchJsonHeaderObject.getString(Constants.ToGUID).replaceAll("-", ""));
                dbHeadTable.put(Constants.ToTypeID, fetchJsonHeaderObject.getString(Constants.ToTypeID));
                dbHeadTable.put(Constants.Source, fetchJsonHeaderObject.getString(Constants.Source));
                dbHeadTable.put(Constants.MaterialDocDate, fetchJsonHeaderObject.getString(Constants.MaterialDocDate));
                dbHeadTable.put(Constants.MvmtTypeID, fetchJsonHeaderObject.getString(Constants.MvmtTypeID));
                dbHeadTable.put(Constants.RefType, fetchJsonHeaderObject.getString(Constants.RefType));
                dbHeadTable.put(Constants.RefDocTypeID, fetchJsonHeaderObject.getString(Constants.RefDocTypeID));
                try {
                    dbHeadTable.put(Constants.VehicleNo, fetchJsonHeaderObject.getString(Constants.VehicleNo));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            /*try {
                dbHeadTable.put(Constants.VehicleRepDate, fetchJsonHeaderObject.getString(Constants.VehicleRepDate));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                dbHeadTable.put(Constants.UnloadDate, fetchJsonHeaderObject.getString(Constants.UnloadDate));
            } catch (JSONException e) {
                e.printStackTrace();
            }*/

                JSONArray itemsArray = new JSONArray(fetchJsonHeaderObject.getString(Constants.ITEM_TXT));
                JSONArray jsonArray = new JSONArray();
//            JSONArray splitArray = new JSONArray();
                for (int incrementVal = 0; incrementVal < itemsArray.length(); incrementVal++) {
                    JSONObject singleRow = itemsArray.getJSONObject(incrementVal);
                    JSONObject itemObject = new JSONObject();
                    itemObject.put(Constants.MatDocItemGUID, singleRow.get(Constants.MatDocItemGUID));
                    try {
                        itemObject.put(Constants.MaterialDocGUID, singleRow.get(Constants.MaterialDocGUID));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    itemObject.put(Constants.RefDocNo, singleRow.get(Constants.RefDocNo));
                    itemObject.put(Constants.RefType, singleRow.get(Constants.RefType));
                    itemObject.put(Constants.RefDocItemNo, singleRow.get(Constants.RefDocItemNo));
                    itemObject.put(Constants.MaterialDocQty, singleRow.get(Constants.MaterialDocQty));
                    itemObject.put(Constants.ItemNo, singleRow.get(Constants.ItemNo));
                    itemObject.put(Constants.MaterialNo, singleRow.get(Constants.MaterialNo));
                    itemObject.put(Constants.MaterialDesc, singleRow.get(Constants.MaterialDesc));
                    itemObject.put(Constants.Currency, singleRow.get(Constants.Currency));
                    itemObject.put(Constants.UOM, singleRow.get(Constants.UOM));
                    if (singleRow.get(Constants.NetValue) != null && !singleRow.get(Constants.NetValue).equals("")) {
                        itemObject.put(Constants.NetValue, singleRow.get(Constants.NetValue));
                    }
                    itemObject.put(Constants.MRP, singleRow.get(Constants.MRP));
                    itemObject.put(Constants.Price, singleRow.get(Constants.Price));
                    itemObject.put(Constants.Batch, singleRow.get(Constants.Batch));
                    try {
                        itemObject.put(Constants.ExpiryDate, singleRow.get(Constants.ExpiryDate));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        itemObject.put(Constants.MFD, singleRow.get(Constants.MFD));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONArray splitArray = new JSONArray();
                    String docsSplitItems = singleRow.getString(Constants.ITEM_TXT_DOC_SPLIT);
                    JSONArray docsSplitArray = new JSONArray(docsSplitItems);
//                for (int incrementValue = 0; incrementValue < docsSplitArray.length(); incrementValue++){
                    JSONObject singleRowDoc = docsSplitArray.getJSONObject(0);
                    JSONObject itemObjectDoc = new JSONObject();
                    itemObjectDoc.put(Constants.RefDocNo, singleRowDoc.get(Constants.RefDocNo));
                    itemObjectDoc.put(Constants.RefDocItemNo, singleRowDoc.get(Constants.RefDocItemNo));
                    itemObjectDoc.put(Constants.UOM, singleRowDoc.get(Constants.UOM));
                    itemObjectDoc.put(Constants.MaterialDocQty, singleRowDoc.get(Constants.MaterialDocQty));
                    itemObjectDoc.put(Constants.MaterialCatID, singleRowDoc.get(Constants.MaterialCatID));
                    itemObjectDoc.put(Constants.MaterialCatDesc, singleRowDoc.get(Constants.MaterialCatDesc));
                    splitArray.put(itemObjectDoc);
//                }
                    itemObject.put(Constants.MaterialDocItemCatSplits, splitArray);
                    jsonArray.put(itemObject);
                }
                dbHeadTable.put(Constants.MaterialDocItemDetails, jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return dbHeadTable;
        }
        public static void dialogBoxWithCallBack (Context context, String title, String
        message, String positiveButton, String negativeButton,boolean isCancel,
        final DialogCallBack dialogCallBack){
            try {
                try {
                    new DialogFactory.Alert(context).setPositiveButton(positiveButton).isAlert(false).setNegativeButton(negativeButton).setTheme(R.style.MyDialogTheme_new).setMessage(message).setOnDialogClick(new OnDialogClick() {
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
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public static void dialogBoxWithCallBack2 (Context context, String title, String
        message, String positiveButton, String negativeButton,boolean isCancel,
        final DialogCallBack dialogCallBack){
            try {
                try {
                    new DialogFactory.Alert(context).setPositiveButton(positiveButton).isAlert(false).setNegativeButton(negativeButton).setMessage(message).setOnDialogClick(new OnDialogClick() {
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
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    public static void dialogBoxWithCallBack1 (Context context, String title, String
            message, String positiveButton, String negativeButton,boolean isCancel,
                                              final DialogCallBack dialogCallBack){
        try {
            try {
                new DialogFactory.Alert(context).setPositiveButton(positiveButton).isAlert(true).setNegativeButton(negativeButton).setTheme(R.style.MyDialogTheme_new).setMessage(message).setOnDialogClick(new OnDialogClick() {
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

        public static void showAlert (String message, Context context){
            try {
                new DialogFactory.Alert(context).setMessage(message).isAlert(true)
                        .setTheme(R.style.MyDialogTheme_new).setPositiveButton(context.getString(R.string.msg_ok))
                        .setOnDialogClick(isPositive -> {
                            if (isPositive) {
                            }
                        })
                        .show();
//                AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MaterialAlertDialog);
//                String timeStamp = (String) android.text.format.DateFormat.format("dd-MM-yyyy hh:mm", new Date());
//                builder.setTitle(timeStamp);
////            builder.setMessage(message+"\n\n"+timeStamp+"").setCancelable(false)
//                builder.setMessage(message).setCancelable(false)
//                        .setPositiveButton(context.getString(R.string.msg_ok), new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//                builder.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
            public static void navigateToPlayStore (String message, Context context, String packageName)
            {
                try {
                    new DialogFactory.Alert(context).setPositiveButton(context.getString(R.string.msg_ok)).isAlert(true).setTheme(R.style.MaterialAlertDialog).setMessage(message).setOnDialogClick(new OnDialogClick() {
                        @Override
                        public void onDialogClick(boolean isPositive) {
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName + "")));
                        }
                    }).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        public static String getTotalLineCuts (Context context){
            String itemCount = "0";
            try {
                ArrayList<String> alSSS0Guid = getSSSOGuid();
                itemCount = getSSSOiteMDeatils(alSSS0Guid, context);
            } catch (Exception e) {
                itemCount = "0";
                e.printStackTrace();
            }
            return itemCount;
        }

        private static ArrayList<String> getSSSOGuid () {
            String mStrGuid = "";
            ODataGuid oDataGuid = null;
            ArrayList<String> alSSS0Guid = new ArrayList<>();
            try {
                String mStSSSOlistQry = Constants.SSSOs +
                        "?$filter=" + Constants.OrderDate + " eq datetime'" + UtilConstants.getNewDate() + "' and " + Constants.SPGUID + " eq guid'" + Constants.getSPGUID().toLowerCase() + "'";

                List<ODataEntity> entities = null;
                try {
                    entities = UtilOfflineManager.getEntities(offlineStore, mStSSSOlistQry);
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }
                try {
                    ODataProperty property;
                    ODataPropMap properties;
                    if (entities != null && entities.size() > 0) {
                        for (ODataEntity oDataEntity : entities) {
                            properties = oDataEntity.getProperties();
                            property = properties.get(Constants.SSSOGuid);
                            oDataGuid = (ODataGuid) property.getValue();
                            if (oDataGuid != null) {
                                mStrGuid = oDataGuid.guidAsString36();
                                alSSS0Guid.add(mStrGuid);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("Test", "getSSSODetails");
            return alSSS0Guid;
        }

        private static String getSSSOiteMDeatils (ArrayList < String > alSSS0Guid, Context context){
            String mItemQry = "";
            String strItemCount = "0";
            int itemSSSOCount = 0;
            int deviceItemSSSOCount = 0;
            ODataGuid sssoGuid = null;
            String sssoGuidStr = "";
            try {
                if (alSSS0Guid != null && alSSS0Guid.size() > 0) {
                    for (int i = 0; i < alSSS0Guid.size(); i++) {
                        if (i == 0 && i == alSSS0Guid.size() - 1) {
                            mItemQry = mItemQry
                                    + "(" + Constants.SSSOGuid + "%20eq%20guid'"
                                    + alSSS0Guid.get(i).toUpperCase() + "')";


                        } else if (i == 0) {
                            mItemQry = mItemQry
                                    + "(" + Constants.SSSOGuid + "%20eq%20guid'"
                                    + alSSS0Guid.get(i).toUpperCase() + "'";


                        } else if (i == alSSS0Guid.size() - 1) {
                            mItemQry = mItemQry
                                    + "%20or%20" + Constants.SSSOGuid + "%20eq%20guid'"
                                    + alSSS0Guid.get(i).toUpperCase() + "')";

                        } else {
                            mItemQry = mItemQry
                                    + "%20or%20" + Constants.SSSOGuid + "%20eq%20guid'"
                                    + alSSS0Guid.get(i).toUpperCase() + "'";

                        }


                    }

                    String mStrItemQry = Constants.SSSoItemDetails + "?$filter=" + mItemQry + " &$orderby=" + Constants.OrderMatGrpDesc + " desc";

                    List<ODataEntity> entities = null;
                    try {
                        entities = UtilOfflineManager.getEntities(offlineStore, mStrItemQry);
                    } catch (OfflineODataStoreException e) {
                        e.printStackTrace();
                    }
                    try {
                        ODataProperty property;
                        ODataPropMap properties;
                        if (entities != null && entities.size() > 0) {
                            itemSSSOCount = itemSSSOCount + entities.size();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                strItemCount = String.valueOf(itemSSSOCount + deviceItemSSSOCount);
            } catch (Exception e) {
                strItemCount = "0";
                e.printStackTrace();
            }
            Log.d("Test", "getSSSOItemDetails");
            return strItemCount;
        }


        public static String theMonthName () {
            int mnth = 0;
            int intYear = 0;
            String month = "";
            String year = "";
            try {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.MONTH, -1);
                Date date = c.getTime();
                java.text.DateFormat monthFormat = new SimpleDateFormat("MM");
                month = monthFormat.format(date);
                java.text.DateFormat yearFormat = new SimpleDateFormat("yyyy");
                year = yearFormat.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (!TextUtils.isEmpty(month)) {
                    mnth = (Integer.parseInt(month)) - 1;
                    if (!TextUtils.isEmpty(year))
                        intYear = (Integer.parseInt(year));
                }
            /*if(mnth<0){
                mnth = 11;
                if(!TextUtils.isEmpty(year))
                    intYear = (Integer.parseInt(year))-1;
            }*/
                String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
                return monthNames[mnth] + "'" + intYear;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return "";
        }


        public static String theMonthShortNames (String month, String year){
            int mnth = 0;
            int intYear = 0;
            try {
                if (!TextUtils.isEmpty(month)) {
                    mnth = (Integer.parseInt(month)) - 1;
                    if (!TextUtils.isEmpty(year))
                        intYear = (Integer.parseInt(year));
                }
                String[] monthNames = {"Jan", "Feb", "March", "April", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
                return monthNames[mnth] + "-" + intYear;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return "";
        }

        public static ArrayList<String> getLastSixMonth () {
            ArrayList<String> lastSixMonths = new ArrayList<>();
            Format formatter = new SimpleDateFormat("MM-yyyy");
            Calendar c = Calendar.getInstance();
            for (int i = 0; i < 6; i++) {
                c.add(Calendar.MONTH, -1);
                System.out.println(c);
                lastSixMonths.add(formatter.format(c.getTime()));
            }
            return lastSixMonths;
        }
        public static void setMandatoryCheck (String text, TextView textView){
            String simple = text;
            String colored = "*";
            SpannableStringBuilder builder = new SpannableStringBuilder(simple + colored);

            builder.setSpan(new ForegroundColorSpan(Color.RED), simple.length(), builder.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            textView.setText(builder);
        }

        public static ODataDuration getTimeAsODataDurationConvertion (String timeString){
            List<String> timeDuration = Arrays.asList(timeString.split("-"));
            int hour = Integer.parseInt(timeDuration.get(0));
            int minute = Integer.parseInt(timeDuration.get(1));
            int seconds = Integer.parseInt(timeDuration.get(2));
            ODataDurationDefaultImpl oDataDuration = null;

            try {
                oDataDuration = new ODataDurationDefaultImpl();
                oDataDuration.setHours(hour);
                oDataDuration.setMinutes(minute);
                oDataDuration.setSeconds(BigDecimal.valueOf((long) seconds));
            } catch (Exception var7) {
                var7.printStackTrace();
            }

            return oDataDuration;
        }

        public static String getSyncHistoryddmmyyyyTimeDelay () {
            String currentDateTimeString1 = (String) android.text.format.DateFormat.format("dd/MM/yyyy", new Date());
            Calendar calendar = Calendar.getInstance(); // gets a calendar using the default time zone and locale.
            calendar.add(Calendar.SECOND, 1);
            Date date = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
            String currentDateTimeString2 = dateFormat.format(date);

            String currentDateTimeString = currentDateTimeString1 + "T" + currentDateTimeString2;
            return currentDateTimeString1 + " " + currentDateTimeString2;
        }

        public static void customAlertDialogWithScrollError ( final Context context,
        final String mErrTxt, final Activity activity){
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.custom_dialog_scroll, null);

            String mStrErrorEntity = getErrorEntityName();

            TextView textview = view.findViewById(R.id.tv_err_msg);

            textview.setText("Synchronization not completed.\n" +
                    "Please check your internet connection and retry\n" +
                    "If issue persist please contact support team");

            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context, R.style.MyTheme);
            alertDialog.setCancelable(false)
                    .setPositiveButton(context.getString(R.string.msg_ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            copyMessageToClipBoard(context, mErrTxt);
                        }
                    });
            alertDialog.setView(view);
            AlertDialog alert = alertDialog.create();
            alert.show();

        }

        public static void updateLastSyncTimeToTable (Context
        mContext, ArrayList < String > alAssignColl, String refGuid, String
        syncType, RefreshListInterface listInterface){
            SyncUtils.updatingSyncHistroyTime(mContext, alAssignColl, syncType, listInterface, refGuid);
        }
        public static void SyncTimeToTable (Context
        mContext, ArrayList < String > alAssignColl, String refGuid, String
        syncType, RefreshListInterface listInterface){
            SyncUtils.SyncHistroyTime(mContext, alAssignColl, syncType, listInterface, refGuid);
        }
        public static void setScheduleAlaram (Context context,int hours,
        int minuts, int seconds, int date){
            Calendar calNow = new GregorianCalendar();
            calNow.setTimeInMillis(System.currentTimeMillis());  // Set current time

            Calendar calSet = new GregorianCalendar();
//        cal.add(Calendar.DAY_OF_YEAR, cur_cal.get(Calendar.DAY_OF_YEAR));
            calSet.set(Calendar.HOUR_OF_DAY, hours);
            calSet.set(Calendar.MINUTE, minuts);
//        cal.set(Calendar.AM_PM,Calendar.PM);
//        cal.set(Calendar.SECOND, cur_cal.get(Calendar.SECOND));
//        cal.set(Calendar.MILLISECOND, cur_cal.get(Calendar.MILLISECOND));
//        cal.set(Calendar.DATE, cur_cal.get(Calendar.DATE));
//        cal.set(Calendar.MONTH, cur_cal.get(Calendar.MONTH));
            if (calSet.compareTo(calNow) <= 0) {
                //Today Set time passed, count to tomorrow
                calSet.add(Calendar.DATE, date);
            }


            Log.d("ASWMSAlarm", "Alaram enabled");
        }

        public static void writeTimeLog (String logMessage){
            try {
//            LogManager.writeLogInfo(logMessage);
                Log.e(logMessage, ConstantsUtils.getDateFormat(null, System.currentTimeMillis()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static final String PASSWORD_LOCKED = "PASSWORD_LOCKED";
        public static final String Unauthorized = "Unauthorized";
        public static final String PASSWORD_DISABLED = "PASSWORD_DISABLED";
        public static final String INITIAL_PASSWORD_EXPIRED = "INITIAL_PASSWORD_EXPIRED";
        public static final String USER_INACTIVE = "USER_INACTIVE";
        public static final String PASSWORD_RESET_REQUIRED = "PASSWORD_RESET_REQUIRED";
        public static final String PASSWORD_CHANGE_REQUIRED = "PASSWORD_CHANGE_REQUIRED";
        public static void passwordStatusErrorMessage ( final Context context,
        final JSONObject jsonObject, RegistrationModel registrationModel, String pUserName){
            try {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String message = "";
                        String buttonMSG = "";
                        String errortext = "";
                        int code = 0;
                        if (jsonObject != null) {
                            if (jsonObject.has("code")) {
                                code = jsonObject.optInt("code");
                                LogManager.writeLogInfo("Password Status Response Code : "+code);
                            }

                            if (jsonObject.has("message")) {
                                message = jsonObject.optString("message");
                                LogManager.writeLogInfo("Password Status Response message : "+message);
                            }
                            if (code != 200 && code != 0) {
                                if (message.equalsIgnoreCase(Constants.PASSWORD_LOCKED)) {
                                    errortext = context.getString(R.string.password_lock_error_message);
                                    String userName = context.getSharedPreferences(Constants.PREFS_NAME, 0).getString(UtilRegistrationActivity.KEY_username, "");
                                    com.arteriatech.mutils.common.UtilConstants.getUserlockMessage(context, pUserName, Configuration.IDPURL,
                                            Configuration.IDPTUSRNAME, Configuration.IDPTUSRPWD, Configuration.APP_ID
                                    );
                                } else {
                                    if (message.equalsIgnoreCase(Constants.USER_INACTIVE)) {
                                        errortext = context.getString(R.string.user_inactive_error_message);
                                    } else if (message.equalsIgnoreCase(Constants.PASSWORD_RESET_REQUIRED) || message.equalsIgnoreCase(Constants.PASSWORD_CHANGE_REQUIRED)) {
                                        errortext = context.getString(R.string.password_change_error_message);
                                    } else if (message.equalsIgnoreCase(Constants.PASSWORD_DISABLED)) {
                                        errortext = context.getString(R.string.password_disable_error_message);
                                    }else if (message.equalsIgnoreCase(Constants.INITIAL_PASSWORD_EXPIRED)) {
                                        errortext = context.getString(R.string.password_initial_error_message);
                                    } else {
                                        errortext = context.getString(R.string.unauthorized_error_message);
                                    }

                                    if (message.equalsIgnoreCase(Constants.INITIAL_PASSWORD_EXPIRED) ||message.equalsIgnoreCase(Constants.PASSWORD_CHANGE_REQUIRED) || message.equalsIgnoreCase(Constants.PASSWORD_RESET_REQUIRED)) {
                                        buttonMSG = context.getString(R.string.settings_extend_password);
                                    } else {
                                        buttonMSG = context.getString(R.string.ok);
                                    }
                                    try {
                                        try {
                                            final String finalMessage = message;
                                            String finalMessage1 = message;
                                            new DialogFactory.Alert(context).setPositiveButton(buttonMSG).isAlert(true).setTheme(R.style.MaterialAlertDialog).setMessage(errortext).setOnDialogClick(new OnDialogClick() {
                                                @Override
                                                public void onDialogClick(boolean isPositive) {
                                                    if (finalMessage1.equalsIgnoreCase(Constants.PASSWORD_CHANGE_REQUIRED) || finalMessage1.equalsIgnoreCase(Constants.PASSWORD_RESET_REQUIRED)) {
                                                        if (registrationModel != null) {
                                                            Intent intentNavChangePwdScreen = new Intent(context, ChangePasswordActivity.class);
                                                            intentNavChangePwdScreen.putExtra(com.arteriatech.mutils.common.UtilConstants.RegIntentKey, registrationModel);
                                                            intentNavChangePwdScreen.putExtra("P_USER_ID", pUserName);
                                                            context.startActivity(intentNavChangePwdScreen);
                                                        }
                                                    }else if (finalMessage1.equalsIgnoreCase(Constants.INITIAL_PASSWORD_EXPIRED)){
                                                        Intent intentNavChangePwdScreen = new Intent(context, InitialPasswordActivity.class);
                                                        intentNavChangePwdScreen.putExtra(com.arteriatech.mutils.common.UtilConstants.RegIntentKey, registrationModel);
                                                        intentNavChangePwdScreen.putExtra("isRegisteration", false);
                                                        intentNavChangePwdScreen.putExtra("P_USER_ID", pUserName);
                                                        context.startActivity(intentNavChangePwdScreen);
                                                    }
                                                }
                                            }).show();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } catch (Exception var8) {
                                        var8.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                });

            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        public static String passwordStatusForIMEI ( final Context context,
        final JSONObject jsonObject, RegistrationModel registrationModel, String isComingFrom){
            String message = "";
            String buttonMSG = "";
            String errortext = "";
            try {
                int code = 0;
                if (jsonObject != null) {
                    if (jsonObject.has("code")) {
                        code = jsonObject.optInt("code");
                    }

                    if (jsonObject.has("message")) {
                        message = jsonObject.optString("message");
                    }
                    if (code != 200 && code != 0) {
                        if (message.equalsIgnoreCase(Constants.PASSWORD_LOCKED)) {
                            errortext = context.getString(R.string.password_lock_error_message);
                        } else if (message.equalsIgnoreCase(Constants.USER_INACTIVE)) {
                            errortext = context.getString(R.string.user_inactive_error_message);
                        } else if (message.equalsIgnoreCase(Constants.PASSWORD_RESET_REQUIRED) || message.equalsIgnoreCase(Constants.PASSWORD_CHANGE_REQUIRED)) {
                            errortext = context.getString(R.string.password_change_error_message);
                        } else if (message.equalsIgnoreCase(Constants.PASSWORD_DISABLED)) {
                            errortext = context.getString(R.string.password_disable_error_message);
                        } else if (code == 401 && TextUtils.isEmpty(message)) {
                            errortext = context.getString(R.string.incorrect_user_id);
                        } else if (code == 401 && message.equalsIgnoreCase(Constants.Unauthorized)) {
                            errortext = context.getString(R.string.incorrect_password);
                        } else {
                            errortext = "Registration cannot be performed due to incorrect password. Please change your password and retry";
                        }

                    }
                }

            } catch (Throwable e) {
                e.printStackTrace();
            }
            return errortext;
        }


        public static void getPasswordStatus ( final String idpTenant, final String userID,
        final String Password, final String connectionID,
        final PasswordStatusCallback statusCallback) throws IOException {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    InputStream stream = null;
                    String userDetails = null;
                    String message = "";
                    int code = 0;
                    JSONObject jsonObject = new JSONObject();
                    String hostUrlForm = idpTenant + "/service/users/password";
                    HttpsURLConnection connection = null;
                    try {
                        connection = (HttpsURLConnection) new URL(hostUrlForm).openConnection();
                        connection.setReadTimeout(30000);
                        connection.setConnectTimeout(30000);
                        String userCredentials = userID + ":" + Password;
                        String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes("UTF-8"), 2);
                        connection.setRequestProperty("Authorization", basicAuth);
                        connection.setRequestProperty("x-smp-appid", connectionID);
                        connection.addRequestProperty("Content-Type", "application/json");
                        connection.setRequestProperty("Accept", "application/json");
                        connection.setRequestMethod("POST");
                        connection.setDoOutput(true);
                        connection.setDoInput(true);
                        int responseCode = connection.getResponseCode();
                        if (responseCode == 200) {
                            code = responseCode;
                            message = "OK";
                            stream = connection.getInputStream();
                            if (stream != null) {
                                try {
                                    userDetails = ConstantsUtils.readStream(stream);
                                    if (userDetails == null) {
                                        userDetails = "";
                                    }
                                } catch (Throwable e) {
                                    userDetails = "";
                                    e.printStackTrace();
                                }
                            }
                            jsonObject.put("code", code);
                            jsonObject.put("message", message);
                            jsonObject.put("userDetails", userDetails);
                            LogManager.writeLogError("Password Status : " + "(" + code + "," + message + ")");
                        } else if (responseCode == 401) {
                            code = responseCode;
                            try {
                                message = connection.getHeaderField("X-message-code");
                            } catch (Throwable e) {
                                message = e.getMessage();
                                e.printStackTrace();
                            }
                            try {
                                stream = connection.getInputStream();
                                if (stream != null) {
                                    try {
                                        userDetails = ConstantsUtils.readStream(stream);
                                        if (userDetails == null) {
                                            userDetails = "";
                                        }
                                    } catch (Throwable e) {
                                        userDetails = "";
                                        e.printStackTrace();
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            jsonObject.put("code", code);
                            jsonObject.put("message", message);
                            jsonObject.put("userDetails", userDetails);
                            LogManager.writeLogError("Password Status : " + "(" + code + "," + message + ")");
                        } else {
                            code = 0;
                            message = "";
                            try {
                                stream = connection.getInputStream();
                                if (stream != null) {
                                    try {
                                        userDetails = ConstantsUtils.readStream(stream);
                                        if (userDetails == null) {
                                            userDetails = "";
                                        }
                                    } catch (Throwable e) {
                                        userDetails = "";
                                        e.printStackTrace();
                                    }
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            jsonObject.put("code", code);
                            jsonObject.put("message", message);
                            jsonObject.put("userDetails", userDetails);
                            LogManager.writeLogError("Password Status : " + "(" + code + "," + message + ")");
                        }
                    } catch (Exception var14) {
                        message = var14.toString();
                        try {
                            jsonObject.put("code", code);
                            jsonObject.put("message", message);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        LogManager.writeLogError("Password Status Exception " + var14.toString());
                        var14.printStackTrace();
                    } finally {
                        if (connection != null) {
                            connection.disconnect();
                        }
                    }
                    if (statusCallback != null) {
                        statusCallback.passwordStatus(jsonObject);
                    }
                }
            });
            thread.setPriority(Thread.MAX_PRIORITY);
            thread.start();

        }

        public interface PasswordStatusCallback {
            void passwordStatus(JSONObject jsonObject);
        }

        public static void writeLogsToInternalStorage (Context mContext, String logData){
            try {
                String dateAsFileName = UtilConstants.getCurrentDate();
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    File path = new File(Environment.getExternalStoragePublicDirectory("") + "/AWSMLogs",
                            dateAsFileName + ".txt");
                    if (!path.exists()) {
                        try {
                            path.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        if (path.exists()) {
                            BufferedWriter writer = new BufferedWriter(new FileWriter(path.getAbsolutePath(), true));
                            writer.write(logData);
                            writer.newLine();
                            writer.close();
                        }
                    } catch (Exception e) {
                        Log.e("Error", "Log file error", e);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void createLogDirectory () {
            String folder_main = "AWSMLogs";
            File f = new File(Environment.getExternalStorageDirectory(), folder_main);
            if (!f.exists()) {
                f.mkdirs();
            }
            String path = f.getAbsolutePath();
            Log.d("Files", "Path: " + path);
            File directory = new File(path);
            File[] files = directory.listFiles();
            if (files != null) {
                Log.d("Files", "Size: " + files.length);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                Date date = null;
                try {
                    date = dateFormat.parse(com.arteriatech.mutils.common.UtilConstants.getCurrentDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DATE, -6);
                String yesterdayAsString = dateFormat.format(calendar.getTime());

                String dtStart = yesterdayAsString;
                Date dateCOMPARE = null;
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    dateCOMPARE = format.parse(dtStart);
                    System.out.println(dateCOMPARE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < files.length; i++) {
                    Log.d("Files", "FileName:" + files[i].getName());
                    String dateFIle = files[i].getName();
                    dateFIle = dateFIle.replace(".txt", "");
                    SimpleDateFormat fileformat = new SimpleDateFormat("dd-MM-yyyy");
                    Date pastdate = null;
                    try {
                        pastdate = format.parse(dateFIle);
                        System.out.println(pastdate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    if (pastdate.compareTo(dateCOMPARE) < 0) {
                        files[i].delete();
                    }
                }
            }

            String dateAsFileName = com.arteriatech.mutils.common.UtilConstants.getCurrentDate();
            File dateFile = new File(Environment.getExternalStoragePublicDirectory("") + "/AWSMLogs",
                    dateAsFileName + ".txt");
            if (!dateFile.exists()) {
                try {
                    dateFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        public static void closeStore (Context context){
            try {
                UtilConstants.closeStore(context,
                        OfflineManager.options, "-100036",
                        offlineStore, Constants.PREFS_NAME, "");

            } catch (Exception e) {
                e.printStackTrace();
            }
//            Constants.Entity_Set.clear();
//            Constants.AL_ERROR_MSG.clear();
            offlineStore = null;
            OfflineManager.options = null;
        }
        public static JSONObject getJSONBody ( final String responseBody) throws IOException {
            try {
                JSONObject jsonObject = new JSONObject(responseBody);
                return jsonObject.getJSONObject("d");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new JSONObject();
        }

        public static boolean isValidPinCode (String pinCode)
        {

            // Regex to check valid pin code of India.
            String regex
                    = "^[1-9]{1}[0-9]{5}$";

            // Compile the ReGex
            Pattern p = Pattern.compile(regex);

            // If the pin code is empty
            // return false
            if (pinCode == null || TextUtils.isEmpty(pinCode)) {
                return false;
            }

            // Pattern class contains matcher() method
            // to find matching between given pin code
            // and regular expression.
            Matcher m = p.matcher(pinCode);

            // Return if the pin code
            // matched the ReGex
            return m.matches();
        }
        public static Object getStreamObject (List < ? > list, String fieldName, String filter){
            Object object = null;
            if (Build.VERSION.SDK_INT >= 24) {
                if (list != null && !list.isEmpty()) {
                    try {
                        object = list.stream().filter(x -> {
                            try {
                                Field field = x.getClass().getDeclaredField(fieldName);
                                field.setAccessible(true);
                                String value = (String) field.get(x);
                                return value.equalsIgnoreCase(filter);
                            } catch (Throwable e) {
                                e.printStackTrace();
                            }
                            return false;
                        }).findFirst().orElse(null);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            } else {
                object = Stream.of(list).filter((com.annimon.stream.function.Predicate<Object>) predicate -> {
                    try {
                        Field field = predicate.getClass().getDeclaredField(fieldName);
                        field.setAccessible(true);
                        String value = (String) field.get(predicate);
                        return value.equalsIgnoreCase(filter);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                    return false;
                }).findFirst().orElse(null);
            }

            return object;

        }

    private static int getPendingListSize(Context mContext) {
        int size = 0;
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);

        Set<String> set = new HashSet<>();
        set = sharedPreferences.getStringSet(Constants.Feedbacks, null);
        if (set != null && !set.isEmpty()) {
            size = size + set.size();
        }
        set = sharedPreferences.getStringSet(Constants.SecondarySOCreate, null);
        if (set != null && !set.isEmpty()) {
            size = size + set.size();
        }
        set = sharedPreferences.getStringSet(Constants.FinancialPostings, null);
        if (set != null && !set.isEmpty()) {
            size = size + set.size();
        }
        set = sharedPreferences.getStringSet(Constants.SampleDisbursement, null);
        if (set != null && !set.isEmpty()) {
            size = size + set.size();
        }
        set = sharedPreferences.getStringSet(Constants.CPList, null);
        if (set != null && !set.isEmpty()) {
            size = size + set.size();
        }
        set = sharedPreferences.getStringSet(Constants.ROList, null);
        if (set != null && !set.isEmpty()) {
            size = size + set.size();
        }
        set = sharedPreferences.getStringSet(Constants.Expenses, null);
        if (set != null && !set.isEmpty()) {
            size = size + set.size();
        }
        return size;
    }

    public static ArrayList<Object> getPendingCollList(Context mContext, boolean isFromAutoSync) {
        ArrayList<Object> objectsArrayList = new ArrayList<>();
        int mIntPendingCollVal = 0;
        String[][] invKeyValues = null;
        Set<String> set = new HashSet<>();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.PREFS_NAME, 0);
        invKeyValues = new String[getPendingListSize(mContext)][2];
        set = sharedPreferences.getStringSet(Constants.Feedbacks, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                invKeyValues[mIntPendingCollVal][0] = itr.next().toString();
                invKeyValues[mIntPendingCollVal][1] = Constants.Feedbacks;
                mIntPendingCollVal++;
            }
        }

        set = sharedPreferences.getStringSet(Constants.SecondarySOCreate, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                invKeyValues[mIntPendingCollVal][0] = itr.next().toString();
                invKeyValues[mIntPendingCollVal][1] = Constants.SecondarySOCreate;
                mIntPendingCollVal++;
            }
        }
        set = sharedPreferences.getStringSet(Constants.FinancialPostings, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                invKeyValues[mIntPendingCollVal][0] = itr.next().toString();
                invKeyValues[mIntPendingCollVal][1] = Constants.FinancialPostings;
                mIntPendingCollVal++;
            }
        }
        set = sharedPreferences.getStringSet(Constants.SampleDisbursement, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                invKeyValues[mIntPendingCollVal][0] = itr.next().toString();
                invKeyValues[mIntPendingCollVal][1] = Constants.SampleDisbursement;
                mIntPendingCollVal++;
            }
        }

        set = sharedPreferences.getStringSet(Constants.CPList, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                invKeyValues[mIntPendingCollVal][0] = itr.next().toString();
                invKeyValues[mIntPendingCollVal][1] = Constants.CPList;
                mIntPendingCollVal++;
            }
        }

        set = sharedPreferences.getStringSet(Constants.ROList, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                invKeyValues[mIntPendingCollVal][0] = itr.next().toString();
                invKeyValues[mIntPendingCollVal][1] = Constants.ROList;
                mIntPendingCollVal++;
            }
        }

        set = sharedPreferences.getStringSet(Constants.Expenses, null);
        if (set != null && !set.isEmpty()) {
            Iterator itr = set.iterator();
            while (itr.hasNext()) {
                invKeyValues[mIntPendingCollVal][0] = itr.next().toString();
                invKeyValues[mIntPendingCollVal][1] = Constants.Expenses;
                mIntPendingCollVal++;
            }
        }

        if (mIntPendingCollVal > 0) {
            Arrays.sort(invKeyValues, new SyncSelectionActivity.ArrayComarator());
            objectsArrayList.add(mIntPendingCollVal);
            objectsArrayList.add(invKeyValues);
        }

        return objectsArrayList;

    }
    // It cannot be void as you have to send url
    // change the qry for log

    public static void updateIDPCredential(Context context){
        try {
            String isHttp="";
            String idpTNT="";
            String idpUrl1="";
            String idpUser="";
            String idpPass="";
//            JSONObject jsonObjectIDP=new JSONObject();
            String configQry = Constants.ConfigTypsetTypeValues+"?$filter=Typeset eq 'SS' and (Types eq 'IDPISHTTPS' or Types eq 'IDPTNT' or Types eq 'IDPURL1'or Types eq 'IDPID'or Types eq 'IDPPWD')";
            LogManager.writeLogInfo("qry : "+configQry);
            ArrayList<ConfigTypsetTypeValuesBean> list = (ArrayList<ConfigTypsetTypeValuesBean>) new OfflineUtils.ODataOfflineBuilder<>()
                    .setHeaderPayloadObject(new ConfigTypsetTypeValuesBean())
                    .setODataEntitySet(Constants.ConfigTypsetTypeValues)
                    .setODataEntityType(Constants.ConfigTypsetTypeValueEntity)
                    .setQuery(configQry)
                    .returnODataList(OfflineManager.offlineStore);
            // Frame URL and send as json { "idpurl":"<>"}
            if (list.size()>0) {
                for (int i = 0; i < list.size(); i++) {
                    ConfigTypsetTypeValuesBean configTypes = list.get(i);
                    if (configTypes.getTypes().equalsIgnoreCase("IDPISHTTPS")) {
                        if (configTypes.getTypeValue().equalsIgnoreCase("X")) {
                            isHttp = "https://";
                        } else {
                            isHttp = "http://";
                        }
                    }
                    if (configTypes.getTypes().equalsIgnoreCase("IDPTNT")) {
                        idpTNT = configTypes.getTypeValue();
                    }
                    if (configTypes.getTypes().equalsIgnoreCase("IDPURL1")) {
                        idpUrl1 = configTypes.getTypeValue();
                    }
                    if (configTypes.getTypes().equalsIgnoreCase("IDPID")) {
                        idpUser = configTypes.getTypeValue();
                    }
                    if (configTypes.getTypes().equalsIgnoreCase("IDPPWD")) {
                        idpPass = configTypes.getTypeValue();
                    }
                    String idpUrl = isHttp + idpTNT + "." + idpUrl1;
                    SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("IDPURL", idpUrl);
                    editor.putString("IDPUserId", idpUser);
                    editor.putString("IDPPass", idpPass);
                    editor.commit();

                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    public static void getIDPSharedPrefValue(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);
        Configuration.IDPURL = sharedPreferences.getString("IDPURL", "");
        Configuration.IDPTUSRNAME = sharedPreferences.getString("IDPUserId", "");
        Configuration.IDPTUSRPWD = sharedPreferences.getString("IDPPass", "");
    }
    public static String getB4UUrl(){
        String b4UUrl="";
        try {
            String isHttp="";
            String hstName="";
            String sfxUrl="";

            String configQry = Constants.ConfigTypsetTypeValues+"?$filter=Typeset eq 'SS' and (Types eq 'B4UISHTTPS' or Types eq 'B4UHSTNME' or Types eq 'B4UURLSFX')";
            LogManager.writeLogInfo("qry : "+configQry);
            ArrayList<ConfigTypsetTypeValuesBean> list = (ArrayList<ConfigTypsetTypeValuesBean>) new OfflineUtils.ODataOfflineBuilder<>()
                    .setHeaderPayloadObject(new ConfigTypsetTypeValuesBean())
                    .setODataEntitySet(Constants.ConfigTypsetTypeValues)
                    .setODataEntityType(Constants.ConfigTypsetTypeValueEntity)
                    .setQuery(configQry)
                    .returnODataList(OfflineManager.offlineStore);
            if (list.size()>0) {
                for (int i = 0; i < list.size(); i++) {
                    ConfigTypsetTypeValuesBean configTypes = list.get(i);
                    if (configTypes.getTypes().equalsIgnoreCase("B4UISHTTPS")) {
                        if (configTypes.getTypeValue().equalsIgnoreCase("X")) {
                            isHttp = "https://";
                        } else {
                            isHttp = "http://";
                        }
                    }
                    if (configTypes.getTypes().equalsIgnoreCase("B4UHSTNME")) {
                        hstName = configTypes.getTypeValue();
                    }
                    if (configTypes.getTypes().equalsIgnoreCase("B4UURLSFX")) {
                        sfxUrl = configTypes.getTypeValue();
                    }
                    b4UUrl = isHttp + hstName + "/" + sfxUrl+"/Login";
                }
            }else{
                b4UUrl="";
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return b4UUrl;
    }
    public static String[] getQuoDeckUrl(){
        String[] quoCredential=new String[]{"","","",""};
        String quodeckUrl="";
        String quodeckUrlWeb="";
        try {
            String isHttp="";
            String hstName="";
            String sfxUrl="";
            String VerUrl="";
            String username="";
            String password="";

            String configQry = Constants.ConfigTypsetTypeValues+"?$filter=Typeset eq 'SS' and (Types eq 'QDKISHTTPS' or Types eq 'QDKHSTNME' or Types eq 'QDKURLSFX' or Types eq 'QDKVER' or Types eq 'QDKUSR' or Types eq 'QDKPWD')";
            LogManager.writeLogInfo("qry : "+configQry);
            ArrayList<ConfigTypsetTypeValuesBean> list = (ArrayList<ConfigTypsetTypeValuesBean>) new OfflineUtils.ODataOfflineBuilder<>()
                    .setHeaderPayloadObject(new ConfigTypsetTypeValuesBean())
                    .setODataEntitySet(Constants.ConfigTypsetTypeValues)
                    .setODataEntityType(Constants.ConfigTypsetTypeValueEntity)
                    .setQuery(configQry)
                    .returnODataList(OfflineManager.offlineStore);
            if (list.size()>0) {
                for (int i = 0; i < list.size(); i++) {
                    ConfigTypsetTypeValuesBean configTypes = list.get(i);
                    if (configTypes.getTypes().equalsIgnoreCase("QDKISHTTPS")) {
                        if (configTypes.getTypeValue().equalsIgnoreCase("X")) {
                            isHttp = "https://";
                        } else {
                            isHttp = "http://";
                        }
                    }
                    if (configTypes.getTypes().equalsIgnoreCase("QDKHSTNME")) {
                        hstName = configTypes.getTypeValue();
                    }
                    if (configTypes.getTypes().equalsIgnoreCase("QDKURLSFX")) {
                        sfxUrl = configTypes.getTypeValue();
                    } if (configTypes.getTypes().equalsIgnoreCase("QDKVER")) {
                        VerUrl = configTypes.getTypeValue();
                    }if (configTypes.getTypes().equalsIgnoreCase("QDKUSR")) {
                        username = configTypes.getTypeValue();
                    }if (configTypes.getTypes().equalsIgnoreCase("QDKPWD")) {
                        password = configTypes.getTypeValue();
                    }
                    quodeckUrl = isHttp + hstName + "/" + sfxUrl+"/"+VerUrl;
                    quodeckUrlWeb = isHttp + hstName ;
                    quoCredential=new String[]{quodeckUrl,username,password,quodeckUrlWeb};
                }
            }else{
                quoCredential=new String[]{"","","",""};
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return quoCredential;
    }
    public static String[] getFaceDetails(){
        String[] faceCredential=new String[]{"",""};
        String faceUrl="";
        try {
            String isHttp="";
            String hstName="";
            String sfxUrl="";
            String VerUrl="";
            String token="";

            String configQry = Constants.ConfigTypsetTypeValues+"?$filter=Typeset eq 'SS' and (Types eq 'NFRISHTTPS' or Types eq 'NFRHSTNM' or Types eq 'NFRURLSFX' or Types eq 'NFRVER' or Types eq 'NFRTKN')";
            LogManager.writeLogInfo("qry : "+configQry);
            ArrayList<ConfigTypsetTypeValuesBean> list = (ArrayList<ConfigTypsetTypeValuesBean>) new OfflineUtils.ODataOfflineBuilder<>()
                    .setHeaderPayloadObject(new ConfigTypsetTypeValuesBean())
                    .setODataEntitySet(Constants.ConfigTypsetTypeValues)
                    .setODataEntityType(Constants.ConfigTypsetTypeValueEntity)
                    .setQuery(configQry)
                    .returnODataList(OfflineManager.offlineStore);
            if (list.size()>0) {
                for (int i = 0; i < list.size(); i++) {
                    ConfigTypsetTypeValuesBean configTypes = list.get(i);
                    if (configTypes.getTypes().equalsIgnoreCase("NFRISHTTPS")) {
                        if (configTypes.getTypeValue().equalsIgnoreCase("X")) {
                            isHttp = "https://";
                        } else {
                            isHttp = "http://";
                        }
                    }
                    if (configTypes.getTypes().equalsIgnoreCase("NFRHSTNM")) {
                        hstName = configTypes.getTypeValue();
                    }
                    if (configTypes.getTypes().equalsIgnoreCase("NFRURLSFX")) {
                        sfxUrl = configTypes.getTypeValue();
                    } if (configTypes.getTypes().equalsIgnoreCase("NFRVER")) {
                        VerUrl = configTypes.getTypeValue();
                    }if (configTypes.getTypes().equalsIgnoreCase("NFRTKN")) {
                        token = configTypes.getTypeValue();
                    }
                    faceUrl = isHttp + hstName + "/" + sfxUrl+"/"+VerUrl+"/"+Configuration.Recognition+"/";

                    faceCredential=new String[]{faceUrl,token};
                }
            }else{
                faceCredential=new String[]{"",""};
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return faceCredential;
    }
    public static String getOSAResponseApi(Context context,String hostName,String key,String clientID){
        try {
            if (UtilConstants.isNetworkAvailable(context)) {
                String finalFetchURL = hostName;
                HttpsURLConnection connection = null;
                int responseCode = 0;
                try {
                    URL url = new URL(finalFetchURL);
                    connection = (HttpsURLConnection) url.openConnection();
                    connection.setRequestProperty("APITOKEN", key);
                    connection.setRequestProperty("CLIENTID", clientID);
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.setUseCaches(false);
                    connection.setDefaultUseCaches(false);
                    connection.setAllowUserInteraction(false);
                    connection.connect();
                    LogManager.writeLogInfo("OSA Qry " + finalFetchURL);
                    responseCode = connection.getResponseCode();
                    connection.getResponseMessage();
                    String resultJson = "";
                    InputStream stream = null;
                    if (responseCode == 200) {
                        stream = connection.getInputStream();
                        if (stream != null) {
                            resultJson = DeviceIDUtils.readResponse(stream);
                        }
                        return resultJson;
                    } else {
                        return "";
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }

            } else {
                return "";
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getWeekOfTheFullDay(String weekOfTheDay){
        String monthStr = "";

        switch (weekOfTheDay){
            case "0" :
                monthStr = "Sunday";
                break;
            case "1" :
                monthStr = "Monday";
                break;

            case "2" :
                monthStr = "Tuesday";
                break;

            case "3" :
                monthStr = "Wednesday";
                break;

            case "4" :
                monthStr = "Thursday";
                break;

            case "5" :
                monthStr = "Friday";
                break;

            case "6" :
                monthStr = "Saturday";
                break;

        }
        if(TextUtils.isEmpty(monthStr)){
            monthStr = ""+weekOfTheDay;
        }
        return monthStr;
    }
    public static String getWeekOfTheFullDay1(String weekOfTheDay){
        String monthStr = "";

        switch (weekOfTheDay){
            case "1" :
                monthStr = "Sun";
                break;
            case "2" :
                monthStr = "Mon";
                break;

            case "3" :
                monthStr = "Tue";
                break;

            case "4" :
                monthStr = "Wed";
                break;

            case "5" :
                monthStr = "Thu";
                break;

            case "6" :
                monthStr = "Fri";
                break;

            case "7" :
                monthStr = "Sat";
                break;

        }
        if(TextUtils.isEmpty(monthStr)){
            monthStr = ""+weekOfTheDay;
        }
        return monthStr;
    }

    public static int gate1BillCut =0;
    public static int gate2BillCut =0;
    public static void getTodayDashboardAch(Context context){

    }
    public static String getTodaysAch(String sssoGuidQry,String brand,ArrayList<String> brandStr,Context context,String type){
        double netPrice = 0.0;
        if(type.equalsIgnoreCase(GATE1)){
            netPrice = netPrice + getDataValutTodayAch(Constants.SecondarySOCreate, context, com.arteriatech.mutils.common.UtilConstants.getNewDate(), brandStr,type);
            return ""+ConstantsUtils.decimalRoundOff(new BigDecimal(netPrice),2);
        }else if(type.equalsIgnoreCase(GATE2)){
            netPrice = netPrice + getDataValutTodayAch(Constants.SecondarySOCreate, context, com.arteriatech.mutils.common.UtilConstants.getNewDate(), brandStr,type);
            return ""+ConstantsUtils.decimalRoundOff(new BigDecimal(netPrice),2);
        }else {

            return "" + ConstantsUtils.decimalRoundOff(new BigDecimal(netPrice), 2);
        }
    }
    public static double getDataValutTodayAch(String createType, Context context, String mStrCurrentDate,ArrayList<String> brandStr,String type) {

         double mDoubleDevOrderValue = 0;
        String[] splitToday = mStrCurrentDate.split("T");
        try {
            Set<String> set = new HashSet<>();
            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREFS_NAME, 0);
            set = sharedPreferences.getStringSet(createType, null);
            if (set != null && !set.isEmpty()) {
                Iterator itr = set.iterator();
                while (itr.hasNext()) {
                    String store = null, deviceNo = "";
                    try {
                        deviceNo = itr.next().toString();
                        store = ConstantsUtils.getFromDataVault(deviceNo,context);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        JSONObject fetchJsonHeaderObject = new JSONObject(store);
                        String[] createdOn = fetchJsonHeaderObject.getString(Constants.CreatedOn).split("T");
                        if (fetchJsonHeaderObject.getString(Constants.EntityType).equalsIgnoreCase(Constants.SecondarySOCreate)
                                && createdOn[0].equalsIgnoreCase(splitToday[0])) {
                            double netPrice = 0;
                            if (fetchJsonHeaderObject.has(Constants.NetPrice)) {
                                netPrice = Double.parseDouble(fetchJsonHeaderObject.getString(Constants.NetPrice));
                            }
                            String OrderOrigin ="";
                            if (fetchJsonHeaderObject.has(Constants.OrderOrigin)) {
                                OrderOrigin = fetchJsonHeaderObject.getString(Constants.OrderOrigin);
                            }
                            if(type.equalsIgnoreCase(GATE1)){
                                if(netPrice>=gate1BillCut){
                                    if(OrderOrigin.equalsIgnoreCase("G")) {
                                        mDoubleDevOrderValue++;
                                    }
                                }
                            }else if(type.equalsIgnoreCase(GATE2)){
                                if(netPrice>=gate2BillCut){
                                    mDoubleDevOrderValue++;
                                }
                            }else {
                                JSONArray itemsArray = new JSONArray(fetchJsonHeaderObject.getString(Constants.ITEM_TXT));
                                try {
                                    if (itemsArray != null && itemsArray.length() > 0) {
                                        for (int incrementVal = 0; incrementVal < itemsArray.length(); incrementVal++) {

                                            JSONObject singleRow = itemsArray.getJSONObject(incrementVal);
                                            String brand = "";
                                            if (singleRow.has(Constants.BrandInd)) {
                                                brand = singleRow.getString(Constants.BrandInd);
                                            }
                                            try {
                                                if (brandStr.contains(brand)) {
                                                    if (singleRow.has(Constants.NetPrice)) {
                                                        if (singleRow.get(Constants.NetPrice) != null && !singleRow.get(Constants.NetPrice).equals("")) {
                                                            mDoubleDevOrderValue = mDoubleDevOrderValue + Double.parseDouble(singleRow.getString(Constants.NetPrice));
                                                        }
                                                    }
                                                }
                                            } catch (NumberFormatException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                } catch (Throwable e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return ConstantsUtils.decimalRoundOff(new BigDecimal(mDoubleDevOrderValue),2).doubleValue();
    }

    // common method to frame the URL

    public static String getCommonURL() {

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        String https = "";
        String hostname = "";
        String suffix1 = "";
        String suffix2 = "";
        String param1 = "";
        String param2 = "";
        String imageURL = "";
        boolean httpsFlag = false;
        try {
            https = OfflineManager.getValueByColumnName(Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Typeset + " eq '" + Constants.MSEC + "' and " + Constants.Types + " eq '" + Constants.ZZIMGHTTPS + "' &$top=1", Constants.TypeValue);
        } catch (OfflineODataStoreException ex) {
            ex.printStackTrace();
        }
        try {
            hostname = OfflineManager.getValueByColumnName(Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Typeset + " eq '" + Constants.MSEC + "' and " + Constants.Types + " eq '" + Constants.ZZIMGHOST + "' &$top=1", Constants.TypeValue);
        } catch (OfflineODataStoreException ex) {
            ex.printStackTrace();
        }
        try {
            suffix1 = OfflineManager.getValueByColumnName(Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Typeset + " eq '" + Constants.MSEC + "' and " + Constants.Types + " eq '" + Constants.ZZIMGSFX1 + "' &$top=1", Constants.TypeValue);
        } catch (OfflineODataStoreException ex) {
            ex.printStackTrace();
        }
        try {
            suffix2 = OfflineManager.getValueByColumnName(Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Typeset + " eq '" + Constants.MSEC + "' and " + Constants.Types + " eq '" + Constants.ZZIMGSFX2 + "' &$top=1", Constants.TypeValue);
        } catch (OfflineODataStoreException ex) {
            ex.printStackTrace();
        }
        try {
            param1 = OfflineManager.getValueByColumnName(Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Typeset + " eq '" + Constants.MSEC + "' and " + Constants.Types + " eq '" + Constants.ZZIMGPARM1 + "' &$top=1", Constants.TypeValue);
        } catch (OfflineODataStoreException ex) {
            ex.printStackTrace();
        }
        try {
            param2 = OfflineManager.getValueByColumnName(Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Typeset + " eq '" + Constants.MSEC + "' and " + Constants.Types + " eq '" + Constants.ZZIMGPARM2 + "' &$top=1", Constants.TypeValue);
        } catch (OfflineODataStoreException ex) {
            ex.printStackTrace();
        }

        if (https.equalsIgnoreCase("X")) {
            https = "https://";
        } else {
            https = "http://";
        }
        if (TextUtils.isEmpty(param2)) {
            param2 = "";
        } else {
            param2 = "&" + param2;
        }
        if (TextUtils.isEmpty(suffix2)) {
            suffix2 = "";
        } else {
            suffix2 = "/" + suffix2;
        }

        imageURL = https + hostname + "/" + suffix1 + suffix2 + "?" + param1  + param2;

//
//            }
//        });

        return imageURL;

    }

    public static String getBreadCommonURL() {

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        String https = "";
        String hostname = "";
        String suffix1 = "";
        String suffix2 = "";
        String param1 = "";
        String param2 = "";
        String imageURL = "";
        boolean httpsFlag = false;
        try {
            https = OfflineManager.getValueByColumnName(Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Typeset + " eq '" + Constants.MSEC + "' and " + Constants.Types + " eq '" + Constants.ZZIMGHTTPS + "' &$top=1", Constants.TypeValue);
        } catch (OfflineODataStoreException ex) {
            ex.printStackTrace();
        }
        try {
            hostname = OfflineManager.getValueByColumnName(Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Typeset + " eq '" + Constants.MSEC + "' and " + Constants.Types + " eq '" + Constants.ZZIMGHOST + "' &$top=1", Constants.TypeValue);
        } catch (OfflineODataStoreException ex) {
            ex.printStackTrace();
        }
        try {
            suffix1 = OfflineManager.getValueByColumnName(Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Typeset + " eq '" + Constants.MSEC + "' and " + Constants.Types + " eq '" + Constants.ZZIMGSFX1 + "' &$top=1", Constants.TypeValue);
        } catch (OfflineODataStoreException ex) {
            ex.printStackTrace();
        }
        try {
            suffix2 = OfflineManager.getValueByColumnName(Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Typeset + " eq '" + Constants.MSEC + "' and " + Constants.Types + " eq '" + Constants.ZZIMGSFX2 + "' &$top=1", Constants.TypeValue);
        } catch (OfflineODataStoreException ex) {
            ex.printStackTrace();
        }
        try {
            param1 = OfflineManager.getValueByColumnName(Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Typeset + " eq '" + Constants.MSEC + "' and " + Constants.Types + " eq '" + Constants.ZZIMGPARM1 + "' &$top=1", Constants.TypeValue);
        } catch (OfflineODataStoreException ex) {
            ex.printStackTrace();
        }
        try {
            param2 = OfflineManager.getValueByColumnName(Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Typeset + " eq '" + Constants.MSEC + "' and " + Constants.Types + " eq '" + Constants.ZZIMGPARM2 + "' &$top=1", Constants.TypeValue);
        } catch (OfflineODataStoreException ex) {
            ex.printStackTrace();
        }

        if (https.equalsIgnoreCase("X")) {
            https = "https://";
        } else {
            https = "http://";
        }
        if (TextUtils.isEmpty(param2)) {
            param2 = "";
        } else {
            param2 = "&" + param2;
        }
        if (TextUtils.isEmpty(suffix2)) {
            suffix2 = "";
        } else {
            suffix2 = "/" + suffix2;
        }

        imageURL = https + hostname + "/" + suffix1 + suffix2 + "?" + param1  + param2;

//
//            }
//        });

        return imageURL;

    }
    public static String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress.nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += inetAddress.getHostAddress();
                    }

                }

            }

        } catch (SocketException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }

        return ip;
    }

    public static String getErrorMessage(IReceiveEvent event, Context context) {
        String errorMsg = "";

        try {
            if (event.getReader() != null) {
                String responseBody = IReceiveEvent.Util.getResponseBody(event.getReader());
                if (event.getResponseStatusCode() != 401) {
                    try {
                        if(!responseBody.contains("html")) {
                            JSONObject jsonObject = null;

                            try {
                                jsonObject = new JSONObject(responseBody);
                                JSONObject errorObject = jsonObject.getJSONObject("error");
                                JSONObject erMesgObject = errorObject.getJSONObject("message");
                                errorMsg = erMesgObject.optString("value");
                            } catch (JSONException var7) {
                                var7.printStackTrace();
                                errorMsg = var7.getMessage();
                            }
                        }else {
                            errorMsg = responseBody;
                        }
                    } catch (Exception var8) {
                        var8.printStackTrace();
                        errorMsg = var8.getMessage();
                    }
                } else {
                    errorMsg = responseBody;
                }
            } else {
                errorMsg = context.getString(R.string.error_bad_req);
            }
        } catch (Throwable var9) {
            errorMsg = var9.getMessage();
            var9.printStackTrace();
        }

        return errorMsg;
    }






    public static boolean isInteger(String s) {
        return isInteger(s, 10);
    }

    public static boolean isInteger(String s, int radix) {
        if (s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1) return false;
                else continue;
            }
            if (Character.digit(s.charAt(i), radix) < 0) return false;
        }
        return true;
    }

    public static String getLastNoOfDays(int days) {
        String dateFormat = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -days);
        return simpleDateFormat.format(cal.getTime()) + "T00:00:00";
    }

    public static  String ConvertJsonDate(String jsonDate,String format){
        jsonDate=jsonDate.replace("/Date(", "").replace(")/", "");
        long time = Long.parseLong(jsonDate);
        Date d= new Date(time);
        return new SimpleDateFormat(format).format(d).toString();
    }
}