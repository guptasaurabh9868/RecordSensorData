package com.mobilecomputing.guptas.a173059002_hw3;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by guptas on 3/3/18.
 */


public class RecordFragment extends Fragment implements Serializable {
    private static final String TAG = "Record", STATE_ITEMS = "items";
    private TextView record;
    private ToggleButton toggleButton;
    public static ArrayAdapter<String> timestampAdapter;
    public static ArrayList<String> timestampStr = new ArrayList<String>();
    ListView listView;
    private String Entry = "";
    public static String gpsEntry = null;
    public String file1, file2;
    public static int num1 = 0, num2 = 0;
    private static Boolean sensorListener_flag = false, gpslistener_flag = false;
    private static String fields = "timestamp, lat, long, accelx, accely, accelz, label\n";
    public static String labelString = "Stationary";

    private RadioGroup label;
    private RadioButton walking, stationary;

    private float x_accel, y_accel, z_accel;
    public SensorManager sensorManager;
    public Sensor sensor;
    private long lastUpdate;

    public LocationManager locationManager = null;
    public LocationListener locationListener = null;


    /**
     * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}, and
     * {@link #onActivityCreated(Bundle)}.
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_ITEMS, timestampStr);
    }

    /**
     * {@link #onStart()}.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            timestampStr = (ArrayList<String>) savedInstanceState.getSerializable(STATE_ITEMS);
        }
    }


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);
        Log.d(TAG, "Oncreate : Starting");
        record = view.findViewById(R.id.record);
        toggleButton = view.findViewById(R.id.toggleButton);
        listView = view.findViewById(R.id.timestamps);
        label = view.findViewById(R.id.label);
        walking = view.findViewById(R.id.walking);
        stationary = view.findViewById(R.id.stationary);


        if (MainActivity.sharedPrefs != null)
        {
            Log.d(TAG,"SharedPreference Worked!!!");

            String label = MainActivity.sharedPrefs.getString("label","");
            String record_toggle = MainActivity.sharedPrefs.getString("record_toggle","");
            String accel_flag = MainActivity.sharedPrefs.getString("Accel_flag","");
            String gps_flag = MainActivity.sharedPrefs.getString("GPS_flag","");
            if("Stationary".equalsIgnoreCase(label))
            {
                stationary.setChecked(true);
            }else if("walking".equalsIgnoreCase(label))
            {
                walking.setChecked(true);
            }
            if("true".equalsIgnoreCase(record_toggle))
            {
                toggleButton.setChecked(true);
                MainActivity.record_toggle = true;
                record.setText("Stop Recording");
            }else{
                toggleButton.setChecked(false);
                MainActivity.record_toggle = false;
                record.setText("Start Recording");
            }
            if("true".equalsIgnoreCase(accel_flag))
            {
                MainActivity.accel_flag = true;
            }
            if("true".equalsIgnoreCase(gps_flag))
            {
                MainActivity.gps_flag = true;
            }
        }





        label.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                labelString = walking.isChecked() ? "Walking" : "Stationary";
            }
        });

        timestampAdapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, timestampStr);
        listView.setAdapter(timestampAdapter);

        sensorManager = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked && (MainActivity.accel_flag || MainActivity.gps_flag)) {

                    MainActivity.record_toggle = true;
                    file1 = "data" + "_" + getTimestamp() + ".csv";
                    if (MainActivity.accel_flag) {
                        saveFile(file1,MainActivity.Entry);
                        saveFile(file1,fields);
                        sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
                    }
                    if (MainActivity.gps_flag) {
                        if(!MainActivity.accel_flag)
                        {
                            saveFile(file1,MainActivity.Entry);
                            saveFile(file1,fields);
                        }
                        GPSfunction();
                    }

                    record.setText("Stop Recording");

                } else if (!isChecked) {
                    addEntryToList(getTimestamp());
                    MainActivity.record_toggle = false;
                    if(sensorListener_flag)
                        sensorManager.unregisterListener(sensorEventListener);
                    if(gpslistener_flag)
                        locationManager.removeUpdates(locationListener);

                    record.setText("Start Recording");

                } else {
                    Toast.makeText(getContext(), "Please Select atleast one Sensor from Sensor Tab", Toast.LENGTH_SHORT).show();
                }
            }
        });

        MainActivity.editor.putString("label",labelString);
        MainActivity.editor.putString("record_toggle",MainActivity.record_toggle.toString());
        MainActivity.editor.apply();
        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager
                            .GPS_PROVIDER, 0, 0, locationListener);
                    locationManager.requestLocationUpdates(LocationManager.
                            NETWORK_PROVIDER, 0, 0, locationListener);
                }
                break;
            case 20:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    saveFile(file1,Entry);
                }
                return;

        }
    }
    public void GPSfunction() {
        if (!displayGpsStatus()) {
            alertbox("Gps Status!!", "Your GPS is: OFF");
        } else {
            gpslistener_flag = true;
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    gpsEntry = location.getLatitude() + ", "+location.getLongitude();
                    if (!MainActivity.accel_flag)
                    {
                        gpsEntry = null == gpsEntry  ? "Nan, Nan":gpsEntry;
                        Entry = getTimestamp() + ", " + gpsEntry + ", "
                                + "Nan" + ", " + "Nan" + ", " + "Nan" + ", " + labelString +"\n";
                        saveFile(file1,Entry);
                    }

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };

            if (ActivityCompat.checkSelfPermission(getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
               requestPermissions(new String[]{Manifest.permission.INTERNET,
                       Manifest.permission.ACCESS_COARSE_LOCATION,
               Manifest.permission.ACCESS_FINE_LOCATION},10);
                // TODO: Consider calling


                return;
            }else{
                locationManager.requestLocationUpdates(LocationManager
                        .GPS_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.
                        NETWORK_PROVIDER,0,0,locationListener);
            }
        }
    }

    final SensorEventListener sensorEventListener = new SensorEventListener() {


        @Override
        public void onSensorChanged(SensorEvent event) {

            sensorListener_flag = true;
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                gpsEntry = null == gpsEntry  ? "Nan, Nan":gpsEntry;
                Entry = getTimestamp() + ", " + gpsEntry + ", " + event.values[0]+ ", "
                        + event.values[1]+ ", "  + event.values[2] + ", " + labelString +"\n";
                Log.d(TAG, "Sensing Data");

                saveFile(file1,Entry);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    public void addEntryToList(String Entry)
    {
        timestampStr.add(Entry);

        if (timestampStr.size() > 5) {
            timestampStr.remove(0);
        }
        listView.setAdapter(timestampAdapter);
    }

    public void saveFile(String file, String Entry){
//
    String State;
    State = Environment.getExternalStorageState();

    if(State.equals(Environment.MEDIA_MOUNTED)) {
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 20);
            return;
        } else {
            fileWrite(file,Entry);
        }
    }
        else{
            Toast.makeText(getContext(), "SDCARD not Found!!!", Toast.LENGTH_SHORT).show();
        }
    }
    private void fileWrite(String f_string, String entry) {
        File root = Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/mobileComputing");
        if (!dir.exists()) {
            dir.mkdir();
        }

        File file = new File(dir, f_string);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file,true);
            fileOutputStream.write(entry.getBytes());
            fileOutputStream.close();
            Toast.makeText(getContext(), "SAVED  to " + getContext().getFilesDir(), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error Saving File!!", Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"OnResume State");
        if (MainActivity.sharedPrefs != null)
        {
            Log.d(TAG,"SharedPreference Worked!!!");

            String label = MainActivity.sharedPrefs.getString("label","");
            String record_toggle = MainActivity.sharedPrefs.getString("record_toggle","");
            String accel_flag = MainActivity.sharedPrefs.getString("Accel_flag","");
            String gps_flag = MainActivity.sharedPrefs.getString("GPS_flag","");
            if("Stationary".equalsIgnoreCase(label))
            {
                stationary.setChecked(true);
            }else if("walking".equalsIgnoreCase(label))
            {
                walking.setChecked(true);
            }
            if("true".equalsIgnoreCase(record_toggle))
            {
                toggleButton.setChecked(true);
                MainActivity.record_toggle = true;
                record.setText("Stop Recording");
            }else{
                toggleButton.setChecked(false);
                MainActivity.record_toggle = false;
                record.setText("Start Recording");
            }
            if("true".equalsIgnoreCase(accel_flag))
            {
                MainActivity.accel_flag = true;
            }
            if("true".equalsIgnoreCase(gps_flag))
            {
                MainActivity.gps_flag = true;
            }
        }

        sensorManager.unregisterListener(sensorEventListener);
        if(MainActivity.record_toggle && MainActivity.accel_flag)
            sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        listView.setAdapter(timestampAdapter);
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG,"OnPaused Fragment");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG,"Onstop Fragment");

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG,"OnAttach Fragment");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG,"Ondetach Fragment");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"OnDestroy Fragment");
    }


    /****Getting the Timestamp***/
    public String getTimestamp() {
        String currentDateTime =java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        String timestamp = new SimpleDateFormat("HH:mm:ss").format(new Date(currentDateTime));
        return timestamp;
    }



    /***** Check the GPS Status: Enabled or Disabled ****/

    public Boolean displayGpsStatus() {
        ContentResolver contentResolver = getContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }

    /*----------Method to create an AlertBox ------------- */
    protected void alertbox(String title, String mymessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Your Device's GPS is Disable")
                .setCancelable(false)
                .setTitle("** Gps Status **")
                .setPositiveButton("Gps On",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // finish the current activity
                                // AlertBoxAdvance.this.finish();
                                Intent myIntent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(myIntent);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // cancel the dialog box
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}

