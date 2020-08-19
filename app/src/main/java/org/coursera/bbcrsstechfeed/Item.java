package org.coursera.bbcrsstechfeed;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.DataFormatException;

public class Item implements Parcelable {
    private String title;
    private String description;
    private String link;
    private Date pubDate;

    public Item(String title, String description, String link, String pubDateString) throws DataFormatException {
        this.title = title;
        this.description = description;
        this.link = link;

        this.pubDate = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z")
                .parse(pubDateString, new ParsePosition(0));
        if (this.pubDate == null) {
            throw new DataFormatException("Cannot parse string to date");
        }
    }

    public Item(String title, String description, String link, long pubDateLong) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = new Date(pubDateLong);
    }

    public Item(Parcel source) {
        String[] data = new String[3];
        source.readStringArray(data);

        this.title = data[0];
        this.description = data[1];
        this.link = data[2];
        this.pubDate = new Date(source.readLong());
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public Date getPubDate() {
        return pubDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {title, description, link});
        dest.writeLong(pubDate.getTime());
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
