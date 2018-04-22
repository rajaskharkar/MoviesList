package com.example.android.movieslist;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MovieListRecyclerViewAdapter extends RecyclerView.Adapter<MovieListRecyclerViewAdapter.ViewHolder>{

    private ArrayList<JsonObject> jsonObjects;
    private Context context;

    public MovieListRecyclerViewAdapter(ArrayList<JsonObject> jsonObjects, Context context) {
        this.jsonObjects=jsonObjects;
        this.context=context;
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);
    }

    @Override
    public MovieListRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_adapter_row, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieListRecyclerViewAdapter.ViewHolder holder, int position) {
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
        holder.movieTitleTextView.setText(title);
        holder.movieRatingTextView.setText("Rating: "+rating.toString());
        holder.releaseYearTextView.setText("Release Year: "+releaseYear.toString());
        holder.genresTextView.setText("Genre: "+genresFinal);

        String imageURLString= jsonObject.get("image").toString();
        loadImage(imageURLString, position, holder);
    }

    private String removeQuotes(String string) {
        string= string.substring(1, string.length()-1);
        return string;
    }

    @Override
    public int getItemCount() {
        return jsonObjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView movieTitleTextView;
        public TextView movieRatingTextView;
        public TextView releaseYearTextView;
        public TextView genresTextView;
        public ImageView imageView;

        public ViewHolder(View view) {
            super(view);

            //viewholder pattern in lv
            movieTitleTextView=(TextView) view.findViewById(R.id.movieTitleID);
            movieRatingTextView=(TextView) view.findViewById(R.id.movieRatingID);
            releaseYearTextView=(TextView) view.findViewById(R.id.movieReleaseYearID);
            genresTextView=(TextView) view.findViewById(R.id.movieGenreID);
            imageView= (ImageView) view.findViewById(R.id.logoID);
        }
    }
    private void loadImage(String imageURLString, int position, final MovieListRecyclerViewAdapter.ViewHolder holder) {
        imageURLString = removeQuotes(imageURLString);
        com.nostra13.universalimageloader.core.ImageLoader imageLoader= com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.loadImage(imageURLString, new SimpleImageLoadingListener(){

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                super.onLoadingComplete(imageUri, view, loadedImage);
                holder.imageView.setImageBitmap(loadedImage);
            }
        });
    }
}
