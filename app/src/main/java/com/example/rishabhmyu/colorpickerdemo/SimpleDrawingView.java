package com.example.rishabhmyu.colorpickerdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.Iterator;

public class SimpleDrawingView extends View {
    private static final float TOUCH_TOLERANCE = 4.0f;
    private Bitmap bitmap;
    private Paint bitmapPaint = new Paint(4);
    private Canvas canvas = new Canvas();
    int color = -16777216;
    private float mX;
    private float mY;
    private Paint paint = new Paint();
    private Path path = new Path();
    private ArrayList<PathPoints> paths = new ArrayList();

    class PathPoints {
        private int color;
        private boolean isTextToDraw;
        private Path path;
        private String textToDraw;
        private int x;
        private int y;

        public PathPoints(Path path, int color, boolean isTextToDraw) {
            this.path = path;
            this.color = color;
            this.isTextToDraw = isTextToDraw;
        }

        public PathPoints(int color, String textToDraw, boolean isTextToDraw, int x, int y) {
            this.color = color;
            this.textToDraw = textToDraw;
            this.isTextToDraw = isTextToDraw;
            this.x = x;
            this.y = y;
        }

        public Path getPath() {
            return this.path;
        }

        public void setPath(Path path) {
            this.path = path;
        }

        public int getColor() {
            return this.color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public String getTextToDraw() {
            return this.textToDraw;
        }

        public void setTextToDraw(String textToDraw) {
            this.textToDraw = textToDraw;
        }

        public boolean isTextToDraw() {
            return this.isTextToDraw;
        }

        public void setTextToDraw(boolean isTextToDraw) {
            this.isTextToDraw = isTextToDraw;
        }

        public int getX() {
            return this.x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return this.y;
        }

        public void setY(int y) {
            this.y = y;
        }
    }

    public SimpleDrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.paint.setAntiAlias(true);
        this.paint.setDither(true);
        this.paint.setColor(this.color);
        this.paint.setStyle(Style.STROKE);
        this.paint.setStrokeJoin(Join.ROUND);
        this.paint.setStrokeCap(Cap.ROUND);
        this.paint.setStrokeWidth(9.0f);
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.bitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        float xscale = ((float) w) / ((float) this.bitmap.getWidth());
        float yscale = ((float) h) / ((float) this.bitmap.getHeight());
        if (xscale > yscale) {
            xscale = yscale;
        }
        float newx = ((float) w) * xscale;
        float newy = ((float) h) * xscale;
        this.bitmap = Bitmap.createScaledBitmap(this.bitmap, getWidth(), getHeight(), true);
    }

    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(this.bitmap, 0.0f, 0.0f, this.bitmapPaint);
        Iterator it = this.paths.iterator();
        while (it.hasNext()) {
            PathPoints p = (PathPoints) it.next();
            this.paint.setColor(p.getColor());
            canvas.drawPath(p.getPath(), this.paint);
        }
        if (this.paths.size() == 0) {
            this.path = new Path();
            this.paths.add(new PathPoints(this.path, this.color, false));
        }
    }

    private void touchStart(float x, float y) {
        this.path.reset();
        this.path.moveTo(x, y);
        this.mX = x;
        this.mY = y;
    }

    private void touchMove(float x, float y) {
        float dx = Math.abs(x - this.mX);
        float dy = Math.abs(y - this.mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            this.path.quadTo(this.mX, this.mY, (this.mX + x) / 2.0f, (this.mY + y) / 2.0f);
            this.mX = x;
            this.mY = y;
        }
    }

    private void touchUp() {
        this.path.lineTo(this.mX, this.mY);
        this.canvas.drawPath(this.path, this.paint);
        this.path = new Path();
        this.paths.add(new PathPoints(this.path, this.color, false));
    }

    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: /*0*/
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP /*1*/:
                touchUp();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE: /*2*/
                touchMove(x, y);
                invalidate();
                break;
        }
        return true;
    }

    public Bitmap getBitmap() {
        setDrawingCacheEnabled(true);
        buildDrawingCache();
        Bitmap bmp = Bitmap.createBitmap(getDrawingCache());
        setDrawingCacheEnabled(false);
        return bmp;
    }

    public void clear() {
        this.bitmap.eraseColor(-1);
        invalidate();
        System.gc();
    }

    public void setPathColor(int color) {
        this.color = color;
        this.paint.setColor(color);
        ((PathPoints) this.paths.get(this.paths.size() - 1)).setColor(color);
    }

    public void onClickUndo() {
        if (this.paths.size() > 0) {
            this.paths.remove(this.paths.size() - 2);
            invalidate();
        }
    }
}
