package com.ipal.itu.harzindagi.Entity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

/**
 * Created by Ali on 1/13/2016.
 */
@Table(name = "ChildInfo")
public class ChildInfo  extends Model{
    @Column(name = "Name")
    public String  name;
    @Column(name = "FatherName")
    public String  fatherName;
    @Column(name = "DOB")
    public String  dob;
    @Column(name = "NIC")
    public String  cnic;
    @Column(name = "PhoneNum")
    public String  phoneNum;

    public List<Transaction> transactions() {
        return getMany(Transaction.class, "ChildInfo");
    }
}
