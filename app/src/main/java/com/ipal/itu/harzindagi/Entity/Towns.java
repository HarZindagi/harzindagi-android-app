package com.ipal.itu.harzindagi.Entity;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.ipal.itu.harzindagi.Utils.TruncatableModel;

import java.util.List;

/**
 * Created by Ali on 3/31/2016.
 */
@Table(name = "Towns")
public class Towns extends TruncatableModel {

    @Column(name = "tId")
    public int tId;

    @Column(name = "name")
    public String name;

    public List<Towns> getAll() {
        return new Select()
                .from(Towns.class)
                .orderBy("name ASC")
                .execute();
    }

    public Towns() {
        super();
    }

    public static  void bulkInsert(List<Towns> items) {
        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < items.size(); i++) {

                items.get(i).save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }
    public static void deleteTable(){
        Towns.truncate(Towns.class);
    }
}
