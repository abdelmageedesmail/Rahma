package com.technology.team.rahmaapp.activities.donaters;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.jacksonandroidnetworking.JacksonParserFactory;
import com.technology.team.rahmaapp.R;
import com.technology.team.rahmaapp.adapters.donaterAdapter.AddressAdapter;
import com.technology.team.rahmaapp.classes.LocaleShared;
import com.technology.team.rahmaapp.classes.Urls;
import com.technology.team.rahmaapp.models.AdressListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

public class AddressListActivity extends AppCompatActivity {

    RecyclerView addressList;
    TextView marqueeTxt,noData;
    Button btnAddNewAddress;
    private String id;
    ArrayList<AdressListModel> arrayList;
    ProgressBar pbProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        LocaleShared localeShared=new LocaleShared(this);
        id = localeShared.getId();
        arrayList=new ArrayList<>();
        setUpRefrence();
    }

    private void setUpRefrence(){
        addressList=findViewById(R.id.addressList);
        marqueeTxt = findViewById(R.id.mywidget);
        btnAddNewAddress=findViewById(R.id.btnAddNewAddress);
        pbProgress=findViewById(R.id.pbProgress);
        noData=findViewById(R.id.noData);
        marqueeTxt .setSelected(true);  // Set focus to the textview
        btnAddNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddressListActivity.this,MapsActivity.class));
            }
        });
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
                            pbProgress.setVisibility(View.GONE);
                            String status = response.getString("status");
                            if (status.equals("1")){
                                JSONArray user_addresses = response.getJSONArray("user_addresses");
                                for (int i=0;i<user_addresses.length();i++){
                                    JSONObject jsonObject = user_addresses.getJSONObject(i);
                                    AdressListModel model=new AdressListModel();
                                    model.setTitle(jsonObject.getString("title"));
                                    model.setId(jsonObject.getString("id"));
                                    model.setHouseNumber(jsonObject.getString("house_number"));
                                    model.setFloorNumber(jsonObject.getString("floor_number"));
                                    arrayList.add(model);
                                }
                                if (arrayList.size()>0){
                                    addressList.setAdapter(new AddressAdapter(AddressListActivity.this,arrayList));
                                    addressList.setLayoutManager(new LinearLayoutManager(AddressListActivity.this,LinearLayoutManager.VERTICAL,false));
                                }else {
                                    noData.setVisibility(View.VISIBLE);
                                    noData.setText(getString(R.string.noData));
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        pbProgress.setVisibility(View.GONE);

                    }
                });
    }
}
