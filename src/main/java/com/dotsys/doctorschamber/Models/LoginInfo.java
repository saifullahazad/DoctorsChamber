package com.dotsys.doctorschamber.Models;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;

/**
 * Created by Azad on 15-Jul-17.
 */

public class LoginInfo {

    public LoginInfo(String userEmail, String userPass, String userType) {
        _userEmail = userEmail;
        _userPass = userPass;
        _userType = userType;
    }


    private String _userEmail;

    public String GET_userEmail() {
        return _userEmail;
    }

    public void SET_userEmail(String userEmail) {
        _userEmail = userEmail;
    }

    private String _userPass;

    public String GET_userPass() {
        return _userPass;
    }

    public void SET_userPass(String userPass) {
        _userPass = userPass;
    }

    private String _userType;

    public String GET_userType() {
        return _userType;
    }

    public void SET_userType(String userType) {
        _userType = userType;
    }

    public ArrayList<NameValuePair> toNameValuePairList() {
        ArrayList<NameValuePair> user = new ArrayList<NameValuePair>();
        try {
            user.add(new BasicNameValuePair("userEmail", _userEmail));
            user.add(new BasicNameValuePair("userPass", _userPass));
            user.add(new BasicNameValuePair("userType", _userType));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return user;
    }


}
