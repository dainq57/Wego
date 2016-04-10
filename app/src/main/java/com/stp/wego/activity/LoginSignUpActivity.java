package com.stp.wego.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.stp.wego.R;

public class LoginSignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private Boolean exit = false;
    private static final int TIME_DELAY = 3 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initView();
    }

    private void initView() {
        Button btnLogin = (Button) findViewById(R.id.btn_login_wego);
        btnLogin.setOnClickListener(this);

        Button btnSignUp = (Button) findViewById(R.id.btn_sign_up_wego);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (exit) {
            finish();
        } else {
            Toast.makeText(this, "Press Back again to quit Wego", Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, TIME_DELAY);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_wego:
                Intent login = new Intent(this, LoginActivity.class);
                startActivity(login);
                break;
            case R.id.btn_sign_up_wego:
                Intent signup = new Intent(this, SignUpActivity.class);
                startActivity(signup);
        }
    }
}