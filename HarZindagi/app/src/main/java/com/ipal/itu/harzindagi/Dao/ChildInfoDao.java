package com.ipal.itu.harzindagi.Dao;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Entity.Transaction;

import java.util.List;

/**
 * Created by Ali on 1/14/2016.
 */
public class ChildInfoDao {

    public  void save(String name,String fatherName,String CNIC,String phoneNum){
        ChildInfo item = new ChildInfo();
        item.name = name;
        item.fatherName = fatherName;
        item.cnic = CNIC;
        item.phoneNum = phoneNum;
        item.save();

    }
    public List<Transaction> getTransactions(){
        ChildInfo item = new ChildInfo();
        return  item.transactions();
    }

    public  void bulkInsert(List<Transaction> items){
        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < items.size(); i++) {

                Transaction item =new Transaction();
                item.CID = items.get(i).CID;
                item.date = items.get(i).date;
                item.nextDate = items.get(i).nextDate;
                item.VacID = items.get(i).VacID;
                item.VisitNumb = items.get(i).VisitNumb;
                item.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        }
        finally {
            ActiveAndroid.endTransaction();
        }
    }
    public  void deleteItem(int CID){
        ChildInfo item = new ChildInfo();
        item.delete(ChildInfo.class, CID);
    }
    public static List<ChildInfo> getAll(ChildInfo childInfo) {
        return new Select()
                .from(ChildInfo.class)
                .where("ChildInfo = ?", childInfo.getId())
                .orderBy("Name ASC")
                .execute();
    }
}
