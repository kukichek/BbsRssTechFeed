package org.coursera.bbcrsstechfeed.activities;

import androidx.core.content.res.ResourcesCompat;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import org.coursera.bbcrsstechfeed.item.Item;
import org.coursera.bbcrsstechfeed.adapters.LatestNewsAdapter;
import org.coursera.bbcrsstechfeed.R;
import org.coursera.bbcrsstechfeed.data.ItemDbHelper;
import org.coursera.bbcrsstechfeed.data.ItemsContract;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class LatestNewsActivity extends NewsActivity {
    public static final String extrasItems = "NEWS_ITEMS";

    private SQLiteOpenHelper itemsDbHelper;
    private boolean[] isSelectedItem;
    private Set<String> linksSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        items = getIntent().getParcelableArrayListExtra(extrasItems);
        isSelectedItem = new boolean[items.size()];

        setRecyclerView(new LatestNewsAdapter(
                items,
                isSelectedItem,
                new SeeArticleClickListener(),
                new ChooseFavArticleClickListener()));

        itemsDbHelper = new ItemDbHelper(this);
        GetDbItemsLinksTask task = new GetDbItemsLinksTask();
        task.execute(itemsDbHelper);

        try {
            linksSet = task.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < items.size(); ++i) {
            isSelectedItem[i] = linksSet.contains(items.get(i).getLink());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        UpdateDbTask task = new UpdateDbTask(linksSet, items, isSelectedItem);
        task.execute(itemsDbHelper);
    }

//    private void showToast(String text) {
//        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
//        toast.show();
//    }

    public class ChooseFavArticleClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int itemIndex = itemsView.getChildLayoutPosition((View) v.getParent().getParent().getParent());
            isSelectedItem[itemIndex] = !isSelectedItem[itemIndex];

            Drawable heartIcon = ResourcesCompat.getDrawable(
                    getResources(),
                    ((isSelectedItem[itemIndex])
                            ? R.drawable.ic_heart_selected
                            : R.drawable.ic_heart_unselected),
                    null);
            ((ImageView) v).setImageDrawable(heartIcon);
        }
    }

    private static class GetDbItemsLinksTask extends AsyncTask<SQLiteOpenHelper, Void, Set<String>> {
        @Override
        protected Set<String> doInBackground(SQLiteOpenHelper... sqLiteOpenHelpers) {
            SQLiteDatabase db = sqLiteOpenHelpers[0].getReadableDatabase();
            Cursor cursor = db.query(
                    ItemsContract.ItemEntry.TABLE_NAME,
                    new String[]{ItemsContract.ItemEntry.COLUMN_LINK},
                    null,
                    null,
                    null,
                    null,
                    null );

            Set<String> links = new HashSet<>();
            int linkId = cursor.getColumnIndex(ItemsContract.ItemEntry.COLUMN_LINK);
            while (cursor.moveToNext()) {
                links.add(cursor.getString(linkId));
            }
            cursor.close();

            return links;
        }
    }

    private static class UpdateDbTask extends AsyncTask<SQLiteOpenHelper, Void, Void> {
        private Set<String> links;
        private List<Item> items;
        private boolean[] isSelectedItem;

        public UpdateDbTask(Set<String> links, List<Item> items, boolean[] isSelectedItem) {
            this.links = links;
            this.items = items;
            this.isSelectedItem = isSelectedItem;
        }

        @Override
        protected Void doInBackground(SQLiteOpenHelper... sqLiteOpenHelpers) {
            SQLiteDatabase db = sqLiteOpenHelpers[0].getWritableDatabase();

            for (int i = 0; i < isSelectedItem.length; ++i) {
                if (isSelectedItem[i] ^ links.contains(items.get(i).getLink())) {
                    if (isSelectedItem[i]) { // item is selected but is not contained in db
                        ContentValues values = new ContentValues();
                        values.put(ItemsContract.ItemEntry.COLUMN_TITLE, items.get(i).getTitle());
                        values.put(ItemsContract.ItemEntry.COLUMN_DESCRIPTION, items.get(i).getDescription());
                        values.put(ItemsContract.ItemEntry.COLUMN_LINK, items.get(i).getLink());
                        values.put(ItemsContract.ItemEntry.COLUMN_PUB_DATE_MS, items.get(i).getPubDate().getTime());

                        db.insert(ItemsContract.ItemEntry.TABLE_NAME, null, values);
                        links.add(items.get(i).getLink());
                    } else { // item is not selected but is contained in db
                        db.delete(ItemsContract.ItemEntry.TABLE_NAME, ItemsContract.ItemEntry.COLUMN_LINK + "=?", new String[] {items.get(i).getLink()});
                        links.remove(items.get(i).getLink());
                    }
                }
            }

            return null;
        }
    }
}