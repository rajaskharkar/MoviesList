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
        initializeImageLoader();
    }

    private void initializeImageLoader() {
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
        String title=jsonObject.get(context.getString(R.string.title_key)).toString();
        Float rating=Float.parseFloat(jsonObject.get(context.getResources().getString(R.string.rating_key)).toString());
        Integer releaseYear= Integer.parseInt(jsonObject.get(context.getResources().getString(R.string.release_year_key)).toString());
        String genresFromJson= jsonObject.get(context.getResources().getString(R.string.genre_key)).toString();
        String imageURLString= jsonObject.get(context.getResources().getString(R.string.image_key)).toString();

        title=removeQuotes(title);
        String genres= cleanGenresFromJson(genresFromJson);

        holder.movieTitleTextView.setText(title);
        holder.movieRatingTextView.setText(context.getResources().getString(R.string.rating_movie_details)+rating.toString());
        holder.releaseYearTextView.setText(context.getResources().getString(R.string.release_year_movie_details)+releaseYear.toString());
        holder.genresTextView.setText(context.getResources().getString(R.string.genre_movie_details)+genres);

        loadImage(imageURLString, holder);
    }

    private String cleanGenresFromJson(String genresFromJson) {
        String genresFinal="";
        Pattern p = Pattern.compile("\"([^\"]*)\"");
        Matcher m = p.matcher(genresFromJson);
        while (m.find()) {
            genresFinal=genresFinal+(m.group(1))+ ", ";
        }
        genresFinal= genresFinal.substring(0, genresFinal.length()-2);
        genresFinal=genresFinal+".";
        return genresFinal;
    }

    private String removeQuotes(String string) {
        string= string.substring(1, string.length()-1);
        return string;
    }

    private void loadImage(String imageURLString, final MovieListRecyclerViewAdapter.ViewHolder holder) {
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

            movieTitleTextView= view.findViewById(R.id.movieTitleID);
            movieRatingTextView= view.findViewById(R.id.movieRatingID);
            releaseYearTextView= view.findViewById(R.id.movieReleaseYearID);
            genresTextView= view.findViewById(R.id.movieGenreID);
            imageView= view.findViewById(R.id.logoID);
        }
    }
}