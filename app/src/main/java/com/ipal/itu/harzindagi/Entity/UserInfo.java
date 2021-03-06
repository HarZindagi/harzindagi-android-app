package com.ipal.itu.harzindagi.Entity;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.ipal.itu.harzindagi.Utils.TruncatableModel;

import java.util.List;

/**
 * Created by Ali on 1/13/2016.
 */
@Table(name = "UserInfo")
public class UserInfo extends TruncatableModel {
    @Column(name = "UCNumber")
    public String UCNumber;
    @Column(name = "GUserInfo")
    public String Username;
    @Column(name = "Password")
    public String Password;

    public UserInfo() {
        super();
    }

    public void setUserInfo(String UCNumber, String Username, String Password) {

        this.UCNumber = UCNumber;
        this.Username = Username;
        this.Password = Password;
    }


}
