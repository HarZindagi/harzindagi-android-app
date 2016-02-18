package com.ipal.itu.harzindagi.Entity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Ali on 1/14/2016.
 */
@Table(name = "Vaccinations")
public class Vaccinations extends Model {


    @Column(name = "_id")
    public int id;


    @Column(name = "visit_id")   // it is the auto-increment id from Childinfo
    public int visit_id;

    @Column(name = "injection_id")
    public int injection_id;


    public Vaccinations() {
        super();
    }

    public void SetKidVaccinations(int ID, int visitNumber, int injectionID) {


        this.id = ID;
        this.visit_id = visitNumber;
        this.injection_id = injectionID;


    }


}
