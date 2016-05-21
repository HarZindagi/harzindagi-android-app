package com.ipal.itu.harzindagi.Entity;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.ipal.itu.harzindagi.Utils.TruncatableModel;

/**
 * Created by Ali on 1/13/2016.
 */
@Table(name = "ChildInfo")
public class ChildInfo extends TruncatableModel {


   @Column(name = "mobile_id")
    public Long mobile_id;

    @Column(name = "kid_id")
    public Long kid_id;

    @Column(name = "epi_number")
    public String epi_number;

    @Column(name = "epi_name")
    public String epi_name;


    @Column(name = "kid_name")
    public String kid_name;

    @Column(name = "gender")
    public int gender;


    @Column(name = "date_of_birth")
    public String date_of_birth;


    @Column(name = "mother_name")
    public String mother_name;


    @Column(name = "guardian_name")
    public String guardian_name;


    @Column(name = "guardian_cnic")
    public String guardian_cnic;

    @Column(name = "phone_number")
    public String phone_number;


    @Column(name = "location")
    public String location;


    @Column(name = "kids_station")
    public String kids_station;

    @Column(name = "image_path")
    public String image_path;


    @Column(name = "nfc_number")
    public String nfc_number;


/*    @Column(name = "book_update_flag")
    public boolean book_update_flag;*/

    @Column(name = "record_update_flag")
    public boolean record_update_flag;

    @Column(name = "image_update_flag")
    public boolean image_update_flag;

    @Column(name = "next_due_date")
    public long next_due_date;

    @Column(name = "child_address")
    public String child_address;

    @Column(name = "imei_number")
    public String imei_number;

    @Column(name = "created_timestamp")
    public long created_timestamp;

    @Column(name = "upload_timestamp")
    public long upload_timestamp;

    @Column(name = "book_id")
    public String book_id;

    public ChildInfo() {
        super();
    }

    public void setChildInfo(String book_id,String childID, String name, int gender, String dob, String motherName, String guardianName, String CNIC, String phoneNum, long createdTime, String Location,String EpiName, String kidStation, String imageName, String nfcNumber, boolean bookFlag, boolean recordFlag,String address,String imei) {
       this.imei_number = imei;
        this.epi_number = childID;
        this.kid_name = name;
        this.gender = gender;
        this.date_of_birth = dob;
        this.mother_name = motherName;
        this.guardian_name = guardianName;
        this.guardian_cnic = CNIC;
        this.phone_number = phoneNum;
        this.created_timestamp = createdTime;
        this.location = Location;
        this.epi_name=EpiName;
        this.kids_station = kidStation;
        this.image_path = imageName;
        this.nfc_number = nfcNumber;
       // this.book_update_flag = bookFlag;
        this.record_update_flag = recordFlag;
        this.child_address = address;
        this.book_id =book_id;



    }
    public void setChildInfo(String name,  String CNIC, String phoneNum) {
        this.kid_name = name;
        this.guardian_cnic = CNIC;
        this.phone_number = phoneNum;

    }

}
