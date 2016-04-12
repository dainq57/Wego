package com.stp.wego.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.stp.wego.R;
import com.stp.wego.dialog.ConfirmLogout;
import com.stp.wego.fragment.FragmentDrawer;
import com.stp.wego.fragment.NotificationsFragment;
import com.stp.wego.fragment.HomeFragment;
import com.stp.wego.fragment.FavouritesFragment;
import com.stp.wego.support.MakeHttpRequest;
import com.stp.wego.support.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener, View.OnClickListener {
    private static final String GETDATA_PROFILE_URL = "http://uetour.16mb.com/app_tour/user/getData.php";
    private static final String TAG_USER_NAME = "username";

    private String mUsername;
    private TextView mName;
    private TextView mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FragmentDrawer drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
        displayView(0);

        initView();
    }

    private void initView() {
        RelativeLayout mProfile = (RelativeLayout) findViewById(R.id.nav_header_container);
        mProfile.setOnClickListener(this);

        mName = (TextView) findViewById(R.id.drawer_name);
        mEmail = (TextView) findViewById(R.id.drawer_email);

        UserSessionManager sessionManager = new UserSessionManager(getApplicationContext());
        HashMap user = sessionManager.getDetailsUser();
        mUsername = (String) user.get(UserSessionManager.KEY_NAME);

//        Toast.makeText(MainActivity.this, mUsername, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData(mUsername);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
        }
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.nav_item_home);
                break;
            case 1:
                fragment = new NotificationsFragment();
                title = getString(R.string.nav_item_notifications);
                break;
            case 2:
                fragment = new FavouritesFragment();
                title = getString(R.string.nav_item_favourites);
                break;
            case 3:
                logOut();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    private void logOut() {
        ConfirmLogout mDialog = new ConfirmLogout();
        mDialog.show(getFragmentManager(), null);
    }

    private void getData(String username) {
        class GetdataProfile extends AsyncTask<String, Void, String> {
            MakeHttpRequest ruc = new MakeHttpRequest();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String name = jsonObject.optString("name");
                    String email = jsonObject.optString("email");

                    mName.setText(name);
                    mEmail.setText(email);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String, String> data = new HashMap<>();
                data.put(TAG_USER_NAME, params[0]);
                return ruc.sendPostRequest(GETDATA_PROFILE_URL, data);
            }
        }

        GetdataProfile getdataProfile = new GetdataProfile();
        getdataProfile.execute(username);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_header_container:
                Intent profile = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(profile);
                break;
            default:
                break;
        }
    }
}