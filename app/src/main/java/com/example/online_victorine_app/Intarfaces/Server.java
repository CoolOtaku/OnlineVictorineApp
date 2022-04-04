package com.example.online_victorine_app.Intarfaces;

import com.example.online_victorine_app.Obj.TestObj;
import com.example.online_victorine_app.Obj.TestsThemesObj;
import com.example.online_victorine_app.Obj.UserStatistic;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Server {
    @FormUrlEncoded
    @POST("getTests")
    Call<TestsThemesObj> getThemesListData (@Field("api_key") String api_key, @Field("type") String type);
    @FormUrlEncoded
    @POST("getTests")
    Call<List<TestObj>> getTestsOfThemeData (@Field("api_key") String api_key, @Field("type") String type, @Field("theme") String theme);
    @FormUrlEncoded
    @POST("pushUserStatistics")
    Call<Boolean> pushUserStatistics (@Field("api_key") String api_key, @Field("user_login") String user_login, @Field("question_count") int question_count, @Field("question_true") int question_true, @Field("res") String res);
    @FormUrlEncoded
    @POST("getUserStatistics")
    Call<UserStatistic> getUserStatistics (@Field("api_key") String api_key, @Field("user_login") String user_login);
}
