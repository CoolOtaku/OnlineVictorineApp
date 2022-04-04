package com.example.online_victorine_app.Activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.online_victorine_app.Obj.AppAPI;
import com.example.online_victorine_app.Obj.Consts;
import com.example.online_victorine_app.Obj.QuestionObj;
import com.example.online_victorine_app.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassingTestActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private TextView TitleTest;
    private ImageButton BtnBack;
    private ImageView TestImage;
    private TextView QuestionTitle;
    private TextView QuestionStatus;
    private LinearLayout PassingTestContainer;

    private List<QuestionObj> questions = new ArrayList<>();
    private List<Integer> answersSets = new ArrayList<>();
    private int QuestionPosition = -1;
    private int CountTrueReply = 0;
    private boolean IsPassing = true;

    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;
    private String user_login = "";
    private Animation animClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_OnlineVictorineApp);
        setContentView(R.layout.activity_passing_test);

        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

        TitleTest = (TextView) findViewById(R.id.TitleTest);
        TitleTest.setSelected(true);
        QuestionStatus = (TextView) findViewById(R.id.QuestionStatus);
        BtnBack = (ImageButton) findViewById(R.id.BtnBack);
        TestImage = (ImageView) findViewById(R.id.TestImage);
        QuestionTitle = (TextView) findViewById(R.id.QuestionTitle);
        PassingTestContainer = (LinearLayout) findViewById(R.id.PassingTestContainer);

        Intent intent = getIntent();
        String testData = intent.getStringExtra("data");
        try {
            JSONObject jsonTest = new JSONObject(testData);
            Picasso.get().load(jsonTest.getString("cover")).into(TestImage);
            TitleTest.setText(jsonTest.getString("title"));
            JSONArray array = jsonTest.getJSONArray("questions");
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                JSONArray answerArr = obj.getJSONArray("answers");
                String[] answerToObj = new String[answerArr.length()];
                for (int j = 0; j < answerArr.length(); j++) {
                    answerToObj[j] = answerArr.getString(j);
                }
                QuestionObj questionObj = new QuestionObj(obj.getString("question"),
                        obj.getInt("true_reply"), answerToObj);
                questions.add(questionObj);
            }
            setQuestion();
        } catch (Exception e) {
            e.printStackTrace();
        }
        animClick = AnimationUtils.loadAnimation(this, R.anim.btn_click1);
        BtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animClick);
                onBackPressed();
            }
        });
    }
    private void setQuestion(){
        QuestionPosition++;
        PassingTestContainer.removeAllViews();
        if(QuestionPosition == questions.size()){
            FinishTest();
            return;
        }
        QuestionObj itemQuestion = questions.get(QuestionPosition);
        QuestionTitle.setText(itemQuestion.getQuestion());
        QuestionStatus.setText(QuestionPosition+1+" "+getString(R.string.from)+" "+questions.size());
        int TrueReply = itemQuestion.getTrue_reply();
        String[] answers =  itemQuestion.getAnswers();

        for (int i = 0; i < answers.length; i++){
            View itemView = LayoutInflater.from(PassingTestActivity.this).inflate(R.layout.theme_btn_widget, null, false);
            TextView answersTitle = (TextView) itemView.findViewById(R.id.TitleTheme);
            answersTitle.setText(answers[i]);

            int finalI = i;
            View.OnClickListener answersClick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(finalI == TrueReply){
                        CountTrueReply++;
                    }
                    answersSets.add(QuestionPosition, finalI);
                    setQuestion();
                }
            };
            itemView.setOnClickListener(answersClick);
            PassingTestContainer.addView(itemView);
        }
    }
    private void FinishTest(){
        IsPassing = false;
        QuestionStatus.setVisibility(View.GONE);
        float percentage = (CountTrueReply * 100 / QuestionPosition);
        if(percentage <= 40){
            TestImage.setImageResource(R.drawable.badly_icon);
            QuestionTitle.setText(R.string.result_badly);
        }else if(percentage >= 41 && percentage <= 60){
            TestImage.setImageResource(R.drawable.normal_icon);
            QuestionTitle.setText(R.string.result_normal);
        }else if(percentage >= 61 && percentage <= 99){
            TestImage.setImageResource(R.drawable.not_bad_icon);
            QuestionTitle.setText(R.string.result_no_bad);
        }else if(percentage == 100){
            TestImage.setImageResource(R.drawable.perfectly_icon);
            QuestionTitle.setText(R.string.result_perfect);
        }
        View itemView = LayoutInflater.from(PassingTestActivity.this).inflate(R.layout.white_btn_widget, null, false);
        TextView textContains = (TextView) itemView.findViewById(R.id.textContains);
        String resultData = getString(R.string.total_percentage)+percentage+"%\n\n";
        resultData += getString(R.string.the_number_of_correct_answers)+CountTrueReply+"\n\n";
        resultData += getString(R.string.number_of_questions)+QuestionPosition;
        textContains.setText(resultData);
        PassingTestContainer.addView(itemView);

        View buttonEnd = LayoutInflater.from(PassingTestActivity.this).inflate(R.layout.theme_btn_widget, null, false);
        TextView buttonEndTitle = (TextView) buttonEnd.findViewById(R.id.TitleTheme);
        buttonEndTitle.setText(R.string.end_test);
        buttonEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        PassingTestContainer.addView(buttonEnd);
        pushRes();
        viewAnswersResult();
    }
    private void pushRes() {
        if(user_login.isEmpty()){
            return;
        }
        String res = "";
        switch(QuestionTitle.getText().toString()){
            case "Трішки погано, але не розчаровуйся":
                res = "badly_res";
                break;
            case "Нормально, не поганий результат!":
                res = "normal_res";
                break;
            case "Хороший результат, так тримати!":
                res = "not_bad_res";
                break;
            case "Perfect!!! Ви пройшли тест ідеально!":
                res = "perfectly_res";
                break;
        }
        Call<Boolean> call = AppAPI.create().pushUserStatistics(Consts.API_KEY,user_login,QuestionPosition,CountTrueReply,res);
        call.enqueue(new Callback<Boolean>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                System.out.println(response.body().toString());
            }
            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                System.out.println(t);
            }
        });
    }
    private void viewAnswersResult(){
        for(int i = 0; i < questions.size(); i++){
            View itemView = LayoutInflater.from(PassingTestActivity.this).inflate(R.layout.white_btn_widget, null, false);
            TextView textContains = (TextView) itemView.findViewById(R.id.textContains);
            textContains.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            LinearLayout Container = (LinearLayout) itemView.findViewById(R.id.Container);
            QuestionObj itemQuestion = questions.get(i);
            textContains.setText(itemQuestion.getQuestion());
            String[] answers =  itemQuestion.getAnswers();
            for (int j = 0; j < answers.length; j++){
                View answersView = LayoutInflater.from(PassingTestActivity.this).inflate(R.layout.theme_btn_widget, null, false);
                TextView answersTitle = (TextView) answersView.findViewById(R.id.TitleTheme);
                answersTitle.setText(answers[j]);
                ConstraintLayout BtnTheme = (ConstraintLayout) answersView.findViewById(R.id.BtnTheme);
                if(j == answersSets.get(i)){
                    BtnTheme.setBackgroundResource(R.drawable.red_radius);
                }
                if(j == itemQuestion.getTrue_reply()){
                    BtnTheme.setBackgroundResource(R.drawable.green_radius);
                }
                Container.addView(answersView);
            }
            PassingTestContainer.addView(itemView);
        }
    }
    private void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            assert account != null;
            user_login = account.getEmail();
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
    public void onBackPressed() {
        if(IsPassing){
            AlertDialog.Builder dialogExit = new AlertDialog.Builder(PassingTestActivity.this);
            dialogExit.setIcon(R.drawable.logo);
            dialogExit.setTitle(getString(R.string.close_test));
            dialogExit.setMessage(getString(R.string.exit_test));
            dialogExit.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    IsPassing = false;
                    onBackPressed();
                }
            });
            dialogExit.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog newDialog2 = dialogExit.create();
            newDialog2.show();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }
}