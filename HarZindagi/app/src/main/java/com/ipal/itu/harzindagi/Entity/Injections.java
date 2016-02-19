package com.ipal.itu.harzindagi.Entity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Ali on 1/14/2016.
 */
@Table(name = "Injections")
public class Injections extends Model {


    @Column(name = "_id")
    public int id;


    @Column(name = "name")   // it is the auto-increment id from Childinfo
    public String name;

    @Column(name = "description")
    public String description;

    @Column(name = "is_drop")
    public boolean is_drop;

    public Injections() {
        super();
    }

    public void SetInjections(int ID, String Name, String Description,boolean is_drop) {


        this.id = ID;
        this.name = Name;
        this.description = Description;
        this.is_drop = is_drop;


    }


}
