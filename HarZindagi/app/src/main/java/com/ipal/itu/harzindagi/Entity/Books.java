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

    @Column(name = "book_number")
    public int book_number;

    @Column(name = "kid_id")
    public long kid_id;

    @Column(name = "date")
    public long date;

    @Column(name = "is_sync")
    public boolean is_sync;

    public List<Books> getAll() {
        return new Select()
                .from(Books.class)
                .execute();
    }
    public  static List<Books> getNotSync() {
        return new Select()
                .from(Books.class)
                .where("is_sync = ?", false)
                .execute();
    }
    public Books() {
        super();
    }
    public  static  List<Books> getByBookId(long id) {
        return new Select()
                .from(Books.class)
                .where("book_number = ?", id)
                .execute();
    }

    public static void deleteTable(){
        Books.truncate(Books.class);
    }
}
