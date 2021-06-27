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
    ArrayList<AlbumModel> arrayListX;
    Activity activity;
    OnAlbumClickListner listner;


    public AlbumAdapter (Context context,ArrayList<AlbumModel> arrayListX, Activity activity)
    {
        this.context = context;
        this.arrayListX = arrayListX;
        this.activity  = activity;
    }

    public AlbumAdapter(Context context, ArrayList<AlbumModel> arrayListX, Activity activity, OnAlbumClickListner listner) {
        this.context = context;
        this.arrayListX = arrayListX;
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
        holder.duration.setText(setName(arrayListX.get(position).getAlbumName()));
        Glide.with(context)
                .load(arrayListX.get(position).getThumbURI())
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return arrayListX.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView img;
        OnAlbumClickListner listner;
        TextView duration;
        public MyViewHolder(@NonNull View itemView, OnAlbumClickListner listner) {
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

    public  interface  OnAlbumClickListner{
        void onclick(int position);
    }

    private String setName(String AlbumName)
    {
        String AlBack= AlbumName;
        boolean flag = false;

        try {
            AlbumName = AlbumName.replace("WhatsApp", "WA")
                    .replace("Documents", "Docs")
                    .replace("Screenshots", "SS")
                    .replace("_", " ")
                    .replace("-", " ");
            flag = true;
        } catch (Exception e) {
            Log.d("StringReplace", "onBindViewHolder: " + e.toString());
        }


        if (AlbumName.length() > 10) {
            AlbumName = AlbumName.substring(0, 9);
            AlbumName = AlbumName.concat(" ..");
        }

        if (!flag)
            AlbumName = AlBack;

        return AlbumName;
    }

}

