package com.stp.wego.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.stp.wego.R;
import com.stp.wego.dialog.DialogGender;
import com.stp.wego.support.MakeHttpRequest;
import com.stp.wego.interfaces.SelectGenderListener;
import com.stp.wego.support.UserSessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener,
        SelectGenderListener {
    private static final String UPDATE_PROFILE_URL = "http://uetour.16mb.com/app_tour/user/upDateProfile.php";
    private static final String TAG_USER_NAME = "username";
    private static final String TAG_NAME = "name";
    private static final String TAG_DATE_OF_BIRTH = "birthday";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_PLACE = "place";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_ABOUT_ME = "aboutme";

    private EditText editName;
    private EditText editAboutMe;
    private EditText editPlace;
    private EditText editEmail;
    private EditText editPhone;

    private TextView tvGender;
    private TextView tvDateOfBirth;

    private String username;

    private DatePickerDialog mDialogDateBirth;
    private DialogGender mDialogGender;
    private SimpleDateFormat mDateFormat;

    private boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
        initDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edit_profile, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_upload_image) {
            Toast.makeText(EditProfileActivity.this, "Upload avatar", Toast.LENGTH_SHORT).show();
        } else {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        UserSessionManager sessionManager = new UserSessionManager(getApplicationContext());
        HashMap user = sessionManager.getDetailsUser();
        username = (String) user.get(UserSessionManager.KEY_NAME);

        editName = (EditText) findViewById(R.id.edit_profile_name);
        editAboutMe = (EditText) findViewById(R.id.edit_profile_about_me);
        editPlace = (EditText) findViewById(R.id.edit_profile_place);
        editPhone = (EditText) findViewById(R.id.edit_profile_phone);
        editEmail = (EditText) findViewById(R.id.edit_profile_email);
        tvGender = (TextView) findViewById(R.id.edit_profile_gender);
        tvDateOfBirth = (TextView) findViewById(R.id.edit_profile_birth_date);

        Intent profile = getIntent();
        editName.setText(profile.getStringExtra(TAG_NAME));
        editAboutMe.setText(profile.getStringExtra(TAG_ABOUT_ME));
        editPlace.setText(profile.getStringExtra(TAG_PLACE));
        editPhone.setText(profile.getStringExtra(TAG_PHONE));
        editEmail.setText(profile.getStringExtra(TAG_EMAIL));
        tvDateOfBirth.setText(profile.getStringExtra(TAG_DATE_OF_BIRTH));
        tvGender.setText(profile.getStringExtra(TAG_GENDER));

        editName.setSelection(editName.length());
        editAboutMe.setSelection(editAboutMe.length());
        editPlace.setSelection(editPlace.length());
        editPhone.setSelection(editPhone.length());
        editEmail.setSelection(editEmail.length());

        RelativeLayout rowDateOfBirth = (RelativeLayout) findViewById(R.id.edit_profile_row_date_birth);
        rowDateOfBirth.setOnClickListener(this);

        RelativeLayout rowGender = (RelativeLayout) findViewById(R.id.edit_profile_row_gender);
        rowGender.setOnClickListener(this);

        Button btnUpdate = (Button) findViewById(R.id.btn_update_profile);
        btnUpdate.setOnClickListener(this);

        mDialogGender = new DialogGender();
    }

    public void initDialog() {
        mDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar calendar = Calendar.getInstance();
        mDialogDateBirth = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                tvDateOfBirth.setText(mDateFormat.format(newDate.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void updateProfile() {
        String name = editName.getText().toString().trim();
        String aboutme = editAboutMe.getText().toString().trim();
        String place = editPlace.getText().toString().trim();
        String phone = editPhone.getText().toString().toLowerCase().trim();
        String email = editEmail.getText().toString().toLowerCase().trim();

        String gender = tvGender.getText().toString().trim();
        String date = tvDateOfBirth.getText().toString().toLowerCase().trim();
        checkInfo(name, place, phone, email);

        Log.e("INFORMATION: ", name + " " + gender + " " + place + " " + aboutme);
        if (isUpdate) {
            update(username, name, date, place, phone, email, gender, aboutme);
        }
    }

    public void checkInfo(String name, String place, String phone, String email) {
        if (name.equals("") ||
                place.equals("") ||
                phone.equals("") ||
                email.equals("")) {
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            isUpdate = false;
        } else {
            isUpdate = true;
        }
    }

    private void update(String username, String name, String date, String place, String phone,
                        String email, String gender, String aboutme) {
        class UpdateProfile extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            MakeHttpRequest ruc = new MakeHttpRequest();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(EditProfileActivity.this, "Please wait...", null, true, true);
                loading.setCancelable(false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (loading.isShowing()) {
                    loading.dismiss();
                }
                if (s.equalsIgnoreCase(getString(R.string.update_success))) {
                    onBackPressed();
                } else {
                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<>();
                data.put(TAG_USER_NAME, params[0]);
                data.put(TAG_NAME, params[1]);
                data.put(TAG_DATE_OF_BIRTH, params[2]);
                data.put(TAG_PLACE, params[3]);
                data.put(TAG_PHONE, params[4]);
                data.put(TAG_EMAIL, params[5]);
                data.put(TAG_GENDER, params[6]);
                data.put(TAG_ABOUT_ME, params[7]);

                return ruc.sendPostRequest(UPDATE_PROFILE_URL, data);
            }
        }

        UpdateProfile updateProfile = new UpdateProfile();
        updateProfile.execute(username, name, date, place, phone, email, gender, aboutme);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String gender = data.getStringExtra(TAG_GENDER);
                Toast.makeText(getApplicationContext(), gender, Toast.LENGTH_SHORT).show();
                tvGender.setText(gender);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_update_profile) {
            updateProfile();
        } else if (i == R.id.edit_profile_row_date_birth) {
            mDialogDateBirth.show();
        } else if (i == R.id.edit_profile_row_gender) {
            mDialogGender.setTargetFragment(mDialogGender, 1);
            mDialogGender.show(getFragmentManager(), null);
        }
    }

    @Override
    public void onComplete(String gender) {
        tvGender.setText(gender);
    }
}

