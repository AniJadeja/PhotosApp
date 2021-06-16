package com.example.collapsingtoolbar.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.collapsingtoolbar.Model.VideoModel;
import com.example.collapsingtoolbar.R;
import com.example.collapsingtoolbar.utils.GlideApp;

import java.util.ArrayList;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.MyViewHolder> {


    Context context;
    ArrayList<VideoModel> arrayList;
    Activity activity;

    public VideosAdapter(Context context, ArrayList<VideoModel> arrayList, Activity activity) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_video, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        GlideApp.with(context)
                //.load("file://" + arrayList.get(position).getUri())
                .load(arrayList.get(position).getUri())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(holder.video);
        Log.d("FetchVideos(): "," Glide Called");
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView video;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            video = itemView.findViewById(R.id.video);
        }
    }
}
