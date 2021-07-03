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


    public static CustomRecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ImageModel> arrayList;
    FetchImages fetchImages;
    Thread Task;
    public String Album = "FETCH_ALL";
    String TAG = "Flow AllPhotosFragment";
    boolean isSelectionMode = false;
    CheckBox selection;
    ConstraintLayout side;

    public AllPhotosFragment() {
    }

    public AllPhotosFragment(String Album) {
        this.Album = Album;
    }

    Parcelable State;
    PhotosAdapter adapter;

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
        requireActivity().runOnUiThread(this::fetchImages);
       return v;
    }



    @Override
    public void onResume() {
        super.onResume();
        requireActivity().runOnUiThread(this::fetchImages);
    }

    public void init(View v) {
        recyclerView = v.findViewById(R.id.recyclerviewPhotos);
        layoutManager = new GridLayoutManager(getActivity(), 4);
        fetchImages = new FetchImages(getActivity());
        side = v.findViewById(R.id.side);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                if (isSelectionMode) {
                    //BackPressLogic
                    isSelectionMode = false;
                    /*  toolbar.animate().alpha(0.0f);*/
                    side.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                    Log.d(TAG, "onLongClick: selectionMode " + isSelectionMode);
                    Log.d(TAG, "onLongClick: selectionMode visibility VISIBLE ");

                } else {
                    setEnabled(false);
                    requireActivity().onBackPressed();
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        State = layoutManager.onSaveInstanceState(); // Save RecyclerView State
    }


    @SuppressLint("SetTextI18n")
    public void fetchImages() {
        arrayList = fetchImages.fetchImages(Album);
        TextView count = requireActivity().findViewById(R.id.count);
        count.setText(arrayList.size() + " Photos");

//        Objects.requireNonNull(layoutManager.findViewByPosition(0)).setSelected(true);
        adapter = new PhotosAdapter(requireActivity().getApplicationContext(), arrayList, getActivity(), this, this);
        recyclerView.setAdapter(adapter);
        layoutManager.onRestoreInstanceState(State);// Restore State
    }


    @Override
    public void onclick(int position, PhotosAdapter.MyViewHolder holder,CheckBox selection) {
        if (!isSelectionMode) {
            Intent intent = new Intent(getActivity(), FullPhoto.class);
            intent.putExtra("uri", arrayList.get(position).getUri().toString());
            startActivity(intent);
        } else {
            this.selection = selection;
            holder.selection.setVisibility(View.VISIBLE);
            if (selection.isChecked()){
                selection.setChecked(false);
            selection.setVisibility(View.GONE);}
            else if (!selection.isChecked()) {
                selection.setVisibility(View.VISIBLE);
                selection.setChecked(true);
            }
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





















