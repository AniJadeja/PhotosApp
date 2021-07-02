package com.example.collapsingtoolbar.Adapter;

import android.app.Activity;
import android.content.Context;
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
import com.example.collapsingtoolbar.Model.VideoModel;
import com.example.collapsingtoolbar.R;
import com.example.collapsingtoolbar.utils.GlideApp;

import java.util.ArrayList;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.MyViewHolder> {


    Context context;
    ArrayList<VideoModel> arrayList;
    Activity activity;
    OnVideoClickListner listner;
    TextView view;

    public VideosAdapter(Context context, ArrayList<VideoModel> arrayList, Activity activity, OnVideoClickListner listner) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
        this.listner = listner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_video, parent, false);
        return new MyViewHolder(view, listner);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        double preTime = arrayList.get(position).getDuration();
        preTime = preTime / 1000;
        double hourX = preTime / 3600;
        double minX = (hourX % 1) * 60;
        double secX = (minX % 1) * 60;
        hourX = hourX % 1;

        int sec = (int) secX;
        int min = (int) minX;
        int hour = (int) hourX;


        setTime(sec, min, hour, holder);

        Log.d("URI PATH ", "onBindViewHolder: "+arrayList.get(position).getUri().getPath());

        activity.runOnUiThread(() -> GlideApp.with(context)
                .load(arrayList.get(position).getUri())
                .apply(RequestOptions.overrideOf(180, 180))
                .apply(RequestOptions.centerCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(holder.video));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView video;
        OnVideoClickListner listner;
        TextView time;

        public MyViewHolder(@NonNull View itemView, OnVideoClickListner listner) {
            super(itemView);
            this.listner = listner;
            itemView.setOnClickListener(this);
            video = itemView.findViewById(R.id.video);
            time = itemView.findViewById(R.id.timeView);

        }

        @Override
        public void onClick(View v) {
            listner.onClick(getAdapterPosition());
        }
    }

    public interface OnVideoClickListner {
        void onClick(int position);
    }


    private void setTime(int sec, int min, int hour, MyViewHolder holder) {


        if (sec != 0 && min == 0 && hour == 0) {
            setSec(sec, holder);
        } else if (sec != 0 && min != 0 && hour == 0) {
            setSecMin(String.valueOf(sec), String.valueOf(min), holder);
        } else if (sec == 0 && min != 0 && hour == 0) {
            String seco = String.valueOf(sec);
            String[] secs = seco.split("");

            if (secs.length == 1)
                seco = "0 " + secs[0];
            else
                seco = secs[0]+" "+secs[1];




            String mino = String.valueOf(min);
            String[] mins = mino.split("");

            if (mins.length == 1)
                mino = "0 " + mins[0];
            else
                mino = mins[0]+" "+mins[1];


                setSecMin(seco, mino, holder);
        } else
            holder.time.setText(hour + "  :  " + min + "  :  " + sec);

    }

    private void setSec(int sec, MyViewHolder holder) {
        holder.time.setText("0 0 : " + sec);
    }

    private void setSecMin(String sec, String min, MyViewHolder holder) {
        holder.time.setText(min + "  :  " + sec);
    }


}
