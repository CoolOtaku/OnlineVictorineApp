package com.example.online_victorine_app.Obj;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class TestsThemesObj implements Serializable {

    @SerializedName("themes")
    private List<String> themes;

    public TestsThemesObj(List<String> themes) {
        this.themes = themes;
    }

    public List<String> getThemes() {
        return themes;
    }

    public void setThemes(List<String> themes) {
        this.themes = themes;
    }
}
