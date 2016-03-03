package me.liujia95.instantmessaging.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import me.liujia95.instantmessaging.R;


/**
 * Created by Administrator on 2016/3/3 10:57.
 */
public class SlideBar extends Button {
    public interface OnTouchAssortListener {
        void onTouchAssortListener(String s);
    }

    // 分类
    private static final String[] ASSORT_TEXT = {"↑", "#", "A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"};

    private Paint                 mPaint       = new Paint();
    private int                   mSelectIndex = -1;
    private OnTouchAssortListener mListener    = null;
    private Activity mAttachActivity;
    PopupWindow mPopupWindow = null;
    View     layoutView;
    TextView text;

    public SlideBar(Context context) {
        this(context, null);
    }

    public SlideBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mAttachActivity = (Activity) context;
        init(context);
    }

    private void init(Context context) {
        layoutView = LayoutInflater.from(context).inflate(R.layout.alert_dialog_menu_layout, null);
        text = (TextView) layoutView.findViewById(R.id.content);
    }

    public void setOnTouchAssortListener(OnTouchAssortListener listener) {
        this.mListener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int nHeight = getHeight();
        int hWidth = getWidth();
        int nAssortCount = ASSORT_TEXT.length;
        int nInterval = nHeight / nAssortCount;

        for (int i = 0; i < nAssortCount; i++) {
            mPaint.setAntiAlias(true);  // 抗锯齿
            mPaint.setTypeface(Typeface.DEFAULT_BOLD);  // 默认粗体
            mPaint.setColor(Color.parseColor("#5f5f5f"));  // 白色
            if (i == mSelectIndex) {
                // 被选择的字母改变颜色和粗体
                mPaint.setColor(Color.parseColor("#3399ff"));
                mPaint.setFakeBoldText(true);
                mPaint.setTextSize(30);
            }
            float xPos = hWidth / 2 - mPaint.measureText(ASSORT_TEXT[i]) / 2;  // 计算字母的X坐标
            float yPos = nInterval * i + nInterval;  // 计算字母的Y坐标
            canvas.drawText(ASSORT_TEXT[i], xPos, yPos, mPaint);
            mPaint.reset();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //判断是哪一个字母被点击了
        //被点击的字母索引
        int nIndex = (int) (event.getY() / getHeight() * ASSORT_TEXT.length);
        if (nIndex >= 0 && nIndex < ASSORT_TEXT.length) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    // 如果滑动改变
                    if (mSelectIndex != nIndex) {
                        mSelectIndex = nIndex;
                        showCharacter(ASSORT_TEXT[mSelectIndex]);
                        if (mListener != null) {
                            mListener.onTouchAssortListener(ASSORT_TEXT[mSelectIndex]);
                        }
                    }
                    break;
                case MotionEvent.ACTION_DOWN:
                    mSelectIndex = nIndex;
                    showCharacter(ASSORT_TEXT[mSelectIndex]);
                    if (mListener != null) {
                        mListener.onTouchAssortListener(ASSORT_TEXT[mSelectIndex]);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    disShowCharacter();
                    mSelectIndex = -1;
                    break;
            }
        } else {
            mSelectIndex = -1;
            disShowCharacter();
        }
        invalidate();
        return true;
    }

    /**
     * 隐藏弹出的字符
     */
    private void disShowCharacter() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        }
    }

    /**
     * 显示弹出的字符
     *
     * @param string
     */
    private void showCharacter(String string) {
        if (mPopupWindow != null) {
            text.setText(string);
        } else {
            mPopupWindow = new PopupWindow(layoutView, 100, 100, false);
            mPopupWindow.showAtLocation(mAttachActivity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
        }
        text.setText(string);
    }
}
