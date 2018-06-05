package com.technology.team.rahmaapp.activities.mostafeed;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jacksonandroidnetworking.JacksonParserFactory;
import com.squareup.picasso.Picasso;
import com.technology.team.rahmaapp.R;
import com.technology.team.rahmaapp.activities.LoginActivity;
import com.technology.team.rahmaapp.activities.donaters.HomeActivity;
import com.technology.team.rahmaapp.classes.GPSTracker;
import com.technology.team.rahmaapp.classes.LocaleShared;
import com.technology.team.rahmaapp.classes.Urls;
import com.technology.team.rahmaapp.fragmen.FragmentDrawer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MostafeedHomeActivity extends FragmentActivity implements OnMapReadyCallback, FragmentDrawer.FragmentDrawerListener {

    private GoogleMap mMap;
    DrawerLayout mDrawerLayout;
    FragmentDrawer drawerFragment;
    Toolbar toolbar;
    TextView marqueeTxt, donationQuantity, donationType, tvDesc;
    GPSTracker mgps;
    Button btnPlace, IneedThisDonation;
    ImageView popUpDonate;
    private String snippet;
    LocaleShared localeShared;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostafeed_home);
        mgps = new GPSTracker(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mDrawerLayout = findViewById(R.id.mDrawer);
        toolbar = findViewById(R.id.toolbar);
        marqueeTxt = findViewById(R.id.mywidget);
        marqueeTxt.setSelected(true);  // Set focus to the textview
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, mDrawerLayout, toolbar);
        drawerFragment.setDrawerListener(this);
        localeShared = new LocaleShared(this);
        userId = localeShared.getId();
        setUpPermissions();
        getAvaliableDonates();
    }

    private void setUpPermissions() {
        String per[] = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        ActivityCompat.requestPermissions(this, per, 1);
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
        LatLng sydney = new LatLng(mgps.getLatitude(), mgps.getLongitude());
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                sydney, 15);
        mMap.animateCamera(location);
    }

    private void getAvaliableDonates() {
        AndroidNetworking.initialize(this);
        AndroidNetworking.setParserFactory(new JacksonParserFactory());
        AndroidNetworking.post(Urls.getAvaliableDonats)
                .addBodyParameter("current_lat", "" + mgps.getLatitude())
                .addBodyParameter("current_lng", "" + mgps.getLongitude())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response", "" + response);
                        try {
                            String status = response.getString("status");
                            if (status.equals("1")) {
                                JSONArray donate = response.getJSONArray("donations");
                                for (int i = 0; i < donate.length(); i++) {
                                    JSONObject jsonObject = donate.getJSONObject(i);
                                    createMarker(Double.parseDouble(jsonObject.getString("address_lat")), Double.parseDouble(jsonObject.getString("address_lng")), jsonObject.getString("id"), R.mipmap.marker_map);
                                }

                                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                    @Override
                                    public boolean onMarkerClick(Marker marker) {
                                        showPopUpAddAddress();
                                        snippet = marker.getSnippet();
                                        getDonationDetails(snippet);
                                        return true;
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("error", "" + anError.getMessage());
                    }
                });
    }

    protected Marker createMarker(double latitude, double longitude, String snippt, int iconResID) {

        Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).icon(BitmapDescriptorFactory
                .fromResource(iconResID)).snippet(snippt));

        return marker;
    }


    private void showPopUpAddAddress() {
        Dialog d = new Dialog(this);
        d.setContentView(R.layout.popup_details);
        donationQuantity = d.findViewById(R.id.donationQuantity);
        donationType = d.findViewById(R.id.donationType);
        tvDesc = d.findViewById(R.id.tvDesc);
        btnPlace = d.findViewById(R.id.btnPlace);
        IneedThisDonation = d.findViewById(R.id.IneedThisDonation);
        popUpDonate = d.findViewById(R.id.popUpDonate);
        IneedThisDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNeedDonation();
            }
        });


        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        d.getWindow().setLayout(width, height);
        d.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;
        d.setCanceledOnTouchOutside(false);
        d.show();
    }

    private void userNeedDonation() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.uploading));
        progressDialog.show();
        AndroidNetworking.initialize(this);
        AndroidNetworking.setParserFactory(new JacksonParserFactory());
        AndroidNetworking.post(Urls.userApplyForDonation)
                .addBodyParameter("user_id", userId)
                .addBodyParameter("donation_id", snippet)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            String status = response.getString("status");
                            if (status.equals("1")) {
                                startActivity(new Intent(MostafeedHomeActivity.this, DonateRecieveActivity.class));
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

    private void getDonationDetails(final String donationId) {
        AndroidNetworking.initialize(this);
        AndroidNetworking.setParserFactory(new JacksonParserFactory());
        AndroidNetworking.post(Urls.getDonationDetails)
                .addBodyParameter("donation_id", donationId)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if (status.equals("1")) {
                                JSONObject donation = response.getJSONObject("donation");
                                tvDesc.setText(donation.getString("description"));
                                Log.e("image", Urls.imageBaseUrl + donation.getString("image"));
                                Picasso.with(MostafeedHomeActivity.this).load(Urls.imageBaseUrl + donation.getString("image")).into(popUpDonate);
                                if (donation.getString("food").equals("1")) {
                                    donationType.setText(getString(R.string.food));
                                } else if (donation.getString("need").equals("1")) {
                                    donationType.setText(getString(R.string.accessories));
                                }
                                if (donation.getString("for_more_than_10").equals("1")) {
                                    donationQuantity.setText(getString(R.string.morethan10));
                                } else if (donation.getString("for_less_than_10").equals("1")) {
                                    donationQuantity.setText(getString(R.string.lessthan10));
                                }
                                if (donation.getString("for_more_than_3").equals("1")) {
                                    donationQuantity.setText(getString(R.string.morethan10));
                                } else if (donation.getString("for_less_than_3").equals("1")) {
                                    donationQuantity.setText(getString(R.string.lessThan3));
                                }

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

    @Override
    public void onDrawerItemSelected(View view, int position) {

        switch (position) {
            case 6:
                SharedPreferences sh = getSharedPreferences("userData", MODE_PRIVATE);
                Intent i_exsit = new Intent(MostafeedHomeActivity.this, LoginActivity.class);
                i_exsit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i_exsit);
                sh.edit().putBoolean("userExist", false).apply();
                finish();
                break;
        }
    }
}

