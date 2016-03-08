package me.liujia95.instantmessaging.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import me.liujia95.instantmessaging.R;

public class RatioLayout extends FrameLayout {

    private static final int RELATIVE_WIDTH  = 0;
    private static final int RELATIVE_HEIGHT = 1;

    private float mRatio;
    private int   mRelative;

    public RatioLayout(Context context) {
        this(context, null);
    }

    public RatioLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);
        mRatio = typedArray.getFloat(R.styleable.RatioLayout_ratio, 0);
        mRelative = typedArray.getInt(R.styleable.RatioLayout_relative, RELATIVE_WIDTH);

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY && mRatio != 0 && mRelative == RELATIVE_WIDTH) {
            //计算子控件的宽高
            int width = widthSize - getPaddingLeft() - getPaddingRight();
            int height = (int) (width / mRatio + 0.5f);

            measureChildren(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));

            setMeasuredDimension(widthSize, height + getPaddingBottom() + getPaddingTop());
        } else if (heightMode == MeasureSpec.EXACTLY && mRatio != 0 && mRelative == RELATIVE_HEIGHT) {
            //计算子控件的宽高
            int height = heightSize - getPaddingBottom() - getPaddingTop();
            int width = (int) (height * mRatio + 0.5f);

            measureChildren(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));

            setMeasuredDimension(width + getPaddingLeft() + getPaddingRight(), heightSize);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

    }
}
