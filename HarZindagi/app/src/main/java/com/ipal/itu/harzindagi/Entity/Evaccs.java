package com.ipal.itu.harzindagi.Entity;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.ipal.itu.harzindagi.Utils.TruncatableModel;

/**
 * Created by Ali on 3/31/2016.
 */
@Table(name = "Evaccs")
public class Evaccs extends TruncatableModel {


    @Column(name = "epi_number")
    public String epi_number;

    @Column(name = "kid_name")
    public String kid_name;

    @Column(name = "vacc_id")
    public String vacc_id;

    @Column(name = "vacc_name")
    public String vacc_name ;

    @Column(name = "is_guest")
    public int is_guest ;


    @Column(name = "name_of_guest_kid")
    public String name_of_guest_kid ;


    @Column(name = "location")
    public String location;


    @Column(name = "image_path")
    public String image_path;


    @Column(name = "record_update_flag")
    public boolean record_update_flag;

    @Column(name = "image_update_flag")
    public boolean image_update_flag;


    @Column(name = "imei_number")
    public String imei_number;

    @Column(name = "created_timestamp")
    public long created_timestamp;

    @Column(name = "upload_timestamp")
    public long upload_timestamp;

    public Evaccs() {
        super();
    }



}
