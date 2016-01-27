package com.ipal.itu.harzindagi.Entity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

/**
 * Created by Ali on 1/13/2016.
 */
@Table(name = "ChildInfo")
public class ChildInfo  extends Model
{
    @Column(name = "ChildID")
    public String  childID;
    @Column(name = "Name")
    public String  name;
    @Column(name = "FatherName")
    public String  fatherName;
    @Column(name = "DOB")
    public String  dob;
    @Column(name = "CNIC")
    public String  cnic;
    @Column(name = "PhoneNumber")
    public String  phoneNumber;
    @Column(name = "Address")
    public String  address;

    public ChildInfo()
    {
        super();
    }

    public ChildInfo( String ChildID , String Name , String FatherName , String DOB , String CNIC , String PhoneNumber , String Address )
    {
        //super();

        this.childID = ChildID;
        this.name = Name;
        this.fatherName = FatherName;
        this.dob = DOB;
        this.cnic = CNIC;
        this.phoneNumber = PhoneNumber;
        this.address = Address;
    }

    public List<Transaction> transactions() {
        return getMany(Transaction.class, "ChildInfo");
    }
}
