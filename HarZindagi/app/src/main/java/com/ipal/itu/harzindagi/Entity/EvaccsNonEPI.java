package com.ipal.itu.harzindagi.Entity;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.ipal.itu.harzindagi.Utils.TruncatableModel;

/**
 * Created by Ali on 3/31/2016.
 */
@Table(name = "evacs_nonepi")
public class EvaccsNonEPI extends TruncatableModel {

    @Column(name = "imei_number")
    public String imei_number;

    @Column(name = "location")
    public String location;

    @Column(name = "location_source")
    public String location_source;

    @Column(name = "created_timestamp")
    public long created_timestamp;

    @Column(name = "upload_timestamp")
    public long upload_timestamp;

    @Column(name = "child_type")
    public String child_type;

    @Column(name = "name")
    public String name;

    @Column(name = "daily_reg_no")
    public String daily_reg_no;

    @Column(name = "vaccination")
    public String vaccination ;

    @Column(name = "cnic")
    public String cnic ;

    @Column(name = "phone_number")
    public String phone_number ;

    @Column(name = "epi_no")
    public String epi_no;

    @Column(name = "date_of_birth")
    public long date_of_birth ;

    @Column(name = "child_address")
    public String child_address ;

    @Column(name = "birth_place")
    public String birth_place;


    @Column(name = "record_update_flag")
    public boolean record_update_flag;



    public EvaccsNonEPI() {
        super();
    }



}
