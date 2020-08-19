package org.coursera.bbcrsstechfeed.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.coursera.bbcrsstechfeed.item.Item;
import org.coursera.bbcrsstechfeed.R;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ItemsViewHolder> {
    private ArrayList<Item> items;
    private View.OnClickListener seeArticleClickListener;
    private View.OnClickListener chooseArticleClickListener;

    public NewsAdapter(
            ArrayList<Item> items,
            View.OnClickListener seeArticleClickListener,
            View.OnClickListener chooseArticleClickListener) {
        this.items = items;
        this.seeArticleClickListener = seeArticleClickListener;
        this.chooseArticleClickListener = chooseArticleClickListener;
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_itemview, parent, false);
        view.findViewById(R.id.linkArrowImageView).setOnClickListener(seeArticleClickListener);
        view.findViewById(R.id.chooseImageView).setOnClickListener(chooseArticleClickListener);
        return new ItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
        holder.titleTextView.setText(items.get(position).getTitle());
        holder.descriptionTextView.setText(items.get(position).getDescription());
        holder.pubDateTextView.setText(items.get(position).getPubDate().toString());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ItemsViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView pubDateTextView;
        protected ImageView chooserImageView;

        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
            pubDateTextView = (TextView) itemView.findViewById(R.id.pubDateTextView);
            chooserImageView = (ImageView) itemView.findViewById(R.id.chooseImageView);
        }
    }
}
