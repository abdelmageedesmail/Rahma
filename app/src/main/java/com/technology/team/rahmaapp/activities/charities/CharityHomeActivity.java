package com.technology.team.rahmaapp.activities.charities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.technology.team.rahmaapp.R;
import com.technology.team.rahmaapp.activities.LoginActivity;
import com.technology.team.rahmaapp.activities.donaters.AddNewDonation;
import com.technology.team.rahmaapp.activities.donaters.AddressListActivity;
import com.technology.team.rahmaapp.activities.donaters.HomeActivity;
import com.technology.team.rahmaapp.fragmen.FragmentDrawer;

public class CharityHomeActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener{
    DrawerLayout mDrawerLayout;
    FragmentDrawer drawerFragment;
    Toolbar toolbar;
    TextView marqueeTxt;
    CardView addressList,liAddNewDonation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charity_home);
        setUpReference();
    }

    private void setUpReference() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.mDrawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        addressList=findViewById(R.id.addressList);

        liAddNewDonation=findViewById(R.id.liAddNewDonation);
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, mDrawerLayout, toolbar);
        drawerFragment.setDrawerListener(this);
        liAddNewDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CharityHomeActivity.this,AddEmployee.class));
            }
        });

    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        switch (position){
            case 6:
                SharedPreferences sh = getSharedPreferences("userData", MODE_PRIVATE);
                Intent i_exsit = new Intent(CharityHomeActivity.this, LoginActivity.class);
                i_exsit.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i_exsit);
                sh.edit().putBoolean("userExist", false).apply();
                finish();
                break;
        }
    }
}
