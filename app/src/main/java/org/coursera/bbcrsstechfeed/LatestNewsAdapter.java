package org.coursera.bbcrsstechfeed;

import android.graphics.drawable.Drawable;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import java.util.ArrayList;

public class LatestNewsAdapter extends NewsAdapter {
    private boolean[] isSelectedItem;

    LatestNewsAdapter(
            ArrayList<Item> items,
            boolean[] isSelectedItem,
            View.OnClickListener seeArticleClickListener,
            View.OnClickListener chooseArticleClickListener) {
        super(items, seeArticleClickListener, chooseArticleClickListener);
        this.isSelectedItem = isSelectedItem;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        Drawable heartIcon = ResourcesCompat.getDrawable(
                holder.itemView.getContext().getResources(),
                ((isSelectedItem[position])
                        ? R.drawable.ic_heart_selected
                        : R.drawable.ic_heart_unselected),
                null);
        holder.chooserImageView.setImageDrawable(heartIcon);
    }
}
