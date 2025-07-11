package com.arteriatech.ss.msec.iscan.v4.sync;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;

public class OutletSQLHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "MSEC_OUTLET.DB";
    public static final int DB_VERSION = 16;
    public static final String ChannelPartners = "ChannelPartners";
    public static final String CPDMSDivisions="CPDMSDivisions";
    public static final String RouteSchedulePlans="RouteSchedulePlans";

    public OutletSQLHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        LogManager.writeLogDebug("SQL : "+ChannelPartners+" table created");
        createTable(db,ChannelPartners,ChannelPartners_COLUMNS);
        LogManager.writeLogDebug("SQL : "+CPDMSDivisions+" table created");
        createTable(db,CPDMSDivisions,CPDMSDivisions_COLUMNS);
        LogManager.writeLogDebug("SQL : "+RouteSchedulePlans+" table created");
        createTable(db,RouteSchedulePlans,RouteSchedulePlans_COLUMNS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*db.execSQL("DROP TABLE IF EXISTS " + ChannelPartners);
        db.execSQL("DROP TABLE IF EXISTS " + CPDMSDivisions);
        db.execSQL("DROP TABLE IF EXISTS " + RouteSchedulePlans);
        onCreate(db);*/
        if (newVersion > oldVersion) {
            try {
               /* try {
                    db.execSQL("ALTER TABLE " + ChannelPartners + " ADD COLUMN " + CPUID + " text");
                }catch (Throwable e){
                    e.printStackTrace();
                }
                try {
                    db.execSQL("ALTER TABLE "+ChannelPartners+ " ADD COLUMN "+CP_TYPE+" text");
                    db.execSQL("ALTER TABLE "+ChannelPartners+ " ADD COLUMN "+Latitude+" text");
                    db.execSQL("ALTER TABLE "+ChannelPartners+ " ADD COLUMN "+Longitude+" text");
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                db.execSQL("ALTER TABLE "+ChannelPartners+ " ADD COLUMN "+URL1+" text");
                db.execSQL("ALTER TABLE "+ChannelPartners+ " ADD COLUMN "+DOA+" text");*/
                db.execSQL("ALTER TABLE " + ChannelPartners + " ADD COLUMN " + Landmark + " text");

            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
    public static final String CPGUID="CPGUID";
    public static final String NAME="Name";
    public static final String OWNER_NAME = "OwnerName";
    public static final String URL1 = "URL1";
    public static final String DOA = "DOA";
    public static final String ADDRESS1 = "Address1";
    public static final String ADDRESS2 = "Address2";
    public static final String MOBILE1 = "mobile1";
    public static final String MOBILE3 = "mobile3";
    public static final String CPUID = "CPUID";
    public static final String CP_TYPE = "CP_TYPE";
    public static final String Latitude = "Latitude";
    public static final String Longitude = "Longitude";
    public static final String Landmark = "Landmark";

    public static final String CP1GUID = "CP1GUID";
    public static final String DMSDivision = "DMSDivision";
    public static final String DMSDivisionDesc = "DMSDivisionDesc";
    public static final String Group1 = "Group1";
    public static final String Group1Desc = "Group1Desc";
    public static final String Group2 = "Group2";
    public static final String Group2Desc = "Group2Desc";
    public static final String Group3 = "Group3";
    public static final String Group3Desc = "Group3Desc";
    public static final String Group4="Group4";
    public static final String Group4Desc="Group4Desc";
    
    public static final String RouteSchPlanGUID="RouteSchPlanGUID";
    public static final String RouteSchGUID="RouteSchGUID";
    public static final String VisitCPGUID="VisitCPGUID";
    public static final String SequenceNo="SequenceNo";


    public static final String RouteSchedulePlans_COLUMNS="(" +
            ""+RouteSchPlanGUID+" text primary key not null ," +
            ""+RouteSchGUID+" text," +
            ""+VisitCPGUID+" text," +
            ""+SequenceNo+" text)";
    public static final String ChannelPartners_COLUMNS="(" +
            ""+CPGUID+" text primary key not null ," +
            ""+NAME+" text," +
            "" + OWNER_NAME + " text," +
            "" + ADDRESS1 + " text, " +
            "" + ADDRESS2 + " text," +
            "" + MOBILE1 + " text," +
            "" + MOBILE3 + " text," +
            "" + CPUID + " text," +
            "" + CP_TYPE + " text," +
            "" + Latitude + " text," +
            "" + Longitude + " text," +
            "" + URL1 + " text," +
            "" + DOA + " text," +
            "" + Landmark + " text)";
    public static final String CPDMSDivisions_COLUMNS = "(" +
            "" + CP1GUID + " text primary key not null ," +
            "" + CPGUID + " text," +
            "" + DMSDivision + " text," +
            "" + DMSDivisionDesc + " text, " +
            "" + Group1 + " text," +
            "" + Group1Desc + " text," +
            "" + Group2 + " text," +
            "" + Group2Desc + " text," +
            "" + Group3 + " text," +
            ""+Group3Desc+" text," +
            ""+Group4+" text," +
            ""+Group4Desc+" text)";
    


    public static void createTable(SQLiteDatabase db, String tableName, String clumsname) {
        try {
            String sql = Constants.create_table + tableName
                    + "  " + clumsname + "";

            db.execSQL(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void insertCPDmsRecord(String cpguid,
                                  String division,
                                  String divisionDesc,
                                  String group1,
                                  String group1Desc,
                                  String group2,
                                  String group2Desc,
                                  String group3,
                                  String group3Desc,
                                  String group4,
                                  String group4Desc
                                  ) {

        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put(CP1GUID,cpguid);
            contentValues.put(CPGUID,cpguid);
            contentValues.put(DMSDivision, division);
            contentValues.put(DMSDivisionDesc, divisionDesc);
            contentValues.put(Group1, group1);
            contentValues.put(Group1Desc, group1Desc);
            contentValues.put(Group2, group2);
            contentValues.put(Group2Desc, group2Desc);
            contentValues.put(Group3,group3);
            contentValues.put(Group3Desc, group3Desc);
            contentValues.put(Group4, group4);
            contentValues.put(Group4Desc,group4Desc);
            db.insert(CPDMSDivisions, null, contentValues);
            db.close();
            LogManager.writeLogDebug("SQL : CPDMSDivisions data inserted");
        } catch (Throwable e) {
            LogManager.writeLogDebug("SQL : error inserting  data with CPDMSDivisions"+e.getMessage());
            db.close();
        }
    }

}
