package com.ipal.itu.harzindagi.Entity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

/**
 * Created by Ali on 1/14/2016.
 */
@Table(name = "Visit")
public class Visit extends Model{


    @Column(name = "_id")
    public int id;


    @Column(name = "visit_num")
    public int visit_number;

    @Column(name = "description")
    public String description;

    public Visit() {
        super();
    }

    public void setVisit(int ID, int visitNumber,String description) {

        this.id = ID;
        this.visit_number = visitNumber;
        this.description = description;

    }


}
