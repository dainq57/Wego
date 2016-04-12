package com.stp.wego.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stp.wego.R;
import com.stp.wego.support.MakeHttpRequest;
import com.stp.wego.support.UserSessionManager;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String LOGIN_URL = "http://uetour.16mb.com/app_tour/user/login.php";
    public static final String PREFS_NAME = "myPrefsLogin";
    public static final String PREF_USERNAME = "username";
    public static final String PREF_PASSWORD = "password";

    private TextView tvForgotPass;
    private EditText editTextUserName, editTextPassword;
    private CheckedTextView ctvRemember;

    private String username;
    private String password;

    public UserSessionManager sessionManager;
    private boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
    }

    private void initView() {
        Button btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);

        TextView tvSignUp = (TextView) findViewById(R.id.tv_sign_up);
        tvSignUp.setOnClickListener(this);

        tvForgotPass = (TextView) findViewById(R.id.tv_forgot_pass);
        tvForgotPass.setOnClickListener(this);

        editTextUserName = (EditText) findViewById(R.id.edit_account_login);
        editTextPassword = (EditText) findViewById(R.id.edit_password_login);

        ctvRemember = (CheckedTextView) findViewById(R.id.ctv_remeber);
        ctvRemember.setOnClickListener(this);
        ctvRemember.setChecked(true);

        sessionManager = new UserSessionManager(getApplicationContext());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent wego = new Intent(LoginActivity.this, LoginSignUpActivity.class);
        wego.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(wego);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                getInfo();
                setPreferences();
                if (ctvRemember.isChecked()) {
                    rememberMe(username, password);
                } else if (!ctvRemember.isChecked()) {
                    forgetUser();
                }
                isLogin();
//                login();
                break;
            case R.id.tv_sign_up:
                Intent signup = new Intent(LoginActivity.this, SignUpActivity.class);
                signup.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(signup);
                break;
            case R.id.ctv_remeber:
                if (ctvRemember.isChecked()) {
                    ctvRemember.setChecked(false);
                } else {
                    ctvRemember.setChecked(true);
                }
                break;
            case R.id.tv_forgot_pass:
                Intent forgot = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                forgot.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(forgot);
                break;
            default:
                break;
        }
    }

    public void setPreferences() {
        sessionManager.createSession(username);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUser();
    }

    public void rememberMe(String user, String pass) {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putString(PREF_USERNAME, user)
                .putString(PREF_PASSWORD, pass)
                .commit();

    }

    void forgetUser() {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putString(PREF_USERNAME, null)
                .putString(PREF_PASSWORD, null)
                .commit();
    }

    public void getUser() {
        SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String user = pref.getString(PREF_USERNAME, null);
        String pass = pref.getString(PREF_PASSWORD, null);

        editTextUserName.setText(user);
        editTextPassword.setText(pass);

        editTextUserName.setSelection(user.length());
        editTextPassword.setSelection(pass.length());
    }

    private void getInfo() {
        username = editTextUserName.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
    }

    private void login() {
        checkInfo(username, password);
        if (isLogin) {
            userLogin(username, password);
        }
    }

    private void checkInfo(String username, String password) {
        if (username.equals("") || password.equals("")) {
            Toast.makeText(this, R.string.fill_all_fields, Toast.LENGTH_SHORT).show();
            isLogin = false;
        } else {
            isLogin = true;
        }
    }

    private void userLogin(final String username, final String password) {
        class UserLoginClass extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(LoginActivity.this, "Please Wait", null, true, true);
                loading.setCancelable(false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (loading.isShowing()) {
                    loading.dismiss();
                }
                if (s.equalsIgnoreCase("success")) {
                    isLogin();
                } else {
                    Toast.makeText(LoginActivity.this, s, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<>();
                data.put("username", params[0]);
                data.put("password", params[1]);

                MakeHttpRequest ruc = new MakeHttpRequest();

                return ruc.sendPostRequest(LOGIN_URL, data);
            }
        }
        UserLoginClass ulc = new UserLoginClass();
        ulc.execute(username, password);
    }

    public void isLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
