package com.smb116.tp10;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 123;

    WifiManager wifiManager;
    BroadcastReceiver wifiScanReceiver;
    IntentFilter intentFilter;

    TextView tp9TextZone;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tp9TextZone = findViewById(R.id.tp9_text_zone);

        checkPermissions();

        wifiManager = (WifiManager)
                getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                boolean success = intent.getBooleanExtra(
                        WifiManager.EXTRA_RESULTS_UPDATED, false);
                if (success) {
                    scanSuccess();
                    Log.i(TAG, "success");
                } else {
                    scanFailure();
                    Log.i(TAG, "failure");
                }
            }
        };

        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);

        boolean success = wifiManager.startScan();
        if (!success) {
            scanFailure();
        }
    }

    private void scanSuccess() {
        List<ScanResult> results = wifiManager.getScanResults();
        Log.i(TAG, String.valueOf(results.size()));
        for (ScanResult scanResult : results){
            String ssid=scanResult.SSID;
            String bsid=scanResult.BSSID;
            int frequency=scanResult.frequency;
            int level=scanResult.level;
            String message = "\nSSID: "+ssid+"\n\tBSSID: "+bsid+"\n\tFREQUENCY: "+frequency+"\n\tLEVEL: "+level+"\n\n";
            tp9TextZone.setText(message);
            Log.i(TAG,message);
        }
    }

    private void scanFailure() {
        List<ScanResult> results = wifiManager.getScanResults();
        Log.i(TAG, String.valueOf(results.size()));
        for (ScanResult scanResult : results){
            String ssid=scanResult.SSID;
            String bsid=scanResult.BSSID;
            int frequency=scanResult.frequency;
            int level=scanResult.level;
            String message = "\nSSID: "+ssid+"\n\tBSSID: "+bsid+"\n\tFREQUENCY: "+frequency+"\n\tLEVEL: "+level+"\n\n";
            tp9TextZone.setText(message);
            Log.i(TAG,message);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void checkPermissions(){
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
        && ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
        &&ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.CHANGE_WIFI_STATE) ==
                PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "checkPermissions OK");
        }else {
            requestPermissions(
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.CHANGE_WIFI_STATE},
                    REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 123:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "checkPermissions OK");

                }  else {
                    Log.i(TAG, "checkPermissions DENIED");
                }
                return;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(wifiScanReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(wifiScanReceiver);
    }
}