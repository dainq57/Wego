package com.stp.wego.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.stp.wego.R;
import com.stp.wego.support.MakeHttpRequest;
import com.stp.wego.support.UserSessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class FragmentProfileInfo extends Fragment {

    private static final String GETDATA_PROFILE_URL = "http://uetour.16mb.com/app_tour/user/getData.php";
    private static final String TAG_USER_NAME = "username";
    private static final String TAG_NAME = "name";
    private static final String TAG_DATE_OF_BIRTH = "birthday";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_PLACE = "place";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_ABOUT_ME = "aboutme";
    private static final String TAG_SCORE = "score";

    private String mUsername;
    private TextView mName, mAbout, mPlace, mGender, mBirthDate, mEmail, mPhone, mLevel;
    private static String name, place, aboutme, gender, birthdate, email, phone;

    public FragmentProfileInfo() {
    }

    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        UserSessionManager sessionManager = new UserSessionManager(getActivity());
        HashMap user = sessionManager.getDetailsUser();
        mUsername = (String) user.get(UserSessionManager.KEY_NAME);

        mName = (TextView) view.findViewById(R.id.profile_name);
        mPlace = (TextView) view.findViewById(R.id.profile_place);
        mAbout = (TextView) view.findViewById(R.id.profile_about_me);
        mGender = (TextView) view.findViewById(R.id.profile_gender);
        mBirthDate = (TextView) view.findViewById(R.id.profile_birth_date);
        mEmail = (TextView) view.findViewById(R.id.profile_email);

        mPhone = (TextView) view.findViewById(R.id.profile_phone);
        mLevel = (TextView) view.findViewById(R.id.profile_level);
    }

    @Override
    public void onResume() {
        super.onResume();
        getData(mUsername);
    }

    private void getDataProfile(JSONObject object) {
        name = object.optString(TAG_NAME);
        place = object.optString(TAG_PLACE);
        aboutme = object.optString(TAG_ABOUT_ME);
        gender = object.optString(TAG_GENDER);
        birthdate = object.optString(TAG_DATE_OF_BIRTH);
        email = object.optString(TAG_EMAIL);
        phone = object.optString(TAG_PHONE);
        String level = object.optString(TAG_SCORE);

        mName.setText(name);
        mPlace.setText(place);
        mAbout.setText(aboutme);
        mGender.setText(gender);
        mBirthDate.setText(birthdate);
        mEmail.setText(email);
        mPhone.setText(phone);
        mLevel.setText(level);
    }

    private void getData(String username) {
        class GetdataProfile extends AsyncTask<String, Void, String> {
            MakeHttpRequest ruc = new MakeHttpRequest();
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Please wait...", null, true, true);
                loading.setCancelable(false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    getDataProfile(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (loading.isShowing()) {
                    loading.dismiss();
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

    public static String getName() {
        return name;
    }

    public static String getPlace() {
        return place;
    }

    public static String getAboutme() {
        return aboutme;
    }

    public static String getGender() {
        return gender;
    }

    public static String getBirthdate() {
        return birthdate;
    }

    public static String getEmail() {
        return email;
    }

    public static String getPhone() {
        return phone;
    }
}
