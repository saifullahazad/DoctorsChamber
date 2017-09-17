package com.dotsys.doctorschamber.layout;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClosedRequestsFragment extends android.app.Fragment {

    View myView, request_view, mProgressView;
    android.app.Fragment closedRequestsFragment;

    TableLayout request_table;
    TreatmentRequest request;
    Button addnew_button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_pending_requests,container,false);
        closedRequestsFragment=this;
        request_view = myView.findViewById(R.id.request_view);
        mProgressView = myView.findViewById(R.id.progressBar);

        TextView title=(TextView) myView.findViewById(R.id.title);
        title.setText(getString(R.string.title_fragment_closed_requests));

        addnew_button=(Button) myView.findViewById(R.id.addnew_button);
        addnew_button.setOnClickListener(addnew_button_click);
        if(Global.loggedInUser.GET_userType().equals("Doctor"))
            addnew_button.setVisibility(View.GONE);

        request_table = (TableLayout) myView.findViewById(R.id.request_table);

        attemptGetPendingList();

        return myView;
    }

    Button.OnClickListener addnew_button_click=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, new RequestFragment()).addToBackStack(null).commit();
        }
    };

    private TreatmentRequest CreateRequest(){
        TreatmentRequest request=new TreatmentRequest();
        request.SET_treatmentId(3);
        Calendar c = Calendar.getInstance();
        request.SET_requestDate(Utility.DateToString(c.getTime(),"yyyy-MM-dd"));
        request.SET_requestTime(Utility.DateToString(c.getTime(),"hh:mm a"));
        return request;
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

    public void attemptGetPendingList(){
        showProgress(true);

        TreatmentRequestTask mAuthTask = new TreatmentRequestTask();
        mAuthTask.execute((Void) null);
    }

    public class TreatmentRequestTask extends AsyncTask<Void, Void, Boolean> {



        TreatmentRequestTask() {

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                TreatmentRequestRepository repository = new TreatmentRequestRepository();
                repository.MyClosedRequests();
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
                List<TreatmentRequest> myClosedRequests = Global.myClosedRequest;
                for (TreatmentRequest request : myClosedRequests) {
                    // do something with object
                    RequestTableRow requestTableRow = new RequestTableRow(closedRequestsFragment.getContext(),closedRequestsFragment,request);
                    TableRow tr = requestTableRow.GET_TableRow();
                    request_table.addView(tr);
                }
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
