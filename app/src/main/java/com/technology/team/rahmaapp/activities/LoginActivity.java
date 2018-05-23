package com.technology.team.rahmaapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.jacksonandroidnetworking.JacksonParserFactory;
import com.technology.team.rahmaapp.R;
import com.technology.team.rahmaapp.activities.donaters.HomeActivity;
import com.technology.team.rahmaapp.classes.LocaleShared;
import com.technology.team.rahmaapp.classes.Urls;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    TextView marqueeTxt,tvRegister;
    Button btnLogin;
    EditText etEmail,etpass;
    private LocaleShared localeShared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setUpRefrence();
        localeShared=new LocaleShared(this);
    }

    private void setUpRefrence(){
        marqueeTxt = findViewById(R.id.mywidget);
        btnLogin=findViewById(R.id.btnLogin);
        tvRegister=findViewById(R.id.tvRegister);
        etEmail=findViewById(R.id.etEmail);
        etpass=findViewById(R.id.etPass);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ChooseUser.class));

            }
        });
        marqueeTxt .setSelected(true);  // Set focus to the textview
        SharedPreferences shaerd = getSharedPreferences("userData", MODE_PRIVATE);
        if (!shaerd.getBoolean("userExist", false)) {
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setUpValidation();
                }
            });
        }else{
            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
        }
    }

    private void setUpValidation(){
        if (etEmail.getText().toString().equals("")||etEmail.getText().toString().isEmpty()){
            etEmail.setError(getString(R.string.emptyFiled));
        }else if (etpass.getText().toString().equals("")||etpass.getText().toString().isEmpty()){
            etpass.setError(getString(R.string.emptyFiled));
        }else {
            userLogin();
        }
    }

    private void userLogin() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.uploading));
        progressDialog.show();
        AndroidNetworking.initialize(this);
        AndroidNetworking.setParserFactory(new JacksonParserFactory());
        AndroidNetworking.post(Urls.login)
                .addBodyParameter("email", etEmail.getText().toString())
                .addBodyParameter("password", etpass.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            progressDialog.dismiss();
                            String status = response.getString("status");
                            if (status.equals("1")) {
                                JSONObject user = response.getJSONObject("user");
                                localeShared.storeId(user.getString("id"));
                                localeShared.storeKey("name", user.getString("name"));
                                localeShared.storeKey("phone", user.getString("phone"));
                                localeShared.storeKey("email", user.getString("email"));
                                localeShared.storeKey("type",user.getString("type"));
                                SharedPreferences sh = getSharedPreferences("userData", MODE_PRIVATE);
                                SharedPreferences.Editor edit = sh.edit();
                                edit.putBoolean("userExist", true).apply();
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
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
