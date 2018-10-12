package com.ipal.itu.harzindagi.Entity;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.ipal.itu.harzindagi.Utils.TruncatableModel;

/**
 * Created by Ali on 3/31/2016.
 */
@Table(name = "evacs_epi")
public class Evaccs extends TruncatableModel {

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

    @Column(name = "epi_number")
    public String epi_number;

    @Column(name = "vaccination")
    public String vaccination ;
    @Column(name = "record_update_flag")
    public boolean record_update_flag;
    public Evaccs() {
        super();
    }



}
