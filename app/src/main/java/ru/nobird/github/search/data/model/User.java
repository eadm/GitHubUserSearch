package ru.nobird.github.search.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private long id;
    private String login;
    private String name;
    private String avatar_url;
    private String html_url;
    private String location;
    private String bio;
    private long followers;
    private long following;

    public User() {}

    public User(long id, String login, String name, String avatar_url, String html_url, String location, String bio, long followers, long following) {
        this.id = id;
        this.login = login;
        this.name = name;
        this.avatar_url = avatar_url;
        this.html_url = html_url;
        this.location = location;
        this.bio = bio;
        this.followers = followers;
        this.following = following;
    }

    protected User(Parcel in) {
        id = in.readLong();
        login = in.readString();
        name = in.readString();
        avatar_url = in.readString();
        html_url = in.readString();
        location = in.readString();
        bio = in.readString();
        followers = in.readLong();
        following = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(login);
        dest.writeString(name);
        dest.writeString(avatar_url);
        dest.writeString(html_url);
        dest.writeString(location);
        dest.writeString(bio);
        dest.writeLong(followers);
        dest.writeLong(following);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
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

    public String getBio() {
        return bio;
    }

    public long getFollowers() {
        return followers;
    }

    public long getFollowing() {
        return following;
    }

    public String getName() {
        return name;
    }

    public String getHtml_url() {
        return html_url;
    }

    public String getLocation() {
        return location;
    }
}
