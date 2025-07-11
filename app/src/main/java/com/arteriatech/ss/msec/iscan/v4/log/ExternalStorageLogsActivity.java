package com.arteriatech.ss.msec.iscan.v4.log;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.arteriatech.mutils.adapter.AdapterViewInterface;
import com.arteriatech.mutils.adapter.SimpleRecyclerViewTypeAdapter;
import com.arteriatech.ss.msec.iscan.v4.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ExternalStorageLogsActivity extends AppCompatActivity implements AdapterViewInterface {
    private RecyclerView recyclerView;
    private TextView noRecordFound,textViewLogcat;
    private SimpleRecyclerViewTypeAdapter<ExternalLogViewBean> recyclerViewAdapter;
    ArrayList<ExternalLogViewBean> listofFiles = new ArrayList<>();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getLogFromStorage();
        captureLog();
        exportLog();
    }

    private void getLogFromStorage() {
        listofFiles.addAll(getLogFilesFromStorage());
        recyclerViewAdapter.refreshAdapter(listofFiles);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(Object o, View view, int i) {

    }

    @Override
    public int getItemViewType(int i, ArrayList arrayList) {
        return 0;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i, View view) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem= layoutInflater.inflate(R.layout.ext_log_itemview, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;     }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position, Object o, ArrayList arrayList) {
        final ExternalLogViewBean countBean = (ExternalLogViewBean) arrayList.get(position);
        ((ViewHolder) viewHolder).tvFileName.setText(countBean.getFileName());

        /*((ViewHolder) viewHolder).ll_mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri textUri = null;
                *//*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    pdfUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", countBean);
                } else {*//*
                File newFile = new File(countBean.getFilePath());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    textUri = FileProvider.getUriForFile(ExternalStorageLogsActivity.this, getPackageName() + ".provider", newFile);
                    grantUriPermission(" com.arteriatech.ss.msecsales.rspl.visualaid", textUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else {
                    textUri = Uri.fromFile(newFile);
                }

                //  Uri uri = Uri.fromFile(textUri);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                // Text file
                intent.setDataAndType(textUri, "text/plain");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(intent, "Open File"));

            }
        });*/

        ((ViewHolder) viewHolder).ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri pdfUri = null;
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    pdfUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", countBean);
                } else {*/
                File newFile = new File(countBean.getFilePath());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    pdfUri = FileProvider.getUriForFile(ExternalStorageLogsActivity.this, getPackageName() + ".provider", newFile);
                } else {
                    pdfUri = Uri.fromFile(newFile);
                }
               // }
                /*Intent share = new Intent();
                share.setAction(Intent.ACTION_SEND);
                share.setType("application/pdf");
                share.putExtra(Intent.EXTRA_STREAM, pdfUri);
                startActivity(Intent.createChooser(share, "Share"));*/

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Log Files");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, pdfUri);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));




            }
        });
       /* if(countBean.getCount()>0){
            holder.tvPendingStatus.setBackgroundColor(context.getResources().getColor(R.color.RejectedColor));
        }else {
            holder.tvPendingStatus.setBackgroundColor(context.getResources().getColor(R.color.ApprovedColor));
        }
        holder.tvEntityName.setText(countBean.getCollection());
        holder.tvPendingCount.setText(""+countBean.getCount());
        holder.tvSyncTime.setText(""+countBean.getSyncTime());
//        holder.ivUploadDownload.setText("UPLOAD");
        holder.ivUploadDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (collectionSyncInterface!=null){
                    syncType = Constants.DownLoad;
                    collectionSyncInterface.onUploadDownload(false,countBean,syncType);
                }
            }
        });*/
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvFileName;
        public ImageView ivShare;
        public LinearLayout ll_mainLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            tvFileName = (TextView)itemView.findViewById(R.id.tvEntityName);
            ivShare = (ImageView) itemView.findViewById(R.id.ivShare);
            ll_mainLayout = (LinearLayout) itemView.findViewById(R.id.ll_mainLayout);
        }
    }
    public static ArrayList<ExternalLogViewBean> getLogFilesFromStorage() {
        ArrayList<ExternalLogViewBean> dataList = null;
        try {
            dataList = new ArrayList<>();
            ExternalLogViewBean beanData = new ExternalLogViewBean();
            String folder_main = "AWSMLogs";
            File f = new File(Environment.getExternalStorageDirectory(), folder_main);
            if (!f.exists()) {
                f.mkdirs();
            }
            String path = f.getAbsolutePath();
            Log.d("Files", "Path: " + path);
            File directory = new File(path);
            File[] files = directory.listFiles();
            if (files!=null) {
                Log.d("Files", "Size: "+ files.length);
                beanData = new ExternalLogViewBean();
                for (int i = 0; i < files.length; i++) {
                    beanData = new ExternalLogViewBean();
                    Log.d("Files", "FileName:" + files[i].getName());
                    beanData.setFileName(files[i].getName());
                    beanData.setFilePath(files[i].getAbsolutePath());
                    dataList.add(beanData);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return dataList;
    }
    private void createLog() {
        try {
            /*new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Process process = Runtime.getRuntime().exec("logcat -d");
                        BufferedReader bufferedReader = new BufferedReader(
                                new InputStreamReader(process.getInputStream()));

                        StringBuilder log = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            log.append(line);
                        }
                        textViewLogcat.setText(log.toString());
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            });*/
                File appDirectory = new File( Environment.getExternalStorageDirectory() + "/MyPersonalAppFolder" );
                File logDirectory = new File( appDirectory + "/log" );
                File logFile = new File( logDirectory, "logcat" + System.currentTimeMillis() + ".txt" );

                // create app folder
                if ( !appDirectory.exists() ) {
                    appDirectory.mkdir();
                }

                // create log folder
                if ( !logDirectory.exists() ) {
                    logDirectory.mkdir();
                }

                // clear the previous logcat and then write the new one to the file
                try {
                    Process process = Runtime.getRuntime().exec("logcat -c");
                    process = Runtime.getRuntime().exec("logcat -f " + logFile);
                } catch ( Throwable e ) {
                    e.printStackTrace();
                }

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    private void captureLog(){
        try {
            File filename = new File(Environment.getExternalStorageDirectory()+"/AWSMLocat.txt");
            filename.createNewFile();
            String cmd = "logcat -d -f"+filename.getAbsolutePath();
            Runtime.getRuntime().exec(cmd);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    private String mStrFilePath = "";
    private boolean exportLog() {
        boolean flagforlog;
        mStrFilePath = "";
        try {
            File filename = new File(Environment.getExternalStorageDirectory()
                    + "/" + getString(R.string.app_name) + "_device.txt");
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
}
