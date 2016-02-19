package com.ipal.itu.harzindagi.Dao;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Select;
import com.ipal.itu.harzindagi.Entity.Injections;
import com.ipal.itu.harzindagi.Entity.Vaccinations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ali on 2/18/2016.
 */
public class InjectionsDao {
    public void save(int ID, String name, String description,boolean is_drop) {
        Injections obj = new Injections();
        obj.SetInjections(ID, name,description,is_drop);
        obj.save();

    }
    public static List<Injections> getAll() {
        return new Select()
                .from(Injections.class)
                        //.where("ChildInfo = ?", childInfo.getId())
                .orderBy("_id ASC")
                .execute();
    }
    public static List<Injections> getById(int id) {
        return new Select()
                .from(Injections.class)
                .where("_id = ?", id)
                .orderBy("_id ASC")
                .execute();
    }
    public static List<Injections> getInjectionsByVisit(int v_id)
    {
        List<Vaccinations> vc= new Select()
                .from(Vaccinations.class)
                .where("visit_id = ?", v_id)
                .orderBy("visit_id ASC")
                .execute();


        List<Injections> lij=new ArrayList<>();


        for(int i=0;i<vc.size();i++)
        {
          List  <Injections> ij=new Select()
                    .from(Injections.class)
                    .where("_id = ?", vc.get(i).injection_id)
                    .execute();

                lij.add(ij.get(0));

        }
        return lij;

    }
    public void bulkInsert(ArrayList<Injections> items) {
        ActiveAndroid.beginTransaction();
        try {
            for (int i = 0; i < items.size(); i++) {

                Injections item = new Injections();
                item.id = items.get(i).id;
                item.name = items.get(i).name;
                item.description = items.get(i).description;
                item.is_drop = items.get(i).is_drop;
                item.save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }
    }
}
