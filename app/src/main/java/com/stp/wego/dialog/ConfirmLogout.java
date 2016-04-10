package com.stp.wego.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.stp.wego.R;
import com.stp.wego.support.UserSessionManager;

public class ConfirmLogout extends DialogFragment {
    private UserSessionManager sessionManager;

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        AlertDialog.Builder dialogLogout = new AlertDialog.Builder(getActivity());

        dialogLogout.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        }).setTitle("Sure?");

        return dialogLogout.create();
    }
}

