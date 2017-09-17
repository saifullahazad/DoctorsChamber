package com.dotsys.doctorschamber.util;

import com.dotsys.doctorschamber.Models.LoginInfo;
import com.dotsys.doctorschamber.Models.TreatmentRequest;
import com.dotsys.doctorschamber.Models.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Azad on 16-Jul-17.
 */

public class Global {
    public static UserInfo loggedInUser= null;
    public static List<TreatmentRequest> myPendingRequest= new ArrayList<TreatmentRequest>();
    public static List<TreatmentRequest> myClosedRequest= null;
}
