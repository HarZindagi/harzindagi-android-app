package com.ipal.itu.harzindagi.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.ipal.itu.harzindagi.Adapters.ChildListAdapter;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Dao.KidVaccinationDao;
import com.ipal.itu.harzindagi.Dao.VaccinationsDao;
import com.ipal.itu.harzindagi.Dao.VisitsDao;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Entity.Injections;
import com.ipal.itu.harzindagi.Entity.KidVaccinations;
import com.ipal.itu.harzindagi.Entity.Vaccinations;
import com.ipal.itu.harzindagi.Entity.Visit;
import com.ipal.itu.harzindagi.GJson.GKidTransactionAry;
import com.ipal.itu.harzindagi.GJson.GVaccinationAry;
import com.ipal.itu.harzindagi.GJson.GVisitAry;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChildrenListActivity extends AppCompatActivity {
    String app_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        app_name = getResources().getString(R.string.app_name);
        boolean fromSMS = getIntent().getBooleanExtra("fromSMS", false);


        if (!fromSMS) {


            if (SearchActivity.data != null) {
                if (SearchActivity.data.size() == 0) {
                    Toast.makeText(this, "No Record Found", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
                ListView listView = (ListView) findViewById(R.id.childrenListActivityListView);
                ChildListAdapter childListAdapter = new ChildListAdapter(this, R.layout.listactivity_row, SearchActivity.data, app_name);
                listView.setAdapter(childListAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                        selectedPosition = position;
                        Intent intent = new Intent(getApplication(), VaccinationActivity.class);
                        long kid = 0;
                        int size = 0;
                        if (SearchActivity.data.get(position).kid_id != null) {
                            kid = SearchActivity.data.get(position).kid_id;
                            size = ChildInfoDao.getByKId(kid).size();
                        } else {
                            kid = SearchActivity.data.get(position).mobile_id;
                            size = ChildInfoDao.getById(kid).size();
                        }

                        if (size != 0) {
                            Bundle bnd = KidVaccinationDao.get_visit_details_db(kid);
                            intent.putExtra("childid", SearchActivity.data.get(position).epi_number);
                            intent.putExtras(bnd);
                            startActivity(intent);
                        }else{
                            getVaccinations(kid);
                        }


                    }
                });
            }
        } else {
            ChildInfoDao dao = new ChildInfoDao();


            String ID = getIntent().getStringExtra("ID");

            String CHILD_NAME = getIntent().getStringExtra("CHILD_NAME");
            if (CHILD_NAME.equals("0")) {
                Toast.makeText(this, "No Record Found", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
            String guardian_name = getIntent().getStringExtra("Guardian_name");
            String Address = getIntent().getStringExtra("Address");
            String VisitNum = getIntent().getStringExtra("VisitNum");
            String VAC_LIST = getIntent().getStringExtra("VAC_LIST");

            final List<ChildInfo> list = new ArrayList<>();
            ChildInfo childInfo = new ChildInfo();
            childInfo.mobile_id = Long.parseLong(ID);
            childInfo.kid_name = CHILD_NAME;
            childInfo.guardian_name = guardian_name;
            childInfo.child_address = Address;

            list.add(childInfo);
            ListView listView = (ListView) findViewById(R.id.childrenListActivityListView);
            ChildListAdapter childListAdapter = new ChildListAdapter(this, R.layout.listactivity_row, list, app_name);
            listView.setAdapter(childListAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                    Intent intent = new Intent(getApplication(), VaccinationActivity.class);
                    long kid = 0;
                    if (list.get(position).kid_id != null) {
                        kid = list.get(position).kid_id;
                    } else {
                        kid = list.get(position).mobile_id;
                    }
                    Bundle bnd = KidVaccinationDao.get_visit_details_db(kid);
                    intent.putExtra("childid", list.get(position).epi_number);
                    intent.putExtras(bnd);
                    startActivity(intent);

                }

            });
        }

    }
    public  void getVaccinations(final long kid){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Constants.vaccinationsItems+ kid + "/by_kid_id?" + "user[auth_token]=" + Constants.getToken(this);
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading Vaccinations");
        pDialog.show();
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.GET,
                url, new JSONObject(),
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        //Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                        //  Log.d(TAG, response.toString());
                        pDialog.hide();
                        parseVaccs(response,kid);

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();

                pDialog.hide();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                return headers;
            }

        };

