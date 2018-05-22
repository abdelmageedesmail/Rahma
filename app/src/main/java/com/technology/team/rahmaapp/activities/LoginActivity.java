package com.technology.team.rahmaapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.technology.team.rahmaapp.R;
import com.technology.team.rahmaapp.activities.charities.Recyclerlist;

public class LoginActivity extends AppCompatActivity {

    TextView marqueeTxt,tvRegister;
    Button btnLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUpRefrence();
    }

    private void setUpRefrence(){
        marqueeTxt = findViewById(R.id.mywidget);
        btnLogin=findViewById(R.id.btnLogin);
        tvRegister=findViewById(R.id.tvRegister);
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignUp.class));

            }
        });
        marqueeTxt .setSelected(true);  // Set focus to the textview

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ChooseUser.class));
            }
        });
    }
}
