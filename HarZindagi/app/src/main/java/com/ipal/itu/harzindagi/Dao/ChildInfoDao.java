package com.ipal.itu.harzindagi.Dao;

import com.activeandroid.ActiveAndroid;
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

    public void bulkInsert(List<Transaction> items) {
        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < items.size(); i++) {

                Transaction item = new Transaction();
                item.childID = items.get(i).childID;
                item.date = items.get(i).date;
                item.nextDate = items.get(i).nextDate;
                item.VacID = items.get(i).VacID;
                item.VisitNumb = items.get(i).VisitNumb;
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
    public  void deleteTable(){
        ChildInfo.truncate(ChildInfo.class);
    }
}
