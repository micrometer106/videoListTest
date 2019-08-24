package com.example.micro.videolisttest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import java.util.ArrayList;

public class VideoRecyclerViewHolder extends RecyclerView.Adapter<VideoRecyclerViewHolder.MyViewHolder> {
    private final static String TAG = VideoRecyclerViewHolder.class.getSimpleName();

    private Context mContext;
//    private ArrayList<VideoListData> mList;
    private ArrayList<MediaObject> mMediaList;
    private RequestManager mRequestManager;


    public VideoRecyclerViewHolder(Context context, ArrayList<MediaObject> list, RequestManager requestManager) {
        mContext = context;
        mMediaList = list;
        mRequestManager = requestManager;
        Log.d(TAG, "TTT list is null = "+(mMediaList==null));
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "TTT onCreateHolder");
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_element, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        myViewHolder.mParent = view;
        myViewHolder.mText = view.findViewById(R.id.list_tile);
        myViewHolder.mMediaContainer = view.findViewById(R.id.media_container);
        myViewHolder.mThumbNail = view.findViewById(R.id.thumbnail);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d(TAG, "TTT onBindViewHolder position+1 = "+(position+1));
        holder.mText.setText(String.valueOf(position+1));
        holder.mParent.setTag(this);
        mRequestManager.load(mMediaList.get(position).getThumbnail())
                .into(holder.mThumbNail);
//        holder.mThumbNail.setImageResource(R.drawable.ic_launcher_background);
    }

    @Override
    public int getItemCount() {
        if (mMediaList != null) {
//            Log.d(TAG, "TTT item size = " + mList.size());
            return mMediaList.size();
        }
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mText;
        public FrameLayout mMediaContainer;
        public ImageView mThumbNail;
        public View mParent;

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

}
