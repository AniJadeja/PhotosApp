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

import com.example.collapsingtoolbar.Model.AlbumModel;
import com.example.collapsingtoolbar.R;
import com.example.collapsingtoolbar.utils.GlideApp;

import java.util.ArrayList;

import static com.example.collapsingtoolbar.Fragments.AlbumsFragment.VIDEO;

public class VideoAlbumAdapter extends RecyclerView.Adapter<VideoAlbumAdapter.MyViewHolder> {


    Context context;
    ArrayList<AlbumModel> arrayListX;
    Activity activity;
    OnAlbumClickListner listner;

    /*===============================================================   CONSTRUCTOR   ===============================================================*/

    public VideoAlbumAdapter(Context context, ArrayList<AlbumModel> arrayListX, Activity activity, OnAlbumClickListner listner) {
        this.context = context;
        this.arrayListX = arrayListX;
        this.activity = activity;
        this.listner = listner;
    }

    /*===============================================================   OVERRIDDEN METHODS   ===============================================================*/

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_album, parent, false);         //This methods returns single_album.xml as a view for RecyclerView.
        return new MyViewHolder(view,listner);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {          //Binding the uris with each view depending upon the position of current view.
        holder.duration.setText(setName(arrayListX.get(position).getAlbumName()));
        GlideApp.with(context)
                .load(Uri.parse(arrayListX.get(position).getThumbURI()))            //Uri is stored in string so, It needs to be parsed into Uri again.
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return arrayListX.size();
    }


    /*===============================================================   INNER CLASS   ===============================================================*/

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView img;
        OnAlbumClickListner listner;
        TextView duration;
        public MyViewHolder(@NonNull View itemView, OnAlbumClickListner listner) {
            super(itemView);
            this.listner = listner;
            itemView.setOnClickListener(this);          //onClickListener is set to all of the RecyclerView Items for once rather than setting on each item in BindViewHolder for repeated times
            img = itemView.findViewById(R.id.AlbumThumbnail);
            duration = itemView.findViewById(R.id.AlbumName);
        }

        @Override
        public void onClick(View v) {
            listner.onclick(getAdapterPosition(),VIDEO);            //Returning the current clicked position and type as video to the implemented method.
        }
    }

    public  interface  OnAlbumClickListner{
        void onclick(int position,String type);         //Interface to generate call back when user clicked an image.
    }

                //This method is to shortening the long names as well as generating short names for popular albums

    private String setName(String AlbumName)
    {
        String AlBack= AlbumName;
        boolean flag = false;           //This flag is declared to check if the album name is modified or not.

        try {           //Modifies the album name if it matches the popular album names
            AlbumName = AlbumName.replace("WhatsApp", "WA")
                    .replace("Documents", "Docs")
                    .replace("Screenshots", "SS")
                    .replace("_", " ")
                    .replace("-", " ");
            flag = true;
        } catch (Exception e) {
            Log.d("StringReplace", "onBindViewHolder: " + e.toString());
        }


        if (AlbumName.length() > 10) {          //If the album name is longer than 10 characters it ignores all the characters after 10th characters and puts ".." instead.
            AlbumName = AlbumName.substring(0, 9);
            AlbumName = AlbumName.concat(" ..");
        }

//        if (!flag)
//            AlbumName = AlBack;         //If name is not modified then it is set to the original name.

        return AlbumName;
    }

}

