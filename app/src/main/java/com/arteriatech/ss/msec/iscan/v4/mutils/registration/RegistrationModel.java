package com.arteriatech.ss.msec.iscan.v4.mutils.registration;

import android.app.Application;
import android.content.Context;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by e10526 on 9/13/2017.
 */

public class RegistrationModel<T> implements Serializable {
    public String IDPURL = "";
    public String ExternalUserName = "";
    public String ExternalTUserName = "";

    public boolean isExtenndPwdReq() {
        return isExtenndPwdReq;
    }

    public void setExtenndPwdReq(boolean extenndPwdReq) {
        isExtenndPwdReq = extenndPwdReq;
    }

    public boolean isExtenndPwdReq = false;

    public boolean isUpdateAsPortalPwdReq() {
        return isUpdateAsPortalPwdReq;
    }

    public void setUpdateAsPortalPwdReq(boolean updateAsPortalPwdReq) {
        isUpdateAsPortalPwdReq = updateAsPortalPwdReq;
    }

    public boolean isUpdateAsPortalPwdReq = false;

    public String getIDPURL() {
        return IDPURL;
    }

    public void setIDPURL(String IDPURL) {
        this.IDPURL = IDPURL;
    }

    public String getExternalUserName() {
        return ExternalUserName;
    }

    public void setExternalUserName(String externalUserName) {
        ExternalUserName = externalUserName;
    }

    public String getExternalTUserName() {
        return ExternalTUserName;
    }

    public void setExternalTUserName(String externalTUserName) {
        ExternalTUserName = externalTUserName;
    }

    public String getExternalTPWD() {
        return ExternalTPWD;
    }

    public void setExternalTPWD(String externalTPWD) {
        ExternalTPWD = externalTPWD;
    }

    public String ExternalTPWD = "";
    public String UserName = "";
    public String Password = "";
    public String ServerText = "";
    public String Port = "";
    public String FormID = "";
    public String Suffix = "";
    public String AppID = "";
    public String SecConfig = "";
    public Boolean isHttps = false;
    public String ShredPrefKey = "";
    private int appActionBarIcon = 0;
    private int appLogo = 0;
    private String appVersionName = "";
    private boolean isRelay = false;
    private String emainId = "";
    private String phoneNo = "";
    private String DataVaultFileName = "";
    private boolean displayDBReInitMenu = false;
    private boolean displayExportDataMenu = false;
    private boolean displayImportDataMenu = false;
    private boolean displayExportMenu = false;
    private boolean displayImportMenu = false;
    private ArrayList<MainMenuBean> menuBeen = new ArrayList<>();
    private String emailSubject = "";
    private int backButtonIcon = 0;
    private int viewBackgroundImage = 0;
    private int viewDrawableBorder = 0;
    private boolean displayForgetPsw=false;
    private boolean isLgnUsrNameNonEdit=false;
    private String DataVaultPassword="";
    private ArrayList<String> noPasscodeActivity = new ArrayList<>();

    public ArrayList<String> getAlEntityNames() {
        return alEntityNames;
    }

    public void setAlEntityNames(ArrayList<String> alEntityNames) {
        this.alEntityNames = alEntityNames;
    }

    private ArrayList<String> alEntityNames = new ArrayList<>();
    private Application mApplication = null;
    private Context context = null;
    private boolean isBackToStartActivity = false;
    private String appName = "";

    private String offlineDBPath = "";
    private String offlineReqDBPath = "";
    private String icurrentUDBPath = "";
    private String ibackupUDBPath = "";
    private String icurrentRqDBPath = "";
    private String ibackupRqDBPath = "";
    public String getOfflineDBPath() {
        return offlineDBPath;
    }

    public void setOfflineDBPath(String offlineDBPath) {
        this.offlineDBPath = offlineDBPath;
    }

    public String getOfflineReqDBPath() {
        return offlineReqDBPath;
    }

    public void setOfflineReqDBPath(String offlineReqDBPath) {
        this.offlineReqDBPath = offlineReqDBPath;
    }

    public String getIcurrentUDBPath() {
        return icurrentUDBPath;
    }

    public void setIcurrentUDBPath(String icurrentUDBPath) {
        this.icurrentUDBPath = icurrentUDBPath;
    }

