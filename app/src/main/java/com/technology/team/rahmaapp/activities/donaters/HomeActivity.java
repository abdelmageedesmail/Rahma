package com.technology.team.rahmaapp.activities.donaters;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.technology.team.rahmaapp.R;
import com.technology.team.rahmaapp.fragmen.FragmentDrawer;

import java.util.ArrayList;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    DrawerLayout mDrawerLayout;
    FragmentDrawer drawerFragment;
    Toolbar toolbar;
    TextView marqueeTxt;
    CardView addressList,liAddNewDonation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setUpReference();

    }
    private void setUpReference() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.mDrawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        marqueeTxt = findViewById(R.id.mywidget);
        addressList=findViewById(R.id.addressList);
        marqueeTxt .setSelected(true);  // Set focus to the textview
        liAddNewDonation=findViewById(R.id.liAddNewDonation);
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, mDrawerLayout, toolbar);
        drawerFragment.setDrawerListener(this);
        addressList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,AddressListActivity.class));
            }
        });

        liAddNewDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,AddNewDonation.class));
            }
        });
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {

    }
}
