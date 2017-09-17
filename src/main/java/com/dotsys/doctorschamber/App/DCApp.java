package com.dotsys.doctorschamber.App;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.dotsys.doctorschamber.Factory.UserFactory;
import com.dotsys.doctorschamber.util.DBHelper;
import com.dotsys.doctorschamber.util.DatabaseManager;
import com.dotsys.doctorschamber.util.Global;

import java.util.Locale;

/**
 * Created by Azad on 17-Jul-17.
 */

public class DCApp extends Application {
    private static Context context;
    private static DBHelper dbHelper;

    @Override
    public void onCreate()
    {
        super.onCreate();
        try {
            context = this.getApplicationContext();
            dbHelper = new DBHelper();
            DatabaseManager.initializeInstance(dbHelper);
            SetGlobalUser();
            //SetLocalization();
            //Log.d("App","OnCreate");
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void SetGlobalUser(){
        UserFactory factory = new UserFactory();
        Global.loggedInUser = factory.GetUserInfo();
    }
//
//    private void SetLocalization(){
//        try {
//            //---------------------------Get Configuration-------------------------
//            BaseSettingRepository baseSettingRepository=new BaseSettingRepository();
//            BaseSetting baseSetting = baseSettingRepository.GetBaseSetting();
//
//            int position = 0;
//
//            //---------------------------Set Localization--------------------------
//            if(baseSetting!=null)
//                position=baseSetting.Language_GET();
//
//            String short_lan =getResources().getStringArray(R.array.language_short_arrays)[position];
//            Locale locale = new Locale(short_lan);
//
//            Resources resources = context.getResources();
//            Configuration configuration = resources.getConfiguration();
//            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
//            configuration.locale = locale;
//            resources.updateConfiguration(configuration, displayMetrics);
//        } catch (Exception e) {
//            throw e;
//        }
//    }

    public static Context getContext(){
        return context;
    }
}
