package com.example.collapsingtoolbar.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collapsingtoolbar.Activities.FullPhoto;
import com.example.collapsingtoolbar.Adapter.PhotosAdapter;
import com.example.collapsingtoolbar.Model.ImageModel;
import com.example.collapsingtoolbar.R;
import com.example.collapsingtoolbar.utils.FetchImages;

import java.util.ArrayList;


public class AllPhotosFragment extends Fragment implements PhotosAdapter.OnImageClickListner {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ImageModel> arrayList;
    FetchImages fetchImages;
    Thread Task;
    String TAG ="AllPhotosFragment";
    public AllPhotosFragment() {}

    @Override
    public void onStart() {
        super.onStart();
         Task = new Thread(this::init);
         Task.start();
        try {
            Task.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Task = new Thread(this::fetchImages);
        Task.start();
        try {
            Task.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        fetchImages.setFETCHED(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_all_photos, container, false);
    }

    void init() {
        recyclerView = requireView().findViewById(R.id.recyclerview);
        layoutManager = new GridLayoutManager(getActivity(), 4);
        arrayList = new ArrayList<>();
        fetchImages = new FetchImages(getActivity());
    }




    private void fetchImages() {
        arrayList = fetchImages.fetchImages();
        new Thread(() -> requireActivity().runOnUiThread(() -> {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setHasFixedSize(true);
            PhotosAdapter adapter = new PhotosAdapter(requireActivity().getApplicationContext(), arrayList, getActivity(),this);
            recyclerView.setAdapter(adapter);
            Log.d("FetchImages(): ", " RecyclerView Adapter attached");
        })
        ).start();
    }


    @Override
    public void onclick(int position) {
        Intent intent = new Intent(getActivity(), FullPhoto.class);
        intent.putExtra("uri",arrayList.get(position).getUri().toString());
        startActivity(intent);
    }
}