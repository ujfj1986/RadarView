package com.shjj.radarview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shjj on 15/5/13.
 */
public class RadarScanView extends View {

    private final String TAG = RadarScanView.class.getSimpleName();

    private Paint circlePaint;//圆形画笔
    private Paint linePaint;//线形画笔
    private Paint sweepPaint;//扫描画笔
    private Paint textPaint;//文字画笔
    private Paint backgroundPaint; //背景画笔
    private SweepGradient sweepGradient;//扇形渐变Shader
    private int degree = 0;
    private float margin = 10f;
    private float maxValue = 40f;
    private float radarCenterX = 0f;
    private float radarCenterY = 0f;
    private float radarRadius = 0f;
    private float DEGREE_INC = 5.0f;
    private ScanThread scanThread;
    private int scanTime = 0;
    private List<Point> points;
    private Context context;

    public RadarScanView(Context context){
        super(context);
        this.context = context;
        initPaint();
        this.scanThread = new ScanThread(this);
        this.points = new ArrayList<>();
    }

    public RadarScanView(Context context, AttributeSet att){
        super(context, att);
        this.context = context;
        initPaint();
        this.scanThread = new ScanThread(this);
        this.points = new ArrayList<>();
    }

    public void startScan() {
        this.scanThread.isRun = true;
        this.scanThread.start();
    }

    public void stopScan() {
        this.scanThread.isRun = false;
    }

    public void updatePoints(List<Point> points) {
        synchronized (points) {
            this.points = points;
        }
    }

    public void setScanTime(int s) {
        if (s < 0) throw new InvalidParameterException();
        if (s == 0) {
            this.DEGREE_INC = 5.0f;
            this.scanTime = 0;
        } else {
            this.scanTime = s;
            this.DEGREE_INC = 360 / s * 10;
        }
    }

    //draw background
    private void drawBackground(Canvas canvas) {
        canvas.save();
        canvas.translate(this.radarCenterX, this.radarCenterY);
        canvas.drawCircle(0,0, this.radarRadius, backgroundPaint);
        canvas.restore();
    }

    //draw max value
    protected void drawMaxValue(Canvas canvas) {
        canvas.save();
        canvas.translate(this.radarCenterX, this.radarCenterY);
        String v = Float.toString(this.maxValue);
        canvas.drawText(v, -this.radarRadius + margin, 0, this.textPaint);// left
        canvas.drawText(v, this.radarRadius - margin - (v.length() * 10), 0, this.textPaint); //right
        canvas.drawText(v, -(v.length() * 5), this.radarRadius - 30, this.textPaint);//up
        canvas.drawText(v, -(v.length() * 5), - this.radarRadius + 5, this.textPaint); //down
        /*canvas.drawCircle(-this.radarRadius + 10, 0, 20, this.textPaint);
        canvas.drawCircle(this.radarRadius - 10, 0, 20, this.textPaint);
        canvas.drawCircle(-5, this.radarRadius-5, 20, this.textPaint);
        canvas.drawCircle(-5, -this.radarRadius + 5, 20, this.textPaint);*/
        canvas.restore();
    }

    /**
     * @param
     * @return void
     * @Description //初始化定义的画笔
     */
    private void initPaint(){
        Resources r = this.getResources();

        /*circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);//圆形画笔，设置Paint为抗锯齿
        circlePaint.setARGB(255, 50, 57, 74);//设置透明度和RGB颜色
        circlePaint.setStrokeWidth(3);//轮廓宽度
        circlePaint.setStyle(Paint.Style.STROKE);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);//线性画笔
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setARGB(150, 50, 57, 74);
        linePaint.setStrokeWidth(2);*/

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG); //文字画笔
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(20.0f);

        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(Color.BLACK);

        sweepPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//雷达Shader画笔
        sweepPaint.setStrokeCap(Paint.Cap.ROUND);
        sweepPaint.setStrokeWidth(4);
        sweepGradient = new SweepGradient(0,0,r.getColor(R.color.start_color),r.getColor(R.color.end_color));
        sweepPaint.setShader(sweepGradient);

    }

    @Override
    protected void onMeasure(int wMeasureSpec, int hMeasureSpec){
        Log.d(TAG, "in onMeasure");
        int width = MeasureSpec.getSize(wMeasureSpec);
        int height = MeasureSpec.getSize(hMeasureSpec);
        int d = (width>=height)?height:width; //获取最短的边作为直径
        setMeasuredDimension(d, d); //重写测量方法，保证获得的画布是正方形
    }

    protected void drawRadar(Canvas canvas) {
        float radius = this.radarRadius;
        float pointX = this.radarCenterX;
        float pointY = this.radarCenterY;
        canvas.drawCircle(pointX, pointY, radius, circlePaint);//绘制3个嵌套同心圆形，使用circlePaint画笔
        circlePaint.setAlpha(100);//降低内部圆形的透明度
        circlePaint.setStrokeWidth(2);//轮廓宽度
        canvas.drawCircle(pointX, pointY, radius*2/3, circlePaint);
        canvas.drawCircle(pointX, pointY, radius/3, circlePaint);

        canvas.drawLine(pointX, pointY - radius, pointX, pointY + radius, linePaint);//绘制十字分割线 ， 竖线
        canvas.drawLine(pointX - radius, pointY, pointX + radius, pointY, linePaint);

        canvas.save();//保存Canvas坐标原点
        canvas.translate(pointX - radius, pointY);//设置相对横线起始坐标

        float s = radius/12f;//刻度间距
        float minlength = s/2;//短刻度线长度
        float maxlength = s;//长刻度线长度

        for(int i=0;i<24;i++){
            float fromX,toX;
            fromX=toX=s*i;
            if(i%4!=0){//与圆形重叠处不画刻度
                if(i%2!=0){
                    canvas.drawLine(fromX,-minlength,toX,minlength, linePaint);//绘制X轴短刻度
                }else{
                    canvas.drawLine(fromX,-maxlength,toX,maxlength, linePaint);//绘制X轴长刻度
                }
            }
        }

        canvas.restore();
        canvas.save();
        canvas.translate(pointX, pointY - radius);//设置相对竖线起始坐标
        for(int i=0;i<24;i++){
            float fromY,toY;
            fromY=toY=s*i;
            if(i%4!=0){
                if(i%2!=0){
                    canvas.drawLine(-minlength,fromY,minlength,toY, linePaint);//绘制Y短轴刻度
                }else{
                    canvas.drawLine(-maxlength,fromY,maxlength,toY, linePaint);//绘制Y长轴刻度
                }
            }
        }
        canvas.restore();
    }

    @Override
    protected void onDraw(Canvas canvas){
        Log.d(TAG, "in onDraw");
        int Width = getMeasuredWidth();////计算控件的中心位置
        int Height = getMeasuredHeight();
        int pointX =  Width/2;//获得圆心坐标
        int pointY = Height/2;
        this.radarCenterX = pointX;
        this.radarCenterY = pointY;

        int radius = (pointX>=pointY) ? pointY : pointX;//设置半径
        this.radarRadius = radius - margin;

        drawBackground(canvas);
        drawScanLine(canvas);
        /*drawRadar(canvas);
        drawMaxValue(canvas);
        drawPoints(canvas);*/
    }

    protected void drawScanLine(Canvas canvas) {
        canvas.save();//保存Canvas坐标原点

        degree += DEGREE_INC;//扫描旋转增量度数
        canvas.translate(this.radarCenterX, this.radarCenterY);//设置旋转的原点
        canvas.rotate(270 + degree);
        canvas.drawCircle(0, 0, this.radarRadius, sweepPaint);//绘制扫描区域
        canvas.restore();
    }

    class Point {
        public float x;
        public float y;
        public String info;
        public Point(float x, float y, String info) {
            this.x = x;
            this.y = y;
            this.info = info;
        }
    }
    protected void drawPoints(Canvas canvas) {
        canvas.save();
        canvas.translate(this.radarCenterX, this.radarCenterY);
        for (Point p : this.points) {

        }
        canvas.restore();
    }

    class ScanThread extends Thread {
        private View holder;
        public boolean isRun = true;
        public ScanThread(View holder) {
            this.holder = holder;
        }

        @Override
        public void run() {
            while(isRun) {
                try {
                    this.holder.postInvalidate();
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
