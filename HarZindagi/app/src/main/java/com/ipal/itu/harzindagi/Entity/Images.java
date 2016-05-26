package com.ipal.itu.harzindagi.Entity;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.ipal.itu.harzindagi.Utils.TruncatableModel;

import java.util.List;

/**
 * Created by Ahmed on 3/31/2016.
 */
@Table(name = "Images")
public class Images extends TruncatableModel {

    @Column(name = "kid_id")
    public int kid_id;

    @Column(name = "name")
    public int name;

    @Column(name = "is_sync")
    public boolean is_sync;

    public List<Images> getAll() {
        return new Select()
                .from(Images.class)
                .execute();
    }
    public  static List<Images> getNotSync() {
        return new Select()
                .from(Images.class)
                .where("is_sync = ?", false)
                .execute();
    }
    public Images() {
        super();
    }
    public  static  List<Images> getById(long id) {
        return new Select()
                .from(Images.class)
                .where("kid_id = ?", id)
                .execute();
    }

    public static void deleteTable(){
        Images.truncate(Images.class);
    }
}