    public String getIbackupUDBPath() {
        return ibackupUDBPath;
    }

    public void setIbackupUDBPath(String ibackupUDBPath) {
        this.ibackupUDBPath = ibackupUDBPath;
    }

    public String getIcurrentRqDBPath() {
        return icurrentRqDBPath;
    }

    public void setIcurrentRqDBPath(String icurrentRqDBPath) {
        this.icurrentRqDBPath = icurrentRqDBPath;
    }

    public String getIbackupRqDBPath() {
        return ibackupRqDBPath;
    }

    public void setIbackupRqDBPath(String ibackupRqDBPath) {
        this.ibackupRqDBPath = ibackupRqDBPath;
    }



    private Class<? extends AppCompatActivity> logInActivity = null;
    private Class<? extends AppCompatActivity> passcodeLoginActivity = null;
    private Class<? extends AppCompatActivity> registerSuccessActivity = null;
    private Class<? extends AppCompatActivity> loginSuccessActivity = null;
    private Class<? extends AppCompatActivity> mainActivity = null;
    private Class<? extends AppCompatActivity> registerActivity = null;

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    private int layout=0;

    public int getAppBandIcon() {
        return appBandIcon;
    }

    public void setAppBandIcon(int appBandIcon) {
        this.appBandIcon = appBandIcon;
    }

    private int appBandIcon=0;

    public int getAppBandLogo() {
        return appBandLogo;
    }

    public void setAppBandLogo(int appBandLogo) {
        this.appBandLogo = appBandLogo;
    }

    private int appBandLogo=0;

    public Application getmApplication() {
        return mApplication;
    }

