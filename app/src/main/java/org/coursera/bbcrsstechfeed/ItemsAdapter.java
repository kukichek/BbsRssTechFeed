package org.coursera.bbcrsstechfeed;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemsViewHolder> {
    private ArrayList<Item> items;
    private boolean[] isSelectedItem;
    private View.OnClickListener seeArticleClickListener;
    private View.OnClickListener chooseFavArticleClickListener;

    ItemsAdapter(
            ArrayList<Item> items,
            boolean[] isSelectedItem,
            View.OnClickListener seeArticleClickListener,
            View.OnClickListener chooseFavArticleClickListener) {
        this.items = items;
        this.isSelectedItem = isSelectedItem;
        this.seeArticleClickListener = seeArticleClickListener;
        this.chooseFavArticleClickListener = chooseFavArticleClickListener;
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_itemview, parent, false);
        view.findViewById(R.id.linkArrowImageView).setOnClickListener(seeArticleClickListener);
        view.findViewById(R.id.heartImageView).setOnClickListener(chooseFavArticleClickListener);
        return new ItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
        holder.titleTextView.setText(items.get(position).getTitle());
        holder.descriptionTextView.setText(items.get(position).getDescription());
        holder.pubDateTextView.setText(items.get(position).getPubDate().toString());

        Drawable heartIcon = ResourcesCompat.getDrawable(
                holder.itemView.getContext().getResources(),
                ((isSelectedItem[position])
                        ? R.drawable.ic_heart_selected
                        : R.drawable.ic_heart_unselected),
                null);
        holder.heartImageView.setImageDrawable(heartIcon);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ItemsViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView pubDateTextView;
        private ImageView heartImageView;

        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            descriptionTextView = (TextView) itemView.findViewById(R.id.descriptionTextView);
            pubDateTextView = (TextView) itemView.findViewById(R.id.pubDateTextView);
            heartImageView = (ImageView) itemView.findViewById(R.id.heartImageView);
        }
    }


}
