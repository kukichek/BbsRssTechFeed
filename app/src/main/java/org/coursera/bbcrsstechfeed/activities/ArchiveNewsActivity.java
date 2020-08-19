package org.coursera.bbcrsstechfeed.activities;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import org.coursera.bbcrsstechfeed.item.Item;
import org.coursera.bbcrsstechfeed.adapters.NewsAdapter;
import org.coursera.bbcrsstechfeed.R;
import org.coursera.bbcrsstechfeed.data.ItemDbHelper;
import org.coursera.bbcrsstechfeed.data.ItemsContract;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ArchiveNewsActivity extends NewsActivity {
    private SQLiteOpenHelper itemsDbHelper;
    private NewsAdapter newsAdapter;
    private List<String> linksToDelete = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_view);

        itemsDbHelper = new ItemDbHelper(this);
        GetDbItemsTask task = new GetDbItemsTask();
        task.execute(itemsDbHelper);

        try {
            items = task.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        newsAdapter = new NewsAdapter(
                items,
                new SeeArticleClickListener(),
                new DeleteItemClickListener());
        setRecyclerView(newsAdapter);
    }

    @Override
    public void onPause() {
        super.onPause();

        DeleteItemsDbTask task = new DeleteItemsDbTask(linksToDelete);
        task.execute(itemsDbHelper);
    }

    public class DeleteItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int itemIndex = itemsView.getChildLayoutPosition((View) v.getParent().getParent().getParent());
            linksToDelete.add(items.get(itemIndex).getLink());
            items.remove(itemIndex);
            newsAdapter.notifyItemRemoved(itemIndex);
        }
    }

    private static class GetDbItemsTask extends AsyncTask<SQLiteOpenHelper, Void, ArrayList<Item>> {
        @Override
        protected ArrayList<Item> doInBackground(SQLiteOpenHelper... sqLiteOpenHelpers) {
            SQLiteDatabase db = sqLiteOpenHelpers[0].getReadableDatabase();
            Cursor cursor = db.query(
                    ItemsContract.ItemEntry.TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null );

            ArrayList<Item> items = new ArrayList<>();
            int titleIndex = cursor.getColumnIndex(ItemsContract.ItemEntry.COLUMN_TITLE);
            int descriptIndex = cursor.getColumnIndex(ItemsContract.ItemEntry.COLUMN_DESCRIPTION);
            int linkIndex = cursor.getColumnIndex(ItemsContract.ItemEntry.COLUMN_LINK);
            int pubDateIndex = cursor.getColumnIndex(ItemsContract.ItemEntry.COLUMN_PUB_DATE_MS);

            while (cursor.moveToNext()) {
                items.add(new Item(
                        cursor.getString(titleIndex),
                        cursor.getString(descriptIndex),
                        cursor.getString(linkIndex),
                        cursor.getLong(pubDateIndex)
                ));
            }
            cursor.close();

            return items;
        }
    }

    private static class DeleteItemsDbTask extends AsyncTask<SQLiteOpenHelper, Void, Void> {
        private List<String> linksToDelete;

        public DeleteItemsDbTask(List<String> linksToDelete) {
            this.linksToDelete = linksToDelete;
        }

        @Override
        protected Void doInBackground(SQLiteOpenHelper... sqLiteOpenHelpers) {
            SQLiteDatabase db = sqLiteOpenHelpers[0].getWritableDatabase();
            for (String link : linksToDelete) {
                db.delete(ItemsContract.ItemEntry.TABLE_NAME, ItemsContract.ItemEntry.COLUMN_LINK + "=?", new String[] {link});
            }

            linksToDelete.clear();

            return null;
        }
    }
}