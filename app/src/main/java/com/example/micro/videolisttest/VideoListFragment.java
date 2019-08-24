package com.example.micro.videolisttest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class VideoListFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View root = inflater.inflate(R.layout.fragment_layout, container, false);
        TextView textView = root.findViewById(R.id.text);
        textView.setText("Fragment");
        return root;
    }

}
