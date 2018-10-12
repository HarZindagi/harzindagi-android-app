package com.ipal.itu.harzindagi.Dao;

import android.database.Cursor;
import android.os.Bundle;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Cache;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.ipal.itu.harzindagi.Entity.Injections;
import com.ipal.itu.harzindagi.Entity.KidVaccinations;
import com.ipal.itu.harzindagi.Entity.Vaccinations;
import com.ipal.itu.harzindagi.GJson.GVaccination;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ali on 1/14/2016.
 */
public class KidVaccinationDao {

    public static List<KidVaccinations> getById(long id) {
        return new Select()
                .from(KidVaccinations.class)
                .where("kid_id = ?", id)
                .orderBy("created_timestamp ASC")
                .execute();
    }
    public static List<KidVaccinations> getVacByIdAndVacId(long id,int  vacId) {
        return new Select()
                .from(KidVaccinations.class)
                .where("kid_id = ?", id).and("vaccination_id =?",vacId)
                .orderBy("created_timestamp ASC")
                .execute();
    }
    public static List<KidVaccinations> getAllVacByKidId(long id) {
        return new Select()
                .from(KidVaccinations.class)
                .where("kid_id = ?", id)
                .orderBy("created_timestamp ASC")
                .execute();
    }
    public static Bundle get_visit_details_db(long kid,boolean isSync) {


        Bundle bnd = new Bundle();
        From query = new Select()
                .from(Vaccinations.class)
                .innerJoin(KidVaccinations.class)
                .on(" Vaccinations._id=KidVaccinations.vaccination_id")
                .where("KidVaccinations.kid_id =?", kid).and("KidVaccinations.is_sync=?",isSync)
                .orderBy("Vaccinations.visit_id DESC");


        Cursor cursor = Cache.openDatabase().rawQuery(query.toSql(), query.getArguments());
        int max_visit = 0;
        if (cursor.moveToFirst()) {

            max_visit = cursor.getInt(cursor.getColumnIndex("visit_id"));
        }

        bnd.putString("visit_num", max_visit + "");

   /*     From query2 = new Select()
                .from(Injections.class)
                .leftJoin(Vaccinations.class)
                .on(" Injections._id=Vaccinations.injection_id")
                .leftJoin(KidVaccinations.class)
                .on(" Vaccinations._id=KidVaccinations.vaccination_id")
                .where("KidVaccinations.kid_id =?", kid).and("Vaccinations.visit_id =?", max_visit)
                .orderBy("Injections._id");*/


        List<Injections> inj = new Select()
                .from(Injections.class)
                .innerJoin(Vaccinations.class)
                .on(" Injections._id=Vaccinations.injection_id")
                .where("Vaccinations.visit_id =?", max_visit)
                .orderBy("Vaccinations._id")
                .execute();

        List<Vaccinations> vacs = new Select().distinct()
                .from(Vaccinations.class)
                .innerJoin(KidVaccinations.class)
                .on(" Vaccinations._id=KidVaccinations.vaccination_id")
                .where("KidVaccinations.kid_id =?", kid)
                .and("Vaccinations.visit_id =?", max_visit)
                .orderBy("Vaccinations._id")
                .execute();


        String str = "";

        for (int i = 0; i < inj.size(); i++) {
            boolean isFound=false;
            for (int j = 0; j <vacs.size() ; j++) {
                if (inj.get(i).id == vacs.get(j).injection_id) {
                    isFound =true;
                    break;
                }
            }
            if(isFound){
                if(i==0){
                    str = str + "1";
                }else{
                    str = str + ",1";
                }
            }else{
                if(i==0){
                    str = str + "0";
                }else{
                    str = str + ",0";
                }
            }

        }
        ArrayList<GVaccination> gVaccinations = new ArrayList<>();
        for (int i = 0; i <vacs.size() ; i++) {
            GVaccination gVaccination = new GVaccination();
            gVaccination.injection_id = vacs.get(i).injection_id;
            gVaccinations.add(gVaccination);
        }
        bnd.putSerializable("vacs",gVaccinations);
        bnd.putString("vacc_details", str);
        return bnd;
    }
    public static Bundle get_visit_details_db(long kid) {


        Bundle bnd = new Bundle();
        From query = new Select()
                .from(Vaccinations.class)
                .innerJoin(KidVaccinations.class)
                .on(" Vaccinations._id=KidVaccinations.vaccination_id")
                .where("KidVaccinations.kid_id =?", kid)
                .orderBy("Vaccinations.visit_id DESC");


        Cursor cursor = Cache.openDatabase().rawQuery(query.toSql(), query.getArguments());
        int max_visit = 0;
        if (cursor.moveToFirst()) {

            max_visit = cursor.getInt(cursor.getColumnIndex("visit_id"));
        }

        bnd.putString("visit_num", max_visit + "");

   /*     From query2 = new Select()
                .from(Injections.class)
                .leftJoin(Vaccinations.class)
                .on(" Injections._id=Vaccinations.injection_id")
                .leftJoin(KidVaccinations.class)
                .on(" Vaccinations._id=KidVaccinations.vaccination_id")
                .where("KidVaccinations.kid_id =?", kid).and("Vaccinations.visit_id =?", max_visit)
                .orderBy("Injections._id");*/


        List<Injections> inj = new Select()
                .from(Injections.class)
                .innerJoin(Vaccinations.class)
                .on(" Injections._id=Vaccinations.injection_id")
                .where("Vaccinations.visit_id =?", max_visit)
                .orderBy("Vaccinations._id")
                .execute();

        List<Vaccinations> vacs = new Select().distinct()
                .from(Vaccinations.class)
                .innerJoin(KidVaccinations.class)
                .on(" Vaccinations._id=KidVaccinations.vaccination_id")
                .where("KidVaccinations.kid_id =?", kid)
                .and("Vaccinations.visit_id =?", max_visit)
                .orderBy("Vaccinations._id")
                .execute();


        String str = "";

        for (int i = 0; i < inj.size(); i++) {
            boolean isFound=false;
            for (int j = 0; j <vacs.size() ; j++) {
                if (inj.get(i).id == vacs.get(j).injection_id) {
                    isFound =true;
                    break;
                }
            }
            if(isFound){
                if(i==0){
                    str = str + "1";
                }else{
                    str = str + ",1";
                }
            }else{
                if(i==0){
                    str = str + "0";
                }else{
                    str = str + ",0";
                }
            }

        }
        ArrayList<GVaccination> gVaccinations = new ArrayList<>();
        for (int i = 0; i <vacs.size() ; i++) {
            GVaccination gVaccination = new GVaccination();
            gVaccination.injection_id = vacs.get(i).injection_id;
            gVaccinations.add(gVaccination);
        }
        bnd.putSerializable("vacs",gVaccinations);
        bnd.putString("vacc_details", str);
        return bnd;
    }
    public static Bundle get_visit_details_db_old(long kid) {


        Bundle bnd = new Bundle();
        From query = new Select()
                .from(Vaccinations.class)
                .innerJoin(KidVaccinations.class)
                .on(" Vaccinations._id=KidVaccinations.vaccination_id")
                .where("KidVaccinations.kid_id =?", kid)
                .orderBy("Vaccinations.visit_id DESC");


        Cursor cursor = Cache.openDatabase().rawQuery(query.toSql(), query.getArguments());
        int max_visit = 0;
        if (cursor.moveToFirst()) {

            max_visit = cursor.getInt(cursor.getColumnIndex("visit_id"));
        }

        bnd.putString("visit_num", max_visit + "");

        From query2 = new Select()
                .from(Injections.class)
                .leftJoin(Vaccinations.class)
                .on(" Injections._id=Vaccinations.injection_id")
                .leftJoin(KidVaccinations.class)
                .on(" Vaccinations._id=KidVaccinations.vaccination_id")
                .where("KidVaccinations.kid_id =?", kid).and("Vaccinations.visit_id =?", max_visit)
                .orderBy("Injections._id");


        List<Injections> inj = new Select()
                .from(Injections.class)
                .innerJoin(Vaccinations.class)
                .on(" Injections._id=Vaccinations.injection_id")
                .where("Vaccinations.visit_id =?", max_visit)
                .orderBy("Vaccinations._id")
                .execute();

        List<Vaccinations> vacs = new Select().distinct()
                .from(Vaccinations.class)
                .innerJoin(KidVaccinations.class)
                .on(" Vaccinations._id=KidVaccinations.vaccination_id")
                .where("KidVaccinations.kid_id =?", kid)
                .and("Vaccinations.visit_id =?", max_visit)
                .orderBy("Vaccinations._id")
                .execute();


        String str = "";
        int x = 0;
        if (vacs.size() > 0) {
            if (inj.get(0).id == vacs.get(0).injection_id) {
                str = "1";
                x++;


            } else {
                str = "0";

            }
        } else {
            str = "0";
        }
        for (int i = 1; i < inj.size(); i++) {

            if (x < vacs.size()) {
                if (inj.get(i).id == vacs.get(x).injection_id) {
                    str = str + ",1";
                    x++;


                } else {
                    str = str + ",0";

                }
            } else {
                str = str + ",0";


            }

        }


        bnd.putString("vacc_details", str);
        return bnd;
    }

