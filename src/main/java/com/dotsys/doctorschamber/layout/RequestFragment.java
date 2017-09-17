package com.dotsys.doctorschamber.layout;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dotsys.doctorschamber.App.DCApp;
import com.dotsys.doctorschamber.Models.TreatmentRequest;
import com.dotsys.doctorschamber.Models.UserInfo;
import com.dotsys.doctorschamber.R;
import com.dotsys.doctorschamber.repository.TreatmentRequestRepository;
import com.dotsys.doctorschamber.util.Global;
import com.dotsys.doctorschamber.util.PermissionManager;
import com.dotsys.doctorschamber.util.Utility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;
import java.util.Date;


public class RequestFragment extends android.app.Fragment implements OnMapReadyCallback {
    View myView, request_view, mProgressView;



    AutoCompleteTextView relation_TextView, patientName_TextView, currentDiseases_TextView,preDiseases_TextView,symptom_TextView;
    EditText patientAge_TextView;
    RadioGroup gender_radioGroup;
    RadioButton own_radioButton, relative_radioButton, male_radioButton, female_radioButton, other_radioButton, gender_selectedRadio;
    Button gmap_show_button, request_button;
    Spinner doctorGender_Spinner;
    CheckBox diabetes_CheckBox,pressure_CheckBox,asthma_CheckBox,ambulance_CheckBox;

