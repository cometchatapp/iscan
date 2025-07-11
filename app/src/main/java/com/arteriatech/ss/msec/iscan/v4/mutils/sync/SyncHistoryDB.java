package com.arteriatech.ss.msec.iscan.v4.mutils.sync;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.arteriatech.ss.msec.iscan.v4.common.Constants;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by e10769 on 13-01-2017.
 */

public class SyncHistoryDB extends SQLiteOpenHelper {
    public static final String SSSO_TABLE = "SSSO_TABLE";
    public static String SSSO_GUID = "SSSO_GUID";
    public static final String SSSOTable = "create table " + SSSO_TABLE + " (" + SSSO_GUID + " text)";
    public static final String DB_NAME = "syncDB";
    private static final int DB_VERSION = 2;
    public static final String TABLE_NAME = "syncHistory";
    public static final String CP_STOCK_TABLE_NAME = "cpStock";

    /*table column name*/
    public static String COL_SYNC_GROUP = "syncGroup";
    public static String COL_COLLECTION = "collection";
    public static String COL_TIMESTAMP = "timestamp";
    public static String DEVICE_NO = "deviceNo";
    public static String INV_GUID = "invoiceGUID";
    public static String MATERIAL_NO = "materialNo";
    public static String MATERIAL_QTY = "materialQTY";
    public static String MATERIAL_UOM = "materialUOM";
    public static String MRP = "MRP";

    public static final String syncHistoryTable = "create table " + TABLE_NAME + " (" + COL_SYNC_GROUP + " text, " + COL_COLLECTION + " text, " + COL_TIMESTAMP + " text)";
    public static final String cpStockTable = "create table " + CP_STOCK_TABLE_NAME + " (" + DEVICE_NO + " text, " + INV_GUID + " text, " + MATERIAL_NO + " text, "
            + MATERIAL_QTY + " text, " + MATERIAL_UOM+ " text, " + MRP + " text)";

    public SyncHistoryDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(syncHistoryTable);
        db.execSQL(cpStockTable);
        db.execSQL(SSSOTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        db.execSQL("drop table if exists " + CP_STOCK_TABLE_NAME);
        db.execSQL("drop table if exists " + SSSO_TABLE);
        if (newVersion > oldVersion) {
            try {
                db.execSQL("ALTER TABLE "+CP_STOCK_TABLE_NAME +" ADD COLUMN "+MRP+" text");
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        onCreate(db);
    }

    /*create record*/
    public void createRecord(SyncHistoryModel syncHistoryModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_SYNC_GROUP, syncHistoryModel.getSyncGroup());
        contentValues.put(COL_COLLECTION, syncHistoryModel.getCollections());
        contentValues.put(COL_TIMESTAMP, syncHistoryModel.getTimeStamp());
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }


    /*delete record*/
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
    public void deleteAllCPStock() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CP_STOCK_TABLE_NAME, null, null);
        db.close();
    }

    /*update record*/
    public void updateRecord(String collection, String timestamp) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TIMESTAMP, timestamp);
        // updating row
        db.update(TABLE_NAME, contentValues, COL_COLLECTION + " = ?",
                new String[]{collection});
        db.close();
    }

    /*get all record*/
    public List<SyncHistoryModel> getAllRecord() {
        List<SyncHistoryModel> syncHistoryModelList = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            syncHistoryModelList = new ArrayList<>();
            String selectQuery = "select * from " + TABLE_NAME;
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    SyncHistoryModel syncHistoryModel = new SyncHistoryModel();
                    syncHistoryModel.setCollections(cursor.getString(1));
                    syncHistoryModel.setTimeStamp(cursor.getString(2));
                    syncHistoryModelList.add(syncHistoryModel);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return syncHistoryModelList;
    }

    public List<SyncHistoryModel> getSyncTimeBySpecificColl(String whereCol, String whereColVal) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<SyncHistoryModel> syncHistoryModelList = new ArrayList<>();
        String selectQuery = "select * from " + TABLE_NAME +" WHERE "+whereCol+"='"+whereColVal+"'";
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    SyncHistoryModel syncHistoryModel = new SyncHistoryModel();
                    syncHistoryModel.setCollections(cursor.getString(1));
                    syncHistoryModel.setTimeStamp(cursor.getString(2));
                    syncHistoryModelList.add(syncHistoryModel);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return syncHistoryModelList;
    }
    public boolean syncHistoryTableExist() {
      return syncHistoryTableExist(TABLE_NAME);
    }
    public boolean syncHistoryTableExist(String tableName) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.close();
                    return true;
                }
                cursor.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public static void deleteInvoiceDocID(Context context, String docID) {
        SyncHistoryDB dbHelper = new SyncHistoryDB(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try {

            String[] args = new String[]{docID};
            db.delete(SyncHistoryDB.CP_STOCK_TABLE_NAME, SyncHistoryDB.DEVICE_NO + "=?", args);

            db.close();
        } catch (SQLiteException e) {
            db.close();
            e.printStackTrace();
        }

    }
    public static void deleteAllInvoices(Context context) {
        SyncHistoryDB dbHelper = new SyncHistoryDB(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try {
            db.delete(SyncHistoryDB.CP_STOCK_TABLE_NAME, null, null);
            db.close();
        } catch (Throwable e) {
            db.close();
            e.printStackTrace();
        }

    }

    public static boolean getMAterialPresentORnot(String materialNo, Context context) {
        boolean check = false;
        SyncHistoryDB dbHelper = new SyncHistoryDB(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        String selectQuery = "select * from " + CP_STOCK_TABLE_NAME +" WHERE "+MATERIAL_NO+"='"+materialNo+"'";
        try {
            cursor = db.rawQuery(selectQuery, null);
            if (cursor!=null && cursor.getCount()>0) {
                check = true;
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return check;
    }

    public Cursor getLastSyncTime(String Tbl,String whereColumn,String wherecolumnVal,Context context) {
        String lastSyncTimeStampQry = Constants.getLastSyncTimeStamp(Tbl,whereColumn,wherecolumnVal);
        Cursor cursor = null;
        SyncHistoryDB dbHelper = new SyncHistoryDB(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            if(db==null)
                db = Constants.EventUserHandler;
            cursor = db.rawQuery(lastSyncTimeStampQry, new String[]{});
        } catch (Exception e) {
            cursor = null;
        }
        return cursor;
    }
    public void createSSSORecord(String SOGUID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SSSO_GUID, SOGUID);
        db.insert(SSSO_TABLE, null, contentValues);
        db.close();
    }
}
