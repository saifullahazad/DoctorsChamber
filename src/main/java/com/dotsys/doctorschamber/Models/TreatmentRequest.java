package com.dotsys.doctorschamber.Models;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Azad on 06-Jul-17.
 */

public class TreatmentRequest {
    public TreatmentRequest(){

    }

    public TreatmentRequest(String patientEmail, String patientRelation, String patientName, String patientGender, int patientAge
                                , double latitude, double longitude, String diabetes, String pressure, String asthma, String currentDiseases, String symptom, String preDiseases
                                , String doctorGender, String ambulance, String requestDate, String requestTime){

        _patientEmail=patientEmail;
        _patientRelation=patientRelation;
        _patientName=patientName;
        _patientGender=patientGender;
        _patientAge=patientAge;
        _latitude=latitude;
        _longitude=longitude;
        _diabetes=diabetes;
        _pressure=pressure;
        _asthma=asthma;
        _currentDiseases=currentDiseases;
        _symptom=symptom;
        _preDiseases=preDiseases;
        _doctorGender=doctorGender;
        _ambulance=ambulance;
        _requestDate=requestDate;
        _requestTime=requestTime;
    }

    public TreatmentRequest(JSONObject jRequest) {
        try {
            _treatmentId = jRequest.getInt("treatmentId");
            _patientEmail=jRequest.getString("patientEmail");
            _patientRelation=jRequest.getString("patientRelation");
            _patientName=jRequest.getString("patientName");
            _patientGender=jRequest.getString("patientGender");
            _patientAge=jRequest.getInt("patientAge");
            _latitude=jRequest.getDouble("latitude");
            _longitude=jRequest.getDouble("longitude");
            _diabetes=jRequest.getString("diabetes");
            _pressure=jRequest.getString("pressure");
            _asthma=jRequest.getString("asthma");
            _currentDiseases=jRequest.getString("currentDiseases");
            _symptom=jRequest.getString("symptom");
            _preDiseases=jRequest.getString("preDiseases");
            _doctorGender=jRequest.getString("doctorGender");
            _ambulance=jRequest.getString("ambulance");
            _requestDate=jRequest.getString("requestDate");
            _requestTime=jRequest.getString("requestTime");
            _doctorCharge=jRequest.getInt("doctorCharge");
            _ambulanceCharge=jRequest.getInt("ambulanceCharge");
            _requestStatus=jRequest.getString("requestStatus");
            _chargeStatus=jRequest.getString("chargeStatus");
            _doctorEmail=jRequest.getString("doctorEmail");
            _treatmentStatus=jRequest.getString("treatmentStatus");
            _paymentStatus=jRequest.getString("paymentStatus");
            _treatmentInfo=jRequest.getString("treatmentInfo");
            _userCommand=jRequest.getString("userCommand");

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

    }

    private int _treatmentId;
    public int GET_treatmentId(){
        return _treatmentId;
    }
    public void SET_treatmentId(int treatmentId){
        _treatmentId=treatmentId;
    }

    private String _patientEmail;
    public String GET_patientEmail(){
        return _patientEmail;
    }
    public void SET_patientEmail(String patientEmail){
        _patientEmail=patientEmail;
    }

    private String _patientRelation;
    public String GET_patientRelation(){
        return _patientRelation;
    }
    public void SET_patientRelation(String patientRelation){
        _patientRelation=patientRelation;
    }
    public boolean GET_IsMyself(){
        return (_patientRelation=="Myself");
    }

    private String _patientName;
    public String GET_patientName(){
        return _patientName;
    }
    public void SET_patientName(String patientName){
        _patientName=patientName;
    }

    private String _patientGender; // M for Male; F for Female
    /**
     * @return M for Male; F for Female
     */
    public String GET_patientGender() {
        return _patientGender;
    }
    /**
     * @param patientGender: M for Male; F for Female
     */
    public void SET_patientGender(String patientGender) {
        _patientGender = patientGender;
    }

    private int _patientAge;
    public int GET_patientAge(){
        return _patientAge;
    }
    public void SET_patientAge(int patientAge){
        _patientAge=patientAge;
    }

    private double _latitude;
    public double GET_latitude(){
        return _latitude;
    }
    public void SET_latitude(double latitude){
        _latitude=latitude;
    }

    private double _longitude;
    public double GET_longitude(){
        return _longitude;
    }
    public void SET_longitude(double longitude){
        _longitude=longitude;
    }

    private String _preDiseases;
    public String GET_preDiseases(){
        return _preDiseases;
    }
    public void SET_preDiseases(String preDiseases){
        _preDiseases=preDiseases;
    }

    private String _diabetes; // Yes or No
    /**
     *
     * @return Yes or No
     */
    public String GET_diabetes(){
        return _diabetes;
    }
    /**
     *
     * @param ambulance: Yes or No
     */
    public void SET_diabetes(String diabetes){
        _diabetes=diabetes;
    }

    private String _pressure; // Yes or No
    /**
     *
     * @return Yes or No
     */
    public String GET_pressure(){
        return _pressure;
    }
    /**
     *
     * @param ambulance: Yes or No
     */
    public void SET_pressure(String pressure){
        _pressure=pressure;
    }

    private String _asthma; // Yes or No
    /**
     *
     * @return Yes or No
     */
    public String GET_asthma(){
        return _asthma;
    }
    /**
     *
     * @param ambulance: Yes or No
     */
    public void SET_asthma(String asthma){
        _asthma=asthma;
    }

    private String _currentDiseases;
    public String GET_currentDiseases(){
        return _currentDiseases;
    }
    public void SET_currentDiseases(String currentDiseases){
        _currentDiseases=currentDiseases;
    }

    private String _symptom;
    public String GET_symptom(){
        return _symptom;
    }
    public void SET_symptom(String symptom){
        _symptom=symptom;
    }

    private String _doctorGender = "M"; // M for Male; F for Female
    /**
     * @return M for Male; F for Female; O for Other
     */
    public String GET_doctorGender() {
        return _doctorGender;
    }
    /**
     * @param doctorGender: M for Male; F for Female; O for Other
     */
    public void SET_doctorGender(String doctorGender) {
        _doctorGender = doctorGender;
    }

    private String _ambulance; // Yes or No
    /**
     *
     * @return Yes or No
     */
    public String GET_ambulance(){
        return _ambulance;
    }
    /**
     *
     * @param ambulance: Yes or No
     */
    public void SET_ambulance(String ambulance){
        _ambulance=ambulance;
    }

    private String _requestDate;
    public String GET_requestDate(){
        return _requestDate;
    }
    public void SET_requestDate(String requestDate){
        _requestDate=requestDate;
    }

    private String _requestTime;
    public String GET_requestTime(){
        return _requestTime;
    }
    public void SET_requestTime(String requestTime){
        _requestTime=requestTime;
    }

    private int _doctorCharge=0;
    public int GET_doctorCharge(){
        return _doctorCharge;
    }
    public void SET_doctorCharge(int doctorCharge){
        _doctorCharge=doctorCharge;
    }

    private int _ambulanceCharge=0;
    public int GET_ambulanceCharge(){
        return _ambulanceCharge;
    }
    public void SET_ambulanceCharge(int ambulanceCharge){
        _ambulanceCharge=ambulanceCharge;
    }

    private String _requestStatus = "Pending"; // Closed or Pending
    public String GET_requestStatus(){
        return _requestStatus;
    }
    public void SET_requestStatus(String requestStatus){
        _requestStatus=requestStatus;
    }

    private String _chargeStatus = ""; // Confirmed or Canceled
    public String GET_chargeStatus(){
        return _chargeStatus;
    }
    public void SET_chargeStatus(String chargeStatus){
        _chargeStatus=chargeStatus;
    }

    private String _doctorEmail;
    public String GET_doctorEmail(){
        return _doctorEmail;
    }
    public void SET_doctorEmail(String doctorEmail){
        _doctorEmail=doctorEmail;
    }

    private String _treatmentStatus = "Not Taken"; // Taken or Not Taken
    public String GET_treatmentStatus(){
        return _treatmentStatus;
    }
    public void SET_treatmentStatus(String treatmentStatus){
        _treatmentStatus=treatmentStatus;
    }

    private String _paymentStatus = "Due"; // Paid or Due
    public String GET_paymentStatus(){
        return _paymentStatus;
    }
    public void SET_paymentStatus(String paymentStatus){
        _paymentStatus=paymentStatus;
    }

    private String _treatmentInfo;
    public String GET_treatmentInfo(){
        return _treatmentInfo;
    }
    public void SET_treatmentInfo(String treatmentInfo){
        _treatmentInfo=treatmentInfo;
    }

    private String _userCommand;
    public String GET_userCommand(){
        return _userCommand;
    }
    public void SET_userCommand(String userCommand){
        _userCommand=userCommand;
    }

    public ArrayList<NameValuePair> toNameValuePairList() {
        ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
        try {
            param.add(new BasicNameValuePair("treatmentId", String.valueOf(_treatmentId)));
            param.add(new BasicNameValuePair("patientEmail", _patientEmail));
            param.add(new BasicNameValuePair("patientRelation", _patientRelation));
            param.add(new BasicNameValuePair("patientName", _patientName));
            param.add(new BasicNameValuePair("patientGender", _patientGender));
            param.add(new BasicNameValuePair("patientAge", String.valueOf(_patientAge)));
            param.add(new BasicNameValuePair("latitude", String.valueOf(_latitude)));
            param.add(new BasicNameValuePair("longitude", String.valueOf(_longitude)));
            param.add(new BasicNameValuePair("diabetes", _diabetes));
            param.add(new BasicNameValuePair("pressure", _pressure));
            param.add(new BasicNameValuePair("asthma", _asthma));
            param.add(new BasicNameValuePair("currentDiseases", _currentDiseases));
            param.add(new BasicNameValuePair("symptom", _symptom));
            param.add(new BasicNameValuePair("preDiseases", _preDiseases));
            param.add(new BasicNameValuePair("doctorGender", _doctorGender));
            param.add(new BasicNameValuePair("ambulance", _ambulance));
            param.add(new BasicNameValuePair("requestDate", _requestDate));
            param.add(new BasicNameValuePair("requestTime", _requestTime));
            param.add(new BasicNameValuePair("doctorCharge", String.valueOf(_doctorCharge)));
            param.add(new BasicNameValuePair("ambulanceCharge", String.valueOf(_ambulanceCharge)));
            param.add(new BasicNameValuePair("requestStatus", _requestStatus));
            param.add(new BasicNameValuePair("chargeStatus", _chargeStatus));
            param.add(new BasicNameValuePair("doctorEmail", _doctorEmail));
            param.add(new BasicNameValuePair("treatmentStatus", _treatmentStatus));
            param.add(new BasicNameValuePair("paymentStatus", _paymentStatus));
            param.add(new BasicNameValuePair("treatmentInfo", _treatmentInfo));
            param.add(new BasicNameValuePair("userCommand", _userCommand));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return param;
    }
}
