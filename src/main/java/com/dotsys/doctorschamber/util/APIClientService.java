package com.dotsys.doctorschamber.util;

import android.support.annotation.NonNull;

import com.dotsys.doctorschamber.App.DCApp;
import com.dotsys.doctorschamber.Models.LoginInfo;
import com.dotsys.doctorschamber.Models.TreatmentRequest;
import com.dotsys.doctorschamber.Models.UserInfo;
import com.dotsys.doctorschamber.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.lang.reflect.Type;
import java.util.ListIterator;

/**
 * Created by Azad on 17-Jul-17.
 */

public class APIClientService {

    public boolean Register(UserInfo userInfo) {
        try {
            String strURL = DCApp.getContext().getString(R.string.API_Register);

            APIService service = new APIService(strURL, "POST", userInfo.toNameValuePairList());
            service.ExecuteForJSONObject();
            return true;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    public boolean Login(LoginInfo loginInfo) {
        try {

            String strURL = DCApp.getContext().getString(R.string.API_Login);

            APIService service = new APIService(strURL, "POST", loginInfo.toNameValuePairList());
            JSONObject jUser = service.ExecuteForJSONObject();
            Global.loggedInUser = new UserInfo(jUser);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean Logout(LoginInfo loginInfo) {
        try {

            String strURL = DCApp.getContext().getString(R.string.API_Logout);

            APIService service = new APIService(strURL, "POST", loginInfo.toNameValuePairList());
            service.ExecuteForJSONObject();
            Global.loggedInUser = null;
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean RequestNow(TreatmentRequest treatmentRequest) {
        try {
            String strURL = DCApp.getContext().getString(R.string.API_RequestNow);

            APIService service = new APIService(strURL, "POST", treatmentRequest.toNameValuePairList());
            service.ExecuteForJSONObject();
            return true;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    public List<TreatmentRequest> MyPendingRequests(){
        List<TreatmentRequest> treatmentRequestList = new ArrayList<TreatmentRequest>();
        try {
            String strURL = DCApp.getContext().getString(R.string.API_MyPendingRequests);

            APIService service = new APIService(strURL, "POST", Global.loggedInUser.toNameValuePairList());
            JSONArray jsonArray = service.ExecuteForJSONArray();

            if (jsonArray != null) {
                for (int i=0;i<jsonArray.length();i++){
                    String jSonString = jsonArray.getString(i);
                    JSONObject jObj = new JSONObject(jSonString);
                    TreatmentRequest request = new TreatmentRequest(jObj);
                    treatmentRequestList.add(request);
                }
            }
            return treatmentRequestList;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public List<TreatmentRequest> MyClosedRequests(){
        List<TreatmentRequest> treatmentRequestList = new ArrayList<TreatmentRequest>();
        try {
            String strURL = DCApp.getContext().getString(R.string.API_MyClosedRequests);

            APIService service = new APIService(strURL, "POST", Global.loggedInUser.toNameValuePairList());
            JSONArray jsonArray = service.ExecuteForJSONArray();

            if (jsonArray != null) {
                for (int i=0;i<jsonArray.length();i++){
                    String jSonString = jsonArray.getString(i);
                    JSONObject jObj = new JSONObject(jSonString);
                    TreatmentRequest request = new TreatmentRequest(jObj);
                    treatmentRequestList.add(request);
                }
            }
            return treatmentRequestList;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public boolean UpdateRequest(TreatmentRequest treatmentRequest) {
        try {
            String strURL = DCApp.getContext().getString(R.string.API_RequestUpdate);

            APIService service = new APIService(strURL, "POST", treatmentRequest.toNameValuePairList());
            service.ExecuteForJSONObject();
            return true;
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }

    }


}
