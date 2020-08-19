package org.coursera.bbcrsstechfeed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {
    private RecyclerView itemsView;
    private ArrayList<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_view);

//        itemsView = (RecyclerView) findViewById(R.id.itemsView);
//        itemsView.setHasFixedSize(true);
//        itemsView.setLayoutManager(new LinearLayoutManager(this));
//        itemsView.setAdapter(
//                new ItemsAdapter(
//                        items,
//                        isSelectedItem,
//                        new SeeArticleClickListener(),
//                        new ChooseFavArticleClickListener()));

    }
//    private void showToast(String text) {
//        Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
//        toast.show();
//    }

    public class SeeArticleClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int itemIndex = itemsView.getChildLayoutPosition((View) v.getParent().getParent().getParent());

            Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(items.get(itemIndex).getLink()));
            startActivity(intent);
        }
    }
}