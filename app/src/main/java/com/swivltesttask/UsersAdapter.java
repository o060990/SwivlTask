package com.swivltesttask;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_USER = 0;
    private final int VIEW_TYPE_PROGRESS = 1;

    private ArrayList<User> mUsers;

    private boolean mIsLoadingVisible = false;
    private ImageLoader mImageLoader;

    public UsersAdapter(ImageLoader imageLoader) {
        mUsers = new ArrayList<>();
        mImageLoader = imageLoader;
    }

    public UsersAdapter(ArrayList<User> users, ImageLoader imageLoader) {
        mUsers = users;
        mImageLoader = imageLoader;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        if(viewType == VIEW_TYPE_USER){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
            viewHolder =  new ViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_progress_indicator, parent, false);
            viewHolder = new ProgressViewHolder(v);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == VIEW_TYPE_USER){
            final User user = mUsers.get(position);

            final ViewHolder viewHolder = (ViewHolder)holder;
            viewHolder.mAvatarImageView.setImageUrl(user.getAvatarUrl(), mImageLoader);
            viewHolder.mAvatarImageView.setDefaultImageResId(R.drawable.avatar_dummy);
            viewHolder.mAvatarImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(viewHolder.mContext, AvatarActivity.class);
                    intent.putExtra(AvatarActivity.EXTRA_IMAGE_URL, user.getAvatarUrl());
                    viewHolder.mContext.startActivity(intent);
                }
            });
            viewHolder.mLoginTextView.setText(user.getLogin());
            viewHolder.mProfileLinkTextView.setText(user.getHtmlUrl());
        }
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    @Override
    public int getItemViewType(int position) {
        return ((position == mUsers.size() - 1) && mIsLoadingVisible) ? VIEW_TYPE_PROGRESS : VIEW_TYPE_USER;
    }

    public void addItem(User user){
        mUsers.add(user);
        notifyDataSetChanged();
    }

    public ArrayList<User> getItems(){
        return mUsers;
    }

    public User getLastItem(){
        return mUsers.get(mUsers.size() - 1);
    }

    public void setLoadingVisible(boolean isLoadingVisible){
        mIsLoadingVisible = isLoadingVisible;
        if(isLoadingVisible){
            mUsers.add(null);
        } else {
            mUsers.remove(mUsers.size() - 1);
        }
        notifyDataSetChanged();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        public NetworkImageView mAvatarImageView;
        public TextView mLoginTextView;
        public TextView mProfileLinkTextView;
        public Context mContext;

        public ViewHolder(View itemView) {
            super(itemView);

            mAvatarImageView = (NetworkImageView)itemView.findViewById(R.id.avatar_image);
            mLoginTextView = (TextView)itemView.findViewById(R.id.login_text);
            mProfileLinkTextView = (TextView)itemView.findViewById(R.id.profile_link_text);

            mContext = itemView.getContext();
        }

    }

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar mProgressBar;

        public ProgressViewHolder(View itemView) {
            super(itemView);

            mProgressBar = (ProgressBar)itemView.findViewById(R.id.progress_bar);
        }

    }

}
