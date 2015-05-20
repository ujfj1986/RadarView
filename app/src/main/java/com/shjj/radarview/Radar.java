package com.shjj.radarview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shjj on 15/5/20.
 */
public class Radar {
    private final String TAG = Radar.class.getSimpleName();
    private IRadarView view;
    private List<UnknowObject> objects; // store unknown object that are scanned
    private float scanTime;

    public Radar() {
        this.view = null;
        this.objects = new ArrayList<>();
        this.scanTime = 0f;
    }

    public static interface IRadarView {
        public void startScan();
        public void stopScan();
        public void onDataChanged(); //on unknown objects data chenged
    }

    public void startScan() {
        //TODO: scan data and start scan view
    }

    public void stopScan() {
        //TODO: stop scan data and stop scan view
    }

    public void setView(IRadarView view) {
        //TODO: update show view
    }

    public void setScanTime(float time) {
        //TODO: set scan time to device
    }

    public List<UnknowObject> getUnknownObjects() {
        return this.objects;
    }
}
