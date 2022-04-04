package com.example.online_victorine_app.Obj;

import static com.example.online_victorine_app.Activitys.MainActivity.LoadBar;
import static com.example.online_victorine_app.Activitys.MainActivity.adapter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.example.online_victorine_app.Activitys.MainActivity;
import com.example.online_victorine_app.Adapters.TestListAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class Helper {

    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void getDataInStorage(){
        Gson gson = new Gson();
        String jsonThemes = MainActivity.sp.getString("themes", "");
        String jsonSavedData = MainActivity.sp.getString("savedData", "");
        if(jsonThemes.isEmpty()||jsonSavedData.isEmpty()){
            return;
        }
        Type type1 = new TypeToken<List<String>>(){}.getType();
        Type type2 = new TypeToken<Map<String, List<TestObj>>>(){}.getType();
        TestListAdapter.themes = gson.fromJson(jsonThemes, type1);
        TestListAdapter.savedData = gson.fromJson(jsonSavedData, type2);
        try {
            adapter.notifyDataSetChanged();
        }catch (Exception e){}
        LoadBar.setVisibility(View.GONE);
    }
}
