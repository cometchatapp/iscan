package com.arteriatech.ss.msec.iscan.v4.ui;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class AnimatedDashedLineView extends View {
    private Paint paint;
    private Path path;
    private float phase = 0f;
    private ValueAnimator animator;

    public AnimatedDashedLineView(Context context) {
        super(context);
        init();
    }

    public AnimatedDashedLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AnimatedDashedLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20);

        path = new Path();

        animator = ValueAnimator.ofFloat(0, 40); // 40 = dash+gap
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(animation -> {
            phase = (float) animation.getAnimatedValue();
            invalidate(); // trigger onDraw
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Reset and define the rectangle path
        path.reset();
        path.addRect(0, 0, getWidth(), getHeight() , Path.Direction.CW);

        // Animate dash phase
        DashPathEffect dashEffect = new DashPathEffect(new float[]{20, 20}, phase);
        paint.setPathEffect(dashEffect);

        canvas.drawPath(path, paint);
    }
}
