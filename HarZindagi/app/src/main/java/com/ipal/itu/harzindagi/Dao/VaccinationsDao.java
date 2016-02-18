package com.ipal.itu.harzindagi.Dao;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.ipal.itu.harzindagi.Entity.Vaccinations;

import java.util.List;

/**
 * Created by Ali on 2/18/2016.
 */
public class VaccinationsDao  {
    public void save(int ID, int visitNumber, int injectionID) {
        Vaccinations obj = new Vaccinations();
        obj.SetKidVaccinations(ID, visitNumber, injectionID);
        obj.save();

    }
    public  List<Vaccinations> getAll() {
        return new Select()
                .from(Vaccinations.class)
                .orderBy("_id ASC")
                .execute();
    }
    public static List<Vaccinations> getById(int id) {
        return new Select()
                .from(Vaccinations.class)
                .where("visit_id = ?", id)
                .orderBy("_id ASC")
                .execute();
    }
    public void bulkInsert(List<Vaccinations> items) {
        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < items.size(); i++) {

                Vaccinations item = new Vaccinations();
                item.id = items.get(i).id;
                item.visit_id = items.get(i).visit_id;
                item.injection_id = items.get(i).injection_id;

                item.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }
}
