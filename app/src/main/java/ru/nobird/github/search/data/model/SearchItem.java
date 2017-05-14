package ru.nobird.github.search.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SearchItem implements Parcelable {
    private String login;
    private long id;
    private String avatar_url;
    private String url;
    private String type;
    private double score;

    private SearchItem(Parcel in) {
        login = in.readString();
        id = in.readLong();
        avatar_url = in.readString();
        url = in.readString();
        type = in.readString();
        score = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(login);
        dest.writeLong(id);
        dest.writeString(avatar_url);
        dest.writeString(url);
        dest.writeString(type);
        dest.writeDouble(score);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SearchItem> CREATOR = new Creator<SearchItem>() {
        @Override
        public SearchItem createFromParcel(Parcel in) {
            return new SearchItem(in);
        }

        @Override
        public SearchItem[] newArray(int size) {
            return new SearchItem[size];
        }
    };

    public String getLogin() {
        return login;
    }

    public long getId() {
        return id;
    }

    public String getAvatarUrl() {
        return avatar_url;
    }
}
