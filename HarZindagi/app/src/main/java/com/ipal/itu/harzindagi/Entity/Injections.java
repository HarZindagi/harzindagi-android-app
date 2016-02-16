package com.ipal.itu.harzindagi.Entity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Ali on 1/14/2016.
 */
@Table(name = "Injections")
public class Injections extends Model{


    @Column(name="_id")
    public int id;





    @Column(name="name")   // it is the auto-increment id from Childinfo
    public String name;

    @Column(name="description")
    public String description;









    public Injections() {
        super();
    }

    public void SetKidVaccinations(int ID,String Name,String Description)
    {


            this.id=ID;
            this.name =Name ;
            this.description=Description;




    }




}
