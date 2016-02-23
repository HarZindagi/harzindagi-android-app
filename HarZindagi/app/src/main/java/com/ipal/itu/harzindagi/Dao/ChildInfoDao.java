package com.ipal.itu.harzindagi.Dao;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.annotation.Column;
import com.activeandroid.query.Select;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Entity.Transaction;
import com.ipal.itu.harzindagi.Entity.Vaccinations;

import java.util.List;

/**
 * Created by Ali on 1/14/2016.
 */
public class ChildInfoDao {

    public void save(String childID, String name, int gender, String dob,  String motherName,String  guardianName, String CNIC, String phoneNum,long createdTime,String Location,String EpiName,String kidStation,String imageName, String nfcNumber,boolean bookFlag,boolean recordFlag ) {
        ChildInfo item = new ChildInfo();
        item.setChildInfo(childID,name,gender,dob,motherName,guardianName,CNIC,phoneNum,createdTime,Location,EpiName,kidStation,imageName,nfcNumber,bookFlag,recordFlag );
        item.save();

    }

    public void save(ChildInfo info) {
        ChildInfo item = new ChildInfo();
        item.setChildInfo(info.epi_number, info.kid_name, info.gender, info.date_of_birth, info.mother_name, info.guardian_name, info.guardian_cnic, info.phone_number, info.created_timestamp, info.location,info.epi_name,info.kids_station ,info.image_name ,info.nfc_number ,info.book_update_flag,info.record_update_flag );
        item.save();
    }

  /*  public List<Transaction> getTransactions() {
        ChildInfo item = new ChildInfo();
        return item.transactions();
    }*/

    public void bulkInsert(List<ChildInfo> items) {
        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < items.size(); i++) {

                ChildInfo item = new ChildInfo();
                item.id = items.get(i).id;
                item.epi_name = items.get(i).epi_name;
                item.kid_name = items.get(i).kid_name;
                item.epi_number = items.get(i).epi_number;
                item.child_address = items.get(i).child_address;

                item.date_of_birth = items.get(i).date_of_birth;
                item.gender = items.get(i).gender;
                item.location = items.get(i).location;
                item.guardian_cnic = items.get(i).guardian_cnic;
                item.phone_number = items.get(i).phone_number;
                item.mother_name = items.get(i).mother_name;
                item.next_due_date = items.get(i).next_due_date;


                item.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    public void deleteItem(int CID) {
        ChildInfo item = new ChildInfo();
        item.delete(ChildInfo.class, CID);
    }

    public  List<ChildInfo> getAll() {
        return new Select()
                .from(ChildInfo.class)
                        //.where("ChildInfo = ?", childInfo.getId())
                .orderBy("kid_name ASC")
                .execute();
    }
    public  List<ChildInfo> getById(String id) {
        return new Select()
                .from(ChildInfo.class)
                .where("epi_number = ?", id)
                .orderBy("kid_name ASC")
                .execute();
    }


    public  List<ChildInfo> getById(String id,String phone,String cnic) {
        return new Select()
                .from(ChildInfo.class)
                .where("epi_number = ?", id).or("phone_number", phone).or("guardian_cnic", cnic)
                .orderBy("kid_name ASC")
                .execute();
    }
    public  void deleteTable(){
        ChildInfo.truncate(ChildInfo.class);
    }
}
