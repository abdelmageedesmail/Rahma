package com.technology.team.rahmaapp.activities.donaters;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jacksonandroidnetworking.JacksonParserFactory;
import com.technology.team.rahmaapp.R;
import com.technology.team.rahmaapp.activities.SignUp;
import com.technology.team.rahmaapp.classes.GPSTracker;
import com.technology.team.rahmaapp.classes.LocaleShared;
import com.technology.team.rahmaapp.classes.Urls;

import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    GPSTracker mGps;
    TextView marqueeTxt;
    EditText etAddress,etHomeNum,etflatNum;
    Button btnAdd;
    LocaleShared localeShared;
    private String id;
    int PLACE_PICKER_REQUEST = 1;
    private double longitude, latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setUpPermissions();
        localeShared=new LocaleShared(this);
        id = localeShared.getId();
        showPopUpAddAddress();
        marqueeTxt = findViewById(R.id.mywidget);
        marqueeTxt.setSelected(true);
        mGps=new GPSTracker(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(mGps.getLatitude(), mGps.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                sydney, 15);
        mMap.animateCamera(location);
    }
    private void showPopUpAddAddress(){
        Dialog d=new Dialog(this);
        d.setContentView(R.layout.popup_add_address);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        d.getWindow().setLayout(width, height);
        d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        etAddress=d.findViewById(R.id.etAddress);
        etHomeNum=d.findViewById(R.id.etHomeNum);
        etflatNum=d.findViewById(R.id.etflatNum);
        btnAdd=d.findViewById(R.id.btnAdd);
        etAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlacePicker();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAddress();
            }
        });
        d.setCanceledOnTouchOutside(false);
        d.show();
    }


    private void showPlacePicker() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
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
                etAddress.setText(toastMsg);
            }
        }
    }
    private void addAddress(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.uploading));
        progressDialog.show();
        AndroidNetworking.initialize(this);
        AndroidNetworking.setParserFactory(new JacksonParserFactory());
        AndroidNetworking.post(Urls.addUSerAddress)
                .addBodyParameter("user_id",id)
                .addBodyParameter("title",etAddress.getText().toString())
                .addBodyParameter("house_number",etHomeNum.getText().toString())
                .addBodyParameter("floor_number",etflatNum.getText().toString())
                .addBodyParameter("lat",""+latitude)
                .addBodyParameter("lng",""+longitude)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.e("response",""+response);
                        try {
                            String status = response.getString("status");
                            if (status.equals("1")){
                                startActivity(new Intent(MapsActivity.this,AddressListActivity.class));
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Log.e("error",""+anError.getMessage());
                    }
                });
    }
}
