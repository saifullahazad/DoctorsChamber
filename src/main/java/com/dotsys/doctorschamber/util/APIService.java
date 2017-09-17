package com.dotsys.doctorschamber.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;




/**
 * Created by Azad on 17-Jul-17.
 */

public class APIService {
    private String _method;
    public void SET_Method(String method){
        _method=method;
        if(_method=="GET"){
            String paramString = URLEncodedUtils.format(_params, "utf-8");
            _url += "?" + paramString;
        }
    }

    private String _url;
    public void SET_URL(String url){_url=url;}

    private ArrayList<NameValuePair> _params;

    public APIService(String url, String method,ArrayList<NameValuePair> params){
        _method=method;
        _url=url;
        _params=params;

        if(_method=="GET"){
            String paramString = URLEncodedUtils.format(_params, "utf-8");
            _url += "?" + paramString;
        }
    }



    public String ExecuteForString(){
        InputStream is = null;
        JSONObject jObj = null;
        String json = "";
        // Making HTTP request
        try {

            // check for request method
            if(_method == "POST"){
                // request method is POST
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(_url);
                UrlEncodedFormEntity entity=new UrlEncodedFormEntity(_params);
                httpPost.setEntity(entity);

                HttpResponse httpResponse = httpClient.execute(httpPost);

                HttpEntity httpEntity = httpResponse.getEntity();
                String con = httpEntity.getContentType().toString();
                is = httpEntity.getContent();


            }else if(_method == "GET"){
                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(_params, "utf-8");
                _url += "?" + paramString;
                HttpGet httpGet = new HttpGet(_url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }else if(_method == "PUT"){
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPut httpPut = new HttpPut(_url);
                httpPut.setEntity(new UrlEncodedFormEntity(_params));

                HttpResponse httpResponse = httpClient.execute(httpPut);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject ExecuteForJSONObject() {
        JSONObject jObj=null;

        // try parse the string to a JSON object
        try {
            String json = ExecuteForString();
            jObj = new JSONObject(json);
        } catch (JSONException e) {
            throw new RuntimeException(e.getMessage());
        }

        // return JSON Object
        return jObj;

    }

    public JSONArray ExecuteForJSONArray() {
        JSONArray jArray=null;

        // try parse the string to a JSON object
        try {
            String json = ExecuteForString();
            jArray = new JSONArray(json);

        } catch (JSONException e) {
            throw new RuntimeException(e.getMessage());
        }

        // return JSON Array
        return jArray;

    }


}
