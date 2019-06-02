package com.example.jonathan.ussd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;

public class MyBroadCastReceiver extends BroadcastReceiver {

    private static final String TAG = "MyBroadcastReceiver";
    TextView txtV;
    public MyBroadCastReceiver(TextView txt){
        txtV = txt;
    }
    public MyBroadCastReceiver(){

    }
    @Override
    public void onReceive(Context context, Intent intent) {


        String message = intent.getStringExtra("message");
        Log.i(TAG, "Got message: " + message);
        showText(message);
    }

    private void showText(String message){
        txtV.setText(message);
    }
}
