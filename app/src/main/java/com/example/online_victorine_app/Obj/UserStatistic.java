package com.example.online_victorine_app.Obj;

import com.google.gson.annotations.SerializedName;

public class UserStatistic {

    @SerializedName("question_count")
    private int question_count;
    @SerializedName("question_true")
    private int question_true;
    @SerializedName("test_count")
    private int test_count;
    @SerializedName("badly_res")
    private int badly_res;
    @SerializedName("normal_res")
    private int normal_res;
    @SerializedName("not_bad_res")
    private int not_bad_res;
    @SerializedName("perfectly_res")
    private int perfectly_res;

    public UserStatistic(int question_count, int question_true, int test_count, int badly_res, int normal_res, int not_bad_res, int perfectly_res) {
        this.question_count = question_count;
        this.question_true = question_true;
        this.test_count = test_count;
        this.badly_res = badly_res;
        this.normal_res = normal_res;
        this.not_bad_res = not_bad_res;
        this.perfectly_res = perfectly_res;
    }

    public int getQuestion_count() {
        return question_count;
    }

    public void setQuestion_count(int question_count) {
        this.question_count = question_count;
    }

    public int getQuestion_true() {
        return question_true;
    }

    public void setQuestion_true(int question_true) {
        this.question_true = question_true;
    }

    public int getTest_count() {
        return test_count;
    }

    public void setTest_count(int test_count) {
        this.test_count = test_count;
    }

    public int getBadly_res() {
        return badly_res;
    }

    public void setBadly_res(int badly_res) {
        this.badly_res = badly_res;
    }

    public int getNormal_res() {
        return normal_res;
    }

    public void setNormal_res(int normal_res) {
        this.normal_res = normal_res;
    }

    public int getNot_bad_res() {
        return not_bad_res;
    }

    public void setNot_bad_res(int not_bad_res) {
        this.not_bad_res = not_bad_res;
    }

    public int getPerfectly_res() {
        return perfectly_res;
    }

    public void setPerfectly_res(int perfectly_res) {
        this.perfectly_res = perfectly_res;
    }
}
