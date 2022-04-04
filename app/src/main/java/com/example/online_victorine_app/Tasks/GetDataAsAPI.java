package com.example.online_victorine_app.Tasks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

public class GetDataAsAPI extends AsyncTask<String, String, String> {

    @SuppressLint("StaticFieldLeak")
    private Context context;

    public GetDataAsAPI(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {

        return null;
    }
    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }

    public String getJSON(String login, String pass) // получаем json объект в виде строки
    {
        JSONObject bot = new JSONObject();
        try {
            bot.put("Login", login);
            bot.put("Password", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bot.toString();
    }

}
