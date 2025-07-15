package com.arteriatech.ss.msec.iscan.v4.mutils.registration;

/**
 * Created by e10769 on 21-09-2017.
 */


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arteriatech.ss.msec.iscan.v4.R;
import com.arteriatech.ss.msec.iscan.v4.mutils.actionbar.ActionBarView;
import com.arteriatech.ss.msec.iscan.v4.mutils.adapter.AdapterInterface;
import com.arteriatech.ss.msec.iscan.v4.mutils.adapter.SimpleRecyclerViewAdapter;
import com.arteriatech.ss.msec.iscan.v4.mutils.common.UtilConstants;
import com.arteriatech.ss.msec.iscan.v4.mutils.interfaces.DialogCallBack;
import com.arteriatech.mutils.log.LogManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class SupportActivity extends AppCompatActivity implements AdapterInterface<MainMenuBean> {

    private RegistrationModel registrationModel = null;
    private TextView tvAppVersion;
    private TextView tvURL;
    private TextView tvPort;
    private TextView tvEmailId;
    private TextView tvPhoneNo;
    private RecyclerView recyclerView;
    private SimpleRecyclerViewAdapter<MainMenuBean> simpleRecyclerViewAdapter;
    private TextView tvSuffix;
    private View llSuffix;
    private Bundle bundleExtra = null;
    private Toolbar toolbar;
    private View llEmailId;
    private View llPhoneNo;
    private String mStrFilePath = "";
    public static final int OFFLINE_DB_REINITIALIZE=3450;
    public static final int OFFLINE_DB_IMPORT=3451;
    private ProgressDialog pdLoadDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setContentView(R.layout.activity_support);
        Bundle bundleExtras = getIntent().getExtras();
        if (bundleExtras != null) {
            registrationModel = (RegistrationModel) bundleExtras.getSerializable(UtilConstants.RegIntentKey);
            bundleExtra = bundleExtras.getBundle(UtilRegistrationActivity.EXTRA_BUNDLE_REGISTRATION);
        }
        if (registrationModel != null) {
          /*  int icon = 0;
            int backIcon = 0;
            if (registrationModel.getAppActionBarIcon() != 0) {
                icon = registrationModel.getAppActionBarIcon();//AppCompatResources.getDrawable(SupportActivity.this, registrationModel.getAppActionBarIcon());
            }
            if (registrationModel.getAppActionBarIcon() != 0) {
                backIcon = registrationModel.getBackButtonIcon();
            }*/
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            ActionBarView.initActionBarView(this, toolbar, true, getString(R.string.support_menu), 0, R.drawable.ic_close);
            initUI();
        }

    }

    private void initUI() {
        tvAppVersion = (TextView) findViewById(R.id.tvAppVersion);
        tvURL = (TextView) findViewById(R.id.tvURL);
        tvPort = (TextView) findViewById(R.id.tvPort);
        tvSuffix = (TextView) findViewById(R.id.tvSuffix);
        llSuffix =  findViewById(R.id.llSuffix);
        tvEmailId = (TextView) findViewById(R.id.tvEmailId);
        llEmailId = findViewById(R.id.llEmailId);
        llPhoneNo = findViewById(R.id.llPhoneNo);
        tvPhoneNo = (TextView) findViewById(R.id.tvPhoneNo);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SupportActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        simpleRecyclerViewAdapter = new SimpleRecyclerViewAdapter<MainMenuBean>(SupportActivity.this, R.layout.support_menu_item, this, null, null);
        recyclerView.setAdapter(simpleRecyclerViewAdapter);
        simpleRecyclerViewAdapter.refreshAdapter(registrationModel.getMenuBeen());


        tvAppVersion.setText(registrationModel.getAppVersionName());
        tvURL.setText(registrationModel.getServerText());
        tvPort.setText(registrationModel.getPort());
        tvEmailId.setText(Html.fromHtml("<u>" + registrationModel.getEmainId() + "</u>"));
        tvPhoneNo.setText(Html.fromHtml("<u>" + registrationModel.getPhoneNo() + "</u>"));
        if (registrationModel.isRelay()) {
            llSuffix.setVisibility(View.VISIBLE);
            tvSuffix.setText(registrationModel.getSuffix());
        } else {
            llSuffix.setVisibility(View.GONE);
        }

        llEmailId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(registrationModel.getEmainId())) {
                    try {
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setData(Uri.parse("mailto:"));
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{registrationModel.getEmainId()});
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, registrationModel.getEmailSubject());
//                        emailIntent.putExtra(Intent.EXTRA_TEXT, "message body");
                        startActivity(Intent.createChooser(emailIntent, "Email via..."));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        llPhoneNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(registrationModel.getPhoneNo())) {
                    try {
                        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + registrationModel.getPhoneNo()));
                        startActivity(dialIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    public void onItemClick(MainMenuBean item, View view, int position) {
        Intent intent = new Intent(SupportActivity.this, item.getActivityRedirect());
        if (bundleExtra != null)
            intent.putExtra(UtilRegistrationActivity.EXTRA_BUNDLE_REGISTRATION, bundleExtra);
        startActivity(intent);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, View viewItem) {
        return new SupportVH(viewItem);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, MainMenuBean item) {
        ((SupportVH) holder).tvMenuText.setText(item.getMenuName());
//        ((SupportVH) holder).ivMenuImage.setImageResource(item.getMenuImage());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_supports, menu);
        MenuItem menuReInitDB = menu.findItem(R.id.menu_re_init);
        MenuItem menuExport = menu.findItem(R.id.menu_mainmenu_export);
        MenuItem menuImport = menu.findItem(R.id.menu_mainmenu_import);
        MenuItem menuExportData = menu.findItem(R.id.menu_exportdatavault);
        MenuItem menuImportData = menu.findItem(R.id.menu_importdatavault);
        if (registrationModel!=null && registrationModel.isDisplayDBReInitMenu()){
            menuReInitDB.setVisible(true);
        }else {
            menuReInitDB.setVisible(false);
        }
        if (registrationModel!=null && registrationModel.isDisplayExportMenu()){
            menuExport.setVisible(true);
        }else {
            menuExport.setVisible(false);
        }
        if (registrationModel!=null && registrationModel.isDisplayImportMenu()){
            menuImport.setVisible(true);
        }else {
            menuImport.setVisible(false);
        }
        if (registrationModel!=null && registrationModel.isDisplayExportDataMenu()){
            menuExportData.setVisible(true);
        }else {
            menuExportData.setVisible(false);
        }
        if (registrationModel!=null && registrationModel.isDisplayImportDataMenu()){
            menuImportData.setVisible(true);
        }else {
            menuImportData.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (i == R.id.menu_export_device_log) {
            if (UtilConstants.isReadWritePermissionEnabled(SupportActivity.this, SupportActivity.this)) {
                if (exportLog()) {
                    UtilConstants.dialogBoxWithCallBack(SupportActivity.this, "", getString(R.string.crash_log_to_sdcard_finish), getString(R.string.ok), "", false, new DialogCallBack() {
                        @Override
                        public void clickedStatus(boolean clickedStatus) {
                            shareFile();
                        }
                    });
                }
            }
            return true;
        } else if (i == R.id.menu_re_init) {
            showConformationDialog();
            return true;
        } else if (i == R.id.menu_mainmenu_export) {
            showConformationDialogExportDB();
            return true;
        }else if (i == R.id.menu_mainmenu_import) {
            showConformationDialogImportDB();
            return true;
        }  else if (i == R.id.menu_exportdatavault) {
            alertPOPUPExportDataVaultData();
            return true;
        } else if (i == R.id.menu_importdatavault) {
            alertPOPUPImportDataVaultData();
            return true;
        }  else {
            return super.onOptionsItemSelected(item);
        }

    }

    private void showConformationDialog() {
        LogManager.writeLogInfo(getString(R.string.db_reinit_request));
        UtilConstants.dialogBoxWithCallBack(SupportActivity.this, "", getString(R.string.pending_req_aval_do_want_reopen_store), getString(R.string.yes), getString(R.string.no), false, new DialogCallBack() {
            @Override
            public void clickedStatus(boolean b) {
                if (b) {
                    LogManager.writeLogInfo(getString(R.string.db_reinit_confirmed));
                    openOfflineStore();
                }
            }
        });
    }
    private void openOfflineStore() {
        if (UtilConstants.isNetworkAvailable(this)) {
            try {
                Intent intent = new Intent();
                setResult(OFFLINE_DB_REINITIALIZE, intent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            UtilConstants.onNoNetwork(this);
        }
    }
    private void showConformationDialogExportDB() {
        UtilConstants.dialogBoxWithCallBack(SupportActivity.this, "", getString(R.string.alert_do_u_want_export_db), getString(R.string.yes), getString(R.string.no), false, new DialogCallBack() {
            @Override
            public void clickedStatus(boolean b) {
                if (b) {
                    if (UtilConstants.checkStoragePermission(SupportActivity.this)) {
                        exportOfflineDB(SupportActivity.this,registrationModel);
                    }

                }
            }
        });
    }
    private void showConformationDialogImportDB() {
        UtilConstants.dialogBoxWithCallBack(SupportActivity.this, "", getString(R.string.alert_do_u_want_import_db), getString(R.string.yes), getString(R.string.no), false, new DialogCallBack() {
            @Override
            public void clickedStatus(boolean b) {
                if (b) {
                    if (UtilConstants.checkStoragePermission(SupportActivity.this)) {
                        ImportDB();
                    }
                }
            }
        });
    }
    private void ImportDB() {
            try {
                Intent intent = new Intent();
                setResult(OFFLINE_DB_IMPORT, intent);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    private void shareFile() {
        try {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            Uri screenshotUri = Uri.parse("file://" + mStrFilePath);
            sharingIntent.setType("*/*");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
            startActivity(Intent.createChooser(sharingIntent, "Share file using"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean exportLog() {
        boolean flagforlog;
        mStrFilePath = "";
        try {
            File filename = new File(Environment.getExternalStorageDirectory()
                    + "/" + getString(R.string.app_name) + "_device.log");
            filename.createNewFile();
            mStrFilePath = filename.getAbsolutePath();
            String cmd = "logcat -v long -f " + filename.getAbsolutePath();
            Runtime.getRuntime().exec(cmd);
            flagforlog = true;
        } catch (IOException e) {
            flagforlog = false;
            e.printStackTrace();
        }
        return flagforlog;
    }
    private void alertPOPUPImportDataVaultData(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SupportActivity.this, R.style.MyTheme);
        builder.setMessage(R.string.alert_import_data_to_device).setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if (UtilConstants.checkStoragePermission(SupportActivity.this)) {
                            importDatavaultData(SupportActivity.this,registrationModel);
                        }
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }

                });
        builder.show();
    }
    private void alertPOPUPExportDataVaultData(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SupportActivity.this, R.style.MyTheme);
        builder.setMessage(R.string.alert_export_data_to_device).setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if (UtilConstants.checkStoragePermission(SupportActivity.this)) {
                            exportDatavaultData(SupportActivity.this);
                        }
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }

                });
        builder.show();
    }
    private void exportDatavaultData(final Context mContext) {
        if (TextUtils.isEmpty(registrationModel.getDataVaultPassword())){
            UtilConstants.displayShortToast(mContext, "Need password for export data");
            return;
        }
        pdLoadDialog = new ProgressDialog(mContext, R.style.UtilsDialogTheme);
        pdLoadDialog.setMessage(getString(R.string.export_datavault_data_to_storage));
        pdLoadDialog.setCancelable(false);
        pdLoadDialog.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    final int isExportData = exportDataVault(mContext,registrationModel);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pdLoadDialog.dismiss();
                            String msg = mContext.getString(R.string.export_datavault_to_sdcard_error_occurred);
                            if (isExportData == 3) {//success
                                msg = mContext.getString(R.string.export_datavault_to_sdcard_finish);
                                UtilConstants.removeDataValtFromSharedPref(mContext, "", "", true,
                                        registrationModel.getAlEntityNames(),registrationModel.getShredPrefKey());
                            } else if (isExportData == 2) {
                                msg = "No Pending Requests Available";
                            }
                            UtilConstants.displayShortToast(mContext, msg);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    boolean isFileExists =false;
    private void importDatavaultData(final Context mContext,final RegistrationModel regModel) {
        if (TextUtils.isEmpty(registrationModel.getDataVaultPassword())){
            UtilConstants.displayShortToast(mContext, "Need password for import data");
            return;
        }
        isFileExists =false;
        pdLoadDialog = new ProgressDialog(mContext, R.style.UtilsDialogTheme);
        pdLoadDialog.setMessage(getString(R.string.import_datavault_data_from_sdcard));
        pdLoadDialog.setCancelable(false);
        pdLoadDialog.show();
        new Thread(new Runnable() {
            public void run() {
                String message = getString(R.string.import_datavault_from_sdcard_finish);
                String fileName="";
                if (TextUtils.isEmpty(regModel.getDataVaultFileName())){
                    fileName=mContext.getPackageName();
                }else {
                    fileName = regModel.getDataVaultFileName();
                }
                try {
                    isFileExists = UtilConstants.isFileExits(fileName);
                    if (isFileExists) {
                        String datavaultData = UtilConstants.getTextFileData(fileName);
                        UtilConstants.setJsonStringDataToDataVault(datavaultData, mContext,regModel.getShredPrefKey(),regModel.getDataVaultPassword());
                    } else {
                        message = getString(R.string.file_not_exist);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogManager.writeLogError("importDatavaultData() (InterruptedException): " + e.getMessage());
                    message = getString(R.string.import_datavault_from_sdcard_error_occurred);
                }
                final String finalMessage = message;
                String finalFileName = fileName;
                runOnUiThread(new Runnable() {
                    public void run() {
                        pdLoadDialog.cancel();
                        if(isFileExists){
                            UtilConstants.deleteFileFromDeviceStorage(finalFileName);
                        }
                        UtilConstants.displayShortToast(mContext, finalMessage);
                    }
                });
            }
        }).start();

    }
    public static int exportDataVault(Context mContext, RegistrationModel regModel) {
        boolean flagforlog = false;
        try {
            FileWriter fileWriter = null;
            String jsonData = null;
            try {
                jsonData = UtilConstants.makePendingDataToJsonString(mContext,regModel);
            } catch (Exception e) {
                e.printStackTrace();
                LogManager.writeLogError("exportDataVault() : " + e.getMessage());
                jsonData = "";
            }
            if (jsonData != null && !jsonData.equalsIgnoreCase("")) {
                String fileName="";
                if (TextUtils.isEmpty(regModel.getDataVaultFileName())){
                    fileName=mContext.getPackageName();
                }else {
                    fileName = regModel.getDataVaultFileName();
                }
                fileWriter = new FileWriter(Environment.getExternalStorageDirectory()
                        + "/" + fileName + "");
                fileWriter.write(jsonData);
                fileWriter.close();
//                flagforlog = true;
                return 3;
            } else {
//                Constants.ExportDataFailedErrorMsg = "No Pending Requests Available";
//                flagforlog = false;
                return 2;
            }


        } catch (IOException e) {
            e.printStackTrace();
            LogManager.writeLogError("exportDataVault() (IOException) : " + e.getMessage());
            return 1;
        }
    }

    private void exportOfflineDB(final Context mContext,final RegistrationModel regModel) {
        pdLoadDialog = new ProgressDialog(mContext, R.style.UtilsDialogTheme);
        pdLoadDialog.setMessage(getString(R.string.export_databse_to_sdcard));
        pdLoadDialog.setCancelable(false);
        pdLoadDialog.show();
        new Thread(new Runnable() {
            public void run() {
                try {
                    final boolean isExportDB = exportDB(mContext,regModel);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pdLoadDialog.cancel();
                            String msg = mContext.getString(R.string.export_databse_to_sdcard_error_occurred);
                            if (isExportDB) {//success
                                msg = mContext.getString(R.string.export_databse_to_sdcard_finish);
                            }
                            UtilConstants.displayShortToast(mContext, msg);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static boolean exportDB(Context context,RegistrationModel regModel) {

        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String PACKAGE_NAME;
        PACKAGE_NAME = context.getPackageName();
        String currentDBPath = regModel.getOfflineDBPath();
        String currentrqDBPath = regModel.getOfflineReqDBPath();

        String backupDBPath = regModel.getIbackupUDBPath();
        String backuprqDBPath = regModel.getIbackupRqDBPath();

        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        File currentrqDB = new File(data, currentrqDBPath);
        File backuprqDB = new File(sd, backuprqDBPath);
        try {
            // Exporting Offline DB
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            // Exporting Offline rq DB
            source = new FileInputStream(currentrqDB).getChannel();
            destination = new FileOutputStream(backuprqDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();

            return true;
        } catch (IOException e) {
            LogManager.writeLogError(e.getMessage());
            return false;
        }
    }
}
