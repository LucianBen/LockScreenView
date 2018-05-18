package com.ben.lockscreen;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;

import static com.ben.lockscreen.LockScreenView.State.STATE_NORMAL;

public class LockScreenView extends View {
    private int smallRadius;//小圆的半径
    private int bigRadius;//大圆的半径
    private int normalColor;//正常的颜色
    private int correctColor;//正确的颜色
    private int wrongColor;//错误的颜色

    private State mCurrentState = STATE_NORMAL;
    private Paint mPaint;
    private boolean needZoomIn;

    public enum State { //四种状态，分别是正常状态、选中状态、结果正确状态、结果错误状态
        STATE_NORMAL, STATE_CHOOSE, STATE_RESULT_CORRECT, STATE_RESULT_WRONG
    }

    public LockScreenView(Context context, int smallRadius, int bigRadius,
                          int normalColor, int correctColor, int wrongColor) {
        super(context);
        this.smallRadius = smallRadius;
        this.bigRadius = bigRadius;
        this.normalColor = normalColor;
        this.correctColor = correctColor;
        this.wrongColor = wrongColor;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST) widthSize = Math.round(bigRadius * 2);
        if (heightMode == MeasureSpec.AT_MOST) heightSize = Math.round(bigRadius * 2);

        setMeasuredDimension(widthSize, heightSize);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mCurrentState) {
            case STATE_NORMAL:
                mPaint.setColor(normalColor);
                mPaint.setAlpha(255); // 透明度为0到255，255为不透明，0为全透明
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, smallRadius, mPaint);

                if (needZoomIn) ZoomIn();// 放大以后，在下次恢复正常时要缩小回去

                break;
            case STATE_CHOOSE:
                mPaint.setColor(normalColor);
                mPaint.setAlpha(255);
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, smallRadius, mPaint);
                mPaint.setAlpha(50);
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, bigRadius, mPaint);
                ZoomOut();
                break;
            case STATE_RESULT_CORRECT:
                mPaint.setColor(correctColor);
                mPaint.setAlpha(50);
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, bigRadius, mPaint);
                mPaint.setAlpha(255);
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, smallRadius, mPaint);
                break;
            case STATE_RESULT_WRONG:
                mPaint.setColor(wrongColor);
                mPaint.setAlpha(50);
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, bigRadius, mPaint);
                mPaint.setAlpha(255);
                canvas.drawCircle(getWidth() / 2, getHeight() / 2, smallRadius, mPaint);
                break;
        }
    }

    public void setCurrentState(State state) {
        this.mCurrentState = state;
        invalidate();
    }

    private void ZoomIn() {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "scaleX",
                1, 1.2f);
        animatorX.setDuration(50);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "scaleY",
                1, 1.2f);
        animatorY.setDuration(50);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animatorX, animatorY);
        set.start();
        needZoomIn = true;

    }

    private void ZoomOut() {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "scaleX",
                1, 1f);
        animatorX.setDuration(0);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "scaleY",
                1, 1f);
        animatorY.setDuration(0);
        AnimatorSet set = new AnimatorSet();
        set.playTogether(animatorX, animatorY);
        set.start();

        needZoomIn = false;

    }

}
