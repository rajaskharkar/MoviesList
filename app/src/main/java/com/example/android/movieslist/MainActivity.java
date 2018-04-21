package com.example.android.movieslist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity{
    public static ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String url="https://api.androidhive.info/json/movies.json";
        listView=(ListView) findViewById(R.id.moviesListView);
        GetJSONData getJSONData= new GetJSONData(context);
        getJSONData.execute();
    }
}