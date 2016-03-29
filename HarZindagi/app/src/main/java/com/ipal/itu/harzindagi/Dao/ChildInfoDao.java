package com.ipal.itu.harzindagi.Dao;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;
import com.ipal.itu.harzindagi.Entity.ChildInfo;

import java.util.List;

/**
 * Created by Ali on 1/14/2016.
 */
public class ChildInfoDao {

    public void save(String childID, String name, int gender, String dob,  String motherName,String  guardianName, String CNIC, String phoneNum,long createdTime,String Location,String EpiName,String kidStation,String imageName, String nfcNumber,boolean bookFlag,boolean recordFlag ,String address) {
        ChildInfo item = new ChildInfo();
        item.setChildInfo(childID,name,gender,dob,motherName,guardianName,CNIC,phoneNum,createdTime,Location,EpiName,kidStation,imageName,nfcNumber,bookFlag,recordFlag ,address);
        item.save();
        item.mobile_id = item.getId();
        item.save();

    }

    public void save(ChildInfo info) {
        ChildInfo item = new ChildInfo();
        item.setChildInfo(info.epi_number, info.kid_name, info.gender, info.date_of_birth, info.mother_name, info.guardian_name, info.guardian_cnic, info.phone_number, info.created_timestamp, info.location,info.epi_name,info.kids_station ,info.image_path,info.nfc_number ,info.book_update_flag,info.record_update_flag ,info.child_address);
        item.save();
    }

    public void update(int childId){
        new Update(ChildInfo.class)
                .set("record_update_flag",true)
                .where("_id = ?", childId)
                .execute();
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
                item.mobile_id = items.get(i).mobile_id;
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
                item.image_path = items.get(i).image_path;
                item.record_update_flag = items.get(i).record_update_flag;
                item.book_update_flag =  items.get(i).book_update_flag;;


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
    public  List<ChildInfo> getByEPINum(String id) {
        return new Select()
                .from(ChildInfo.class)
                .where("epi_number = ?", id)
                .orderBy("kid_name ASC")
                .execute();
    }
    public  static  List<ChildInfo> getById(long id) {
        return new Select()
                .from(ChildInfo.class)
                .where("mobile_id = ?", id)
                .orderBy("kid_name ASC")
                .execute();
    }
    public  static  List<ChildInfo> getByKId(long id) {
        return new Select()
                .from(ChildInfo.class)
                .where("kid_id = ?", id)
                .orderBy("kid_name ASC")
                .execute();
    }
    public  static List<ChildInfo> getNotSync() {
        return new Select()
                .from(ChildInfo.class)
                .where("record_update_flag = ?", false)
                .orderBy("kid_name ASC")
                .execute();
    }
    public  static List<ChildInfo> getImageNotSync() {
        return new Select()
                .from(ChildInfo.class)
                .where("image_update_flag = ?", false)
                .orderBy("kid_name ASC")
                .execute();
    }
    public  static List<ChildInfo> getByEpiNum(String epi_number) {
        return new Select()
                .from(ChildInfo.class)
                .where("epi_number = ?", epi_number)
                .orderBy("kid_name ASC")
                .execute();
    }
    public  List<ChildInfo> getByCnic( String cnic) {

        return new Select()
                .from(ChildInfo.class)
                .where("guardian_cnic = ?", cnic)
                .orderBy("kid_name ASC")
                .execute();
    }
    public  List<ChildInfo> getByEPIPhone( String phone) {

        return new Select()
                .from(ChildInfo.class)
                .where("phone_number = ?", phone)
                .orderBy("kid_name ASC")
                .execute();
    }
    public List<ChildInfo> getToday(long curr_date){

        return new Select()
                .from(ChildInfo.class)
                .where("next_due_date >? and next_due_date < ?",curr_date-((86400000)*5),curr_date+((86400000)*5))
                .orderBy("kid_name ASC")
                .execute();

    }

    public List<ChildInfo> getDefaulter(long curr_date){

        return new Select()
                .from(ChildInfo.class)
                .where("next_due_date < ? OR next_due_date =?",curr_date-((86400000)*5),curr_date-((86400000)*5))
                .orderBy("kid_name ASC")
                .execute();
    }

    public List<ChildInfo> getCompleted(long curr_date){

        return new Select()
                .from(ChildInfo.class)
                .where("next_due_date > ? OR next_due_date =?",curr_date+((86400000)*5),curr_date+((86400000)*5))
                .orderBy("kid_name ASC")
                .execute();
    }
    public  void deleteTable(){
        ChildInfo.truncate(ChildInfo.class);
    }
}
