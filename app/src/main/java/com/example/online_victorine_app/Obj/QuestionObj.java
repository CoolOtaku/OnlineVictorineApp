package com.example.online_victorine_app.Obj;

import com.google.gson.annotations.SerializedName;

public class QuestionObj {

    @SerializedName("question")
    private String question;
    @SerializedName("true_reply")
    private int true_reply;
    @SerializedName("answers")
    private String[] answers;

    public QuestionObj(String question, int true_reply, String[] answers) {
        this.question = question;
        this.true_reply = true_reply;
        this.answers = answers;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getTrue_reply() {
        return true_reply;
    }

    public void setTrue_reply(int true_reply) {
        this.true_reply = true_reply;
    }

    public String[] getAnswers() {
        return answers;
    }

    public void setAnswers(String[] answers) {
        this.answers = answers;
    }

}
