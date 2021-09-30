package com.webandcrafts.healwire.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.webandcrafts.healwire.R;

import java.util.regex.Pattern;

/**
 * Created By vineeth on 7/19/2018.
 */

public class Utils {


    public static void hideKeyboard(Activity ctx) {
        if (ctx != null) {
            InputMethodManager inputManager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);

// check if no view has focus:
            View v = ctx.getCurrentFocus();
            if (v == null)
                return;

            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }
    }

    public static boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }

}
