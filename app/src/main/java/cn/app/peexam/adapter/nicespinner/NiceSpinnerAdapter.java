package cn.app.peexam.adapter.nicespinner;

import android.content.Context;
import java.util.List;

public class NiceSpinnerAdapter<T> extends NiceSpinnerBaseAdapter {
    private List<T> mItems;

    public NiceSpinnerAdapter(Context context, List<T> items, int textColor, int backgroundSelector) {
        super(context, textColor, backgroundSelector);
        this.mItems = items;
    }

    public int getCount() {
        return this.mItems.size() - 1;
    }

    public T getItem(int position) {
        if (position >= this.mSelectedIndex) {
            return this.mItems.get(position + 1);
        }
        return this.mItems.get(position);
    }

    public void update(List<T> mItems) {
        this.mItems = mItems;
        notifyDataSetChanged();
    }

    public T getItemInDataset(int position) {
        return this.mItems.get(position);
    }
}
