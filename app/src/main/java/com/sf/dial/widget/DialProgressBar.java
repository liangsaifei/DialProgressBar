package com.sf.dial.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.sf.dial.R;


/**
 * @author Saifei
 *         liangsaifei@163.com
 */
public class DialProgressBar extends View {

    private int centerX;
    private int centerY;

    private Paint normalPaint;

    private Paint progressPaint;

    private Paint textPaint;

    private int strokeWidth = 3;


    /**
     * 扫过的度
     */
    private float sweepDegree = 300f;

    private float lineHeight;

    private float perDegree;

    private int maxPercent = 100;
    private int maxValue;

    private int lastValue;
    private ObjectAnimator animator;

    private int percent;
    private int duration;
    private int ballColor;
    private Bitmap ballBitmap;


    public DialProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DialProgressBar);
        maxValue = a.getInteger(R.styleable.DialProgressBar_max_value, 100);
        duration = a.getInteger(R.styleable.DialProgressBar_duration, 1000);
        strokeWidth = a.getInteger(R.styleable.DialProgressBar_line_stroke_width, 3);
        lineHeight = a.getDimension(R.styleable.DialProgressBar_progress_line_height, dip(20));
        initNormalPaint(a);
        initProgressPaint(a);
        initTextPaint(a);
        initBall(a);

        a.recycle();


        perDegree = sweepDegree / maxPercent;

        initAnimation();
    }

    private void initBall(TypedArray a) {
        ballColor = a.getColor(R.styleable.DialProgressBar_ball_color, progressPaint.getColor());

        Drawable ballDrawable = a.getDrawable(R.styleable.DialProgressBar_android_icon);
        if (ballDrawable == null)
            ballDrawable = getResources().getDrawable(R.drawable.circle_shape);

        ballBitmap = getBitmap(ballDrawable);
    }

    private void initTextPaint(TypedArray a) {
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStrokeWidth(strokeWidth);
        textPaint.setTextSize(a.getDimension(R.styleable.DialProgressBar_text_size, 22));
        textPaint.setColor(a.getColor(R.styleable.DialProgressBar_text_color, progressPaint.getColor()));
        textPaint.setStyle(Paint.Style.FILL);

    }

    private void initNormalPaint(TypedArray a) {
        int normalColor = a.getColor(R.styleable.DialProgressBar_normal_color, Color.GRAY);
        normalPaint = simplePaint();
        normalPaint.setColor(normalColor);
    }

    private void initProgressPaint(TypedArray a) {
        int progressColor = a.getColor(R.styleable.DialProgressBar_progress_color, Color.GREEN);
        progressPaint = simplePaint();
        progressPaint.setStrokeWidth(4f);
        progressPaint.setColor(progressColor);
    }

    private void initAnimation() {
        animator = new ObjectAnimator();
        animator.setDuration(duration);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                percent = (int) animation.getAnimatedValue();
                lastValue = percent;

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

        int size = Math.max(widthSize, heightSize);


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

        String value = String.valueOf(maxValue * percent / maxPercent);

//        canvas.drawText(value, centerX - mRect.right / 2, centerY + mRect.bottom / 2, textPaint);
        canvas.drawText(value, centerX - textPaint.measureText(value) / 2, centerY - (textPaint.descent() + textPaint.ascent()) / 2, textPaint);

    }

    private void drawPercentLines(Canvas canvas) {

        canvas.save();
        canvas.rotate(-60, centerX, centerY);
        for (int i = 0; i < percent; i++) {
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
        canvas.drawBitmap(ballBitmap, lineHeight + dip(5), centerY, normalPaint);
        canvas.rotate(percent / maxPercent * sweepDegree);
        canvas.restore();
    }

    public void setValue(int value) {
        percent = value;
        animator.setIntValues(lastValue, percent);
        animator.start();

    }

    private float dip(int dip) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getContext().getResources().getDisplayMetrics());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = centerY = w / 2;
    }

    private Bitmap getBitmap(Drawable drawable) {
        drawable.setColorFilter(ballColor, PorterDuff.Mode.MULTIPLY);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }


}
