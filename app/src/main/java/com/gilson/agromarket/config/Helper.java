package com.gilson.agromarket.config;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Helper {

    public static void message(Context context, String sms){

        Toast.makeText(context, sms, Toast.LENGTH_SHORT).show();

    }

}
