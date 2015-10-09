package com.swivltesttask;

import android.app.Activity;
import android.os.Bundle;

import com.android.volley.toolbox.NetworkImageView;

public class AvatarActivity extends Activity {

    public static final String EXTRA_IMAGE_URL = "EXTRA_IMAGE_URL";

    private NetworkImageView mAvatarImage;

    private SwivlTestTaskApplication mApplication = SwivlTestTaskApplication.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.avatar_activity);

        mAvatarImage = (NetworkImageView)findViewById(R.id.avatar_image);

        Bundle extras = getIntent().getExtras();
        if(extras.containsKey(EXTRA_IMAGE_URL)){
            String imageUrl = extras.getString(EXTRA_IMAGE_URL);
            mAvatarImage.setImageUrl(imageUrl, mApplication.getImageLoader());
            mAvatarImage.setDefaultImageResId(R.drawable.avatar_dummy);
        }
    }
}
