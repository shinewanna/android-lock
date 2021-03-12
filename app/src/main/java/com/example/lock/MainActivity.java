package com.example.lock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button lock,enable,disable;
    public static final int RESULT_ENABLE = 11;
    DevicePolicyManager devicePolicyManager;
    ActivityManager activityManager;
    ComponentName compName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        devicePolicyManager = (DevicePolicyManager)  getSystemService(DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager)getSystemService(
                Context.ACTIVITY_SERVICE);
        compName = new ComponentName(this, MyAdmin.class);
        lock = (Button) findViewById(R.id.lock);
        enable = (Button) findViewById(R.id.enable);
        disable = (Button) findViewById(R.id.disable);

        lock.setOnClickListener(this);
        enable.setOnClickListener(this);
        disable.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isActive = devicePolicyManager.isAdminActive(compName);
        disable.setVisibility(isActive ? View.VISIBLE : View.GONE);
        enable.setVisibility(isActive ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view == lock) {
            System.out.print("lock");
            boolean active = devicePolicyManager.isAdminActive(compName);

            if (active) {
                devicePolicyManager.lockNow();
            } else {
                Toast.makeText(this, "You need to enable the Admin Device Features", Toast.LENGTH_SHORT).show();
            }

        } else if (view == enable) {
            Toast.makeText(this, "Enable", Toast.LENGTH_SHORT).show();
            System.out.print("enable");
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Need Permission For Double Tap To Lock Screen");
            startActivityForResult(intent, RESULT_ENABLE);

        } else if (view == disable) {
            System.out.print("disable");
            devicePolicyManager.removeActiveAdmin(compName);
            disable.setVisibility(View.GONE);
            enable.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case RESULT_ENABLE :
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(MainActivity.this, "You have enabled the Admin Device features", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Problem to enable the Admin Device features", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
