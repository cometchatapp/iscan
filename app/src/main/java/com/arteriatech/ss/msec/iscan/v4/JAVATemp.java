package com.arteriatech.ss.msec.iscan.v4;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class JAVATemp {
    public static void main(String[] args) {
//        getMetadataProperties(".Customer");
        String currentDateTimeString = (String) android.text.format.DateFormat
                .format("yyyy-MM-dd'T'HH:mm:ss", new Date());
        System.out.println(currentDateTimeString);
    }
    @SuppressLint("NewApi")
    private static void getMetadataProperties(String entityName){
        /*Set<String> set =  OfflineManager.offlineStore.getMetadata().getMetaEntity("SSGW_ALL".concat(entityName)).getPropertyNames();
        ArrayList<String> s= new ArrayList<>();
        for (String name :set) {
            s.add("@ODataProperty \nprivate String ".concat(name));
        }
        Collections.sort(s);
        String str=s.toString();*/

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("executing t1");
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("SubBrand eq '01020101' or SubBrand eq '04010104' or SubBrand eq '0908' or" +
                        " SubBrand eq '07030101' or SubBrand eq '02010110' or SubBrand eq '01010107' or " +
                        "SubBrand eq '8402' or SubBrand eq '01020201' or SubBrand eq '1528' or SubBrand eq '1409' or SubBrand eq '04010102' or " +
                        "SubBrand eq '1520' or SubBrand eq '0906' or SubBrand eq '1410' or SubBrand eq '2102' or SubBrand eq '01100101' or " +
                        "SubBrand eq '07040301' or SubBrand eq '1523' or SubBrand eq '01080101' or SubBrand eq '01100202' or SubBrand eq '01020304'" +
                        " or SubBrand eq '0106' or SubBrand eq '01030104' or SubBrand eq '1315' or SubBrand eq '01040102' or SubBrand eq '1507' or " +
                        "SubBrand eq '1512' or SubBrand eq '01030109' or SubBrand eq '1524' or SubBrand eq '04010101' or SubBrand eq '01010103' or " +
                        "SubBrand eq '1905' or SubBrand eq '01020202' or SubBrand eq '1519' or SubBrand eq '01100102' or SubBrand eq '07010102' or " +
                        "SubBrand eq '1314' or SubBrand eq '1108' or SubBrand eq '07040204' or SubBrand eq '07010104' or SubBrand eq '2501' or " +
                        "SubBrand eq '02010108' or SubBrand eq '07040203' or SubBrand eq '07010101' or SubBrand eq '2308' or SubBrand eq '0105' or " +
                        "SubBrand eq '1404' or SubBrand eq '1304' or SubBrand eq '07040201' or SubBrand eq '01070201' or SubBrand eq '8403' or " +
                        "SubBrand eq '0602' or SubBrand eq '01070103' or SubBrand eq '7401' or SubBrand eq '1522' or SubBrand eq '01010102' or " +
                        "SubBrand eq '01030102' or SubBrand eq '01030103' or SubBrand eq '1518' or SubBrand eq '2307' or SubBrand eq '01060102' or " +
                        "SubBrand eq '03010101' or SubBrand eq '01030101' or SubBrand eq '04010103' or SubBrand eq '04010106' or SubBrand eq '01070102' " +
                        "or SubBrand eq '07040401' or SubBrand eq '07040202' or SubBrand eq '04010105' or SubBrand eq '0103' or SubBrand eq '07010103' " +
                        "or SubBrand eq '1318' or SubBrand eq '8401' or SubBrand eq '1104' or SubBrand eq '01050102' or SubBrand eq '1101' " +
                        "or SubBrand eq '01010201' or SubBrand eq '7402' or SubBrand eq '02010101' or SubBrand eq '04010107' or SubBrand eq '1115' or " +
                        "SubBrand eq '01040101' or SubBrand eq '0907' or SubBrand eq '07030201' or SubBrand eq '05010101' or SubBrand eq '1106'");
            }
        });

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
        executor.execute(thread1);
        executor.execute(thread2);


    }

}



class ModelClass implements Serializable{

}
