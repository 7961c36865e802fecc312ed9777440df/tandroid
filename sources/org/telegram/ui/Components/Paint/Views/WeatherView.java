package org.telegram.ui.Components.Paint.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.view.ViewGroup;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.Paint.Views.EntityView;
import org.telegram.ui.Components.Point;
import org.telegram.ui.Components.Rect;
import org.telegram.ui.Stories.recorder.Weather;
/* loaded from: classes3.dex */
public class WeatherView extends EntityView {
    private int currentColor;
    private int currentType;
    private boolean hasColor;
    public final LocationMarker marker;
    public Weather.State weather;

    @Override // org.telegram.ui.Components.Paint.Views.EntityView
    protected float getMaxScale() {
        return 1.5f;
    }

    @Override // org.telegram.ui.Components.Paint.Views.EntityView
    protected float getStickyPaddingLeft() {
        return this.marker.padx;
    }

    @Override // org.telegram.ui.Components.Paint.Views.EntityView
    protected float getStickyPaddingTop() {
        return this.marker.pady;
    }

    @Override // org.telegram.ui.Components.Paint.Views.EntityView
    protected float getStickyPaddingRight() {
        return this.marker.padx;
    }

    @Override // org.telegram.ui.Components.Paint.Views.EntityView
    protected float getStickyPaddingBottom() {
        return this.marker.pady;
    }

    public WeatherView(Context context, Point point, int i, Weather.State state, float f, int i2) {
        super(context, point);
        LocationMarker locationMarker = new LocationMarker(context, 1, f, 0);
        this.marker = locationMarker;
        locationMarker.setMaxWidth(i2);
        locationMarker.setType(0, this.currentColor);
        setWeather(i, state);
        addView(locationMarker, LayoutHelper.createFrame(-2, -2, 51));
        setClipChildren(false);
        setClipToPadding(false);
        updatePosition();
    }

    public void setWeather(int i, Weather.State state) {
        this.weather = state;
        String emoji = state.getEmoji();
        String temperature = state.getTemperature();
        this.marker.setCodeEmoji(i, emoji);
        this.marker.setText(temperature);
        updateSelectionView();
    }

    public void setMaxWidth(int i) {
        this.marker.setMaxWidth(i);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        updatePosition();
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        updatePosition();
    }

    public void setColor(int i) {
        this.hasColor = true;
        this.currentColor = i;
    }

    public boolean hasColor() {
        return this.hasColor;
    }

    public void setType(int i) {
        LocationMarker locationMarker = this.marker;
        this.currentType = i;
        locationMarker.setType(i, this.currentColor);
    }

    public int getTypesCount() {
        return this.marker.getTypesCount() - (!this.hasColor ? 1 : 0);
    }

    public int getColor() {
        return this.currentColor;
    }

    @Override // org.telegram.ui.Components.Paint.Views.EntityView
    public void setIsVideo(boolean z) {
        this.marker.setIsVideo(true);
    }

    public int getType() {
        return this.currentType;
    }

