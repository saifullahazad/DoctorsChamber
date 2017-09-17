package com.dotsys.doctorschamber.layout;


import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dotsys.doctorschamber.MainActivity;
import com.dotsys.doctorschamber.Models.TreatmentRequest;
import com.dotsys.doctorschamber.R;
import com.dotsys.doctorschamber.util.Global;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class SplashFragment extends android.app.Fragment {

    View myView;
    Timer myTimer;

    public SplashFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myView = inflater.inflate(R.layout.fragment_splash,container,false);

        MyTimerTask myTask = new MyTimerTask();
        myTimer = new Timer();

        myTimer.schedule(myTask, 3000);

        return myView;
    }

    class MyTimerTask extends TimerTask {
        public void run() {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, new PendingRequestsFragment()).commit();
            myTimer.cancel();
        }
    }

}
