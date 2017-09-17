package com.dotsys.doctorschamber.Factory;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dotsys.doctorschamber.Models.UserInfo;
import com.dotsys.doctorschamber.util.DatabaseManager;

/**
 * Created by Azad on 22-Jul-17.
 */

public class UserFactory {

    public static String createTable() {
        return "CREATE TABLE " + UserInfo.TABLE + "("
                + UserInfo.Col_userEmail + "  TEXT PRIMARY KEY    ,"
                + UserInfo.Col_fullName + " TEXT ,"
                + UserInfo.Col_userPass + " TEXT ,"
                + UserInfo.Col_userGender + " TEXT ,"
                + UserInfo.Col_birthday + " TEXT ,"
                + UserInfo.Col_contactNo + " TEXT ,"
                + UserInfo.Col_address + " TEXT ,"
                + UserInfo.Col_holdingType + " TEXT ,"
                + UserInfo.Col_userType + " TEXT )";
    }

    private String QUERY_GET_ALL() {
        return "SELECT "
                + UserInfo.Col_userEmail + ", "
                + UserInfo.Col_fullName + ", "
                + UserInfo.Col_userPass + ", "
                + UserInfo.Col_userGender + ", "
                + UserInfo.Col_birthday + ", "
                + UserInfo.Col_contactNo + ", "
                + UserInfo.Col_address + ", "
                + UserInfo.Col_holdingType + ", "
                + UserInfo.Col_userType + " "
                + " FROM "
                + UserInfo.TABLE;
    }


    private boolean insert(UserInfo userInfo) {
        ContentValues values = new ContentValues();
        values.put(userInfo.Col_userEmail, userInfo.GET_userEmail());
        values.put(userInfo.Col_fullName, userInfo.GET_fullName());
        values.put(userInfo.Col_userPass, userInfo.GET_userPass());
        values.put(userInfo.Col_userGender, userInfo.GET_userGender());
        values.put(userInfo.Col_birthday, userInfo.GET_birthday());
        values.put(userInfo.Col_contactNo, userInfo.GET_contactNos());
        values.put(userInfo.Col_address, userInfo.GET_address());
        values.put(userInfo.Col_holdingType, userInfo.GET_holdingType());
        values.put(userInfo.Col_userType, userInfo.GET_userType());

        // Inserting Row
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.insert(userInfo.TABLE, null, values);
        DatabaseManager.getInstance().closeDatabase();
        Log.d(UserInfo.TAG, "Data inserted");

        return true;
    }


    private int update(UserInfo userInfo) {
        int result = 0;

        ContentValues values = new ContentValues();
        values.put(userInfo.Col_fullName, userInfo.GET_fullName());
        values.put(userInfo.Col_userPass, userInfo.GET_userPass());
        values.put(userInfo.Col_userGender, userInfo.GET_userGender());
        values.put(userInfo.Col_birthday, userInfo.GET_birthday());
        values.put(userInfo.Col_contactNo, userInfo.GET_contactNos());
        values.put(userInfo.Col_address, userInfo.GET_address());
        values.put(userInfo.Col_holdingType, userInfo.GET_holdingType());
        values.put(userInfo.Col_userType, userInfo.GET_userType());

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        result = (int) db.update(UserInfo.TABLE, values, UserInfo.Col_userEmail + " =?", new String[]{String.valueOf(userInfo.GET_userEmail())});
        DatabaseManager.getInstance().closeDatabase();
        Log.d(UserInfo.TAG, "Data updated");

        return result;
    }

    public boolean deleteAll() {
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            db.delete(UserInfo.TABLE, null, null);
            DatabaseManager.getInstance().closeDatabase();
            Log.d(UserInfo.TAG, "All data deleted");
            return true;
        } catch (Exception e) {
            throw e;
        }
    }

    public UserInfo GetUserInfo() {
        try {
            SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
            Cursor result = db.rawQuery(QUERY_GET_ALL(), null);
            if (result.getCount() > 0) {
                result.moveToLast();

                UserInfo userInfo = new UserInfo();
                userInfo.SET_userEmail(result.getString(result.getColumnIndex(UserInfo.Col_userEmail)));
                userInfo.SET_fullName(result.getString(result.getColumnIndex(UserInfo.Col_fullName)));
                userInfo.SET_userPass(result.getString(result.getColumnIndex(UserInfo.Col_userPass)));
                userInfo.SET_userGender(result.getString(result.getColumnIndex(UserInfo.Col_userGender)));
                userInfo.SET_birthday(result.getString(result.getColumnIndex(UserInfo.Col_birthday)));
                userInfo.SET_contactNo(result.getString(result.getColumnIndex(UserInfo.Col_contactNo)));
                userInfo.SET_address(result.getString(result.getColumnIndex(UserInfo.Col_address)));
                userInfo.SET_holdingType(result.getString(result.getColumnIndex(UserInfo.Col_holdingType)));
                userInfo.SET_userType(result.getString(result.getColumnIndex(UserInfo.Col_userType)));
                Log.d(UserInfo.TAG, "Data Selected");

                return userInfo;
            } else
                return null;
        } catch (Exception e) {
            throw e;
        }
    }


    public boolean Save(UserInfo userInfo) {
        try {
            deleteAll();
            insert(userInfo);
            return true;

        } catch (Exception e) {
            throw e;
        }
    }
}
