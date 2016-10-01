package com.ipal.itu.harzindagi.Dao;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;
import com.ipal.itu.harzindagi.Entity.ChildInfo;

import java.util.List;

import static com.google.android.gms.analytics.internal.zzy.c;

/**
 * Created by Ali on 1/14/2016.
 */
public class ChildInfoDao {

    public long save(String book_id,String childID, String name, int gender, String dob,  String motherName,String  guardianName, String CNIC, String phoneNum,long createdTime,String Location,String EpiName,String kidStation,String imageName, String nfcNumber,boolean bookFlag,boolean recordFlag ,String address,String imei,long due_date,long next_visit_date) {
        ChildInfo item = new ChildInfo();
        item.setChildInfo(book_id,childID, name, gender, dob, motherName, guardianName, CNIC, phoneNum, createdTime, Location, EpiName, kidStation, imageName, nfcNumber, bookFlag, recordFlag, address, imei,due_date,next_visit_date);
        item.save(); // to get system generated id we have to save it first
        item.kid_id = item.getId();
        item.mobile_id = item.getId();
        item.save();
        return item.kid_id;

    }
    public void save( ChildInfo item,String name,String cnic,String phoneNum) {

        item.setChildInfo( name ,cnic, phoneNum);
        item.save(); // to get system generated id we have to save it first


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
                item.kid_id = items.get(i).kid_id;
                item.epi_name = items.get(i).epi_name;
                item.guardian_name = items.get(i).guardian_name;
                item.vaccination_date = items.get(i).vaccination_date;
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
                item.next_visit_date = items.get(i).next_visit_date;
                item.image_path = items.get(i).image_path;
                item.record_update_flag = items.get(i).record_update_flag;
                item.image_update_flag = items.get(i).image_update_flag;
                item.book_id = items.get(i).book_id;
               // item.book_update_flag =  items.get(i).book_update_flag;
                item.imei_number =  items.get(i).imei_number;;


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
    public  List<ChildInfo> getByBookNum(String id) {
        return new Select()
                .from(ChildInfo.class)
                .where("book_id = ?", id)
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
    public  static  List<ChildInfo> getByKIdAndIMEI(long id,String imei) {
        return new Select()
                .from(ChildInfo.class)
                .where("kid_id = ?", id).and("imei_number = ?",imei)
                .orderBy("kid_name ASC")
                .execute();
    }
    public  static  List<ChildInfo> getByLocalKId(long id) {
        return new Select()
                .from(ChildInfo.class)
                .where("mobile_id = ?", id)
                .orderBy("kid_name ASC")
                .execute();
    }
    public  static  List<ChildInfo> getByLocalKIdandIMEI(long id,String imei) {
        return new Select()
                .from(ChildInfo.class)
                .where("mobile_id = ?", id).and("imei_number = ?",imei)
                .orderBy("kid_name ASC")
                .execute();
    }
    public  static  List<ChildInfo> getByLocalCnicandIMEI(String id,String imei) {
        return new Select()
                .from(ChildInfo.class)
                .where("guardian_cnic = ?", id).and("imei_number = ?",imei)
                .orderBy("kid_name ASC")
                .execute();
    }
    public  static  List<ChildInfo> getByLocalPhoneandIMEI(String id,String imei) {
        return new Select()
                .from(ChildInfo.class)
                .where("phone_number = ?", id).and("imei_number = ?",imei)
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
    public  static List<ChildInfo> getByEpiNumAndIMEI(String epi_number,String imei) {
        return new Select()
                .from(ChildInfo.class)
                .where("epi_number = ?", epi_number).and("imei_number = ?",imei)
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
                .where("? >= next_due_date and ? <= next_visit_date",curr_date,curr_date)

                .execute();

    }

    public List<ChildInfo> getDefaulter(long curr_date){

        return new Select()
                .from(ChildInfo.class)
                .where("? > next_visit_date",curr_date)

                .execute();
    }

    public List<ChildInfo> getCompleted(long curr_date){

        return new Select()
                .from(ChildInfo.class)
                .where("next_due_date > ?",curr_date)

                .execute();
    }

    /* public List<ChildInfo> getToday(long curr_date){

        return new Select()
                .from(ChildInfo.class)
                .where("next_due_date >? and next_due_date < ?",curr_date-((long)(86400000)*5),curr_date+((long)(86400000)*5))

                .execute();

    }

    public List<ChildInfo> getDefaulter(long curr_date){

        return new Select()
                .from(ChildInfo.class)
                .where("next_due_date < ? OR next_due_date =?",curr_date-((long)(86400000)*5),curr_date-((long)(86400000)*5))

                .execute();
    }

    public List<ChildInfo> getCompleted(long curr_date){

        return new Select()
                .from(ChildInfo.class)
                .where("next_due_date > ? OR next_due_date =?",curr_date+((long)(86400000)*5),curr_date+((long)(86400000)*5))

                .execute();
    }*/
    public List<ChildInfo> getTodayCompleted(long curr_date){

        return new Select()
                .from(ChildInfo.class)
                .where("vaccination_date =?",curr_date)
                .execute();
    }


    public  void deleteTable(){
        ChildInfo.truncate(ChildInfo.class);
    }
}
