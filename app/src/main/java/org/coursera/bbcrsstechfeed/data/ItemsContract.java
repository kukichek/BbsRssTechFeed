package org.coursera.bbcrsstechfeed.data;

import android.provider.BaseColumns;

public class ItemsContract {
    private ItemsContract() {}

    public static final class ItemEntry implements BaseColumns {
        public final static String TABLE_NAME = "items";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_TITLE = "title";
        public final static String COLUMN_DESCRIPTION = "description";
        public final static String COLUMN_LINK = "link";
        public final static String COLUMN_PUB_DATE_MS = "pub_date";
    }
}
