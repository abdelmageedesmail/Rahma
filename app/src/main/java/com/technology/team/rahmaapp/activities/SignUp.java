package com.technology.team.rahmaapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
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
import com.technology.team.rahmaapp.activities.donaters.AddNewFood;
import com.technology.team.rahmaapp.activities.donaters.HomeActivity;
import com.technology.team.rahmaapp.adapters.MyAdapter;
import com.technology.team.rahmaapp.classes.LocaleShared;
import com.technology.team.rahmaapp.classes.Urls;
import com.technology.team.rahmaapp.models.SpinnerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class SignUp extends AppCompatActivity {

    private TextView marqueeTxt;
    private int type;
    EditText etUserName, etRepeatPass, etPass, etCity, etEmail, etPone;
    Button btnRegister;
    int PLACE_PICKER_REQUEST = 1;
    private double longitude, latitude;
    private LocaleShared localeShared;
    ArrayList<SpinnerModel> arrayAssistance;
    ArrayList<SpinnerModel> arrayCity;
    ArrayList<String> arrayList;
    Spinner etNeedHelp, spCity;
    FrameLayout frAssistance;
    private String name;
    LinearLayout liCity;
    private String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setUpPermissions();
        setUpRefrence();
        Intent i = getIntent();
        type = i.getExtras().getInt("type");
        if (type != 2) {
            frAssistance.setVisibility(View.GONE);
            liCity.setVisibility(View.GONE);
            etCity.setVisibility(View.VISIBLE);
        } else {
            frAssistance.setVisibility(View.VISIBLE);
            liCity.setVisibility(View.VISIBLE);
            etCity.setVisibility(View.GONE);
        }
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
        etNeedHelp = findViewById(R.id.etNeedHelp);
        frAssistance = findViewById(R.id.frAssistance);
        spCity = findViewById(R.id.spCity);
        liCity = findViewById(R.id.liCity);

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
        arrayAssistance = new ArrayList<>();
        arrayCity = new ArrayList<>();
        arrayList = new ArrayList<>();
        getAssitanceType();
        getCities();
    }


    private void getCities() {
        AndroidNetworking.initialize(this);
        AndroidNetworking.setParserFactory(new JacksonParserFactory());
        AndroidNetworking.get(Urls.getCities)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if (status.equals("1")) {
                                JSONArray cities = response.getJSONArray("cities");
                                for (int i = 0; i < cities.length(); i++) {
                                    JSONObject object = cities.getJSONObject(i);
                                    SpinnerModel spinnerModel = new SpinnerModel();
                                    spinnerModel.setId(object.getString("id"));
                                    if (Locale.getDefault().getDisplayLanguage().equals("العربية")) {
                                        spinnerModel.setName(object.getString("name_ar"));
                                    } else {
                                        spinnerModel.setName(object.getString("name_en"));
                                    }
                                    arrayCity.add(spinnerModel);
                                }
                                spCity.setAdapter(new MyAdapter(SignUp.this, arrayCity));
                                spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        cityName = arrayCity.get(i).getName();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private void getAssitanceType() {
        AndroidNetworking.initialize(this);
        AndroidNetworking.setParserFactory(new JacksonParserFactory());
        AndroidNetworking.get(Urls.get_assistance_types)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if (status.equals("1")) {
                                JSONArray assistance_types = response.getJSONArray("assistance_types");
                                for (int i = 0; i < assistance_types.length(); i++) {
                                    JSONObject object = assistance_types.getJSONObject(i);
                                    SpinnerModel model = new SpinnerModel();
                                    model.setId(object.getString("id"));
                                    if (Locale.getDefault().getDisplayLanguage().equals("العربية")) {
                                        model.setName(object.getString("name_ar"));
                                    } else {
                                        model.setName(object.getString("name_en"));
                                    }
                                    arrayAssistance.add(model);
                                }
                                etNeedHelp.setAdapter(new MyAdapter(SignUp.this, arrayAssistance));
                                etNeedHelp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        arrayList.add(arrayAssistance.get(i).getId());
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

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

    private void setUpValidation() {
        if (etUserName.getText().toString().equals("") || etUserName.getText().toString().isEmpty()) {
            etUserName.setError(getString(R.string.emptyFiled));
        } else if (etEmail.getText().toString().equals("") || etEmail.getText().toString().isEmpty()) {
            etEmail.setError(getString(R.string.emptyFiled));
        } else if (etPone.getText().toString().equals("") || etPone.getText().toString().isEmpty()) {
            etPone.setError(getString(R.string.emptyFiled));
        } else if (type != 2) {
            if (etCity.getText().toString().equals("") || etCity.getText().toString().isEmpty()) {
                etCity.setError(getString(R.string.emptyFiled));
            }
        } else if (etPass.getText().toString().equals("") || etPass.getText().toString().isEmpty()) {
            etPass.setError(getString(R.string.emptyFiled));
        } else if (etRepeatPass.getText().toString().equals("") || etRepeatPass.getText().toString().isEmpty()) {
            etRepeatPass.setError(getString(R.string.emptyFiled));
        } else if (!etRepeatPass.getText().toString().equals(etPass.getText().toString())) {
            Toast.makeText(this, getString(R.string.passwordDontoMatch), Toast.LENGTH_SHORT).show();
        } else {
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
        ANRequest.PostRequestBuilder post = AndroidNetworking.post(Urls.signUp);
        post.addBodyParameter("name", etUserName.getText().toString())
                .addBodyParameter("phone", etPone.getText().toString())
                .addBodyParameter("email", etEmail.getText().toString())
                .addBodyParameter("password", etPass.getText().toString())
                .addBodyParameter("type", "" + type);

        if (type == 2) {
            for (int i = 0; i < arrayList.size(); i++) {
                post.addBodyParameter("assistance_types[" + "]", arrayList.get(i));

            }
            post.addBodyParameter("city", cityName);
            post.addBodyParameter("lat", "0.0");
            post.addBodyParameter("lng", "0.0");
        } else {
            post.addBodyParameter("lat", "" + latitude);
            post.addBodyParameter("lng", "" + longitude);
            post.addBodyParameter("city", etCity.getText().toString());
        }
        post.setPriority(Priority.MEDIUM)
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
                                localeShared.storeKey("type", user.getString("type"));
                                startActivity(new Intent(SignUp.this, LoginActivity.class));
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
