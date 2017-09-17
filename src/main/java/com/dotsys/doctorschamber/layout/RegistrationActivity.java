package com.dotsys.doctorschamber.layout;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dotsys.doctorschamber.App.DCApp;
import com.dotsys.doctorschamber.MainActivity;
import com.dotsys.doctorschamber.Models.UserInfo;
import com.dotsys.doctorschamber.R;
import com.dotsys.doctorschamber.repository.UserRepository;
import com.dotsys.doctorschamber.util.PermissionManager;
import com.dotsys.doctorschamber.util.Utility;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class RegistrationActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    int _year, _month, _day;
    Button.OnClickListener sign_in_button_click = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;
    // UI references.
    Intent intent ;
    Bitmap bitmap;
    private ImageView photo_imageView;
    private AutoCompleteTextView name, mobile, email, location;
    private EditText password, confirm_password;
    private RadioGroup gender_radioGroup, holdingType_radioGroup;
    private RadioButton gender_selectedRadio, holdingType_selectedRadio;
    private TextView birthdate;
    DatePickerDialog.OnDateSetListener DatePickerDialog_OnDateSet = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            _year = year;
            _month = month;
            _day = dayOfMonth;
            showDate(year, month + 1, dayOfMonth);
        }
    };
    Button.OnClickListener birthdate_picker_button_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog dialog = new DatePickerDialog(RegistrationActivity.this, R.style.Theme_AppCompat_Light_Dialog_MinWidth, DatePickerDialog_OnDateSet, _year, _month, _day);
            dialog.show();
        }
    };
    private View mProgressView;
    private View mLoginFormView;
    Button.OnClickListener registration_button_click = new OnClickListener() {
        @Override
        public void onClick(View v) {
            attemptRegistration();
        }
    };

    Button.OnClickListener capture_imageView_click = new OnClickListener() {
        @Override
        public void onClick(View v) {
            intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

            startActivityForResult(intent, 7);
        }
    };

    Button.OnClickListener galary_imageView_click=new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 7);
        }
    };

    // Star activity for result method to Set captured image on image view after click.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 7 && resultCode == RESULT_OK && data != null) {

            Uri uri = data.getData();

            try {

                // Adding captured image in bitmap.
                if(uri==null)
                    bitmap = (Bitmap)data.getExtras().get("data");
                else
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                // adding captured image in imageview.
                photo_imageView.setImageBitmap(bitmap);

            } catch (IOException e) {

                Toast.makeText(RegistrationActivity.this,e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

    private Calendar calendar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Log.d("registration on create", "available");
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getString(R.string.title_activity_registration));

        //Initiate all controls
        name = (AutoCompleteTextView) findViewById(R.id.name);
        photo_imageView = (ImageView) findViewById(R.id.photo_imageView);
        mobile = (AutoCompleteTextView) findViewById(R.id.mobile);
        location = (AutoCompleteTextView) findViewById(R.id.location);

        //Initiate Birth Date Picker
        birthdate = (EditText) findViewById(R.id.birthdate);

        calendar = Calendar.getInstance();
        _year = calendar.get(Calendar.YEAR);
        _month = calendar.get(Calendar.MONTH);
        _day = calendar.get(Calendar.DAY_OF_MONTH);

        //showDate(_year, _month+1, _day);

        birthdate.setOnClickListener(birthdate_picker_button_click);

        // Set up the login form.
        email = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        password = (EditText) findViewById(R.id.password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);

        gender_radioGroup = (RadioGroup) findViewById(R.id.gender_radioGroup);
        holdingType_radioGroup = (RadioGroup) findViewById(R.id.holdingType_radioGroup);

        EnableRuntimePermissionToAccessCamera();

        ImageView capture_imageView = (ImageView) findViewById(R.id.capture_imageView);
        capture_imageView.setOnClickListener(capture_imageView_click);

        ImageView galary_imageView=(ImageView) findViewById(R.id.galary_imageView);
        galary_imageView.setOnClickListener(galary_imageView_click);

        Button registration_button = (Button) findViewById(R.id.registration_button);
        registration_button.setOnClickListener(registration_button_click);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(sign_in_button_click);

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);




    }


    // Requesting runtime permission to access camera.
    public void EnableRuntimePermissionToAccessCamera(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(RegistrationActivity.this, Manifest.permission.CAMERA))
        {

            // Printing toast message after enabling runtime permission.
            Toast.makeText(RegistrationActivity.this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(RegistrationActivity.this,new String[]{Manifest.permission.CAMERA}, 1);

        }
    }









    private void showDate(int year, int month, int day) {
        _year = year;
        _month = month;
        _day = day;
        String strDate = new StringBuilder().append(day).append("/").append(month).append("/").append(year).toString();
        Log.d("date of birth:", strDate);
        birthdate.setText(strDate);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(email, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptRegistration() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        email.setError(null);
        password.setError(null);
        confirm_password.setError(null);

        // Store values at the time of the registration attempt.
        String nameValue = name.getText().toString();
        String mobileValue = mobile.getText().toString();
        String emailValue = email.getText().toString();

        int selected_gender_radio_id = gender_radioGroup.getCheckedRadioButtonId();
        gender_selectedRadio = (RadioButton) findViewById(selected_gender_radio_id);
        String genderValue = gender_selectedRadio.getText().toString();

        int selected_holdingType_radio_id = holdingType_radioGroup.getCheckedRadioButtonId();
        holdingType_selectedRadio = (RadioButton) findViewById(selected_holdingType_radio_id);
        String holdingTypeValue = holdingType_selectedRadio.getText().toString();

        Date birthdateValue = new Date(_year, _month, _day);
        String locationValue = location.getText().toString();
        String passwordValue = password.getText().toString();
        String confirmPasswordValue = confirm_password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid name.
        if (TextUtils.isEmpty(nameValue)) {
            name.setError(getString(R.string.error_field_required));
            focusView = name;
            cancel = true;
        }

        // Check for a valid name.
        if (TextUtils.isEmpty(mobileValue)) {
            mobile.setError(getString(R.string.error_field_required));
            focusView = mobile;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailValue)) {
            email.setError(getString(R.string.error_field_required));
            focusView = email;
            cancel = true;
        } else if (!isEmailValid(emailValue)) {
            email.setError(getString(R.string.error_invalid_email));
            focusView = email;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(passwordValue) && !isPasswordValid(passwordValue)) {
            password.setError(getString(R.string.error_invalid_password));
            focusView = password;
            cancel = true;
        }

        // Check for a valid confirm password.
        if (TextUtils.isEmpty(confirmPasswordValue)) {
            confirm_password.setError(getString(R.string.error_field_required));
            focusView = confirm_password;
            cancel = true;
        } else if (!isConfirmPasswordValid(passwordValue, confirmPasswordValue)) {
            confirm_password.setError(getString(R.string.error_confirm_password));
            focusView = confirm_password;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            String birthdayString = String.valueOf(_year)
                                        .concat("-")
                                        .concat(String.valueOf(_month<10?"0".concat(String.valueOf(_month)):_month))
                                        .concat("-")
                                        .concat(String.valueOf(_day<10?"0".concat(String.valueOf(_day)):_day));

            String photoValue;
            if(bitmap!=null)
                photoValue=Utility.BitmapToString(bitmap);
            UserInfo userInfo = new UserInfo(nameValue, emailValue, passwordValue, Utility.GET_GenderValue(genderValue), birthdayString
                    , mobileValue, locationValue, holdingTypeValue, "Patient", Utility.BitmapToString(bitmap));
            mAuthTask = new UserLoginTask(userInfo);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isConfirmPasswordValid(String vPassword, String vOonfirm_password) {
        //TODO: Replace this with your own logic
        return vPassword.equals(vOonfirm_password);
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(RegistrationActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        email.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final UserInfo _userInfo;


        UserLoginTask(UserInfo userInfo) {
            _userInfo = userInfo;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                UserRepository repository = new UserRepository();
                return repository.Register(_userInfo);

            } catch (Exception e) {
                Log.d("Error",e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                password.setError(getString(R.string.error_incorrect_password));
                password.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

