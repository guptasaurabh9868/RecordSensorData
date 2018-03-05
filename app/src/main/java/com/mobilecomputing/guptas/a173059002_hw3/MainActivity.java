package com.mobilecomputing.guptas.a173059002_hw3;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

import static com.mobilecomputing.guptas.a173059002_hw3.R.id;
import static com.mobilecomputing.guptas.a173059002_hw3.R.layout;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static Boolean accel_flag = false;
    public static Boolean gps_flag = false;
    public static String Entry;
    public static Boolean record_toggle = false;

    private ViewPager viewPager;
    public ArrayList<Fragment> tabFragmentsList;

    public SensorManager sensorManager;
    public Sensor sensor;
    public static SharedPreferences sharedPrefs;
    public static SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);

        Log.d(TAG, "Oncreate : Strating");
        //Get the  ViewPager and set the viewPageadapter so that it can show items..
        viewPager = findViewById(id.viewpager);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        setupViewPager(viewPager);


        //Give the Tablayout the viewPager
        TabLayout tabLayout = (TabLayout) findViewById(id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        SharedPreferences sharedPrefs = getSharedPreferences("pref.dat", Context.MODE_PRIVATE);
        editor = sharedPrefs.edit();

    }

    private void setupViewPager(ViewPager viewPager) {
        AdapterFragmentAdapter pagerAdapter = new AdapterFragmentAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new LoginFragment(), "Login");
        pagerAdapter.addFragment(new SensorFragment(), "Sensor");
        pagerAdapter.addFragment(new RecordFragment(), "Record");
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
      Log.d(TAG,"onResume Activity");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG,"OnPaused Activity");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"Onstop Activity");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"OnDestroy Acitivity");
    }
}
