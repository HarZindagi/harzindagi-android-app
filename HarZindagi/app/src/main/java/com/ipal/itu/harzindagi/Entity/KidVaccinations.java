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

/*    @Column(name = "mobile_id")
    public long mobile_id;*/

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

    @Column(name = "imei_number")
    public String imei_number;

    @Column(name = "guest_imei_number")
    public String guest_imei_number;


    public KidVaccinations() {
        super();
    }

    public void SetKidVaccinations(String Location, long kid_id, int VaccinationID, String Image_, long CreateTime, Boolean IsSync, String imei,String guest_imei) {


        this.location = Location;

        this.kid_id = kid_id;
        this.vaccination_id = VaccinationID;
        this.image = Image_;
        this.created_timestamp = CreateTime;
        this.is_sync = IsSync;
        this.imei_number = imei;
        this.guest_imei_number = guest_imei;

    }


}
