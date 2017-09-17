package com.dotsys.doctorschamber.util;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.dotsys.doctorschamber.Models.TreatmentRequest;
import com.dotsys.doctorschamber.R;
import com.dotsys.doctorschamber.layout.RequestDetailFragment;

/**
 * Created by Azad on 06-Jul-17.
 */

public class RequestTableRow {

    TreatmentRequest request;
    Context _context;
    Fragment _fragment;

    public RequestTableRow(Context context, Fragment fragment, TreatmentRequest bookingRequest){
        request=bookingRequest;
        _context = context;
        _fragment=fragment;

    }

    public TableRow GET_TableRow(){
        TableRow row  = new TableRow(_context);
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setLayoutParams(layoutParams);
        row.addView(CreateDateTextView());
        row.addView(CreateTimeTextView());
        row.addView(CreateDetailButton());
        Log.d("RequestTableRow:", String.valueOf(request.GET_treatmentId()));
        return row;
    }


    private TextView CreateDateTextView(){
        TextView textView=new TextView(_context);
//        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.setMargins(3,3,0,0);
//        textView.setLayoutParams(layoutParams);
        textView.setPadding(30,30,0,0);
        textView.setText(request.GET_requestDate());
        textView.setTextColor(Color.BLACK);
        return textView;
    }


    private TextView CreateTimeTextView(){
        TextView textView=new TextView(_context);
        //LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        layoutParams.setMargins(3,3,0,0);
        //textView.setLayoutParams(layoutParams);
        textView.setText(request.GET_requestTime());
        textView.setTextColor(Color.BLACK);
        return textView;
    }


    private TextView CreateDetailButton(){
        TextView textView=new TextView(_context);
        String request_detail_text = _context.getResources().getString(R.string.action_button_request_detail_link);
        SpannableString spannableString=new SpannableString(request_detail_text);
        spannableString.setSpan(new UnderlineSpan(),0,spannableString.length(),0);
        textView.setText(spannableString);
        textView.setTextColor(Color.BLUE);
        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(null, Typeface.BOLD);



//        ImageButton detailButton=new ImageButton(_context);
////        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
////        layoutParams.setMargins(0,3,0,0);
////        detailButton.setLayoutParams(layoutParams);
//        detailButton.setImageResource(R.drawable.info_small);
//        detailButton.setBackgroundColor(Color.TRANSPARENT);
//
        textView.setOnClickListener(detailButton_OnClick);
        return textView;
    }

    View.OnClickListener detailButton_OnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fragmentManager = _fragment.getFragmentManager();
            RequestDetailFragment fragment = new RequestDetailFragment();
            fragment.SET_BookingRequest(request);
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).addToBackStack(null).commit();
        }
    };

}