    public void save(String Location, long KidID, int VaccinationID, String Image_, long CreateTime, Boolean Is_Sync,String imei) {
        KidVaccinations item = new KidVaccinations();
        item.SetKidVaccinations(Location, KidID, VaccinationID, Image_, CreateTime, Is_Sync,imei,"");
        item.save();

    }
    public void save(String Location, long KidID, int VaccinationID, String Image_, long CreateTime, Boolean Is_Sync,String imei,String guest_imei) {
        KidVaccinations item = new KidVaccinations();
        item.SetKidVaccinations(Location, KidID, VaccinationID, Image_, CreateTime, Is_Sync,imei,guest_imei);
        item.save();

    }
    public void deleteItem(int CID) {
        KidVaccinations item = new KidVaccinations();
        item.delete(KidVaccinations.class, CID);
    }

    public List<KidVaccinations> getAll() {
        return new Select()
                .from(KidVaccinations.class)
                        //.where("KidVaccinations = ?", KidVaccinations.getId())
                .orderBy("_id ASC")
                .execute();
    }

    public List<KidVaccinations> getNoSync() {
        return new Select()
                .from(KidVaccinations.class)
                .where("is_sync = ?", false)
                .orderBy("created_timestamp ASC")
                .execute();
    }


    public void deleteTable() {
        KidVaccinations.truncate(KidVaccinations.class);
    }

    public void bulkInsert(List<KidVaccinations> items) {
        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < items.size(); i++) {

                KidVaccinations item = new KidVaccinations();
                item.location = items.get(i).location;
                item.kid_id = items.get(i).kid_id;


                item.vaccination_id = items.get(i).vaccination_id;

                item.image = items.get(i).image;

                item.created_timestamp = items.get(i).created_timestamp;

                item.is_sync = items.get(i).is_sync;

                item.imei_number = items.get(i).imei_number;
                item.guest_imei_number = items.get(i).guest_imei_number;

                item.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }
}
