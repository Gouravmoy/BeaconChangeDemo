package com.example.lenovo.beaconchangedemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ListView;

import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.SystemRequirementsChecker;
import com.estimote.sdk.eddystone.Eddystone;

import java.util.Collections;
import java.util.List;

public class ChangeActivity extends AppCompatActivity {

    private static final String TAG = ChangeActivity.class.toString();
    private BeaconManager beaconManager;
    private EddystoneListAdapter adapter;
    ListView eddyStoneListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.i(TAG, "Inside onCreate ");

        adapter = new EddystoneListAdapter(MyApplication.getAppContext());
        eddyStoneListView = (ListView) findViewById(R.id.device_list_eddystone);
        eddyStoneListView.setAdapter(adapter);

        beaconManager = new BeaconManager(MyApplication.getAppContext());

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onDestroy() {
        beaconManager.disconnect();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (SystemRequirementsChecker.checkWithDefaultDialogs(this)) {
            startScanning();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        beaconManager.disconnect();
    }

    private void startScanning() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Log.i(TAG, "Inside srart running");
        toolbar.setSubtitle("Scanning...");
        adapter.replaceWith(Collections.<Eddystone>emptyList());
        beaconManager.setEddystoneListener(new BeaconManager.EddystoneListener() {
            @Override
            public void onEddystonesFound(List<Eddystone> eddystones) {
                toolbar.setSubtitle("Found beacons with Eddystone protocol: " + eddystones.size());
                adapter.replaceWith(eddystones);
            }
        });
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startEddystoneScanning();
            }
        });
    }
}