    public void setmApplication(Application mApplication) {
        this.mApplication = mApplication;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Class<? extends AppCompatActivity> getRegisterActivity() {
        return registerActivity;
    }

    public void setRegisterActivity(Class<? extends AppCompatActivity> registerActivity) {
        this.registerActivity = registerActivity;
    }

    public Class<? extends AppCompatActivity> getLogInActivity() {
        return logInActivity;
    }

    public void setLogInActivity(Class<? extends AppCompatActivity> logInActivity) {
        this.logInActivity = logInActivity;
    }

    public Class<? extends AppCompatActivity> getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(Class<? extends AppCompatActivity> mainActivity) {
        this.mainActivity = mainActivity;
    }

    public Class<? extends AppCompatActivity> getRegisterSuccessActivity() {
        return registerSuccessActivity;
    }

    public void setRegisterSuccessActivity(Class<? extends AppCompatActivity> registerSuccessActivity) {
        this.registerSuccessActivity = registerSuccessActivity;
    }


    public String getShredPrefKey() {
        return ShredPrefKey;
    }

    public void setShredPrefKey(String shredPrefKey) {
        ShredPrefKey = shredPrefKey;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getServerText() {
        return ServerText;
    }

    public void setServerText(String serverText) {
        ServerText = serverText;
    }

    public String getPort() {
        return Port;
    }

    public void setPort(String port) {
        Port = port;
    }

    public String getFormID() {
        return FormID;
    }

    public void setFormID(String formID) {
        FormID = formID;
    }

    public String getSuffix() {
        return Suffix;
    }

    public void setSuffix(String suffix) {
        Suffix = suffix;
    }

    public String getAppID() {
        return AppID;
    }

    public void setAppID(String appID) {
        AppID = appID;
    }

    public String getSecConfig() {
        return SecConfig;
    }

    public void setSecConfig(String secConfig) {
        SecConfig = secConfig;
    }

    public Boolean getHttps() {
        return isHttps;
    }

    public void setHttps(Boolean https) {
        isHttps = https;
    }


    public int getAppActionBarIcon() {
        return appActionBarIcon;
    }

    public void setAppActionBarIcon(int appActionBarIcon) {
        this.appActionBarIcon = appActionBarIcon;
    }

    public int getAppLogo() {
        return appLogo;
    }

    public void setAppLogo(int appLogo) {
        this.appLogo = appLogo;
    }

    public String getAppVersionName() {
        return appVersionName;
    }

    public void setAppVersionName(String appVersionName) {
        this.appVersionName = appVersionName;
    }

    public boolean isRelay() {
        return isRelay;
    }

    public void setRelay(boolean relay) {
        isRelay = relay;
    }

    public String getEmainId() {
        return emainId;
    }

    public void setEmainId(String emainId) {
        this.emainId = emainId;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public ArrayList<MainMenuBean> getMenuBeen() {
        return menuBeen;
    }

    public void setMenuBeen(ArrayList<MainMenuBean> menuBeen) {
        this.menuBeen = menuBeen;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public int getBackButtonIcon() {
        return backButtonIcon;
    }

    public void setBackButtonIcon(int backButtonIcon) {
        this.backButtonIcon = backButtonIcon;
    }

    public ArrayList<String> getNoPasscodeActivity() {
        return noPasscodeActivity;
    }

    public void setNoPasscodeActivity(ArrayList<String> noPasscodeActivity) {
        this.noPasscodeActivity = noPasscodeActivity;
    }

    public Class<? extends AppCompatActivity> getPasscodeLoginActivity() {
        return passcodeLoginActivity;
    }

    public void setPasscodeLoginActivity(Class<? extends AppCompatActivity> passcodeLoginActivity) {
        this.passcodeLoginActivity = passcodeLoginActivity;
    }

    public boolean isDisplayDBReInitMenu() {
        return displayDBReInitMenu;
    }

    public void setDisplayDBReInitMenu(boolean displayDBReInitMenu) {
        this.displayDBReInitMenu = displayDBReInitMenu;
    }

    public boolean isDisplayExportDataMenu() {
        return displayExportDataMenu;
    }

    public void setDisplayExportDataMenu(boolean displayExportDataMenu) {
        this.displayExportDataMenu = displayExportDataMenu;
    }

    public boolean isDisplayImportDataMenu() {
        return displayImportDataMenu;
    }

    public void setDisplayImportDataMenu(boolean displayImportDataMenu) {
        this.displayImportDataMenu = displayImportDataMenu;
    }

    public boolean isDisplayExportMenu() {
        return displayExportMenu;
    }

    public void setDisplayExportMenu(boolean displayExportMenu) {
        this.displayExportMenu = displayExportMenu;
    }

    public boolean isDisplayImportMenu() {
        return displayImportMenu;
    }

    public void setDisplayImportMenu(boolean displayImportMenu) {
        this.displayImportMenu = displayImportMenu;
    }

    public boolean isBackToStartActivity() {
        return isBackToStartActivity;
    }

    public void setBackToStartActivity(boolean backToStartActivity) {
        isBackToStartActivity = backToStartActivity;
    }

    public Class<? extends AppCompatActivity> getLoginSuccessActivity() {
        return loginSuccessActivity;
    }

    public void setLoginSuccessActivity(Class<? extends AppCompatActivity> loginSuccessActivity) {
        this.loginSuccessActivity = loginSuccessActivity;
    }

    public int getViewBackgroundImage() {
        return viewBackgroundImage;
    }

    public void setViewBackgroundImage(int viewBackgroundImage) {
        this.viewBackgroundImage = viewBackgroundImage;
    }

    public int getViewDrawableBorder() {
        return viewDrawableBorder;
    }

    public void setViewDrawableBorder(int viewDrawableBorder) {
        this.viewDrawableBorder = viewDrawableBorder;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDataVaultFileName() {
        return DataVaultFileName;
    }

    public void setDataVaultFileName(String dataVaultFileName) {
        DataVaultFileName = dataVaultFileName;
    }

    public boolean isDisplayForgetPsw() {
        return displayForgetPsw;
    }

    public void setDisplayForgetPsw(boolean displayForgetPsw) {
        this.displayForgetPsw = displayForgetPsw;
    }

    public boolean isLgnUsrNameNonEdit() {
        return isLgnUsrNameNonEdit;
    }

    public void setLgnUsrNameNonEdit(boolean lgnUsrNameNonEdit) {
        isLgnUsrNameNonEdit = lgnUsrNameNonEdit;
    }

    public String getDataVaultPassword() {
        return DataVaultPassword;
    }

    public void setDataVaultPassword(String dataVaultPassword) {
        DataVaultPassword = dataVaultPassword;
    }
}
