package com.technology.team.rahmaapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.jacksonandroidnetworking.JacksonParserFactory;
import com.technology.team.rahmaapp.R;
import com.technology.team.rahmaapp.activities.donaters.HomeActivity;
import com.technology.team.rahmaapp.classes.LocaleShared;
import com.technology.team.rahmaapp.classes.Urls;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUp extends AppCompatActivity {

    private TextView marqueeTxt;
    private int type;
    EditText etUserName, etRepeatPass, etPass, etCity, etEmail, etPone;
    Button btnRegister;
    int PLACE_PICKER_REQUEST = 1;
    private double longitude, latitude;
    private LocaleShared localeShared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setUpPermissions();
        setUpRefrence();
        Intent i = getIntent();
        type = i.getExtras().getInt("type");
    }

    private void setUpRefrence() {
        marqueeTxt = findViewById(R.id.mywidget);
        marqueeTxt.setSelected(true);  // Set focus to the textview
        etUserName = findViewById(R.id.etUserName);
        etRepeatPass = findViewById(R.id.etRepeatPass);
        etPass = findViewById(R.id.etPass);
        etCity = findViewById(R.id.etCity);
        etEmail = findViewById(R.id.etEmail);
        etPone = findViewById(R.id.etPone);
        btnRegister = findViewById(R.id.btnRegister);
        localeShared = new LocaleShared(this);
        etCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlacePicker();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpValidation();
            }
        });

    }

    private void showPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(SignUp.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
    private void setUpPermissions() {
        String per[] = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        ActivityCompat.requestPermissions(this, per, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format(String.valueOf(place.getAddress()));
                LatLng latLng = place.getLatLng();
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                etCity.setText(toastMsg);
            }
        }
    }
    private void setUpValidation(){
        if (etUserName.getText().toString().equals("")||etUserName.getText().toString().isEmpty()){
            etUserName.setError(getString(R.string.emptyFiled));
        }else if (etEmail.getText().toString().equals("")||etEmail.getText().toString().isEmpty()){
            etEmail.setError(getString(R.string.emptyFiled));
        }else if (etPone.getText().toString().equals("")||etPone.getText().toString().isEmpty()){
            etPone.setError(getString(R.string.emptyFiled));
        }else if (etCity.getText().toString().equals("")||etCity.getText().toString().isEmpty()){
            etCity.setError(getString(R.string.emptyFiled));
        }else if (etPass.getText().toString().equals("")||etPass.getText().toString().isEmpty()){
            etPass.setError(getString(R.string.emptyFiled));
        }else if (etRepeatPass.getText().toString().equals("")||etRepeatPass.getText().toString().isEmpty()){
            etRepeatPass.setError(getString(R.string.emptyFiled));
        }else if (!etRepeatPass.getText().toString().equals(etPass.getText().toString())){
            Toast.makeText(this, getString(R.string.passwordDontoMatch), Toast.LENGTH_SHORT).show();
        }else {
            registerUser();
        }
    }
    private void registerUser() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.uploading));
        progressDialog.show();
        AndroidNetworking.initialize(this);
        AndroidNetworking.setParserFactory(new JacksonParserFactory());
        AndroidNetworking.post(Urls.signUp)
                .addBodyParameter("name", etUserName.getText().toString())
                .addBodyParameter("phone", etPone.getText().toString())
                .addBodyParameter("email", etEmail.getText().toString())
                .addBodyParameter("password", etPass.getText().toString())
                .addBodyParameter("type", "" + type)
                .addBodyParameter("city", etCity.getText().toString())
                .addBodyParameter("lat", "" + latitude)
                .addBodyParameter("lng", "" + longitude)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            String status = response.getString("status");
                            if (status.equals("0")) {
                                JSONObject user = response.getJSONObject("user");
                                localeShared.storeId(user.getString("id"));
                                localeShared.storeKey("name", user.getString("name"));
                                localeShared.storeKey("phone", user.getString("phone"));
                                localeShared.storeKey("email", user.getString("email"));
                                localeShared.storeKey("type",user.getString("type"));
                                startActivity(new Intent(SignUp.this, HomeActivity.class));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                    }
                });
    }
}
