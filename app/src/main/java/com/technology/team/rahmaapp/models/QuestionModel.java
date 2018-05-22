package com.technology.team.rahmaapp.models;

import java.util.ArrayList;

public class QuestionModel {
    private String title;
    private String answertype;
    private ArrayList<InnerModel> arrayList;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnswertype() {
        return answertype;
    }

    public void setAnswertype(String answertype) {
        this.answertype = answertype;
    }

    public ArrayList<InnerModel> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<InnerModel> arrayList) {
        this.arrayList = arrayList;
    }
}
