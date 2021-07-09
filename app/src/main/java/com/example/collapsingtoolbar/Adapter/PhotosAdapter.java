package com.example.collapsingtoolbar.Adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

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

    /*===============================================================   CONSTRUCTOR   ===============================================================*/

    public PhotosAdapter(Context context, ArrayList<ImageModel> arrayList, Activity activity, OnImageClickListner listner, OnImageLongClickListener longClickListener) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
        this.listner = listner;
        this.longClickListener = longClickListener;

    }

    /*===============================================================   OVERRIDDEN METHODS   ===============================================================*/

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {           //This methods returns single_view.xml as a view for RecyclerView.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view, parent, false);
        return new MyViewHolder(view,listner,longClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {          //Binding the uris with each view depending upon the position of current view.

        activity.runOnUiThread(() -> GlideApp.with(context)
                .load(Uri.parse(arrayList.get(position).getUri()))
                //.load(arrayList.get(position).getBitmap())
                .apply(RequestOptions.overrideOf(150,150))          //It overrides the value of original image and reduces it to the visible thumbnail size.
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)          //Then it caches the reduced size thumbnail for faster loading speed.
                .into(holder.img));


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    /*===============================================================   INNER VIEW HOLDER CLASS   ===============================================================*/

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener {


        private final ImageView img;
        public CheckBox selection;
        OnImageClickListner listner;
        OnImageLongClickListener longClickListener;
        public MyViewHolder(@NonNull View itemView, OnImageClickListner listner,OnImageLongClickListener longClickListener) {
            super(itemView);
            this.listner = listner;
            this.longClickListener = longClickListener;
            itemView.setOnLongClickListener(this);          //onLongClickListener is set to all of the RecyclerView Items for once rather than setting on each item in BindViewHolder for repeated times
            itemView.setOnClickListener(this);          //onClickListener is set to all of the RecyclerView Items for once rather than setting on each item in BindViewHolder for repeated times
            img = itemView.findViewById(R.id.img);
            selection = itemView.findViewById(R.id.checkbox);

        }

        @Override
        public void onClick(View v) {
            listner.onclick(getAdapterPosition(),selection);            //Returning the current clicked position and selection checkbox to the implemented method.
        }

        @Override
        public boolean onLongClick(View v) {
            longClickListener.onLongClick(getAdapterPosition(),v);          //Returning the current clicked position and view to the implemented method.
            return  true;
        }
    }

    public  interface  OnImageClickListner{         //Interface to generate call back when user clicked an image.
         void onclick(int position,CheckBox selection);
    }

    public interface OnImageLongClickListener{          //Interface to generate call back when user long clicked an image.
        void onLongClick(int position,View v);
    }


}
