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
    @Column(name = "UCNumber" )
    public String UCNumber;
    @Column(name = "EPICenterName")
    public String EPICenterName;
    @Column(name = "ChildID")
    public String ChildID;
    @Column(name = "Name")
    public String name;
    @Column(name = "Gender")
    public String gender;
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

    public void setChildInfo(String childID, String dob, String gender, String name, String fatherName, String motherName, String CNIC, String phoneNum, String UCNumber, String EPICenterName) {

        this.UCNumber = UCNumber;
        this.EPICenterName = EPICenterName;
        this.ChildID = childID;
        this.name = name;
        this.gender = gender;
        this.fatherName = fatherName;
        this.motherName = motherName;
        this.dob = dob;
        this.cnic = CNIC;
        this.phoneNumber = phoneNum;
    }

    public List<Transaction> transactions() {
        return getMany(Transaction.class, "ChildID");
    }
}
