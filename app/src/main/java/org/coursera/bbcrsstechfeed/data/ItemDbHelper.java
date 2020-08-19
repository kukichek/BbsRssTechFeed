package org.coursera.bbcrsstechfeed.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import org.coursera.bbcrsstechfeed.data.ItemsContract.ItemEntry;

public class ItemDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "items.db";

    private static final int DATABASE_VERSION = 1;

    public ItemDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_GUESTS_TABLE = "CREATE TABLE " + ItemEntry.TABLE_NAME + " ("
                + ItemEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ItemEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                + ItemEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, "
                + ItemEntry.COLUMN_LINK + " TEXT NOT NULL, "
                + ItemEntry.COLUMN_PUB_DATE_MS + " INTEGER NOT NULL);";

        db.execSQL(SQL_CREATE_GUESTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
