package cn.app.peexam.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import cn.app.peexam.R;

public class TextProgressBar extends ProgressBar {
    private Paint mPaint;
    private String text;

    public TextProgressBar(Context context) {
        super(context);
        System.out.println("1");
        initText();
    }

    public TextProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        System.out.println("2");
        initText();
    }

    public TextProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        System.out.println("3");
        initText();
    }

    public synchronized void setProgress(int progress) {
        setText(progress);
        super.setProgress(progress);
    }

    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = new Rect();
        this.mPaint.setTextSize(getResources().getDimension(R.dimen.x10));
        this.mPaint.getTextBounds(this.text, 0, this.text.length(), rect);
        int x = (getMeasuredWidth() / 2) - rect.centerX();
        int y = (getMeasuredHeight() / 2) - rect.centerY();
        float width = ((float) getProgress()) * ((float) (getMeasuredWidth() / getMax()));
        FontMetricsInt fontMetrics = this.mPaint.getFontMetricsInt();
        int baseline = (((getMeasuredHeight() - fontMetrics.bottom) + fontMetrics.top) / 2) - fontMetrics.top;
        if (getProgress() == getMax()) {
            canvas.drawText(this.text, (float) x, (float) baseline, this.mPaint);
        } else if (width < ((float) rect.width())) {
            this.mPaint.setColor(getResources().getColor(R.color.title));
            canvas.drawText(this.text, (float) x, (float) baseline, this.mPaint);
            this.mPaint.setColor(-1);
        } else {
            canvas.drawText(this.text, width - ((float) rect.width()), (float) baseline, this.mPaint);
        }
    }

    private void initText() {
        this.mPaint = new Paint();
        this.mPaint.setColor(-1);
        this.mPaint.setTextAlign(Align.LEFT);
    }

    private void setText() {
        setText(getProgress());
    }

    private void setText(int progress) {
        this.text = String.valueOf((progress * 100) / getMax()) + "%";
    }
}
