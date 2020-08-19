package org.coursera.bbcrsstechfeed;

import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;

public class ArchiveNewsAdapter extends NewsAdapter {
    public ArchiveNewsAdapter(ArrayList<Item> items,
                              View.OnClickListener seeArticleClickListener,
                              View.OnClickListener chooseArticleClickListener) {
        super(items, seeArticleClickListener, chooseArticleClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        Drawable heartIcon = ResourcesCompat.getDrawable(
                holder.itemView.getContext().getResources(),
                R.drawable.ic_baseline_delete_outline_24,
                null);
        holder.chooserImageView.setImageDrawable(heartIcon);
    }
}
