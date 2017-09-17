package com.dotsys.doctorschamber.Models;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Azad on 17-Jul-17.
 */

public class UserInfo {

    /////////////////////////////////////////////Database Related Start\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
    public static final String TAG = UserInfo.class.getSimpleName();
    public static final String TABLE = "UserInfo";

    // Labels Table Columns names
    public static final String Col_fullName = "fullName";
    public static final String Col_userEmail = "userEmail";
    public static final String Col_userPass = "userPass";
    public static final String Col_userGender = "userGender";
    public static final String Col_birthday = "birthday";
    public static final String Col_contactNo = "contactNo";
    public static final String Col_address = "address";
    public static final String Col_holdingType = "holdingType";
    public static final String Col_userType = "userType";
    public static final String Col_userPhoto = "userPhoto";
    /////////////////////////////////////////////Database Related End\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    public UserInfo() {

    }

    /**
     * Initiate New User Info
     *
     * @param fullName
     * @param userEmail
     * @param userPass
     * @param userGender  Only Support M or F
     * @param birthday    Only Support YYYY-MM-DD Format
     * @param contactNo
     * @param address
     * @param holdingType Only Support Owner or Rent
     * @param userType    Only Support Patient or Doctor
     * @param userPhoto   Image as string
     */
    public UserInfo(String fullName, String userEmail, String userPass, String userGender, String birthday
            , String contactNo, String address, String holdingType, String userType, String userPhoto) {
        _fullName = fullName;
        _userEmail = userEmail;
        _userPass = userPass;
        _userGender = userGender;
        _birthday = birthday;
        _contactNo = contactNo;
        _address = address;
        _holdingType = holdingType;
        _userType = userType;
        _userPhoto = userPhoto;
    }

    public UserInfo(JSONObject jUser) {
        try {
            _userId = jUser.getInt("userId");
            _fullName = jUser.getString("fullName");
            _userEmail = jUser.getString("userEmail");
            _userPass = jUser.getString("userPass");
            _userGender = jUser.getString("userGender");
            _birthday = jUser.getString("birthday");
            _contactNo = jUser.getString("contactNo");
            _address = jUser.getString("address");
            _holdingType = jUser.getString("holdingType");
            _userType = jUser.getString("userType");
            _userPhoto = jUser.getString("userPhoto");

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    private int _userId;

    public int GET_userId() {
        return _userId;
    }

    public void SET_userId(int userId) {
        _userId = userId;
    }

    private String _fullName;

    public String GET_fullName() {
        return _fullName;
    }

    public void SET_fullName(String fullName) {
        _fullName = fullName;
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

    private String _userGender; // M for Male; F for Female; O for Other

    /**
     * @return M for Male; F for Female
     */
    public String GET_userGender() {
        return _userGender;
    }

    /**
     * @param userGender: M for Male; F for Female
     */
    public void SET_userGender(String userGender) {
        _userGender = userGender;
    }

    private String _birthday;

    /**
     * @return Return As 'YYYY-MM-DD'
     */
    public String GET_birthday() {
        return _birthday;
    }
    /**
     * @param birthday must be 'YYYY-MM-DD' formated
     */
    public void SET_birthday(String birthday) {
        _birthday = birthday;
    }

    public int GET_Age(){
        String birthYear = _birthday.substring(0, 4);
        int byInt = Integer.valueOf(birthYear);
        int cdInt = Calendar.getInstance().get(Calendar.YEAR);
        return cdInt-byInt;
    }

    private String _contactNo;

    public String GET_contactNos() {
        return _contactNo;
    }

    public void SET_contactNo(String contactNo) {
        _contactNo = contactNo;
    }

    private String _address;

    public String GET_address() {
        return _address;
    }

    public void SET_address(String address) {
        _address = address;
    }

    private String _holdingType;

    /**
     * @return Only Support Owner or Rent
     */
    public String GET_holdingType() {
        return _holdingType;
    }

    /**
     * @param holdingType Only Support Owner or Rent
     */
    public void SET_holdingType(String holdingType) {
        _holdingType = holdingType;
    }

    private String _userType;

    /**
     * @return Only Support Patient or Doctor
     */
    public String GET_userType() {
        return _userType;
    }

    /**
     * @param userType Only Support Patient or Doctor
     */
    public void SET_userType(String userType) {
        _userType = userType;
    }

    private String _userPhoto;

    public String GET_userPhoto() {
        return _userPhoto;
    }

    public void SET_userPhoto(String userPhoto) {
        _userPhoto = userPhoto;
    }

    public ArrayList<NameValuePair> toNameValuePairList() {
        ArrayList<NameValuePair> patient = new ArrayList<NameValuePair>();
        try {
            patient.add(new BasicNameValuePair("userId", String.valueOf(_userId)));
            patient.add(new BasicNameValuePair("fullName", _fullName));
            patient.add(new BasicNameValuePair("userEmail", _userEmail));
            patient.add(new BasicNameValuePair("userPass", _userPass));
            patient.add(new BasicNameValuePair("userGender", _userGender));
            patient.add(new BasicNameValuePair("birthday", _birthday));
            patient.add(new BasicNameValuePair("contactNo", _contactNo));
            patient.add(new BasicNameValuePair("address", _address));
            patient.add(new BasicNameValuePair("holdingType", _holdingType));
            patient.add(new BasicNameValuePair("userType", _userType));
            patient.add(new BasicNameValuePair("userPhoto", _userPhoto));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return patient;
    }
}
