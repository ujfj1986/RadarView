package com.shjj.radarview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shjj on 15/5/18.
 */
public class UnknowObject {
    private final String TAG = UnknowObject.class.getSimpleName();
    private String id; //标识物体
    private int centerX;
    private int centerY;
    private List<RadarScanView.Point> path; //存储物体路径
    private UnknowObjectObserver observer;
    private float deep;
    private float distance;
    private float degree;

    public UnknowObject(final String id) {
        this.id = id;
        this.centerX = 0;
        this.centerY = 0;
        this.path = new ArrayList<>(10);
        this.observer = null;
    }

    public void setCenter(int x, int y) {
        this.centerX = x;
        this.centerY = y;
    }

    public int getCenterX() {
        return this.centerX;
    }

    public int getCenterY() {
        return this.centerY;
    }

    public List<RadarScanView.Point> getPath() {
        return this.path;
    }

    public String getId() {
        return this.id;
    }

    public static abstract class UnknowObjectObserver {
        public void onChanged() {

        }

        public void onInvalied() {

        }
    }

    public void registerObserver(UnknowObjectObserver observer) {
        this.observer = observer;
    }

    public void unregisterObserver(UnknowObjectObserver observer) {
        this.observer = null;
    }

    public float getDeep() {
        return deep;
    }

    public float getDistance() {
        return distance;
    }
}
