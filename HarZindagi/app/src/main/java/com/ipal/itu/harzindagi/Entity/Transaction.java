package com.ipal.itu.harzindagi.Entity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Ali on 1/14/2016.
 */
@Table(name = "Transaction")
public class Transaction extends Model{
    @Column(name = "ChildID")
    public int childID;
    @Column(name = "VID")
    public int VID;
    @Column(name = "VisitNumb")
    public String VisitNumb;
    @Column(name = "VacID")
    public int VacID;
    @Column(name = "Date")
    public long date;
    @Column(name = "NextDate")
    public long nextDate;
}
