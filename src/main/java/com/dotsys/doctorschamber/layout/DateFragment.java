package com.dotsys.doctorschamber.layout;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.dotsys.doctorschamber.R;

import java.util.Calendar;
import android.app.Dialog;
import android.app.DatePickerDialog;

/**

 */
public class DateFragment extends Fragment {
    View myView;

    private Button date_picker_button;
    private TextView date_textView;

    private Calendar calendar;
    int _year, _month, _day;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.fragment_date,container,false);

        date_textView = (TextView) myView.findViewById(R.id.date_textView);

        calendar = Calendar.getInstance();
        _year = calendar.get(Calendar.YEAR);
        _month = calendar.get(Calendar.MONTH);
        _day = calendar.get(Calendar.DAY_OF_MONTH);

        showDate(_year, _month+1, _day);

        date_picker_button = (Button) myView.findViewById(R.id.date_picker_button);
        date_picker_button.setOnClickListener(date_picker_button_click);

        return myView;
    }



    Button.OnClickListener date_picker_button_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog dialog = new DatePickerDialog(getActivity(),R.style.Theme_AppCompat_Light_Dialog_MinWidth,DatePickerDialog_OnDateSet,_year,_month,_day);
            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
    };

    DatePickerDialog.OnDateSetListener DatePickerDialog_OnDateSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            showDate(year, month+1, dayOfMonth);
        }
    };

    private void showDate(int year, int month, int day) {
        String strDate=new StringBuilder().append(day).append("/").append(month).append("/").append(year).toString();
        Log.d("date of birth:", strDate);
        date_textView.setText(strDate);
    }


}
