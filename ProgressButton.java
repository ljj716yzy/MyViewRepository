package net.oschina.app.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by Administrator on 2016/6/25.
 */
public class ProgressButton extends Button {

    private int mProgress;
    private Paint mPaint = new Paint();
    private Drawable mProgressDrawable;

    public void setProgressDrawable(Drawable progressDrawable) {
        mProgressDrawable = progressDrawable;
    }

    public ProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public ProgressButton(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.BLUE);
        int left = 0;
        int top = 0;
        int width = getMeasuredWidth();
        int right = (int) (mProgress / 100f * width);
        int bottom = getMeasuredHeight();
        if(mProgressDrawable != null){
            mProgressDrawable.setBounds(left,top,right,bottom);
            mProgressDrawable.draw(canvas);
        }else {
            //每一次都比前一次绘制的矩形更宽，
            canvas.drawRect(left, top, right, bottom, mPaint);
        }
    }

    public void setProgress(Integer value) {
        this.mProgress = value;
        invalidate();
    }
}
