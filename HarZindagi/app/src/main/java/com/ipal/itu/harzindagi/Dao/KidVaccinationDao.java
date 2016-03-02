package com.ipal.itu.harzindagi.Dao;

import android.database.Cursor;
import android.os.Bundle;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Cache;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Entity.Injections;
import com.ipal.itu.harzindagi.Entity.KidVaccinations;
import com.ipal.itu.harzindagi.Entity.Transaction;
import com.ipal.itu.harzindagi.Entity.Vaccinations;

import java.util.Date;
import java.util.List;

/**
 * Created by Ali on 1/14/2016.
 */
public class KidVaccinationDao {

    public void save(String Location, long KidID, int VaccinationID, String Image_, long CreateTime, Boolean Is_Sync) {
        KidVaccinations item = new KidVaccinations();
        item.SetKidVaccinations(Location, KidID, VaccinationID, Image_, CreateTime, Is_Sync);
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

    public static List<KidVaccinations> getById(long id) {
        return new Select()
                .from(KidVaccinations.class)
                .where("mobile_id = ?", id)
                .orderBy("created_timestamp ASC")
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

    public static Bundle get_visit_details_db(long kid) {


        Bundle bnd=new Bundle();
        From query = new Select()
                .from(Vaccinations.class)
                .innerJoin(KidVaccinations.class)
                .on(" Vaccinations._id=KidVaccinations.vaccination_id")
                .where("KidVaccinations.mobile_id =?", kid)
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
                .where("KidVaccinations.mobile_id =?", kid).and("Vaccinations.visit_id =?", max_visit)
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
                .where("KidVaccinations.mobile_id =?", kid)
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
        }else
        {
            str = "0";
        }
        for (int i = 1; i < inj.size(); i++) {

           if (x<vacs.size()){
            if (inj.get(i).id == vacs.get(x).injection_id) {
                str = str+",1";
                x++;


            } else {
                str =str+ ",0";

            }}else
           {
               str = str+",0";


           }

        }


        bnd.putString("vacc_details",str);
        return bnd;
    }
}
