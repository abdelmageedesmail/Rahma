package com.technology.team.rahmaapp.activities.donaters;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.technology.team.rahmaapp.R;

public class AddNewDonation extends AppCompatActivity {

    TextView marqueeTxt;
    LinearLayout addFood,addAccessories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_donation);
        marqueeTxt = findViewById(R.id.mywidget);
        marqueeTxt .setSelected(true);  // Set focus to the textview
        addFood=findViewById(R.id.addFood);
        addAccessories=findViewById(R.id.addAccessories);
        addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddNewDonation.this,AddNewFood.class));
            }
        });

        addAccessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddNewDonation.this,AddAccessories.class));
            }
        });
    }
}
