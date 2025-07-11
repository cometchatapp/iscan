package com.arteriatech.ss.msec.iscan.v4.ui;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.text.TextUtils;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AlertDialog.Builder;


import com.arteriatech.ss.msec.iscan.v4.R;

import java.util.Date;

public class DialogFactory {
    public static String DEFAULT_TIMESTAMP="dd-MM-yyyy hh:mm a";
    private  String positiveButton = "";
    private String negativeButton = "";
    public static String message = "";
    private String title = "";
    private int theme = 0;
    private boolean isAlert = false;
    private boolean isCancelable = false;
    private Context context;
    private OnDialogClick dialogInterface=null;
    private static ProgressDialog pdLoadDialog = null;
    private static String countdown = "";
    private static long milliSec = 2400000;
    private static CountDownTimer countDownTimer = null;

    public static class Alert {
        private String positiveButton = "";
        private String negativeButton = "";
        private String message = "";
        private String title = "";
        private int theme = 0;
        private boolean isAlert = false;
        private Context context;
        private OnDialogClick dialogInterface=null;

        public Alert(Context context) {
            this.context = context;
        }

        public Alert setOnDialogClick(OnDialogClick dialogInterface) {
            this.dialogInterface = dialogInterface;
            return this;
        }
        public Alert setPositiveButton(String text) {
            this.positiveButton = text;
            return this;
        }

        public Alert setNegativeButton(String text) {
            this.negativeButton = text;
            return this;
        }

        public Alert setMessage(String text) {
            this.message = text;
            return this;
        }

        public Alert setTitle(String text) {
            this.title = text;
            return this;
        }

        public Alert setTheme(int theme) {
            this.theme = theme;
            return this;
        }

        public Alert isAlert(boolean isAlert) {
            this.isAlert = isAlert;
            return this;
        }

        public DialogFactory show() {
            DialogFactory dialogFactory = null;
            if (!((Activity)context).isFinishing()) {
                dialogFactory = new DialogFactory();
                dialogFactory.positiveButton = this.positiveButton;
                dialogFactory.negativeButton = this.negativeButton;
                dialogFactory.message = this.message;
                dialogFactory.title = this.title;
                dialogFactory.theme = this.theme;
                dialogFactory.isAlert = this.isAlert;
                dialogFactory.dialogInterface = this.dialogInterface;
                String defaultButtonYes = "Ok";
                String defaultButtonNo = "Cancel";
                if (positiveButton != null && !TextUtils.isEmpty(positiveButton)) {
                    defaultButtonYes = positiveButton;
                }
                if (negativeButton != null && !TextUtils.isEmpty(negativeButton)) {
                    defaultButtonNo = negativeButton;
                }
                if (isAlert) {
                    Builder builder = null;
                    if (theme!=0) {
                        builder = new Builder(context, theme);
                    }else{
                        builder = new Builder(context, R.style.MaterialAlertDialog);
                    }
                    String timeStamp = (String) android.text.format.DateFormat.format(DEFAULT_TIMESTAMP, new Date());
                    builder.setTitle(timeStamp);
                    builder.setMessage(message).setCancelable(false)
                            .setPositiveButton(defaultButtonYes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    if (dialogInterface!=null) {
                                        dialogInterface.onDialogClick(true);
                                    }
                                }
                            });
                    builder.show();
                } else {
                    try {
                        Builder builder = null;
                        if (theme!=0) {
                            builder = new Builder(context, theme);
                        }else{
                            builder = new Builder(context, R.style.MaterialAlertDialog);
                        }
                        String timeStamp = (String) android.text.format.DateFormat.format(DEFAULT_TIMESTAMP, new Date());
                        builder.setTitle(timeStamp);
                        builder.setMessage(message).setCancelable(false);
                        builder.setPositiveButton(defaultButtonYes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (dialogInterface!=null) {
                                    dialogInterface.onDialogClick(true);
                                }
                            }
                        });
                        builder.setNegativeButton(defaultButtonNo, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (dialogInterface!=null) {
                                    dialogInterface.onDialogClick(false);
                                }
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
            }
            return dialogFactory;
        }
    }

    public static class Progress {
        private String message = "";
        private String title = "";
        private int theme = 0;
        private boolean isCancelable = false;
        private Context context;

        public Progress(Context context) {
            this.context = context;
        }

        public Progress setMessage(String text) {
            this.message = text;
            return this;
        }

        public Progress setTitle(String text) {
            this.title = text;
            return this;
        }

        public Progress setTheme(int theme) {
            this.theme = theme;
            return this;
        }
        public Progress setCancelable(boolean isCancelable) {
            this.isCancelable = isCancelable;
            return this;
        }

        public ProgressDialog show() {
            try {
                DialogFactory dialogFactory = new DialogFactory();
                dialogFactory.message = this.message;
                dialogFactory.title = this.title;
                dialogFactory.theme = this.theme;
                dialogFactory.isCancelable = this.isCancelable;
                pdLoadDialog = null;
                if (theme!=0) {
                    pdLoadDialog = new ProgressDialog(context, theme);
                }else{
                    pdLoadDialog = new ProgressDialog(context,R.style.MaterialAlertDialog);
                }
                try {
                    if (theme!=0) {
                        pdLoadDialog = new ProgressDialog(context, theme);
                    }else{
                        pdLoadDialog = new ProgressDialog(context,R.style.MaterialAlertDialog);
                    }
                    pdLoadDialog.setTitle(title);
                    pdLoadDialog.setMessage(message);
                    pdLoadDialog.setCancelable(isCancelable);
                    pdLoadDialog.show();
                    setTimer();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return pdLoadDialog;
        }
        public void hide(){
            try {
                cancelTimer();
                if (pdLoadDialog!=null&&pdLoadDialog.isShowing()){
                    pdLoadDialog.dismiss();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        public void setProgressMessage(String progressMessage){
            this.message = progressMessage;
            DialogFactory.message = progressMessage;
            if(pdLoadDialog!=null){
                pdLoadDialog.setMessage(progressMessage);
            }
        }
        private void setTimer() {
            countdown = "";
            try {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    countDownTimer = null;
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            String timeStamp = (String) android.text.format.DateFormat.format(DEFAULT_TIMESTAMP, new Date());
            countDownTimer = new CountDownTimer(milliSec, 1000) {
                public void onTick(long millisUntilFinished) {
                    long seconds = milliSec / 1000 - millisUntilFinished / 1000;
                    long minutes = ((milliSec / 1000) / 60) - ((millisUntilFinished / 1000) / 60) - 1;
                    int sec = (int) seconds - (int) minutes * 60;
                    countdown = String.format("%02d", minutes) + ":" + String.format("%02d", sec);
                    if (pdLoadDialog != null && pdLoadDialog.isShowing()) {
                        pdLoadDialog.setTitle(timeStamp);
                        pdLoadDialog.setMessage(DialogFactory.message + "\t\t" + countdown);
                    }
                }

                @Override
                public void onFinish() {

                }

            };
            countDownTimer.start();
        }

        private void cancelTimer() {
            try {
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    countDownTimer = null;
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }

        }
    }
    
}

