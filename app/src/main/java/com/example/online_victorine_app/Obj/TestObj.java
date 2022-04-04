package com.example.online_victorine_app.Obj;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TestObj {

    @SerializedName("title")
    private String title;
    @SerializedName("cover")
    private String cover;
    @SerializedName("questions")
    private List<QuestionObj> questions;

    public TestObj(String title, String cover, List<QuestionObj> questions) {
        this.title = title;
        this.cover = cover;
        this.questions = questions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public List<QuestionObj> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionObj> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        String json = new Gson().toJson(questions);
        return "{\n"+
                "\"title\":\"" + title + "\",\n" +
                "\"cover\":\"" + cover + "\",\n" +
                "\"questions\":\n" + json +"\n"+
                "}";
    }
}
