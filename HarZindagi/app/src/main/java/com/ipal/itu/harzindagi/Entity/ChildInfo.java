package com.ipal.itu.harzindagi.Entity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

/**
 * Created by Ali on 1/13/2016.
 */
@Table(name = "ChildInfo")
public class ChildInfo extends Model {


    @Column(name = "_id", index = true, unique = true)
    public int id;


    @Column(name = "epi_number")
    public String epi_number;


    @Column(name = "kid_name")
    public String kid_name;

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


    @Column(name = "created_timestamp")
    public long created_timestamp;


    @Column(name = "location")
    public String location;


    @Column(name = "kids_station")
    public String kids_station;

    @Column(name = "image_name")
    public String image_name;


    @Column(name = "nfc_number")
    public String nfc_number;


    @Column(name = "book_update_flag")
    public boolean book_update_flag;

    @Column(name = "record_update_flag")
    public boolean record_update_flag;

    @Column(name = "next_due_date")
    public long next_due_date;

    @Column(name = "UCNumber")
    public String UCNumber;
    @Column(name = "EPICenterName")
    public String EPICenterName;
    @Column(name = "ChildID")
    public String ChildID;
    @Column(name = "Name")
    public String name;
    @Column(name = "Gender")
    public int gender;
    @Column(name = "FatherName")
    public String fatherName;
    @Column(name = "MotherName")
    public String motherName;
    @Column(name = "DOB")
    public String dob;
    @Column(name = "CNIC")
    public String cnic;
    @Column(name = "PhoneNumber")
    public String phoneNumber;


    public ChildInfo() {
        super();
    }

    public void setChildInfo(String childID, String name, int gender, String dob, String motherName, String guardianName, String CNIC, String phoneNum, long createdTime, String Location, String kidStation, String imageName, String nfcNumber, boolean bookFlag, boolean recordFlag) {

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

        this.kids_station = kidStation;
        this.image_name = imageName;
        this.nfc_number = nfcNumber;
        this.book_update_flag = bookFlag;
        this.record_update_flag = recordFlag;


    }

    public List<Transaction> transactions() {
        return getMany(Transaction.class, "epi_number");
    }
}
