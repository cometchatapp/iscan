package com.arteriatech.ss.msec.iscan.v4.retailercreate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.arteriatech.ss.msec.iscan.v4.mutils.common.OfflineODataStoreException;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UIListener;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.store.OnlineODataInterface;
import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.common.ConstantsUtils;
import com.arteriatech.ss.msec.iscan.v4.mbo.DMSDivisionBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.RetailerClassificationBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.RetailerCreateBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.RoutePlanBean;
import com.arteriatech.ss.msec.iscan.v4.mbo.ValueHelpBean;
import com.arteriatech.ss.msec.iscan.v4.store.OfflineManager;
import com.sap.smp.client.odata.ODataDuration;
import com.sap.smp.client.odata.ODataEntity;
import com.sap.smp.client.odata.exception.ODataException;
import com.sap.smp.client.odata.exception.ODataParserException;
import com.sap.smp.client.odata.store.ODataRequestExecution;
import com.sap.client.odata.v4.core.GUID;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

//import id.zelory.compressor.Compressor;

/**
 *@desc Created by e10526 on 21-04-2018.
 */
public class RetailerCreatePresenterImpl implements RetailerCreatePresenter, OnlineODataInterface {
    private Context mContext;
    private Activity mActivity;
    private RetailerCreateView retailerCreateView;
    private boolean isSessionRequired;
    private ArrayList<ValueHelpBean> alCPType = new ArrayList<>();
    private ArrayList<ValueHelpBean> alStateID = new ArrayList<>();
    private ArrayList<RoutePlanBean> alBeat = new ArrayList<>();
    private ArrayList<ValueHelpBean> alCountryID = new ArrayList<>();
    private ArrayList<ValueHelpBean> alWeekOff = new ArrayList<>();
    private ArrayList<ValueHelpBean> alTaxRegStatus = new ArrayList<>();
    private ArrayList<DMSDivisionBean> alDmsDivision = new ArrayList<>();
    private ArrayList<ValueHelpBean> alGrpOne = new ArrayList<>();
    private ArrayList<ValueHelpBean> alGrpTwo = new ArrayList<>();
    private ArrayList<ValueHelpBean> alGrpThree = new ArrayList<>();
    private ArrayList<ValueHelpBean> alGrpFour = new ArrayList<>();
    private ArrayList<ValueHelpBean> alGrpFive = new ArrayList<>();
    private Hashtable<String, String> masterHeaderTable = new Hashtable<>();
    private ArrayList<HashMap<String, String>> itemTable = new ArrayList<>();
    private ODataDuration mStartTimeDuration;
    private String[][] mArrayDistributors, mArraySPValues = null;
    private RetailerCreateBean retailerBean = null;
    private int totalRequest = 0;
    private int currentRequest = 0;
    private String ScreenType ="";
    private String filename="";
    ArrayList<RetailerClassificationBean> cpDMSDivisionList=new ArrayList<>();
    String CPGUID="";
    private RetailerCreateBean retailerDefCreateBean=null;
    String DefaultStateId = "";

