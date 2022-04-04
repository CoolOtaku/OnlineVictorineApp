package com.example.online_victorine_app.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.online_victorine_app.Obj.AppAPI;
import com.example.online_victorine_app.Obj.CircleTransform;
import com.example.online_victorine_app.Obj.Consts;
import com.example.online_victorine_app.Obj.UserStatistic;
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
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    private ImageButton BtnBack;
    private ImageButton BtnLogOut;
    private ImageView UserProfileAvatar;
    private TextView UserProfileNameView;
    private TextView UserProfileEmailView;
    private TextView StatisticsTextView;
    private TextView ProgressText;
    private ProgressBar ProgressProfile;

    private TextView PerfectlyResText;
    private TextView NotBadResText;
    private TextView NormalResText;
    private TextView BadlyResText;

    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;
    private Animation animClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_OnlineVictorineApp);
        setContentView(R.layout.activity_profile);

        BtnBack = (ImageButton) findViewById(R.id.BtnBack);
        BtnLogOut = (ImageButton) findViewById(R.id.BtnLogOut);
        UserProfileAvatar = (ImageView) findViewById(R.id.UserProfileAvatar);
        UserProfileNameView = (TextView) findViewById(R.id.UserProfileNameView);
        UserProfileEmailView = (TextView) findViewById(R.id.UserProfileEmailView);
        StatisticsTextView = (TextView) findViewById(R.id.StatisticsTextView);
        ProgressText = (TextView) findViewById(R.id.ProgressText);
        ProgressProfile = (ProgressBar) findViewById(R.id.ProgressProfile);

        PerfectlyResText = (TextView) findViewById(R.id.PerfectlyResText);
        NotBadResText = (TextView) findViewById(R.id.NotBadResText);
        NormalResText = (TextView) findViewById(R.id.NormalResText);
        BadlyResText = (TextView) findViewById(R.id.BadlyResText);

        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

        BtnBack.setOnClickListener(this);
        BtnLogOut.setOnClickListener(this);
        animClick = AnimationUtils.loadAnimation(this, R.anim.btn_click1);
    }
    @Override
    public void onClick(View view) {
        view.startAnimation(animClick);
        switch (view.getId()){
            case R.id.BtnBack:
                onBackPressed();
                break;
            case R.id.BtnLogOut:
                AlertDialog.Builder dialogExit = new AlertDialog.Builder(ProfileActivity.this);
                dialogExit.setIcon(R.drawable.logo);
                dialogExit.setTitle(getString(R.string.logout));
                dialogExit.setMessage(getString(R.string.enable_logout));
                dialogExit.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog newDialog2 = dialogExit.create();
                        newDialog2.show();
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
    }
    private void gotoLogin(){
        Intent intent = new Intent(ProfileActivity.this, Login_RegisterActivity.class);
        startActivity(intent);
    }
    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            assert account != null;
            try{
                Picasso.get().load(account.getPhotoUrl()).transform(new CircleTransform()).error(R.drawable.ic_person)
                        .placeholder(R.drawable.ic_person).into(UserProfileAvatar);
            }catch (NullPointerException e){
                Toast.makeText(getApplicationContext(),"image not found", Toast.LENGTH_LONG).show();
            }
            UserProfileNameView.setText(account.getDisplayName());
            UserProfileEmailView.setText(account.getEmail());
            Call<UserStatistic> call = AppAPI.create().getUserStatistics(Consts.API_KEY,account.getEmail());
            call.enqueue(new Callback<UserStatistic>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(Call<UserStatistic> call, Response<UserStatistic> response) {
                    UserStatistic statistic = response.body();
                    float percentage;
                    try {
                        percentage = (statistic.getQuestion_true() * 100 / statistic.getQuestion_count());
                    }catch (ArithmeticException e){
                        percentage = 0;
                    }
                    int guestion_false_count = statistic.getQuestion_count() - statistic.getQuestion_true();
                    String statisticText = getString(R.string.question_count)+statistic.getQuestion_count()+";\n\n";
                    statisticText += getString(R.string.question_true)+statistic.getQuestion_true()+";\n\n";
                    statisticText += getString(R.string.question_false)+guestion_false_count+";\n\n";
                    statisticText += getString(R.string.test_count)+statistic.getTest_count()+";\n\n";
                    statisticText += getString(R.string.total_interest)+percentage+"%.";
                    StatisticsTextView.setText(statisticText);
                    ProgressText.setText(percentage+"%");
                    ProgressProfile.setProgress((int) percentage);

                    PerfectlyResText.setText(getString(R.string.perfectly_res)+statistic.getPerfectly_res());
                    NotBadResText.setText(getString(R.string.not_bad_res)+statistic.getNot_bad_res());
                    NormalResText.setText(getString(R.string.normal_res)+statistic.getNormal_res());
                    BadlyResText.setText(getString(R.string.badly_res)+statistic.getBadly_res());
                }
                @Override
                public void onFailure(Call<UserStatistic> call, Throwable t) {
                    System.out.println(t);
                }
            });
        }else{
            onBackPressed();
        }
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
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}