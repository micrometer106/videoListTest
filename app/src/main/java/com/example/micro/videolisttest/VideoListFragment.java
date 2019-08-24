package com.example.micro.videolisttest;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Arrays;

public class VideoListFragment extends Fragment {

    private Context mContext;
    private VideoRecyclerView mRecyclerView;
    private ArrayList<VideoListData> mList;
    private ArrayList<MediaObject> mMediaList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View root = inflater.inflate(R.layout.fragment_layout, container, false);
//        TextView textView = root.findViewById(R.id.text);
//        textView.setText("Fragment");

        mList = getArguments().getParcelableArrayList(MainActivity.KEY_VIDEO_LIST);
        mRecyclerView = root.findViewById(R.id.list);
        //TODO: use Resources temporarily
//        VideoRecyclerViewHolder mVideoRecyclerViewHolder = new VideoRecyclerViewHolder(mContext, mList);
        mMediaList = new ArrayList<MediaObject>(Arrays.asList(Resources.MEDIA_OBJECTS));
        mRecyclerView.setMediaObjects(mMediaList);
        VideoRecyclerViewHolder mVideoRecyclerViewHolder = new VideoRecyclerViewHolder(mContext, mMediaList, initGlide());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mVideoRecyclerViewHolder);
        return root;
    }

    //TODO: release player

    private RequestManager initGlide(){
        RequestOptions options = new RequestOptions();
//                .placeholder(R.drawable.white_background)
//                .error(R.drawable.white_background);

        return Glide.with(this)
                .setDefaultRequestOptions(options);
    }

}
