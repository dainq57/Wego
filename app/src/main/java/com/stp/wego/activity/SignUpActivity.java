package com.stp.wego.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stp.wego.R;
import com.stp.wego.support.MakeHttpRequest;

import org.w3c.dom.Text;

import java.util.HashMap;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String REGISTER_URL = "http://uetour.16mb.com/app_tour/user/register.php";
    private static final int LENGTH_PASSWORD = 6;
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_PASSWORD = "password";
    private static final String TAG_CONFIRM_PASSWORD = "confirmpassword";

    private EditText mName;
    private EditText mEmail;
    private EditText mUserName;
    private EditText mPass;
    private EditText mConfirmPass;

    private boolean isRegister = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
    }

    private void initView() {
        Button btnSignUp = (Button) findViewById(R.id.btn_sign_up);
        btnSignUp.setOnClickListener(this);

        TextView tvLogIn = (TextView) findViewById(R.id.tv_log_in);
        tvLogIn.setOnClickListener(this);

        mName = (EditText) findViewById(R.id.edit_name_sign_up);
        mEmail = (EditText) findViewById(R.id.edit_email_sign_up);
        mUserName = (EditText) findViewById(R.id.edit_account_sign_up);
        mPass = (EditText) findViewById(R.id.edit_pass_sign_up);
        mConfirmPass = (EditText) findViewById(R.id.edit_confirm_pass_sign_up);
    }

    private void registerUser() {
        String name = mName.getText().toString().trim();
        String email = mEmail.getText().toString().trim().toLowerCase();
        String username = mUserName.getText().toString().trim().toLowerCase();
        String password = mPass.getText().toString().trim().toLowerCase();
        String confirmpassword = mConfirmPass.getText().toString().trim().toLowerCase();

        checkInfo(name, email, username, password, confirmpassword);
        if (isRegister) {
            register(name, email, username, password, confirmpassword);
        }
    }

    public void checkInfo(String name, String email, String username, String password, String confirmpassword) {
        if (name.equals("") ||
                email.equals("") ||
                username.equals("") ||
                password.equals("") ||
                confirmpassword.equals("")) {
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            isRegister = false;
        } else if (password.length() < LENGTH_PASSWORD) {
            Toast.makeText(this, "Password have at least 6 character", Toast.LENGTH_SHORT).show();
            isRegister = false;
        } else if (!password.equals(confirmpassword)) {
            Toast.makeText(this, R.string.confirm_not_match, Toast.LENGTH_SHORT).show();
            isRegister = false;
        } else {
            isRegister = true;
        }
    }

    private void register(String name, String email, String username, String password, String confirmpassword) {
        class RegisterUser extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            MakeHttpRequest ruc = new MakeHttpRequest();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SignUpActivity.this, "Please wait...", null, true, true);
                loading.setCancelable(false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (loading.isShowing()) {
                    loading.dismiss();
                }
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                if (s.equalsIgnoreCase("success")) {
                    Intent login = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(login);
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<>();
                data.put(TAG_NAME, params[0]);
                data.put(TAG_EMAIL, params[1]);
                data.put(TAG_USERNAME, params[2]);
                data.put(TAG_PASSWORD, params[3]);
                data.put(TAG_CONFIRM_PASSWORD, params[4]);

                return ruc.sendPostRequest(REGISTER_URL, data);
            }
        }

        RegisterUser registerUser = new RegisterUser();
        registerUser.execute(name, email, username, password, confirmpassword);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent wego = new Intent(SignUpActivity.this, LoginSignUpActivity.class);
        wego.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(wego);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_up:
                registerUser();
                break;
            case R.id.tv_log_in:
                Intent login = new Intent(SignUpActivity.this, LoginActivity.class);
                login.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(login);
                break;
            default:
                break;
        }
    }
}
