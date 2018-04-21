package com.example.android.movieslist;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MovieListAdapter extends ArrayAdapter<JsonObject> {

    private ArrayList<JsonObject> jsonObjects;


    public MovieListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<JsonObject> jsonObjects) {
        super(context, resource, jsonObjects);
        this.jsonObjects= jsonObjects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createCustomView(position, parent);
    }

    private View createCustomView(int position, ViewGroup parent) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view= inflater.inflate(R.layout.movie_adapter_row, parent, false);
        JsonObject jsonObject=jsonObjects.get(position);
        String title=jsonObject.get("title").toString();
        title=removeQuotes(title);
        Float rating=Float.parseFloat(jsonObject.get("rating").toString());
        Integer releaseYear= Integer.parseInt(jsonObject.get("releaseYear").toString());
        String genres= jsonObject.get("genre").toString();
        String genresFinal="";
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(genres);
        int counter=0;
        while (m.find()) {
            counter+=1;
            genresFinal=genresFinal+(m.group(1))+ ", ";
        }
        genresFinal= genresFinal.substring(0, genresFinal.length()-2);
        genresFinal=genresFinal+".";
        //viewholder pattern in lv
        TextView movieTitleTextView=(TextView) view.findViewById(R.id.movieTitleID);
        TextView movieRatingTextView=(TextView) view.findViewById(R.id.movieRatingID);
        TextView releaseYearTextView=(TextView) view.findViewById(R.id.movieReleaseYearID);
        TextView genresTextView=(TextView) view.findViewById(R.id.movieGenreID);

        movieTitleTextView.setText(title);
        movieRatingTextView.setText("Rating: "+rating.toString());
        releaseYearTextView.setText("Release Year: "+releaseYear.toString());
        genresTextView.setText("Genre: "+genresFinal);

        String imageURLString= jsonObject.get("image").toString();
        loadImage(imageURLString, position, view);

        return view;
    }

    private void loadImage(String imageURLString, int position, View view) {
        imageURLString = removeQuotes(imageURLString);
        final ImageView imageView= (ImageView) view.findViewById(R.id.logoID);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getContext()).build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
        com.nostra13.universalimageloader.core.ImageLoader imageLoader= com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.loadImage(imageURLString, new SimpleImageLoadingListener(){

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                imageView.setImageBitmap(loadedImage);
            }
        });
    }

    private String removeQuotes(String string) {
        string= string.substring(1, string.length()-1);
        return string;
    }
}
