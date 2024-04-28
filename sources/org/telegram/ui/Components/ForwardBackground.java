package org.telegram.ui.Components;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.SharedConfig;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.CachedStaticLayout;
/* loaded from: classes3.dex */
public class ForwardBackground {
    public final ButtonBounce bounce;
    public float cx;
    public float cy;
    private Drawable rippleDrawable;
    private int rippleDrawableColor;
    private final View view;
    public final Path path = new Path();
    public final android.graphics.Rect bounds = new android.graphics.Rect();
    private final RectF r = new RectF();

    public ForwardBackground(View view) {
        this.view = view;
        this.bounce = new ButtonBounce(view, 0.8f, 1.4f);
    }

    public void set(CachedStaticLayout[] cachedStaticLayoutArr, boolean z) {
        float f;
        float f2;
        float dp;
        int dp2 = AndroidUtilities.dp(4.0f) + (((int) Theme.chat_forwardNamePaint.getTextSize()) * 2);
        float max = Math.max(0, Math.min(6, SharedConfig.bubbleRadius) - 1);
        float min = Math.min(9, SharedConfig.bubbleRadius);
        float min2 = Math.min(3, SharedConfig.bubbleRadius);
        float f3 = -AndroidUtilities.dp(((min / 9.0f) * 2.66f) + 4.0f);
        float f4 = -AndroidUtilities.dp(3.0f);
        float dp3 = dp2 + AndroidUtilities.dp(5.0f);
        float lineWidth = cachedStaticLayoutArr[0].layout.getLineWidth(0) + AndroidUtilities.dp(f);
        float lineWidth2 = cachedStaticLayoutArr[1].layout.getLineWidth(0) + AndroidUtilities.dp(f);
        this.path.rewind();
        if (!z) {
            max = SharedConfig.bubbleRadius / 2.0f;
        }
        float dp4 = AndroidUtilities.dp(max) * 2;
        this.r.set(f3, f4, f3 + dp4, dp4 + f4);
        this.path.arcTo(this.r, 180.0f, 90.0f);
        float f5 = lineWidth - lineWidth2;
        float max2 = Math.abs(f5) < ((float) AndroidUtilities.dp(min2 + min)) ? Math.max(lineWidth, lineWidth2) : lineWidth;
        if (Math.abs(f5) > AndroidUtilities.dp(f2)) {
            float dp5 = AndroidUtilities.dp(min2) * 2;
            if (lineWidth < lineWidth2) {
                float f6 = ((dp3 - f4) * 0.45f) + f4;
                dp = AndroidUtilities.dp(min) * 2;
                this.r.set(max2 - dp, f4, max2, f4 + dp);
                this.path.arcTo(this.r, 270.0f, 90.0f);
                this.r.set(lineWidth, f6 - dp5, dp5 + lineWidth, f6);
                this.path.arcTo(this.r, 180.0f, -90.0f);
                float f7 = lineWidth2 - (dp3 - f6);
                this.r.set(f7, f6, lineWidth2, dp3);
                this.path.arcTo(this.r, 270.0f, 90.0f);
                this.r.set(f7, f6, lineWidth2, dp3);
                this.path.arcTo(this.r, 0.0f, 90.0f);
            } else {
                float f8 = ((dp3 - f4) * 0.55f) + f4;
                float f9 = f8 - f4;
                this.r.set(max2 - f9, f4, max2, f8);
                this.path.arcTo(this.r, 270.0f, 90.0f);
                dp = AndroidUtilities.dp(min) * 2;
                this.r.set(lineWidth - f9, f4, lineWidth, f8);
                this.path.arcTo(this.r, 0.0f, 90.0f);
                this.r.set(lineWidth2, f8, lineWidth2 + dp5, dp5 + f8);
                this.path.arcTo(this.r, 270.0f, -90.0f);
                this.r.set(lineWidth2 - dp, dp3 - dp, lineWidth2, dp3);
                this.path.arcTo(this.r, 0.0f, 90.0f);
            }
        } else {
            dp = AndroidUtilities.dp(min) * 2;
            float f10 = max2 - dp;
            this.r.set(f10, f4, max2, f4 + dp);
            this.path.arcTo(this.r, 270.0f, 90.0f);
            this.r.set(f10, dp3 - dp, max2, dp3);
            this.path.arcTo(this.r, 0.0f, 90.0f);
        }
        this.r.set(f3, dp3 - dp, dp + f3, dp3);
        this.path.arcTo(this.r, 90.0f, 90.0f);
        this.path.close();
        this.bounds.set((int) f3, (int) f4, (int) Math.max(lineWidth, lineWidth2), (int) dp3);
    }

    public void setColor(int i) {
        if (this.rippleDrawableColor != i) {
            Drawable drawable = this.rippleDrawable;
            if (drawable == null) {
                this.rippleDrawable = Theme.createSelectorDrawable(i, 2);
            } else {
                Theme.setSelectorDrawableColor(drawable, i, true);
            }
            this.rippleDrawableColor = i;
        }
    }

    public void setPressed(boolean z) {
        setPressed(z, this.bounds.centerX(), this.bounds.centerY());
    }

    public void setPressed(boolean z, float f, float f2) {
        this.bounce.setPressed(z);
        Drawable drawable = this.rippleDrawable;
        if (drawable != null) {
            drawable.setState(z ? new int[]{16842910, 16842919} : new int[0]);
        }
        if (z) {
            this.cx = f;
            this.cy = f2;
            Drawable drawable2 = this.rippleDrawable;
            if (drawable2 != null && Build.VERSION.SDK_INT >= 21) {
                drawable2.setHotspot(f, f2);
            }
        }
        this.view.invalidate();
    }

    public void draw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(this.path);
        Drawable drawable = this.rippleDrawable;
        if (drawable != null) {
            drawable.setBounds(this.bounds);
            this.rippleDrawable.draw(canvas);
        }
        canvas.restore();
    }
}
