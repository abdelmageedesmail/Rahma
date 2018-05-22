package com.technology.team.rahmaapp.activities.donaters;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.technology.team.rahmaapp.R;
import com.technology.team.rahmaapp.adapters.donaterAdapter.AddressAdapter;

import java.util.Locale;

public class AddressListActivity extends AppCompatActivity {

    RecyclerView addressList;
    TextView marqueeTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);
        Locale locale = new Locale("ar");
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        addressList=findViewById(R.id.addressList);
        marqueeTxt = findViewById(R.id.mywidget);
        marqueeTxt .setSelected(true);  // Set focus to the textview
        addressList.setAdapter(new AddressAdapter(this));
        addressList.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
    }
}
