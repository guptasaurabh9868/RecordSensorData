package com.mobilecomputing.guptas.a173059002_hw3;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

/**
 * Created by guptas on 3/3/18.
 */

public class SensorFragment extends Fragment {

    private static final String TAG = "Sensor";
    private CheckBox accel, gps;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_sensor,container,false);

        Log.d(TAG,"Oncreate : Strating");

        accel = view.findViewById(R.id.accel);
        gps = view.findViewById(R.id.gps);

        accel.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    MainActivity.accel_flag = true;
                    Toast.makeText(getContext(),"Accelerometer Selected",Toast.LENGTH_SHORT).show();
                }
                else{
                    MainActivity.accel_flag = false;
                    Toast.makeText(getContext(),"Accelerometer Removed",Toast.LENGTH_SHORT).show();
                }
            }
        });
        gps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    MainActivity.gps_flag = true;
                    Toast.makeText(getContext(),"GPS Selected",Toast.LENGTH_SHORT).show();
                }
                else{
                    MainActivity.gps_flag = false;
                    Toast.makeText(getContext(),"GPS Removed",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"OnResume State");
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

    /**
     * Called when the fragment is no longer attached to its activity.  This
     * is called after {@link #onDestroy()}.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG,"Ondetach Fragment");
    }

    /**
     * Called when the view previously created by {@link #onCreateView} has
     * been detached from the fragment.  The next time the fragment needs
     * to be displayed, a new view will be created.  This is called
     * after {@link #onStop()} and before {@link #onDestroy()}.  It is called
     * <em>regardless</em> of whether {@link #onCreateView} returned a
     * non-null view.  Internally it is called after the view's state has
     * been saved but before it has been removed from its parent.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"OnDestroy Fragment");
    }
}
