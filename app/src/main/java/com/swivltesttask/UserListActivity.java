package com.swivltesttask;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.ArrayList;

public class UserListActivity extends Activity {

    private static final String USERS_URL = "https://api.github.com/users?since=%1$s&per_page=%2$s";
    private static final int USERS_LOAD_TRIGGER_THRESHOLD = 5;
    private static final int USERS_CHUNK_SIZE = 30;

    private static final String SS_USERS_LIST = "SS_USERS_LIST";
    private static final String SS_LAYOUT_MANAGER_STATE = "SS_LAYOUT_MANAGER_STATE";

    /****************************************
     *
     *  I already finished the code when noticed that
     *  task says I should use ListView. Anyways, I hope
     *  this is not really important and implementation
     *  using recyclerview will also be fine.
     *
     ****************************************/
    private RecyclerView mUsersRecyclerView;
    private UsersAdapter mUsersAdapter;
    private LinearLayoutManager mUsersLayoutManager;

    private boolean mLoading = false;

    private SwivlTestTaskApplication mApplication = SwivlTestTaskApplication.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUsersRecyclerView = new RecyclerView(this);
        mUsersRecyclerView.setHasFixedSize(true);
        mUsersRecyclerView.setOnScrollListener(mOnScrollListener);
        mUsersLayoutManager = new LinearLayoutManager(this);

        Parcelable layoutManagerState = null;
        if(savedInstanceState == null){
            mUsersAdapter = new UsersAdapter(mApplication.getImageLoader());
        } else {
            ArrayList<User> users = savedInstanceState.getParcelableArrayList(SS_USERS_LIST);
            mUsersAdapter = new UsersAdapter(users, mApplication.getImageLoader());
            layoutManagerState = savedInstanceState.getParcelable(SS_LAYOUT_MANAGER_STATE);
        }
        mUsersRecyclerView.setAdapter(mUsersAdapter);
        mUsersRecyclerView.setLayoutManager(mUsersLayoutManager);

        if(layoutManagerState != null){
            mUsersLayoutManager.onRestoreInstanceState(layoutManagerState);
        }

        if(mUsersAdapter.getItemCount() > 0){
            User lastUser = mUsersAdapter.getLastItem();
            if(lastUser != null){
                startLoading(mUsersAdapter.getLastItem().getId());
            }
        } else {
            startLoading(0);
        }

        setContentView(mUsersRecyclerView);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(SS_USERS_LIST, mUsersAdapter.getItems());
        outState.putParcelable(SS_LAYOUT_MANAGER_STATE, mUsersLayoutManager.onSaveInstanceState());

        super.onSaveInstanceState(outState);
    }

    private void startLoading(int offset){
        String url = String.format(USERS_URL, String.valueOf(offset), USERS_CHUNK_SIZE);
        GsonRequest<User[]> usersRequest = new GsonRequest<>(url, User[].class,
                mUsersResponseListener, mUsersErrorListener);
        mApplication.getRequestQueue().add(usersRequest);
        mUsersAdapter.setLoadingVisible(true);
        mLoading = true;
    }

    private void onLoadingFinished(){
        mUsersAdapter.setLoadingVisible(false);
        mLoading = false;
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = recyclerView.getChildCount();
            int totalItemCount = mUsersLayoutManager.getItemCount();
            int firstVisibleItem = mUsersLayoutManager.findFirstVisibleItemPosition();

            if (!mLoading && (totalItemCount - visibleItemCount)
                    <= (firstVisibleItem + USERS_LOAD_TRIGGER_THRESHOLD)) {
                startLoading(mUsersAdapter.getLastItem().getId());
            }
        }
    };

    private Response.Listener<User[]> mUsersResponseListener = new Response.Listener<User[]>() {
        @Override
        public void onResponse(User[] users) {
            onLoadingFinished();
            for(User user: users){
                mUsersAdapter.addItem(user);
            }
        }
    };

    private Response.ErrorListener mUsersErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            onLoadingFinished();
            Toast.makeText(UserListActivity.this, error.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    };

}
