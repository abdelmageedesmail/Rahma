package com.technology.team.rahmaapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.technology.team.rahmaapp.R;
import com.technology.team.rahmaapp.activities.donaters.HomeActivity;

public class ChooseUser extends AppCompatActivity {

    Button btnDonate,btnMostafeed,btnCharity;
    TextView marqueeTxt;
    int fromUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user);
        setUpRefrence();
    }

    private void setUpRefrence(){
        btnDonate=findViewById(R.id.btnDonate);
        btnMostafeed=findViewById(R.id.btnMostafeed);
        btnCharity=findViewById(R.id.btnCharity);
        marqueeTxt = findViewById(R.id.mywidget);
        marqueeTxt .setSelected(true);  // Set focus to the textview

        btnDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseUser.this, SignUp.class);
                intent.putExtra("type",1);
                startActivity(intent);
            }
        });

        btnMostafeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseUser.this, SignUp.class);
                intent.putExtra("type",2);
                startActivity(intent);
            }
        });

        btnCharity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseUser.this, SignUp.class);
                intent.putExtra("type",3);
                startActivity(intent);
            }
        });
    }
}
