package com.example.android.movieslist;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


public class MainActivity extends AppCompatActivity{
    private static RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=(RecyclerView) findViewById(R.id.moviesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        GetJSONData getJSONData= new GetJSONData(context, recyclerView);
        getJSONData.execute();
    }
}