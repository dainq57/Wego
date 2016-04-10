package com.stp.wego.support;

import com.stp.wego.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class InfomationJSONParse {
    public void createData(String string, User user) throws JSONException {
        JSONArray jsonArray = new JSONArray(string);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            String name = jsonObject.optString("name");
            String email = jsonObject.optString("email");
            String birthday = jsonObject.optString("birthday");
            String gender = jsonObject.optString("gender");
            String place = jsonObject.optString("place");
            String phone = jsonObject.optString("phone");
//        String score = jsonObject.optString("score");

            user.setmName(name);
            user.setmDateOfBirth(birthday);
            user.setmGender(gender);
            user.setmPlace(place);
            user.setmPhone(phone);
            user.setmEmail(email);
        }

    }
}
