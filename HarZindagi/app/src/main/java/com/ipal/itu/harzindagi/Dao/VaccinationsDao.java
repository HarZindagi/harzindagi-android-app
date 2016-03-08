package com.ipal.itu.harzindagi.Dao;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.ipal.itu.harzindagi.Activities.VaccDetailBook;
import com.ipal.itu.harzindagi.Entity.Injections;
import com.ipal.itu.harzindagi.Entity.Vaccinations;
import com.ipal.itu.harzindagi.GJson.VaccineInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ali on 2/18/2016.
 */
public class VaccinationsDao {
    public static List<Vaccinations> getById(int id) {
        return new Select()
                .from(Vaccinations.class)
                .where("visit_id = ?", id)
                .orderBy("_id ASC")
                .execute();
    }

    public static List<Integer> get_VaccinationID_Vaccs_details(int v_num, String inj, VaccDetailBook vdb) {

        String[] injarr = inj.split(",");
        List<Integer> arr = new ArrayList<>();
        List<Vaccinations> vc = new Select()
                .from(Vaccinations.class)
                .where("visit_id = ?", v_num)
                .orderBy("_id ASC")
                .execute();
        List<Injections> lij = new ArrayList<>();


        for (int i = 0; i < vc.size(); i++) {
            VaccineInfo VI=new VaccineInfo();
            List<Injections> ij = new Select()
                    .from(Injections.class)
                    .where("_id = ?", vc.get(i).injection_id)
                    .execute();

            VI.vac_name=ij.get(0).name;
            VI.vac_type=ij.get(0).is_drop;
            vdb.vaccinfo.add(VI);

            lij.add(ij.get(0));

        }

        int x = 0;
        for (int i = 0; i < lij.size() && i < injarr.length; i++) {
            if (injarr[i].equals("1")) {
                if (vc.get(i).injection_id == lij.get(i).id) {
                    arr.add(vc.get(i).id);


                }


            }


        }


        return arr;

    }

    public void save(int ID, int visitNumber, int injectionID) {
        Vaccinations obj = new Vaccinations();
        obj.SetKidVaccinations(ID, visitNumber, injectionID);
        obj.save();

    }

    public List<Vaccinations> getAll() {
        return new Select()
                .from(Vaccinations.class)
                .orderBy("_id ASC")
                .execute();
    }

    public void bulkInsert(List<Vaccinations> items) {
        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < items.size(); i++) {

                Vaccinations item = new Vaccinations();
                item.id = items.get(i).id;
                item.visit_id = items.get(i).visit_id;
                item.injection_id = items.get(i).injection_id;

                item.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }

    public void deleteTable() {
        Vaccinations.truncate(Vaccinations.class);
    }
}
