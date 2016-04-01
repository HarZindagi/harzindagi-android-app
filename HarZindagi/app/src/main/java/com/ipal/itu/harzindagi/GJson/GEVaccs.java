package com.ipal.itu.harzindagi.GJson;

import java.io.Serializable;

/**
 * Created by Ali on 2/18/2016.
 */
public class GEVaccs implements Serializable {
    public long id;
    public String mobile_id;
    public String imei_number;
    public String kid_name;
    public String location;
    public int vaccination_id;
    public String vaccination_name;
    public boolean is_guest ;
    public String name_of_guest_kid ;
    public String image_path;
    public long created_timestamp;
    public long upload_timestamp;
    public String epi_number;

}
