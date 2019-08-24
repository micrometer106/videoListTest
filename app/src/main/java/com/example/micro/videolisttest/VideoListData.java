package com.example.micro.videolisttest;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoListData implements Parcelable {
    public String mTitle;
    public String mPath;

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        /** int 寫入 **/
        dest.writeString(mTitle);
        /** string 寫入 **/
        dest.writeString(mPath);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public VideoListData createFromParcel(Parcel in) {

            VideoListData data = new VideoListData();
            data.mTitle = in.readString();
            data.mPath = in.readString();

            return data;
        }

        public VideoListData[] newArray(int size) {
            return new VideoListData[size];
        }
    };
}
