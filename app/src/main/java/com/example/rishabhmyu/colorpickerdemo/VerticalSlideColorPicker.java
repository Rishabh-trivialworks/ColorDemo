package com.example.rishabhmyu.colorpickerdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class VerticalSlideColorPicker extends View {
    private Bitmap bitmap;
    private int borderColor;
    private float borderWidth;
    private boolean cacheBitmap = true;
    private int centerX;
    private RectF colorPickerBody;
    private float colorPickerRadius;
    private int[] colors;
    private OnColorChangeListener onColorChangeListener;
    private Paint paint;
    private Path path;
    private float selectorYPos;
    private Paint strokePaint;
    private int viewHeight;
    private int viewWidth;

    public interface OnColorChangeListener {
        void onColorChange(int i);
    }

    public VerticalSlideColorPicker(Context context) {
        super(context);
        init();
    }

    public VerticalSlideColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.VerticalSlideColorPicker, 0, 0);
        try {
            this.borderColor = a.getColor(R.styleable.VerticalSlideColorPicker_borderColor, -1);
            this.borderWidth = a.getDimension(R.styleable.VerticalSlideColorPicker_borderWidth, 10.0f);
            this.colors = a.getResources().getIntArray(a.getResourceId(R.styleable.VerticalSlideColorPicker_colors, R.array.default_colors));
            init();
        } finally {
            a.recycle();
        }
    }

    public VerticalSlideColorPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("NewApi")
    public VerticalSlideColorPicker(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        this.paint = new Paint();
        this.paint.setStyle(Style.FILL);
        this.paint.setAntiAlias(true);
        this.path = new Path();
        this.strokePaint = new Paint();
        this.strokePaint.setStyle(Style.STROKE);
        this.strokePaint.setColor(this.borderColor);
        this.strokePaint.setAntiAlias(true);
        this.strokePaint.setStrokeWidth(this.borderWidth);
        setDrawingCacheEnabled(true);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.path.addCircle((float) this.centerX, this.borderWidth + this.colorPickerRadius, this.colorPickerRadius, Direction.CW);
        this.path.addRect(this.colorPickerBody, Direction.CW);
        this.path.addCircle((float) this.centerX, ((float) this.viewHeight) - (this.borderWidth + this.colorPickerRadius), this.colorPickerRadius, Direction.CW);
        canvas.drawPath(this.path, this.strokePaint);
        canvas.drawPath(this.path, this.paint);
        if (this.cacheBitmap) {
            this.bitmap = getDrawingCache();
            this.cacheBitmap = false;
            invalidate();
            return;
        }
        canvas.drawLine(this.colorPickerBody.left, this.selectorYPos, this.colorPickerBody.right, this.selectorYPos, this.strokePaint);
    }

    public boolean onTouchEvent(MotionEvent event) {
        this.selectorYPos = Math.max(this.colorPickerBody.top, Math.min(event.getY(), this.colorPickerBody.bottom));
        int selectedColor = this.bitmap.getPixel(this.viewWidth / 2, (int) this.selectorYPos);
        if (this.onColorChangeListener != null) {
            this.onColorChangeListener.onColorChange(selectedColor);
        }
        invalidate();
        return true;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.viewWidth = w;
        this.viewHeight = h;
        this.centerX = this.viewWidth / 2;
        this.colorPickerRadius = ((float) (this.viewWidth / 2)) - this.borderWidth;
        this.colorPickerBody = new RectF(((float) this.centerX) - this.colorPickerRadius, this.borderWidth + this.colorPickerRadius, ((float) this.centerX) + this.colorPickerRadius, ((float) this.viewHeight) - (this.borderWidth + this.colorPickerRadius));
        this.paint.setShader(new LinearGradient(0.0f, this.colorPickerBody.top, 0.0f, this.colorPickerBody.bottom, this.colors, null, TileMode.CLAMP));
        resetToDefault();
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        invalidate();
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
        invalidate();
    }

    public void setColors(int[] colors) {
        this.colors = colors;
        this.cacheBitmap = true;
        invalidate();
    }

    public void resetToDefault() {
        this.selectorYPos = this.borderWidth + this.colorPickerRadius;
        if (this.onColorChangeListener != null) {
            this.onColorChangeListener.onColorChange(0);
        }
        invalidate();
    }

    public void setOnColorChangeListener(OnColorChangeListener onColorChangeListener) {
        this.onColorChangeListener = onColorChangeListener;
    }
}
