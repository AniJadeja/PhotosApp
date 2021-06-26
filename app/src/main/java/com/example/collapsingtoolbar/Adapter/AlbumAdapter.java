package com.example.collapsingtoolbar.Adapter;


import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.collapsingtoolbar.Activities.MainActivity;
import com.example.collapsingtoolbar.Model.AlbumModel;
import com.example.collapsingtoolbar.Model.ImageModel;
import com.example.collapsingtoolbar.R;
import com.example.collapsingtoolbar.utils.GlideApp;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder> {


    Context context;
    ArrayList<String> arrayList;
    ArrayList<AlbumModel> arrayListX;
    Activity activity;
    OnImageClickListner listner;

    public AlbumAdapter (ArrayList<String> arrayList,Activity activity)
    {
        this.arrayList = arrayList;
        this.activity  = activity;
    }

    public AlbumAdapter (Context context,ArrayList<AlbumModel> arrayListX, Activity activity)
    {
        this.context = context;
        this.arrayListX = arrayListX;
        this.activity  = activity;
    }

    public AlbumAdapter(Context context, ArrayList<String> arrayList, Activity activity, OnImageClickListner listner) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
        this.listner = listner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_album, parent, false);
        return new MyViewHolder(view,listner);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.duration.setText(arrayList.get(position));
       /* Glide.with(context)
                .load(arrayListX.get(position).getThumbURI())
                .into(holder.img);*/
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView img;
        OnImageClickListner listner;
        TextView duration;
        public MyViewHolder(@NonNull View itemView, OnImageClickListner listner) {
            super(itemView);
            this.listner = listner;
            itemView.setOnClickListener(this);
            img = itemView.findViewById(R.id.AlbumThumbnail);
            duration = itemView.findViewById(R.id.AlbumName);
        }

        @Override
        public void onClick(View v) {
            listner.onclick(getAdapterPosition());
        }
    }

    public  interface  OnImageClickListner{
        void onclick(int position);
    }



}

