package com.ipal.itu.harzindagi.Activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.androidquery.callback.LocationAjaxCallback;
import com.ipal.itu.harzindagi.Dao.ChildInfoDao;
import com.ipal.itu.harzindagi.Entity.Books;
import com.ipal.itu.harzindagi.Entity.ChildInfo;
import com.ipal.itu.harzindagi.Entity.UpdateChildInfo;
import com.ipal.itu.harzindagi.R;
import com.ipal.itu.harzindagi.Utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by IPAL on 4/7/2016.
 */
public class EditBookActivity extends BaseActivity {
    private static final int CAMERA_REQUEST = 1888;
    private static final int CALENDAR_CODE = 100;

    EditText book_number_ed;
    Button registerEditChildRecord;

    String epiNumber;
    long kid_id;

    int old_book_numbr;
    String app_name;
    Calendar myCalendar = Calendar.getInstance();
    public static String location = "0.0000,0.0000";
    private PopupWindow pw;
    private View popUpView;
    TextView toolbar_title;
    long activityTime;

    private void createContexMenu() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popUpView = inflater.inflate(R.layout.contex_popup, null, false);
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 130, getResources().getDisplayMetrics());
        pw = new PopupWindow(popUpView, width, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pw.setOutsideTouchable(true);
        pw.setTouchable(true);
        pw.setBackgroundDrawable(getResources().getDrawable(R.drawable.pop_up_bg_drawable));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("معلومات تبدیل کریں");
        app_name = getResources().getString(R.string.app_name);


        final Bundle bundle = getIntent().getExtras();
        old_book_numbr = bundle.getInt("book_numr");
        kid_id=bundle.getLong("kid_id");
        book_number_ed = (EditText) findViewById(R.id.book_number_et);

        registerEditChildRecord = (Button) findViewById(R.id.registerEditChildRecord);
        registerEditChildRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = inputValidate();
                if (!msg.equals("")) {
                    return;
                }
                List<ChildInfo> childInfo = ChildInfoDao.getByKIdAndIMEI(kid_id, Constants.getIMEI(EditBookActivity.this));
                List<Books>bookses=Books.getByBookId(old_book_numbr);
                if (bookses.size()>0)
                {
                    bookses.get(0).delete();
                }
                if(childInfo.size()>0)
                {
                    Books books=new Books();
                    books.book_number= Integer.parseInt(book_number_ed.getText().toString());
                    books.kid_id=kid_id;
                    books.save();
                    childInfo.get(0).book_id = book_number_ed.getText().toString();
                    childInfo.get(0).save();
                    finish();
                }


            }
        });
        createContexMenu();

    }

    public void showError(View v, String error) {

        ((TextView) popUpView.findViewById(R.id.errorText)).setText(error);
        pw.showAsDropDown(v, 0, -Constants.pxToDp(EditBookActivity.this, 10));
        Constants.sendGAEvent(EditBookActivity.this, Constants.getUserName(EditBookActivity.this), Constants.GaEvent.EDIT_REGISTER_ERROR, error, 0);
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        v.startAnimation(shake);
    }

   /* public void fillValues(long kid_id) {
        List<ChildInfo> chidInfo = ChildInfoDao.getByKId(kid_id);
        if (chidInfo.size() > 0) {
            EPINumber.setText(chidInfo.get(0).epi_number);
            CenterName.setText(chidInfo.get(0).epi_name);
            childName.setText(chidInfo.get(0).kid_name);
            Gender = chidInfo.get(0).gender;
            ChildGender.setText("Female");
            if (chidInfo.get(0).gender == 1)
                ChildGender.setText("Male");
            DOBText.setText(chidInfo.get(0).date_of_birth);
            guardianName.setText(chidInfo.get(0).guardian_name);
            guardianCNIC.setText(chidInfo.get(0).guardian_cnic);
            guardianMobileNumber.setText(chidInfo.get(0).phone_number);
            motherName.setText(chidInfo.get(0).mother_name);
            houseAddress.setText(chidInfo.get(0).child_address);
        }

    }
*/
    public String inputValidate() {
        String error = "";


        String bb = book_number_ed.getText().toString().trim();
        if (bb.equals("")) {
                error = "کتاب نمبر درج کریں";
                showError(book_number_ed, error);

                return error;
        }


        return error;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == android.R.id.home) {
            // startActivity(new Intent(getApplication(),RegisterChildActivity.class).putExtra("epiNumber",childID));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
