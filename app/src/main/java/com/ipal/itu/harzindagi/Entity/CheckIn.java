package com.ipal.itu.harzindagi.Entity;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.ipal.itu.harzindagi.Utils.TruncatableModel;

import java.util.List;

/**
 * Created by Ahmed on 3/31/2016.
 */
@Table(name = "CheckIn")
public class CheckIn extends TruncatableModel {

    @Column(name = "is_sync")
    public boolean is_sync;



    @Column(name = "location")
    public String location;



    @Column(name = "created_timestamp")
    public String created_timestamp;


    public static List<CheckIn> getAll() {
        return new Select()
                .from(CheckIn.class)
                .execute();
    }
    public  static List<CheckIn> getNotSync() {
        return new Select()
                .from(CheckIn.class)
                .where("is_sync = ?", false)
                .execute();
    }
    public CheckIn() {
        super();
    }
    public  static  List<CheckIn> getByBookId(long id) {
        return new Select()
                .from(CheckIn.class)
                .where("book_number = ?", id)
                .execute();
    }

    public static void deleteTable(){
        CheckIn.truncate(CheckIn.class);
    }
}
