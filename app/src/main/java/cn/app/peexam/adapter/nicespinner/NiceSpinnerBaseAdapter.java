package cn.app.peexam.adapter.nicespinner;

import android.content.Context;
import android.os.Build.VERSION;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.app.peexam.R;

public abstract class NiceSpinnerBaseAdapter<T> extends BaseAdapter {
    protected int mBackgroundSelector;
    protected Context mContext;
    protected int mSelectedIndex;
    protected int mTextColor;

    protected static class ViewHolder {
        public TextView textView;

        public ViewHolder(TextView textView) {
            this.textView = textView;
        }
    }

    public abstract int getCount();

    public abstract T getItem(int i);

    public abstract T getItemInDataset(int i);

    public NiceSpinnerBaseAdapter(Context context, int textColor, int backgroundSelector) {
        this.mContext = context;
        this.mTextColor = textColor;
        this.mBackgroundSelector = backgroundSelector;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            convertView = View.inflate(this.mContext, R.layout.spinner_list_item, null);
            textView = (TextView) convertView.findViewById(R.id.tv_tinted_spinner);
            if (VERSION.SDK_INT >= 16) {
                textView.setBackground(ContextCompat.getDrawable(this.mContext, this.mBackgroundSelector));
            }
            convertView.setTag(new ViewHolder(textView));
        } else {
            textView = ((ViewHolder) convertView.getTag()).textView;
        }
        textView.setText(getItem(position).toString());
        textView.setTextColor(this.mTextColor);
        return convertView;
    }

    public int getSelectedIndex() {
        return this.mSelectedIndex;
    }

    public void notifyItemSelected(int index) {
        this.mSelectedIndex = index;
    }

    public long getItemId(int position) {
        return (long) position;
    }
}
