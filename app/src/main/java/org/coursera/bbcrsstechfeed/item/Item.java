package org.coursera.bbcrsstechfeed.item;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.DataFormatException;

/**
 * Class that describes a piece of news
 */
public class Item implements Parcelable {
    private String title;
    private String description;
    private String link;
    private String pubDate;

    public Item(String title, String description, String link, String pubDate) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = pubDate;
    }

    public Item(Parcel source) {
        String[] data = new String[4];
        source.readStringArray(data);

        this.title = data[0];
        this.description = data[1];
        this.link = data[2];
        this.pubDate = data[3];
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

    public String getPubDate() {
        return pubDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {title, description, link, pubDate});
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
