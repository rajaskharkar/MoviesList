package com.example.android.movieslist;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetJSONData extends AsyncTask<Void, Void, Void> {

    private Context mContext;
    private RecyclerView recyclerView;
    private String jsonData="";

    public GetJSONData (Context context, RecyclerView recyclerView){
        mContext = context;
        this.recyclerView=recyclerView;
    }

    @Override
    protected Void doInBackground(Void... voids){
        try {
            jsonData=getJsonObjectFromURL(); //gets JSON data as a String from URL
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        display(jsonData); //parses JSON data and passes it to the recycler view's adapter to be displayed
    }

    private void display(String jsonData) {
        JsonParser parser= new JsonParser();
        JsonArray jsonArray= parser.parse(jsonData).getAsJsonArray();
        ArrayList<JsonObject> jsonObjectArrayList= new ArrayList<>();
        for(JsonElement jsonElement: jsonArray){
            JsonObject jsonObject=jsonElement.getAsJsonObject();
            jsonObjectArrayList.add(jsonObject);
        }
        MovieListRecyclerViewAdapter movieListRecyclerViewAdapter= new MovieListRecyclerViewAdapter(jsonObjectArrayList, mContext);
        recyclerView.setAdapter(movieListRecyclerViewAdapter);
    }

    public String getJsonObjectFromURL() throws IOException {
        URL url= new URL(mContext.getResources().getString(R.string.data_url));
        HttpURLConnection urlConnection=(HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod(mContext.getResources().getString(R.string.get_request));
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);
        urlConnection.connect();
        String data=getData(urlConnection);
        return data;
    }

    private String getData(HttpURLConnection urlConnection) throws IOException {
        String data="";
        InputStream inputStream= urlConnection.getInputStream();
        BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(inputStream));
        String line="sample";
        while(line!=null){
            line=bufferedReader.readLine();
            data=data+line;
        }
        data = data.substring(0, data.length() - 4);
        return data;
    }
}
