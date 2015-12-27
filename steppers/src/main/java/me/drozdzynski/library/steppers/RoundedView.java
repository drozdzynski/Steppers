package me.drozdzynski.library.steppers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;

public class RoundedView extends View {

    protected RoundedView(Context context) {
        super(context);
    }

    protected RoundedView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected RoundedView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    private int color = Color.parseColor("#BDBDBD");
    private String text = null;

    public void setCircleColor(int color) {
        this.color = color;
        invalidate();
    }

    public void setCircleAccentColor(){
        final TypedValue value = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.colorAccent, value, true);
        if(value != null) color = value.data;
        else ContextCompat.getColor(getContext(), R.color.circle_color_default_blue);
        invalidate();
    }

    public void setCircleGrayColor(){
        color = Color.parseColor("#BDBDBD");
        invalidate();
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getBackgroundColor(this) != 0) color = getBackgroundColor(this);
        //setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent));

        Paint paint = new Paint(0);

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);

        paint.setColor(color);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2,
                getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        if(text != null) drawText(canvas);
    }

    private int getBackgroundColor(View view) {
        ColorDrawable drawable = (ColorDrawable) view.getBackground();
        if(drawable != null) {
            if (Build.VERSION.SDK_INT >= 11) {
                return drawable.getColor();
            }
            try {
                Field field = drawable.getClass().getDeclaredField("mState");
                field.setAccessible(true);
                Object object = field.get(drawable);
                field = object.getClass().getDeclaredField("mUseColor");
                field.setAccessible(true);
                return field.getInt(object);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return 0;
    }

    private void drawText(Canvas canvas) {
        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextSize(getResources().getDimension(R.dimen.item_circle_text_size));

        Rect areaRect = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());

        RectF bounds = new RectF(areaRect);

        bounds.right = mPaint.measureText(text, 0, text.length());

        bounds.bottom = mPaint.descent() - mPaint.ascent();

        bounds.left += (areaRect.width() - bounds.right) / 2.0f;
        bounds.top += (areaRect.height() - bounds.bottom) / 2.0f;

        mPaint.setColor(Color.WHITE);
        canvas.drawText(text, bounds.left, bounds.top - mPaint.ascent(), mPaint);
    }

}