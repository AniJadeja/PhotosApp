package com.example.collapsingtoolbar.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.collapsingtoolbar.Activities.VideoPlay;
import com.example.collapsingtoolbar.Adapter.VideosAdapter;
import com.example.collapsingtoolbar.Model.VideoModel;
import com.example.collapsingtoolbar.R;
import com.example.collapsingtoolbar.utils.CustomRecyclerView;
import com.example.collapsingtoolbar.Fetch.FetchVideos;

import java.util.ArrayList;


public class AllVideosFragment extends Fragment implements VideosAdapter.OnVideoClickListner {


    private static CustomRecyclerView recyclerView;
    private static RecyclerView.LayoutManager layoutManager;
    private static ArrayList<VideoModel> arrayList;
    private static Parcelable State;

    private FetchVideos fetchVideos;
    private Thread Task;
    private VideosAdapter adapter;
    private String Album = "FETCH_ALL";
    private String TAG = "Flow AllVideosFragment";

    /*===============================================================   CONSTRUCTORS   ===============================================================*/

    public AllVideosFragment() {
    }

    public AllVideosFragment(String Album) {
        this.Album = Album;
    }

    /*===============================================================   LIFE CYCLE METHODS   ===============================================================*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_videos, container, false);

        //----------------------------   INITIATOR   ----------------------------//

        Task = new Thread(()->init(view));          //init fun is called to initialize all the views.
        Task.start();
        try {
            Task.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

                    //This function tries to read the file and get uris from file so, System won't execute the cycle of fetching all the Albums.
                    //This will be pre-written uris from last app launch, So, It won't be the heavy task for onCreate.
                    //fetches initial 3 videos and shows in recycler view to reduce the load on onCreateView

        requireActivity().runOnUiThread(()->fetchVideos(1));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().runOnUiThread(()->fetchVideos(0));
    }


    @Override
    public void onPause() {
        super.onPause();
        State = layoutManager.onSaveInstanceState(); // Save RecyclerView State
    }

    /*===============================================================   UTILITY METHODS   ===============================================================*/

                //Support method for initializing all the views.

    void init(View view) {
        recyclerView = view.findViewById(R.id.recyclerviewVideos);
        layoutManager = new GridLayoutManager(getActivity(), 3);
        arrayList = new ArrayList<>();
        fetchVideos = new FetchVideos(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

                //Support method fot fetching the videos depending upon i.

    @SuppressLint("SetTextI18n")
    private void fetchVideos(int i) {
        if (i==1)
            arrayList = fetchVideos.fetchInitVideos();
        else if (i==0)
            arrayList = fetchVideos.fetchVideos(Album);
        TextView count = requireActivity().findViewById(R.id.count);
        count.setText(arrayList.size() + " Videos");
        new Thread(() -> requireActivity().runOnUiThread(() -> {
            adapter = new VideosAdapter(requireActivity().getApplicationContext(), arrayList, getActivity(), this);
            recyclerView.setAdapter(adapter);
            layoutManager.onRestoreInstanceState(State); //Restore state
        })
        ).start();
    }


    /*===================================================================   INTERFACE METHODS   ====================================================================*/

                //Support method for typical RecyclerViewOnClick event.

    @Override
    public void onClick(int position) {
        requireActivity().runOnUiThread(() -> {
            Intent intent = new Intent(getActivity(), VideoPlay.class);
            intent.putExtra("Uri", arrayList.get(position).getUri().toString());
            startActivity(intent);
        });

    }
}