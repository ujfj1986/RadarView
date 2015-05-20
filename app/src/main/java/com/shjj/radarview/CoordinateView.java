package com.shjj.radarview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by shjj on 15/5/13.
 */
public class CoordinateView extends View {
    private final String TAG = CoordinateView.class.getSimpleName();
    private Paint linePaint;
    private Paint circlePaint;
    private int centerX; //坐标原点位置
    private int centerY;
    private int radius = 0;   //坐标最大长度
    private String showLength = "";
    public CoordinateView(Context context) {
        super(context);
        initPaint();
    }

    public CoordinateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setCircleARGB(int a, int r, int g, int b) {
        this.circlePaint.setARGB(a, r, g, b);
    }

    public void setLineARGB(int a, int r, int g, int b) {
        this.linePaint.setARGB(a, r, g, b);
    }

    private void initPaint() {
        this.linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        //linePaint.setARGB(150, 50, 57, 74);
        linePaint.setColor(Color.GREEN);
        linePaint.setStrokeWidth(2);

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);//圆形画笔，设置Paint为抗锯齿
        //circlePaint.setARGB(255, 50, 57, 74);//设置透明度和RGB颜色
        circlePaint.setColor(Color.GREEN);
        circlePaint.setStrokeWidth(3);//轮廓宽度
        circlePaint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onMeasure(int measureSpecWidth, int measureSpecHeight) {
        Log.d(TAG, "in onMeasure");
        int width = MeasureSpec.getSize(measureSpecWidth);
        int height = MeasureSpec.getSize(measureSpecHeight);
        int d = (width > height) ? height : width;

        setMeasuredDimension(d, d);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "in onDraw");
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        this.centerX = width / 2;
        this.centerY = height / 2;
        if (this.radius <= 0) radius = (width / 2) - 10 ;

        canvas.save();
        canvas.translate(this.centerX, this.centerY);

        //绘制3个同心圆形
        circlePaint.setStrokeWidth(5);
        canvas.drawCircle(0,0, radius, circlePaint);
        circlePaint.setStrokeWidth(3);
        canvas.drawCircle(0, 0, radius * 2 / 3, circlePaint);
        circlePaint.setStrokeWidth(2);
        canvas.drawCircle(0, 0, radius / 3, circlePaint);

        //绘制X轴
        canvas.drawLine(-radius, 0, radius, 0, linePaint);
        canvas.drawLine(0, -radius, 0, radius, linePaint);
        canvas.restore();
    }

}
