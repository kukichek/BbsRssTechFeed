package org.coursera.bbcrsstechfeed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import org.coursera.bbcrsstechfeed.data.ItemDbHelper;
import org.coursera.bbcrsstechfeed.data.ItemsContract;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ArchiveNewsActivity extends AppCompatActivity {
    private SQLiteOpenHelper itemsDbHelper;
    private ArrayList<Item> items;
    private RecyclerView itemsView;

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

        itemsView = (RecyclerView) findViewById(R.id.itemsView);
        itemsView.setHasFixedSize(true);
        itemsView.setLayoutManager(new LinearLayoutManager(this));
        itemsView.setAdapter(
                new ArchiveNewsAdapter(
                        items,
                        new ArchiveNewsActivity.SeeArticleClickListener(),
                        new ArchiveNewsActivity.DeleteItemClickListener()));
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

    // TODO: delete doubling method in LatestNewsActivity by extending one superclass
    public class SeeArticleClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int itemIndex = itemsView.getChildLayoutPosition((View) v.getParent().getParent().getParent());

            Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(items.get(itemIndex).getLink()));
            startActivity(intent);
        }
    }

    public class DeleteItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // TODO: implement deleting
        }
    }
}