package com.ipal.itu.harzindagi.Entity;

import com.ipal.itu.harzindagi.GJson.GUserInfo;
import com.ipal.itu.harzindagi.GJson.VaccineInfo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Admin on 10/21/2015.
 */
public class VaccInfoList implements Serializable{
    public ArrayList<VaccineInfo> vaccinfo= new ArrayList<>();
}
