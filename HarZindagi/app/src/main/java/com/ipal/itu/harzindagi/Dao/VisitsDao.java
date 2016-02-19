package com.ipal.itu.harzindagi.Dao;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.ipal.itu.harzindagi.Entity.Transaction;
import com.ipal.itu.harzindagi.Entity.UserInfo;
import com.ipal.itu.harzindagi.Entity.Visit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ali on 2/18/2016.
 */
public class VisitsDao  {
    public void save(int ID, int visitNumber,String description) {
        Visit obj = new Visit();
        obj.setVisit(ID,visitNumber ,description);
        obj.save();

    }
    public static List<Visit> getAll() {
        return new Select()
                .from(Visit.class)
                        //.where("ChildInfo = ?", childInfo.getId())
                .orderBy("_id ASC")
                .execute();
    }
    public static List<Visit> getById(int id) {
        return new Select()
                .from(Visit.class)
                .where("visit_num = ?", id)
                .orderBy("_id ASC")
                .execute();
    }
    public void bulkInsert(ArrayList<Visit> items) {
        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < items.size(); i++) {

                Visit item = new Visit();
                item.id = items.get(i).id;
                item.visit_number = items.get(i).visit_number;
                item.description = items.get(i).description;

                item.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }
}
