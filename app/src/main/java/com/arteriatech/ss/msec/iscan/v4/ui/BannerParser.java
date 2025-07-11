package com.arteriatech.ss.msec.iscan.v4.ui;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;

import com.arteriatech.mutils.registration.UtilRegistrationActivity;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;
import com.arteriatech.ss.msec.iscan.v4.registration.Configuration;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class BannerParser {
    public static List<SlideModel> getBannerImageList(Context context){
        List<SlideModel> imageList = new ArrayList<>();
        try {
            File folder = new File(getStoragePath(context));
            File[] allFiles=null;
            if(folder.exists()){
                allFiles = folder.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png"));
                    }
                });
                if (allFiles!=null) {

                    for (int i = 0; i < allFiles.length; i++) {
                        imageList.add(new SlideModel("file:///"+allFiles[i].getAbsolutePath(), ScaleTypes.FIT));
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return imageList;
    }

    public static List<SlideModel> getAWSMBannerImageList(Context context){
        List<SlideModel> imageList = new ArrayList<>();
        try {
            File folder = new File(getAWSMStoragePath(context));
            File[] allFiles=null;
            if(folder.exists()){
                allFiles = folder.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png"));
                    }
                });
                if (allFiles!=null) {

                    for (int i = 0; i < allFiles.length; i++) {
                        imageList.add(new SlideModel("file:///"+allFiles[i].getAbsolutePath(), ScaleTypes.FIT));
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return imageList;
    }

    public static List<SlideModel> getOutletBannerImageList(Context context,String filename){
        List<SlideModel> imageList = new ArrayList<>();
        try {
            File folder = new File(getCPStoragePath(context));
            File[] allFiles=null;
            if(folder.exists()){
                allFiles = folder.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return (name.contains(filename));
                    }
                });
                if (allFiles!=null) {

                    for (int i = 0; i < allFiles.length; i++) {
                        imageList.add(new SlideModel("file:///"+allFiles[i].getAbsolutePath(), ScaleTypes.FIT));
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return imageList;
    }

    public static List<SlideModel> deleteImages(Context context){
        List<SlideModel> imageList = new ArrayList<>();
        try {
            File folder = new File(getStoragePath(context));
            File[] allFiles=null;
            if(folder.exists()){
                allFiles = folder.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png"));
                    }
                });
                if (allFiles!=null) {

                    for (int i = 0; i < allFiles.length; i++) {
                        allFiles[i].delete();
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return imageList;
    }

    public static List<SlideModel> deleteOutletsImages(Context context){
        List<SlideModel> imageList = new ArrayList<>();
        try {
            File folder = new File(getCPStoragePath(context));
            File[] allFiles=null;
            if(folder.exists()){
                allFiles = folder.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png"));
                    }
                });
                if (allFiles!=null) {

                    for (int i = 0; i < allFiles.length; i++) {
                        allFiles[i].delete();
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return imageList;
    }

    public static List<SlideModel> deleteAWSMImages(Context context){
        List<SlideModel> imageList = new ArrayList<>();
        try {
            File folder = new File(getAWSMStoragePath(context));
            File[] allFiles=null;
            if(folder.exists()){
                allFiles = folder.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png"));
                    }
                });
                if (allFiles!=null) {

                    for (int i = 0; i < allFiles.length; i++) {
                        allFiles[i].delete();
                    }
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return imageList;
    }

    public static void extractBannerImages(Context context){
        try {
            String zipLocation = getStoragePath(context)+"bundle.zip";
            String extractLocation = getStoragePath(context);
            unzip(zipLocation,extractLocation);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    public static void downloadBannerFile(Context context){
        try {
            String DownloadUrl = getBundleURL()+"/Banner";
            try {
                try {
                    deleteDir(getStoragePath(context));
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(DownloadUrl));
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI| DownloadManager.Request.NETWORK_MOBILE);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);  // This will show notification on top when downloading the file.
                String userName = context.getSharedPreferences(Constants.PREFS_NAME,0).getString(UtilRegistrationActivity.KEY_username, "");
                String password = context.getSharedPreferences(Constants.PREFS_NAME,0).getString(UtilRegistrationActivity.KEY_password, "");
                String userCredentials = userName + ":" + password;
                String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes(StandardCharsets.UTF_8), 2);
                request.addRequestHeader("Authorization", basicAuth);
                request.setDescription("Banner downloading");
                request.setAllowedOverRoaming(true);
                request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOCUMENTS + "/Britannia/", "bundle.zip");
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                long downloadID = downloadManager.enqueue(request);
                if (DownloadManager.STATUS_SUCCESSFUL == 8) {

                }

            } catch (Throwable e) {
                e.printStackTrace();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    private static String getBundleURL(){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(Configuration.IS_HTTPS?"https":"http")
                .encodedAuthority(Configuration.server_Text)
                .appendPath("mobileservices")
                .appendPath("application")
                .appendPath(Configuration.APP_ID)
                .appendPath("bundles")
                .appendPath("v1")
                .appendPath("runtime")
                .appendPath("bundle")
                .appendPath("application")
                .appendPath(Configuration.APP_ID)
                .appendPath("bundle");
//                .fragment(applicationName);
        return builder.build().toString();
    }
    private static String getStoragePath(Context context){
        return context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/Britannia/";
    }

    private static String getAWSMStoragePath(Context context){
        return context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/AWSMBanner/";
    }

    public static String getCPStoragePath(Context context){
        return context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) + "/OutletsBanner/";
    }

    private static void deleteDir(String path){
        try {
            File dir = new File(path);
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    new File(dir, children[i]).delete();
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void saveImage (Context context,Bitmap decodedByte,String filename){
        File f = new File(getStoragePath(context));
        if (!f.exists()) {
            if (!f.mkdir()) {
                f.mkdirs();
            }
        }
        try {
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//            final Bitmap bitMap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
            final Bitmap bitMap = decodedByte;
//                final Bitmap bitMap = Compressor.getDefault(CreateAttendanceActivity.this).compressToBitmap(f);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitMap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            String extStorageDirectory = getStoragePath(context);
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
                decodedByte.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.flush();
                outStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveAWSMImage (Context context,Bitmap decodedByte,String filename){
        File f = new File(getAWSMStoragePath(context));
        if (!f.exists()) {
            if (!f.mkdir()) {
                f.mkdirs();
            }
        }
        try {
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//            final Bitmap bitMap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
            final Bitmap bitMap = decodedByte;
//                final Bitmap bitMap = Compressor.getDefault(CreateAttendanceActivity.this).compressToBitmap(f);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitMap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            String extStorageDirectory = getAWSMStoragePath(context);
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
                decodedByte.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.flush();
                outStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveCPImage (Context context,Bitmap decodedByte,String filename){
        File f = new File(getCPStoragePath(context));
        if (!f.exists()) {
            if (!f.mkdir()) {
                f.mkdirs();
            }
        }
        try {
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//            final Bitmap bitMap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
            final Bitmap bitMap = decodedByte;
//                final Bitmap bitMap = Compressor.getDefault(CreateAttendanceActivity.this).compressToBitmap(f);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitMap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            String extStorageDirectory = getCPStoragePath(context);
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
                decodedByte.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                outStream.flush();
                outStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean unzip(String zipFile, String location){
        int size;
        final int BUFFER_SIZE = 2048;

        byte[] buffer = new byte[BUFFER_SIZE];

        try {
            if ( !location.endsWith(File.separator) ) {
                location += File.separator;
            }
            File f = new File(location);
            if(!f.isDirectory()) {
                f.mkdirs();
            }
            ZipInputStream zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile), BUFFER_SIZE));
            try {
                ZipEntry ze = null;
                while ((ze = zin.getNextEntry()) != null) {
                    String path = location + ze.getName();
                    File unzipFile = new File(path);

                    if (ze.isDirectory()) {
                        if(!unzipFile.isDirectory()) {
                            unzipFile.mkdirs();
                        }
                    } else {
                        // check for and create parent directories if they don't exist
                        File parentDir = unzipFile.getParentFile();
                        if ( null != parentDir ) {
                            if ( !parentDir.isDirectory() ) {
                                parentDir.mkdirs();
                            }
                        }

                        // unzip the file
                        FileOutputStream out = new FileOutputStream(unzipFile, false);
                        BufferedOutputStream fout = new BufferedOutputStream(out, BUFFER_SIZE);
                        try {
                            while ( (size = zin.read(buffer, 0, BUFFER_SIZE)) != -1 ) {
                                fout.write(buffer, 0, size);
                            }

                            zin.closeEntry();
                        }
                        finally {
                            fout.flush();
                            fout.close();
                        }
                    }
                }
            }
            finally {
                zin.close();
                return true;
            }
        }
        catch (Exception e) {
        }
        return false;
    }
}
