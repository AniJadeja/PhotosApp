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
        preTime = preTime/1000;
        double hourX = preTime / 3600;
        double minX  = (hourX%1) * 60;
        double secX = (minX%1) * 60;

        String sec = String.valueOf((int)secX);
        String min = String.valueOf((int)minX);
        String hour = String.valueOf((int) hourX);


        setTime(sec, min, hour, holder);

        activity.runOnUiThread(() -> {
            GlideApp.with(context)
                    .load(arrayList.get(position).getUri())
                    .apply(RequestOptions.overrideOf(180, 180))
                    .apply(RequestOptions.centerCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(holder.video);
            Log.d("FetchVideos(): ", " Glide Called");
        });
    }

    @Override
    public int getItemCount() {
        view = activity.findViewById(R.id.count);
        view.setText(arrayList.size()+" Videos");
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


    private void setTime(String sec, String min, String hour, MyViewHolder holder) {
        String second, minute;
        String[] secs = sec.split("");


        if (secs.length == 1)
            second = "0 "+secs[0];
        else second = secs[0] + " " + secs[1];

        String[] mins = min.split("");
        if (mins.length == 1)
            minute = "0 "+mins[0];
        else minute = mins[0] + " " + mins[1];

        if (!sec.equals("0") && min.equals("0") && hour.equals("0")) {
            setSec(second, holder);
        }

        else if (!sec.equals("0") && !min.equals("0") && hour.equals("0")) {
            setSecMin(second,minute,holder);
        }
        else
            holder.time.setText(hour+"  :  "+minute+"  :  "+second);

    }

    private void setSec(String sec, MyViewHolder holder) {
        holder.time.setText("0 0 : "+sec);
    }

    private void setSecMin(String sec, String min, MyViewHolder holder) {
        holder.time.setText(min+"  :  "+sec);
    }




}
