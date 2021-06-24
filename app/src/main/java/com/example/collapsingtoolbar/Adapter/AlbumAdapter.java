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

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.collapsingtoolbar.Activities.MainActivity;
import com.example.collapsingtoolbar.Model.ImageModel;
import com.example.collapsingtoolbar.R;
import com.example.collapsingtoolbar.utils.GlideApp;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyViewHolder> {


    Context context;
    ArrayList<ImageModel> arrayList;
    Activity activity;
    OnImageClickListner listner;
    MainActivity mactivity;
    NumberCallback callback;
    TextView view;
    public AlbumAdapter ()
    {}

    public AlbumAdapter(Context context, ArrayList<ImageModel> arrayList, Activity activity, OnImageClickListner listner) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
        this.listner = listner;
        mactivity = new MainActivity();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view, parent, false);
        return new MyViewHolder(view,listner);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        activity.runOnUiThread(() -> {
            GlideApp.with(context)
                    .load(arrayList.get(position).getUri())
                    .apply(RequestOptions.overrideOf(180,180))
                    .apply(RequestOptions.centerCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(holder.img);
            Log.d("FetchImages(): "," Glide Called");
        });


    }

    @Override
    public int getItemCount() {
        view = activity.findViewById(R.id.count);
        view.setText(arrayList.size()+" Photos");
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView img;
        OnImageClickListner listner;
        public MyViewHolder(@NonNull View itemView, OnImageClickListner listner) {
            super(itemView);
            this.listner = listner;
            itemView.setOnClickListener(this);
            img = itemView.findViewById(R.id.img);

        }

        @Override
        public void onClick(View v) {
            listner.onclick(getAdapterPosition());
        }
    }

    public  interface  OnImageClickListner{
        void onclick(int position);
    }

    public void setCallback(NumberCallback callback){this.callback = callback;}

    public interface NumberCallback
    {
        void number(int num);
    }


}

