package com.technology.team.rahmaapp.activities.donaters;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.jacksonandroidnetworking.JacksonParserFactory;
import com.technology.team.rahmaapp.R;
import com.technology.team.rahmaapp.adapters.donaterAdapter.AddressAdapter;
import com.technology.team.rahmaapp.adapters.donaterAdapter.MyDonationAdapter;
import com.technology.team.rahmaapp.classes.LocaleShared;
import com.technology.team.rahmaapp.classes.Urls;
import com.technology.team.rahmaapp.models.AdressListModel;
import com.technology.team.rahmaapp.models.MyDonationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyDonations extends AppCompatActivity {
    RecyclerView addressList;
    TextView marqueeTxt, noData;
    private String id;
    ProgressBar pbProgress;
    ArrayList<MyDonationModel> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_donations);
        arrayList=new ArrayList<>();
        LocaleShared localeShared = new LocaleShared(this);
        id = localeShared.getId();
        setUpRefrence();
    }

    private void setUpRefrence() {
        addressList = findViewById(R.id.addressList);
        marqueeTxt = findViewById(R.id.mywidget);
        pbProgress = findViewById(R.id.pbProgress);
        noData = findViewById(R.id.noData);
        marqueeTxt.setSelected(true);  // Set focus to the textview
        getMyDonations();

    }

    private void getMyDonations(){
        AndroidNetworking.initialize(this);
        AndroidNetworking.setParserFactory(new JacksonParserFactory());
        AndroidNetworking.post(Urls.get_my_donations)
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
                                JSONArray user_addresses = response.getJSONArray("donations");
                                for (int i=0;i<user_addresses.length();i++){
                                    JSONObject jsonObject = user_addresses.getJSONObject(i);
                                    MyDonationModel model=new MyDonationModel();
                                    String food = jsonObject.getString("food");
                                    String needs = jsonObject.getString("needs");
                                    if (food.equals("1")){
                                        model.setType(getString(R.string.food));
                                    }else {
                                        model.setType(getString(R.string.accessories));
                                    }
                                    String for_more_than_10 = jsonObject.getString("for_more_than_10");
                                    String for_less_than_10 = jsonObject.getString("for_less_than_10");
                                    String for_more_than_3 = jsonObject.getString("for_more_than_3");
                                    String for_less_than_3 = jsonObject.getString("for_less_than_3");
                                    if (for_more_than_10.equals("1")){
                                        model.setQuanitity(getString(R.string.foodForMore10));
                                    }else if (for_less_than_10.equals("1")){
                                        model.setQuanitity(getString(R.string.foodForLess10));
                                    }

                                    if (for_more_than_3.equals("1")){
                                        model.setQuanitity(getString(R.string.accessoriesForMore10));
                                    }else if (for_less_than_3.equals("1")){
                                        model.setQuanitity(getString(R.string.accessoriesForLess10));
                                    }
                                    model.setId(jsonObject.getString("id"));
                                    arrayList.add(model);
                                }
                                if (arrayList.size()>0){
                                    addressList.setAdapter(new MyDonationAdapter(MyDonations.this,arrayList));
                                    addressList.setLayoutManager(new LinearLayoutManager(MyDonations.this,LinearLayoutManager.VERTICAL,false));
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
