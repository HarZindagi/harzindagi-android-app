package com.ipal.itu.harzindagi.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ipal.itu.harzindagi.Activities.LoginActivity;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Entity.Books;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Handlers.OnUploadListner;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ali on 2/25/2016.
 */
public class BooksSyncHandler {

    Context context;
    List<Books> books;
    ProgressDialog pDialog;
    OnUploadListner onUploadListner;
    int index = 0;
    Calendar calendar;

    public BooksSyncHandler(Context context, List<Books> books, OnUploadListner onUploadListner) {
        this.books = books;
        this.context = context;
        this.onUploadListner = onUploadListner;
        calendar = Calendar.getInstance();
    }

    public void execute() {
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Saving Books data...");
        pDialog.show();
        if (books.size() != 0) {
            sendChildData(books.get(index));
        } else {
            pDialog.dismiss();
            onUploadListner.onUpload(true, "");
        }

    }

    private void nextUpload(boolean isUploaded) {
        if (isUploaded) {
            index++;
            if (index < books.size()) {
                sendChildData(books.get(index));
                pDialog.setMessage("Uploading books... " + index + " of " + books.size());
            } else {
                onUploadListner.onUpload(true, "");
                pDialog.dismiss();
            }
        } else {
            onUploadListner.onUpload(false, "");
            pDialog.dismiss();
        }
    }

    public long componentTimeToTimestamp(int year, int month, int day, int hour, int minute) {

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        c.set(Calendar.HOUR, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTimeInMillis();
    }

    private void sendChildData(final Books books) {

        final List<ChildInfo> data = ChildInfoDao.getByKId(books.kid_id);
        if (data.size() > 0) {
            RequestQueue queue = Volley.newRequestQueue(context);
            String url = Constants.books;

            JSONObject obj = null;
            final JSONObject book = new JSONObject();
            try {
                obj = new JSONObject();
                JSONObject user = new JSONObject();
                user.put("auth_token", Constants.getToken(context));
                obj.put("user", user);

                book.put("kid_id", books.kid_id);
                book.put("book_number", books.book_number);


                book.put("nfc_chip_id", "00000000");


                obj.put("book", book);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, obj,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            // Log.d("response",response.toString());
                            if (response.optString("book_number").equals(book.optString("book_number"))) {

                                if( (books.kid_id == Integer.parseInt(response.optString("kid_id")))){
                                    List<Books> child = Books.getByBookId(books.book_number);
                                    if (child.size() != 0) {
                                        child.get(0).is_sync = true;
                                        child.get(0).save();
                                        nextUpload(true);
                                    } else {
                                        Books books = new Books();
                                        books.kid_id = Integer.parseInt(response.optString("kid_id"));
                                        books.book_number = Integer.parseInt(response.optString("book_number"));
                                        books.save();
                                        nextUpload(true);
                                    }
                                }else {
                                 /*   List<Books> child = Books.getByBookId(books.book_number);
                                    if (child.size() != 0) {
                                        child.get(0).delete();
                                    }

                                    Books books = new Books();
                                    books.kid_id = Integer.parseInt(response.optString("kid_id"));
                                    books.book_number = Integer.parseInt(response.optString("book_number"));
                                    books.is_sync = true;
                                    books.save();*/

                                    nextUpload(false);
                                }



                            }

                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                    nextUpload(false);
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
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(10000,
                    LoginActivity.MAX_RETRY,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsonObjReq);
        } else {
            books.delete();
            nextUpload(true);
        }

    }

}
