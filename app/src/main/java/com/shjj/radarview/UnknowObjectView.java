package com.shjj.radarview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by shjj on 15/5/18.
 */
public class UnknowObjectView extends View implements View.OnTouchListener{
    private final String TAG = UnknowObjectView.class.getSimpleName();
    private UnknowObject unknowObject;
    private Paint paint;
    private final int HEADER_R = 20;
    private boolean isShowInfo = false;
    private UnknowObject.UnknowObjectObserver observer = new UnknowObject.UnknowObjectObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            UnknowObjectView.this.postInvalidate();
        }

        @Override
        public void onInvalied() {
            super.onInvalied();
        }
    };
    private float centerX, centerY;

    public UnknowObjectView(Context context) {
        super(context);
        initPaint();
    }

    public UnknowObjectView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public void setUnknowObject(UnknowObject obj) {
        if (this.unknowObject != null) {
            this.unknowObject.unregisterObserver(observer);
        }
        this.unknowObject = obj;
        this.unknowObject.registerObserver(observer);
    }

    protected void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(2);
    }

    public void setCoordinateCenter(float x, float y) {
        this.centerX = x;
        this.centerY = y;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (this.unknowObject == null) return;

        canvas.save();
        canvas.translate(centerX, centerY);

        float centerX = unknowObject.getCenterX();
        float centerY = unknowObject.getCenterY();

        //draw a circle, means the unknow object
        canvas.drawCircle(centerX, centerY, HEADER_R, paint);

        // draw a text, show unknow object id,
        // if user click the circle, show long information of unknow object
        String info = unknowObject.getId();
        if (isShowInfo) {
            info += " [deep: " +
                    unknowObject.getDeep() + "M, " +
                    "distance: " + unknowObject.getDistance() + "M]";
        }
        canvas.drawText(info, centerX + 50, centerY, paint);

        canvas.restore();
    }

    protected boolean touchHeader(float x, float y) {
        double powX = Math.pow(x - this.unknowObject.getCenterX(), 2);
        double powY = Math.pow(y - this.unknowObject.getCenterY(), 2);
        if (Math.sqrt(powX + powY) <= HEADER_R) {
            return true;
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (null == this.unknowObject) return false;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            if (touchHeader(x, y)) {
                this.isShowInfo = !this.isShowInfo;
                this.postInvalidate();
                return true;
            }
        }
        return false;
    }
}
