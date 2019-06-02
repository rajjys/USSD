package com.example.jonathan.ussd;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LaunchActivity extends AppCompatActivity {

    private static final int MY_PERMISSION_CALL_PHONE = 102;

    private BroadcastReceiver mMessageReceiver; //Initialise the broadcast receiver



    private boolean isRegistered;
    private EditText editText;
    private Button btn;
    private TextView txtV;
    private boolean second = false;
    private IntentFilter mFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        btn = findViewById(R.id.btn);
        editText = findViewById(R.id.edText);
        txtV = findViewById(R.id.msgText);

        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getStringExtra("message");
                Log.i("MyBroadcastReceiver", "Got message: " + message);
                showText(message);
            }
        };


        requestCallPermissions(); ///Ask for permissions to make calls/USSD transactions

        mFilter = new IntentFilter("REFRESH");
        this.registerReceiver(mMessageReceiver, mFilter); //register in order to listen to broadcast messages
        isRegistered = true;

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = editText.getText().toString();
                runTransaction(number, true);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try
        {
            if (isRegistered) {
                this.unregisterReceiver(mMessageReceiver);//Un register the Broadcast receiver when the window is left
                isRegistered = false;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestCallPermissions() {

        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LaunchActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSION_CALL_PHONE);
        }
    }

    private void runTransaction(String number, Boolean type) {

        if (type){
            String tempNumber = "";
            if (number.endsWith("#")){
                tempNumber = number.substring(0,number.indexOf("#"));
            }
            String encodedHash = Uri.encode("#");
            String ussd = tempNumber + encodedHash;
            startActivityForResult(new Intent("android.intent.action.CALL",
                    Uri.parse("tel:" + ussd)), 1);

        }
    }
    private void showText(String message){
        txtV.setText(message);
    }
}