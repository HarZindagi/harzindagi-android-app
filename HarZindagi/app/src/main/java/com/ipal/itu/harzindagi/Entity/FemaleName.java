package com.ipal.itu.harzindagi.Entity;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.ipal.itu.harzindagi.Utils.TruncatableModel;

import java.util.List;

/**
 * Created by Ahmed on 3/31/2016.
 */
@Table(name = "FemaleName")
public class FemaleName extends TruncatableModel {

    @Column(name = "name")
    public String name;

    public  static void bulkInsert(List<FemaleName> items) {
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

    public static  List<FemaleName> getAll() {
        return new Select()
                .from(FemaleName.class)
                .execute();
    }

    public FemaleName() {
        super();
    }


    public static void deleteTable(){
        FemaleName.truncate(FemaleName.class);
    }
}
