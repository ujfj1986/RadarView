package com.shjj.radarview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;

/**
 * Created by shjj on 15/5/15.
 * transform the coordinate 0,0 to width/2, height/2
 */
public class StageView extends View {
    private final String TAG = StageView.class.getSimpleName();
    private Adapter adapter;
    private int selectPosition = 0;

    public StageView(Context context) {
        super(context);
    }

    public StageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        canvas.save();
        canvas.translate(width/2, height/2);
        super.onDraw(canvas);
        canvas.restore();
    }


}
