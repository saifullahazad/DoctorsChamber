package com.dotsys.doctorschamber.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Azad on 18-Jul-17.
 */

public class Utility {

    public static String GET_GenderValue(String genderText){
        if(genderText.equals("Male")) return "M";
        else if(genderText.equals("Female")) return "F";
        else return "O";
    }

    public static String GET_GenderString(String genderValue){
        if(genderValue.equals("M")) return "Male";
        else if(genderValue.equals("F")) return "Female";
        else return "Other";
    }

    public static String DateToString(Date date, String format){
        DateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    public static String BitmapToString(Bitmap bitmap){
        String ba1;
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] ba = bao.toByteArray();
        ba1 = Base64.encodeToString(ba, Base64.DEFAULT);
        return ba1;
    }

    public static Bitmap StringToBitmap(String ba){
        byte[] data = Base64.decode(ba, Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
        return bmp;
    }


}
