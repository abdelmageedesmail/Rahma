package com.technology.team.rahmaapp.activities.donaters;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.jacksonandroidnetworking.JacksonParserFactory;
import com.technology.team.rahmaapp.R;
import com.technology.team.rahmaapp.adapters.MyAdapter;
import com.technology.team.rahmaapp.classes.LocaleShared;
import com.technology.team.rahmaapp.classes.Urls;
import com.technology.team.rahmaapp.models.SpinnerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddAccessories extends AppCompatActivity {

    TextView marqueeTxt;
    CheckBox chMoreThan,chLessThan;
    LinearLayout liAddImage;
    ImageView ivDonate;
    EditText etDesc;
    Spinner spAddress;
    FrameLayout addNewAddress;
    Button btnPublishAddress;
    private String id;
    ArrayList<SpinnerModel> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_accessories);
        LocaleShared localeShared=new LocaleShared(this);
        id = localeShared.getId();
        arrayList=new ArrayList<>();
        setUpRefrence();
    }

    private void setUpRefrence(){
        marqueeTxt = findViewById(R.id.mywidget);
        marqueeTxt .setSelected(true);  // Set focus to the textview
        chMoreThan=findViewById(R.id.chMoreThan);
        chLessThan=findViewById(R.id.chLessThan);
        liAddImage=findViewById(R.id.liAddImage);
        ivDonate=findViewById(R.id.ivDonate);
        etDesc=findViewById(R.id.etDesc);
        spAddress=findViewById(R.id.spAddress);
        addNewAddress=findViewById(R.id.addNewAddress);
        btnPublishAddress=findViewById(R.id.btnPublishAddress);
        getAddressList();
    }

    private void getAddressList(){
        AndroidNetworking.initialize(this);
        AndroidNetworking.setParserFactory(new JacksonParserFactory());
        AndroidNetworking.post(Urls.getUSerAddress)
                .addBodyParameter("user_id",id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if (status.equals("1")){
                                JSONArray user_addresses = response.getJSONArray("user_addresses");
                                for (int i=0;i<user_addresses.length();i++){
                                    JSONObject jsonObject = user_addresses.getJSONObject(i);
                                    SpinnerModel model=new SpinnerModel();
                                    model.setName(jsonObject.getString("title"));
                                    model.setId(jsonObject.getString("id"));
                                    arrayList.add(model);
                                }
                                if (arrayList.size()>0){
                                    spAddress.setAdapter(new MyAdapter(AddAccessories.this,arrayList));
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

}
