package com.example.android.movieslist;

import android.content.Context;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetJSONData extends AsyncTask<Void, Void, Void> {

    private Context mContext;
    String jsonData="";

    public GetJSONData (Context context){
        mContext = context;
    }

    @Override
    protected Void doInBackground(Void... voids){
        try {
            jsonData=getJsonObjectFromURL();
        } catch (IOException e) {
           // Toast.makeText(mContext, "catch failed", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Toast.makeText(mContext, jsonData, Toast.LENGTH_SHORT).show();
        display(jsonData);
    }

    private void display(String jsonData) {
        Gson gson = new GsonBuilder().setLenient().create();
        JsonParser parser= new JsonParser();
        JsonArray jsonArray= parser.parse(jsonData).getAsJsonArray();
        ArrayList<JsonObject> jsonObjectArrayList= new ArrayList<>();
        for(JsonElement jsonElement: jsonArray){
            JsonObject jsonObject=jsonElement.getAsJsonObject();
            jsonObjectArrayList.add(jsonObject);
        }

        MovieListRecyclerViewAdapter movieListRecyclerViewAdapter= new MovieListRecyclerViewAdapter(jsonObjectArrayList, mContext);
        MainActivity.recyclerView.setAdapter(movieListRecyclerViewAdapter);
        //movie adapter
//        final FriendsListAdapter friendsListAdapter = new FriendsListAdapter(this, R.layout.friends_list_row, currentUserFriendsAL);
//        friendsLV.setAdapter(friendsListAdapter);
    }

    public String getJsonObjectFromURL() throws IOException {
        String data="";
        URL url= new URL("https://api.androidhive.info/json/movies.json");
        HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
//        urlConnection.setReadTimeout(10000);
//        urlConnection.setConnectTimeout(15000);
//        urlConnection.setDoOutput(true);
//        urlConnection.connect();
        InputStream inputStream= urlConnection.getInputStream();
        BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
        String line="rajas";
        while(line!=null){
            line=bufferedReader.readLine();
            data=data+line;
        }
        data = data.substring(0, data.length() - 4);
        return data;
    }
}
