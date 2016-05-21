package com.sf.dial.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.sf.dial.R;

import java.text.DecimalFormat;


/**
 * @author Saifei
 *         liangsaifei@163.com
 */
public class DialProgressBar extends View {


    private int centerX;

    private int centerY;

    private Paint normalPaint;

    private Paint progressPaint;

    private Paint ballPaint;


    private Paint textPaint;

    private int textStrokeWidth = 3;

    private int strokeWidth = 3;


    /**
     * 扫过的度
     */
    private float sweepDegree = 300f;

    private float lineHeight;

    private float perDegree;

    private int maxPercent = 100;

    private float targetValue;
    private int maxValue;

    private float lastPercent;
    private ValueAnimator animator;

    private float targetPercent;
    private float currPercent;

    private int totalDuration;
    private float ballRadius;
    private DecimalFormat decimalFormat;


    public DialProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DialProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DialProgressBar);
        maxValue = a.getInteger(R.styleable.DialProgressBar_max_value, 100);
        totalDuration = a.getInteger(R.styleable.DialProgressBar_duration, 1000);
        strokeWidth = a.getInteger(R.styleable.DialProgressBar_line_stroke_width, 3);
        lineHeight = a.getDimension(R.styleable.DialProgressBar_progress_line_height, dip(20));


        setNormalPaint(a);
        setProgressPaint(a);
        setBallPaint(a);
        setTextPaint(a);
        a.recycle();
        perDegree = sweepDegree / maxPercent;
        decimalFormat = new DecimalFormat("0.#");
        initAnimation();
    }

    private void setBallPaint(TypedArray a) {
        ballPaint = simplePaint();
        ballPaint.setStyle(Paint.Style.FILL);
        int ballColor = a.getColor(R.styleable.DialProgressBar_ball_color, progressPaint.getColor());
        ballPaint.setColor(ballColor);
        ballRadius = a.getDimension(R.styleable.DialProgressBar_ball_radius, dip(3));
    }


    private void setTextPaint(TypedArray a) {
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textStrokeWidth = a.getInteger(R.styleable.DialProgressBar_text_stroke_width, textStrokeWidth);
        textPaint.setStrokeWidth(textStrokeWidth);
        textPaint.setTextSize(a.getDimension(R.styleable.DialProgressBar_text_size, sp(22)));
        textPaint.setColor(a.getColor(R.styleable.DialProgressBar_text_color, progressPaint.getColor()));
        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);

    }

    private void setNormalPaint(TypedArray a) {
        normalPaint = simplePaint();
        int normalColor = a.getColor(R.styleable.DialProgressBar_normal_color, Color.GRAY);
        normalPaint.setColor(normalColor);
    }

    private void setProgressPaint(TypedArray a) {
        progressPaint = simplePaint();
        int progressColor = a.getColor(R.styleable.DialProgressBar_progress_color, Color.GREEN);
        progressPaint.setStrokeWidth(4f);
        progressPaint.setColor(progressColor);
    }

    private void initAnimation() {
        animator = new ValueAnimator();
        animator.setDuration(totalDuration);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currPercent = (float) animation.getAnimatedValue();

                lastPercent = currPercent;

                invalidate();
            }
        });
    }


    private Paint simplePaint() {
        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setDither(true);


        return mPaint;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY) {
            widthSize = (int) dip(100);
        }
        if (heightMode != MeasureSpec.EXACTLY) {
            heightSize = (int) dip(100);
        }

        int size = Math.min(widthSize, heightSize);
        setMeasuredDimension(size, size);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawNormalLines(canvas);

        drawPercentLines(canvas);

        drawSmallBall(canvas);
        drawCenterValue(canvas);

    }

    private void drawCenterValue(Canvas canvas) {

        float value;
        if (currPercent == targetPercent)
            value = targetValue;
        else
            value = (int) (maxValue * currPercent / maxPercent);

        canvas.drawText(decimalFormat.format(value), centerX - textPaint.measureText(decimalFormat.format(value)) / 2, centerY - (textPaint.descent() + textPaint.ascent()) / 2, textPaint);

    }

    private void drawPercentLines(Canvas canvas) {

        canvas.save();
        canvas.rotate(-60, centerX, centerY);
        for (float i = 0; i < currPercent; i++) {
            canvas.drawLine(strokeWidth, centerY, lineHeight, centerY, progressPaint);
            canvas.rotate(perDegree, centerX, centerY);
        }

    }

    private void drawNormalLines(Canvas canvas) {
        canvas.save();
        canvas.rotate(-60, centerX, centerY);
        for (int i = 0; i < maxPercent; i++) { //画普通的
            canvas.drawLine(strokeWidth, centerY, lineHeight, centerY, normalPaint);
            canvas.rotate(perDegree, centerX, centerY);
        }
        canvas.restore();
    }

    private void drawSmallBall(Canvas canvas) {

        canvas.drawCircle(lineHeight + dip(5) + ballRadius, centerY + ballRadius
                , ballRadius, ballPaint);
        canvas.rotate(currPercent / maxPercent * sweepDegree);
        canvas.restore();
    }

    public void setValue(float value) {

        targetValue = value;

        if (value >= maxValue) {
            targetPercent = maxPercent;
        } else {
            targetPercent = (value / maxValue * maxPercent);
        }

        animator.setFloatValues(lastPercent, targetPercent);
        float duration = (Math.abs((targetPercent - lastPercent))) / maxPercent * totalDuration;
        animator.setDuration((int) duration);
        animator.start();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = centerY = w / 2;
    }


    public int getMaxValue() {
        return maxValue;
    }


    private float dip(int dip) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getContext().getResources().getDisplayMetrics());
    }

    private float sp(int sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getContext().getResources().getDisplayMetrics());
    }
}
