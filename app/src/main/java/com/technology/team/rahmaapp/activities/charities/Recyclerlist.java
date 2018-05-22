package com.technology.team.rahmaapp.activities.charities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.jacksonandroidnetworking.JacksonParserFactory;
import com.technology.team.rahmaapp.R;
import com.technology.team.rahmaapp.adapters.ParentAdapter;
import com.technology.team.rahmaapp.models.InnerModel;
import com.technology.team.rahmaapp.models.QuestionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Recyclerlist extends AppCompatActivity {

    RecyclerView recyclerQuestion;
    Button btnNext, btnPrev;
    ArrayList<QuestionModel> arrayList;
    ArrayList<InnerModel> innerModelArrayList, innerModelArrayList2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerlist);
        recyclerQuestion = findViewById(R.id.recyclerQuestion);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        arrayList = new ArrayList<>();

        getData();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                innerModelArrayList.clear();
                getData();
            }
        });
    }

    private void getData() {
        AndroidNetworking.initialize(this);
        AndroidNetworking.setParserFactory(new JacksonParserFactory());
        AndroidNetworking.get("http://logi.metrvocato.com/api/Common/GetAllCustomizedMobeli?SpecaialtyServiceID=1125&lng=1")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            innerModelArrayList = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
                                QuestionModel model = new QuestionModel();
                                model.setTitle(object.getString("text"));
                                model.setAnswertype(object.getString("AnswerType"));
                                JSONArray children = object.getJSONArray("children");
                                for (int j = 0; j < children.length(); j++) {
                                    JSONObject innerObject = children.getJSONObject(j);
                                    InnerModel innerModel = new InnerModel();
                                    innerModel.setTitle(innerObject.getString("text"));
                                    innerModel.setAnswerType(innerObject.getString("AnswerType"));
                                    innerModelArrayList.add(innerModel);
                                    model.setArrayList(innerModelArrayList);
                                    innerModelArrayList2 = new ArrayList<>();
                                    if (innerObject.getString("AnswerType").equals("4")) {
                                        JSONArray children1 = innerObject.getJSONArray("children");
                                        for (int x = 0; x < children1.length(); x++) {
                                            JSONObject objectKhara = children1.getJSONObject(x);
                                            InnerModel innerModel1 = new InnerModel();
                                            innerModel1.setTitle(objectKhara.getString("text"));
                                            innerModel1.setAnswerType(object.getString("AnswerType"));
                                            innerModelArrayList2.add(innerModel1);
                                            model.setArrayList(innerModelArrayList2);
                                        }
                                    }
                                }
                                arrayList.add(model);
                            }
                            recyclerQuestion.setAdapter(new ParentAdapter(Recyclerlist.this, arrayList));
                            recyclerQuestion.setLayoutManager(new LinearLayoutManager(Recyclerlist.this, LinearLayoutManager.VERTICAL, false));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("error", "" + anError.getMessage());
                    }
                });
    }
}
