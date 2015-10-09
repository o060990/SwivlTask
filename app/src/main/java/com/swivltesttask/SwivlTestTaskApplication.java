package com.swivltesttask;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class SwivlTestTaskApplication extends Application {

    private static SwivlTestTaskApplication sApplication;

    private RequestQueue mQueue;
    private ImageLoader mImageLoader;

    @Override
    public void onCreate() {
        super.onCreate();

        sApplication = this;
        mQueue = Volley.newRequestQueue(this);

        ImageLoader.ImageCache imageCache = new BitmapLruCache();
        mImageLoader = new ImageLoader(mQueue, imageCache);
    }

    public static SwivlTestTaskApplication getInstance(){
        return sApplication;
    }

    public RequestQueue getRequestQueue(){
        return mQueue;
    }

    public ImageLoader getImageLoader(){
        return mImageLoader;
    }

}
