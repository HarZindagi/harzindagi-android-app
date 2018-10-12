package com.ipal.itu.harzindagi.Dao;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.ipal.itu.harzindagi.Entity.Transaction;
import com.ipal.itu.harzindagi.Entity.UserInfo;
import com.ipal.itu.harzindagi.Entity.Vaccinations;

import java.util.List;

/**
 * Created by Ali on 1/14/2016.
 */

public class UserInfoDao {

    public static List<UserInfo> getAll() {
        return new Select()
                .from(UserInfo.class)
                        //.where("ChildInfo = ?", childInfo.getId())
                .orderBy("UCNumber ASC")
                .execute();
    }

    public static List<UserInfo> getById(String id) {
        return new Select()
                .from(UserInfo.class)
                .where("GUserInfo = ?", id)
                .orderBy("UCNumber ASC")
                .execute();
    }

    public void save(String UCNumber, String Username, String Password) {
        UserInfo info = new UserInfo();
        info.setUserInfo(UCNumber, Username, Password);
        info.save();

    }

    public void deleteItem(int CID) {
        UserInfo item = new UserInfo();
        item.delete(UserInfo.class, CID);
    }
    public  void deleteTable(){
        UserInfo.truncate(UserInfo.class);
    }
}
