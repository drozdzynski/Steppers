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
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.lang.reflect.Field;

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
    private boolean checked = false;

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

    public void setChecked(boolean checked){
        this.checked = checked;
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

        if(text != null && !checked) drawText(canvas);
        if(checked) drawChecked(canvas);
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
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(getResources().getDimension(R.dimen.item_circle_text_size));

        Rect areaRect = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());

        RectF bounds = new RectF(areaRect);

        bounds.right = paint.measureText(text, 0, text.length());

        bounds.bottom = paint.descent() - paint.ascent();

        bounds.left += (areaRect.width() - bounds.right) / 2.0f;
        bounds.top += (areaRect.height() - bounds.bottom) / 2.0f;

        paint.setColor(Color.WHITE);
        canvas.drawText(text, bounds.left, bounds.top - paint.ascent(), paint);
    }

    private void drawChecked(Canvas canvas) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_check);

        float posX = (canvas.getWidth()  - bitmap.getWidth()) / 2;

        float posY = (canvas.getHeight() - bitmap.getHeight()) / 2;

        canvas.drawBitmap(bitmap, posX, posY, paint);
    }

}