package com.mobilecomputing.guptas.a173059002_hw3;

/**
 * Created by guptas on 3/3/18.
 */

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends Fragment {

    private static final String TAG = "Login";
    private Button submit;
    private EditText first_name, last_name, mobile, email, age;
    private RadioGroup gender;
    private RadioButton male, female;
    private TextView errormobile, erroremail, errorage;
    private String mobilepattern, emailPattern;
    private Boolean flag_mob=false,flag_em=false, flag_age=false ;


    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_login,container,false);
        Log.d(TAG,"OncreateView fragment");

        // Finding Items from the view
    first_name = view.findViewById(R.id.firstname);
    last_name =  view.findViewById(R.id.lastname);
    mobile =  view.findViewById(R.id.mobile);
    email =  view.findViewById(R.id.email);
    age =  view.findViewById(R.id.age);
    gender = view.findViewById(R.id.gen_radio);
    male = view.findViewById(R.id.gen_male);
    female = view.findViewById(R.id.gen_female);
    submit = view.findViewById(R.id.submit);
    errormobile = view.findViewById(R.id.errormobile);
    erroremail = view.findViewById(R.id.erroremail);
    errorage = view.findViewById(R.id.errorage);

    mobilepattern = "^[789]\\d{9}$";
    emailPattern = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$";


        mobile.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(mobile.getText().toString().trim().matches(mobilepattern))
            {
                errormobile.setText("Valid Mobile");
                errormobile.setVisibility(View.VISIBLE);
                flag_mob = true;
                Log.d("flag_mob","true");

            }else{

                errormobile.setText("InValid Mobile");
                errormobile.setVisibility(View.VISIBLE);
                flag_mob = false;
            }
        }
    });

    email.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(email.getText().toString().trim().matches(emailPattern))
            {
                erroremail.setText("Valid Email.");
                erroremail.setVisibility(View.VISIBLE);
                flag_em = true;
                Log.d("flag_em","true");
            }else{
                //Toast.makeText(getApplicationContext(),"Email is InValid\n Please Correct Email First",Toast.LENGTH_SHORT).show();
                erroremail.setText("Invalid Email.");
                erroremail.setVisibility(View.VISIBLE);
                flag_em = false;
            }
        }
    });
        age.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String Age = age.getText().toString();
            if(!Age.isEmpty() && Integer.parseInt(Age) < 100)
            {
                errorage.setText("Valid Age");
                errorage.setVisibility(View.VISIBLE);
                flag_age = true;
                Log.d("flag_age","true");
            }else{

                errorage.setText("InValid Age");
                errorage.setVisibility(View.VISIBLE);
                flag_age = false;
            }
        }
    });

        submit.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if(isEmptyField(first_name) && isEmptyField(last_name) && flag_mob && flag_em && flag_age && (male.isChecked() || female.isChecked())) {

                MainActivity.Entry = first_name.getText().toString() +","+ last_name.getText().toString() + "," + email.getText().toString()
                        + "," + mobile.getText().toString() + "," + age.getText().toString() + "," +
                        (male.isChecked() ? male.getText().toString(): female.getText().toString()) + "\n";

                first_name.setText("");
                last_name.setText("");
                email.setText("");
                mobile.setText("");
                age.setText("0", TextView.BufferType.EDITABLE);
                age.getText().clear();
                gender.clearCheck();
                errorage.setVisibility(View.GONE);
                erroremail.setVisibility(View.GONE);
                errormobile.setVisibility(View.GONE);

            }else{
                Toast.makeText(getContext(),"Please Fill the Form Correctly First!!!",Toast.LENGTH_SHORT).show();
            }
        }

        });
        return view;
    }

    private boolean isEmptyField(EditText ed) {
        if(!ed.getText().toString().isEmpty()){
            Log.d(ed.getText().toString(),"true");
            return true;
        }
        return false;
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"OnResume State");
        errorage.setVisibility(View.GONE);
        erroremail.setVisibility(View.GONE);
        errormobile.setVisibility(View.GONE);
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