    public RetailerCreatePresenterImpl(Context mContext, RetailerCreateView retailerCreateView, boolean isSessionRequired, Activity mActivity, RetailerCreateBean retailerBean) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.retailerCreateView = retailerCreateView;
        this.isSessionRequired = isSessionRequired;
        this.mStartTimeDuration = UtilConstants.getOdataDuration();
        this.retailerBean = retailerBean;
    }

    @Override
    public void onStart() {
        requestCPType();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public boolean validateFields(RetailerCreateBean retailerBean) {
        boolean isNotError = true;
        if (TextUtils.isEmpty(retailerBean.getCPTypeID())) {
            retailerCreateView.errorRetailerType("Select Retailer Type");
            isNotError = false;
        }

        if (TextUtils.isEmpty(retailerBean.getName().trim())) {
            retailerCreateView.errorOutletName("Enter Retailer Name");
            isNotError = false;
        }

        if (TextUtils.isEmpty(retailerBean.getOwnerName().trim())) {
            retailerCreateView.errorOwnerName("Enter Owner Name");
            isNotError = false;
        }

        if (TextUtils.isEmpty(retailerBean.getAddress1().trim())) {
            retailerCreateView.errorAddressOne("Enter Address 1");
            isNotError = false;
        }

        /*if (TextUtils.isEmpty(retailerBean.getLandmark().trim())) {
            retailerCreateView.errorLandMArk("Enter Land Mark");
            isNotError = false;
        }*/

        if (TextUtils.isEmpty(retailerBean.getCountry())) {
            retailerCreateView.errorCountry("Select Country");
            isNotError = false;
        }

        if (TextUtils.isEmpty(retailerBean.getStateID())) {
            retailerCreateView.errorState("Select State");
            isNotError = false;
        }

        if (TextUtils.isEmpty(retailerBean.getRouteSchGUID())) {
            retailerCreateView.errorRouteName("Select Beat Name");
            isNotError = false;
        }

        if (TextUtils.isEmpty(retailerBean.getPostalCode())) {
            retailerCreateView.errorPostlcode("Enter Postal code");
            isNotError = false;
        }
        if (!TextUtils.isEmpty(retailerBean.getPostalCode().trim())) {
            if(retailerBean.getPostalCode().trim().length()<6){
                retailerCreateView.errorPostlcode(mContext.getString(R.string.val_plz_enter_valid_postal_code));
                isNotError = false;
            }
        }
        if (TextUtils.isEmpty(retailerBean.getMobile1())) {
            retailerCreateView.errorMobileOne("Enter Mobile No");
            isNotError = false;
        }

        if (!TextUtils.isEmpty(retailerBean.getMobile1().trim())) {
            if(retailerBean.getMobile1().trim().length()<10){
                retailerCreateView.errorMobileOne(mContext.getString(R.string.val_plz_enter_valid_mobile));
                isNotError = false;
            }
        }
        if (!TextUtils.isEmpty(retailerBean.getMobile2().trim())) {
            if(retailerBean.getMobile2().trim().length()<10){
                retailerCreateView.errorMobileTwo(mContext.getString(R.string.val_plz_enter_valid_mobile));
                isNotError = false;
            }
        }

        if (!TextUtils.isEmpty(retailerBean.getFaxNo().trim())) {
            if(retailerBean.getFaxNo().trim().length()<11){
                retailerCreateView.errorFaxNo(mContext.getString(R.string.val_plz_enter_valid_fax));
                isNotError = false;
            }
        }
        if (!TextUtils.isEmpty(retailerBean.getID1().trim())) {
            if(retailerBean.getID1().trim().length()<12){
                retailerCreateView.errorID(mContext.getString(R.string.val_plz_enter_valid_aadhaar));
                isNotError = false;
            }
        }
        if (!TextUtils.isEmpty(retailerBean.getBusinessID1().trim())) {
            if(!UtilConstants.isValidPanNumber(retailerBean.getBusinessID1().trim())){
                retailerCreateView.errorBussnessID(mContext.getString(R.string.val_plz_enter_valid_pan));
                isNotError = false;
            }
        }
       /* if (!TextUtils.isEmpty(retailerBean.getLandline().trim())) {
            if(retailerBean.getLandline().trim().length()<10){
                retailerCreateView.errorMobileTwo(mContext.getString(R.string.val_plz_enter_valid_mobile));
                isNotError = false;
            }
        }*/

        if (!TextUtils.isEmpty(retailerBean.getEmailID().trim())) {
            if(!UtilConstants.isValidEmailAddress(retailerBean.getEmailID().toString())){
                retailerCreateView.errorEmailId(mContext.getString(R.string.val_plz_enter_valid_email_id));
                isNotError = false;
            }
        }

        if(!TextUtils.isEmpty(retailerBean.getDOB()) && !TextUtils.isEmpty(retailerBean.getAnniversary())){
            if(!Constants.compareTwoDates(retailerBean.getDOB(),retailerBean.getAnniversary())){
                retailerCreateView.errorAnniversary(mContext.getString(R.string.val_plz_enter_valid_anniversry));
                isNotError = false;
            }
        }
 /*
        if (TextUtils.isEmpty(retailerBean.getOutletName()) || retailerBean.getOutletName().trim().length() == 0) {
            retailerCreateView.errorRemarks("Enter Outlet Name");
            isNotError = false;
        }*/

        return isNotError;
    }

    @Override
    public boolean validateDMSDiv(RetailerCreateBean retailerBean) {
        boolean isNotError = true;
        ArrayList<RetailerClassificationBean> alDIVVal = retailerBean.getAlRetClassfication();
        ArrayList<RetailerClassificationBean> alDIVValTemp = retailerBean.getAlRetClassfication();
        if(alDIVVal!=null && alDIVVal.size()>0){
            for(RetailerClassificationBean retailerClassificationBean:alDIVVal){
                if(alDIVValTemp.size()>1){
                    for(int incVal=1;incVal<alDIVValTemp.size();incVal++){
                        RetailerClassificationBean retDMSDivTemp = alDIVValTemp.get(incVal);
                        if(retailerClassificationBean.getDMSDivision().equalsIgnoreCase(retDMSDivTemp.getDMSDivision())){
                            isNotError = false;
                            retailerCreateView.displayMessage("DMS Division is duplicated "+retailerClassificationBean.getDMSDivisionDesc());
                            break;
                        }
                    }
                }
                if(!isNotError){
                    break;
                }
            }

        }else{
            retailerCreateView.displayMessage("Please add atleast one DMS Division");
            isNotError = false;
        }
        return isNotError;
    }

    @Override
    public boolean validateDMSDivFields(RetailerClassificationBean retailerClassificationBean) {
        boolean isNotError = true;
//        Discount Percentage Should not be greater than 2.5
        if (TextUtils.isEmpty(retailerClassificationBean.getDMSDivision())) {
            retailerCreateView.errorDMSDiv("Select DMS Division");
            isNotError = false;
        }

        if (TextUtils.isEmpty(retailerClassificationBean.getGroup1())) {
            retailerCreateView.errorGroupOne("Select Group 1");
            isNotError = false;
        }

        if (TextUtils.isEmpty(retailerClassificationBean.getGroup2())) {
            retailerCreateView.errorGroupTwo("Select Group 2");
            isNotError = false;
        }

        if (TextUtils.isEmpty(retailerClassificationBean.getGroup3())) {
            retailerCreateView.errorGroupThree("Select Group 3");
            isNotError = false;
        }

        if (TextUtils.isEmpty(retailerClassificationBean.getGroup4())) {
            retailerCreateView.errorGroupFour("Select Group 4");
            isNotError = false;
        }

        if (TextUtils.isEmpty(retailerClassificationBean.getGroup5())) {
            retailerCreateView.errorGroupFive("Select Group 5");
            isNotError = false;
        }

        if (!TextUtils.isEmpty(retailerClassificationBean.getDiscountPer())) {
            if(Double.parseDouble(retailerClassificationBean.getDiscountPer())>=2.5){
                retailerCreateView.errorDiscountPercentage("Discount Percentage Should not be greater than 2.5");
                isNotError = false;
            }

        }


        return isNotError;
    }



    @Override
    public void onAsignData(String save, String strRejReason, String strRejReasonDesc, RetailerCreateBean retailerBean) {
//        assignDataVar("", "", "", retailerBean);
    }

    @Override
    public void approveData(String ids, String description, String approvalStatus) {

    }

    @Override
    public void onSaveData(RetailerCreateBean retailerDefCreateBean) {
        this.retailerDefCreateBean=retailerDefCreateBean;
        finalSaveCondition();
    }

    @Override
    public void openCameraIntent() {
        try {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File out = Environment.getExternalStorageDirectory();
            filename = (System.currentTimeMillis() + ".jpg");
            out = new File(out, filename);
            if (Build.VERSION_CODES.N <= Build.VERSION.SDK_INT) {
                Uri photoURI = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", out);
                i.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            } else {
                i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out));
            }
            mActivity.startActivityForResult(i, ConstantsUtils.CAMERA_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ConstantsUtils.CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            File f = new File(Environment.getExternalStorageDirectory().toString());
            for (File temp : f.listFiles()) {
                if (temp.getName().equals(filename)) {
                    f = temp;
                    break;
                }
            }
            try {
//                final Bitmap bitMap = Compressor.getDefault(mContext).compressToBitmap(f);
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                final Bitmap bitMap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitMap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                final byte[] imageInByte = stream.toByteArray();
                int mLongBitmapSize = imageInByte.length;
                File fileName = Constants.SaveImageInDevice(filename, bitMap);
                String selectedImagePath = fileName.getPath();
                //mime
                String strMimeType = MimeTypeMap.getFileExtensionFromUrl(selectedImagePath);
                String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(strMimeType);
                if (retailerCreateView != null) {
                    retailerCreateView.displayImages(bitMap, imageInByte, mLongBitmapSize, mimeType, selectedImagePath, filename);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void getRetailerGroupList(String CPGUID) {
        cpDMSDivisionList.clear();
        new AsyncGetGroupList().execute(CPGUID);

    }

    public class AsyncGetGroupList extends AsyncTask<String, Void, ArrayList<RetailerClassificationBean>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (retailerCreateView != null) {
                retailerCreateView.loadProgressBar();
            }
        }

        @Override
        protected ArrayList<RetailerClassificationBean> doInBackground(String... params) {
            try {

                    String query=Constants.CPDMSDivisions + "?$filter="+ Constants.CPGUID + " eq guid'" + params[0].toUpperCase().toUpperCase() + "'";
                    cpDMSDivisionList.addAll(OfflineManager.getCPDMSDivisionListRetailer(query));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            return cpDMSDivisionList;
        }

        @Override
        protected void onPostExecute(ArrayList<RetailerClassificationBean> result) {
            super.onPostExecute(result);
            retailerCreateView.hideProgressBar();
            if(mActivity instanceof RetailerCreateClassificationActivity)
            {
                (( RetailerCreateClassificationActivity)mActivity).refreshAdaptGroup(result);
            }

        }

    }
    private void finalSaveCondition() {
        Bundle bundle = new Bundle();

        if (retailerCreateView != null) {
            retailerCreateView.showProgressDialog(mContext.getString(R.string.saving_data_wait));
        }
        bundle.putInt(Constants.BUNDLE_REQUEST_CODE, 1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                onSave();
            }
        }).start();

    }

    private void onSave() {

        String doc_no = (System.currentTimeMillis() + "");
        Hashtable table = new Hashtable();
        table.put(Constants.OutletName, retailerBean.getName());
        table.put(Constants.OwnerName, retailerBean.getOwnerName());
        table.put(Constants.CPUID, retailerBean.getCPUID().trim());
        table.put(Constants.PAN, retailerBean.getPAN().trim());
        table.put(Constants.VATNo, retailerBean.getVATNo());
        table.put(Constants.DOB, retailerBean.getDOB());
        table.put(Constants.Anniversary, retailerBean.getAnniversary());
        table.put(Constants.EmailID, retailerBean.getEmailID());
        table.put(Constants.MobileNo, retailerBean.getMobile1());
        table.put(Constants.Mobile2, retailerBean.getMobile2());
        table.put(Constants.Landline, retailerBean.getLandline());
        table.put(Constants.PostalCode, retailerBean.getPostalCode());
        table.put(Constants.Landmark, retailerBean.getLandmark().trim());
        table.put(Constants.StateID, retailerBean.getStateID());
        table.put(Constants.StateDesc, retailerBean.getStateDesc());

        table.put(Constants.CityDesc, retailerBean.getCityDesc().trim());
        table.put(Constants.CityID, "");
        table.put(Constants.DistrictDesc, retailerBean.getDistrictDesc().trim());
        table.put(Constants.DistrictID, "");
        table.put(Constants.Address1, retailerBean.getAddress1().trim());
        table.put(Constants.Address2, retailerBean.getAddress2().trim());
        table.put(Constants.Address3, retailerBean.getAddress3().trim());
        table.put(Constants.Address4, retailerBean.getAddress4().trim());
        table.put(Constants.CPTypeID, retailerBean.getCPTypeID());
        table.put(Constants.CPTypeDesc, retailerBean.getCPTypeDesc());

        table.put(Constants.Latitude, retailerBean.getLatitude());
        table.put(Constants.Longitude, retailerBean.getLongitude());



        String mStrSPGuid = Constants.getSPGUID();
        String mStrCurrency= Constants.getNameByCPGUID(Constants.SalesPersons,Constants.Currency,Constants.SPGUID,mStrSPGuid);
        table.put(Constants.PartnerMgrGUID, mStrSPGuid);

        table.put(Constants.Currency,mStrCurrency);
        table.put(Constants.Group2, "");
        table.put(Constants.Group4, "");
        table.put(Constants.Country, retailerBean.getCountry());
        table.put(Constants.Tax1, retailerBean.getTax1().trim());
        table.put(Constants.Tax2, retailerBean.getTax2().trim());
        table.put(Constants.Tax3, retailerBean.getTax3().trim());
        table.put(Constants.Tax4, retailerBean.getTax4().trim());
        table.put(Constants.TaxRegStatus, retailerBean.getTaxRegStatus());
        table.put(Constants.WeeklyOff, retailerBean.getWeeklyOff());

        table.put(Constants.ID1, retailerBean.getID1().trim());
        table.put(Constants.ID2, retailerBean.getID2().trim());
        table.put(Constants.BusinessID1, retailerBean.getBusinessID1().trim());
        table.put(Constants.BusinessID2, retailerBean.getBusinessID2().trim());

        if(retailerBean.isKeyCP()){
            table.put(Constants.IsKeyCP, Constants.X);
        }else{
            table.put(Constants.IsKeyCP, "");
        }

        table.put(Constants.Landline, retailerBean.getLandline().trim());
        table.put(Constants.FaxNo, retailerBean.getFaxNo().trim());
        table.put(Constants.SalesOfficeID, retailerBean.getSalesOfficeID().trim());
        table.put(Constants.SalesGroupID, retailerBean.getSalesGroupID().trim());

        GUID guid = GUID.newRandom();


        table.put(Constants.CPGUID, guid.toString().toUpperCase());

        ArrayList<RetailerClassificationBean> getDMSDivList =retailerBean.getAlRetClassfication();
        ArrayList<HashMap<String, String>> soItems = new ArrayList<>();
        String mStrParentID = "",mStrParentTypeTypeID="",mStrParentName="";
        if(getDMSDivList!=null && getDMSDivList.size()>0){
            for(RetailerClassificationBean dmsDiv:getDMSDivList){
                HashMap<String, String> singleItem = new HashMap<>();
                String mStrCp1Guid = GUID.newRandom().toString().toUpperCase();
                if(dmsDiv.getCP1GUID()!=null && !dmsDiv.getCP1GUID().equalsIgnoreCase("")){
                    singleItem.put(Constants.CP1GUID, dmsDiv.getCP1GUID());
                }else{
                    singleItem.put(Constants.CP1GUID, mStrCp1Guid);
                }

                singleItem.put(Constants.DMSDivision, dmsDiv.getDMSDivision());
                singleItem.put(Constants.Currency, mStrCurrency);
                singleItem.put(Constants.Group1, dmsDiv.getGroup1().trim());
                singleItem.put(Constants.Group2, dmsDiv.getGroup2().trim());
                singleItem.put(Constants.Group3, dmsDiv.getGroup3().trim());
                singleItem.put(Constants.Group4, dmsDiv.getGroup4().trim());
                singleItem.put(Constants.Group5, dmsDiv.getGroup5().trim());
                try {
                    singleItem.put(Constants.PartnerMgrGUID, Constants.convertStrGUID32to36(dmsDiv.getPartnerMgrGUID()));
                } catch (Exception e) {
                    singleItem.put(Constants.PartnerMgrGUID, "");
                }
                singleItem.put(Constants.PartnerMgrNo, dmsDiv.getPartnerMgrNo());
                singleItem.put(Constants.ParentID, dmsDiv.getParentID());
                singleItem.put(Constants.ParentTypeID, dmsDiv.getParentTypeID());
                singleItem.put(Constants.ParentName, dmsDiv.getParentName());
                singleItem.put(Constants.DiscountPer, dmsDiv.getDiscountPer());
                singleItem.put(Constants.CreditLimit, dmsDiv.getCreditLimit());
                singleItem.put(Constants.CreditDays, dmsDiv.getCreditDays());
                singleItem.put(Constants.CreditBills, dmsDiv.getCreditBills());
                singleItem.put(Constants.StatusID, Constants.str_01);
                singleItem.put(Constants.RouteGUID, retailerBean.getRouteSchGUID());
                singleItem.put(Constants.RouteID, retailerBean.getRouteID());
                singleItem.put(Constants.RouteDesc, retailerBean.getRouteDesc());
                mStrParentID = dmsDiv.getParentID();
                mStrParentTypeTypeID = dmsDiv.getParentTypeID();
                mStrParentName = dmsDiv.getParentName();
                soItems.add(singleItem);
            }
        }
        table.put(Constants.RouteID, retailerBean.getRouteID());
        table.put(Constants.ParentID, mStrParentID);
        table.put(Constants.ParentTypeID, mStrParentTypeTypeID);
        table.put(Constants.ParentName, mStrParentName);
        table.put(Constants.ITEM_TXT, UtilConstants.convertArrListToGsonString(soItems));
        table.put(Constants.CreatedOn, UtilConstants.getNewDateTimeFormat());
        table.put(Constants.CreatedAt, UtilConstants.getOdataDuration().toString());
        table.put(Constants.entityType, Constants.ChannelPartners);
        //image start
        if (!TextUtils.isEmpty(retailerBean.getImagePath())) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(Constants.CPGUID, guid.toString36().toUpperCase());
            hashMap.put(Constants.DocumentLink, retailerBean.getImagePath());
            hashMap.put(Constants.FileName, retailerBean.getFileName());
            hashMap.put(Constants.DocumentMimeType, retailerBean.getImageMimeType());
            hashMap.put(Constants.DocumentSize, retailerBean.getImageSize());
            hashMap.put(Constants.Remarks, "");
            try {
                OfflineManager.createRetailerImage(hashMap, new UIListener() {
                    @Override
                    public void onRequestError(int i, Exception e) {
                        Log.d("createRetailer", "onRequestError: image error");
                    }

                    @Override
                    public void onRequestSuccess(int i, String s) throws ODataException, OfflineODataStoreException {
                        Log.d("createRetailer", "onRequestSuccess: success");
                    }
                }, mContext);
            } catch (OfflineODataStoreException e) {
                e.printStackTrace();
            }
        }
        //image end

        if (retailerDefCreateBean==null) {
            Constants.saveDeviceDocNoToSharedPref(mContext, Constants.CPList, doc_no);
            JSONObject jsonHeaderObject = new JSONObject(table);
            ConstantsUtils.storeInDataVault(doc_no, jsonHeaderObject.toString(), mContext);
            navigateToDetails();
        }else {
            table.put(Constants.CPGUID, retailerDefCreateBean.getCPGUID().toUpperCase());
            table.put(Constants.SetResourcePath,  Constants.ChannelPartners + "(guid'" + retailerDefCreateBean.getCPGUID().toUpperCase() + "')");
            try {
                OfflineManager.updateRetilerBatchReq(table, soItems, new UIListener() {
                    @Override
                    public void onRequestError(int i, Exception e) {
                        Log.d("RetailerUpdate", "onRequestError: ");
                    }

                    @Override
                    public void onRequestSuccess(int i, String s) throws ODataException, OfflineODataStoreException {
                        Log.d("RetailerUpdate", "onRequestSuccess: ");
                        navigateToAddress();
                    }
                }, mContext);
            } catch (ODataParserException e) {
                e.printStackTrace();
            }

         /*   try {
                OfflineManager.updateRetilerBatchReq(table, "" );
            } catch (ODataParserException e) {
                e.printStackTrace();
            }*/

        }

    }

    private void navigateToDetails() {
        if (retailerCreateView != null) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    retailerCreateView.hideProgressDialog();
                    retailerCreateView.showMessage(mContext.getString(R.string.msg_ret_created), false);
                }
            });
        }
    }

    private void navigateToAddress() {
        if (retailerCreateView != null) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    retailerCreateView.hideProgressDialog();
                    retailerCreateView.showMessage(mContext.getString(R.string.msg_ret_updated), false);
                }
            });
        }
    }

    @Override
    public void requestCPType() {
        if (retailerCreateView != null) {
            retailerCreateView.showProgressDialog(mContext.getString(R.string.app_loading));
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                totalRequest = 3;
                currentRequest = 0;
                ScreenType = Constants.str_01;

                List<ODataEntity> entities=null;
                String mStrConfigQry = Constants.ValueHelps + "?$filter=" + Constants.PropName + " eq '" + Constants.Country + "' and "+Constants.EntityType+" eq '"+Constants.ChannelPartner+"' &$orderby=ID asc";
                try {
                    entities = Constants.getListEntities(mStrConfigQry,OfflineManager.offlineStore);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                alCountryID.clear();
                try {
                    alCountryID.addAll(OfflineManager.getConfigListFromValueHelp(entities, Constants.Country));
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }
                mStrConfigQry = Constants.ValueHelps + "?$filter=" + Constants.PropName + " eq '" + Constants.StateID + "' and "+Constants.EntityType+" eq '"+Constants.ChannelPartner+"' &$orderby=ID asc";

                try {
                    entities = Constants.getListEntities(mStrConfigQry,OfflineManager.offlineStore);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String mStrSPGuid=Constants.getSPGUID(Constants.SPGUID);
                DefaultStateId= Constants.getNameByCPGUID(Constants.SalesPersons,Constants.StateID,Constants.SPGUID,mStrSPGuid);
                alStateID.clear();
                try {
                    alStateID.addAll(OfflineManager.getConfigListFromValueHelp(entities, Constants.StateID));
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }
                ValueHelpBean valueHelp = new ValueHelpBean();
                valueHelp.setID("01");
                valueHelp.setDescription("Get");
                valueHelp.setDisplayData("GAT");
                alStateID.add(valueHelp);
                mStrConfigQry = Constants.ValueHelps + "?$filter=" + Constants.PropName + " eq '" + Constants.CPTypeID + "'&$orderby=ID asc";

                try {
                    entities = Constants.getListEntities(mStrConfigQry,OfflineManager.offlineStore);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                alCPType.clear();
                try {
                    alCPType.addAll(OfflineManager.getConfigListFromValueHelp(entities, Constants.CPTypeID));
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }

                mStrConfigQry = Constants.RouteSchedules+"?$filter="+ Constants.ApprovalStatus + " eq '03'";

                try {
                    entities = Constants.getListEntities(mStrConfigQry,OfflineManager.offlineStore);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                alBeat.clear();

                try {
                    alBeat.addAll(OfflineManager.getBeatNamesFromRouteSchedules(entities));
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }

                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (retailerCreateView != null) {
                            retailerCreateView.hideProgressDialog();
                            retailerCreateView.displayCPType(alCPType,alCountryID,alStateID,alBeat,DefaultStateId);
                        }
                    }
                });

               /* String mStrConfigQry = Constants.ValueHelps + "?$filter=" + Constants.PropName + " eq '" + Constants.Country + "' and "+Constants.EntityType+" eq '"+Constants.ChannelPartner+"' &$orderby=ID asc";
                ConstantsUtils.onlineRequest(mContext, mStrConfigQry, isSessionRequired, 1, ConstantsUtils.SESSION_HEADER, RetailerCreatePresenterImpl.this, false);

                mStrConfigQry = Constants.ValueHelps + "?$filter=" + Constants.PropName + " eq '" + Constants.StateID + "' and "+Constants.EntityType+" eq '"+Constants.ChannelPartner+"' &$orderby=ID asc";
                ConstantsUtils.onlineRequest(mContext, mStrConfigQry, isSessionRequired, 2, ConstantsUtils.SESSION_HEADER, RetailerCreatePresenterImpl.this, false);

                mStrConfigQry = Constants.ValueHelps + "?$filter=" + Constants.PropName + " eq '" + Constants.CPTypeID + "'&$orderby=ID asc";
                ConstantsUtils.onlineRequest(mContext, mStrConfigQry, isSessionRequired, 3, ConstantsUtils.SESSION_HEADER, RetailerCreatePresenterImpl.this, false);*/
            }
        }).start();



    }

    @Override
    public void onReqSalesData() {
        if (retailerCreateView != null) {
            retailerCreateView.showProgressDialog(mContext.getString(R.string.app_loading));
        }
        currentRequest = 0;
        totalRequest = 2;
        ScreenType = Constants.str_02;
        String mStrConfigQry = Constants.ValueHelps + "?$filter=" + Constants.PropName + " eq '"+ Constants.WeeklyOff+"' and " + Constants.EntityType + " eq '"+ Constants.ChannelPartner+"' &$orderby=ID asc" ;
        ConstantsUtils.onlineRequest(mContext, mStrConfigQry, isSessionRequired, 4, ConstantsUtils.SESSION_HEADER, this, false);

        mStrConfigQry = Constants.ValueHelps + "?$filter=" + Constants.PropName + " eq '"+ Constants.TaxRegStatus+"' and " + Constants.EntityType + " eq '"+ Constants.ChannelPartner+"' " ;
        ConstantsUtils.onlineRequest(mContext, mStrConfigQry, isSessionRequired, 7, ConstantsUtils.SESSION_HEADER, this, false);
    }

    @Override
    public void onReqDMSDivsion(final String mStrDistId) {
        if (retailerCreateView != null) {
            retailerCreateView.showProgressDialog(mContext.getString(R.string.app_loading));
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                currentRequest = 0;
                totalRequest = 2;
                ScreenType = Constants.str_03;

                String mStrDistQry = Constants.CPSPRelations+ "?$filter=" + Constants.CPGUID + " eq '"+mStrDistId +"'";
                List<ODataEntity> entities=null;
                try {
                    entities = Constants.getListEntities(mStrDistQry,OfflineManager.offlineStore);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                alDmsDivision.clear();
                try {
                    alDmsDivision.addAll(OfflineManager.getDistributorsDmsDivision(entities));
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }

                String mStrConfigQry = Constants.ValueHelps + "?$filter=" + Constants.PropName + " eq '" + Constants.RetailerProfileText + "' and "+Constants.EntityType+" eq '"+Constants.ChannelPartner+"' &$orderby=ID asc";

                try {
                    entities = Constants.getListEntities(mStrConfigQry,OfflineManager.offlineStore);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                alGrpOne.clear();
                try {
                    alGrpOne.addAll(OfflineManager.getConfigListFromValueHelp(entities, Constants.RetailerProfileText));
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }

                mStrConfigQry = Constants.ConfigTypesetTypes + "?$filter=" + Constants.Typeset + " eq '" + Constants.CPGRP4 + "' &$orderby="+Constants.Types+" asc";

                try {
                    entities = Constants.getListEntities(mStrConfigQry,OfflineManager.offlineStore);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                alGrpFour.clear();
                try {
                    alGrpFour.addAll(OfflineManager.getTypesetListFromConfigTypesetTypes(entities));
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }

                mStrConfigQry = Constants.ConfigTypesetTypes + "?$filter=" + Constants.Typeset + " eq '" + Constants.CPGRP5 + "' &$orderby="+Constants.Types+" asc";

                try {
                    entities = Constants.getListEntities(mStrConfigQry,OfflineManager.offlineStore);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                alGrpFive.clear();
                try {
                    alGrpFive.addAll(OfflineManager.getTypesetListFromConfigTypesetTypes(entities));
                } catch (OfflineODataStoreException e) {
                    e.printStackTrace();
                }

                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (retailerCreateView != null) {
                            retailerCreateView.hideProgressDialog();
                            retailerCreateView.displayDMSDivision(alDmsDivision,alGrpOne,alGrpFour,alGrpFive);
                        }
                    }
                });
            }
        }).start();
