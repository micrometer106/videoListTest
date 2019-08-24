package com.example.micro.videolisttest;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class VideoListFragment extends Fragment {

    private Context mContext;
    private RecyclerView mRecyclerView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View root = inflater.inflate(R.layout.fragment_layout, container, false);
//        TextView textView = root.findViewById(R.id.text);
//        textView.setText("Fragment");

        mRecyclerView = root.findViewById(R.id.list);
        VideoRecyclerView mVideoRecyclerView = new VideoRecyclerView(mContext);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mVideoRecyclerView);
        return root;
    }



}
