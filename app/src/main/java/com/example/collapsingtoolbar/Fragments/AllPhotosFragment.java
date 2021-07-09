package com.example.collapsingtoolbar.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Selection;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collapsingtoolbar.Activities.FullPhoto;
import com.example.collapsingtoolbar.Activities.MainActivity;
import com.example.collapsingtoolbar.Adapter.PhotosAdapter;
import com.example.collapsingtoolbar.Fetch.FetchImages;
import com.example.collapsingtoolbar.Model.ImageModel;
import com.example.collapsingtoolbar.R;
import com.example.collapsingtoolbar.utils.CustomRecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Objects;


public class AllPhotosFragment extends Fragment implements PhotosAdapter.OnImageClickListner, PhotosAdapter.OnImageLongClickListener {

    private static CustomRecyclerView recyclerView;
    private static RecyclerView.LayoutManager layoutManager;
    private static ArrayList<ImageModel> arrayList;
    private static Parcelable State;

    private FetchImages fetchImages;
    private Thread Task;
    private ConstraintLayout side;
    private PhotosAdapter adapter;

    private String Album = "FETCH_ALL";
    private String TAG = "Flow AllPhotosFragment";
    private boolean isSelectionMode = false;

    /*===============================================================   CONSTRUCTORS   ===============================================================*/

    public AllPhotosFragment() { }

    public AllPhotosFragment(String Album) {
        this.Album = Album;
    }

    /*===============================================================   LIFE CYCLE METHODS   ===============================================================*/

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_all_photos, container, false);

        //----------------------------   INITIATOR   ----------------------------//

        Task = new Thread(()->init(v));         //init fun is called to initialize all the views.
        Task.start();
        try {
            Task.join();            //Joining the thread to ensure it finishes the job assigned.
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        requireActivity().runOnUiThread(()->{
            try {

                arrayList = readFromFile("Images.arl");         //Reads from file and stores into arrayList.
                if (arrayList == null)
                {
                    fetchImages(1);
                    Log.d(TAG, "onCreateView: Init Fetch ");
                }
                else {
                    adapter = new PhotosAdapter(requireActivity().getApplicationContext(), arrayList, getActivity(), this, this);
                    requireActivity().runOnUiThread(()->recyclerView.setAdapter(adapter));

                    Log.d(TAG, "onCreateView: ReadFile "+arrayList.size());
                    for (int i = 0; i<arrayList.size();i++)
                    {
                        Log.d(TAG, "onCreateView: uris "+arrayList.get(i).toString());
                    }
                    Log.d(TAG, "onCreateView: loaded from uris ");
                }




            }
            catch (Exception e)
            {
                Log.d(TAG, "onCreateView: Exception "+e.toString());
            }
            });

       return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        setBackPress();

                    //As only 30 photos are loaded from previous launch ,new Photos might be missed.
                    //And App needs to show all the remaining photos. This fun fetches all the remaining images.

        requireActivity().runOnUiThread(()->fetchImages(0));
    }

    @Override
    public void onPause() {
        super.onPause();
        State = layoutManager.onSaveInstanceState(); // Save RecyclerView State
    }

    /*===============================================================   UTILITY METHODS   ===============================================================*/

                //Support method for initializing views.

    public void init(View v) {
        recyclerView = v.findViewById(R.id.recyclerviewPhotos);
        layoutManager = new GridLayoutManager(getActivity(), 4);
        fetchImages = new FetchImages(getActivity(),getContext());
        side = v.findViewById(R.id.side);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

                //Fetches images from the internal storage and sets adapter depending upon i.

    @SuppressLint("SetTextI18n")
    private void fetchImages(int i) {
        if (i == 1){
            arrayList =fetchImages.fetchInitImages();
            writeToFile(arrayList,"Images.arl");
            }
        else if(i == 0)
            arrayList = fetchImages.fetchImages(Album);
        TextView count = requireActivity().findViewById(R.id.count);
        count.setText(arrayList.size() + " Photos");
        adapter = new PhotosAdapter(requireActivity().getApplicationContext(), arrayList, getActivity(), this, this);
        recyclerView.setAdapter(adapter);
        layoutManager.onRestoreInstanceState(State);// Restore State
    }

                //When long click activates selection mode, It needs logic in which onBackPress will hide selection menu,
                //clear all the selection,Hide the selection number TextView.

    private void setBackPress(){
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                            // Handle the back button event

                if (isSelectionMode) {

                                //BackPressLogic

                    isSelectionMode = false;            //Selection mode turned off.
                    side.setVisibility(View.GONE);          //Side panel, which shows operational options onLong click is hidden
                    Log.d(TAG, "onLongClick: selectionMode " + isSelectionMode);
                    Log.d(TAG, "onLongClick: selectionMode visibility VISIBLE ");

                } else {

                                //if selection mode is already turned off, it should call the normal onBackPress

                    setEnabled(false);
                    requireActivity().onBackPressed();
                }
            }
        });
    }


    //----------------------------   file management   ----------------------------//

                //Support fun for writing uris into the file.


    private void writeToFile(ArrayList<ImageModel> arrayList, String filename) {

                    //Create a Dir if it doesn't exists

        File Dir = new File(requireContext().getExternalFilesDir(null).getAbsolutePath() + "/Hello");
        if(!Dir.exists()){
            boolean y= Dir.mkdirs();
            if (y)
                Log.d(TAG, "writeToFile: directory creation successful ");
            else
                Log.d(TAG, "writeToFile: directory creation unsuccessful ");
        }

                    //Create a new file every time so, no data will be appended and all the data will be overwritten.

        File file = new File(requireContext().getExternalFilesDir(null).getAbsolutePath() +"/"+Dir.getName()+"/"+filename);

        try {

                        //Write arrayList to the file.

            FileOutputStream outputStreamWriter = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(outputStreamWriter);
            oos.writeObject(arrayList);
            oos.close();
            outputStreamWriter.close();
            Log.d(TAG, "writeToFile: file Write successful ");
        } catch (IOException e) {
            Log.e(TAG, "Exception : File write failed : " + e.toString());
        }
    }

                //Support fun for reading uris from file.

    private ArrayList<ImageModel> readFromFile(String filename) {

        try {
            FileInputStream fis = new FileInputStream(new File(requireContext().getExternalFilesDir(null).getAbsolutePath() +"/"+"Hello"+"/"+filename));
            ObjectInputStream ois = new ObjectInputStream(fis);
            // Log.d(TAG, "readFromFile: object "+ object);

            return (ArrayList<ImageModel>) ois.readObject();
        } catch (Exception ex) {
            Log.d(TAG, "readFromFile: exception "+ex.toString());
            return null;
        }
    }

    /*====================================================================   INTERFACE METHODS   ====================================================================*/

                //Support fun for typical RecyclerViewOnClick event.

    @Override
    public void onclick(int position,CheckBox selection) {
        if (!isSelectionMode) {
            Intent intent = new Intent(getActivity(), FullPhoto.class);
            intent.putExtra("uri", arrayList.get(position).getUri());
            startActivity(intent);
        }
    }

            //Support fun to turn selectionMode on, onLongClick event.

    @Override
    public void onLongClick(int position, View v) {
        isSelectionMode = true;
        side.setVisibility(View.VISIBLE);
        Log.d(TAG, "onLongClick: selectionMode " + isSelectionMode);
        Log.d(TAG, "onLongClick: selectionMode visibility VISIBLE ");
        Toast.makeText(getActivity(), "onLongCLick " + position, Toast.LENGTH_SHORT).show();
    }


}





















