package com.arteriatech.ss.msec.iscan.v4.utils.dateutils;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class DateUtils<T>{
    private Context context;
    private final static String defaultDateFormat = "dd/MM/yyyy";
    private final static String SAPDateFormat = "yyyy-MM-dd";
    private String dateFormat;
    private int maxNumberOfDaysRange;
    private int casePosition;
    private boolean isPastDaysAllowed;
    private boolean isFutureDaysAllowed;
    private boolean isFromDate;
    private OnDateUtilListener onDateUtilListener;
    private static Calendar calendar;
    private DateUtils() {} // cannot create an Instance here.

    public static class DateSelectionBuilder<T> implements DatePickerDialog.OnDateSetListener {
        private Context context;
        private String dateFormat;
        private int casePosition;
        private int maxNumberOfDaysRange;
        private boolean isPastDaysAllowed=true;
        private boolean isFutureDaysAllowed=true;
        private boolean isFromDate;
        private OnDateUtilListener onDateUtilListener;
        public DateSelectionBuilder(Context context){
            this.context = context;
        }

        public DateSelectionBuilder isPastDaysAllowed(boolean isPastDaysAllowed){
            this.isPastDaysAllowed = isPastDaysAllowed;
            return this;
        }
        public DateSelectionBuilder isFutureDaysAllowed(boolean isFutureDaysAllowed){
            this.isFutureDaysAllowed = isFutureDaysAllowed;
            return this;
        }
        public DateSelectionBuilder isFromDate(boolean isFromDate){
            this.isFromDate = isFromDate;
            return this;
        }
        public DateSelectionBuilder maxNumberOfDaysRange(int maxNumberOfDaysRange){
            this.maxNumberOfDaysRange = maxNumberOfDaysRange;
            return this;
        }

        public DateSelectionBuilder casePosition(int casePosition){
            this.casePosition = casePosition;
            return this;
        }

        public DateUtils<T>build(OnDateUtilListener listener){
            this.onDateUtilListener=listener;
            DateUtils<T> dateUtils = new DateUtils<>();
            dateUtils.context=this.context;
            dateUtils.dateFormat=this.dateFormat;
            dateUtils.isPastDaysAllowed=this.isPastDaysAllowed;
            dateUtils.isFutureDaysAllowed=this.isFutureDaysAllowed;
            dateUtils.maxNumberOfDaysRange=this.maxNumberOfDaysRange;
            dateUtils.casePosition=this.casePosition;
            if (isFromDate){
                calendar = null;
            }
            if (dateFormat==null){
                dateFormat = defaultDateFormat;
                getDate();
            }
            return dateUtils;
        }
        public void getDate() {
            try {
                DatePickerDialog dialogBox;
                if (calendar==null) {
                    calendar = Calendar.getInstance(TimeZone.getDefault());
                }
                if (maxNumberOfDaysRange!=0){
                    dialogBox = new DatePickerDialog(context, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    dialogBox.getDatePicker().setMinDate(calendar.getTimeInMillis());
                    calendar.add(Calendar.DATE, maxNumberOfDaysRange);
                    dialogBox.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                }else{
                    dialogBox= new DatePickerDialog(context, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                }
                if (!isPastDaysAllowed) {
                    dialogBox.getDatePicker().setMinDate(calendar.getTimeInMillis());
                }
                if (!isFutureDaysAllowed){
                    dialogBox.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                }
                dialogBox.show();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            if (calendar==null) {
                calendar = Calendar.getInstance();
            }
            calendar.set(year, month, dayOfMonth);
            onDateUtilListener.getSelectedDate(getDateFormat(dateFormat).format(calendar.getTime()), getDateFormat(SAPDateFormat).format(calendar.getTime()), casePosition);
            Calendar calendar1 = Calendar.getInstance();
            onDateUtilListener.currentDate(getDateFormat(dateFormat).format(calendar1.getTime()), getDateFormat(SAPDateFormat).format(calendar1.getTime()));
        }
    }
    //---------------------------------end of builder --------------------------------------------

    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat getDateFormat(String customFormat) {
        return new SimpleDateFormat(customFormat);
    }
    // interface
    public interface OnDateUtilListener{
        void getSelectedDate(String date, String formattedDate, int casePosition);
        void currentDate(String current, String formattedDate);
    }

}
