package com.ucmap.test;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

/**
 * 作者: Justson
 * 时间:2016/9/11 19:03.
 * 邮箱: cenxiaozhong.qqcom@qq.com
 * 公司: YGS
 */
public class ScView extends View {

    private int RADUIS = 120;
    private int DEFWIDTH;
    private int DEFHEIGHT;
    private Paint mPaint;
    private int density;


    private int model = 0;
    private static final int NOMAL_MODEL = 0;
    private static final int MANUAL_MODEL = 1;

    //信用值
    private int value;
    private int outRadianPaintWidth;
    private int innerRadianPaintWidth;
    private Paint mDynamicPaint;
    private ValueAnimator v;
    private int LONGSCALEMULTIPE = 45;
    private int longScale;
    private int shortScale;

    private String limit = "705";

    float dValue = -1;

    int cx;
    int cy;
    private final int angel = 165;
    private int sweep = 225;

    private String record = "信用极好";
    private int dynamicScale = 210;
    private Paint smallPaint;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    private static final RectF rectf = new RectF();

    private static final RectF innerRectF = new RectF();

    public ScView(Context context) {
        this(context, null);
    }

    public ScView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {

        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setAlpha(80);
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mDynamicPaint = new Paint();
        mDynamicPaint = new Paint();
        mDynamicPaint.setColor(Color.WHITE);
        mDynamicPaint.setAlpha(80);
        mDynamicPaint.setStrokeCap(Paint.Cap.BUTT);
        mDynamicPaint.setAntiAlias(true);
        mDynamicPaint.setDither(true);
        mDynamicPaint.setStyle(Paint.Style.STROKE);

        density = (int) context.getResources().getDisplayMetrics().density;
        RADUIS *= density;
        DEFHEIGHT = RADUIS * 2;
        DEFWIDTH = DEFHEIGHT;

        outRadianPaintWidth = 4;
        outRadianPaintWidth *= density;

        innerRadianPaintWidth = 12;
        innerRadianPaintWidth *= density;

        mPaint.setStrokeWidth(outRadianPaintWidth);

        GAP *= density;
        SMALLRADUIS *= density;

        smallPaint = new Paint();
        smallPaint.setColor(Color.WHITE);
//        smallPaint.setAlpha(180);
        smallPaint.setStrokeCap(Paint.Cap.BUTT);
        smallPaint.setAntiAlias(true);
        smallPaint.setDither(true);
        smallPaint.setStyle(Paint.Style.FILL);
    }

    private int SMALLRADUIS = 3;
    private int GAP = 10;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectf.left = outRadianPaintWidth / 2 + .5f;
        rectf.right = w - (outRadianPaintWidth / 2 + .5f);
        rectf.top = outRadianPaintWidth / 2 + .5f;
        rectf.bottom = h - (outRadianPaintWidth / 2 + .5f);

