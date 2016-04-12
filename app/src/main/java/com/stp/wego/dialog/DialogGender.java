package com.stp.wego.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.stp.wego.R;
import com.stp.wego.interfaces.SelectGenderListener;

public class DialogGender extends DialogFragment implements View.OnClickListener {
    RadioButton rbMale, rbFeMale, rbOther;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_gender, container, false);
        Button btnCancel = (Button) view.findViewById(R.id.btn_edit_gender_cancel);
        btnCancel.setOnClickListener(this);

        Button btnOK = (Button) view.findViewById(R.id.btn_edit_gender_ok);
        btnOK.setOnClickListener(this);

        rbMale = (RadioButton) view.findViewById(R.id.gender_male);
        rbFeMale = (RadioButton) view.findViewById(R.id.gender_female);
        rbOther = (RadioButton) view.findViewById(R.id.gender_other);

        getDialog().setTitle(R.string.select_gender);
        return view;
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        String mGender = null;
        if (i == R.id.btn_edit_gender_cancel) {
            dismiss();
        } else if (i == R.id.btn_edit_gender_ok) {
            if (rbMale.isChecked()) {
                mGender = "Male";
            } else if (rbFeMale.isChecked()) {
                mGender = "Female";
            } else if (rbOther.isChecked()) {
                mGender = "Other";
            }
            SelectGenderListener genderListener = (SelectGenderListener) getActivity();
            genderListener.onComplete(mGender);
            dismiss();
            Toast.makeText(getActivity(), mGender, Toast.LENGTH_SHORT).show();
        }
    }
}
