package com.swivltesttask;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class User implements Parcelable {

    private int id;
    private String login;
    @SerializedName("avatar_url")
    private String avatarUrl;
    @SerializedName("html_url")
    private String htmlUrl;

    private User(Parcel in){
        id = in.readInt();
        login = in.readString();
        avatarUrl = in.readString();
        htmlUrl = in.readString();
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(login);
        dest.writeString(avatarUrl);
        dest.writeString(htmlUrl);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

}