    private MapView mMapView;
    private GoogleMap mGoogleMap;
    LatLng latLng;
    private LocationManager mLocationManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_request,container,false);

        TextView title=(TextView) myView.findViewById(R.id.title);
        title.setText(getString(R.string.title_fragment_request));

        request_view = myView.findViewById(R.id.request_view);
        mProgressView = myView.findViewById(R.id.progressBar);

        own_radioButton = (RadioButton) myView.findViewById(R.id.own_radioButton);
        own_radioButton.setOnClickListener(own_radioButton_Click);
        relative_radioButton = (RadioButton) myView.findViewById(R.id.relative_radioButton);
        relative_radioButton.setOnClickListener(relative_radioButton_Click);

        relation_TextView = (AutoCompleteTextView) myView.findViewById(R.id.relation_TextView);
        patientName_TextView = (AutoCompleteTextView) myView.findViewById(R.id.patientName_TextView);
        patientAge_TextView = (EditText) myView.findViewById(R.id.patientAge_TextView);
        gender_radioGroup = (RadioGroup) myView.findViewById(R.id.gender_radioGroup);
        male_radioButton = (RadioButton) myView.findViewById(R.id.male_radioButton);
        female_radioButton = (RadioButton) myView.findViewById(R.id.female_radioButton);
        other_radioButton = (RadioButton) myView.findViewById(R.id.other_radioButton);

        diabetes_CheckBox = (CheckBox) myView.findViewById(R.id.diabetes_CheckBox);
        pressure_CheckBox = (CheckBox) myView.findViewById(R.id.pressure_CheckBox);
        asthma_CheckBox = (CheckBox) myView.findViewById(R.id.asthma_CheckBox);
        currentDiseases_TextView = (AutoCompleteTextView) myView.findViewById(R.id.currentDiseases_TextView);
        preDiseases_TextView = (AutoCompleteTextView) myView.findViewById(R.id.preDiseases_TextView);
        symptom_TextView = (AutoCompleteTextView) myView.findViewById(R.id.symptom_TextView);

        doctorGender_Spinner = (Spinner) myView.findViewById(R.id.doctorGender_Spinner);
        doctorGender_Spinner.setVisibility(View.GONE);
        ambulance_CheckBox = (CheckBox) myView.findViewById(R.id.ambulance_CheckBox);

        gmap_show_button = (Button) myView.findViewById(R.id.gmap_show_button);
        gmap_show_button.setOnClickListener(gmap_show_button_click);

        request_button = (Button) myView.findViewById(R.id.request_button);
        request_button.setOnClickListener(request_button_click);

        LoadOwnData();

        PermissionManager pm = new PermissionManager();
        if (!pm.canAccessLocation() || !pm.canAccessContacts()) {
            ActivityCompat.requestPermissions(this.getActivity(), PermissionManager.INITIAL_PERMS, PermissionManager.INITIAL_REQUEST);
        }

        mLocationManager = (LocationManager) this.getActivity().getSystemService(this.getActivity().LOCATION_SERVICE);

        try{
            Location loc=mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(loc != null) latLng = new LatLng(loc.getLatitude(), loc.getLongitude());
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5,5,mLocationListener);
        }catch (SecurityException se){
            throw se;
        }




        return myView;
    }

    RadioButton.OnClickListener own_radioButton_Click=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            LoadOwnData();
        }
    };

    RadioButton.OnClickListener relative_radioButton_Click=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ResetForRelative();
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(DCApp.getContext());
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Add a marker in Sydney, Australia, and move the camera.
        if(latLng==null)
        {

            latLng = new LatLng(23.752478828201518, 90.39267271757126);
        }

        googleMap.addMarker(new MarkerOptions().position(latLng).title("My Location").snippet("I am here"));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));


    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if(location!=null){
                latLng=new LatLng(location.getLatitude(),location.getLongitude());
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

    Button.OnClickListener gmap_show_button_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try{
                OpenPopup(v.getContext());
            }catch (Exception e){
                Toast.makeText(v.getContext(), e.getMessage(),Toast.LENGTH_SHORT);
            }

        }
    };

    Button.OnClickListener request_button_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            attemptTreatementRequest();
        }
    };

    private void OpenPopup(Context context){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_map);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        TextView title = (TextView) dialog.findViewById(R.id.title);
        title.setText(getString(R.string.title_fragment_gmap));

        Button cancel_button = (Button) dialog.findViewById(R.id.cancel_button);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button ok_button = (Button) dialog.findViewById(R.id.ok_button);
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        mMapView = (MapView) dialog.findViewById(R.id.mapView);
        if(mMapView != null){
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dialogWidth = (int)(displayMetrics.widthPixels * 0.85);
        int dialogHeight = (int)(displayMetrics.heightPixels * 0.85);
        dialog.getWindow().setLayout(dialogWidth, dialogHeight);

//        TableLayout tl = (TableLayout) dialog.findViewById(R.id.buttons_table);
//        DisplayMetrics dm = tl.getResources().getDisplayMetrics();
//        int dh = (int)(dialogHeight - displayMetrics.heightPixels);
//        ViewGroup.LayoutParams lp =new AbsListView.LayoutParams(dm.widthPixels, dh);
//        mMapView.setLayoutParams(lp);

        dialog.show();
    }

    private void LoadOwnData(){
        if(Global.loggedInUser!=null){
            UserInfo u = Global.loggedInUser;
            relation_TextView.setText("Myself");
            relation_TextView.setEnabled(false);
            patientName_TextView.setText(u.GET_fullName());
            patientName_TextView.setEnabled(false);
            patientAge_TextView.setText(String.valueOf(u.GET_Age()));
            patientAge_TextView.setEnabled(false);
            if(u.GET_userGender().equals("M")) male_radioButton.setChecked(true);
            else if(u.GET_userGender().equals("F")) female_radioButton.setChecked(true);
            else other_radioButton.setChecked(true);
            gender_radioGroup.setEnabled(false);
            male_radioButton.setEnabled(false);
            female_radioButton.setEnabled(false);
            other_radioButton.setEnabled(false);
        }
    }

    private void ResetForRelative(){
        relation_TextView.setText("");
        relation_TextView.setEnabled(true);
        patientName_TextView.setText("");
        patientName_TextView.setEnabled(true);
        patientAge_TextView.setText("1");
        patientAge_TextView.setEnabled(true);
        gender_radioGroup.setEnabled(true);
        male_radioButton.setEnabled(true);
        female_radioButton.setEnabled(true);
        other_radioButton.setEnabled(true);
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            request_view.setVisibility(show ? View.GONE : View.VISIBLE);
            request_view.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    request_view.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            request_view.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptTreatementRequest() {
        boolean cancel = false;
        View focusView = null;

        // Reset errors.
        //email.setError(null);


        // Store values at the time of the registration attempt.
        String relationValue = relation_TextView.getText().toString();
        if (TextUtils.isEmpty(relationValue)) {
            relation_TextView.setError(getString(R.string.error_field_required));
            focusView = relation_TextView;
            cancel = true;
        }

        String patientNameValue = patientName_TextView.getText().toString();
        if (TextUtils.isEmpty(patientNameValue)) {
            patientName_TextView.setError(getString(R.string.error_field_required));
            focusView = patientName_TextView;
            cancel = true;
        }

        int selected_gender_radio_id = gender_radioGroup.getCheckedRadioButtonId();
        gender_selectedRadio = (RadioButton) myView.findViewById(selected_gender_radio_id);
        String patientGenderValue = Utility.GET_GenderValue(gender_selectedRadio.getText().toString());;

        if (TextUtils.isEmpty(patientAge_TextView.getText())) {
            patientAge_TextView.setError(getString(R.string.error_field_required));
            focusView = patientAge_TextView;
            cancel = true;
        }
        int patientAgeValue =Integer.parseInt(patientAge_TextView.getText().toString());

        double latitude = latLng.latitude;
        double longitude = latLng.longitude;

        String diabetesValue = (diabetes_CheckBox.isChecked()?"Yes":"No");
        String pressureValue = (pressure_CheckBox.isChecked()?"Yes":"No");
        String asthmaValue = (asthma_CheckBox.isChecked()?"Yes":"No");

        String currentDiseasesValue = currentDiseases_TextView.getText().toString();
        String preDiseasesValue = preDiseases_TextView.getText().toString();
        String symptomValue = symptom_TextView.getText().toString();
        if (TextUtils.isEmpty(symptomValue)) {
            symptom_TextView.setError(getString(R.string.error_field_required));
            focusView = symptom_TextView;
            cancel = true;
        }

        String doctorGenderValue = doctorGender_Spinner.getSelectedItem().toString();
        if(doctorGenderValue==getString(R.string.prompt_preference_doctor)){
            Toast.makeText(doctorGender_Spinner.getContext(),getString(R.string.error_field_required),Toast.LENGTH_LONG);
            focusView = doctorGender_Spinner;
            cancel = true;
        }
        doctorGenderValue = Utility.GET_GenderValue(doctorGenderValue);

        String ambulanceValue = (ambulance_CheckBox.isChecked()?"Yes":"No");
        Date CurrentDateTime = Calendar.getInstance().getTime();
        String requestDateValue = Utility.DateToString(CurrentDateTime,"yyyy-MM-dd");
        String requestTimeValue = Utility.DateToString(CurrentDateTime,"hh:mm a");

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
             showProgress(true);

            TreatmentRequest treatmentRequest = new TreatmentRequest(Global.loggedInUser.GET_userEmail()
                    ,relationValue,patientNameValue, patientGenderValue, patientAgeValue, latitude, longitude,diabetesValue,pressureValue,asthmaValue
                    , currentDiseasesValue, symptomValue, preDiseasesValue, doctorGenderValue, ambulanceValue, requestDateValue, requestTimeValue);
            TreatmentRequestTask mAuthTask = new TreatmentRequestTask(treatmentRequest);
            mAuthTask.execute((Void) null);
        }
    }


    public class TreatmentRequestTask extends AsyncTask<Void, Void, Boolean> {

        private final TreatmentRequest _treatmentRequest;


        TreatmentRequestTask(TreatmentRequest treatmentRequest) {
            _treatmentRequest = treatmentRequest;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                TreatmentRequestRepository repository = new TreatmentRequestRepository();
                return repository.RequestNow(_treatmentRequest);
            } catch (Exception e) {
                Log.d("Error",e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            showProgress(false);

            if (success) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, new PendingRequestsFragment()).addToBackStack(null).commit();
            } else {
                Toast.makeText(myView.getContext(),"Request Failed, Please check your internet & location service", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {

            showProgress(false);
        }
    }


}
