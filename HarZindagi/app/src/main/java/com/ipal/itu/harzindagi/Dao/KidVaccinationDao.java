package com.ipal.itu.harzindagi.Dao;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.ipal.itu.harzindagi.Entity.KidVaccinations;
import com.ipal.itu.harzindagi.Entity.Transaction;
import com.ipal.itu.harzindagi.Entity.Vaccinations;

import java.util.Date;
import java.util.List;

/**
 * Created by Ali on 1/14/2016.
 */
public class KidVaccinationDao {

    public void save(String Location, long KidID, int VaccinationID, String Image_, long CreateTime,Boolean Is_Sync ) {
        KidVaccinations item = new KidVaccinations();
        item.SetKidVaccinations(Location, KidID, VaccinationID, Image_, CreateTime,Is_Sync);
        item.save();

    }




    public void deleteItem(int CID) {
        KidVaccinations item = new KidVaccinations();
        item.delete(KidVaccinations.class, CID);
    }

    public  List<KidVaccinations> getAll() {
        return new Select()
                .from(KidVaccinations.class)
                        //.where("KidVaccinations = ?", KidVaccinations.getId())
                .orderBy("_id ASC")
                .execute();
    }

    public  List<KidVaccinations> getById(String id) {
        return new Select()
                .from(KidVaccinations.class)
                .where("kid_id = ?", id)
                .orderBy("created_timestamp ASC")
                .execute();
    }
    public  List<KidVaccinations> getNoSync() {
        return new Select()
                .from(KidVaccinations.class)
                .where("is_sync = ?", false)
                .orderBy("created_timestamp ASC")
                .execute();
    }
    public  void deleteTable(){
        KidVaccinations.truncate(KidVaccinations.class);
    }


}
