package com.ipal.itu.harzindagi.Entity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.ipal.itu.harzindagi.Utils.TruncatableModel;

/**
 * Created by Ali on 1/14/2016.
 */
@Table(name = "KidVaccinations")
public class KidVaccinations extends TruncatableModel {


    @Column(name = "_id", index = true, unique = true)
    public int id;


    @Column(name = "location")
    public String location;


    @Column(name = "kid_id")   // it is the auto-increment id from Childinfo
    public int kid_id;

    @Column(name = "vaccination_id")
    public int vaccination_id;


    @Column(name = "image")
    public String image;


    @Column(name = "created_timestamp")
    public long created_timestamp;





    public KidVaccinations() {
        super();
    }

    public void SetKidVaccinations(String Location, int KidID, int VaccinationID, String Image_, long CreateTime) {


        this.location = Location;
        this.kid_id = KidID;
        this.vaccination_id = VaccinationID;
        this.image = Image_;
        this.created_timestamp = CreateTime;



    }


}
