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
    @Column(name = "UCNumber")
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
    @Column(name = "Address")
    public String address;

    public ChildInfo() {
        super();
    }

    public void setChildInfo(String UCNumber, String EPICenterName, String ChildID, String Name, String gender, String FatherName, String motherName, String DOB, String CNIC, String PhoneNumber, String Address) {

        this.UCNumber = UCNumber;
        this.EPICenterName = EPICenterName;
        this.ChildID = ChildID;
        this.name = Name;
        this.gender = gender;
        this.fatherName = FatherName;
        this.motherName = motherName;
        this.dob = DOB;
        this.cnic = CNIC;
        this.phoneNumber = PhoneNumber;
        this.address = Address;
    }

    public List<Transaction> transactions() {
        return getMany(Transaction.class, "ChildID");
    }
}
