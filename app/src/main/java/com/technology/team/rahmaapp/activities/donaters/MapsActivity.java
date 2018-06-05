package com.technology.team.rahmaapp.activities.donaters;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.technology.team.rahmaapp.adapters.MyAdapter;
import com.technology.team.rahmaapp.classes.GPSTracker;
import com.technology.team.rahmaapp.classes.LocaleShared;
import com.technology.team.rahmaapp.classes.Urls;
import com.technology.team.rahmaapp.models.SpinnerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    GPSTracker mGps;
    TextView marqueeTxt;
    EditText etHomeNum,etflatNum;
    EditText etAddress;
    Button btnAdd;
    LocaleShared localeShared;
    private String id;
    int PLACE_PICKER_REQUEST = 1;
    private double longitude, latitude;
    ArrayList<SpinnerModel> arrayList;
    private String name;

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
        arrayList=new ArrayList<>();

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
                final Place place = PlacePicker.getPlace(this,data);
                Geocoder geocoder = new Geocoder(this);
                try
                {
                    List<Address> addresses = geocoder.getFromLocation(place.getLatLng().latitude,place.getLatLng().longitude, 1);
                    String address = addresses.get(0).getAddressLine(0);
                    String[] split = address.split(",");
                    String city = addresses.get(0).getAddressLine(1);
                    //String country = addresses.get(0).getAddressLine(2);
                    etAddress.setText(split[1]+" , "+split[2]);
                    LatLng latLng = place.getLatLng();
                    latitude=latLng.latitude;
                    longitude=latLng.longitude;
                } catch (IOException e)
                {

                    e.printStackTrace();
                }
            }
        }
    }

//    private void getCities(){
//        AndroidNetworking.initialize(this);
//        AndroidNetworking.setParserFactory(new JacksonParserFactory());
//        AndroidNetworking.get(Urls.getCities)
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            String status = response.getString("status");
//                            if (status.equals("1")){
//                                JSONArray cities = response.getJSONArray("cities");
//                                for (int i=0;i<cities.length();i++){
//                                    JSONObject object=cities.getJSONObject(i);
//                                    SpinnerModel spinnerModel=new SpinnerModel();
//                                    spinnerModel.setId(object.getString("id"));
//                                    if (Locale.getDefault().getDisplayLanguage().equals("العربية")){
//                                        spinnerModel.setName(object.getString("name_ar"));
//                                    }else{
//                                        spinnerModel.setName(object.getString("name_en"));
//                                    }
//                                    arrayList.add(spinnerModel);
//                                }
//                                etAddress.setAdapter(new MyAdapter(MapsActivity.this,arrayList));
//                                etAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                    @Override
//                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                                        name = arrayList.get(i).getName();
//                                    }
//
//                                    @Override
//                                    public void onNothingSelected(AdapterView<?> adapterView) {
//
//                                    }
//                                });
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//
//                    }
//                });
//    }

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
