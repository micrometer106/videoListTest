package com.example.micro.videolisttest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    private final static String FRAGMENT_TAG = "f1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VideoListFragment fragment = new VideoListFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, FRAGMENT_TAG).commit();
    }
}
