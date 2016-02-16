package com.ipal.itu.harzindagi.Dao;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.ipal.itu.harzindagi.Entity.Transaction;
import com.ipal.itu.harzindagi.Entity.UserInfo;

import java.util.List;

/**
 * Created by Ali on 1/14/2016.
 */
public class UserInfoDao {

    public void save(String UCNumber, String Username, String Password) {
        UserInfo info = new UserInfo();
        info.setUserInfo(UCNumber, Username, Password);
        info.save();

    }

    /*public List<Transaction> getTransactions() {
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
        UserInfo item = new UserInfo();
        item.delete(UserInfo.class, CID);
    }

    public static List<UserInfo> getAll() {
        return new Select()
                .from(UserInfo.class)
                        //.where("ChildInfo = ?", childInfo.getId())
                .orderBy("Name ASC")
                .execute();
    }

    public static List<UserInfo> getChild(String id) {
        return new Select()
                .from(UserInfo.class)
                .where("Username = ?", id)
                .orderBy("Name ASC")
                .execute();
    }
}
