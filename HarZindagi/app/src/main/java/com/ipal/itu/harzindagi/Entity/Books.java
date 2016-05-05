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
@Table(name = "Books")
public class Books extends TruncatableModel {

    @Column(name = "bookId")
    public int bookId;

    @Column(name = "epi")
    public String epi;

    @Column(name = "date")
    public long date;

    @Column(name = "book_update_flag")
    public boolean book_update_flag;

    public List<Books> getAll() {
        return new Select()
                .from(Books.class)
                .orderBy("epi ASC")
                .execute();
    }

    public Books() {
        super();
    }

    public static  void bulkInsert(List<Books> items) {
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
        Books.truncate(Books.class);
    }
}
