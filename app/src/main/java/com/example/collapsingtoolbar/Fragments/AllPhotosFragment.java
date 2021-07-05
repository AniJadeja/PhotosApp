package com.example.collapsingtoolbar.Fragments;

import android.annotation.SuppressLint;
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

    //--------------------------------------------------------------   Constructors   --------------------------------------------------------------//

    public AllPhotosFragment() { }

    public AllPhotosFragment(String Album) {
        this.Album = Album;
    }

    //--------------------------------------------------------------   Life Cycle Methods   --------------------------------------------------------------//

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_all_photos, container, false);
        Task = new Thread(()->init(v));
        Task.start();
        try {
            Task.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        requireActivity().runOnUiThread(()->fetchImages(1));

       return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        setBackPress();
        requireActivity().runOnUiThread(()->fetchImages(0));
    }

    @Override
    public void onPause() {
        super.onPause();
        State = layoutManager.onSaveInstanceState(); // Save RecyclerView State
    }

    //--------------------------------------------------------------   Utility Methods   --------------------------------------------------------------//

    public void init(View v) {
        recyclerView = v.findViewById(R.id.recyclerviewPhotos);
        layoutManager = new GridLayoutManager(getActivity(), 4);
        fetchImages = new FetchImages(getActivity());
        side = v.findViewById(R.id.side);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    @SuppressLint("SetTextI18n")
    private void fetchImages(int i) {
        if (i == 1)
            arrayList =fetchImages.fetchInitImages();
        else if(i == 0)
            arrayList = fetchImages.fetchImages(Album);
        TextView count = requireActivity().findViewById(R.id.count);
        count.setText(arrayList.size() + " Photos");
        adapter = new PhotosAdapter(requireActivity().getApplicationContext(), arrayList, getActivity(), this, this);
        recyclerView.setAdapter(adapter);
        layoutManager.onRestoreInstanceState(State);// Restore State
    }

    private void setBackPress(){
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                if (isSelectionMode) {
                    //BackPressLogic
                    isSelectionMode = false;
                    side.setVisibility(View.GONE);
                    Log.d(TAG, "onLongClick: selectionMode " + isSelectionMode);
                    Log.d(TAG, "onLongClick: selectionMode visibility VISIBLE ");

                } else {
                    setEnabled(false);
                    requireActivity().onBackPressed();
                }
            }
        });
    }

    //--------------------------------------------------------------   Interface Methods   --------------------------------------------------------------//

    @Override
    public void onclick(int position, PhotosAdapter.MyViewHolder holder,CheckBox selection) {
        if (!isSelectionMode) {
            Intent intent = new Intent(getActivity(), FullPhoto.class);
            intent.putExtra("uri", arrayList.get(position).getUri().toString());
            startActivity(intent);
        }
    }

    @Override
    public void onLongClick(int position, View v) {
        isSelectionMode = true;
        side.setVisibility(View.VISIBLE);
        Log.d(TAG, "onLongClick: selectionMode " + isSelectionMode);
        Log.d(TAG, "onLongClick: selectionMode visibility VISIBLE ");
        Toast.makeText(getActivity(), "onLongCLick " + position, Toast.LENGTH_SHORT).show();
    }


}





















