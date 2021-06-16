package com.example.collapsingtoolbar.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.Window;

import com.example.collapsingtoolbar.R;

public class Dialog {


    AlertDialog.Builder builder;
    AlertDialog dialog;
    Activity activity;
    DialogCallBack callBack;
    public Dialog(Activity activity) {
        this.activity = activity;
        builder = new AlertDialog.Builder(activity, R.style.MyDialogTheme);

    }

    public void set(String title, String message) {

        builder.setTitle(title);
        builder.setMessage(message);
    }

    public void show(String funP) {

        if (funP.equals("askPermission"))
            builder.setPositiveButton("OK", (dialog, which) ->
            {
               int choice = 1;
               callBack.choice(choice);
                dialog.cancel();
            });
        else if (funP.equals("null"))
            builder.setPositiveButton(null, null);
        builder.setNegativeButton("CANCEL",
                (dialog, which) -> {
                    dialog.cancel();
                    activity.finishAffinity();
                });

        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void cancel() {
        dialog = null;
        dialog.cancel();

    }

    public void setCallBack(DialogCallBack callBack) {
        this.callBack = callBack;
    }

    public interface DialogCallBack{
        void choice(int choice);
    }

}