        innerRectF.left = innerRadianPaintWidth / 2 + rectf.left + GAP;
        innerRectF.right = rectf.right - GAP - innerRadianPaintWidth / 2;
        innerRectF.top = innerRadianPaintWidth / 2 + rectf.top + GAP;
        innerRectF.bottom = rectf.bottom - GAP - innerRadianPaintWidth / 2;
    }


    private int dynamic = 0;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        startAnimationToScView();
    }

    private void startAnimationToScView() {
        v = ValueAnimator.ofFloat(Float.valueOf(dynamicScale));
        v.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float dynimacValue = (float) animation.getAnimatedValue();
                ScView.this.dValue = dynimacValue;
                invalidate();
            }
        });
        v.setDuration(2000);
        v.start();
    }

    private void stopAnimationToScView() {
        if (v != null && v.isRunning()) {
            v.cancel();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimationToScView();

    }


    private int getAlpha(int value, int all) {
        return (value / all )* 255;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        cx = this.getWidth() / 2;
        cy = this.getHeight() / 2;
        canvas.rotate(-LONGSCALEMULTIPE / 6, cx, cy);

        mDynamicPaint.setStrokeWidth(outRadianPaintWidth);
        Path path = new Path();
        path.addArc(rectf, angel, dValue);
        mDynamicPaint.setColor(Color.WHITE);
//        mDynamicPaint.setAlpha(getAlpha((int) dValue, dynamicScale));
        canvas.drawPath(path, mDynamicPaint);

        float sCx = (float) ((float) cx + rectf.right / 2 * (Math.cos(Math.PI * (dValue - LONGSCALEMULTIPE / 3) / 180)));
        float sCy = (float) ((float) cy + rectf.bottom / 2 * (Math.sin(Math.PI * (dValue - LONGSCALEMULTIPE / 3) / 180)));

//        Log.i("Infoss", "sCx:" + sCx + "   sCy:" + sCy + "  value:" + dValue);
        if (sCx < cx) {
            sCx = sCx + outRadianPaintWidth / 2 - 3;
        } else {
            sCx = sCx - outRadianPaintWidth / 2 + 3;
        }
        canvas.drawCircle(this.getWidth() - sCx, this.getHeight() - sCy, SMALLRADUIS, smallPaint);


        mPaint.setStrokeWidth(outRadianPaintWidth);
        canvas.drawArc(rectf, angel, sweep, false, mPaint);

        mPaint.setStrokeWidth(innerRadianPaintWidth);
        canvas.drawArc(innerRectF, angel, sweep, false, mPaint);


        canvas.save();
        drawSale(canvas, mDynamicPaint);
        canvas.restore();

        canvas.save();
        drawText(canvas, new Paint());
//        canvas.restore();
    }


    private void drawSale(Canvas canvas, Paint mDynamicPaint) {
        canvas.rotate(-15, this.getWidth() / 2, this.getHeight() / 2);
        longScale = innerRadianPaintWidth + (3 * density);
        shortScale = innerRadianPaintWidth;
        mDynamicPaint.setAlpha(255);
        mDynamicPaint.setStrokeWidth(outRadianPaintWidth - (2 * density));
        float degree = 7.5f;
        for (int i = 0; i <= 30; i++) {

//            Log.i("Infoss", "drawSale:" + i);
            if (i * degree % LONGSCALEMULTIPE == 0) {

                canvas.drawLine(innerRectF.left - innerRadianPaintWidth / 2, this.getHeight() / 2, innerRectF.left - innerRadianPaintWidth / 2 + longScale, this.getHeight() / 2, mDynamicPaint);
                canvas.rotate(degree, this.getWidth() / 2, this.getHeight() / 2);
                continue;
            }
            canvas.drawLine(innerRectF.left - innerRadianPaintWidth / 2, this.getHeight() / 2, innerRectF.left - innerRadianPaintWidth / 2 + shortScale, this.getHeight() / 2, mDynamicPaint);
            canvas.rotate(degree, this.getWidth() / 2, this.getHeight() / 2);
        }
    }


    private String[] text = new String[]{"350", "较差", "550", "中等", "600", "良好", "650", "优秀", "700", "较好", "950"};

    private void drawText(Canvas canvas, Paint paint) {
        canvas.rotate(-15, this.getWidth() / 2, this.getHeight() / 2);
        int textScale = innerRadianPaintWidth + (8 * density);

        paint.setShader(null);
        paint.setTextSize(6 * density);
        paint.setAlpha(120);
        paint.setColor(Color.WHITE);
        float degree = 7.5f;
        int index = 0;
        for (int i = 0; i <= 30; i++) {

            if ((i * degree) % 22.5 == 0) {
                canvas.drawText(text[index++], innerRectF.left - innerRadianPaintWidth / 2 + textScale, this.getHeight() / 2, paint);
            }
            canvas.rotate(degree, this.getWidth() / 2, this.getHeight() / 2);
        }

        canvas.restore();
        canvas.rotate(8, this.getWidth() / 2, this.getHeight() / 2);
        paint.setTextSize(5 * density);
        float textWidth = paint.measureText("BETA");
        canvas.drawText("BETA", this.getWidth() / 2 - textWidth / 2, this.getHeight() / 2 - 20 * density, paint);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setTextSize(22 * density);
        textWidth = paint.measureText(limit);
        float textHeight = paint.ascent() - paint.descent();
        canvas.drawText(limit, this.getWidth() / 2 - textWidth / 2, this.getHeight() / 2, paint);


        paint.setTypeface(Typeface.DEFAULT);
        paint.setTextSize(17 * density);
        float recordHeight = (paint.ascent() - paint.descent());
        textWidth = paint.measureText(record);
        canvas.drawText(record, this.getWidth() / 2 - textWidth / 2, this.getHeight() / 2 - recordHeight, paint);
    }


    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
        Integer l = Integer.valueOf(limit);
        if (l > 600) {
            record = "信用很好";
        } else {
            record = "信用很差";
        }
        convertScale(l);
        postInvalidate();
    }


    private void convertScale(int limit) {

        if (limit <= 350) {

            dynamicScale = -1;
        } else if (350 < limit && limit < 700) {
            dynamicScale = 180;
        } else {
            dynamicScale = 198;
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        this.setMeasuredDimension(widthMode != MeasureSpec.EXACTLY ? DEFWIDTH : MeasureSpec.getSize(widthMeasureSpec),
                heightMode != MeasureSpec.EXACTLY ? DEFHEIGHT : MeasureSpec.getSize(heightMeasureSpec));
    }
}