//        String mStrDistQry = Constants.CPSPRelations;
//        ConstantsUtils.onlineRequest(mContext, mStrDistQry, isSessionRequired, 5, ConstantsUtils.SESSION_HEADER, this, false);
//
//
////        String mStrConfigQry = Constants.ValueHelps + "?$filter=" + Constants.PropName + " eq '" + Constants.RetailerProfile + "' " +
////                "and "+Constants.EntityType+" eq '"+Constants.ChannelPartner+"' &$orderby=ID asc";
//
//        String mStrConfigQry = Constants.ValueHelps + "?$filter=" + Constants.PropName + " eq '" + Constants.RetailerProfileText + "' and "+Constants.EntityType+" eq '"+Constants.ChannelPartner+"' &$orderby=ID asc";
//        ConstantsUtils.onlineRequest(mContext, mStrConfigQry, isSessionRequired, 6, ConstantsUtils.SESSION_HEADER, this, false);


    }

    @Override
    public void onReqGrp2byGrpOne(String mStrTypeValue) {
        if (retailerCreateView != null) {
            retailerCreateView.showProgressDialog(mContext.getString(R.string.app_loading));
        }
        currentRequest = 0;
        totalRequest = 1;
        ScreenType = Constants.str_04;
        String mStrConfigQry = Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Typeset + " eq '"+ Constants.CPGRP2+"' and " + Constants.TypeValue + " eq '"+ mStrTypeValue+"' &$orderby="+Constants.Types+" asc" ;
        ConstantsUtils.onlineRequest(mContext, mStrConfigQry, isSessionRequired, 8, ConstantsUtils.SESSION_HEADER, this, false);
    }

    @Override
    public void onReqGrp3byGrpTwo(String mStrTypeValue) {
        if (retailerCreateView != null) {
            retailerCreateView.showProgressDialog(mContext.getString(R.string.app_loading));
        }
        currentRequest = 0;
        totalRequest = 1;
        ScreenType = Constants.str_05;
        String mStrConfigQry = Constants.ConfigTypsetTypeValues + "?$filter=" + Constants.Typeset + " eq '"+ Constants.CPGRP3+"' and " + Constants.TypeValue + " eq '"+ mStrTypeValue+"' &$orderby="+Constants.Types+" asc" ;
        ConstantsUtils.onlineRequest(mContext, mStrConfigQry, isSessionRequired, 9, ConstantsUtils.SESSION_HEADER, this, false);
    }


    @Override
    public void responseSuccess(ODataRequestExecution oDataRequestExecution,final List<ODataEntity> list, Bundle bundle) {
        final int type = bundle != null ? bundle.getInt(Constants.BUNDLE_REQUEST_CODE) : 0;
                switch (type) {
                    case 1:

                        alCountryID.clear();
                        try {
                            alCountryID.addAll(OfflineManager.getConfigListFromValueHelp(list, Constants.Country));
                        } catch (OfflineODataStoreException e) {
                            e.printStackTrace();
                        }
                        currentRequest++;
                        break;
                    case 2:

                        alStateID.clear();
                        try {
                            alStateID.addAll(OfflineManager.getConfigListFromValueHelp(list, Constants.StateID));
                        } catch (OfflineODataStoreException e) {
                            e.printStackTrace();
                        }
                        currentRequest++;
                        break;
                    case 3:

                        alCPType.clear();
                        try {
                            alCPType.addAll(OfflineManager.getConfigListFromValueHelp(list, Constants.CPTypeID));
                        } catch (OfflineODataStoreException e) {
                            e.printStackTrace();
                        }
                        currentRequest++;
                        break;
                    case 4:

                        alWeekOff.clear();
                        try {
                            alWeekOff.addAll(OfflineManager.getConfigListFromValueHelp(list, Constants.WeeklyOff));
                        } catch (OfflineODataStoreException e) {
                            e.printStackTrace();
                        }
                        currentRequest++;
                        break;
                    case 7:

                        alTaxRegStatus.clear();
                        try {
                            alTaxRegStatus.addAll(OfflineManager.getConfigListFromValueHelp(list, Constants.TaxRegStatus));
                        } catch (OfflineODataStoreException e) {
                            e.printStackTrace();
                        }
                        currentRequest++;
                        break;
                    case 5:

                        alDmsDivision.clear();
                        try {
                            alDmsDivision.addAll(OfflineManager.getDistributorsDmsDivision(list));
                        } catch (OfflineODataStoreException e) {
                            e.printStackTrace();
                        }
                        currentRequest++;
                        break;
                    case 6:

                        alGrpOne.clear();
                        try {
                            alGrpOne.addAll(OfflineManager.getConfigListFromValueHelp(list, Constants.RetailerProfileText));
                        } catch (OfflineODataStoreException e) {
                            e.printStackTrace();
                        }
                        currentRequest++;
                        break;
                    case 8:

                        alGrpTwo.clear();
                        try {
                            alGrpTwo.addAll(OfflineManager.getTypesetListFromConfigTypsetTypeValues(list));
                        } catch (OfflineODataStoreException e) {
                            e.printStackTrace();
                        }
                        currentRequest++;
                        break;
                    case 9:

                        alGrpThree.clear();
                        try {
                            alGrpThree.addAll(OfflineManager.getTypesetListFromConfigTypsetTypeValues(list));
                        } catch (OfflineODataStoreException e) {
                            e.printStackTrace();
                        }
                        currentRequest++;
                        break;
                }

        if (totalRequest == currentRequest) {
            if(ScreenType.equalsIgnoreCase(Constants.str_01)){
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (retailerCreateView != null) {
                            retailerCreateView.hideProgressDialog();
                            retailerCreateView.displayCPType(alCPType,alCountryID,alStateID,alBeat,DefaultStateId);
                        }
                    }
                });
            }else if(ScreenType.equalsIgnoreCase(Constants.str_02)){
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (retailerCreateView != null) {
                            retailerCreateView.hideProgressDialog();
                            retailerCreateView.displayWeeklyOff(alWeekOff,alTaxRegStatus);
                        }
                    }
                });
            }else if(ScreenType.equalsIgnoreCase(Constants.str_03)){
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (retailerCreateView != null) {
                            retailerCreateView.hideProgressDialog();
                            retailerCreateView.displayDMSDivision(alDmsDivision,alGrpOne,alGrpFour,alGrpFive);
                        }
                    }
                });
            }else if(ScreenType.equalsIgnoreCase(Constants.str_04)){
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (retailerCreateView != null) {
                            retailerCreateView.hideProgressDialog();
                            retailerCreateView.displayGrpTwoValue(alGrpTwo);
                        }
                    }
                });
            }else{
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (retailerCreateView != null) {
                            retailerCreateView.hideProgressDialog();
                            retailerCreateView.displayGrpThreeValue(alGrpThree);
                        }
                    }
                });
            }
        }
    }

    @Override
    public void responseFailed(ODataRequestExecution oDataRequestExecution,final String s, Bundle bundle) {
        currentRequest++;
        if (totalRequest == currentRequest) {
            if(ScreenType.equalsIgnoreCase(Constants.str_01)){
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (retailerCreateView != null) {
                            retailerCreateView.hideProgressDialog();
                            retailerCreateView.displayMessage(s);
                        }
                    }
                });
            }else if(ScreenType.equalsIgnoreCase(Constants.str_02)){
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (retailerCreateView != null) {
                            retailerCreateView.hideProgressDialog();
                            retailerCreateView.displayMessage(s);
                        }
                    }
                });
            }else if(ScreenType.equalsIgnoreCase(Constants.str_03)){
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (retailerCreateView != null) {
                            retailerCreateView.hideProgressDialog();
                            retailerCreateView.displayMessage(s);
                        }
                    }
                });
            }else if(ScreenType.equalsIgnoreCase(Constants.str_04)){
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (retailerCreateView != null) {
                            retailerCreateView.hideProgressDialog();
                            retailerCreateView.displayMessage(s);
                        }
                    }
                });
            }else{
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (retailerCreateView != null) {
                            retailerCreateView.hideProgressDialog();
                            retailerCreateView.displayMessage(s);
                        }
                    }
                });
            }
        }
    }
}
