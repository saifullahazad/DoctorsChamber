package com.dotsys.doctorschamber.layout;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.dotsys.doctorschamber.Models.TreatmentRequest;
import com.dotsys.doctorschamber.R;
import com.dotsys.doctorschamber.repository.TreatmentRequestRepository;
import com.dotsys.doctorschamber.util.Global;
import com.dotsys.doctorschamber.util.RequestTableRow;
import com.dotsys.doctorschamber.util.Utility;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestDetailFragment extends android.app.Fragment {

    View myView, request_view, mProgressView;

    TextView request_date_TextView, request_time_TextView, relation_TextView, patient_name_TextView, patient_age_TextView, patient_gender_TextView
            , symptom_TextView, need_ambulance_TextView, preference_doctor_TextView, diabetes_TextView, pressure_TextView, asthma_TextView, currentDiseases_TextView, preDiseases_TextView
            , doctorCharge_TextView, ambulanceCharge_TextView, treatmentStatus_TextView, paymentStatus_TextView
            , treatmentInfo_TextView, userCommand_TextView, servey_report_TextView;
    Button cancel_button, confirm_button, complete_button, done_button;
    TableRow preference_doctor_TableRow;

    CheckBox payment_CheckBox;

    TableLayout action_table, action_table_doctor;

    private TreatmentRequest request;
    public void SET_BookingRequest(TreatmentRequest bookingRequest){
        request = bookingRequest;
    }

    public RequestDetailFragment() {
        // Required empty public constructor
    }

    public enum ActionType {
        CANCEL,
        CONFIRM,
        DONE,
        COMPLETE
    }

    ActionType currentActionType=ActionType.CANCEL;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_request_detail,container,false);
        request_view = myView.findViewById(R.id.request_view);
        mProgressView = myView.findViewById(R.id.progressBar);

        TextView title=(TextView) myView.findViewById(R.id.title);
        title.setText(getString(R.string.title_fragment_request_detail));

        SetData();
        if(request.GET_requestStatus().equals("Closed"))
            SetActionControlForClosed();
        else if(Global.loggedInUser.GET_userType().equals("Patient"))
            SetActionControlForPatient();
        else
            SetActionControlForDoctor();

        preference_doctor_TableRow = (TableRow) myView.findViewById(R.id.preference_doctor_TableRow);
        preference_doctor_TableRow.setVisibility(View.GONE);
        // Inflate the layout for this fragment
        return myView;
    }

    private void SetActionControlForClosed(){
        action_table.setVisibility(View.GONE);
        action_table_doctor.setVisibility(View.GONE);
        payment_CheckBox.setVisibility(View.GONE);
        servey_report_TextView.setVisibility(View.GONE);
    }

    private void SetActionControlForPatient(){
        action_table.setVisibility(View.VISIBLE);
        action_table_doctor.setVisibility(View.GONE);
        payment_CheckBox.setVisibility(View.GONE);

        cancel_button.setEnabled(false);
        confirm_button.setEnabled(false);
        complete_button.setEnabled(false);
        if(!request.GET_requestStatus().equals("Pending")) return;

        cancel_button.setEnabled(true);
        if(request.GET_doctorCharge()>0) confirm_button.setEnabled(true);

        if(request.GET_chargeStatus().equals("Confirmed")){
            cancel_button.setEnabled(false);
            confirm_button.setEnabled(false);
            complete_button.setEnabled(true);
        }
    }

    private void SetActionControlForDoctor(){
        action_table.setVisibility(View.GONE);
        action_table_doctor.setVisibility(View.VISIBLE);
        payment_CheckBox.setVisibility(View.VISIBLE);

        if(request.GET_chargeStatus().equals("Confirmed")){
            done_button.setEnabled(true);
        }
        else
            done_button.setEnabled(false);
    }

    private void SetData(){
        try{
            request_date_TextView=(TextView) myView.findViewById(R.id.request_date_TextView);
            request_time_TextView=(TextView) myView.findViewById(R.id.request_time_TextView);
            relation_TextView=(TextView) myView.findViewById(R.id.relation_TextView);
            patient_name_TextView=(TextView) myView.findViewById(R.id.patient_name_TextView);
            patient_age_TextView=(TextView) myView.findViewById(R.id.patient_age_TextView);
            patient_gender_TextView=(TextView) myView.findViewById(R.id.patient_gender_TextView);
            diabetes_TextView=(TextView) myView.findViewById(R.id.diabetes_TextView);
            pressure_TextView=(TextView) myView.findViewById(R.id.pressure_TextView);
            asthma_TextView=(TextView) myView.findViewById(R.id.asthma_TextView);
            currentDiseases_TextView=(TextView) myView.findViewById(R.id.currentDiseases_TextView);
            preDiseases_TextView=(TextView) myView.findViewById(R.id.preDiseases_TextView);
            symptom_TextView=(TextView) myView.findViewById(R.id.symptom_TextView);
            need_ambulance_TextView=(TextView) myView.findViewById(R.id.need_ambulance_TextView);
            preference_doctor_TextView=(TextView) myView.findViewById(R.id.preference_doctor_TextView);
            doctorCharge_TextView=(TextView) myView.findViewById(R.id.doctorCharge_TextView);
            ambulanceCharge_TextView=(TextView) myView.findViewById(R.id.ambulanceCharge_TextView);
            treatmentStatus_TextView=(TextView) myView.findViewById(R.id.treatmentStatus_TextView);
            paymentStatus_TextView=(TextView) myView.findViewById(R.id.paymentStatus_TextView);
            treatmentInfo_TextView=(TextView) myView.findViewById(R.id.treatmentInfo_TextView);
            userCommand_TextView=(TextView) myView.findViewById(R.id.userCommand_TextView);
            payment_CheckBox=(CheckBox) myView.findViewById(R.id.payment_CheckBox);
            servey_report_TextView=(TextView) myView.findViewById(R.id.servey_report_TextView);
            action_table=(TableLayout) myView.findViewById(R.id.action_table);
            action_table_doctor=(TableLayout) myView.findViewById(R.id.action_table_doctor);
            cancel_button=(Button) myView.findViewById(R.id.cancel_button);
            cancel_button.setOnClickListener(cancel_button_click);
            confirm_button=(Button) myView.findViewById(R.id.confirm_button);
            confirm_button.setOnClickListener(confirm_button_click);
            complete_button=(Button) myView.findViewById(R.id.complete_button);
            complete_button.setOnClickListener(complete_button_click);
            done_button=(Button) myView.findViewById(R.id.done_button);
            done_button.setOnClickListener(done_button_click);


            request_date_TextView.setText(request.GET_requestDate());
            request_time_TextView.setText(request.GET_requestTime());
            relation_TextView.setText(request.GET_patientRelation());
            patient_name_TextView.setText(request.GET_patientName());
            patient_age_TextView.setText(String.valueOf(request.GET_patientAge()));
            patient_gender_TextView.setText(Utility.GET_GenderString(request.GET_patientGender()));
            preDiseases_TextView.setText(request.GET_preDiseases());
            diabetes_TextView.setText(request.GET_diabetes());
            pressure_TextView.setText(request.GET_pressure());
            asthma_TextView.setText(request.GET_asthma());
            currentDiseases_TextView.setText(request.GET_currentDiseases());
            symptom_TextView.setText(request.GET_symptom());
            need_ambulance_TextView.setText(request.GET_ambulance());
            preference_doctor_TextView.setText(Utility.GET_GenderString(request.GET_doctorGender()));
            doctorCharge_TextView.setText(String.valueOf(request.GET_doctorCharge()));
            ambulanceCharge_TextView.setText(String.valueOf(request.GET_ambulanceCharge()));
            treatmentStatus_TextView.setText(request.GET_treatmentStatus());
            paymentStatus_TextView.setText(request.GET_paymentStatus());
            treatmentInfo_TextView.setText(request.GET_treatmentInfo());
            userCommand_TextView.setText(request.GET_userCommand());


        }catch (Exception e){
            Toast.makeText(myView.getContext(), e.getMessage(),Toast.LENGTH_SHORT).show();

        }

    }

    Button.OnClickListener cancel_button_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try{
                currentActionType=ActionType.CANCEL;
                attemptToAction();
            }catch (Exception e){
                Toast.makeText(myView.getContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    };

    Button.OnClickListener confirm_button_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try{
                currentActionType=ActionType.CONFIRM;
                attemptToAction();
            }catch (Exception e){
                Toast.makeText(myView.getContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    };

    Button.OnClickListener done_button_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try{
                currentActionType=ActionType.DONE;
                request.SET_treatmentInfo(servey_report_TextView.getText().toString());
                String paymentStatus = (payment_CheckBox.isChecked()?"Paid":"Due");
                request.SET_paymentStatus(paymentStatus);
                attemptToAction();
            }catch (Exception e){
                Toast.makeText(myView.getContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    };

    Button.OnClickListener complete_button_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try{
                currentActionType=ActionType.COMPLETE;
                request.SET_userCommand(servey_report_TextView.getText().toString());
                attemptToAction();
            }catch (Exception e){
                Toast.makeText(myView.getContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    };


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

    public void attemptToAction(){
        showProgress(true);

        TreatmentRequestTask mAuthTask = new TreatmentRequestTask(request);
        mAuthTask.execute((Void) null);
    }

    public class TreatmentRequestTask extends AsyncTask<Void, Void, Boolean> {

        TreatmentRequest _treatmentRequest;

        TreatmentRequestTask(TreatmentRequest treatmentRequest) {
            _treatmentRequest = treatmentRequest;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                TreatmentRequestRepository repository = new TreatmentRequestRepository();
                switch (currentActionType){
                    case CANCEL:{
                        repository.CancelRequests(_treatmentRequest);
                        break;
                    }
                    case CONFIRM:{
                        repository.ConfirmRequests(_treatmentRequest);
                        break;
                    }
                    case DONE:{
                        repository.DoneRequests(_treatmentRequest);
                        break;
                    }
                    case COMPLETE:{
                        repository.CompleteRequests(_treatmentRequest);
                        break;
                    }
                    default:
                        break;
                }
                return true;
            } catch (Exception e) {
                Toast.makeText(request_view.getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
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
                Toast.makeText(request_view.getContext(),"Request Failed, Please check your internet & location service", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {

            showProgress(false);
        }
    }

}
