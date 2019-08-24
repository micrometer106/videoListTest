package com.example.micro.videolisttest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class VideoRecyclerView extends RecyclerView.Adapter<VideoRecyclerView.MyViewHolder> {
    private final static String TAG = VideoRecyclerView.class.getSimpleName();

    private Context mContext;
    private ArrayList<VideoListData> mList;

    public VideoRecyclerView(Context context, ArrayList<VideoListData> list) {
        mContext = context;
        mList = list;
        Log.d(TAG, "TTT list is null = "+(mList==null));
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_element, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        myViewHolder.mText = view.findViewById(R.id.list_tile);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.mText.setText(String.valueOf(position+1));
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            Log.d(TAG, "TTT item size = " + mList.size());
            return mList.size();
        }
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
//        public TextureView mTextureView;
        public TextView mText;

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

}
