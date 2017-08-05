package com.xcomputers.networking.clubs;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by xComputers on 05/08/2017.
 */

public class Club implements Parcelable {

    @SerializedName("id")
    private long id;
    @SerializedName("name")
    private String name;
    @SerializedName("phone_number")
    private String phoneNumber;
    @SerializedName("email")
    private String email;
    @SerializedName("facebook_url")
    private String fbUrl;
    @SerializedName("updated_at")
    private Date updatedAt;
    @SerializedName("created_at")
    private Date createdAt;
    @SerializedName("fullsize_url")
    private String fullSizeUrl;
    @SerializedName("thumbnail_url")
    private String thumbNailUrl;

    public Club(long id, String name, String phoneNumber, String email, String fbUrl, Date updatedAt, Date createdAt, String thumbNailUrl, String fullSizeUrl) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.fbUrl = fbUrl;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.thumbNailUrl = thumbNailUrl;
        this.fullSizeUrl = fullSizeUrl;
    }

    protected Club(Parcel in) {
        id = in.readLong();
        name = in.readString();
        phoneNumber = in.readString();
        email = in.readString();
        fbUrl = in.readString();
        thumbNailUrl = in.readString();
        fullSizeUrl = in.readString();
        createdAt = new Date(in.readLong());
        updatedAt = new Date(in.readLong());
    }

    public static final Creator<Club> CREATOR = new Creator<Club>() {
        @Override
        public Club createFromParcel(Parcel in) {
            return new Club(in);
        }

        @Override
        public Club[] newArray(int size) {
            return new Club[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(phoneNumber);
        dest.writeString(email);
        dest.writeString(fbUrl);
        dest.writeString(thumbNailUrl);
        dest.writeString(fullSizeUrl);
        dest.writeLong(createdAt.getTime());
        dest.writeLong(updatedAt.getTime());
    }

    public String getFullSizeUrl() {
        return fullSizeUrl;
    }

    public String getThumbNailUrl() {
        return thumbNailUrl;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getFbUrl() {
        return fbUrl;
    }

    public Date getUpdatedAt() {

        return updatedAt;
    }

    public Date getCreatedAt() {

        return createdAt;
    }
}
