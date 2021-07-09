package com.example.collapsingtoolbar.Adapter;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
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

    /*===============================================================   CONSTRUCTOR   ===============================================================*/

    public VideosAdapter(Context context, ArrayList<VideoModel> arrayList, Activity activity, OnVideoClickListner listner) {
        this.context = context;
        this.arrayList = arrayList;
        this.activity = activity;
        this.listner = listner;
    }

    /*===============================================================   OVERRIDDEN METHODS   ===============================================================*/

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_video, parent, false);         //This methods returns single_video.xml as a view for RecyclerView.
        return new MyViewHolder(view, listner);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {          //Binding the uris with each view depending upon the position of current view.

                    //This part is responsible to display the time of the video correctly.

        double preTime = arrayList.get(position).getDuration();         //Get the raw time in long milliseconds
        preTime = preTime / 1000;           //This gives the usable duration
        double hourX = preTime / 3600;          //Converting duration to hours
        double minX = (hourX % 1) * 60;         //Converting hours to minutes
        double secX = (minX % 1) * 60;          //Converting minutes to seconds
        hourX = hourX % 1;          //Converting hours to hour. %1 eliminates all the digits after the points
                                    //eg. 1.56 to 1, 6.89 to 6

        int sec = (int) secX;           //Converting double seconds to int seconds
        int min = (int) minX;           //Converting double minutes to int minutes
        int hour = (int) hourX;         //Converting double hour to int hour


        setTime(sec, min, hour, holder);            //Calling method to set time and adjust timing format.



        activity.runOnUiThread(() -> GlideApp.with(context)
                .load(Uri.parse(arrayList.get(position).getUri()))         //Load uris into holder based on position.
                .apply(RequestOptions.overrideOf(180, 180))         //Creating a thumbnail by overriding  the size of original image.
                .apply(RequestOptions.centerCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)          //Caching the thumbnails
                .into(holder.video));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    /*===============================================================   INNER CLASS   ===============================================================*/

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView video;
        OnVideoClickListner listner;
        TextView time;

        public MyViewHolder(@NonNull View itemView, OnVideoClickListner listner) {
            super(itemView);
            this.listner = listner;
            itemView.setOnClickListener(this);          //onClickListener is set to all of the RecyclerView Items for once rather than setting on each item in BindViewHolder for repeated times
            video = itemView.findViewById(R.id.video);
            time = itemView.findViewById(R.id.timeView);

        }

        @Override
        public void onClick(View v) {
            listner.onClick(getAdapterPosition());          //Returning the current clicked position  to the implemented method.
        }
    }

    public interface OnVideoClickListner {          //Interface to generate call back when user clicked an image.
        void onClick(int position);
    }

                //This method sets time to holder in preserving the format

    private void setTime(int sec, int min, int hour, MyViewHolder holder) {
        String seco = String.valueOf(sec);
        String[] secs = seco.split("");         //Splits second digits into individual characters

        if (secs.length == 1)
            seco = "0 " + secs[0];          //If seconds is a single digit number then add a 0 before it.
        else
            seco = secs[0]+" "+secs[1];     //else join it together

        String mino = String.valueOf(min);
        String[] mins = mino.split("");

        if (mins.length == 1)
            mino = "0 " + mins[0];          //If minutes is a single digit number then add a 0 before it.
        else
            mino = mins[0]+" "+mins[1];     //else join it together

        if (sec != 0 && min == 0 && hour == 0) {
            setSec(seco, holder);           //If minutes and hour are 0 and seconds is not 0 set only seconds as the duration.
        }
        else if (sec != 0 && min != 0 && hour == 0) {
            setSecMin(seco, mino, holder);          //If hour is 0 and minutes and seconds are not 0 set seconds and minutes as the duration.
        }
        else if (sec == 0 && min != 0 && hour == 0) {
            setSecMin(seco, mino, holder);          //If hour and seconds are 0 and minutes is not 0 set seconds and minutes as the duration.
        } else
            holder.time.setText(hour + "  :  " + min + "  :  " + sec);          //else set the hour,minutes,seconds time format as the duration.

    }

    private void setSec(String sec, MyViewHolder holder) {
        holder.time.setText("0 0 : " + sec);            //If minutes and hours are 0, set the seconds with minutes as 00
    }

    private void setSecMin(String sec, String min, MyViewHolder holder) {
        holder.time.setText(min + "  :  " + sec);           //If minutes and seconds are not 0 set the seconds and minutes as the duration.
    }


}
