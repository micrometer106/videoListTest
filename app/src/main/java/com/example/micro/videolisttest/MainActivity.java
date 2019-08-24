package com.example.micro.videolisttest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getSimpleName();

    private final static String FRAGMENT_TAG = "f1";
    public final static String KEY_VIDEO_LIST = "key_video_list";
    private ArrayList<VideoListData> mList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        isStoragePermissionGranted();

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        QueryVideo queryVideo = new QueryVideo();
        queryVideo.executeOnExecutor(executorService);
    }

    private void showFragment(ArrayList<VideoListData> list) {
        mList = list;
        VideoListFragment fragment = new VideoListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_VIDEO_LIST, mList);
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, FRAGMENT_TAG).commit();
    }

    class QueryVideo extends AsyncTask<Void, Void, Void> {
        private ArrayList<VideoListData> mList;

        @Override
        protected Void doInBackground(Void... voids) {

            Cursor cursor = null;
            try {
                cursor = getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        null, null, null);
                Log.d(TAG, "TTT cursor.count() = "+cursor.getCount());
                while(cursor != null && cursor.moveToNext()) {
                    if (mList == null) {
                        mList = new ArrayList<VideoListData>();
                    }
                    VideoListData data = new VideoListData();
                    data.mTitle = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.TITLE));
                    data.mPath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA));
                    mList.add(data);
                    Log.d(TAG, "TTT "+cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA)));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            showFragment(mList);
        }
    }

    private void isStoragePermissionGranted() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permission granted");
            return;
        }
        Log.d(TAG, "Permission revoked");
        ActivityCompat.requestPermissions(this,
                new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        return;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Permsission: "+permissions[0]+" was "+grantResults[0]);
        }
    }
}
