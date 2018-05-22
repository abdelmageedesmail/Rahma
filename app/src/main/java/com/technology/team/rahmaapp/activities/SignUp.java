package com.technology.team.rahmaapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.technology.team.rahmaapp.R;

public class SignUp extends AppCompatActivity {

    private TextView marqueeTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setUpRefrence();
    }

    private void setUpRefrence(){
        marqueeTxt = findViewById(R.id.mywidget);
        marqueeTxt .setSelected(true);  // Set focus to the textview
    }
}
