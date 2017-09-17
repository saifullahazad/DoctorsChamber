package com.dotsys.doctorschamber;

import android.app.AlarmManager;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dotsys.doctorschamber.Models.TreatmentRequest;
import com.dotsys.doctorschamber.layout.ClosedRequestsFragment;
import com.dotsys.doctorschamber.layout.PendingRequestsFragment;
import com.dotsys.doctorschamber.layout.LoginActivity;
import com.dotsys.doctorschamber.layout.RequestFragment;
import com.dotsys.doctorschamber.layout.SplashFragment;
import com.dotsys.doctorschamber.repository.UserRepository;
import com.dotsys.doctorschamber.util.Global;
import com.dotsys.doctorschamber.util.PermissionManager;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private UserLogoutTask mAuthTask = null;
    ImageView call_imageView, spalsh_imageView;
    TextView call_textView;
    PermissionManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        invalidateOptionsMenu();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        SetMenuUserTypeWise(navigationView);
        navigationView.setNavigationItemSelectedListener(this);


//        spalsh_imageView = (ImageView) findViewById(R.id.spalsh_imageView);
//        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
//                R.anim.zoomin);
//        spalsh_imageView.startAnimation(animation);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new SplashFragment()).addToBackStack(null).commit();

        pm = new PermissionManager();
        if(!pm.canAccessPhoneCall()) {
            ActivityCompat.requestPermissions(this, PermissionManager.INITIAL_PERMS, PermissionManager.INITIAL_REQUEST);
        }

        call_textView  = (TextView) findViewById(R.id.call_textView);
        call_imageView  = (ImageView) findViewById(R.id.call_imageView);
        call_textView.setOnClickListener(call_click);
        call_imageView.setOnClickListener(call_click);

        MyTimerTask myTask = new MyTimerTask();
        Timer myTimer = new Timer();

        myTimer.schedule(myTask, 5000, 60*1000);


    }

    View.OnClickListener call_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String phone_no= call_textView.getText().toString().replaceAll("-", "");
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+phone_no));

            if(pm.canAccessPhoneCall()) {
                startActivity(callIntent);
            }
        }
    };

    TranslateAnimation.AnimationListener animation_listener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.content_frame, new PendingRequestsFragment()).addToBackStack(null).commit();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    class MyTimerTask extends TimerTask {
        public void run() {
            boolean found=false;
            for (TreatmentRequest request: Global.myPendingRequest) {
                if(Global.loggedInUser.GET_userType().equals("Patient") && request.GET_doctorCharge()>0 && !request.GET_chargeStatus().equals("Confirmed")){
                    generateNotification(getApplicationContext(), "Service charge assigned over your pending request. Please confirm or cancel");
                    found=true;
                }
                else if(!Global.loggedInUser.GET_userType().equals("Patient")){
                    generateNotification(getApplicationContext(), "One Service is assigned to you. Please visit");
                    found=true;
                }
                if(found) break;
            }
        }
    }

    private void generateNotification(Context context, String message) {

        int icon = R.drawable.logo;
        long when = System.currentTimeMillis();
        String appname = context.getResources().getString(R.string.app_name);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        Notification notification;
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        // To support 2.3 os, we use "Notification" class and 3.0+ os will use
        // "NotificationCompat.Builder" class.
        if (currentapiVersion < android.os.Build.VERSION_CODES.HONEYCOMB) {
            notification = new Notification(icon, message, 0);
            notification.flags = Notification.FLAG_AUTO_CANCEL;
            notificationManager.notify((int) when, notification);

        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(
                    context);
            notification = builder.setContentIntent(contentIntent)
                    .setSmallIcon(icon).setTicker(appname).setWhen(0)
                    .setAutoCancel(true).setContentTitle(appname)
                    .setContentText(message).build();

            notificationManager.notify((int) when, notification);

        }

    }

    private void SetMenuUserTypeWise(NavigationView nv){
        if(Global.loggedInUser.GET_userType().equals("Doctor")){
            nv.getMenu().findItem(R.id.menu_now_request).setVisible(false);
        }
    }

    @Override
    public void onBackPressed() {
        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            try {
                mAuthTask = new UserLogoutTask();
                mAuthTask.execute((Void) null);
                return true;
            }
            catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.menu_now_request) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new RequestFragment()).addToBackStack(null).commit();
        } else if (id == R.id.menu_pending_requests) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new PendingRequestsFragment()).addToBackStack(null).commit();
        } else if (id == R.id.menu_closed_requests) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new ClosedRequestsFragment()).addToBackStack(null).commit();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class UserLogoutTask extends AsyncTask<Void, Void, Boolean> {


        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            try{
                UserRepository userRepository = new UserRepository();
                return userRepository.Logout();
            }catch (Exception e){
                throw e;
            }

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;

            if (success) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                throw new RuntimeException("Logout Failed! Check your network connection");
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;

        }
    }
}
