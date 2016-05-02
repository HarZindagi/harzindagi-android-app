package com.ipal.itu.harzindagi.Dao;

import com.activeandroid.query.Select;
import com.ipal.itu.harzindagi.Entity.Evaccs;
import com.ipal.itu.harzindagi.Entity.EvaccsNonEPI;

import java.util.List;

/**
 * Created by Ali on 4/4/2016.
 */
public class EvaccsNonEPIDao {
  /*  public void save(String epi_number, String kid_name, String vaccination_id, String vaccination_name, int is_guest, String name_of_guest_kid, String location, String image_path, long created_timestamp) {
        Evaccs item = new Evaccs();
        item.epi_number = epi_number;
        item.kid_name = kid_name;
        item.vaccination_id = vaccination_id;
        item.vaccination_name = vaccination_name;
        item.is_guest = is_guest;
        item.name_of_guest_kid = name_of_guest_kid;
        item.location = location;
        item.image_path =image_path;
        item.created_timestamp = created_timestamp;
        item.save();

    }*/

    public List<EvaccsNonEPI> getByEPINum(String epi_number) {
        return new Select()
                .from(EvaccsNonEPI.class)
                .where("epi_no = ?", epi_number)
                .orderBy("name ASC")
                .execute();
    }
    public static List<EvaccsNonEPI> getAll() {
        return new Select()
                .from(EvaccsNonEPI.class)
                .orderBy("name ASC")
                .execute();
    }
    public static List<EvaccsNonEPI> getDistinct() {
        return new Select("epi_no").distinct()
                .from(EvaccsNonEPI.class)
                .orderBy("name ASC")
                .execute();
    }
}