// Add the request to the RequestQueue.
        queue.add(jsonObjReq);
    }

    private void parseVaccs(JSONArray response,long kid) {

            Gson gson = new Gson();
          GKidTransactionAry gVisitAry = gson.fromJson("{\"kidVaccinations\":" + response.toString() + "}", GKidTransactionAry.class);

            ArrayList<KidVaccinations> vaccsList = new ArrayList<>();
            for (int i = 0; i < gVisitAry.kidVaccinations.size(); i++) {
                KidVaccinations vaccs = new KidVaccinations();
                vaccs.location = gVisitAry.kidVaccinations.get(i).location;
                vaccs.kid_id = gVisitAry.kidVaccinations.get(i).kid_id;
                vaccs.vaccination_id = gVisitAry.kidVaccinations.get(i).vaccination_id;
                vaccs.mobile_id = gVisitAry.kidVaccinations.get(i).kid_id;
                vaccs.created_timestamp = gVisitAry.kidVaccinations.get(i).created_timestamp;
                vaccs.is_sync = true;
                vaccs.save();

                vaccsList.add(vaccs);
            }
        Intent intent = new Intent(getApplication(), VaccinationActivity.class);
        Bundle bnd = KidVaccinationDao.get_visit_details_db(kid);
        intent.putExtra("childid", SearchActivity.data.get(selectedPosition).epi_number);
        intent.putExtras(bnd);
        startActivity(intent);
           //  showVaccinations(vaccsList);


    }
    int selectedPosition = 0;
    private  void showVaccinations(ArrayList<KidVaccinations> vaccsList){
        Bundle b = new Bundle();
        int max_vaccID=0;
        for(int i=0;i<vaccsList.size();i++){
            if(max_vaccID<vaccsList.get(i).vaccination_id){
                max_vaccID = vaccsList.get(i).vaccination_id;
            }
        }

        int max_visit = VaccinationsDao.getById(max_vaccID).get(0).visit_id;
        b.putString("visit_num", max_visit + "");

        List<Injections> inj = new Select()
                .from(Injections.class)
                .innerJoin(Vaccinations.class)
                .on(" Injections._id=Vaccinations.injection_id")
                .where("Vaccinations.visit_id =?", max_visit)
                .orderBy("Vaccinations._id")
                .execute();

        List<Vaccinations> vacs = new Select().distinct()
                .from(Vaccinations.class)
                .innerJoin(KidVaccinations.class)
                .on(" Vaccinations._id=KidVaccinations.vaccination_id")
                .where("KidVaccinations.kid_id =?", SearchActivity.data.get(selectedPosition).kid_id )
                .and("Vaccinations.visit_id =?", max_visit)
                .orderBy("Vaccinations._id")
                .execute();

        String str = "";
        int x = 0;
        if (vaccsList.size() > 0) {
            if (inj.get(0).id == vacs.get(0).injection_id) {
                str = "1";
                x++;


            } else {
                str = "0";

            }
        } else {
            str = "0";
        }
        for (int i = 1; i < inj.size(); i++) {

            if (x < vacs.size()) {
                if (inj.get(i).id == vacs.get(x).injection_id) {
                    str = str + ",1";
                    x++;


                } else {
                    str = str + ",0";

                }
            } else {
                str = str + ",0";


            }

        }

        b.putString("vacc_details", str);
        Intent intent = new Intent(getApplication(), VaccinationActivity.class);
        intent.putExtra("childid", SearchActivity.data.get(selectedPosition).epi_number);
        intent.putExtras(b);
        startActivity(intent);

    }
}
