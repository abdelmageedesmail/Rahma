package com.technology.team.rahmaapp.classes;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by abdelmageed on 05/10/17.
 */

public class LocaleShared {

    Context context;
    SharedPreferences sharedPreferences;
    String key = "userData";
    private SharedPreferences.Editor edit;
    private String id="id";
    private String token="token";
    private String userExist="userExist";

    public LocaleShared(Context context) {
        this.context = context;
        sharedPreferences=context.getSharedPreferences(key, Context.MODE_PRIVATE);
    }

    public void storeId(String text){
        edit = sharedPreferences.edit();
        edit.putString(id,text);
        edit.apply();
    }

    public String getId(){
        sharedPreferences=context.getSharedPreferences(key, Context.MODE_PRIVATE);
        String userId=sharedPreferences.getString(id,"null");
        return userId;
    }


    public void storeToken(String text){
        edit = sharedPreferences.edit();
        edit.putString(token,text);
        edit.apply();
    }

    public String getToken(){
        sharedPreferences=context.getSharedPreferences(key, Context.MODE_PRIVATE);
        String value=sharedPreferences.getString(token,"null");
        return value;
    }


    public void setUserLogOut(){
        edit = sharedPreferences.edit();
        edit.putBoolean(id,false);
        edit.apply();
    }

    public boolean getLogoutState(){
        sharedPreferences=context.getSharedPreferences(key, Context.MODE_PRIVATE);
        boolean exist=sharedPreferences.getBoolean(userExist,false);
        return exist;
    }

    public void storeKey(String key, String text){
        edit = sharedPreferences.edit();
        edit.putString(key,text);
        edit.apply();
    }

    public String getKey(String textKey){
        sharedPreferences=context.getSharedPreferences(key, Context.MODE_PRIVATE);
        String value=sharedPreferences.getString(textKey,"null");
        return value;
    }

}
