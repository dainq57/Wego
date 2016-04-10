package com.stp.wego.model;

public class User {
    private String mName;
    private String mDateOfBirth;
    private String mGender;
    private String mPlace;
    private String mPhone;
    private String mEmail;

    public User() {
    }

    public String getmName() {
        return mName;
    }

    public String getmDateOfBirth() {
        return mDateOfBirth;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public void setmDateOfBirth(String mDateOfBirth) {
        this.mDateOfBirth = mDateOfBirth;
    }

    public void setmGender(String mGender) {
        this.mGender = mGender;
    }

    public void setmPlace(String mPlace) {
        this.mPlace = mPlace;
    }

    public void setmPhone(String mPhone) {
        this.mPhone = mPhone;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmEmail() {
        return mEmail;
    }

    public String getmPhone() {
        return mPhone;
    }

    public String getmPlace() {
        return mPlace;
    }

    public String getmGender() {
        return mGender;
    }
}
