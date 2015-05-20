package com.shjj.radarview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.List;

/**
 * Created by shjj on 15/5/14.
 */
public class RadarView extends FrameLayout implements Radar.IRadarView{
    private final String TAG = RadarView.class.getSimpleName();
    private final RadarScanView scanView;
    private RelativeLayout stage;
    private float centerX;
    private float centerY;
    private Radar radar;

    public RadarView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.radar_view_layout, this);
        scanView = (RadarScanView) findViewById(R.id.radarScanView);
        stage = (RelativeLayout) findViewById(R.id.stage);
    }

    public RadarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.radar_view_layout, this);
        scanView = (RadarScanView) findViewById(R.id.radarScanView);
        stage = (RelativeLayout) findViewById(R.id.stage);
    }

//    @Override
//    protected void onMeasure(int measureSpecWidth, int measureSpecHeight) {
//        int width = MeasureSpec.getSize(measureSpecWidth);
//        int height = MeasureSpec.getSize(measureSpecHeight);
//        int d = (width > height) ? height : width;
//        setMeasuredDimension(d, d);
//    }

    public void startScan() {
        scanView.startScan();
    }

    public void stopScan() {
        scanView.stopScan();
    }

    @Override
    public void onDataChanged() {

    }

    public void uploadUnknownObjects(List<UnknowObject> objects) {

    }

    public void clearUnknownObjects() {

    }
}
