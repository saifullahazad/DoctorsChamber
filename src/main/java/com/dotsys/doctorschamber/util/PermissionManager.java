package com.dotsys.doctorschamber.util;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.dotsys.doctorschamber.App.DCApp;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Azad on 23-Jul-17.
 */

public class PermissionManager {



    public static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE
    };
    public static final String[] CAMERA_PERMS={
            Manifest.permission.CAMERA
    };
    public static final String[] CONTACTS_PERMS={
            Manifest.permission.READ_CONTACTS
    };
    public static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    public static final String[] CALL_PHONE_PERMS={
            Manifest.permission.CALL_PHONE
    };

    public static final int INITIAL_REQUEST=1337;
    public static final int CAMERA_REQUEST=INITIAL_REQUEST+1;
    public static final int CONTACTS_REQUEST=INITIAL_REQUEST+2;
    public static final int LOCATION_REQUEST=INITIAL_REQUEST+3;
    public static final int CALL_REQUEST=INITIAL_REQUEST+4;

    public boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    public boolean canAccessPhoneCall() {
        return(hasPermission(Manifest.permission.CALL_PHONE));
    }

    public boolean canAccessCamera() {
        return(hasPermission(Manifest.permission.CAMERA));
    }

    public boolean canAccessContacts() {
        return(hasPermission(Manifest.permission.READ_CONTACTS));
    }

    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED== ContextCompat.checkSelfPermission(DCApp.getContext(),perm));
    }



}