    @Override // org.telegram.ui.Components.Paint.Views.EntityView
    public Rect getSelectionBounds() {
        ViewGroup viewGroup = (ViewGroup) getParent();
        if (viewGroup == null) {
            return new Rect();
        }
        float scaleX = viewGroup.getScaleX();
        float measuredWidth = (getMeasuredWidth() * getScale()) + (AndroidUtilities.dp(64.0f) / scaleX);
        float measuredHeight = (getMeasuredHeight() * getScale()) + (AndroidUtilities.dp(64.0f) / scaleX);
        float positionX = (getPositionX() - (measuredWidth / 2.0f)) * scaleX;
        return new Rect(positionX, (getPositionY() - (measuredHeight / 2.0f)) * scaleX, ((measuredWidth * scaleX) + positionX) - positionX, measuredHeight * scaleX);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.telegram.ui.Components.Paint.Views.EntityView
    public TextViewSelectionView createSelectionView() {
        return new TextViewSelectionView(getContext());
    }

    /* loaded from: classes3.dex */
    public class TextViewSelectionView extends EntityView.SelectionView {
        private final Paint clearPaint;
        private Path path;

        public TextViewSelectionView(Context context) {
            super(context);
            Paint paint = new Paint(1);
            this.clearPaint = paint;
            this.path = new Path();
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }

        @Override // org.telegram.ui.Components.Paint.Views.EntityView.SelectionView
        protected int pointInsideHandle(float f, float f2) {
            float dp = AndroidUtilities.dp(19.5f);
            float dp2 = AndroidUtilities.dp(1.0f) + dp;
            float f3 = dp2 * 2.0f;
            float measuredWidth = getMeasuredWidth() - f3;
            float measuredHeight = ((getMeasuredHeight() - f3) / 2.0f) + dp2;
            if (f <= dp2 - dp || f2 <= measuredHeight - dp || f >= dp2 + dp || f2 >= measuredHeight + dp) {
                float f4 = dp2 + measuredWidth;
                return (f <= f4 - dp || f2 <= measuredHeight - dp || f >= f4 + dp || f2 >= measuredHeight + dp) ? 0 : 2;
            }
            return 1;
        }

        @Override // android.view.View
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            int saveCount = canvas.getSaveCount();
            float showAlpha = getShowAlpha();
            if (showAlpha <= 0.0f) {
                return;
            }
            if (showAlpha < 1.0f) {
                canvas.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), (int) (showAlpha * 255.0f), 31);
            }
            float dpf2 = AndroidUtilities.dpf2(5.66f);
            float dp = AndroidUtilities.dp(2.0f) + dpf2 + AndroidUtilities.dp(15.0f);
            float f = dp * 2.0f;
            float measuredWidth = getMeasuredWidth() - f;
            float measuredHeight = getMeasuredHeight() - f;
            RectF rectF = AndroidUtilities.rectTmp;
            float f2 = dp + measuredWidth;
            float f3 = dp + measuredHeight;
            rectF.set(dp, dp, f2, f3);
            float dp2 = AndroidUtilities.dp(12.0f);
            float min = Math.min(dp2, measuredWidth / 2.0f);
            float f4 = measuredHeight / 2.0f;
            float min2 = Math.min(dp2, f4);
            this.path.rewind();
            float f5 = min * 2.0f;
            float f6 = dp + f5;
            float f7 = 2.0f * min2;
            float f8 = dp + f7;
            rectF.set(dp, dp, f6, f8);
            this.path.arcTo(rectF, 180.0f, 90.0f);
            float f9 = f2 - f5;
            rectF.set(f9, dp, f2, f8);
            this.path.arcTo(rectF, 270.0f, 90.0f);
            canvas.drawPath(this.path, this.paint);
            this.path.rewind();
            float f10 = f3 - f7;
            rectF.set(dp, f10, f6, f3);
            this.path.arcTo(rectF, 180.0f, -90.0f);
            rectF.set(f9, f10, f2, f3);
            this.path.arcTo(rectF, 90.0f, -90.0f);
            canvas.drawPath(this.path, this.paint);
            float f11 = dp + f4;
            canvas.drawCircle(dp, f11, dpf2, this.dotStrokePaint);
            canvas.drawCircle(dp, f11, (dpf2 - AndroidUtilities.dp(1.0f)) + 1.0f, this.dotPaint);
            canvas.drawCircle(f2, f11, dpf2, this.dotStrokePaint);
            canvas.drawCircle(f2, f11, (dpf2 - AndroidUtilities.dp(1.0f)) + 1.0f, this.dotPaint);
            canvas.saveLayerAlpha(0.0f, 0.0f, getWidth(), getHeight(), 255, 31);
            float f12 = dp + min2;
            float f13 = f3 - min2;
            canvas.drawLine(dp, f12, dp, f13, this.paint);
            canvas.drawLine(f2, f12, f2, f13, this.paint);
            canvas.drawCircle(f2, f11, (AndroidUtilities.dp(1.0f) + dpf2) - 1.0f, this.clearPaint);
            canvas.drawCircle(dp, f11, (dpf2 + AndroidUtilities.dp(1.0f)) - 1.0f, this.clearPaint);
            canvas.restoreToCount(saveCount);
        }
    }
}