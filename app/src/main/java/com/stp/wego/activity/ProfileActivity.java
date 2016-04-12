package com.stp.wego.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.stp.wego.R;
import com.stp.wego.adapter.ViewPagerAdapter;
import com.stp.wego.fragment.FragmentProfileInfo;
import com.stp.wego.fragment.FragmentProfileTour;

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG_NAME = "name";
    private static final String TAG_DATE_OF_BIRTH = "birthday";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_PLACE = "place";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_ABOUT_ME = "aboutme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();
    }

    private void initView() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

//        mFragmentTour = new FragmentProfileTour();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editProfile = new Intent(ProfileActivity.this, EditProfileActivity.class);
                editProfile.putExtra(TAG_NAME, FragmentProfileInfo.getName());
                editProfile.putExtra(TAG_ABOUT_ME, FragmentProfileInfo.getAboutme());
                editProfile.putExtra(TAG_GENDER, FragmentProfileInfo.getGender());
                editProfile.putExtra(TAG_DATE_OF_BIRTH, FragmentProfileInfo.getBirthdate());
                editProfile.putExtra(TAG_EMAIL, FragmentProfileInfo.getEmail());
                editProfile.putExtra(TAG_PHONE, FragmentProfileInfo.getPhone());
                editProfile.putExtra(TAG_PLACE, FragmentProfileInfo.getPlace());

                startActivity(editProfile);
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentProfileInfo(), getString(R.string.about_me_tag));
        adapter.addFragment(new FragmentProfileTour(), getString(R.string.my_tour_tag));
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
