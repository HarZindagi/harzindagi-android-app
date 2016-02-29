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


    @Column(name = "location")
    public String location;

    @Column(name = "mobile_id")
    public long mobile_id;

    @Column(name = "kid_id")
    public long kid_id;

    @Column(name = "vaccination_id")
    public int vaccination_id;


    @Column(name = "image")
    public String image;


    @Column(name = "created_timestamp")
    public long created_timestamp;


    @Column(name = "is_sync")
    public boolean is_sync;




    public KidVaccinations() {
        super();
    }

    public void SetKidVaccinations(String Location, long KidID, int VaccinationID, String Image_, long CreateTime,Boolean IsSync) {


        this.location = Location;
        this.kid_id = KidID;
        this.vaccination_id = VaccinationID;
        this.image = Image_;
        this.created_timestamp = CreateTime;
        this.is_sync=IsSync;


    }


}
