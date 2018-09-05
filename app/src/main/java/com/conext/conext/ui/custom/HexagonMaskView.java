package com.conext.conext.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by Ashith VL on 7/13/2017.
 */

public class HexagonMaskView extends android.support.v7.widget.AppCompatImageView {

    private Path hexagonPath;
    private Path hexagonBorderPath;
    private Paint mBorderPaint;

    public HexagonMaskView(Context context) {
        super(context);
        init();
    }

    public HexagonMaskView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HexagonMaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.hexagonPath = new Path();
        this.hexagonBorderPath = new Path();

        this.mBorderPaint = new Paint();
        this.mBorderPaint.setColor(Color.WHITE);
        this.mBorderPaint.setStrokeCap(Paint.Cap.ROUND);
        this.mBorderPaint.setStrokeWidth(17f);
        this.mBorderPaint.setStyle(Paint.Style.STROKE);

        this.mBorderPaint.setDither(true);        // set to STOKE
        this.mBorderPaint.setStrokeJoin(Paint.Join.ROUND);    // set the join to round you want
        this.mBorderPaint.setPathEffect(new CornerPathEffect(10));   // set the path effect when they join.
        this.mBorderPaint.setAntiAlias(true);


    }

    public void setRadius(float radius) {
        calculatePath(radius);
    }

    public void setBorderColor(int color) {
        this.mBorderPaint.setColor(color);
        invalidate();
    }

    private void calculatePath(float radius) {
        float halfRadius = radius / 2f;
        float triangleHeight = (float) (Math.sqrt(3.0) * halfRadius);
        float centerX = getMeasuredWidth() / 2f;
        float centerY = getMeasuredHeight() / 2f;

        this.hexagonPath.reset();

        this.hexagonPath.moveTo(centerX, centerY + radius);
        this.hexagonPath.lineTo(centerX - triangleHeight, centerY + halfRadius);

//        this.hexagonPath.addArc(new RectF(centerX - triangleHeight, centerY + halfRadius - 3f, centerX - triangleHeight + 3f,
//                centerY + halfRadius + 3f), 180, 100);
//
//        Log.e("centerX", "" + centerX);
//        Log.e("centerY + radius", "" + centerY + " " + radius);
//        Log.e("centerX - triangHeight", "" + centerX + " " + triangleHeight);
//        Log.e("centerY + halfRadius", "" + centerY + " " + halfRadius);

//        float rad = 20;
//        RectF arc = new RectF(centerX - triangleHeight - 20f - rad, centerY + halfRadius - 20f - rad, 0f - rad, 0f - rad);
//        int startAngle = (int) (180 / Math.PI * Math.atan2(centerY + halfRadius - 20f - 0f, centerX - triangleHeight - 20f - 0f));
//        this.hexagonPath.arcTo(arc, startAngle, -(float) 90, true);


        this.hexagonPath.lineTo(centerX - triangleHeight, centerY - halfRadius);


        this.hexagonPath.lineTo(centerX, centerY - radius);
        this.hexagonPath.lineTo(centerX + triangleHeight, centerY - halfRadius);
        this.hexagonPath.lineTo(centerX + triangleHeight, centerY + halfRadius);
        this.hexagonPath.close();

        float radiusBorder = radius - 5f;
        float halfRadiusBorder = radiusBorder / 2f;
        float triangleBorderHeight = (float) (Math.sqrt(3.0) * halfRadiusBorder);

        this.hexagonBorderPath.reset();
        this.hexagonBorderPath.moveTo(centerX, centerY + radiusBorder);
        this.hexagonBorderPath.lineTo(centerX - triangleBorderHeight, centerY + halfRadiusBorder);
        this.hexagonBorderPath.lineTo(centerX - triangleBorderHeight, centerY - halfRadiusBorder);
        this.hexagonBorderPath.lineTo(centerX, centerY - radiusBorder);
        this.hexagonBorderPath.lineTo(centerX + triangleBorderHeight, centerY - halfRadiusBorder);
        this.hexagonBorderPath.lineTo(centerX + triangleBorderHeight, centerY + halfRadiusBorder);

        //   this.hexagonBorderPath.addRoundRect(centerX + triangleBorderHeight, centerY + halfRadiusBorder);

        this.hexagonBorderPath.close();
        invalidate();
    }

    @Override
    public void onDraw(Canvas c) {
        c.drawPath(hexagonBorderPath, mBorderPaint);
        c.clipPath(hexagonPath);
        c.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        super.onDraw(c);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
        calculatePath(Math.min(width / 2f, height / 2f) - 10f);
    }
}