package cn.app.peexam.adapter.nicespinner;

import android.content.Context;
import android.widget.ListAdapter;

public class NiceSpinnerAdapterWrapper extends NiceSpinnerBaseAdapter {
    private final ListAdapter mBaseAdapter;

    public NiceSpinnerAdapterWrapper(Context context, ListAdapter toWrap, int textColor, int backgroundSelector) {
        super(context, textColor, backgroundSelector);
        this.mBaseAdapter = toWrap;
    }

    public int getCount() {
        return this.mBaseAdapter.getCount() - 1;
    }

    public Object getItem(int position) {
        if (position >= this.mSelectedIndex) {
            return this.mBaseAdapter.getItem(position + 1);
        }
        return this.mBaseAdapter.getItem(position);
    }

    public Object getItemInDataset(int position) {
        return this.mBaseAdapter.getItem(position);
    }
}
