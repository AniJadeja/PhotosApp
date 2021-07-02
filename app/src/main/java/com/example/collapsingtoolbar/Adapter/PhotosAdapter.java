package com.example.collapsingtoolbar.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.collapsingtoolbar.Model.ImageModel;
import com.example.collapsingtoolbar.R;
import com.example.collapsingtoolbar.utils.GlideApp;

import java.util.ArrayList;

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.MyViewHolder> {


    Context context;
    ArrayList<ImageModel> arrayList;
    Activity activity;
    OnImageClickListner listner;
    OnImageLongClickListener longClickListener;

    public PhotosAdapter(Context context, ArrayList<ImageModel> arrayList, Activity activity, OnImageClickListner listner, OnImageLongClickListener longClickListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
        this.listner = listner;
        this.longClickListener = longClickListener;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view, parent, false);
        return new MyViewHolder(view,listner,longClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        activity.runOnUiThread(() -> GlideApp.with(context)
                .load(arrayList.get(position).getUri())
                .apply(RequestOptions.overrideOf(180,180))
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(holder.img));


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {

        ImageView img;
        OnImageClickListner listner;
        OnImageLongClickListener longClickListener;
        public MyViewHolder(@NonNull View itemView, OnImageClickListner listner,OnImageLongClickListener longClickListener) {
            super(itemView);
            this.listner = listner;
            this.longClickListener = longClickListener;
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
            img = itemView.findViewById(R.id.img);

        }

        @Override
        public void onClick(View v) {
            listner.onclick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            longClickListener.onLongClick(getAdapterPosition(),v);
            return  true;
        }
    }

    public  interface  OnImageClickListner{
         void onclick(int position);
    }

    public interface OnImageLongClickListener{
        void onLongClick(int position,View v);
    }


}
