package com.stp.wego.support;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class UserSessionManager {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;

    public static final String PREF_NAME = "PrefUserLogin";
    public static final String KEY_NAME = "username";
    public static final String IS_LOGIN = "isLogin";

    public UserSessionManager(Context context) {
        this.context = context;
        prefs = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = prefs.edit();
    }

    public void createSession(String username) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, username);
        editor.commit();
    }


    public HashMap getDetailsUser() {
        HashMap user = new HashMap();
        user.put(KEY_NAME, prefs.getString(KEY_NAME, null));
        return user;
    }

}
