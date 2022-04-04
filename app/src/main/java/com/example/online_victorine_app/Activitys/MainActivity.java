package com.example.online_victorine_app.Activitys;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.online_victorine_app.Adapters.SearchListAdapter;
import com.example.online_victorine_app.Adapters.TestListAdapter;
import com.example.online_victorine_app.Obj.AppAPI;
import com.example.online_victorine_app.Obj.CircleTransform;
import com.example.online_victorine_app.Obj.Consts;
import com.example.online_victorine_app.Obj.Helper;
import com.example.online_victorine_app.Obj.NetWork;
import com.example.online_victorine_app.Obj.TestObj;
import com.example.online_victorine_app.Obj.TestsThemesObj;
import com.example.online_victorine_app.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, NavigationView.OnNavigationItemSelectedListener{

    public static RecyclerView ListTheme;
    public static ConstraintLayout LoadBar;
    private ImageView logo;
    private EditText InputSearch;
    private ImageButton BtnSearch;
    private DrawerLayout drawer;
    private ImageView UserImageView;
    private TextView UserNameView;
    private TextView UserEmailView;
    public static SharedPreferences sp;
    private NavigationView menu;

    public static TestListAdapter adapter;
    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;
    private Animation animClick;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_OnlineVictorineApp);
        setContentView(R.layout.activity_main);
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

        drawer = findViewById(R.id.drawer_layout);
        menu = (NavigationView) findViewById(R.id.nav_view);
        logo = (ImageView) findViewById(R.id.logo);

        ListTheme = (RecyclerView) findViewById(R.id.ListTheme);
        ListTheme.setLayoutManager(new LinearLayoutManager(this));

        LoadBar = (ConstraintLayout) findViewById(R.id.LoadBar);
        InputSearch = (EditText) findViewById(R.id.InputSearch);
        BtnSearch = (ImageButton) findViewById(R.id.BtnSearch);

        adapter = new TestListAdapter(MainActivity.this);
        ListTheme.setAdapter(adapter);

        if(NetWork.isConnection(MainActivity.this)){
            getThemesList();
        }else{
            Helper.getDataInStorage();
        }

        BtnSearch.setOnClickListener(this);
        logo.setOnClickListener(this);
        menu.setNavigationItemSelectedListener(this);
        animClick = AnimationUtils.loadAnimation(this, R.anim.btn_click1);
    }
    private void getThemesList() {
        Call<TestsThemesObj> call = AppAPI.create().getThemesListData(Consts.API_KEY, "themes");
        call.enqueue(new Callback<TestsThemesObj>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<TestsThemesObj> call, Response<TestsThemesObj> response) {
                TestListAdapter.themes = response.body().getThemes();
                TestListAdapter.themes.add(0,null);
                getTestsOfThemeList();
            }
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onFailure(Call<TestsThemesObj> call, Throwable t) {
                System.out.println(t);
                Helper.getDataInStorage();
            }
        });
    }
    private void getTestsOfThemeList() {
        for(int i = 1; i < TestListAdapter.themes.size(); i++){
            String itemThemes = TestListAdapter.themes.get(i);
            Call<List<TestObj>> call = AppAPI.create().getTestsOfThemeData(Consts.API_KEY,"byTheme",itemThemes);
            int finalI = i;
            call.enqueue(new Callback<List<TestObj>>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(Call<List<TestObj>> call, Response<List<TestObj>> response) {
                    TestListAdapter.savedData.put(itemThemes,response.body());
                    if(finalI == TestListAdapter.themes.size()-1){
                        LoadResult();
                    }
                }
                @Override
                public void onFailure(Call<List<TestObj>> call, Throwable t) {
                    System.out.println(t);
                }
            });
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void LoadResult(){
        adapter.notifyDataSetChanged();
        SharedPreferences.Editor editor = sp.edit();
        Gson gson = new Gson();
        String jsonThemes = gson.toJson(TestListAdapter.themes);
        String jsonSavedData = gson.toJson(TestListAdapter.savedData);
        editor.putString("themes", jsonThemes);
        editor.putString("savedData", jsonSavedData);
        editor.apply();
        LoadBar.setVisibility(View.GONE);
    }
    private void SearchTests(){
        List<TestObj> searchList = new ArrayList<>();
        String searchText = InputSearch.getText().toString();
        for (Map.Entry<String, List<TestObj>> entry : TestListAdapter.savedData.entrySet()) {
            List<TestObj> tests = entry.getValue();
            for(TestObj test : tests){
                if(!searchText.isEmpty()){
                    String data = test.toString();
                    if(data.contains(searchText) || data.contains(searchText.toUpperCase()) ||
                            data.contains(searchText.toLowerCase())){
                        searchList.add(test);
                    }
                }else{
                    searchList.add(test);
                }
            }
        }
        AlertDialog.Builder dialogComm = new AlertDialog.Builder(MainActivity.this);
        View v = View.inflate(MainActivity.this, R.layout.recycler_list, null);
        dialogComm.setView(v);
        AlertDialog newDialog = dialogComm.create();
        RecyclerView SearchListView = (RecyclerView) v.findViewById(R.id.SearchListView);
        SearchListView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        SearchListAdapter searchListAdapter = new SearchListAdapter(MainActivity.this, searchList);
        SearchListView.setAdapter(searchListAdapter);
        newDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        newDialog.show();
    }

    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            View headerLayout = menu.getHeaderView(0);
            UserImageView = headerLayout.findViewById(R.id.UserImageView);
            UserNameView = headerLayout.findViewById(R.id.UserNameView);
            UserEmailView = headerLayout.findViewById(R.id.UserEmailView);
            GoogleSignInAccount account = result.getSignInAccount();
            UserNameView.setText(account.getDisplayName());
            UserEmailView.setText(account.getEmail());
            Picasso.get().load(account.getPhotoUrl()).transform(new CircleTransform()).error(R.drawable.ic_person)
                    .placeholder(R.drawable.ic_person).into(UserImageView);
        }else{
            gotoLogin();
        }
    }
    private void gotoLogin(){
        Intent intent = new Intent(MainActivity.this,Login_RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        OptionalPendingResult<GoogleSignInResult> opr= Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if(opr.isDone()){
            GoogleSignInResult result=opr.get();
            handleSignInResult(result);
        }else{
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.b_profile:
                if(!NetWork.isConnection(MainActivity.this)){
                    Toast.makeText(getApplicationContext(),getString(R.string.internet_error), Toast.LENGTH_LONG).show();
                    return false;
                }
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.b_logOut:
                AlertDialog.Builder dialogExit = new AlertDialog.Builder(MainActivity.this);
                dialogExit.setIcon(R.drawable.logo);
                dialogExit.setTitle(getString(R.string.logout));
                dialogExit.setMessage(getString(R.string.enable_logout));
                dialogExit.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                if (status.isSuccess()){
                                    gotoLogin();
                                }else{
                                    Toast.makeText(getApplicationContext(),getString(R.string.internet_error), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
                dialogExit.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog newDialog2 = dialogExit.create();
                newDialog2.show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }
    @Override
    public void onClick(View view) {
        view.startAnimation(animClick);
        switch (view.getId()){
            case R.id.BtnSearch:
                SearchTests();
                break;
            case R.id.logo:
                drawer.openDrawer(GravityCompat.START);
                break;
        }
    }
    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }
}